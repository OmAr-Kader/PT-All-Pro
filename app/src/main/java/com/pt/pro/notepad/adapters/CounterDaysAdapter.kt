package com.pt.pro.notepad.adapters

import com.pt.common.global.*
import com.pt.common.stable.getI
import com.pt.pro.notepad.fragments.FragmentCounter.Companion.isKeys

class CounterDaysAdapter(
    override val counterDaysNumbers: MutableList<String>,
    override var tableTimeCounter: Long,
    override val allStringResource: String,
    override var dataClickListener: com.pt.pro.notepad.interfaces.CounterListener?,
) : androidx.recyclerview.widget.RecyclerView.Adapter<CounterDaysAdapter.DaysViewHolder>(), com.pt.pro.notepad.interfaces.DaysListener {

    private var previous: com.pt.pro.notepad.models.CounterDayFasten? = null
    private var previousHolderNumber: Int? = null

    @Volatile
    override var clickedDay: Int = -1

    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): DaysViewHolder = DaysViewHolder(com.pt.pro.notepad.models.CounterDayFasten.run { parent.context.inflaterDay() })

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        holder.bind()
    }

    override fun onViewAttachedToWindow(holder: DaysViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    override fun getItemCount(): Int = counterDaysNumbers.size

    override fun getItemViewType(position: Int): Int = position

    override fun getItemId(position: Int): Long = position.toLong()

    override fun resetFromOut() {
        val prev = clickedDay
        clickedDay = -1
        notifyItemChanged(prev)
    }

    override fun onAdapterDestroy() {
        counterDaysNumbers.clear()
        previousHolderNumber = null
        dataClickListener = null
        previous = null
        com.pt.pro.notepad.models.CounterDayFasten.destroyFasten()
    }


    override suspend fun MutableList<String>.update(color: Int) {
        com.pt.common.stable.withMain {
            if (previous != null) (previous ?: return@withMain).daysButton.backReColor(color)
        }
        val siz = counterDaysNumbers.size
        clearer()
        justRemove(siz)
        adder()
        justAdd(size)
    }

    @com.pt.common.global.MainAnn
    private suspend fun clearer() {
        com.pt.common.stable.withMain {
            counterDaysNumbers.clear()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun MutableList<String>.adder() {
        com.pt.common.stable.withMain {
            counterDaysNumbers.addAll(this@adder)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justRemove(siz: Int) {
        com.pt.common.stable.withMain {
            notifyItemRangeRemoved(0, siz)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justAdd(siz: Int) {
        com.pt.common.stable.withMain {
            notifyItemRangeInserted(0, siz)
        }
    }

    override fun changeColor(counterPos: Int, isAll: Boolean) {
        val prev = clickedDay
        clickedDay = if (!isKeys && !isAll) {
            counterPos
        } else if (!isKeys && isAll) {
            counterDaysNumbers.size - 1
        } else {
            counterPos
        }
        androidx.core.os.HandlerCompat.createAsync(android.os.Looper.getMainLooper()).post {
            notifyItemChanged(prev)
            if (clickedDay != -1) notifyItemChanged(clickedDay)
        }
    }

    internal inline val Int.dayText: String
        get() {
            return tableTimeCounter.fetchCalender.run {
                return@run CalendarLate.getInstance().also { a ->
                    a.set(this@run[CalendarLate.YEAR], this@run[CalendarLate.MONTH], this@dayText)
                }.timeInMillis.let { tm ->
                    return@let "EEE".toDataFormat(tm)
                }
            }
        }

    inner class DaysViewHolder(
        view: com.pt.pro.notepad.models.CounterDayFasten,
    ) : com.pt.common.mutual.adapter.GlobalAdapter<com.pt.pro.notepad.models.CounterDayFasten, String>(view) {

        override val Int.item: String
            get() = counterDaysNumbers.getI(this)

        override fun com.pt.pro.notepad.models.CounterDayFasten.bind() {
            daysButton.text = if (clickedDay == posA) {
                daysButton.backReColor(themA.findAttr(android.R.attr.colorAccent))
                if (!isKeys && posA.item != allStringResource) {
                    posA.item.toInt().dayText
                } else posA.item
            } else {
                daysButton.backReColor(themA.findAttr(android.R.attr.colorPrimary))
                posA.item
            }
            daysButton.setOnClickListener(this@DaysViewHolder)
        }

        override fun com.pt.pro.notepad.models.CounterDayFasten.attach(it: String) {

        }

        override fun clear() {}

        override fun String.onClick(i: Int) {
            changeColor(i, this == allStringResource)
            dataClickListener?.onCounterDays(this, true)
        }

        override fun com.pt.pro.notepad.models.CounterDayFasten.clear() {}
    }

}
