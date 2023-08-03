package com.pt.pro.musical.back

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.pt.common.BuildConfig.*
import com.pt.common.global.*
import com.pt.common.moderator.over.MySQLiteOpenHelper
import com.pt.common.stable.*
import com.pt.pro.musical.interfaces.DBPlaylistListener

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
class DBPlaylist(
    context: Context,
) : MySQLiteOpenHelper(context, DATABASE_PLAYLIST, null, 1), DBPlaylistListener {

    override fun onCreate(db: SQLiteDatabase?) {
        (db ?: return).dropMsgTable()
        db.createTable()

        db.dropMsgTableTwo()
        db.createTableTwo()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //db?.alterColumn()
    }


    /*@Suppress("HardCodedStringLiteral")
    private fun SQLiteDatabase.alterColumn() {
        execSQL("ALTER TABLE $TABLE_MUSIC ADD COLUMN $LIST_PATH TEXT DEFAULT '' ")
    }*/

    private fun SQLiteDatabase.createTable() {
        runCatching {
            (DATA_CREATE + " " + TABLE_MUSIC + " (" +
                    ID_MUSIC + " " + DATA_PRIM_INT + ", " +
                    MUSIC_TIT + " " + DATA_TXT + " " + DATA_NO_NU + ", " +
                    PATH_SONG + " " + DATA_TXT + " " + DATA_NO_NU + ", " +
                    SONG_ARTIST + " " + DATA_TXT + " " + DATA_NO_NU + ", " +
                    LIST_PATH + " " + DATA_TXT + " " + DATA_NO_NU +
                    " " + DATA_UNIQUE + ", " +
                    BELONG_PLAYLIST + " " + DATA_TXT + " " + DATA_NO_NU + ", " +
                    IS_DEFAULT_PLAYLIST + " " + DATA_INT + " " + DATA_NO_NU + " " +
                    ");").let {
                execSQL(it)
            }
        }.getOrElse {
            it.listThrowable("INVALID${TABLE_MUSIC}")
        }
    }

    override suspend fun MutableList<MusicSack>.insertMusic() {
        withBackCurDef(-1L) {
            writableDatabase.useBack {
                runCatching {
                    onEachSusBack(this@useBack) {
                        ContentValues(4).apply {
                            put(MUSIC_TIT, title.toString())
                            put(PATH_SONG, pathAudio.toString())
                            put(SONG_ARTIST, artist.toString())
                            put(BELONG_PLAYLIST, playlistSong.toString())
                            put(IS_DEFAULT_PLAYLIST, 0)
                            put(LIST_PATH, playlistSong.toString() + pathAudio.toString())
                        }.run {
                            this@useBack?.insertWithOnConflict(
                                TABLE_MUSIC,
                                null,
                                this,
                                SQLiteDatabase.CONFLICT_REPLACE
                            )
                        }
                    }
                }.getOrElse {
                    it.listThrowable("INVALID${TABLE_MUSIC}")
                    -1L
                }
            }
        }
    }


    override suspend fun MutableList<MusicSack>.insertMusicDefault() {
        withBackCurDef(Unit) {
            writableDatabase.useBack {
                runCatching {
                    onEachSusBack(this@useBack) {
                        ContentValues(4).apply {
                            put(MUSIC_TIT, title.toString())
                            put(PATH_SONG, pathAudio.toString())
                            put(SONG_ARTIST, artist.toString())
                            put(BELONG_PLAYLIST, DEFAULT_PLAYLIST)
                            put(IS_DEFAULT_PLAYLIST, 1)
                            put(LIST_PATH, DEFAULT_PLAYLIST + pathAudio.toString())
                        }.run {
                            this@useBack?.insertWithOnConflict(
                                TABLE_MUSIC,
                                null,
                                this,
                                SQLiteDatabase.CONFLICT_IGNORE
                            )
                        }
                    }
                }.getOrElse {
                    it.listThrowable("INVALID${TABLE_MUSIC}")
                }
            }
        }
    }


    override suspend fun getAllSongs(playlist: String): MutableList<MusicSack> = justCoroutineCur {
        withBackCurDef(mutableListOf()) {
            runCatching {
                val msgList: MutableList<MusicSack> = mutableListOf()
                var someFilesDeleted = false
                readableDatabase.query(
                    TABLE_MUSIC,
                    arrayOf(
                        MUSIC_TIT,
                        PATH_SONG,
                        SONG_ARTIST,
                        BELONG_PLAYLIST
                    ),
                    BELONG_PLAYLIST + " " + DATA_EQUAL + DATA_AND + " " +
                            IS_DEFAULT_PLAYLIST + " " + DATA_EQUAL,
                    arrayOf(playlist, "0"),
                    null,
                    null,
                    ID_MUSIC.toStr + " " + DATA_ASC,
                    null
                )?.useBack {
                    while (moveToNext()) {
                        curStr(PATH_SONG).toString().let { path ->
                            if (FileLate(path).exists()) {
                                MusicSack(
                                    title = curStr(MUSIC_TIT),
                                    pathAudio = path,
                                    artist = curStr(SONG_ARTIST),
                                    playlistSong = curStr(BELONG_PLAYLIST)
                                ).let { itM ->
                                    msgList.add(itM)
                                }
                            } else {
                                someFilesDeleted = true
                                deleteMusic1(path)
                            }
                        }
                    }
                }
                if (someFilesDeleted) {
                    updatePlaylist(playlist, msgList.size)
                }
                return@withBackCurDef msgList
            }.getOrElse {
                it.listThrowable("INVALID${TABLE_MUSIC}")
                return@withBackCurDef mutableListOf()
            }
        }
    }

    override suspend fun getAllSongsDefault(): MutableList<MusicSack> = justCoroutineCur {
        withBackCurDef(mutableListOf()) {
            runCatching {
                val msgList: MutableList<MusicSack> = mutableListOf()
                readableDatabase.query(
                    TABLE_MUSIC,
                    arrayOf(
                        MUSIC_TIT,
                        PATH_SONG,
                        SONG_ARTIST,
                        BELONG_PLAYLIST
                    ),
                    IS_DEFAULT_PLAYLIST.toStr + " " + DATA_EQUAL,
                    arrayOf("1"),
                    null,
                    null,
                    ID_MUSIC.toStr + " " + DATA_ASC,
                    null
                )?.useBack {
                    while (moveToNext()) {
                        curStr(PATH_SONG).toString().let { path ->
                            if (FileLate(path).exists()) {
                                MusicSack(
                                    title = curStr(MUSIC_TIT),
                                    pathAudio = path,
                                    artist = curStr(SONG_ARTIST),
                                    playlistSong = curStr(BELONG_PLAYLIST)
                                ).let { itM ->
                                    msgList.add(itM)
                                }
                            } else {
                                deleteMusic1(path)
                            }
                        }
                    }
                }
                return@withBackCurDef msgList
            }.getOrElse {
                it.listThrowable("INVALID${TABLE_MUSIC}")
                return@withBackCurDef mutableListOf()
            }
        }
    }

    override suspend fun deleteAllMusic(playlist: String?): Int = justCoroutineCur {
        withBackCurDef(-1) {
            runCatching {
                val c = if (playlist == null) {
                    (IS_DEFAULT_PLAYLIST.toStr + " " + DATA_EQUAL)
                } else {
                    (BELONG_PLAYLIST + " " + DATA_EQUAL + DATA_AND + " " +
                            IS_DEFAULT_PLAYLIST + " " + DATA_EQUAL)
                }
                val s = if (playlist == null) {
                    arrayOf("1")
                } else {
                    arrayOf(playlist, "0")
                }
                writableDatabase.delete(TABLE_MUSIC, c, s)
            }.getOrElse {
                it.listThrowable("INVALID${TABLE_MUSIC}")
                -1
            }
        }
    }

    private suspend fun deleteMusic1(path: String): Int = justCoroutineCur {
        withBackCurDef(-1) {
            runCatching {
                writableDatabase.delete(
                    TABLE_MUSIC,
                    PATH_SONG.toStr + " " + DATA_EQUAL,
                    arrayOf(path)
                )
            }.getOrElse {
                it.listThrowable("INVALID${TABLE_MUSIC}")
                -1
            }
        }
    }

    private fun SQLiteDatabase.dropMsgTable() {
        execSQL(DATA_DROP.toStr + " " + TABLE_MUSIC)
    }


    private fun SQLiteDatabase.createTableTwo() {
        runCatching {
            (DATA_CREATE + " " + PLAYLIST_TABLE + " (" +
                    ID_PLAYLIST + " " + DATA_PRIM_INT + ", " +
                    PLAYLIST_NAME + " " + DATA_TXT + " " + DATA_NO_NU +
                    " " + DATA_UNIQUE + ", " +
                    PLAYLIST_FIRST + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                    PLAYLIST_POSITION + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                    PLAYLIST_NUMBERS + " " + DATA_INT + " " + DATA_NO_NU + " " +
                    ");").let {
                execSQL(it)
            }
        }.getOrElse {
            it.listThrowable("INVALID$PLAYLIST_TABLE")
        }
    }


    override suspend fun MusicSack.insertPlaylist(): Long = justCoroutineCur {
        withBackCurDef(-1L) {
            return@withBackCurDef runCatching {
                ContentValues(4).apply {
                    put(PLAYLIST_NAME, title.toString())
                    put(PLAYLIST_FIRST, pathAudio.toString())
                    put(PLAYLIST_NUMBERS, artist.toString())
                    put(PLAYLIST_POSITION, idArtAlb)
                }.run {
                    writableDatabase.useBack {
                        this?.insert(PLAYLIST_TABLE, null, this@run) ?: -1L
                    }
                }
            }.getOrElse {
                it.listThrowable("INVALID${PLAYLIST_TABLE}")
                -1L
            }
        }
    }

    override suspend fun updatePlaylist(
        name: String,
        num: Int,
    ): Boolean = withBackCurDef(false) {
        runCatching {
            (DATA_UPDATE + " " + PLAYLIST_TABLE +
                    " " + DATA_SET + " " + PLAYLIST_NUMBERS + " = ?" +
                    " " + DATA_WHERE + " " + PLAYLIST_NAME + " = ?").let { com ->
                writableDatabase.execSQL(com, arrayOf(num, name))
            }
            return@withBackCurDef true
        }.getOrElse {
            it.listThrowable(PLAYLIST_TABLE)
            return@withBackCurDef false
        }
    }

    override suspend fun isAlreadyPlaylist(playlist: String): Boolean = justCoroutineCur {
        withBackCurDef(false) {
            runCatching {
                readableDatabase.query(
                    PLAYLIST_TABLE,
                    null,
                    PLAYLIST_NAME.toStr + " " + DATA_EQUAL,
                    arrayOf(playlist),
                    null,
                    null,
                    ID_PLAYLIST.toStr + " " + DATA_ASC,
                    null
                )?.useBack {
                    return@useBack moveToFirst()
                } ?: false
            }.getOrElse {
                it.listThrowable("INVALID${PLAYLIST_TABLE}")
                return@withBackCurDef false
            }
        }
    }

    override suspend fun getAllPlaylist(): MutableList<MusicSack> = justCoroutineCur {
        withBackCurDef(mutableListOf()) {
            runCatching {

                val msgList: MutableList<MusicSack> = mutableListOf()
                readableDatabase.query(
                    PLAYLIST_TABLE,
                    arrayOf(
                        PLAYLIST_NAME,
                        PLAYLIST_FIRST,
                        PLAYLIST_POSITION,
                        PLAYLIST_NUMBERS
                    ),
                    null,
                    null,
                    null,
                    null,
                    ID_PLAYLIST.toStr + " " + DATA_ASC,
                    null
                )?.useBack {
                    while (moveToNext()) {
                        MusicSack(
                            title = curStr(PLAYLIST_NAME),
                            pathAudio = curStr(PLAYLIST_FIRST),
                            idArtAlb = curInt(PLAYLIST_POSITION),
                            artist = curInt(PLAYLIST_NUMBERS).toString(),
                            songType = ALL_PLAYLIST
                        ).let { itM ->
                            msgList.add(itM)
                        }
                    }
                }
                return@withBackCurDef msgList
            }.getOrElse {
                it.listThrowable("INVALID${PLAYLIST_TABLE}")
                return@withBackCurDef mutableListOf()
            }
        }
    }

    private fun SQLiteDatabase.dropMsgTableTwo() {
        execSQL(DATA_DROP.toStr + " " + PLAYLIST_TABLE + ";")
    }

}