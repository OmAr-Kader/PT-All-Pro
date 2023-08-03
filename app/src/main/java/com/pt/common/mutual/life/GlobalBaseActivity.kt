package com.pt.common.mutual.life

import com.pt.common.global.*
import com.pt.common.stable.*
import kotlinx.coroutines.asCoroutineDispatcher

abstract class GlobalBaseActivity : androidx.appcompat.app.AppCompatActivity(),
    kotlinx.coroutines.CoroutineScope,
    com.pt.common.mutual.base.BackInterface {

    internal var jobNative: kotlinx.coroutines.Job? = null
    internal var extNative: java.util.concurrent.ExecutorService? = null
    internal var dispatcherNative: kotlinx.coroutines.ExecutorCoroutineDispatcher? = null

    internal inline val job: kotlinx.coroutines.Job
        get() = jobNative ?: kotlinx.coroutines.SupervisorJob().also { jobNative = it }

    internal inline val ext: java.util.concurrent.ExecutorService
        get() = extNative ?: fetchExtractor.also { extNative = it }

    internal inline val dispatch: kotlinx.coroutines.ExecutorCoroutineDispatcher
        get() = dispatcherNative ?: ext.asCoroutineDispatcher().also { dispatcherNative = it }

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = dispatch + job + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    @com.pt.common.global.MainAnn
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        fetchStyle.apply(::setTheme)
        super.onCreate(savedInstanceState)
    }

    protected fun recreateAct() {
        catchyUnit {
            finish()
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    android.content.Intent.FLAG_ACTIVITY_NO_HISTORY or
                    android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    internal suspend fun intiOnBackPressed(b: () -> Unit) {
        withMainNormal {
            onBackPressedDispatcher.addCallback(this@GlobalBaseActivity,
                object : androidx.activity.OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        b.invoke()
                    }
                })
        }
    }

    fun destroyBaseScope() {
        jobNative?.cancelJob()
        catchyUnit {
            dispatcherNative?.close()
        }
        extNative?.shutdownNow()
        jobNative = null
        dispatcherNative = null
        extNative = null
    }

    override fun onDestroy() {
        destroyBaseScope()
        cancelScope()
        super.onDestroy()
    }

}