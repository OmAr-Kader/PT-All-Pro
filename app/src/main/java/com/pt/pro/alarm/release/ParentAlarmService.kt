package com.pt.pro.alarm.release

import com.pt.common.global.*
import com.pt.common.media.getAudioPathFromURI
import com.pt.common.stable.*
import kotlinx.coroutines.asCoroutineDispatcher

abstract class ParentAlarmService : android.app.Service(), androidx.lifecycle.LifecycleOwner, com.pt.common.mutual.base.JobHand,
    kotlinx.coroutines.CoroutineScope {

    internal var jobNative: kotlinx.coroutines.Job? = null
    internal var extNative: java.util.concurrent.ExecutorService? = null
    internal var dispatcherNative: kotlinx.coroutines.ExecutorCoroutineDispatcher? = null

    internal inline val job: kotlinx.coroutines.Job
        get() = jobNative ?: kotlinx.coroutines.SupervisorJob().also { jobNative = it }

    internal inline val ext: java.util.concurrent.ExecutorService
        get() = extNative ?: fetchExtractor.also { extNative = it }

    internal inline val dispatch: kotlinx.coroutines.ExecutorCoroutineDispatcher
        get() = dispatcherNative ?: ext.asCoroutineDispatcher().also { dispatcherNative = it }

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = dispatch + job + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    @Volatile
    internal var mWakeLock: android.os.PowerManager.WakeLock? = null

    @Volatile
    internal var vibrator: android.os.Vibrator? = null

    @Volatile
    internal var vib31: android.os.VibratorManager? = null

    @Volatile
    internal var alarm: AlarmSack? = null

    @Volatile
    internal var deviceMode: Int? = null

    @Volatile
    internal var modeRinging: Int? = null

    @Volatile
    protected var ring: android.media.Ringtone? = null

    @Volatile
    protected var audioManager: android.media.AudioManager? = null

    private inline val audioMange: android.media.AudioManager?
        get() {
            return audioManager ?: this@ParentAlarmService.baseContext?.fetchSystemService(audioService).also {
                audioManager = it
            }
        }

    @Volatile
    protected var mediaPlayer: androidx.media3.exoplayer.ExoPlayer? = null

    protected fun android.content.Context.doRing(alarmSack: AlarmSack?) {
        launchDef {
            alarmSack.fetchRingTone().alsoSusBack { uri ->
                if (isV_P) {
                    kotlin.runCatching {
                        useRingtone(uri)
                    }.onFailure {
                        it.toStr.logProvCrash("useRingtone")
                        catchySus(Unit) {
                            ring?.stop()
                            ring = null
                        }
                        useMediaPlayer(uri)
                    }
                } else {
                    useMediaPlayer(uri)
                }
            }
        }
    }

    @androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.P)
    private suspend fun android.content.Context.useRingtone(uri: android.net.Uri) {
        withMain {
            android.media.RingtoneManager.getRingtone(
                this@useRingtone, uri
            ).also { r ->
                r.isLooping = true
                r.audioAttributes = alarmAudioNativeType.build()
                r.play()
                ring = r
            }
        }
        justCoroutine {
            findIntegerPreference(VOL_T, 0).let { volumeNumber ->
                if (volumeNumber != 0) {
                    ring?.volume = 0.25F
                    volumeNumber.runForVolumeRing.rKTSack(500L).postAfter()
                }
            }
        }
    }

    @get:androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.P)
    private val Int.runForVolumeRing: DSackT<() -> Unit, Int>
        get() = toCatchSack(933) {
            ring?.apply {
                (volume + 0.05F).let {
                    if (it < 1.0F) {
                        volume = it
                        runForVolumeRing.rKTSack(this@runForVolumeRing * 1000L).postAfter()
                    } else {
                        volume = 1.0F
                    }
                }
            }
            Unit
        }

    private inline val alarmAudioNativeType: android.media.AudioAttributes.Builder
        @com.pt.common.global.WorkerAnn get() {
            return android.media.AudioAttributes.Builder().apply {
                setFlags(android.media.AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                setUsage(android.media.AudioAttributes.USAGE_ALARM)
                setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
            }
        }

    private suspend fun AlarmSack?.fetchRingTone(): android.net.Uri = justCoroutine {
        withBackDef(fetchDefAlarm) {
            contentResolver.getAudioPathFromURI(this@fetchRingTone?.ringAlarm)?.letSusBack {
                FileLate(it)
            }.letSusBack {
                return@letSusBack if (this@fetchRingTone?.ringAlarm != null && it?.exists() == true) this@fetchRingTone.ringAlarm.toUri
                else fetchDefAlarm
            }
        }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    protected suspend fun android.content.Context.intiMediaPlayer(): androidx.media3.exoplayer.ExoPlayer = justCoroutine {
        androidx.media3.exoplayer.ExoPlayer.Builder(
            this@intiMediaPlayer
        ).setWakeMode(androidx.media3.common.C.WAKE_MODE_LOCAL)
            .setAudioAttributes(musicTypeAudio, true)
            .build().also {
                it.setForegroundMode(true)
                it.playWhenReady = true
            }.applySus {
                mediaPlayer = this
            }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private suspend fun android.content.Context.useMediaPlayer(uri: android.net.Uri) {
        withMain {
            androidx.media3.exoplayer.ExoPlayer.Builder(
                this@useMediaPlayer
            ).setWakeMode(androidx.media3.common.C.WAKE_MODE_LOCAL)
                .setAudioAttributes(alarmAudioType.build(), false)
                .build().also { exp ->
                    exp.setForegroundMode(true)
                    catchyUnit { audioMange?.generateAudioSessionId()?.also { exp.audioSessionId = it } }
                    exp.playWhenReady = true
                }.applySus {
                    mediaPlayer = this
                    playAlarmSong(uri)
                }
        }
    }

    private suspend fun androidx.media3.exoplayer.ExoPlayer.playAlarmSong(uri: android.net.Uri) {
        justScope {
            findIntegerPreference(VOL_T, 0).let { volumeNumber ->
                if (volumeNumber != 0) {
                    volume = 0.25F
                    volumeNumber.runForVolume.rKTSack(500L).postAfter()
                }
            }
        }
        doSound(uri)
    }

    private inline val musicTypeAudio: androidx.media3.common.AudioAttributes
        get() {
            return androidx.media3.common.AudioAttributes.Builder().apply {
                setUsage(androidx.media3.common.C.USAGE_MEDIA)
                setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC)
            }.build()
        }


    private var recordCall: androidx.media3.common.Player.Listener? = object : androidx.media3.common.Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == androidx.media3.common.Player.STATE_ENDED) {
                toCatchSack(174) {
                    if (mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.pause()
                    }
                }.postNow()
                launchDef {
                    forDestroySus {
                        stopServiceNow()
                    }
                }
            }
        }
    }

    protected suspend fun androidx.media3.exoplayer.ExoPlayer.playRecord(record: String) {
        withMain {
            alarmCall?.letSus {
                addListener(it)
                alarmCall = null
            }
        }
        withMain {
            repeatMode = androidx.media3.common.Player.REPEAT_MODE_OFF
            setMediaItem(androidx.media3.common.MediaItem.Builder().setUri(record.toUri).build())
            prepare()
            recordCall?.let {
                addListener(it)
            }
        }
    }

    private suspend fun androidx.media3.exoplayer.ExoPlayer.doSound(ringUri: android.net.Uri) {
        withMain {
            repeatMode = androidx.media3.common.Player.REPEAT_MODE_ALL
            setMediaItem(androidx.media3.common.MediaItem.Builder().setUri(ringUri).build())
            prepare()
            alarmCall?.let { addListener(it) }
        }
    }

    private inline val alarmAudioType: androidx.media3.common.AudioAttributes.Builder
        @com.pt.common.global.WorkerAnn get() {
            return androidx.media3.common.AudioAttributes.Builder().apply {
                setUsage(androidx.media3.common.C.USAGE_ALARM)
            }
        }

    protected var alarmCall: androidx.media3.common.Player.Listener? =
        object : androidx.media3.common.Player.Listener {

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (mediaPlayer != null) {
                    toCatchSack(164) {
                        if (playbackState == androidx.media3.common.Player.STATE_ENDED) {
                            mediaPlayer?.apply {
                                if (!isPlaying) play()
                            }
                        }
                    }.postNow()
                }
            }
        }

    private val Int.runForVolume: DSackT<() -> Unit, Int>
        get() = toCatchSack(943) {
            mediaPlayer?.apply {
                (volume + 0.05F).let {
                    if (it < 1.0F) {
                        volume = it
                        runForVolume.rKTSack(this@runForVolume * 1000L).postAfter()
                    } else {
                        volume = 1.0F
                    }
                }
            }
            Unit
        }

    private suspend fun AlarmSack.updateAlarmSus(a: suspend () -> Unit) {
        withBack {
            if (!isAlarm) {
                newDBAlarm {
                    deleteAlarm(this@updateAlarmSus)
                }
                return@withBack
            } else return@withBack
        }
        withBack {
            if (!allDays.contains(true)) {
                this@updateAlarmSus.copy(switchAlarm = false)
            } else {
                com.pt.pro.alarm.objects.AlarmHelper.getTimeForNextAlarm(timeAlarm, allDays).letSusBack {
                    AlarmReceiver.applySusBack {
                        setReminderAlarm(it.timeInMillis, idAlarm)
                    }
                    return@letSusBack justCoroutine {
                        return@justCoroutine this@updateAlarmSus.copy(timeAlarm = it.timeInMillis)
                    }
                }
            }.letSusBack {
                newDBAlarm {
                    it.updateAlarmSus()
                }
            }
        }
        justCoroutine {
            a.invoke()
        }
    }

    protected fun stopRinging(a: () -> Unit) {
        unPostAll()
        catchy(Unit) {
            ring?.stop()
            ring = null
        }
        kotlin.runCatching {
            mediaPlayer?.apply {
                synchronized(this) {
                    catchy(Unit) {
                        alarmCall?.also { removeListener(it) }
                        recordCall?.also { removeListener(it) }
                    }
                    catchy(Unit) {
                        stop()
                    }
                    catchy(Unit) {
                        release()
                    }
                }
            }
            alarmCall = null
            audioManager?.resetAudio()
            recordCall = null
            mediaPlayer = null
            a.invoke()
        }.onFailure {
            mediaPlayer?.apply {
                synchronized(this) {
                    catchy(Unit) {
                        alarmCall?.also { it1 -> removeListener(it1) }
                        recordCall?.also { it1 -> removeListener(it1) }
                    }
                    catchy(Unit) {
                        stop()
                    }
                    catchy(Unit) {
                        release()
                    }
                }
            }
            alarmCall = null
            recordCall = null
            mediaPlayer = null
            it.toStr.logProvCrash("AlarmServiceNot")
        }
    }

    protected suspend fun forDestroySus(a: () -> Unit) {
        alarm?.updateAlarmSus {
            stopRingingSus()
            disMiss(a)
        } ?: disMiss(a)
    }

    private suspend fun disMiss(a: () -> Unit) {
        withMain {
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
        withMain {
            a.invoke()
        }
    }

    private suspend fun stopRingingSus() {
        withMain {
            unPostAll()
            catchy(Unit) {
                ring?.stop()
                ring = null
            }
            kotlin.runCatching {
                mediaPlayer?.apply {
                    synchronized(this) {
                        catchy(Unit) {
                            alarmCall?.also { it1 -> removeListener(it1) }
                            recordCall?.also { it1 -> removeListener(it1) }
                        }
                        catchy(Unit) {
                            stop()
                        }
                        catchy(Unit) {
                            release()
                        }
                    }
                }
                audioManager?.resetAudio()
                alarmCall = null
                recordCall = null
                mediaPlayer = null
            }.onFailure {
                mediaPlayer?.apply {
                    synchronized(this) {
                        catchy(Unit) {
                            alarmCall?.also { it1 -> removeListener(it1) }
                            recordCall?.also { it1 -> removeListener(it1) }
                        }
                        catchy(Unit) {
                            stop()
                        }
                        catchy(Unit) {
                            release()
                        }
                    }
                }
                alarmCall = null
                recordCall = null
                mediaPlayer = null
                it.toStr.logProvCrash("AlarmServiceNot")
            }
        }
    }


    protected fun android.media.AudioManager.resetAudio() {
        deviceMode?.also {
            mode = it
            deviceMode = null
        }
        modeRinging?.also {
            ringerMode = it
            modeRinging = null
        }
        audioManager = null
    }

}