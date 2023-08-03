package com.pt.pro.extra.fasten

data class ActivitySettingFasten(
    val root_: android.widget.FrameLayout,
    val cardSetting: com.pt.common.moderator.over.FrameTint,
    val textPt: androidx.appcompat.widget.AppCompatTextView,
    val tittlePager: androidx.viewpager2.widget.ViewPager2,
    val mainBack: com.pt.common.moderator.over.ResizeImageView,
    val clickPrevious: com.pt.common.moderator.over.ResizeImageView,
    val clickNext: com.pt.common.moderator.over.ResizeImageView,
    val frameSetting: androidx.constraintlayout.widget.ConstraintLayout,
    val viewForAd: android.widget.FrameLayout,
    val swipePager: androidx.viewpager2.widget.ViewPager2,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}