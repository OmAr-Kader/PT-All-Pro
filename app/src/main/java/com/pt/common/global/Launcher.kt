package com.pt.common.global

import com.pt.common.media.uriProviderNormal
import com.pt.common.stable.newIntent

inline val goToOverlay: android.content.Intent
    @com.pt.common.global.APIAnn(android.os.Build.VERSION_CODES.M)
    @com.pt.common.global.WorkerAnn
    get() {
        return android.content.Intent(
            android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            (com.pt.common.BuildConfig.PACK_AGE + com.pt.pro.BuildConfig.APPLICATION_ID).toUri
        )
    }

internal inline val android.content.Context.widgetAlarm: android.content.Intent
    @com.pt.common.global.WorkerAnn
    get() {
        return android.content.Intent(
            this,
            com.pt.pro.alarm.views.MainActivityAlarm::class.java
        ).apply {
            flags = BACKGROUND_FLAGS
            action = android.content.Intent.ACTION_MAIN
            putExtra(com.pt.common.stable.SHORTCUT, true)
        }
    }

internal inline val android.content.Context.widgetDataKeeper: android.content.Intent
    @com.pt.common.global.WorkerAnn
    get() {
        return android.content.Intent(
            this,
            com.pt.pro.notepad.activities.NoteActivity::class.java
        ).apply {
            flags = BACKGROUND_FLAGS
            action = android.content.Intent.ACTION_MAIN
            putExtra(com.pt.common.stable.SHORTCUT, true)
        }
    }

internal inline val android.content.Context.widgetAllInOne: android.content.Intent
    @com.pt.common.global.WorkerAnn
    get() {
        return android.content.Intent(
            this,
            com.pt.pro.main.former.SplashActivity::class.java
        ).apply {
            flags = BACKGROUND_FLAGS
            action = android.content.Intent.ACTION_MAIN
            addCategory(android.content.Intent.CATEGORY_LAUNCHER)
            putExtra(com.pt.common.stable.SHORTCUT, true)
        }
    }

internal inline val android.content.Context.goToSetting: android.content.Intent
    @com.pt.common.global.WorkerAnn
    get() {
        return if (isV_M) {
            android.content.Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                data = (com.pt.common.BuildConfig.PACK_AGE +
                        com.pt.pro.BuildConfig.APPLICATION_ID
                        ).toUri

                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                        android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
        } else {
            android.content.Intent(this, com.pt.pro.main.former.MainActivity::class.java).apply {
                flags = BACKGROUND_FLAGS
                action = com.pt.common.stable.WRITE_SETTING_ACCESS
            }
        }
    }


@com.pt.common.global.WorkerAnn
fun android.content.Context.stringForWidget(msg: String) {
    runCatching {
        android.content.Intent(
            this,
            com.pt.pro.extra.receivers.BroadcastWidget::class.java
        ).apply {
            putExtra(com.pt.common.stable.STRING_FOR_WIDGET, msg)
            action = com.pt.common.stable.UPDATE_WIDGET_NOTE
        }.run {
            android.app.PendingIntent.getBroadcast(
                this@stringForWidget, 0, this@run,
                com.pt.common.stable.PEND_FLAG
            )
        }.run {

        }
    }
}

internal suspend fun android.content.Context.updateWidget(mode: Int) {
    com.pt.common.stable.withDefault {
        this.runCatching {
            android.content.Intent(
                this@updateWidget,
                com.pt.pro.extra.receivers.BroadcastWidget::class.java
            ).apply {
                this.action = com.pt.common.stable.REFRESH_WID
                this.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                this.putExtra(com.pt.common.stable.EXTRA_WIDGET, mode)
                android.app.PendingIntent.getBroadcast(
                    this@updateWidget, 5, this,
                    com.pt.common.stable.PEND_FLAG
                ).run {
                    this.send()
                }
            }
        }
    }
}

@com.pt.common.global.WorkerAnn
internal inline fun android.content.Context.shareSingleImage(
    temp: com.pt.pro.notepad.models.DataKeeperItem,
    intent: android.content.Intent.() -> Unit,
) {
    temp.imageUrl?.let {
        uriProviderNormal(it, com.pt.pro.BuildConfig.APPLICATION_ID).getImageSend.also(intent)
    }
}

@com.pt.common.global.WorkerAnn
internal inline fun android.content.Context.shareSetImage(
    temp: MutableList<com.pt.pro.notepad.models.DataKeeperItem>,
    intent: android.content.Intent.() -> Unit,
) {
    mutableListOf<android.net.Uri>().apply {
        temp.forEach {
            it.imageUrl?.let { iu ->
                this@apply.add(uriProviderNormal(iu, com.pt.pro.BuildConfig.APPLICATION_ID))
            }
        }
    }.let(::ArrayList).getMultiImageSend(intent)
}

@com.pt.common.global.WorkerAnn
internal inline fun android.content.Context.shareSingleRecord(
    temp: com.pt.pro.notepad.models.DataKeeperItem,
    intent: android.content.Intent.() -> Unit,
) {
    temp.recordPath?.let {
        uriProviderNormal(it, com.pt.pro.BuildConfig.APPLICATION_ID).getAudioSend.also(intent)
    }
}

@com.pt.common.global.WorkerAnn
internal inline fun android.content.Context.shareSetRecord(
    temp: MutableList<com.pt.pro.notepad.models.DataKeeperItem>,
    intent: android.content.Intent.() -> Unit,
) {
    mutableListOf<android.net.Uri>().apply {
        temp.forEach {
            it.recordPath?.let { rp ->
                this@apply.add(uriProviderNormal(rp, com.pt.pro.BuildConfig.APPLICATION_ID))
            }
        }
    }.let(::ArrayList).getMultiAudioSend(intent)
}

@com.pt.common.global.WorkerAnn
fun doSendMail(e: DSack<String?, String?, String?>, a: android.content.Intent.() -> Unit) {
    android.content.Intent.createChooser(e.getEmailToSend, "SendEmail").also(a)
}
