package com.pt.common.global

inline val FLAGS: Int
    @com.pt.common.global.WorkerAnn
    get() {
        return android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP or
                android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

inline val homeIntent: android.content.Intent
    @com.pt.common.global.WorkerAnn
    get() {
        return android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
            addCategory(android.content.Intent.CATEGORY_HOME)
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
        }
    }

inline val appIntent: android.content.Intent
    get() {
        return android.content.Intent(
            android.content.Intent.ACTION_VIEW,
            com.pt.common.BuildConfig.APP_LINK.toUri,
        )
    }

inline val rateIntent: android.content.Intent
    get() {
        return android.content.Intent(
            android.content.Intent.ACTION_VIEW,
            com.pt.common.BuildConfig.APP_LINK_MARKET.toUri
        )
    }

inline val LAUNCH_CATO: String
    @com.pt.common.global.WorkerAnn
    get() = android.content.Intent.CATEGORY_LAUNCHER

inline val BACKGROUND_FLAGS: Int
    @com.pt.common.global.WorkerAnn
    get() {
        return android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP or
                android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION or
                android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or
                android.content.Intent.FLAG_ACTIVITY_TASK_ON_HOME or
                android.content.Intent.FLAG_FROM_BACKGROUND or
                android.content.Intent.FLAG_RECEIVER_FOREGROUND
    }

inline val android.net.Uri.goToPhotoView: android.content.Intent
    get() {
        return android.content.Intent().apply {
            action = android.content.Intent.ACTION_VIEW
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            clipData = android.content.ClipData(
                "View",
                arrayOf(com.pt.common.BuildConfig.IMAGE_SENDER),
                android.content.ClipData.Item(this@goToPhotoView)
            )
            setDataAndType(this@goToPhotoView, com.pt.common.BuildConfig.IMAGE_SENDER)
        }
    }

inline val android.content.Context.fetchSystemWindowManager: android.view.WindowManager
    get() = androidx.core.content.ContextCompat.getSystemService(
        this@fetchSystemWindowManager,
        android.view.WindowManager::class.java
    ) ?: getSystemService(android.content.Context.WINDOW_SERVICE) as android.view.WindowManager

inline val powerService: DSackT<Class<android.os.PowerManager>, String>
    get() = DSackT(android.os.PowerManager::class.java, android.content.Context.POWER_SERVICE)

inline val audioService: DSackT<Class<android.media.AudioManager>, String>
    get() = DSackT(android.media.AudioManager::class.java, android.content.Context.AUDIO_SERVICE)

inline val clipService: DSackT<Class<android.content.ClipboardManager>, String>
    get() = DSackT(android.content.ClipboardManager::class.java, android.content.Context.CLIPBOARD_SERVICE)

inline val keyguardService: DSackT<Class<android.app.KeyguardManager>, String>
    get() = DSackT(android.app.KeyguardManager::class.java, android.content.Context.KEYGUARD_SERVICE)

inline val alarmService: DSackT<Class<android.app.AlarmManager>, String>
    get() = DSackT(android.app.AlarmManager::class.java, android.content.Context.ALARM_SERVICE)

inline val windowService: DSackT<Class<android.view.WindowManager>, String>
    get() = DSackT(android.view.WindowManager::class.java, android.content.Context.WINDOW_SERVICE)

inline val activityService: DSackT<Class<android.app.ActivityManager>, String>
    get() = DSackT(android.app.ActivityManager::class.java, android.content.Context.ACTIVITY_SERVICE)

inline val notificationService: DSackT<Class<android.app.NotificationManager>, String>
    get() = DSackT(android.app.NotificationManager::class.java, android.content.Context.NOTIFICATION_SERVICE)

inline val sensorService: DSackT<Class<android.hardware.SensorManager>, String>
    get() = DSackT(android.hardware.SensorManager::class.java, android.content.Context.SENSOR_SERVICE)

inline val mediaProjectionService: DSackT<Class<android.media.projection.MediaProjectionManager>, String>
    get() = DSackT(android.media.projection.MediaProjectionManager::class.java, android.content.Context.MEDIA_PROJECTION_SERVICE)

@get:androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.S)
inline val vibratorManagerService: DSackT<Class<android.os.VibratorManager>, String>
    get() = DSackT(android.os.VibratorManager::class.java, android.content.Context.VIBRATOR_MANAGER_SERVICE)

inline val vibratorService: DSackT<Class<android.os.Vibrator>, String>
    @Suppress("DEPRECATION")
    get() = DSackT(android.os.Vibrator::class.java, android.content.Context.VIBRATOR_SERVICE)

inline val uiModeService: DSackT<Class<android.app.UiModeManager>, String>
    @Suppress("DEPRECATION")
    get() = DSackT(android.app.UiModeManager::class.java, android.content.Context.UI_MODE_SERVICE)

