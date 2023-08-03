package com.pt.common.global

import com.pt.common.mutual.pref.DbPrefInitializer.Companion.pref

suspend fun android.content.Context.findPrefBoolean(
    key: String, def: suspend () -> Boolean
): Boolean = pref.getPrefBoolean(keyString = key, def = def)

suspend fun android.content.Context.findPrefBooleanNull(
    key: String
): Boolean? = pref.getPrefBooleanNull(keyString = key)

suspend fun android.content.Context.updatePrefBoolean(
    key: String,
    newVal: Boolean,
) {
    pref.addPrefInt(keyString = key, intPref = if (newVal) 1 else 0, null)
}

suspend fun android.content.Context.findPrefInt(
    key: String, def: suspend () -> Int
): Int = pref.getPrefInt(keyString = key, def = def)

suspend fun android.content.Context.updatePrefInt(
    key: String,
    newVal: Int,
) {
    pref.addPrefInt(keyString = key, intPref = newVal, null)
}

suspend fun android.content.Context.findPrefString(
    key: String, def: suspend () -> String
): String = pref.getPrefString(keyString = key, def = def)

suspend fun android.content.Context.findPrefStringNull(
    key: String
): String? = pref.getPrefStringNull(keyString = key)

suspend fun android.content.Context.updatePrefString(
    key: String,
    newVal: String,
) {
    pref.addPrefString(keyString = key, strPref = newVal)
}

suspend fun android.content.Context.findPrefLong(
    key: String, def: suspend () -> Long
): Long = pref.getPrefLong(keyString = key, def = def)

suspend fun android.content.Context.updatePrefLong(
    key: String,
    newVal: Long,
) {
    pref.addPrefInt(keyString = key, intPref = null, longPref = newVal)
}
