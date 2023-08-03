package com.pt.pro.gallery.activities

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*

class FragmentAlbumGallery : ParentFragmentGallery<com.pt.pro.gallery.fasten.StubGalleryAllFasten>(),
    com.pt.pro.gallery.interfaces.GalleryCommonInterface {

    override var itemListener: com.pt.pro.gallery.interfaces.GalleryListener? = null

    private var foldsAlbumNative: MutableList<MediaFolderSack>? = mutableListOf()
    private inline val foldsAlbum: MutableList<MediaFolderSack>
        get() = foldsAlbumNative ?: mutableListOf<MediaFolderSack>().also {
            foldsAlbumNative = it
        }

    private var increase: Int = 400

    private var widthColumn: Int = 0

    private var folderAdapter: com.pt.pro.gallery.adapters.PictureFolderAdapter? = null

    @Volatile
    private var hiddenRunning = false

    @Volatile
    private var galleryMode: Int = GALLERY_ALL

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run { this@creBin.context.inflaterGalleryAll() }.also {
                binder = it
            }.root_
        }

    private suspend fun android.util.Size.intiViewDim() {
        2.let { itC ->
            increase = ctx.findBooleanPrefDb(
                ID_RESOLE,
                RES_NUM,
                false
            ).let { itR ->
                if (!itR) (itC - 1).findResole else itC.findHighResole
            }
            widthColumn = if (!rec.isLandTraditional) {
                width / (itC)
            } else {
                width / ((itC) * 2)
            }.let { it1 ->
                if (it1 != 0) it1 else rec.getDimension(com.pt.pro.R.dimen.gp).toInt()
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.StubGalleryAllFasten.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentAlbumGallery)
        launchDef {
            intiViews()
        }
        launchDef {
            context.nullChecker()
            getFolders(galleryMode) {
                foldsAlbumNative = this
            }
        }
    }

    private suspend fun intiViews() {
        justCoroutine {
            act.windowManager?.fetchDimensionsSus {
                context.nullChecker()
                intiViewDim()
                intiRec()
                context.nullChecker()
                withMain {
                    if (hiddenRunning) {
                        scannerHidden(galleryMode)
                    } else {
                        doInitAllLoad(galleryMode)
                    }
                }
            }
        }
    }

    private suspend fun intiRec() {
        withMain {
            binding.recGallery.applySus {
                layoutManager = 2.let {
                    if (!rec.isLandTraditional) {
                        com.pt.common.moderator.recycler.NoAnimGridManager(ctx, it)
                    } else {
                        com.pt.common.moderator.recycler.NoAnimGridManager(ctx, (it) * 2)
                    }
                }
                if (widthColumn != 0) {
                    widthColumn
                } else {
                    rec.getDimension(com.pt.pro.R.dimen.gf).toInt()
                }.alsoSus { fW ->
                    com.pt.pro.gallery.adapters.PictureFolderAdapter(
                        mutableListOf(),
                        itemListener,
                        fW,
                        fW / 2,//(fW + (fW * 0.1).toInt())
                        them.findAttr(android.R.attr.colorAccent)
                    ).alsoSus {
                        folderAdapter = it
                        adapter = it
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.StubGalleryAllFasten.onLongClick(v: android.view.View): Boolean {

        return false
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
                getFolders(galleryMode) {
                    if (foldsAlbum != this) {
                        foldsAlbumNative = this
                        foldsAlbum.loadFolders()
                    } else {
                        withMain {
                            binding.galleryBarCard.apply {
                                unPost(loadRun.two)
                                justGoneSus()
                            }
                            binding.galleryBarCard.justGone()
                        }
                    }
                }
            }
        }
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
            getFolders(galleryMode) {
                foldsAlbumNative = this
            }
            context.nullChecker()
            foldsAlbum.loadFolders()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun getAllMainRoots(): MutableList<FileLate> = justCoroutine {
        return@justCoroutine FileLate(com.pt.common.BuildConfig.ROOT).listAllFiles().filter { it.isDirectory }.letSus {
            mutableListOf<FileLate>().applySus {
                addAll(it)
            }
        }
    }

    override fun scannerHidden(galleryM: Int) {
        hiddenRunning = true
        galleryMode = galleryM
        launchDef {
            val deviceFolders: MutableList<MediaFolderSack> = mutableListOf()
            beforeHidden()
            withBack {
                allFavorite.clear()
                ctx.newDBGallerySus {
                    getAllVFavMedia()
                }.letSusBack {
                    allFavorite.addAll(it)
                }
            }
            withBack {
                if (allFavorite.isNotEmpty()) {
                    MediaFolderSack(
                        "",
                        com.pt.common.BuildConfig.FAVORITE,
                        allFavorite.size,
                        allFavorite.getI(0).pathMedia
                    ).letSusBack {
                        deviceFolders.add(it)
                    }
                }
            }
            context.nullChecker()
            withBackDef(mutableListOf()) {
                cont.getListRoot(galleryM).orderAllLoaders(orderBy())
            }.letSusBack {
                context.nullChecker()
                if (it.isNotEmpty()) {
                    MediaFolderSack(
                        com.pt.common.BuildConfig.ROOT,
                        ROOT_NAME,
                        it.size,
                        it.getI(0).pathMedia
                    ).letSusBack { itF ->
                        deviceFolders.add(itF)
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
            getAllMainRoots().alsoSusBack { ro ->
                withBackDef(mutableListOf()) {
                    val newAa: MutableList<MediaFolderSack> = mutableListOf()
                    ro.forEach {
                        if (context == null && !hiddenRunning) return@forEach
                        withBackDef(mutableListOf()) {
                            it.absolutePath.toRegex().let { rg ->
                                allHidden.filter { f ->
                                    f.pathMedia?.contains(rg) == true
                                }.toMutableList()
                            }
                        }.letSusBack { s ->
                            if (context == null && !hiddenRunning) return@letSusBack
                            context.nullChecker()
                            s.orderAllLoaders(orderBy()).letSusBack { itR ->
                                if (s.size != 0) newAa.add(
                                    MediaFolderSack(
                                        it.absolutePath,
                                        it.name,
                                        s.size,
                                        itR.getI(0).pathMedia
                                    )
                                )
                                s.clear()
                            }
                        }
                    }
                    newAa
                }.letSusBack { aa ->
                    context.nullChecker()
                    withBack {
                        deviceFolders.addAll(aa)
                        aa.clear()
                        if (context != null) deviceFolders.loadHiddenFolders()
                    }
                }
            }
        }
    }

    private suspend fun beforeHidden() {
        withMain {
            if (binding.galleryBarCard.isGon) {
                binding.galleryBarCard.loadRun.rKTSack(LOAD_CONST).postAfterIfNot()
            }
        }
    }

    private suspend fun MutableList<MediaFolderSack>.loadHiddenFolders() {
        withMain {
            binding.recGallery.recycledViewPool.clear()
            binding.galleryBarCard.apply {
                unPost(loadRun.two)
                justGoneSus()
            }
        }
        withMain {
            folderAdapter?.applySus {
                if (folderMedia != this@loadHiddenFolders) {
                    this@loadHiddenFolders.updateFolder()
                }
            }
            binding.galleryBarCard.apply {
                unPost(loadRun.two)
                justGoneSus()
            }
        }
    }

    private suspend fun MutableList<MediaFolderSack>.loadFolders() {
        fetchAllFavorites()
        withBackDef(mutableListOf()) {
            mutableListOf<MediaFolderSack>().applySusBack {
                if (allFavorite.isNotEmpty()) {
                    this@applySusBack.add(fetchFavoriteFolder)
                }
                this@applySusBack.addAll(this@loadFolders)
            }
        }.displayLoadFolders()
    }

    private inline val fetchFavoriteFolder: MediaFolderSack
        get() = MediaFolderSack(
            "",
            com.pt.common.BuildConfig.FAVORITE,
            allFavorite.size,
            allFavorite.getI(0).pathMedia
        )

    @com.pt.common.global.WorkerAnn
    private suspend fun fetchAllFavorites() = justCoroutine {
        withBack {
            allFavorite.clear()
        }
        withBack {
            ctx.newDBGallerySus {
                getAllVFavMedia()
            }.let {
                allFavorite.addAll(it)
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun MutableList<MediaFolderSack>.displayLoadFolders() {
        withMain {
            binding.recGallery.recycledViewPool.clear()
            binding.galleryBarCard.apply {
                unPost(loadRun.two)
                justGoneSus()
            }
        }
        withMain {
            folderAdapter?.applySus {
                if (folderMedia != this@displayLoadFolders) {
                    this@displayLoadFolders.updateFolder()
                }
            }
        }
    }

    private suspend inline fun getFolders(
        galleryM: Int,
        crossinline list: suspend MutableList<MediaFolderSack>.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            when (galleryM) {
                GALLERY_VID -> cont.videoFoldersLoader(orderByFolder())
                GALLERY_IMG -> cont.imageFoldersLoader(orderByFolder())
                else -> cont.allFoldersLoader(orderByFolder())
            }
        }.applySusBack(list)
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.StubGalleryAllFasten.onClick(v: android.view.View) {

    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        launchDef {
            intiViews()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        folderAdapter?.onAdapterDestroy()
        foldsAlbumNative = null
        allMediaNative = null
        allFavoriteNative = null
        itemListener = null
        folderAdapter = null
        hiddenRunning = false
        catchy(Unit) {
            binder?.recGallery?.adapter = null
            binder?.recGallery?.removeAllViewsInLayout()
            binder?.root_?.removeAllViewsInLayout()
        }
        super.onDestroyView()
    }

}