package com.pt.pro.extra.fasten

data class TitleScreenFasten(
    val root_: androidx.constraintlayout.widget.ConstraintLayout,
    val title: androidx.appcompat.widget.AppCompatButton,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.constraintlayout.widget.ConstraintLayout = root_
}