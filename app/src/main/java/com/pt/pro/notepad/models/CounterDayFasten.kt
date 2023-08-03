package com.pt.pro.notepad.models

import com.pt.common.global.*

data class CounterDayFasten(
    val root_: android.widget.FrameLayout,
    val daysButton: androidx.appcompat.widget.AppCompatButton,
) : androidx.viewbinding.ViewBinding {

    companion object {

        internal var dis3Native: Int? = null
        internal var tqdNative: Float? = null

        @get:androidx.annotation.ColorInt
        internal var rmoTextNative: Int? = null

        @get:androidx.annotation.ColorInt
        internal var colorPrimaryNative: Int? = null

        @JvmStatic
        internal inline val android.content.res.Resources.dis3: Int
            get() = dis3Native ?: displayMetrics.findPixelDis(3F)
                .also { dis3Native = it }

        @JvmStatic
        internal inline val android.content.res.Resources.tqdDim: Float
            get() = tqdNative ?: getDimension(com.pt.pro.R.dimen.tqd)
                .also { tqdNative = it }

        @JvmStatic
        internal inline val android.content.Context.rmoText: Int
            @androidx.annotation.ColorInt
            get() = rmoTextNative ?: theme.findAttr(com.pt.pro.R.attr.rmoText)
                .also { rmoTextNative = it }

        @JvmStatic
        internal inline val android.content.Context.colorPrimary: Int
            @androidx.annotation.ColorInt
            get() = colorPrimaryNative ?: theme.findAttr(android.R.attr.colorPrimary)
                .also { colorPrimaryNative = it }

        @JvmStatic
        fun android.content.Context.inflaterDay(): CounterDayFasten {
            val fasten: CounterDayFasten
            android.widget.FrameLayout(this@inflaterDay).apply root_@{
                androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                    com.pt.common.stable.WRAP,
                    com.pt.common.stable.WRAP
                ).apply {
                    resources.dis3.also { d3 ->
                        marginStart = d3
                        marginEnd = d3
                        topMargin = d3
                        bottomMargin = d3
                    }
                }.also(this@root_::setLayoutParams)

                val daysButton = androidx.appcompat.widget.AppCompatButton(
                    context
                ).apply {
                    framePara(
                        com.pt.common.stable.WRAP,
                        com.pt.common.stable.WRAP
                    ) {

                    }
                    compactImage(com.pt.pro.R.drawable.item_inside_bcl) {
                        background = this@compactImage
                    }
                    backReColor(colorPrimary)
                    setTextColor(rmoText)
                    setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, resources.tqdDim)
                    isAllCaps = false
                }.also(this@root_::addView)
                fasten = CounterDayFasten(
                    root_ = this@root_,
                    daysButton = daysButton,
                )
            }
            return fasten
        }

        @JvmStatic
        fun destroyFasten() {
            dis3Native = null
            rmoTextNative = null
            colorPrimaryNative = null
            tqdNative = null
        }
    }

    override fun getRoot(): android.widget.FrameLayout = root_
}