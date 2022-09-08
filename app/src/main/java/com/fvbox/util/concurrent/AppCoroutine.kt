package com.fvbox.util.concurrent

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object AppCoroutine {
    private const val TAG = "AppCoroutine"

    private val supervisor = SupervisorJob()

    private val handler = CoroutineExceptionHandler { context, throwable ->
    }

    val IO = Dispatchers.IO + CoroutineName("FV-IO") + handler + supervisor

    val Default = Dispatchers.Default + CoroutineName("FV-Default") + handler + supervisor

    val Main = Dispatchers.Main + CoroutineName("FV-Main") + handler + supervisor

}
