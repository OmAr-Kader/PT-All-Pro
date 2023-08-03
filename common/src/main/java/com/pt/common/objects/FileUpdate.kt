package com.pt.common.objects

data class FileUpdate(
    val pathScroll: String,
    val fileType: Int,
    var pos: Int = 0
)