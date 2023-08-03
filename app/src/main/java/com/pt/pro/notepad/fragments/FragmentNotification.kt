package com.pt.pro.notepad.fragments

import android.content.res.Configuration
import android.view.Gravity
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.widget.LinearLayoutCompat
import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalFragment
import com.pt.common.stable.*
import com.pt.common.stable.KEEPER_TIME
import com.pt.common.stable.NOTIFICATION
import com.pt.common.stable.newDBData
import com.pt.common.stable.newDBNotification
import com.pt.pro.R
import com.pt.pro.databinding.LayoutNotBinding
import com.pt.pro.notepad.interfaces.OnActivityStateChanged
import com.pt.pro.notepad.models.DataNotification
import com.pt.pro.notepad.objects.noteDb
import com.pt.pro.notepad.receivers.NoteReceiver.Companion.setReminderNote
import kotlin.random.Random

class FragmentNotification : GlobalFragment<LayoutNotBinding>(), OnActivityStateChanged {

    private var dataNotify: DataNotification? = null

    private var time: Boolean = false

    private var yearData: Int? = null
    private var monthData: Int? = null
    private var day: Int? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> View
        get() = {
            LayoutNotBinding.inflate(this@creBin, this, false).also {
                binder = it
                it.initViews()
            }.root
        }

    override fun LayoutNotBinding.onViewCreated() {

        dataNotify = if (isV_T) {
            act.intent.getParcelableExtra(NOTIFICATION, DataNotification::class.java)
        } else {
            @Suppress("DEPRECATION")
            act.intent.getParcelableExtra(NOTIFICATION)
        }

        (dataNotify?.timeNotify ?: System.currentTimeMillis()).let { tTime ->
            if (tTime != -1L && tTime > System.currentTimeMillis()) {
                dataNotify?.timeNotify ?: System.currentTimeMillis()
            } else {
                System.currentTimeMillis()
            }
        }.let { t ->
            dataTime.setTimePickerTime(t)
            dataPicker.apply {
                minDate = System.currentTimeMillis()
                date = t
                setOnDateChangeListener { _, year, month, dayOfMonth ->
                    yearData = year
                    monthData = month
                    day = dayOfMonth
                    dataPicker.justInvisible()
                    dataTime.justVisible()
                    time = true
                }
            }
        }
        editDataText.apply {
            text = (dataNotify ?: return@apply).dataText
        }
        backData.setOnClickListener(this@FragmentNotification)
        setNotification.apply {
            setOnClickListener(this@FragmentNotification)
            setOnLongClickListener(this@FragmentNotification)
        }
    }

    override fun LayoutNotBinding.onClick(v: View) {
        if (v == backData) {
            act.finish()
        } else {
            launchDef {
                withMain {
                    if (!time) {
                        ctx.makeToastRec(R.string.cf, 0)
                    } else {
                        if (binding.editDataText.text.isNullOrEmpty()) {
                            ctx.makeToastRec(R.string.pj, 0)
                            return@withMain
                        }
                        CalendarLate.getInstance().apply {
                            this[CalendarLate.YEAR] = (yearData ?: return@apply)
                            this[CalendarLate.MONTH] = (monthData ?: return@apply)
                            this[CalendarLate.DAY_OF_MONTH] = (day ?: return@apply)
                            this[CalendarLate.HOUR_OF_DAY] = binding.dataTime.pickHour
                            this[CalendarLate.MINUTE] = binding.dataTime.pickMinute
                            this[CalendarLate.SECOND] = 1
                        }.let { calOne ->
                            ((dataNotify ?: return@withMain).copy(
                                dataText = binding.editDataText.text.toString(),
                                timeNotify = calOne.timeInMillis
                            )).apply {
                                withBackDef(false) {
                                    ctx.newDBNotification {
                                        if ((dataNotify?.timeNotify ?: -1L) == -1L) {
                                            addNotification()
                                        } else {
                                            updateNotification()
                                        }
                                    }
                                    tableName?.let { tN ->
                                        ctx.newDBData(tN.noteDb) {
                                            act.intent.getLongExtra(KEEPER_TIME, -1L).let {
                                                upDateNotify(tN, it.toString(), timeNotify)
                                            }
                                        }
                                    }
                                    ctx.setReminderNote(timeNotify, Random.nextInt(10000, 20000))
                                    true
                                }.let {
                                    withMainNormal {
                                        if (it) {
                                            time = false
                                            ctx.makeToastRec(R.string.nd, 0)
                                            act.finish()
                                        } else {
                                            ctx.makeToastRec(R.string.xe, 0)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun LayoutNotBinding.initViews() {
        LinearLayoutCompat.LayoutParams(
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.MATCH_PARENT
        ).apply {
            weight = 1F
            gravity = Gravity.CENTER_HORIZONTAL
            topMargin = -1 * rec.statusBarHeight
            tempFrame.layoutParams = this
        }
        subMainNotification.orientation = rec.linDirection
    }

    private inline var TimePicker.pickHour: Int
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

    private inline var TimePicker.pickMinute: Int
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

    override fun LayoutNotBinding.onLongClick(v: View): Boolean {
        v.popUpComment(R.string.ef, R.attr.rmoBackground, (-1 * 120F.toPixel))
        return true
    }

    private fun TimePicker.setTimePickerTime(time: Long) {
        CalendarLate.getInstance().apply {
            timeInMillis = time
        }.also {
            pickMinute = it[CalendarLate.MINUTE]
            pickHour = it[CalendarLate.HOUR_OF_DAY]
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.apply {
            mainNotification.myActMargin(ctx.actionBarHeight)
            cardNot.cardAsView(ctx.actionBarHeight)
            titleOfNot.apply {
                editAppearance()
                setTextColor(them.findAttr(android.R.attr.textColorPrimary))
            }
            initViews()
        }
    }

    override val onMyOption: Boolean
        get() {
            return if (time) {
                binder?.apply {
                    dataPicker.justVisible()
                    dataTime.justInvisible()
                }
                time = false
                false
            } else {
                true
            }
        }

    override fun onDestroyView() {
        binder
        super.onDestroyView()
    }
}