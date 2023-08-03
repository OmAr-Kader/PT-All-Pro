package com.pt.pro.alarm.decline

import com.pt.pro.alarm.interfaces.AlarmDismiss

interface DismissListener {
    var alarm: com.pt.common.global.AlarmSack?
    var colorImage: Int
    var alarmDismiss: AlarmDismiss?
    var test: Boolean
}