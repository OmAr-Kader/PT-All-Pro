package com.pt.pro.file.fasten

data class ListNoteFasten(
    val root_: androidx.appcompat.widget.LinearLayoutCompat,
    val backOpt: android.widget.FrameLayout,
    val not: android.widget.FrameLayout?,
    val copy: android.widget.FrameLayout?,
    val share: android.widget.FrameLayout,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.appcompat.widget.LinearLayoutCompat = root_
}