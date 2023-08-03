package com.pt.common.moderator.touch

import com.pt.common.global.isV_M

interface SenseListener : android.view.View.OnTouchListener {

    fun onUp(v: android.view.View, event: android.view.MotionEvent): Boolean
    fun onDown(v: android.view.View, event: android.view.MotionEvent): Boolean
    fun onMove(v: android.view.View, event: android.view.MotionEvent): Boolean

    override fun onTouch(v: android.view.View?, event: android.view.MotionEvent?): Boolean {
        (v ?: return false)
        (event ?: return false)
        event.apply {
            if (action == android.view.MotionEvent.ACTION_UP ||
                actionMasked == android.view.MotionEvent.ACTION_UP ||
                action == android.view.MotionEvent.ACTION_POINTER_UP ||
                actionMasked == android.view.MotionEvent.ACTION_POINTER_UP ||
                (isV_M && actionButton == android.view.MotionEvent.ACTION_BUTTON_RELEASE) ||
                action == android.view.MotionEvent.ACTION_CANCEL ||
                actionMasked == android.view.MotionEvent.ACTION_CANCEL
            ) {
                v.performClick()
                return onUp(v, event)
            } else if (action == android.view.MotionEvent.ACTION_DOWN ||
                actionMasked == android.view.MotionEvent.ACTION_DOWN
            ) {
                return onDown(v, event)
            } else if (action == android.view.MotionEvent.ACTION_MOVE ||
                actionMasked == android.view.MotionEvent.ACTION_MOVE
            ) {
                return onMove(v, event)
            }
        }
        return false
    }

}