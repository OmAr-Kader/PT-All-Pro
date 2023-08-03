package com.pt.pro.main.odd

data class MainIconFasten(
    val root_: android.widget.FrameLayout,
    val iconFrame: androidx.constraintlayout.widget.ConstraintLayout,
    val cardPic: androidx.cardview.widget.CardView,
    val iconPic: com.pt.common.moderator.over.FitWidthAtBottomImageView,
    val iconTxt: androidx.appcompat.widget.AppCompatTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}