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
class DBDataUser(
    context: Context,
) : MySQLiteOpenHelper(context, DateUtils.DB_DATA_USER_FILE, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        (db ?: return).dropMsgTable()
        db.createTable()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        (db ?: return).dropMsgTable()
        db.createTable()
    }

    private fun SQLiteDatabase.createTable() {
        kotlin.runCatching {
            DateUtils.apply {
                (DATA_CREATE + " " + TABLE_DB_DATA_USER + " (" +
                        ID_DATA_USER + " " + DATA_PRIM_INT + ", " +
                        USER_ID + " " + DATA_TXT + " " + DATA_UNIQUE + ", " +
                        USER_NAME + " " + DATA_TXT + " " +
                        ");").let {
                    execSQL(it)
                }
            }

        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID${DateUtils.TABLE_DB_DATA_USER}")
        }
    }

    suspend fun TablesModelUser.insertUser() {
        withBackCurDef(-1) {
            kotlin.runCatching {
                ContentValues(2).apply {
                    put(DateUtils.USER_ID, userId)
                    put(DateUtils.USER_NAME, userName)
                }.letSusBack<ContentValues, Unit> { cv ->
                    writableDatabase.insertWithOnConflict(
                        DateUtils.TABLE_DB_DATA_USER,
                        null,
                        cv,
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID${DateUtils.TABLE_DB_DATA_USER}")
            }
        }
    }

    suspend fun getAllUsers(): MutableList<TablesModelUser> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef kotlin.runCatching {
                val msgList: MutableList<TablesModelUser> = mutableListOf()
                readableDatabase.query(
                    DateUtils.TABLE_DB_DATA_USER,
                    arrayOf(DateUtils.ID_DATA_USER, DateUtils.USER_ID, DateUtils.USER_NAME),
                    null,
                    null,
                    null,
                    null,
                    DateUtils.ID_DATA_USER + " " + DATA_ASC,
                    null
                )?.useBack {
                    while (this@useBack.moveToNext()) {
                        TablesModelUser(
                            userId = this@useBack.curStr(DateUtils.USER_ID),
                            userName = this@useBack.curStr(DateUtils.USER_NAME)
                        ).letSusBack<TablesModelUser, Unit> { tmm ->
                            msgList.add(tmm)
                        }
                    }
                }
                msgList
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID${DateUtils.TABLE_DB_DATA_USER}")
                mutableListOf()
            }
        }
    }

    private fun SQLiteDatabase.dropMsgTable() {
        kotlin.runCatching {
            execSQL(DATA_DROP + " " + DateUtils.TABLE_DB_DATA_USER + ";")
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID${DateUtils.TABLE_DB_DATA_USER}")
        }
    }
}