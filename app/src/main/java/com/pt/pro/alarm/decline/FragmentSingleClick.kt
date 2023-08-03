package com.pt.pro.alarm.decline

import com.pt.common.global.*
import com.pt.common.global.AlarmSack
import com.pt.pro.alarm.interfaces.AlarmDismiss
import com.pt.pro.databinding.FragmentSingleClickBinding
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.launchImdMain


class FragmentSingleClick : GlobalSimpleFragment<FragmentSingleClickBinding>(), DismissListener {

    override var alarm: AlarmSack? = null
    override var colorImage: Int = 0
    override var alarmDismiss: AlarmDismiss? = null

    override var test: Boolean = false

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            FragmentSingleClickBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
                it.subMainClick.orientation = rec.linDirection
            }.root
        }

    @com.pt.common.global.UiAnn
    override fun FragmentSingleClickBinding.onViewCreated() {
        launchImdMain {
            com.pt.common.stable.justCoroutineMain {
                snoozeButton.setOnClickListener(this@FragmentSingleClick)
                dismissFrame.setOnClickListener(this@FragmentSingleClick)
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
    override fun FragmentSingleClickBinding.onClick(v: android.view.View) {
        if (v == dismissFrame) {
            alarmDismiss?.doDismiss()
            requireActivity().supportFragmentManager.popBackStack()
        } else {
            alarmDismiss?.doSnooze()
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.subMainClick.orientation = newConfig.linConDirection
    }

    override fun onDestroyView() {
        binder?.content?.stopRippleAnimation()
        alarmDismiss = null
        alarm = null
        binder = null
        super.onDestroyView()
    }

}