package com.pt.pro.musical.fasten

import com.pt.common.global.*

object MusicInflater : com.pt.common.mutual.life.GlobalInflater() {

    internal var dimTxtNative: Float? = null
    internal var dimTvsNative: Float? = null

    internal var rmoTextNative: Int? = null

    @JvmStatic
    internal inline val android.content.Context.txtColor: Int
        get() = rmoTextNative ?: rmoText
            .also { rmoTextNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.dimTxt: Float
        get() = dimTxtNative ?: getDimension(com.pt.pro.R.dimen.txt)
            .also { dimTxtNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.dimTvs: Float
        get() = dimTvsNative ?: getDimension(com.pt.pro.R.dimen.tvs)
            .also { dimTvsNative = it }

    @JvmStatic
    fun android.content.Context.inflaterActivityMusic(): ActivityMusicFasten {
        val fasten: ActivityMusicFasten
        frameLayoutParent root_@{
            setBackgroundColor(rmoBackHint)
            android.view.WindowManager.LayoutParams(
                com.pt.common.stable.MATCH,
                com.pt.common.stable.MATCH,
            ).also(this@root_::setLayoutParams)
            val backImg = this@root_.image {
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            }
            this@root_.linear linView@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                verticalLin()
                val cardView: androidx.cardview.widget.CardView
                val coverImg: androidx.appcompat.widget.AppCompatImageView
                val circleSeekBar: com.pt.common.moderator.recycler.CircularSeekBar
                this@linView.frame upperFrame@{
                    linearPara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH, 1F) {}
                    this@upperFrame.frame gfhFrame@{
                        resources.getDimension(com.pt.pro.R.dimen.gfh).toInt().also {
                            framePara(it, it) {
                                gravity = android.view.Gravity.CENTER
                            }
                        }
                        cardView = this@gfhFrame.card cardView@{
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                                dpToPx(10).also { d10 ->
                                    marginStart = d10
                                    marginEnd = d10
                                    topMargin = d10
                                    bottomMargin = d10
                                }
                            }
                            setCardBackgroundColor(fetchColor(com.pt.pro.R.color.fre))
                            radius = resources.getDimension(com.pt.pro.R.dimen.dih)
                            cardElevation = 0F
                            coverImg = this@cardView.image {
                                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                            }
                        }
                        circleSeekBar = com.pt.common.moderator.recycler.CircularSeekBar(
                            context
                        ).apply {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                            dpToPx(22).also { setPadding(it, it, it, it) }
                            circleStrokeWidth = dpToPx(5).toFloat()
                            pointerStrokeWidth = dpToPx(20).toFloat()
                            progress = 250F
                        }.also(this@gfhFrame::addView)
                    }
                }
                val musicArtist: androidx.appcompat.widget.AppCompatTextView
                val musicTitle: androidx.appcompat.widget.AppCompatTextView
                this@linView.constraint down@{
                    linearPara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH, 1.15F) {}
                    this@down.linear lin@{
                        constraintPara(0, com.pt.common.stable.WRAP) {
                            bottomToTop = com.pt.pro.R.id.returnMusic //playPause
                            endToEnd = com.pt.pro.R.id.lockBrowser //nextMusic
                            startToStart = com.pt.pro.R.id.deletePlaylist //previousMusic
                            topToTop = 0
                            verticalBias = 0.7F
                        }
                        verticalLin()
                        musicArtist = this@lin.textViewer {
                            linearPara(
                                com.pt.common.stable.MATCH,
                                com.pt.common.stable.MATCH,
                                1F
                            ) {}
                            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                            singleNonScroll()
                            gravity = android.view.Gravity.CENTER
                            marqueeRepeatLimit = -1
                            setTypeface(typeface, android.graphics.Typeface.BOLD)
                            textSize(resources.getDimension(com.pt.pro.R.dimen.ted))
                            textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                            setPaddingRelative(dpToPx(3), 0, 0, dpToPx(3))
                        }
                        musicTitle = this@lin.textViewer {
                            linearPara(
                                com.pt.common.stable.MATCH,
                                com.pt.common.stable.MATCH,
                                1F
                            ) {}
                            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                            singleNonScroll()
                            gravity = android.view.Gravity.CENTER
                            marqueeRepeatLimit = -1
                            setTypeface(typeface, android.graphics.Typeface.BOLD_ITALIC)
                            textSize(resources.getDimension(com.pt.pro.R.dimen.ctt))
                            textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                            setPaddingRelative(dpToPx(3), 0, 0, dpToPx(5))
                        }
                    }
                    val playPause = this@down.image {
                        id = com.pt.pro.R.id.returnMusic //playPause
                        resources.getDimension(com.pt.pro.R.dimen.wqt).toInt().also { wqt ->
                            constraintPara(wqt, wqt) {
                                bottomToBottom = 0
                                endToEnd = 0
                                startToStart = 0
                                topToTop = 0
                                verticalBias = 0.55F
                            }
                        }
                        dpToPx(5).also { d5 ->
                            setPadding(d5, d5, d5, d5)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval) {
                            background = this@compactImage
                        }
                        compactImage(com.pt.pro.R.drawable.ic_play_circle) {
                            setImageDrawable(this@compactImage)
                        }
                        svgReColor(textColorPrimary)
                    }
                    val previousMusic = this@down.image {
                        id = com.pt.pro.R.id.deletePlaylist //previousMusic
                        resources.getDimension(com.pt.pro.R.dimen.rle).toInt().also { wqt ->
                            constraintPara(wqt, wqt) {
                                bottomToBottom = com.pt.pro.R.id.returnMusic //playPause
                                endToStart = com.pt.pro.R.id.returnMusic //playPause
                                startToStart = 0
                                topToTop = com.pt.pro.R.id.returnMusic //playPause
                                horizontalBias = 0.65F
                            }
                        }
                        dpToPx(7).also { d5 ->
                            setPadding(d5, d5, d5, d5)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval) {
                            background = this@compactImage
                        }
                        compactImage(com.pt.pro.R.drawable.ic_previous_song_auto) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    val nextMusic = this@down.image {
                        id = com.pt.pro.R.id.lockBrowser //nextMusic
                        resources.getDimension(com.pt.pro.R.dimen.rle).toInt().also { wqt ->
                            constraintPara(wqt, wqt) {
                                bottomToBottom = com.pt.pro.R.id.returnMusic //playPause
                                endToEnd = 0
                                startToEnd = com.pt.pro.R.id.returnMusic //playPause
                                topToTop = com.pt.pro.R.id.returnMusic //playPause
                                horizontalBias = 0.35F
                            }
                        }
                        dpToPx(7).also { d5 ->
                            setPadding(d5, d5, d5, d5)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval) {
                            background = this@compactImage
                        }
                        compactImage(com.pt.pro.R.drawable.ic_next_song_auto) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    val unlockScreen = this@down.button {
                        constraintPara(
                            resources.getDimension(com.pt.pro.R.dimen.srh).toInt(),
                            com.pt.common.stable.WRAP
                        ) {
                            bottomToBottom = 0
                            endToEnd = 0
                            startToStart = 0
                            bottomMargin = dpToPx(20)
                        }
                        dpToPx(7).also { d5 ->
                            setPadding(d5, d5, d5, d5)
                        }
                        compactImage(com.pt.pro.R.drawable.item_empty_large) {
                            background = this@compactImage
                        }
                        backReColor(colorPrimary)
                        elevation = 6F
                        text = resources.getString(com.pt.pro.R.string.zj)
                        isAllCaps = false
                        setTextColor(rmoText)
                        textSize(resources.getDimension(com.pt.pro.R.dimen.txt))
                    }
                    fasten = ActivityMusicFasten(
                        root_ = this@root_,
                        backImg = backImg,
                        linView = this@linView,
                        cardView = cardView,
                        coverImg = coverImg,
                        circleSeekBar = circleSeekBar,
                        musicArtist = musicArtist,
                        musicTitle = musicTitle,
                        playPause = playPause,
                        previousMusic = previousMusic,
                        nextMusic = nextMusic,
                        unlockScreen = unlockScreen
                    )
                }
            }

        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterItemSong(): ItemSongFasten {
        val fasten: ItemSongFasten
        frameLayoutParent root_@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                com.pt.common.stable.MATCH,
                dpToPx(60)
            ).also(this@root_::setLayoutParams)
            this@root_.background = compactImageReturn(com.pt.pro.R.drawable.ripple_rectangle)

            val songImage = this@root_.image songImage@{
                dpToPx(45).also {
                    framePara(it, it) {
                        gravity = android.view.Gravity.START or
                                android.view.Gravity.CENTER_VERTICAL
                        marginStart = dpToPx(5)
                    }
                }
            }
            val songTitle = this@root_.scalelessText songTitle@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    marginStart = dpToPx(52)
                    topMargin = dpToPx(7)
                    marginEnd = dpToPx(3)
                }
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                isHorizontalScrollBarEnabled = false
                isNestedScrollingEnabled = false
                isVerticalScrollBarEnabled = false
                isSingleLine = true
                gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                textSize(resources.dimTxt)
                setTextColor(txtColor)
            }
            val songArtist = this@root_.scalelessText songArtist@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    marginStart = dpToPx(60)
                    topMargin = dpToPx(30)
                    marginEnd = dpToPx(5)
                }
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                singleNonScroll()
                gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                textSize(resources.dimTvs)
                setTextColor(txtColor)
            }
            fasten = ItemSongFasten(
                root_ = this@root_,
                songImage = songImage,
                songTitle = songTitle,
                songArtist = songArtist,
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterMusicHead(): MusicHeadFasten {
        val fasten: MusicHeadFasten
        frameLayoutParent root_@{
            android.view.WindowManager.LayoutParams(
                com.pt.common.stable.WRAP,
                com.pt.common.stable.WRAP
            ).also(this@root_::setLayoutParams)
            this@root_.frame circleFrame@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {}
                val pro = com.pt.common.moderator.recycler.CircleProgressBar(
                    context
                ).apply pro@{
                    dpToPx(79).also { d79 ->
                        framePara(d79, d79) {
                            gravity = android.view.Gravity.CENTER
                        }
                    }
                    setMax(360)
                    setMin(0)
                    setStrokeWidth(dpToPx(5).toFloat())
                }.also(this@circleFrame::addView)
                this@circleFrame.card circleCard@{
                    dpToPx(76).also { d76 ->
                        framePara(d76, d76) {
                            gravity = android.view.Gravity.CENTER
                        }
                    }
                    radius = dpToPx(38).toFloat()
                    preventCornerOverlap = true
                    setCardBackgroundColor(rmoBackHint)
                    val circleMusic = this@circleCard.touchImage {
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                        clickableView()
                    }
                    fasten = MusicHeadFasten(
                        root_ = this@root_,
                        circleFrame = this@circleFrame,
                        pro = pro,
                        circleCard = this@circleCard,
                        circleMusic = circleMusic
                    )
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterMusicHeadPlayer(): MusicHeadPlayerFasten {
        val fasten: MusicHeadPlayerFasten
        frameLayoutParent root_@{
            android.view.WindowManager.LayoutParams(
                com.pt.common.stable.WRAP,
                com.pt.common.stable.WRAP
            ).apply {
                gravity = android.view.Gravity.CENTER_VERTICAL
            }.also(this@root_::setLayoutParams)
            frameLayoutParent frameDetails@{
                framePara(dpToPx(210), dpToPx(270)) {}
                justInvisible()
                val playList = this@frameDetails.image {
                    dpToPx(40).also { d40 ->
                        framePara(d40, d40) {
                            marginStart = dpToPx(10)
                        }
                    }
                    dpToPx(7).let {
                        setPadding(it, it, it, it)
                    }
                    compactImage(com.pt.pro.R.drawable.circle) {
                        background = this@compactImage
                    }
                    compactImage(com.pt.pro.R.drawable.ic_play_list) {
                        setImageDrawable(this@compactImage)
                    }
                }
                val playMusic = this@frameDetails.image {
                    dpToPx(40).also { d40 ->
                        framePara(d40, d40) {
                            marginStart = dpToPx(80)
                        }
                    }
                    dpToPx(8).let {
                        setPadding(it, it, it, it)
                    }
                    compactImage(com.pt.pro.R.drawable.circle) {
                        background = this@compactImage
                    }
                    compactImage(com.pt.pro.R.drawable.ic_play_song) {
                        setImageDrawable(this@compactImage)
                    }
                }
                val nextMusic = this@frameDetails.image {
                    dpToPx(35).also { d35 ->
                        framePara(d35, d35) {
                            dpToPx(30).also { d30 ->
                                marginEnd = d30
                                bottomMargin = d30
                            }
                            gravity = android.view.Gravity.BOTTOM or
                                    android.view.Gravity.END
                        }
                    }
                    dpToPx(7).let {
                        setPadding(it, it, it, it)
                    }
                    compactImage(com.pt.pro.R.drawable.circle) {
                        background = this@compactImage
                    }
                    backReColor(colorPrimary)
                    compactImage(com.pt.pro.R.drawable.ic_next_song_auto) {
                        setImageDrawable(this@compactImage)
                    }
                }
                val refiner = this@frameDetails.image {
                    dpToPx(35).also { d35 ->
                        framePara(d35, d35) {
                            topMargin = dpToPx(80)
                            gravity = android.view.Gravity.END
                        }
                    }
                    dpToPx(4).let {
                        setPadding(it, it, it, it)
                    }
                    compactImage(com.pt.pro.R.drawable.circle) {
                        background = this@compactImage
                    }
                    backReColor(colorPrimary)
                    compactImage(com.pt.pro.R.drawable.ic_repeat_on) {
                        setImageDrawable(this@compactImage)
                    }
                }
                val hideCircle = this@frameDetails.image {
                    dpToPx(35).also { d35 ->
                        framePara(d35, d35) {
                            bottomMargin = dpToPx(80)
                            gravity = android.view.Gravity.BOTTOM or
                                    android.view.Gravity.END
                        }
                    }
                    dpToPx(4).let {
                        setPadding(it, it, it, it)
                    }
                    compactImage(com.pt.pro.R.drawable.circle) {
                        background = this@compactImage
                    }
                    backReColor(colorPrimary)
                    compactImage(com.pt.pro.R.drawable.ic_hidden) {
                        setImageDrawable(this@compactImage)
                    }
                    svgReColor(textColorPrimary)
                }
                val previousMusic = this@frameDetails.image {
                    dpToPx(35).also { d35 ->
                        framePara(d35, d35) {
                            dpToPx(30).also { d30 ->
                                topMargin = d30
                                marginEnd = d30
                            }
                            gravity = android.view.Gravity.END
                        }
                    }
                    dpToPx(7).let {
                        setPadding(it, it, it, it)
                    }
                    compactImage(com.pt.pro.R.drawable.circle) {
                        background = this@compactImage
                    }
                    backReColor(colorPrimary)
                    compactImage(com.pt.pro.R.drawable.ic_previous_song_auto) {
                        setImageDrawable(this@compactImage)
                    }
                }
                this@frameDetails.linearTint musicDetails@{
                    dpToPx(45).also { d45 ->
                        framePara(dpToPx(140), d45) {
                            marginStart = dpToPx(5)
                            topMargin = d45
                            gravity = android.view.Gravity.BOTTOM
                        }
                    }
                    verticalLin()
                    compactImage(com.pt.pro.R.drawable.card_music) {
                        background = this@compactImage
                    }
                    val musicArtist = this@musicDetails.textViewer musicArtist@{
                        androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
                            com.pt.common.stable.MATCH,
                            com.pt.common.stable.MATCH
                        ).apply {
                            weight = 1.0F
                        }.also(this@musicArtist::setLayoutParams)
                        ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                        isHorizontalScrollBarEnabled = false
                        isNestedScrollingEnabled = false
                        isVerticalScrollBarEnabled = false
                        isSingleLine = true
                        marqueeRepeatLimit = -1
                        gravity = android.view.Gravity.CENTER
                        textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                        setPaddingRelative(dpToPx(3), 0, dpToPx(5), 0)
                        setTypeface(typeface, android.graphics.Typeface.BOLD)
                        textSize(dpToPx(16))
                    }
                    val musicTitle = this@musicDetails.textViewer musicArtist@{
                        androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
                            com.pt.common.stable.MATCH,
                            com.pt.common.stable.MATCH
                        ).apply {
                            weight = 1.0F
                        }.also(this@musicArtist::setLayoutParams)
                        ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                        isHorizontalScrollBarEnabled = false
                        isNestedScrollingEnabled = false
                        isVerticalScrollBarEnabled = false
                        isSingleLine = true
                        marqueeRepeatLimit = -1
                        gravity = android.view.Gravity.CENTER
                        textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                        setPaddingRelative(dpToPx(3), 0, dpToPx(5), 0)
                        setTypeface(typeface, android.graphics.Typeface.ITALIC)
                        textSize(dpToPx(15).toFloat())
                    }
                    this@root_.addView(this@frameDetails)
                    val stubRecycler = this@root_.frame frameDetails@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    }
                    fasten = MusicHeadPlayerFasten(
                        root_ = this@root_,
                        frameDetails = this@frameDetails,
                        playList = playList,
                        playMusic = playMusic,
                        nextMusic = nextMusic,
                        refiner = refiner,
                        hideCircle = hideCircle,
                        previousMusic = previousMusic,
                        musicDetails = this@musicDetails,
                        musicArtist = musicArtist,
                        musicTitle = musicTitle,
                        stubRecycler = stubRecycler
                    )
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterMusicRecycler(): MusicRecyclerHeadFasten {
        val d3 = dpToPx(3)
        val d4 = dpToPx(4)
        val d10 = dpToPx(10)
        val fasten: MusicRecyclerHeadFasten
        cardViewParent root_@{
            framePara(dpToPx(220), dpToPx(400)) {}
            radius = dpToPx(15).toFloat()
            setCardBackgroundColor(rmoBackground)
            this@root_.frame layoutDetails@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                this@layoutDetails.constraint Constraint@{
                    framePara(com.pt.common.stable.MATCH, dpToPx(50)) {}
                    val returnMusic = this@Constraint.resizeImage returnMusic@{
                        id = com.pt.pro.R.id.returnMusic
                        androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                            com.pt.common.stable.WRAP,
                            com.pt.common.stable.MATCH
                        ).apply {
                            marginStart = dpToPx(2)
                            bottomToBottom = 0
                            startToStart = 0
                            topToTop = 0
                            topMargin = d4
                            bottomMargin = d4
                        }.also(::setLayoutParams)
                        d4.also {
                            setPadding(0, it, 0, it)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this@compactImage
                        }
                        compactImage(com.pt.pro.R.drawable.ic_back_arrow) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    val searchMusic = this@Constraint.editTextEvent searchMusic@{
                        androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                            0,
                            com.pt.common.stable.MATCH
                        ).apply {
                            endToStart = com.pt.pro.R.id.deletePlaylist
                            startToEnd = com.pt.pro.R.id.returnMusic
                            topMargin = d4
                            bottomMargin = d4
                        }.also(::setLayoutParams)
                        setPadding(0, d10, 0, d10)
                        isFocusableInTouchMode = true
                        hint = resources.getString(com.pt.pro.R.string.hr)
                        setTextColor(rmoText)
                        setHintTextColor(fetchColor(com.pt.pro.R.color.gry))
                        imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_DONE
                        maxLines = 1
                        isHorizontalScrollBarEnabled = false
                        isNestedScrollingEnabled = false
                        isVerticalScrollBarEnabled = false
                        isSingleLine = true
                        justGone()
                    }
                    val deletePlaylist = this@Constraint.resizeImage deletePlaylist@{
                        id = com.pt.pro.R.id.deletePlaylist
                        androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                            com.pt.common.stable.WRAP,
                            com.pt.common.stable.MATCH
                        ).apply {
                            marginEnd = d3
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.savePlaylist
                            topToTop = 0
                            topMargin = d4
                            bottomMargin = d4
                        }.also(::setLayoutParams)
                        d4.also {
                            setPadding(0, it, 0, it)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this@compactImage
                        }
                        compactImage(com.pt.pro.R.drawable.ic_delete_icon) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    val savePlaylist = this@Constraint.resizeImage savePlaylist@{
                        id = com.pt.pro.R.id.savePlaylist
                        androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                            com.pt.common.stable.WRAP,
                            com.pt.common.stable.MATCH
                        ).apply {
                            marginEnd = d3
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.searchButton
                            topToTop = 0
                            topMargin = d4
                            bottomMargin = d4
                        }.also(::setLayoutParams)
                        d4.also {
                            setPadding(0, it, 0, it)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this@compactImage
                        }
                        compactImage(com.pt.pro.R.drawable.ic_save_playlist) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    val searchButton = this@Constraint.resizeImage searchButton@{
                        id = com.pt.pro.R.id.searchButton
                        androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                            com.pt.common.stable.WRAP,
                            com.pt.common.stable.MATCH
                        ).apply {
                            marginEnd = d3
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.extendButton
                            topToTop = 0
                            topMargin = d4
                            bottomMargin = d4
                        }.also(::setLayoutParams)
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this@compactImage
                        }
                        compactImage(com.pt.pro.R.drawable.ic_search) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    val extendButton = this@Constraint.resizeImage extendButton@{
                        id = com.pt.pro.R.id.extendButton
                        androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                            com.pt.common.stable.WRAP,
                            com.pt.common.stable.MATCH
                        ).apply {
                            marginEnd = d3
                            bottomToBottom = 0
                            endToEnd = 0
                            topToTop = 0
                            topMargin = d4
                            bottomMargin = d4
                        }.also(::setLayoutParams)
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this@compactImage
                        }
                        compactImage(com.pt.pro.R.drawable.ic_extend) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    this@layoutDetails.constraint optionMusic@{
                        dpToPx(50).also { d50 ->
                            framePara(com.pt.common.stable.MATCH, d50) {
                                topMargin = d50
                            }
                        }
                        val showAlbums = this@optionMusic.resizeImage {
                            id = com.pt.pro.R.id.showAlbums
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                                com.pt.common.stable.WRAP,
                                com.pt.common.stable.MATCH
                            ).apply {
                                marginEnd = d3
                                bottomToBottom = 0
                                endToEnd = 0
                                startToStart = 0
                                topToTop = 0
                                topMargin = d4
                                bottomMargin = d4
                            }.also(::setLayoutParams)
                            setPadding(0, d10, 0, d10)
                            compactImage(com.pt.pro.R.drawable.card_seven) {
                                background = this@compactImage
                            }
                            compactImage(com.pt.pro.R.drawable.ic_album) {
                                setImageDrawable(this@compactImage)
                            }
                        }
                        this@optionMusic.frame {
                            id = com.pt.pro.R.id.listAllFrame
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                                1,
                                com.pt.common.stable.MATCH
                            ).apply {
                                marginEnd = d3
                                bottomToBottom = 0
                                endToEnd = 0
                                startToEnd = com.pt.pro.R.id.showAlbums
                                topToTop = 0
                            }.also(::setLayoutParams)
                        }
                        this@optionMusic.frame {
                            id = com.pt.pro.R.id.listArtistFrame
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                                com.pt.common.stable.WRAP,
                                com.pt.common.stable.MATCH
                            ).apply {
                                marginEnd = d3
                                bottomToBottom = 0
                                endToStart = com.pt.pro.R.id.showAlbums
                                startToStart = 0
                                topToTop = 0
                            }.also(::setLayoutParams)
                        }
                        val myPlaylist = this@optionMusic.resizeImage {
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                                com.pt.common.stable.WRAP,
                                com.pt.common.stable.MATCH
                            ).apply {
                                marginEnd = d3
                                bottomToBottom = 0
                                endToStart = com.pt.pro.R.id.listArtistFrame
                                startToStart = 0
                                topToTop = 0
                                topMargin = d4
                                bottomMargin = d4
                            }.also(::setLayoutParams)
                            setPadding(0, d10, 0, d10)
                            compactImage(com.pt.pro.R.drawable.card_seven) {
                                background = this@compactImage
                            }
                            compactImage(com.pt.pro.R.drawable.ic_play_list) {
                                setImageDrawable(this@compactImage)
                            }
                        }
                        val showArtists = this@optionMusic.resizeImage {
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                                com.pt.common.stable.WRAP,
                                com.pt.common.stable.MATCH
                            ).apply {
                                marginEnd = d3
                                bottomToBottom = 0
                                endToStart = com.pt.pro.R.id.showAlbums
                                startToEnd = com.pt.pro.R.id.listArtistFrame
                                topToTop = 0
                                topMargin = d4
                                bottomMargin = d4
                            }.also(::setLayoutParams)
                            setPadding(0, d10, 0, d10)
                            compactImage(com.pt.pro.R.drawable.card_seven) {
                                background = this@compactImage
                            }
                            compactImage(com.pt.pro.R.drawable.ic_artist) {
                                setImageDrawable(this@compactImage)
                            }
                        }
                        val showAllMusic = this@optionMusic.resizeImage {
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                                com.pt.common.stable.WRAP,
                                com.pt.common.stable.MATCH
                            ).apply {
                                marginEnd = d3
                                bottomToBottom = 0
                                endToStart = com.pt.pro.R.id.listAllFrame
                                startToEnd = com.pt.pro.R.id.showAlbums
                                topToTop = 0
                                topMargin = d4
                                bottomMargin = d4
                            }.also(::setLayoutParams)
                            setPadding(0, d10, 0, d10)
                            compactImage(com.pt.pro.R.drawable.card_seven) {
                                background = this@compactImage
                            }
                            compactImage(com.pt.pro.R.drawable.ic_all_file_icon) {
                                setImageDrawable(this@compactImage)
                            }
                        }
                        val showAllList = this@optionMusic.resizeImage {
                            androidx.constraintlayout.widget.ConstraintLayout.LayoutParams(
                                com.pt.common.stable.WRAP,
                                com.pt.common.stable.MATCH
                            ).apply {
                                marginEnd = d3
                                bottomToBottom = 0
                                endToEnd = 0
                                startToEnd = com.pt.pro.R.id.listAllFrame
                                topToTop = 0
                                topMargin = d4
                                bottomMargin = d4
                            }.also(::setLayoutParams)
                            setPadding(0, d10, 0, d10)
                            compactImage(com.pt.pro.R.drawable.card_seven) {
                                background = this@compactImage
                            }
                            compactImage(com.pt.pro.R.drawable.ic_all_playlist) {
                                setImageDrawable(this@compactImage)
                            }
                        }
                        this@layoutDetails.card cardRec@{
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                                topMargin = dpToPx(100)
                            }
                            setCardBackgroundColor(fetchColor(com.pt.pro.R.color.fre))
                            cardElevation = 0F
                            radius = 0F
                            this@cardRec.coordinator coordinator@{
                                framePara(
                                    com.pt.common.stable.MATCH,
                                    com.pt.common.stable.MATCH
                                ) {}
                                val recyclerSongs = this@coordinator.recycler recyclerSongs@{
                                    androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                        com.pt.common.stable.MATCH,
                                        com.pt.common.stable.MATCH
                                    ).apply {
                                        behavior =
                                            com.pt.common.moderator.recycler.OverScrollBounceBehavior()
                                    }.also(this@recyclerSongs::setLayoutParams)
                                }
                                val stubPlaylistFrame = this@root_.frame {
                                    framePara(
                                        com.pt.common.stable.MATCH,
                                        com.pt.common.stable.MATCH
                                    ) {}
                                    justGone()
                                }
                                fasten = MusicRecyclerHeadFasten(
                                    root_ = this@root_,
                                    cardFrame = this@root_,
                                    layoutDetails = this@layoutDetails,
                                    returnMusic = returnMusic,
                                    searchMusic = searchMusic,
                                    deletePlaylist = deletePlaylist,
                                    savePlaylist = savePlaylist,
                                    searchButton = searchButton,
                                    extendButton = extendButton,
                                    myPlaylist = myPlaylist,
                                    showArtists = showArtists,
                                    showAlbums = showAlbums,
                                    showAllMusic = showAllMusic,
                                    showAllList = showAllList,
                                    recyclerSongs = recyclerSongs,
                                    stubPlaylistFrame = stubPlaylistFrame
                                )
                            }
                        }
                    }
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterStubSavePlaylist(): StubSavePlaylistFasten {
        val fasten: StubSavePlaylistFasten
        frameLayoutParent root_@{
            setBackgroundColor(fetchColor(com.pt.pro.R.color.bla))
            val playlistEdit = this@root_.editTextEvent {
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                    dpToPx(5).also {
                        marginStart = it
                        marginEnd = it
                    }
                }
                hint = resources.getString(com.pt.pro.R.string.ba)
                setTextColor(rmoText)
                setHintTextColor(fetchColor(com.pt.pro.R.color.gry))
            }
            this@root_.linear lin@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    dpToPx(3).also {
                        bottomMargin = it
                        marginEnd = it
                        gravity = android.view.Gravity.BOTTOM or android.view.Gravity.END
                    }
                }
                val cancelEdit = this@lin.button cancelEdit@{
                    androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
                        com.pt.common.stable.WRAP,
                        com.pt.common.stable.WRAP
                    ).apply {
                        gravity = android.view.Gravity.END
                    }.also(this@cancelEdit::setLayoutParams)
                    dpToPx(10).also { d10 ->
                        dpToPx(5).also { d5 ->
                            setPaddingRelative(d10, d5, d10, d5)
                        }
                    }
                    compactImage(com.pt.pro.R.drawable.ripple_cur) {
                        background = this@compactImage
                    }
                    text = resources.getString(com.pt.pro.R.string.a5)
                }
                val saveEdit = this@lin.button cancelEdit@{
                    androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
                        com.pt.common.stable.WRAP,
                        com.pt.common.stable.WRAP
                    ).apply {
                        gravity = android.view.Gravity.END
                    }.also(this@cancelEdit::setLayoutParams)
                    dpToPx(10).also { d10 ->
                        dpToPx(5).also { d5 ->
                            setPaddingRelative(d10, d5, d10, d5)
                        }
                    }
                    compactImage(com.pt.pro.R.drawable.ripple_cur) {
                        background = this@compactImage
                    }
                    text = resources.getString(com.pt.pro.R.string.dn)
                }
                fasten = StubSavePlaylistFasten(
                    root_ = this@root_,
                    editCon = this@root_,
                    playlistEdit = playlistEdit,
                    cancelEdit = cancelEdit,
                    saveEdit = saveEdit
                )
            }
        }
        return fasten
    }

    @JvmStatic
    fun destroyFasten() {
        dimTxtNative = null
        dimTvsNative = null
        rmoTextNative = null
        destroyGlobalFasten()
    }
}