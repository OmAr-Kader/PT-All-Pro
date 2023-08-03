package com.pt.pro.musical.interfaces

interface MusicListenerAdapter : kotlinx.coroutines.CoroutineScope {

    val songList: MutableList<com.pt.common.global.MusicSack>
    val pathsList: MutableList<String>
    var currentPath: String
    var picListener: MusicOption?
    var colorItem: Int
    val isRTL: Boolean
    val MutableList<com.pt.common.global.MusicSack>.fetchTruePosition: (adaPos: Int, adaPosTarget: Int) -> Int
    suspend fun onItemMove(adaPos: Int, adaPosTarget: Int, newCurrentPath: String)
    suspend fun updateCurrentView(newCurrentPath: String)
    suspend fun onDismissItem(removedPath: String, newCurrentPath: String)
    suspend fun MutableList<com.pt.common.global.MusicSack>.updateSongs()
    suspend fun removeSongs()
    suspend fun MutableList<String>.updatePaths()
    fun justClear()
    suspend fun justNotify()
    fun onAdapterDestroy()

    fun com.pt.pro.musical.fasten.ItemSongFasten.loadAsyncAlbum(
        album: String,
        str: String,
        job: kotlinx.coroutines.Job?.() -> Unit
    )

    fun com.pt.pro.musical.fasten.ItemSongFasten.loadAsyncImg(
        path: String,
        str: String,
        job: kotlinx.coroutines.Job?.() -> Unit
    )

    fun com.pt.pro.musical.fasten.ItemSongFasten.displayLoadAsyncAlbum(
        bit: com.pt.common.global.DSackV<android.graphics.Bitmap?, String>,
        job: kotlinx.coroutines.Job?.() -> Unit
    )

    fun com.pt.pro.musical.fasten.ItemSongFasten.displayLoadAsyncImg(
        bit: com.pt.common.global.DSackV<android.graphics.Bitmap?, String>,
        job: kotlinx.coroutines.Job?.() -> Unit
    )
}