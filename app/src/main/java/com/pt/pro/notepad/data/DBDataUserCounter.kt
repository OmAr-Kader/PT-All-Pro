package com.pt.pro.notepad.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.pt.common.BuildConfig.*
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.moderator.over.MySQLiteOpenHelper
import com.pt.common.stable.*
import com.pt.pro.notepad.models.TablesModelUser

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
class DBDataUserCounter(
    context: Context,
) : MySQLiteOpenHelper(context, CounterUtils.USER_COUNTER_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.dropMsgTable()
        db?.createTable()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropMsgTable()
        db?.createTable()
    }

    private fun SQLiteDatabase.createTable() {
        kotlin.runCatching {
            CounterUtils.apply {
                (DATA_CREATE + " " + TABLE_COUNTER_USER + " (" +
                        ID_COUNTER_USER + " " + DATA_PRIM_INT + ", " +
                        USER_ID_COUNTER + " " + DATA_TXT + " " + DATA_UNIQUE + ", " +
                        USER_NAME_COUNTER + " " + DATA_TXT + " " +
                        ");").let {
                    execSQL(it)
                }
            }
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID${CounterUtils.TABLE_COUNTER_USER}")
        }
    }

    suspend fun TablesModelUser.insertCounterUser() {
        withBackCurDef(-1) {
            kotlin.runCatching {
                ContentValues(2).apply {
                    put(CounterUtils.USER_ID_COUNTER, userId)
                    put(CounterUtils.USER_NAME_COUNTER, userName)
                }.let { cv ->
                    writableDatabase.insertWithOnConflict(
                        CounterUtils.TABLE_COUNTER_USER,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID${CounterUtils.TABLE_COUNTER_USER}")
            }
        }
    }

    suspend fun getAllCounterUsers(): MutableList<TablesModelUser> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef kotlin.runCatching {
                val msgList: MutableList<TablesModelUser> = mutableListOf()
                val c = CounterUtils.run {
                    arrayOf(ID_COUNTER_USER, USER_ID_COUNTER, USER_NAME_COUNTER)
                }
                readableDatabase?.query(
                    CounterUtils.TABLE_COUNTER_USER,
                    c,
                    null,
                    null,
                    null,
                    null,
                    CounterUtils.ID_COUNTER_USER + " " + DATA_ASC,
                    null
                )?.useBack {
                    while (this@useBack.moveToNext()) {
                        TablesModelUser(
                            userId = this@useBack.curStr(CounterUtils.USER_ID_COUNTER),
                            userName = this@useBack.curStr(CounterUtils.USER_NAME_COUNTER)
                        ).letSusBack<TablesModelUser, Unit> { tmm ->
                            msgList.add(tmm)
                        }
                    }
                }
                msgList
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID${CounterUtils.TABLE_COUNTER_USER}")
                mutableListOf()
            }
        }
    }

    private fun SQLiteDatabase.dropMsgTable() {
        execSQL(DATA_DROP + " " + CounterUtils.TABLE_COUNTER_USER + ";")
    }
}