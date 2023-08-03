package com.pt.pro.musical.interfaces

import com.pt.common.moderator.recycler.CircularSeekBar

interface ActivityListener : CircularSeekBar.OnCircularSeekBarChangeListener {

    fun updatePlayPause(isPlayer: Boolean)
    fun forFinish()
    fun updateProgress(f: Float)

    fun com.pt.common.global.DSack<Int, Int, Int>.updateMusicActivity(
        musicSack: com.pt.common.global.MusicSack,
        f: Float,
        isFailedLoadPic: Boolean,
    )
}