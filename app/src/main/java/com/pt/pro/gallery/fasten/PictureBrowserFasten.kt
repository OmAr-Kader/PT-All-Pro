package com.pt.pro.gallery.fasten

data class PictureBrowserFasten(
    val root_: com.sothree.slidinguppanel.SlidingUpPanelLayout,
    val emptyFrame: android.widget.FrameLayout,
    val imagePager: androidx.viewpager2.widget.ViewPager2,
    val head: android.widget.FrameLayout,
    val headInner: androidx.constraintlayout.widget.ConstraintLayout,
    val pagerMenu: com.pt.common.moderator.over.ResizeImageView,
    val picName: androidx.appcompat.widget.AppCompatTextView,
    val unLockImage: com.pt.common.moderator.touch.TouchImageView,
    val rotationBarCard: androidx.cardview.widget.CardView,
    val rotationBarProgressBar: androidx.core.widget.ContentLoadingProgressBar,
    val headDown: android.widget.FrameLayout,
    val headOptions: androidx.constraintlayout.widget.ConstraintLayout,
    val videoDuration: androidx.appcompat.widget.AppCompatTextView,
    val currentDuration: android.widget.Chronometer,
    val floatingVideo: com.pt.common.moderator.over.ResizeImageView,
    val isFavorite: com.pt.common.moderator.over.ResizeImageView,
    val editActivity: com.pt.common.moderator.over.ResizeImageView,
    val rotateScreen: com.pt.common.moderator.over.ResizeImageView,
    val lockBrowser: com.pt.common.moderator.over.ResizeImageView,
    val lowerVideoOptions: android.widget.FrameLayout,
    val seekBar: androidx.appcompat.widget.AppCompatSeekBar,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): com.sothree.slidinguppanel.SlidingUpPanelLayout = root_
}