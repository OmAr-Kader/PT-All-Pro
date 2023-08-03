package com.pt.pro.gallery.fasten

data class StubDeleteFasten(
    val root_: android.widget.FrameLayout,
    val deleteFrame: androidx.cardview.widget.CardView,
    val snakeText: androidx.appcompat.widget.AppCompatTextView,
    val cancelDelete: androidx.appcompat.widget.AppCompatButton,
    val confirmDelete: androidx.appcompat.widget.AppCompatButton,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}