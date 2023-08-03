package com.pt.pro.alarm.objects

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.pt.common.BuildConfig.*
import com.pt.common.global.AlarmSack
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.moderator.over.MySQLiteOpenHelper
import com.pt.common.stable.*
import com.pt.pro.alarm.objects.AlarmHelper.buildAlarmNow
import com.pt.pro.alarm.objects.AlarmHelper.buildAlarmsAsIt
import com.pt.pro.alarm.objects.AlarmHelper.toContentValues

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
class DBAlarm(
    context: Context
) : MySQLiteOpenHelper(context, AlarmHelper.DATA_ALARM_NAME, null, 1) {

    override fun onCreate(dp: SQLiteDatabase?) {
        (dp ?: return).dropTableAlarm()
        dp.createTableAlarmNEW()
    }

    private fun SQLiteDatabase.createTableAlarmNEW() {
        runCatching {
            with(AlarmHelper) {
                (DATA_CREATE + " " + TABLE_NAME + " (" +
                        ID_ALARM + " " + DATA_PRIM_INT + ", " +
                        ALARM_ID + " " + DATA_INT + " " + DATA_UNIQUE + ", " +
                        COL_TIME + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                        COL_LABEL + " " + DATA_TXT + ", " +
                        RECORD + " " + DATA_TXT + ", " +
                        COL_RINGTONE + " " + DATA_TXT + ", " +
                        COL_DISMISS_WAY + " " + DATA_INT + ", " +
                        COL_IMG + " " + DATA_TXT + ", " +
                        COL_IS_ALARM + " " + DATA_INT + ", " +
                        COL_IS_VIBRATE + " " + DATA_INT + ", " +
                        COL_SWITCH + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                        COL_MON + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                        COL_TUES + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                        COL_WED + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                        COL_THURS + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                        COL_FRI + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                        COL_SAT + " " + DATA_INT + " " + DATA_NO_NU + ", " +
                        COL_SUN + " " + DATA_INT + " " + DATA_NO_NU + " " +
                        ");").let {
                    execSQL(it)
                }
            }
        }
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, i: Int, i1: Int) {

    }

    suspend fun AlarmSack.addAlarm(): Long = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(-1L) {
            return@withBackCurDef writableDatabase.insertWithOnConflict(
                AlarmHelper.TABLE_NAME,
                null,
                toContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }
    }

    fun AlarmSack.updateAlarm(): Int {
        return runCatching {
            writableDatabase.update(
                AlarmHelper.TABLE_NAME,
                toContentValues(),
                AlarmHelper.ALARM_ID + " " + DATA_EQUAL,
                arrayOf(idAlarm.toString())
            )
        }.getOrElse {
            it.toStr.logProvCrash("DBAlarm")
            -1
        }
    }

    suspend fun AlarmSack.updateAlarmSus(): Int = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(-1) {
            return@withBackCurDef runCatching {
                writableDatabase.update(
                    AlarmHelper.TABLE_NAME,
                    toContentValues(),
                    AlarmHelper.ALARM_ID + " " + DATA_EQUAL,
                    arrayOf(idAlarm.toString())
                )
            }.getOrElse {
                it.toStr.logProvCrash("DBAlarm")
                -1
            }
        }
    }

    fun deleteAlarm(alarm: AlarmSack): Int = deleteAlarm1(alarm.idAlarm)

    fun deleteAlarm1(id: Int): Int {
        return runCatching {
            writableDatabase.delete(
                AlarmHelper.TABLE_NAME,
                AlarmHelper.ALARM_ID + " " + DATA_EQUAL,
                arrayOf(id.toString())
            )
        }.getOrElse {
            it.toStr.logProvCrash("DBAlarm")
            -1
        }
    }

    fun getBootAlarms(): MutableList<AlarmSack> = run {
        val s = AlarmHelper.COL_SWITCH + " " + DATA_NOT_EQUAL + " " +
                DATA_AND + " " + AlarmHelper.COL_TIME + " " + DATA_MORE
        return@run runCatching {
            readableDatabase.query(
                AlarmHelper.TABLE_NAME,
                AlarmHelper.allColumns,
                s,
                arrayOf("0", System.currentTimeMillis().toString()),
                null,
                null,
                AlarmHelper.COL_TIME + " " + DATA_ASC
            ).useMy {
                buildAlarmsAsIt()
            }
        }.getOrElse {
            it.toStr.logProvCrash("DBAlarm")
            mutableListOf()
        }
    }

    suspend fun getValidAlarms(): MutableList<AlarmSack> = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(mutableListOf()) {
            return@withBackCurDef runCatching {
                readableDatabase.query(
                    AlarmHelper.TABLE_NAME,
                    AlarmHelper.allColumns,
                    AlarmHelper.COL_IS_ALARM + " " + DATA_NOT_EQUAL,
                    arrayOf("0"),
                    null,
                    null,
                    AlarmHelper.COL_TIME + " " + DATA_ASC
                ).useBack {
                    buildAlarmsAsIt()
                }
            }.getOrElse {
                it.toStr.logProvCrash("DBAlarm")
                mutableListOf()
            }
        }
    }

    suspend fun getAlarmFire(id: Int): AlarmSack? = justCoroutineCur {
        return@justCoroutineCur withBackCurDef(null) {
            return@withBackCurDef runCatching {
                return@runCatching readableDatabase.query(
                    AlarmHelper.TABLE_NAME,
                    AlarmHelper.allColumns,
                    AlarmHelper.ALARM_ID + " " + DATA_EQUAL,
                    arrayOf(id.toString()),
                    null,
                    null,
                    null
                ).useBack {
                    buildAlarmNow()
                }
            }.getOrElse {
                it.toStr.logProvCrash("DBAlarm")
                return@withBackCurDef null
            }
        }
    }

    private fun SQLiteDatabase.dropTableAlarm() {
        execSQL(DATA_DROP + " " + AlarmHelper.TABLE_NAME + ";")
    }

}