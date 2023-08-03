package com.pt.pro.gallery.interfaces

interface GalleryListener {
    fun toRefresh()
    fun String.onFolderClicked(pathFolder: String, realFolderCLick: Boolean) {}
    fun com.pt.common.global.DSack<Long, Long, String>.onTimeClicked(displayType: Int)
    fun com.pt.common.global.MediaSack.addMediaPending() {}
    fun MutableList<com.pt.common.global.MediaSack>.onPicClicked(
        position: Int,
        p: () -> Unit,
    )
}