package com.pt.common.moderator.over

open class LinearTint @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = 0,
) : androidx.appcompat.widget.LinearLayoutCompat(context!!, attrs, defStyle) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
            backgroundTintList?.let { csl ->
                csl.defaultColor.let {
                    background?.colorFilter = android.graphics.PorterDuffColorFilter(
                        it,
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                }
            }
        }
    }
}