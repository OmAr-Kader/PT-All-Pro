package com.pt.pro.notepad.data

import android.content.ContentValues
import android.database.Cursor
import com.pt.common.stable.*
import com.pt.pro.notepad.models.DataKeeperItem

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
object DateUtils {

    const val ID_DATA: String = "idData"
    const val TEXT_DATA: String = "textData"
    const val TEXT_TYPE: String = "textType"
    const val EMAIL_SUB: String = "emailSubject"
    const val EMAIL_TO: String = "toEmail"
    const val TIME: String = "timeData"
    const val IMG_URL: String = "imageUrl"
    const val FIlE_PATH: String = "filePath"
    const val LENGTH: String = "lengthData"
    const val IS_DONE: String = "isDoneData"
    const val NOTIFY_TIME: String = "timeNot"

    const val LINK_HINT: String = "hintLink"
    const val DAY_SEARCH: String = "daySearch"

////////////////////////////////////////////////////////////////////////////////////////////////////

    const val DB_DATA_USER_FILE: String = "date_users.db"
    const val ID_DATA_USER: String = "idDataUSer"
    const val USER_ID: String = "textDataUSer"
    const val USER_NAME: String = "colorDataUSer"
    const val TABLE_DB_DATA_USER: String = "dataDataUSer"

////////////////////////////////////////////////////////////////////////////////////////////////////

    const val ID_TABLES: String = "id"
    const val TABLES_NAME_VALID: String = "tablesList"
    const val TABLES_DISPLAY_NOT_VALID: String = "tablesListDisplay"
    const val TABLES_TIME: String = "tablesTime"


////////////////////////////////////////////////////////////////////////////////////////////////////

    const val DATABASE_NOT: String = "all_notify.db"
    const val TABLE_NOTIFY: String = "notificationTable"
    const val ID_T: String = "idAutoNotify"
    const val ID_NOT: String = "idNotify"
    const val TEXT_DATA_NOT: String = "textNotify"
    const val TIME_NOTIFY: String = "timeForNotify"
    const val RECORD_PATH: String = "recordUri"
    const val IMG_LINK: String = "imageLink"
    const val DONE_NOT: String = "doneIs"
    const val NAME_TABLE: String = "nameTable"
    const val TABLE_USER: String = "tableUser"
    const val TABLE_INDEX: String = "indexIfTable"
    const val TEXT_INDEX: String = "textIndex"

    fun DataKeeperItem.toContentValues(): ContentValues = kotlin.run {
        return@run ContentValues(10).apply {
            put(TEXT_DATA, dataText)
            put(TIME, timeData)
            put(TEXT_TYPE, keeperType)
            put(EMAIL_SUB, emailSubject)
            put(EMAIL_TO, emailToSome)
            put(IMG_URL, imageUrl)
            put(FIlE_PATH, recordPath)
            put(LENGTH, recordLength)
            put(DAY_SEARCH, dayNum)
            put(IS_DONE, isDone)
            put(NOTIFY_TIME, notTime)
        }
    }

    suspend fun DataKeeperItem.toContentValuesSus(): ContentValues = justCoroutine {
        return@justCoroutine ContentValues(10).applySusBack {
            put(TEXT_DATA, dataText)
            put(TIME, timeData)
            put(TEXT_TYPE, keeperType)
            put(EMAIL_SUB, emailSubject)
            put(EMAIL_TO, emailToSome)
            put(IMG_URL, imageUrl)
            put(FIlE_PATH, recordPath)
            put(LENGTH, recordLength)
            put(DAY_SEARCH, dayNum)
            put(IS_DONE, isDone)
            put(NOTIFY_TIME, notTime)
        }
    }

    fun toContentValuesNot(notTime: Long): ContentValues = kotlin.run {
        return@run ContentValues(2).apply {
            put(NOTIFY_TIME, notTime)
        }
    }

