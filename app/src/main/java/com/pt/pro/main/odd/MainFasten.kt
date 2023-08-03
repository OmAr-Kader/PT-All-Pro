package com.pt.pro.main.odd

import com.pt.common.global.*

object MainFasten : com.pt.common.mutual.life.GlobalInflater() {

    internal var dis3Native: Int? = null
    internal var dmNative: Int? = null
    internal var tsgDimNative: Float? = null
    internal var gopNative: Int? = null
    internal var bclNative: Int? = null
    internal var cardLargeNative: Float? = null

    @androidx.annotation.ColorInt
    internal var colorPrimaryNative: Int? = null

    @androidx.annotation.ColorInt
    internal var textPrimaryNative: Int? = null

    @androidx.annotation.ColorInt
    internal var rmoTextNative: Int? = null

    @JvmStatic
    internal inline val android.content.res.Resources.dis3: Int
        get() = dis3Native ?: displayMetrics.findPixelDis(3F)
            .also { dis3Native = it }

    @JvmStatic
    internal inline val android.content.res.Resources.dmDim: Int
        get() = dmNative ?: getDimension(com.pt.pro.R.dimen.dm).toInt()
            .also { dmNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.bclDim: Int
        get() = bclNative ?: getDimension(com.pt.pro.R.dimen.bcl).toInt()
            .also { bclNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.tsgDim: Float
        get() = tsgDimNative ?: getDimension(com.pt.pro.R.dimen.tsg)
            .also { tsgDimNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.gopDim: Int
        get() = gopNative ?: getDimension(com.pt.pro.R.dimen.gop).toInt()
            .also { gopNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.cardLarge: Float
        get() = cardLargeNative ?: getDimension(com.pt.pro.R.dimen.card_fixed_large)
            .also { cardLargeNative = it }

    @JvmStatic
    internal inline val android.content.Context.primary: Int
        @androidx.annotation.ColorInt
        get() = colorPrimaryNative ?: theme.findAttr(android.R.attr.colorPrimary)
            .also { colorPrimaryNative = it }

    @JvmStatic
    internal inline val android.content.Context.textPrimary: Int
        @androidx.annotation.ColorInt
        get() = textPrimaryNative ?: theme.findAttr(android.R.attr.textColorPrimary)
            .also { textPrimaryNative = it }

    @JvmStatic
    internal inline val android.content.Context.rmoTxt: Int
        @androidx.annotation.ColorInt
        get() = rmoTextNative ?: theme.findAttr(com.pt.pro.R.attr.rmoText)
            .also { rmoTextNative = it }

    @JvmStatic
    fun android.content.Context.inflaterMainIcon(wid: Int): MainIconFasten {
        val fasten: MainIconFasten
        android.widget.FrameLayout(this@inflaterMainIcon).apply root_@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                com.pt.common.stable.WRAP,
                com.pt.common.stable.WRAP
            ).also(this@root_::setLayoutParams)
            androidx.constraintlayout.widget.ConstraintLayout(
                context
            ).apply iconFrame@{
                framePara(wid, wid) {}
                androidx.cardview.widget.CardView(
                    context
                ).apply cardPic@{
                    id = com.pt.pro.R.id.savePlaylist
                    androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                        0,
                        0
                    ).apply {
                        resources.also {
                            topMargin = it.dis3
                            marginStart = it.gopDim
                            marginEnd = it.gopDim
                            bottomMargin = it.dmDim
                            topToTop = 0
                            endToEnd = 0
                            startToStart = 0
                            bottomToBottom = 0
                        }
                    }.also(this@cardPic::setLayoutParams)
                    cardElevation = 0F
                    radius = resources.cardLarge
                    setCardBackgroundColor(primary)
                    val iconPic = com.pt.common.moderator.over.FitWidthAtBottomImageView(
                        context
                    ).apply {
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                            gravity = android.view.Gravity.CENTER
                            resources.bclDim.also {
                                marginStart = it
                                topMargin = it
                                bottomMargin = it
                                marginEnd = it
                            }
                        }
                        contentDescription = resources.getString(com.pt.pro.R.string.ff)
                        scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
                        svgReColor(textPrimary)
                    }.also(this@cardPic::addView)
                    val iconTxt = androidx.appcompat.widget.AppCompatTextView(
                        context
                    ).apply iconTxt@{
                        androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                            0,
                            com.pt.common.stable.WRAP
                        ).apply {
                            resources.also {
                                topToBottom = com.pt.pro.R.id.savePlaylist
                                endToEnd = 0
                                startToStart = 0
                                bottomToBottom = 0
                            }
                        }.also(this@iconTxt::setLayoutParams)
                        gravity = android.view.Gravity.CENTER_HORIZONTAL
                        textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                        setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, resources.tsgDim)
                        setTextColor(rmoTxt)
                    }.also(this@iconFrame::addView)

                    fasten = MainIconFasten(
                        root_ = this@root_,
                        iconFrame = this@iconFrame,
                        cardPic = this@cardPic,
                        iconPic = iconPic,
                        iconTxt = iconTxt
                    )
                }.also(this@iconFrame::addView)
            }.also(this@root_::addView)
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterMainFragment(
        act: Int,
        @androidx.annotation.ColorInt rmoBack: Int,
        @androidx.annotation.ColorInt rmoText: Int
    ): FragmentMainFasten {
        val fasten: FragmentMainFasten
        com.pt.common.moderator.over.FrameSizer(this@inflaterMainFragment).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            setBackgroundColor(rmoBack)
            androidx.constraintlayout.widget.ConstraintLayout(
                context
            ).apply cardMain@{
                framePara(com.pt.common.stable.MATCH, act) {}
                val mainBack = com.pt.common.moderator.over.ResizeImageView(
                    context
                ).apply {
                    id = com.pt.pro.R.id.returnMusic
                    androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                        com.pt.common.stable.WRAP,
                        com.pt.common.stable.MATCH
                    ).apply {
                        bottomToBottom = 0
                        endToEnd = 0
                        startToStart = 0
                        topToTop = 0
                        horizontalBias = 0.0F
                        topMargin = findPixel(4)
                        bottomMargin = findPixel(4)
                        marginStart = findPixel(5)
                    }.also(::setLayoutParams)
                    setPadding(0, findPixel(10), 0, findPixel(10))
                    compactImage(com.pt.pro.R.drawable.ic_home) {
                        setImageDrawable(this@compactImage)
                    }
                    svgReColor(rmoText)
                }.also(this@cardMain::addView)
                val textPt = androidx.appcompat.widget.AppCompatTextView(
                    context
                ).apply {
                    androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                        0,
                        com.pt.common.stable.WRAP
                    ).apply {
                        marginEnd = findPixel(3)
                        bottomToBottom = 0
                        endToStart = com.pt.pro.R.id.lockBrowser
                        startToEnd = com.pt.pro.R.id.returnMusic
                        topToTop = 0
                        horizontalBias = 0.0F
                        marginStart = findPixel(3)
                    }.also(::setLayoutParams)
                    editAppearance()
                    maxLines = 1
                    isHorizontalScrollBarEnabled = false
                    isVerticalScrollBarEnabled = false
                    text = resources.getString(com.pt.pro.R.string.icon_name)
                    textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                    setTextColor(rmoText)
                }.also(this@cardMain::addView)
                val pagerMenu = com.pt.common.moderator.over.ResizeImageView(
                    context
                ).apply {
                    id = com.pt.pro.R.id.lockBrowser
                    androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                        com.pt.common.stable.WRAP,
                        com.pt.common.stable.MATCH
                    ).apply {
                        bottomToBottom = 0
                        endToEnd = 0
                        startToStart = 0
                        topToTop = 0
                        horizontalBias = 1.0F
                        topMargin = findPixel(4)
                        bottomMargin = findPixel(4)
                    }.also(::setLayoutParams)
                    setPadding(0, findPixel(10), 0, findPixel(10))
                    compactImage(com.pt.pro.R.drawable.ic_menu_list) {
                        setImageDrawable(this@compactImage)
                    }
                    svgReColor(rmoText)
                }.also(this@cardMain::addView)
                val iconsRecyclerView: com.pt.common.moderator.recycler.RecyclerForViews
                val frameRec = android.widget.FrameLayout(context).apply frameRec@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                        topMargin = act
                        bottomMargin = dpToPx(10)
                    }
                    iconsRecyclerView = com.pt.common.moderator.recycler.RecyclerForViews(
                        context
                    ).apply {
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                            gravity = android.view.Gravity.CENTER_VERTICAL
                        }
                    }.also(this@frameRec::addView)
                }.also(this@root_::addView)
                fasten = FragmentMainFasten(
                    root_ = this@root_,
                    cardMain = this@cardMain,
                    mainBack = mainBack,
                    textPt = textPt,
                    pagerMenu = pagerMenu,
                    frameRec = frameRec,
                    iconsRecyclerView = iconsRecyclerView,
                )
            }.also(this@root_::addView)
        }
        return fasten
    }

    @JvmStatic
    fun destroyFasten() {
        dis3Native = null
        dmNative = null
        gopNative = null
        colorPrimaryNative = null
        cardLargeNative = null
        bclNative = null
        textPrimaryNative = null
        tsgDimNative = null
        rmoTextNative = null
        destroyGlobalFasten()
    }

}