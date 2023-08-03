package com.pt.pro.file.interfaces

interface ItemFileListener {
    fun onVirtualItemCLick(name: String)
    fun onCatoItemCLick(cato: Int, setPath: Boolean)
    fun onActionDisplay(showCard: Boolean, text: String?)
    fun MutableList<com.pt.common.global.FileSack>.applyHidden()
    fun MutableList<com.pt.common.global.FileSack>.applyShow()
    fun MutableList<com.pt.common.global.FileSack>.applyStoreShow()
    fun MutableList<com.pt.common.global.MusicSack>.launchMusic(pos: Int, o: Int)
    fun openMusicPlaylist()
    fun MutableList<com.pt.common.global.FileSack>.addForVir(itF: String)
    fun MutableList<com.pt.common.global.FileSack>.removeFromVir(itF: String)
    fun MutableList<com.pt.common.global.FileSack>.openVirtual()
    fun com.pt.common.global.FileSack.onRealItemCLick(position: Int, fromFav: Boolean)
    fun MutableList<com.pt.common.global.FileSack>.createFolder(name: String, real: Boolean, isFromVir: Boolean)
}