package com.pt.common.mutual.life

import com.pt.common.global.fetchHand
import kotlinx.coroutines.asCoroutineDispatcher

abstract class GlobalServiceManger(@Volatile protected var context: android.content.Context?) :
    android.view.View.OnClickListener, com.pt.common.mutual.base.OnConfigurationChanged, kotlinx.coroutines.CoroutineScope,
    com.pt.common.mutual.base.JobHand {

    internal var jobNative: kotlinx.coroutines.Job? = null
    internal var extNative: java.util.concurrent.ExecutorService? = null
    internal var dispatcherNative: kotlinx.coroutines.ExecutorCoroutineDispatcher? = null

    internal inline val job: kotlinx.coroutines.Job
        get() = jobNative ?: kotlinx.coroutines.SupervisorJob().also { jobNative = it }

    internal inline val ext: java.util.concurrent.ExecutorService
        get() = extNative ?: com.pt.common.stable.fetchExtractor.also { extNative = it }

    internal inline val dispatch: kotlinx.coroutines.ExecutorCoroutineDispatcher
        get() = dispatcherNative ?: ext.asCoroutineDispatcher().also { dispatcherNative = it }

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = dispatch + job +
                kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = ctxS.fetchHand
    override var disposableMain: com.pt.common.global.InvokingMutable = mutableListOf()
    override var invokerBagList: com.pt.common.global.InvokingBackMutable = mutableListOf()

    internal inline val ctxS: android.content.Context get() = context!!

    internal inline val recS: android.content.res.Resources get() = context!!.resources

    internal inline val contS: android.content.ContentResolver get() = context!!.contentResolver

    internal inline val disS: android.util.DisplayMetrics
        get() = context!!.resources.displayMetrics

    internal inline val Float.toPixelS: Int
        get() = (this * disS.density + 0.5f).toInt()

}