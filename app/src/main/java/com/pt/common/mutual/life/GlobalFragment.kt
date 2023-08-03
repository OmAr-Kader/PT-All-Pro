package com.pt.common.mutual.life

import com.pt.common.global.commentView

abstract class GlobalFragment<VB : androidx.viewbinding.ViewBinding> : GlobalSimpleFragment<VB>(),
    android.view.View.OnLongClickListener {

    override fun onLongClick(p0: android.view.View?): Boolean {
        return binding.onLongClick(p0 ?: return false)
    }

    abstract fun VB.onLongClick(v: android.view.View): Boolean

    protected fun android.view.View.popUpComment(
        @androidx.annotation.StringRes des: Int,
        @androidx.annotation.AttrRes color: Int,
        iy: Int,
    ) {
        with(
            ctx.commentView(color, des.dStr)
        ) {
            android.widget.PopupWindow(
                this@with,
                com.pt.common.stable.WRAP,
                com.pt.common.stable.WRAP,
                false
            ).run {
                this@run.isOutsideTouchable = true
                this@run.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                if (iy == 0) {
                    this@run.showAsDropDown(this@popUpComment)
                } else {
                    this@run.showAsDropDown(this@popUpComment, 0, iy)
                }
            }
        }
    }

}