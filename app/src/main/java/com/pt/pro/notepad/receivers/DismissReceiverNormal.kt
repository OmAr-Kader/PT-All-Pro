package com.pt.pro.notepad.receivers

open class DismissReceiverNormal : android.content.BroadcastReceiver() {

    override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
        com.pt.common.stable.ensureBack {
            com.pt.common.stable.catchyUnit {
                val notificationId = (intent ?: return@catchyUnit).getIntExtra(com.pt.common.stable.INTENT_ID_CLOSE, 3)
                if (context != null) {
                    androidx.core.app.NotificationManagerCompat.from(context.applicationContext ?: return@catchyUnit).apply {
                        cancel(notificationId)
                    }
                }
            }
        }
    }

}