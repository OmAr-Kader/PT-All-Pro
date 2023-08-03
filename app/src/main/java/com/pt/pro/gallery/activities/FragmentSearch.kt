package com.pt.pro.gallery.activities

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*

class FragmentSearch : ParentFragmentGallery<com.pt.pro.gallery.fasten.StubGalleryAllFasten>(), com.pt.pro.gallery.interfaces.GalleryCommonInterface {

    override var lastJob: kotlinx.coroutines.Job? = null

    override var itemListener: com.pt.pro.gallery.interfaces.GalleryListener? = null

    private var mediaAdapter: com.pt.pro.gallery.adapters.SearchAdapter? = null

    @Volatile
    private var hiddenRunning = false

    @Volatile
    private var galleryMode: Int = GALLERY_ALL

    private var increase: Int = 400

    private var widthColumn: Int = 0

    private var searchJob: kotlinx.coroutines.Job? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run { this@creBin.context.inflaterGalleryAll() }.also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.StubGalleryAllFasten.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentSearch)
        launchImdMain {
            intiViews()
        }
    }

    private suspend fun intiViews() {
        justCoroutineMain {
            act.windowManager?.fetchDimensionsSus {
                ctx.findIntegerPref(ID_COL, COL_NUM, 3).letSus { itC ->
                    increase = ctx.findBooleanPref(
                        ID_RESOLE,
                        RES_NUM,
                        false
                    ).letSus { itR ->
                        if (!itR) itC.findResole else itC.findHighResole
                    }
                    widthColumn = if (!rec.isLandTraditional) {
                        this@fetchDimensionsSus.width / itC
                    } else {
                        this@fetchDimensionsSus.width / (itC * 2)
                    }
                    intiRec()
                }
            }
        }
    }

    private suspend fun intiRec() {
        withMain {
            binding.recGallery.applySus {
                layoutManager = ctx.findIntegerPref(
                    ID_COL, COL_NUM, 3
                ).letSus {
                    if (!rec.isLandTraditional) {
                        com.pt.common.moderator.recycler.NoAnimGridManager(ctx, it)
                    } else {
                        com.pt.common.moderator.recycler.NoAnimGridManager(ctx, it * 2)
                    }
                }
                com.pt.pro.gallery.adapters.SearchAdapter(
                    mutableListOf(),
                    itemListener,
                    widthColumn,
                    increase,
                ).applySus {
                    mediaAdapter = this
                    adapter = mediaAdapter
                }
            }
        }
        justScope {
            if (hiddenRunning) {
                scannerHidden(galleryMode)
            } else {
                doInitAllLoad(galleryMode)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.StubGalleryAllFasten.onClick(v: android.view.View) {
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.StubGalleryAllFasten.onLongClick(v: android.view.View): Boolean {
        return false
    }

    override fun setISHidden(boolean: Boolean, galleryM: Int) {
        hiddenRunning = boolean
        galleryMode = galleryM
    }

    override fun doInitAllLoad(galleryM: Int) {
        if (context == null) return
        hiddenRunning = false
        galleryMode = galleryM
        launchDef {
            forDisplayCard()
            loadAllForFolder(galleryMode) {
                context.nullChecker()
                loadAll()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend inline fun loadAllForFolder(
        galleryM: Int,
        crossinline list: suspend MutableList<MediaSack>.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            when (galleryM) {
                GALLERY_VID -> cont.loadAllVideoTime(orderBy())
                GALLERY_IMG -> cont.loadAllImageTime(orderBy())
                else -> cont.loadAllMediaSacks(orderBy())
            }
        }.applySusBack(list)
    }

    private suspend fun forDisplayCard() {
        withMain {
            binding.galleryBarCard.loadRun.rKTSack(LOAD_CONST).postAfter()
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun onSearch(searchTxt: CharSequence?) {
        if (context == null) return
        searchJob?.cancelJob()
        searchJob = launchDef {
            withBack {
                if (!searchTxt.isNullOrEmpty()) {
                    withBackDef(mutableListOf()) {
                        return@withBackDef allMedia.runCatching {
                            return@runCatching filter {
                                it.nameMedia.toStr.contains(searchTxt, true)
                            }.toMutableList()
                        }.getOrDefault(allMedia)
                    }.let {
                        context.nullChecker()
                        it.newLoadMedia()
                    }
                } else {
                    allMedia.newLoadMedia()
                }
            }
        }
    }

    private suspend fun MutableList<MediaSack>.newLoadMedia() {
        withMain {
            binding.recGallery.recycledViewPool.clear()
            mediaAdapter?.apply { updateMedia() }
        }
    }

    override fun toRefresh() {
        if (binder?.galleryBarCard?.isGon == true) {
            launchDef {
                swipeToRefreshAll()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun swipeToRefreshAll() {
        context.nullChecker()
        withBack {
            if (hiddenRunning) {
                scannerHidden(galleryMode)
            } else {
                forDisplayCard()
                loadAllForFolder(galleryMode) {
                    if (this@loadAllForFolder != allMedia) {
                        this@loadAllForFolder.loadAll()
                    } else {
                        withMain {
                            binding.galleryBarCard.apply {
                                unPost(loadRun.two)
                                justGoneSus()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun scannerHidden(galleryM: Int) {
        hiddenRunning = true
        galleryMode = galleryM
        launchDef {
            forDisplayCard()
            mutableListOf<MediaSack>().newLoadMedia()
            context.nullChecker()
            withBackDef(mutableListOf()) {
                val strS: MutableList<String> = mutableListOf()
                getAllMainRoots().forEach {
                    strS.add(it.absolutePath)
                }
                return@withBackDef strS.makeMediaFolders()
            }.letSusBack {
                context.nullChecker()
                it.loadAllHidden(galleryM)
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun getAllMainRoots(): MutableList<FileLate> = justCoroutine {
        return@justCoroutine FileLate(com.pt.common.BuildConfig.ROOT).listFiles()?.let {
            mutableListOf<FileLate>().apply {
                addAll(it)
            }
        } ?: mutableListOf()
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaFolderSack>.loadAllHidden(galleryM: Int) = justCoroutine {
        allMedia.clear()
        val new: MutableList<MediaSack> = mutableListOf()

        withBackDef(mutableListOf()) {
            cont.getListRoot(galleryM)
        }.let { rootMedia ->
            context.nullChecker()
            withMain {
                new.addAll(rootMedia)
            }
        }
        context.nullChecker()
        val allHidden = ctx.newDBGallerySus {
            getAllHiddenMedia()
        }.toMutableList().asSequence().runSusBack {
            when (galleryM) {
                GALLERY_VID -> this@runSusBack.filter { !it.isImage }
                GALLERY_IMG -> this@runSusBack.filter { it.isImage }
                else -> this@runSusBack
            }.toMutableList()
        }
        context.nullChecker()
        withBack {
            for ((path, _, _, _) in this@loadAllHidden) {
                if (context == null || !hiddenRunning) break
                if (FileLate(path ?: return@withBack).isDirectory) {
                    withBackDef(mutableListOf()) {
                        path.toRegex().let { rg ->
                            allHidden.filter { f ->
                                f.pathMedia?.contains(rg) == true
                            }.toMutableList()
                        }
                    }.letSusBack { fhH ->
                        context.nullChecker()
                        withBackDef(mutableListOf()) {
                            if (context == null || !hiddenRunning)
                                mutableListOf()
                            else
                                fhH.orderAllLoaders(orderBy())
                        }.letSusBack {
                            new.addAll(it)
                        }
                    }
                }
            }
        }
        context.nullChecker()
        new.displayAllHidden()
        allMedia.addAll(new)
    }

    @com.pt.common.global.UiAnn
    private suspend fun MutableList<MediaSack>.displayAllHidden() {
        withMain {
            if (context != null && hiddenRunning && this@displayAllHidden.isNotEmpty()) {
                this@displayAllHidden.newLoadMedia()
            }
            if (context != null) {
                binding.galleryBarCard.apply {
                    unPost(loadRun.two)
                    justGoneSus()
                }
            }
        }
    }

    private suspend fun MutableList<MediaSack>.loadAll() {
        withMain {
            context.nullChecker()
            withMain {
                binding.galleryBarCard.apply {
                    unPost(loadRun.two)
                    justGoneSus()
                }
                newLoadMedia()
            }
            allMedia.clear()
            allMedia.addAll(this@loadAll)
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        launchImdMain {
            intiViews()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        allMediaNative = null
        allFavoriteNative = null
        mediaAdapter?.onAdapterDestroy()
        itemListener = null
        mediaAdapter = null
        hiddenRunning = false
        searchJob = null
        binder = null
        super.onDestroyView()
    }

}