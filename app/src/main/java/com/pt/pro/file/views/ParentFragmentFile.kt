package com.pt.pro.file.views

import com.pt.common.global.*
import com.pt.common.media.getFileType
import com.pt.common.media.getFileUri
import com.pt.common.stable.*
import kotlinx.coroutines.cancelChildren

abstract class ParentFragmentFile : ParentFileFragment(), com.pt.common.mutual.base.BackInterface {

    @Volatile
    protected var isRight: Boolean = false

    protected var folderMana: com.pt.common.moderator.recycler.NoAnimLinearManager? = null
    protected var stubDel: com.pt.pro.gallery.fasten.StubDeleteFasten? = null

    protected var searchJob: kotlinx.coroutines.Job? = null

    @Volatile
    protected var searchActive: Boolean = false

    @Volatile
    protected var firstSearch: Boolean = false

    @Volatile
    protected var isTouch: Boolean = false

    @Volatile
    protected var details: Boolean = false

    @Volatile
    protected var stillOpenOption: Boolean = false

    @Volatile
    protected var category: Int = CATO_IMAGE

    private var tHNative: androidx.collection.ArrayMap<Int, androidx.appcompat.widget.AppCompatTextView>? = androidx.collection.arrayMapOf()
    private inline val txtHash: androidx.collection.ArrayMap<Int, androidx.appcompat.widget.AppCompatTextView>
        get() = tHNative ?: androidx.collection.arrayMapOf<Int, androidx.appcompat.widget.AppCompatTextView>().also {
            tHNative = it
        }

    internal inline val isPicture: suspend () -> Boolean
        get() = {
            ctx.findBooleanPreference(IMG_APP, true)
        }

    internal inline val isVideo: suspend () -> Boolean
        get() = {
            ctx.findBooleanPreference(VID_APP, true)
        }

    internal inline val isAudio: suspend () -> Boolean
        get() = {
            ctx.findBooleanPreference(MUS_APP, true)
        }

    internal inline val isPdfViewer: suspend () -> Boolean
        get() = {
            ctx.findBooleanPreference(PDF_APP, true)
        }

    internal inline val isTextViewer: suspend () -> Boolean
        get() = {
            ctx.findBooleanPreference(TEXT_APP, true)
        }

    internal inline val isZipViewer: suspend () -> Boolean
        get() = {
            ctx.findBooleanPreference(ZIP_APP, true)
        }

