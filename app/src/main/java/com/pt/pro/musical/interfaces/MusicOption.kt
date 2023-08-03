package com.pt.pro.musical.interfaces

interface MusicOption : com.pt.common.mutual.base.OnConfigurationChanged, () -> Unit,
    com.pt.common.mutual.base.JobHand {
    var app: android.app.Application?
    var fetchMusicService: (() -> com.pt.pro.musical.music.MusicPlayer)?
    fun androidx.media3.session.MediaLibraryService.intiPlayer(): androidx.media3.session.MediaLibraryService.MediaLibrarySession
    fun fetchNotify(): androidx.media3.session.MediaNotification?
    fun fetchSession(): androidx.media3.session.MediaLibraryService.MediaLibrarySession?

    fun MutableList<com.pt.common.global.MusicSack>.pushNewSongs(pos: Int, displayAfter: Boolean)
    fun onUpdateService(action: String?)
    fun inti(fromCreate: Boolean)
    suspend fun MutableList<com.pt.common.global.MusicSack>.setOrgAllSongs(pos: Int)

    fun onDetailsClick(filterFromAlbum: String?, filterFromArtist: String?, artist: String?)
    fun com.pt.common.global.MusicSack.onSongItemCLick(add: Boolean, run: () -> Unit)
    fun com.pt.common.global.MusicSack.onPlayItemCLick()
    fun onPlaylist(playlistBelong: String?, pos: Int)
    fun updatePlayer(progress: Long)
    suspend fun onServiceDestroySus(b: () -> Unit)

}