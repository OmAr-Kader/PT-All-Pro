package com.pt.common.mutual.life

import com.pt.common.global.findBooleanPref
import com.pt.common.stable.cancelJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.isActive

abstract class GlobalSimpleFragment<VB : androidx.viewbinding.ViewBinding> : androidx.fragment.app.Fragment(),
    android.view.View.OnClickListener, kotlinx.coroutines.CoroutineScope, com.pt.common.mutual.base.JobHand {

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
        get() = actBase.coroutineContext.let {
            if (it.isActive) {
                it
            } else {
                actBase.destroyBaseScope()
                dispatch + job + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }
            }
        }

    /*override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = dispatch + job +
                kotlinx.coroutines.CoroutineExceptionHandler { _, e ->
                    e.toStr.logProvCrash("CoroutineExceptionHandler")
                }*/

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: com.pt.common.global.InvokingMutable = mutableListOf()
    override var invokerBagList: com.pt.common.global.InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    @Volatile
    internal var binder: VB? = null

    internal inline val binding: VB
        @com.pt.common.global.UiAnn
        get() = binder!!

    @com.pt.common.global.UiAnn
    internal inline fun iBinding(@com.pt.common.global.UiAnn b: VB.() -> Unit) {
        kotlin.contracts.contract {
            callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        b(binder ?: return)
    }

    @com.pt.common.global.UiAnn
    internal suspend inline fun iBindingSus(
        @com.pt.common.global.UiAnn crossinline b: suspend VB.() -> Unit
    ) {
        kotlin.contracts.contract {
            callsInPlace(b, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
        }
        b(binder ?: return)
    }

    internal inline val ctx: android.content.Context
        get() = context ?: activity?.baseContext!!

    internal inline val them: android.content.res.Resources.Theme get() = ctx.theme

    internal inline val rec: android.content.res.Resources get() = ctx.resources

    internal inline val Float.toPixel: Int
        get() = (this * dis.density + 0.5f).toInt()

    internal inline val dis: android.util.DisplayMetrics
        get() = ctx.resources.displayMetrics

    internal inline val Int.dStr: String
        get() {
            return try {
                ctx.resources.getString(this)
            } catch (e: android.content.res.Resources.NotFoundException) {
                ""
            }
        }

    internal inline val act: androidx.appcompat.app.AppCompatActivity
        get() = requireActivity() as androidx.appcompat.app.AppCompatActivity

    internal inline val actBase: GlobalBaseActivity
        get() = requireActivity() as GlobalBaseActivity

    internal inline val cont: android.content.ContentResolver
        get() = kotlin.runCatching {
            if (com.pt.common.global.isV_N) {
                (androidx.core.content.ContextCompat.isDeviceProtectedStorage(ctx).run {
                    if (this) androidx.core.content.ContextCompat.createDeviceProtectedStorageContext(ctx)?.contentResolver else ctx.contentResolver
                } ?: ctx.contentResolver)
            } else {
                ctx.contentResolver
            }
        }.getOrDefault(ctx.contentResolver)


    internal inline val nightRider: Boolean
        get() = ctx.findBooleanPref(com.pt.common.stable.NIGHT, com.pt.common.stable.RIDERS, false)

    @com.pt.common.global.UiAnn
    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?,
    ): android.view.View = inflater.creBin(container)

    @com.pt.common.global.UiAnn
    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.onViewCreated()
    }

    abstract fun VB.onViewCreated()

    abstract val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View

    override fun onClick(p0: android.view.View?) {
        binding.onClick(p0 ?: return)
    }

    abstract fun VB.onClick(v: android.view.View)

    private fun destroyScope() {
        jobNative?.cancelJob()
        com.pt.common.stable.catchyUnit {
            dispatcherNative?.close()
        }
        extNative?.shutdownNow()
        jobNative = null
        dispatcherNative = null
        extNative = null
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        destroyScope()
        //cancelScope()
        super.onDestroyView()
        binder = null
    }
}