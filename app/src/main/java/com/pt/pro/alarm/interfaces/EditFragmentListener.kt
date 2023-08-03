package com.pt.pro.alarm.interfaces

interface EditFragmentListener {
    var ala: com.pt.common.global.AlarmSack?
    var editAlarmListener: EditAlarmListener?
    fun swipeWay(way: Int)

}