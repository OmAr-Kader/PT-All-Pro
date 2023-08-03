package com.pt.pro.gallery.fasten

data class FragmentDisplayFasten(
    val root_: com.pt.common.moderator.over.FrameSizer,
    val displayContainer: android.widget.FrameLayout,
    val displayCard: androidx.constraintlayout.widget.ConstraintLayout,
    val backDisplay: com.pt.common.moderator.over.ResizeImageView,
    val searchEdit: androidx.appcompat.widget.AppCompatEditText,
    val displayTitle: androidx.appcompat.widget.AppCompatTextView,
    val searchIcon: com.pt.common.moderator.over.ResizeImageView,
    val reSort: com.pt.common.moderator.over.ResizeImageView,
    val stubOptionsDisplay: android.widget.FrameLayout,
    val displaySubFrame: android.widget.FrameLayout,
    val recycler: com.pt.common.moderator.recycler.RecyclerForViews,
    val pointerScroll: androidx.cardview.widget.CardView,
    val displayBarCard: androidx.cardview.widget.CardView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): com.pt.common.moderator.over.FrameSizer = root_
}