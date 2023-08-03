package com.pt.common.moderator.over

open class ResizeImageView @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = android.R.attr.previewImage,
) : androidx.appcompat.widget.AppCompatImageView(context!!, attrs, defStyle) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec)
    }

}