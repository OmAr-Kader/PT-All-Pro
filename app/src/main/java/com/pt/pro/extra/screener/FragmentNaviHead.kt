package com.pt.pro.extra.screener

import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.fetchMainExtractor
import com.pt.common.stable.rKTSack
import com.pt.pro.R
import com.pt.pro.databinding.FloatingHeadWindowsBinding
import com.pt.pro.databinding.LayoutMyHeadBinding

class FragmentNaviHead : GlobalSimpleFragment<LayoutMyHeadBinding>() {

    private var stubFloating: FloatingHeadWindowsBinding? = null


    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            LayoutMyHeadBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }


    @com.pt.common.global.UiAnn
    override fun LayoutMyHeadBinding.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentNaviHead)

        androidx.asynclayoutinflater.view.AsyncLayoutInflater(ctx).apply {
            inflate(
                R.layout.floating_head_windows,
                requireView() as? android.view.ViewGroup?,
                context.fetchMainExtractor,
            ) { view, _, _ ->
                @com.pt.common.global.ViewAnn
                stubFloating = FloatingHeadWindowsBinding.bind(view)
                android.widget.FrameLayout.LayoutParams(
                    -2,
                    -2,
                    android.view.Gravity.END and android.view.Gravity.CENTER_VERTICAL
                ).apply {
                    frameAdd.addView(view, this)
                }
                stubFloating?.initViews()
            }
        }
    }

    private fun FloatingHeadWindowsBinding.initViews() {
        mainOne.framePara(-2, -2) {
            gravity = android.view.Gravity.CENTER_HORIZONTAL
        }
        constraintOne.apply {
            framePara(-2, -2) {
                gravity = android.view.Gravity.CENTER_VERTICAL
            }
            justVisible()
        }
        ctx.compactImage(R.drawable.ic_volume) {
            alarmHead.setImageDrawable(this)
        }
        ctx.compactImage(R.drawable.ic_volume_off) {
            fileHead.setImageDrawable(this)
        }
        ctx.compactImage(R.drawable.ic_rotation_screen) {
            dataHead.setImageDrawable(this)
        }
        ctx.compactImage(R.drawable.ic_notification) {
            galleryFloating.setImageDrawable(this)
        }
        ctx.compactImage(R.drawable.ic_screen_brightness) {
            screenShootHead.setImageDrawable(this)
        }
        ctx.compactImage(R.drawable.ic_brightness_off) {
            screenRecordHead.setImageDrawable(this)
        }
        androidx.asynclayoutinflater.view.AsyncLayoutInflater(ctx).apply {
            inflate(
                R.layout.floating_head_apps,
                requireView() as? android.view.ViewGroup?,
                context.fetchMainExtractor,
            ) { view, _, _ ->
                mainOne.framePara(
                    -2,
                    -2,
                ) {
                    gravity = android.view.Gravity.END or android.view.Gravity.CENTER_VERTICAL
                    mainOne.addView(view, this)
                }
            }
        }
        root.justVisible()
        run.rKTSack(100L).postAfter()
    }

    override fun onResume() {
        super.onResume()
        if (stubFloating != null) {
            run.rKTSack(100L).postAfter()
        }
    }

    override fun onPause() {
        unPost(run.two)
        super.onPause()
    }


    private val run: DSackT<() -> Unit, Int>
        get() = com.pt.common.stable.toCatchSack(22) {
            stubFloating?.constraintOne?.apply {
                com.pt.common.stable.catchy(Unit) {
                    if (isVis) {
                        invisibleFade(300L)
                        run.rKTSack(1600L).postAfter()
                    } else {
                        visibleFade(300L)
                        run.rKTSack(2000L).postAfter()
                    }
                }
            }
            Unit
        }

    override fun onDestroyView() {
        //onDestroyHandling()
        stubFloating = null
        super.onDestroyView()
    }

    @com.pt.common.global.UiAnn
    override fun LayoutMyHeadBinding.onClick(v: android.view.View) {

    }

}