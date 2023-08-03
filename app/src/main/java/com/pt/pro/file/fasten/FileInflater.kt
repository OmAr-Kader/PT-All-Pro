package com.pt.pro.file.fasten

import com.pt.common.BuildConfig
import com.pt.common.global.*

object FileInflater : com.pt.common.mutual.life.GlobalInflater() {

    private var dimTxtNative: Float? = null
    private var dimTvsNative: Float? = null

    @JvmStatic
    @androidx.annotation.ColorInt
    internal var transNative: Int? = null

    @JvmStatic
    internal inline val trans: Int
        @androidx.annotation.ColorInt get() = transNative ?: android.graphics.Color.TRANSPARENT.also { transNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.dimTxt: Float
        get() = dimTxtNative ?: getDimensionsF(com.pt.pro.R.dimen.txt, 16F).also { dimTxtNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.dimTvs: Float
        get() = dimTvsNative ?: getDimensionsF(com.pt.pro.R.dimen.tvs, 12F).also { dimTvsNative = it }

    @JvmStatic
    fun android.content.Context.inflaterFileOptions(d32: Int, d4: Int): FileOptionsFasten {
        val fasten: FileOptionsFasten
        cardViewParent root_@{
            framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                gravity = android.view.Gravity.END
            }
            justGone()
            setCardBackgroundColor(rmoBackground)
            radius = dpToPx(10).toFloat()
            this@root_.linear lin@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    gravity = android.view.Gravity.END
                }
                horizontalLin()
                val hidden = this@lin.image hidden@{
                    linearPara(d32, d32, 1F) {
                        gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                        marginStart = d4
                        marginEnd = d4
                        topMargin = d4
                        bottomMargin = d4
                    }
                    compactImage(com.pt.pro.R.drawable.ripple_oval) {
                        background = this
                    }
                    compactImage(com.pt.pro.R.drawable.ic_hidden) {
                        setImageDrawable(this)
                    }
                }
                val searchFile = this@lin.image searchFile@{
                    linearPara(d32, d32, 1F) {
                        gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                        marginStart = d4
                        marginEnd = d4
                        topMargin = d4
                        bottomMargin = d4
                    }
                    compactImage(com.pt.pro.R.drawable.ripple_oval) {
                        background = this
                    }
                    compactImage(com.pt.pro.R.drawable.ic_search) {
                        setImageDrawable(this)
                    }
                }
                val pendingText: androidx.appcompat.widget.AppCompatTextView
                val pendingFrame = this@lin.frame pendingFrame@{
                    linearPara(d32, d32, 1F) {
                        gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                        marginStart = d4
                        marginEnd = d4
                        topMargin = d4
                        bottomMargin = d4
                    }
                    clickableView()
                    this@pendingFrame.image {
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                            gravity = android.view.Gravity.END
                        }
                        compactImage(com.pt.pro.R.drawable.ic_pending) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    pendingText = this@pendingFrame.textViewer {
                        dpToPx(18).also {
                            framePara(it, it) {
                                gravity = android.view.Gravity.END or android.view.Gravity.BOTTOM
                            }
                        }
                        compactImage(com.pt.pro.R.drawable.circle) {
                            background = this@compactImage
                        }
                        backReColor(colorAccent)
                        singleNonScroll()
                        ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                        gravity = android.view.Gravity.CENTER
                        textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                        setTextColor(textColorPrimary)
                        textSize(dpToPx(12))
                        justInvisible()
                    }
                }
                val createFolder = this@lin.image createFolder@{
                    dpToPx(35).also {
                        linearPara(it, it, 1F) {
                            gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                            marginStart = d4
                            marginEnd = d4
                            topMargin = d4
                            bottomMargin = d4
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_folder_create) {
                            setImageDrawable(this)
                        }
                    }
                }
                val reSort = this@lin.image {
                    id = com.pt.pro.R.id.searchButton
                    linearPara(d32, d32, 1F) {
                        gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                        marginStart = d4
                        marginEnd = d4
                        topMargin = d4
                        bottomMargin = d4
                    }
                    background = compactImageReturn(com.pt.pro.R.drawable.ripple_oval_card)
                    setImageDrawable(compactImageReturn(com.pt.pro.R.drawable.ic_re_sort))
                }
                fasten = FileOptionsFasten(
                    root_ = this@root_,
                    hidden = hidden,
                    searchFile = searchFile,
                    pendingFrame = pendingFrame,
                    pendingText = pendingText,
                    createFolder = createFolder,
                    reSort = reSort,
                )
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterFileShare(): FileShareFasten {
        val fasten: FileShareFasten
        cardViewParent root_@{
            framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {}
            setCardBackgroundColor(rmoBackHint)
            radius = dpToPx(25).toFloat()
            cardElevation = dpToPx(5).toFloat()
            this@root_.coordinator coordinator@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) { }
                this@coordinator.nestedScroll scrollCounter@{
                    androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                        com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                    ).apply {
                        behavior = com.pt.common.moderator.recycler.OverScrollBounceBehavior()
                    }.also(this@scrollCounter::setLayoutParams)
                    scrollBarSize = 0
                    val linearScroll = this@scrollCounter.linear linearScroll@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {}
                        verticalLin()
                        justInvisible()
                    }

                    fasten = FileShareFasten(
                        root_ = this@root_, scrollCounter = this@scrollCounter, linearScroll = linearScroll
                    )
                }

            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterFolderAdapter(): FolderFasten {
        val fasten: FolderFasten
        frameLayoutParent root_@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                com.pt.common.stable.MATCH,
                dpToPx(70)
            ).also(this@root_::setLayoutParams)
            val rowFile = this@root_.frame rowFile@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) { }
                background = compactImageReturn(com.pt.pro.R.drawable.ripple_rectangle)
            }
            val folderImage = this@root_.glideImage folderImage@{
                framePara(dpToPx(45)) {
                    gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                    marginStart = dpToPx(5)
                }
            }
            val fileName = this@root_.textViewer fileName@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                    marginStart = dpToPx(57)
                    topMargin = dpToPx(10)
                    marginEnd = dpToPx(30)
                }
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                isHorizontalScrollBarEnabled = false
                isNestedScrollingEnabled = false
                isVerticalScrollBarEnabled = false
                isSingleLine = true
                textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                textSize(resources.dimTxt)
            }
            val fileNumber = this@root_.textViewer fileNumber@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.BOTTOM or android.view.Gravity.START
                    marginStart = dpToPx(60)
                    bottomMargin = dpToPx(10)
                }
                textSize(resources.dimTvs)
            }
            val fileType = this@root_.textViewer fileType@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.BOTTOM or android.view.Gravity.END
                    marginEnd = dpToPx(15)
                    bottomMargin = dpToPx(10)
                }
                textSize(resources.dimTvs)
            }
            fasten = FolderFasten(
                this@root_,
                rowFile,
                folderImage,
                fileName,
                fileNumber,
                fileType,
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterFragmentFile(): FragmentFileFasten {
        val d4 = dpToPx(4)
        val d5 = dpToPx(5)
        val d10 = dpToPx(10)
        val textPrimary = theme.findAttr(android.R.attr.textColorPrimary)
        val fasten: FragmentFileFasten
        com.pt.common.moderator.over.FrameSizer(this@inflaterFragmentFile).apply root_@{
            id = com.pt.pro.R.id.searchButton
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            this@root_.frame subFileFrame@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    topMargin = resources.statusBarHeight
                }
                val hiddenTittleLinear: androidx.appcompat.widget.LinearLayoutCompat
                val fileBarCard: androidx.cardview.widget.CardView
                this@subFileFrame.constraint galleryCard@{
                    framePara(com.pt.common.stable.MATCH, actionBarHeight) {}
                    backReColor(theme.findAttr(android.R.attr.colorPrimary))
                    compactImage(com.pt.pro.R.drawable.card_back) {
                        background = this
                    }
                    val mainBack = this@galleryCard.resizeImage {
                        id = com.pt.pro.R.id.showAlbums
                        constraintPara(
                            com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                        ) {
                            marginStart = d5
                            bottomToBottom = 0
                            startToStart = 0
                            topToTop = 0
                            topMargin = d4
                            bottomMargin = d4
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ic_back_home) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    val picName: androidx.appcompat.widget.AppCompatTextView
                    val searchEdit: androidx.appcompat.widget.AppCompatEditText
                    this@galleryCard.frame fE@{
                        constraintPara(
                            0, com.pt.common.stable.MATCH
                        ) {
                            marginStart = d5
                            endToStart = com.pt.pro.R.id.extendButton//clipboardFrame
                            startToEnd = com.pt.pro.R.id.showAlbums//mainBack
                        }
                        picName = this@fE.textViewer {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.CENTER or android.view.Gravity.START
                            }
                            singleNonScroll()
                            setTextColor(textPrimary)
                            text = resources.getString(com.pt.pro.R.string.fq)
                            editAppearance()
                        }
                        searchEdit = this@fE.editText {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.CENTER or android.view.Gravity.START
                            }
                            singleNonScroll()
                            setTextColor(textPrimary)
                            imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_DONE
                            justGone()
                            editAppearance()
                        }
                    }
                    val clipboardImage: com.pt.common.moderator.over.ResizeImageView
                    val clipboardText: androidx.appcompat.widget.AppCompatTextView
                    val clipboardFrame = this@galleryCard.frame clipboardFrame@{
                        id = com.pt.pro.R.id.extendButton//clipboardFrame
                        constraintPara(
                            com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                        ) {
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.lockBrowser//swipeMode
                            topToTop = 0
                            topMargin = d4
                            bottomMargin = d4
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        clickableView()
                        justInvisible()
                        clipboardImage = this@clipboardFrame.resizeImage {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                                gravity = android.view.Gravity.END
                                dpToPx(2).also { d2 ->
                                    marginStart = d2
                                    marginEnd = d2
                                }
                            }
                            compactImage(com.pt.pro.R.drawable.ic_paste) {
                                setImageDrawable(this@compactImage)
                            }
                        }
                        clipboardText = this@clipboardFrame.textViewer {
                            dpToPx(20).also {
                                framePara(it, it) {
                                    gravity = android.view.Gravity.BOTTOM
                                    dpToPx(2).also { d2 ->
                                        marginStart = d2
                                        marginEnd = d2
                                    }
                                }
                            }
                            backReColor(theme.findAttr(android.R.attr.colorAccent))
                            compactImage(com.pt.pro.R.drawable.circle) {
                                background = this@compactImage
                            }
                            singleNonScroll()
                            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                            gravity = android.view.Gravity.CENTER
                            textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                            setTextColor(textPrimary)
                            textSize(dpToPx(12).toFloat())
                        }
                    }
                    val swipeMode = this@galleryCard.resizeImage {
                        id = com.pt.pro.R.id.lockBrowser
                        constraintPara(
                            com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                        ) {
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.savePlaylist//extendFile
                            topToTop = 0
                            topMargin = d4
                            bottomMargin = d4
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ic_swipe_file) {
                            setImageDrawable(this@compactImage)
                        }
                        justInvisible()
                    }
                    val extendFile = this@galleryCard.resizeImage {
                        id = com.pt.pro.R.id.savePlaylist
                        constraintPara(
                            com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                        ) {
                            bottomToBottom = 0
                            endToEnd = 0
                            topToTop = 0
                            topMargin = d4
                            bottomMargin = d4
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ic_extend_gallery) {
                            setImageDrawable(this@compactImage)
                        }
                    }
                    val includeShareSub: android.widget.FrameLayout
                    val includeOptionsStub: android.widget.FrameLayout
                    this@subFileFrame.frame fileFrame@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                            topMargin = actionBarHeight
                        }
                        val dSack: DSackT<android.widget.HorizontalScrollView, androidx.appcompat.widget.LinearLayoutCompat>
                        this@fileFrame.frame scrollRoot@{
                            framePara(com.pt.common.stable.MATCH, dpToPx(40)) {}
                            val scrollFile = this@scrollRoot.horizontalScroll {
                                framePara(
                                    com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                ) {}
                                contentDescription = resources.getString(
                                    com.pt.pro.R.string.a5
                                )
                            }
                            val filesLinear = scrollFile.linear {
                                framePara(
                                    com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                                ) {}
                                horizontalLin()
                            }
                            dSack = DSackT(scrollFile, filesLinear)
                        }
                        val recyclerFavorites: com.pt.common.moderator.recycler.RecyclerForViews
                        val codFav = this@fileFrame.frame codFav@{
                            framePara(
                                com.pt.common.stable.MATCH, dpToPx(100)
                            ) {
                                topMargin = dpToPx(40)
                            }
                            this@codFav.coordinator coordinator@{
                                framePara(
                                    com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                ) {}
                                recyclerFavorites = this@coordinator.recycler recyclerFavorites@{
                                    androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                        com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                    ).apply {
                                        behavior = com.pt.common.moderator.recycler.OverScrollHorizontalBehavior()
                                    }.also(this@recyclerFavorites::setLayoutParams)
                                }
                            }
                        }
                        this@fileFrame.frame subMangerFrame@{
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                                topMargin = dpToPx(140)
                            }
                            val recyclerFiles: com.pt.common.moderator.recycler.RecyclerForViews
                            this@subMangerFrame.coordinator coordinator@{
                                framePara(
                                    com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                ) {}
                                constraintPara(
                                    com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                ) {
                                    bottomToBottom = 0
                                    topToTop = 0
                                    startToStart = 0
                                    endToEnd = 0
                                }
                                recyclerFiles = this@coordinator.recycler recyclerFiles@{
                                    androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                        com.pt.common.stable.MATCH,
                                        com.pt.common.stable.MATCH,
                                    ).apply {
                                        behavior = com.pt.common.moderator.recycler.OverScrollBounceBehavior()
                                    }.also(this@recyclerFiles::setLayoutParams)
                                }
                            }
                            val forSnakes = this@subMangerFrame.frame forSnakes@{
                                framePara(
                                    com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                ) {
                                    gravity = android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL
                                }
                            }
                            val pointerScroll = this@subMangerFrame.card pointerScroll@{
                                dpToPx(35).also { d35 ->
                                    framePara(d35, d35) {
                                        gravity = android.view.Gravity.END
                                    }
                                }
                                clickableView()
                                contentDescription = resources.getString(
                                    com.pt.pro.R.string.ya
                                )
                                setCardBackgroundColor(android.graphics.Color.TRANSPARENT)
                                cardElevation = dpToPx(10).toFloat()
                                this@pointerScroll.image {
                                    framePara(
                                        com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                    ) {}
                                    compactImage(com.pt.pro.R.drawable.ic_scroll_pointer) {
                                        setImageDrawable(this@compactImage)
                                    }
                                }
                            }
                            val hiddenTittleClick: androidx.appcompat.widget.AppCompatTextView
                            hiddenTittleLinear = linearLayoutParent hiddenTittleLinear@{
                                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                                    gravity = android.view.Gravity.CENTER
                                }
                                justGone()
                                horizontalLin()
                                hiddenTittleClick = this@hiddenTittleLinear.textViewer hiddenTittleClick@{
                                    linearPara(
                                        com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F
                                    ) {}
                                    clickableView()
                                    text = resources.getString(com.pt.pro.R.string.ow)
                                    textSize(resources.getDimensions(com.pt.pro.R.dimen.txt, 15))
                                    setTextColor(colorAccent)
                                }
                                this@hiddenTittleLinear.textViewer hiddenTittle@{
                                    androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
                                        com.pt.common.stable.WRAP, com.pt.common.stable.WRAP
                                    ).apply {

                                    }.also(this@hiddenTittle::setLayoutParams)
                                    text = resources.getString(com.pt.pro.R.string.ag)
                                    textSize(resources.getDimensions(com.pt.pro.R.dimen.txt, 15))
                                    setTypeface(typeface, android.graphics.Typeface.BOLD)
                                    setTextColor(rmoText)
                                }
                            }
                            fileBarCard = cardViewParent galleryBarCard@{
                                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                                    gravity = android.view.Gravity.CENTER
                                }
                                dpToPx(10).toFloat().also {
                                    elevation = it
                                    radius = it
                                    cardElevation = it
                                }
                                justGone()
                                this@galleryBarCard.progressBar {
                                    resources.getDimension(
                                        com.pt.pro.R.dimen.rle
                                    ).toInt().also {
                                        framePara(it, it) {
                                            dpToPx(5).also { d5 ->
                                                marginStart = d5
                                                marginEnd = d5
                                                topMargin = d5
                                                bottomMargin = d5
                                            }
                                        }
                                    }
                                    compactImage(com.pt.pro.R.drawable.rotate_gallery) {
                                        indeterminateDrawable = this@compactImage
                                    }
                                }
                            }
                            includeShareSub = frameLayoutParent {
                                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                                    gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                                    dpToPx(40).also { d40 ->
                                        topMargin = d40
                                        bottomMargin = d40
                                    }
                                }
                            }
                            includeOptionsStub = frameLayoutParent {
                                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                                    gravity = android.view.Gravity.END
                                }
                            }
                            fasten = FragmentFileFasten(
                                root_ = this@root_,
                                subFileFrame = this@subFileFrame,
                                galleryCard = this@galleryCard,
                                mainBack = mainBack,
                                picName = picName,
                                searchEdit = searchEdit,
                                clipboardFrame = clipboardFrame,
                                clipboardImage = clipboardImage,
                                clipboardText = clipboardText,
                                swipeMode = swipeMode,
                                extendFile = extendFile,
                                fileFrame = this@fileFrame,
                                scrollFile = dSack.one,
                                filesLinear = dSack.two,
                                codFav = codFav,
                                recyclerFavorites = recyclerFavorites,
                                subMangerFrame = this@subMangerFrame,
                                recyclerFiles = recyclerFiles,
                                forSnakes = forSnakes,
                                pointerScroll = pointerScroll,
                                includeShareSub = includeShareSub,
                                includeOptionsStub = includeOptionsStub,
                                hiddenTittleLinear = hiddenTittleLinear,
                                hiddenTittleClick = hiddenTittleClick,
                                fileBarCard = fileBarCard
                            )
                        }
                        includeShareSub.also(this@fileFrame::addView)
                        includeOptionsStub.also(this@fileFrame::addView)
                    }
                }
                fileBarCard.also(this@subFileFrame::addView)
                hiddenTittleLinear.also(this@subFileFrame::addView)
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterFavor(): ItemFileFavorFasten {
        val fasten: ItemFileFavorFasten
        linearLayoutParent root_@{
            dpToPx(100).also {
                androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                    it, it
                ).apply {
                    dpToPx(3).also { d3 ->
                        marginStart = d3
                        marginEnd = d3
                    }
                }.also(this@root_::setLayoutParams)
            }
            clickableView()
            verticalLin()
            compactImage(com.pt.pro.R.drawable.ripple_cur) {
                background = this@compactImage
            }
            val folderImage: com.pt.common.moderator.over.GlideImageView
            val folderImageCard = this@root_.card folderImageCard@{
                dpToPx(60).also {
                    linearPara(it, it, 0F) {
                        dpToPx(3).also { d3 ->
                            marginStart = d3
                            marginEnd = d3
                        }
                        gravity = android.view.Gravity.CENTER_HORIZONTAL
                    }
                }
                setCardBackgroundColor(trans)
                cardElevation = 0F
                maxCardElevation = 0F
                radius = dpToPx(15).toFloat()
                folderImage = this@folderImageCard.glideImage {
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                }
            }
            val fileName = this@root_.scalelessText {
                linearPara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP, 0F) {}
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                singleNonScroll()
                gravity = android.view.Gravity.CENTER_HORIZONTAL
                textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                textSize(dpToPx(16))
            }
            val fileType = this@root_.scalelessText {
                linearPara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP, 0F) {}
                ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                singleNonScroll()
                gravity = android.view.Gravity.CENTER_HORIZONTAL
                textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                textSize(dpToPx(13))
            }
            fasten = ItemFileFavorFasten(
                root_ = this@root_,
                folderImageCard = folderImageCard,
                folderImage = folderImage,
                fileName = fileName,
                fileType = fileType,
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterPdf(): PdfBrowserFasten {
        val d4 = dpToPx(4)
        val d10 = dpToPx(10)
        val action = actionBarHeight
        val whi = android.graphics.Color.WHITE
        val fasten: PdfBrowserFasten
        com.sothree.slidinguppanel.SlidingUpPanelLayout(this@inflaterPdf).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            nonClickableView()
            setGravity(android.view.Gravity.BOTTOM)
            panelState = com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED
            panelHeight = 0
            shadowHeight = 0
            val emptyFrame = this@root_.frame emptyFrame@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            }
            this@root_.frame deFrame@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                nonClickableView()
                val imagePager: androidx.viewpager2.widget.ViewPager2
                this@deFrame.coordinator coordinator@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    imagePager = this@coordinator.pager imagePager@{
                        androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                            com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                        ).apply {
                            behavior = com.pt.common.moderator.recycler.OverScrollHorizontalBehavior()
                        }.also(this@imagePager::setLayoutParams)
                        setBackgroundColor(fetchColor(com.pt.pro.R.color.bla))
                        nonClickableView()
                        isNestedScrollingEnabled = true
                        orientation = androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
                        overScrollMode = 2
                        isHorizontalFadingEdgeEnabled = false
                        isVerticalScrollBarEnabled = false
                        @Suppress("DEPRECATION")
                        isAnimationCacheEnabled = false
                    }
                }
                val unLockImage = this@deFrame.touchImage {
                    framePara(action, action) {
                        gravity = android.view.Gravity.END
                    }
                    backReColor(fetchColor(com.pt.pro.R.color.msc))
                    compactImage(com.pt.pro.R.drawable.circle) {
                        background = this@compactImage
                    }
                    compactImage(com.pt.pro.R.drawable.ic_new_lock) {
                        setImageDrawable(this@compactImage)
                    }
                    dpToPx(12).also { d12 ->
                        setPadding(d12, d12, d12, d12)
                    }
                    justInvisible()
                    clickableView()
                    if (isV_M) {
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            foreground = this@compactImage
                        }
                    }
                }
                this@deFrame.frame head@{
                    framePara(com.pt.common.stable.MATCH, action + (action / 4)) {}
                    compactImage(com.pt.pro.R.drawable.pager_background) {
                        background = this@compactImage
                    }
                    setPadding(0, 0, 0, (action / 4))
                    this@head.constraint headInner@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                        val picName = this@headInner.textViewer picName@{
                            constraintPara(
                                0, com.pt.common.stable.WRAP
                            ) {
                                gravity = android.view.Gravity.CENTER or android.view.Gravity.START
                                marginStart = d10
                                marginEnd = dpToPx(20)
                                bottomToBottom = 0
                                endToEnd = 0
                                startToStart = 0
                                topToTop = 0
                            }
                            editAppearance()
                            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                            isHorizontalScrollBarEnabled = true
                            isSingleLine = true
                            textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                            setTextColor(whi)
                        }
                        this@deFrame.frame headDown@{
                            if (isV_R) {
                                this@head.justVisible()
                                this@headDown.justVisible()
                            } else {
                                this@head.justGone()
                                this@headDown.justGone()
                            }
                            framePara(com.pt.common.stable.MATCH, action + (action / 4)) {
                                gravity = android.view.Gravity.BOTTOM
                            }
                            setPadding(0, (action / 4), 0, 0)
                            compactImage(com.pt.pro.R.drawable.pager_background_reversed) {
                                background = this@compactImage
                            }
                            this@headDown.constraint headOptions@{
                                framePara(
                                    com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                ) {
                                    gravity = android.view.Gravity.BOTTOM
                                }
                                val pageNum = this@headOptions.textViewer {
                                    constraintPara(
                                        com.pt.common.stable.WRAP, com.pt.common.stable.WRAP
                                    ) {
                                        bottomToBottom = 0
                                        endToStart = com.pt.pro.R.id.showAlbums
                                        startToStart = 0
                                        topToTop = 0
                                        horizontalBias = 0.74F
                                    }
                                    isClickable = false
                                    gravity = android.view.Gravity.CENTER
                                    textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                                    setTextColor(whi)
                                    textSize(resources.getDimensions(com.pt.pro.R.dimen.txt, 15))
                                }
                                val prePage = this@headOptions.resizeImage {
                                    id = com.pt.pro.R.id.showAlbums
                                    constraintPara(
                                        com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                                    ) {
                                        topMargin = d4
                                        bottomMargin = d4
                                        bottomToBottom = 0
                                        startToStart = 0
                                        endToEnd = 0
                                        topToTop = 0
                                        horizontalBias = 0.33F
                                    }
                                    setPadding(0, d10, 0, d10)
                                    compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                        background = this@compactImage
                                    }
                                    svgReColor(whi)
                                    compactImage(com.pt.pro.R.drawable.ic_left_arrow) {
                                        setImageDrawable(this@compactImage)
                                    }
                                }
                                val nextPage = this@headOptions.resizeImage {
                                    constraintPara(
                                        com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                                    ) {
                                        topMargin = d4
                                        bottomMargin = d4
                                        bottomToBottom = 0
                                        startToStart = 0
                                        endToEnd = 0
                                        topToTop = 0
                                    }
                                    setPadding(0, d10, 0, d10)
                                    compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                        background = this@compactImage
                                    }
                                    svgReColor(whi)
                                    compactImage(com.pt.pro.R.drawable.ic_down_arrow) {
                                        setImageDrawable(this@compactImage)
                                    }
                                }
                                val rotateScreen = this@headOptions.resizeImage {
                                    constraintPara(
                                        com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                                    ) {
                                        topMargin = d4
                                        bottomMargin = d4
                                        bottomToBottom = 0
                                        startToStart = 0
                                        endToEnd = 0
                                        topToTop = 0
                                        horizontalBias = 0.67F
                                    }
                                    setPadding(0, d10, 0, d10)
                                    compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                        background = this@compactImage
                                    }
                                    svgReColor(whi)
                                    compactImage(com.pt.pro.R.drawable.ic_rotation_screen) {
                                        setImageDrawable(this@compactImage)
                                    }
                                }
                                val lockBrowser = this@headOptions.resizeImage {
                                    constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                                        topMargin = d4
                                        bottomMargin = d4
                                        bottomToBottom = 0
                                        startToStart = 0
                                        endToEnd = 0
                                        topToTop = 0
                                        horizontalBias = 0.84F
                                    }
                                    setPadding(0, d10, 0, d10)
                                    compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                        background = this@compactImage
                                    }
                                    svgReColor(whi)
                                    compactImage(com.pt.pro.R.drawable.ic_un_lock) {
                                        setImageDrawable(this@compactImage)
                                    }
                                }
                                fasten = PdfBrowserFasten(
                                    root_ = this@root_,
                                    emptyFrame = emptyFrame,
                                    imagePager = imagePager,
                                    head = this@head,
                                    headInner = this@headInner,
                                    lockBrowser = lockBrowser,
                                    picName = picName,
                                    unLockImage = unLockImage,
                                    headDown = this@headDown,
                                    headOptions = this@headOptions,
                                    pageNum = pageNum,
                                    prePage = prePage,
                                    nextPage = nextPage,
                                    rotateScreen = rotateScreen,
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
    fun android.content.Context.inflaterOption(): PopOptionFasten {
        val fasten: PopOptionFasten
        cardViewParent root_@{
            android.view.WindowManager.LayoutParams(
                com.pt.common.stable.WRAP, com.pt.common.stable.WRAP
            ).apply {
                gravity = android.view.Gravity.CENTER
            }.also(this@root_::setLayoutParams)
            setCardBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackHint))
            radius = dpToPx(25).toFloat()
            cardElevation = dpToPx(5).toFloat()
            this@root_.linear lin@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.CENTER
                }
                isBaselineAligned = false
                horizontalLin()
                val cardOne: androidx.cardview.widget.CardView
                val audioPic: androidx.appcompat.widget.AppCompatImageView
                val txtOne: androidx.appcompat.widget.AppCompatTextView
                val audioFrame = this@lin.frame audioFrame@{
                    linearPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH, 1F) {
                        gravity = android.view.Gravity.CENTER
                    }
                    dpToPx(5).also {
                        setPadding(0, it, 0, it)
                    }
                    compactImage(com.pt.pro.R.drawable.ripple_cur) {
                        background = this@compactImage
                    }
                    cardOne = this@audioFrame.card cardOne@{
                        framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                            gravity = android.view.Gravity.CENTER_HORIZONTAL
                        }
                        setCardBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackHint))
                        radius = dpToPx(15).toFloat()
                        cardElevation = 0F
                        audioPic = this@cardOne.image {
                            dpToPx(100).also {
                                framePara(it, it) {}
                            }
                            scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                        }
                    }
                    txtOne = this@audioFrame.textViewer {
                        framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                            gravity = android.view.Gravity.CENTER_HORIZONTAL
                            topMargin = dpToPx(110)
                        }
                        text = resources.getString(com.pt.pro.R.string.xk)
                    }
                }
                val cardTwo: androidx.cardview.widget.CardView
                val musicPic: androidx.appcompat.widget.AppCompatImageView
                val txtTwo: androidx.appcompat.widget.AppCompatTextView
                val musicFrame = this@lin.frame audioFrame@{
                    linearPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH, 1F) {
                        gravity = android.view.Gravity.CENTER
                    }
                    dpToPx(5).also {
                        setPadding(0, it, 0, it)
                    }
                    compactImage(com.pt.pro.R.drawable.ripple_cur) {
                        background = this@compactImage
                    }
                    cardTwo = this@audioFrame.card cardOne@{
                        framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                            gravity = android.view.Gravity.CENTER_HORIZONTAL
                        }
                        setCardBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackHint))
                        radius = dpToPx(50).toFloat()
                        cardElevation = 0F
                        musicPic = this@cardOne.image {
                            dpToPx(100).also {
                                framePara(it, it) {}
                            }
                            scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                        }
                    }
                    txtTwo = this@audioFrame.textViewer {
                        framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                            gravity = android.view.Gravity.CENTER_HORIZONTAL
                            topMargin = dpToPx(110)
                        }
                        text = resources.getString(com.pt.pro.R.string.mk)
                    }
                }
                fasten = PopOptionFasten(
                    root_ = this@root_,
                    cardOne = cardOne,
                    audioPic = audioPic,
                    txtOne = txtOne,
                    audioFrame = audioFrame,
                    cardTwo = cardTwo,
                    musicPic = musicPic,
                    txtTwo = txtTwo,
                    musicFrame = musicFrame
                )
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterTextViewer(): TextViewerFasten {
        val d4 = dpToPx(4)
        val d10 = dpToPx(10)
        val act = actionBarHeight
        val rmoText = theme.findAttr(com.pt.pro.R.attr.rmoText)
        val fasten: TextViewerFasten
        com.pt.common.moderator.over.FrameSizer(this@inflaterTextViewer).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            setBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackground))
            fitsSystemWindows = true
            clickableView()
            this@root_.frame head@{
                framePara(com.pt.common.stable.MATCH, act) {}
                this@head.constraint headInner@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    val mainBack = this@headInner.resizeImage {
                        id = com.pt.pro.R.id.lockBrowser
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginStart = dpToPx(5)
                            dpToPx(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            startToStart = 0
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_back_arrow) {
                            setImageDrawable(this)
                        }
                        svgReColor(rmoText)
                    }
                    val picName: androidx.appcompat.widget.AppCompatTextView
                    val searchName: com.pt.common.moderator.over.EditTextBackEvent
                    this@headInner.frame frame@{
                        constraintPara(0, com.pt.common.stable.MATCH) {
                            topToTop = 0
                            startToEnd = com.pt.pro.R.id.lockBrowser // mainBack
                            endToStart = com.pt.pro.R.id.searchButton//downText
                            bottomToBottom = 0
                        }
                        picName = this@frame.textViewer {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                                marginStart = d10
                                marginEnd = dpToPx(20)
                            }
                            editAppearance()
                            singleNonScroll()

                            ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                            textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                            setTextColor(rmoText)
                        }
                        searchName = this@frame.editTextEvent {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                                marginStart = d10
                                marginEnd = dpToPx(20)
                            }
                            editAppearance()
                            singleNonScroll()
                            justGone()
                            textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                            setTextColor(rmoText)
                            //setTextIsSelectable(true)
                        }
                    }
                    val downText = this@headInner.resizeImage {
                        id = com.pt.pro.R.id.searchButton
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginEnd = dpToPx(3)
                            dpToPx(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.returnMusic // upText
                        }
                        setPadding(0, d10, 0, d10)
                        justInvisible()
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_down_arrow) {
                            setImageDrawable(this)
                        }
                        svgReColor(rmoText)
                        rotation = 90F
                    }
                    val upText = this@headInner.resizeImage {
                        id = com.pt.pro.R.id.returnMusic
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginEnd = dpToPx(3)
                            dpToPx(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.deletePlaylist // searchText
                        }
                        setPadding(0, d10, 0, d10)
                        justInvisible()
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_down_arrow) {
                            setImageDrawable(this)
                        }
                        svgReColor(rmoText)
                        rotation = -90F
                    }
                    val searchText = this@headInner.resizeImage {
                        id = com.pt.pro.R.id.deletePlaylist
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginEnd = dpToPx(3)
                            dpToPx(4).also { d4 ->
                                topMargin = d4
                                bottomMargin = d4
                            }
                            topToTop = 0
                            bottomToBottom = 0
                            endToEnd = 0
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_search) {
                            setImageDrawable(this)
                        }
                        svgReColor(rmoText)
                    }
                    val textViewer: androidx.appcompat.widget.AppCompatEditText
                    val scroll: androidx.core.widget.NestedScrollView
                    val frameRec = this@root_.constraint frameRec@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                            topMargin = act
                        }
                        this@frameRec.frame fr@{
                            constraintPara(
                                com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                            ) {
                                topToTop = 0
                                bottomToBottom = 0
                                endToEnd = 0
                                startToStart = 0
                            }
                            this@fr.coordinator coordinator@{
                                framePara(
                                    com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                ) {}
                                setPaddingRelative(dpToPx(23), 0, d10, 0)
                                scroll = this@coordinator.nestedScroll scroll@{
                                    androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                        com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                    ).apply {
                                        behavior = com.pt.common.moderator.recycler.OverScrollBounceBehavior()
                                    }.also(this@scroll::setLayoutParams)
                                    scrollBarSize = 0
                                    textViewer = this@scroll.editText {
                                        framePara(
                                            com.pt.common.stable.MATCH, com.pt.common.stable.WRAP
                                        ) {

                                        }
                                        setBackgroundColor(android.graphics.Color.TRANSPARENT)
                                        minHeight = dpToPx(48)
                                        inputType = android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE or
                                                android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or
                                                android.text.InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE or
                                                android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                                        textSize(resources.getDimension(com.pt.pro.R.dimen.tle))
                                        isSingleLine = false
                                        setTextColor(rmoText)

                                    }
                                }
                            }
                        }
                    }
                    this@root_.frame headDown@{
                        framePara(com.pt.common.stable.MATCH, act) {
                            gravity = android.view.Gravity.BOTTOM
                        }
                        this@headDown.constraint headOptions@{
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                                gravity = android.view.Gravity.BOTTOM
                            }
                            val saveChanges = this@headOptions.resizeImage {
                                id = com.pt.pro.R.id.showAlbums
                                constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                                    topMargin = d4
                                    bottomMargin = d4
                                    bottomToBottom = 0
                                    startToStart = 0
                                    endToEnd = 0
                                    topToTop = 0
                                    horizontalBias = 0.18F
                                }
                                setPadding(0, d10, 0, d10)
                                compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                    background = this@compactImage
                                }
                                svgReColor(rmoText)
                                compactImage(com.pt.pro.R.drawable.ic_send) {
                                    setImageDrawable(this@compactImage)
                                }
                                justGone()
                            }
                            val zoomOut = this@headOptions.resizeImage {
                                id = com.pt.pro.R.id.showAlbums
                                constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                                    topMargin = d4
                                    bottomMargin = d4
                                    bottomToBottom = 0
                                    startToStart = 0
                                    endToEnd = 0
                                    topToTop = 0
                                    horizontalBias = 0.33F
                                }
                                setPadding(0, d10, 0, d10)
                                compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                    background = this@compactImage
                                }
                                svgReColor(rmoText)
                                compactImage(com.pt.pro.R.drawable.ic_minus) {
                                    setImageDrawable(this@compactImage)
                                }
                            }
                            val zoomIn = this@headOptions.resizeImage {
                                constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                                    topMargin = d4
                                    bottomMargin = d4
                                    bottomToBottom = 0
                                    startToStart = 0
                                    endToEnd = 0
                                    topToTop = 0
                                }
                                setPadding(0, d10, 0, d10)
                                compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                    background = this@compactImage
                                }
                                svgReColor(rmoText)
                                compactImage(com.pt.pro.R.drawable.ic_plus) {
                                    setImageDrawable(this@compactImage)
                                }
                            }
                            val rotateScreen = this@headOptions.resizeImage {
                                constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                                    topMargin = d4
                                    bottomMargin = d4
                                    bottomToBottom = 0
                                    startToStart = 0
                                    endToEnd = 0
                                    topToTop = 0
                                    horizontalBias = 0.67F
                                }
                                setPadding(0, d10, 0, d10)
                                compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                    background = this@compactImage
                                }
                                svgReColor(rmoText)
                                compactImage(com.pt.pro.R.drawable.ic_rotation_screen) {
                                    setImageDrawable(this@compactImage)
                                }
                            }
                            val pointerScroll: androidx.cardview.widget.CardView
                            val frameForScroll = this@root_.frame frameForScroll@{
                                framePara(
                                    com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                                ) {
                                    topMargin = act
                                    gravity = android.view.Gravity.END
                                }
                                pointerScroll = this@frameForScroll.card pointerScroll@{
                                    dpToPx(35).also { d35 ->
                                        framePara(d35, d35) {
                                            gravity = android.view.Gravity.END
                                        }
                                    }
                                    clickableView()
                                    contentDescription = resources.getString(
                                        com.pt.pro.R.string.ya
                                    )
                                    setCardBackgroundColor(android.graphics.Color.TRANSPARENT)
                                    cardElevation = d10.toFloat()
                                    this@pointerScroll.image {
                                        framePara(
                                            com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                        ) {}
                                        compactImage(com.pt.pro.R.drawable.ic_scroll_pointer) {
                                            setImageDrawable(this@compactImage)
                                        }
                                    }
                                }
                            }
                            fasten = TextViewerFasten(
                                root_ = this@root_,
                                head = this@head,
                                headInner = this@headInner,
                                mainBack = mainBack,
                                picName = picName,
                                searchName = searchName,
                                downText = downText,
                                upText = upText,
                                searchText = searchText,
                                frameRec = frameRec,
                                scroll = scroll,
                                textViewer = textViewer,
                                saveChanges = saveChanges,
                                headDown = this@headDown,
                                headOptions = this@headOptions,
                                zoomOut = zoomOut,
                                zoomIn = zoomIn,
                                rotateScreen = rotateScreen,
                                frameForScroll = frameForScroll,
                                pointerScroll = pointerScroll,
                            )
                        }
                    }
                }
            }

        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterListOption(isArch: Boolean): ListOptionFasten {
        val fasten: ListOptionFasten
        linearLayoutParent root_@{
            linearPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F) {}
            orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
            val backOpt = inflaterOptionItem(com.pt.pro.R.drawable.ic_back_arrow, BuildConfig.BACK_ARROW).apply {
                context?.compactImage(com.pt.pro.R.drawable.circle) {
                    two.background = this@compactImage
                }
                two.backReColor(colorPrimary)
            }.one.also(this@root_::addView)
            val favorite = if (isArch)
                null
            else
                inflaterOptionItem(com.pt.pro.R.drawable.ic_is_favorite, BuildConfig.FAVORITE).one.also(this@root_::addView)
            val unFavorite = if (isArch)
                null
            else
                inflaterOptionItem(com.pt.pro.R.drawable.ic_not_favorite, BuildConfig.UN_FAVOR).one.also(this@root_::addView)
            val addVir = if (isArch)
                null
            else
                inflaterOptionItem(com.pt.pro.R.drawable.ic_add, BuildConfig.ADD_VIR).one.also(this@root_::addView)
            val removeVir = if (isArch)
                null
            else
                inflaterOptionItem(com.pt.pro.R.drawable.ic_minus, BuildConfig.REMOVE_VIR).one.also(this@root_::addView)
            val openWith = inflaterOptionItem(com.pt.pro.R.drawable.ic_open_with, BuildConfig.OPEN_WITH).one.also(this@root_::addView)
            val openAs = inflaterOptionItem(com.pt.pro.R.drawable.ic_open_as, BuildConfig.OPEN_AS).one.also(this@root_::addView)
            val shareOpt = inflaterOptionItem(com.pt.pro.R.drawable.ic_share_option, BuildConfig.SHARE_OPT).one.also(this@root_::addView)
            val addPend = inflaterOptionItem(com.pt.pro.R.drawable.ic_pending, BuildConfig.ADD_PENDING).one.also(this@root_::addView)
            val deleteOpt = if (isArch)
                null
            else
                inflaterOptionItem(com.pt.pro.R.drawable.ic_delete_icon, BuildConfig.DELETE_OPT).one.also(this@root_::addView)
            val properties = if (isArch)
                null
            else
                inflaterOptionItem(com.pt.pro.R.drawable.ic_properties, BuildConfig.PROPERTIES_OPT).one.also(this@root_::addView)
            val selectAll = inflaterOptionItem(com.pt.pro.R.drawable.ic_select_all, BuildConfig.SELECT_OPT).one.also(this@root_::addView)
            val renameOpt = if (isArch)
                null
            else
                inflaterOptionItem(com.pt.pro.R.drawable.ic_renam_icon, BuildConfig.RENAME_OPT).one.also(this@root_::addView)
            val hide = if (isArch)
                null
            else
                inflaterOptionItem(com.pt.pro.R.drawable.ic_hidden, BuildConfig.HIDE_UN_OPT).one.also(this@root_::addView)
            val copy = inflaterOptionItem(com.pt.pro.R.drawable.ic_copy_option, BuildConfig.COPY_OPT).one.also(this@root_::addView)
            val move = if (isArch)
                null
            else
                inflaterOptionItem(com.pt.pro.R.drawable.ic_move_icon, BuildConfig.MOVE_OPT).one.also(this@root_::addView)

            fasten = ListOptionFasten(
                root_ = this@root_,
                backOpt = backOpt,
                favorite = favorite,
                unFavorite = unFavorite,
                addVir = addVir,
                removeVir = removeVir,
                openWith = openWith,
                openAs = openAs,
                shareOpt = shareOpt,
                addPend = addPend,
                deleteOpt = deleteOpt,
                properties = properties,
                selectAll = selectAll,
                renameOpt = renameOpt,
                hide = hide,
                copy = copy,
                move = move,
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterOpenAsOption(): ListOpenAsFasten {
        val fasten: ListOpenAsFasten
        linearLayoutParent root_@{
            linearPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F) {}
            orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
            val c = colorAccent
            val backOpt = inflaterOptionItem(com.pt.pro.R.drawable.ic_back_arrow, colorPrimary, BuildConfig.BACK_ARROW).one.also(this@root_::addView)
            val textAs = inflaterOptionItem(com.pt.pro.R.drawable.ic_text_file_icon, c, BuildConfig.TEXT_FIELD).one.also(this@root_::addView)
            val imageAs = inflaterOptionItem(com.pt.pro.R.drawable.ic_image_file_icon, c, BuildConfig.IMAGE_FIELD).one.also(this@root_::addView)
            val audioAs = inflaterOptionItem(com.pt.pro.R.drawable.ic_audio_filter, c, BuildConfig.AUDIO_FIELD).one.also(this@root_::addView)
            val videoAs = inflaterOptionItem(com.pt.pro.R.drawable.ic_video_filter, c, BuildConfig.VIDEO_FIELD).one.also(this@root_::addView)
            val allAs = inflaterOptionItem(com.pt.pro.R.drawable.ic_all_file_icon, c, BuildConfig.ALL_FIELD).one.also(this@root_::addView)
            fasten = ListOpenAsFasten(
                root_ = this@root_,
                backOpt = backOpt,
                textAs = textAs,
                imageAs = imageAs,
                audioAs = audioAs,
                videoAs = videoAs,
                allAs = allAs,
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterNoteOption(haveText: Boolean): ListNoteFasten {
        val fasten: ListNoteFasten
        linearLayoutParent root_@{
            linearPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F) {}
            orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
            val c = colorPrimary
            val backOpt = inflaterOptionItem(com.pt.pro.R.drawable.ic_back_arrow, c, BuildConfig.BACK_ARROW).one.also(this@root_::addView)
            val not = if (haveText)
                inflaterOptionItem(com.pt.pro.R.drawable.ic_not, c, BuildConfig.NOTIFICATION_OPT).one.also(this@root_::addView)
            else
                null
            val copy = if (haveText)
                inflaterOptionItem(com.pt.pro.R.drawable.ic_content_copy, c, BuildConfig.COPY_OPT).one.also(this@root_::addView)
            else
                null
            val share = inflaterOptionItem(com.pt.pro.R.drawable.ic_share_option, c, BuildConfig.SHARE_OPT).apply {
                two.svgReColor(textColorPrimary)
            }.one.also(this@root_::addView)
            fasten = ListNoteFasten(
                root_ = this@root_,
                backOpt = backOpt,
                not = not,
                copy = copy,
                share = share,
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterOptionItem(
        @androidx.annotation.DrawableRes icon: Int,
        nameOpt: String
    ): DSack<android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView, androidx.appcompat.widget.AppCompatTextView> {
        val fasten: DSack<android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView, androidx.appcompat.widget.AppCompatTextView>
        frameLayoutParent root_@{
            linearPara(dpToPx(127), com.pt.common.stable.WRAP, 0F) {
                dpToPx(10).also {
                    topMargin = it
                    bottomMargin = it
                }
            }
            compactImage(com.pt.pro.R.drawable.ripple_curvy) {
                background = this@compactImage
            }
            clickableView()
            val iconApp = this@root_.image iconApp@{
                framePara(dpToPx(40)) {
                    gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                    dpToPx(5).also { d5 ->
                        topMargin = d5
                        bottomMargin = d5
                        marginStart = d5
                        marginEnd = d5
                    }
                }
                dpToPx(3).also { d3 ->
                    setPadding(d3, d3, d3, d3)
                }
                context?.compactImage(icon) {
                    setImageDrawable(this@compactImage)
                }
                scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
            }
            val txtApp = this@root_.textViewer {
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.START or
                            android.view.Gravity.CENTER_VERTICAL
                    marginStart = dpToPx(55)
                }
                setTextColor(rmoText)
                maxWidth = dpToPx(70)
                textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                text = nameOpt
            }
            fasten = DSack<android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView, androidx.appcompat.widget.AppCompatTextView>(
                one = this@root_,
                two = iconApp,
                three = txtApp,
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterOptionItem(
        @androidx.annotation.DrawableRes icon: Int,
        @androidx.annotation.ColorInt t: Int,
        nameOpt: String,
    ): DSack<android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView, androidx.appcompat.widget.AppCompatTextView> {
        val fasten: DSack<android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView, androidx.appcompat.widget.AppCompatTextView>
        frameLayoutParent root_@{
            linearPara(dpToPx(127), com.pt.common.stable.WRAP, 0F) {
                dpToPx(10).also {
                    topMargin = it
                    bottomMargin = it
                }
            }
            compactImage(com.pt.pro.R.drawable.ripple_curvy) {
                background = this@compactImage
            }
            clickableView()
            val iconApp = this@root_.image iconApp@{
                framePara(dpToPx(40)) {
                    gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                    dpToPx(5).also { d5 ->
                        topMargin = d5
                        bottomMargin = d5
                        marginStart = d5
                        marginEnd = d5
                    }
                }
                dpToPx(3).also { d3 ->
                    setPadding(d3, d3, d3, d3)
                }
                context?.compactImage(com.pt.pro.R.drawable.circle) {
                    background = this@compactImage
                }
                backReColor(t)
                context?.compactImage(icon) {
                    setImageDrawable(this@compactImage)
                }
                scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
            }
            val txtApp = this@root_.textViewer {
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.START or
                            android.view.Gravity.CENTER_VERTICAL
                    marginStart = dpToPx(55)
                }
                setTextColor(rmoText)
                maxWidth = dpToPx(70)
                textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                text = nameOpt
            }
            fasten = DSack<android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView, androidx.appcompat.widget.AppCompatTextView>(
                one = this@root_,
                two = iconApp,
                three = txtApp,
            )
        }
        return fasten
    }

    @JvmStatic
    fun destroyFasten() {
        dimTxtNative = null
        dimTvsNative = null
        transNative = null
        destroyGlobalFasten()
    }
}