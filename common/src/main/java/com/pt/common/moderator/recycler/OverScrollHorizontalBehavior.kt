package com.pt.common.moderator.recycler

import com.pt.common.stable.OVER_SCROLL_AREA

open class OverScrollHorizontalBehavior @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
) : androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<android.view.View>(
    context,
    attrs
) {


    private var overScrollX: Float = 0F

    override fun onStartNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: android.view.View,
        directTargetChild: android.view.View,
        target: android.view.View,
        axes: Int,
        type: Int
    ): Boolean {
        overScrollX = 0F
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
        if (dxUnconsumed == 0) {
            return
        }
        if (type == androidx.core.view.ViewCompat.TYPE_TOUCH) {
            @Strictfp
            overScrollX -= (dxUnconsumed.toFloat() / OVER_SCROLL_AREA)
            (target as? android.view.ViewGroup?)?.also { t ->
                //t.children.forEach {
                t.translationX = overScrollX
                //}
            }
        }
    }

    override fun onStopNestedScroll(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: android.view.View,
        target: android.view.View,
        type: Int
    ) {
        if (type == androidx.core.view.ViewCompat.TYPE_TOUCH) {
            moveToDefPosition(target)
        }
    }

    override fun onNestedPreFling(
        coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout,
        child: android.view.View,
        target: android.view.View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (overScrollX == 0F) {
            return false
        }
        moveToDefPosition(target)
        return true
    }

    private fun moveToDefPosition(target: android.view.View) {
        (target as? android.view.ViewGroup?)?.also { t ->
            //t.children.forEach {
            androidx.core.view.ViewCompat.animate(t).apply {
                translationX(0F)
                duration = 200L
                interpolator = android.view.animation.AccelerateInterpolator()
            }.start()
            //}
        }
    }

}