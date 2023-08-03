package com.pt.common.objects

data class Alarm(
    val idAlarm: Int,
    val timeAlarm: Long,
    val labelAlarm: String?,
    val dismissWay: Int,
    val ringAlarm: String?,
    val recordAlarm: String?,
    val isAlarm: Boolean,
    val isVibrate: Boolean,
    val switchAlarm: Boolean,
    val allDays: MutableList<Boolean> = mutableListOf<Boolean>().apply {
        repeat(7) { add(false) }
    },
    val imgBackground: String?,
)