package com.pt.pro.alarm.interfaces

interface AlarmDismiss {
    fun doDismiss() {}
    fun doSnooze() {}
    fun stopAlarm() {}
    fun userFinish() {}
}