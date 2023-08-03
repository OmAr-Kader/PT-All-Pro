package com.pt.pro.alarm.interfaces

interface AlarmMainListener {
    fun com.pt.common.global.AlarmSack.forLaunch()
    fun com.pt.common.global.AlarmSack.forSwitchOff(a: suspend () -> Unit)
    fun com.pt.common.global.AlarmSack.forSwitchOn(a: suspend () -> Unit)
    fun com.pt.common.global.AlarmSack.forDelete()
}