package com.pt.common.moderator.touch

interface ContactListener {
    fun onUp(v: android.view.View)
    fun onUp(v: android.view.View, event: android.view.MotionEvent) {}
    fun onDown(v: android.view.View, event: android.view.MotionEvent)
    fun onMove(v: android.view.View, event: android.view.MotionEvent)
}