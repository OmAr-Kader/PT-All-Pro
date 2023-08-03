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
class DBTables(
    context: Context,
    DATABASE_NAME: String,
) : MySQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    suspend fun createTable(ss: String) {
        withBackCurDef(-1) {
            kotlin.runCatching {
                DateUtils.apply {
                    (DATA_CREATE + " " + ss + " (" +
                            ID_TABLES + " " + DATA_PRIM_INT + ", " +
                            TABLES_NAME_VALID + " " + DATA_TXT + " " + DATA_UNIQUE + ", " +
                            TABLES_DISPLAY_NOT_VALID + " " + DATA_TXT + " " + DATA_UNIQUE + ", " +
                            TABLES_TIME + " " + DATA_INT + " " + DATA_UNIQUE + " " +
                            ");").let {
                        writableDatabase.execSQL(it)
                    }
                }
            }.onFailure { e ->
                e.toStr.logProvCrash("DBAlarm")
            }
        }
    }

    suspend fun TablesModelMonth.insertTable(ss: String) {
        withBackCurDef(-1) {
            kotlin.runCatching {
                ContentValues(3).apply {
                    put(DateUtils.TABLES_NAME_VALID, mTableName)
                    put(DateUtils.TABLES_DISPLAY_NOT_VALID, tableDisplay)
                    put(DateUtils.TABLES_TIME, mTableTime)
                }.let<ContentValues, Unit> { cv ->
                    writableDatabase.insertWithOnConflict(
                        ss,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
            }
        }
    }

    suspend fun getAllDataTables(
        ss: String,
    ): MutableList<TablesModelMonth> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef kotlin.runCatching {
                val msgList: MutableList<TablesModelMonth> = mutableListOf()
                val c = DateUtils.run {
                    arrayOf(TABLES_NAME_VALID, TABLES_DISPLAY_NOT_VALID, TABLES_TIME)
                }
                readableDatabase.query(
                    ss,
                    c,
                    null,
                    null,
                    null,
                    null,
                    DateUtils.ID_TABLES + " " + DATA_ASC,
                    null
                )?.useBack {
                    while (this@useBack.moveToNext()) {
                        TablesModelMonth(
                            mTableName = this@useBack.curStr(DateUtils.TABLES_NAME_VALID),
                            tableDisplay = this@useBack.curStr(DateUtils.TABLES_DISPLAY_NOT_VALID),
                            mTableTime = this@useBack.curLong(DateUtils.TABLES_TIME)
                        ).let<TablesModelMonth, Unit> { tmm ->
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
