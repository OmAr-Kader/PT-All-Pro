package com.pt.pro.gallery.interfaces

interface VideoListener {

    var mediaPlay: androidx.media3.exoplayer.ExoPlayer?
    var videoCall: androidx.media3.common.Player.Listener?

    var mediaHolder: com.pt.common.global.MediaSack?
    val allImages: MutableList<com.pt.common.global.MediaSack>?
    var marginNum: Int
    var marginHeight: Int
    var browserListener: BrowserListener?
    var hideSys: Boolean
    val scaleRemove: com.pt.common.global.DSackT<() -> Unit, Int>
    val onEverySecond: com.pt.common.global.DSackT<() -> Unit, Int>
    val runFloatingVideo: com.pt.common.global.DSackT<() -> Unit, Int>
    val seekSacker: com.pt.common.global.DSackT<Long, Int>?

    fun doLockVid()
    fun doUnLockVid()
    fun removeSeek()

    fun onChangedPlaybackState(playbackState: Int)
    fun onChangedTracks()
    fun onChangedIsPlaying(isPlaying: Boolean)
    fun onError(error: androidx.media3.common.PlaybackException)
}