package com.pt.common.global

import androidx.core.view.setPadding
import com.pt.common.stable.letSus

internal inline val android.view.ViewGroup?.childCountSus: suspend () -> Int
    get() = {
        com.pt.common.stable.withMainDef(0) {
            this@childCountSus?.childCount ?: 0
        }
    }

internal inline fun android.view.View.addSenseListener(
    reT: Boolean,
    @com.pt.common.global.UiAnn crossinline onSense: (
        v: android.view.View,
        event: android.view.MotionEvent,
        type: Int,
    ) -> Unit = { _, _, _ -> },
): android.view.View.OnTouchListener {
    return android.view.View.OnTouchListener { v, event ->
        event.apply {
            if (action == android.view.MotionEvent.ACTION_UP ||
                actionMasked == android.view.MotionEvent.ACTION_UP ||
                action == android.view.MotionEvent.ACTION_POINTER_UP ||
                actionMasked == android.view.MotionEvent.ACTION_POINTER_UP ||
                (isV_M && actionButton == android.view.MotionEvent.ACTION_BUTTON_RELEASE) ||
                action == android.view.MotionEvent.ACTION_CANCEL ||
                actionMasked == android.view.MotionEvent.ACTION_CANCEL
            ) {
                onSense.invoke(v, event, com.pt.common.stable.UP_SEN)
                v.performClick()
            } else if (action == android.view.MotionEvent.ACTION_DOWN ||
                actionMasked == android.view.MotionEvent.ACTION_DOWN
            ) {
                onSense.invoke(v, event, com.pt.common.stable.DOWN_SEN)
            } else if (action == android.view.MotionEvent.ACTION_MOVE ||
                actionMasked == android.view.MotionEvent.ACTION_MOVE
            ) {
                onSense.invoke(v, event, com.pt.common.stable.MOVE_SEN)
            }
        }
        return@OnTouchListener reT
    }.apply {
        setOnTouchListener(this)
    }
}

internal inline val android.view.View.fetchViewDimensions: android.util.Size
    get() {
        return IntArray(2).apply {
            getLocationOnScreen(this@apply)
        }.let {
            android.util.Size(it[0], it[1])
        }
    }

internal inline fun androidx.recyclerview.widget.RecyclerView.scrollListener(
    @com.pt.common.global.UiAnn crossinline a: () -> Unit,
) {
    object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrolled(
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            dx: Int,
            dy: Int,
        ) {
            super.onScrolled(recyclerView, dx, dy)
            a.invoke()
        }
    }.also {
        addOnScrollListener(it)
    }
}

inline val android.content.Context.getVerManager: com.pt.common.moderator.recycler.NoAnimLinearManager
    @com.pt.common.global.UiAnn
    get() {
        return com.pt.common.moderator.recycler.NoAnimLinearManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
    }

inline val android.content.Context.getManager: com.pt.common.moderator.recycler.NoAnimLinearManager
    @com.pt.common.global.UiAnn
    get() {
        return com.pt.common.moderator.recycler.NoAnimLinearManager(
            this,
            androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
            false
        )
    }

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
inline fun android.content.Context.launchKeyBoard(
    input: android.view.inputmethod.InputMethodManager.() -> Unit,
) {
    (androidx.core.content.ContextCompat.getSystemService(
        this@launchKeyBoard,
        android.view.inputmethod.InputMethodManager::class.java
    ) ?: getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? android.view.inputmethod.InputMethodManager?)?.apply(input)
}

@com.pt.common.global.UiAnn
inline fun androidx.appcompat.widget.AppCompatSeekBar.onSeekListener(
    @com.pt.common.global.UiAnn crossinline onProgressChanged: (
        progress: Int,
        stop: Int
    ) -> Unit,
) {
    object : android.widget.SeekBar.OnSeekBarChangeListener {
        override fun onStopTrackingTouch(seekBar: android.widget.SeekBar) {
            onProgressChanged.invoke(seekBar.progress, -1)
        }

        override fun onStartTrackingTouch(seekBar: android.widget.SeekBar) {
            onProgressChanged.invoke(seekBar.progress, 1)
        }

        override fun onProgressChanged(
            seekBar: android.widget.SeekBar,
            progress: Int,
            fromUser: Boolean,
        ) {
            if (fromUser) {
                onProgressChanged.invoke(seekBar.progress, 0)
            }
        }
    }.also {
        setOnSeekBarChangeListener(it)
    }
}

