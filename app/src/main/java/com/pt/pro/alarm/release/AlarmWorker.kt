package com.pt.pro.alarm.release

import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.stable.newDBAlarm
import com.pt.common.stable.newDBNotification
import com.pt.common.stable.withBackDef

class AlarmWorker(
    context: android.content.Context,
    parameters: androidx.work.WorkerParameters,
) : androidx.work.CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        withBackDef(Unit) {
            runCatching {
                com.pt.pro.notepad.receivers.NoteReceiver.apply {
                    applicationContext.newDBNotification {
                        getDataNotifyFilter()
                    }.forEach {
                        applicationContext.isNoteReminderSet(
                            it.timeNotify,
                            it.idData
                        ).let { i ->
                            if (!i) {
                                applicationContext.setReminderNote(
                                    it.timeNotify,
                                    it.idData
                                )
                            }
                        }
                    }
                }
                AlarmReceiver.apply {
                    if (!applicationContext.isAlarmReminderSet) {
                        applicationContext.newDBAlarm {
                            getBootAlarms()
                        }.forEach {
                            runCatching {
                                applicationContext.setReminderAlarm(
                                    it.timeAlarm,
                                    it.idAlarm
                                )
                            }
                        }
                    }
                }
            }.onFailure {
                it.toStr.logProvCrash("onFailure")
            }
        }
        return Result.success()
    }

}