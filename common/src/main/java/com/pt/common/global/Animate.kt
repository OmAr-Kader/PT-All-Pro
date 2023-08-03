@file:Suppress("unused")

package com.pt.common.global

inline val android.view.View?.isVis: Boolean
    @com.pt.common.global.UiAnn
    get() = this?.visibility == android.view.View.VISIBLE

inline val android.view.View?.isInVis: Boolean
    @com.pt.common.global.UiAnn
    get() = this?.visibility == android.view.View.INVISIBLE

inline val android.view.View?.isGon: Boolean
    @com.pt.common.global.UiAnn
    get() = this?.visibility == android.view.View.GONE

inline fun scaleViewFromTo(
    scaleIn: Float,
    scaleOut: Float,
    durationAnimation: Long,
    scaleView: android.view.animation.ScaleAnimation.() -> Unit,
) {
    android.view.animation.ScaleAnimation(
        scaleIn, scaleOut,
        scaleIn, scaleOut,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
    ).apply {
        fillAfter = true
        duration = durationAnimation
    }.also {
        scaleView(it)
    }
}

@com.pt.common.global.UiAnn
inline fun animateRotationNor(
    fromDegrees: Float,
    toDegrees: Float,
    d: Long,
    @UiAnn crossinline rotateAnimation: android.view.animation.RotateAnimation.() -> Unit,
) {
    android.view.animation.RotateAnimation(
        fromDegrees, toDegrees,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
    ).apply {
        duration = d
        interpolator = android.view.animation.LinearInterpolator()
        fillAfter = true
    }.also {
        rotateAnimation(it)
    }
}

@com.pt.common.global.UiAnn
suspend inline fun animateRotation(
    fromDegrees: Float,
    toDegrees: Float,
    d: Long,
    @UiAnn crossinline rotateAnimation: suspend android.view.animation.RotateAnimation.() -> Unit,
) {
    com.pt.common.stable.withMain {
        android.view.animation.RotateAnimation(
            fromDegrees, toDegrees,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
        ).apply {
            duration = d
            interpolator = android.view.animation.LinearInterpolator()
            fillAfter = true
        }.also {
            rotateAnimation(it)
        }
    }
}

@com.pt.common.global.UiAnn
suspend inline fun scaleView(
    scale: Float,
    durationAnimation: Long,
    @UiAnn crossinline scaleView: suspend android.view.animation.ScaleAnimation.() -> Unit,
) {
    com.pt.common.stable.withMain {
        android.view.animation.ScaleAnimation(
            1f, scale,
            1f, scale,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
        ).apply {
            fillAfter = true
            duration = durationAnimation
        }.also {
            scaleView(it)
        }
    }
}

@com.pt.common.global.UiAnn
suspend inline fun scaleViewOutPend(
    scale: Float,
    durationAnimation: Long,
    @UiAnn crossinline scaleView: suspend android.view.animation.ScaleAnimation.() -> Unit,
) {
    com.pt.common.stable.withMain {
        android.view.animation.ScaleAnimation(
            scale, 1F,
            scale, 1F,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
        ).apply {
            fillAfter = true
            duration = durationAnimation
        }.also {
            scaleView(it)
        }
    }
}


@com.pt.common.global.UiAnn
inline fun animateRotate(
    fromDegrees: Float,
    toDegrees: Float,
    rotateAnimation: android.view.animation.RotateAnimation.() -> Unit,
) {
    android.view.animation.RotateAnimation(
        fromDegrees, toDegrees,
        android.view.animation.RotateAnimation.RELATIVE_TO_SELF, 0.5F,
        android.view.animation.RotateAnimation.RELATIVE_TO_SELF, 0.5F
    ).apply {
        duration = 0
        isFillEnabled = true
        fillAfter = true
        fillBefore = true
    }.also(rotateAnimation)
}

@com.pt.common.global.UiAnn
inline fun scaleViewOut(
    from: Float,
    to: Float,
    scaleView: android.view.animation.ScaleAnimation.() -> Unit,
) {
    android.view.animation.ScaleAnimation(
        from, to,
        from, to,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
    ).apply {
        fillAfter = true
        duration = 200
        fillBefore = true
    }.also(scaleView)
}

