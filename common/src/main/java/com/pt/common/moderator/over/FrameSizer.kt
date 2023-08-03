package com.pt.common.moderator.over

class FrameSizer @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = 0,
) : android.widget.FrameLayout(context!!, attrs, defStyle) {

    @Volatile
    private var widthForEnsure: Int? = null

    @Volatile
    private var heightForEnsure: Int? = null

    @Volatile
    private var widthX: Int? = null

    @Volatile
    private var heightX: Int? = null

    @Volatile
    private var run: ((rootWidth: Int, rootHeight: Int) -> Unit)? = null

    private inline val android.view.View.isValidDim: Boolean
        get() = arrayOf(0, -1, -2).let {
            !arrayOf(0, -1, -2, widthForEnsure).contains(measuredWidth) &&
                    !arrayOf(0, -1, -2, heightForEnsure).contains(measuredHeight)
        }

    private inline val android.view.View.isValidDimNormal: Boolean
        get() = arrayOf(0, -1, -2).let {
            !arrayOf(0, -1, -2).contains(measuredWidth) &&
                    !arrayOf(0, -1, -2).contains(measuredHeight)
        }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (isValidDim) {
            com.pt.common.stable.catchyUnit {
                if (run != null) {
                    run?.invoke(measuredWidth, measuredHeight)
                    widthX = null
                    heightX = null
                    handler?.apply {
                        removeCallbacksAndMessages(null)
                        postDelayed({ run = null }, 2000L)
                    } ?: run<Unit> {
                        run = null
                    }
                } else {
                    widthX = measuredWidth
                    heightX = measuredHeight
                }
            }
        }
        widthForEnsure = w
        heightForEnsure = h
    }

    fun fetchViewDim(a: (rootWidth: Int, rootHeight: Int) -> Unit) {
        if (isValidDimNormal) {
            a.invoke(measuredWidth, measuredHeight)
            run = a
            handler?.apply {
                removeCallbacksAndMessages(null)
                postDelayed({ run = null }, 2000L)
            } ?: run<Unit> {
                run = null
            }
        } else {
            if (widthX != null && heightX != null) {
                widthX?.let { one ->
                    heightX?.let { two ->
                        a.invoke(one, two)
                        widthX = null
                        heightX = null
                        run = null
                    }
                }
            } else {
                run = a
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        widthX = null
        heightX = null
    }

}