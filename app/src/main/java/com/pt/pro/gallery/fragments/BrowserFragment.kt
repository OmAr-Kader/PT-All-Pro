package com.pt.pro.gallery.fragments

import com.pt.common.global.*
import com.pt.common.media.setAsUseServices
import com.pt.common.media.shareMedia
import com.pt.common.stable.*
import kotlin.math.abs

class BrowserFragment : ParentBrowser() {

    override var imageFragment: FragmentImage? = null
    override var vidFragment: FragmentVideo? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run {
                this@creBin.context.let {
                    it.inflaterPictureBrowser(
                        d4 = it.findPixel(4),
                        d10 = it.findPixel(10),
                        action = it.actionBarHeight
                    )
                }
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
                hide = !isV_R
                it.intiViews(resources.configuration)
            }.root_
        }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.PictureBrowserFasten.onViewCreated() {
        if (allImagesNative.isNullOrEmpty()) {
            requireParentFragment().childFragmentManager.popBackStack()
            return
        }
        pushJob {
            launchImdMain {
                context.nullChecker()
                justCoroutineMain {
                    if (allImages.any { !it.isImage }) {
                        intiPlayer()
                    } else return@justCoroutineMain
                }
                context.nullChecker()
                withMain {
                    imagePager.loadPager(imagePosition)
                }
                context.nullChecker()
                justCoroutineMain {
                    intiViewsAll()
                }
            }
        }
    }

    private fun com.pt.pro.gallery.fasten.PictureBrowserFasten.intiViewsAll() {
        pushJob {
            launchImdMain {
                context.nullChecker()
                withMain {
                    allImages.getINull(imagePosition)?.displayTitle()
                }
                context.nullChecker()
                withMain {
                    scrollerChang?.let { it1 -> imagePager.registerOnPageChangeCallback(it1) }
                    editActivity.apply {
                        setOnClickListener(this@BrowserFragment)
                        setOnLongClickListener(this@BrowserFragment)
                    }
                    isFavorite.apply {
                        setOnClickListener(this@BrowserFragment)
                        setOnLongClickListener(this@BrowserFragment)
                    }
                    pagerMenu.apply {
                        setOnClickListener(this@BrowserFragment)
                        setOnLongClickListener(this@BrowserFragment)
                    }
                    rotateScreen.setOnClickListener(this@BrowserFragment)
                    floatingVideo.setOnClickListener(this@BrowserFragment)
                    lockBrowser.setOnClickListener(this@BrowserFragment)
                    emptyFrame.setOnClickListener(this@BrowserFragment)
                    unLockImage.apply {
                        senseListener?.setContactListener(true)
                    }
                    root_.addPanelSlideListener(slideListen)
                }
                context.nullChecker()
                doLoadFavoriteStrings()
                context.nullChecker()
                withMain {
                    allImages.getINull(imagePosition)?.displayFavorites(favorites)
                    funPageSelected(imagePosition)
                }
            }
        }
    }

    private var senseListener: com.pt.common.moderator.touch.ContactListener? =
        object : com.pt.common.moderator.touch.ContactListener {
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

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.gallery.fasten.PictureBrowserFasten.intiViews(newConfig: android.content.res.Configuration) {
        marginNum = pagerHold?.getMarginNumber?.invoke(newConfig) ?: run {
            requireParentFragment().childFragmentManager.popBackStack()
            return
        }
        ctx.actionBarHeight.let { act ->
            rec.statusBarHeight.let { stat ->
                head.framePara(MATCH, act + (act / 4)) {
                    topMargin = stat
                }
                headInner.framePara(MATCH, MATCH) {
                    marginStart = marginNum
                    marginEnd = marginNum
                }
                pagerHolder {
                    headDown.framePara(MATCH, WRAP) {
                        gravity = android.view.Gravity.BOTTOM
                        bottomMargin = this@pagerHolder.getMarginNumberDown(newConfig)
                    }
                }
                headOptions.framePara(MATCH, act) {
                    gravity = android.view.Gravity.BOTTOM
                    marginStart = marginNum
                    marginEnd = marginNum
                }
                lowerVideoOptions.framePara(MATCH, WRAP) {
                    gravity = android.view.Gravity.BOTTOM
                    //marginStart = marginNum
                    //marginEnd = marginNum
                    marginStart = marginNum
                    marginEnd = marginNum
                    bottomMargin = act
                }
                unLockImage.framePara(act, act) {
                    gravity = android.view.Gravity.END
                    //topMargin = stat
                    //marginEnd = if (marginNum != 0) marginNum else 5
                }
            }
        }
    }

    private suspend fun doLoadFavoriteStrings() {
        context.nullChecker()
        withBack {
            ctx.newDBGallerySus {
                getAllFavPath()
            }.letSusBack {
                context.nullChecker()
                favorites.addAll(it)
            }
        }
    }

    override fun com.pt.pro.gallery.fasten.PictureBrowserFasten.onClick(v: android.view.View) {
        launchMain {
            when (v) {
                pagerMenu -> doOptionDisplay()
                isFavorite -> doFavUnFav()
                editActivity -> doEditLauncher()
                rotateScreen -> doRotateScreen()
                floatingVideo -> videoPop()
                lockBrowser -> doLock()
                emptyFrame -> backForGallery()
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
            com.pt.pro.gallery.objects.MyHash.isLock = true
            if (allImages[imagePosition].isImage) {
                imageFragment?.doLockImg()
            } else {
                vidFragment?.doLockVid()
            }

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
                com.pt.pro.gallery.objects.MyHash.isLock = false
                if (allImages[imagePosition].isImage) {
                    imageFragment?.doUnLockImg()
                } else {
                    vidFragment?.doUnLockVid()
                }
            }
        }
    }

    private fun backForGallery() {
        requireParentFragment().childFragmentManager.popBackStack()
    }

    private inline val DSackT<com.pt.pro.gallery.fasten.PopWindowGalleryFasten, android.widget.PopupWindow>.popClickList: android.view.View.OnClickListener
        @com.pt.common.global.UiAnn get() {
            return android.view.View.OnClickListener {
                launchDef {
                    withMain {
                        allImages[imagePosition].let { pic1 ->
                            mutableListOf<MediaSack>().apply {
                                add(pic1)
                            }.also { aaa ->
                                when ((it ?: return@withMain)) {
                                    one.delete -> aaa.deleteMedia()
                                    one.share -> ctx.shareMedia(
                                        aaa,
                                        com.pt.pro.BuildConfig.APPLICATION_ID,
                                        s = resources.getString(com.pt.pro.R.string.p1),
                                    ) {
                                        runCatching<Unit> {
                                            startActivity(this@shareMedia)
                                        }.getOrElse {
                                            ctx.makeToastRecSus(com.pt.pro.R.string.px, 0)
                                        }
                                    }
                                    one.properties -> aaa.properties()
                                    one.setAs -> {
                                        launchDef {
                                            ctx.setAsUseServices(
                                                deleteCopy = pic1,
                                                s = resources.getString(com.pt.pro.R.string.ge),
                                                pac = com.pt.pro.BuildConfig.APPLICATION_ID
                                            ) {
                                                runCatching<Unit> {
                                                    startActivity(this@setAsUseServices)
                                                }.getOrElse {
                                                    ctx.makeToastRecSus(com.pt.pro.R.string.px, 0)
                                                }
                                            }
                                        }
                                    }
                                    one.rename -> pic1.rename()
                                    one.hide -> pagerHolder { pic1.checkHidden(this@pagerHolder).doHide() }
                                    one.clipboard -> aaa.clipboard()
                                    one.pendingDisplay -> pic1.addPicPending()
                                    one.sendScanner -> ctx.scannerLauncherGall(pic1.pathMedia.toStr) {
                                        this@BrowserFragment.startActivity(this@scannerLauncherGall)
                                    }
                                }
                            }
                        }
                    }
                    withMain {
                        if (two.isShowing) two.dismiss() else return@withMain
                    }
                }
            }
        }


    private var scrollerChang: androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback? =
        object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_IDLE) {
                    funStateChanged()
                } else if (state == androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING) {
                    binder?.apply {
                        currentDuration.justGone()
                        videoDuration.justGone()
                        lowerVideoOptions.justGone()
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                funPageSelected(position)
                imagePosition = position
            }
        }

    @com.pt.common.global.MainAnn
    override fun funStateChanged() {
        haveOption = false
        imageForPager = allImages[imagePosition].isImage
        if (!imageForPager) {
            binder?.apply {
                currentDuration.justVisible()
                videoDuration.justVisible()
                lowerVideoOptions.justVisible()
            }
        } else {
            binder?.apply {
                currentDuration.apply {
                    justGone()
                    text = ""
                }
                videoDuration.apply {
                    justGone()
                    text = ""
                }
                lowerVideoOptions.justGone()
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun funPageSelected(position: Int) {
        launchImdMain {
            withMain {
                allImages.getINull(imagePosition)?.let {
                    it.displayTitle()
                    it.displayFavorites(favorites)
                    binder?.floatingVideo.applySus {
                        if (!it.isImage) justVisibleSus() else justGoneSus()
                    }
                }
                binder?.applySus {
                    rotationBarCard.justGoneSus()
                }
            }
        }
    }


    private suspend fun MediaSack.addPicPending() {
        withBackDef(false) {
            com.pt.pro.gallery.objects.MyHash.run {
                addPending()
            }
        }.let {
            ctx.makeToastRecSus(com.pt.pro.R.string.ap, 0)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun MediaSack.displayTitle() = justCoroutine {
        binding.picName.text = if (isImage) dateModified.findPicTitle
        else FileLate(nameMedia.toStr).nameWithoutExtension
    }

    @com.pt.common.global.UiAnn
    private suspend fun MediaSack.displayFavorites(aa: MutableList<String>) = justCoroutine {
        if (aa.isFav(pathMedia)) {
            ctx.compactImage(com.pt.pro.R.drawable.ic_is_favorite) {
                binding.isFavorite.setImageDrawable(this)
            }
        } else {
            ctx.compactImage(com.pt.pro.R.drawable.ic_not_favorite) {
                binding.isFavorite.setImageDrawable(this@compactImage)
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun androidx.viewpager2.widget.ViewPager2.loadPager(imagePosition: Int) {
        withMain {
            funStateChanged()
            setCurrentItem(imagePosition, false)
        }
        withMain {
            pagingImages = MyAdapter()
            adapter = pagingImages
            setCurrentItem(imagePosition, false)
            offscreenPageLimit = 2
            setPageTransformer { page, position ->
                with(page) {
                    catchyUnit {
                        if (position != abs(position) && !haveOption) {
                            checkB = abs(position % 1) < 0.4
                            haveOption = true
                        }
                        if (imageForPager) {
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
                        } else {
                            if (checkB) {
                                if (position % 1 != 0.0F && position != abs(position)) {
                                    MIN_SCALE.coerceAtLeast(1 - abs(position)).let {
                                        scaleX = it
                                        scaleY = it
                                    }
                                } else {
                                    scrollX = 0
                                    scaleX = 1.0F
                                    scaleY = 1.0F
                                }
                            } else {
                                if (position % 1 != 0.0F && position == abs(position)) {
                                    MIN_SCALE.coerceAtLeast(1 - abs(position)).let {
                                        scaleX = it
                                        scaleY = it
                                    }
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


    @com.pt.common.global.UiAnn
    private suspend fun doOptionDisplay() {
        withMain {
            with<com.pt.pro.gallery.fasten.PopWindowGalleryFasten, Unit>(
                com.pt.pro.gallery.fasten.GalleryInflater.run { ctx.inflaterPopGallery() }
            ) {
                android.widget.PopupWindow(
                    root_, WRAP, WRAP, true
                ).applySus {
                    setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                    pagerHolder {
                        if (this@pagerHolder.pending || this@pagerHolder.isFileManager) {
                            pendingDisplay.justGone()
                        }
                        allImages[imagePosition].let { pic1 ->
                            if (pic1.checkHidden(this@pagerHolder).uriMedia == null || FileLate(pic1.pathMedia.toStr).name.checkHiddenMedia) {
                                com.pt.pro.R.string.hm.dStr
                            } else {
                                com.pt.pro.R.string.fa.dStr
                            }.let {
                                hide.text = it
                            }
                        }
                    }
                    selected.justGone()
                    favDis.justGone()
                    unFavDis.justGone()
                    DSackT(this@with, this@applySus).run {
                        delete.setOnClickListener(popClickList)
                        share.setOnClickListener(popClickList)
                        properties.setOnClickListener(popClickList)
                        setAs.setOnClickListener(popClickList)
                        rename.setOnClickListener(popClickList)
                        hide.setOnClickListener(popClickList)
                        clipboard.setOnClickListener(popClickList)
                        pendingDisplay.setOnClickListener(popClickList)
                        sendScanner.setOnClickListener(popClickList)
                    }
                    showAsDropDown(binding.pagerMenu)
                }
            }
        }
    }

    private suspend fun doFavUnFav() {
        withBack {
            allImages[imagePosition].also {
                com.pt.pro.gallery.objects.MyHash.favChange = true
                if (!favorites.isFav(it.pathMedia)) {
                    ctx.newDBGallerySus {
                        mutableListOf(it).insertFavMedia(0)
                    }
                    favorites.add(it.pathMedia.toStr)
                    displayDoFavorites(true)
                } else {
                    ctx.newDBGallerySus {
                        deleteFavorite(it.pathMedia.toStr)
                    }
                    favorites.remove(it.pathMedia)
                    displayDoFavorites(false)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun displayDoFavorites(isFav: Boolean) {
        withMain {
            ctx.compactImage(
                if (isFav) com.pt.pro.R.drawable.ic_is_favorite else com.pt.pro.R.drawable.ic_not_favorite
            ) {
                binding.isFavorite.setImageDrawable(this@compactImage)
            }
        }
    }

    private suspend fun MediaSack.doHide() {
        doHideHide()
        /*context.nullChecker()
        displayAfterHide(nameMedia)*/
    }

    /*@com.pt.common.global.UiAnn
    private suspend fun displayAfterHide(nameMedia: String?) {
        withMain {
            (nameMedia?.substring(nameMedia.lastIndexOf(".")) ?: "").checkHiddenMedia.let {
                if (!it) viewPagerSkip()
            }
        }
    }*/

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<String>.isFav(path: String?): Boolean = withBackDef(false) {
        contains(path)
    }


    override suspend fun viewPagerSkip() {
        withMain {
            mutableListOf<MediaSack>().apply {
                add(allImages[imagePosition])
            }.also {
                com.pt.pro.gallery.objects.MyHash.apply {
                    it.checkGalleryPending()
                }
            }
            if (allImages.size > 1) {
                pagingImages?.applySus {
                    refresh(imagePosition)
                }
                kotlinx.coroutines.delay(200L)
                funStateChanged()
            } else {
                act.supportFragmentManager.popBackStack()
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.clipboard() {
        withBack {
            allImages[imagePosition].pathMedia.let { itPic ->
                FileLate(itPic.toStr).parent?.toStr.let { iP ->
                    folderMedia.toMutableList().asSequence().filter {
                        it.path != iP
                    }
                }
            }.letSusBack { itA ->
                withMainNormal {
                    if (childFragmentManager.findFragmentByTag(DIALOG_CLIPBOARD) == null) {
                        newPopForClipboard {
                            folderMedia = itA.toMutableList()
                            folderPath = this@clipboard.toMutableList()
                            listener = clipboardListener
                            this@newPopForClipboard
                        }.show(childFragmentManager, DIALOG_CLIPBOARD)
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.properties() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_PROPERTIES) == null) {
                newPopForProperties {
                    folderPath = this@properties.toMutableList()
                    listener = false
                    this@newPopForProperties
                }.show(childFragmentManager, DIALOG_PROPERTIES)
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.deleteMedia() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_DELETE) == null) {
                newPopForDelete {
                    folderPath = this@deleteMedia.toMutableList()
                    isNight = nightRider
                    listener = deleteListener
                    this@newPopForDelete
                }.show(childFragmentManager, DIALOG_DELETE)
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    private suspend fun MediaSack.rename() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_RENAME) == null) {
                newRenameDialog {
                    fileHolder = null
                    mediaHold = this@rename
                    oneListener = renameListener
                    twoListener = null
                    this@newRenameDialog
                }.show(childFragmentManager, DIALOG_RENAME)
            }
        }
    }

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

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.PictureBrowserFasten.onLongClick(v: android.view.View): Boolean {
        when (v) {
            pagerMenu -> v.popUpComment(com.pt.pro.R.string.ly, com.pt.pro.R.attr.rmoBackHint, 0)
            isFavorite -> v.popUpComment(
                com.pt.pro.R.string.lt,
                com.pt.pro.R.attr.rmoBackHint,
                -catchy(0) { (pagerHold?.getMarginNumberDown?.invoke(rec.configuration) ?: 54) + ctx.actionBarHeight }
            )
            editActivity -> v.popUpComment(
                com.pt.pro.R.string.lu,
                com.pt.pro.R.attr.rmoBackHint,
                -catchy(0) { (pagerHold?.getMarginNumberDown?.invoke(rec.configuration) ?: 54) + ctx.actionBarHeight }
            )
        }
        return true
    }

    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        val pos = imagePosition
        binder?.imagePager?.adapter = null
        super.onConfigurationChanged(newConfig)
        launchImdMain {
            context.nullChecker()
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


    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        scrollerChang?.let {
            binding.imagePager.apply {
                adapter = null
                unregisterOnPageChangeCallback(it)
            }
        }
        activityLauncher?.unregister()
        binder?.unLockImage?.onViewDestroy()
        mediaPlay?.apply {
            synchronized(this) {
                catchy(Unit) {
                    pause()
                }
                catchy(Unit) {
                    stop()
                }
                catchy(Unit) {
                    release()
                }
            }
        }
        mediaPlay = null
        pagingImages = null
        allImagesNative = null
        folNative = null
        favoritesNative = null
        hide = false
        imageForPager = false
        haveOption = false
        checkB = false
        marginNum = 0
        imagePosition = 0
        slideOff = 0.0F
        slideState = com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED
        slideListen = null
        deleteListener = null
        senseListener = null
        scrollerChang = null
        clipboardListener = null
        renameListener = null
        activityLauncher = null
        binder = null
        pagerHold = null
        imageFragment = null
        vidFragment = null
        super.onDestroyView()
    }

}
