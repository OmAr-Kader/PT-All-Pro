package com.pt.pro.gallery.fasten

data class VideoFragmentFasten(
    val root_: android.widget.FrameLayout,
    val frameVideo: com.pt.common.moderator.touch.TouchFrameLayout,
    val videoView: android.view.SurfaceView,
    val thumb: com.pt.common.moderator.over.GlideImageView,
    val playPause: androidx.appcompat.widget.AppCompatImageView,
    val upperOptionsVideo: android.widget.FrameLayout,
    val volume: androidx.appcompat.widget.AppCompatImageView,
    val seekScale: androidx.appcompat.widget.AppCompatSeekBar,
    val screenBrightness: androidx.appcompat.widget.AppCompatImageView,
    val forRearFrame: android.widget.FrameLayout,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}