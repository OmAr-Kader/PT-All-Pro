package com.pt.pro.alarm.views

import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalFragment
import com.pt.common.stable.fetchMainExtractor

abstract class BaseSetAlarm : GlobalFragment<com.pt.pro.alarm.fasten.SetAlarmFasten>(),
    com.pt.common.mutual.base.BackInterface, com.pt.pro.alarm.interfaces.EditAlarmListener,
    androidx.work.Configuration.Provider {

    @Volatile
    protected var mediaPlayer: androidx.media3.exoplayer.ExoPlayer? = null

    @Volatile
    protected var setClicked: Boolean = true

    @Volatile
    protected var stubPicker: com.pt.pro.alarm.fasten.StubTimePickFasten? = null

    @Volatile
    protected var record: String? = null

    @Volatile
    protected var title: String? = null

    @Volatile
    protected var declineWay: Int = com.pt.pro.alarm.objects.AlarmHelper.SINGLE_CLICK

    @Volatile
    protected var ringtoneUri: String? = null

    @Volatile
    protected var image: String? = null

    protected var isCircle: Boolean = true

    private inline val unselectedDayColor: Int get() = requireContext().fetchColor(com.pt.pro.R.color.gry)

    private inline val unselectedDay: Int get() = requireContext().fetchColor(com.pt.pro.R.color.rdd)

    private inline val android.content.res.Resources.Theme.selectedDayDark: Int
        get() = findAttr(android.R.attr.colorPrimary)

    private inline val android.content.res.Resources.Theme.selectedDayColor: Int
        get() = findAttr(android.R.attr.textColorPrimary)

    protected val isHour24: Boolean by lazy {
        return@lazy ctx.is24Hour
    }

    protected val currentDays: MutableList<Boolean> = mutableListOf()

    protected var mon: Boolean = false
    protected var tues: Boolean = false
    protected var wed: Boolean = false
    protected var thurs: Boolean = false
    protected var fri: Boolean = false
    protected var sat: Boolean = false
    protected var sun: Boolean = false

    @com.pt.common.global.UiAnn
    protected fun com.pt.pro.alarm.fasten.StubAlarmDayFasten.notAnyDayRepeating() {
        mon = false
        mMon.backReColor(unselectedDay)
        mMon.setTextColor(unselectedDayColor)
        tues = false
        mTues.backReColor(unselectedDay)
        mTues.setTextColor(unselectedDayColor)
        wed = false
        mWed.backReColor(unselectedDay)
        mWed.setTextColor(unselectedDayColor)
        thurs = false
        mThurs.backReColor(unselectedDay)
        mThurs.setTextColor(unselectedDayColor)
        fri = false
        mFri.backReColor(unselectedDay)
        mFri.setTextColor(unselectedDayColor)
        sat = false
        mSat.backReColor(unselectedDay)
        mSat.setTextColor(unselectedDayColor)
        sun = false
        mSun.backReColor(unselectedDay)
        mSun.setTextColor(unselectedDayColor)
        loadCurrentDays()
    }


    @com.pt.common.global.UiAnn
    protected fun loadCurrentDays() {
        currentDays.clear()
        currentDays.add(com.pt.pro.alarm.objects.AlarmHelper.MON, mon)
        currentDays.add(com.pt.pro.alarm.objects.AlarmHelper.TUES, tues)
        currentDays.add(com.pt.pro.alarm.objects.AlarmHelper.WED, wed)
        currentDays.add(com.pt.pro.alarm.objects.AlarmHelper.THURS, thurs)
        currentDays.add(com.pt.pro.alarm.objects.AlarmHelper.FRI, fri)
        currentDays.add(com.pt.pro.alarm.objects.AlarmHelper.SAT, sat)
        currentDays.add(com.pt.pro.alarm.objects.AlarmHelper.SUN, sun)
    }

    @com.pt.common.global.UiAnn
    protected fun com.sztorm.timepicker.TimePicker.setCirclePickerTime(time: Long) {
        time.fetchCalender.also {
            if (this@BaseSetAlarm.isHour24) {
                setTime(it[CalendarLate.HOUR_OF_DAY], it[CalendarLate.MINUTE])
            } else {
                setTime(
                    if (it[CalendarLate.HOUR] > 12) it[CalendarLate.HOUR] - 12 else it[CalendarLate.HOUR],
                    it[CalendarLate.MINUTE],
                    it[CalendarLate.HOUR_OF_DAY] < com.sztorm.timepicker.timeangleconstants.HOURS_IN_HALF_DAY
                )
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected fun com.sztorm.timepicker.TimePicker.setCirclePickerDate(H: Int, M: Int) {
        if (this@BaseSetAlarm.isHour24) {
            setTime(H, M)
        } else {
            setTime(
                if (H >= 12) H - 12 else H,
                M,
                H < com.sztorm.timepicker.timeangleconstants.HOURS_IN_HALF_DAY
            )
        }
    }

    @com.pt.common.global.UiAnn
    protected fun android.widget.TimePicker.setTimePickerTime(time: Long) {
        time.fetchCalender.also {
            pickHour = it[java.util.Calendar.HOUR_OF_DAY]
            pickMinute = it[java.util.Calendar.MINUTE]
        }
    }

    @com.pt.common.global.UiAnn
    protected fun com.pt.pro.alarm.fasten.StubAlarmDayFasten.setDayCheckboxes(alarm: AlarmSack) {
        with(alarm) {
            mon = if (allDays[com.pt.pro.alarm.objects.AlarmHelper.MON]) {
                mMon.backReColor(them.selectedDayDark)
                mMon.setTextColor(them.selectedDayColor)
                true
            } else {
                mMon.backReColor(unselectedDay)
                mMon.setTextColor(unselectedDayColor)
                false
            }
            tues = if (allDays[com.pt.pro.alarm.objects.AlarmHelper.TUES]) {
                mTues.backReColor(them.selectedDayDark)
                mTues.setTextColor(them.selectedDayColor)
                true
            } else {
                mTues.backReColor(unselectedDay)
                mTues.setTextColor(unselectedDayColor)
                false
            }
            wed = if (allDays[com.pt.pro.alarm.objects.AlarmHelper.WED]) {
                mWed.backReColor(them.selectedDayDark)
                mWed.setTextColor(them.selectedDayColor)
                true
            } else {
                mWed.backReColor(unselectedDay)
                mWed.setTextColor(unselectedDayColor)
                false
            }
            thurs = if (allDays[com.pt.pro.alarm.objects.AlarmHelper.THURS]) {
                mThurs.backReColor(them.selectedDayDark)
                mThurs.setTextColor(them.selectedDayColor)
                true
            } else {
                mThurs.backReColor(unselectedDay)
                mThurs.setTextColor(unselectedDayColor)
                false
            }
            fri = if (allDays[com.pt.pro.alarm.objects.AlarmHelper.FRI]) {
                mFri.backReColor(them.selectedDayDark)
                mFri.setTextColor(them.selectedDayColor)
                true
            } else {
                mFri.backReColor(unselectedDay)
                mFri.setTextColor(unselectedDayColor)
                false
            }
            sat = if (allDays[com.pt.pro.alarm.objects.AlarmHelper.SAT]) {
                mSat.backReColor(them.selectedDayDark)
                mSat.setTextColor(them.selectedDayColor)
                true
            } else {
                mSat.backReColor(unselectedDay)
                mSat.setTextColor(unselectedDayColor)
                false
            }
            sun = if (allDays[com.pt.pro.alarm.objects.AlarmHelper.SUN]) {
                mSun.backReColor(them.selectedDayDark)
                mSun.setTextColor(them.selectedDayColor)
                true
            } else {
                mSun.backReColor(unselectedDay)
                mSun.setTextColor(unselectedDayColor)
                false
            }
        }
        loadCurrentDays()
    }

    protected inline val anyDayRepeating: Boolean
        get() {
            return (mon || tues || wed || thurs || fri || sat || sun)
        }


    protected inline val com.pt.pro.alarm.fasten.StubTimePickFasten.getTimeFromPicker: java.util.Calendar
        @com.pt.common.global.UiAnn
        get() {
            return java.util.Calendar.getInstance().apply {
                this[java.util.Calendar.HOUR_OF_DAY] =
                    if (isCircle) pickerCircle.hour else timeAlarm.pickHour
                this[java.util.Calendar.MINUTE] =
                    if (isCircle) pickerCircle.minute else timeAlarm.pickMinute
                this[java.util.Calendar.SECOND] = 0
            }
        }

    internal inline val Int.dismissText: String
        get() {
            return when (this) {
                com.pt.pro.alarm.objects.AlarmHelper.SINGLE_CLICK -> com.pt.pro.R.string.gr.dStr
                com.pt.pro.alarm.objects.AlarmHelper.SHAKE_DEVICE -> com.pt.pro.R.string.sd.dStr
                com.pt.pro.alarm.objects.AlarmHelper.SINGLE_SWIPE -> com.pt.pro.R.string.gh.dStr
                com.pt.pro.alarm.objects.AlarmHelper.DOUBLE_SWIPE -> com.pt.pro.R.string.jq.dStr
                else -> ""
            }
        }

    protected inline var android.widget.TimePicker.pickHour: Int
        @com.pt.common.global.UiAnn
        get() {
            return if (isV_M) {
                hour
            } else {
                @Suppress("DEPRECATION")
                currentHour
            }
        }
        set(value) {
            if (isV_M) {
                hour = value
            } else {
                @Suppress("DEPRECATION")
                currentHour = value
            }
        }

    protected inline var android.widget.TimePicker.pickMinute: Int
        @com.pt.common.global.UiAnn
        get() {
            return if (isV_M) {
                minute
            } else {
                @Suppress("DEPRECATION")
                currentMinute
            }
        }
        set(value) {
            if (isV_M) {
                minute = value
            } else {
                @Suppress("DEPRECATION")
                currentMinute = value
            }
        }


    override fun com.pt.pro.alarm.fasten.SetAlarmFasten.onLongClick(v: android.view.View): Boolean {
        when (v) {
            setAlarmButton -> {
                v.popUpComment(
                    com.pt.pro.R.string.lb,
                    com.pt.pro.R.attr.rmoBackground,
                    (-1 * 120F.toPixel)
                )
            }
            dismissWay -> {
                v.popUpComment(com.pt.pro.R.string.lc, com.pt.pro.R.attr.rmoBackground, 0)
            }
            buttonBackgroundPicker -> {
                v.popUpComment(com.pt.pro.R.string.lw, com.pt.pro.R.attr.rmoBackground, 0)
            }
            ringtonePickerButton -> {
                v.popUpComment(com.pt.pro.R.string.lk, com.pt.pro.R.attr.rmoBackground, 0)
            }
            stubPicker?.switchAlarm -> {
                v.popUpComment(com.pt.pro.R.string.lq, com.pt.pro.R.attr.rmoBackground, 0)
            }
            else -> return false
        }
        return true
    }

    protected var com.pt.pro.alarm.fasten.StubAlarmDayFasten?.myOnClickListener: android.view.View.OnClickListener?
        get() = android.view.View.OnClickListener { v ->
            with(this@myOnClickListener ?: return@OnClickListener) {
                when (v) {
                    mMon -> {
                        mon = !mon
                        if (mon) {
                            mMon.backReColor(them.selectedDayDark)
                            mMon.setTextColor(them.selectedDayColor)
                        } else {
                            mMon.backReColor(unselectedDay)
                            mMon.setTextColor(unselectedDayColor)
                        }
                    }
                    mTues -> {
                        tues = !tues
                        if (tues) {
                            mTues.backReColor(them.selectedDayDark)
                            mTues.setTextColor(them.selectedDayColor)
                        } else {
                            mTues.backReColor(unselectedDay)
                            mTues.setTextColor(unselectedDayColor)
                        }
                    }
                    mWed -> {
                        wed = !wed
                        if (wed) {
                            mWed.backReColor(them.selectedDayDark)
                            mWed.setTextColor(them.selectedDayColor)
                        } else {
                            mWed.backReColor(unselectedDay)
                            mWed.setTextColor(unselectedDayColor)
                        }
                    }
                    mThurs -> {
                        thurs = !thurs
                        if (thurs) {
                            mThurs.backReColor(them.selectedDayDark)
                            mThurs.setTextColor(them.selectedDayColor)
                        } else {
                            mThurs.backReColor(unselectedDay)
                            mThurs.setTextColor(unselectedDayColor)
                        }
                    }
                    mFri -> {
                        fri = !fri
                        if (fri) {
                            mFri.backReColor(them.selectedDayDark)
                            mFri.setTextColor(them.selectedDayColor)
                        } else {
                            mFri.backReColor(unselectedDay)
                            mFri.setTextColor(unselectedDayColor)
                        }
                    }
                    mSat -> {
                        sat = !sat
                        if (sat) {
                            mSat.backReColor(them.selectedDayDark)
                            mSat.setTextColor(them.selectedDayColor)
                        } else {
                            mSat.backReColor(unselectedDay)
                            mSat.setTextColor(unselectedDayColor)
                        }
                    }
                    mSun -> {
                        sun = !sun
                        if (sun) {
                            mSun.backReColor(them.selectedDayDark)
                            mSun.setTextColor(them.selectedDayColor)
                        } else {
                            mSun.backReColor(unselectedDay)
                            mSun.setTextColor(unselectedDayColor)
                        }
                    }
                    else -> {
                    }
                }
                ch()
                loadCurrentDays()
            }
        }
        set(value) {
            value.logProvLess()
        }

    private fun com.pt.pro.alarm.fasten.StubAlarmDayFasten.ch() {
        if (!anyDayRepeating) {
            binding.repeatingCheck.isChecked = false
            days.invisibleFade(200)
        }
    }

    override fun getWorkManagerConfiguration(): androidx.work.Configuration {
        return androidx.work.Configuration.Builder()
            .setExecutor(context.fetchMainExtractor)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setDefaultProcessName(com.pt.pro.BuildConfig.APPLICATION_ID)
            .build()
    }
}