@com.pt.common.global.UiAnn
inline fun androidx.recyclerview.widget.RecyclerView.onScrollListener(
    @com.pt.common.global.UiAnn crossinline onScrollStateChanged: (
        recyclerView: androidx.recyclerview.widget.RecyclerView,
    ) -> Unit,
) {
    object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            newState: Int,
        ) {
            super.onScrollStateChanged(recyclerView, newState)
            onScrollStateChanged.invoke(recyclerView)

        }
    }.also {
        addOnScrollListener(it)
    }
}

inline val android.content.Context.fetchPicker: android.widget.NumberPicker
    get() {
        return android.widget.NumberPicker(
            androidx.appcompat.view.ContextThemeWrapper(
                this@fetchPicker,
                com.pt.pro.R.style.AppTheme_Picker
            ),
        ).also {
            it.framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {

            }
        }
    }

@com.pt.common.global.UiAnn
inline fun android.view.View.framePara(
    w: Int,
    h: Int = w,
    para: android.widget.FrameLayout.LayoutParams.() -> Unit,
) {
    android.widget.FrameLayout.LayoutParams(w, h).apply {
        para.invoke(this)
    }.also(this@framePara::setLayoutParams)
}

@com.pt.common.global.UiAnn
inline fun android.view.View.linearPara(
    w: Int,
    h: Int,
    we: Float = 0F,
    para: androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.() -> Unit,
) {
    androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(w, h, we).apply {
        para.invoke(this)
    }.also(this@linearPara::setLayoutParams)
}

@com.pt.common.global.UiAnn
inline fun android.view.View.constraintPara(
    w: Int,
    h: Int = w,
    para: androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.() -> Unit,
) {
    androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(w, h).apply {
        para.invoke(this)
    }.also(this@constraintPara::setLayoutParams)
}

suspend fun android.view.ViewGroup.removeAllViewsSus() {
    com.pt.common.stable.withMain {
        removeAllViews()
    }
}

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
inline fun android.content.Context.fetchScrollView(
    margin: Int = 0,
    @UiAnn txt: androidx.appcompat.widget.AppCompatTextView.() -> Unit,
) {
    androidx.appcompat.widget.AppCompatTextView(this).apply {
        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
        textAlignment = gravity
        textSize = 18F
        androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
            androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT
        ).apply {
            marginStart = margin
            marginEnd = margin
        }.also(::setLayoutParams)
    }.also(txt)
}

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
fun android.content.Context.fetchScrollViewReturn(
    margin: Int = 0,
): androidx.appcompat.widget.AppCompatTextView {
    return androidx.appcompat.widget.AppCompatTextView(this).apply {
        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
        textAlignment = gravity
        textSize = 18F
        androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
            androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT
        ).apply {
            marginStart = margin
            marginEnd = margin
        }.also(::setLayoutParams)
    }
}

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
suspend fun android.content.Context.fetchScrollViewContact(
    txt: String,
    color: Int,
    margin: Int,
): androidx.appcompat.widget.AppCompatTextView = com.pt.common.stable.justCoroutine {
    androidx.appcompat.widget.AppCompatTextView(this@fetchScrollViewContact).apply {
        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
        textAlignment = gravity
        textSize = 16F
        text = txt
        setTextColor(color)
        background = context?.compactImageReturn?.invoke(com.pt.pro.R.drawable.ripple_cur)
        androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
            androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT
        ).apply {
            marginStart = margin * 2
            bottomMargin = margin
            topMargin = margin
            setPadding(margin)
        }.also(::setLayoutParams)
    }
}

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
internal suspend inline fun android.content.Context.fetchLicenseText(
    @androidx.annotation.DimenRes textSizeRec: Int = com.pt.pro.R.dimen.tvs,
    @androidx.annotation.AttrRes textColorRec: Int = com.pt.pro.R.attr.rmoGrey,
    @UiAnn crossinline txt: suspend androidx.appcompat.widget.AppCompatTextView.() -> Unit,
) {
    com.pt.common.stable.withMain {
        androidx.appcompat.widget.AppCompatTextView(this@fetchLicenseText).apply {
            setTextSize(
                android.util.TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(textSizeRec)
            )
            setTextColor(theme.findAttr(textColorRec))
            isClickable = true
            isFocusable = true
            context.compactImage(com.pt.pro.R.drawable.ripple_rectangle) {
                background = this
            }
            layoutParams = androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
                androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            )
            minimumHeight = findPixel(48)
        }.also {
            txt(it)
        }
    }
}

