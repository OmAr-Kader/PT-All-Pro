package com.pt.pro.alarm.objects

import com.pt.common.BuildConfig.*
import com.pt.common.global.*
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.notepad.receivers.DismissReceiverNormal
import kotlin.random.Random

object AlarmHelper {

    const val DATA_ALARM_NAME: String = "alarms.db"
    const val TABLE_NAME: String = "alarmApp"
    const val ID_ALARM: String = "idAlarm"
    const val ALARM_ID: String = "alarmId"
    const val COL_TIME: String = "timeForAlarm"
    const val COL_LABEL: String = "labelAlarm"
    const val COL_SWITCH: String = "switchAlarm"
    const val COL_RINGTONE: String = "ringtoneSong"
    const val COL_DISMISS_WAY: String = "dismissWay"
    const val COL_IMG: String = "imgBackground"
    const val COL_IS_ALARM: String = "isAlarm"
    const val COL_IS_VIBRATE: String = "isVibrate"
    const val RECORD: String = "recordBab"
    const val COL_MON: String = "monDay"
    const val COL_TUES: String = "tuesDay"
    const val COL_WED: String = "wedDay"
    const val COL_THURS: String = "thursDay"
    const val COL_FRI: String = "friDay"
    const val COL_SAT: String = "satDay"
    const val COL_SUN: String = "sunDay"

    const val SINGLE_SWIPE: Int = 0
    const val SINGLE_CLICK: Int = 1
    const val SHAKE_DEVICE: Int = 2
    const val DOUBLE_SWIPE: Int = 3

    const val MON: Int = 0
    const val TUES: Int = 1
    const val WED: Int = 2
    const val THURS: Int = 3
    const val FRI: Int = 4
    const val SAT: Int = 5
    const val SUN: Int = 6

    private const val CH_MIS: String = "missedChannel"


    internal suspend fun android.content.Context.fetchValidId() = justCoroutine {
        withBackDef(-1) {
            findIntegerPrefDb(ID_ALARM_, KEY_ALARM_CREATOR, 1001).let {
                updatePrefInt(KEY_ALARM_CREATOR, it + 1)
                it
            }
        }.letSusBack {
            return@letSusBack if (it == -1) {
                Random.nextInt(0, 20000)
            } else {
                it
            }
        }
    }

    inline val allColumns: Array<String>
        get() {
            return arrayOf(
                ALARM_ID,
                COL_TIME,
                COL_LABEL,
                COL_SWITCH,
                COL_RINGTONE,
                COL_DISMISS_WAY,
                COL_IMG,
                COL_IS_ALARM,
                COL_IS_VIBRATE,
                RECORD,
                COL_MON,
                COL_TUES,
                COL_WED,
                COL_THURS,
                COL_FRI,
                COL_SAT,
                COL_SUN,
            )
        }


    @com.pt.common.global.CurAnn
    @com.pt.common.global.WorkerAnn
    fun AlarmSack.toContentValues(): android.content.ContentValues = run {
        return@run android.content.ContentValues(22).apply {
            put(ALARM_ID, idAlarm)
            put(COL_TIME, timeAlarm)
            put(COL_LABEL, labelAlarm ?: "")
            put(COL_RINGTONE, ringAlarm)
            put(COL_DISMISS_WAY, dismissWay)
            put(RECORD, recordAlarm)
            put(COL_SWITCH, switchAlarm)
            put(COL_IMG, imgBackground)
            put(COL_IS_ALARM, isAlarm)
            put(COL_IS_VIBRATE, isVibrate)
            put(COL_MON, if (allDays[MON]) 1 else 0)
            put(COL_TUES, if (allDays[TUES]) 1 else 0)
            put(COL_WED, if (allDays[WED]) 1 else 0)
            put(COL_THURS, if (allDays[THURS]) 1 else 0)
            put(COL_FRI, if (allDays[FRI]) 1 else 0)
            put(COL_SAT, if (allDays[SAT]) 1 else 0)
            put(COL_SUN, if (allDays[SUN]) 1 else 0)
        }
    }


