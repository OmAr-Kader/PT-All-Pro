package com.pt.pro.gallery.activities

import androidx.core.widget.doAfterTextChanged
import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.common.global.MediaFolderSack

class FragmentGallery : ParentFragmentGallery<com.pt.pro.gallery.fasten.FragmentGalleryFasten>(), com.pt.pro.gallery.interfaces.GalleryListener,
    com.pt.common.mutual.base.BackInterface, () -> Unit {

    @Volatile
    private var galleryMode: Int = GALLERY_ALL

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    private var animRun: (() -> Unit)? = null

    private var fragmentAlbum: FragmentAlbumGallery? = null

    private var fragmentAll: FragmentAllGallery? = null

    private var fragmentTime: FragmentTimeGallery? = null

    private var fragmentSearch: FragmentSearch? = null

    private var foldsNative: MutableList<MediaFolderSack>? = mutableListOf()
    private inline val folds: MutableList<MediaFolderSack>
        get() = foldsNative ?: mutableListOf<MediaFolderSack>().also {
            foldsNative = it
        }

    @Volatile
    private var hiddenRunning: Boolean = false

    private var marginWidth = 0
    private var marginHeight = 0
    private var isDoInLand = false

    private var isHaveRefresh = false
    private var isPaused = false
    private var haveNewUpdate = false

    private var observer: android.database.ContentObserver? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run {
                this@creBin.context.inflaterGallery()
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
                it.initViews()
            }.root_
        }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.onViewCreated() {
        lifecycle.addObserver(this@FragmentGallery)
        initDim()
        pushJob { j ->
            launchImdMain {
                j?.checkIfDone()
                intiAnim()
                initImageMode()
                context.nullChecker()
                justScope {
                    intiGallery()
                }
                context.nullChecker()
                withMain {
                    reloadMain.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    reloadImages.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    reloadVideos.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    hidden.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    searchIcon.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    reSort.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    pendingFrame.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    storyButton.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    extendGallery.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    camera.apply {
                        setOnClickListener(this@FragmentGallery)
                        setOnLongClickListener(this@FragmentGallery)
                    }
                    mainBack.setOnClickListener(this@FragmentGallery)
                }
                context.nullChecker()
                storyVisButton(isHaveStoresPathes())
                context.nullChecker()
                justCoroutine {
                    intiFirstResume()
                }
            }
        }
    }

    private fun intiGallery() {
        launchDef {
            getFolders().alsoSusBack {
                justScope {
                    foldsNative = it
                }
                context.nullChecker()
                doIntiGallery()
            }
        }
    }

    private suspend fun doIntiGallery() {
        withMain {
            when (loadAll()) {
                GALLERY_ALL -> initLoadAll()
                GALLERY_FOLDER -> initLoadFolders()
                else -> initLoadTime()
            }
            withMain {
                if (folds.isEmpty())
                    binder?.empty.justVisibleSus()
                else
                    binder?.empty.justGoneSus()
            }
        }
    }

    @com.pt.common.global.MainAnn
    private fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.initViews() {
        reloadMain.tag = SCALE_IN
        reloadImages.tag = SCALE_IN
        reloadVideos.tag = SCALE_IN
    }

    private suspend fun intiAnim() {
        context.nullChecker()
        withMain {
            binder?.tripleAnimation(300L, GALLERY_ALL)
        }
    }

    private suspend fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.storyVisButton(isHave: Boolean) {
        withMain {
            if (isHave) storyButton.visibleBottom(500L) else storyButton.justGone()
        }
    }

    @com.pt.common.global.MainAnn
    private fun initDim() {
        act.windowManager?.fetchDimensionsMan {
            binding.root_.fetchViewDim { rootWidth, rootHeight ->
                (this@fetchDimensionsMan.width - rootWidth).also { mW ->
                    (this@fetchDimensionsMan.height - rootHeight).also { mH ->
                        if (mW >= 0 && mH >= 0) {
                            marginWidth = mW
                            marginHeight = mH
                        }
                    }
                }
                isDoInLand = rec.configuration.isLandTrad
                launchDef {
                    if (!ctx.findBooleanPreference(B_RES_FIR, false)) {
                        ctx.updatePrefBoolean(RES_NUM, kotlin.math.min(width, height) > 800)
                        ctx.updatePrefBoolean(B_RES_FIR, true)
                    }
                }
            }
        }
        /*if(mIsPickIntent) {
            imagesMode = intent.hasImageContentData()
            videosMode = intent.hasVideoContentData()
        }*/
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.onLongClick(v: android.view.View): Boolean {
        when (v) {
            reloadImages -> v.popUpComment(R.string.ki, R.attr.rmoBackHint, 0)
            reloadVideos -> v.popUpComment(R.string.kv, R.attr.rmoBackHint, 0)
            camera -> v.popUpComment(R.string.oc, R.attr.rmoBackHint, 0)
            extendGallery -> v.popUpComment(R.string.ew, R.attr.rmoBackHint, 0)
            storyButton -> v.popUpComment(R.string.uq, R.attr.rmoBackHint, (-1 * 110F.toPixel))
            hidden -> v.popUpComment(R.string.ls, R.attr.rmoBackHint, 0)
            pendingFrame -> v.popUpComment(R.string.vw, R.attr.rmoBackHint, 0)
            reloadMain -> v.popUpComment(R.string.kc, R.attr.rmoBackHint, 0)
            searchIcon -> v.popUpComment(R.string.sq, R.attr.rmoBackHint, 0)
            reSort -> v.popUpComment(R.string.fl, R.attr.rmoBackHint, 0)
        }
        return true
    }

    private suspend fun initLoadAll() {
        doInitLoadAll()
        context.nullChecker()
        initImageMode()
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun doInitLoadAll() {
        withMainNormal {
            newFragmentAllGallery {
                itemListener = this@FragmentGallery
                setISHidden(hiddenRunning, galleryMode)
                fragmentAll = this@newFragmentAllGallery
                return@newFragmentAllGallery this@newFragmentAllGallery
            }.alsoSus {
                this@FragmentGallery.childFragmentManager.fragmentLauncher(FRAGMENT_GALLERY) {
                    add(binding.galleryModeFrame.id, it, FRAGMENT_GALLERY)
                }
            }
        }
    }

    private suspend fun initLoadTime() {
        doInitLoadTime()
        context.nullChecker()
        initImageMode()
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun doInitLoadTime() {
        withMainNormal {
            newFragmentTimeGallery {
                itemListener = this@FragmentGallery
                setISHidden(hiddenRunning, galleryMode)
                fragmentTime = this@newFragmentTimeGallery
                return@newFragmentTimeGallery this@newFragmentTimeGallery
            }.alsoSus {
                this@FragmentGallery.childFragmentManager.fragmentLauncher(FRAGMENT_GALLERY) {
                    add(binding.galleryModeFrame.id, it, FRAGMENT_GALLERY)
                }
            }
        }
    }

    private suspend fun initLoadFolders() {
        doInitLoadFolders()
        context.nullChecker()
        withMain {
            initImageMode()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun doInitLoadFolders() {
        withMainNormal {
            newFragmentAlbumGallery {
                itemListener = this@FragmentGallery
                setISHidden(hiddenRunning, galleryMode)
                fragmentAlbum = this@newFragmentAlbumGallery
                return@newFragmentAlbumGallery this@newFragmentAlbumGallery
            }.alsoSus {
                this@FragmentGallery.childFragmentManager.fragmentLauncher(FRAGMENT_GALLERY) {
                    add(binding.galleryModeFrame.id, it, FRAGMENT_GALLERY)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun initImageMode() {
        context.nullChecker()
        withMain {
            when (loadAll()) {
                GALLERY_ALL -> {
                    binding.reSort.justVisible()
                    R.drawable.ic_load_all
                }
                GALLERY_FOLDER -> {
                    binding.reSort.justVisible()
                    R.drawable.ic_load_folders
                }
                else -> {
                    binding.reSort.justGone()
                    R.drawable.ic_load_time
                }
            }.let {
                context?.compactImage(it) {
                    binding.reloadMain.setImageDrawable(this@compactImage)
                }
            }
        }
    }

    private fun doMainLoader() {
        if (galleryMode != GALLERY_ALL) {
            galleryMode = GALLERY_ALL
            launchMain {
                binding.tripleAnimation(300L, GALLERY_ALL)
            }
            launchDef {
                justScope {
                    if (hiddenRunning) {
                        doReHidden()
                    } else {
                        actionUnHidden()
                    }
                }
            }
        } else {
            launchDef {
                foldsNative = cont.allFoldersLoader(orderByFolder())
                context.nullChecker()
                withBack {
                    when (loadAll()) {
                        GALLERY_ALL -> {
                            ctx.updatePrefInt(GAL_MODE, GALLERY_FOLDER)
                            initLoadFolders()
                        }
                        GALLERY_FOLDER -> {
                            ctx.updatePrefInt(GAL_MODE, GALLERY_TIME)
                            initLoadTime()
                        }
                        else -> {
                            ctx.updatePrefInt(GAL_MODE, GALLERY_ALL)
                            initLoadAll()
                        }
                    }
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    private fun displayUpdateMode(mode: Int) {
        launchMain {
            withMain {
                justScope {
                    galleryMode = mode
                    binding.tripleAnimation(300L, mode)
                }
                justScope {
                    if (hiddenRunning) {
                        when (loadAll()) {
                            GALLERY_ALL -> fragmentAll?.scannerHidden(mode)
                            GALLERY_FOLDER -> fragmentAlbum?.scannerHidden(mode)
                            else -> fragmentTime?.scannerHidden(mode)
                        }
                        fragmentSearch?.scannerHidden(galleryMode)
                    } else {
                        when (loadAll()) {
                            GALLERY_ALL -> fragmentAll?.doInitAllLoad(mode)
                            GALLERY_FOLDER -> fragmentAlbum?.doInitAllLoad(mode)
                            else -> fragmentTime?.doInitAllLoad(mode)
                        }
                        fragmentSearch?.doInitAllLoad(galleryMode)
                    }
                }
            }
        }
    }

    private fun intiFirstResume() {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                withBack {
                    foundObserver {
                        if (!isHaveRefresh && !(isV_R && hiddenRunning)) {
                            isHaveRefresh = true
                            runRef.rKTSack(500L).postBackAfter()
                        }
                    }.alsoSusBack {
                        observer = it
                        cont.registerContentObserver(
                            android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            true,
                            it
                        )
                        cont.registerContentObserver(
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            true,
                            it
                        )
                    }
                    this@FragmentGallery.childFragmentManager.addOnBackStackChangedListener {
                        fraMangerHaveChanges()
                    }
                    if (act.intent.isImageViewIntent || act.intent.isVideoViewIntent) {
                        act.launchViewer()
                    }
                }
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onPause() {
        isPaused = true
        super.onPause()
    }

    @com.pt.common.global.MainAnn
    override fun onResume() {
        medPend.showPendingText()
        if (haveNewUpdate || com.pt.pro.gallery.objects.MyHash.favChange) {
            toRefresh()
            com.pt.pro.gallery.objects.MyHash.favChange = false
        }
        super.onResume()
        isHaveRefresh = false
        haveNewUpdate = false
        isPaused = false
    }

    @com.pt.common.global.UiAnn
    private fun fraMangerHaveChanges() {
        if (this@FragmentGallery.childFragmentManager.backStackEntryCount == 0) {
            if (hiddenRunning) {
                ctx.fetchColor(R.color.hbd)
            } else {
                them.findAttr(android.R.attr.colorPrimary)
            }.let<@androidx.annotation.ColorInt Int, Unit> { itP ->
                if (hiddenRunning) {
                    ctx.fetchColor(R.color.whi)
                } else {
                    them.findAttr(android.R.attr.textColorPrimary)
                }.let<@androidx.annotation.ColorInt Int, Unit> { itT ->
                    them.findAttr(android.R.attr.navigationBarColor)
                        .let<@androidx.annotation.ColorInt Int, Unit> { color ->
                            act.apply {
                                if (requestedOrientation != UNSPECIFIED_SCREEN) {
                                    requestedOrientation = UNSPECIFIED_SCREEN
                                }
                                animRun?.invoke()
                                window.sliderDestroy(
                                    nav = color,
                                    txt = itT,
                                    pri = itP
                                )
                            }
                        }
                }
            }
            medPend.showPendingText()
            if (com.pt.pro.gallery.objects.MyHash.favChange) {
                toRefresh()
                com.pt.pro.gallery.objects.MyHash.favChange = false
            }
        } else {
            act.window?.also {
                if (isV_R) it.showSystemUI() else it.hideAllSystemUI()
                launchImdMain {
                    it.browser(
                        ctx.fetchColor(R.color.psc),
                        ctx.findBooleanPreference(GALLERY_SCREEN_ON, true)
                    )
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.onClick(v: android.view.View) {
        when (v) {
            mainBack -> act.onBackPressedDispatcher.onBackPressed()
            reSort -> displayResort()
            searchIcon -> displaySearch()
            pendingFrame -> displayPending()
            storyButton -> displayStory()
            extendGallery -> run<Unit> { if (extendOption.isVis) extendOption.goneTop(250L) else extendOption.visibleTop(400L) }
            hidden -> doHidden()
            camera -> launchCam()
            reloadVideos -> displayUpdateMode(GALLERY_VID)
            reloadImages -> displayUpdateMode(GALLERY_IMG)
            reloadMain -> displayMainReload()
        }
    }

    private fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.displayMainReload() {
        launchImdMain {
            if (searchEdit.isVis) removeSearch() else doMainLoader()
        }
    }

    private fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.displayResort() {
        launchImdMain {
            justScope {
                if (searchEdit.isVis) removeSearch()
            }
            withMain {
                reSort.popWindow(this@FragmentGallery,  false)
            }
        }
    }

    private fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.displaySearch() {
        launchImdMain {
            if (searchEdit.isVis) removeSearch() else addSearch()
        }
    }

    private fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.displayPending() {
        if (medPend.isNotEmpty()) {
            launchImdMain {
                extendOption.goneTopSus(200L)
                displayActivity(
                    DSack(
                        false,
                        com.pt.common.BuildConfig.PENDING_TABLE,
                        true
                    ).findDisplayType,
                    com.pt.common.BuildConfig.PENDING_TABLE,
                    isFromFolder = false
                )
            }
        } else {
            ctx.makeToastRec(R.string.pe, 0)
        }
    }

    private fun displayStory() {
        launchDef {
            displayActivity(displayType = DISPLAY_STORY, pathFolder = "", isFromFolder = false)
        }
    }

    private fun launchCam() {
        launchDef {
            withDefault {
                startActivity(android.content.Intent.createChooser(launchCamera, "Camera"))
            }
        }
    }

    private suspend fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.removeSearch() {
        withMain {
            picName.justVisible()
            searchEdit.apply {
                text?.clear()
                justGone()
                runCatching {
                    ctx.launchKeyBoard {
                        if (isAcceptingText) {
                            hideSoftInputFromWindow(windowToken, 0)
                        }
                    }
                }
            }
            binding.frameForSearch.constraintPara(0, 0) {
                topToTop = 0
                startToStart = 0
            }
        }
        withMain {
            fragmentSearch?.also {
                childFragmentManager.beginTransaction().remove(it).commit()
                fragmentSearch = null
            }
        }
        justScope {
            loadAll().also {
                if (it != GALLERY_TIME) {
                    withMain {
                        reSort.justVisible()
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun initLoadSearch() {
        withMainNormal {
            newFragmentSearch {
                itemListener = this@FragmentGallery
                setISHidden(hiddenRunning, galleryMode)
                return@newFragmentSearch this@newFragmentSearch
            }.alsoSus {
                binding.frameForSearch.constraintPara(0, 0) {
                    topToTop = 0
                    endToEnd = 0
                    bottomToBottom = 0
                    startToStart = 0
                }
                this@FragmentGallery.fragmentSearch = it
                this@FragmentGallery.childFragmentManager.fragmentLauncher(FRAGMENT_SEARCH) {
                    add(binding.frameForSearch.id, it, FRAGMENT_SEARCH)
                }
            }
        }
    }


    private suspend fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.addSearch() {
        initLoadSearch()
        displayForSearch()
    }

    @com.pt.common.global.UiAnn
    private suspend fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.displayForSearch() {
        withMain {
            picName.justGone()
            reSort.justGone()
            searchEdit.apply {
                doAfterTextChanged {
                    fragmentSearch?.onSearch(it)
                }
                justVisible()
                requestFocus()
                ctx.launchKeyBoard {
                    showSoftInput(this@apply, 1)
                }
            }
        }
    }

    private inline val DSack<Boolean, String, Boolean>.findDisplayType: Int
        get() {
            return when {
                three -> {
                    DISPLAY_PENDING
                }
                two == com.pt.common.BuildConfig.FAVORITE -> {
                    DISPLAY_FAVORITES
                }
                one -> {
                    DISPLAY_HIDDEN
                }
                else -> {
                    DISPLAY_NORMAL
                }
            }
        }

    private fun doHidden() {
        if (!hiddenRunning) {
            launchImdMain {
                binder?.displayForHidden()
            }
            launchDef {
                doReHidden()
            }
        } else {
            launchImdMain {
                displayForUnHidden()
            }
            launchDef {
                actionUnHidden()
            }
        }
    }

    private suspend fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.displayForHidden() {
        withMain {
            justScope {
                hiddenRunning = true
                act.window.applySus {
                    myBaseActivity(false)
                    statusBarColor = ctx.fetchColor(R.color.hbd)
                }
            }
            justScope {
                ctx.compactImage(R.drawable.ic_hidden_show) {
                    hidden.setImageDrawable(this@compactImage)
                }
                mainBack.svgReColorWhite()
                extendGallery.svgReColorWhite()
                reloadMain.svgReColorWhite()
                reloadImages.svgReColorWhite()
                reloadVideos.svgReColorWhite()
                picName.setTextColor(ctx.fetchColor(R.color.whi))
                searchEdit.setTextColor(ctx.fetchColor(R.color.whi))
                galleryCard.backReColor(ctx.fetchColor(R.color.hbd))
            }
            forSnakes.snakeBar()
        }
    }

    private suspend fun doReHidden() {
        withBack {
            when (loadAll()) {
                GALLERY_ALL -> fragmentAll?.scannerHidden(galleryMode)
                GALLERY_FOLDER -> fragmentAlbum?.scannerHidden(galleryMode)
                else -> fragmentTime?.scannerHidden(galleryMode)
            }
            fragmentSearch?.scannerHidden(galleryMode)
        }
    }

    private suspend fun actionUnHidden() {
        withMain {
            when (loadAll()) {
                GALLERY_ALL -> fragmentAll?.doInitAllLoad(galleryMode)
                GALLERY_FOLDER -> fragmentAlbum?.doInitAllLoad(galleryMode)
                else -> fragmentTime?.doInitAllLoad(galleryMode)
            }
            fragmentSearch?.doInitAllLoad(galleryMode)
        }
    }

    private suspend fun displayForUnHidden() {
        withMain {
            hiddenRunning = false
            act.window.apply {
                statusBarColor = them.findAttr(android.R.attr.colorPrimary)
                myBaseActivity(
                    them.findAttr(
                        android.R.attr.textColorPrimary
                    ) == android.graphics.Color.BLACK
                )
            }
            with(binding) {
                ctx.compactImage(R.drawable.ic_hidden) {
                    hidden.setImageDrawable(this@compactImage)
                }
                them.findAttr(android.R.attr.textColorPrimary).let {
                    mainBack.svgReColor(it)
                    extendGallery.svgReColor(it)
                    reloadMain.svgReColor(it)
                    reloadImages.svgReColor(it)
                    reloadVideos.svgReColor(it)
                    picName.setTextColor(it)
                    searchEdit.setTextColor(it)
                }
                galleryCard.backReColor(them.findAttr(android.R.attr.colorPrimary))
            }
        }
    }

    private val runRef: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            if (isPaused) {
                haveNewUpdate = true
            } else {
                isHaveRefresh = false
                toRefresh()
            }
        }


    override fun toRefresh() {
        if (context != null) {
            launchDef {
                foldsNative = getFolders()
                context.nullChecker()
                forRefresh()
            }
        }
    }

    private suspend fun forRefresh() {
        withMain {
            if (binding.searchEdit.isVis) {
                return@withMain
            } else {
                when (loadAll()) {
                    GALLERY_ALL -> fragmentAll?.toRefresh()
                    GALLERY_FOLDER -> fragmentAlbum?.toRefresh()
                    else -> fragmentTime?.toRefresh()
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun MediaSack.addMediaPending() {
        with(com.pt.pro.gallery.objects.MyHash) {
            if (addPending()) {
                binding.extendGallery.apply {
                    svgReColor(them.findAttr(android.R.attr.colorAccent))
                    handler?.postDelayed({
                        catchy(Unit) {
                            if (hiddenRunning) {
                                svgReColorWhite()
                            } else {
                                svgReColor(them.findAttr(android.R.attr.textColorPrimary))
                            }
                        }
                    }, 400)
                    startAnimation(shakeAnimation)
                }
            }
        }
        medPend.showPendingText()
    }

    private fun MutableList<MediaSack>.showPendingText() {
        binder?.pendingText?.apply {
            if (size != 0) {
                if (!isVis) justVisible()
                text = size.toString()
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    override fun String.onFolderClicked(pathFolder: String, realFolderCLick: Boolean) {
        launchDef {
            displayActivity(
                DSack(hiddenRunning, this@onFolderClicked, false).findDisplayType,
                pathFolder,
                isFromFolder = realFolderCLick
            )
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun DSack<Long, Long, String>.onTimeClicked(displayType: Int) {
        launchDef {
            displayActivity(displayType, three, one, two, isFromFolder = false)
        }
    }

    private suspend fun displayActivity(
        displayType: Int,
        pathFolder: String,
        fromTime: Long = -1L,
        toTime: Long = -1L,
        isFromFolder: Boolean,
    ) {
        withBack {
            ctx.newIntentSus(DisplayMedia::class.java) {
                putExtra(GALLERY_TYPE, galleryMode)
                putExtra(DISPLAY_TYPE, displayType)
                putExtra(DISPLAY_FROM_FOLDERS, isFromFolder)
                putExtra(
                    DISPLAY_PICK_TYPE,
                    DSack(mIsPickIntent, multiplyPick, "").findPickType
                )
                putExtra(DISPLAY_FOLDER, pathFolder)

                putExtra(DISPLAY_TIME_START, fromTime)
                putExtra(DISPLAY_TIME_END, toTime)
                this@newIntentSus
            }.applySusBack {
                if (!mIsPickIntent) {
                    this@FragmentGallery.startActivity(this)
                } else {
                    resultGal?.launch(this)
                }
            }
        }
    }

    private inline val DSack<Boolean, Boolean, String>.findPickType: Int
        get() {
            return when {
                one && two -> DISPLAY_PICK_MULTI
                one && !two -> DISPLAY_PICK_NORMAL
                else -> DISPLAY_NO_PICKING
            }
        }

    private var resultGal: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK && it.data != null) {
                android.content.Intent().apply {
                    flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    if (it.data?.data != null) {
                        data = it.data?.data
                    }
                    if (it.data?.clipData != null) {
                        clipData = it.data?.clipData
                    }
                }.also { intent ->
                    act.setResult(androidx.appcompat.app.AppCompatActivity.RESULT_OK, intent)
                    act.finish()
                }
            }
        }

    override fun MutableList<MediaSack>.onPicClicked(
        position: Int,
        p: () -> Unit,
    ) {
        animRun = p
        onPicClicked(position)
    }

    override fun invoke() {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                doResort()
            }
        }
    }


    private suspend fun doResort() = justCoroutine {
        withDefault {
            when (loadAll()) {
                GALLERY_ALL -> {
                    if (!hiddenRunning) {
                        fragmentAll?.doInitAllLoad(galleryMode)
                    } else {
                        fragmentAll?.scannerHidden(galleryMode)
                    }
                }
                GALLERY_FOLDER -> {
                    if (!hiddenRunning) {
                        fragmentAlbum?.doInitAllLoad(galleryMode)
                    } else {
                        fragmentAlbum?.scannerHidden(galleryMode)
                    }
                }
                else -> {
                    if (!hiddenRunning) {
                        fragmentTime?.doInitAllLoad(galleryMode)
                    } else {
                        fragmentTime?.scannerHidden(galleryMode)
                    }
                }
            }
            /*fragmentSearch?.apply {
                if (!hiddenRunning) {
                    doInitAllLoad(galleryMode)
                } else {
                    scannerHidden(galleryMode)
                }
            }*/
        }
    }


    private inline val MutableList<MediaSack>.fetchPagerHolder: (Int) -> com.pt.pro.gallery.objects.PagerHolder
        get() = {
            com.pt.pro.gallery.objects.PagerHolder(
                mediaHolder = toMutableList(),
                folds = folds.toMutableList(),
                imagePosition = it,
                pending = hiddenRunning,
                main = true,
                isHiddenActive = hiddenRunning,
                isFileManager = false,
                isDoInLand = isDoInLand,
                margeWidth = marginWidth,
                margeHeight = marginHeight
            )
        }

    private fun MutableList<MediaSack>.launchBrowser(position: Int) {
        launchMain {
            withMainNormal {
                iBindingSus {
                    runCatching {
                        ctx.launchKeyBoard {
                            if (isAcceptingText) {
                                hideSoftInputFromWindow(searchEdit.windowToken, 0)
                            }
                        }
                    }
                }
            }
            withMainNormal {
                fetchPagerHolder(position).letSus {
                    newBrowserFragment {
                        it.init()
                        this@newBrowserFragment
                    }.alsoSus {
                        this@FragmentGallery.childFragmentManager.doLaunchGalleryImage(BROWSER_FRAGMENT) {
                            add(binding.root_.id, it, BROWSER_FRAGMENT)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun MutableList<MediaSack>.onPicClicked(position: Int) {
        if (!mIsPickIntent) {
            launchBrowser(position)
        } else {
            getOrNull(position)?.sendMedia()
        }
    }

    private fun MediaSack.sendMedia() {
        launchDef {
            if (this@sendMedia.uriMedia != null) {
                this@sendMedia.uriMedia.toUri
            } else {
                if (this@sendMedia.isImage) {
                    IMAGE
                } else {
                    VIDEO
                }.letSusBack { t ->
                    ctx.getFileUri(
                        this@sendMedia.pathMedia.toString(),
                        t,
                        com.pt.pro.BuildConfig.APPLICATION_ID
                    )
                }
            }.letSusBack { uri ->
                android.content.Intent().run {
                    flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    data = uri
                    act.setResult(androidx.appcompat.app.AppCompatActivity.RESULT_OK, this)
                }
            }
            act.finish()
        }
    }


    private suspend fun getFolders(): MutableList<MediaFolderSack> = justCoroutine {
        return@justCoroutine withBackDef(mutableListOf()) {
            cont.allFoldersLoader(orderByFolder())
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun androidx.appcompat.app.AppCompatActivity.launchViewer() {
        withBack {
            if (intent.data != null) {
                viewProvider(
                    intent.data ?: return@withBack,
                    intent.isImageViewIntent,
                    orderBy(),
                    orderByFolder(),
                    hiddenRunning,
                    isDoInLand,
                    marginWidth,
                    marginHeight
                ) {
                    doLaunchViewer()
                }
            }
        }
    }

    private fun com.pt.pro.gallery.objects.PagerHolder.doLaunchViewer() {
        launchMain {
            withMainNormal {
                newBrowserFragment {
                    this@doLaunchViewer.init()
                    this@newBrowserFragment
                }.alsoSus {
                    this@FragmentGallery.childFragmentManager.doLaunchGalleryImage(BROWSER_FRAGMENT) {
                        add(binder?.root_?.id ?: return@doLaunchGalleryImage, it, BROWSER_FRAGMENT)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun android.view.ViewGroup.snakeBar() {
        withMain {
            if (childCount == 0) {
                with(com.pt.pro.gallery.fasten.GalleryInflater.run { layoutInflater.context.inflaterDelete() }) {
                    snakeText.framePara(MATCH, MATCH) {
                        gravity = android.view.Gravity.CENTER_VERTICAL
                        marginEnd = 65F.toPixel
                    }
                    this@snakeBar.addView(this@with.root_)
                    snakeText.text = rec.getString(R.string.ve)
                    cancelDelete.justGone()
                    confirmDelete.also {
                        it.text = rec.getString(R.string.yk)
                        it.setOnClickListener {
                            this@with.deleteFrame.goneFade(250)
                            handler?.postDelayed({
                                catchy(Unit) {
                                    this@snakeBar.removeView(this@with.root_)
                                }
                            }, 300)
                        }
                    }
                    this@with.deleteFrame.visibleBottom(300L)
                }
            } else return@withMain
        }
    }

    override val onBackCheck: Boolean
        get() {
            return when {
                com.pt.pro.gallery.objects.MyHash.isLock -> false
                this@FragmentGallery.childFragmentManager.backStackEntryCount != 0 -> {
                    this@FragmentGallery.childFragmentManager.popBackStack()
                    false
                }
                binder?.searchEdit?.isVis == true -> {
                    pushJob { j ->
                        launchDef {
                            j?.checkIfDone()
                            binder?.removeSearch()
                        }
                    }
                    false
                }
                act.intent.isImageViewIntent || act.intent.isVideoViewIntent || mIsPickIntent -> {
                    act.finish()
                    false
                }
                else -> {
                    true
                }
            }
        }

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.gallery.fasten.FragmentGalleryFasten.tripleAnimation(scaleDur: Long, mode: Int) {
        val imageAnim: Float
        val videoAnim: Float
        val mainAnim: Float
        when (mode) {
            GALLERY_IMG -> {
                imageAnim = SCALE_IN
                videoAnim = SCALE_OUT
                mainAnim = SCALE_OUT
            }
            GALLERY_VID -> {
                imageAnim = SCALE_OUT
                videoAnim = SCALE_IN
                mainAnim = SCALE_OUT
            }
            else -> {
                imageAnim = SCALE_OUT
                videoAnim = SCALE_OUT
                mainAnim = SCALE_IN
            }
        }
        reloadImages.apply {
            scaleViewFromTo((tag ?: return@apply).toString().toFloat(), imageAnim, scaleDur) {
                tag = imageAnim
                startAnimation(this)
            }
        }
        reloadVideos.apply {
            scaleViewFromTo(tag.toString().toFloat(), videoAnim, scaleDur) {
                tag = videoAnim
                startAnimation(this)
            }
        }
        reloadMain.apply {
            scaleViewFromTo(tag.toString().toFloat(), mainAnim, scaleDur) {
                tag = mainAnim
                startAnimation(this)
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        launchImdMain {
            withMain {
                if (binding.searchEdit.isVis) {
                    binding.removeSearch()
                }
                binding.applySus {
                    frameGalleryMain.myStatMargin(rec.statusBarHeight)
                    ctx.actionBarHeight.letSus {
                        galleryCard.cardAsView(it)
                        frameAllGallery.myActMarginCon(it)
                    }
                    picName.applySus {
                        textColors.also {
                            editAppearance()
                            setTextColor(it)
                        }
                    }
                    searchEdit.applySus {
                        textColors.also {
                            editAppearance()
                            setTextColor(it)
                        }
                    }
                    withMain {
                        if (!newConfig.isTablet) {
                            val marginWid = marginWidth
                            val marginHe = marginHeight
                            marginWidth = marginHe
                            marginHeight = marginWid
                        }
                        isDoInLand = newConfig.isLandTrad
                    }
                }
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        observer?.let {
            cont.unregisterContentObserver(
                it
            )
        }
        resultGal?.unregister()
        allMediaNative = null
        allFavoriteNative = null
        fragmentTime = null
        fragmentAll = null
        fragmentAlbum = null
        foldsNative = null
        hiddenRunning = false
        resultGal = null
        observer = null
        fragmentSearch = null
        catchy(Unit) {
            binder?.root_?.removeAllViewsInLayout()
        }
        super.onDestroyView()
    }

}