@com.pt.common.global.UiAnn
inline fun returnAnimForChange(
    startX: Int,
    endX: Int,
    startY: Int,
    endY: Int,
    anim: androidx.core.animation.ValueAnimator.() -> Unit,
) {
    androidx.core.animation.PropertyValuesHolder.ofInt(
        com.pt.common.BuildConfig.X_DIMENSION,
        startX,
        endX
    ).let { pvhX ->
        androidx.core.animation.PropertyValuesHolder.ofInt(
            com.pt.common.BuildConfig.Y_DIMENSION,
            startY,
            endY
        ).let { pvhY ->
            anim(androidx.core.animation.ValueAnimator.ofPropertyValuesHolder(pvhX, pvhY))
        }
    }
}

@com.pt.common.global.UiAnn
inline fun returnForSlip(
    begin: Int,
    end: Int,
    anim: androidx.core.animation.ValueAnimator.() -> Unit,
) {
    anim(androidx.core.animation.ValueAnimator.ofInt(begin, end))
}

inline fun androidx.core.animation.ValueAnimator.endListener(
    crossinline f: () -> Unit
) {
    object : androidx.core.animation.Animator.AnimatorListener {
        override fun onAnimationStart(animation: androidx.core.animation.Animator) {}

        override fun onAnimationEnd(animation: androidx.core.animation.Animator) {
            f.invoke()
        }

        override fun onAnimationCancel(animation: androidx.core.animation.Animator) {}
        override fun onAnimationRepeat(animation: androidx.core.animation.Animator) {}

    }.apply {
        addListener(this)
    }
}

@com.pt.common.global.UiAnn
fun android.view.View?.shifterVisible(boolean: Boolean) {
    if (boolean)
        this?.visibility = android.view.View.VISIBLE
    else
        this?.visibility = android.view.View.GONE
}

@com.pt.common.global.UiAnn
fun android.view.View?.justVisible() {
    if (this?.visibility != android.view.View.VISIBLE)
        this?.visibility = android.view.View.VISIBLE
}

@com.pt.common.global.UiAnn
fun android.view.View?.justInvisible() {
    if (this?.visibility != android.view.View.INVISIBLE)
        this?.visibility = android.view.View.INVISIBLE
}

@com.pt.common.global.UiAnn
fun android.view.View?.justGone() {
    if (this?.visibility != android.view.View.GONE)
        this?.visibility = android.view.View.GONE
}

@com.pt.common.global.UiAnn
fun android.view.View.visibleFade(d: Long) {
    androidx.transition.Fade(androidx.transition.Visibility.MODE_IN).apply {
        duration = d
        addTarget(this@visibleFade)
    }.also(::fetchParent)
    visibility = android.view.View.VISIBLE
}

@com.pt.common.global.UiAnn
fun android.view.View.invisibleFade(d: Long) {
    androidx.transition.Fade(androidx.transition.Visibility.MODE_OUT).apply {
        duration = d
        addTarget(this@invisibleFade)
    }.also(::fetchParent)
    visibility = android.view.View.INVISIBLE
}

@com.pt.common.global.UiAnn
fun android.view.View.goneFade(d: Long) {
    if (visibility == android.view.View.GONE) return
    androidx.transition.Fade(androidx.transition.Visibility.MODE_OUT).apply {
        duration = d
        addTarget(this@goneFade)
    }.also(::fetchParent)
    visibility = android.view.View.GONE
}

@com.pt.common.global.UiAnn
fun android.view.View?.goneFadeNull(d: Long) {
    this ?: return
    if (visibility == android.view.View.GONE) return
    androidx.transition.Fade(androidx.transition.Visibility.MODE_OUT).apply {
        duration = d
        addTarget(this@goneFadeNull)
    }.also(::fetchParent)
    visibility = android.view.View.GONE
}

@com.pt.common.global.UiAnn
fun android.view.View.visibleTop(d: Long) {
    androidx.transition.Slide(android.view.Gravity.TOP).apply {
        duration = d
        addTarget(this@visibleTop)
    }.also(::fetchParent)
    visibility = android.view.View.VISIBLE
}

