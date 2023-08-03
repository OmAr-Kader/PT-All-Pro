package com.pt.pro.gallery.objects

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.pt.common.BuildConfig.*
import com.pt.common.global.FileLate
import com.pt.common.global.MediaSack
import com.pt.common.global.toStr
import com.pt.common.moderator.over.MySQLiteOpenHelper
import com.pt.common.stable.*

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
class DBGallery(context: Context) : MySQLiteOpenHelper(context, DATABASE_GAL, null, 1) {

    override fun onCreate(p0: SQLiteDatabase?) {
        (p0 ?: return).dropFavTable()
        p0.createFavTable()

        p0.dropTableSeek()
        p0.createTableSeek()
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    private fun SQLiteDatabase.createFavTable() {
        runCatching {
            (DATA_CREATE + " " + TABLE_GAL + " (" +
                    ID_GAL + " " + DATA_PRIM_INT + ", " +
                    MEDIA_NAME + " " + DATA_TXT + ", " +
                    PATH_GAL + " " + DATA_TXT + " " + DATA_NO_NU + ", " +
                    URI_GAL + " " + DATA_TXT + ", " +
                    IS_IMAGE_GAL + " " + DATA_INT + ", " +
                    SIZE_GAL + " " + DATA_INT + ", " +
                    WIDTH_GAL + " " + DATA_INT + ", " +
                    HEIGHT_GAL + " " + DATA_INT + ", " +
                    DATA_MODIFIED_GAL + " " + DATA_INT + ", " +
                    PATH_GAL_HID + " " + DATA_TXT + " " + DATA_NO_NU + " "
                    + DATA_UNIQUE + ", " +
                    IS_HIDDEN_GALLERY + " " + DATA_INT + " " +
                    ");").let {
                execSQL(it)
            }
        }.getOrElse {
            it.listThrowable("INVALID${TABLE_GAL}")
        }
    }

    suspend fun MutableList<MediaSack>.insertFavMedia(isHidden: Int) {
        withBackCurDef(-1L) {
            kotlin.runCatching {
                onEachSusBack(true) {
                    ContentValues(10).apply {
                        put(MEDIA_NAME, nameMedia)
                        put(PATH_GAL, pathMedia)
                        put(URI_GAL, uriMedia)
                        put(IS_IMAGE_GAL, isImage)
                        put(SIZE_GAL, mediaSize)
                        put(WIDTH_GAL, mediaWidth)
                        put(HEIGHT_GAL, mediaHigh)
                        put(DATA_MODIFIED_GAL, dateModified)
                        put(IS_HIDDEN_GALLERY, isHidden)
                        put(PATH_GAL_HID, (pathMedia.toStr + isHidden.toStr))
                    }.let<ContentValues, Unit> { itC ->
                        writableDatabase.useBack {
                            this@useBack?.insertWithOnConflict(
                                TABLE_GAL,
                                null,
                                itC,
                                SQLiteDatabase.CONFLICT_REPLACE
                            )
                        }
                    }
                }
            }.getOrElse {
                it.listThrowable("INVALID$TABLE_GAL")
                -1L
            }
        }
    }

    suspend fun getAllHiddenMedia(): MutableList<MediaSack> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef runCatching {
                val msgList: MutableList<MediaSack> = mutableListOf()
                val columns = arrayOf(
                    PATH_GAL,
                    MEDIA_NAME,
                    URI_GAL,
                    IS_IMAGE_GAL,
                    SIZE_GAL,
                    WIDTH_GAL,
                    HEIGHT_GAL,
                    DATA_MODIFIED_GAL
                )
                readableDatabase.query(
                    TABLE_GAL,
                    columns,
                    IS_HIDDEN_GALLERY.toStr + " " + DATA_EQUAL,
                    arrayOf("1"),
                    null,
                    null,
                    ID_GAL.toStr + " " + DATA_DES,
                    null
                ).useBack {
                    while (moveToNext()) {
                        this.curStr(PATH_GAL)?.let<String, Unit> { path ->
                            if (FileLate(path).isFile) {
                                MediaSack(
                                    nameMedia = this.curStr(MEDIA_NAME),
                                    pathMedia = path,
                                    uriMedia = this.curStr(URI_GAL),
                                    isImage = this.curBool(IS_IMAGE_GAL),
                                    mediaSize = this.curLong(SIZE_GAL),
                                    mediaWidth = this.curInt(WIDTH_GAL),
                                    mediaHigh = this.curInt(HEIGHT_GAL),
                                    dateModified = this.curLong(DATA_MODIFIED_GAL)
                                ).let(msgList::add)
                            } else {
                                deleteMedia1(path)
                            }
                        }
                    }
                }
                msgList
            }.getOrElse {
                it.listThrowable("INVALID${TABLE_GAL}")
                mutableListOf()
            }
        }
    }

    suspend fun getAllVFavMedia(): MutableList<MediaSack> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef runCatching {
                val msgList: MutableList<MediaSack> = mutableListOf()
                val columns = arrayOf(
                    PATH_GAL,
                    MEDIA_NAME,
                    URI_GAL,
                    IS_IMAGE_GAL,
                    SIZE_GAL,
                    WIDTH_GAL,
                    HEIGHT_GAL,
                    DATA_MODIFIED_GAL
                )
                readableDatabase.query(
                    TABLE_GAL,
                    columns,
                    IS_HIDDEN_GALLERY.toStr + " " + DATA_EQUAL,
                    arrayOf("0"),
                    null,
                    null,
                    ID_GAL.toStr + " " + DATA_DES,
                    null
                ).useBack {
                    while (moveToNext()) {
                        this.curStr(PATH_GAL)?.let<String, Unit> { path ->
                            if (FileLate(path).isFile) {
                                MediaSack(
                                    nameMedia = this.curStr(MEDIA_NAME),
                                    pathMedia = path,
                                    uriMedia = this.curStr(URI_GAL),
                                    isImage = this.curBool(IS_IMAGE_GAL),
                                    mediaSize = this.curLong(SIZE_GAL),
                                    mediaWidth = this.curInt(WIDTH_GAL),
                                    mediaHigh = this.curInt(HEIGHT_GAL),
                                    dateModified = this.curLong(DATA_MODIFIED_GAL),
                                    isFromFav = true,
                                ).let(msgList::add)
                            } else {
                                deleteMedia1(path)
                            }
                        }
                    }
                }
                msgList
            }.getOrElse {
                it.listThrowable("INVALID${TABLE_GAL}")
                mutableListOf()
            }
        }
    }

    suspend fun getAllFavPath(): MutableList<String> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            runCatching {
                val msgList: MutableList<String> = mutableListOf()
                readableDatabase.query(
                    TABLE_GAL,
                    arrayOf(PATH_GAL),
                    IS_HIDDEN_GALLERY.toStr + " " + DATA_EQUAL,
                    arrayOf("0"),
                    null,
                    null,
                    ID_GAL.toStr + " " + DATA_DES,
                    null
                )?.useBack {
                    while (moveToNext()) {
                        this.curStr(PATH_GAL)?.let<String, Unit> { path ->
                            if (FileLate(path).isFile) {
                                msgList.add(path)
                            } else {
                                deleteMedia1(path)
                            }
                        }
                    }
                }
                return@withBackCurDef msgList
            }.getOrElse {
                it.listThrowable("INVALID${TABLE_GAL}")
                return@withBackCurDef mutableListOf<String>()
            }
        }
    }

    suspend fun deleteFavorite(path: String): Int = withBackCurDef(-1) {
        deleteMedia1(path)
    }

    private fun deleteMedia1(path: String): Int {
        return writableDatabase.useMy {
            this@useMy?.delete(
                TABLE_GAL,
                PATH_GAL.toStr + " " + DATA_EQUAL,
                arrayOf(path)
            ) ?: -1
        }
    }

    private fun SQLiteDatabase.dropFavTable() {
        execSQL(DATA_DROP.toStr + " " + TABLE_GAL + ";")
    }


    private fun SQLiteDatabase.createTableSeek() {
        runCatching {
            (DATA_CREATE + " " + TABLE_SEEK_GAL + " (" +
                    ID_SEEK_GAL + " " + DATA_PRIM_INT + ", " +
                    VIDEOS_NAME_GAL + " " + DATA_TXT + " " + DATA_NO_NU +
                    " " + DATA_UNIQUE + ", " +
                    VIDEOS_TIME_GAL + " " + DATA_INT + " " +
                    ");").let {
                execSQL(it)
            }
        }.getOrElse {
            it.listThrowable("INVALID$TABLE_SEEK_GAL")
        }
    }

    private fun SeekVideo.insertVideo() {
        runCatching {
            ContentValues(3).apply {
                put(VIDEOS_NAME_GAL, videoName ?: return)
                put(VIDEOS_TIME_GAL, seekTime)
            }.let<ContentValues, Unit> { itC ->
                writableDatabase.insertWithOnConflict(
                    TABLE_SEEK_GAL,
                    null,
                    itC,
                    SQLiteDatabase.CONFLICT_REPLACE
                )
            }
        }.getOrElse {
            it.listThrowable("INVALID${TABLE_SEEK_GAL}")
        }
    }

    fun SeekVideo.updateVideo() {
        runCatching {
            ContentValues(3).apply {
                put(VIDEOS_NAME_GAL, videoName ?: return)
                put(VIDEOS_TIME_GAL, seekTime)
            }.let<ContentValues, Unit> { itC ->
                writableDatabase.updateWithOnConflict(
                    TABLE_SEEK_GAL,
                    itC,
                    VIDEOS_NAME_GAL.toStr + " " + DATA_EQUAL,
                    arrayOf(videoName.toString()),
                    SQLiteDatabase.CONFLICT_REPLACE
                )
            }
        }.getOrElse {
            it.listThrowable("INVALID${TABLE_SEEK_GAL}")
        }
    }

    fun getOneSeek(name: String): SeekVideo {
        return runCatching {
            val columns = arrayOf(
                ID_SEEK_GAL,
                VIDEOS_NAME_GAL,
                VIDEOS_TIME_GAL
            )
            readableDatabase.query(
                TABLE_SEEK_GAL,
                columns,
                VIDEOS_NAME_GAL.toStr + " " + DATA_EQUAL,
                arrayOf(name),
                null,
                null,
                null,
                null
            ).useMy {
                if (moveToFirst()) {
                    return@useMy SeekVideo(
                        this.curStr(VIDEOS_NAME_GAL),
                        this.curLong(VIDEOS_TIME_GAL)
                    )
                } else {
                    return@useMy null
                }
            }
        }.getOrElse {
            it.listThrowable("INVALID${TABLE_SEEK_GAL}")
            null
        }.let {
            it ?: SeekVideo(name, 1L).apply {
                insertVideo()
            }

        }
    }

    private fun SQLiteDatabase.dropTableSeek() {
        execSQL(DATA_DROP.toStr + " " + TABLE_SEEK_GAL + ";")
    }

}