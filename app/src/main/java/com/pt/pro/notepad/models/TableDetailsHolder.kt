package com.pt.pro.notepad.models

data class TableDetailsHolder(
    val tableIndex: Int,
    val nightRider: Boolean,
    val user: Boolean,
    val detail: Boolean,
    val firstTime: Boolean,
    var isNotification: Boolean = false,
)
