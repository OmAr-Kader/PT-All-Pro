package com.pt.pro.gallery.fasten

import androidx.annotation.StringRes
import com.pt.common.global.*

object GalleryInflater : com.pt.common.mutual.life.GlobalInflater() {

    internal var txtNative: Float? = null

    internal var itemBackNative: android.graphics.drawable.Drawable? = null
    internal var playSvgNative: android.graphics.drawable.Drawable? = null
    internal var sendScannerNative: android.graphics.drawable.Drawable? = null
    internal var circleBackNative: android.graphics.drawable.Drawable? = null
    internal var circleBackSelectNative: android.graphics.drawable.Drawable? = null

    @androidx.annotation.ColorInt
    internal var backColorNative: Int? = null

    @androidx.annotation.ColorInt
    internal var whiteNative: Int? = null

    @androidx.annotation.ColorInt
    internal var backHintNative: Int? = null

    @androidx.annotation.ColorInt
    internal var primaryColorNative: Int? = null

    @androidx.annotation.ColorInt
    internal var primaryTxtNative: Int? = null

    private var bimDimNative: Int? = null
    private var hntDimNative: Int? = null
    private var gt3DimNative: Int? = null
    private var txtDimNative: Float? = null
    private var txtColorNative: Int? = null
    private var backHintColorNative: Int? = null

    private var voDesNative: String? = null
    private var gp1DimNative: Int? = null
    private var brightnessOnNative: android.graphics.drawable.Drawable? = null
    private var volumeOffNative: android.graphics.drawable.Drawable? = null
    private var emptyProgressNative: android.graphics.drawable.Drawable? = null
    private var videoPointerNative: android.graphics.drawable.Drawable? = null

    @androidx.annotation.ColorInt
    private var grBroNative: Int? = null

    @androidx.annotation.ColorInt
    private var ordNative: Int? = null

    @JvmStatic
    internal inline val android.content.Context.emptyProgress: android.graphics.drawable.Drawable?
        get() = emptyProgressNative ?: compactImageReturn(com.pt.pro.R.drawable.empty_progress).also { emptyProgressNative = it }

    @JvmStatic
    internal inline val android.content.Context.videoPointer: android.graphics.drawable.Drawable?
        get() = videoPointerNative ?: compactImageReturn(com.pt.pro.R.drawable.ic_video_pointer).also { videoPointerNative = it }

    @JvmStatic
    internal inline val android.content.Context.brightnessOn: android.graphics.drawable.Drawable?
        get() = brightnessOnNative ?: compactImageReturn(com.pt.pro.R.drawable.ic_screen_brightness).also { brightnessOnNative = it }

    @JvmStatic
    internal inline val android.content.Context.volumeOff: android.graphics.drawable.Drawable?
        get() = volumeOffNative ?: compactImageReturn(com.pt.pro.R.drawable.ic_volume_off).also { volumeOffNative = it }

