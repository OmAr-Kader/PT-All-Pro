package com.pt.pro.gallery.fasten

data class PopWindowGalleryFasten(
    val root_: androidx.cardview.widget.CardView,
    val delete: androidx.appcompat.widget.AppCompatTextView,
    val share: androidx.appcompat.widget.AppCompatTextView,
    val properties: androidx.appcompat.widget.AppCompatTextView,
    val selected: androidx.appcompat.widget.AppCompatTextView,
    val favDis: androidx.appcompat.widget.AppCompatTextView,
    val unFavDis: androidx.appcompat.widget.AppCompatTextView,
    val setAs: androidx.appcompat.widget.AppCompatTextView,
    val rename: androidx.appcompat.widget.AppCompatTextView,
    val hide: androidx.appcompat.widget.AppCompatTextView,
    val sendScanner: androidx.appcompat.widget.AppCompatTextView,
    val clipboard: androidx.appcompat.widget.AppCompatTextView,
    val pendingDisplay: androidx.appcompat.widget.AppCompatTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.cardview.widget.CardView = root_
}