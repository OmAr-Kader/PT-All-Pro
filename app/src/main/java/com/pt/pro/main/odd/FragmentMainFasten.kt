package com.pt.pro.main.odd

data class FragmentMainFasten(
    val root_: com.pt.common.moderator.over.FrameSizer,
    val cardMain: androidx.constraintlayout.widget.ConstraintLayout,
    val mainBack: com.pt.common.moderator.over.ResizeImageView,
    val textPt: androidx.appcompat.widget.AppCompatTextView,
    val pagerMenu: com.pt.common.moderator.over.ResizeImageView,
    val frameRec: android.widget.FrameLayout,
    val iconsRecyclerView: com.pt.common.moderator.recycler.RecyclerForViews,
) : androidx.viewbinding.ViewBinding {


    override fun getRoot(): com.pt.common.moderator.over.FrameSizer = root_
}