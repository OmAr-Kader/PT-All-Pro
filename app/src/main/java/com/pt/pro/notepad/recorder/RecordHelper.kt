package com.pt.pro.notepad.recorder

import android.animation.ValueAnimator
import android.content.Context
import android.media.AudioManager
import android.view.animation.AccelerateDecelerateInterpolator
import com.pt.common.global.*
import com.pt.pro.notepad.interfaces.OnAudioRecordListener
import com.pt.pro.notepad.models.DataKeeperItem
import kotlin.random.Random


class RecordHelper(ctx: Context) {

    private var audioManager: AudioManager? = null

    private var initialX = 0f
    private var startTime: Long = 0

    private var recordListener: OnAudioRecordListener? = null
    private var isSwiped = false

    private var mAudioRecording: AudioRecording? = null
    private var audioDirectory: FileLate? = null
    private var isRecordingStarted = false

    init {
        audioManager = ctx.getSystemService(Context.AUDIO_SERVICE) as? AudioManager?
        mAudioRecording = AudioRecording()
    }

    private fun RecordButton.moveImageToBack() {
        ValueAnimator.ofFloat(x, initialX).apply {
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation: ValueAnimator ->
                x = animation.animatedValue as Float
            }
            duration = 0
            start()
        }
        //stopScale()
    }

    private fun isLessThanOneSecond(time: Long): Boolean {
        return time <= 1000
    }

    fun RecordButton.onActionDown() {
        handler?.post {
            startRecord()
            startTime = System.currentTimeMillis()
            mAudioRecording?.startRecording(soundSource)
            audioManager.runCatching {
                request(audioListener)
            }
            initialX = x
            isSwiped = false
        }
    }

    fun onActionUp(recordBtn: RecordButton?) {
        if (isLessThanOneSecond((System.currentTimeMillis() - startTime)) && !isSwiped) {
            stopRecord(true)
        } else {
            if (recordListener != null && !isSwiped) {
                stopRecord(false)
            }
        }
        recordBtn?.moveImageToBack()
    }


    fun setOnRecordListener(recordListener: OnAudioRecordListener?) {
        this.recordListener = recordListener
    }

    private fun RecordButton.startRecord() {
        if (recordListener != null) {
            mAudioRecording?.setOnAudioRecordListener(onRecordListener)

            //MediaRecorder.OutputFormat.M PEG_4
            Random.nextInt(1000).let {
                (audioDirectory.toString() + "/" + System.currentTimeMillis() + it + ".m4a")
            }.let {
                mAudioRecording?.setFile(it)
            }
        }
    }


    private val RecordButton.onRecordListener: OnAudioRecordListener
        get() = object : OnAudioRecordListener {
            override fun onRecordFinished(dataKeeperItem: DataKeeperItem?) {
                isRecordingStarted = false
                recordListener?.onRecordFinished(dataKeeperItem)
            }

            override fun onError(errorCode: Int) {
                recordListener?.onError(errorCode)
                stopRecordingResetViews(this@onRecordListener)
            }

            override fun onRecordingStarted() {
                isRecordingStarted = true
                recordListener?.onRecordingStarted()
            }
        }

    private fun stopRecord(cancel: Boolean) {
        if (recordListener != null && mAudioRecording != null) {
            mAudioRecording?.stopRecording(cancel)
            isRecordingStarted = false
        }
    }

    fun stopRecordingResetViews(button: RecordButton) {
        button.handler.post {
            button.setBackgroundResource(0)
            stopRecord(true)
            button.context.vibrationProvider(200L)
            button.moveImageToBack()
        }
    }

    private var audioListener: AudioManager.OnAudioFocusChangeListener? =
        AudioManager.OnAudioFocusChangeListener {

        }


    fun setAudioDirectory(audioDirectory: FileLate) {
        this.audioDirectory = audioDirectory
    }

}
