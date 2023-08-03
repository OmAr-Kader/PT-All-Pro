package com.pt.pro.extra.interfaces

interface SwiperListener : android.widget.CompoundButton.OnCheckedChangeListener {
    fun doLaunch() {}
    fun doStop() {}
    fun setRunnable(b: (Boolean) -> Unit) {}
}