inline val inflaterService: DSackT<Class<android.view.LayoutInflater>, String>
    get() = DSackT(android.view.LayoutInflater::class.java, android.content.Context.LAYOUT_INFLATER_SERVICE)

inline fun <reified T> android.content.Context.fetchSystemService(
    a: DSackT<Class<T>, String>
): T? = androidx.core.content.ContextCompat.getSystemService(
    this@fetchSystemService,
    a.one
) ?: getSystemService(a.two) as? T?

@android.annotation.SuppressLint("WrongConstant")
fun android.content.Context.expendStatusBar(expand: Boolean): Boolean {
    return try {
        com.pt.common.stable.catchy(false) {
            getSystemService(com.pt.common.BuildConfig.STATUS_BAR).apply {
                val methodName = if (expand)
                    com.pt.common.BuildConfig.STATUS_EXPAND
                else
                    com.pt.common.BuildConfig.STATUS_COLLAPSE

                Class.forName(com.pt.common.BuildConfig.STATUS_MANAGER).also {
                    it.getMethod(methodName).invoke(this@apply)
                }
            }
            return true
        }
    } catch (e: Exception) {
        false
    }
}

inline val goToDisturb: android.content.Intent
    @com.pt.common.global.APIAnn(android.os.Build.VERSION_CODES.M)
    @com.pt.common.global.WorkerAnn
    get() {
        return android.content.Intent(
            android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
        )
    }

@com.pt.common.global.WorkerAnn
inline fun homeLauncher(
    @com.pt.common.global.WorkerAnn crossinline intent: android.content.Intent.() -> Unit,
) {
    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
        com.pt.common.stable.catchy(Unit) {
            android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
                addCategory(android.content.Intent.CATEGORY_HOME)
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }.also(intent)
        }
    }, 500L)
}

inline fun launchSender(
    text: String,
    packageApp: String?,
    activity: String,
    intent: android.content.Intent.() -> Unit,
) {
    android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        type = com.pt.common.BuildConfig.TEXT_SENDER
        putExtra(android.content.Intent.EXTRA_TEXT, text)
        if (packageApp != null) {
            setPackage(packageApp)
            component = android.content.ComponentName(packageApp, activity)
        }
    }.also(intent)
}

fun shareTextProvider(
    text: String,
    int: android.content.Intent.() -> Unit
) {
    android.content.Intent().apply {
        action = android.content.Intent.ACTION_SEND
        type = com.pt.common.BuildConfig.TEXT_SENDER
        putExtra(android.content.Intent.EXTRA_TEXT, text)
    }.apply {
        android.content.Intent.createChooser(this@apply, "Share").let(int)
    }
}

@com.pt.common.global.WorkerAnn
inline fun android.content.Context.findPicker(
    pac: String,
    intent: android.content.Intent.() -> Unit
) {
    android.content.Intent(android.content.Intent.ACTION_PICK).apply {
        type = com.pt.common.BuildConfig.IMAGE_SENDER
        flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
    }.let { itA ->
        android.content.Intent.createChooser(itA, "SelectImage").apply {
            flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE).apply {
                action = android.provider.MediaStore.ACTION_IMAGE_CAPTURE
                flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                putExtra(
                    android.provider.MediaStore.EXTRA_OUTPUT,
                    getCaptureImageOutputUri(pac)
                )
            }.let { itB ->
                putExtra(android.content.Intent.EXTRA_INITIAL_INTENTS, arrayOf(itB))
            }
        }.also(intent)
    }
}

@Suppress("SpellCheckingInspection")
@com.pt.common.global.WorkerAnn
inline fun translateProvider(
    txtBefore: String, type: Int, intent: android.content.Intent.() -> Unit,
) {
    txtBefore.toWebString.let { txt ->
        return@let when (type) {
            com.pt.common.stable.SEARCH_GOOGLE -> {
                "http://translate.google.com/?q=$txt"
            }
            com.pt.common.stable.SEARCH_YANDEX -> {
                "https://translate.yandex.com/?text=$txt"
            }
            com.pt.common.stable.SEARCH_DUCK -> {
                "http://translate.google.com/?q=$txt"
            }
            com.pt.common.stable.SEARCH_BAIDU -> {
                "https://fanyi.baidu.com/#auto/#auto/$txt"
            }
            com.pt.common.stable.SEARCH_BING -> {
                "http://translate.google.com/?q=$txt"
            }
            com.pt.common.stable.SEARCH_AOL -> {
                "http://translate.google.com/?q=$txt"
            }
            else -> {
                "http://translate.google.com/?q=$txt"
            }
        }
    }.let {
        android.content.Intent(
            android.content.Intent.ACTION_VIEW,
            it.toUri,
        ).also(intent)
    }
}

