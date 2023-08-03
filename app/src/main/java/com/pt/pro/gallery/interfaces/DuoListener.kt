package com.pt.pro.gallery.interfaces

interface DuoListener {

    val foldsDuo: MutableList<com.pt.common.objects.MediaDuo>
    var picListener: GalleryListener?
    val widthColumn: Int
    val increase: Int
    val isRightToLeft: Boolean
    val col: Int
    val observerDestroy: MutableList<() -> Unit>
    suspend fun MutableList<com.pt.common.objects.MediaDuo>.updateDuo()
    fun onAdapterDestroy()

}