@com.pt.common.global.UiAnn
fun android.view.View.goneTop(d: Long) {
    if (visibility == android.view.View.GONE) return
    androidx.transition.Slide(android.view.Gravity.TOP).apply {
        duration = d
        addTarget(this@goneTop)
    }.also(::fetchParent)
    visibility = android.view.View.GONE
}

@com.pt.common.global.UiAnn
suspend fun android.view.View.goneTopSus(d: Long) {
    com.pt.common.stable.withMain {
        if (visibility == android.view.View.GONE) return@withMain
        androidx.transition.Slide(android.view.Gravity.TOP).apply {
            duration = d
            addTarget(this@goneTopSus)
        }.also(::fetchParent)
        visibility = android.view.View.GONE
    }
}

@com.pt.common.global.UiAnn
fun android.view.View.visibleBottom(d: Long) {
    androidx.transition.Slide(android.view.Gravity.BOTTOM).apply {
        duration = d
        addTarget(this@visibleBottom)
    }.also(::fetchParent)
    visibility = android.view.View.VISIBLE
}

@com.pt.common.global.UiAnn
suspend fun android.view.View.visibleBottomSus(d: Long) {
    com.pt.common.stable.withMain {
        androidx.transition.Slide(android.view.Gravity.BOTTOM).apply {
            duration = d
            addTarget(this@visibleBottomSus)
        }.also(::fetchParent)
        visibility = android.view.View.VISIBLE
    }
}

@com.pt.common.global.UiAnn
fun android.view.View.goneBottom(d: Long) {
    if (visibility == android.view.View.GONE) return
    androidx.transition.Slide(android.view.Gravity.BOTTOM).apply {
        duration = d
        addTarget(this@goneBottom)
    }.also(::fetchParent)
    visibility = android.view.View.GONE
}

@com.pt.common.global.UiAnn
suspend fun android.view.View.goneBottomSus(d: Long) {
    com.pt.common.stable.withMain {
        if (visibility == android.view.View.GONE) return@withMain
        androidx.transition.Slide(android.view.Gravity.BOTTOM).apply {
            duration = d
            addTarget(this@goneBottomSus)
        }.also(::fetchParent)
        visibility = android.view.View.GONE
    }
}

suspend fun android.widget.FrameLayout.scrollToTop() {
    com.pt.common.stable.withMain {
        androidx.core.animation.ObjectAnimator.ofInt(
            this@scrollToTop,
            "scrollX",
            0
        ).let { xTranslate ->
            androidx.core.animation.ObjectAnimator.ofInt(
                this@scrollToTop,
                "scrollY",
                0
            ).let { yTranslate ->
                androidx.core.animation.AnimatorSet().apply {
                    duration = 300L
                    playTogether(xTranslate, yTranslate)
                    start()
                }
            }
        }
    }
}

suspend fun android.view.View.extendForSplash(to: Float) {
    com.pt.common.stable.withMain {
        androidx.core.animation.ObjectAnimator.ofFloat(
            this@extendForSplash,
            com.pt.common.BuildConfig.SCALE,
            to
        ).let { xTranslate ->
            androidx.core.animation.AnimatorSet().apply {
                duration = 300L
                play(xTranslate)
                start()
            }
        }
    }
}

suspend fun android.view.View.animateTranRot(
    from: Float,
    to: Float,
    viewY: Float,
    targetY: Float,
    d: Long,
) {
    com.pt.common.stable.withMain {
        androidx.core.animation.ObjectAnimator.ofFloat(
            this@animateTranRot,
            com.pt.common.BuildConfig.TRANSLATION_Y,
            viewY,
            targetY
        ).let { y ->
            androidx.core.animation.ObjectAnimator.ofFloat(
                this@animateTranRot,
                com.pt.common.BuildConfig.ROTATION_DIM,
                from,
                to
            ).let { r ->
                androidx.core.animation.AnimatorSet().apply {
                    playTogether(y, r)
                    duration = d
                    start()
                }
            }
        }
    }
}