inline val DSackT<String, Int>.fetchWebSearch: String
    @Suppress("SpellCheckingInspection")
    @com.pt.common.global.WorkerAnn
    get() {
        return one.toWebString.let { txt ->
            return@let when (two) {
                com.pt.common.stable.SEARCH_GOOGLE -> {
                    "https://www.google.com/search?q=$txt"
                }
                com.pt.common.stable.SEARCH_YANDEX -> {
                    "https://yandex.com/search?text=$txt"
                }
                com.pt.common.stable.SEARCH_DUCK -> {
                    "https://duckduckgo.com/?q=$txt"
                }
                com.pt.common.stable.SEARCH_BAIDU -> {
                    "https://www.baidu.com/s?ie=utf-8&wd$txt"
                }
                com.pt.common.stable.SEARCH_BING -> {
                    "https://www.bing.com/search?q=$txt"
                }
                com.pt.common.stable.SEARCH_AOL -> {
                    @Suppress("LongLine")
                    "https://search.aol.com/aol/search;_ylt=AwrJ6ycgCElhcmgAEBBpCWVH;_ylu=Y29sbwNiZjEEcG9zAzEEdnRpZAMEc2VjA3BpdnM-?q=$txt"
                }
                else -> {
                    "https://www.google.com/search?q=$txt"
                }
            }
        }
    }

@com.pt.common.global.WorkerAnn
inline fun searchProvider(
    txtBefore: String,
    type: Int,
    intent: android.content.Intent.() -> Unit,
) {
    DSackT(txtBefore, type).fetchWebSearch.let {
        android.content.Intent(
            android.content.Intent.ACTION_VIEW,
            it.toUri,
        ).also(intent)
    }
}

inline val String.toWebString: String
    @com.pt.common.global.WorkerAnn
    get() {
        return replace(
            " ",
            "+"
        ).replace(
            "&",
            com.pt.common.BuildConfig.NORMAL_AND
        ).let {
            java.net.URLDecoder.decode(it, com.pt.common.BuildConfig.WEB_UT) ?: it
        }
    }

@Suppress("SpellCheckingInspection")
@com.pt.common.global.WorkerAnn
inline fun searchProviderImgHd(
    txtBefore: String, type: Int, intent: android.content.Intent.() -> Unit,
) {
    txtBefore.toWebString.let { txt ->
        return@let when (type) {
            com.pt.common.stable.SEARCH_GOOGLE -> {
                "https://www.google.com/search?q=$txt&tbm=isch&tbs=isz:l"
            }
            com.pt.common.stable.SEARCH_YANDEX -> {
                "https://yandex.com/images/search?text=$txt"
            }
            com.pt.common.stable.SEARCH_DUCK -> {
                "https://duckduckgo.com/?q=$txt&iax=images&ia=images"
            }
            com.pt.common.stable.SEARCH_BAIDU -> {
                "https://image.baidu.com/search/index?tn=baiduimage&wd=$txt"
            }
            com.pt.common.stable.SEARCH_BING -> {
                "https://www.bing.com/images/search?q=$txt"
            }
            com.pt.common.stable.SEARCH_AOL -> {
                "https://search.aol.com/aol/image;_ylt=AwrJ6ycgCElhcmgAEBBpCWVH;_ylu=Y29sbwNiZjEEcG9zAzEEdnRpZAMEc2VjA3BpdnM-?q=$txt"
            }
            else -> {
                "https://www.google.com/search?q=$txt&tbm=isch&tbs=isz:l"
            }
        }
    }.let {
        android.content.Intent(
            android.content.Intent.ACTION_VIEW,
            it.toUri,
        ).also(intent)
    }
}

@com.pt.common.global.WorkerAnn
inline fun phoneProvider(txt: String, intent: android.content.Intent.() -> Unit) {
    android.content.Intent(
        android.content.Intent.ACTION_VIEW,
        (com.pt.common.BuildConfig.TEL_TO + " " + txt).toUri
    ).also(intent)
}

@com.pt.common.global.WorkerAnn
inline fun mailProvider(txt: String, intent: android.content.Intent.() -> Unit) {
    android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
        data = (com.pt.common.BuildConfig.MAIL_TO + txt).toUri
    }.also(intent)
}

@com.pt.common.global.WorkerAnn
inline fun webViewProvider(txt: String, intent: android.content.Intent.() -> Unit) {
    android.content.Intent(
        android.content.Intent.ACTION_VIEW,
        txt.toUri
    ).also(intent)
}

inline val String.txtCorrecter: String
    get() = replace("\n", "").replace(" ", "")

inline val String.linkCorrecter: String
    get() {
        return if (this@linkCorrecter.endsWith("/")) {
            StringBuffer(this@linkCorrecter).deleteCharAt(this@linkCorrecter.length - 1).let { sb ->
                sb.toStr
            }
        } else {
            this@linkCorrecter
        }.let { txt ->
            java.net.URLEncoder.encode(
                txt,
                com.pt.common.BuildConfig.WEB_UT
            ).replace("+", "%20")
        }
    }

