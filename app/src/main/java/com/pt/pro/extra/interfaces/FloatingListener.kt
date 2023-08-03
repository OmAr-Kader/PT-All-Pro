package com.pt.pro.extra.interfaces

interface FloatingListener {
    val headMine: com.pt.pro.databinding.FloatingHeadBinding
    val windowManage: android.view.WindowManager
    val headRestBall: com.pt.pro.databinding.FloatingHeadWindowsBinding

    fun com.pt.pro.databinding.FloatingHeadBinding.setViewRoot2()
    fun animationOpenFloat(beginValue: Int, endValue: Int)
    fun com.pt.pro.databinding.FloatingHeadWindowsBinding.setViewRoot()

    fun setSerBoolean(serBool: Boolean)
    fun setResultCode(resultCode: Int)
    fun android.content.Intent.setDataIntent()
    fun com.pt.pro.databinding.FloatingHeadWindowsBinding.changeVisibility()
}