@file:Suppress("HardCodedStringLiteral")

package com.pt.pro.notepad.objects

import com.pt.common.global.CalendarLate
import com.pt.common.global.fetchCalender
import com.pt.pro.notepad.models.TablesModelMonth
import com.pt.common.stable.*

fun TablesModelMonth.loadWithCategory(): MutableList<TablesModelMonth> = kotlin.run {
    return@run mutableListOf<TablesModelMonth>().apply {
        add(this@loadWithCategory)
        TablesModelMonth(
            mTableName = com.pt.common.BuildConfig.DONE_OPT,
            tableDisplay = com.pt.common.BuildConfig.DONE_OPT,
            mTableTime = 1L
        ).run {
            this@apply.add(this)
        }
        TablesModelMonth(
            mTableName = KEEP_DATA.toString(),
            tableDisplay = "red",
            mTableTime = 2L
        ).run {
            this@apply.add(this)
        }
        TablesModelMonth(
            mTableName = KEEP_IMPORTANT.toString(),
            tableDisplay = "blue",
            mTableTime = 3L
        ).run {
            this@apply.add(this)
        }
        TablesModelMonth(
            mTableName = KEEP_LINK.toString(),
            tableDisplay = "green",
            mTableTime = 4L
        ).run {
            this@apply.add(this)
        }
        TablesModelMonth(
            mTableName = KEEP_REMEMBER.toString(),
            tableDisplay = "sky",
            mTableTime = 5L
        ).run {
            this@apply.add(this)
        }
        TablesModelMonth(
            mTableName = KEEP_EMAIL.toString(),
            tableDisplay = EMAIL_CHOOSE,
            mTableTime = 6L
        ).run {
            this@apply.add(this)
        }
        TablesModelMonth(
            mTableName = IMAGE_CHOOSE,
            tableDisplay = IMAGE_CHOOSE,
            mTableTime = 7L
        ).run {
            this@apply.add(this)
        }
        TablesModelMonth(
            mTableName = RECORD_CHOOSE,
            tableDisplay = RECORD_CHOOSE,
            mTableTime = 8L
        ).run {
            this@apply.add(this)
        }
    }
}

fun TablesModelMonth.loadWithCategoryCounter(): MutableList<TablesModelMonth> = kotlin.run {
    return@run mutableListOf<TablesModelMonth>().apply {
        add(this@loadWithCategoryCounter)
        TablesModelMonth(
            mTableName = "positive",
            tableDisplay = "positive",
            mTableTime = 1L
        ).run {
            this@apply.add(this)
        }
        TablesModelMonth(
            mTableName = "negative",
            tableDisplay = "negative",
            mTableTime = 2L
        ).run {
            this@apply.add(this)
        }
    }
}

internal inline val Long.fetchMinAndMaxDate: com.pt.common.global.DSackT<Long, Long>
    get() {
        return fetchCalender.let { table ->
            CalendarLate.getInstance().apply {
                this[table[CalendarLate.YEAR], table[CalendarLate.MONTH]] = 1
            }.let { cm ->
                System.currentTimeMillis().fetchCalender.let {
                    CalendarLate.getInstance().apply {
                        if (it[CalendarLate.MONTH] == table[CalendarLate.MONTH]) {
                            this[table[CalendarLate.YEAR], table[CalendarLate.MONTH]] =
                                it[CalendarLate.DAY_OF_MONTH]
                        } else {
                            this[table[CalendarLate.YEAR], table[CalendarLate.MONTH]] =
                                table.getActualMaximum(CalendarLate.DATE)
                        }
                    }.let { calendarMax ->
                        com.pt.common.global.DSackT(cm.timeInMillis, calendarMax.timeInMillis)
                    }
                }
            }
        }
    }