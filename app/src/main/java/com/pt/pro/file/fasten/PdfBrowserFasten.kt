package com.pt.pro.file.fasten

data class PdfBrowserFasten(
    val root_: com.sothree.slidinguppanel.SlidingUpPanelLayout,
    val emptyFrame: android.widget.FrameLayout,
    val imagePager: androidx.viewpager2.widget.ViewPager2,
    val head: android.widget.FrameLayout,
    val headInner: androidx.constraintlayout.widget.ConstraintLayout,
    val lockBrowser: com.pt.common.moderator.over.ResizeImageView,
    val picName: androidx.appcompat.widget.AppCompatTextView,
    val unLockImage: com.pt.common.moderator.touch.TouchImageView,
    val headDown: android.widget.FrameLayout,
    val headOptions: androidx.constraintlayout.widget.ConstraintLayout,
    val pageNum: androidx.appcompat.widget.AppCompatTextView,
    val prePage: com.pt.common.moderator.over.ResizeImageView,
    val nextPage: com.pt.common.moderator.over.ResizeImageView,
    val rotateScreen: com.pt.common.moderator.over.ResizeImageView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): com.sothree.slidinguppanel.SlidingUpPanelLayout = root_
}