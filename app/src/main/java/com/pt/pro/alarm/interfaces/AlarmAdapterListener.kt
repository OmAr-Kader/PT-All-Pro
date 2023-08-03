package com.pt.pro.alarm.interfaces

interface AlarmAdapterListener {
    var allAlarms: MutableList<com.pt.common.global.AlarmSack>
    val nightRider: Boolean
    val is24: Boolean
    var refreshListener: AlarmMainListener?

    suspend fun MutableList<com.pt.common.global.AlarmSack>.setAlarms()

    fun Array<String>.buildSelectedDays(
        alarm: com.pt.common.global.AlarmSack,
        mAccentColor: Int
    ): android.text.Spannable

    fun onAdapterDestroy()
}