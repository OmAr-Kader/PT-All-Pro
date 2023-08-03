package com.pt.pro.file.fasten

data class FileShareFasten(
    val root_: androidx.cardview.widget.CardView,
    val scrollCounter: androidx.core.widget.NestedScrollView,
    val linearScroll: androidx.appcompat.widget.LinearLayoutCompat,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.cardview.widget.CardView = root_
}