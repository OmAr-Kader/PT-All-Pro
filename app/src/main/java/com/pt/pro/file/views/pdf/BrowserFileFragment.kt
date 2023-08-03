package com.pt.pro.file.views.pdf

import com.pt.common.global.*
import com.pt.common.media.fetchPdfForRender
import com.pt.common.media.renderPdfSize
import com.pt.common.stable.*
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import kotlin.math.abs

class BrowserFileFragment : com.pt.common.mutual.life.GlobalFragment<com.pt.pro.file.fasten.PdfBrowserFasten>(),
    com.pt.pro.file.interfaces.SliderListener {

    @Volatile
    private var hide: Boolean = true

    private var isDoInLand: Boolean = false
    private var margeWidth: Int = 0
    private var margeHeight: Int = 0
    private var marginNum: Int = 0

    private var imagePosition: Int = 0

    private var itemPdfCount: Int = 0

    private var filePath: String? = null
    private var haveOption: Boolean = false
    private var checkB: Boolean = false

    private var slideOff: Float = 0.0F
    private var slideState: PanelState = PanelState.EXPANDED

    private var runForSaveLastPosition: ((String?, Int) -> Unit)? = null

    override fun DSack<Int, Int, Boolean>.setIntiMargin() {
        margeWidth = one
        margeHeight = two
        isDoInLand = three
    }

    override fun String.setFileUriAndPath(a: (String?, Int) -> Unit) {
        filePath = this@setFileUriAndPath
        runForSaveLastPosition = a
    }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.file.fasten.FileInflater.run {
                this@creBin.context.inflaterPdf()
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
                hide = !isV_R
                it.intiViews(resources.configuration)
            }.root_
        }

    override fun com.pt.pro.file.fasten.PdfBrowserFasten.onViewCreated() {
        launchDef {
            cont.fetchPdfForRender(filePath, act.intent.data) {
                itemPdfCount = renderPdfSize()
            }
            context.nullChecker()
            withMain {
                filePath?.let {
                    ctx.findIntegerPreference(it, 0).letSus {
                        withMain {
                            imagePosition = it
                            imagePager.loadPager(it)
                            funPageSelected(it)
                        }
                    }
                }
                binding.picName.text = FileLate(filePath.toStr).run {
                    nameWithoutExtension.ifEmpty {
                        name
                    }
                }

                scrollerChang?.let { it1 -> imagePager.registerOnPageChangeCallback(it1) }
                prePage.apply {
                    setOnClickListener(this@BrowserFileFragment)
                    setOnLongClickListener(this@BrowserFileFragment)
                }
                rotateScreen.apply {
                    setOnClickListener(this@BrowserFileFragment)
                    setOnLongClickListener(this@BrowserFileFragment)
                }
                nextPage.apply {
                    setOnClickListener(this@BrowserFileFragment)
                    setOnLongClickListener(this@BrowserFileFragment)
                }
                lockBrowser.apply {
                    setOnClickListener(this@BrowserFileFragment)
                    setOnLongClickListener(this@BrowserFileFragment)
                }
                emptyFrame.setOnClickListener(this@BrowserFileFragment)
                unLockImage.apply {
                    senseListener?.setContactListener(true)
                }
                root_.apply {
                    panelState = PanelState.EXPANDED
                    addSenseListener(false) { _, _, type ->
                        if (type == UP_SEN) {
                            if (slideOff < 0.3647541F && slideState == PanelState.ANCHORED) {
                                requireParentFragment().childFragmentManager.popBackStack()
                            }
                        }
                    }
                    addPanelSlideListener(slideListen)
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    private fun androidx.viewpager2.widget.ViewPager2.loadPager(imagePosition: Int) {
        launchDef {
            withMain {
                funStateChanged()
                setCurrentItem(imagePosition, false)
            }
            withMain {
                adapter = MyAdapter()
                setCurrentItem(imagePosition, false)
                offscreenPageLimit = 1
                setPageTransformer { page, position ->
                    with(page) {
                        catchyUnit {
                            if (position != abs(position) && !haveOption) {
                                checkB = abs(position % 1) < 0.4
                                haveOption = true
                            }
                            if (checkB) {
                                if (position % 1 != 0.0F && position != abs(position)) {
                                    scrollX = (width * 0.5 * position).toInt()
                                } else {
                                    scrollX = 0
                                    scaleX = 1.0F
                                    scaleY = 1.0F
                                }
                            } else {
                                if (position % 1 != 0.0F && position == abs(position)) {
                                    scrollX = (width * 0.5 * position).toInt()
                                } else {
                                    scrollX = 0
                                    scaleX = 1.0F
                                    scaleY = 1.0F
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    inner class MyAdapter : androidx.viewpager2.adapter.FragmentStateAdapter(childFragmentManager, lifecycle) {

        override fun getItemCount(): Int = itemPdfCount

        override fun createFragment(position: Int): androidx.fragment.app.Fragment = run {
            newViewPdfFragment {
                this@newViewPdfFragment.hideSys = this@BrowserFileFragment.hide
                this@newViewPdfFragment.pdfPath = this@BrowserFileFragment.filePath
                this@newViewPdfFragment.browserFileListener = this@BrowserFileFragment
                this@newViewPdfFragment.pageNumber = position
                this@newViewPdfFragment
            }
        }
    }

    override var pdfFragment: ViewPdfFragment? = null

    @com.pt.common.global.UiAnn
    override suspend fun onShowCardView() {
        withMain {
            binder?.applySus {
                head.visibleTop(250L)
                headDown.visibleBottom(250L)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun onHideCardView() {
        withMain {
            binder?.applySus {
                head.goneTop(200L)
                headDown.goneBottom(200L)
            }
        }
    }

    override fun funStateChanged() {
        haveOption = false
    }


    @android.annotation.SuppressLint("SetTextI18n")
    @com.pt.common.global.UiAnn
    override fun funPageSelected(position: Int) {
        binder?.pageNum?.text = (position + 1).toStr + "/" + itemPdfCount.toStr
    }


    private var slideListen: com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener? =
        object : com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener {
            override fun onPanelSlide(panel: android.view.View, slideOffset: Float) {
                slideOff = slideOffset
            }

            override fun onPanelStateChanged(
                panel: android.view.View,
                previousState: PanelState,
                newState: PanelState,
            ) {
                if (
                    newState == PanelState.COLLAPSED
                ) {
                    requireParentFragment().childFragmentManager.popBackStack()
                }
                slideState = newState
            }
        }


    private var scrollerChang: androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback? =
        object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE) {
                    funStateChanged()
                }
            }

            override fun onPageSelected(position: Int) {
                funPageSelected(position)
                imagePosition = position
            }
        }


    @com.pt.common.global.UiAnn
    private fun com.pt.pro.file.fasten.PdfBrowserFasten.intiViews(config: android.content.res.Configuration) {
        marginNum = getMarginNumber(config)
        ctx.actionBarHeight.let { act ->
            rec.statusBarHeight.let { stat ->
                head.framePara(MATCH, act + (act / 4)) {
                    topMargin = stat
                }
                headInner.framePara(MATCH, MATCH) {
                    marginStart = marginNum
                    marginEnd = marginNum
                }
                headDown.framePara(MATCH, WRAP) {
                    gravity = android.view.Gravity.BOTTOM
                    bottomMargin = getMarginNumberDown(config)
                }
                headOptions.framePara(MATCH, act) {
                    gravity = android.view.Gravity.BOTTOM
                    marginStart = marginNum
                    marginEnd = marginNum
                }
                unLockImage.framePara(act, act) {
                    gravity = android.view.Gravity.END
                    //topMargin = stat
                    //marginEnd = if (marginNum != 0) marginNum else 5
                }
            }
        }
    }


    private inline val getMarginNumber: (android.content.res.Configuration) -> Int
        get() = { config ->
            config.isLandTrad.let { isLand ->
                (isDoInLand == isLand).let { same ->
                    if (same && isLand) {
                        margeWidth
                    } else if (!same && isLand) {
                        if (config.isTablet) {
                            margeWidth
                        } else {
                            margeHeight
                        }
                    } else if (same && !isLand) {
                        margeWidth
                    } else {
                        if (config.isTablet) {
                            margeWidth
                        } else {
                            margeHeight
                        }
                    }
                }
            }
        }

    private inline val getMarginNumberDown: (android.content.res.Configuration) -> Int
        get() = { config ->
            config.isLandTrad.let { isLand ->
                (isDoInLand == isLand).let { same ->
                    if (same && isLand) {
                        margeHeight
                    } else if (!same && isLand) {
                        if (config.isTablet) {
                            margeHeight
                        } else {
                            margeWidth
                        }
                    } else if (same && !isLand) {
                        margeHeight
                    } else {
                        if (config.isTablet) {
                            margeHeight
                        } else {
                            margeWidth
                        }
                    }
                }
            }
        }


    private suspend fun doLock() {
        withMain {
            binding.unLockImage.justVisibleSus()
            binding.applySus {
                root_.isEnabled = false
                imagePager.isUserInputEnabled = false
            }
        }
        withMain {
            pdfFragment?.doLockImg()
        }
    }

    internal fun doUnLock() {
        launchImdMain {
            withMain {
                binding.apply {
                    root_.isEnabled = true
                    unLockImage.goneTop(250L)
                    imagePager.isUserInputEnabled = true
                }
            }
            withMain {
                pdfFragment?.doUnLockImg()
            }
        }
    }

    private var senseListener: com.pt.common.moderator.touch.ContactListener? = object : com.pt.common.moderator.touch.ContactListener {
        override fun onUp(v: android.view.View) {
            v.handler?.removeCallbacksAndMessages(null)
            v.handler?.postDelayed({
                doUnLock()
            }, android.view.ViewConfiguration.getTapTimeout().toLong())
        }

        override fun onDown(v: android.view.View, event: android.view.MotionEvent) {

        }

        override fun onMove(v: android.view.View, event: android.view.MotionEvent) {

        }

    }

    override fun com.pt.pro.file.fasten.PdfBrowserFasten.onLongClick(v: android.view.View): Boolean {

        return false
    }

    override fun com.pt.pro.file.fasten.PdfBrowserFasten.onClick(v: android.view.View) {
        launchDef {
            when (v) {
                rotateScreen -> doRotateScreen()
                prePage -> doPrevious()
                nextPage -> doNext()
                lockBrowser -> doLock()
                emptyFrame -> backForGallery()
            }
        }
    }


    @android.annotation.SuppressLint("SourceLockedOrientationActivity")
    private suspend fun doRotateScreen() {
        withMain {
            if (!rec.isLandTraditional) {
                act.requestedOrientation =
                    android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                act.requestedOrientation =
                    android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

    private suspend fun doPrevious() {
        withMain {
            binding.imagePager.apply {
                currentItem.also {
                    if (it != 0) {
                        setCurrentItem(it - 1, true)
                    }
                }
            }
        }
    }

    private suspend fun doNext() {
        withMain {
            binding.imagePager.apply {
                currentItem.also {
                    if (it != itemPdfCount - 1) {
                        setCurrentItem(it + 1, true)
                    }
                }
            }
        }
    }

    private fun backForGallery() {
        requireParentFragment().childFragmentManager.popBackStack()
    }


    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        val pos = imagePosition
        binder?.imagePager?.adapter = null
        super.onConfigurationChanged(newConfig)
        launchImdMain {
            withMain {
                binding.intiViews(newConfig)
                binding.apply {
                    picName.apply {
                        editAppearance()
                        setTextColor(ctx.fetchColor(com.pt.pro.R.color.whi))
                    }
                    imagePosition = pos
                    imagePager.loadPager(imagePosition)
                }
            }
        }
    }

    override fun onDestroyView() {
        runForSaveLastPosition?.invoke(filePath, imagePosition)
        haveOption = false
        checkB = false
        slideOff = 0.0F
        slideState = PanelState.EXPANDED
        slideListen = null
        scrollerChang = null
        super.onDestroyView()
        filePath = null
        runForSaveLastPosition = null
    }

}