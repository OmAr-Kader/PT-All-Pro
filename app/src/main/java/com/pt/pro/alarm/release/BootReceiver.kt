package com.pt.pro.alarm.release

class BootReceiver : android.content.BroadcastReceiver() {

    @Suppress("SpellCheckingInspection")
    private inline val actions: Array<String>
        get() = arrayOf(
            "android.intent.action.BOOT_COMPLETED",
            "android.intent.action.LOCKED_BOOT_COMPLETED",
            "android.intent.action.QUICKBOOT_POWERON",
            "android.intent.action.REBOOT",
            "com.htc.intent.action.QUICKBOOT_POWERON",
            "android.intent.action.ACTION_SHUTDOWN"
        )

    override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
        com.pt.common.stable.ensureBack {
            if (null != intent?.action && actions.contains(intent.action)) {
                com.pt.common.stable.catchyUnit {
                    com.pt.pro.main.odd.WorkInitializer().create(
                        context?.applicationContext ?: return@ensureBack
                    ).enqueueUniqueWork(
                        "AlarmWorker",
                        androidx.work.ExistingWorkPolicy.REPLACE,
                        androidx.work.OneTimeWorkRequest.Builder(
                            AlarmWorker::class.java
                        ).build(),
                    )
                }
            }
        }
    }
}