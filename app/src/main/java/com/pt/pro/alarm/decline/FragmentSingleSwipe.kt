package com.pt.pro.alarm.decline

import android.view.View
import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.launchImdMain
import com.pt.pro.alarm.interfaces.AlarmDismiss
import com.pt.pro.databinding.FragmentSingleSwipeBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState

class FragmentSingleSwipe : GlobalSimpleFragment<FragmentSingleSwipeBinding>(), DismissListener {

    override var alarm: AlarmSack? = null
    override var colorImage: Int = 0
    override var alarmDismiss: AlarmDismiss? = null

    override var test: Boolean = false
    private var slideOff = 0.0F
    private var slideState = PanelState.EXPANDED


    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> View
        @com.pt.common.global.UiAnn
        get() = {
            FragmentSingleSwipeBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
                it.subMainSwipe.orientation = rec.linDirection
            }.root
        }

    @com.pt.common.global.UiAnn
    override fun FragmentSingleSwipeBinding.onViewCreated() {
        launchImdMain {
            com.pt.common.stable.justCoroutineMain {
                slidingFrame.apply {
                    panelState = PanelState.EXPANDED
                    addSenseListener(false) { _, _, type ->
                        if (type == com.pt.common.stable.UP_SEN) {
                            if (slideOff < 0.3647541F && slideState == PanelState.ANCHORED) {
                                alarmDismiss?.doDismiss()
                                act.supportFragmentManager.popBackStack()
                            }
                        }
                    }
                    addPanelSlideListener(slideListener)
                }
                centerText.setOnClickListener(this@FragmentSingleSwipe)
                if (ctx.is24Hour) {
                    clockAmPm.justGone()
                }
                content.startRippleAnimation()
            }
            com.pt.common.stable.justCoroutineMain {
                com.pt.common.stable.catchy(null) {
                    if (isV_O) {
                        try {
                            androidx.core.content.res.ResourcesCompat.getFont(
                                ctx,
                                com.pt.pro.R.font.widget_font_clock
                            )
                        } catch (_: android.content.res.Resources.NotFoundException) {
                            null
                        }
                    } else null
                }?.let {
                    clockTime.apply {
                        typeface = it
                    }
                    clockAmPm.apply {
                        typeface = it
                    }
                }
            }
            com.pt.common.stable.justCoroutineMain {
                if (test && !nightRider) {
                    ctx.fetchColor(com.pt.pro.R.color.bbo).let {
                        clockTime.setTextColor(it)
                        clockAmPm.setTextColor(it)
                        textLabel.setTextColor(it)
                        textRepeating.setTextColor(it)
                    }
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    override fun FragmentSingleSwipeBinding.onClick(v: View) {
        alarmDismiss?.doSnooze()
    }

    private var slideListener: SlidingUpPanelLayout.PanelSlideListener? =
        object : SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: View, slideOffset: Float) {
                slideOff = slideOffset
            }

            override fun onPanelStateChanged(
                panel: View,
                previousState: PanelState,
                newState: PanelState,
            ) {
                if (newState == PanelState.COLLAPSED) {
                    alarmDismiss?.doDismiss()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                slideState = newState
            }
        }


    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.subMainSwipe.orientation = newConfig.linConDirection
    }

    override fun onDestroyView() {
        binder?.content?.stopRippleAnimation()
        alarmDismiss = null
        alarm = null
        binder = null
        slideListener = null
        super.onDestroyView()
    }

}