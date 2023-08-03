package com.pt.common.mutual.pref

import com.pt.common.BuildConfig
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.stable.*

class DBPreference(
    context: android.content.Context,
) : com.pt.common.moderator.over.MySQLiteOpenHelper(context, DB_PREF, null, 1) {

    override fun onCreate(p0: android.database.sqlite.SQLiteDatabase?) {
        (p0 ?: return).dropMsgTable()
        p0.createTable()
    }

    override fun onUpgrade(p0: android.database.sqlite.SQLiteDatabase?, p1: Int, p2: Int) {

    }

    private fun android.database.sqlite.SQLiteDatabase.createTable() {
        runCatching {
            (BuildConfig.DATA_CREATE + " " + PREF_TABLE_NAME + " (" +
                    KEY_PRI + " " + BuildConfig.DATA_PRIM_INT + ", " +
                    KEY_PREF + " " + BuildConfig.DATA_TXT + " " + BuildConfig.DATA_NO_NU +
                    " " + BuildConfig.DATA_UNIQUE + ", " +
                    STRING_PREF + " " + BuildConfig.DATA_TXT + ", " +
                    INT_PREF + " " + BuildConfig.DATA_INT + " " +
                    ");").let {
                execSQL(it)
            }
        }.getOrElse {
            it.toStr.logProvCrash("INVALID$TABLE_FILE")
        }
    }

    suspend fun addPrefString(
        keyString: String,
        strPref: String,
    ): Long = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(-1L) {
            android.content.ContentValues(4).applySusBack {
                put(KEY_PREF, keyString)
                put(STRING_PREF, strPref)
                putNull(INT_PREF)
            }.letSusBack {
                return@letSusBack writableDatabase.insertWithOnConflict(
                    PREF_TABLE_NAME,
                    null,
                    it,
                    android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
                )
            }
        }
    }

    /*suspend fun updateString(
        keyString: String,
        strPref: String? = null,
    ): Int = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(-1) {
            android.content.ContentValues(4).applySusBack {
                put(KEY_PREF, keyString)
                put(STRING_PREF, strPref)
                putNull(INT_PREF)
            }.letSusBack {
                return@letSusBack writableDatabase.updateWithOnConflict(
                    PREF_TABLE_NAME,
                    it,
                    "$KEY_PREF ${BuildConfig.DATA_EQUAL}",
                    arrayOf(keyString),
                    android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
                )
            }
        }
    }*/

    /*suspend fun updateInt(
        keyString: String,
        intPref: Int?,
        longPref: Long?,
    ): Int = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(-1) {
            android.content.ContentValues(4).applySusBack {
                put(KEY_PREF, keyString)
                putNull(STRING_PREF)
                if (intPref != null) {
                    put(INT_PREF, intPref)
                } else {
                    put(INT_PREF, longPref)
                }
            }.letSusBack {
                return@letSusBack writableDatabase.updateWithOnConflict(
                    PREF_TABLE_NAME,
                    it,
                    "$KEY_PREF ${BuildConfig.DATA_EQUAL}",
                    arrayOf(keyString),
                    android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
                )
            }
        }
    }*/

    suspend fun getPrefString(keyString: String, def: suspend () -> String): String =
        justCoroutineCur {
            return@justCoroutineCur withBackCurDef(null) {
                return@withBackCurDef runCatching {
                    return@runCatching readableDatabase.query(
                        PREF_TABLE_NAME,
                        null,
                        "$KEY_PREF ${BuildConfig.DATA_EQUAL}",
                        arrayOf(keyString),
                        null,
                        null,
                        null
                    ).useBack {
                        if (moveToFirst()) {
                            curStr(STRING_PREF)
                        } else {
                            null
                        }
                    }
                }.getOrElse {
                    it.toStr.logProvCrash("DBAlarm")
                    return@withBackCurDef null
                }
            } ?: runSusBack {
                def.invoke().alsoSusBack {
                    addPrefString(keyString, it)
                }
            }
        }

    suspend fun getPrefStringNull(keyString: String): String? = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(null) {
            return@withBackCurDef runCatching {
                return@runCatching readableDatabase.query(
                    PREF_TABLE_NAME,
                    null,
                    "$KEY_PREF ${BuildConfig.DATA_EQUAL}",
                    arrayOf(keyString),
                    null,
                    null,
                    null
                ).useBack {
                    if (moveToFirst()) {
                        curStr(STRING_PREF)
                    } else {
                        null
                    }
                }
            }.getOrElse {
                return@withBackCurDef null
            }
        }
    }

    suspend fun addPrefInt(
        keyString: String,
        intPref: Int?,
        longPref: Long?,
    ): Long = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(-1L) {
            android.content.ContentValues(4).applySusBack {
                put(KEY_PREF, keyString)
                putNull(STRING_PREF)
                if (intPref != null) {
                    put(INT_PREF, intPref)
                } else {
                    put(INT_PREF, longPref)
                }
            }.letSusBack {
                return@letSusBack writableDatabase.insertWithOnConflict(
                    PREF_TABLE_NAME,
                    null,
                    it,
                    android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
                )
            }
        }
    }

    suspend fun getPrefInt(keyString: String, def: suspend () -> Int): Int = justCoroutineCur {
        return@justCoroutineCur withDefaultDef(null) {
            return@withDefaultDef runCatching {
                return@runCatching readableDatabase.query(
                    PREF_TABLE_NAME,
                    null,
                    "$KEY_PREF ${BuildConfig.DATA_EQUAL}",
                    arrayOf(keyString),
                    null,
                    null,
                    null
                ).useBack {
                    if (moveToFirst()) {
                        curIntNull(INT_PREF)
                    } else {
                        null
                    }
                }
            }.getOrElse {
                return@withDefaultDef null
            }
        } ?: runSusBack {
            def.invoke().alsoSusBack {
                addPrefInt(keyString, intPref = it, longPref = null)
            }
        }
    }

    suspend fun getPrefLong(keyString: String, def: suspend () -> Long): Long =
        justCoroutineCur {
            return@justCoroutineCur withBackCurDef(null) {
                return@withBackCurDef runCatching {
                    return@runCatching readableDatabase.query(
                        PREF_TABLE_NAME,
                        null,
                        "$KEY_PREF ${BuildConfig.DATA_EQUAL}",
                        arrayOf(keyString),
                        null,
                        null,
                        null
                    ).useBack {
                        if (moveToFirst()) {
                            curLongNull(INT_PREF)
                        } else {
                            null
                        }
                    }
                }.getOrElse {
                    it.toStr.logProvCrash("DBAlarm")
                    return@withBackCurDef null
                }
            } ?: runSusBack {
                def.invoke().alsoSusBack {
                    addPrefInt(keyString, intPref = null, longPref = it)
                }
            }
        }

    suspend fun getPrefBoolean(keyString: String, def: suspend () -> Boolean): Boolean =
        justCoroutineCur {
            return@justCoroutineCur withBackCurDef(null) {
                return@withBackCurDef runCatching {
                    return@runCatching readableDatabase.query(
                        PREF_TABLE_NAME,
                        null,
                        "$KEY_PREF ${BuildConfig.DATA_EQUAL}",
                        arrayOf(keyString),
                        null,
                        null,
                        null
                    ).useBack {
                        if (moveToFirst()) {
                            curIntNull(INT_PREF)
                        } else {
                            null
                        }
                    }
                }.getOrElse {
                    it.toStr.logProvCrash("DBAlarm")
                    return@withBackCurDef null
                }
            }.runSusBack {
                if (this@runSusBack != null) {
                    this@runSusBack == 1
                } else {
                    def.invoke().alsoSusBack {
                        addPrefInt(keyString, intPref = if (it) 1 else 0, longPref = null)
                    }
                }
            }
        }

    suspend fun getPrefBooleanNull(keyString: String): Boolean? = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(null) {
            return@withBackCurDef runCatching {
                return@runCatching readableDatabase.query(
                    PREF_TABLE_NAME,
                    null,
                    "$KEY_PREF ${BuildConfig.DATA_EQUAL}",
                    arrayOf(keyString),
                    null,
                    null,
                    null
                ).useBack {
                    if (moveToFirst()) {
                        curIntNull(INT_PREF) == 1
                    } else {
                        null
                    }
                }
            }.getOrElse {
                it.toStr.logProvCrash("DBAlarm")
                return@withBackCurDef null
            }
        }
    }

    private fun android.database.sqlite.SQLiteDatabase.dropMsgTable() {
        execSQL(BuildConfig.DATA_DROP.toStr + " " + PREF_TABLE_NAME + ";")
    }

}