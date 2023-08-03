package com.pt.common.objects

data class FolderMedia(
    val path: String?,
    val folderName: String?,
    var numberOfPics: Int,
    val firstImagePath: String?,
) {
    fun addPics() {
        numberOfPics++
    }
}