@com.pt.common.global.UiAnn
internal suspend inline fun android.view.ViewGroup.addViewSus(
    @com.pt.common.global.UiAnn crossinline txt: suspend Unit.() -> android.view.View?,
) {
    com.pt.common.stable.withMain {
        txt(Unit)?.letSus { this@addViewSus.addView(it) }
    }
}

@com.pt.common.global.UiAnn
internal suspend inline fun androidx.appcompat.widget.AppCompatTextView.setTextSus(
    @androidx.annotation.StringRes txt: Int,
) {
    com.pt.common.stable.withMain {
        this@setTextSus.setText(txt)
    }
}

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
inline fun android.content.Context.fetchMyEditText(
    edit: androidx.appcompat.widget.AppCompatEditText.() -> Unit,
) {
    androidx.appcompat.widget.AppCompatEditText(this).apply {
        inputType = android.text.InputType.TYPE_CLASS_TEXT
        android.text.InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Character.isLetter(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }.let {
            filters = arrayOf(it)
        }
        setHintTextColor(android.graphics.Color.GRAY)
        hint = resources.getString(com.pt.pro.R.string.za)
        layoutParams = androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
            androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            androidx.appcompat.widget.LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
        minimumHeight = findPixel(48)
    }.also(edit)
}

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
internal inline fun android.content.Context.fetchImageView(
    edit: androidx.appcompat.widget.AppCompatImageView.() -> Unit,
) {
    androidx.appcompat.widget.AppCompatImageView(this).apply {
        resources.displayMetrics.run {
            layoutParams = android.widget.FrameLayout.LayoutParams(
                com.pt.common.stable.MATCH,
                com.pt.common.stable.MATCH,
            )
        }
    }.also(edit)
}

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
inline fun android.content.Context.compactImage(
    r: Int,
    drawable: android.graphics.drawable.Drawable?.() -> Unit,
) {
    androidx.appcompat.content.res.AppCompatResources.getDrawable(this, r)?.let {
        androidx.core.graphics.drawable.DrawableCompat.wrap(it)
    }.also(drawable)
}

inline val android.content.Context.compactImageReturn: (Int) -> android.graphics.drawable.Drawable?
    @com.pt.common.global.UiAnn
    get() = {
        androidx.appcompat.content.res.AppCompatResources.getDrawable(this, it)?.also { d ->
            androidx.core.graphics.drawable.DrawableCompat.wrap(d)
        }
    }

inline val android.content.Context.compactImageReturnTint: (Int, Int) -> android.graphics.drawable.Drawable?
    @com.pt.common.global.UiAnn
    get() = { rec, col ->
        androidx.appcompat.content.res.AppCompatResources.getDrawable(this, rec)?.also { d ->
            androidx.core.graphics.drawable.DrawableCompat.wrap(d)
        }.apply {
            this?.let { androidx.core.graphics.drawable.DrawableCompat.setTint(it, col) }
        }
    }

@com.pt.common.global.UiAnn
fun android.view.View.applyEdit(@androidx.annotation.ColorInt color: Int) {
    androidx.core.graphics.BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
        color,
        androidx.core.graphics.BlendModeCompat.SRC_IN
    ).let {
        background.mutate().colorFilter = it
    }
}

@com.pt.common.global.UiAnn
fun android.widget.TextView.editAppearance() {
    @Suppress("DEPRECATION")
    if (isV_M) {
        setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Title)
    } else {
        setTextAppearance(
            context,
            android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Title
        )
    }
}

@com.pt.common.global.UiAnn
fun android.widget.TextView.editAppearanceLargePopupMenu() {
    @Suppress("DEPRECATION")
    if (isV_M) {
        setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Widget_PopupMenu_Large)
    } else {
        setTextAppearance(
            context,
            android.R.style.TextAppearance_DeviceDefault_Widget_PopupMenu_Large
        )
    }
}

@com.pt.common.global.UiAnn
fun android.widget.TextView.editAppearanceLarge() {
    @Suppress("DEPRECATION")
    if (isV_M) {
        setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Large)
    } else {
        setTextAppearance(
            context,
            android.R.style.TextAppearance_DeviceDefault_Large
        )
    }
}

@com.pt.common.global.UiAnn
fun android.widget.TextView.subtitleAppearance() {
    @Suppress("DEPRECATION")
    if (isV_M) {
        setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Subtitle)
    } else {
        setTextAppearance(
            context,
            android.R.style.TextAppearance_DeviceDefault_Widget_ActionBar_Subtitle
        )
    }
}

