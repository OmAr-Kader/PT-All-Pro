package com.pt.pro.musical.fasten


data class MusicHeadPlayerFasten(
    val root_: android.widget.FrameLayout,
    val frameDetails: android.widget.FrameLayout,
    val playList: androidx.appcompat.widget.AppCompatImageView,
    val playMusic: androidx.appcompat.widget.AppCompatImageView,
    val nextMusic: androidx.appcompat.widget.AppCompatImageView,
    val refiner: androidx.appcompat.widget.AppCompatImageView,
    val hideCircle: androidx.appcompat.widget.AppCompatImageView,
    val previousMusic: androidx.appcompat.widget.AppCompatImageView,
    val musicDetails: com.pt.common.moderator.over.LinearTint,
    val musicArtist: androidx.appcompat.widget.AppCompatTextView,
    val musicTitle: androidx.appcompat.widget.AppCompatTextView,
    val stubRecycler: android.widget.FrameLayout,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}