    abstract suspend fun doBackOption()
    abstract suspend fun doSearchBack()
    abstract suspend fun doBackDelete()

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.file.fasten.FileInflater.run {
                this@creBin.context.inflaterFragmentFile()
            }.also {
                @ViewAnn
                binder = it

                if (rec.isLandTraditional) it.changeForLand() else it.changeForPort()
            }.root_
        }

    @com.pt.common.global.MainAnn
    protected fun android.content.Context.updateDim(heightPar: Float) {
        (if (rec.isLandTraditional) actionBarHeight.toFloat() + rec.statusBarHeight + 40F.toPixel else actionBarHeight.toFloat() + rec.statusBarHeight + 140F.toPixel).also { c ->
            start = c
            endRatio = heightPar - c - 70F.toPixel - marginHeight
            endHeight = endRatio + start
        }
    }


    @com.pt.common.global.MainAnn
    protected fun com.pt.pro.file.fasten.FragmentFileFasten.changeForLand() {
        codFav.framePara(100F.toPixel, MATCH) {
            topMargin = 40F.toPixel
        }
        androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
            MATCH, MATCH
        ).apply {
            behavior = com.pt.common.moderator.recycler.OverScrollBounceBehavior()
        }.also(recyclerFavorites::setLayoutParams)
        subMangerFrame.framePara(MATCH, MATCH) {
            topMargin = 40F.toPixel
            marginStart = 100F.toPixel
        }
    }

    @com.pt.common.global.MainAnn
    protected fun com.pt.pro.file.fasten.FragmentFileFasten.changeForPort() {
        codFav.framePara(MATCH, 100F.toPixel) {
            topMargin = 40F.toPixel
        }
        androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(
            MATCH, MATCH
        ).apply {
            behavior = com.pt.common.moderator.recycler.OverScrollHorizontalBehavior()
        }.also(recyclerFavorites::setLayoutParams)
        subMangerFrame.framePara(MATCH, MATCH) {
            topMargin = 140F.toPixel
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.file.fasten.FragmentFileFasten.onLongClick(
        v: android.view.View,
    ): Boolean {
        when (v) {
            extendFile -> com.pt.pro.R.string.ew
            includeOptions.pendingFrame -> com.pt.pro.R.string.vq
            includeOptions.hidden -> com.pt.pro.R.string.lp
            includeOptions.reSort -> com.pt.pro.R.string.fh
            clipboardFrame -> com.pt.pro.R.string.sw
            includeOptions.createFolder -> com.pt.pro.R.string.sl
            includeOptions.searchFile -> com.pt.pro.R.string.sb
            swipeMode -> com.pt.pro.R.string.sx
            else -> null
        }.let {
            it?.let { it1 -> v.popUpComment(it1, com.pt.pro.R.attr.rmoBackHint, 0) }
        }
        return true
    }

    protected inline val androidx.cardview.widget.CardView?.loadRun: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            this@loadRun?.justVisible()
            unPost(11)
        }

    @com.pt.common.global.UiAnn
    protected suspend fun FolderAdapter.offDisplayMenu(hiddenActive: Boolean) {
        withMain {
            if (isActionMode) {
                val lastPos = withMainDef(0) {
                    folderMana?.findFirstCompletelyVisibleItemPosition() ?: 0
                }
                justCoroutineMain {
                    binder?.picName?.applySus {
                        text = com.pt.pro.R.string.fq.dStr
                        if (hiddenActive)
                            setTextColor(ctx.fetchColor(com.pt.pro.R.color.whi))
                        else
                            setTextColor(them.findAttr(android.R.attr.textColorPrimary))
                    }
                    ctx.compactImageReturn(com.pt.pro.R.drawable.ripple_rectangle).refreshAdapter()
                }
                justCoroutineMain {
                    folderMana?.scrollToPositionWithOffset(lastPos, 0)
                }
            }
        }
    }

    protected suspend fun hideHiddenTittle() {
        withMain {
            binder?.hiddenTittleLinear?.justGoneSus()
        }
    }

    protected suspend fun beforeUpdateRecycler() {
        hideHiddenTittle()
        withBack {
            coroutineContext.cancelChildren()
        }
        withMain {
            binder?.fileBarCard.loadRun.rKTSack(LOAD_CONST).postAfterIfNot()
        }
    }

    protected suspend fun afterUpdateRecycler() {
        withMain {
            binder?.fileBarCard.also {
                unPost(it.loadRun.rKTSack(LOAD_CONST).two)
                it.justGoneSus()
            }
        }
    }

    protected var listOptionFasten: com.pt.pro.file.fasten.ListOptionFasten? = null

    @com.pt.common.global.UiAnn
    override suspend fun com.pt.pro.file.fasten.FragmentFileFasten.refresh() {
        withMain {
            includeShare.linearScroll.applySus {
                if (this@applySus.childCount != 0) {
                    this@applySus.goneFade(150)
                    this@applySus.removeAllViews()
                }
            }
            stillOpenOption = false
            listOptionFasten = null
        }
    }

    @com.pt.common.global.UiAnn
    protected suspend fun deleteRefresh(hiddenActive: Boolean) {
        withMain {
            iBindingSus {
                forSnakes.removeView(stubDel?.root_)
                picName.applySus {
                    text = com.pt.pro.R.string.fq.dStr
                    if (hiddenActive)
                        setTextColor(ctx.fetchColor(com.pt.pro.R.color.whi))
                    else
                        setTextColor(them.findAttr(android.R.attr.textColorPrimary))
                }
            }
        }
        withMain {
            folderAdapter.applySus {
                deleteActive = false
                ctx.compactImageReturn(com.pt.pro.R.drawable.ripple_rectangle).refreshAdapter()
            }
            stubDel = null
            onlyFolder.clear()
            onlyMedia.clear()
        }
    }

    protected fun doRecover() {
        pushJob {
            launchDef {
                withMain {
                    binder?.refresh()
                    folderAda?.offDisplayMenu(hiddenActive)
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    protected suspend fun androidx.collection.ArrayMap<Int, androidx.appcompat.widget.AppCompatTextView>.removeText(
        pos: Int,
    ) {
        context.nullChecker()
        withMain {
            keys.reversed().toMutableList().onEachSus(context) {
                if (this@onEachSus >= pos) {
                    binder?.filesLinear?.removeChild(this@removeText[this@onEachSus])
                    this@removeText.removeAtIndexMap(this@onEachSus)
                    fileCLick.removeAtIndex(this@onEachSus)
                }
            }
        }
    }


    @Volatile
    private var haveSnakeAndroid = false

    @com.pt.common.global.UiAnn
    @com.pt.common.global.APIAnn(android.os.Build.VERSION_CODES.R)
    override suspend fun android.view.ViewGroup.snakeBarAndroid(
        parentChildCount: Int,
    ) {
        if (childCount == parentChildCount && !haveSnakeAndroid) {
            withMainNormal {
                with(com.pt.pro.gallery.fasten.GalleryInflater.run { layoutInflater.context.inflaterDelete() }) {
                    this@snakeBarAndroid.addView(this@with.root_)
                    snakeText.text = com.pt.pro.R.string.jw.dStr
                    cancelDelete.setOnClickListener {
                        this@with.deleteFrame.goneBottom(300L)
                        toCatchSackAfter(6465, 400L) {
                            this@snakeBarAndroid.removeView(this@with.root_)
                            haveSnakeAndroid = false
                        }.postAfter()
                    }
                    confirmDelete.apply {
                        text = com.pt.pro.R.string.qw.dStr
                        confirmDelete.setOnClickListener {
                            forAndroidData.let(::startActivity)
                            this@with.deleteFrame.goneBottom(300L)
                            toCatchSackAfter(4645, 400L) {
                                this@snakeBarAndroid.removeView(this@with.root_)
                                haveSnakeAndroid = false
                            }.postAfter()
                        }
                    }
                    this@with.deleteFrame.visibleBottom(300L)
                    haveSnakeAndroid = true
                }
            }
        }
    }

    @Volatile
    private var haveSnakeHidden = false

    @com.pt.common.global.UiAnn
    protected fun android.view.ViewGroup.snakeBar(parentChildCount: Int) {
        if (childCount == parentChildCount && !haveSnakeHidden) {
            with(
                com.pt.pro.gallery.fasten.GalleryInflater.run { layoutInflater.context.inflaterDelete() }
            ) {
                snakeText.framePara(MATCH, MATCH) {
                    gravity = android.view.Gravity.CENTER_VERTICAL
                    marginEnd = 65F.toPixel
                }
                this@snakeBar.addView(this@with.root_)
                snakeText.text = com.pt.pro.R.string.ve.dStr
                cancelDelete.justGone()
                confirmDelete.apply {
                    text = com.pt.pro.R.string.yk.dStr
                    confirmDelete.setOnClickListener {
                        this@with.deleteFrame.goneBottom(300L)
                        toCatchSackAfter(35, 400L) {
                            haveSnakeHidden = false
                            this@snakeBar.removeView(this@with.root_)
                        }.postAfter()
                    }
                }
                haveSnakeHidden = true
                this@with.deleteFrame.visibleBottom(300L)
            }
        }
    }


    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        binder?.recyclerFavorites?.adapter = null
        super.onConfigurationChanged(newConfig)
        //pushJob {
        launchImdMain {
            withMain {
                iBindingSus {
                    subFileFrame.myStatMargin(resources.statusBarHeight)
                    galleryCard.cardAsView(ctx.actionBarHeight)
                    fileFrame.myActMargin(ctx.actionBarHeight)
                    if (
                        newConfig.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
                    ) {
                        changeForLand()
                    } else {
                        changeForPort()
                    }
                    picName.apply {
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
                    recyclerFavorites.apply {
                        layoutManager = if (rec.isLandTraditional) ctx.getVerManager else ctx.getManager
                        adapter = favAdapter
                    }
                    if (!rec.configuration.isTablet) {
                        val marginWid = marginWidth
                        val marginHe = marginHeight
                        marginWidth = marginHe
                        marginHeight = marginWid
                    }
                    isDoInLand = rec.isLandTraditional
                    root_.fetchViewDim { _, rootHeight ->
                        ctx.updateDim(rootHeight.toFloat())
                    }
                }
            }
        }
        //}
    }

    @com.pt.common.global.UiAnn
    override fun fraMangerHaveChanges() {
        if (childFragmentManager.backStackEntryCount == 0) {
            if (hiddenActive) {
                ctx.fetchColor(com.pt.pro.R.color.hbd)
            } else {
                them.findAttr(android.R.attr.colorPrimary)
            }.let<@androidx.annotation.ColorInt Int, Unit> { itP ->
                if (hiddenActive) {
                    ctx.fetchColor(com.pt.pro.R.color.whi)
                } else {
                    them.findAttr(android.R.attr.textColorPrimary)
                }.let<@androidx.annotation.ColorInt Int, Unit> { itT ->
                    them.findAttr(
                        android.R.attr.navigationBarColor
                    ).let<@androidx.annotation.ColorInt Int, Unit> { color ->
                        act.apply {
                            if (requestedOrientation != UNSPECIFIED_SCREEN) {
                                requestedOrientation = UNSPECIFIED_SCREEN
                            }
                            window.sliderDestroy(
                                nav = color,
                                txt = itT,
                                pri = itP
                            )
                        }
                    }
                }
            }
        } else {
            if (childFragmentManager.findFragmentByTag(FRAGMENT_TEXT) != null) {
                act.window?.also {
                    launchImdMain {
                        it.browserTextViewer(
                            nav = them.findAttr(com.pt.pro.R.attr.rmoBackground),
                            ifTrue = ctx.findBooleanPreference(TEXT_SCREEN_ON, true),
                            isBlack = !nightRider
                        )
                    }
                }
            } else if (childFragmentManager.findFragmentByTag(PDF_BROWSER_FRAGMENT) != null) {
                act.window?.also {
                    if (isV_R) it.showSystemUI() else it.hideAllSystemUI()
                    launchImdMain {
                        it.browser(
                            ctx.fetchColor(com.pt.pro.R.color.psc),
                            ctx.findBooleanPreference(PDF_SCREEN_ON, true)
                        )
                    }
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
    }

    @com.pt.common.global.UiAnn
    protected suspend fun com.pt.pro.file.fasten.ListOptionFasten.displayDoFavorite(pos: Int) {
        withMain {
            if (pos == 1) {
                favorite?.goneFadeSus(200L)
            } else {
                unFavorite?.goneFadeSus(200L)
            }
        }
    }


    protected suspend fun checkIfHaveHidden(filePath: String) {
        withMain {
            FileUpdateSack(
                filePath,
                NORMAL_FILE,
            ).addScrollPath(FileLate(filePath).name)
            binder?.includeOptions?.hidden?.applySus {
                if (this@applySus.isGon) this@applySus.justVisible()
            }
        }
    }

    protected suspend fun com.pt.pro.file.fasten.ListOptionFasten.afterCheckFav(boolean: Boolean) {
        withMain {
            if (boolean) {
                favorite.justGoneSus()
            } else {
                favorite.justVisibleSus()
            }
        }
    }

    protected suspend fun com.pt.pro.file.fasten.ListOptionFasten.displayCheckVir(boolean: Boolean) {
        withMain {
            if (boolean) {
                removeVir.justGoneSus()
            } else {
                removeVir.justVisibleSus()
            }
        }
    }

    protected suspend fun com.pt.pro.file.fasten.ListOptionFasten.doCheckFav(boolean: Boolean) {
        withMain {
            if (boolean) {
                unFavorite.justGoneSus()
            } else {
                unFavorite.justVisibleSus()
            }
        }
    }

    protected val com.pt.pro.file.fasten.ListOptionFasten.shareOptionClickListener: android.view.View.OnClickListener
        @com.pt.common.global.UiAnn
        get() = android.view.View.OnClickListener { v ->
            folderAdapter.deleteCopy.apply {
                if (this@apply.isNotEmpty()) {
                    when (v) {
                        shareOpt -> {
                            launchDef {
                                lastOrNull()?.letSusBack {
                                    FileLate(it.filePath).letSusBack { f ->
                                        f.getFileType ?: "*/*"
                                    }.letSusBack { itType ->
                                        ctx.getFileUri(
                                            it.filePath,
                                            it.typeFile,
                                            com.pt.pro.BuildConfig.APPLICATION_ID
                                        ).letSusBack { nP ->
                                            it.shareOption(itType, nP)
                                        }
                                    }
                                }
                            }
                        }

                        deleteOpt -> launchDef { this@apply.deleteMedia() }
                        renameOpt -> launchDef { rename() }
                        properties -> launchDef { this@apply.toMutableList().properties() }
                        selectAll -> launchDef { folderAdapter.selectAll() }
                        hide -> launchDef { this@apply.toMutableList().hideFiles() }
                        copy -> launchDef { addToClipboard(true) }
                        move -> launchDef { addToClipboard(false) }
                        addPend -> launchDef { pending() }
                        favorite -> launchDef { doFavorite() }
                        unFavorite -> launchDef { unDoFavorite() }
                        openAs -> {
                            launchDef {
                                lastOrNull()?.letSusBack {
                                    it.openAs(
                                        context?.getFileUri(
                                            it.filePath,
                                            it.typeFile,
                                            com.pt.pro.BuildConfig.APPLICATION_ID
                                        ) ?: return@letSusBack
                                    )
                                }
                            }
                        }
                        addVir -> launchDef { this@apply.toMutableList().addToVir() }
                        removeVir -> launchDef { this@apply.toMutableList().removeToVir() }
                        openWith -> {
                            launchDef {
                                lastOrNull()?.wholeFiles()
                            }
                        }
                        backOpt -> doRecover()
                    }
                } else {
                    ctx.makeToastRec(com.pt.pro.R.string.qe, 0)
                }
            }
        }

    abstract suspend fun MutableList<FileSack>.deleteMedia()


    private suspend fun MutableList<FileSack>.doFavorite() {
        listOptionFasten?.displayDoFavorite(1)
        context.nullChecker()
        actionDoFavorite()
        context.nullChecker()
        reloadFavorites()
    }


    private suspend fun MutableList<FileSack>.unDoFavorite() {
        listOptionFasten?.displayDoFavorite(2)
        context.nullChecker()
        actionUnDoFavorite()
        context.nullChecker()
        reloadFavorites()
    }

    protected suspend fun com.pt.pro.file.fasten.ListOptionFasten.displayCheckNonFolder(it: Boolean) {
        withMain {
            runCatching {
                if (!it) {
                    //this[16].justGoneSus()
                    openWith.justGoneSus()
                    openAs.justGoneSus()
                    //shareOpt.justGoneSus()
                } else {
                    //this[16].justVisibleSus()
                    openWith.justVisibleSus()
                    openAs.justVisibleSus()
                    //shareOpt.justVisibleSus()
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected suspend fun doBackNormal() {
        context.nullChecker()
        justCoroutineMain {
            txtHash.removeText(txtHash.size - 1)
            fileCLick.apply {
                this[lastIndex].doReloadFile()
            }
        }
    }

    protected fun ifAlreadyOpened(pos: Int) {
        pushJob {
            launchDef {
                withMain {
                    fileCLick[pos].doReloadFile()
                    if (pos + 1 != txtHash.size) txtHash.removeText(pos + 1)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected suspend fun FileUpdateSack.addScrollPath(name: String) {
        withMain {
            when (fileType) {
                PENDING_FILE -> them.findAttr(android.R.attr.colorAccent)
                VIR_FILE -> them.findAttr(android.R.attr.colorAccent)
                CATO_FILE -> them.findAttr(android.R.attr.colorAccent)
                else -> them.findAttr(com.pt.pro.R.attr.rmoGrey)
            }.letSus { c ->
                context?.fetchScrollView {
                    this@fetchScrollView.setTextColor(c)
                    this@fetchScrollView.text = name.letSus { itN ->
                        if (isRight) " < $itN" else "$itN > "
                    }
                    this@fetchScrollView.setOnClickListener(txtScrollListener)
                    binder?.filesLinear?.addView(this@fetchScrollView)
                    txtHash[txtHash.size] = this@fetchScrollView
                }
            }
            fileCLick.add(this@addScrollPath)
            toCatchSackAfter(571, 100L) {
                binder?.scrollFile?.fullScroll(android.widget.HorizontalScrollView.FOCUS_RIGHT)
            }.postAfter()
        }
    }

    protected suspend fun com.pt.pro.file.fasten.FragmentFileFasten.checkSearchVis() {
        withMain {
            searchEdit.apply {
                if (isVis) {
                    picName.justVisible()
                    justGone()
                    text?.clear()
                    context?.launchKeyBoard {
                        hideSoftInputFromWindow(windowToken, 0)
                    }
                }
            }
        }
    }

    private val txtScrollListener: android.view.View.OnClickListener
        get() = android.view.View.OnClickListener {
            if (fileCLick.isNotEmpty()) {
                pushJob { j ->
                    launchDef {
                        j?.checkIfDone()
                        it.txtScroll()
                        doBackOption()
                    }
                }
            }
        }

    private suspend fun android.view.View.txtScroll() {
        withMain {
            txtHash.values.indexOf(this@txtScroll).letSus { i ->
                fileCLick.getOrNull(i)?.doReloadFile()
                if (i + 1 != txtHash.size) txtHash.removeText(i + 1)
            }
        }
        withMain {
            binder?.refresh()
            folderAdapter.offDisplayMenu(hiddenActive)
            binder?.checkSearchVis()
        }
    }

    private var resortPop: android.widget.PopupWindow? = null

    @com.pt.common.global.UiAnn
    internal suspend fun android.view.View.popWindow() {
        withMain {
            with(com.pt.pro.databinding.PopWindowResortBinding.inflate(layoutInflater)) {
                android.widget.PopupWindow(root, WRAP, WRAP, true).applySus {
                    setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                    withMain {
                        resortPop = this@applySus
                        setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                    }
                    intiPopView(
                        tarColor = them.findAttr(android.R.attr.colorAccent),
                        wholeColor = them.findAttr(com.pt.pro.R.attr.rmoText),
                        isFolder = isNowFolder()
                    )
                    withMain {
                        showAsDropDown(this@popWindow)
                    }

                    showAsDropDown(this@popWindow)
                }
            }
        }
    }

    private suspend fun com.pt.pro.databinding.PopWindowResortBinding.intiPopView(
        @androidx.annotation.ColorInt tarColor: Int,
        @androidx.annotation.ColorInt wholeColor: Int,
        isFolder: Boolean
    ) {
        withMain {
            recolorViewWindow(
                up = if (isFolder) orderFolderBy() else orderFileBy(),
                tarColor = tarColor,
                wholeColor = wholeColor
            )
            popClickList(resortPop, tarColor, wholeColor).apply {
                nameAsc.setOnClickListener(this)
                nameDesc.setOnClickListener(this)
                dateAsc.setOnClickListener(this)
                dateDesc.setOnClickListener(this)
                sizeAsc.setOnClickListener(this)
                sizeDesc.setOnClickListener(this)
            }
            radioFolder.isChecked = isFolder
            radioFolder.jumpDrawablesToCurrentState()
            radioFile.isChecked = !isFolder
            radioFile.jumpDrawablesToCurrentState()

            sizeAsc.run {
                if (isFolder) justGone() else justVisible()
            }
            sizeDesc.run {
                if (isFolder) justGone() else justVisible()
            }
        }
        withMain {
            radioFolder.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    launchMain {
                        radioFolder.isChecked = true
                        radioFile.isChecked = false
                        sizeAsc.justGone()
                        sizeDesc.justGone()
                        recolorViewWindow(
                            up = orderFolderBy(),
                            tarColor = tarColor,
                            wholeColor = wholeColor
                        )
                        ctx.updatePrefBoolean(
                            MAN_WHICH, true
                        )
                    }
                }
            }
            radioFile.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    launchMain {
                        radioFile.isChecked = true
                        radioFolder.isChecked = false
                        sizeAsc.justVisible()
                        sizeDesc.justVisible()
                        recolorViewWindow(
                            up = orderFileBy(),
                            tarColor = tarColor,
                            wholeColor = wholeColor
                        )
                        ctx.updatePrefBoolean(
                            MAN_WHICH, false
                        )
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.databinding.PopWindowResortBinding.recolorViewWindow(
        up: String,
        tarColor: Int,
        wholeColor: Int,
    ) {
        when (up) {
            com.pt.common.BuildConfig.BY_NAME_ASC -> {
                nameAsc.setTextColor(tarColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_NAME_DESC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(tarColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_DATE_ASC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(tarColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_DATE_DESC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(tarColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_DATE_FOLDER_ASC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(tarColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_DATE_FOLDER_DESC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(tarColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_SIZE_ASC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(tarColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_SIZE_DESC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(tarColor)
            }
        }
    }


    @com.pt.common.global.UiAnn
    private fun com.pt.pro.databinding.PopWindowResortBinding.popClickList(
        pop: android.widget.PopupWindow?,
        tarColor: Int,
        wholeColor: Int,
    ) = android.view.View.OnClickListener {
        when (it.id) {
            com.pt.pro.R.id.nameAsc -> com.pt.common.BuildConfig.BY_NAME_ASC
            com.pt.pro.R.id.nameDesc -> com.pt.common.BuildConfig.BY_NAME_DESC
            com.pt.pro.R.id.dateAsc -> com.pt.common.BuildConfig.BY_DATE_ASC
            com.pt.pro.R.id.dateDesc -> com.pt.common.BuildConfig.BY_DATE_DESC
            com.pt.pro.R.id.sizeAsc -> com.pt.common.BuildConfig.BY_SIZE_ASC
            com.pt.pro.R.id.sizeDesc -> com.pt.common.BuildConfig.BY_SIZE_DESC
            else -> com.pt.common.BuildConfig.BY_NAME_ASC
        }.let { up ->
            launchMain {
                recolorViewWindow(up = up, tarColor = tarColor, wholeColor = wholeColor)
                if (isNowFolder()) {
                    if (!up.contains(orderFolderBy().toRegex())) {
                        ctx.updatePrefString(FO_B_SORT, up)
                        onResort()
                    }
                } else {
                    if (!up.contains(orderFileBy().toRegex())) {
                        ctx.updatePrefString(FI_SORT_B, up)
                        onResort()
                    }
                }
            }
        }
        if (pop?.isShowing == true) pop.dismiss() else return@OnClickListener
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        favAda?.onAdapterDestroy()
        folderAda?.onAdapterDestroy()
        searchJob?.cancelJob()
        searchActive = false
        firstSearch = false
        isTouch = false
        details = false
        stillOpenOption = false
        hiddenActive = false
        category = CATO_IMAGE
        catchy(Unit) {
            binder?.recyclerFiles?.adapter = null
            binder?.recyclerFavorites?.adapter = null
            binder?.recyclerFiles?.removeAllViewsInLayout()
            binder?.recyclerFavorites?.removeAllViewsInLayout()
            binder?.root_?.removeAllViewsInLayout()
        }
        folderAda = null
        favAda = null
        stubDel = null
        resortPop = null
        tHNative = null
        //resultAccess = null
        searchJob = null
        folderMana = null
        super.onDestroyView()
    }

}