suspend fun android.view.View.spinForLast() {
    com.pt.common.stable.withMain {
        androidx.core.animation.ObjectAnimator.ofFloat(
            this@spinForLast,
            com.pt.common.BuildConfig.ROTATION_DIM,
            360F
        ).apply {
            duration = 1000L
            interpolator = androidx.core.animation.LinearInterpolator()
            start()
        }
    }
}

suspend fun android.view.View.changeWeight(from: Float, to: Float) {
    com.pt.common.stable.withMain {
        androidx.core.animation.ValueAnimator.ofFloat(from, to).apply {
            duration = 300L
            interpolator = androidx.core.animation.LinearInterpolator()
            addUpdateListener {
                layoutParams = androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
                    -1,
                    -1,
                    (animatedValue as Float)
                )
            }
            start()
        }
    }
}

suspend fun android.view.View.spinForEver(): androidx.core.animation.ObjectAnimator =
    com.pt.common.stable.justCoroutine {
        androidx.core.animation.ObjectAnimator.ofFloat(
            this@spinForEver,
            com.pt.common.BuildConfig.ROTATION_DIM,
            0F,
            360F
        ).apply {
            duration = 4000L
            repeatCount = android.view.animation.Animation.INFINITE
            interpolator = androidx.core.animation.LinearInterpolator()
            repeatMode = androidx.core.animation.ObjectAnimator.RESTART
        }
    }

suspend fun android.view.View.getSpringAnimation(
    dir: Int,
    finalPos: Float,
) {
    com.pt.common.stable.withMain {
        androidx.dynamicanimation.animation.SpringForce().apply {
            finalPosition = finalPos
            dampingRatio = androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_NO_BOUNCY
            stiffness = androidx.dynamicanimation.animation.SpringForce.STIFFNESS_MEDIUM
        }.let {
            androidx.dynamicanimation.animation.SpringAnimation(
                this@getSpringAnimation,
                if (dir == 0) androidx.dynamicanimation.animation.DynamicAnimation.SCALE_X else androidx.dynamicanimation.animation.DynamicAnimation.SCALE_Y
            ).apply {
                spring = it
                start()
            }
        }
    }
}

@com.pt.common.global.UiAnn
fun androidx.appcompat.widget.AppCompatSeekBar.animateSeekBar(value: Int) {
    androidx.core.animation.ObjectAnimator.ofInt(
        this,
        com.pt.common.BuildConfig.PROGRESS_CONST,
        value
    ).apply {
        duration = 400
        start()
    }
}

fun android.content.Context.translateRec(
    dur: Long,
    del: Float,
): android.view.animation.LayoutAnimationController {
    return android.view.animation.TranslateAnimation(0F, 0F, -20F, 0F).let {
        when {
            dur < 50L -> 50L
            dur > 250L -> 250L
            else -> dur
        }.let { itD ->
            it.duration = itD
        }
        android.view.animation.LayoutAnimationController(it).apply {
            order = android.view.animation.LayoutAnimationController.ORDER_NORMAL
            delay = del
            interpolator = android.view.animation.LinearInterpolator()
        }
    }
}

fun android.content.Context.fallDownAnimationRec(
    dur: Long,
    del: Float,
): android.view.animation.LayoutAnimationController {
    return android.view.animation.AnimationSet(true).apply {
        android.view.animation.TranslateAnimation(0F, 0F, -20F, 0F).also(this@apply::addAnimation)
        android.view.animation.ScaleAnimation(
            1.05F, 1F,
            1.05F, 1F,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
        ).also(this@apply::addAnimation)
    }.let {
        when {
            dur < 50L -> 50L
            dur > 250L -> 250L
            else -> dur
        }.let { itD ->
            it.duration = itD
        }
        android.view.animation.LayoutAnimationController(it).apply {
            order = android.view.animation.LayoutAnimationController.ORDER_NORMAL
            delay = del
            interpolator = android.view.animation.LinearInterpolator()
        }
    }
}

inline val scaleDownAnimation: android.view.animation.ScaleAnimation
    get() = android.view.animation.ScaleAnimation(
        1.05F, 1F,
        1.05F, 1F,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
    ).also {
        it.duration = 200
        it.interpolator = android.view.animation.DecelerateInterpolator()
    }

