package com.pt.common.moderator.over

open class EditTextBackEvent @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = android.R.attr.editTextStyle,
) : androidx.appcompat.widget.AppCompatEditText(context!!, attrs, defStyle) {

    @Volatile
    private var mOnImeBack: (() -> Unit)? = null

    override fun onKeyPreIme(keyCode: Int, event: android.view.KeyEvent): Boolean {
        if (event.keyCode == android.view.KeyEvent.KEYCODE_BACK &&
            event.action == android.view.KeyEvent.ACTION_UP
        ) {
            mOnImeBack?.invoke()
        }
        return super.dispatchKeyEvent(event)
    }

    fun (() -> Unit)?.setOnEditTextImeBackListener() {
        mOnImeBack = this
    }


    fun onViewDestroy() {
        mOnImeBack = null
        setOnTouchListener(null)
    }

}