package com.pt.common.moderator.recycler

open class NoAnimLinearManager(
    context: android.content.Context,
    @androidx.recyclerview.widget.RecyclerView.Orientation orientation: Int,
    reverseLayout: Boolean
) : androidx.recyclerview.widget.LinearLayoutManager(context, orientation, reverseLayout) {

    override fun supportsPredictiveItemAnimations(): Boolean = false

}