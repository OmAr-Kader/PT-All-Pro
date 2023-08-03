package com.pt.pro.notepad.recorder

import android.view.MotionEvent
import android.view.View
import com.pt.common.global.*
import com.pt.pro.R
import com.pt.pro.notepad.interfaces.OnAudioRecordListener

open class RecordButton @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = android.R.attr.previewImage,
) : androidx.appcompat.widget.AppCompatImageView(context!!, attrs, defStyle),
    View.OnTouchListener, View.OnClickListener {

    private var recordHelper: RecordHelper? = null

    private var onRecordClickListener: ((v: View?) -> Unit)? = null

    private var listenForRecord = true
    private var isTextMessage = false

    init {
        init(context)
    }

    fun changeToMessage(flag: Boolean) {
        isTextMessage = flag
        setListenForRecord(!flag)
        context.compactImage(if (flag) R.drawable.ic_send else R.drawable.ic_mic_black) {
            setImageDrawable(this)
        }
    }


    /*fun isTextMessage(): Boolean {
        return isTextMessage
    }


    fun setRecordHelper(recordHelper: RecordHelper?) {
        this.recordHelper = recordHelper
    }*/

    private fun init(context: android.content.Context?) {
        recordHelper = RecordHelper(context ?: return)
        setOnTouchListener(this)
        setOnClickListener(this)
    }

    fun setDirectoryAudio(audioDirectory: FileLate) {
        recordHelper?.setAudioDirectory(audioDirectory)
    }

    fun setOnListenerRecord(recordListener: OnAudioRecordListener?) {
        recordHelper?.setOnRecordListener(recordListener)
    }

    @Suppress("HardCodedStringLiteral")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (isListenForRecord()) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    v?.backgroundTintList = context.theme.findAttr(
                        android.R.attr.colorPrimary
                    ).toTintList
                    v?.context?.vibrationProvider(400L)
                    recordHelper!!.run { (v as RecordButton).onActionDown() }
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    v?.backReColor(v.context.theme.findAttr(R.attr.rmoBackHint))
                    v?.context?.vibrationProvider(200L)
                    recordHelper!!.onActionUp(v as RecordButton)
                }
                else -> return false
            }
        }
        return isListenForRecord()
    }

    private fun setListenForRecord(listenForRecord: Boolean) {
        this.listenForRecord = listenForRecord
    }

    private fun isListenForRecord(): Boolean {
        return listenForRecord
    }

    fun setOnRecordClickListener(onRecordClickListener: ((v: View?) -> Unit)?) {
        this.onRecordClickListener = onRecordClickListener
    }

    override fun onClick(v: View?) {
        onRecordClickListener?.invoke(v)
    }

}