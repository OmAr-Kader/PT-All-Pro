package com.pt.pro.gallery.activities

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.objects.MediaDuo
import com.pt.common.stable.*
import com.pt.pro.gallery.interfaces.GalleryCommonInterface
import com.pt.pro.gallery.fasten.StubGalleryAllFasten

class FragmentAllGallery : ParentFragmentGallery<StubGalleryAllFasten>(), GalleryCommonInterface {

    override var itemListener: com.pt.pro.gallery.interfaces.GalleryListener? = null

    private var duoAdapter: com.pt.pro.gallery.adapters.DuoAdapter? = null

    @Volatile
    private var hiddenRunning = false

    @Volatile
    private var galleryMode: Int = GALLERY_ALL

    private var increase: Int = 400

    private var widthColumn: Int = 0

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run { this@creBin.context.inflaterGalleryAll() }.also {
                binder = it
            }.root_
        }

    @com.pt.common.global.UiAnn
    override fun StubGalleryAllFasten.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentAllGallery)
        launchImdMain {
            intiViews()
        }
    }

    private suspend fun intiViews() {
        withMain {
            act.windowManager?.fetchDimensionsSus {
                ctx.findIntegerPrefDb(ID_COL, COL_NUM, 3).letSusBack { itC ->
                    increase = ctx.findBooleanPrefDb(
                        ID_RESOLE,
                        RES_NUM, false
                    ).letSusBack { itR ->
                        if (!itR) itC.findResole else itC.findHighResole
                    }
                    widthColumn = if (!rec.isLandTraditional) {
                        this@fetchDimensionsSus.width / itC
                    } else {
                        this@fetchDimensionsSus.width / (itC * 2)
                    }
                    intiRec(ctx.findIntegerPrefDb(ID_COL, COL_NUM, 3))
                }
            }
        }
    }


    private suspend fun intiRec(col: Int) {
        withMain {
            binding.recGallery.apply {
                layoutManager = ctx.getVerManager
                com.pt.pro.gallery.adapters.DuoAdapter(
                    mutableListOf(),
                    itemListener,
                    widthColumn,
                    increase,
                    rec.isRightToLeft,
                    col = col,
                ).also { da ->
                    adapter = da
                    duoAdapter = da
                    if (hiddenRunning) {
                        scannerHidden(galleryMode)
                    } else {
                        doInitAllLoad(galleryMode)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun StubGalleryAllFasten.onLongClick(v: android.view.View): Boolean {

        return false
    }

    override fun setISHidden(boolean: Boolean, galleryM: Int) {
        hiddenRunning = boolean
        galleryMode = galleryM
    }

    private suspend fun forDisplayCard() {
        withMain {
            binding.galleryBarCard.loadRun.rKTSack(LOAD_CONST).postAfterIfNot()
        }
    }

    override fun doInitAllLoad(galleryM: Int) {
        hiddenRunning = false
        galleryMode = galleryM
        launchDef {
            forDisplayCard()
            loadAllForFolder(galleryM) {
                context.nullChecker()
                withBack {
                    mutableListOf<MediaDuo>().applySusBack {
                        loadFavoritePic {
                            add(0, this@loadFavoritePic)
                        }
                        addAll(this@loadAllForFolder)
                    }.loadAll()
                }
            }
        }
    }

    override fun toRefresh() {
        if (binding.galleryBarCard.isGon) {
            launchDef {
                swipeToRefreshAll()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun swipeToRefreshAll() {
        withBack {
            if (hiddenRunning) {
                scannerHidden(galleryMode)
            } else {
                binding.galleryBarCard.loadRun.rKTSack(LOAD_CONST).postAfterIfNot()
                loadAllForFolder(galleryMode) {
                    mutableListOf<MediaDuo>().alsoSusBack { itD ->
                        loadFavoritePic {
                            itD.add(0, this@loadFavoritePic)
                        }
                        itD.addAll(this@loadAllForFolder)
                        context.nullChecker()
                        itD.onlyMedia().letSusBack { allCheck ->
                            ctx.newDBGallerySus {
                                getAllVFavMedia()
                            }.letSusBack { ref ->
                                if (allCheck != allMedia || ref != allFavorite) {
                                    itD.loadAll()
                                } else {
                                    removeCard()
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private suspend fun removeCard() {
        withMain {
            binding.galleryBarCard.apply {
                unPost(loadRun.two)
                justGoneSus()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaDuo>.onlyMedia(): MutableList<MediaSack> =
        justCoroutine {
            context.nullChecker()
            withBackDef(mutableListOf()) {
                mutableListOf<MediaSack>().applySusBack {
                    this@onlyMedia.forEach {
                        this@applySusBack.addAll(it.mediaHolder)
                    }
                }
            }
        }

    @com.pt.common.global.WorkerAnn
    private suspend inline fun loadAllForFolder(
        galleryM: Int,
        crossinline list: suspend MutableList<MediaDuo>.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            context.nullChecker()
            when (galleryM) {
                GALLERY_VID -> cont.loadAllVideo(orderByFolder())
                GALLERY_IMG -> cont.loadAllImage(orderByFolder())
                else -> cont.loadAllMedia(orderByFolder())
            }.onEachSusBack(context) {
                mediaHolder.orderAllLoaders(orderBy()).letSusBack {
                    mediaHolder = it
                }
            }
        }.applySusBack(list)
    }


    override fun scannerHidden(galleryM: Int) {
        hiddenRunning = true
        galleryMode = galleryM
        launchDef {
            beforeScannerHidden()
            context.nullChecker()
            withBackDef(mutableListOf()) {
                val strS: MutableList<String> = mutableListOf()
                getAllMainRoots().forEach {
                    strS.add(it.absolutePath)
                }
                return@withBackDef strS.makeMediaFolders()
            }.loadHidden(galleryM)
        }
    }

    private suspend fun beforeScannerHidden() {
        withMain {
            if (binding.galleryBarCard.isGon) {
                binding.galleryBarCard.loadRun.rKTSack(LOAD_CONST).postAfterIfNot()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun getAllMainRoots() = justCoroutine {
        return@justCoroutine FileLate(com.pt.common.BuildConfig.ROOT).listAllFiles().filter { it.isDirectory }.letSus {
            mutableListOf<FileLate>().applySus {
                addAll(it)
            }
        }
    }

    private suspend fun MutableList<MediaDuo>.loadAll() {
        context.nullChecker()
        withMain {
            binding.recGallery.recycledViewPool.clear()
        }
        duoAdapter?.applySus {
            updateDuo()
        }
        removeCard()
        onlyMedia().replaceList()
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.replaceList() {
        withMain {
            allMedia.clear()
        }
        withMain {
            allMedia.addAll(this@replaceList)
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaFolderSack>.loadHidden(galleryM: Int) = justCoroutine {
        val new: MutableList<MediaSack> = mutableListOf()
        val newDuo: MutableList<MediaDuo> = mutableListOf()
        withBack {
            loadFavoritePic {
                newDuo.add(this)
                new.addAll(this.mediaHolder)
            }
        }
        context.nullChecker()
        withBackDef(mutableListOf()) {
            allMedia.clear()
            cont.getListRoot(galleryM)
        }.letSusBack {
            context.nullChecker()
            withBack {
                if (it.isNotEmpty()) {
                    MediaDuo(
                        com.pt.common.BuildConfig.ROOT,
                        ROOT_NAME,
                        it.size,
                        false,
                        it.toMutableList()
                    ).letSusBack {
                        newDuo.add(it)
                    }
                    new.addAll(it)
                }
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
            }
        }
        context.nullChecker()
        withBack {
            for ((path, folderName, _, _) in this@loadHidden) {
                if (context == null || !hiddenRunning) break
                if (FileLate(path ?: return@withBack).isDirectory) {
                    context.nullChecker()
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
                            context.nullChecker()
                            withBack {
                                if (context != null && hiddenRunning) {
                                    if (it.isNotEmpty()) {
                                        MediaDuo(
                                            path,
                                            folderName,
                                            it.size,
                                            false,
                                            it.toMutableList()
                                        ).letSusBack {
                                            newDuo.add(it)
                                        }
                                        new.addAll(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        context.nullChecker()
        newDuo.displayHidden()
        withBack {
            allMedia.addAll(new)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun MutableList<MediaDuo>.displayHidden() {
        withMain {
            if (context != null) {
                removeCard()
            }
            if (this@displayHidden != duoAdapter?.foldsDuo) {
                this@displayHidden.loadAll()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend inline fun loadFavoritePic(crossinline duo: suspend MediaDuo.() -> Unit) {
        withBackDef(mutableListOf()) {
            ctx.newDBGallerySus {
                getAllVFavMedia()
            }
        }.letSusBack {
            context.nullChecker()
            withBack {
                allFavorite.clear()
            }
            withBack {
                allFavorite.addAll(it)
            }
        }
        context.nullChecker()
        withBack {
            if (allFavorite.isNotEmpty()) {
                MediaDuo(
                    null,
                    com.pt.common.BuildConfig.FAVORITE,
                    allFavorite.size,
                    true,
                    allFavorite
                ).applySusBack(duo)
            }
        }
    }

    override fun StubGalleryAllFasten.onClick(v: android.view.View) {

    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        launchImdMain {
            intiViews()
        }
    }

    override fun onDestroyView() {
        duoAdapter?.onAdapterDestroy()
        allMediaNative = null
        allFavoriteNative = null
        itemListener = null
        duoAdapter = null
        hiddenRunning = false
        catchy(Unit) {
            binder?.recGallery?.adapter = null
            binder?.recGallery?.removeAllViewsInLayout()
            binder?.root_?.removeAllViewsInLayout()
        }
        super.onDestroyView()
    }

}