package com.pt.common.mutual.life

import com.pt.common.global.findBooleanPref
import com.pt.common.stable.launchImdMain

abstract class GlobalDia<VB : androidx.viewbinding.ViewBinding> : androidx.appcompat.app.AppCompatDialogFragment(), kotlinx.coroutines.CoroutineScope,
    com.pt.common.mutual.base.JobHand {

    /*private var jobNative: kotlinx.coroutines.Job? = null
        get() = field ?: kotlinx.coroutines.SupervisorJob().also { field = it }

    private inline val job: kotlinx.coroutines.Job
        get() = jobNative!!

    private var extNative: java.util.concurrent.ExecutorService? = null
        get() = field ?: com.pt.common.stable.fetchExtractor.also { field = it }

    private inline val ext: java.util.concurrent.ExecutorService
        get() = extNative!!

    private var dispatcherNative: kotlinx.coroutines.ExecutorCoroutineDispatcher? = null
        get() = field ?: ext.asCoroutineDispatcher().also { field = it }

    private inline val dispatch: kotlinx.coroutines.ExecutorCoroutineDispatcher
        get() = dispatcherNative!!

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = dispatch + job +
                kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }*/

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = actDBase.coroutineContext

    override var qHand: android.os.Handler? = null
    override var disposableMain: com.pt.common.global.InvokingMutable = mutableListOf()
    override var invokerBagList: com.pt.common.global.InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    override var lastJob: kotlinx.coroutines.Job? = null

    private inline val actDBase: GlobalBaseActivity
        get() = requireActivity() as GlobalBaseActivity

    inline val actD: androidx.appcompat.app.AppCompatActivity
        get() = requireActivity() as androidx.appcompat.app.AppCompatActivity

    inline val ctxD: android.content.Context get() = requireContext()

    inline val recD: android.content.res.Resources get() = requireContext().resources

    inline val dia: android.app.Dialog get() = requireDialog()

    inline val Float.toPixelD: Int
        get() = (this * recD.displayMetrics.density + 0.5f).toInt()

    internal inline val nightRider: Boolean
        get() = ctxD.findBooleanPref(com.pt.common.stable.NIGHT, com.pt.common.stable.RIDERS, false)

    protected inline val binding: VB get() = binder!!
    protected var binder: VB? = null

    @com.pt.common.global.MainAnn
    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?,
    ): android.view.View? {
        requireDialog().window?.apply {
            android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT).also {
                setBackgroundDrawable(it)
            }
            attributes.windowAnimations = com.pt.pro.R.style.MyAnimation_Window
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @com.pt.common.global.MainAnn
    override fun onCreateDialog(savedInstanceState: android.os.Bundle?): android.app.Dialog {
        return androidx.appcompat.app.AlertDialog.Builder(actD).apply {
            actD.layoutInflater.creBinD.also(::setView)
        }.run {
            launchImdMain {
                binding.intiViews()
            }
            create()
        }
    }

    abstract val android.view.LayoutInflater.creBinD: android.view.View

    abstract suspend fun VB.intiViews()

    override fun onDestroyView() {
        /*job.cancelJob()
        com.pt.common.stable.catchy(Unit) {
            dispatch.close()
        }
        ext.shutdownNow()
        cancelScope()
        extNative = null
        jobNative = null
        dispatcherNative = null*/
        super.onDestroyView()
        binder = null
    }

}