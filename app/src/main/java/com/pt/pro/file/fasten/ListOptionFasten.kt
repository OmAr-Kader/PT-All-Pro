package com.pt.pro.file.fasten

data class ListOptionFasten(
    val root_: androidx.appcompat.widget.LinearLayoutCompat,
    val backOpt: android.widget.FrameLayout,
    val favorite: android.widget.FrameLayout?,
    val unFavorite: android.widget.FrameLayout?,
    val addVir: android.widget.FrameLayout?,
    val removeVir: android.widget.FrameLayout?,
    val openWith: android.widget.FrameLayout,
    val openAs: android.widget.FrameLayout,
    val shareOpt: android.widget.FrameLayout,
    val addPend: android.widget.FrameLayout,
    val deleteOpt: android.widget.FrameLayout?,
    val properties: android.widget.FrameLayout?,
    val selectAll: android.widget.FrameLayout,
    val renameOpt: android.widget.FrameLayout?,
    val hide: android.widget.FrameLayout?,
    val copy: android.widget.FrameLayout,
    val move: android.widget.FrameLayout?,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.appcompat.widget.LinearLayoutCompat = root_
}