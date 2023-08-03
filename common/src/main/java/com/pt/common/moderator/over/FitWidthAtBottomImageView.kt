package com.pt.common.moderator.over

import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent
import com.pt.common.global.isV_M
import com.pt.common.moderator.touch.ContactListener

open class FitWidthAtBottomImageView @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = android.R.attr.previewImage,
) : androidx.appcompat.widget.AppCompatImageView(context!!, attrs, defStyle),
    android.view.View.OnTouchListener {

    private var senseListener: ContactListener? = null
    private var reTurn: Boolean = true
    private var lastEvent: MotionEvent? = null

    fun ContactListener.setContactListener(reT: Boolean) {
        senseListener = this@setContactListener
        reTurn = reT
        setOnTouchListener(this@FitWidthAtBottomImageView)
    }

    override fun onTouch(v: android.view.View?, event: MotionEvent?): Boolean {
        (v ?: return false)
        (event ?: return false)
        event.apply {
            if (action == MotionEvent.ACTION_UP ||
                actionMasked == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_POINTER_UP ||
                actionMasked == MotionEvent.ACTION_POINTER_UP ||
                (isV_M && actionButton == MotionEvent.ACTION_BUTTON_RELEASE) ||
                action == MotionEvent.ACTION_CANCEL ||
                actionMasked == MotionEvent.ACTION_CANCEL
            ) {
                lastEvent = event
                v.performClick()
                return true
            } else if (action == MotionEvent.ACTION_DOWN ||
                actionMasked == MotionEvent.ACTION_DOWN
            ) {
                senseListener?.onDown(v, event)
            } else if (action == MotionEvent.ACTION_MOVE ||
                actionMasked == MotionEvent.ACTION_MOVE
            ) {
                senseListener?.onMove(v, event)
            }
        }
        return reTurn
    }

    @android.annotation.SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        drawable?.apply {
            android.graphics.Matrix().also { mtx ->
                RectF(
                    0F, 0F, this@apply.intrinsicWidth.toFloat(),
                    this@apply.intrinsicHeight.toFloat()
                ).let { src ->
                    RectF(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat()).also { dst ->
                        mtx.setRectToRect(src, dst, android.graphics.Matrix.ScaleToFit.CENTER)
                        floatArrayOf(
                            0F, this@apply.intrinsicHeight
                                .toFloat()
                        ).run {
                            mtx.mapPoints(this@run)
                            mtx.postTranslate(0F, measuredHeight - this@run[1])
                            imageMatrix = mtx
                        }
                    }
                }
            }
        }
        super.onDraw(canvas)
    }

    fun onViewDestroy() {
        senseListener = null
        reTurn = true
        setOnTouchListener(null)
    }

    override fun performClick(): Boolean {
        super.performClick()
        senseListener?.onUp(this@FitWidthAtBottomImageView)
        lastEvent?.let { senseListener?.onUp(this@FitWidthAtBottomImageView, it) }
        return true
    }

}