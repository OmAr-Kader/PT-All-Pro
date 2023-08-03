package com.pt.pro.file.interfaces

interface DBFileListener {
    suspend fun com.pt.common.global.FileSack.insertFavFile(vir: String?): Long
    suspend fun MutableList<com.pt.common.global.FileSack>.insertFavFile(vir: String?)
    suspend fun getAllVFavFiles(name: String?): MutableList<com.pt.common.global.FileSack>
    suspend fun getAllFavPath(): MutableList<String>
    suspend fun getAllVirName(): com.pt.common.global.DSack<MutableList<com.pt.common.global.FileSack>, MutableList<String>, MutableList<String>>
    suspend fun deleteMedia(path: String, vir: String): Int
    suspend fun insertVirFile(name: String): Long
    suspend fun updateVir(name: String, num: Int): Boolean
    suspend fun updateVirtual(name: String, num: Int): Boolean
    suspend fun getAllVIrsFull(): MutableList<String>
    suspend fun getAllVIrs(): MutableList<com.pt.common.global.FileSack>
}