@com.pt.common.global.UiAnn
fun androidx.appcompat.widget.AppCompatTextView.appearanceLikeButton() {
    @Suppress("DEPRECATION")
    if (isV_M) {
        setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Widget_Button)
    } else {
        setTextAppearance(
            context,
            android.R.style.TextAppearance_DeviceDefault_Widget_Button
        )
    }
}

@com.pt.common.global.UiAnn
fun android.widget.TextView.singleNonScroll() {
    @Suppress("DEPRECATION")
    isHorizontalScrollBarEnabled = false
    maxLines = 1
    isHorizontalScrollBarEnabled = false
    isNestedScrollingEnabled = false
    isVerticalScrollBarEnabled = false
    isSingleLine = true
}

@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
fun android.content.Context.makeToast(
    tstTxt: Any?,
    @androidx.annotation.IntRange(from = 0, to = 1) length: Int,
) {
    if (isV_R) {
        android.widget.Toast.makeText(this, tstTxt.toString(), length).apply {
            show()
        }
    } else {
        android.widget.Toast.makeText(this, tstTxt.toString(), length).apply {
            toastCard(
                theme.findAttr(com.pt.pro.R.attr.rmoBackHint),
                theme.findAttr(com.pt.pro.R.attr.rmoText)
            )
            runCatching {
                setGravity(android.view.Gravity.CENTER, 0, 100)
            }
            show()
        }
    }
}

@androidx.annotation.UiContext
suspend fun android.content.Context.makeToastSus(
    tstTxt: String,
    @androidx.annotation.IntRange(from = 0, to = 1) length: Int,
) {
    com.pt.common.stable.withMain {
        if (isV_R) {
            android.widget.Toast.makeText(this@makeToastSus, tstTxt, length).apply {
                show()
            }
        } else {
            android.widget.Toast.makeText(this@makeToastSus, tstTxt, length).apply {
                toastCard(
                    theme.findAttr(com.pt.pro.R.attr.rmoBackHint),
                    theme.findAttr(com.pt.pro.R.attr.rmoText)
                )
                runCatching {
                    setGravity(android.view.Gravity.CENTER, 0, 100)
                }
                show()
            }
        }
    }
}


@android.annotation.SuppressLint("ShowToast")
@androidx.annotation.UiContext
suspend fun android.content.Context.makeToastRecSus(
    @androidx.annotation.StringRes rec: Int,
    @androidx.annotation.IntRange(from = 0, to = 1) length: Int,
) {
    com.pt.common.stable.withMain {
        if (isV_R) {
            android.widget.Toast.makeText(
                this@makeToastRecSus,
                resources.getString(rec),
                length
            ).apply {
                show()
            }
        } else {
            android.widget.Toast.makeText(
                this@makeToastRecSus,
                resources.getString(rec),
                length
            ).apply {
                toastCard(
                    theme.findAttr(com.pt.pro.R.attr.rmoBackHint),
                    theme.findAttr(com.pt.pro.R.attr.rmoText)
                )
                setGravity(android.view.Gravity.CENTER, 0, 100)
                show()
            }
        }
    }
}

@android.annotation.SuppressLint("ShowToast")
@com.pt.common.global.UiAnn
@androidx.annotation.UiContext
fun android.content.Context.makeToastRec(
    @androidx.annotation.StringRes rec: Int,
    @androidx.annotation.IntRange(from = 0, to = 1) length: Int,
) {
    if (isV_R) {
        android.widget.Toast.makeText(
            this,
            resources.getString(rec),
            length
        ).apply {
            show()
        }
    } else {
        android.widget.Toast.makeText(this, resources.getString(rec), length).apply {
            toastCard(
                theme.findAttr(com.pt.pro.R.attr.rmoBackHint),
                theme.findAttr(com.pt.pro.R.attr.rmoText)
            )
            setGravity(android.view.Gravity.CENTER, 0, 100)
            show()
        }
    }
}

@Suppress("DEPRECATION")
@com.pt.common.global.UiAnn
fun android.widget.Toast.toastCard(txtColor: Int, backColor: Int) {
    view?.apply {
        androidx.core.graphics.BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            backColor,
            androidx.core.graphics.BlendModeCompat.SRC_IN
        ).let {
            background.colorFilter = it
        }
        findViewById<android.widget.TextView>(android.R.id.message).run {
            setTextColor(txtColor)
        }
    }
}


