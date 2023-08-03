package com.pt.pro.gallery.objects

data class PagerHolder(
    val mediaHolder: MutableList<com.pt.common.global.MediaSack>,
    val folds: MutableList<com.pt.common.global.MediaFolderSack>,
    val imagePosition: Int,
    val pending: Boolean,
    val main: Boolean = true,
    val isHiddenActive: Boolean,
    val isFileManager: Boolean,
    val isDoInLand: Boolean,
    val margeWidth: Int,
    val margeHeight: Int,
)