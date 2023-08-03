package com.pt.pro.musical.fasten

data class MusicHeadFasten(
    val root_: android.widget.FrameLayout,
    val circleFrame: android.widget.FrameLayout,
    val pro: com.pt.common.moderator.recycler.CircleProgressBar,
    val circleCard: androidx.cardview.widget.CardView,
    val circleMusic: com.pt.common.moderator.touch.TouchImageView
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}