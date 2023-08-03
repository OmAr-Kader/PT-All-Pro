package com.pt.common.moderator.recycler

import com.pt.common.stable.OVER_SCROLL_AREA

open class OverScrollBounceBehavior @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<android.view.View>(
    context,
    attrs
) {

    private var overScrollY: Float = 0F

    override fun onStartNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: android.view.View,
        directTargetChild: android.view.View,
        target: android.view.View,
        axes: Int,
        type: Int
    ): Boolean {
        overScrollY = 0F
        return true
    }

    override fun onNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: android.view.View,
        target: android.view.View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (dyUnconsumed == 0) {
            return
        }
        if (type == androidx.core.view.ViewCompat.TYPE_TOUCH) {
            @Strictfp
            overScrollY -= (dyUnconsumed.toFloat() / OVER_SCROLL_AREA)
            (target as? android.view.ViewGroup?)?.also { t ->
                t.translationY = overScrollY
            }
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: android.view.View,
        target: android.view.View,
        type: Int
    ) {
        //if (type == androidx.core.view.ViewCompat.TYPE_TOUCH) {
        moveToDefPosition(target)
        //}
    }

    override fun onNestedPreFling(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: android.view.View,
        target: android.view.View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (overScrollY == 0F) {
            return false
        }
        moveToDefPosition(target)
        return true
    }

    private fun moveToDefPosition(target: android.view.View) {
        (target as? android.view.ViewGroup?)?.also { t ->
            //t.children.forEach {
            androidx.core.view.ViewCompat.animate(t).apply {
                translationY(0F)
                duration = 200L
                interpolator = android.view.animation.AccelerateInterpolator()
            }.start()
            //}
        }
    }

}