package com.pt.pro.file.fasten

data class PopOptionFasten(
    val root_: androidx.cardview.widget.CardView,
    val cardOne: androidx.cardview.widget.CardView,
    val audioPic: androidx.appcompat.widget.AppCompatImageView,
    val txtOne: androidx.appcompat.widget.AppCompatTextView,
    val audioFrame: android.widget.FrameLayout,
    val cardTwo: androidx.cardview.widget.CardView,
    val musicPic: androidx.appcompat.widget.AppCompatImageView,
    val txtTwo: androidx.appcompat.widget.AppCompatTextView,
    val musicFrame: android.widget.FrameLayout,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.cardview.widget.CardView = root_
}