    /*@com.pt.common.global.CurAnn
    @com.pt.common.global.WorkerAnn
    fun android.database.Cursor?.buildAlarms(): MutableList<AlarmSack> = run {
        return@run this?.use {
            mutableListOf<AlarmSack>().apply {
                while (it.moveToNext()) {
                    AlarmSack(
                        idAlarm = this@useMy.curInt(ALARM_ID),
                        timeAlarm = this@useMy.curLong(COL_TIME).fetchCalenderAlarm,
                        labelAlarm = this@useMy.curStr(COL_LABEL),
                        ringAlarm = this@useMy.curStr(COL_RINGTONE),
                        dismissWay = this@useMy.curInt(COL_DISMISS_WAY),
                        recordAlarm = this@useMy.curStr(RECORD),
                        switchAlarm = this@useMy.curBool(COL_SWITCH),
                        imgBackground = this@useMy.curStr(COL_IMG),
                        isAlarm = this@useMy.curBool(COL_IS_ALARM),
                        isVibrate = this@useMy.curBool(COL_IS_VIBRATE)
                    ).apply {
                        allDays.clear()
                        allDays.add(MON, this@useMy.curBool(COL_MON))
                        allDays.add(TUES, this@useMy.curBool(COL_TUES))
                        allDays.add(WED, this@useMy.curBool(COL_WED))
                        allDays.add(THURS, this@useMy.curBool(COL_THURS))
                        allDays.add(FRI, this@useMy.curBool(COL_FRI))
                        allDays.add(SAT, this@useMy.curBool(COL_SAT))
                        allDays.add(SUN, this@useMy.curBool(COL_SUN))
                    }.let { itA ->
                        add(itA)
                    }
                }
            }
        } ?: mutableListOf()
    }*/


    @com.pt.common.global.CurAnn
    @com.pt.common.global.WorkerAnn
    fun android.database.Cursor?.buildAlarmsAsIt(): MutableList<AlarmSack> = run {
        return@run this?.useMy {
            mutableListOf<AlarmSack>().apply {
                while (this@useMy.moveToNext()) {
                    AlarmSack(
                        idAlarm = this@useMy.curInt(ALARM_ID),
                        timeAlarm = this@useMy.curLong(COL_TIME),
                        labelAlarm = this@useMy.curStr(COL_LABEL),
                        ringAlarm = this@useMy.curStr(COL_RINGTONE),
                        dismissWay = this@useMy.curInt(COL_DISMISS_WAY),
                        recordAlarm = this@useMy.curStr(RECORD),
                        switchAlarm = this@useMy.curBool(COL_SWITCH),
                        imgBackground = this@useMy.curStr(COL_IMG),
                        isAlarm = this@useMy.curBool(COL_IS_ALARM),
                        isVibrate = this@useMy.curBool(COL_IS_VIBRATE)
                    ).apply {
                        allDays.clear()
                        allDays.add(MON, this@useMy.curBool(COL_MON))
                        allDays.add(TUES, this@useMy.curBool(COL_TUES))
                        allDays.add(WED, this@useMy.curBool(COL_WED))
                        allDays.add(THURS, this@useMy.curBool(COL_THURS))
                        allDays.add(FRI, this@useMy.curBool(COL_FRI))
                        allDays.add(SAT, this@useMy.curBool(COL_SAT))
                        allDays.add(SUN, this@useMy.curBool(COL_SUN))
                    }.let { itA ->
                        add(itA)
                    }
                }
            }
        } ?: mutableListOf()
    }