    suspend fun Cursor.buildDataQuery(): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            val msgList: MutableList<DataKeeperItem> = mutableListOf()
            this@buildDataQuery.useBack {
                while (this@useBack.moveToNext()) {
                    DataKeeperItem(
                        dataText = this@useBack.curStr(TEXT_DATA),
                        timeData = this@useBack.curLong(TIME),
                        imageUrl = this@useBack.curStr(IMG_URL),
                        keeperType = this@useBack.curInt(TEXT_TYPE),
                        emailSubject = this@useBack.curStr(EMAIL_SUB),
                        emailToSome = this@useBack.curStr(EMAIL_TO),
                        recordPath = this@useBack.curStr(FIlE_PATH),
                        recordLength = this@useBack.curLong(LENGTH),
                        isDone = this@useBack.curBool(IS_DONE),
                        dayNum = this@useBack.curInt(DAY_SEARCH),
                        notTime = this@useBack.curLong(NOTIFY_TIME)
                    ).let<DataKeeperItem, Unit> { itD ->
                        msgList.add(itD)
                    }
                }
            }
            return@withBackCurDef msgList
        }
    }

    suspend fun Cursor.buildImagesList(): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            val msgList: MutableList<DataKeeperItem> = mutableListOf()
            this@buildImagesList.useBack {
                while (this@useBack.moveToNext()) {
                    DataKeeperItem(
                        dataText = null,
                        keeperType = this@useBack.curInt(TEXT_TYPE),
                        emailToSome = null,
                        emailSubject = null,
                        timeData = this@useBack.curLong(TIME),
                        recordPath = null,
                        recordLength = 0L,
                        imageUrl = this@useBack.curStr(IMG_URL),
                        isDone = this@useBack.curBool(IS_DONE),
                        dayNum = this@useBack.curInt(DAY_SEARCH),
                        notTime = this@useBack.curLong(NOTIFY_TIME)
                    ).let<DataKeeperItem, Unit> { itD ->
                        msgList.add(itD)
                    }
                }
            }
            return@withBackCurDef msgList
        }
    }

    suspend fun Cursor.buildRecordList(): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            val msgList: MutableList<DataKeeperItem> = mutableListOf()
            this@buildRecordList.useBack {
                while (this@useBack.moveToNext()) {
                    DataKeeperItem(
                        dataText = null,
                        keeperType = this@useBack.curInt(TEXT_TYPE),
                        emailToSome = null,
                        emailSubject = null,
                        timeData = this@useBack.curLong(TIME),
                        recordPath = this@useBack.curStr(FIlE_PATH),
                        recordLength = this@useBack.curLong(LENGTH),
                        imageUrl = null,
                        dayNum = this@useBack.curInt(DAY_SEARCH),
                        isDone = this@useBack.curBool(IS_DONE),
                        notTime = this@useBack.curLong(NOTIFY_TIME)
                    ).let { itD ->
                        msgList.add(itD)
                    }
                }
            }
            return@withBackCurDef msgList
        }
    }

    suspend fun Cursor.buildColorList(): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            val msgList: MutableList<DataKeeperItem> = mutableListOf()
            this@buildColorList.useBack {
                while (this@useBack.moveToNext()) {
                    DataKeeperItem(
                        dataText = this@useBack.curStr(TEXT_DATA),
                        keeperType = this@useBack.curInt(TEXT_TYPE),
                        emailToSome = null,
                        emailSubject = null,
                        timeData = this@useBack.curLong(TIME),
                        recordPath = null,
                        recordLength = 0L,
                        imageUrl = null,
                        dayNum = this@useBack.curInt(DAY_SEARCH),
                        isDone = this@useBack.curBool(IS_DONE),
                        notTime = this@useBack.curLong(NOTIFY_TIME)
                    ).let { itD ->
                        msgList.add(itD)
                    }
                }
            }
            return@withBackCurDef msgList
        }
    }

    suspend fun Cursor.buildEmailList(): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            val msgList: MutableList<DataKeeperItem> = mutableListOf()
            this@buildEmailList.useBack {
                while (this@useBack.moveToNext()) {
                    DataKeeperItem(
                        dataText = this@useBack.curStr(TEXT_DATA),
                        keeperType = this@useBack.curInt(TEXT_TYPE),
                        emailToSome = this@useBack.curStr(EMAIL_TO),
                        emailSubject = this@useBack.curStr(EMAIL_SUB),
                        timeData = this@useBack.curLong(TIME),
                        recordPath = null,
                        recordLength = 0L,
                        imageUrl = null,
                        dayNum = this@useBack.curInt(DAY_SEARCH),
                        isDone = this@useBack.curBool(IS_DONE),
                        notTime = this@useBack.curLong(NOTIFY_TIME)
                    ).let<DataKeeperItem, Unit> { itD ->
                        msgList.add(itD)
                    }
                }
            }
            return@withBackCurDef msgList
        }
    }

}