fun android.content.Context.commentView(
    @androidx.annotation.AttrRes color: Int,
    str: String,
): androidx.appcompat.widget.AppCompatTextView {
    return androidx.appcompat.widget.AppCompatTextView(
        this@commentView
    ).apply {
        compactImage(com.pt.pro.R.drawable.item_outside_bum) {
            background = this@compactImage
        }
        backReColor(theme.findAttr(color))
        findPixel(10).also { d10 ->
            findPixel(10).also { d15 ->
                setPadding(d10, d15, d10, d15)
            }
        }
        maxWidth = findPixel(140)
        setTextColor(theme.findAttr(com.pt.pro.R.attr.rmoText))
        text = str
    }
}


fun android.content.Context.mainFrameSetting(
    d20: Int, // findPixel(20)
    d15: Int, // findPixel(15)
    d5: Int, // findPixel(5)
    d30: Int, // findPixel(30)
    txtDim: Int, // resources.getDimension(com.pt.pro.R.dimen.txt).toInt()
    tedDim: Float, // resources.getDimension(com.pt.pro.R.dimen.ted)
    @androidx.annotation.StringRes str: Int, // com.pt.pro.R.string.vt
    @androidx.annotation.ColorInt accent: Int, // theme.findAttr(android.R.attr.colorAccent)
    @androidx.annotation.ColorInt rmoText: Int, // theme.findAttr(com.pt.pro.R.attr.rmoText)
): DSackT<android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView> {
    return android.widget.FrameLayout(
        this@mainFrameSetting
    ).run mainFrame@{
        linearPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F) {
            marginStart = d20
        }
        compactImage(com.pt.pro.R.drawable.ripple_cur) {
            background = this@compactImage
        }
        minimumHeight = findPixel(48)
        setPadding(d15, 0, d15, 0)
        val image = androidx.appcompat.widget.AppCompatImageView(
            context
        ).apply {
            framePara(txtDim, txtDim) {
                gravity = android.view.Gravity.START or
                        android.view.Gravity.CENTER_VERTICAL
            }
            compactImage(com.pt.pro.R.drawable.ic_down_arrow) {
                setImageDrawable(this@compactImage)
            }
            svgReColor(accent)
        }.also(this@mainFrame::addView)
        androidx.appcompat.widget.AppCompatTextView(
            context
        ).apply {
            framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                marginStart = d30
                topMargin = d5
                bottomMargin = d5
                gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
            }
            text = resources.getString(str)
            isAllCaps = false
            editAppearanceLarge()
            setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, tedDim)
            setTextColor(rmoText)
        }.also(this@mainFrame::addView)
        DSackT(this@mainFrame, image)
    }
}

fun android.content.Context.mainSettingFrame(
    d50: Int,
): DSackT<android.widget.FrameLayout, android.widget.FrameLayout> {
    return android.widget.FrameLayout(
        this@mainSettingFrame
    ).run frameOne@{
        linearPara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP, 0F) {
            marginStart = d50
            marginEnd = d50
        }
        val frameTwo = android.widget.FrameLayout(
            context
        ).apply {
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            justGone()
        }.also(this@frameOne::addView)
        DSackT(this@frameOne, frameTwo)
    }
}

fun android.content.Context.checkSettingBox(
    d5: Int,
    d30: Int,
    d48: Int,
    d80: Int,
    txtDim: Float, // resources.getDimension(com.pt.pro.R.dimen.txt)
    tggDim: Int, // resources.getDimension(com.pt.pro.R.dimen.tgg).toInt()
    @androidx.annotation.StringRes str: Int, // com.pt.pro.R.string.vt
    @androidx.annotation.ColorInt rmoText: Int, // theme.findAttr(com.pt.pro.R.attr.rmoText)
): DSackT<android.widget.FrameLayout, androidx.appcompat.widget.AppCompatCheckBox> {
    return android.widget.FrameLayout(
        this@checkSettingBox
    ).run frameOne@{
        linearPara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP, 0F) {
            topMargin = d5
            bottomMargin = d5
        }
        findPixel(48).also {
            minimumHeight = it
            minimumWidth = it
        }
        androidx.appcompat.widget.AppCompatTextView(
            context
        ).apply {
            framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                marginStart = d30
                gravity = android.view.Gravity.CENTER_VERTICAL
                marginEnd = d80
            }
            text = resources.getString(str)
            isAllCaps = false
            editAppearanceLarge()
            setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, txtDim)
            setTextColor(rmoText)
        }.also(this@frameOne::addView)
        val check = androidx.appcompat.widget.AppCompatCheckBox(
            context
        ).apply {
            framePara(kotlin.math.max(d48, tggDim), kotlin.math.max(d48, tggDim)) {
                gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                marginEnd = d30
            }
        }.also(this@frameOne::addView)
        DSackT(this@frameOne, check)
    }
}