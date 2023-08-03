package com.pt.pro.musical.fasten

data class ItemSongFasten(
    val root_: android.widget.FrameLayout,
    val songImage: androidx.appcompat.widget.AppCompatImageView,
    val songTitle: androidx.appcompat.widget.AppCompatTextView,
    val songArtist: androidx.appcompat.widget.AppCompatTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}