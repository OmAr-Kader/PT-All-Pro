package com.pt.common.objects

data class MediaDuoTime(
    val path: String?,
    val folderName: String?,
    val numberOfPics: Int,
    val mediaHolder: com.pt.common.global.MediaSack?,
    var typeItem: Int = com.pt.common.stable.ITEM_NORMAL,
)