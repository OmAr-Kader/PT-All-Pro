package com.pt.pro.file.fasten

data class FileOptionsFasten(
    val root_: androidx.cardview.widget.CardView,
    val hidden: androidx.appcompat.widget.AppCompatImageView,
    val searchFile: androidx.appcompat.widget.AppCompatImageView,
    val pendingFrame: android.widget.FrameLayout,
    val pendingText: androidx.appcompat.widget.AppCompatTextView,
    val createFolder: androidx.appcompat.widget.AppCompatImageView,
    val reSort: androidx.appcompat.widget.AppCompatImageView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.cardview.widget.CardView = root_
}