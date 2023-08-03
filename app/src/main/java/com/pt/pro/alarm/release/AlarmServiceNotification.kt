package com.pt.pro.alarm.release

import com.pt.common.global.*
import com.pt.common.stable.*

class AlarmServiceNotification : ParentAlarmService() {

    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var lastJob: kotlinx.coroutines.Job? = null
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    private val dispatcher: androidx.lifecycle.ServiceLifecycleDispatcher = androidx.lifecycle.ServiceLifecycleDispatcher(this)

    override val lifecycle: androidx.lifecycle.Lifecycle
        get() = dispatcher.lifecycle

    override fun onBind(intent: android.content.Intent): android.os.IBinder? {
        dispatcher.onServicePreSuperOnBind()
        return null
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onStart(intent: android.content.Intent?, startId: Int) {
        dispatcher.onServicePreSuperOnStart()
        super.onStart(intent, startId)
    }

    override fun onCreate() {
        dispatcher.onServicePreSuperOnCreate()
        super.onCreate()

        channelPro()
        channelProForPop()

        androidx.core.app.NotificationCompat.Builder(this, CH_ALA).apply {
            setSmallIcon(com.pt.pro.R.drawable.ic_launcher_foreground)
            color = fetchColor(com.pt.pro.R.color.cac)
            newIntent(AlarmServiceNotification::class.java) {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                action = ACTION_DISMISS_ALARM
                this@newIntent
            }.let {
                android.app.PendingIntent.getService(this@AlarmServiceNotification, 35, it, PEND_FLAG)
            }.let {
                setContentIntent(it)
                androidx.core.app.NotificationCompat.Action.Builder(
                    com.pt.pro.R.drawable.ic_close,
                    resources.getString(com.pt.pro.R.string.s6), it
                ).build().let { itA ->
                    this@apply.addAction(itA)
                }
            }
            setContentTitle(resources.getString(com.pt.pro.R.string.tg))
            setContentText(resources.getString(com.pt.pro.R.string.fs))
            setAutoCancel(false)
            setOnlyAlertOnce(false)
            setOngoing(true)
            setCategory(androidx.core.app.NotificationCompat.CATEGORY_SERVICE)
            setDefaults(androidx.core.app.NotificationCompat.DEFAULT_ALL)
            priority = androidx.core.app.NotificationCompat.PRIORITY_HIGH or
                    androidx.core.app.NotificationCompat.PRIORITY_MAX
            foregroundServiceBehavior =
                androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
        }.build().apply {
            flags = androidx.core.app.NotificationCompat.FLAG_FOREGROUND_SERVICE
            if (isV_Q) {
                startForeground(
                    SERVICE_ALARM,
                    this,
                    android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(SERVICE_ALARM, this)
            }
        }
        qHand = fetchHandler
        lifecycle.addObserver(this@AlarmServiceNotification)
    }


    override fun onStartCommand(intent: android.content.Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            val idAlarm: Int = it.getIntExtra(SERVICE_TIME, -1)
            if (it.action == ACTION_DISMISS_ALARM) {
                forAlarmRecordPlay()
            } else if (it.action == ALARM_RECORD_FOR_DISMISS) {
                forAlarmRecordDismiss(intent)
            } else if (idAlarm != -1) {
                alarmProviderService(idAlarm)
            }
            Unit
        }
        return START_STICKY
    }

    private fun alarmProviderService(idAlarm: Int) {
        launchDef {
            newDBAlarm {
                getAlarmFire(idAlarm)
            }.letSusBack {
                withDefaultDef(true) {
                    turnOnSCR(findIntegerPreference(INTER_T, 10))
                    fetchSystemService(keyguardService).runSusBack {
                        return@runSusBack this?.isKeyguardLocked ?: false
                    }.letSusBack { keyguard ->
                        fetchSystemService(powerService).runSusBack {
                            return@runSusBack this?.isInteractive ?: false
                        }.letSusBack { power ->
                            keyguard || !power
                        }
                    }
                }.letSusBack { boolean ->
                    withDefault {
                        AlarmReceiver.applySusBack {
                            it?.let { it1 -> cancelReminderAlarmSus(it1) }
                        }
                    }
                    kotlinx.coroutines.delay(100L)
                    withDefault {
                        if (mediaPlayer != null) {
                            it?.updateAlarm { }
                        } else {
                            it.doAlarmRing(boolean, idAlarm)
                        }
                    }
                }
            }
        }
    }

    private suspend fun AlarmSack?.doAlarmRing(boolean: Boolean, idAlarm: Int) {
        withDefault {
            alarm = this@doAlarmRing
            if (boolean) {
                android.content.Intent(applicationContext, com.pt.pro.alarm.decline.AlarmFiredActivity::class.java).apply {
                    this.flags = BACKGROUND_FLAGS
                    this.action = android.content.Intent.ACTION_MAIN
                    this.addCategory(android.content.Intent.CATEGORY_LAUNCHER)
                    this.putExtra(TIME_FIRED, idAlarm)
                    this@AlarmServiceNotification.startActivity(this)
                }
                if (this@doAlarmRing?.isVibrate == true) {
                    if (isV_S) {
                        vibrateProv31()
                    } else {
                        vibrateProv()
                    }
                }
            }
        }
        withDefault {
            fetchMediaContext.alsoSusBack { ctxMedia ->
                justCoroutine {
                    audioManager = fetchSystemService(audioService).apply {
                        java.util.concurrent.Executors.newSingleThreadExecutor().execute {
                            audioMange()
                        }
                    }
                }
                kotlinx.coroutines.delay(100L)
                justCoroutine {
                    this@doAlarmRing?.let { com.pt.pro.alarm.objects.AlarmHelper.apply { updateNotification(it, boolean) } }
                    ctxMedia.doRing(this@doAlarmRing)
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun turnOnSCR(inter: Int) {
        fetchSystemService(powerService)?.also {
            if (isV_T) {
                (805306394 or
                        android.os.PowerManager.ON_AFTER_RELEASE)
            } else {
                @Suppress("DEPRECATION")
                (805306394 or
                        android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP or
                        android.os.PowerManager.ON_AFTER_RELEASE)
            }.also { flags ->
                mWakeLock = it.newWakeLock(
                    flags,
                    com.pt.pro.BuildConfig.APPLICATION_ID + ":full_wake_lock_alarm_service"
                ).apply {
                    setReferenceCounted(false)
                    acquire(inter * 60 * 1000L)
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun android.media.AudioManager?.audioMange() {
        if (this == null) return
        catchy(Unit) {
            if (mode != android.media.AudioManager.MODE_RINGTONE) {
                deviceMode = mode
                mode = android.media.AudioManager.MODE_RINGTONE
            }
            if (ringerMode != android.media.AudioManager.RINGER_MODE_NORMAL) {
                modeRinging = ringerMode
                ringerMode = android.media.AudioManager.RINGER_MODE_NORMAL
            }
            requestAlarmFocus {
                androidx.media.AudioManagerCompat.requestAudioFocus(this@audioMange, build())
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    private inline fun requestAlarmFocus(focus: androidx.media.AudioFocusRequestCompat.Builder.() -> Unit) {
        audioListener?.let {
            androidx.media.AudioFocusRequestCompat.Builder(
                androidx.media.AudioManagerCompat.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE
            ).apply {
                setAudioAttributes(alarmAudioTypeCompat.build())
                setWillPauseWhenDucked(true)
                setOnAudioFocusChangeListener(
                    it,
                    qHand ?: fetchHandler
                )
            }.apply(focus)
        }
    }

    private inline val alarmAudioTypeCompat: androidx.media.AudioAttributesCompat.Builder
        @com.pt.common.global.WorkerAnn
        get() {
            return androidx.media.AudioAttributesCompat.Builder().apply {
                setFlags(androidx.media.AudioAttributesCompat.FLAG_AUDIBILITY_ENFORCED)
                setUsage(androidx.media.AudioAttributesCompat.USAGE_ALARM)
                setContentType(androidx.media.AudioAttributesCompat.CONTENT_TYPE_MUSIC)
            }
        }


    private var audioListener: android.media.AudioManager.OnAudioFocusChangeListener? =
        android.media.AudioManager.OnAudioFocusChangeListener {

        }


    @Suppress("DEPRECATION")
    @com.pt.common.global.WorkerAnn
    private fun vibrateProv() {
        fetchSystemService(vibratorService)?.also {
            vibrator = it
            catchy(Unit) {
                if (it.hasVibrator()) {
                    if (isV_O) {
                        it.vibrate(
                            android.os.VibrationEffect.createWaveform(longArrayOf(0, 500, 1000), 0)
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        it.vibrate(longArrayOf(0, 500, 1000), 0)
                    }
                }
            }
        }
    }

    @APIAnn(31)
    private fun vibrateProv31() {
        fetchSystemService(vibratorManagerService)?.also {
            vib31 = it
            catchy(Unit) {
                android.os.CombinedVibration.createParallel(
                    android.os.VibrationEffect.createWaveform(longArrayOf(0, 500, 1000), 0)
                ).apply {
                    it.vibrate(this)
                }
            }
        }
    }

    private fun AlarmSack.updateAlarm(a: () -> Unit) {
        if (!isAlarm) {
            newDBAlarmNor {
                deleteAlarm(this@updateAlarm)
            }
            return
        }
        if (!allDays.contains(true)) {
            this@updateAlarm.copy(switchAlarm = false)
        } else {
            com.pt.pro.alarm.objects.AlarmHelper.getTimeForNextAlarm(timeAlarm, allDays).let {
                AlarmReceiver.apply {
                    setReminderAlarm(it.timeInMillis, idAlarm)
                }
                return@let this@updateAlarm.copy(timeAlarm = it.timeInMillis)
            }
        }.let {
            newDBAlarmNor {
                it.updateAlarm()
            }
        }
        a.invoke()
    }

    private fun android.content.Context.channelPro() {
        androidx.core.app.NotificationChannelCompat.Builder(
            CH_ALA,
            androidx.core.app.NotificationManagerCompat.IMPORTANCE_LOW or
                    androidx.core.app.NotificationManagerCompat.IMPORTANCE_MIN
        ).apply {
            setName(resources.getString(com.pt.pro.R.string.hn))
            setDescription(resources.getString(com.pt.pro.R.string.pn))
            setShowBadge(false)
            setImportance(
                androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH or
                        androidx.core.app.NotificationManagerCompat.IMPORTANCE_MAX
            )
            setSound(null, null)
        }.build().let {
            androidx.core.app.NotificationManagerCompat.from(this@channelPro).apply {
                createNotificationChannel(it)
            }
        }
    }

    private fun android.content.Context.channelProForPop() {
        androidx.core.app.NotificationChannelCompat.Builder(
            CH_ALA_POP,
            androidx.core.app.NotificationManagerCompat.IMPORTANCE_MAX or
                    androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
        ).apply {
            setName(resources.getString(com.pt.pro.R.string.hn))
            setDescription(resources.getString(com.pt.pro.R.string.pn))
            setShowBadge(false)
            setImportance(
                androidx.core.app.NotificationManagerCompat.IMPORTANCE_MAX or
                        androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
            )
            setSound(null, null)
        }.build().let {
            androidx.core.app.NotificationManagerCompat.from(this@channelProForPop).apply {
                createNotificationChannel(it)
            }
        }
    }

    private fun forAlarmRecordPlay() {
        alarm.let {
            if (it?.recordAlarm != null &&
                it.recordAlarm != null.toString() &&
                FileLate(it.recordAlarm.toStr).isFile
            ) {
                launchDef {
                    it.doPlayRecord()
                }
            } else {
                launchDef {
                    forDestroySus {
                        stopServiceNow()
                    }
                }
            }
        }
    }

    private suspend fun AlarmSack.doPlayRecord() {
        withMain {
            catchySus(Unit) {
                ring?.stop()
                ring = null
            }
        }
        withMain {
            if (recordAlarm != null) {
                mediaPlayer.applySus {
                    if (this@applySus == null) {
                        intiMediaPlayer().playRecord(recordAlarm.toStr)
                    } else {
                        withMain {
                            alarmCall?.alsoSus {
                                removeListener(it)
                            }
                            stopSus()
                            justScope {
                                mediaPlayer?.clearMediaItems()
                            }
                            mediaPlayer?.playRecord(recordAlarm.toStr)
                        }
                    }
                }
                justCoroutine {
                    com.pt.pro.alarm.objects.AlarmHelper.apply {
                        updateRecord(this@doPlayRecord)
                    }
                }
            } else {
                forDestroySus {
                    stopServiceNow()
                }
            }
        }
    }

    private fun forAlarmRecordDismiss(intent: android.content.Intent) {
        if (!intent.getBooleanExtra(JUST_CLOSE, true)) {
            newIntent(com.pt.pro.alarm.views.SetEditAlarm::class.java) {
                putExtra(MODE_EXTRA, EDIT_ALARM)
                putExtra(ALARM_EXTRA, intent.getIntExtra(ID_TO_DISMISS, -1))
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                        android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
                addCategory(LAUNCH_CATO)
                this@newIntent
            }.also(this@AlarmServiceNotification::startActivity)
        }
        launchDef {
            forDestroySus {
                stopServiceNow()
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroy() {
        alarm?.updateAlarm { }
        stopRinging {
            if (vibrator != null) {
                vibrator?.cancel()
            }
            if (vib31 != null) {
                if (isV_S) {
                    vib31?.cancel()
                }
            }
            if (mWakeLock?.isHeld == true) {
                mWakeLock?.release()
                mWakeLock = null
            }
            audioManager?.resetAudio()
            vibrator = null
            vib31 = null
            alarm = null
        }
        dispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()

        jobNative?.cancelJob()
        catchy(Unit) {
            dispatcherNative?.close()
        }
        extNative?.shutdownNow()
        cancelScope()
        extNative = null
        jobNative = null
        dispatcherNative = null
    }

}