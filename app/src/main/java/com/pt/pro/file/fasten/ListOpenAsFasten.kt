package com.pt.pro.file.fasten

data class ListOpenAsFasten(
    val root_: androidx.appcompat.widget.LinearLayoutCompat,
    val backOpt: android.widget.FrameLayout,
    val textAs: android.widget.FrameLayout,
    val imageAs: android.widget.FrameLayout,
    val audioAs: android.widget.FrameLayout,
    val videoAs: android.widget.FrameLayout,
    val allAs: android.widget.FrameLayout,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.appcompat.widget.LinearLayoutCompat = root_
}