@com.pt.common.global.WorkerAnn
inline fun webBrowserProvider(txt: String, intent: android.content.Intent.() -> Unit) {
    android.content.Intent(
        android.content.Intent.ACTION_VIEW,
        txt.linkCorrecter.toUri
    ).also(intent)
}

fun android.content.Context.sendToClipDate(copyText: String) {
    fetchSystemService(
        clipService
    ).apply {
        android.content.ClipData.newPlainText("ClipBoard", copyText).run {
            this@apply?.setPrimaryClip(this@run)
        }
    }
}


inline val DSack<String?, String?, String?>.getEmailToSend: android.content.Intent
    get() = android.content.Intent(android.content.Intent.ACTION_SENDTO).apply {
        type = "message/rfc822"
        (com.pt.common.BuildConfig.MAIL_TO + (one ?: "") +
                com.pt.common.BuildConfig.MAIL_SUB +
                android.net.Uri.encode(two ?: "") +
                com.pt.common.BuildConfig.MAIL_BODY +
                android.net.Uri.encode(three)).let {
            data = android.net.Uri.parse(it)
        }
        putExtra(android.content.Intent.EXTRA_EMAIL, arrayOf(one ?: ""))
        putExtra(android.content.Intent.EXTRA_SUBJECT, (two ?: ""))
        putExtra(android.content.Intent.EXTRA_TEXT, (three ?: ""))
    }.let {
        android.content.Intent.createChooser(it, "Send Email")
    }

inline fun ArrayList<android.net.Uri>.getMultiAudioSend(a: android.content.Intent.() -> Unit) {
    android.content.ClipData(
        "Share",
        arrayOf(com.pt.common.BuildConfig.AUDIO_SENDER),
        android.content.ClipData.Item(this@getMultiAudioSend.firstOrNull())
    ).apply { this@getMultiAudioSend.forEach { addItem(android.content.ClipData.Item(it)) } }.let { cl ->
        android.content.Intent(android.content.Intent.ACTION_SEND_MULTIPLE).apply {
            flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            action = android.content.Intent.ACTION_SEND_MULTIPLE
            type = com.pt.common.BuildConfig.AUDIO_SENDER
            putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, this@getMultiAudioSend)
            clipData = cl
        }.let {
            android.content.Intent.createChooser(it, "share")
        }.also(a)
    }
}

inline val android.net.Uri.getAudioSend: android.content.Intent
    get() = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        action = android.content.Intent.ACTION_SEND
        type = com.pt.common.BuildConfig.AUDIO_SENDER
        putExtra(
            android.content.Intent.EXTRA_STREAM,
            this@getAudioSend
        )
        clipData = android.content.ClipData(
            "Share",
            arrayOf(com.pt.common.BuildConfig.AUDIO_SENDER),
            android.content.ClipData.Item(this@getAudioSend)
        )
    }.let {
        android.content.Intent.createChooser(it, "share")
    }

inline fun ArrayList<android.net.Uri>.getMultiImageSend(a: android.content.Intent.() -> Unit) {
    android.content.ClipData(
        "Share",
        arrayOf(com.pt.common.BuildConfig.IMAGE_SENDER),
        android.content.ClipData.Item(this@getMultiImageSend.firstOrNull())
    ).apply { this@getMultiImageSend.forEach { addItem(android.content.ClipData.Item(it)) } }.let { cl ->
        android.content.Intent(android.content.Intent.ACTION_SEND_MULTIPLE).apply {
            action = android.content.Intent.ACTION_SEND_MULTIPLE
            flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            type = com.pt.common.BuildConfig.IMAGE_SENDER
            putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, this@getMultiImageSend)
            clipData = cl
        }.let {
            android.content.Intent.createChooser(it, "share")
        }
    }.also(a)
}

inline val android.net.Uri.getImageSend: android.content.Intent
    get() = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
        flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        action = android.content.Intent.ACTION_SEND
        type = com.pt.common.BuildConfig.IMAGE_SENDER
        putExtra(
            android.content.Intent.EXTRA_STREAM,
            this@getImageSend
        )
        clipData = android.content.ClipData(
            "Share",
            arrayOf(com.pt.common.BuildConfig.IMAGE_SENDER),
            android.content.ClipData.Item(this@getImageSend)
        )
    }.let {
        android.content.Intent.createChooser(it, "share")
    }

inline val launchCamera: android.content.Intent
    @com.pt.common.global.WorkerAnn
    get() {
        return android.content.Intent(
            android.provider.MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA
        ).apply {
            action = android.provider.MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA
        }
    }
