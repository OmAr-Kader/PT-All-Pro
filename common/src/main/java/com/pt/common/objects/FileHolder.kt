package com.pt.common.objects

data class FileHolder(
    val fileName: String,
    val filePath: String,
    val fileUri: String?,
    val fileSize: Long,
    val typeFile: Int,
    val dateModified: Long,
    var isSelect: Boolean = false,
    var isCopy: Boolean = false,
    var isReal: Boolean = true,
    var virName: String? = null,
    var typeItem: Int = 0,
)