inline val scaleDownAnimationForMain: (Float) -> android.view.animation.ScaleAnimation
    get() = { f ->
        android.view.animation.ScaleAnimation(
            f, 1F,
            f, 1F,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
            android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
        ).also {
            it.duration = 200
            it.interpolator = android.view.animation.DecelerateInterpolator()
        }
    }

inline val shakeAnimation: android.view.animation.RotateAnimation
    get() = android.view.animation.RotateAnimation(
        -30F, 30F,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
    ).also {
        it.duration = 100
        it.repeatCount = 2
        it.repeatMode = android.view.animation.Animation.REVERSE
        it.interpolator = android.view.animation.DecelerateInterpolator()
    }

fun android.content.Context.scaleDownAnimationRec(
    dur: Long,
    del: Float,
): android.view.animation.LayoutAnimationController {
    return android.view.animation.ScaleAnimation(
        1.05F, 1F,
        1.05F, 1F,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F,
        android.view.animation.Animation.RELATIVE_TO_SELF, 0.5F
    ).let {
        when {
            dur < 50L -> 50L
            dur > 250L -> 250L
            else -> dur
        }.let { itD ->
            it.duration = itD
        }
        android.view.animation.LayoutAnimationController(it).apply {
            order = android.view.animation.LayoutAnimationController.ORDER_NORMAL
            delay = del
            interpolator = android.view.animation.LinearInterpolator()
        }
    }
}

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
fun android.content.Context.recyclerAnim(
    @androidx.annotation.AnimRes animRec: Int,
    dur: Long,
    del: Float,
): android.view.animation.LayoutAnimationController {
    return android.view.animation.AnimationUtils.loadAnimation(this, animRec).let {
        when {
            dur < 50L -> 50L
            dur > 250L -> 250L
            else -> dur
        }.let { itD ->
            it.duration = itD
        }
        android.view.animation.LayoutAnimationController(it).apply {
            order = android.view.animation.LayoutAnimationController.ORDER_NORMAL
            delay = del
            interpolator = android.view.animation.LinearInterpolator()
        }
    }
}

suspend fun android.view.View?.justVisibleSus() {
    com.pt.common.stable.withMain {
        if (this@justVisibleSus?.visibility != android.view.View.VISIBLE)
            @com.pt.common.global.UiAnn
            this@justVisibleSus?.visibility = android.view.View.VISIBLE
    }
}

suspend fun android.view.View?.justInvisibleSus() {
    com.pt.common.stable.withMain {
        if (this@justInvisibleSus?.visibility != android.view.View.INVISIBLE)
            @com.pt.common.global.UiAnn
            this@justInvisibleSus?.visibility = android.view.View.INVISIBLE
    }
}

suspend fun android.view.View?.justGoneSus() {
    com.pt.common.stable.withMain {
        if (this@justGoneSus?.visibility != android.view.View.GONE)
            @com.pt.common.global.UiAnn
            this@justGoneSus?.visibility = android.view.View.GONE
    }
}

suspend fun android.view.View.visibleFadeSus(d: Long) {
    com.pt.common.stable.withMain {
        androidx.transition.Fade(androidx.transition.Visibility.MODE_IN).apply {
            duration = d
            addTarget(this@visibleFadeSus)
        }.also(::fetchParent)
        @com.pt.common.global.UiAnn
        visibility = android.view.View.VISIBLE
    }
}

suspend fun android.view.View.invisibleFadeSus(d: Long) {
    com.pt.common.stable.withMain {
        androidx.transition.Fade(androidx.transition.Visibility.MODE_OUT).apply {
            duration = d
            addTarget(this@invisibleFadeSus)
        }.also(::fetchParent)
        @com.pt.common.global.UiAnn
        visibility = android.view.View.INVISIBLE
    }
}

suspend fun android.view.View.goneFadeSus(d: Long) {
    com.pt.common.stable.withMain {
        if (visibility == android.view.View.GONE) return@withMain
        androidx.transition.Fade(androidx.transition.Visibility.MODE_OUT).apply {
            duration = d
            addTarget(this@goneFadeSus)
        }.also(::fetchParent)
        @com.pt.common.global.UiAnn
        visibility = android.view.View.GONE
    }
}

