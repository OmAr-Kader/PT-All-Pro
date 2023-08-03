package com.pt.pro.alarm.decline

import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import com.pt.common.global.*
import com.pt.common.moderator.touch.ContactListener
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.pro.alarm.interfaces.AlarmDismiss
import com.pt.pro.databinding.FragmentDoubleSwipeBinding

class FragmentDoubleSwipe : GlobalSimpleFragment<FragmentDoubleSwipeBinding>(), DismissListener {

    override var alarm: AlarmSack? = null
    override var colorImage: Int = 0
    override var alarmDismiss: AlarmDismiss? = null

    override var test: Boolean = false

    private var ver = 0F
    private var hor = 0F
    private var xXLimit = 0F
    private var yYLimit = 0F
    private var xXPos = 0F
    private var yYPos = 0F
    private var dif = 0F

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> View
        @com.pt.common.global.UiAnn
        get() = {
            FragmentDoubleSwipeBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }

    @com.pt.common.global.MainAnn
    override fun FragmentDoubleSwipeBinding.onViewCreated() {
        act.windowManager?.fetchDimensionsMan {
            ver = this@fetchDimensionsMan.width.toFloat() - 65F.toPixel
            hor = this@fetchDimensionsMan.height.toFloat() - 65F.toPixel - 100
            xXLimit = this@fetchDimensionsMan.width.toFloat() - (65F.toPixel * 2)
            yYLimit = this@fetchDimensionsMan.height.toFloat() - 65F.toPixel
            xXPos = (this@fetchDimensionsMan.width.toFloat()) - 65F.toPixel
            yYPos = (this@fetchDimensionsMan.height.toFloat()) - 65F.toPixel
        }
        dif = 37F.toPixel.toFloat()

        doubleFrame.setOnClickListener(this@FragmentDoubleSwipe)

        if (!test) {
            doubleSwipe.apply {
                touchListener?.setContactListener(false)
            }
        }

        doubleSwipe.showSwipe(0F, targetX = ver, fromY = 0F, targetY = -hor)
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
        if (test && !nightRider) {
            ctx.fetchColor(com.pt.pro.R.color.bbo).let {
                clockTime.setTextColor(it)
                clockAmPm.setTextColor(it)
                textLabel.setTextColor(it)
                textRepeating.setTextColor(it)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun FragmentDoubleSwipeBinding.onClick(v: View) {
        act.windowManager?.fetchDimensionsMan {
            (this@fetchDimensionsMan.width.toFloat() - 65F.toPixel).let { itVer ->
                (this@fetchDimensionsMan.height.toFloat() - 65F.toPixel - 100).let { itHor ->
                    doubleSwipe.apply {
                        clearAnimation()
                        showSwipe(0F, targetX = itVer, fromY = 0F, targetY = -itHor)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun View.showSwipe(fromX: Float, targetX: Float, fromY: Float, targetY: Float) {
        AnimationSet(true).apply {
            isFillEnabled = true
            TranslateAnimation(fromX, targetX, fromY, 0.0f).also {
                it.interpolator = AccelerateDecelerateInterpolator()
                it.duration = 700
                it.fillAfter = true
                addAnimation(it)
            }
            TranslateAnimation(0.0f, 0.0f, 0.0f, targetY).also {
                it.interpolator = AccelerateDecelerateInterpolator()
                it.duration = 700
                it.startOffset = 500
                addAnimation(it)
            }
            startAnimation(this)
        }
    }


    @com.pt.common.global.UiAnn
    internal fun View.backSwipe(fromX: Float, targetX: Float, fromY: Float, targetY: Float) {
        AnimationSet(true).apply {
            isFillEnabled = true
            if (fromY != -1.0F) {
                TranslateAnimation(fromX, fromX, fromY, targetY).also {
                    it.interpolator = AccelerateDecelerateInterpolator()
                    it.duration = 400
                    it.fillAfter = true
                    addAnimation(it)
                }
            }
            TranslateAnimation(fromX, targetX, 0.0F, 0.0F).also {
                it.interpolator = AccelerateDecelerateInterpolator()
                it.duration = 400
                if (fromY != -1.0F) it.startOffset = 500
                it.fillAfter = true
                it.setAnimationListener(animationListener)
                addAnimation(it)
            }
            startAnimation(this)
        }
    }

    private var touchListener: ContactListener? = object : ContactListener {
        override fun onUp(v: View) {
            if (v.y < 250) {
                if (v.x == 0F) {
                    alarmDismiss?.doSnooze()
                } else {
                    alarmDismiss?.doDismiss()
                    act.supportFragmentManager.popBackStack()
                }
            }
            binding.apply {
                ctx.fetchColor(com.pt.pro.R.color.rdd).let {
                    cardOne.setCardBackgroundColor(it)
                    cardTwo.setCardBackgroundColor(it)
                    cardThree.setCardBackgroundColor(it)
                }
                doubleSwipe.backSwipe(
                    0F,
                    targetX = (-1 * v.x),
                    fromY = if (v.y == yYPos) -1.0F else 0.0F,
                    targetY = (yYLimit - v.y)
                )
            }
        }

        override fun onDown(v: View, event: MotionEvent) {
            binding.apply {
                ctx.fetchColor(com.pt.pro.R.color.rda).let {
                    cardOne.setCardBackgroundColor(it)
                    cardTwo.setCardBackgroundColor(it)
                    cardThree.setCardBackgroundColor(it)
                }
                doubleSwipe.clearAnimation()
            }
        }

        override fun onMove(v: View, event: MotionEvent) {
            if (event.rawX < ver + dif && event.rawX > dif && v.y >= hor) {
                v.x = event.rawX - dif
                v.y = yYPos
            } else if (event.rawY < yYLimit + dif && v.x >= xXLimit) {
                v.x = xXPos
                v.y = event.rawY - dif
            } else if (event.rawY < yYLimit + dif && v.x < 20F) {
                v.x = 0F
                v.y = event.rawY - dif
            }
        }

    }

    private var View?.animationListener: AnimationListener?
        @com.pt.common.global.UiAnn
        get() = object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                this@animationListener?.x = 0F
                this@animationListener?.y = yYPos
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }
        set(value) {
            value.logProvLess()
        }


    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        binder?.doubleSwipe?.onViewDestroy()
        null.animationListener = null
        touchListener = null
        alarmDismiss = null
        alarm = null
        test = false
        super.onDestroyView()
    }

}