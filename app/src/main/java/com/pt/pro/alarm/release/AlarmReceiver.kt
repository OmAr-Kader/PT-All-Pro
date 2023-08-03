package com.pt.pro.alarm.release

import com.pt.common.global.fetchSystemService
import com.pt.common.stable.newIntent
import com.pt.pro.alarm.release.AlarmReceiver.ScheduleAlarm.Companion.scheduleCheck
import com.pt.pro.alarm.release.AlarmReceiver.ScheduleAlarm.Companion.scheduleM

@com.pt.common.global.WorkerAnn
open class AlarmReceiver : android.content.BroadcastReceiver() {

    private var mWakeLock: android.os.PowerManager.WakeLock? = null

    companion object {
        private const val ALARM_ACTION = "actionAlarm"
        private const val ALARM_CATO = "catoAlarm"
        private const val TIME_EXTRA = "timeExtra"

        inline val android.content.Context.isAlarmReminderSet: Boolean
            get() {
                return applicationContext.scheduleCheck {
                    check() != null
                }
            }

        internal suspend inline fun android.content.Context.setReminderAlarm(
            time: Long, id: Int, crossinline f: suspend () -> Unit
        ) {
            com.pt.common.stable.withBack {
                android.content.Intent(applicationContext, AlarmReceiver::class.java).apply {
                    action = ALARM_ACTION
                    addCategory(ALARM_CATO)
                    putExtra(TIME_EXTRA, id)
                }.also {
                    android.app.PendingIntent.getBroadcast(
                        applicationContext, id, it, com.pt.common.stable.PEND_FLAG
                    ).also {
                        applicationContext.scheduleM {
                            it.schedule(time)
                        }
                    }
                }
            }
            com.pt.common.stable.withBack {
                f.invoke()
            }
        }

        fun android.content.Context.setReminderAlarm(time: Long, id: Int) {
            android.content.Intent(applicationContext, AlarmReceiver::class.java).apply {
                action = ALARM_ACTION
                addCategory(ALARM_CATO)
                putExtra(TIME_EXTRA, id)
                android.app.PendingIntent.getBroadcast(
                    applicationContext, id, this, com.pt.common.stable.PEND_FLAG
                ).also {
                    applicationContext.scheduleM {
                        it.schedule(time)
                    }
                }
            }
        }

        fun android.content.Context.cancelReminderAlarm(alarm: com.pt.common.global.AlarmSack) {
            android.content.Intent(applicationContext, AlarmReceiver::class.java).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = ALARM_ACTION
                addCategory(ALARM_CATO)

                android.app.PendingIntent.getBroadcast(
                    applicationContext, alarm.idAlarm, this, com.pt.common.stable.PEND_FLAG
                ).also {
                    applicationContext.scheduleM {
                        it.cancelAlarm()
                    }
                }
            }
        }

        suspend fun android.content.Context.cancelReminderAlarmSus(
            alarm: com.pt.common.global.AlarmSack
        ): Unit = com.pt.common.stable.justCoroutineBack {
            android.content.Intent(applicationContext, AlarmReceiver::class.java).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = ALARM_ACTION
                addCategory(ALARM_CATO)

                android.app.PendingIntent.getBroadcast(
                    applicationContext, alarm.idAlarm, this, com.pt.common.stable.PEND_FLAG
                ).also {
                    applicationContext.scheduleM {
                        it.cancelAlarm()
                    }
                }
            }
        }
    }

    override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
        com.pt.common.stable.ensureBack {
            (context ?: return@ensureBack).turnOnSCR()
            if (ALARM_ACTION == intent?.action) {
                com.pt.common.stable.catchyUnit {
                    context.applicationContext.newIntent(AlarmServiceNotification::class.java) {
                        putExtra(com.pt.common.stable.SERVICE_TIME, intent.getIntExtra(TIME_EXTRA, 0))
                        flags = com.pt.common.global.BACKGROUND_FLAGS
                        action = android.content.Intent.ACTION_MAIN
                        this@newIntent
                    }.also {
                        androidx.core.content.ContextCompat.startForegroundService(
                            context.applicationContext, it
                        )
                    }
                    turnOffSCR()
                }
            }
        }
    }


    private fun android.content.Context.turnOnSCR() {
        fetchSystemService(com.pt.common.global.powerService)?.also {
            if (com.pt.common.global.isV_T) {
                (805306394 or android.os.PowerManager.ON_AFTER_RELEASE)
            } else {
                @Suppress("DEPRECATION") (805306394 or android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP or android.os.PowerManager.ON_AFTER_RELEASE)
            }.also { flags ->
                mWakeLock = it.newWakeLock(
                    flags, com.pt.pro.BuildConfig.APPLICATION_ID + ":full_wake_lock_alarm"
                ).apply {
                    setReferenceCounted(false)
                    acquire(60 * 1000L)
                }
            }
        }
    }

    private fun turnOffSCR() {
        if (mWakeLock?.isHeld == true) {
            mWakeLock?.release()
            mWakeLock = null
        }
    }

    @com.pt.common.global.WorkerAnn
    open class ScheduleAlarm(private val am: android.app.AlarmManager) {
        companion object {

            inline fun android.content.Context.scheduleM(schedule: ScheduleAlarm.() -> Unit) {
                fetchSystemService(com.pt.common.global.alarmService)?.let {
                    ScheduleAlarm(it).apply(schedule)
                }
            }

            inline fun android.content.Context.scheduleCheck(
                schedule: ScheduleAlarm.() -> Boolean
            ): Boolean {
                return fetchSystemService(com.pt.common.global.alarmService)?.let {
                    schedule(ScheduleAlarm(it))
                } ?: false
            }
        }

        fun android.app.PendingIntent.cancelAlarm() {
            am.cancel(this)
        }

        fun android.app.PendingIntent.schedule(alarmTime: Long) {
            am.setAlarmClock(android.app.AlarmManager.AlarmClockInfo(alarmTime, this), this)
        }

        fun check(): android.app.PendingIntent? = runCatching {
            return@runCatching am.nextAlarmClock?.showIntent
        }.getOrNull()
    }

}