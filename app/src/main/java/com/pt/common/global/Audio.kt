package com.pt.common.global

inline val android.content.Context.fetchDefAlarm: android.net.Uri
    get() = android.media.RingtoneManager.getValidRingtoneUri(this@fetchDefAlarm)

internal inline val soundSource: Int
    @com.pt.common.global.WorkerAnn
    get() {
        return if (isV_Q) {
            android.media.MediaRecorder.AudioSource.VOICE_PERFORMANCE
        } else {
            android.media.MediaRecorder.AudioSource.MIC
        }
    }

internal inline val androidx.media3.exoplayer.ExoPlayer?.mediaPosLongRun: (Long) -> Long
    @com.pt.common.global.WorkerAnn
    get() = {
        try {
            if (this?.currentMediaItem != null) {
                this.currentPosition
            } else {
                it
            }
        } catch (e: java.lang.IllegalStateException) {
            it
        } catch (e: java.lang.RuntimeException) {
            it
        } catch (e: Throwable) {
            it
        }
    }


internal inline val androidx.media3.exoplayer.ExoPlayer?.mediaPosLong: Long?
    @com.pt.common.global.WorkerAnn
    get() {
        return try {
            if (this?.currentMediaItem != null) {
                this.currentPosition
            } else {
                null
            }
        } catch (e: java.lang.IllegalStateException) {
            null
        } catch (e: java.lang.RuntimeException) {
            null
        } catch (e: Throwable) {
            null
        }
    }

internal inline val androidx.media3.common.Player.Events.isItemChanged: Boolean
    get() = contains(androidx.media3.common.Player.EVENT_MEDIA_METADATA_CHANGED) ||
            contains(androidx.media3.common.Player.EVENT_TRACKS_CHANGED)

suspend fun androidx.media3.common.Player.pauseSus() {
    com.pt.common.stable.withMain {
        if (isPlaying) pause()
    }
}

suspend fun androidx.media3.common.Player.playSus() {
    com.pt.common.stable.withMain {
        if (!isPlaying) play()
    }
}

internal inline val androidx.media3.common.Player.durationSus: (suspend () -> Long)
    get() = {
        com.pt.common.stable.withMainDef(10000) {
            duration.let {
                if (it > 0) it else 10000
            }
        }
    }

internal inline val androidx.media3.common.Player.currentPositionSus: (suspend () -> Long)
    get() = {
        com.pt.common.stable.withMainDef(0L) {
            currentPosition
        }
    }

suspend fun androidx.media3.common.Player.stopSus() {
    com.pt.common.stable.withMain {
        stop()
    }
}

internal suspend inline fun androidx.media3.common.Player.setMediaItemsSus(
    crossinline items: suspend () -> MutableList<androidx.media3.common.MediaItem>
) {
    com.pt.common.stable.withMain {
        setMediaItems(items.invoke())
    }
}

suspend fun androidx.media3.common.Player.prepareSus() {
    com.pt.common.stable.withMain {
        prepare()
    }
}

suspend fun androidx.media3.common.Player.intiMusicSeek(progress: Long) {
    com.pt.common.stable.withMain {
        if (progress != 1L) {
            seekTo(progress)
        } else return@withMain
    }
}

internal inline val androidx.media3.common.Player.isPlayingSus: suspend () -> Boolean
    get() = {
        com.pt.common.stable.withMainDef(false) {
            return@withMainDef isPlaying
        }
    }

inline val androidx.media3.exoplayer.ExoPlayer.sessionID: Int
    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    get() = audioSessionId


@com.pt.common.global.WorkerAnn
fun requestAudioFocus(
    b: android.media.AudioManager.OnAudioFocusChangeListener
): androidx.media.AudioFocusRequestCompat.Builder {
    return androidx.media.AudioFocusRequestCompat.Builder(
        androidx.media.AudioManagerCompat.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE
    ).also {
        /*musicAudioType {
            it.setAudioAttributes(this@musicAudioType.build())
        }*/
        it.setWillPauseWhenDucked(true)
        it.setOnAudioFocusChangeListener(
            b,
            androidx.core.os.HandlerCompat.createAsync(android.os.Looper.getMainLooper())
        )
    }
}

internal val musicAudioAttr: androidx.media3.common.AudioAttributes
    get() {
        return androidx.media3.common.AudioAttributes.Builder().apply {
            setUsage(androidx.media3.common.C.USAGE_MEDIA)
            setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC)
        }.build()
    }

internal inline val androidx.media3.exoplayer.ExoPlayer.audioSessionIdSus: (suspend () -> Int)
    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    get() = {
        com.pt.common.stable.withMainDef(0) {
            return@withMainDef audioSessionId
        }
    }

@com.pt.common.global.WorkerAnn
inline fun ringtonePicker(str: String, intent: android.content.Intent.() -> Unit) {
    android.content.Intent(android.media.RingtoneManager.ACTION_RINGTONE_PICKER).apply {
        putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_TITLE, str)
        putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
        putExtra(android.media.RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false)
        putExtra(
            android.media.RingtoneManager.EXTRA_RINGTONE_TYPE,
            android.media.RingtoneManager.TYPE_RINGTONE
        )
    }.also(intent)
}

@com.pt.common.global.WorkerAnn
fun android.media.AudioManager?.request(
    b: android.media.AudioManager.OnAudioFocusChangeListener?
) {
    if (this == null) return
    b?.let {
        requestAudioFocus(it).apply {
            androidx.media.AudioManagerCompat.requestAudioFocus(this@request, build())
        }
    }
}

@com.pt.common.global.WorkerAnn
fun android.content.Context.vibrationProvider(
    vibe: Long,
    am: Int = -1
) {
    when {
        isV_S -> {
            fetchSystemService(vibratorManagerService).run {
                this?.vibrate(
                    android.os.CombinedVibration.createParallel(
                        android.os.VibrationEffect.createOneShot(
                            vibe,
                            am
                        )
                    )
                )
            }
        }
        isV_O -> {
            @Suppress("DEPRECATION")
            fetchSystemService(vibratorService).run {
                this?.vibrate(
                    android.os.VibrationEffect.createOneShot(
                        vibe,
                        am
                    )
                )
            }
        }
        else -> {
            @Suppress("DEPRECATION")
            fetchSystemService(vibratorService).run {
                android.os.VibrationEffect.createOneShot(
                    vibe,
                    am
                ).apply {
                    this@run?.vibrate(this@apply, null)
                }
                //this?.vibrate(vibe)
            }
        }
    }
}

internal fun android.media.MediaRecorder.setVideoRate(mediumQ: Boolean) {
    if (mediumQ) {
        5 * 1000 * 1000
    } else {
        10 * 1000 * 1000
    }.let {
        setVideoEncodingBitRate(it)
    }
}

internal fun android.media.MediaRecorder.setRateAndEncoding(isAud: Boolean) {
    if (isAud) {
        setAudioSamplingRate(44100)
        setAudioEncodingBitRate(16 * 44100)
    } else {
        return
    }
}