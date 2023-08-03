package com.pt.pro.main.former

interface MainAdapterListener {
    var icons: MutableList<Int>
    val widthIcon: Int
    val heightIcon: Int
    var lastApp: Int
    var refreshMode: Int
    var iconListener: ((Int) -> Unit)?
    val isNight: Boolean
    suspend fun MutableList<Int>.updateIcons()
    fun onAdapterDestroy()
    fun refreshAdapter(newApp: Int, mode: Int)
    fun androidx.appcompat.widget.AppCompatImageView.setAnimImage(
        re: com.pt.common.global.DSackT<Int, Int>,
        anim: androidx.vectordrawable.graphics.drawable.Animatable2Compat.() -> Unit,
    )

    fun androidx.appcompat.widget.AppCompatImageView.setImage(
        @androidx.annotation.DrawableRes stan: Int
    )
}