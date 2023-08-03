package com.pt.pro.musical.fasten

data class ActivityMusicFasten(
    val root_: android.widget.FrameLayout,
    val backImg: androidx.appcompat.widget.AppCompatImageView,
    val linView: androidx.appcompat.widget.LinearLayoutCompat,
    val cardView: androidx.cardview.widget.CardView,
    val coverImg: androidx.appcompat.widget.AppCompatImageView,
    val circleSeekBar: com.pt.common.moderator.recycler.CircularSeekBar,
    val musicArtist: androidx.appcompat.widget.AppCompatTextView,
    val musicTitle: androidx.appcompat.widget.AppCompatTextView,
    val playPause: androidx.appcompat.widget.AppCompatImageView,
    val previousMusic: androidx.appcompat.widget.AppCompatImageView,
    val nextMusic: androidx.appcompat.widget.AppCompatImageView,
    val unlockScreen: androidx.appcompat.widget.AppCompatButton,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}