package com.pt.pro.extra.fasten

import com.pt.common.global.*

object ExtraInflater : com.pt.common.mutual.life.GlobalInflater() {

    @JvmStatic
    fun android.content.Context.inflaterScreenCapture(): ScreenCaptureFasten {
        val act: Int = actionBarHeight
        val fasten: ScreenCaptureFasten
        frameLayoutParent root_@{
            framePara(com.pt.common.stable.MATCH) {}
            val mainBack: com.pt.common.moderator.over.ResizeImageView
            val clickPrevious: com.pt.common.moderator.over.ResizeImageView
            val clickNext: com.pt.common.moderator.over.ResizeImageView
            val textPt: androidx.appcompat.widget.AppCompatTextView
            val tittlePager: androidx.viewpager2.widget.ViewPager2
            frame cardScreen@{
                framePara(com.pt.common.stable.MATCH, act) {}
                compactImage(com.pt.pro.R.drawable.card_back) {
                    background
                }
                backReColor(colorPrimary)
                this@cardScreen.constraint constraint@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    mainBack = this@constraint.resizeImage {
                        id = com.pt.pro.R.id.lockBrowser
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginStart = findPixel(5)
                            findPixel(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            startToStart = 0
                        }
                        findPixel(10).also { d10 ->
                            setPadding(0, d10, 0, d10)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_back_home) {
                            setImageDrawable(this)
                        }
                    }
                    textPt = this@constraint.textViewer {
                        id = com.pt.pro.R.id.searchButton
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                            bottomToBottom = 0
                            startToEnd = com.pt.pro.R.id.lockBrowser//mainBack
                            topToTop = 0
                            marginStart = findPixel(3)
                        }
                        singleNonScroll()
                        editAppearance()
                        text = resources.getString(com.pt.pro.R.string.hs)
                        setTextColor(textColorPrimary)
                    }
                    this@constraint.coordinator coordinator@{
                        constraintPara(0, com.pt.common.stable.WRAP) {
                            bottomToBottom = 0
                            startToEnd = com.pt.pro.R.id.searchButton//textPt
                            endToStart = com.pt.pro.R.id.showAlbums//clickPrevious
                            topToTop = 0
                            marginStart = findPixel(3)
                        }
                        tittlePager = this@coordinator.pager tittlePager@{
                            androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                com.pt.common.stable.MATCH,
                                com.pt.common.stable.MATCH
                            ).apply {
                                behavior =
                                    com.pt.common.moderator.recycler.OverScrollHorizontalBehavior()
                            }.also(this@tittlePager::setLayoutParams)
                            orientation = androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            isNestedScrollingEnabled = true
                            overScrollMode = android.view.View.OVER_SCROLL_NEVER
                            scrollBarSize = 0
                        }
                    }
                    clickPrevious = this@constraint.resizeImage {
                        id = com.pt.pro.R.id.showAlbums
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginStart = findPixel(5)
                            findPixel(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.deletePlaylist//clickNext
                        }
                        isClickable = true
                        isFocusable = true
                        rotation = 180F
                        contentDescription = resources.getString(com.pt.pro.R.string.zp)
                        findPixel(10).also { d10 ->
                            setPadding(0, d10, 0, d10)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_next_page) {
                            setImageDrawable(this)
                        }
                    }
                    clickNext = this@constraint.resizeImage {
                        id = com.pt.pro.R.id.deletePlaylist
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginStart = findPixel(5)
                            findPixel(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            endToEnd = 0
                        }
                        isClickable = true
                        isFocusable = true
                        contentDescription = resources.getString(com.pt.pro.R.string.zp)
                        findPixel(10).also { d10 ->
                            setPadding(0, d10, 0, d10)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_next_page) {
                            setImageDrawable(this)
                        }
                    }
                }
                this@root_.frame screenFrame@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                        topMargin = act
                    }
                    val swipePager: androidx.viewpager2.widget.ViewPager2
                    this@screenFrame.coordinator coordinator@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                        swipePager = this@coordinator.pager tittlePager@{
                            androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                com.pt.common.stable.MATCH,
                                com.pt.common.stable.MATCH
                            ).apply {
                                behavior = com.pt.common.moderator.recycler.OverScrollHorizontalBehavior()
                            }.also(this@tittlePager::setLayoutParams)
                            orientation =
                                androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            isNestedScrollingEnabled = true
                            overScrollMode = android.view.View.OVER_SCROLL_NEVER
                            scrollBarSize = 0

                        }
                    }
                    this@screenFrame.frame frameLayout@{
                        framePara(dpToPx(300), dpToPx(100)) {
                            gravity = android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL
                        }
                        this@frameLayout.frame cardSwiper@{
                            framePara(com.pt.common.stable.MATCH, dpToPx(60)) {
                                gravity = android.view.Gravity.CENTER_VERTICAL
                            }
                            compactImage(com.pt.pro.R.drawable.item_fixed_large) {
                                background = this@compactImage
                            }
                            backReColor(rmoBackHint)
                            nonClickableView()
                            elevation = dpToPx(9).toFloat()
                            val slideText = this@cardSwiper.textViewer {
                                framePara(com.pt.common.stable.WRAP) {
                                    gravity = android.view.Gravity.CENTER
                                }
                                text = resources.getString(com.pt.pro.R.string.fp)
                                setTextColor(rmoText)
                                textSize(dpToPx(16))
                            }
                            val seekLaunch = this@cardSwiper.seekBar {
                                framePara(com.pt.common.stable.MATCH, dpToPx(50)) {
                                    gravity = android.view.Gravity.CENTER_VERTICAL
                                }
                                clickableView()
                                max = 1000
                                progress = 50
                                progressDrawable = compactImageReturn(com.pt.pro.R.drawable.empty_progress)
                                seekReColor(android.graphics.Color.TRANSPARENT)
                                thumb = compactImageReturn(com.pt.pro.R.drawable.circle)
                            }
                            fasten = ScreenCaptureFasten(
                                root_ = this@root_,
                                cardScreen = this@cardScreen,
                                textPt = textPt,
                                tittlePager = tittlePager,
                                mainBack = mainBack,
                                clickPrevious = clickPrevious,
                                clickNext = clickNext,
                                screenFrame = this@screenFrame,
                                swipePager = swipePager,
                                slideText = slideText,
                                seekLaunch = seekLaunch,
                            )
                        }
                    }
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterSetting(): ActivitySettingFasten {
        val act = actionBarHeight
        val fasten: ActivitySettingFasten
        android.widget.FrameLayout(this@inflaterSetting).apply root_@{
            framePara(com.pt.common.stable.MATCH) {}
            val mainBack: com.pt.common.moderator.over.ResizeImageView
            val clickPrevious: com.pt.common.moderator.over.ResizeImageView
            val clickNext: com.pt.common.moderator.over.ResizeImageView
            val textPt: androidx.appcompat.widget.AppCompatTextView
            val tittlePager: androidx.viewpager2.widget.ViewPager2
            com.pt.common.moderator.over.FrameTint(
                context
            ).apply cardSetting@{
                framePara(com.pt.common.stable.MATCH, act) {}
                compactImage(com.pt.pro.R.drawable.card_back) {
                    background
                }
                backReColor(theme.findAttr(android.R.attr.colorPrimary))
                androidx.constraintlayout.widget.ConstraintLayout(
                    context
                ).apply constraint@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    mainBack = com.pt.common.moderator.over.ResizeImageView(
                        context
                    ).apply {
                        id = com.pt.pro.R.id.lockBrowser
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginStart = findPixel(5)
                            findPixel(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            startToStart = 0
                        }
                        findPixel(10).also { d10 ->
                            setPadding(0, d10, 0, d10)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_back_home) {
                            setImageDrawable(this)
                        }
                    }.also(this@constraint::addView)
                    textPt = androidx.appcompat.widget.AppCompatTextView(context).apply {
                        id = com.pt.pro.R.id.searchButton
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                            bottomToBottom = 0
                            startToEnd = com.pt.pro.R.id.lockBrowser//mainBack
                            topToTop = 0
                            marginStart = findPixel(3)
                        }
                        singleNonScroll()
                        editAppearance()
                        text = resources.getString(com.pt.pro.R.string.hs)
                        setTextColor(theme.findAttr(android.R.attr.textColorPrimary))
                    }.also(this@constraint::addView)
                    androidx.coordinatorlayout.widget.CoordinatorLayout(
                        context
                    ).apply coordinator@{
                        constraintPara(0, com.pt.common.stable.WRAP) {
                            bottomToBottom = 0
                            startToEnd = com.pt.pro.R.id.searchButton//textPt
                            endToStart = com.pt.pro.R.id.showAlbums//clickPrevious
                            topToTop = 0
                            marginStart = findPixel(3)
                        }
                        tittlePager = androidx.viewpager2.widget.ViewPager2(
                            context
                        ).apply tittlePager@{
                            androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                com.pt.common.stable.MATCH,
                                com.pt.common.stable.MATCH
                            ).apply {
                                behavior =
                                    com.pt.common.moderator.recycler.OverScrollHorizontalBehavior()
                            }.also(this@tittlePager::setLayoutParams)
                            orientation =
                                androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
                            setBackgroundColor(android.graphics.Color.TRANSPARENT)
                            isNestedScrollingEnabled = true
                            overScrollMode = android.view.View.OVER_SCROLL_NEVER
                            scrollBarSize = 0
                        }.also(this@coordinator::addView)
                    }.also(this@constraint::addView)
                    clickPrevious = com.pt.common.moderator.over.ResizeImageView(
                        context
                    ).apply {
                        id = com.pt.pro.R.id.showAlbums
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginStart = findPixel(5)
                            findPixel(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.deletePlaylist//clickNext
                        }
                        isClickable = true
                        isFocusable = true
                        rotation = 180F
                        contentDescription = resources.getString(com.pt.pro.R.string.zp)
                        findPixel(10).also { d10 ->
                            setPadding(0, d10, 0, d10)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_next_page) {
                            setImageDrawable(this)
                        }
                    }.also(this@constraint::addView)
                    clickNext = com.pt.common.moderator.over.ResizeImageView(
                        context
                    ).apply {
                        id = com.pt.pro.R.id.deletePlaylist
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginStart = findPixel(5)
                            findPixel(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            endToEnd = 0
                        }
                        isClickable = true
                        isFocusable = true
                        contentDescription = resources.getString(com.pt.pro.R.string.zp)
                        findPixel(10).also { d10 ->
                            setPadding(0, d10, 0, d10)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_next_page) {
                            setImageDrawable(this)
                        }
                    }.also(this@constraint::addView)
                }.also(this@cardSetting::addView)
                androidx.constraintlayout.widget.ConstraintLayout(
                    context
                ).apply frameSetting@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                        topMargin = act
                    }
                    val viewForAd = android.widget.FrameLayout(
                        context
                    ).apply {
                        id = com.pt.pro.R.id.galleryModeFrame//viewForAd
                        constraintPara(com.pt.common.stable.MATCH, findPixel(60)) {
                            bottomToBottom = 0
                            startToStart = 0
                            endToEnd = 0
                        }
                        justGone()
                    }.also(this@frameSetting::addView)
                    val swipePager: androidx.viewpager2.widget.ViewPager2
                    android.widget.FrameLayout(
                        context
                    ).apply mainFrame@{
                        constraintPara(com.pt.common.stable.MATCH, 0) {
                            bottomToTop = com.pt.pro.R.id.galleryModeFrame
                            startToStart = 0
                            endToEnd = 0
                            topToTop = 0
                        }
                        androidx.coordinatorlayout.widget.CoordinatorLayout(
                            context
                        ).apply coordinator@{
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                            swipePager = androidx.viewpager2.widget.ViewPager2(
                                context
                            ).apply tittlePager@{
                                androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                    com.pt.common.stable.MATCH,
                                    com.pt.common.stable.MATCH
                                ).apply {
                                    behavior = com.pt.common.moderator.recycler.OverScrollHorizontalBehavior()
                                }.also(this@tittlePager::setLayoutParams)
                                orientation =
                                    androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
                                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                isNestedScrollingEnabled = true
                                overScrollMode = android.view.View.OVER_SCROLL_NEVER
                                scrollBarSize = 0

                            }.also(this@coordinator::addView)
                        }.also(this@mainFrame::addView)
                    }.also(this@frameSetting::addView)
                    fasten = ActivitySettingFasten(
                        root_ = this@root_,
                        cardSetting = this@cardSetting,
                        textPt = textPt,
                        tittlePager = tittlePager,
                        mainBack = mainBack,
                        clickPrevious = clickPrevious,
                        clickNext = clickNext,
                        frameSetting = this@frameSetting,
                        viewForAd = viewForAd,
                        swipePager = swipePager,
                    )
                }.also(this@root_::addView)
            }.also(this@root_::addView)
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterColorItem(): ColorItemFasten {
        val fasten: ColorItemFasten
        androidx.cardview.widget.CardView(this@inflaterColorItem).apply root_@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                com.pt.common.stable.WRAP,
                com.pt.common.stable.WRAP
            ).also(this@root_::setLayoutParams)
            setCardBackgroundColor(0)
            radius = dpToPx(5).toFloat()
            cardElevation = 0F
            androidx.appcompat.widget.AppCompatImageView(
                context
            ).apply colorButton@{
                dpToPx(50).also { d50 ->
                    framePara(d50, d50) {
                        gravity = android.view.Gravity.CENTER
                        dpToPx(7).also { d7 ->
                            topMargin = d7
                            bottomMargin = d7
                            marginStart = d7
                            marginEnd = d7
                        }
                    }
                }
                compactImage(com.pt.pro.R.drawable.ripple_cur) {
                    background = this@compactImage
                }
                compactImage(com.pt.pro.R.drawable.circle) {
                    setImageDrawable(this@compactImage)
                }
                fasten = ColorItemFasten(
                    root_ = this@root_,
                    colorButton = this@colorButton
                )
            }.also(this@root_::addView)
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterAlarmSetting(): SettingAlarmFasten {
        val txtDim = resources.getDimension(com.pt.pro.R.dimen.txt).toInt()
        val tedDim = resources.getDimension(com.pt.pro.R.dimen.ted)
        val d20 = dpToPx(20)
        val d15 = dpToPx(15)
        val d5 = dpToPx(5)
        val d30 = dpToPx(30)
        val d50 = dpToPx(50)

        val fasten: SettingAlarmFasten
        android.widget.FrameLayout(this@inflaterAlarmSetting).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            androidx.cardview.widget.CardView(
                context
            ).apply card@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    findPixel(15).also {
                        marginStart = it
                        marginEnd = it
                        topMargin = it
                        bottomMargin = it
                    }
                }
                setCardBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackHint))
                radius = findPixel(38).toFloat()
                cardElevation = findPixel(5).toFloat()
                androidx.core.widget.NestedScrollView(
                    context
                ).apply scroll@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    androidx.appcompat.widget.LinearLayoutCompat(
                        context
                    ).apply lin@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                        orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
                        val intervalText = mainFrameSetting(
                            d20 = d20,
                            d15 = d15,
                            d5 = d5,
                            d30 = d30,
                            txtDim = txtDim,
                            tedDim = tedDim,
                            str = com.pt.pro.R.string.vt,
                            accent = colorAccent,
                            rmoText = rmoText
                        ).also { it.one.also(this@lin::addView) }
                        val intervalMainFrame = mainSettingFrame(
                            d50 = d50
                        ).also { it.one.also(this@lin::addView) }
                        val snoozeText = mainFrameSetting(
                            d20 = d20,
                            d15 = d15,
                            d5 = d5,
                            d30 = d30,
                            txtDim = txtDim,
                            tedDim = tedDim,
                            str = com.pt.pro.R.string.zt,
                            accent = colorAccent,
                            rmoText = rmoText
                        ).also { it.one.also(this@lin::addView) }
                        val snoozeMainFrame = mainSettingFrame(
                            d50 = d50
                        ).also { it.one.also(this@lin::addView) }
                        val frameRepeat = mainFrameSetting(
                            d20 = d20,
                            d15 = d15,
                            d5 = d5,
                            d30 = d30,
                            txtDim = txtDim,
                            tedDim = tedDim,
                            str = com.pt.pro.R.string.t3,
                            accent = colorAccent,
                            rmoText = rmoText
                        ).also { it.one.also(this@lin::addView) }
                        val repeatMainFrame = mainSettingFrame(
                            d50 = d50
                        ).also { it.one.also(this@lin::addView) }
                        val volumeText = mainFrameSetting(
                            d20 = d20,
                            d15 = d15,
                            d5 = d5,
                            d30 = d30,
                            txtDim = txtDim,
                            tedDim = tedDim,
                            str = com.pt.pro.R.string.vi,
                            accent = colorAccent,
                            rmoText = rmoText
                        ).also { it.one.also(this@lin::addView) }
                        val volumeMainFrame = mainSettingFrame(
                            d50 = d50
                        ).also { it.one.also(this@lin::addView) }
                        fasten = SettingAlarmFasten(
                            root_ = this@root_,
                            frameIntervalText = intervalText.one,
                            intervalImage = intervalText.two,
                            mainFrameInterval = intervalMainFrame.one,
                            frameInterval = intervalMainFrame.two,
                            frameSnoozeText = snoozeText.one,
                            snoozeImage = snoozeText.two,
                            mainFrameSnooze = snoozeMainFrame.one,
                            frameSnooze = snoozeMainFrame.two,
                            frameRepeatText = frameRepeat.one,
                            repeatImage = frameRepeat.two,
                            mainFrameRepeat = repeatMainFrame.one,
                            frameRepeat = repeatMainFrame.two,
                            frameVolumeText = volumeText.one,
                            volumeImage = volumeText.two,
                            mainFrameVolume = volumeMainFrame.one,
                            frameVolume = volumeMainFrame.two,
                        )
                    }.also(this@scroll::addView)
                }.also(this@card::addView)
            }.also(this@root_::addView)

        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterFileSetting(): SettingFileFasten {
        val txtDim = resources.getDimension(com.pt.pro.R.dimen.txt).toInt()
        val tggDim = resources.getDimension(com.pt.pro.R.dimen.tgg).toInt()
        val tedDim = resources.getDimension(com.pt.pro.R.dimen.ted)

        val fasten: SettingFileFasten
        android.widget.FrameLayout(this@inflaterFileSetting).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            androidx.cardview.widget.CardView(
                context
            ).apply card@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    findPixel(15).also {
                        marginStart = it
                        marginEnd = it
                        topMargin = it
                        bottomMargin = it
                    }
                }
                setCardBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackHint))
                radius = findPixel(38).toFloat()
                cardElevation = findPixel(5).toFloat()
                androidx.core.widget.NestedScrollView(
                    context
                ).apply scroll@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    androidx.appcompat.widget.LinearLayoutCompat(
                        context
                    ).apply lin@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                        orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
                        val sortByText = mainFrameSetting(
                            d20 = dpToPx(20),
                            d15 = dpToPx(15),
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            txtDim = txtDim,
                            tedDim = tedDim,
                            str = com.pt.pro.R.string.gq,
                            accent = colorAccent,
                            rmoText = rmoText
                        ).also { it.one.also(this@lin::addView) }
                        val sortByMainFrame = mainSettingFrame(
                            d50 = dpToPx(50)
                        ).also { it.one.also(this@lin::addView) }
                        val sortByFolderText = mainFrameSetting(
                            d20 = dpToPx(20),
                            d15 = dpToPx(15),
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            txtDim = txtDim,
                            tedDim = tedDim,
                            str = com.pt.pro.R.string.gy,
                            accent = colorAccent,
                            rmoText = rmoText
                        ).also { it.one.also(this@lin::addView) }
                        val sortByFolderMainFrame = mainSettingFrame(
                            d50 = dpToPx(50)
                        ).also { it.one.also(this@lin::addView) }

                        val usePictureFrame = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.xv,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val useVideoFrame = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.bh,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val useMusicFrame = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.fg,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val usePdfFrame = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.fz,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val useTxtFrame = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.wv,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val useZipFrame = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.zv,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val screenTxtFrame = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.wb,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val screenPdfFrame = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.wp,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        fasten = SettingFileFasten(
                            root_ = this@root_,
                            frameSortByText = sortByText.one,
                            sortByImage = sortByText.two,
                            mainFrameSortBy = sortByMainFrame.one,
                            frameSortBy = sortByMainFrame.two,
                            frameSortByFolderText = sortByFolderText.one,
                            sortByFolderImage = sortByFolderText.two,
                            mainFrameSortByFolder = sortByFolderMainFrame.one,
                            frameSortByFolder = sortByFolderMainFrame.two,
                            usePicture = usePictureFrame.two,
                            useVideo = useVideoFrame.two,
                            useMusic = useMusicFrame.two,
                            usePdf = usePdfFrame.two,
                            useTxt = useTxtFrame.two,
                            useZip = useZipFrame.two,
                            screenTxt = screenTxtFrame.two,
                            screenPdf = screenPdfFrame.two,
                        )
                    }.also(this@scroll::addView)
                }.also(this@card::addView)
            }.also(this@root_::addView)
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterGallerySetting(): SettingGalleryFasten {
        val txtDim = resources.getDimension(com.pt.pro.R.dimen.txt).toInt()
        val tggDim = resources.getDimension(com.pt.pro.R.dimen.tgg).toInt()
        val tedDim = resources.getDimension(com.pt.pro.R.dimen.ted)
        val fasten: SettingGalleryFasten
        android.widget.FrameLayout(this@inflaterGallerySetting).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            androidx.cardview.widget.CardView(
                context
            ).apply card@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    findPixel(15).also {
                        marginStart = it
                        marginEnd = it
                        topMargin = it
                        bottomMargin = it
                    }
                }
                setCardBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackHint))
                radius = findPixel(38).toFloat()
                cardElevation = findPixel(5).toFloat()
                androidx.core.widget.NestedScrollView(
                    context
                ).apply scroll@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    androidx.appcompat.widget.LinearLayoutCompat(
                        context
                    ).apply lin@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                        orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
                        val sortByText = mainFrameSetting(
                            d20 = dpToPx(20),
                            d15 = dpToPx(15),
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            txtDim = txtDim,
                            tedDim = tedDim,
                            str = com.pt.pro.R.string.gq,
                            accent = colorAccent,
                            rmoText = rmoText
                        ).also { it.one.also(this@lin::addView) }
                        val sortByMainFrame = mainSettingFrame(
                            d50 = dpToPx(50)
                        ).also { it.one.also(this@lin::addView) }
                        val sortByFolderText = mainFrameSetting(
                            d20 = dpToPx(20),
                            d15 = dpToPx(15),
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            txtDim = txtDim,
                            tedDim = tedDim,
                            str = com.pt.pro.R.string.gy,
                            accent = colorAccent,
                            rmoText = rmoText
                        ).also { it.one.also(this@lin::addView) }
                        val sortByFolderMainFrame = mainSettingFrame(
                            d50 = dpToPx(50)
                        ).also { it.one.also(this@lin::addView) }
                        val columnText = mainFrameSetting(
                            d20 = dpToPx(20),
                            d15 = dpToPx(15),
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            txtDim = txtDim,
                            tedDim = tedDim,
                            str = com.pt.pro.R.string.ca,
                            accent = colorAccent,
                            rmoText = rmoText
                        ).also { it.one.also(this@lin::addView) }
                        val columnMainFrame = mainSettingFrame(
                            d50 = dpToPx(50)
                        ).also { it.one.also(this@lin::addView) }

                        val switchResolution = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.zu,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val galleryScreen = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.wl,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val galleryStory = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.uz,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        fasten = SettingGalleryFasten(
                            root_ = this@root_,
                            frameSortByText = sortByText.one,
                            sortByImage = sortByText.two,
                            mainFrameSortBy = sortByMainFrame.one,
                            frameSortBy = sortByMainFrame.two,
                            frameSortByFolderText = sortByFolderText.one,
                            sortByFolderImage = sortByFolderText.two,
                            mainFrameSortByFolder = sortByFolderMainFrame.one,
                            frameSortByFolder = sortByFolderMainFrame.two,
                            frameColumnText = columnText.one,
                            columnImage = columnText.two,
                            mainFrameColumn = columnMainFrame.one,
                            frameColumn = columnMainFrame.two,
                            switchResolution = switchResolution.two,
                            galleryScreen = galleryScreen.two,
                            galleryStory = galleryStory.two,
                        )
                    }.also(this@scroll::addView)
                }.also(this@card::addView)
            }.also(this@root_::addView)
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterMusicSetting(): SettingMusicFasten {
        val txtDim = resources.getDimension(com.pt.pro.R.dimen.txt).toInt()
        val tggDim = resources.getDimension(com.pt.pro.R.dimen.tgg).toInt()
        val fasten: SettingMusicFasten
        android.widget.FrameLayout(this@inflaterMusicSetting).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            androidx.cardview.widget.CardView(
                context
            ).apply card@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    findPixel(15).also {
                        marginStart = it
                        marginEnd = it
                        topMargin = it
                        bottomMargin = it
                    }
                }
                setCardBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackHint))
                radius = findPixel(38).toFloat()
                cardElevation = findPixel(5).toFloat()
                androidx.core.widget.NestedScrollView(
                    context
                ).apply scroll@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    androidx.appcompat.widget.LinearLayoutCompat(
                        context
                    ).apply lin@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                        orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
                        val switchMusicScreen = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.zb,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val showVoice = checkSettingBox(
                            d5 = dpToPx(5),
                            d30 = dpToPx(30),
                            d48 = dpToPx(48),
                            d80 = dpToPx(80),
                            txtDim = txtDim.toFloat(),
                            tggDim = tggDim,
                            str = com.pt.pro.R.string.ne,
                            rmoText = rmoText,
                        ).also { it.one.also(this@lin::addView) }
                        val seekTitle: androidx.appcompat.widget.AppCompatTextView
                        val scaleNum: com.pt.common.moderator.over.ScalelessTextView
                        val pauseSeek: androidx.appcompat.widget.AppCompatSeekBar
                        val maxNum: com.pt.common.moderator.over.ScalelessTextView
                        androidx.appcompat.widget.LinearLayoutCompat(
                            context
                        ).apply anotherLin@{
                            linearPara(
                                com.pt.common.stable.MATCH, com.pt.common.stable.WRAP, 0F
                            ) {
                                marginStart = dpToPx(30)
                                topMargin = dpToPx(10)
                                bottomMargin = dpToPx(10)
                            }
                            orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
                            seekTitle = androidx.appcompat.widget.AppCompatTextView(
                                context
                            ).apply {
                                linearPara(
                                    com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F
                                ) {
                                    marginEnd = dpToPx(60)
                                    gravity = android.view.Gravity.CENTER_VERTICAL
                                }
                                text = resources.getString(com.pt.pro.R.string.iz)
                                editAppearanceLarge()
                                setTextColor(rmoText)
                                setTextSize(
                                    android.util.TypedValue.COMPLEX_UNIT_PX, txtDim.toFloat()
                                )
                            }.also(this@anotherLin::addView)
                            androidx.appcompat.widget.LinearLayoutCompat(
                                context
                            ).apply seekLin@{
                                linearPara(
                                    com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F
                                ) {
                                    topMargin = dpToPx(10)
                                    bottomMargin = dpToPx(10)
                                }
                                orientation = androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
                                scaleNum = com.pt.common.moderator.over.ScalelessTextView(
                                    context
                                ).apply {
                                    linearPara(
                                        com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F
                                    ) {
                                        gravity = android.view.Gravity.CENTER_VERTICAL
                                    }
                                    editAppearanceLarge()
                                    setTextColor(rmoText)
                                    setTextSize(
                                        android.util.TypedValue.COMPLEX_UNIT_PX,
                                        resources.getDimension(com.pt.pro.R.dimen.dt)
                                    )
                                }.also(this@seekLin::addView)
                                pauseSeek = androidx.appcompat.widget.AppCompatSeekBar(
                                    context
                                ).apply {
                                    linearPara(
                                        findPixel(150), com.pt.common.stable.WRAP, 0F
                                    ) {
                                        gravity = android.view.Gravity.CENTER_VERTICAL
                                    }
                                }.also(this@seekLin::addView)
                                maxNum = com.pt.common.moderator.over.ScalelessTextView(
                                    context
                                ).apply {
                                    linearPara(
                                        com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F
                                    ) {
                                        gravity = android.view.Gravity.CENTER_VERTICAL
                                    }
                                    editAppearanceLarge()
                                    setTextColor(rmoText)
                                    setTextSize(
                                        android.util.TypedValue.COMPLEX_UNIT_PX,
                                        resources.getDimension(com.pt.pro.R.dimen.dt)
                                    )
                                }.also(this@seekLin::addView)
                            }.also(this@anotherLin::addView)
                        }.also(this@lin::addView)

                        fasten = SettingMusicFasten(
                            root_ = this@root_,
                            switchMusicScreen = switchMusicScreen.two,
                            showVoice = showVoice.two,
                            seekTitle = seekTitle,
                            scaleNum = scaleNum,
                            pauseSeek = pauseSeek,
                            maxNum = maxNum,
                        )
                    }.also(this@scroll::addView)
                }.also(this@card::addView)
            }.also(this@root_::addView)
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterTitle(): TitleScreenFasten {
        val fasten: TitleScreenFasten
        androidx.constraintlayout.widget.ConstraintLayout(this@inflaterTitle).apply root_@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                com.pt.common.stable.MATCH,
                com.pt.common.stable.MATCH
            ).also(this@root_::setLayoutParams)
            setBackgroundColor(0)
            val title = androidx.appcompat.widget.AppCompatButton(
                context
            ).apply {
                constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    bottomToBottom = 0
                    topToTop = 0
                    startToStart = 0
                }
                editAppearance()
                setBackgroundColor(0)
                gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                isAllCaps = false
                textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                setTextColor(textColorPrimary)
                singleNonScroll()
            }.also(this@root_::addView)
            fasten = TitleScreenFasten(
                root_ = this@root_,
                title = title,
            )
        }
        return fasten
    }

    fun destroyFasten() {
        destroyGlobalFasten()
    }
}