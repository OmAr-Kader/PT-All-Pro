package com.pt.common.moderator.over

open class ScalelessButton @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = android.R.attr.buttonStyle,
) : androidx.appcompat.widget.AppCompatButton(context!!, attrs, defStyle) {

    private var scaleless: Float = 1.0F

    init {
        com.pt.common.stable.catchyUnit {
            context?.resources?.configuration.apply {
                this?.fontScale?.let {
                    if (it != 1.0F) {
                        scaleless = it
                        setTextSize(
                            android.util.TypedValue.COMPLEX_UNIT_PX,
                            textSize / it
                        )
                    }
                }
            }
        }
    }

    /*fun setScalelessTextSize(txtSize: Float) {
        textSize = txtSize / scaleless
    }*/

}