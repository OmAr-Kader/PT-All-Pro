package com.pt.pro.musical.interfaces

interface DBPlaylistListener {
    suspend fun MutableList<com.pt.common.global.MusicSack>.insertMusic()
    suspend fun MutableList<com.pt.common.global.MusicSack>.insertMusicDefault()
    suspend fun getAllSongs(playlist: String): MutableList<com.pt.common.global.MusicSack>
    suspend fun getAllSongsDefault(): MutableList<com.pt.common.global.MusicSack>
    suspend fun deleteAllMusic(playlist: String?): Int
    suspend fun com.pt.common.global.MusicSack.insertPlaylist(): Long
    suspend fun updatePlaylist(name: String, num: Int): Boolean
    suspend fun getAllPlaylist(): MutableList<com.pt.common.global.MusicSack>
    suspend fun isAlreadyPlaylist(playlist: String): Boolean
}