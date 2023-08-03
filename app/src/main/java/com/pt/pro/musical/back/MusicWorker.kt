package com.pt.pro.musical.back

import com.pt.common.global.*
import com.pt.common.stable.*
import com.pt.common.stable.newDBPlaylist

class MusicWorker(
    context: android.content.Context?,
    parameters: androidx.work.WorkerParameters,
) : androidx.work.CoroutineWorker(context!!, parameters) {

    override suspend fun doWork(): Result {
        inputData.also { intent ->
            com.pt.pro.musical.music.MusicObject.musicList?.letSusBack { allMusic ->
                intent.getInt(
                    MUSIC_POS_KEY, -1
                ).letSusBack { pos ->
                    intent.getLong(
                        MUSIC_PROGRESS_KEY, -1L
                    ).letSusBack { progress ->
                        if (pos != -1 && progress != -1L && allMusic.isNotEmpty()) {
                            applicationContext.applySusBack {
                                justScope {
                                    this@applySusBack.newDBPlaylist {
                                        deleteAllMusic(null)
                                    }
                                    kotlinx.coroutines.delay(100L)
                                    this@applySusBack.newDBPlaylist {
                                        justScope {
                                            allMusic.toMutableList().insertMusicDefault()
                                        }
                                    }
                                }
                                this@applySusBack.updatePrefInt(MUSIC_POS_KEY, pos)
                                this@applySusBack.updatePrefLong(MUSIC_PROGRESS_KEY, progress)
                                intent.getString(MUSIC_SONG_KEY)?.letSusBack {
                                    this@applySusBack.updatePrefString(MUSIC_SONG_KEY, it)
                                }
                                justScope {
                                    com.pt.pro.musical.music.MusicObject.musicList = null
                                    applicationContext?.isServiceNotRunning(
                                        com.pt.pro.musical.music.MusicPlayer::class.java.canonicalName
                                    ).also {
                                        if (it == false) {
                                            applicationContext?.stopService(
                                                android.content.Intent(
                                                    applicationContext,
                                                    com.pt.pro.musical.music.MusicPlayer::class.java
                                                )
                                            )
                                        }
                                    }
                                    applicationContext?.fetchSystemService(notificationService)?.also { nm ->
                                        nm.cancel(SERVICE_MUSIC)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return Result.success()
    }

}