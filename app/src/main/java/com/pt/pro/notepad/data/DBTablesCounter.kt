package com.pt.pro.notepad.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.pt.common.BuildConfig.*
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.moderator.over.MySQLiteOpenHelper
import com.pt.common.stable.*
import com.pt.pro.notepad.models.TablesModelMonth

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
class DBTablesCounter(
    context: Context,
    DATABASE_NAME: String,
) : MySQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    suspend fun createTable(ss: String) {
        withBackCurDef(-1) {
            kotlin.runCatching {
                CounterUtils.apply {
                    (DATA_CREATE + " " + ss + " (" +
                            ID_COUNTER_TABLES + " " + DATA_PRIM_INT + ", " +
                            TABLES_NAME_VALID + " " + DATA_TXT + " " + DATA_UNIQUE + ", " +
                            TABLES_DISPLAY_COUNTER + " " + DATA_TXT + " " + DATA_UNIQUE +
                            ", " +
                            TABLES_TIME_COUNTER + " " + DATA_INT + " " + DATA_UNIQUE + " " +
                            ");").let {
                        writableDatabase.execSQL(it)
                    }
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
            }
        }
    }

    suspend fun TablesModelMonth.insertTable(ss: String) {
        withBackCurDef(-1) {
            kotlin.runCatching {
                ContentValues(4).applySusBack {
                    put(CounterUtils.TABLES_NAME_VALID, mTableName)
                    put(CounterUtils.TABLES_DISPLAY_COUNTER, tableDisplay)
                    put(CounterUtils.TABLES_TIME_COUNTER, mTableTime)
                }.letSusBack { cv ->
                    return@letSusBack writableDatabase.insertWithOnConflict(
                        ss,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                e.toStr.logProvCrash("INVALID$ss")
            }
        }
    }

    suspend fun getAllCounterTables(
        ss: String,
    ): MutableList<TablesModelMonth> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef kotlin.runCatching {
                val msgList: MutableList<TablesModelMonth> = mutableListOf()
                readableDatabase.query(
                    ss,
                    null,
                    null,
                    null,
                    null,
                    null,
                    CounterUtils.ID_COUNTER_TABLES + " " + DATA_ASC,
                    null
                )?.useBack {
                    while (this@useBack.moveToNext()) {
                        TablesModelMonth(
                            mTableName = this@useBack.curStr(CounterUtils.TABLES_NAME_VALID),
                            tableDisplay = this@useBack.curStr(CounterUtils.TABLES_DISPLAY_COUNTER),
                            mTableTime = this@useBack.curLong(CounterUtils.TABLES_TIME_COUNTER)
                        ).let { tmm ->
                            msgList.add(tmm)
                        }
                    }
                }
                msgList
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                mutableListOf()
            }
        }
    }

}
