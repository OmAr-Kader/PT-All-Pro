package com.pt.pro.notepad.recorder

import com.pt.common.global.FileLate
import com.pt.common.global.fetchCalenderDay
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.pro.notepad.interfaces.OnAudioRecordListener
import com.pt.pro.notepad.models.DataKeeperItem
import com.pt.pro.notepad.objects.KEEP_RECORD
import com.pt.pro.notepad.recorder.AudioRecordThread.OnRecorderFailedListener

class AudioRecording {

    private var file: FileLate? = null
    private var onAudioRecordListener: OnAudioRecordListener? = null
    private var mStartingTimeMillis: Long = 0
    private val error = 1
    private val recorderError = 2
    private val fileNull = 3

    private var mRecordingThread: Thread? = null

    fun setOnAudioRecordListener(onAudioRecordListener: OnAudioRecordListener?) {
        this.onAudioRecordListener = onAudioRecordListener
    }

    fun setFile(filePath: String) {
        file = FileLate(filePath)
    }

    // Call this method from Activity onStartButton Click to ta recording
    fun startRecording(source: Int) {
        if (file == null) {
            onAudioRecordListener?.onError(fileNull)
            return
        }
        mStartingTimeMillis = System.currentTimeMillis()
        try {
            if (mRecordingThread != null) stopRecording(true)
            mRecordingThread =
                Thread(AudioRecordThread(outputStream(file), object : OnRecorderFailedListener {
                    override fun onRecorderFailed() {
                        onAudioRecordListener?.onError(recorderError)
                        stopRecording(true)
                    }

                    override fun onRecorderStarted() {
                        onAudioRecordListener?.onRecordingStarted()
                    }
                }, source))
            mRecordingThread?.priority = Thread.NORM_PRIORITY
            mRecordingThread?.name = "AudioRecordingThread"
            mRecordingThread?.start()
        } catch (e: java.io.IOException) {
            e.toStr.logProvCrash("playSound")
        }
    }

    // Call this method from Activity onStopButton Click to s6 recording
    fun stopRecording(cancel: Boolean?) {
        if (mRecordingThread != null) {
            mRecordingThread?.interrupt()
            mRecordingThread = null
            if (file?.length() == 0L) {
                onAudioRecordListener?.onError(error)
                return
            }
            val mElapsedMillis = System.currentTimeMillis() - mStartingTimeMillis
            val calendar = java.util.Calendar.getInstance()
            calendar.timeInMillis = mElapsedMillis
            if (!(cancel ?: return)) {
                val tim = System.currentTimeMillis()
                DataKeeperItem(
                    dataText = null,
                    keeperType = KEEP_RECORD,
                    emailToSome = null,
                    emailSubject = null,
                    timeData = tim,
                    recordPath = (file ?: return).absolutePath,
                    recordLength = mElapsedMillis,
                    imageUrl = null,
                    dayNum = tim.fetchCalenderDay,
                    isDone = false,
                ).let {
                    onAudioRecordListener?.onRecordFinished(it)
                }
            } else {
                onAudioRecordListener?.onRecordFinished(null)
                deleteFile()
            }
        }
    }

    private fun deleteFile() {
        file?.exists()
    }

    private fun outputStream(file: FileLate?): java.io.OutputStream {
        if (file == null) {
            throw RuntimeException("file is null !")
        }
        return try {
            java.io.FileOutputStream(file)
        } catch (e: java.io.FileNotFoundException) {
            throw RuntimeException(
                "could not build OutputStream from" + " this file " + file.name, e
            )
        }
    }
}