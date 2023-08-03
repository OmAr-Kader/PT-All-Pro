package com.pt.pro.gallery.interfaces

interface PictureFolderListener {
    val folderMedia: MutableList<com.pt.common.global.MediaFolderSack>
    var listenToClick: GalleryListener?
    val folderWidth: Int
    val folderHeight: Int
    val color: Int
    suspend fun MutableList<com.pt.common.global.MediaFolderSack>.updateFolder()
    fun onAdapterDestroy()
}