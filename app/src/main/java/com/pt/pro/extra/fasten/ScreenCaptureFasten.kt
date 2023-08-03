package com.pt.pro.extra.fasten

data class ScreenCaptureFasten(
    val root_: android.widget.FrameLayout,
    val cardScreen: android.widget.FrameLayout,
    val textPt: androidx.appcompat.widget.AppCompatTextView,
    val tittlePager: androidx.viewpager2.widget.ViewPager2,
    val mainBack: com.pt.common.moderator.over.ResizeImageView,
    val clickPrevious: com.pt.common.moderator.over.ResizeImageView,
    val clickNext: com.pt.common.moderator.over.ResizeImageView,
    val screenFrame: android.widget.FrameLayout,
    val swipePager: androidx.viewpager2.widget.ViewPager2,
    val slideText: androidx.appcompat.widget.AppCompatTextView,
    val seekLaunch: androidx.appcompat.widget.AppCompatSeekBar,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}
