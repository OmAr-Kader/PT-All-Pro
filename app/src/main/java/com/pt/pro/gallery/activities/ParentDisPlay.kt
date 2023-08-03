package com.pt.pro.gallery.activities

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*
import com.pt.pro.gallery.fasten.FragmentDisplayFasten
import com.pt.pro.gallery.fasten.PopWindowGalleryFasten
import com.pt.pro.gallery.fasten.StubOptionFasten

abstract class ParentDisPlay : ParentFragmentGallery<FragmentDisplayFasten>(),
    com.pt.pro.gallery.interfaces.DisplayListen, android.view.View.OnTouchListener,
    com.pt.common.mutual.base.BackInterface {

    protected var isHaveRefresh: Boolean = false
    protected var isPaused: Boolean = false
    protected var haveNewUpdate: Boolean = false

    protected var observing: android.database.ContentObserver? = null

    protected var scaleGestureDetector: android.view.ScaleGestureDetector? = null

    protected var animRun: (() -> Unit)? = null
    protected var columnNumber: Int = 3

    protected inline val pictureAdapter: com.pt.pro.gallery.adapters.PictureAdapter get() = pictureAda!!

    protected var pictureAda: com.pt.pro.gallery.adapters.PictureAdapter? = null

    protected var searchListNative: MutableList<MediaSack>? = mutableListOf()
    protected inline val searchList: MutableList<MediaSack> get() = searchListNative!!

    protected var foldsNative: MutableList<MediaFolderSack>? = mutableListOf()
    protected inline val folds: MutableList<MediaFolderSack> get() = foldsNative!!

    protected var favoritesNative: MutableList<String>? = mutableListOf()
    private inline val favorites: MutableList<String> get() = favoritesNative!!

    protected var galleryMode: Int = GALLERY_ALL
    protected var displayType: Int = DISPLAY_NORMAL
    protected var pickType: Int = DISPLAY_NO_PICKING
    protected var folderPath: String? = null
    protected var firstSearch: Boolean = false
    protected var isTouch: Boolean = false

    protected var stubOptions: StubOptionFasten? = null

    protected var widthColumn: Int = 150
    protected var increase: Int = 400

    protected var ratioPlus: Float = 1F
    protected var endScroll: Float = 0.0F
    protected var intY: Float = 0.0F
    protected var endHeight: Float = 0.0F
    protected var endRatio: Float = 0.0F
    protected var start: Float = 0.0F

    protected var marginWidth: Int = 0
    protected var marginHeight: Int = 0
    protected var isDoInLand: Boolean = false

    protected inline val isPendOrHide: Boolean
        get() = displayType == DISPLAY_PENDING || displayType == DISPLAY_HIDDEN || displayType == DISPLAY_HIDDEN_TIME

    @com.pt.common.global.WorkerAnn
    protected suspend fun scannerHidden(rootPath: String, galleryM: Int): MutableList<MediaSack> = justCoroutine {
        beforeHidden()
        context.nullChecker()
        return@justCoroutine withBackDef(mutableListOf()) {
            ctx.newDBGallerySus {
                getAllHiddenMedia()
            }.toMutableList().asSequence().runSusBack phaseOne@{
                rootPath.toRegex().letSusBack { rg ->
                    when (galleryM) {
                        GALLERY_VID -> this@phaseOne.filter { f -> !f.isImage && f.pathMedia?.contains(rg) == true }
                        GALLERY_IMG -> this@phaseOne.filter { f -> f.isImage && f.pathMedia?.contains(rg) == true }
                        else -> this@phaseOne.filter { f -> f.pathMedia?.contains(rg) == true }
                    }
                }.runSusBack phaseTwo@{
                    return@phaseTwo if (rootPath == com.pt.common.BuildConfig.ROOT) {
                        "$rootPath/".letSusBack { ro ->
                            this@phaseTwo.filter { (nameMedia, pathMedia, _, _, _, _, _, _, _, _, _) ->
                                pathMedia?.contains(ro + nameMedia) == true
                            }
                        }
                    } else this@phaseTwo
                }
            }.toMutableList()
        }.letSusBack { fhH ->
            context.nullChecker()
            afterHidden()
            context.nullChecker()
            return@letSusBack withBackDef(mutableListOf()) {
                mutableListOf<MediaSack>().applySusBack {
                    this@applySusBack.addAll(fhH.orderAllLoaders(orderBy()))
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun scannerHiddenTime(start: Long, end: Long, galleryM: Int): MutableList<MediaSack> = justCoroutine {
        beforeHidden()
        context.nullChecker()
        return@justCoroutine withBackDef(mutableListOf()) {
            ctx.newDBGallerySus {
                getAllHiddenMedia()
            }.toMutableList().asSequence().runSusBack {
                when (galleryM) {
                    GALLERY_VID -> this@runSusBack.filter { f -> !f.isImage && f.dateModified in start until end }
                    GALLERY_IMG -> this@runSusBack.filter { f -> f.isImage && f.dateModified in start until end }
                    else -> filter { f -> f.dateModified in start until end }
                }
            }.toMutableList()
        }.letSusBack { fhH ->
            context.nullChecker()
            afterHidden()
            context.nullChecker()
            return@letSusBack withBackDef(mutableListOf()) {
                mutableListOf<MediaSack>().applySusBack {
                    this@applySusBack.addAll(fhH.orderAllLoaders(orderBy()))
                }
            }.runSusBack {
                if (this@runSusBack.isEmpty()) {
                    loadForTime(galleryM)
                } else this@runSusBack
            }
        }
    }

    protected suspend fun loadForTime(galleryM: Int): MutableList<MediaSack> = justCoroutine {
        return@justCoroutine withBackDef(mutableListOf()) {
            return@withBackDef act.intent.getLongExtra(DISPLAY_TIME_START, -1).let { from ->
                act.intent.getLongExtra(DISPLAY_TIME_END, -1).let { to ->
                    if (from != -1L && to != -1L) {
                        when (galleryM) {
                            GALLERY_VID -> cont.loadAllDisplayVideoTime(orderBy = orderBy(), fromTime = from.toStr, toTime = to.toStr)
                            GALLERY_IMG -> cont.loadAllDisplayImageTime(orderBy = orderBy(), fromTime = from.toStr, toTime = to.toStr)
                            else -> cont.loadAllMediaSacksTime(orderBy = orderBy(), fromTime = from.toStr, toTime = to.toStr)
                        }
                    } else {
                        mutableListOf()
                    }
                }
            }
        }
    }

    private suspend fun beforeHidden() {
        withMain {
            iBindingSus {
                displayBarCard.justVisible()
            }
        }
    }

    protected suspend fun afterHidden() {
        withMain {
            iBindingSus {
                displayBarCard.apply {
                    unPost(loadRun.two)
                    justGoneSus()
                }
            }
        }
    }

    protected suspend fun MutableList<MediaSack>.deleteDialog() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_DELETE) == null) {
                newPopForDelete {
                    folderPath = this@deleteDialog.toMutableList()
                    isNight = nightRider
                    listener = deleteListener
                    this@newPopForDelete
                }.show(childFragmentManager, DIALOG_DELETE)
            }
        }
    }

    protected var deleteListener: com.pt.pro.gallery.dialogs.PopForDelete.DeleteListener?
        set(value) {
            value.logProvLess()
        }
        get() {
            return com.pt.pro.gallery.dialogs.PopForDelete.DeleteListener {
                binder?.displayBarCard?.justVisible()
                val posDesc = pictureAdapter.pos.sortedByDescending { it }.toMutableList()
                launchDef {
                    deleteMedia(posDesc)
                }
            }
        }

    override suspend fun MutableList<MediaSack>.checkAfterEdit(posDesc: MutableList<Int>) {
        withBack {
            runCatching {
                if (size == allMedia.size) {
                    act.finish()
                } else if (displayType == DISPLAY_PENDING) {
                    posDesc.onEachSus(context) {
                        allMedia.removeAt(this@onEachSus)
                        pictureAdapter.mediaList.removeAt(this@onEachSus)
                        pictureAdapter.notifyItemRemoved(this@onEachSus)
                    }
                    withBack {
                        medPend.clear()
                    }
                    withBack {
                        medPend.addAll(pictureAdapter.mediaList)
                    }
                }
            }
        }
    }

    protected suspend fun MutableList<MediaSack>.pending() {
        withBack {
            onEachSus(context) {
                com.pt.pro.gallery.objects.MyHash.apply {
                    this@onEachSus.addPending()
                }
            }
        }
    }

    private suspend fun MutableList<MediaSack>.hideMedia() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_FOR_HIDE) == null) {
                newPopForHide {
                    folderPath = this@hideMedia.toMutableList()
                    listener = hidListener
                    this@newPopForHide
                }.show(childFragmentManager, DIALOG_FOR_HIDE)
            }
        }
    }

    protected var hidListener: com.pt.pro.gallery.dialogs.PopForHide.PopHiddenListener?
        set(value) {
            value.logProvLess()
        }
        get() {
            return com.pt.pro.gallery.dialogs.PopForHide.PopHiddenListener { picsForHide, videosForHide, opt ->
                when (opt) {
                    111 -> applyHidden(picsForHide, videosForHide)
                    222 -> applyShow(picsForHide, videosForHide)
                    333 -> applyMediaStoreShow(picsForHide, videosForHide)
                }
            }
        }

    private fun applyHidden(
        picsForHide: MutableList<MediaSack>,
        videosForHide: MutableList<MediaSack>,
    ) {
        launchDef {
            withBack {
                picsForHide.onEachSus(context) {
                    context.nullChecker()
                    FileLate(pathMedia ?: return@onEachSus).apply {
                        FileLate(
                            pathMedia.toStr
                        ).copyFileTo(
                            FileLate((this@apply.parent?.toStr + "/." + this@apply.name))
                        ).letSusBack {
                            ctx.fileDeleter(pathMedia.toStr, IMAGE, uriMedia?.toUri)
                        }
                    }
                }
            }
            context.nullChecker()
            withBack {
                videosForHide.onEachSus(context) {
                    context.nullChecker()
                    FileLate(pathMedia ?: return@onEachSus).apply {
                        FileLate(
                            pathMedia.toStr
                        ).copyFileTo(
                            FileLate((this@apply.parent?.toStr + "/." + this@apply.name))
                        ).letSusBack {
                            ctx.fileDeleter(pathMedia.toStr, VIDEO, uriMedia?.toUri)
                        }
                    }
                }
            }
            context.nullChecker()
            mutableListOf<MediaSack>().applySusBack {
                addAll(picsForHide)
                addAll(videosForHide)
            }.checkAfterEdit(
                pictureAdapter.pos.sortedByDescending { it }.toMutableList()
            )
            displayHidden()
        }
    }

    private suspend fun displayHidden() {
        withMain {
            com.pt.pro.gallery.objects.MyHash.run {
                pictureAdapter.applySus {
                    deleteCopy.checkGalleryPending()
                    offMenuDisplay()
                }
            }
        }
    }

    private fun applyShow(
        picsForShow: MutableList<MediaSack>,
        videosForShow: MutableList<MediaSack>,
    ) {
        launchDef {
            withBack {
                picsForShow.onEachSus(context) {
                    context.nullChecker()
                    FileLate(this@onEachSus.pathMedia.toStr).letSus { new ->
                        (new.parent?.toStr + "/" + new.name.removeRange(0, 1))
                    }.letSusBack { pat ->
                        FileLate(
                            pathMedia ?: return@letSusBack
                        ).copyFileTo(FileLate(pat)).letSusBack {
                            cont.mediaStoreImage(this@onEachSus, pat, null)
                            ctx.mediaBroadCast(pathMedia, pat)
                            ctx.fileDeleter(
                                pathMedia.toStr,
                                IMAGE,
                                uriMedia?.toUri
                            )
                        }
                    }
                }
            }
            context.nullChecker()
            withBack {
                videosForShow.onEachSus(context) {
                    context.nullChecker()
                    FileLate(this@onEachSus.pathMedia.toStr).letSus { new ->
                        (new.parent?.toStr + "/" + new.name.removeRange(0, 1))
                    }.letSusBack { pat ->
                        FileLate(pathMedia ?: return@letSusBack).copyFileTo(FileLate(pat))
                            .letSusBack {
                                cont.mediaStoreImage(this@onEachSus, pat, null)
                                ctx.mediaBroadCast(pathMedia, pat)
                                ctx.fileDeleter(
                                    pathMedia.toStr,
                                    VIDEO,
                                    uriMedia?.toUri
                                )
                                if (isImage) {
                                    cont.mediaStoreImage(
                                        this@onEachSus,
                                        pat,
                                        null
                                    )
                                } else {
                                    cont.mediaStoreVideo(
                                        this@onEachSus,
                                        pat,
                                        null
                                    )
                                }
                                ctx.mediaBroadCast(pathMedia, pat)
                            }
                    }
                }
            }
        }
    }

    private fun applyMediaStoreShow(
        picsForShow: MutableList<MediaSack>,
        videosForShow: MutableList<MediaSack>,
    ) {
        launchDef {
            withBack {
                picsForShow.onEachSus(context) {
                    context.nullChecker()
                    cont.mediaStoreImage(this@onEachSus, this@onEachSus.pathMedia, null)
                }
            }
            context.nullChecker()
            withBack {
                videosForShow.onEachSus(context) {
                    context.nullChecker()
                    cont.mediaStoreVideo(this@onEachSus, this@onEachSus.pathMedia, null)
                }
            }
        }
    }

    override suspend fun MutableList<MediaSack>.deleteMedia(posDesc: MutableList<Int>) {
        withBack {
            onEachSusBack(context) {
                context.nullChecker()
                this@onEachSusBack.uriMedia.letSusBack { uriStr ->
                    uriStr?.toUri ?: ctx.uriProvider(
                        this@onEachSusBack.pathMedia,
                        com.pt.pro.BuildConfig.APPLICATION_ID
                    )
                }.letSusBack { uri ->
                    ctx.fileDeleter(pathMedia.toStr, if (isImage) IMAGE else VIDEO, uri)
                }
            }
        }
        withMain {
            displayDelete(posDesc)
        }
    }

    private suspend fun MutableList<MediaSack>.displayDelete(
        posDesc: MutableList<Int>,
    ) {
        withMain {
            com.pt.pro.gallery.objects.MyHash.runSus {
                pictureAdapter.deleteCopy.checkGalleryPending()
            }
            checkAfterEdit(posDesc)
            pictureAdapter.offMenuDisplay()
        }
    }

    protected suspend fun MutableList<MediaSack>.clipDate() {
        getClipDate().doClipDate()
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<MediaSack>.getClipDate(): MutableList<android.net.Uri> =
        justCoroutine {
            withBackDef(mutableListOf()) {
                mutableListOf<android.net.Uri>().applySusBack {
                    this@getClipDate.onEachSus(context) {
                        this@onEachSus.uriMedia.letSusBack { uriStr ->
                            uriStr?.toUri ?: ctx.uriProvider(
                                this@onEachSus.pathMedia,
                                com.pt.pro.BuildConfig.APPLICATION_ID
                            )
                        }.letSusBack { uri ->
                            this@applySusBack.add(uri)
                        }
                    }
                }
            }
        }


    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<android.net.Uri>.doClipDate() {
        withBack {
            android.content.ClipData(
                "ImagePicker",
                arrayOf(
                    com.pt.common.BuildConfig.IMAGE_SENDER,
                    com.pt.common.BuildConfig.VIDEO_SENDER,
                    android.content.ClipDescription.MIMETYPE_TEXT_URILIST
                ),
                android.content.ClipData.Item(this@doClipDate.firstOrNull())
            ).alsoSusBack { clip ->
                this@doClipDate.forEach {
                    clip.addItem(android.content.ClipData.Item(it))
                }
            }.alsoSusBack { clip ->
                ctx.fetchSystemService(clipService)?.also {
                    it.setPrimaryClip(clip)
                }
                android.content.Intent().applySusBack {
                    data = this@doClipDate.lastOrNull()
                    putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, ArrayList(this@doClipDate))
                    clipData = clip
                    act.setResult(-1, this)
                    act.finish()
                }
            }
        }
    }

    private suspend fun MutableList<MediaSack>.clipboard() {
        withDefaultDef(folds) {
            //val pictureList = allMedia
            //pictureList.removeAll(this@clipboard)
            folds.toMutableList().asSequence().filter {
                it.path != folderPath.toStr
            }.toMutableList()
        }.letSusBack { itA ->
            withMainNormal {
                if (childFragmentManager.findFragmentByTag(DIALOG_CLIPBOARD) == null) {
                    newPopForClipboard {
                        folderMedia = itA.toMutableList()
                        folderPath = this@clipboard.toMutableList()
                        listener = clipListener
                        this@newPopForClipboard
                    }.show(childFragmentManager, DIALOG_CLIPBOARD)
                }
            }
        }
    }

    private suspend fun MutableList<MediaSack>.properties() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_PROPERTIES) == null) {
                newPopForProperties {
                    folderPath = this@properties.toMutableList()
                    listener = isPendOrHide
                    this@newPopForProperties
                }.show(childFragmentManager, DIALOG_PROPERTIES)
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected suspend fun doDisplayOption() {
        withMain {
            with<PopWindowGalleryFasten, Unit>(
                com.pt.pro.gallery.fasten.GalleryInflater.run { ctx.inflaterPopGallery() }
            ) {
                android.widget.PopupWindow(
                    root_,
                    WRAP,
                    WRAP,
                    true
                ).applySus {
                    withMain {
                        setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                        root_.setCardBackgroundColor(them.findAttr(com.pt.pro.R.attr.rmoBackground))
                        if (pictureAdapter.deleteCopy.size == 1) {
                            setAs.justVisible()
                            rename.justVisible()
                        } else {
                            setAs.justGone()
                            rename.justGone()
                        }
                        delete.justGone()
                        share.justGone()
                        DSackT(this@with, this@applySus).run {
                            setAs.setOnClickListener(popClickList)
                            rename.setOnClickListener(popClickList)
                            properties.setOnClickListener(popClickList)
                            selected.setOnClickListener(popClickList)
                            favDis.setOnClickListener(popClickList)
                            unFavDis.setOnClickListener(popClickList)
                            hide.setOnClickListener(popClickList)
                            clipboard.setOnClickListener(popClickList)
                            sendScanner.setOnClickListener(popClickList)
                            pendingDisplay.applySus {
                                if (displayType == DISPLAY_PENDING)
                                    justGone()
                                else
                                    setOnClickListener(popClickList)
                            }
                            Unit
                        }
                    }
                    context.nullChecker()
                    withMain {
                        showAsDropDown(stubOptions?.pagerMenu)
                    }
                    context.nullChecker()
                    checkFav(pictureAdapter.deleteCopy.toMutableList().asSequence())
                }
            }
        }
    }

    private suspend fun PopWindowGalleryFasten.checkFav(
        deleteCopy: Sequence<MediaSack>
    ) {
        withBackDef(mutableListOf()) {
            ctx.newDBGallerySus {
                getAllFavPath()
            }.letSusBack {
                favorites.addAll(it)
            }
            return@withBackDef favorites
        }.letSusBack { f ->
            context.nullChecker()
            withBackDef(mutableListOf()) {
                return@withBackDef deleteCopy.filter {
                    f.contains(it.pathMedia.toStr)
                }.toMutableList()
            }.letSusBack { favList ->
                context.nullChecker()
                if (favList.isEmpty()) {
                    displayForUnFav()
                }
            }
            context.nullChecker()
            withBackDef(mutableListOf()) {
                return@withBackDef deleteCopy.filter {
                    !f.contains(it.pathMedia.toStr)
                }.toMutableList()
            }.letSusBack { unFavList ->
                context.nullChecker()
                if (unFavList.isEmpty()) {
                    displayForFav()
                }
            }
        }
    }

    private suspend fun PopWindowGalleryFasten.displayForUnFav() {
        unFavDis.justGoneSus()
    }

    private suspend fun PopWindowGalleryFasten.displayForFav() {
        favDis.justGoneSus()
    }

    @com.pt.common.global.UiAnn
    protected suspend fun StubOptionFasten.optionProvider(text: String) {
        withMain {
            if (isPendOrHide) {
                root_.backReColor(ctx.fetchColor(com.pt.pro.R.color.hbd))
                ctx.fetchColor(com.pt.pro.R.color.whi).also {
                    menuNumber.setTextColor(it)
                    clipDate.setTextColor(it)
                }
                deleteDate.svgReColorWhite()
                share.svgReColorWhite()
                backDisplayMenu.svgReColorWhite()
                pagerMenu.svgReColorWhite()
            }
            if (pickType == DISPLAY_PICK_MULTI || pickType == DISPLAY_PICK_NORMAL) {
                clipDate.justVisible()
                share.justGone()
                deleteDate.justGone()
                pagerMenu.justGone()
            }
            share.setOnLongClickListener(this@ParentDisPlay)
            deleteDate.setOnClickListener(this@ParentDisPlay)
            deleteDate.setOnLongClickListener(this@ParentDisPlay)
            share.setOnClickListener(this@ParentDisPlay)
            backDisplayMenu.setOnClickListener(this@ParentDisPlay)
            clipDate.setOnClickListener(this@ParentDisPlay)
            pagerMenu.setOnClickListener(this@ParentDisPlay)
            pagerMenu.setOnLongClickListener(this@ParentDisPlay)
            menuNumber.text = text
        }
    }

    private inline val DSackT<PopWindowGalleryFasten, android.widget.PopupWindow>.popClickList: android.view.View.OnClickListener
        @com.pt.common.global.UiAnn
        get() {
            return android.view.View.OnClickListener {
                if (pictureAdapter.deleteCopy.isEmpty()) {
                    ctx.makeToastRec(com.pt.pro.R.string.si, 0)
                    return@OnClickListener
                }
                launchMain {
                    withMain {
                        pictureAdapter.deleteCopy.applySus {
                            when (it) {
                                one.properties -> properties()
                                one.hide -> hideMedia()
                                one.clipboard -> clipboard()
                                one.pendingDisplay -> pending()
                                one.setAs -> {
                                    launchDef {
                                        ctx.setAsUseServices(
                                            deleteCopy = last(),
                                            s = resources.getString(com.pt.pro.R.string.ge),
                                            pac = com.pt.pro.BuildConfig.APPLICATION_ID,
                                        ) {
                                            runCatching<Unit> {
                                                startActivity(this@setAsUseServices)
                                            }.getOrElse {
                                                ctx.makeToastRecSus(com.pt.pro.R.string.px, 0)
                                            }
                                        }
                                    }
                                }
                                one.selected -> pictureAdapter.selectAll()
                                one.rename -> last().rename()
                                one.favDis -> {
                                    toMutableList().asSequence().filter { i ->
                                        !favorites.contains(i.pathMedia!!)
                                    }.toMutableList().doFavorite()
                                }
                                one.unFavDis -> {
                                    toMutableList().asSequence().filter { i ->
                                        favorites.contains(i.pathMedia!!)
                                    }.toMutableList().unDoFavorite()
                                }
                                one.sendScanner -> ctx.scannerLauncherGall(last().pathMedia.toStr) {
                                    this@ParentDisPlay.startActivity(this@scannerLauncherGall)
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

    private suspend fun MutableList<MediaSack>.doFavorite() {
        withBack {
            com.pt.pro.gallery.objects.MyHash.favChange = true
            ctx.newDBGallerySus {
                insertFavMedia(0)
            }
        }
        context.nullChecker()
        withBack {
            onEachSus(context) {
                favorites.add(pathMedia.toStr)
            }
        }
    }

    private suspend fun MutableList<MediaSack>.unDoFavorite() {
        withBack {
            com.pt.pro.gallery.objects.MyHash.favChange = true
            ctx.newDBGallerySus {
                onEachSus(context) {
                    context.nullChecker()
                    deleteFavorite(pathMedia.toStr)
                    favorites.remove(pathMedia)
                }
            }
        }
    }

    private suspend fun displayAfterClipboard() {
        withMain {
            com.pt.pro.gallery.objects.MyHash.run {
                pictureAdapter.applySus {
                    deleteCopy.checkGalleryPending()
                    offMenuDisplay()
                }
            }
        }
    }

    protected var clipListener: (MutableList<MediaSack>.(currentPath: String?, mCopy: Boolean) -> Unit)?
        get() = DialogListener@{ currentPath, mCopy ->
            if (!mCopy) {
                binder?.displayBarCard?.justVisible()
                launchDef {
                    withBack {
                        this@DialogListener.toMutableList().onEachSus(context) {
                            context.nullChecker()
                            FileLate(
                                currentPath,
                                FileLate(pathMedia.toStr).name
                            ).absolutePath.letSus { toPath ->
                                runCatching {
                                    ctx.renameFile(pathMedia, toPath, sackConverterTo)
                                }.onFailure {
                                    FileLate(
                                        pathMedia.toStr
                                    ).copyFileTo(FileLate(toPath))
                                    if (isImage) {
                                        uriMedia.letSusBack { uriStr ->
                                            uriStr?.toUri ?: ctx.uriProvider(
                                                pathMedia,
                                                com.pt.pro.BuildConfig.APPLICATION_ID
                                            )
                                        }.letSusBack { uri ->
                                            cont.mediaStoreImage(this@onEachSus, toPath, uri)
                                            act.mediaBroadCast(this@onEachSus.pathMedia, toPath)
                                        }
                                    } else {
                                        this@onEachSus.uriMedia.letSusBack { uriStr ->
                                            uriStr?.toUri
                                                ?: ctx.uriProvider(
                                                    this@onEachSus.pathMedia,
                                                    com.pt.pro.BuildConfig.APPLICATION_ID
                                                )
                                        }.letSusBack { uri ->
                                            cont.mediaStoreVideo(this@onEachSus, toPath, uri)
                                            act.mediaBroadCast(this@onEachSus.pathMedia, toPath)
                                        }
                                    }
                                }
                            }
                        }
                        context.nullChecker()
                        checkAfterEdit(
                            pictureAdapter.pos.sortedByDescending { it }.toMutableList()
                        )
                        displayAfterClipboard()
                    }
                }
            } else {
                launchDef {
                    this@DialogListener.toMutableList().onEachSus(context) {
                        context.nullChecker()
                        FileLate(
                            currentPath,
                            "copy" + FileLate(this@onEachSus.pathMedia.toStr).name
                        ).letSusBack { toPath ->
                            context.nullChecker()
                            withBack {
                                runCatching {
                                    FileLate(pathMedia.toStr).copyFileTo(toPath)
                                }
                            }
                            context.nullChecker()
                            withBack {
                                runCatching {
                                    if (isImage) {
                                        cont.mediaStoreImage(
                                            this@onEachSus,
                                            toPath.absolutePath,
                                            null
                                        )
                                        act.callBroadCastInsert(toPath.absolutePath)
                                    } else {
                                        cont.mediaStoreVideo(
                                            this@onEachSus,
                                            toPath.absolutePath,
                                            null
                                        )
                                        act.callBroadCastInsert(toPath.absolutePath)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        set(value) {
            value.logProvLess()
        }

    protected var renameListener: com.pt.pro.gallery.dialogs.RenameDialog.DialogListenerOne?
        set(value) {
            value.logProvLess()
        }
        get() {
            return com.pt.pro.gallery.dialogs.RenameDialog.DialogListenerOne { fromPath, toPath, newName ->
                launchDef {
                    withBack {
                        (uriMedia ?: ctx.uriProvider(
                            pathMedia,
                            com.pt.pro.BuildConfig.APPLICATION_ID
                        ).toStr).letSusBack { uri ->
                            FileSack(
                                fileName = newName.toString(),
                                filePath = toPath.toString(),
                                fileUri = uri,
                                typeFile = if (isImage) IMAGE else VIDEO,
                                fileSize = mediaSize,
                                dateModified = System.currentTimeMillis()
                            ).letSusBack { pic ->
                                ctx.renameFile(fromPath, toPath, pic)
                            }
                        }
                    }
                    doRenameToast(isImage)
                }
            }
        }

    private suspend fun doRenameToast(isImage: Boolean) {
        withMain {
            if (isImage) {
                ctx.makeToastRec(com.pt.pro.R.string.rd, 0)
            } else {
                ctx.makeToastRec(com.pt.pro.R.string.rv, 0)
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected suspend fun android.view.ViewGroup.snakeBarForStory() {
        withMain {
            with(com.pt.pro.gallery.fasten.GalleryInflater.run { layoutInflater.context.inflaterDelete() }) {
                deleteFrame.setCardBackgroundColor(them.findAttr(com.pt.pro.R.attr.rmoBackground))
                snakeText.text = rec.getString(com.pt.pro.R.string.uz)
                confirmDelete.also {
                    it.text = rec.getString(com.pt.pro.R.string.yk)
                    it.setOnClickListener {
                        this@with.deleteFrame.goneFade(250)
                        handler?.postDelayed({
                            catchy(Unit) {
                                this@snakeBarForStory.removeView(this@with.root_)
                            }
                        }, 300)
                        launchDef {
                            justScope {
                                ctx.getOwnFile(STORY_FOLDER).fileCreator()
                            }
                            ctx.updatePrefBoolean(KEY_STORY, true)
                            scanForStatusGallery()
                        }
                    }
                }
                cancelDelete.setOnClickListener {
                    this@with.deleteFrame.goneFade(250)
                    handler?.postDelayed({
                        catchy(Unit) {
                            this@snakeBarForStory.removeView(this@with.root_)
                        }
                    }, 300)
                    launchDef {
                        ctx.updatePrefBoolean(KEY_STORY, false)
                    }
                }
                this@snakeBarForStory.addView(this@with.root_)
                this@with.deleteFrame.visibleBottom(300L)
            }
        }
    }

    private suspend fun scanForStatusGallery() {
        withDefault {
            com.pt.pro.main.odd.WorkInitializer().create(
                actBase.baseContext
            ).enqueueUniqueWork(
                "StatusWorker",
                androidx.work.ExistingWorkPolicy.REPLACE,
                androidx.work.OneTimeWorkRequest.Builder(
                    com.pt.pro.gallery.objects.StatusWorker::class.java
                ).build()
            )
        }
    }
}