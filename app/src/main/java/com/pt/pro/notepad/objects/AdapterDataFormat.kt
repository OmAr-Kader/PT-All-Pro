package com.pt.pro.notepad.objects

import com.pt.common.global.toDefString

internal inline val com.pt.common.global.DSack<Int, String, String>.getMonth: String
    get() {
        return when {
            one >= 7 -> {
                val firstSubString = two.toDefString(5) + ".."
                "<br>$firstSubString<br>$three"
            }
            one == 3 -> {
                "<br>$three<br><b>$two</b>"
            }
            else -> {
                "<br>$two<br>$three"
            }
        }
    }

fun adapterTime(length_: Long): String {
    val calendar = com.pt.common.global.CalendarLate.getInstance()
    calendar.timeInMillis = length_
    val hour = calendar[com.pt.common.global.CalendarLate.MINUTE]
    val minute = calendar[com.pt.common.global.CalendarLate.SECOND]
    val s1: String = if (minute < 10) {
        "0$minute"
    } else {
        minute.toString()
    }
    val s: String = if (hour < 10) {
        "0$hour"
    } else {
        hour.toString()
    }
    return "$s:$s1"
}

fun createTime(): com.pt.common.global.CalendarLate {
    val currentTime = System.currentTimeMillis()
    val calendar = com.pt.common.global.CalendarLate.getInstance()
    calendar.timeInMillis = currentTime
    val year = calendar[com.pt.common.global.CalendarLate.YEAR]
    val month = calendar[com.pt.common.global.CalendarLate.MONTH]
    return com.pt.common.global.CalendarLate.getInstance().apply {
        this[com.pt.common.global.CalendarLate.YEAR] = year
        this[com.pt.common.global.CalendarLate.MONTH] = month
        this[com.pt.common.global.CalendarLate.DAY_OF_MONTH] = 1
        this[com.pt.common.global.CalendarLate.HOUR_OF_DAY] = 0
        this[com.pt.common.global.CalendarLate.MINUTE] = 0
        this[com.pt.common.global.CalendarLate.SECOND] = 0
    }
}

@Suppress("HardCodedStringLiteral")
fun returnTableTitle(ss: String, tim: java.util.Date): String {
    val month = android.text.format.DateFormat.format("MMM", tim) as String // Jun
    val year = android.text.format.DateFormat.format("yyyy", tim) as String // 2013

    val filtered = ".,;:/ "
    val monthString = month.filterNot { filtered.indexOf(it) > -1 }
    return monthString + "_" + year + ss
}

@Suppress("HardCodedStringLiteral")
fun returnTableName(ss: String, tim: java.util.Date): String {
    val simpleDateFormat = java.text.SimpleDateFormat("MMM", java.util.Locale.US)
    val simpleDateFormat2 = java.text.SimpleDateFormat("yyyy", java.util.Locale.US)

    val monthFailed = simpleDateFormat.format(tim)
    val yearFailed = simpleDateFormat2.format(tim)

    val filteredFailed = ".,;:/ "
    val monthStringFailed = monthFailed.filterNot { filteredFailed.indexOf(it) > -1 }
    return monthStringFailed + "_" + yearFailed + ss
}