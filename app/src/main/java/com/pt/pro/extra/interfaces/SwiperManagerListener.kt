package com.pt.pro.extra.interfaces

interface SwiperManagerListener {
    val windowSwiper: android.view.WindowManager
    fun android.content.Intent.setDataIntent(resultCode: Int)
    fun broadcast()
    fun stopRecording()
    fun touchScreenShot(resultCode: Int, data: android.content.Intent)
    fun removeTintVew(forceColor: Boolean)
    suspend fun removeTintSus(forceColor: Boolean)
    fun recreateScannerPort()
    fun recreateScannerLand()
    fun android.view.View.animationOpen(beginValue: Int, endValue: Int, isNeedAnim: Boolean)
    fun android.view.View.animationUp(beginValue: Int, endValue: Int, isNeedAnim: Boolean)
}