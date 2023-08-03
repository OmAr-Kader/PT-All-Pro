package com.pt.pro.extra.interfaces

interface ColorListener {
    val colors: MutableList<Int>
    val styles: MutableList<com.pt.common.global.DSackT<Int, Int>>
    var colorPosition: Int
    var extraItemListener: ((Int, Int, Int) -> Unit)?
    fun onAdapterDestroy()
}