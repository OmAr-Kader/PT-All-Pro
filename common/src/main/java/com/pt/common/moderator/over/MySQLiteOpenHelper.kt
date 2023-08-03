package com.pt.common.moderator.over

abstract class MySQLiteOpenHelper(
    context: android.content.Context,
    name: String,
    factory: android.database.sqlite.SQLiteDatabase.CursorFactory?,
    version: Int,
) : android.database.sqlite.SQLiteOpenHelper(
    context,
    name,
    factory,
    version
), java.io.Closeable, AutoCloseable