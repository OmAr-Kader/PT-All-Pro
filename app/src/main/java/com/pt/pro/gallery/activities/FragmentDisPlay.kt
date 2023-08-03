package com.pt.pro.gallery.activities

import androidx.core.widget.doAfterTextChanged
import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*
import com.pt.pro.gallery.fasten.FragmentDisplayFasten
import kotlin.math.abs

class FragmentDisPlay : ParentDisPlay(), () -> Unit {

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run {
                this@creBin.context.inflaterDisplay()
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
                it.displayContainer.myStatMargin(resources.statusBarHeight)
            }.root_
        }


    @com.pt.common.global.UiAnn
    override fun FragmentDisplayFasten.onViewCreated() {
        launchDef {
            justCoroutine {
                galleryMode = act.intent.getIntExtra(GALLERY_TYPE, GALLERY_ALL)
                displayType = act.intent.getIntExtra(DISPLAY_TYPE, DISPLAY_NORMAL)
                pickType = act.intent.getIntExtra(DISPLAY_PICK_TYPE, DISPLAY_NO_PICKING)
                folderPath = act.intent.getStringExtra(DISPLAY_FOLDER)
                columnNumber = ctx.findIntegerPrefDb(ID_COL, COL_NUM, 3)
                increase = ctx.findBooleanPrefDb(ID_RESOLE, RES_NUM, false).let { b ->
                    if (!b) columnNumber.findResole else columnNumber.findHighResole
                }
            }
            intiColors()
            itemInitDim {
                context.nullChecker()
                loadForDisplay(type = displayType, galleryMode = galleryMode, folderPath.toStr) {
                    withBack {
                        allMedia.clear()
                    }
                    withBack {
                        allMedia.addAll(this@loadForDisplay)
                    }
                }
                withDefault {
                    intiViews()
                }
            }
        }
    }

    private fun intiViews() {
        launchImdMain {
            context.nullChecker()
            withMain {
                rec.isLandTraditional.alsoSus { isLand ->
                    intiRec(isLand = isLand)
                }
            }
            withMain {
                binder?.root_?.fetchViewDim { rootWidth, rootHeight ->
                    intiViewDim(rootWidth, rootHeight)
                }
            }
            context.nullChecker()
            intiBasic()
            context.nullChecker()
            intiFirstResume()
            folds.intiFolders()
        }
    }

    private suspend fun intiRec(isLand: Boolean) {
        withMain {
            com.pt.pro.gallery.adapters.PictureAdapter(
                span = if (isLand) columnNumber * 2 else columnNumber,
            ).applySus {
                picListener = this@FragmentDisPlay
                this@applySus.widthX = this@FragmentDisPlay.widthColumn
                this@applySus.increase = this@FragmentDisPlay.increase
            }.alsoSus {
                iBindingSus {
                    pictureAda = it
                    recycler.adapter = it
                }
            }
        }
        withMain {
            iBindingSus {
                unPost(displayBarCard.loadRun.two)
                displayBarCard.justGoneSus()
                allMedia.loadUI()
            }
        }
    }


    private suspend fun intiColors() {
        withMain {
            qHand = ctx.fetchHand
            lifecycle.addObserver(this@FragmentDisPlay)
            iBindingSus {
                displayCard.intiBack21(them.findAttr(android.R.attr.colorPrimary))
                displayTitle.text = findTitle
                if (isPendOrHide) {
                    displayCard.backReColor(ctx.fetchColor(com.pt.pro.R.color.hbd))
                    displayTitle.setTextColor(ctx.fetchColor(com.pt.pro.R.color.whi))
                    searchIcon.svgReColorWhite()
                    reSort.svgReColorWhite()
                    backDisplay.svgReColorWhite()
                }
                displayBarCard.loadRun.rKTSack(LOAD_CONST).postAfterIfNot()
            }
        }
    }

    @android.annotation.SuppressLint("ClickableViewAccessibility")
    private suspend fun intiBasic() {
        withMain {
            iBindingSus {
                backDisplay.setOnClickListener(this@FragmentDisPlay)
                reSort.setOnClickListener(this@FragmentDisPlay)
                reSort.setOnLongClickListener(this@FragmentDisPlay)
                searchIcon.setOnClickListener(this@FragmentDisPlay)
                searchIcon.setOnLongClickListener(this@FragmentDisPlay)
                pointerScroll.addSenseListener(false) { _, event, type ->
                    when (type) {
                        DOWN_SEN -> {
                            isTouch = true
                            intY = event.rawY - start
                        }
                        UP_SEN -> {
                            isTouch = false
                            intY = event.rawY - start
                        }
                        MOVE_SEN -> {
                            @Strictfp
                            endScroll = when {
                                event.rawY < start -> 0.0F
                                event.rawY > endHeight -> endRatio
                                else -> event.rawY - start
                            }
                            @Strictfp
                            ratioPlus = when (endScroll) {
                                0.0F -> 0F
                                endRatio -> (allMedia.size - 1).toFloat()
                                else -> allMedia.size * (abs(endScroll) / endRatio)
                            }
                            pointerScroll.y = endScroll
                            recycler.scrollToPosition(ratioPlus.toInt())
                        }
                    }
                }
                recycler.scrollListener {
                    funScrollChangeDis()
                }
                scaleGestureDetector = android.view.ScaleGestureDetector(
                    ctx,
                    SimpleOnScale(
                        if (rec.isLandTraditional) {
                            columnNumber * 2
                        } else {
                            columnNumber
                        }.toFloat()
                    )
                )
                recycler.setOnTouchListener(this@FragmentDisPlay)
            }
        }
    }

    private fun intiViewDim(rootWidth: Int, rootH: Int) {
        updateDim(rootH.toFloat())
        findMargins(rootWidth, rootH)
    }

    @android.annotation.SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: android.view.View?, event: android.view.MotionEvent): Boolean {
        return if (event.pointerCount == 2) {
            scaleGestureDetector?.onTouchEvent(event)
            true
        } else {
            false
        }
    }

    private inner class SimpleOnScale(
        siz: Float
    ) : android.view.ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var newProduct: Float = siz
        override fun onScale(detector: android.view.ScaleGestureDetector): Boolean {
            catchy(Unit) {
                ((newProduct / detector.scaleFactor)).also { product ->
                    newProduct = product
                    toCatchSackAfter(234, 250L) {
                        catchy(Unit) {
                            binder?.apply {
                                if ((recycler.layoutManager as com.pt.common.moderator.recycler.NoAnimGridManager).spanCount == product.toInt() ||
                                    product.toInt() < 1
                                ) return@toCatchSackAfter
                                root_.fetchViewDim { rootWight, _ ->
                                    launchMain {
                                        recycler.fetchPictureAdapter(
                                            rootW = rootWight,
                                            product = product.toInt(),
                                            land = rec.isLandTraditional,
                                        ) {
                                            allMedia.loadUI()
                                        }
                                    }
                                }
                            }
                        }
                    }.postAfter()
                }
            }
            return true
        }
    }

    internal suspend fun androidx.recyclerview.widget.RecyclerView.fetchPictureAdapter(
        rootW: Int,
        product: Int,
        land: Boolean,
        ada: suspend com.pt.pro.gallery.adapters.PictureAdapter.() -> Unit
    ) {
        val widthC = if (land) {
            rootW / (product * 2)
        } else {
            rootW / product
        }
        if (rec.isLandTraditional) {
            com.pt.common.moderator.recycler.NoAnimGridManager(ctx, product * 2)
        } else {
            com.pt.common.moderator.recycler.NoAnimGridManager(ctx, product)
        }.run {
            layoutManager = this
            com.pt.pro.gallery.adapters.PictureAdapter(
                if (rec.isLandTraditional) {
                    product * 2
                } else {
                    product
                },
            ).apply {
                picListener = this@FragmentDisPlay
                this@apply.widthX = widthC
                this@apply.increase = this@FragmentDisPlay.increase
            }.also {
                pictureAda = it
                adapter = it
            }.alsoSus(ada)
        }
    }

    private suspend fun MutableList<MediaFolderSack>.intiFolders() {
        withBack {
            addAll(cont.allFoldersLoader(orderByFolder()))
            justScope {
                if (displayType == DISPLAY_STORY && ctx.findBooleanPreferenceNull(KEY_STORY) == null) {
                    binder?.displaySubFrame?.snakeBarForStory()
                } else return@justScope
            }
        }
    }

    private inline val findTitle: String
        get() {
            return when (displayType) {
                DISPLAY_FAVORITES -> com.pt.common.BuildConfig.FAVORITE
                DISPLAY_HIDDEN -> FileLate(folderPath.toStr).name.let { if (it != "0") it else "Internal" }
                DISPLAY_HIDDEN_TIME -> folderPath.toStr
                DISPLAY_PENDING -> com.pt.common.BuildConfig.PENDING_TABLE
                DISPLAY_NORMAL -> FileLate(folderPath.toStr).name.let { if (it != "0") it else "Internal" }
                DISPLAY_TIME -> folderPath.toStr
                DISPLAY_STORY -> rec.getString(com.pt.pro.R.string.uq)
                else -> FileLate(folderPath.toStr).name.let { if (it != "0") it else "Internal" }
            }
        }

    private suspend fun itemInitDim(a: suspend () -> Unit) {
        withMain {
            (!rec.isLandTraditional).letSus { port ->
                act.windowManager?.fetchDimensionsSus {
                    widthColumn = if (port) {
                        this@fetchDimensionsSus.width / columnNumber
                    } else {
                        this@fetchDimensionsSus.width / (columnNumber * 2)
                    }
                    binder?.recycler?.applySus {
                        layoutManager = if (port) {
                            com.pt.common.moderator.recycler.NoAnimGridManager(ctx, columnNumber)
                        } else {
                            com.pt.common.moderator.recycler.NoAnimGridManager(ctx, columnNumber * 2)
                        }
                    }
                    a.invoke()
                }
            }
        }
    }

    @com.pt.common.global.MainAnn
    private fun findMargins(rootWidth: Int, rootHeight: Int) {
        act.windowManager?.fetchDimensionsMan {
            (this@fetchDimensionsMan.width - rootWidth).also { mW ->
                (this@fetchDimensionsMan.height - rootHeight).also { mH ->
                    if (mW >= 0 && mH >= 0) {
                        marginWidth = mW
                        marginHeight = mH
                    }
                }
            }
            isDoInLand = rec.configuration.isLandTrad
        }
    }

    @com.pt.common.global.UiAnn
    override fun toRefresh() {
        if (context != null && !pictureAdapter.isActionMode) {
            if (binder?.displayBarCard?.isGon == true) {
                swipeToRefreshAll()
            }
        }
    }

    private fun swipeToRefreshAll() {
        launchDef {
            context.nullChecker()
            pictureAdapter.offMenuDisplay()
            context.nullChecker()
            withMain {
                iBindingSus {
                    displayBarCard.loadRun.rKTSack(LOAD_CONST).postAfterIfNot()
                }
            }
            loadForDisplay(type = displayType, galleryMode = galleryMode, folderPath.toStr) {
                withBack {
                    if (allMedia != this@loadForDisplay) {
                        withBack {
                            allMediaNative = this@loadForDisplay
                        }
                        loadUI()
                    } else {
                        afterHidden()
                    }
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    private fun funScrollChangeDis() {
        catchy(Unit) {
            pictureAdapter.adPosition.let { position ->
                if (position != 0 && !isTouch) {
                    if ((allMedia.size - position) > (columnNumber * 3)) {
                        position - (columnNumber * 3)
                    } else {
                        position
                    }.let { pos ->
                        (pos.toFloat() / allMedia.size.toFloat())
                    }.let { tempRatio ->
                        if (tempRatio > 0) tempRatio * endRatio else 0F
                    }.let { ratio ->
                        binder?.apply {
                            pointerScroll.y = ratio
                        }
                    }
                }
            }
        }
    }

    private suspend fun CharSequence?.doSearch() {
        if (!this@doSearch.isNullOrEmpty()) {
            if (!firstSearch) {
                firstSearch = true
            }
            withBackDef(mutableListOf()) {
                return@withBackDef searchList.runCatching {
                    return@runCatching searchList.toMutableList().asSequence().filter {
                        it.nameMedia.toStr.contains(this@doSearch, true)
                    }.distinct().toMutableList()
                }.getOrDefault(searchList)
            }.letSus {
                context.nullChecker()
                withMain {
                    binder?.recycler?.clearRecyclerPool()
                    pictureAdapter.applySus {
                        it.updateMedia()
                    }
                }
            }
        } else {
            if (firstSearch) {
                withMain {
                    binder?.recycler?.clearRecyclerPool()
                    pictureAdapter.applySus {
                        allMedia.updateMedia()
                    }
                }
            }
        }
    }

    private fun updateDim(heightPar: Float) {
        (rec.statusBarHeight + ctx.actionBarHeight.toFloat()).also { c ->
            start = c
            endRatio = heightPar - c - 70F.toPixel - marginHeight
            endHeight = endRatio + start
        }
    }

    private suspend fun intiFirstResume() {
        withMain {
            foundObserver {
                if (!isHaveRefresh) {
                    isHaveRefresh = true
                    runRef.rKTSack(500L).postAfter()
                }
            }.alsoSus {
                observing = it
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
            childFragmentManager.addOnBackStackChangedListener {
                fraMangerHaveChanges()
            }
        }
    }

    private fun fraMangerHaveChanges() {
        if (childFragmentManager.backStackEntryCount == 0) {
            if (isPendOrHide) {
                ctx.fetchColor(com.pt.pro.R.color.hbd)
            } else {
                them.findAttr(android.R.attr.colorPrimary)
            }.let<@androidx.annotation.ColorInt Int, Unit> { itP ->
                if (isPendOrHide) {
                    ctx.fetchColor(com.pt.pro.R.color.whi)
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
            if (com.pt.pro.gallery.objects.MyHash.favChange && displayType == DISPLAY_FAVORITES) {
                toRefresh()
            }
        } else {
            act.window?.also {
                if (isV_R) it.showSystemUI() else it.hideAllSystemUI()
                launchImdMain {
                    it.browser(
                        ctx.fetchColor(com.pt.pro.R.color.psc),
                        ctx.findBooleanPreference(GALLERY_SCREEN_ON, true)
                    )
                }
            }
        }
    }

    private val runRef: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            if (isPaused || pictureAdapter.isActionMode) {
                haveNewUpdate = true
            } else if (!isPaused && binder?.searchEdit.isGon) {
                isHaveRefresh = false
                toRefresh()
            }
        }

    @com.pt.common.global.MainAnn
    override fun onResume() {
        if (haveNewUpdate) {
            toRefresh()
        }
        super.onResume()
        isHaveRefresh = false
        haveNewUpdate = false
        isPaused = false
    }

    override fun invoke() {
        reload()
    }

    private fun reload() {
        launchDef {
            doReload()
            context.nullChecker()
            allMedia.loadUI()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun doReload() {
        loadForDisplay(type = displayType, galleryMode = galleryMode, folderPath.toStr) {
            context.nullChecker()
            withBack {
                allMedia.clear()
            }
            withBack {
                allMedia.addAll(this@loadForDisplay)
            }
        }
    }

    override fun onPause() {
        isPaused = true
        super.onPause()
    }


    private suspend inline fun loadForDisplay(
        type: Int,
        galleryMode: Int,
        filePath: String,
        crossinline list: suspend MutableList<MediaSack>.() -> Unit,
    ) {
        withBack {
            when (type) {
                DISPLAY_HIDDEN -> scannerHidden(filePath, galleryMode)
                DISPLAY_PENDING -> medPend.distinct().toMutableList()
                DISPLAY_TIME -> loadForTime(galleryMode)
                DISPLAY_HIDDEN_TIME -> scannerHiddenTime(
                    act.intent.getLongExtra(DISPLAY_TIME_START, -1L).createTime,
                    act.intent.getLongExtra(DISPLAY_TIME_END, -1L).createTime,
                    galleryMode
                )
                DISPLAY_FAVORITES -> {
                    ctx.newDBGallerySus {
                        getAllVFavMedia()
                    }
                }
                DISPLAY_STORY -> {
                    mutableListOf<MediaSack>().applySusBack {
                        ctx.storesPathes().onEachSusBack(context) {
                            cont.getListFolder(this@onEachSusBack).alsoSusBack(this@applySusBack::addAll)
                        }
                    }.distinctBy {
                        it.nameMedia
                    }.toMutableList().orderAllLoaders(orderBy())
                }
                else -> load(folderPath = filePath, galleryM = galleryMode, ord = orderBy())
            }.applySusBack(list)
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun load(folderPath: String, galleryM: Int, ord: String): MutableList<MediaSack> = justCoroutine {
        return@justCoroutine withBackDef(mutableListOf()) {
            when (galleryM) {
                GALLERY_VID -> cont.allVideosLoader(folderPath, ord).orderAllLoaders(ord).toMutableList()
                GALLERY_IMG -> cont.allImageLoader(folderPath, ord).orderAllLoaders(ord).toMutableList()
                else -> {
                    cont.allVideosLoader(folderPath, ord).letSusBack { vi ->
                        return@letSusBack cont.allImageLoader(folderPath, ord).runSusBack {
                            return@runSusBack mutableListOf<MediaSack>().applySusBack {
                                addAll(vi)
                                addAll(this@runSusBack)
                            }
                        }
                    }.orderAllLoaders(ord).toMutableList()
                }
            }
        }
    }

    internal suspend fun MutableList<MediaSack>.loadUI() {
        binder?.recycler?.clearRecyclerPool()
        pictureAdapter.applySus {
            updateMedia()
        }
        afterHidden()
        reloadSearchList()
    }

    private suspend fun MutableList<MediaSack>.reloadSearchList() {
        withBack {
            searchList.clear()
        }
        withBack {
            searchList.addAll(this@reloadSearchList)
        }
    }

    override fun MutableList<MediaSack>.onPicClicked(position: Int, p: () -> Unit) {
        toCatchSack(146) {
            ctx.launchKeyBoard {
                if (isAcceptingText) {
                    hideSoftInputFromWindow(binder?.searchEdit?.windowToken, 0)
                }
            }
        }.postNow()
        onPicClicked(position)
        animRun = p
    }

    @com.pt.common.global.WorkerAnn
    private fun MutableList<MediaSack>.onPicClicked(position: Int) {
        launchDef {
            if (pickType == DISPLAY_NO_PICKING) {
                com.pt.pro.gallery.objects.PagerHolder(
                    mediaHolder = this@onPicClicked.toMutableList(),
                    folds = folds.toMutableList(),
                    imagePosition = position,
                    pending = isPendOrHide,
                    main = false,
                    isHiddenActive = displayType == DISPLAY_HIDDEN || displayType == DISPLAY_HIDDEN_TIME,
                    isFileManager = false,
                    isDoInLand = isDoInLand,
                    margeWidth = marginWidth,
                    margeHeight = marginHeight
                ).letSusBack {
                    withMainNormal {
                        newBrowserFragment {
                            it.init()
                            this@newBrowserFragment
                        }.alsoSus {
                            childFragmentManager.doLaunchGalleryImage(BROWSER_FRAGMENT) {
                                add(binder?.root_?.id ?: return@doLaunchGalleryImage, it, BROWSER_FRAGMENT)
                            }
                        }
                    }
                }
            } else {
                this@onPicClicked[position].uriMedia.letSusBack { uriStr ->
                    uriStr?.toUri ?: ctx.uriProviderNormal(
                        this@onPicClicked[position].pathMedia,
                        com.pt.pro.BuildConfig.APPLICATION_ID
                    )
                }.letSusBack {
                    android.content.Intent().applySusBack {
                        data = it
                        act.setResult(-1, this)
                    }
                }
                act.finish()
            }
        }
    }

    override fun justDisplayAction(text: String) {
        launchImdMain {
            withMain {
                if (stubOptions == null) {
                    displayOptions(text)
                } else {
                    stubOptions?.root_?.visibleFade(300L)
                }
                justCoroutineMain {
                    ctx.launchKeyBoard {
                        if (isAcceptingText) {
                            hideSoftInputFromWindow(binder?.searchEdit?.windowToken, 0)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun onActionDisplay(text: String, siz: Int) {
        stubOptions?.menuNumber?.text = text
        if (siz == 0) {
            launchMain {
                pictureAdapter.offMenuDisplay()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun displayOptions(text: String) {
        withMain {
            binder?.stubOptionsDisplay?.applySus {
                com.pt.pro.gallery.fasten.GalleryInflater.run {
                    layoutInflater.context.inflaterOption()
                }.alsoSus {
                    it.optionProvider(text)
                    this@applySus.addView(it.root_)
                    this@applySus.justVisible()
                    it.root_.visibleFade(300L)
                    stubOptions = it
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun FragmentDisplayFasten.onClick(v: android.view.View) {
        launchImdMain {
            withMain {
                when (v) {
                    stubOptions?.deleteDate -> pictureAdapter.deleteCopy.deleteDialog()
                    stubOptions?.share -> ctx.shareMedia(
                        pictureAdapter.deleteCopy,
                        com.pt.pro.BuildConfig.APPLICATION_ID,
                        s = resources.getString(com.pt.pro.R.string.p1),
                    ) {
                        runCatching<Unit> {
                            startActivity(this@shareMedia)
                        }.getOrElse {
                            ctx.makeToastRecSus(com.pt.pro.R.string.px, 0)
                        }
                    }
                    stubOptions?.backDisplayMenu -> pictureAdapter.offMenuDisplay()
                    binder?.backDisplay -> act.onBackPressedDispatcher.onBackPressed()
                    binder?.searchIcon -> showSearch()
                    binder?.reSort -> reSort.popWindow(this@FragmentDisPlay, true)
                    stubOptions?.pagerMenu -> doDisplayOption()
                    stubOptions?.clipDate -> {
                        pictureAdapter.applySus {
                            deleteCopy.clipDate()
                            justNotify()
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun FragmentDisplayFasten.onLongClick(v: android.view.View): Boolean {
        when (v) {
            binder?.searchIcon -> com.pt.pro.R.string.sq
            stubOptions?.deleteDate -> com.pt.pro.R.string.ll
            stubOptions?.share -> com.pt.pro.R.string.lf
            binder?.reSort -> com.pt.pro.R.string.fl
            stubOptions?.pagerMenu -> com.pt.pro.R.string.ly
            else -> 0
        }.let {
            v.popUpComment(it, com.pt.pro.R.attr.rmoBackground, 0)
        }
        return true
    }

    @com.pt.common.global.UiAnn
    private suspend fun FragmentDisplayFasten.showSearch() {
        withMain {
            if (searchEdit.isVis) {
                searchEdit.justGone()
                displayTitle.justVisible()
                searchEdit.text?.clear()
                //pictureAdapter.isActionMode = false
                launchDef {
                    withMain {
                        recycler.clearRecyclerPool()
                        pictureAdapter.applySus { allMedia.updateMedia() }
                    }
                }
            } else {
                displayTitle.justGone()
                searchEdit.applySus {
                    doAfterTextChanged {
                        pushJob { j ->
                            launchDef {
                                j?.checkIfDone()
                                it.doSearch()
                            }
                        }
                    }
                    justVisible()
                    requestFocus()
                    ctx.launchKeyBoard {
                        showSoftInput(searchEdit, 1)
                    }
                }
                //pictureAdapter.isActionMode = true
            }
        }
    }


    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        binder?.recycler?.adapter = null
        super.onConfigurationChanged(newConfig)
        iBinding {
            reIntiViews(newConfig)
        }
    }

    private fun FragmentDisplayFasten.reIntiViews(
        newConfig: android.content.res.Configuration
    ) {
        launchImdMain {
            withMain {
                ctx.actionBarHeight.alsoSus { action ->
                    context.nullChecker()
                    withMain {
                        displayContainer.myStatMargin(resources.statusBarHeight)
                        displaySubFrame.myActMargin(action)
                        displayCard.cardAsView(action)
                        if (stubOptions != null) stubOptions?.root_?.cardAsView(action)
                        displayTitle.apply {
                            textColors.also {
                                editAppearance()
                                setTextColor(it)
                            }
                        }
                        searchEdit.apply {
                            textColors.also {
                                editAppearance()
                                setTextColor(it)
                            }
                        }
                        if (!rec.configuration.isTablet) {
                            val marginWid = marginWidth
                            val marginHe = marginHeight
                            marginWidth = marginHe
                            marginHeight = marginWid
                        }
                        isDoInLand = rec.isLandTraditional
                        root_.fetchViewDim { _, rootHeight ->
                            updateDim(rootHeight.toFloat())
                        }
                    }
                    itemInitDim {
                        context.nullChecker()
                        withMain {
                            scaleGestureDetector = android.view.ScaleGestureDetector(
                                ctx,
                                SimpleOnScale(
                                    if (newConfig.isLandTrad) {
                                        columnNumber * 2
                                    } else {
                                        columnNumber
                                    }.toFloat()
                                )
                            )
                            intiRec(isLand = newConfig.isLandTrad)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun doBackForOptions() {
        launchMain {
            justCoroutineMain {
                iBinding {
                    if (searchEdit.isVis) {
                        searchEdit.apply {
                            text?.clear()
                            ctx.launchKeyBoard {
                                if (isAcceptingText) {
                                    hideSoftInputFromWindow(windowToken, 0)
                                }
                            }
                            justGone()
                        }
                        displayTitle.justVisible()
                        recycler.clearRecyclerPool()
                        pictureAdapter.applySus {
                            allMedia.updateMedia()
                        }
                    }
                }
            }
            pictureAdapter.offMenuDisplay()
        }
    }

    override suspend fun com.pt.pro.gallery.adapters.PictureAdapter.offMenuDisplay() {
        withMain {
            stubOptions?.apply {
                if (root_.isVis) {
                    root_.justGoneSus()
                    refreshAdapter()
                    binder?.displayBarCard?.apply {
                        unPost(loadRun.two)
                        justGoneSus()
                    }
                }
            }
            if (haveNewUpdate) {
                toRefresh()
                haveNewUpdate = false
            }
        }
    }

    override fun onBack() {
        when {
            com.pt.pro.gallery.objects.MyHash.isLock -> return
            binder?.searchEdit?.isVis == true -> doBackForOptions()
            pictureAda?.isActionMode == true -> doBackForOptions()
            childFragmentManager.backStackEntryCount != 0 -> childFragmentManager.popBackStack()
            else -> act.finish()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        observing?.let {
            cont.unregisterContentObserver(
                it
            )
        }
        pictureAda?.onAdapterDestroy()
        allMediaNative = null
        searchListNative = null
        foldsNative = null
        favoritesNative = null
        deleteListener = null
        hidListener = null
        clipListener = null
        renameListener = null
        catchy(Unit) {
            binder?.recycler?.adapter = null
            binder?.recycler?.removeAllViewsInLayout()
            binder?.root_?.removeAllViewsInLayout()
        }
        pictureAda = null
        displayType = DISPLAY_NORMAL
        pickType = DISPLAY_NO_PICKING
        folderPath = null
        firstSearch = false
        observing = null
        isTouch = false
        galleryMode = GALLERY_ALL
        super.onDestroyView()
    }

}