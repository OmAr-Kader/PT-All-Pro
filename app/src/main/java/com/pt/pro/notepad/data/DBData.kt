package com.pt.pro.notepad.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.pt.common.BuildConfig.*
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.moderator.over.MySQLiteOpenHelper
import com.pt.common.stable.*
import com.pt.pro.notepad.data.DateUtils.buildColorList
import com.pt.pro.notepad.data.DateUtils.buildDataQuery
import com.pt.pro.notepad.data.DateUtils.buildEmailList
import com.pt.pro.notepad.data.DateUtils.buildImagesList
import com.pt.pro.notepad.data.DateUtils.buildRecordList
import com.pt.pro.notepad.data.DateUtils.toContentValues
import com.pt.pro.notepad.data.DateUtils.toContentValuesNot
import com.pt.pro.notepad.data.DateUtils.toContentValuesSus
import com.pt.pro.notepad.models.DataKeeperItem

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
class DBData(
    context: Context,
    DATABASE_NAME: String,
) : MySQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {}

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    suspend fun createMsgTable(aa: String) {
        withBackCurDef(-1) {
            kotlin.runCatching {
                writableDatabase.useBack {
                    DateUtils.apply {
                        (DATA_CREATE + " " + aa + " (" +
                                ID_DATA + " " + DATA_PRIM_INT + ", " +
                                TEXT_DATA + " " + DATA_TXT + ", " +
                                TEXT_TYPE + " " + DATA_INT + ", " +
                                EMAIL_SUB + " " + DATA_TXT + ", " +
                                EMAIL_TO + " " + DATA_TXT + ", " +
                                TIME + " " + DATA_INT + ", " +
                                IMG_URL + " " + DATA_TXT + ", " +
                                FIlE_PATH + " " + DATA_TXT + ", " +
                                LENGTH + " " + DATA_INT + ", " +
                                IS_DONE + " " + DATA_INT + ", " +
                                NOTIFY_TIME + " " + DATA_INT + ", " +
                                DAY_SEARCH + " " + DATA_INT + ", " +
                                LINK_HINT + " " + DATA_TXT + " " +
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

    suspend fun DataKeeperItem.insertMsg(ss: String?) {
        kotlin.runCatching {
            writableDatabase.useBack {
                this?.insert(ss, null, toContentValuesSus())
            }
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID$ss")
        }
    }

    fun DataKeeperItem.upDateRecord(ss: String?) {
        kotlin.runCatching {
            writableDatabase.useMy {
                this@useMy?.update(
                    ss,
                    toContentValues(),
                    DateUtils.TIME + " " + DATA_EQUAL,
                    arrayOf(timeData.toString())
                )
            }
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID$ss")
        }
    }

    fun upDateNotify(ss: String?, timeData: String, notTime: Long) {
        writableDatabase.useMy {
            this@useMy?.update(
                ss,
                toContentValuesNot(notTime),
                DateUtils.TIME + " " + DATA_EQUAL,
                arrayOf(timeData)
            )
        }
    }

    suspend fun getAllChat(ss: String?): MutableList<DataKeeperItem> = justCoroutineCur {
        withBackCurDef(mutableListOf()) {
            kotlin.runCatching {
                readableDatabase.useSusIT {
                    it?.query(
                        ss,
                        null,
                        DateUtils.IS_DONE + " " + DATA_EQUAL,
                        arrayOf("0"),
                        null,
                        null,
                        DateUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildDataQuery()
                    } ?: mutableListOf()
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                mutableListOf()
            }
        }
    }

    suspend fun getAllImages(
        ss: String?,
        isInclude: Boolean,
    ): MutableList<DataKeeperItem> = justCoroutineCur {
        withBackCurDef(mutableListOf()) {
            kotlin.runCatching {
                val columns = DateUtils.run {
                    arrayOf(ID_DATA, TIME, IMG_URL, IS_DONE, TEXT_TYPE, NOTIFY_TIME, DAY_SEARCH)
                }
                val where = if (isInclude) {
                    DateUtils.IMG_URL + " " + DATA_NOT_EQUAL
                } else {
                    (DateUtils.IMG_URL + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                            DateUtils.IS_DONE + " " + DATA_EQUAL)
                }
                val arg = if (isInclude) arrayOf(DATA_NULL) else arrayOf(DATA_NULL, "0")
                readableDatabase.useSusIT {
                    it?.query(
                        ss,
                        columns,
                        where,
                        arg,
                        null,
                        null,
                        DateUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildImagesList()
                    } ?: mutableListOf()
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                mutableListOf()
            }
        }
    }

    suspend fun getAllRecord(
        ss: String?,
        isInc: Boolean,
    ): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef kotlin.runCatching {
                val c = DateUtils.run {
                    arrayOf(
                        ID_DATA,
                        TIME,
                        FIlE_PATH,
                        TEXT_TYPE,
                        LENGTH,
                        DAY_SEARCH,
                        IS_DONE,
                        NOTIFY_TIME
                    )
                }
                val where = if (isInc) {
                    DateUtils.FIlE_PATH + " " + DATA_NOT_EQUAL
                } else {
                    (DateUtils.FIlE_PATH + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                            DateUtils.IS_DONE + " " + DATA_EQUAL)
                }
                readableDatabase.useSusIT {
                    it?.query(
                        ss,
                        c,
                        where,
                        if (isInc) arrayOf(DATA_NULL) else arrayOf(DATA_NULL, "0"),
                        null,
                        null,
                        DateUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildRecordList()
                    } ?: mutableListOf()
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                mutableListOf()
            }
        }
    }

    suspend fun getAllTypes(
        ss: String?,
        type: String?,
        isInclude: Boolean,
    ): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            kotlin.runCatching {
                val where = if (isInclude) {
                    DateUtils.TEXT_TYPE + " " + DATA_EQUAL
                } else {
                    (DateUtils.TEXT_TYPE + " " + DATA_EQUAL + " " + DATA_AND + " " +
                            DateUtils.IS_DONE + " " + DATA_EQUAL)
                }
                val c = DateUtils.run {
                    arrayOf(ID_DATA, TEXT_DATA, TEXT_TYPE, TIME, IS_DONE, NOTIFY_TIME, DAY_SEARCH)
                }
                readableDatabase.useSusIT {
                    it?.query(
                        ss,
                        c,
                        where,
                        if (isInclude) arrayOf(type) else arrayOf(type, "0"),
                        null,
                        null,
                        DateUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildColorList()
                    } ?: mutableListOf()
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                mutableListOf()
            }
        }
    }


    suspend fun getAllEmail(
        ss: String?,
        isIn: Boolean,
    ): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef kotlin.runCatching {
                val c = DateUtils.run {
                    arrayOf(
                        ID_DATA,
                        TEXT_DATA,
                        EMAIL_SUB,
                        EMAIL_TO,
                        TIME,
                        DAY_SEARCH,
                        IS_DONE,
                        NOTIFY_TIME
                    )
                }
                val where = if (isIn) {
                    DateUtils.EMAIL_TO + " " + DATA_NOT_EQUAL
                } else {
                    (DateUtils.EMAIL_TO + " " + DATA_NOT_EQUAL + " " + DATA_AND + " " +
                            DateUtils.IS_DONE + " " + DATA_EQUAL)
                }
                val arg = if (isIn) arrayOf(DATA_NULL) else arrayOf(DATA_NULL, "0")
                readableDatabase.useSusIT {
                    it?.query(
                        ss,
                        c,
                        where,
                        arg,
                        null,
                        null,
                        DateUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildEmailList()
                    } ?: mutableListOf()
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                mutableListOf()
            }
        }
    }

    suspend fun getAllDone(ss: String?): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            kotlin.runCatching {
                readableDatabase.useSusIT {
                    it?.query(
                        ss,
                        null,
                        DateUtils.IS_DONE + " " + DATA_EQUAL,
                        arrayOf("1"),
                        null,
                        null,
                        DateUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildDataQuery()
                    } ?: mutableListOf()
                }
            }.getOrElse { e ->
                e.toStr.logProvCrash("INVALID$ss")
                mutableListOf()
            }
        }
    }

    suspend fun getInTime(
        ss: String?,
        dayOfMonth: Int,
        isInclude: Boolean,
    ): MutableList<DataKeeperItem> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            kotlin.runCatching {
                val whereClause = if (isInclude) {
                    DateUtils.DAY_SEARCH + " " + DATA_EQUAL
                } else {
                    DateUtils.IS_DONE + " " + DATA_EQUAL + " " + DATA_AND + " " +
                            DateUtils.DAY_SEARCH + " " + DATA_EQUAL
                }
                val arg = if (isInclude) {
                    arrayOf(dayOfMonth.toString())
                } else {
                    arrayOf("0", dayOfMonth.toString())
                }
                readableDatabase.useSusIT {
                    it?.query(
                        ss,
                        null,
                        whereClause,
                        arg,
                        null,
                        null,
                        DateUtils.TIME + " " + DATA_ASC,
                        null
                    )?.useBack {
                        this@useBack.buildDataQuery()
                    } ?: mutableListOf()
                }
            }.getOrElse { e ->
                e.stackTraceToString().logProvCrash("INVALID$ss")
                mutableListOf()
            }
        }
    }

    fun deleteDate(time_: Long, ss: String?): Int {
        return kotlin.runCatching {
            writableDatabase.useMy {
                this@useMy?.delete(
                    ss,
                    DateUtils.TIME + " " + DATA_EQUAL,
                    arrayOf(time_.toString())
                ) ?: -1
            }
        }.getOrElse { e ->
            e.toStr.logProvCrash("INVALID$ss")
            -1
        }
    }

}


