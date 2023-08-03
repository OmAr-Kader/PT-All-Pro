package com.pt.common.moderator.over

import android.util.TypedValue
import com.pt.common.global.toColorAlpha

open class RippleBackgroundFrame : android.widget.RelativeLayout {

    private companion object {
        const val DEFAULT_RIPPLE_COUNT = 3
        const val DEFAULT_DURATION_TIME = 2000
        const val DEFAULT_SCALE = 4.0F
    }

    private var rippleStrokeWidth = 0F
    private var paint: android.graphics.Paint? = null
    private var animationRunning = false
    private val rippleViewList: MutableList<RippleView> = mutableListOf()
    private var animatorSet: androidx.core.animation.Animator? = null

    private inline val android.content.res.Resources.toPixel: Float
        get() = (40F * displayMetrics.density + 0.5F)

    constructor(context: android.content.Context?) : super(context!!) {
        init(context)
    }

    constructor(
        context: android.content.Context?,
        attrs: android.util.AttributeSet?
    ) : super(context!!, attrs) {
        init(context)
    }

    constructor(
        context: android.content.Context?,
        attrs: android.util.AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        init(context)
    }

    @android.annotation.SuppressLint("Recycle")
    private fun init(context: android.content.Context?) {
        if (isInEditMode || context == null) return
        android.graphics.Paint().apply {
            this@apply.isAntiAlias = true
            this@apply.style = android.graphics.Paint.Style.FILL
            this@apply.color = TypedValue().apply {
                context.theme.resolveAttribute(android.R.attr.colorAccent, this, true)
            }.data.let {
                it.toColorAlpha((255 / 4))
            }
            this@RippleBackgroundFrame.paint = this
        }
        context.resources.toPixel.let { rad ->
            rippleStrokeWidth = rad
            gravity = android.view.Gravity.CENTER
            LayoutParams(
                (4 * rad).toInt(),
                (4 * rad).toInt()
            ).apply {
                gravity = android.view.Gravity.CENTER
                addRule(CENTER_IN_PARENT, TRUE)
            }
        }.also { rippleParams ->
            androidx.core.animation.AnimatorSet().apply {
                interpolator = androidx.core.animation.AccelerateDecelerateInterpolator()
                animatorSet = this
            }.apply {
                repeat(3) { i ->
                    val rippleView = RippleView(getContext())
                    addView(rippleView, rippleParams)
                    rippleViewList.add(rippleView)
                    mutableListOf<androidx.core.animation.Animator>().apply {
                        androidx.core.animation.ObjectAnimator.ofFloat(
                            rippleView,
                            "ScaleX",
                            1.0f,
                            DEFAULT_SCALE
                        ).also { scaleXAnimator ->
                            scaleXAnimator.repeatCount =
                                androidx.core.animation.ObjectAnimator.INFINITE
                            scaleXAnimator.repeatMode =
                                androidx.core.animation.ObjectAnimator.RESTART
                            scaleXAnimator.startDelay =
                                (i * (DEFAULT_DURATION_TIME / DEFAULT_RIPPLE_COUNT)).toLong()
                            scaleXAnimator.duration = DEFAULT_DURATION_TIME.toLong()
                        }.let(::add)
                        androidx.core.animation.ObjectAnimator.ofFloat(
                            rippleView,
                            "ScaleY",
                            1.0f,
                            DEFAULT_SCALE
                        ).also { scaleYAnimator ->
                            scaleYAnimator.repeatCount =
                                androidx.core.animation.ObjectAnimator.INFINITE
                            scaleYAnimator.repeatMode =
                                androidx.core.animation.ObjectAnimator.RESTART
                            scaleYAnimator.startDelay =
                                (i * (DEFAULT_DURATION_TIME / DEFAULT_RIPPLE_COUNT)).toLong()
                            scaleYAnimator.duration = DEFAULT_DURATION_TIME.toLong()
                        }.let(::add)
                        androidx.core.animation.ObjectAnimator.ofFloat(
                            rippleView,
                            "Alpha",
                            1.0f,
                            0f
                        ).also { alphaAnimator ->
                            alphaAnimator.repeatCount =
                                androidx.core.animation.ObjectAnimator.INFINITE
                            alphaAnimator.repeatMode =
                                androidx.core.animation.ObjectAnimator.RESTART
                            alphaAnimator.startDelay =
                                (i * (DEFAULT_DURATION_TIME / DEFAULT_RIPPLE_COUNT)).toLong()
                            alphaAnimator.duration = DEFAULT_DURATION_TIME.toLong()
                        }.let(::add)
                    }.also(::playTogether)
                }
            }
        }
    }

    private inner class RippleView(context: android.content.Context?) : android.view.View(context) {
        override fun onDraw(canvas: android.graphics.Canvas) {
            val radius: Int = kotlin.math.min(width, height) / 2
            paint?.let {
                canvas.drawCircle(
                    radius.toFloat(), radius.toFloat(), radius - rippleStrokeWidth,
                    it
                )
            }
        }

        init {
            this.visibility = INVISIBLE
        }
    }

    fun startRippleAnimation() {
        if (!isRippleAnimationRunning()) {
            for (rippleView in rippleViewList) {
                rippleView.visibility = VISIBLE
            }
            animatorSet?.start()
            animationRunning = true
        }
    }

    fun stopRippleAnimation() {
        if (isRippleAnimationRunning()) {
            animatorSet?.end()
            animationRunning = false
        }
    }

    private fun isRippleAnimationRunning(): Boolean {
        return animationRunning
    }
}