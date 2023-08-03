package com.pt.pro.notepad.objects

import com.pt.common.global.toColor

const val TYPING_TIMEOUT: Long = 3000L

internal const val DIALOG_USER: String = "userDialog"

const val INTENT_ID: String = "notificationId"

const val FIRST_TIME_ANIM: String = "forFirstAnim"
const val KEY_FIRST_TIME: String = "keyForFirst"

const val NOT_BUNDLE_EXTRA: String = "notBundleExtra"
const val NOT_ALARM_KEY: String = "notAlarmKey"

const val DIALOG_NEW_USER: String = "userNew"
const val FRAGMENT_IMAGE: String = "imageFragment"
const val FRAGMENT_CALENDAR: String = "calendarDate"
const val DIALOG_CHOOSE_IMPORT: String = "importNew"
const val DIALOG_EMAIL: String = "diaEmail"

const val ADAPTER_IMAGE: Int = 0
const val ADAPTER_RECORD: Int = 11
const val ADAPTER_TEXT: Int = 22
const val ADAPTER_LINKER: Int = 55
const val ADAPTER_EMAIL: Int = 66

const val IMAGE_FILE: String = "Compressed"
const val RECORD_FILE: String = "Recorder"

@Suppress("SpellCheckingInspection")
const val TABLES_DATA_FILE: String = "dbtable.db"
const val FILE_DATA: String = "date.db"
const val MY_DATA_KEEPER_FILE: String = "PT Notepad"

const val TABLES_COUNTER: String = "dbCounter.db"
const val MY_COUNTER_FILE: String = "PT DataCounter"
const val COUNTER_FILE: String = "counter.db"

const val EMAIL_CHOOSE: String = "email"
const val IMAGE_CHOOSE: String = "Img"
const val RECORD_CHOOSE: String = "Record"

const val KEEP_DATA: Int = 0
const val KEEP_IMPORTANT: Int = 1
const val KEEP_LINK: Int = 2
const val KEEP_REMEMBER: Int = 3
const val KEEP_SCAN: Int = 4
const val KEEP_EMAIL: Int = 5
const val KEEP_RECORD: Int = 6
const val KEEP_IMG: Int = 7

const val SAMPLE_RATE: Int = 44100
const val SAMPLE_RATE_INDEX: Int = 4
const val CHANNELS: Int = 1
const val BIT_RATE: Int = 32000

const val DATA_TIME_FORM: String = "dd MMM HH:mm"
const val ADD_USER: String = "addUser"

const val HIDE_SEL: Int = -1
const val TXT_SEL: Int = 0
const val IMG_SEL: Int = 1
const val MULTI_IMG_SEL: Int = 2
const val RECORD_SEL: Int = 3
const val MULTI_RECORD_SEL: Int = 4

inline val datCol: Int get() = "#e91e63".toColor

inline val impCol: Int get() = "#3f51b5".toColor

inline val linCol: Int get() = "#E2FFC7".toColor

@Suppress("SpellCheckingInspection")
inline val remCol: Int
    get() = "#94FAEB".toColor

inline val scaCol: Int get() = "#e91e63".toColor

inline val emaCol: Int get() = "#e91e63".toColor

inline val String.noteTab: String get() = this + TABLES_DATA_FILE

inline val String.noteDb: String get() = this + FILE_DATA


inline val String.countTab: String get() = this + TABLES_COUNTER

inline val String.countDb: String get() = this + COUNTER_FILE

