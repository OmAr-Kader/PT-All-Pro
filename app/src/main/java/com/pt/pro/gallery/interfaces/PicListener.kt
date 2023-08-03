package com.pt.pro.gallery.interfaces

interface PicListener {

    val mediaList: MutableList<com.pt.common.global.MediaSack>
    var picListener: DisplayListen?
    var widthX: Int
    var increase: Int
    val deleteCopy: MutableList<com.pt.common.global.MediaSack>
    val pos: MutableList<Int>
    var adPosition: Int
    val isActionMode: Boolean

    val span: Int

    suspend fun refreshAdapter()
    suspend fun MutableList<com.pt.common.global.MediaSack>.updateMedia()
    suspend fun selectAll()
    suspend fun justNotify()
    fun onAdapterDestroy()

}