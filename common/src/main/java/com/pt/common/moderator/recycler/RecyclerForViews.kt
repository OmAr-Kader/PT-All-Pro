package com.pt.common.moderator.recycler

open class RecyclerForViews @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = android.R.attr.scrollViewStyle,
) : androidx.recyclerview.widget.RecyclerView(context!!, attrs, defStyle) {

    init {
        @Suppress("DEPRECATION")
        isAnimationCacheEnabled = false
        overScrollMode = android.view.View.OVER_SCROLL_NEVER
        itemAnimator = null

        isScrollbarFadingEnabled = false
        isVerticalFadingEdgeEnabled = false
        isHorizontalFadingEdgeEnabled = false

        isNestedScrollingEnabled = true
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
    }


    suspend fun clearRecyclerPool() {
        com.pt.common.stable.withMain {
            recycledViewPool.clear()
            stopScroll()
        }
    }

}