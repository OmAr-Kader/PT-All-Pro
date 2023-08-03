package com.pt.pro.notepad.recorder

import android.os.Handler
import android.os.Looper

open class EditMessage @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = android.R.attr.editTextStyle,
) : androidx.appcompat.widget.AppCompatEditText(context!!, attrs, defStyle) {

    private var keyboardListener: KeyboardListener? = null
    private var mTypingHandler: Handler? = Handler(Looper.getMainLooper())
    private var isUserTyping = false

    private val mTypingTimeout: () -> Unit by lazy {
        return@lazy {
            com.pt.common.stable.catchy(Unit) {
                com.pt.common.stable.catchy(Unit) {
                    if (isUserTyping && keyboardListener != null) {
                        keyboardListener?.onTypingStopped()
                        isUserTyping = false
                    }
                }
            }
        }
    }

    fun setKeyboardListener(listener: KeyboardListener?) {
        keyboardListener = listener
        if (listener != null) {
            isUserTyping = false
        }
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        if (mTypingHandler != null && keyboardListener != null) {
            mTypingHandler?.removeCallbacksAndMessages(null)
            mTypingHandler?.postDelayed(mTypingTimeout, com.pt.pro.notepad.objects.TYPING_TIMEOUT)
            val length = text.toString().length
            if (!isUserTyping && length > 0) {
                isUserTyping = true
                keyboardListener?.onTypingStarted()
                //mTypingHandler!!.postDelayed(mTypingResend, 0)
            } else if (isUserTyping && length == 0) {
                isUserTyping = false
                keyboardListener?.onTextDeleted()
            }
        }
    }

    interface KeyboardListener {

        fun onTypingStarted()

        fun onTypingStopped() {}

        fun onTextDeleted()
    }

}
