package com.pt.pro.notepad.receivers

import android.os.Bundle
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pt.common.global.fetchColor
import com.pt.common.global.fetchSystemService
import com.pt.common.stable.PEND_FLAG
import com.pt.common.stable.newDBNotification
import com.pt.pro.R
import com.pt.pro.notepad.activities.NoteActivity
import com.pt.pro.notepad.models.DataNotification
import com.pt.pro.notepad.objects.INTENT_ID
import com.pt.pro.notepad.objects.NOT_ALARM_KEY
import com.pt.pro.notepad.objects.NOT_BUNDLE_EXTRA
import com.pt.pro.notepad.receivers.NoteReceiver.ScheduleNote.Companion.scheduleNote

class NoteReceiver : android.content.BroadcastReceiver() {

    companion object {
        private const val NOTE_ACTION = "actionNote"
        private const val NOTE_CATO = "catoNote"
        private const val TIME_EXTRA = "timeExtra"

        private const val CH_DAT = "dataChannel"

        fun android.content.Context.setReminderNote(time: Long, id: Int) {
            android.content.Intent(applicationContext, NoteReceiver::class.java).run {
                action = NOTE_ACTION
                addCategory(NOTE_CATO)
                putExtra(TIME_EXTRA, time)
                android.app.PendingIntent.getBroadcast(
                    applicationContext, id, this,
                    PEND_FLAG
                ).run {
                    applicationContext.scheduleNote {
                        scheduleNote(time)
                    }
                }
            }
        }


        fun android.content.Context.isNoteReminderSet(time: Long, id: Int): Boolean {
            return android.content.Intent(applicationContext, NoteReceiver::class.java).run {
                action = NOTE_ACTION
                flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(TIME_EXTRA, time)
                return@run android.app.PendingIntent.getBroadcast(
                    applicationContext, id, this,
                    PEND_FLAG
                )
            }.run {
                this != null
            }
        }

        fun android.content.Context.cancelReminderNote(note: Int) {
            android.content.Intent(applicationContext, NoteReceiver::class.java).run {
                flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = NOTE_ACTION
                android.app.PendingIntent.getBroadcast(
                    applicationContext, note, this,
                    PEND_FLAG
                ).run {
                    applicationContext.scheduleNote {
                        cancelNote()
                    }
                }
            }
        }
    }

    override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
        com.pt.common.stable.ensureBack {
            if (context == null) return@ensureBack
            if (NOTE_ACTION == intent?.action) {
                com.pt.common.stable.catchyUnit {
                    val time: Long = intent.getLongExtra(TIME_EXTRA, -1L)
                    val data = context.applicationContext.newDBNotification {
                        getOneData(time)
                    }
                    data?.let { context.applicationContext.normalNotification(it) }
                }
            }
        }
    }


    @android.annotation.SuppressLint("MissingPermission")
    private fun android.content.Context.normalNotification(data: DataNotification) {
        val id = data.idData
        val intent = android.content.Intent(this, NoteActivity::class.java).apply {
            Bundle().run {
                putParcelable(NOT_ALARM_KEY, data)
                putExtra(NOT_BUNDLE_EXTRA, this@run)
            }
            putExtra(INTENT_ID, id)
            addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val pendingIntent = android.app.PendingIntent.getActivity(this, id, intent, PEND_FLAG)

        val manager = NotificationManagerCompat.from(this)

        manager.createNormalNotificationChannel(resources.getString(R.string.hc))

        NotificationCompat.Builder(this, CH_DAT).apply {
            setContentTitle(resources.getString(R.string.zd))
            setContentText(data.dataText)
            setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(data.dataText)
            )
            setTicker(data.dataText)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            color = fetchColor(R.color.cac)
            setVibrate(longArrayOf(1000, 500, 1000, 500))
            setContentIntent(pendingIntent)
            android.content.Intent(this@normalNotification, DismissReceiverNormal::class.java).apply {
                putExtra(com.pt.common.stable.INTENT_ID_CLOSE, id)
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                action = "actionNoteDismiss"
                addCategory("catoNoteDismiss")
            }.run {
                android.app.PendingIntent.getBroadcast(this@normalNotification, id - 1, this, PEND_FLAG)
            }.run {
                NotificationCompat.Action.Builder(
                    R.drawable.ic_close,
                    resources.getString(R.string.co),
                    this@run
                )
            }.let {
                addAction(it.build())
            }
            setAutoCancel(false)
            setOnlyAlertOnce(true)
            setOngoing(false).priority = NotificationCompat.PRIORITY_HIGH
        }.build().apply {
            flags = NotificationCompat.FLAG_ONLY_ALERT_ONCE
            manager.notify(id, this)
        }
    }

    private fun NotificationManagerCompat.createNormalNotificationChannel(str: String) {
        NotificationChannelCompat.Builder(CH_DAT, NotificationManagerCompat.IMPORTANCE_HIGH).apply {
            setName(str)
            setVibrationEnabled(true)
            setVibrationPattern(longArrayOf(1000, 500, 1000, 500))
        }.let {
            createNotificationChannel(it.build())
        }
    }

    open class ScheduleNote(private val am: android.app.AlarmManager) {
        companion object {

            inline fun android.content.Context.scheduleNote(schedule: ScheduleNote.() -> Unit) {
                fetchSystemService(com.pt.common.global.alarmService).run {
                    ScheduleNote(this!!).apply(schedule)
                }
            }
        }

        fun android.app.PendingIntent.cancelNote() {
            am.cancel(this)
        }

        fun android.app.PendingIntent.scheduleNote(alarmTime: Long) {
            androidx.core.app.AlarmManagerCompat.setExactAndAllowWhileIdle(
                am,
                android.app.AlarmManager.RTC_WAKEUP,
                alarmTime,
                this
            )
        }
    }
}