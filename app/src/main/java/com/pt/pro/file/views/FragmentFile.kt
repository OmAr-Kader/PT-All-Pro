package com.pt.pro.file.views

import androidx.core.widget.doAfterTextChanged
import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.file.fasten.FragmentFileFasten
import com.pt.pro.gallery.fasten.StubDeleteFasten
import com.pt.pro.gallery.objects.MyHash
import com.pt.pro.notepad.objects.unzip
import kotlinx.coroutines.cancelChildren

class FragmentFile : ParentFragmentFile() {

    override fun FragmentFileFasten.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentFile)
        pushJob {
            launchDef {
                context.nullChecker()
                val allSvg = context.fetchAllSvg()
                initFavorite(isCato(), allSvg)
                context.nullChecker()
                loadFavorite(isCato(), false, load = false)
                context.nullChecker()
                context?.initRecycler(allSvg)
                context.nullChecker()
                updateRecycler(com.pt.common.BuildConfig.ROOT, anim = false, pos = 0)
                justCoroutine {
                    continueInti()
                }
            }
        }
    }

    private fun continueInti() {
        pushJob {
            launchDef {
                context.nullChecker()
                context?.dbVirFiles()
                context.nullChecker()
                intiFirstResume()
                context.nullChecker()
                intiViews()
                context.nullChecker()
                checkForSwipeVis()
            }
        }
    }

    private fun intiDimensions(
        widthDim: Int,
        heightDim: Int,
        rootWidth: Int,
        rootHeight: Int
    ) {
        (widthDim - rootWidth).let { mW ->
            (heightDim - rootHeight).let { mH ->
                if (mW >= 0 && mH >= 0) {
                    marginWidth = mW
                    marginHeight = mH
                }
                isDoInLand = rec.configuration.isLandTrad
                context?.updateDim(rootHeight.toFloat())
            }
        }
    }

    private suspend fun intiViews() {
        withMain {
            iBindingSus {
                mainBack.setOnClickListener(this@FragmentFile)
                pointerScroll.justInvisible()
                pointerScroll.justVisible()
                pointerScroll.addSenseListener(false) { _, event, type ->
                    when (type) {
                        DOWN_SEN -> {
                            isTouch = true
                        }
                        UP_SEN -> {
                            isTouch = false
                        }
                        MOVE_SEN -> {
                            if (allMedia.isEmpty()) return@addSenseListener
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
                                else -> allMedia.size * (kotlin.math.abs(endScroll) / endRatio)
                            }
                            binder?.apply {
                                pointerScroll.y = endScroll
                                recyclerFiles.scrollToPosition(ratioPlus.toInt())
                            }
                        }
                    }
                }
                recyclerFiles.scrollListener {
                    funScrollChange()
                }
                hiddenTittleClick.setOnClickListener(this@FragmentFile)
                includeOptions.pendingFrame.applySus {
                    setOnClickListener(this@FragmentFile)
                    setOnLongClickListener(this@FragmentFile)
                }
                includeOptions.hidden.applySus {
                    setOnClickListener(this@FragmentFile)
                    setOnLongClickListener(this@FragmentFile)
                }
                includeOptions.reSort.applySus {
                    setOnClickListener(this@FragmentFile)
                    setOnLongClickListener(this@FragmentFile)
                }
                clipboardFrame.applySus {
                    setOnClickListener(this@FragmentFile)
                    setOnLongClickListener(this@FragmentFile)
                }
                includeOptions.createFolder.applySus {
                    setOnClickListener(this@FragmentFile)
                    setOnLongClickListener(this@FragmentFile)
                }
                includeOptions.searchFile.applySus {
                    setOnClickListener(this@FragmentFile)
                    setOnLongClickListener(this@FragmentFile)
                }
                extendFile.applySus {
                    setOnClickListener(this@FragmentFile)
                    setOnLongClickListener(this@FragmentFile)
                }
                swipeMode.applySus {
                    setOnClickListener(this@FragmentFile)
                    setOnLongClickListener(this@FragmentFile)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun FileUpdateSack.doRefresh() {
        justCoroutineMain {
            if (folderAdapter.deleteActive) deleteRefresh(hiddenActive)
        }
        beforeUpdateRecycler()
        when (fileType) {
            PENDING_FILE -> checkForRelaunchPending()
            CATO_FILE -> onCatoItemCLickSus(pathScroll.toInt(), setPath = false, refresh = true)
            VIR_FILE -> updateForVirtualFolder()
            else -> updateForRealFile()
        }
        binder?.refresh()
        folderAdapter.offDisplayMenu(hiddenActive)
        checkIfAnyFileDeleted()
    }

    private suspend fun checkIfAnyFileDeleted() {
        if (!isCato()) {
            context?.dbFavVir()?.letSusBack { fileFav ->
                context.nullChecker()
                if (fileFav.size != favAdapter.favList.size) {
                    fileFav.displayCheck()
                }
            }
            context.nullChecker()
            withBack {
                context?.dbVirFiles()
            }
        }
    }

    private suspend fun MutableList<FileSack>.displayCheck() {
        withMain {
            binder?.recyclerFavorites?.clearRecyclerPool()
            toMutableList().asSequence().resortForFavorite().alsoSus { new ->
                favAdapter.applySus {
                    new.updateSearch(isCato())
                }
                //new.updateAllMedia()
            }
        }
    }

    private suspend fun FileUpdateSack.updateForRealFile() {
        loadForRefresh {
            context.nullChecker()
            if (fileCLick.lastOrNull() == this@updateForRealFile &&
                this@loadForRefresh.size != folderAdapter.fileList.size
            ) {
                updateRecycler(pathScroll, anim = false, pos = 0)
            } else {
                afterUpdateRecycler()
            }
        }
    }

    private suspend fun FileUpdateSack.updateForVirtualFolder() {
        ctx.newDBFile {
            getAllVFavFiles(pathScroll)
        }.letSusBack {
            context.nullChecker()
            if (fileCLick.last() == this@updateForVirtualFolder &&
                it.size != folderAdapter.fileList.size
            ) {
                it.displayForVirtual(false)
                it.updateAllMedia()
            } else {
                afterUpdateRecycler()
            }
        }
    }

    override fun onResort() {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                fileCLick.last().doReloadFile()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun funScrollChange() {
        catchy(Unit) {
            folderAdapter.adPosition.let { position ->
                if (position != 0 && !isTouch) {
                    if (allMedia.size != 0) {
                        iBinding {
                            if ((allMedia.size - position) > 3) {
                                position - 3
                            } else {
                                position
                            }.let { pos ->
                                pos.toFloat() / allMedia.size.toFloat()
                            }.let {
                                if (it > 0) it * endRatio else 0F
                            }.let {
                                pointerScroll.y = it
                            }
                        }
                    }
                }
            }

        }
    }

    @com.pt.common.global.UiAnn
    override fun FragmentFileFasten.onClick(v: android.view.View) {
        pushJob {
            launchImdMain {
                doOnClick(v)
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun FragmentFileFasten.doOnClick(v: android.view.View) {
        justCoroutineMain {
            refresh()
            checkSearchVis()
            folderAdapter.offDisplayMenu(hiddenActive)
        }
        justCoroutineMain {
            when (v) {
                mainBack -> act.onBackPressedDispatcher.onBackPressed()
                includeOptions.createFolder -> mutableListOf<FileSack>().createDialog(false)
                includeOptions.pendingFrame -> launchPending(false)
                clipboardFrame -> clipBoardFiles.applySusClipboard()
                includeOptions.searchFile -> intiSearchViews()
                includeOptions.reSort -> includeOptions.reSort.popWindow()
                extendFile -> intiExtraOptions()
                swipeMode -> initSwitchMode(isCato())
                hiddenTittleClick -> intiHiddenTitle(true)
                includeOptions.hidden -> intiHiddenTitle(!hiddenActive)
            }
        }
    }

    private suspend fun FragmentFileFasten.intiExtraOptions() {
        includeOptions.root_.applySus {
            if (isVis) {
                goneTop(200)
            } else {
                visibleTop(200)
            }
        }
    }

    private suspend fun FragmentFileFasten.intiHiddenTitle(
        sendNewHidden: Boolean,
    ) {
        if (!FileLate(fileCLick.lastOrNull()?.pathScroll ?: return).isDirectory) {
            context?.makeToastRecSus(R.string.nr, 1)
            return
        }
        hiddenActive = sendNewHidden
        if (sendNewHidden) {
            doHiddenActivated(true)
        } else {
            doHiddenDisable()
        }
        updateRecycler(fileCLick.lastOrNull()?.pathScroll ?: return, anim = false, pos = 0)
    }

    @com.pt.common.global.UiAnn
    private suspend fun FragmentFileFasten.intiSearchViews() {
        justScope {
            searchListNative = folderAdapter.fileList.toMutableList().asSequence()
        }
        withMain {
            picName.justGone()
            searchEdit.applySus {
                justVisible()
                requestFocus()
                context?.launchKeyBoard {
                    showSoftInput(this@applySus, 1)
                }
                doAfterTextChanged { ed ->
                    doSearchJob(ed)
                }
            }
            searchActive = true
            includeOptions.root_.goneTop(200)
        }
    }

    private fun doSearchJob(text: android.text.Editable?) {
        searchJob?.cancelJob()
        searchJob = launchDef {
            if (!text.isNullOrEmpty() && text.isNotBlank()) {
                text.doSearch()
            } else {
                withMain {
                    if (binder?.searchEdit?.isVis == true) {
                        emptySearch()
                    }
                }
            }
        }
    }

    private suspend fun initSwitchMode(isCat: Boolean) {
        context?.updatePrefBoolean(FILE_MODE, !isCat)
        loadFavorite(!isCat, anim = true, load = true)
        /*if (!isCat) {
            loadCatoFiles()
        } else {
            context?.dbFavVir().resortForFavorite()
        }.letSusBack {
            context.nullChecker()
            it.displayNewFavorites(isCat)
        }*/
    }

    private suspend fun MutableList<FileSack>.displayNewFavorites(isCat: Boolean) {
        if (this@displayNewFavorites.isNotEmpty()) {
            context?.updatePrefBoolean(FILE_MODE, !isCat)
            withMain {
                binder?.recyclerFiles?.clearRecyclerPool()
                favAdapter.applySus {
                    this@displayNewFavorites.updateSearch(!isCat)
                }
                binder?.recyclerFavorites?.scheduleLayoutAnimation()
            }
        }
    }

    private suspend fun FragmentFileFasten.doHiddenActivated(showSnake: Boolean) {
        withMain {
            context?.apply {
                act.window.applySus {
                    myBaseActivity(false)
                    statusBarColor = fetchColor(R.color.hbd)
                }
                compactImage(R.drawable.ic_hidden_show) {
                    includeOptions.hidden.setImageDrawable(this@compactImage)
                }
                mainBack.svgReColorWhite()
                extendFile.svgReColorWhite()
                swipeMode.svgReColorWhite()
                clipboardImage.svgReColorWhite()
                picName.setTextColor(fetchColor(R.color.whi))
                searchEdit.setTextColor(fetchColor(R.color.whi))
                galleryCard.backReColor(fetchColor(R.color.hbd))
            }
            if (showSnake) {
                forSnakes.snakeBar(forSnakes.childCountSus())
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun FragmentFileFasten.doHiddenDisable() {
        justCoroutineMain {
            act.window.applySus {
                statusBarColor = them.findAttr(android.R.attr.colorPrimary)
                myBaseActivity(
                    them.findAttr(
                        android.R.attr.textColorPrimary
                    ) == android.graphics.Color.BLACK
                )
            }
            context?.compactImage(R.drawable.ic_hidden) {
                includeOptions.hidden.setImageDrawable(this@compactImage)
            }
            them.findAttr(android.R.attr.textColorPrimary).let {
                mainBack.svgReColor(it)
                extendFile.svgReColor(it)
                swipeMode.svgReColor(it)
                clipboardImage.svgReColor(it)
                picName.setTextColor(it)
                searchEdit.setTextColor(it)
            }
            galleryCard.backReColor(them.findAttr(android.R.attr.colorPrimary))
        }
    }

    override suspend fun updateRecycler(newFile: String, anim: Boolean, pos: Int) {
        /*withMain {
            binder?.recyclerFiles?.tag = FileLate(newFile).name
        }*/
        beforeUpdateRecycler()
        context.nullChecker()
        fetchTargetFiles(newFile) {
            context.nullChecker()
            this@fetchTargetFiles.displayUpdateRecycler(pos = pos, anim = anim)
            this@fetchTargetFiles.updateAllMedia()
            context.nullChecker()
            fileCount(newFile) { sub ->
                launchImdMain {
                    context.nullChecker()
                    this@fetchTargetFiles.afterUpdateRecycler(sub)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun MutableList<FileSack>.displayUpdateRecycler(
        pos: Int,
        anim: Boolean
    ) {
        withMain {
            iBindingSus {
                //if (recyclerFiles.tag == FileLate(newFile).name) {
                displayForVirtual(anim = anim)
                folderMana?.scrollToPositionWithOffset(pos, 0)
                //}
            }
        }
    }

    private suspend fun MutableList<FileSack>.afterUpdateRecycler(sub: Int) {
        withMain {
            binder?.hiddenTittleLinear?.applySus {
                if (this@afterUpdateRecycler.isEmpty() && sub != 0)
                    visibleFadeSus(200L)
                else
                    justGoneSus()
            }
        }
    }


    override suspend fun FileUpdateSack.doReloadFile() {
        when (fileType) {
            PENDING_FILE -> launchPending(true)
            VIR_FILE -> loadVirtual(pathScroll)
            CATO_FILE -> {
                onCatoItemCLickSus(pathScroll.toInt(), setPath = false, refresh = false)
            }
            else -> updateRecycler(pathScroll, anim = true, pos = pos)
        }
        if (fileType != CATO_FILE) binder?.includeOptions?.hidden?.justVisible()
        doRecover()
    }


    override suspend fun reloadFavorites() {
        isCato().letSusBack { cato ->
            context?.dbFavVir()?.toMutableList()?.asSequence()?.resortForFavorite().letSusBack {
                if (it != null && it.isNotEmpty()) {
                    swipeVis(true)
                } else {
                    swipeVis(false)
                    context?.updatePrefBoolean(FILE_MODE, true)
                    if (!cato) context?.loadCatoFiles()
                        ?.displayLoadFavorite(anim = false, load = true)
                }
            }
        }
    }

    private suspend fun loadFavorite(isCat: Boolean, anim: Boolean, load: Boolean) {
        if (isCat) {
            context?.loadCatoFiles()
        } else {
            context?.dbFavVir()?.toMutableList()?.asSequence()?.resortForFavorite().letSusBack {
                if (it != null && it.isNotEmpty()) {
                    swipeVis(true)
                    it
                } else {
                    swipeVis(false)
                    context?.updatePrefBoolean(FILE_MODE, true)
                    context?.loadCatoFiles()
                }
            }
        }?.letSus {
            context.nullChecker()
            it.displayLoadFavorite(anim, load)
        }
    }

    private suspend fun swipeVis(vis: Boolean) {
        withMain {
            binder?.swipeMode?.applySus {
                if (vis) justVisibleSus() else justInvisibleSus()
            }
        }
    }

    private suspend fun MutableList<FileSack>.displayLoadFavorite(anim: Boolean, load: Boolean) {
        withMain {
            favAdapter.applySus {
                if (this@displayLoadFavorite != favList) {
                    binder?.recyclerFavorites?.clearRecyclerPool()
                    this@displayLoadFavorite.updateSearch(isCato())
                    if (anim) binder?.recyclerFavorites?.scheduleLayoutAnimation()
                }
            }
        }
        if (load) {
            context?.dbVirFiles()
            checkForSwipeVis()
        }
    }

    private suspend fun checkForSwipeVis() {
        swipeVis(favoritesFiles.isNotEmpty() || virtualFiles.isNotEmpty())
    }

    override fun onCatoItemCLick(cato: Int, setPath: Boolean) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                withBack {
                    fileCLick.indexOfLast {
                        if (it.fileType == CATO_FILE)
                            it.pathScroll.toInt() == cato
                        else
                            rec.findTextCato(cato) == it.pathScroll
                    }.let<Int, Unit> {
                        if (it == -1) {
                            onCatoItemCLickSus(cato, setPath, false)
                        } else {
                            kotlin.runCatching {
                                ifAlreadyOpened(it)
                            }.onFailure {
                                onCatoItemCLickSus(cato, setPath, false)
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun onCatoItemCLickSus(cato: Int, setPath: Boolean, refresh: Boolean) {
        beforeCatoItem(cato)
        loadAllCato(cato) {
            context.nullChecker()
            @Suppress("KotlinConstantConditions")
            if (!refresh || (size != folderAdapter.fileList.size && refresh)) {
                this@loadAllCato.displayForCato(cato = cato, setPath = setPath)
            } else {
                afterUpdateRecycler()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun MutableList<FileSack>.displayForCato(
        cato: Int,
        setPath: Boolean,
    ) {
        withMain {
            displayForVirtual(anim = true)
            binder?.recyclerFiles?.scrollToPosition(0)
        }
        withMain {
            if (setPath) {
                FileUpdateSack(
                    cato.toStr,
                    CATO_FILE,
                ).addScrollPath(rec.findTextCato(cato))
            }
            binder?.includeOptions?.hidden?.justGoneSus()
        }
    }

    private suspend fun beforeCatoItem(cato: Int) {
        withMain {
            beforeUpdateRecycler()
            category = cato
        }
    }

    override suspend fun MutableList<FileSack>.displayForVirtual(anim: Boolean) {
        afterUpdateRecycler()
        binder?.recyclerFiles?.clearRecyclerPool()
        folderAdapter.applySus {
            this@displayForVirtual.updateSearch()
        }
        binder?.checkSearchVis()
        if (anim) doScAnim()
        updateAllMedia()
    }

    @com.pt.common.global.UiAnn
    private suspend fun doScAnim() {
        withMain {
            binder?.recyclerFiles?.scheduleLayoutAnimation()
        }
    }

    override suspend fun MutableList<FileSack>.deleteMedia() {
        displayDeleteChecker()
        context.nullChecker()
        deleteChecker()
        context.nullChecker()
        justScope {
            displayForDelete()
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun displayDeleteChecker() {
        withMain {
            iBindingSus {
                fileBarCard.justVisible()
                refresh()
                recyclerFiles.clearRecyclerPool()
            }
            folderAdapter.applySus {
                mutableListOf<FileSack>().updateSearch()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun displayForDelete() {
        launchImdMain {
            withMain {
                afterUpdateRecycler()
                folderAdapter.applySus {
                    forDelete()
                    binder?.recyclerFiles?.clearRecyclerPool()
                    mutableListOf<FileSack>().applySus {
                        addAll(onlyFolder)
                        addAll(onlyMedia)
                    }.alsoSus {
                        it.updateSearch()
                        it.updateAllMedia()
                    }
                }
            }
            binder?.checkSearchVis()
            withMainNormal {
                with(
                    com.pt.pro.gallery.fasten.GalleryInflater.run { layoutInflater.context.inflaterDelete() }
                ) {
                    @com.pt.common.global.ViewAnn
                    stubDel = this
                    binder?.forSnakes?.addView(root_)
                    deleteFrame.visibleBottom(300L)
                    cancelDelete.setOnClickListener {
                        pushJob { j ->
                            launchDef {
                                j?.checkIfDone()
                                doCancelDelete()
                                fileCLick.lastOrNull()?.doReloadFile()
                            }
                        }
                    }
                    confirmDelete.setOnClickListener {
                        binder?.forSnakes?.removeView(this@with.root_)
                        applyDelete()
                    }
                }
                binder?.picName?.text = R.string.dq.dStr
                afterUpdateRecycler()
            }
        }
    }

    private suspend fun StubDeleteFasten.doCancelDelete() {
        withMain {
            binder?.forSnakes?.removeView(this@doCancelDelete.root_)
        }
        deleteRefresh(hiddenActive)
    }

    @com.pt.common.global.UiAnn
    override suspend fun displayAddToClipboard() {
        withMain {
            iBindingSus {
                refresh()
                folderAdapter.offDisplayMenu(hiddenActive)
                justCoroutineMain {
                    clipboardFrame.justVisible()
                    clipboardText.text = clipBoardFiles.size.toStr
                }
            }
        }
    }

    private suspend fun checkForRelaunchPending() {
        if (pendHash.isNotEmpty()) {
            pendHash.checkLaunchPending {
                if (folderAdapter.fileList.size != this@checkLaunchPending.size) {
                    hideHiddenTittle()
                    context.nullChecker()
                    displayForVirtual(false)
                    displayAfterPend()
                } else {
                    afterUpdateRecycler()
                }
            }
        } else {
            afterUpdateRecycler()
            context?.makeToastRecSus(R.string.pe, 0)
        }
    }

    override suspend fun launchPending(relaunch: Boolean) {
        if (pendHash.isNotEmpty()) {
            hideHiddenTittle()
            displayBeforePend(relaunch)
            pendHash.checkLaunchPending {
                context.nullChecker()
                displayForVirtual(false)
                displayAfterPend()
                updateAllMedia()
            }
        } else {
            context?.makeToastRecSus(R.string.pe, 0)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun displayAfterPend() {
        withMain {
            iBindingSus {
                if (includeOptions.hidden.isVis) includeOptions.hidden.justGone()
            }
        }
    }

    private suspend fun displayBeforePend(refresh: Boolean) {
        if (!refresh) {
            withMain {
                rec.getString(R.string.vp).letSus {
                    FileUpdateSack(it, PENDING_FILE).runSus {
                        addScrollPath(it)
                    }
                }
            }
        } else return
    }


    @com.pt.common.global.UiAnn
    override suspend fun displayPending(siz: Int) {
        withMain {
            binder?.includeOptions?.pendingText?.applySus {
                if (!isVis) justVisible()
                text = siz.toStr
            }
            binder?.extendFile?.applySus {
                svgReColor(them.findAttr(android.R.attr.colorAccent))
                toCatchSackAfter(4139, 400L) {
                    catchy(Unit) {
                        if (hiddenActive) svgReColorWhite()
                        else svgReColor(them.findAttr(android.R.attr.textColorPrimary))
                    }
                }.postAfter()
                startAnimation(shakeAnimation)
            }
        }
    }

    override fun MutableList<FileSack>.openVirtual() {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                createDialog(true)
            }
        }
    }

    override fun MutableList<FileSack>.createFolder(
        name: String,
        real: Boolean,
        isFromVir: Boolean,
    ) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                withBack {
                    if (!isFromVir && !FileLate(
                            fileCLick.lastOrNull()?.pathScroll ?: return@withBack
                        ).isDirectory
                    ) {
                        context?.makeToastRecSus(R.string.nt, 1)
                        return@withBack
                    }
                    if (cont.createNewFolder("${fileCLick.lastOrNull()?.pathScroll}/$name", name)) {
                        updateRecycler(
                            fileCLick.lastOrNull()?.pathScroll ?: return@withBack,
                            anim = false,
                            pos = 0
                        )
                    }
                    if (real && !FileLate(fileCLick.last().pathScroll).isDirectory) {
                        ctx.makeToastRecSus(R.string.nt, 1)
                        return@withBack
                    }
                    if (real) {
                        if (cont.createNewFolder("${fileCLick.last().pathScroll}/$name", name)) {
                            updateRecycler(fileCLick.last().pathScroll, anim = false, pos = 0)
                        }
                    } else {
                        ctx.newDBFile {
                            if (insertVirFile(name) == -1L) {
                                ctx.makeToastRecSus(R.string.gd, 0)
                            } else {
                                reloadFavorites()
                            }
                        }
                        if (isFromVir && this@createFolder.isNotEmpty()) {
                            this@createFolder.addToVir()
                        }
                    }
                }
            }
        }
    }

    private suspend fun MutableList<FileSack>.applySusClipboard() {
        launchDef {
            checkClipboard().alsoSusBack {
                if (it)
                    doApplyClipboard(fileCLick.lastOrNull()?.pathScroll ?: return@alsoSusBack) {
                        launchImdMain {
                            context.nullChecker()
                            binder?.displayAfterClipboard()
                        }
                    }
                else
                    return@alsoSusBack
            }
        }
    }

    private suspend fun checkClipboard(): Boolean = withMainDef(false) {
        return@withMainDef if (!FileLate(
                fileCLick.lastOrNull()?.pathScroll ?: return@withMainDef false
            ).isDirectory
        ) {
            context?.makeToastRecSus(R.string.nr, 1)
            false
        } else {
            withMain {
                binder?.fileBarCard?.justVisible()
            }
            true
        }
    }

    override suspend fun FragmentFileFasten.displayAfterClipboard() {
        withMain {
            afterUpdateRecycler()
            clipboardFrame.justInvisibleSus()
            updateRecycler(
                fileCLick.lastOrNull()?.pathScroll ?: return@withMain,
                anim = false,
                pos = 0
            )
            clipBoardFiles.clear()
        }
    }


    override suspend fun displayJustNotify() {
        withMain {
            folderAdapter.justNotify()
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun displayBeforeDelete() {
        withMain {
            binder?.fileBarCard?.justVisibleSus()
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun displayAfterDelete() {
        deleteRefresh(hiddenActive)
        afterUpdateRecycler()
        fileCLick.lastOrNull()?.doReloadFile()
    }

    private suspend fun FileSack.recreateMedia(fromFav: Boolean) {
        withBackDef(mutableListOf()) {
            return@withBackDef if (fromFav) {
                favAdapter.favList
            } else {
                folderAdapter.fileList
            }.runSusBack {
                toMutableList().asSequence().filter {
                    it.typeFile == IMAGE || it.typeFile == VIDEO
                }.toMutableList()
            }
        }.launchMedia(this@recreateMedia)
    }

    private suspend fun initFavorite(
        isCato: Boolean,
        allSvg: androidx.collection.ArrayMap<Int, android.graphics.drawable.Drawable>,
    ) {
        withMain {
            isRight = rec.isRightToLeft
            binder?.recyclerFavorites?.applySus {
                context?.apply {
                    layoutAnimation = translateRec(dur = 200L, del = 0.1F)//R.anim.anim_favorite,
                    layoutManager = if (rec.isLandTraditional) getVerManager else getManager
                }
                FavoriteAdapter(
                    isCato,
                    this@FragmentFile,
                    allSvg
                ).applySus {
                    favAda = this
                    adapter = this
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun MutableList<FileSack>.displayReloadFavorites() {
        withMain {
            if (this@displayReloadFavorites.isNotEmpty()) {
                iBindingSus {
                    if (!swipeMode.isVis) {
                        swipeMode.justVisibleSus()
                        updateSwipe()
                    }
                    recyclerFiles.clearRecyclerPool()
                    favAdapter.applySus {
                        this@displayReloadFavorites.updateSearch(false)
                    }
                }
            } else {
                context?.loadCatoFiles()?.displayNewFavorites(true)
            }
        }
    }

    private suspend fun updateSwipe() {
        withBack {
            context?.updatePrefBoolean(FILE_MODE, false)
        }
    }

    private suspend fun com.pt.pro.file.fasten.ListOptionFasten.checkVir(f: Sequence<FileSack>) {
        f.doCheckVir {
            context.nullChecker()
            displayCheckVir(this@doCheckVir.isEmpty())
        }
    }

    private suspend fun com.pt.pro.file.fasten.ListOptionFasten.checkFav(f: Sequence<FileSack>) {
        f.filterOnlyFav(favoritesFiles).letSusBack { fav ->
            context.nullChecker()
            doCheckFav(fav.isEmpty())
        }
        context.nullChecker()
        f.filterNotFav(favoritesFiles).letSusBack { unFav ->
            context.nullChecker()
            afterCheckFav(unFav.isEmpty())
        }
    }

    private suspend fun android.content.Context.initRecycler(
        allSvg: androidx.collection.ArrayMap<Int, android.graphics.drawable.Drawable>,
    ) {
        withMain {
            iBindingSus {
                recyclerFiles.applySus {
                    findRecAnim {
                        ((it + 15) % 15).toFloat().let { del ->
                            ((it.toFloat() / 20) * 400).toLong().let { dur ->
                                layoutAnimation = fallDownAnimationRec(
                                    dur = dur,
                                    del = (del / 150),
                                )
                            }
                        }
                    }
                    getVerManager.also {
                        folderMana = it
                        layoutManager = it
                    }
                    act.windowManager?.fetchDimensionsSus {
                        FolderAdapter(
                            mutableListOf(),
                            this@FragmentFile,
                            allSvg,
                        ).alsoSus {
                            folderAda = it
                            adapter = it
                        }
                        root_.fetchViewDim { rootWidth, rootHeight ->
                            intiDimensions(
                                widthDim = this@fetchDimensionsSus.width,
                                heightDim = this@fetchDimensionsSus.height,
                                rootWidth = rootWidth,
                                rootHeight = rootHeight
                            )
                        }
                    }
                }
            }
        }
        FileUpdateSack(
            com.pt.common.BuildConfig.ROOT,
            NORMAL_FILE,
        ).addScrollPath(ROOT_NAME)
    }

    @com.pt.common.global.UiAnn
    override suspend fun myShareTwo(
        fileSack: FileSack,
        actionType: String,
    ) {
        withMain {
            binder?.includeShare?.linearScroll?.apply linearScroll@{
                com.pt.pro.file.fasten.FileInflater.run { ctx.inflaterOpenAsOption() }.apply {
                    backOpt.setOnClickListener {
                        doRecover()
                    }
                    textAs.setOnClickListener {
                        DSack(
                            fileSack,
                            com.pt.common.BuildConfig.TEXT_SENDER,
                            actionType,
                        ).dpOpenAs()
                    }
                    imageAs.setOnClickListener {
                        DSack(
                            fileSack,
                            com.pt.common.BuildConfig.IMAGE_SENDER,
                            actionType,
                        ).dpOpenAs()
                    }
                    audioAs.setOnClickListener {
                        DSack(
                            fileSack,
                            com.pt.common.BuildConfig.AUDIO_SENDER,
                            actionType,
                        ).dpOpenAs()
                    }
                    videoAs.setOnClickListener {
                        DSack(
                            fileSack,
                            com.pt.common.BuildConfig.VIDEO_SENDER,
                            actionType,
                        ).dpOpenAs()
                    }
                    allAs.setOnClickListener {
                        DSack(
                            fileSack,
                            com.pt.common.BuildConfig.ALL_SENDER,
                            actionType,
                        ).dpOpenAs()
                    }
                }.root_.alsoSus(this@linearScroll::addView)
                this@linearScroll.visibleFadeSus(300)
            }
        }
    }


    private fun DSack<FileSack, String, String>.dpOpenAs() {
        launchDef {
            if (three == android.content.Intent.ACTION_VIEW) {
                viewFiles30(one.fileUri.toUri, two) {
                    runCatching<Unit> {
                        this@FragmentFile.startActivity(this)
                    }.onFailure {
                        ctx.makeToastRecSus(R.string.px, 0)
                    }
                }
            } else {
                context?.shareFiles30(mutableListOf(one), two) {
                    runCatching<Unit> {
                        this@FragmentFile.startActivity(this@shareFiles30)
                    }.onFailure {
                        ctx.makeToastRecSus(R.string.px, 0)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun androidx.appcompat.widget.LinearLayoutCompat.displayOptions(isArc: Boolean) {
        withMain {
            com.pt.pro.file.fasten.FileInflater.run { ctx.inflaterListOption(isArc) }.applySus ListOption@{
                listOptionFasten = this@ListOption
                backOpt.setOnClickListener(shareOptionClickListener)
                favorite?.setOnClickListener(shareOptionClickListener)
                unFavorite?.setOnClickListener(shareOptionClickListener)
                addVir?.setOnClickListener(shareOptionClickListener)
                removeVir?.setOnClickListener(shareOptionClickListener)
                openWith.setOnClickListener(shareOptionClickListener)
                openAs.setOnClickListener(shareOptionClickListener)
                shareOpt.setOnClickListener(shareOptionClickListener)
                addPend.setOnClickListener(shareOptionClickListener)
                deleteOpt?.setOnClickListener(shareOptionClickListener)
                properties?.setOnClickListener(shareOptionClickListener)
                selectAll.setOnClickListener(shareOptionClickListener)
                renameOpt?.setOnClickListener(shareOptionClickListener)
                hide?.setOnClickListener(shareOptionClickListener)
                copy.setOnClickListener(shareOptionClickListener)
                move?.setOnClickListener(shareOptionClickListener)
            }.root_.alsoSus(this@displayOptions::addView)
            stillOpenOption = !isArc
            this@displayOptions.visibleFadeSus(300)
        }
        listOptionFasten?.checkNonFolder()
        justCoroutineMain {
            if (!isArc) {
                listOptionFasten?.checkMyOption()
            }
        }
    }

    private suspend fun com.pt.pro.file.fasten.ListOptionFasten.checkMyOption() {
        withBack {
            folderAdapter.deleteCopy.toMutableList().asSequence().applySusBack {
                checkFav(this@applySusBack)
                checkVir(this@applySusBack)
            }
        }
    }

    override fun MutableList<FileSack>.removeFromVir(itF: String) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                binding.fileBarCard.justVisibleSus()
                context.nullChecker()
                doRemoveFromVir()
                context.nullChecker()
                afterUpdateRecycler()
                doBackOption()
                context.nullChecker()
                reloadFavorites()
            }.also {
                jobNativeFile = it
            }
        }
    }

    override fun MutableList<FileSack>.addForVir(itF: String) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                binding.fileBarCard.justVisibleSus()
                context.nullChecker()
                doAddForVir(itF)
                context.nullChecker()
                afterUpdateRecycler()
                reloadFavorites()
            }.also {
                jobNativeFile = it
            }
        }
    }

    private suspend fun saveScrollPos(position: Int) {
        withMain {
            fileCLick.runCatching {
                lastOrNull()?.pos = folderMana?.findFirstCompletelyVisibleItemPosition() ?: position
            }
        }
    }

    override fun FileSack.onRealItemCLick(position: Int, fromFav: Boolean) {
        pushJob { j ->
            launchDef {
                j.checkIfDone()
                saveScrollPos(position)
                withBack {
                    when (typeFile) {
                        FOLDER -> {
                            fileCLick.indexOfLast {
                                this@onRealItemCLick.filePath == it.pathScroll
                            }.let<Int, Unit> {
                                if (it == -1) {
                                    checkIfHaveHidden(filePath)
                                    updateRecycler(filePath, anim = true, pos = 0)
                                } else {
                                    kotlin.runCatching {
                                        ifAlreadyOpened(it)
                                    }.onFailure {
                                        checkIfHaveHidden(filePath)
                                        updateRecycler(filePath, anim = true, pos = 0)
                                    }
                                }
                            }
                        }
                        IMAGE -> {
                            if (isPicture()) {
                                recreateMedia(fromFav)
                            } else {
                                launchDef {
                                    wholeFiles()
                                }
                            }
                        }
                        VIDEO -> {
                            launchDef {
                                if (isVideo()) {
                                    recreateMedia(fromFav)
                                } else {
                                    wholeFiles()
                                }
                            }
                        }
                        AUDIO -> {
                            if (isAudio()) {
                                if (fromFav) {
                                    favAdapter.favList
                                } else {
                                    folderAdapter.fileList
                                }.launchMusicPopUp(this@onRealItemCLick)
                            } else {
                                launchDef {
                                    wholeFiles()
                                }
                            }
                        }
                        PDF -> {
                            launchDef {
                                if (isPdfViewer()) {
                                    launchPdfViewer(filePath)
                                } else {
                                    wholeFiles()
                                }
                            }
                        }
                        TXT -> {
                            launchDef {
                                if (isTextViewer() && catchySus(false) { FileLate(filePath).length() < 500000L }) {
                                    launchTextViewer(filePath)
                                } else {
                                    wholeFiles()
                                }
                            }
                        }
                        ZIP -> {
                            launchDef {
                                if (isZipViewer()) {
                                    forZipFiles()
                                } else {
                                    wholeFiles()
                                }
                            }
                        }
                        UNKNOWN -> {
                            shareUnknownFile(
                                context?.getFileUri(
                                    filePath,
                                    typeFile,
                                    com.pt.pro.BuildConfig.APPLICATION_ID
                                ) ?: return@withBack
                            )
                        }
                        else -> {
                            wholeFiles()
                        }
                    }
                }
            }
        }
    }

    override suspend fun FileSack.forZipFiles() {
        beforeUpdateRecycler()
        withBackDef(Unit) {
            ctx.getOwnFile(com.pt.common.BuildConfig.ZIP_FILE).fileCreator().letSusBack { parent ->
                kotlin.runCatching {
                    FileLate(parent, fileName).fileCreator().letSusBack { temp ->
                        justScope {
                            if (parent.listFiles()?.isNotEmpty() == true) {
                                temp.deleteRecursiveChildren()
                            } else return@justScope
                        }
                        FileLate(filePath).unzip(temp)
                        justCoroutineBack {
                            if (temp.listAllFiles().isNotEmpty()) {
                                hiddenActive = true
                                binder?.doHiddenActivated(false)
                                checkIfHaveHidden(temp.absolutePath)
                                updateRecycler(temp.absolutePath, false, 0)
                            } else {
                                ctx.makeToastRecSus(R.string.xe, 0)
                                afterUpdateRecycler()
                            }
                        }
                    }
                }.getOrElse {
                    it.listThrowable()
                    ctx.makeToastRecSus(R.string.xe, 0)
                    afterUpdateRecycler()
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun onVirtualItemCLick(name: String) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                withBack {
                    fileCLick.indexOfLast {
                        name == it.pathScroll
                    }.let<Int, Unit> {
                        if (it == -1) {
                            beforeUpdateRecycler()
                            loadVirtual(name)
                            FileUpdateSack(name, VIR_FILE).addScrollPath(name)
                            afterUpdateRecycler()
                        } else {
                            kotlin.runCatching {
                                ifAlreadyOpened(it)
                            }.onFailure {
                                loadVirtual(name)
                                FileUpdateSack(name, VIR_FILE).addScrollPath(name)
                            }
                        }
                    }
                }
                checkSwipe()
            }
        }
    }

    private suspend fun checkSwipe() {
        withMain {
            if (binding.includeOptions.hidden.isVis) binding.includeOptions.hidden.justGone()
        }
    }

    @com.pt.common.global.UiAnn
    override fun onActionDisplay(showCard: Boolean, text: String?) {
        if (text != null) {
            binder?.picName?.text = text
        } else {
            doRecover()
            return
        }
        if (showCard) {
            pushJob { j ->
                launchDef {
                    j?.checkIfDone()
                    justShowCard(
                        fileCLick.lastOrNull()?.pathScroll?.contains(ctx.getOwnFile(com.pt.common.BuildConfig.ZIP_FILE).absolutePath) == true
                    )
                }
            }
        }
        if (stillOpenOption) {
            pushJob { j ->
                launchDef {
                    j?.checkIfDone()
                    listOptionFasten?.checkNonFolder()
                    listOptionFasten?.checkActionDisplay()
                }
            }
        }
    }

    private suspend fun justShowCard(isArc: Boolean) {
        withMain {
            binder?.refresh()
            binder?.includeShare?.linearScroll?.displayOptions(isArc)
        }
    }

    private suspend fun com.pt.pro.file.fasten.ListOptionFasten.checkActionDisplay() {
        folderAdapter.deleteCopy.toMutableList().asSequence().applySusBack {
            checkFav(this@applySusBack)
            checkVir(this@applySusBack)
        }
    }

    private suspend fun com.pt.pro.file.fasten.ListOptionFasten.checkNonFolder() {
        folderAdapter.deleteCopy.doCheckNonFolder().letSusBack {
            displayCheckNonFolder(it)
        }
    }


    private suspend fun CharSequence.doSearch() {
        if (!firstSearch) firstSearch = true
        this@doSearch.doSearchOnList {
            context.nullChecker()
            afterUpdateRecycler()
            this@doSearchOnList.displaySearch()
            context.nullChecker()
            this@doSearchOnList.updateAllMedia()
        }
    }

    private suspend fun MutableList<FileSack>.displaySearch() {
        binder?.recyclerFiles?.clearRecyclerPool()
        folderAdapter.applySus {
            this@displaySearch.updateSearch()
        }
    }

    private suspend fun emptySearch() {
        withBack {
            if (firstSearch) {
                doSearchBack()
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun doBackDelete() {
        context.nullChecker()
        justCoroutineMain {
            deleteRefresh(hiddenActive)
            folderAdapter.offDisplayMenu(hiddenActive)
            fileCLick.lastOrNull()?.doReloadFile()
        }
    }

    override suspend fun doBackOption() {
        context.nullChecker()
        withMain {
            iBindingSus {
                refresh()
                folderAdapter.offDisplayMenu(hiddenActive)
                checkSearchVis()
            }
        }
        withMain {
            if (haveNewUpdate) {
                askForRefresh()
                haveNewUpdate = false
            } else return@withMain
        }
    }

    override suspend fun doSearchBack() {
        context.nullChecker()
        justCoroutineMain {
            searchActive = false
            binder?.checkSearchVis()
            fileCLick.lastOrNull()?.doReloadFile()
        }
    }

    private suspend fun android.content.Context.deleteZipFile() {
        justScope {
            androidx.work.OneTimeWorkRequest.Builder(
                com.pt.pro.file.objects.FileWorker::class.java
            ).build().applySusBack {
                com.pt.pro.main.odd.WorkInitializer().create(
                    this@deleteZipFile
                ).enqueueUniqueWork(
                    "FileWorker",
                    androidx.work.ExistingWorkPolicy.REPLACE,
                    this@applySusBack
                )
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun onBack() {
        onDestroyJobs()
        jobNativeFile?.cancelJob()
        coroutineContext.cancelChildren()
        pushJob {
            launchImdMain {
                runCatching {
                    //j?.checkIfDone()
                    withMainDef(7) {
                        if (childFragmentManager.backStackEntryCount != 0)
                            0
                        else if (folderAda?.deleteActive == true)
                            1
                        else if (binder?.includeShare?.linearScroll?.childCountSus?.invoke() != 0)
                            2
                        else if (searchActive)
                            3
                        else if ((fileCLickNative?.size ?: 0) > 1)
                            4
                        else if (MyHash.isLock)
                            5
                        else if (fileCLickNative?.size == 1)
                            7
                        else
                            -1
                    }.letSus {
                        context.nullChecker()
                        justCoroutineMain {
                            when (it) {
                                0 -> childFragmentManager.popBackStack()
                                1 -> doBackDelete()
                                2 -> doBackOption()
                                3 -> doSearchBack()
                                4 -> doBackNormal()
                                5 -> "LogLog".logProvLess()
                                6 -> doBackNormal()
                                else -> {
                                    justScope {
                                        context?.getOwnFile(
                                            com.pt.common.BuildConfig.ZIP_FILE
                                        )?.letSusBack {
                                            if (it.listAllFiles().isNotEmpty()) {
                                                context?.deleteZipFile()
                                            }
                                        } ?: return@justScope
                                    }
                                    justScope {
                                        if ((activity ?: return@justScope).intent.isPDFIntent || (activity ?: return@justScope).intent.isTxtIntent) {
                                            (activity ?: return@justScope).finish()
                                        } else {
                                            act.toFinish()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }.onFailure {
                    if ((activity ?: return@onFailure).intent.isPDFIntent || (activity ?: return@onFailure).intent.isTxtIntent) {
                        (activity ?: return@onFailure).finish()
                    } else {
                        act.toFinish()
                    }
                }
            }
        }
    }

}