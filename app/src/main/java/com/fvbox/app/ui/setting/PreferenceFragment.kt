package com.fvbox.app.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.preference.*
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.fvbox.R
import com.fvbox.app.contract.LaunchSearchActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *
 * @Description: preference封装
 * @Author: Jack
 * @CreateDate: 2022/6/1 22:25
 */
abstract class PreferenceFragment : PreferenceFragmentCompat() {

    abstract fun getSettingTypeList(): Array<BaseBoxPreference>

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val screen = preferenceManager.createPreferenceScreen(requireContext())
        preferenceScreen = screen
        initData()
        loadData()
    }

    fun loadData() {
        lifecycleScope.launch(Dispatchers.IO) {
            preferenceScreen.removeAll()
            loadSubScreen(preferenceScreen)
        }
    }

    open fun initData() {

    }

    private fun loadSubScreen(screen: PreferenceScreen) {
        getSettingTypeList().forEach {
            val preference = when (it) {
                is TitleBoxPreference -> {
                    initTitlePreference(it)
                }

                is SwitchBoxPreference -> {
                    initSwitchPreference(it)
                }

                is EditBoxPreference -> {
                    initEditPreference(it)
                }

                is ClickPreference -> {
                    initClickPreference(it)
                }

                is SingleChoicePreference -> {
                    initSingleChoicePreference(it)
                }
            }

            screen.addPreference(preference)
        }
    }

    private fun initClickPreference(setting: ClickPreference): Preference {
        return Preference(requireContext()).apply {
            title = safeGetString(setting.title)
            key = safeGetString(setting.title)
            setOnPreferenceClickListener {
                setting.click.invoke()
                true
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initEditPreference(setting: EditBoxPreference): Preference {

        return Preference(requireContext()).apply {
            summary = setting.valueDelegate.get()
            title = safeGetString(setting.title)
            key = safeGetString(setting.title)
            setOnPreferenceClickListener {
                MaterialDialog(requireContext()).show {
                    title(text = safeGetString(setting.title))
                    input(
                        waitForPositiveButton = true,
                        prefill = setting.valueDelegate.get()
                    ) { _, charSequence ->
                        setting.valueDelegate.set(charSequence.toString())
                        summary = charSequence
                    }
                    negativeButton(R.string.cancel)
                    positiveButton(R.string.done)
                }
                false
            }
        }
    }

    private fun initSwitchPreference(setting: SwitchBoxPreference): Preference {
        return SwitchPreferenceCompat(requireContext()).apply {
            title = safeGetString(setting.title)
            key = safeGetString(setting.title)
            summary = safeGetString(setting.subTitle)
            isChecked = setting.valueDelegate.get()

            setOnPreferenceChangeListener { _, newValue ->
                setting.valueDelegate.set(newValue == true)
                true
            }
        }
    }

    private fun initTitlePreference(setting: TitleBoxPreference): Preference {
        return PreferenceCategory(requireContext()).apply {
            title = getString(setting.title)
            key = safeGetString(setting.title)
        }
    }

    @SuppressLint("CheckResult")
    private fun initSingleChoicePreference(setting: SingleChoicePreference): Preference {
        return Preference(requireContext()).apply {
            val mTitle = safeGetString(setting.title)
            title = mTitle
            key = mTitle
            summary = setting.valueDelegate.get()

            setOnPreferenceClickListener {
                launchSearch.launch(setting)
                true
            }
        }
    }

    private val launchSearch =
        registerForActivityResult(LaunchSearchActivity()) {
            if (it == null) {
                return@registerForActivityResult
            }

            val key = safeGetString(it.first)
            this.findPreference<Preference>(key)?.let { preference ->
                preference.summary = it.second
            }
        }


    private fun safeGetString(id: Int): String {
        return try {
            getString(id)
        } catch (e: Exception) {
            ""
        }
    }


}
