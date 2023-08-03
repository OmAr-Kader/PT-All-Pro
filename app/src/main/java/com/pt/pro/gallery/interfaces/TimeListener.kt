package com.pt.pro.gallery.interfaces

interface TimeListener {
    var isHiddenActive: Boolean
    val foldsDuo: MutableList<com.pt.common.objects.MediaDuoTime>
    var picListener: GalleryListener?
    val widthColumn: Int
    val increase: Int
    var adPosition: Int
    val span: Int
    val runForLook: (Int) -> Int
    suspend fun MutableList<com.pt.common.objects.MediaDuoTime>.updateDuoTime()
    fun onAdapterDestroy()
}