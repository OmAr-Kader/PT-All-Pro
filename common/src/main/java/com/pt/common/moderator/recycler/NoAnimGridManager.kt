package com.pt.common.moderator.recycler

open class NoAnimGridManager(
    context: android.content.Context?,
    spanCount: Int
) : androidx.recyclerview.widget.GridLayoutManager(context, spanCount, VERTICAL, false) {

    override fun supportsPredictiveItemAnimations(): Boolean = false

    fun setLook(a: (Int) -> Int) {
        spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = a.invoke(position)

        }
    }

}