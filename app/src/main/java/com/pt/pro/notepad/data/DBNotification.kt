package com.pt.pro.notepad.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.pt.common.BuildConfig.*
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.moderator.over.MySQLiteOpenHelper
import com.pt.common.stable.*
import com.pt.pro.notepad.models.DataNotification

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
class DBNotification(
    context: Context,
) : MySQLiteOpenHelper(context, DateUtils.DATABASE_NOT, null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        (db ?: return).dropTableNotify()
        db.createNotTable()
    }

    private fun SQLiteDatabase.createNotTable() {
        kotlin.runCatching {
            DateUtils.apply {
                (DATA_CREATE + " " + TABLE_NOTIFY + " (" +
                        ID_T + " " + DATA_PRIM_INT + ", " +
                        ID_NOT + " " + DATA_INT + " " + DATA_UNIQUE + ", " +
                        TEXT_DATA_NOT + " " + DATA_TXT + ", " +
                        TIME_NOTIFY + " " + DATA_INT + " " + DATA_UNIQUE + ", " +
                        RECORD_PATH + " " + DATA_TXT + ", " +
                        IMG_LINK + " " + DATA_TXT + ", " +
                        DONE_NOT + " " + DATA_INT + ", " +
                        NAME_TABLE + " " + DATA_TXT + ", " +
                        TABLE_USER + " " + DATA_TXT + ", " +
                        TABLE_INDEX + " " + DATA_INT + ", " +
                        TEXT_INDEX + " " + DATA_INT + " " +
                        ");").let {
                    execSQL(it)
                }
            }
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID${DateUtils.TABLE_NOTIFY}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun DataNotification.addNotification(): Long {
        return kotlin.runCatching {
            writableDatabase.insertWithOnConflict(
                DateUtils.TABLE_NOTIFY,
                null,
                toContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID${DateUtils.TABLE_NOTIFY}")
            -1
        }
    }

    fun DataNotification.updateNotification(): Int {
        return kotlin.runCatching {
            writableDatabase.updateWithOnConflict(
                DateUtils.TABLE_NOTIFY,
                toContentValues(),
                DateUtils.ID_NOT + " " + DATA_EQUAL,
                arrayOf(idData.toString()),
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID${DateUtils.TABLE_NOTIFY}")
            -1
        }
    }

    private fun DataNotification.toContentValues(): ContentValues {
        return DateUtils.run {
            ContentValues(15).apply {
                put(ID_NOT, idData)
                put(TEXT_DATA_NOT, dataText)
                put(TIME_NOTIFY, timeNotify)
                put(RECORD_PATH, recordPath)
                put(IMG_LINK, imageUrl)
                put(DONE_NOT, isDone)
                put(NAME_TABLE, tableName)
                put(TABLE_USER, tableUserName)
                put(TABLE_INDEX, tableIndex)
                put(TEXT_INDEX, textIndex)
            }
        }
    }

    fun getOneData(time: Long): DataNotification? {
        val c = DateUtils.run {
            arrayOf(
                ID_T,
                ID_NOT,
                TEXT_DATA_NOT,
                TIME_NOTIFY,
                RECORD_PATH,
                IMG_LINK,
                DONE_NOT,
                NAME_TABLE,
                TABLE_USER,
                TABLE_INDEX,
                TEXT_INDEX
            )
        }
        return kotlin.runCatching {
            readableDatabase.query(
                DateUtils.TABLE_NOTIFY,
                c,
                DateUtils.TIME_NOTIFY + " " + DATA_EQUAL,
                arrayOf(time.toString()),
                null,
                null,
                null
            ).useMy {
                DateUtils.run {
                    if (this@useMy.moveToFirst()) {
                        DataNotification(
                            idData = this@useMy.curInt(ID_NOT),
                            dataText = this@useMy.curStr(TEXT_DATA_NOT),
                            timeNotify = this@useMy.curLong(TIME_NOTIFY),
                            recordPath = this@useMy.curStr(RECORD_PATH),
                            imageUrl = this@useMy.curStr(IMG_LINK),
                            isDone = this@useMy.curBool(DONE_NOT),
                            tableName = this@useMy.curStr(NAME_TABLE),
                            tableUserName = this@useMy.curStr(TABLE_USER),
                            tableIndex = this@useMy.curInt(TABLE_INDEX),
                            textIndex = this@useMy.curInt(TEXT_INDEX)
                        )
                    } else {
                        null
                    }
                }
            }
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID${DateUtils.TABLE_NOTIFY}")
            null
        }
    }

    fun getDataNotifyFilter(): MutableList<DataNotification> {
        return kotlin.runCatching {
            DateUtils.TIME_NOTIFY + " " + DATA_MORE
            readableDatabase.query(
                DateUtils.TABLE_NOTIFY,
                null,
                DateUtils.TIME_NOTIFY + " " + DATA_MORE,
                arrayOf(System.currentTimeMillis().toString()),
                null,
                null,
                null
            )?.useMy {
                this@useMy.buildNotification()
            } ?: mutableListOf()
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID${DateUtils.TABLE_NOTIFY}")
            mutableListOf()
        }
    }


    private fun Cursor?.buildNotification(): MutableList<DataNotification> = kotlin.run {
        if (this@buildNotification == null) return@run mutableListOf()
        val msgList: MutableList<DataNotification> = mutableListOf()
        useMy {
            DateUtils.apply {
                while (this@useMy.moveToNext()) {
                    DataNotification(
                        idData = this@useMy.curInt(ID_NOT),
                        dataText = this@useMy.curStr(TEXT_DATA_NOT),
                        timeNotify = this@useMy.curLong(TIME_NOTIFY),
                        recordPath = this@useMy.curStr(RECORD_PATH),
                        imageUrl = this@useMy.curStr(IMG_LINK),
                        isDone = this@useMy.curBool(DONE_NOT),
                        tableName = this@useMy.curStr(NAME_TABLE),
                        tableUserName = this@useMy.curStr(TABLE_USER),
                        tableIndex = this@useMy.curInt(TABLE_INDEX),
                        textIndex = this@useMy.curInt(TEXT_INDEX)
                    ).let { i ->
                        msgList.add(i)
                    }
                }
            }
        }
        return@run msgList
    }

    fun deleteNot(time_: Long): Int {
        return kotlin.runCatching {
            writableDatabase.delete(
                DateUtils.TABLE_NOTIFY,
                DateUtils.TIME_NOTIFY + " " + DATA_EQUAL,
                arrayOf(time_.toString())
            )
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID${DateUtils.TABLE_NOTIFY}")
            -1
        }
    }

    private fun SQLiteDatabase.dropTableNotify() {
        execSQL(DATA_DROP + " " + DateUtils.TABLE_NOTIFY + ";")
    }
}