suspend fun android.view.View.visibleTopSus(d: Long) {
    com.pt.common.stable.withMain {
        androidx.transition.Slide(android.view.Gravity.TOP).apply {
            duration = d
            addTarget(this@visibleTopSus)
        }.also(::fetchParent)
        @com.pt.common.global.UiAnn
        visibility = android.view.View.VISIBLE
    }
}

internal fun android.view.View.fetchParent(a: androidx.transition.Transition) {
    (this@fetchParent.parent as? android.view.ViewGroup?)?.also {
        androidx.transition.TransitionManager.beginDelayedTransition(
            it,
            a
        )
    }
}

fun android.view.View.invisibleTop() {
    androidx.transition.Slide(android.view.Gravity.TOP).apply {
        duration = 300L
        addTarget(this@invisibleTop)
        (this@invisibleTop.parent as? android.view.ViewGroup?)?.also {
            androidx.transition.TransitionManager.beginDelayedTransition(
                it,
                this
            )
        }
    }
    @com.pt.common.global.UiAnn
    visibility = android.view.View.INVISIBLE
}

suspend fun android.view.View.invisibleTopSus() {
    com.pt.common.stable.withMain {
        androidx.transition.Slide(android.view.Gravity.TOP).apply {
            duration = 300L
            addTarget(this@invisibleTopSus)
        }.also(::fetchParent)
        @com.pt.common.global.UiAnn
        visibility = android.view.View.INVISIBLE
    }
}

fun android.widget.FrameLayout.goneHandler() {
    handler?.postDelayed({
        @com.pt.common.global.UiAnn
        visibility = android.view.View.GONE
    }, 300)
}

suspend fun android.widget.FrameLayout.goneHandlerSus() {
    com.pt.common.stable.withMain {
        handler?.postDelayed({
            @com.pt.common.global.UiAnn
            visibility = android.view.View.GONE
        }, 300)
    }
}

suspend fun android.view.View.visibleStartSus(d: Long) {
    com.pt.common.stable.withMain {
        androidx.transition.Slide(android.view.Gravity.START).apply {
            duration = d
            addTarget(this@visibleStartSus)
        }.also(::fetchParent)
        @com.pt.common.global.UiAnn
        visibility = android.view.View.VISIBLE
    }
}

suspend fun android.view.View.goneStartSus(d: Long) {
    com.pt.common.stable.withMain {
        if (visibility == android.view.View.GONE) return@withMain
        androidx.transition.Slide(android.view.Gravity.START).apply {
            duration = d
            addTarget(this@goneStartSus)
        }.also(::fetchParent)
        @com.pt.common.global.UiAnn
        visibility = android.view.View.GONE
    }
}

suspend fun android.view.View.visibleEndSus(d: Long) {
    com.pt.common.stable.withMain {
        androidx.transition.Slide(android.view.Gravity.END).apply {
            duration = d
            addTarget(this@visibleEndSus)
        }.also(::fetchParent)
        @com.pt.common.global.UiAnn
        visibility = android.view.View.VISIBLE
    }
}

suspend fun android.view.View.goneEndSus(d: Long) {
    com.pt.common.stable.withMain {
        if (visibility == android.view.View.GONE) return@withMain
        androidx.transition.Slide(android.view.Gravity.END).apply {
            duration = d
            addTarget(this@goneEndSus)
        }.also(::fetchParent)
        @com.pt.common.global.UiAnn
        visibility = android.view.View.GONE
    }
}

@com.pt.common.global.UiAnn
fun android.view.View.visibleEnd(d: Long) {
    androidx.transition.Slide(android.view.Gravity.END).apply {
        duration = d
        addTarget(this@visibleEnd)
    }.also(::fetchParent)
    visibility = android.view.View.VISIBLE
}

@com.pt.common.global.UiAnn
fun android.view.View.inVisibleEnd(d: Long) {
    androidx.transition.Slide(android.view.Gravity.END).apply {
        duration = d
        addTarget(this@inVisibleEnd)
    }.also(::fetchParent)
    visibility = android.view.View.INVISIBLE
}