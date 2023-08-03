package com.pt.pro.file.interfaces

interface DialogListener {
    fun MutableList<com.pt.common.global.MusicSack>.pushMusic(position: Int) {}
    fun MutableList<com.pt.common.global.FileSack>.pushList(bool: Boolean) {}
    fun ItemFileListener.pushListener() {}
    fun MutableList<String>.pushStrings() {}

}