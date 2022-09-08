package com.fvbox.app.ui.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.fvbox.data.BoxRepository

/**
 *
 * @description: 应用安装卸载广播
 * @author: Jack
 * @create: 2022-07-05
 */
class AppInstallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        BoxRepository.initLocalAppList()
    }

    companion object {
        fun register(context: Context) {
            val intent = IntentFilter()
            intent.addAction(Intent.ACTION_PACKAGE_ADDED)
            intent.addAction(Intent.ACTION_PACKAGE_REMOVED)
            intent.addDataScheme("package")
            context.registerReceiver(AppInstallReceiver(), intent)
        }
    }
}
