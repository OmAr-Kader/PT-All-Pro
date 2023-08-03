package com.pt.pro.extra.views

import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.extra.fasten.SettingAlarmFasten

class FragmentSettingAlarm : GlobalSimpleFragment<SettingAlarmFasten>() {

    override var lastJob: kotlinx.coroutines.Job? = null

    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    private var currentTime = 0
    private var lapTime = 0
    private var lapCounter = 0
    private var isStart = false
    private var previousLap = 0

    private inline val minuteText: String get() = " " + rec.getString(com.pt.pro.R.string.mu)
    private inline val minutesText: String get() = " " + rec.getString(com.pt.pro.R.string.us)
    private inline val timeText: String get() = " " + rec.getString(com.pt.pro.R.string.i1)
    private inline val timesText: String get() = " " + rec.getString(com.pt.pro.R.string.i5)
    private inline val secondsText: String get() = " " + rec.getString(com.pt.pro.R.string.ns)

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.extra.fasten.ExtraInflater.run {
                this@creBin.context.inflaterAlarmSetting()
            }.also {
                @com.pt.common.global.UiAnn
                binder = it
            }.root_
        }

    @android.annotation.SuppressLint("SetTextI18n")
    @com.pt.common.global.UiAnn
    override fun SettingAlarmFasten.onViewCreated() {
        lifecycle.addObserver(this@FragmentSettingAlarm)
        launchImdMain {
            frameIntervalText.setOnClickListener(this@FragmentSettingAlarm)
            frameSnoozeText.setOnClickListener(this@FragmentSettingAlarm)
            frameRepeatText.setOnClickListener(this@FragmentSettingAlarm)
            frameVolumeText.setOnClickListener(this@FragmentSettingAlarm)

            val intervalList = minutesText.let { ss ->
                buildList {
                    add(DSackT("5 $ss", 5))
                    add(DSackT("10 $ss", 10))
                    add(DSackT("15 $ss", 15))
                    add(DSackT("20 $ss", 20))
                    add(DSackT("30 $ss", 30))
                    add(DSackT("60 $ss", 60))
                    add(DSackT("90 $ss", 90))
                }
            }
            val intervalStr = intervalList.map {
                it.one
            }.toTypedArray()
            val intervalValues = intervalList.map {
                it.two
            }.toTypedArray()
            ctx.fetchPicker.apply {
                minValue = 0
                maxValue = intervalStr.size - 1
                displayedValues = intervalStr
                value = ctx.findIntegerPreference(INTER_T, 10).let {
                    intervalValues.indexOf(it)
                }
                addSenseListener(false) { _, _, type ->
                    if (type == UP_SEN) {
                        intervalValues.getOrNull(value)?.also { valIn ->
                            toCatchSackAfter(11, SAVE_CONST) {
                                setIntervalTime(valIn)
                            }.postBackAfter()
                        }
                    }
                }
            }.also {
                frameInterval.addView(it)
            }
            val snoozeList = minuteText.let { s ->
                minutesText.let { ss ->
                    buildList {
                        add(DSackT("1 $s", 1))
                        add(DSackT("3 $ss", 3))
                        add(DSackT("5 $ss", 5))
                        add(DSackT("10 $ss", 10))
                        add(DSackT("15 $ss", 15))
                        add(DSackT("20 $ss", 20))
                        add(DSackT("30 $ss", 30))
                    }
                }
            }

            val snoozeStr = snoozeList.map {
                it.one
            }.toTypedArray()
            val snoozeValues = snoozeList.map {
                it.two
            }.toTypedArray()
            ctx.fetchPicker.apply {
                minValue = 0
                maxValue = snoozeStr.size - 1
                displayedValues = snoozeStr
                value = ctx.findIntegerPreference(SNO_T, 3).let {
                    snoozeValues.indexOf(it)
                }
                addSenseListener(false) { _, _, type ->
                    if (type == UP_SEN) {
                        snoozeValues.getOrNull(value)?.also { valSn ->
                            toCatchSackAfter(22, SAVE_CONST) {
                                setSnoozeTime(valSn)
                            }.postBackAfter()
                        }
                    }
                }
            }.also {
                frameSnooze.addView(it)
            }
            val repeatList = timeText.let { s ->
                timesText.let { ss ->
                    buildList {
                        add(DSackT("1 $s", 1))
                        add(DSackT("2 $ss", 2))
                        add(DSackT("3 $ss", 3))
                        add(DSackT("5 $ss", 5))
                        add(DSackT("7 $ss", 7))
                        add(DSackT("10 $ss", 10))
                        add(DSackT("15 $ss", 15))
                    }
                }
            }

            val repeatStr = repeatList.map {
                it.one
            }.toTypedArray()
            val repeatValues = repeatList.map {
                it.two
            }.toTypedArray()
            ctx.fetchPicker.apply {
                minValue = 0
                maxValue = repeatStr.size - 1
                displayedValues = repeatStr
                value = ctx.findIntegerPreference(REP_T, 3).let {
                    repeatValues.indexOf(it)
                }
                addSenseListener(false) { _, _, type ->
                    if (type == UP_SEN) {
                        repeatValues.getOrNull(value)?.also { valRep ->
                            toCatchSackAfter(33, SAVE_CONST) {
                                setRepeatTime(valRep)
                            }.postBackAfter()
                        }
                    }
                }
            }.also {
                frameRepeat.addView(it)
            }

            val vVolume = secondsText.let { s ->
                buildList {
                    add(DSackT(com.pt.pro.R.string.nl.dStr, 0))
                    add(DSackT("3 $s", 3))
                    add(DSackT("5 $s", 5))
                    add(DSackT("10 $s", 10))
                    add(DSackT("15 $s", 15))
                    add(DSackT("20 $s", 20))
                    add(DSackT("30 $s", 30))
                    add(DSackT("45 $s", 45))
                    add(DSackT("60 $s", 60))
                    add(DSackT("75 $s", 75))
                    add(DSackT("90 $s", 90))
                }
            }
            val vV = vVolume.map {
                it.one
            }.toTypedArray()
            val vVal = vVolume.map {
                it.two
            }.toTypedArray()
            ctx.fetchPicker.apply {
                minValue = 0
                maxValue = vVal.size - 1
                displayedValues = vV
                value = ctx.findIntegerPreference(VOL_T, 0).let {
                    vVal.indexOf(it)
                }
                addSenseListener(false) { _, _, type ->
                    if (type == UP_SEN) {
                        vVal.getOrNull(value)?.also { valVVv ->
                            toCatchSackAfter(44, SAVE_CONST) {
                                setVolumeLength(valVVv)
                            }.postBackAfter()
                        }
                    }
                }
            }.also {
                frameVolume.addView(it)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun SettingAlarmFasten.onClick(v: android.view.View) {
        when (v) {
            frameIntervalText -> displayInterval()
            frameSnoozeText -> displaySnooze()
            frameRepeatText -> displayRepeat()
            frameVolumeText -> displayVolume()
        }
    }

    private fun SettingAlarmFasten.displayInterval() {
        pushJob {
            launchDef {
                withMain {
                    if (frameInterval.isVis) {
                        frameInterval.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            intervalImage.startAnimation(this)
                        }
                        mainFrameInterval.goneHandlerSus()
                    } else {
                        mainFrameInterval.justVisibleSus()
                        frameInterval.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            intervalImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    private fun SettingAlarmFasten.displaySnooze() {
        pushJob {
            launchDef {
                withMain {
                    if (frameSnooze.isVis) {
                        frameSnooze.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            snoozeImage.startAnimation(this)
                        }
                        mainFrameSnooze.goneHandlerSus()
                    } else {
                        mainFrameSnooze.justVisibleSus()
                        frameSnooze.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            snoozeImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    private fun SettingAlarmFasten.displayRepeat() {
        pushJob {
            launchDef {
                withMain {
                    if (frameRepeat.isVis) {
                        frameRepeat.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            repeatImage.startAnimation(this)
                        }
                        mainFrameRepeat.goneHandlerSus()
                    } else {
                        mainFrameRepeat.justVisibleSus()
                        frameRepeat.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            repeatImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    private fun SettingAlarmFasten.displayVolume() {
        pushJob {
            launchDef {
                withMain {
                    if (frameVolume.isVis) {
                        frameVolume.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            volumeImage.startAnimation(this)
                        }
                        mainFrameVolume.goneHandlerSus()
                    } else {
                        mainFrameVolume.justVisibleSus()
                        frameVolume.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            volumeImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun setIntervalTime(state: Int) {
        launchDef {
            ctx.updatePrefInt(INTER_T, state)
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun setSnoozeTime(state: Int) {
        launchDef {
            ctx.updatePrefInt(SNO_T, state)
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun setRepeatTime(state: Int) {
        launchDef {
            ctx.updatePrefInt(REP_T, state)
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun setVolumeLength(state: Int) {
        launchDef {
            ctx.updatePrefInt(VOL_T, state)
        }
    }

    override fun onDestroyView() {
        currentTime = 0
        lapTime = 0
        lapCounter = 0
        isStart = false
        previousLap = 0
        binder = null
        super.onDestroyView()
    }
}