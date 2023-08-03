package com.pt.common.mutual.base

interface OnConfigurationChanged {
    val fetchParams: android.view.WindowManager.LayoutParams
    val getViewRoot: android.view.View
    val getViewRoot2: android.view.View
    fun onChange(newConfig: Int)
    fun onServiceDestroy(b: () -> Unit)

    fun updateViewRoot()
    fun setInit()
    fun android.view.WindowManager.setWindowManager()
    fun onKeyBoardOpened()
    fun onKeyBoardClosed()
}