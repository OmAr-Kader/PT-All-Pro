package com.pt.pro.notepad.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.pt.common.BuildConfig.*
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.moderator.over.MySQLiteOpenHelper
import com.pt.common.stable.*
import com.pt.pro.notepad.data.CounterUtils.buildCounterList
import com.pt.pro.notepad.data.CounterUtils.buildCounterNegative
import com.pt.pro.notepad.data.CounterUtils.buildCounterPositive
import com.pt.pro.notepad.data.CounterUtils.buildKeysList
import com.pt.pro.notepad.data.CounterUtils.toContentValues
import com.pt.pro.notepad.models.CounterItem
import com.pt.pro.notepad.models.ItemForCounter

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
class DBCounter(
    context: Context,
    DATABASE_NAME: String,
) : MySQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    private inline val counterColumns
        get() = CounterUtils.run {
            arrayOf(ID_COUNTER, TIME, COUNTER_TEXT, PRICE_COUNTER, DAY_NUMBER, KEY, ALL_PRICES)
        }

    override fun onCreate(db: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    suspend fun createMsgTable(aa: String) {
        withBackCurDef(-1) {
            kotlin.runCatching {
                writableDatabase.useBack {
                    CounterUtils.apply {
                        (DATA_CREATE + " " + aa + " (" +
                                ID_COUNTER + " " + DATA_PRIM_INT + ", " +
                                COUNTER_TEXT + " " + DATA_TXT + ", " +
                                PRICE_COUNTER + " " + DATA_INT + ", " +
                                DAY_NUMBER + " " + DATA_INT + ", " +
                                KEY + " " + DATA_TXT + ", " +
                                TIME + " " + DATA_INT + " " + DATA_UNIQUE + ", " +
                                ALL_PRICES + " " + DATA_INT + " " +
                                ");").let {
                            this@useBack.execSQL(it)
                        }
                    }
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$aa")
            }
        }
    }

    suspend fun CounterItem.insertCounter(ss: String?) {
        withBackCurDef(-1) {
            kotlin.runCatching {
                writableDatabase.useBack {
                    this@useBack.insertWithOnConflict(
                        ss,
                        null,
                        toContentValues(),
                        SQLiteDatabase.CONFLICT_REPLACE
                    )
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
            }
        }
    }

    suspend fun CounterItem.updateCounter(ss: String?): Int = justCoroutine {
        withBackCurDef(-1) {
            kotlin.runCatching {
                writableDatabase.useBack {
                    this@useBack.updateWithOnConflict(
                        ss,
                        toContentValues(),
                        CounterUtils.TIME + " " + DATA_EQUAL,
                        arrayOf(time.toString()),
                        SQLiteDatabase.CONFLICT_REPLACE
                    )
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                -1
            }
        }
    }

    suspend fun getAllOnKey(ss: String?, key: String): ItemForCounter = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(ItemForCounter(mutableListOf(), 0.0)) {
            return@withBackCurDef kotlin.runCatching {
                readableDatabase.useSusIT {
                    it?.query(
                        ss,
                        counterColumns,
                        CounterUtils.KEY + " " + DATA_EQUAL,
                        arrayOf(key),
                        null,
                        null,
                        CounterUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildCounterList()
                    } ?: ItemForCounter(mutableListOf(), 0.0)
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                ItemForCounter(mutableListOf(), 0.0)
            }
        }
    }

    suspend fun getAllKeys(ss: String?): MutableList<String> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef kotlin.runCatching {
                readableDatabase.useSusIT {
                    it.query(
                        ss,
                        arrayOf(CounterUtils.KEY),
                        CounterUtils.KEY + " " + DATA_NOT_EQUAL,
                        arrayOf(DATA_NULL),
                        null,
                        null,
                        CounterUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildKeysList()
                    } ?: mutableListOf()
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                mutableListOf()
            }
        }
    }

    suspend fun getAllOnDay(ss: String?, day: Int): ItemForCounter = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(ItemForCounter(mutableListOf(), 0.0)) {
            return@withBackCurDef kotlin.runCatching {
                readableDatabase.useSusIT {
                    it.query(
                        ss,
                        counterColumns,
                        CounterUtils.DAY_NUMBER + " " + DATA_EQUAL,
                        arrayOf(day.toString()),
                        null,
                        null,
                        CounterUtils.TIME + " " + DATA_ASC,
                        null
                    ).useBack {
                        this@useBack?.buildCounterList()
                    } ?: ItemForCounter(mutableListOf(), 0.0)
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                ItemForCounter(mutableListOf(), 0.0)
            }
        }
    }

    suspend fun getAllOnDayPositive(
        ss: String?,
        day: String,
        isKeys: Boolean,
    ): ItemForCounter = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(ItemForCounter(mutableListOf(), 0.0)) {
            return@withBackCurDef kotlin.runCatching {
                val wh = if (isKeys)
                    CounterUtils.KEY + " " + DATA_EQUAL
                else
                    CounterUtils.DAY_NUMBER + " " + DATA_EQUAL

                readableDatabase.useSusIT {
                    it.query(
                        ss,
                        counterColumns,
                        wh,
                        arrayOf(day),
                        null,
                        null,
                        CounterUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildCounterPositive()
                    } ?: ItemForCounter(mutableListOf(), 0.0)
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                ItemForCounter(mutableListOf(), 0.0)
            }
        }
    }

    suspend fun getAllOnDayNegative(
        ss: String?,
        day: String,
        isKeys: Boolean,
    ): ItemForCounter = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(ItemForCounter(mutableListOf(), 0.0)) {
            return@withBackCurDef kotlin.runCatching {
                val wh = if (isKeys)
                    CounterUtils.KEY + " " + DATA_EQUAL
                else
                    CounterUtils.DAY_NUMBER + " " + DATA_EQUAL

                readableDatabase.useSusIT {
                    it.query(
                        ss,
                        counterColumns,
                        wh,
                        arrayOf(day),
                        null,
                        null,
                        CounterUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildCounterNegative()
                    } ?: ItemForCounter(mutableListOf(), 0.0)
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                ItemForCounter(mutableListOf(), 0.0)
            }
        }
    }

    suspend fun deleteCounter(timeC: Long?, ss: String?) {
        withBackCurDef(ItemForCounter(mutableListOf(), 0.0)) {
            kotlin.runCatching {
                writableDatabase.useBack {
                    this@useBack.delete(
                        ss,
                        CounterUtils.TIME + " " + DATA_EQUAL,
                        arrayOf(timeC.toString())
                    )
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
            }
        }
    }

}