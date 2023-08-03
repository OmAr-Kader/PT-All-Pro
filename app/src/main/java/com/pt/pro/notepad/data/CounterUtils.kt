package com.pt.pro.notepad.data

import android.content.ContentValues
import android.database.Cursor
import com.pt.common.stable.*
import com.pt.pro.notepad.models.CounterItem
import com.pt.pro.notepad.models.ItemForCounter
import kotlin.math.abs

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
object CounterUtils {

    const val ID_COUNTER: String = "idCounter"
    const val TIME: String = "timeCounter"

    const val COUNTER_TEXT: String = "textCounter"
    const val PRICE_COUNTER: String = "priceCounter"
    const val DAY_NUMBER: String = "numberDay"
    const val KEY: String = "keyReach"
    const val ALL_PRICES: String = "allPrices"


////////////////////////////////////////////////////////////////////////////////////////////////////

    const val USER_COUNTER_NAME: String = "date_users_counter.db"
    const val ID_COUNTER_USER: String = "idCounterUSer"
    const val USER_ID_COUNTER: String = "textCounterUSer"
    const val USER_NAME_COUNTER: String = "colorCounterUSer"
    const val TABLE_COUNTER_USER: String = "dataCounterUSer"

////////////////////////////////////////////////////////////////////////////////////////////////////

    const val ID_COUNTER_TABLES: String = "idCounterTables"
    const val TABLES_NAME_VALID: String = "tablesListCounter"
    const val TABLES_DISPLAY_COUNTER: String = "tablesCounterDisplay"
    const val TABLES_TIME_COUNTER: String = "tablesTime"

    suspend fun CounterItem.toContentValues(): ContentValues = justCoroutineCur {
        return@justCoroutineCur ContentValues(5).apply {
            put(TIME, time)
            put(COUNTER_TEXT, counterText)
            put(PRICE_COUNTER, price)
            put(DAY_NUMBER, day)
            put(KEY, key.toString())
        }
    }

    suspend fun Cursor.buildCounterList(): ItemForCounter = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(ItemForCounter(mutableListOf(), 0.0)) {
            var prices = 0.0
            val msgList: MutableList<CounterItem> = mutableListOf()
            useBack {
                while (moveToNext()) {
                    curDouble(PRICE_COUNTER).letSusBack { itD ->
                        prices += itD
                        CounterItem(
                            idCounter = curLong(ID_COUNTER),
                            time = curLong(TIME),
                            counterText = curStr(COUNTER_TEXT),
                            price = itD,
                            day = curInt(DAY_NUMBER),
                            key = curStr(KEY)
                        ).letSusBack<CounterItem, Unit> { ci ->
                            msgList.add(ci)
                        }
                    }
                }
            }
            return@withBackCurDef ItemForCounter(msgList, prices)
        }
    }

    suspend fun Cursor.buildCounterPositive(): ItemForCounter = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(ItemForCounter(mutableListOf(), 0.0)) {
            var prices = 0.0
            val msgList: MutableList<CounterItem> = mutableListOf()
            this@buildCounterPositive.useBack {
                while (this@useBack.moveToNext()) {
                    this@useBack.curDouble(PRICE_COUNTER).letSusBack<Double, Unit> { priceC ->
                        if (priceC == abs(priceC)) {
                            prices += priceC
                            CounterItem(
                                idCounter = this@useBack.curLong(ID_COUNTER),
                                time = this@useBack.curLong(TIME),
                                counterText = this@useBack.curStr(COUNTER_TEXT),
                                price = priceC,
                                day = this@useBack.curInt(DAY_NUMBER),
                                key = this@useBack.curStr(KEY)
                            ).letSusBack<CounterItem, Unit> { ci ->
                                msgList.add(ci)
                            }
                        }
                    }

                }
            }
            return@withBackCurDef ItemForCounter(msgList, prices)
        }
    }

    suspend fun Cursor.buildCounterNegative(): ItemForCounter = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(ItemForCounter(mutableListOf(), 0.0)) {
            val msgList: MutableList<CounterItem> = mutableListOf()
            var prices = 0.0
            this@buildCounterNegative.useBack {
                while (this@useBack.moveToNext()) {
                    this@useBack.curDouble(PRICE_COUNTER).letSusBack<Double, Unit> { priceC ->
                        if (priceC != abs(priceC)) {
                            prices += priceC
                            CounterItem(
                                idCounter = this@useBack.curLong(ID_COUNTER),
                                time = this@useBack.curLong(TIME),
                                counterText = this@useBack.curStr(COUNTER_TEXT),
                                price = priceC,
                                day = this@useBack.curInt(DAY_NUMBER),
                                key = this@useBack.curStr(KEY)
                            ).letSusBack<CounterItem, Unit> { ci ->
                                msgList.add(ci)
                            }
                        }
                    }
                }
            }
            return@withBackCurDef ItemForCounter(msgList, prices)
        }
    }

    suspend fun Cursor.buildKeysList(): MutableList<String> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            val msgList: MutableList<String> = mutableListOf()
            this@buildKeysList.useBack {
                while (this@useBack.moveToNext()) {
                    msgList.add(this@useBack.curStr(KEY).toString())
                }
            }
            return@withBackCurDef msgList.distinct().toMutableList()
        }
    }

}