    @JvmStatic
    internal inline val ord: Int
        @androidx.annotation.ColorInt get() = ordNative ?: android.graphics.Color.argb(255, 255, 0, 0).also { ordNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.voDes: String
        get() = voDesNative ?: getString(com.pt.pro.R.string.vo).also { voDesNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.gp1Dim: Int
        get() = gp1DimNative ?: getDimension(com.pt.pro.R.dimen.gp1).toInt().also { gp1DimNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.gt3Dim: Int
        get() = gt3DimNative ?: getDimension(com.pt.pro.R.dimen.gt3).toInt().also { gt3DimNative = it }


    @JvmStatic
    internal inline val grBro: Int
        @androidx.annotation.ColorInt get() = grBroNative ?: android.graphics.Color.argb(77, 9, 7, 7).also { grBroNative = it }

    private inline val android.content.res.Resources.bimDim: Int
        get() = bimDimNative ?: getDimension(com.pt.pro.R.dimen.bim).toInt().also { bimDimNative = it }

    private inline val android.content.res.Resources.hntDim: Int
        get() = hntDimNative ?: getDimension(com.pt.pro.R.dimen.hnt).toInt().also { hntDimNative = it }

    private inline val android.content.res.Resources.txtDim: Float
        get() = txtDimNative ?: getDimension(com.pt.pro.R.dimen.txt).also { txtDimNative = it }

    private inline val android.content.Context.txtColor: Int
        get() = txtColorNative ?: rmoText.also { txtColorNative = it }

    private inline val android.content.Context.backHintColor: Int
        get() = backHintColorNative ?: rmoBackHint.also { backHintColorNative = it }

    @JvmStatic
    internal inline val android.content.res.Resources.txt: Float
        get() = txtNative ?: getDimension(com.pt.pro.R.dimen.txt).also { txtNative = it }

    @JvmStatic
    internal inline val white: Int
        @androidx.annotation.ColorInt get() = whiteNative ?: android.graphics.Color.WHITE.also { whiteNative = it }

    @JvmStatic
    internal inline val android.content.Context.itemBack: android.graphics.drawable.Drawable?
        get() = itemBackNative ?: compactImageReturn(com.pt.pro.R.drawable.item_inside_normal).also { itemBackNative = it }

    @JvmStatic
    internal inline val android.content.Context.playSvg: android.graphics.drawable.Drawable?
        get() = playSvgNative ?: compactImageReturn(com.pt.pro.R.drawable.ic_play).also { playSvgNative = it }

    @JvmStatic
    internal inline val android.content.Context.backHint: Int
        get() = backHintNative ?: rmoBackHint.also { backHintNative = it }

    @JvmStatic
    internal inline val android.content.Context.circleBack: android.graphics.drawable.Drawable?
        get() = circleBackNative ?: compactImageReturn(com.pt.pro.R.drawable.circle).also { circleBackNative = it }

    @JvmStatic
    internal inline val android.content.Context.circleBackSelect: android.graphics.drawable.Drawable?
        get() = circleBackSelectNative ?: compactImageReturn(com.pt.pro.R.drawable.circle).also { circleBackSelectNative = it }

    @JvmStatic
    internal inline val android.content.Context.sendScanner: android.graphics.drawable.Drawable?
        get() = sendScannerNative ?: compactImageReturn(com.pt.pro.R.drawable.ic_send_scanner).also { sendScannerNative = it }

    @JvmStatic
    internal inline val android.content.Context.backRmo: Int
        get() = backColorNative ?: rmoBackground.also { backColorNative = it }

    @JvmStatic
    internal inline val android.content.Context.primaryColor: Int
        get() = primaryColorNative ?: colorPrimary.also { primaryColorNative = it }

    @JvmStatic
    internal inline val android.content.Context.primaryTxt: Int
        get() = primaryTxtNative ?: textColorPrimary.also { primaryTxtNative = it }

    @JvmStatic
    fun android.content.Context.inflaterDisplay(): FragmentDisplayFasten {
        val stat = resources.statusBarHeight
        val act = actionBarHeight
        val fasten: FragmentDisplayFasten
        com.pt.common.moderator.over.FrameSizer(this@inflaterDisplay).apply root_@{
            id = com.pt.pro.R.id.listAllFrame
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            this@root_.frame displayContainer@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    topMargin = stat
                }
                val backDisplay: com.pt.common.moderator.over.ResizeImageView
                val searchEdit: androidx.appcompat.widget.AppCompatEditText
                val displayTitle: androidx.appcompat.widget.AppCompatTextView
                val searchIcon: com.pt.common.moderator.over.ResizeImageView
                val reSort: com.pt.common.moderator.over.ResizeImageView
                val displayCard = this@displayContainer.constraint displayCard@{
                    framePara(com.pt.common.stable.MATCH, act) {}
                    compactImage(com.pt.pro.R.drawable.card_back) {
                        background = this@compactImage
                    }
                    backReColor(colorPrimary)
                    backDisplay = this@displayCard.resizeImage backDisplay@{
                        id = com.pt.pro.R.id.extendButton
                        constraintPara(
                            com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                        ) {
                            marginStart = dpToPx(5)
                            dpToPx(4).also {
                                topMargin = it
                                bottomMargin = it
                            }
                            dpToPx(10).also {
                                setPadding(0, it, 0, it)
                            }
                            bottomToBottom = 0
                            startToStart = 0
                            topToTop = 0
                        }
                        background = compactImageReturn(com.pt.pro.R.drawable.ripple_oval_card)
                        setImageDrawable(compactImageReturn(com.pt.pro.R.drawable.ic_back_arrow))
                    }
                    this@displayCard.frame frame@{
                        constraintPara(0, com.pt.common.stable.MATCH) {
                            topToTop = 0
                            endToStart = com.pt.pro.R.id.showAlbums//searchIcon
                            startToEnd = com.pt.pro.R.id.extendButton//backDisplay
                        }
                        searchEdit = this@frame.editText {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.START
                            }
                            textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                            imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_DONE
                            singleNonScroll()
                            setTextColor(textColorPrimary)
                            justGone()
                            editAppearance()
                        }
                        displayTitle = this@frame.textViewer {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.START
                            }
                            textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                            imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_DONE
                            singleNonScroll()
                            setTextColor(textColorPrimary)
                            editAppearance()
                        }
                    }
                    searchIcon = this@displayCard.resizeImage {
                        id = com.pt.pro.R.id.showAlbums
                        constraintPara(
                            com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                        ) {
                            dpToPx(4).also {
                                topMargin = it
                                bottomMargin = it
                            }
                            dpToPx(10).also {
                                setPadding(0, it, 0, it)
                            }
                            bottomToBottom = 0
                            topToTop = 0
                            endToStart = com.pt.pro.R.id.searchButton//searchIcon
                        }
                        background = compactImageReturn(com.pt.pro.R.drawable.ripple_oval_card)
                        setImageDrawable(compactImageReturn(com.pt.pro.R.drawable.ic_search))
                        svgReColor(textColorPrimary)
                    }
                    reSort = this@displayCard.resizeImage {
                        id = com.pt.pro.R.id.searchButton
                        constraintPara(
                            com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                        ) {
                            dpToPx(4).also {
                                topMargin = it
                                bottomMargin = it
                            }
                            dpToPx(10).also {
                                setPadding(0, it, 0, it)
                            }
                            bottomToBottom = 0
                            topToTop = 0
                            endToEnd = 0
                        }
                        background = compactImageReturn(com.pt.pro.R.drawable.ripple_oval_card)
                        setImageDrawable(compactImageReturn(com.pt.pro.R.drawable.ic_re_sort))
                        svgReColor(textColorPrimary)
                    }
                }
                val stubOptionsDisplay = this@displayContainer.frame {
                    framePara(com.pt.common.stable.MATCH, act) {}
                    justGone()
                }
                this@displayContainer.frame displaySubFrame@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                        topMargin = act
                    }
                    val recycler: com.pt.common.moderator.recycler.RecyclerForViews
                    this@displaySubFrame.coordinator coordinator@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                        recycler = this@coordinator.recycler recycler@{
                            androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                            ).apply {
                                behavior = com.pt.common.moderator.recycler.OverScrollBounceBehavior()
                            }.also(this@recycler::setLayoutParams)
                        }
                    }
                    val pointerScroll = this@displaySubFrame.card pointerScroll@{
                        dpToPx(35).also { d35 ->
                            framePara(d35, d35) {
                                gravity = android.view.Gravity.END
                            }
                        }
                        clickableView()
                        setCardBackgroundColor(android.graphics.Color.TRANSPARENT)
                        cardElevation = dpToPx(10).toFloat()
                        this@pointerScroll.image {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                            compactImage(com.pt.pro.R.drawable.ic_scroll_pointer) {
                                setImageDrawable(this@compactImage)
                            }
                        }
                    }
                    val displayBarCard = this@displaySubFrame.card rotationBarCard@{
                        framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                            gravity = android.view.Gravity.CENTER
                        }
                        dpToPx(10).toFloat().also {
                            elevation = it
                            radius = it
                        }
                        justGone()
                        this@rotationBarCard.progressBar {
                            resources.getDimension(com.pt.pro.R.dimen.rle).toInt().also { rle ->
                                framePara(rle, rle) {
                                    dpToPx(5).also { d5 ->
                                        marginEnd = d5
                                        marginStart = d5
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

                    fasten = FragmentDisplayFasten(
                        root_ = this@root_,
                        displayContainer = this@displayContainer,
                        displayCard = displayCard,
                        backDisplay = backDisplay,
                        searchEdit = searchEdit,
                        displayTitle = displayTitle,
                        searchIcon = searchIcon,
                        reSort = reSort,
                        stubOptionsDisplay = stubOptionsDisplay,
                        displaySubFrame = this@displaySubFrame,
                        recycler = recycler,
                        pointerScroll = pointerScroll,
                        displayBarCard = displayBarCard
                    )
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterGallery(): FragmentGalleryFasten {
        val d4 = dpToPx(4)
        val d10 = dpToPx(10)
        val act = actionBarHeight
        val fasten: FragmentGalleryFasten
        com.pt.common.moderator.over.FrameSizer(this@inflaterGallery).apply root_@{
            id = com.pt.pro.R.id.listAllFrame
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            this@root_.frame frameGalleryMain@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    topMargin = resources.statusBarHeight
                }
                val empty: androidx.appcompat.widget.AppCompatTextView
                this@frameGalleryMain.constraint galleryCard@{
                    framePara(com.pt.common.stable.MATCH, act) {}
                    compactImage(com.pt.pro.R.drawable.card_back) {
                        background = this
                    }
                    backReColor(colorPrimary)
                    val mainBack = this@galleryCard.resizeImage {
                        id = com.pt.pro.R.id.lockBrowser
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            marginStart = dpToPx(5)
                            topMargin = d4
                            bottomMargin = d4
                            topToTop = 0
                            bottomToBottom = 0
                            startToStart = 0
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_back_home) {
                            setImageDrawable(this)
                        }
                    }
                    val picName: androidx.appcompat.widget.AppCompatTextView
                    val searchEdit: androidx.appcompat.widget.AppCompatEditText
                    this@galleryCard.frame frame@{
                        constraintPara(0, com.pt.common.stable.MATCH) {
                            bottomToBottom = 0
                            startToEnd = com.pt.pro.R.id.lockBrowser//mainBack
                            endToStart = com.pt.pro.R.id.savePlaylist//reloadVideos
                            topToTop = 0
                        }
                        searchEdit = this@frame.editText {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.START
                            }
                            textAlignment = android.view.View.TEXT_ALIGNMENT_VIEW_START
                            imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_DONE
                            singleNonScroll()
                            setTextColor(textColorPrimary)
                            justGone()
                            editAppearance()
                        }
                        picName = this@frame.textViewer {
                            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.START
                            }
                            singleNonScroll()
                            editAppearance()
                            text = resources.getString(com.pt.pro.R.string.rg)
                            setTextColor(textColorPrimary)
                        }
                    }
                    val reloadVideos = this@galleryCard.resizeImage {
                        id = com.pt.pro.R.id.savePlaylist
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            topMargin = d4
                            bottomMargin = d4
                            topToTop = 0
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.extendButton//reloadImages
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_reload_vid) {
                            setImageDrawable(this)
                        }
                    }
                    val reloadImages = this@galleryCard.resizeImage {
                        id = com.pt.pro.R.id.extendButton
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            topMargin = d4
                            bottomMargin = d4
                            topToTop = 0
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.showAlbums//reloadMain
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_reload_img) {
                            setImageDrawable(this)
                        }
                    }
                    val reloadMain = this@galleryCard.resizeImage {
                        id = com.pt.pro.R.id.showAlbums
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            topMargin = d4
                            bottomMargin = d4
                            topToTop = 0
                            bottomToBottom = 0
                            endToStart = com.pt.pro.R.id.returnMusic//extendGallery
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_load_folders) {
                            setImageDrawable(this)
                        }
                    }
                    val extendGallery = this@galleryCard.resizeImage {
                        id = com.pt.pro.R.id.returnMusic
                        constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            topMargin = d4
                            bottomMargin = d4
                            topToTop = 0
                            bottomToBottom = 0
                            endToEnd = 0
                        }
                        setPadding(0, d10, 0, d10)
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_extend_gallery) {
                            setImageDrawable(this)
                        }
                    }
                    this@frameGalleryMain.constraint frameAllGallery@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                            topMargin = act
                        }
                        val galleryModeFrame = this@frameAllGallery.frame {
                            id = com.pt.pro.R.id.galleryModeFrame
                            constraintPara(com.pt.common.stable.MATCH, 0) {
                                topToTop = 0
                                endToEnd = 0
                                bottomToBottom = 0
                                startToStart = 0
                            }
                        }
                        val frameForSearch = this@frameAllGallery.frame {
                            id = com.pt.pro.R.id.globalFrame
                            constraintPara(0, 0) {
                                topToTop = 0
                                startToStart = 0
                            }
                            setBackgroundColor(rmoBackground)
                        }
                        val storyButton = this@frameAllGallery.card storyButton@{
                            id = com.pt.pro.R.id.searchButton
                            this@storyButton.constraintPara(findPixel(50)) {
                                marginEnd = findPixel(20)
                                bottomMargin = findPixel(15)
                                bottomToBottom = 0
                                endToEnd = 0
                            }
                            this@storyButton.elevation = findPixel(4).toFloat()
                            this@storyButton.background = compactImageReturn(com.pt.pro.R.drawable.circle)
                            this@storyButton.backReColor(colorPrimary)
                            this@storyButton.foreground = compactImageReturn(com.pt.pro.R.drawable.ripple_rectangle)
                            this@storyButton.justGone()
                            this@storyButton.image {
                                framePara(
                                    com.pt.common.stable.MATCH,
                                    com.pt.common.stable.MATCH
                                ) {
                                    findPixel(10).also { setMargins(it, it, it, it) }
                                }
                                setImageDrawable(compactImageReturn(com.pt.pro.R.drawable.ic_story))
                                svgReColor(theme.findAttr(android.R.attr.textColorPrimary))
                            }
                        }
                        val forSnakes = this@frameAllGallery.frame {
                            constraintPara(0, com.pt.common.stable.WRAP) {
                                endToStart = com.pt.pro.R.id.searchButton
                                bottomToBottom = 0
                                startToStart = 0
                            }
                        }
                        empty = androidx.appcompat.widget.AppCompatTextView(context).apply {
                            framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.CENTER
                            }
                            text = resources.getString(com.pt.pro.R.string.py)
                            setTextColor(rmoText)
                            setTextSize(
                                android.util.TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.pt.pro.R.dimen.txt)
                            )
                            setTypeface(typeface, android.graphics.Typeface.BOLD)
                            justGone()
                        }
                        this@frameAllGallery.card extendOption@{
                            constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                                endToEnd = 0
                                topToTop = 0
                            }
                            radius = d10.toFloat()
                            setCardBackgroundColor(rmoBackground)
                            this@extendOption.justGone()
                            this@extendOption.linear lin@{
                                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                                horizontalLin()
                                val hidden = this@lin.image {
                                    dpToPx(35).also {
                                        linearPara(it, it, 1F) {
                                            gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                                            topMargin = d4
                                            bottomMargin = d4
                                            marginStart = d4
                                            marginEnd = d4
                                        }
                                    }
                                    compactImage(com.pt.pro.R.drawable.ripple_oval) {
                                        background = this
                                    }
                                    compactImage(com.pt.pro.R.drawable.ic_hidden) {
                                        setImageDrawable(this)
                                    }
                                }
                                val camera = this@lin.image {
                                    dpToPx(35).also {
                                        linearPara(it, it, 1F) {
                                            gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                                            topMargin = d4
                                            bottomMargin = d4
                                            marginStart = d4
                                            marginEnd = d4
                                        }
                                    }
                                    dpToPx(3).also { d3 -> setPadding(d3, d3, d3, d3) }
                                    compactImage(com.pt.pro.R.drawable.ripple_oval) {
                                        background = this
                                    }
                                    compactImage(com.pt.pro.R.drawable.ic_camera) {
                                        setImageDrawable(this)
                                    }
                                }
                                val pendingText: androidx.appcompat.widget.AppCompatTextView
                                val pendingFrame = this@lin.frame pendingFrame@{
                                    dpToPx(32).also {
                                        linearPara(it, it, 1F) {
                                            gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                                            topMargin = d4
                                            bottomMargin = d4
                                            marginStart = d4
                                            marginEnd = d4
                                        }
                                    }
                                    compactImage(com.pt.pro.R.drawable.ripple_oval) {
                                        background = this
                                    }
                                    clickableView()
                                    this@pendingFrame.image {
                                        compactImage(com.pt.pro.R.drawable.ic_pending) {
                                            setImageDrawable(this)
                                        }
                                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                                            gravity = android.view.Gravity.END
                                        }
                                    }
                                    pendingText = this@pendingFrame.textViewer {
                                        dpToPx(15).also {
                                            framePara(it, it) {
                                                gravity = android.view.Gravity.END or android.view.Gravity.BOTTOM
                                            }
                                        }
                                        compactImage(com.pt.pro.R.drawable.circle) {
                                            background = this
                                        }
                                        backReColor(colorAccent)
                                        gravity = android.view.Gravity.CENTER
                                        textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                                        ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
                                        isSingleLine = true
                                        setTextColor(textColorPrimary)
                                        setTextSize(
                                            android.util.TypedValue.COMPLEX_UNIT_PX, dpToPx(12).toFloat()
                                        )
                                        justInvisible()
                                    }
                                }
                                val reSort = this@lin.image {
                                    dpToPx(35).also {
                                        linearPara(it, it, 1F) {
                                            gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                                            topMargin = d4
                                            bottomMargin = d4
                                            marginStart = d4
                                            marginEnd = d4
                                        }
                                    }
                                    compactImage(com.pt.pro.R.drawable.ripple_oval) {
                                        background = this
                                    }
                                    compactImage(com.pt.pro.R.drawable.ic_re_sort) {
                                        setImageDrawable(this)
                                    }
                                }
                                val searchIcon = this@lin.image {
                                    dpToPx(35).also {
                                        linearPara(it, it, 1F) {
                                            gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                                            topMargin = d4
                                            bottomMargin = d4
                                            marginStart = d4
                                            marginEnd = d4
                                        }
                                    }
                                    compactImage(com.pt.pro.R.drawable.ripple_oval) {
                                        background = this
                                    }
                                    compactImage(com.pt.pro.R.drawable.ic_search) {
                                        setImageDrawable(this)
                                    }
                                }
                                fasten = FragmentGalleryFasten(
                                    root_ = this@root_,
                                    frameGalleryMain = this@frameGalleryMain,
                                    galleryCard = this@galleryCard,
                                    mainBack = mainBack,
                                    picName = picName,
                                    searchEdit = searchEdit,
                                    reloadVideos = reloadVideos,
                                    reloadImages = reloadImages,
                                    reloadMain = reloadMain,
                                    extendGallery = extendGallery,
                                    frameAllGallery = this@frameAllGallery,
                                    galleryModeFrame = galleryModeFrame,
                                    frameForSearch = frameForSearch,
                                    forSnakes = forSnakes,
                                    storyButton = storyButton,
                                    extendOption = this@extendOption,
                                    camera = camera,
                                    hidden = hidden,
                                    pendingFrame = pendingFrame,
                                    pendingText = pendingText,
                                    reSort = reSort,
                                    searchIcon = searchIcon,
                                    empty = empty,
                                )
                            }
                        }
                    }
                }
                empty.also(this@frameGalleryMain::addView)
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterPicFolder(
        folderWidth: Int,
        folderHeight: Int,
    ): PicFolderFasten {
        val fasten: PicFolderFasten
        frameLayoutParent root_@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                com.pt.common.stable.WRAP, com.pt.common.stable.WRAP
            ).also(this@root_::setLayoutParams)
            this@root_.card folderFrame@{
                framePara(folderWidth, folderHeight) {
                    gravity = android.view.Gravity.CENTER
                    bottomMargin = dpToPx(1)
                }
                radius = dpToPx(10).toFloat()
                setCardBackgroundColor(android.graphics.Color.TRANSPARENT)
                cardElevation = 0F
                maxCardElevation = 0F
                val folderPic = this@folderFrame.glideImage {
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                }
                val folderName: com.pt.common.moderator.over.ScalelessTextView
                this@folderFrame.frame frame@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    setBackgroundColor(android.graphics.Color.argb(76, 9, 7, 7))//pcc
                    folderName = this@frame.scalelessText {
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {}
                        maxLines = 4
                        setTextColor(white)
                        textSize(resources.txt)
                        dpToPx(5).also {
                            setPadding(it, 0, it, 0)
                        }

                    }
                }
                fasten = PicFolderFasten(
                    root_ = this@root_, folderFrame = this@folderFrame, folderPic = folderPic, folderName = folderName
                )
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterPicItem(widthColumn: Int): PicItemFasten {
        val fasten: PicItemFasten
        com.pt.common.moderator.over.FrameTint(this@inflaterPicItem).apply FrameTint@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                widthColumn, widthColumn
            ).also(this@FrameTint::setLayoutParams)
            background = itemBack
            backReColor(backRmo)

            val picture = this@FrameTint.glideImage {
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) { }
                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            }
            val videoPlay = this@FrameTint.image {
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.CENTER
                    dpToPx(10).let { r10 ->
                        setMargins(r10, r10, r10, r10)
                    }
                }
                justGone()
                setImageDrawable(playSvg)
            }
            val selected = this@FrameTint.image {
                dpToPx(24).also { r24 ->
                    framePara(r24, r24) {
                        gravity = android.view.Gravity.BOTTOM or android.view.Gravity.END
                        dpToPx(2).also { r2 ->
                            marginEnd = r2
                            bottomMargin = r2
                        }
                    }
                }
                dpToPx(4).also {
                    setPadding(it, it, it, it)
                }
                justGone()
                background = circleBackSelect
                backReColor(primaryColor)
                setImageDrawable(sendScanner)
                svgReColor(primaryTxt)
            }
            fasten = PicItemFasten(
                root_ = this@FrameTint, picture = picture, videoPlay = videoPlay, selected
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterPopGallery(): PopWindowGalleryFasten {
        val d2 = dpToPx(2)
        val d15 = dpToPx(15)
        val d48 = dpToPx(48)
        val fasten: PopWindowGalleryFasten
        cardViewParent root_@{
            setCardBackgroundColor(rmoBackHint)
            radius = dpToPx(20).toFloat()
            this@root_.nestedScroll scroll@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) { }
                scrollBarSize = 0
                this@scroll.linear lin@{
                    framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {}
                    verticalLin()

                    val delete = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.d1)
                    val share = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.p1)
                    val properties = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.pp)
                    val selected = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.st)
                    val favDis = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.fx)
                    val unFavDis = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.uv)
                    val setAs = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.sa)
                    val rename = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.rm)
                    val hide = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.fa)
                    val sendScanner = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.sn)
                    val clipboard = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.pd)
                    val pendingDisplay = this@lin.fetchPopTextView(d2 = d2, d15 = d15, d48 = d48, str = com.pt.pro.R.string.pt)

                    fasten = PopWindowGalleryFasten(
                        root_ = this@root_,
                        delete = delete,
                        share = share,
                        properties = properties,
                        selected = selected,
                        favDis = favDis,
                        unFavDis = unFavDis,
                        setAs = setAs,
                        rename = rename,
                        hide = hide,
                        sendScanner = sendScanner,
                        clipboard = clipboard,
                        pendingDisplay = pendingDisplay,
                    )
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.view.ViewGroup.fetchPopTextView(
        d2: Int, d15: Int, d48: Int, @StringRes str: Int,
    ): androidx.appcompat.widget.AppCompatTextView {
        return textViewer {
            linearPara(
                com.pt.common.stable.MATCH,
                d48,
                1.0F,
            ) {
                topMargin = d2
                bottomMargin = d2
            }
            editAppearanceLargePopupMenu()
            this@fetchPopTextView.context.compactImage(com.pt.pro.R.drawable.ripple_curvy) {
                background = this@compactImage
            }
            setPaddingRelative(d15, 0, d15, 0)
            gravity = android.view.Gravity.CENTER
            textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
            text = resources.getString(str)
            setTextColor(this@fetchPopTextView.context.rmoText)
        }
    }

    @JvmStatic
    fun android.content.Context.inflaterDelete(): StubDeleteFasten {
        val fasten: StubDeleteFasten
        android.widget.FrameLayout(this@inflaterDelete).apply root_@{
            framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                gravity = android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL
                resources.getDimension(com.pt.pro.R.dimen.dad).toInt().also {
                    marginStart = it
                    marginEnd = it
                }
                bottomMargin = dpToPx(3)
            }
            this@root_.card deleteFrame@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {}
                setCardBackgroundColor(rmoBackHint)
                radius = dpToPx(10).toFloat()
                cardElevation = dpToPx(6).toFloat()
                clickableView()
                this@deleteFrame.frame frame@{
                    framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {}
                    dpToPx(5).also {
                        setPadding(0, it, 0, it)
                    }
                    val snakeText = this@frame.textViewer {
                        dpToPx(7).also {
                            setPadding(0, it, 0, it)
                        }
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                            gravity = android.view.Gravity.CENTER_VERTICAL
                            marginEnd = dpToPx(120)
                        }
                        dpToPx(10).also { setPadding(it, 0, it, 0) }
                        text = resources.getString(com.pt.pro.R.string.dc)
                        setTextColor(rmoText)
                        setTextSize(
                            android.util.TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.pt.pro.R.dimen.tle)
                        )
                    }
                    val cancelDelete = this@frame.button {
                        framePara(dpToPx(55), com.pt.common.stable.WRAP) {
                            gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                            marginEnd = dpToPx(65)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_cur) {
                            background = this@compactImage
                        }
                        text = resources.getString(com.pt.pro.R.string.no)
                        isAllCaps = false
                        setTextColor(rmoText)
                        setTextSize(
                            android.util.TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.pt.pro.R.dimen.tle)
                        )
                    }
                    val confirmDelete = this@frame.button {
                        framePara(dpToPx(55), com.pt.common.stable.WRAP) {
                            gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.END
                            marginEnd = dpToPx(10)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_cur) {
                            background = this@compactImage
                        }
                        text = resources.getString(com.pt.pro.R.string.ys)
                        isAllCaps = false
                        setTextColor(colorAccent)
                        setTextSize(
                            android.util.TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.pt.pro.R.dimen.tle)
                        )
                    }
                    fasten = StubDeleteFasten(
                        root_ = this@root_,
                        deleteFrame = this@deleteFrame,
                        snakeText = snakeText,
                        cancelDelete = cancelDelete,
                        confirmDelete = confirmDelete
                    )
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterGalleryAll(): StubGalleryAllFasten {
        val fasten: StubGalleryAllFasten
        com.pt.common.moderator.over.FrameSizer(this@inflaterGalleryAll).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) { }
            val recGallery: com.pt.common.moderator.recycler.RecyclerForViews
            this@root_.coordinator coordinator@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) { }
                recGallery = this@coordinator.recycler recGallery@{
                    androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                        com.pt.common.stable.MATCH, com.pt.common.stable.WRAP
                    ).apply {
                        behavior = com.pt.common.moderator.recycler.OverScrollBounceBehavior()
                    }.also(this@recGallery::setLayoutParams)

                }
            }
            val galleryBarCard = this@root_.card galleryBarCard@{
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
                    resources.getDimension(com.pt.pro.R.dimen.rle).toInt().also {
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
            fasten = StubGalleryAllFasten(
                root_ = this@root_, recGallery = recGallery, galleryBarCard = galleryBarCard
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterGalleryTime(): StubGalleryTimeFasten {
        val fasten: StubGalleryTimeFasten
        com.pt.common.moderator.over.FrameSizer(this@inflaterGalleryTime).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) { }
            val recGallery: com.pt.common.moderator.recycler.RecyclerForViews
            this@root_.coordinator coordinator@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) { }
                recGallery = this@coordinator.recycler recGallery@{
                    androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                        com.pt.common.stable.MATCH, com.pt.common.stable.WRAP
                    ).apply {
                        behavior = com.pt.common.moderator.recycler.OverScrollBounceBehavior()
                    }.also(this@recGallery::setLayoutParams)

                }
            }
            val pointerScroll = this@root_.card pointerScroll@{
                dpToPx(35).also {
                    framePara(it, it) {
                        gravity = android.view.Gravity.END
                    }
                }
                clickableView()
                setCardBackgroundColor(fetchColor(com.pt.pro.R.color.fre))
                cardElevation = dpToPx(10).toFloat()
                this@pointerScroll.image {
                    framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {}
                    compactImage(com.pt.pro.R.drawable.ic_scroll_pointer) {
                        setImageDrawable(this@compactImage)
                    }
                }
            }
            val galleryBarCard = this@root_.card galleryBarCard@{
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
                    resources.getDimension(com.pt.pro.R.dimen.rle).toInt().also {
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
            fasten = StubGalleryTimeFasten(
                root_ = this@root_, recGallery = recGallery, galleryBarCard = galleryBarCard, pointerScroll = pointerScroll
            )
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterOption(): StubOptionFasten {
        val d4 = dpToPx(4)
        val d10 = dpToPx(10)
        val txtColor = textColorPrimary
        val fasten: StubOptionFasten
        com.pt.common.moderator.over.FrameTint(this@inflaterOption).apply root_@{
            framePara(com.pt.common.stable.MATCH, actionBarHeight) {}
            clickableView()
            compactImage(com.pt.pro.R.drawable.card_back) {
                background = this@compactImage
            }
            backReColor(colorPrimary)
            this@root_.constraint constraint@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                val backDisplayMenu = this@constraint.resizeImage {
                    id = com.pt.pro.R.id.lockBrowser
                    constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                        marginStart = dpToPx(5)
                        topMargin = d4
                        bottomMargin = d4
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
                }
                val menuNumber = this@constraint.textViewer {
                    constraintPara(0, com.pt.common.stable.WRAP) {
                        marginStart = dpToPx(3)
                        bottomToBottom = 0
                        topToTop = 0
                        startToEnd = com.pt.pro.R.id.lockBrowser //backDisplayMenu
                        endToStart = com.pt.pro.R.id.showAlbums //share
                    }
                    editAppearance()
                    singleNonScroll()
                    setTextColor(txtColor)
                }
                val share = this@constraint.resizeImage {
                    id = com.pt.pro.R.id.showAlbums
                    constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                        topMargin = d4
                        bottomMargin = d4
                        topToTop = 0
                        bottomToBottom = 0
                        endToStart = com.pt.pro.R.id.searchButton //deleteDate
                    }
                    setPadding(0, d10, 0, d10)
                    compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                        background = this
                    }
                    compactImage(com.pt.pro.R.drawable.ic_share_option) {
                        setImageDrawable(this)
                    }
                    svgReColor(txtColor)
                }
                val deleteDate = this@constraint.resizeImage {
                    id = com.pt.pro.R.id.searchButton
                    constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                        topMargin = d4
                        bottomMargin = d4
                        topToTop = 0
                        bottomToBottom = 0
                        endToStart = com.pt.pro.R.id.galleryModeFrame //frameLayout4
                    }
                    setPadding(0, d10, 0, d10)
                    compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                        background = this
                    }
                    compactImage(com.pt.pro.R.drawable.ic_delete_icon) {
                        setImageDrawable(this)
                    }
                    svgReColor(txtColor)
                }
                this@constraint.frame frame@{
                    id = com.pt.pro.R.id.galleryModeFrame
                    constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                        topMargin = d4
                        bottomMargin = d4
                        topToTop = 0
                        bottomToBottom = 0
                        endToEnd = 0
                    }
                    val pagerMenu = this@frame.resizeImage {
                        id = com.pt.pro.R.id.searchButton
                        framePara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                            topMargin = d4
                            bottomMargin = d4
                        }
                        dpToPx(9).also {
                            setPadding(0, it, 0, it)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                            background = this
                        }
                        compactImage(com.pt.pro.R.drawable.ic_menu_list) {
                            setImageDrawable(this)
                        }
                    }
                    val clipDate = this@frame.textViewer {
                        editAppearance()
                        framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                            gravity = android.view.Gravity.CENTER_VERTICAL
                            marginEnd = dpToPx(5)
                        }
                        compactImage(com.pt.pro.R.drawable.ripple_rectangle) {
                            background = this@compactImage
                        }
                        text = resources.getString(com.pt.pro.R.string.dn)
                        setTextColor(txtColor)
                        justGone()
                    }
                    fasten = StubOptionFasten(
                        root_ = this@root_,
                        backDisplayMenu = backDisplayMenu,
                        menuNumber = menuNumber,
                        share = share,
                        deleteDate = deleteDate,
                        pagerMenu = pagerMenu,
                        clipDate = clipDate
                    )
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterTime(widthColumn: Int): TimeFasten {
        val fasten: TimeFasten
        com.pt.common.moderator.over.FrameTint(this@inflaterTime).apply FrameTint@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                widthColumn, widthColumn
            ).also(this@FrameTint::setLayoutParams)
            background = itemBack
            backReColor(backHint)
            val picture = this@FrameTint.glideImage {
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) { }
                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            }
            val videoPlay = this@FrameTint.image {
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.CENTER
                    dpToPx(10).also {
                        setMargins(it, it, it, it)
                    }
                }
                justGone()
                setImageDrawable(playSvg)
            }
            fasten = TimeFasten(
                root_ = this@FrameTint, picture = picture, videoPlay = videoPlay
            )
        }
        return fasten
    }


    fun android.content.Context.inflaterTitle(): TittleFasten {
        val fasten: TittleFasten
        android.widget.FrameLayout(this@inflaterTitle).apply FrameTint@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                com.pt.common.stable.MATCH, kotlin.math.max(resources.hntDim, dpToPx(48))
            ).also(this@FrameTint::setLayoutParams)
            compactImage(com.pt.pro.R.drawable.ripple_curvy) {
                background = this@compactImage
            }
            val fileName = this@FrameTint.textViewer fileName@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                    marginStart = resources.bimDim
                    marginEnd = resources.gt3Dim
                }
                setTextColor(txtColor)
                setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, resources.txtDim)
            }
            val extend = this@FrameTint.textViewer extend@{
                framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.END or android.view.Gravity.CENTER_VERTICAL
                    marginEnd = resources.bimDim
                }
                setTextColor(txtColor)
                setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, resources.txtDim)
            }
            backReColor(backHintColor)
            fasten = TittleFasten(this@FrameTint, fileName, extend)
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterVideo(): VideoFragmentFasten {
        val fasten: VideoFragmentFasten
        frameLayoutParent root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            val t: DSack<android.view.SurfaceView, com.pt.common.moderator.over.GlideImageView, androidx.appcompat.widget.AppCompatImageView>
            val frameVideo = this@root_.touchFrame frameVideo@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                this@frameVideo.clickableView()
                contentDescription = resources.voDes
                val videoView = android.view.SurfaceView(
                    context
                ).apply videoView@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    this@videoView.nonClickableView()
                }.also(::addView)
                val thumb = this@frameVideo.glideImage {
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                        gravity = android.view.Gravity.CENTER
                    }
                    nonClickableView()
                }
                val playPause = this@frameVideo.image {
                    resources.gp1Dim.also { gp1d ->
                        framePara(gp1d, gp1d) {
                            gravity = android.view.Gravity.CENTER
                        }
                    }
                    background = circleBack
                    backReColor(grBro)
                    compactImage(com.pt.pro.R.drawable.ic_play) {
                        setImageDrawable(this@compactImage)
                    }
                    svgReColor(ord)
                    clickableView()
                    isEnabled = false
                }
                t = DSack(videoView, thumb, playPause)
            }
            this@root_.frame upperOptionsVideo@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                    dpToPx(5).let { d5 ->
                        marginStart = d5
                        marginEnd = d5
                        topMargin = dpToPx(80)
                    }
                }
                val volume = this@upperOptionsVideo.image {
                    resources.gt3Dim.also { dG ->
                        framePara(dG, dG) {}
                    }
                    dpToPx(5).also { setPadding(it, it, it, it) }
                    compactImage(com.pt.pro.R.drawable.ripple_oval_video) {
                        background = this
                    }
                    setImageDrawable(volumeOff)
                    svgReColorWhite()
                }
                val seekScale = this@upperOptionsVideo.seekBar {
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                        resources.gt3Dim.let { gopD ->
                            marginStart = gopD
                            marginEnd = gopD
                        }
                    }
                    progressDrawable = emptyProgress
                    seekReColor(0)
                    thumb = videoPointer
                    justGone()
                }
                val screenBrightness = this@upperOptionsVideo.image {
                    resources.gt3Dim.also { dG ->
                        framePara(dG, dG) {
                            gravity = android.view.Gravity.END
                        }
                    }
                    dpToPx(5).also { setPadding(it, it, it, it) }
                    compactImage(com.pt.pro.R.drawable.ripple_oval_video) {
                        background = this
                    }
                    setImageDrawable(brightnessOn)
                    svgReColorWhite()
                }
                val forRearFrame = this@root_.frame forRearFrame@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                    justGone()
                    nonClickableView()
                }
                fasten = VideoFragmentFasten(
                    root_ = this@root_,
                    frameVideo = frameVideo,
                    videoView = t.one,
                    thumb = t.two,
                    playPause = t.three,
                    upperOptionsVideo = this@upperOptionsVideo,
                    volume = volume,
                    seekScale = seekScale,
                    screenBrightness = screenBrightness,
                    forRearFrame = forRearFrame
                )
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterZoomView(): ZoomViewFasten {
        val fasten: ZoomViewFasten
        android.widget.FrameLayout(this@inflaterZoomView).apply root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
            val zoomView = com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView(
                context
            ).apply zoomView@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                this@zoomView.clickableView()
            }.also(this@root_::addView)
            fasten = ZoomViewFasten(
                root_ = this@root_, zoomView = zoomView
            )
        }
        return fasten
    }


    @JvmStatic
    fun android.content.Context.inflaterPictureBrowser(d4: Int, d10: Int, action: Int): PictureBrowserFasten {
        val whi = android.graphics.Color.WHITE
        val fasten: PictureBrowserFasten
        com.sothree.slidinguppanel.SlidingUpPanelLayout(this@inflaterPictureBrowser).apply root_@{
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
                val rotationBarProgressBar: androidx.core.widget.ContentLoadingProgressBar
                val rotationBarCard = this@deFrame.card rotationBarCard@{
                    framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                        gravity = android.view.Gravity.CENTER
                    }
                    elevation = d10.toFloat()
                    radius = d10.toFloat()
                    justGone()
                    rotationBarProgressBar = this@rotationBarCard.progressBar {
                        resources.getDimension(com.pt.pro.R.dimen.rle).toInt().also { rle ->
                            framePara(rle, rle) {
                                dpToPx(5).also { d5 ->
                                    marginEnd = d5
                                    marginStart = d5
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

                this@deFrame.frame head@{
                    framePara(com.pt.common.stable.MATCH, action + (action / 4)) {}
                    compactImage(com.pt.pro.R.drawable.pager_background) {
                        background = this@compactImage
                    }
                    setPadding(0, 0, 0, (action / 4))
                    this@head.constraint headInner@{
                        framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {}
                        val pagerMenu = this@headInner.resizeImage {
                            constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                                topMargin = d4
                                bottomMargin = d4
                                bottomToBottom = 0
                                endToEnd = 0
                                topToTop = 0
                            }
                            setPadding(0, d10, 0, d10)
                            id = com.pt.pro.R.id.lockBrowser
                            compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                background = this@compactImage
                            }
                            compactImage(com.pt.pro.R.drawable.ic_menu_list) {
                                setImageDrawable(this@compactImage)
                            }
                            svgReColor(whi)
                        }
                        val picName = this@headInner.textViewer picName@{
                            constraintPara(0, com.pt.common.stable.WRAP) {
                                gravity = android.view.Gravity.CENTER or android.view.Gravity.START
                                marginStart = d10
                                marginEnd = dpToPx(20)
                                bottomToBottom = 0
                                endToStart = com.pt.pro.R.id.lockBrowser
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
                            compactImage(com.pt.pro.R.drawable.pager_background_reversed) {
                                background = this@compactImage
                            }
                            setPadding(0, (action / 4), 0, 0)
                            this@headDown.constraint headOptions@{
                                framePara(
                                    com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                ) {
                                    gravity = android.view.Gravity.BOTTOM
                                }
                                val videoDuration = this@headOptions.textViewer {
                                    constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                                        endToEnd = 0
                                        topToTop = 0
                                    }
                                    nonClickableView()
                                    text = resources.getString(com.pt.pro.R.string.bz)
                                    setTextColor(whi)
                                    setTextSize(
                                        android.util.TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.pt.pro.R.dimen.tvs)
                                    )
                                }
                                val currentDuration = android.widget.Chronometer(context).apply {
                                    constraintPara(
                                        com.pt.common.stable.WRAP, com.pt.common.stable.WRAP
                                    ) {
                                        startToStart = 0
                                        topToTop = 0
                                    }
                                    nonClickableView()
                                    text = resources.getString(com.pt.pro.R.string.bz)
                                    setTextColor(whi)
                                    setTextSize(
                                        android.util.TypedValue.COMPLEX_UNIT_PX, resources.getDimension(com.pt.pro.R.dimen.tvs)
                                    )
                                }.also(this@headOptions::addView)
                                val floatingVideo = this@headOptions.resizeImage {
                                    constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                                        topMargin = d4
                                        bottomMargin = d4
                                        bottomToBottom = 0
                                        startToStart = 0
                                        endToEnd = 0
                                        topToTop = 0
                                        horizontalBias = 0.16F
                                    }
                                    setPadding(0, d10, 0, d10)
                                    compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                                        background = this@compactImage
                                    }
                                    svgReColor(whi)
                                    compactImage(com.pt.pro.R.drawable.ic_floating_video) {
                                        setImageDrawable(this@compactImage)
                                    }
                                    justInvisible()
                                }
                                val isFavorite = this@headOptions.resizeImage {
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
                                    svgReColor(whi)
                                    compactImage(com.pt.pro.R.drawable.ic_not_favorite) {
                                        setImageDrawable(this@compactImage)
                                    }
                                }
                                val editActivity = this@headOptions.resizeImage {
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
                                    svgReColor(whi)
                                    compactImage(com.pt.pro.R.drawable.ic_edit_music) {
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
                                this@headDown.frame lowerVideoOptions@{
                                    framePara(
                                        com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                    ) {
                                        gravity = android.view.Gravity.BOTTOM
                                        bottomMargin = action
                                    }
                                    justGone()
                                    val seekBar = this@lowerVideoOptions.seekBar {
                                        framePara(
                                            com.pt.common.stable.MATCH, dpToPx(40)
                                        ) {
                                            gravity = android.view.Gravity.BOTTOM
                                            bottomMargin = -1 * dpToPx(6)
                                            (-1 * dpToPx(10)).also {
                                                marginEnd = it
                                                marginStart = it
                                            }
                                        }
                                        progress = 50
                                        splitTrack = false
                                        compactImage(com.pt.pro.R.drawable.seekbar_style) {
                                            progressDrawable = this@compactImage
                                            indeterminateDrawable = this@compactImage
                                        }
                                        compactImage(com.pt.pro.R.drawable.seek_thumb) {
                                            thumb = this@compactImage
                                        }

                                    }

                                    fasten = PictureBrowserFasten(
                                        root_ = this@root_,
                                        emptyFrame = emptyFrame,
                                        imagePager = imagePager,
                                        head = this@head,
                                        headInner = this@headInner,
                                        pagerMenu = pagerMenu,
                                        picName = picName,
                                        unLockImage = unLockImage,
                                        rotationBarCard = rotationBarCard,
                                        rotationBarProgressBar = rotationBarProgressBar,
                                        headDown = this@headDown,
                                        headOptions = this@headOptions,
                                        videoDuration = videoDuration,
                                        currentDuration = currentDuration,
                                        floatingVideo = floatingVideo,
                                        isFavorite = isFavorite,
                                        editActivity = editActivity,
                                        rotateScreen = rotateScreen,
                                        lockBrowser = lockBrowser,
                                        lowerVideoOptions = this@lowerVideoOptions,
                                        seekBar = seekBar
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterRowAll(widthColumn: Int, isRightToLeft: Boolean): RowAllFasten {
        val fasten: RowAllFasten
        val h = kotlin.math.max(resources.hntDim, dpToPx(48))
        frameLayoutParent root_@{
            androidx.recyclerview.widget.GridLayoutManager.LayoutParams(
                com.pt.common.stable.MATCH, com.pt.common.stable.WRAP
            ).also(this@root_::setLayoutParams)

            this@root_.frame extendFile@{
                framePara(com.pt.common.stable.MATCH, h) {}
                compactImage(com.pt.pro.R.drawable.ripple_curvy) {
                    background = this@compactImage
                }
                val fileName = this@extendFile.textViewer fileName@{
                    framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                        marginStart = resources.bimDim
                        marginEnd = resources.gt3Dim
                    }
                    setTextColor(txtColor)
                    setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, resources.txtDim)
                }
                val extend = this@extendFile.textViewer extend@{
                    framePara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP) {
                        gravity = android.view.Gravity.END or android.view.Gravity.CENTER_VERTICAL
                        marginEnd = resources.bimDim
                    }
                    setTextColor(txtColor)
                    setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, resources.txtDim)
                }
                this@root_.card folderCardAll@{
                    framePara(com.pt.common.stable.MATCH, widthColumn) {
                        topMargin = h
                    }
                    /**[com.pt.pro.R.dimen.card_radius_normal]**/
                    radius = dpToPx(10).toFloat()
                    elevation = 0F
                    setCardBackgroundColor(0)
                    val folderRecyclerAll: com.pt.common.moderator.recycler.RecyclerForViews
                    this@folderCardAll.coordinator coordinator@{
                        framePara(com.pt.common.stable.MATCH, widthColumn) {}
                        folderRecyclerAll = this@coordinator.recycler folderRecyclerAll@{
                            androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
                                com.pt.common.stable.MATCH, widthColumn
                            ).apply {
                                behavior = com.pt.common.moderator.recycler.OverScrollHorizontalBehavior()
                            }.also(this@folderRecyclerAll::setLayoutParams)
                        }
                    }
                    val startGrd = this@folderCardAll.image {
                        framePara(dpToPx(40), widthColumn) {}
                        nonClickableView()

                        if (isRightToLeft) {
                            compactImage(com.pt.pro.R.drawable.hor_end_gradient) {
                                setImageDrawable(this@compactImage)
                            }
                        } else {
                            compactImage(com.pt.pro.R.drawable.hor_start_gradient) {
                                setImageDrawable(this@compactImage)
                            }
                        }
                    }
                    val endGrd = this@folderCardAll.image {
                        framePara(dpToPx(40), widthColumn) {
                            gravity = android.view.Gravity.END
                        }
                        nonClickableView()
                        if (isRightToLeft) {
                            compactImage(com.pt.pro.R.drawable.hor_start_gradient) {
                                setImageDrawable(this@compactImage)
                            }
                        } else {
                            compactImage(com.pt.pro.R.drawable.hor_end_gradient) {
                                setImageDrawable(this@compactImage)
                            }
                        }
                    }
                    fasten = RowAllFasten(
                        root_ = this@root_,
                        extendFile = this@extendFile,
                        fileName = fileName,
                        extend = extend,
                        folderCardAll = this@folderCardAll,
                        folderRecyclerAll = folderRecyclerAll,
                        startGrd = startGrd,
                        endGrd = endGrd,
                    )
                }
            }
        }

        return fasten
    }

    @JvmStatic
    fun destroyFasten() {
        txtNative = null
        whiteNative = null
        backColorNative = null
        primaryColorNative = null
        primaryTxtNative = null
        playSvgNative = null
        circleBackNative = null
        circleBackSelectNative = null
        sendScannerNative = null
        itemBackNative = null
        backHintNative = null
        bimDimNative = null
        hntDimNative = null
        txtDimNative = null
        txtColorNative = null
        backHintColorNative = null
        voDesNative = null
        gp1DimNative = null
        grBroNative = null
        ordNative = null
        gt3DimNative = null
        brightnessOnNative = null
        volumeOffNative = null
        emptyProgressNative = null
        videoPointerNative = null
        destroyGlobalFasten()
    }

}