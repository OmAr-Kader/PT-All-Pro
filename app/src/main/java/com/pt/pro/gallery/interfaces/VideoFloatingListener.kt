package com.pt.pro.gallery.interfaces

interface VideoFloatingListener : com.pt.common.mutual.base.OnConfigurationChanged {

    val viewVideo: com.pt.pro.gallery.service.VideoFloatFasten
    val floPlayer: androidx.media3.exoplayer.ExoPlayer

    fun com.pt.common.global.MediaSack.setCurrentVideo()
    fun MutableList<com.pt.common.global.MediaSack>.setAllVideos()
    fun com.pt.pro.gallery.service.VideoFloatFasten.setViewRoot()
    fun com.pt.pro.gallery.service.VideoFloatFasten.extensionsVisible()
    fun com.pt.pro.gallery.service.VideoFloatFasten.extensionsGone()
    fun playCompleted()
    fun MutableList<com.pt.common.global.MediaSack>.updateVideos(
        newVideo: com.pt.common.global.MediaSack,
    )
}