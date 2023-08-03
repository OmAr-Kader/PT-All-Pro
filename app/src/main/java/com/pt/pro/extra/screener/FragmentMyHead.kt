package com.pt.pro.extra.screener

import android.view.Gravity
import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.databinding.FloatingHeadLayoutBinding
import com.pt.pro.databinding.FloatingHeadWindowsBinding
import com.pt.pro.extra.interfaces.SwiperListener

class FragmentMyHead : GlobalSimpleFragment<FloatingHeadLayoutBinding>(), SwiperListener {

    private var isRight = false
    private var isEnabled = true
    private var runnable: ((Boolean) -> Unit)? = null

    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            FloatingHeadLayoutBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }

    override fun FloatingHeadLayoutBinding.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentMyHead)
        launchImdMain {
            justCoroutine {
                isRight = rec.isRightToLeft
                stubFloating.initViews()
            }
            justCoroutine {
                isScreen.apply {
                    ctx.findBooleanPreference(IS_SCREEN, true).let {
                        isChecked = it
                        stubFloating.changeFramesForEnd(it, isRight)
                    }
                    setOnCheckedChangeListener(this@FragmentMyHead)
                }
            }
        }
    }

    override fun setRunnable(b: (Boolean) -> Unit) {
        runnable = b
    }

    override fun doLaunch() {
        if (isEnabled) {
            launchDef {
                withMain {
                    binding.isScreen.isEnabled = false
                    isEnabled = false
                }
            }
        }
    }

    override fun doStop() {
        if (!isEnabled) {
            launchDef {
                withMain {
                    binding.isScreen.isEnabled = true
                    isEnabled = true
                }
            }
        }
    }

    private fun FloatingHeadWindowsBinding.changeFramesForEnd(screen: Boolean, isRight: Boolean) {
        launchDef {
            withMain {
                if (screen) {
                    screenShootFrame.justVisible()
                    screenRecordFrame.justVisible()
                } else {
                    screenShootFrame.justGone()
                    screenRecordFrame.justGone()
                }
                if (isRight) {
                    if (screen) {
                        changeFramesForStartScreen()
                    } else {
                        changeFramesForStartNot()
                    }
                } else {
                    if (screen) {
                        changeFramesForEndScreen()
                    } else {
                        changeFramesForEndNot()
                    }
                }
            }
        }
    }


    private fun FloatingHeadWindowsBinding.changeFramesForEndNot() {
        appsFrame.tag = Gravity.END or Gravity.CENTER_VERTICAL
        appsFrame.framePara(45F.toPixel, 45F.toPixel) {
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
        }
        alarmFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.END or Gravity.TOP
            bottomMargin = 124F.toPixel
            marginEnd = 2F.toPixel
        }
        fileFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.END or Gravity.BOTTOM
            topMargin = 124F.toPixel
            marginEnd = 2F.toPixel
        }
        dataFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginEnd = 64F.toPixel
            bottomMargin = 30F.toPixel
        }
        galleryFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START
            marginEnd = 64F.toPixel
            topMargin = 30F.toPixel
        }
    }

    private fun FloatingHeadWindowsBinding.changeFramesForStartNot() {
        appsFrame.tag = Gravity.START or Gravity.CENTER_VERTICAL
        appsFrame.framePara(45F.toPixel, 45F.toPixel) {
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }
        alarmFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.TOP
            bottomMargin = 124F.toPixel
            marginStart = 2F.toPixel
        }
        galleryFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START
            marginStart = 64F.toPixel
            topMargin = 30F.toPixel
        }
        dataFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginStart = 64F.toPixel
            bottomMargin = 30F.toPixel
        }
        fileFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.BOTTOM
            topMargin = 124F.toPixel
            marginStart = 2F.toPixel
        }
    }


    private fun FloatingHeadWindowsBinding.changeFramesForEndScreen() {
        appsFrame.tag = Gravity.END or Gravity.CENTER_VERTICAL
        appsFrame.framePara(45F.toPixel, 45F.toPixel) {
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
        }
        alarmFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.END or Gravity.TOP
            bottomMargin = 140F.toPixel
            marginEnd = 2F.toPixel
        }
        screenShootFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START
            marginStart = 35F.toPixel
            topMargin = 10F.toPixel
        }
        galleryFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START
            topMargin = 45F.toPixel
            marginEnd = 84F.toPixel
        }
        dataFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.BOTTOM
            bottomMargin = 45F.toPixel
            marginEnd = 84F.toPixel
        }
        screenRecordFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginStart = 35F.toPixel
            bottomMargin = 10F.toPixel
        }
        fileFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.END or Gravity.BOTTOM
            topMargin = 140F.toPixel
            marginEnd = 2F.toPixel
        }
    }


    private fun FloatingHeadWindowsBinding.changeFramesForStartScreen() {
        appsFrame.tag = Gravity.START or Gravity.CENTER_VERTICAL
        appsFrame.framePara(45F.toPixel, 45F.toPixel) {
            gravity = Gravity.START or Gravity.CENTER_VERTICAL
        }
        alarmFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.TOP
            bottomMargin = 140F.toPixel
            marginStart = 2F.toPixel
        }
        screenShootFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START
            marginStart = 49F.toPixel
            topMargin = 10F.toPixel
        }
        galleryFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START
            marginStart = 84F.toPixel
            topMargin = 45F.toPixel
        }
        dataFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginStart = 84F.toPixel
            bottomMargin = 45F.toPixel
        }
        screenRecordFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.BOTTOM
            marginStart = 49F.toPixel
            bottomMargin = 10F.toPixel
        }
        fileFrame.framePara(35F.toPixel, 35F.toPixel) {
            gravity = Gravity.START or Gravity.BOTTOM
            topMargin = 140F.toPixel
            marginStart = 2F.toPixel
        }
    }

    private fun FloatingHeadWindowsBinding.initViews() {
        mainOne.framePara(-2, -2) {
            gravity = Gravity.CENTER_HORIZONTAL
        }
        constraintOne.apply {
            framePara(-2, -2) {
                gravity = Gravity.CENTER_VERTICAL
            }
            justVisible()
        }
        appsFrame.apply {
            ctx.fetchImageView {
                ctx.compactImage(com.pt.pro.R.drawable.ic_logo_floating) {
                    setImageDrawable(this)
                }
                ctx.compactImage(com.pt.pro.R.drawable.circle) {
                    background = this
                }
                backReColor(them.findAttr(android.R.attr.colorPrimary))
                appsFrame.addView(this)
            }
            justVisible()
        }
        root.justVisible()
        run.rKTSack(100L).postAfter()
    }

    override fun onResume() {
        super.onResume()
        run.rKTSack(100L).postAfter()
    }

    override fun onPause() {
        unPostAll()
        super.onPause()
    }

    private val run: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            binder?.stubFloating?.constraintOne?.apply {
                catchy(Unit) {
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

    @com.pt.common.global.UiAnn
    override fun FloatingHeadLayoutBinding.onClick(v: android.view.View) {
    }

    override fun onCheckedChanged(buttonView: android.widget.CompoundButton?, isChecked: Boolean) {
        launchImdMain {
            justCoroutine {
                binding.stubFloating.changeFramesForEnd(isChecked, isRight)
                runnable?.invoke(isChecked)
            }
            ctx.updatePrefBoolean(IS_SCREEN, isChecked)
        }
    }

}