package com.pt.pro.gallery.fragments

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState

abstract class ParentBrowser : com.pt.common.mutual.life.GlobalFragment<com.pt.pro.gallery.fasten.PictureBrowserFasten>(),
    com.pt.pro.gallery.interfaces.BrowserListener {

    protected var haveOption: Boolean = false
    protected var checkB: Boolean = false
    protected var marginNum: Int = 0
    protected var imagePosition: Int = 0

    protected var slideOff: Float = 0.0F
    protected var slideState: PanelState = PanelState.EXPANDED

    override var imageFragment: FragmentImage? = null
    override var vidFragment: FragmentVideo? = null

    internal var pagerHold: com.pt.pro.gallery.objects.PagerHolder? = null
    internal inline fun pagerHolder(a: com.pt.pro.gallery.objects.PagerHolder.() -> Unit) {
        pagerHold.also {
            if (it != null) {
                a.invoke(it)
            } else {
                this@ParentBrowser.requireParentFragment().childFragmentManager.popBackStack()
            }
        }
    }

    protected var pagingImages: MyAdapter? = null
    protected var imageForPager: Boolean = false
    protected var hide: Boolean = true

    protected var allImagesNative: MutableList<MediaSack>? = mutableListOf()
    override val allImages: MutableList<MediaSack>
        get() = allImagesNative ?: mutableListOf<MediaSack>().also {
            allImagesNative = it
        }

    protected var folNative: MutableList<MediaFolderSack>? = mutableListOf()
    protected inline val folderMedia: MutableList<MediaFolderSack>
        get() = folNative ?: mutableListOf<MediaFolderSack>().also {
            folNative = it
        }

    protected var favoritesNative: MutableList<String>? = mutableListOf()
    protected inline val favorites: MutableList<String>
        get() = favoritesNative ?: mutableListOf<String>().also {
            favoritesNative = it
        }

    override val runSeek: (Long) -> Unit
        get() = {
            runCatching {
                binder?.apply {
                    if (isV_N) {
                        seekBar.setProgress(it.toInt(), false)
                    } else {
                        seekBar.progress = it.toInt()
                    }
                    currentDuration.base = android.os.SystemClock.elapsedRealtime() - it
                }
            }
        }

    override val android.widget.SeekBar.OnSeekBarChangeListener.seekListenerRun: () -> Unit
        get() = {
            binder?.seekBar?.setOnSeekBarChangeListener(this@seekListenerRun)
        }

    override val setMax: (Int) -> Unit
        get() = {
            binder?.apply {
                seekBar.max = it
                videoDuration.apply {
                    text = it.toLong().findMediaDuration
                    justVisible()
                }
                currentDuration.justVisible()
                lowerVideoOptions.justVisible()
            }
        }

    internal suspend fun MediaSack.checkHidden(pagerHold: com.pt.pro.gallery.objects.PagerHolder): MediaSack {
        return if (pagerHold.isHiddenActive) {
            if (isImage) {
                cont.checkImageUri(pathMedia.toStr)
            } else {
                cont.checkVideoUri(pathMedia.toStr)
            }.let {
                if (it != null) {
                    copy(uriMedia = it.toStr)
                } else {
                    this
                }
            }
        } else {
            this
        }
    }


    override fun com.pt.pro.gallery.objects.PagerHolder.init() {
        this@ParentBrowser.pagerHold = this
        this@ParentBrowser.allImagesNative = mediaHolder
        this@ParentBrowser.imagePosition = imagePosition
        this@ParentBrowser.folderMedia.addAll(folds)
    }

    protected var slideListen: com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener? =
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


    inner class MyAdapter : androidx.viewpager2.adapter.FragmentStateAdapter(childFragmentManager, lifecycle) {

        private val pageIds = allImages.map { it.hashCode().toLong() }

        suspend fun refresh(position: Int) {
            withMain {
                allImages.removeAtIndex(position)
                notifyItemRemoved(position)
            }
        }

        override fun getItemCount(): Int = allImagesNative?.size ?: 0

        override fun getItemId(position: Int): Long = allImages.getOrNull(position)?.hashCode()?.toLong() ?: 0L

        override fun containsItem(itemId: Long): Boolean = pageIds.contains(itemId)

        override fun createFragment(position: Int): androidx.fragment.app.Fragment = run {
            if (allImages[position].isImage) {
                newFragmentImage {
                    this@newFragmentImage.mediaHolder = this@ParentBrowser.allImages[position]
                    this@newFragmentImage.browserListener = this@ParentBrowser
                    this@newFragmentImage.hideSys = this@ParentBrowser.hide
                    this@newFragmentImage
                }
            } else {
                newFragmentVideo {
                    this@newFragmentVideo.mediaHolder = this@ParentBrowser.allImages[position]
                    this@newFragmentVideo.allImages = this@ParentBrowser.allImages.toMutableList()
                    this@newFragmentVideo.marginNum = this@ParentBrowser.marginNum
                    pagerHolder { this@newFragmentVideo.marginHeight = getMarginNumberDown(this@ParentBrowser.resources.configuration) }
                    this@newFragmentVideo.browserListener = this@ParentBrowser
                    this@newFragmentVideo.hideSys = this@ParentBrowser.hide
                    this@newFragmentVideo.mediaPlay = this@ParentBrowser.mediaPlay
                    this@newFragmentVideo
                }
            }
        }
    }

    internal inline val com.pt.pro.gallery.objects.PagerHolder.getMarginNumber: (android.content.res.Configuration) -> Int
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

    internal inline val com.pt.pro.gallery.objects.PagerHolder.getMarginNumberDown: (android.content.res.Configuration) -> Int
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

    protected var clipboardListener: (MutableList<MediaSack>.(currentPath: String?, mCopy: Boolean) -> Unit)?
        get() = { currentPath, mCopy ->
            launchDef {
                last().doClipboard(currentPath, mCopy)
            }
        }
        set(value) {
            value.logProvLess()
        }

    @com.pt.common.global.WorkerAnn
    private suspend fun MediaSack.doClipboard(currentPath: String?, mCopy: Boolean) {
        withBack {
            if (!mCopy) {
                FileLate(
                    currentPath,
                    FileLate(pathMedia.toStr).name
                ).absolutePath.let { toPath ->
                    runCatching {
                        ctx.renameFile(
                            pathMedia,
                            toPath,
                            sackConverterTo
                        ).let {
                            if (it) {
                                viewPagerSkip()
                            }
                        }
                    }.onFailure {
                        FileLate(pathMedia.toStr).let {
                            uriMedia.let { uriStr ->
                                uriStr?.toUri ?: ctx.uriProvider(
                                    pathMedia,
                                    com.pt.pro.BuildConfig.APPLICATION_ID
                                )
                            }.let { uri ->
                                if (it.copyFileTo(FileLate(toPath))) {
                                    if (isImage) {
                                        cont.mediaStoreImage(this@doClipboard, toPath, uri)
                                        ctx.mediaBroadCast(pathMedia, toPath)
                                    } else {
                                        cont.mediaStoreVideo(this@doClipboard, toPath, uri)
                                        ctx.mediaBroadCast(pathMedia, toPath)
                                    }
                                    viewPagerSkip()
                                }
                            }

                        }
                    }
                }
            } else {
                FileLate(
                    currentPath,
                    "copy" + FileLate(pathMedia.toStr).name
                ).let {
                    runCatching {
                        if (FileLate(pathMedia.toStr).copyFileTo(it)) {
                            if (isImage) {
                                cont.mediaStoreImage(
                                    this@doClipboard,
                                    it.absolutePath,
                                    null
                                )
                                ctx.callBroadCastInsert(it.absolutePath)
                            } else {
                                cont.mediaStoreVideo(
                                    this@doClipboard,
                                    it.absolutePath,
                                    null
                                )
                                ctx.callBroadCastInsert(it.absolutePath)
                            }
                        }
                    }
                }
            }
        }
    }

    protected var deleteListener: com.pt.pro.gallery.dialogs.PopForDelete.DeleteListener?
        set(value) {
            value.logProvLess()
        }
        get() {
            return com.pt.pro.gallery.dialogs.PopForDelete.DeleteListener {
                launchDef {
                    last().doDelete()
                    context.nullChecker()
                    viewPagerSkip()
                }
            }
        }

    @com.pt.common.global.WorkerAnn
    private suspend fun MediaSack.doDelete() {
        withBack {
            this@doDelete.uriMedia.let { uriStr ->
                uriStr?.toUri ?: ctx.uriProvider(
                    this@doDelete.pathMedia,
                    com.pt.pro.BuildConfig.APPLICATION_ID
                )
            }.let { uri ->
                (if (this@doDelete.isImage) IMAGE else VIDEO).let { t ->
                    ctx.fileDeleter(this@doDelete.pathMedia.toStr, t, uri)
                }
            }
        }
    }


    protected var renameListener: com.pt.pro.gallery.dialogs.RenameDialog.DialogListenerOne?
        set(value) {
            value.logProvLess()
        }
        get() {
            return com.pt.pro.gallery.dialogs.RenameDialog.DialogListenerOne { fromPath, toPath, newName ->
                doRename(fromPath = fromPath, toPath = toPath, newName = newName)
            }
        }

    @com.pt.common.global.WorkerAnn
    override fun MediaSack.doRename(
        fromPath: String?,
        toPath: String?,
        newName: String?,
    ) {
        launchDef {
            withBack {
                (uriMedia ?: ctx.uriProvider(
                    pathMedia,
                    com.pt.pro.BuildConfig.APPLICATION_ID
                ).toStr).let {
                    FileSack(
                        fileName = newName.toString(),
                        filePath = toPath.toString(),
                        fileUri = it,
                        typeFile = if (isImage) IMAGE else VIDEO,
                        fileSize = mediaSize,
                        dateModified = System.currentTimeMillis()
                    ).let { pic ->
                        ctx.renameFile(fromPath, toPath, pic)
                    }
                }
            }
            if (isImage) {
                ctx.makeToastRecSus(com.pt.pro.R.string.rd, 0)
            } else {
                ctx.makeToastRecSus(com.pt.pro.R.string.rv, 0)
            }
        }
    }

    protected suspend fun videoPop() {
        withMain {
            vidFragment?.runFloatingVideo?.one?.invoke()
        }
    }

    protected var activityLauncher: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {

        }

    @android.annotation.SuppressLint("SourceLockedOrientationActivity")
    protected suspend fun doRotateScreen() {
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

    protected suspend fun doEditLauncher() {
        withBack {
            ctx.runCatching {
                allImages[imagePosition].let { (_, pathMedia, uriMedia, _, _, _, _, _, _, _, _) ->
                    uriMedia.let { uriStr ->
                        uriStr?.toUri ?: ctx.uriProviderNormal(
                            pathMedia,
                            com.pt.pro.BuildConfig.APPLICATION_ID
                        )
                    }.let { uri ->
                        ctx.editLauncher(
                            uri,
                            pathMedia.toStr,
                        ) {
                            activityLauncher?.launch(this)
                        }
                    }
                }
            }.onFailure {
                ctx.makeToastRecSus(com.pt.pro.R.string.op, 1)
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    protected suspend fun MediaSack.doHideHide() {
        withBack {
            FileLate(pathMedia ?: return@withBack).apply {
                if (!this@apply.name.checkHiddenMedia) {
                    val new = FileLate((this@apply.parent?.toStr + "/." + this@apply.name))
                    FileLate(
                        pathMedia.toStr
                    ).copyFileTo(
                        new
                    ).letSusBack {
                        ctx.fileDeleter(
                            pathMedia.toStr,
                            if (isImage) IMAGE else VIDEO,
                            uriMedia?.toUri
                        )
                        justCoroutine {
                            pathMedia = new.absolutePath
                        }
                    }
                } else if (this@apply.name.checkHiddenMedia) {
                    (this@apply.parent?.toStr + "/" + this@apply.name.removeRange(
                        0,
                        1
                    )).let { pat ->
                        FileLate(pathMedia.toStr).copyFileTo(FileLate(pat)).letSusBack {
                            ctx.fileDeleter(
                                pathMedia.toStr,
                                if (isImage) IMAGE else VIDEO,
                                uriMedia?.toUri
                            )
                            if (isImage) {
                                cont.mediaStoreImage(
                                    this@doHideHide,
                                    pat,
                                    null
                                )
                            } else {
                                cont.mediaStoreVideo(
                                    this@doHideHide,
                                    pat,
                                    null
                                )
                            }
                            ctx.mediaBroadCast(pathMedia, pat)
                            justCoroutine {
                                pathMedia = pat
                            }
                        }
                    }
                } else {
                    pagerHolder {
                        if (checkHidden(this@pagerHolder).uriMedia == null && isImage) {
                            cont.mediaStoreImage(this@doHideHide, this@doHideHide.pathMedia, null)
                        } else if (checkHidden(this@pagerHolder).uriMedia == null && !isImage) {
                            cont.mediaStoreVideo(this@doHideHide, this@doHideHide.pathMedia, null)
                        }
                    }
                }
            }
        }
    }


    protected var mediaPlay: androidx.media3.exoplayer.ExoPlayer? = null

    protected suspend fun intiPlayer() {
        withMain {
            androidx.media3.exoplayer.ExoPlayer.Builder(ctx).apply {
                //inti()
                setAudioAttributes(musicAudioAttr, true)
                setHandleAudioBecomingNoisy(true)
            }.build().alsoSus {
                it.playWhenReady = false
            }.applySus {
                context.nullChecker()
                justCoroutineMain {
                    mediaPlay = this@applySus
                    repeatMode = androidx.media3.common.Player.REPEAT_MODE_OFF
                }
                context.nullChecker()
            }
        }
    }

    /*@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun androidx.media3.exoplayer.ExoPlayer.Builder.inti() {
        catchy(Unit) {
            setPauseAtEndOfMediaItems(false)
            setUseLazyPreparation(false)
            setUsePlatformDiagnostics(false)
        }
    }*/


}