    @com.pt.common.global.CurAnn
    @com.pt.common.global.WorkerAnn
    suspend fun android.database.Cursor?.buildAlarmNow(): AlarmSack? = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(null) {
            return@withBackCurDef this@buildAlarmNow?.useBack {
                if (this@useBack.moveToFirst()) {
                    return@useBack AlarmSack(
                        idAlarm = this@useBack.curInt(ALARM_ID),
                        timeAlarm = this@useBack.curLong(COL_TIME),
                        labelAlarm = this@useBack.curStr(COL_LABEL),
                        ringAlarm = this@useBack.curStr(COL_RINGTONE),
                        dismissWay = this@useBack.curInt(COL_DISMISS_WAY),
                        recordAlarm = this@useBack.curStr(RECORD),
                        switchAlarm = this@useBack.curBool(COL_SWITCH),
                        imgBackground = this@useBack.curStr(COL_IMG),
                        isAlarm = this@useBack.curBool(COL_IS_ALARM),
                        isVibrate = this@useBack.curBool(COL_IS_VIBRATE)
                    ).apply {
                        allDays.clear()
                        allDays.add(MON, this@useBack.curBool(COL_MON))
                        allDays.add(TUES, this@useBack.curBool(COL_TUES))
                        allDays.add(WED, this@useBack.curBool(COL_WED))
                        allDays.add(THURS, this@useBack.curBool(COL_THURS))
                        allDays.add(FRI, this@useBack.curBool(COL_FRI))
                        allDays.add(SAT, this@useBack.curBool(COL_SAT))
                        allDays.add(SUN, this@useBack.curBool(COL_SUN))
                    }
                } else {
                    return@useBack null
                }
            }
        }
    }


    inline val initArray: MutableList<Boolean>
        get() {
            return mutableListOf<Boolean>().apply {
                repeat(7) { add(false) }
            }
        }

    private inline val CalendarLate.getStartIndexFromTime: Int
        get() {
            return when (this[CalendarLate.DAY_OF_WEEK]) {
                CalendarLate.MONDAY -> 0
                CalendarLate.TUESDAY -> 1
                CalendarLate.WEDNESDAY -> 2
                CalendarLate.THURSDAY -> 3
                CalendarLate.FRIDAY -> 4
                CalendarLate.SATURDAY -> 5
                CalendarLate.SUNDAY -> 6
                else -> 0
            }
        }

    fun remainingTime(
        all: MutableList<Boolean>,
        pickerTime: Long,
        anyDayRepeating: Boolean,
    ): String {
        val currentTime = System.currentTimeMillis()
        val time = if (pickerTime < currentTime) {
            pickerTime + 86400000
        } else {
            pickerTime
        }
        val aa2 = time.fetchRemainingTime

        return if (anyDayRepeating) {

            val cal = getTimeForNextAlarm(time, all)

            val c1 = CalendarLate.getInstance()
            c1.timeInMillis = System.currentTimeMillis()
            val day = if (pickerTime > currentTime) {
                (cal[CalendarLate.DAY_OF_WEEK] - c1[CalendarLate.DAY_OF_WEEK]).let {
                    ((it + 7) % 7)
                }
            } else {
                (cal[CalendarLate.DAY_OF_WEEK] - c1[CalendarLate.DAY_OF_WEEK] - 1).let {
                    ((it + 7) % 7)
                }
            }
            "$day:$aa2"
        } else {
            aa2
        }
    }

    fun getTimeForNextAlarm(time: Long, allDays: MutableList<Boolean>): CalendarLate {
        val calendar = CalendarLate.getInstance()
        calendar.timeInMillis = time
        val currentTime = System.currentTimeMillis()
        val startIndex = calendar.getStartIndexFromTime
        var count = 0
        var isAlarmSetForDay: Boolean
        do {
            val index = (startIndex + count) % 7
            isAlarmSetForDay = allDays[index] && calendar.timeInMillis > currentTime
            if (!isAlarmSetForDay) {
                calendar.add(CalendarLate.DAY_OF_MONTH, 1)
                count++
            }
        } while (!isAlarmSetForDay && count < 7)
        return calendar
    }

    internal inline val Long.timeFromNow: Long
        get() {
            return java.util.Calendar.getInstance().let {
                it.timeInMillis = this@timeFromNow
                java.util.Calendar.getInstance().apply {
                    this.timeInMillis = System.currentTimeMillis()
                    this[java.util.Calendar.HOUR_OF_DAY] = it[java.util.Calendar.HOUR_OF_DAY]
                    this[java.util.Calendar.MINUTE] = it[java.util.Calendar.MINUTE]
                    this[java.util.Calendar.SECOND] = 0
                }.timeInMillis
            }
        }

    @android.annotation.SuppressLint("MissingPermission")
    fun android.content.Context.updateNotification(alarm: AlarmSack, isScreenLock: Boolean) {
        androidx.core.app.NotificationCompat.Builder(
            this,
            if (isScreenLock) CH_ALA else CH_ALA_POP
        ).apply {
            setContentTitle(resources.getString(R.string.tg))
            (alarm.labelAlarm ?: "").let { l ->
                if (alarm.isAlarm) {
                    l
                } else {
                    if (l.length < 32) {
                        l + " " + resources.getString(R.string.r3)
                    } else {
                        l.toDefString(29) + "... " + resources.getString(R.string.r3)
                    }
                }.let {
                    setContentText(it)
                }
                if (alarm.isAlarm) {
                    l
                } else {
                    l + "<br>" + resources.getString(R.string.r3)
                }.toHtmlText.let {
                    setStyle(androidx.core.app.NotificationCompat.BigTextStyle().bigText(it))
                }
            }
            setSmallIcon(R.drawable.ic_launcher_foreground)
            color = fetchColor(R.color.cac)
            setTicker(alarm.labelAlarm ?: "")
            newIntent(com.pt.pro.alarm.release.AlarmServiceNotification::class.java) {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                action = ACTION_DISMISS_ALARM
                return@newIntent this@newIntent
            }.run {
                android.app.PendingIntent.getService(
                    this@updateNotification,
                    alarm.idAlarm,
                    this,
                    PEND_FLAG
                )
            }.let {
                setContentIntent(it)
                androidx.core.app.NotificationCompat.Action.Builder(
                    R.drawable.ic_close,
                    resources.getString(R.string.s6), it
                ).build().let { itA ->
                    this@apply.addAction(itA)
                }
            }
            setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
            setAutoCancel(false)
            setOngoing(true)
            setOnlyAlertOnce(false)
            setCategory(androidx.core.app.NotificationCompat.CATEGORY_SERVICE)
            priority = androidx.core.app.NotificationCompat.PRIORITY_MAX or
                    androidx.core.app.NotificationCompat.PRIORITY_HIGH
            foregroundServiceBehavior =
                androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
        }.build().let {
            androidx.core.app.NotificationManagerCompat.from(this@updateNotification).apply {
                it.flags = androidx.core.app.NotificationCompat.FLAG_FOREGROUND_SERVICE or
                        androidx.core.app.NotificationCompat.FLAG_ONGOING_EVENT
                notify(SERVICE_ALARM, it)
            }
        }
    }

    @android.annotation.SuppressLint("MissingPermission")
    fun android.content.Context.missedNotification(
        id: Int,
        label: String?,
        times: Int,
        time: Long,
    ) {
        androidx.core.app.NotificationCompat.Builder(this, CH_MIS).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(resources.getString(R.string.ma))

            resources.getString(R.string.pl).let { text1 ->
                android.text.format.DateFormat.format(
                    "$TIME_FORMAT_12 $TIME_A",
                    time
                ).let { aa ->
                    (text1 + TEXT_IT_BIG_BE + aa + TEXT_IT_BIG_AF).toHtmlText
                }
            }.let { textSmall ->
                setContentText(textSmall)
            }
            if (times > 1) {
                (resources.getString(R.string.gw) + TEXT_IT_BIG_BE + times +
                        TEXT_IT_BIG_AF + resources.getString(R.string.i5))
            } else {
                ""
            }.let { text4 ->
                resources.getString(R.string.pl).let { text1 ->
                    android.text.format.DateFormat.format(
                        "$TIME_FORMAT_12 $TIME_A",
                        time
                    ).let { aa ->
                        (text1 + TEXT_IT_BIG_BE + aa + TEXT_IT_BIG_AF +
                                text4 + "<br>$label").toHtmlText
                    }
                }
            }.let { textBig ->
                androidx.core.app.NotificationCompat.BigTextStyle().bigText(textBig).let {
                    setStyle(it)
                }
            }
            setVibrate(longArrayOf(1000, 500))
            android.content.Intent(
                this@missedNotification,
                DismissReceiverNormal::class.java
            ).apply {
                putExtra(INTENT_ID_CLOSE, id)
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                action = "actionNoteDismiss"
                addCategory("catoNoteDismiss")
            }.run {
                android.app.PendingIntent.getBroadcast(
                    this@missedNotification,
                    id - 1,
                    this,
                    PEND_FLAG
                )
            }.run {
                androidx.core.app.NotificationCompat.Action.Builder(
                    R.drawable.ic_close,
                    resources.getString(R.string.co),
                    this@run
                )
            }.let {
                addAction(it.build())
            }
            setAutoCancel(true)
            color = fetchColor(R.color.cac)
            priority = androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
            setCategory(androidx.core.app.NotificationCompat.CATEGORY_EVENT)
        }.build().run {
            flags = androidx.core.app.NotificationCompat.FLAG_ONLY_ALERT_ONCE
            androidx.core.app.NotificationManagerCompat.from(this@missedNotification).apply {
                createMissedNotificationChannel(resources.getString(R.string.ms))
                notify(id, this@run)
            }
        }
    }


    private fun androidx.core.app.NotificationManagerCompat.createMissedNotificationChannel(
        str: String,
    ) {
        androidx.core.app.NotificationChannelCompat.Builder(
            CH_MIS,
            androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
        ).apply {
            setName(str)
            setVibrationEnabled(true)
            setVibrationPattern(longArrayOf(1000, 500))
            setShowBadge(true)
            createNotificationChannel(build())
        }
    }

    fun android.content.Context.updateRecord(alarm: AlarmSack) {
        androidx.core.app.NotificationCompat.Builder(this, CH_ALA).apply {
            setContentTitle(resources.getString(R.string.tg))
            setContentText(resources.getString(R.string.qk))
            android.content.Intent(
                this@updateRecord,
                com.pt.pro.alarm.release.AlarmServiceNotification::class.java
            ).apply {
                action = ALARM_RECORD_FOR_DISMISS
                putExtra(ID_TO_DISMISS, alarm.idAlarm)
                putExtra(JUST_CLOSE, true)//false
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }.run {
                android.app.PendingIntent.getService(
                    this@updateRecord, 9,
                    this, PEND_FLAG
                )
            }.let {
                setContentIntent(it)
            }
            setSmallIcon(R.drawable.ic_launcher_foreground)
            color = fetchColor(R.color.cac)
            setAutoCancel(false)
            setOngoing(true)
            setOnlyAlertOnce(false)
            android.content.Intent(
                this@updateRecord,
                com.pt.pro.alarm.release.AlarmServiceNotification::class.java
            ).apply {
                action = ALARM_RECORD_FOR_DISMISS
                putExtra(ID_TO_DISMISS, alarm.idAlarm)
                putExtra(JUST_CLOSE, true)
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }.run {
                return@run android.app.PendingIntent.getService(
                    this@updateRecord, 10,
                    this, PEND_FLAG
                )
            }.let {
                androidx.core.app.NotificationCompat.Action.Builder(
                    R.drawable.ic_close,
                    resources.getString(R.string.co), it
                ).build().let(::addAction)
            }
            setDefaults(androidx.core.app.NotificationCompat.DEFAULT_ALL)
            priority = androidx.core.app.NotificationCompat.PRIORITY_HIGH or
                    androidx.core.app.NotificationCompat.PRIORITY_MAX
        }.let {
            androidx.core.app.NotificationManagerCompat.from(this@updateRecord).apply {
                it.build().apply {
                    flags = androidx.core.app.NotificationCompat.FLAG_FOREGROUND_SERVICE
                }.also {
                    notify(SERVICE_ALARM, it)
                }
            }
        }
    }
}
