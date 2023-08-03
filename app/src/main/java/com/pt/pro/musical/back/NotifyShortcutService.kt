package com.pt.pro.musical.back

import com.pt.common.global.*
import com.pt.common.stable.*
import com.pt.pro.musical.music.MusicObject
import com.pt.pro.musical.music.MusicPlayer

@androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.N)
class NotifyShortcutService : android.service.quicksettings.TileService() {

    private fun android.service.quicksettings.Tile.updateNotifyIcon(isActive: Boolean) {
        if (((isActive && state == android.service.quicksettings.Tile.STATE_ACTIVE) ||
                    !isActive && state == android.service.quicksettings.Tile.STATE_INACTIVE) && icon != null
        ) return

        icon = android.graphics.drawable.Icon.createWithResource(
            baseContext,
            com.pt.pro.R.drawable.ic_music_notify
        )
        label = resources.getString(com.pt.pro.R.string.zh)
        state = if (isActive) {
            android.service.quicksettings.Tile.STATE_ACTIVE
        } else {
            android.service.quicksettings.Tile.STATE_INACTIVE
        }
        updateTile()
    }

    override fun onTileAdded() {
        super.onTileAdded()
        ensureBack {
            qsTile?.updateNotifyIcon(MusicObject.musicManagerListener != null)
        }
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        ensureBack {
            qsTile?.updateNotifyIcon(false)
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        ensureBack {
            qsTile?.updateNotifyIcon(MusicObject.musicManagerListener != null)
        }
    }


    override fun onClick() {
        super.onClick()
        ensureBack {
            qsTile?.apply {
                if (android.service.quicksettings.Tile.STATE_INACTIVE == state) {
                    updateNotifyIcon(true)
                    openMusicPlayer()
                } else if (android.service.quicksettings.Tile.STATE_ACTIVE == state) {
                    updateNotifyIcon(false)
                    stopMusicPlayer()
                }
            }
        }
    }

    private fun openMusicPlayer() {
        if (!hasExternalReadWritePermission) {
            fetchHand.post {
                makeToastRec(com.pt.pro.R.string.pf, 0)
            }
            return
        }
        launchMusic()
    }

    private fun stopMusicPlayer() {
        newIntent(MusicPlayer::class.java) {
            this@newIntent
        }.also {
            stopService(it)
        }
    }

    private fun launchMusic() {
        if (canFloat) {
            newIntent(MusicPlayer::class.java) {
                flags = FLAGS
                action = PLAY_MUSIC
                putExtra(KEY_ORDER, "tillITHurts")
                this@newIntent
            }.also {
                androidx.core.content.ContextCompat.startForegroundService(
                    this@NotifyShortcutService,
                    it
                )
            }
        } else {
            if (isV_M) {
                fetchHand.post {
                    makeToastRec(com.pt.pro.R.string.rl, 0)
                }
            }
        }
    }

}