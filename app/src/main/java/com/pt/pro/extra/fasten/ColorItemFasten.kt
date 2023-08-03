package com.pt.pro.extra.fasten

data class ColorItemFasten(
    val root_: androidx.cardview.widget.CardView,
    val colorButton: androidx.appcompat.widget.AppCompatImageView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.cardview.widget.CardView = root_
}