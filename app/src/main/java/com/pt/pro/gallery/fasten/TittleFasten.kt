package com.pt.pro.gallery.fasten

data class TittleFasten(
    val root_: android.widget.FrameLayout,
    val fileName: androidx.appcompat.widget.AppCompatTextView,
    val extend: androidx.appcompat.widget.AppCompatTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}