package com.pt.pro.extra.interfaces

interface NaviListener {
    val ballNavi: com.pt.pro.databinding.FloatingHeadAppsBinding
    val windowManage: android.view.WindowManager
    val headRestBall: com.pt.pro.databinding.FloatingHeadWindowsBinding
    val volMinusRun: com.pt.common.global.DSackT<() -> Unit, Int>
    val volPlusRun: com.pt.common.global.DSackT<() -> Unit, Int>

    fun doBrtMin()
    fun doBrtMax()
    fun android.media.AudioManager.volume(inc: Boolean)
    suspend fun updateChanges()
    fun setSerBoolean(serBool: Boolean)
    fun com.pt.pro.databinding.FloatingHeadWindowsBinding.changeVisibilityNavi()
    fun com.pt.pro.databinding.FloatingHeadWindowsBinding.setViewRoot()
    fun com.pt.pro.databinding.FloatingHeadAppsBinding.setLayout2()
    fun animationOpenFloat(beginValue: Int, endValue: Int)
}