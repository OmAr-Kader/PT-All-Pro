package com.pt.pro.notepad.models

data class DataKeeperItem(
    val dataText: String?,
    val keeperType: Int,
    val emailToSome: String?,
    val emailSubject: String?,
    val timeData: Long,
    val recordPath: String?,
    val recordLength: Long,
    val imageUrl: String?,
    val dayNum: Int,
    var isDone: Boolean = false,
    var isSelectData: Boolean = false,
    var notTime: Long = -1L,
)