package com.pt.pro.alarm.views

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pt.common.global.*
import com.pt.common.mutual.adapter.GlobalAdapterLong
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.alarm.interfaces.AlarmAdapterListener
import com.pt.pro.alarm.interfaces.AlarmMainListener
import com.pt.pro.databinding.ItemAlarmBinding

class AlarmAdapter(
    override var allAlarms: MutableList<AlarmSack>,
    override val nightRider: Boolean,
    override val is24: Boolean,
    override var refreshListener: AlarmMainListener?,
) : RecyclerView.Adapter<AlarmAdapter.AlarmHolder>(), AlarmAdapterListener {

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AlarmHolder = LayoutInflater.from(parent.context).run {
        AlarmHolder(ItemAlarmBinding.inflate(this, parent, false))
    }

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: AlarmHolder, i: Int) {
        holder.bind()
    }

    @com.pt.common.global.UiAnn
    override fun onViewAttachedToWindow(holder: AlarmHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    override fun getItemCount(): Int = allAlarms.size

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    override suspend fun MutableList<AlarmSack>.setAlarms() {
        val siz = allAlarms.size
        clearer()
        justRemove(siz)
        adder()
        justAdd(size)
    }

    private suspend fun justRemove(siz: Int) {
        withMain {
            notifyItemRangeRemoved(0, siz)
        }
    }

    private suspend fun justAdd(siz: Int) {
        withMain {
            notifyItemRangeInserted(0, siz)
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun clearer() {
        withMain {
            allAlarms.clear()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun MutableList<AlarmSack>.adder() {
        withMain {
            allAlarms = this@adder
        }
    }

    @com.pt.common.global.MainAnn
    override fun onAdapterDestroy() {
        allAlarms.clear()
        refreshListener = null
    }

    @com.pt.common.global.WorkerAnn
    override fun Array<String>.buildSelectedDays(
        alarm: AlarmSack,
        mAccentColor: Int,
    ): Spannable = kotlin.run {
        val numDays = 7
        val days = alarm.allDays
        val builder = SpannableStringBuilder()
        var span: ForegroundColorSpan
        var startIndex: Int
        var endIndex: Int
        for (i in 0 until numDays) {
            startIndex = builder.length
            val dayText = this[i]
            builder.append(dayText)
            builder.append(" ")
            endIndex = startIndex + dayText.length
            val isSelected = days[i]
            if (isSelected) {
                span = ForegroundColorSpan(mAccentColor)
                builder.setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return@run builder
    }

    @com.pt.common.global.UiAnn
    inner class AlarmHolder(
        item: ItemAlarmBinding
    ) : GlobalAdapterLong<ItemAlarmBinding, AlarmSack>(item) {

        override val Int.item: AlarmSack
            get() = allAlarms.getI(posA)

        private inline val AlarmSack.isRealSwitchAlarm: Boolean
            get() = switchAlarm && timeAlarm > System.currentTimeMillis()

        override fun ItemAlarmBinding.bind() {
            deleteAlarm.setOnClickListener(this@AlarmHolder)
            alarmSwitch.setOnClickListener(this@AlarmHolder)
            alarmCard.setOnClickListener(this@AlarmHolder)
        }

        override fun ItemAlarmBinding.attach(it: AlarmSack, i: Int) {
            itemView.tag = posA
            alarmSwitch.apply {
                isChecked = it.isRealSwitchAlarm
                jumpDrawablesToCurrentState()
            }
            alarmTime.text = if (!is24) {
                amPm.text = com.pt.common.BuildConfig.TIME_A.toDataFormat(it.timeAlarm)
                com.pt.common.BuildConfig.TIME_FORMAT_12.toDataFormat(it.timeAlarm)
            } else {
                com.pt.common.BuildConfig.TIME_FORMAT_24.toDataFormat(it.timeAlarm)
            }
            if (!it.allDays.contains(true)) {
                alarmDays.justInvisible()
            } else {
                alarmDays.justVisible()
                recA.getStringArray(R.array.dv).run {
                    buildSelectedDays(it, themA.findAttr(android.R.attr.colorAccent))
                }.let {
                    alarmDays.text = it
                }
            }
            if (!it.isRealSwitchAlarm) {
                recolorAndText("", themA.findAttr(R.attr.rmoGrey))
            } else {
                recolorAndText(it.labelAlarm ?: "", themA.findAttr(R.attr.rmoText))
            }
        }

        override fun ItemAlarmBinding.clear() {}

        override fun View.onClick(it: AlarmSack) {
            with<ItemAlarmBinding, Unit>(binder) {
                when (this@onClick) {
                    deleteAlarm -> {
                        refreshListener?.apply {
                            it.forDelete()
                        }
                    }
                    alarmSwitch -> {
                        if (it.isRealSwitchAlarm) {
                            refreshListener?.apply {
                                it.copy(timeAlarm = it.timeAlarm).forSwitchOff {
                                    withMain {
                                        recolorAndText("", themA.findAttr(R.attr.rmoGrey))
                                    }
                                }
                            }
                        } else {
                            refreshListener?.apply {
                                it.copy(timeAlarm = it.timeAlarm.fetchCalenderAlarm).forSwitchOn {
                                    withMain {
                                        recolorAndText(
                                            it.labelAlarm ?: "",
                                            themA.findAttr(R.attr.rmoText)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        kotlin.runCatching {
                            binder.alarmTime.startAnimation(scaleDownAnimation)
                        }
                        refreshListener?.apply {
                            it.forLaunch()
                        }
                    }
                }
            }
        }

        override fun View.onLongClick(it: AlarmSack): Boolean = false

        private fun ItemAlarmBinding.recolorAndText(txt: String, itC: Int) {
            alarmLabel.apply {
                text = txt
                setTextColor(itC)
            }
            alarmTime.setTextColor(itC)
            amPm.setTextColor(itC)
            deleteAlarm.setTextColor(itC)
            alarmDays.setTextColor(itC)
        }
    }

}