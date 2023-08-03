package com.pt.pro.file.interfaces

interface PopMusicListener : DialogListener, android.view.View.OnLongClickListener,
    android.view.View.OnClickListener {
    val currentMusic: String
    val onEverySecond: com.pt.common.global.DSackT<() -> Unit, Int>
    fun com.pt.pro.databinding.PopForMusicBinding.updateMusicViews(path: String)
    fun musicPlayerInit()
}