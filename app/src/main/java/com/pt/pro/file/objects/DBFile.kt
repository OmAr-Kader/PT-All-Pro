package com.pt.pro.file.objects

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.pt.common.BuildConfig.*
import com.pt.common.global.FileLate
import com.pt.common.global.FileSack
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.moderator.over.MySQLiteOpenHelper
import com.pt.common.stable.*
import com.pt.pro.file.interfaces.DBFileListener

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
class DBFile(
    context: Context,
) : MySQLiteOpenHelper(context, DB_FILE_NAME, null, 1), DBFileListener {

    private inline val columnFile: Array<String>
        get() {
            return run {
                arrayOf(
                    FILE_NAME,
                    PATH_FILE,
                    URI_FILE,
                    SIZE_FILE,
                    FILE_TYPE_FILE,
                    SUB_NUMBER_FILE,
                    DATA_MOD_FILE,
                    VIR_NAME_FILE
                )
            }
        }

    override fun onCreate(p0: SQLiteDatabase?) {
        (p0 ?: return).dropMsgTable()
        p0.createTable()

        p0.dropMsgTableTwo()
        p0.createTableTwo()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    private fun SQLiteDatabase.createTable() {
        runCatching {
            (DATA_CREATE + " " + TABLE_FILE + " (" +
                    ID_FILE + " " + DATA_PRIM_INT + ", " +
                    FILE_NAME + " " + DATA_TXT + " " + DATA_NO_NU + ", " +
                    PATH_FILE + " " + DATA_TXT + " " + DATA_NO_NU + ", " +
                    PATH_VIR_NAME + " " + DATA_TXT + " " + DATA_NO_NU +
                    " " + DATA_UNIQUE + ", " +
                    URI_FILE + " " + DATA_TXT + ", " +
                    SIZE_FILE + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                    FILE_TYPE_FILE + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                    SUB_NUMBER_FILE + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                    DATA_MOD_FILE + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                    VIR_NAME_FILE + " " + DATA_INT + " " +
                    ");").let {
                execSQL(it)
            }
        }.getOrElse {
            it.stackTraceToString().logProvCrash("INVALID$TABLE_FILE")
        }
    }

    override suspend fun MutableList<FileSack>.insertFavFile(vir: String?) {
        withBackCurDef(-1L) {
            writableDatabase.useBack {
                runCatching {
                    onEachSusBack(this@useBack) {
                        ContentValues(11).apply {
                            put(FILE_NAME, fileName)
                            put(PATH_FILE, filePath)
                            put(PATH_VIR_NAME, filePath + (vir ?: (DATA_NULL)))
                            put(URI_FILE, fileUri)
                            put(SIZE_FILE, fileSize)
                            put(FILE_TYPE_FILE, typeFile)
                            put(SUB_NUMBER_FILE, (FileLate(filePath).list()?.size ?: 0))
                            put(DATA_MOD_FILE, dateModified)
                            put(VIR_NAME_FILE, (vir ?: (DATA_NULL)))
                        }.run {
                            this@useBack.insertWithOnConflict(
                                TABLE_FILE,
                                null,
                                this,
                                SQLiteDatabase.CONFLICT_IGNORE
                            )
                        }
                    }
                }.getOrElse {
                    it.stackTraceToString().logProvCrash(TABLE_FILE)
                    -1L
                }
            }
        }
    }

    override suspend fun FileSack.insertFavFile(vir: String?): Long = justCoroutineCur {
        withBackCurDef(-1L) {
            return@withBackCurDef runCatching {
                run {
                    ContentValues(11).apply {
                        put(FILE_NAME, fileName)
                        put(PATH_FILE, filePath)
                        put(PATH_VIR_NAME, filePath + (vir ?: (DATA_NULL)))
                        put(URI_FILE, fileUri)
                        put(SIZE_FILE, fileSize)
                        put(FILE_TYPE_FILE, typeFile)
                        put(SUB_NUMBER_FILE, (FileLate(filePath).list()?.size ?: 0))
                        put(DATA_MOD_FILE, dateModified)
                        put(VIR_NAME_FILE, (vir ?: (DATA_NULL)))
                    }
                }.run {
                    writableDatabase.insertWithOnConflict(
                        TABLE_FILE,
                        null,
                        this,
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                }
            }.getOrElse {
                it.stackTraceToString().logProvCrash(TABLE_FILE)
                -1L
            }
        }
    }


    override suspend fun getAllVFavFiles(name: String?): MutableList<FileSack> = justCoroutineCur {
        withBackCurDef(mutableListOf()) {
            return@withBackCurDef runCatching {
                val ord = if (name == null) {
                    ID_FILE.toStr + " " + DATA_DEC
                } else {
                    ID_FILE.toStr + " " + DATA_ASC
                }
                readableDatabase.query(
                    TABLE_FILE,
                    columnFile,
                    VIR_NAME_FILE.toStr + " " + DATA_EQUAL,
                    if (name == null) arrayOf(null.toString()) else arrayOf(name),
                    null,
                    null,
                    ord,
                    null
                )?.useBack {
                    buildFavoriteFileList(name)
                } ?: mutableListOf()
            }.getOrElse {
                it.stackTraceToString().logProvCrash(TABLE_FILE)
                mutableListOf()
            }
        }
    }

    private suspend fun Cursor.buildFavoriteFileList(name: String?) = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            val msgList: MutableList<FileSack> = mutableListOf()
            var isFileDeleted = false
            this@buildFavoriteFileList.useBack {
                while (this@useBack.moveToNext()) {
                    this@useBack.curStr(PATH_FILE)?.let<String, Unit> { path ->
                        if (FileLate(path).exists()) {
                            this@useBack.curStr(FILE_NAME)?.let<String, Unit> { itN ->
                                FileSack(
                                    fileName = itN,
                                    filePath = path,
                                    fileUri = this@useBack.curStr(URI_FILE),
                                    fileSize = this@useBack.curLong(SIZE_FILE),
                                    typeFile = this@useBack.curInt(FILE_TYPE_FILE),
                                    dateModified = this@useBack.curLong(DATA_MOD_FILE),
                                    virName = this@useBack.curStr(VIR_NAME_FILE)
                                ).run { msgList.add(this) }
                            }

                        } else {
                            this@useBack.curStr(VIR_NAME_FILE)?.let<String, Unit> { it1 ->
                                isFileDeleted = true
                                deleteFile1(path, it1)
                            }
                        }
                    }
                }
            }
            if (isFileDeleted && name != null) {
                updateVir(name, msgList.size)
            }
            return@withBackCurDef msgList
        }
    }

    override suspend fun getAllFavPath(): MutableList<String> = justCoroutineCur {
        withBackCurDef(mutableListOf()) {
            runCatching {
                val msgList: MutableList<String> = mutableListOf()
                readableDatabase.query(
                    TABLE_FILE,
                    arrayOf(PATH_FILE, VIR_NAME_FILE),
                    VIR_NAME_FILE.toStr + " " + DATA_EQUAL,
                    arrayOf(DATA_NULL),
                    null,
                    null,
                    ID_FILE.toStr + " " + DATA_DEC,
                    null
                )?.useBack {
                    while (moveToNext()) {
                        this.curStr(PATH_FILE)?.let<String, Unit> { path ->
                            if (FileLate(path).exists()) {
                                msgList.add(path)
                            } else {
                                this.curStr(VIR_NAME_FILE)?.let<String, Unit> { it1 ->
                                    deleteFile1(path, it1)
                                }
                            }
                        }
                    }
                }
                msgList
            }.getOrElse {
                it.stackTraceToString().logProvCrash(TABLE_FILE)
                mutableListOf()
            }
        }
    }

    override suspend fun getAllVirName(): com.pt.common.global.DSack<MutableList<FileSack>, MutableList<String>, MutableList<String>> =
        justCoroutineCur {
            withBackCurDef(com.pt.common.global.DSack(mutableListOf(), mutableListOf(), mutableListOf())) {
                runCatching {
                    val msgList: MutableList<String> = mutableListOf()
                    val virList: MutableList<String> = mutableListOf()
                    readableDatabase.query(
                        TABLE_FILE,
                        arrayOf(PATH_FILE, VIR_NAME_FILE),
                        VIR_NAME_FILE.toStr + " " + DATA_NOT_EQUAL,
                        arrayOf(DATA_NULL),
                        null,
                        null,
                        ID_FILE.toStr + " " + DATA_DEC,
                        null
                    )?.useBack {
                        while (moveToNext()) {
                            this.curStr(PATH_FILE)?.let<String, Unit> { it1 ->
                                msgList.add(it1)
                            }
                            this.curStr(VIR_NAME_FILE)?.let<String, Unit> { it1 ->
                                virList.add(it1)
                            }
                        }
                    }
                    com.pt.common.global.DSack<MutableList<FileSack>, MutableList<String>, MutableList<String>>(mutableListOf(), msgList, virList)
                }.getOrElse {
                    it.stackTraceToString().logProvCrash(TABLE_FILE)
                    com.pt.common.global.DSack(mutableListOf(), mutableListOf(), mutableListOf())
                }
            }
        }

    override suspend fun deleteMedia(path: String, vir: String): Int = justCoroutineCur {
        withBackCurDef(-1) {
            deleteFile1(path, vir)
        }
    }

    private suspend fun deleteFile1(path: String, vir: String) = justCoroutineCur {
        withBackCurDef(-1) {
            val wh = PATH_FILE + DATA_EQUAL + " " + DATA_AND + " " +
                    VIR_NAME_FILE + " " + DATA_EQUAL
            runCatching {
                writableDatabase.delete(TABLE_FILE, wh, arrayOf(path, vir))
            }.getOrElse {
                it.stackTraceToString().logProvCrash(TABLE_FILE)
                -1
            }
        }
    }

    private fun SQLiteDatabase.dropMsgTable() {
        execSQL(DATA_DROP.toStr + " " + TABLE_FILE + ";")
    }

    private fun SQLiteDatabase.createTableTwo() {
        (DATA_CREATE + " " + TABLE_TWO_FILE + " (" +
                ID_TWO_FILE + " " + DATA_PRIM_INT + ", " +
                VIRTUAL_NAME_FILE + " " + DATA_TXT + " " + DATA_NO_NU +
                " " + DATA_UNIQUE + ", " +
                VIR_NUMBER_FILE + " " + DATA_INT + " " + DATA_NO_NU + " " +
                ");").let {
            execSQL(it)
        }
    }

    override suspend fun insertVirFile(name: String): Long = justCoroutineCur {
        withBackCurDef(-1L) {
            runCatching {
                ContentValues(3).apply {
                    put(VIRTUAL_NAME_FILE, name)
                    put(VIR_NUMBER_FILE, 0)
                }.run {
                    writableDatabase.insertWithOnConflict(
                        TABLE_TWO_FILE,
                        null,
                        this,
                        SQLiteDatabase.CONFLICT_IGNORE
                    )
                }
            }.getOrElse {
                it.stackTraceToString().logProvCrash(TABLE_TWO_FILE)
                return@withBackCurDef -1L
            }
        }
    }

    override suspend fun updateVir(
        name: String,
        num: Int,
    ): Boolean = withBackCurDef(false) {
        runCatching {
            (DATA_UPDATE + " " + TABLE_TWO_FILE +
                    " " + DATA_SET + " " + VIR_NUMBER_FILE + " = ?" +
                    " " + DATA_WHERE + " " + VIRTUAL_NAME_FILE + " = ?").let { com ->
                writableDatabase.execSQL(com, arrayOf(num, name))
            }
            return@withBackCurDef true
        }.getOrElse {
            it.stackTraceToString().logProvCrash(TABLE_TWO_FILE)
            return@withBackCurDef false
        }
    }

    override suspend fun updateVirtual(
        name: String,
        num: Int,
    ): Boolean = withBackCurDef(false) {
        runCatching {
            (DATA_UPDATE + " " + TABLE_TWO_FILE +
                    " " + DATA_SET + " " + VIR_NUMBER_FILE + " = " +
                    VIR_NUMBER_FILE + " + ?" +
                    " " + DATA_WHERE + " " + VIRTUAL_NAME_FILE + " = ?").let { com ->
                writableDatabase.execSQL(com, arrayOf(num, name))
            }
            return@withBackCurDef true
        }.getOrElse {
            it.stackTraceToString().logProvCrash(TABLE_TWO_FILE)
            return@withBackCurDef false
        }
    }

    override suspend fun getAllVIrsFull(): MutableList<String> = justCoroutineCur {
        withBackCurDef(mutableListOf()) {
            runCatching {
                val msgList: MutableList<String> = mutableListOf()
                readableDatabase.query(
                    TABLE_TWO_FILE,
                    arrayOf(ID_TWO_FILE, VIRTUAL_NAME_FILE),
                    null,
                    null,
                    null,
                    null,
                    ID_TWO_FILE.toStr + " " + DATA_DEC,
                    null
                )?.useBack {
                    while (moveToNext()) {
                        this.curStr(VIRTUAL_NAME_FILE)?.let<String, Unit> { it1 ->
                            msgList.add(it1)
                        }
                    }
                }
                msgList
            }.getOrElse {
                it.stackTraceToString().logProvCrash(TABLE_TWO_FILE)
                mutableListOf()
            }
        }
    }

    override suspend fun getAllVIrs(): MutableList<FileSack> = justCoroutineCur {
        withBackCurDef(mutableListOf()) {
            runCatching {
                val msgList: MutableList<FileSack> = mutableListOf()
                readableDatabase.query(
                    TABLE_TWO_FILE,
                    arrayOf(
                        ID_TWO_FILE,
                        VIRTUAL_NAME_FILE,
                        VIR_NUMBER_FILE
                    ),
                    VIR_NUMBER_FILE.toStr + " " + DATA_NOT_EQUAL,
                    arrayOf("0"),
                    null,
                    null,
                    ID_TWO_FILE.toStr + " " + DATA_DEC,
                    null
                )?.useBack {
                    while (moveToNext()) {
                        this.curStr(VIRTUAL_NAME_FILE)?.let { itN ->
                            FileSack(
                                fileName = itN,
                                filePath = "",
                                fileUri = null,
                                fileSize = this.curInt(VIR_NUMBER_FILE).toLong(),
                                typeFile = FOLDER,
                                dateModified = 0L,
                                isReal = false,
                                virName = this.curStr(VIRTUAL_NAME_FILE)
                            ).let { msgList.add(it) }
                        }
                    }
                }
                msgList
            }.getOrElse {
                it.stackTraceToString().logProvCrash(TABLE_TWO_FILE)
                mutableListOf()
            }
        }
    }

    private fun SQLiteDatabase.dropMsgTableTwo() {
        execSQL(DATA_DROP.toStr + " " + TABLE_TWO_FILE + ";")
    }
}