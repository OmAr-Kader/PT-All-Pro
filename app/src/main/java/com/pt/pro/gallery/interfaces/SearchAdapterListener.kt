package com.pt.pro.gallery.interfaces

interface SearchAdapterListener {
    val mediaList: MutableList<com.pt.common.global.MediaSack>
    var picListener: GalleryListener?
    val widthX: Int
    val increase: Int

    suspend fun MutableList<com.pt.common.global.MediaSack>.updateMedia()
    fun onAdapterDestroy()
}