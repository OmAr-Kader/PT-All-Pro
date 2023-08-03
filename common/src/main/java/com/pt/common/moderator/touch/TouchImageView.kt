package com.pt.common.moderator.touch

import android.view.MotionEvent
import com.pt.common.global.isV_M

open class TouchImageView @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = android.R.attr.previewImage,
) : androidx.appcompat.widget.AppCompatImageView(context!!, attrs, defStyle) {

    private var senseListener: ContactListener? = null
    private var reTurn: Boolean = true
    private var lastEvent: MotionEvent? = null

    fun ContactListener.setContactListener(reT: Boolean) {
        senseListener = this@setContactListener
        reTurn = reT
        setOnTouchListener(touch)
    }

    private var touch: OnTouchListener? = OnTouchListener { p0, p1 ->
        p1.apply {
            if (action == MotionEvent.ACTION_UP ||
                actionMasked == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_POINTER_UP ||
                actionMasked == MotionEvent.ACTION_POINTER_UP ||
                (isV_M && actionButton == MotionEvent.ACTION_BUTTON_RELEASE) ||
                action == MotionEvent.ACTION_CANCEL ||
                actionMasked == MotionEvent.ACTION_CANCEL
            ) {
                lastEvent = p1
                p0.performClick()
                return@OnTouchListener true
            } else if (action == MotionEvent.ACTION_DOWN ||
                actionMasked == MotionEvent.ACTION_DOWN
            ) {
                senseListener?.onDown(p0, p1)
            } else if (action == MotionEvent.ACTION_MOVE ||
                actionMasked == MotionEvent.ACTION_MOVE
            ) {
                senseListener?.onMove(p0, p1)
            }
        }
        return@OnTouchListener reTurn
    }

    override fun performClick(): Boolean {
        super.performClick()
        handler.post {
            senseListener?.apply {
                senseListener?.onUp(this@TouchImageView)
                lastEvent?.let { senseListener?.onUp(this@TouchImageView, it) }
            }
        }
        return true
    }

    fun onViewDestroy() {
        senseListener = null
        reTurn = true
        setOnTouchListener(null)
        touch = null
    }

}