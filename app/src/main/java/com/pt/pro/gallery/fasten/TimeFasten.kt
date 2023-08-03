package com.pt.pro.gallery.fasten

data class TimeFasten(
    val root_: android.widget.FrameLayout,
    val picture: com.pt.common.moderator.over.GlideImageView,
    val videoPlay: androidx.appcompat.widget.AppCompatImageView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}