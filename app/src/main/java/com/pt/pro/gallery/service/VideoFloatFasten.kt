package com.pt.pro.gallery.service

import com.pt.common.global.*

data class VideoFloatFasten(
    val root_: com.pt.common.moderator.touch.TouchFrameLayout,
    val floatingVideo: android.view.SurfaceView,
    val constraintButtons: androidx.constraintlayout.widget.ConstraintLayout,
    val playFloating: androidx.appcompat.widget.AppCompatImageView,
    val previous: androidx.appcompat.widget.AppCompatImageView,
    val next: androidx.appcompat.widget.AppCompatImageView,
    val upperFrame: android.widget.FrameLayout,
    val scaled: androidx.appcompat.widget.AppCompatImageView,
    val close: androidx.appcompat.widget.AppCompatImageView,
    val durationLinear: androidx.appcompat.widget.LinearLayoutCompat,
    val currentDurationFloating: android.widget.Chronometer,
    val durationFloating: androidx.appcompat.widget.AppCompatTextView,
    val seekBarFloating: androidx.appcompat.widget.AppCompatSeekBar,
) : androidx.viewbinding.ViewBinding {

    companion object {
        @JvmStatic
        fun android.content.Context.inflater(
            d50: Int,
        ): VideoFloatFasten {
            val fasten: VideoFloatFasten
            com.pt.common.moderator.touch.TouchFrameLayout(this@inflater).apply root_@{
                android.view.WindowManager.LayoutParams(
                    com.pt.common.stable.WRAP,
                    com.pt.common.stable.WRAP
                ).also(this@root_::setLayoutParams)
                val floatingVideo = android.view.SurfaceView(
                    context
                ).apply {
                    framePara(findPixel(200), findPixel(150)) {
                        gravity = android.view.Gravity.CENTER
                    }
                    isClickable = false
                    isFocusable = false
                }.also(this@root_::addView)
                androidx.constraintlayout.widget.ConstraintLayout(
                    context
                ).apply constraintButtons@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                        gravity = android.view.Gravity.CENTER_VERTICAL
                    }
                    val playFloating = androidx.appcompat.widget.AppCompatImageView(
                        context
                    ).apply {
                        id = com.pt.pro.R.id.searchButton
                        constraintPara(d50, d50) {
                            bottomToBottom = 0
                            topToTop = 0
                            startToStart = 0
                            endToEnd = 0
                        }
                        compactImage(com.pt.pro.R.drawable.ic_play) {
                            setImageDrawable(this@compactImage)
                        }
                    }.also(this@constraintButtons::addView)
                    val previous = androidx.appcompat.widget.AppCompatImageView(
                        context
                    ).apply {
                        constraintPara(d50, d50) {
                            com.pt.pro.R.id.searchButton.let { s ->
                                bottomToBottom = s
                                topToTop = s
                                startToStart = 0
                                endToStart = s
                            }
                        }
                        findPixel(10).also {
                            setPadding(it, it, it, it)
                        }
                        compactImage(com.pt.pro.R.drawable.ic_previous) {
                            setImageDrawable(this@compactImage)
                        }
                        justGone()
                    }.also(this@constraintButtons::addView)
                    val next = androidx.appcompat.widget.AppCompatImageView(
                        context
                    ).apply {
                        constraintPara(d50, d50) {
                            com.pt.pro.R.id.searchButton.let { s ->
                                bottomToBottom = s
                                topToTop = s
                                endToEnd = 0
                                startToEnd = s
                            }
                        }
                        findPixel(10).also {
                            setPadding(it, it, it, it)
                        }
                        compactImage(com.pt.pro.R.drawable.ic_next) {
                            setImageDrawable(this@compactImage)
                        }
                        justGone()
                    }.also(this@constraintButtons::addView)
                    android.widget.FrameLayout(
                        context
                    ).apply upperFrame@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {}
                        compactImage(com.pt.pro.R.drawable.pager_background) {
                            background = this@compactImage
                        }
                        fetchColor(com.pt.pro.R.color.wgl).also { c ->
                            val scaled = androidx.appcompat.widget.AppCompatImageView(
                                context
                            ).apply {
                                findPixel(30).also { framePara(it, it) {} }
                                compactImage(com.pt.pro.R.drawable.ic_scaled) {
                                    setImageDrawable(this@compactImage)
                                }
                                svgReColor(c)
                            }.also(this@upperFrame::addView)
                            val close = androidx.appcompat.widget.AppCompatImageView(
                                context
                            ).apply {
                                findPixel(32).also {
                                    framePara(it, it) {
                                        gravity = android.view.Gravity.END
                                    }
                                }
                                compactImage(com.pt.pro.R.drawable.ic_close) {
                                    setImageDrawable(this@compactImage)
                                }
                                svgReColor(c)
                            }.also(this@upperFrame::addView)
                            androidx.appcompat.widget.LinearLayoutCompat(
                                context
                            ).apply durationLinear@{
                                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                                    gravity = android.view.Gravity.BOTTOM
                                }
                                compactImage(com.pt.pro.R.drawable.pager_background_reversed) {
                                    background = this@compactImage
                                }
                                orientation =
                                    androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
                                justGone()
                                val currentDurationFloating = android.widget.Chronometer(
                                    context
                                ).apply {
                                    linearPara(
                                        com.pt.common.stable.WRAP,
                                        com.pt.common.stable.WRAP,
                                        0F
                                    ) {
                                        gravity = android.view.Gravity.CENTER
                                        marginStart = findPixel(2)
                                    }
                                    text = resources.getString(com.pt.pro.R.string.bz)
                                    gravity = android.view.Gravity.CENTER
                                    textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
                                    setTextColor(android.graphics.Color.WHITE)
                                    setTextSize(
                                        android.util.TypedValue.COMPLEX_UNIT_PX,
                                        findPixel(12).toFloat()
                                    )
                                }.also(this@durationLinear::addView)
                                val seekBarFloating: androidx.appcompat.widget.AppCompatSeekBar
                                android.widget.FrameLayout(
                                    context
                                ).apply frame@{
                                    linearPara(0, findPixel(40), 1F) {}
                                    seekBarFloating = androidx.appcompat.widget.AppCompatSeekBar(
                                        context
                                    ).apply {
                                        framePara(
                                            com.pt.common.stable.MATCH,
                                            com.pt.common.stable.MATCH
                                        ) {
                                            (-1 * findPixel(7)).also {
                                                marginStart = it
                                                marginEnd = it
                                            }
                                        }
                                    }.also(this@frame::addView)
                                }.also(this@durationLinear::addView)

                                val durationFloating = androidx.appcompat.widget.AppCompatTextView(
                                    context
                                ).apply {
                                    linearPara(
                                        com.pt.common.stable.WRAP,
                                        com.pt.common.stable.WRAP,
                                        0F
                                    ) {
                                        gravity = android.view.Gravity.CENTER
                                        marginEnd = findPixel(2)
                                    }
                                    text = resources.getString(com.pt.pro.R.string.bz)
                                    gravity = android.view.Gravity.CENTER
                                    textAlignment = android.view.View.TEXT_ALIGNMENT_CENTER
                                    setTextColor(android.graphics.Color.WHITE)
                                    setTextSize(
                                        android.util.TypedValue.COMPLEX_UNIT_PX,
                                        findPixel(12).toFloat()
                                    )
                                }.also(this@durationLinear::addView)
                                fasten = VideoFloatFasten(
                                    root_ = this@root_,
                                    floatingVideo = floatingVideo,
                                    constraintButtons = this@constraintButtons,
                                    playFloating = playFloating,
                                    previous = previous,
                                    next = next,
                                    upperFrame = this@upperFrame,
                                    scaled = scaled,
                                    close = close,
                                    durationLinear = this@durationLinear,
                                    currentDurationFloating = currentDurationFloating,
                                    durationFloating = durationFloating,
                                    seekBarFloating = seekBarFloating
                                )
                            }.also(this@root_::addView)
                        }
                    }.also(this@root_::addView)
                }.also(this@root_::addView)
            }
            return fasten
        }
    }

    override fun getRoot(): com.pt.common.moderator.touch.TouchFrameLayout = root_
}
