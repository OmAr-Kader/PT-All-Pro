package com.pt.pro.notepad.interfaces

interface OnAudioRecordListener {
    fun onRecordFinished(dataKeeperItem: com.pt.pro.notepad.models.DataKeeperItem?)
    fun onError(errorCode: Int)
    fun onRecordingStarted()
}