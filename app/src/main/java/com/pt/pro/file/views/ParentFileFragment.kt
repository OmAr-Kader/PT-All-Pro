package com.pt.pro.file.views

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*

abstract class ParentFileFragment : com.pt.common.mutual.life.GlobalFragment<com.pt.pro.file.fasten.FragmentFileFasten>(),
    com.pt.pro.file.interfaces.ItemFileListener {

    protected var includeOptionsNative: com.pt.pro.file.fasten.FileOptionsFasten? = null

    internal inline val com.pt.pro.file.fasten.FragmentFileFasten.includeOptions: com.pt.pro.file.fasten.FileOptionsFasten
        get() {
            return includeOptionsNative ?: com.pt.pro.file.fasten.FileInflater.run {
                ctx.let {
                    it.inflaterFileOptions(d32 = it.findPixel(32), d4 = it.findPixel(4))
                }
            }.also {
                includeOptionsStub.addView(it.root_)
                includeOptionsNative = it
            }
        }


    protected var includeShareNative: com.pt.pro.file.fasten.FileShareFasten? = null

    internal inline val com.pt.pro.file.fasten.FragmentFileFasten.includeShare: com.pt.pro.file.fasten.FileShareFasten
        get() {
            return includeShareNative
                ?: com.pt.pro.file.fasten.FileInflater.run { ctx.inflaterFileShare() }.also {
                    includeShareSub.addView(it.root_)
                    includeShareNative = it
                }
        }


    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    protected inline val folderAdapter: FolderAdapter get() = folderAda!!
    protected inline val favAdapter: FavoriteAdapter get() = favAda!!
    protected var folderAda: FolderAdapter? = null
    protected var favAda: FavoriteAdapter? = null

    protected var jobNativeFile: kotlinx.coroutines.Job? = null

    private inline val pcm: android.content.pm.PackageManager
        get() = ctx.packageManager

    @Volatile
    protected var marginWidth: Int = 0

    @Volatile
    protected var marginHeight: Int = 0

    @Volatile
    protected var isDoInLand: Boolean = false

    @Volatile
    protected var hiddenActive: Boolean = false

    internal inline val orderFolderBy: suspend () -> String
        get() = {
            ctx.findStringPreference(FO_B_SORT, com.pt.common.BuildConfig.BY_NAME_ASC)
        }

    internal inline val orderFileBy: suspend () -> String
        get() = {
            ctx.findStringPreference(FI_SORT_B, com.pt.common.BuildConfig.BY_DATE_DESC)
        }


    internal inline val isCato: suspend () -> Boolean
        get() = {
            ctx.findBooleanPreference(FILE_MODE, true)
        }

    internal inline val isNowFolder: suspend () -> Boolean
        get() = {
            ctx.findBooleanPreference(MAN_WHICH, true)
        }

    protected var pendHashNative: MutableList<FileSack>? = mutableListOf()
    protected inline val pendHash: MutableList<FileSack>
        get() = pendHashNative ?: mutableListOf<FileSack>().also {
            pendHashNative = it
        }

    protected var fileCLickNative: MutableList<FileUpdateSack>? = mutableListOf()
    protected inline val fileCLick: MutableList<FileUpdateSack>
        get() = fileCLickNative ?: mutableListOf<FileUpdateSack>().also {
            fileCLickNative = it
        }

    protected var allMediaNative: MutableList<FileSack>? = mutableListOf()
    protected inline val allMedia: MutableList<FileSack> get() = allMediaNative ?: mutableListOf<FileSack>().also { allMediaNative = it }

    protected var searchListNative: Sequence<FileSack>? = emptySequence()
    protected inline val searchList: Sequence<FileSack>
        get() = searchListNative ?: emptySequence<FileSack>().also { searchListNative = it }

    protected var clipBoardFilesNative: MutableList<FileSack>? = mutableListOf()
    protected inline val clipBoardFiles: MutableList<FileSack>
        get() = clipBoardFilesNative ?: mutableListOf<FileSack>().also { clipBoardFilesNative = it }

    protected var onlyFolderNative: MutableList<FileSack>? = mutableListOf()
    protected inline val onlyFolder: MutableList<FileSack>
        get() = onlyFolderNative ?: mutableListOf<FileSack>().also { onlyFolderNative = it }

    protected var onlyMediaNative: MutableList<FileSack>? = mutableListOf()
    protected inline val onlyMedia: MutableList<FileSack> get() = onlyMediaNative ?: mutableListOf<FileSack>().also { onlyMediaNative = it }

    protected var favoritesFilesNative: MutableList<String>? = mutableListOf()
    protected inline val favoritesFiles: MutableList<String>
        get() = favoritesFilesNative ?: mutableListOf<String>().also {
            favoritesFilesNative = it
        }

    private var musicHolderNative: MutableList<MusicSack>? = mutableListOf()
    private inline val musicHolder: MutableList<MusicSack>
        get() = musicHolderNative ?: mutableListOf<MusicSack>().also { musicHolderNative = it }

    internal var virtualFilesNative: MutableList<String>? = mutableListOf()
    internal inline val virtualFiles: MutableList<String> get() = virtualFilesNative ?: mutableListOf<String>().also { virtualFilesNative = it }

    private var virtualFilesNameNative: MutableList<String>? = mutableListOf()
    private inline val virtualFilesName: MutableList<String>
        get() = virtualFilesNameNative ?: mutableListOf<String>().also {
            virtualFilesNameNative = it
        }

    @Volatile
    private var musicPos: Int = 0

    protected var ratioPlus: Float = 1F
    protected var endScroll: Float = 0.0F
    protected var endHeight: Float = 0.0F
    protected var endRatio: Float = 0.0F
    protected var start: Float = 0.0F

    private var observing: android.database.ContentObserver? = null

    private val runRef: DSackT<() -> Unit, Int>
        get() = toCatchSack(22) {
            if (isPaused || folderAdapter.isActionMode) {
                haveNewUpdate = true
            } else {
                isHaveRefresh = false
                askForRefresh()
            }
        }


    @Volatile
    private var isHaveRefresh = false

    @Volatile
    private var isPaused = false

    @Volatile
    protected var haveNewUpdate: Boolean = false

    @com.pt.common.global.MainAnn
    override fun onResume() {
        if (haveNewUpdate) {
            askForRefresh()
        }
        super.onResume()
        isHaveRefresh = false
        haveNewUpdate = false
        isPaused = false
    }

    @com.pt.common.global.MainAnn
    override fun onPause() {
        isPaused = true
        super.onPause()
    }

    protected suspend fun intiFirstResume() {
        withMain {
            childFragmentManager.addOnBackStackChangedListener {
                fraMangerHaveChanges()
            }
            foundObserver {
                if (!isHaveRefresh) {
                    isHaveRefresh = true
                    runRef.rKTSack(500L).postBackAfter()
                }
            }.also {
                observing = it
                cont.registerContentObserver(
                    android.provider.MediaStore.Files.getContentUri(com.pt.common.BuildConfig.DATA_EXTERNAL),
                    true,
                    it
                )
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
                cont.registerContentObserver(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    true,
                    it
                )
            }
            act.intent?.data?.let(::checkIfHaveIntent)
        }
    }

    private fun checkIfHaveIntent(d: android.net.Uri) {
        act.intent?.apply {
            if (isPDFIntent) {
                launchDef {
                    withBack {
                        ctx.getFilePathFromURI(d).letSusBack(::launchPdfViewer)
                    }
                }
            } else if (isTxtIntent) {
                launchDef {
                    withBack {
                        ctx.getFilePathFromURI(d).letSusBack(::launchTextViewer)
                    }
                }
            } else if (isZipIntent) {
                launchDef {
                    withBack {
                        ctx.getFilePathFromURI(d).runSusBack {
                            FileLate(this@runSusBack).letSusBack { f ->
                                if (f.exists()) {
                                    FileSack(
                                        fileName = f.fetchFileName,
                                        filePath = f.absolutePath,
                                        fileUri = null,
                                        fileSize = 0,
                                        typeFile = FOLDER,
                                        dateModified = f.lastModified(),
                                    )
                                } else null
                            }
                        }?.forZipFiles() ?: ctx.makeToastRecSus(com.pt.pro.R.string.xe, 0)
                    }
                }
            }
        }
    }

    protected fun askForRefresh() {
        if (context != null) {
            jobNativeFile = launchMain {
                if (binder?.fileBarCard?.isGon == true &&
                    !fileCLickNative.isNullOrEmpty() &&
                    binder?.searchEdit?.isGon == true
                ) {
                    fileCLick.lastOrNull()?.doRefresh()
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.deleteChecker() {
        withBack {
            this@deleteChecker.onEachIndexedSusBack(context) { _, it ->
                if (it.typeFile == FOLDER) {
                    val new: MutableList<FileSack> = mutableListOf()
                    (FileLate(
                        it.filePath
                    ).listFiles()?.toMutableList() ?: return@onEachIndexedSusBack).onEachSusBack(
                        context
                    ) {
                        if (FileLate(this@onEachSusBack.absolutePath).isDirectory) {
                            FOLDER
                        } else {
                            this@onEachSusBack.extension.trim().findType
                        }.letSusBack { t ->
                            new.add(
                                FileSack(
                                    this@onEachSusBack.name,
                                    this@onEachSusBack.absolutePath,
                                    null,
                                    0,
                                    t,
                                    0
                                )
                            )
                        }
                    }
                    onlyFolder.add(it)
                    new.deleteChecker()
                } else {
                    onlyMedia.add(it)
                }
            }
        }
    }

    protected suspend fun FileSack.openAs(newP: android.net.Uri) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                context.nullChecker()
                binder?.refresh()
                myShareTwo(
                    copy(fileUri = newP.toStr),
                    android.content.Intent.ACTION_VIEW
                )
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.rename() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_RENAME) == null) {
                newRenameDialog {
                    fileHolder = this@rename.getOrNull(0) ?: return@withMainNormal
                    mediaHold = null
                    oneListener = null
                    twoListener = renameListener
                    this@newRenameDialog
                }.show(childFragmentManager, DIALOG_RENAME)
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.properties() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_PROPERTY) == null) {
                newPopForPropertyFile {
                    this@properties.toMutableList().pushList(false)
                    this@newPopForPropertyFile
                }.show(childFragmentManager, DIALOG_PROPERTY)
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    override fun openMusicPlaylist() {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                context?.newDBPlaylist {
                    getAllSongsDefault()
                }?.applySusBack {
                    context.nullChecker()
                    withBack {
                        if (isNotEmpty()) {
                            launchMusic(-1, 222)
                        }
                    }
                }
            }
        }
    }


    protected suspend fun MutableList<FileSack>.createDialog(isFromVir: Boolean) {
        if (!isFromVir && !FileLate(fileCLick.last().pathScroll).isDirectory) {
            ctx.makeToastRecSus(com.pt.pro.R.string.nr, 1)
        } else {
            withMainNormal {
                if (childFragmentManager.findFragmentByTag(DIALOG_CREATE) == null) {
                    newPopForCreate {
                        this@createDialog.pushList(isFromVir)
                        this@ParentFileFragment.pushListener()
                        this@newPopForCreate
                    }.show(childFragmentManager, DIALOG_CREATE)
                }
            }
        }
    }

    protected suspend fun MutableList<FileSack>.updateAllMedia() {
        withBack {
            allMediaNative = this@updateAllMedia
        }
    }

    protected suspend fun MutableList<FileSack>.doApplyClipboard(root: String, a: () -> Unit) {
        withBack {
            this@doApplyClipboard.onEachSusBack(context) {
                if (context == null) return@onEachSusBack
                FileLate(this@onEachSusBack.filePath).letSusBack { coFil ->
                    if (root != coFil.parent) {
                        (root + FileLate.separator + coFil.name).letSusBack<String, Unit> { toP ->
                            if (!this@onEachSusBack.isCopy) {
                                if (this@onEachSusBack.typeFile == FOLDER) {
                                    runCatching {
                                        context?.moveFolderTo(
                                            FileLate(this@onEachSusBack.filePath),
                                            FileLate(toP),
                                            this@onEachSusBack
                                        )
                                        return@runCatching
                                    }
                                } else {
                                    withBack {
                                        runCatching {
                                            context?.renameFile(
                                                filePath,
                                                toP,
                                                this@onEachSusBack
                                            )
                                            return@runCatching
                                        }.onFailure {
                                            runCatching {
                                                FileLate(filePath).copyFileTo(
                                                    FileLate(toP)
                                                )
                                                cont.mediaStoreFile(
                                                    this@onEachSusBack,
                                                    toP,
                                                    filePath
                                                )
                                                return@runCatching
                                            }
                                        }
                                    }
                                }
                            } else {
                                runCatching {
                                    if (this@onEachSusBack.typeFile == FOLDER) {
                                        context?.copyTryFolderTo(
                                            FileLate(this@onEachSusBack.filePath),
                                            FileLate(toP)
                                        )
                                    } else {
                                        FileLate(
                                            this@onEachSusBack.filePath
                                        ).copyFileTo(FileLate(toP))
                                        cont.mediaStoreFile(this@onEachSusBack, toP, null)
                                    }
                                    return@runCatching
                                }
                            }
                        }
                    } else {
                        return@letSusBack
                    }
                }
            }
            context.nullChecker()
            justScope {
                a.invoke()
            }
        }
    }

    protected suspend fun loadVirtual(name: String) {
        withBackDef(mutableListOf()) {
            ctx.newDBFile {
                getAllVFavFiles(name)
            }
        }.alsoSusBack {
            context.nullChecker()
            it.displayForVirtual(false)
            it.updateAllMedia()
        }
    }


    @com.pt.common.global.WorkerAnn
    protected suspend inline fun MutableList<FileSack>.checkLaunchPending(
        crossinline f: suspend MutableList<FileSack>.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            toMutableList().asSequence().distinct().filter {
                FileLate(it.filePath).exists()
            }.toMutableList()
        }.alsoSusBack(f)
    }

    @com.pt.common.global.WorkerAnn
    internal suspend inline fun loadAllCato(
        cato: Int,
        crossinline f: suspend MutableList<FileSack>.() -> Unit,
    ) {
        cont.findCategory(cato).toMutableList().asSequence().letSusBack {
            return@letSusBack mutableListOf<FileSack>().applySusBack {
                it.onlyFolders().orderFileAllLoaders(orderFolderBy()).runSusBack {
                    this@applySusBack.addAll(this)
                }
                it.onlyFiles().orderFileAllLoaders(orderFileBy()).runSusBack {
                    this@applySusBack.addAll(this)
                }
            }
        }.letSusBack(f)
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.launchMedia(curr: FileSack) {
        if (isEmpty()) return
        this@launchMedia.recreateForGallery().letSusBack { media ->
            cont.allFoldersLoader(
                com.pt.common.BuildConfig.BY_DATE_FOLDER_DESC
            ).letSusBack { folds ->
                withMainNormal {
                    com.pt.pro.gallery.objects.PagerHolder(
                        mediaHolder = media,
                        folds = folds,
                        imagePosition = this@launchMedia.indexOf(curr),
                        pending = false,
                        main = true,
                        isHiddenActive = true,
                        isFileManager = true,
                        isDoInLand = isDoInLand,
                        margeWidth = marginWidth,
                        margeHeight = marginHeight
                    ).alsoSus {
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
            }
        }
    }

    protected suspend fun launchPdfViewer(path: String) {
        withMainNormal {
            newBrowserFileFragment {
                DSack(
                    this@ParentFileFragment.marginWidth,
                    this@ParentFileFragment.marginHeight,
                    this@ParentFileFragment.isDoInLand
                ).setIntiMargin()
                path.setFileUriAndPath { str, last ->
                    if (str != null) {
                        launchDef {
                            ctx.updatePrefInt(str, last)
                        }
                    }
                }
                return@newBrowserFileFragment this@newBrowserFileFragment
            }.alsoSus {
                childFragmentManager.doLaunchGalleryImage(PDF_BROWSER_FRAGMENT) {
                    add(binding.root_.id, it, PDF_BROWSER_FRAGMENT)
                }
            }
        }
    }

    protected suspend fun launchTextViewer(filePath: String) {
        withMainNormal {
            newFragmentTextViewer {
                DSack(
                    this@ParentFileFragment.marginWidth,
                    this@ParentFileFragment.marginHeight,
                    this@ParentFileFragment.isDoInLand
                ).setIntiMargin()
                filePath.setFileUriAndPath()
                return@newFragmentTextViewer this@newFragmentTextViewer
            }.alsoSus {
                childFragmentManager.doLaunchGalleryImage(FRAGMENT_TEXT) {
                    add(binder?.root_?.id ?: return@doLaunchGalleryImage, it, FRAGMENT_TEXT)
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.launchMusicPopUp(holder: FileSack) {
        withBack {
            toMutableList().asSequence().filter {
                it.typeFile == AUDIO
            }.letSusBack { itA ->
                mutableListOf<MusicSack>().applySusBack {
                    itA.toMutableList().onEachSusBack(context) {
                        MusicSack(
                            title = this@onEachSusBack.fileName,
                            pathAudio = this@onEachSusBack.filePath
                        ).letSusBack { itMu ->
                            this@applySusBack.add(itMu)
                        }
                    }
                }.letSusBack { strList ->
                    withMainNormal {
                        if (childFragmentManager.findFragmentByTag(DIALOG_MUSIC_OPT) == null) {
                            newChooseMusicOption {
                                strList.pushMusic(itA.indexOf(holder))
                                this@ParentFileFragment.pushListener()
                                this@newChooseMusicOption
                            }.show(childFragmentManager, DIALOG_MUSIC_OPT)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun MutableList<MusicSack>.launchMusic(pos: Int, o: Int) {
        if (this@launchMusic.isEmpty()) return
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                doLaunchMusic(pos, o)
            }
        }
    }

    private suspend fun MutableList<MusicSack>.doLaunchMusic(pos: Int, o: Int) {
        if (o == 111) {
            withMainNormal {
                if (childFragmentManager.findFragmentByTag(DIALOG_MUSIC) == null) {
                    newPopUpForMusic {
                        this@ParentFileFragment.pushListener()
                        this@doLaunchMusic.toMutableList().pushMusic(pos)
                        this@newPopUpForMusic
                    }.show(childFragmentManager, DIALOG_MUSIC)
                }
            }
        } else {
            withBack {
                withSusBack(ctx) {
                    if (isServiceNotRunning(
                            com.pt.pro.musical.music.MusicPlayer::class.java.canonicalName
                        )
                    ) {
                        if (canFloat) {
                            newIntentSus(com.pt.pro.musical.music.MusicPlayer::class.java) {
                                flags = FLAGS
                                action = PLAY_MUSIC
                                putExtra(KEY_ORDER, "tillITHurts")
                                putExtra(ALL_SONGS, this@doLaunchMusic as ArrayList)
                                putExtra(POPUP_POS, pos)
                                this@newIntentSus
                            }.alsoSusBack {
                                androidx.core.content.ContextCompat.startForegroundService(
                                    this@withSusBack,
                                    it
                                )
                                homeLauncher {
                                    if (context != null) {
                                        this@ParentFileFragment.startActivity(this)
                                    }
                                }
                            }
                        } else {
                            if (isV_M) {
                                checkDrawOverlayPermission(pos)
                            }
                        }
                    } else {
                        newIntentSus(com.pt.pro.musical.music.MusicPlayer::class.java) {
                            action = UPDATE_MUSIC
                            putExtra(KEY_ORDER, "tillITHurts")
                            putExtra(ALL_SONGS, this@doLaunchMusic as ArrayList)
                            putExtra(POPUP_POS, pos)
                            this@newIntentSus
                        }.alsoSusBack {
                            android.app.PendingIntent.getService(
                                this@withSusBack,
                                0,
                                it,
                                PEND_FLAG
                            ).send()
                            homeLauncher {
                                if (context != null) {
                                    this@ParentFileFragment.startActivity(this)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    protected suspend fun android.content.Context.loadCatoFiles(): MutableList<FileSack> =
        justCoroutine {
            withBackDef(mutableListOf()) {
                newDBPlaylist {
                    getAllSongsDefault()
                }
            }.letSusBack {
                context.nullChecker()
                withBackDef(mutableListOf()) {
                    if (it.isEmpty()) {
                        getStorageDirectories().getMusicIcon(0, null)
                    } else {
                        findIntegerPreference(MUSIC_POS_KEY, 0).letSusBack { i ->
                            getStorageDirectories().getMusicIcon(it.size, it.getOrNull(i)?.pathAudio)
                        }
                    }
                }
            }
        }


    @com.pt.common.global.WorkerAnn
    internal suspend inline fun fetchTargetFiles(
        newPath: String,
        crossinline f: suspend MutableList<FileSack>.() -> Unit,
    ) {
        context.nullChecker()
        withBackDef(mutableListOf()) {
            return@withBackDef if (hiddenActive) {
                cont.getMangerFiles(FileLate(newPath)).checkIfAndroid(newPath)
            } else {
                cont.allFilesLoader(
                    newPath
                ).checkIfAndroid(newPath)
            }.toMutableList().asSequence().letSusBack {
                if (context == null) return@letSusBack mutableListOf()
                context.nullChecker()
                return@letSusBack mutableListOf<FileSack>().applySusBack {
                    it.onlyFolders().orderFileAllLoaders(orderFolderBy()).letSusBack(::addAll)
                    it.onlyFiles().orderFileAllLoaders(orderFileBy()).letSusBack(::addAll)
                }
            }
        }.alsoSusBack(f)
    }

    private suspend fun MutableList<FileSack>.checkIfAndroid(
        newPath: String
    ): MutableList<FileSack> = justCoroutine {
        if (!isV_R) {
            this@checkIfAndroid
        } else {
            withBackDef(this@checkIfAndroid) {
                when (newPath) {
                    com.pt.common.BuildConfig.ROOT_ANDROID -> {
                        this@checkIfAndroid.add(ctx.androidDataFile)
                        this@checkIfAndroid
                    }
                    com.pt.common.BuildConfig.ROOT_ANDROID_DATA -> {
                        binder?.forSnakes?.apply {
                            snakeBarAndroid(childCountSus())
                        }
                        this@checkIfAndroid
                    }
                    else -> {
                        this@checkIfAndroid
                    }
                }
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    internal suspend inline fun com.pt.common.objects.FileUpdate.loadForRefresh(
        crossinline f: suspend MutableList<FileSack>.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            return@withBackDef if (hiddenActive) {
                cont.getMangerFiles(FileLate(pathScroll)).orderFileAllLoaders(
                    orderFolderBy()
                )
            } else {
                cont.allFilesLoader(
                    pathScroll
                ).orderFileAllLoaders(orderFolderBy())
            }
        }.alsoSusBack(f)
    }

    protected suspend fun FileSack.shareUnknownFile(newP: android.net.Uri) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                context.nullChecker()
                binder?.refresh()
                myShareTwo(
                    this@shareUnknownFile.copy(fileUri = newP.toStr),
                    android.content.Intent.ACTION_VIEW
                )
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.doCheckNonFolder(): Boolean = justCoroutine {
        withBackDef(true) {
            this@doCheckNonFolder.none {
                it.typeFile == FOLDER
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun FileSack.wholeFiles() {
        if (typeFile == APK) {
            installApk(
                uri = context?.getFileUri(FileLate(filePath).path ?: return, FOLDER, com.pt.pro.BuildConfig.APPLICATION_ID)
                    ?: return,
            ) {
                kotlin.runCatching {
                    this@ParentFileFragment.startActivity(this)
                }.onFailure {
                    ctx.makeToastRecSus(com.pt.pro.R.string.px, 0)
                }
            }
        } else {
            doWholeFilesView(
                (FileLate(filePath).getFileType ?: "*/*"),
                context?.getFileUri(filePath, typeFile, com.pt.pro.BuildConfig.APPLICATION_ID)
                    ?: return
            )
        }
    }

    private suspend fun doWholeFilesView(finalType: String, newP: android.net.Uri) {
        viewFiles30(
            uri = newP,
            typ = finalType
        ) {
            runCatching<Unit> {
                this@ParentFileFragment.startActivity(this)
            }.onFailure {
                ctx.makeToastRecSus(com.pt.pro.R.string.px, 0)
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.addToVir() {
        withBackDef(mutableListOf()) {
            ctx.newDBFile {
                mutableListOf<String>().applySusBack {
                    addAll(getAllVIrsFull())
                }
            }
        }.letSusBack { itA ->
            withMainNormal {
                if (childFragmentManager.findFragmentByTag(DIALOG_VIRTUAL) == null) {
                    newPopForVirtual {
                        itA.pushStrings()
                        this@addToVir.toMutableList().pushList(true)
                        this@ParentFileFragment.pushListener()
                        this@newPopForVirtual
                    }.show(childFragmentManager, DIALOG_VIRTUAL)
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.hideFiles() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_HIDDEN) == null) {
                newPopForHideFile {
                    this@hideFiles.toMutableList().pushList(true)
                    this@ParentFileFragment.pushListener()
                    this@newPopForHideFile
                }.show(childFragmentManager, DIALOG_HIDDEN)
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<FileSack>.doAddToClipBoard(isCopy: Boolean) {
        withBack {
            withSusBack<MutableList<FileSack>, Unit>(clipBoardFiles) {
                this@doAddToClipBoard.onEachSusBack(context) {
                    context.nullChecker()
                    this@onEachSusBack.copy(isCopy = isCopy).letSus { edit ->
                        this@onEachSusBack.copy(isCopy = true).letSus { editC ->
                            this@onEachSusBack.copy(isCopy = false).letSus { editM ->
                                when {
                                    this@withSusBack.contains(editC) -> {
                                        this@withSusBack[this@withSusBack.indexOf(editC)] = edit
                                    }
                                    this@withSusBack.contains(editM) -> {
                                        this@withSusBack[this@withSusBack.indexOf(editM)] = edit
                                    }
                                    else -> {
                                        this@withSusBack.add(edit)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected fun FileSack.addPending(): Boolean {
        return kotlin.runCatching {
            if (!pendHash.contains(this)) {
                pendHash.add(this)
            } else {
                return false
            }
            true
        }.getOrDefault(false)
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun android.content.Context.dbFavVir(
    ): MutableList<FileSack> =
        withBackDef(mutableListOf()) {
            return@withBackDef newDBFile {
                mutableListOf<FileSack>().apply {
                    addAll(getAllVIrs())
                    addAll(getAllVFavFiles(null))
                }
            }
        }


    protected suspend fun MutableList<FileSack>.doAddForVir(itF: String) {
        withBack {
            ctx.newDBFile {
                this@doAddForVir.onEachSusBack(context) {
                    if (context == null) return@onEachSusBack
                    this@onEachSusBack.insertFavFile(itF).letSusBack { a ->
                        if (a != -1L) {
                            updateVirtual(itF, 1)
                        }
                    }
                }
            }
        }
    }

    protected suspend fun FileSack.shareOption(finalType: String, newP: android.net.Uri) {
        if (typeFile == UNKNOWN) {
            pushJob { j ->
                launchDef {
                    j?.checkIfDone()
                    context.nullChecker()
                    binder?.refresh()
                    myShareTwo(
                        this@shareOption.copy(fileUri = newP.toStr),
                        android.content.Intent.ACTION_SEND
                    )
                }
            }
        } else {
            context?.shareFiles30(
                deleteCopy = folderAdapter.deleteCopy,
                typ = finalType
            ) {
                runCatching<Unit> {
                    this@ParentFileFragment.startActivity(this@shareFiles30)
                }.onFailure {
                    ctx.makeToastRecSus(com.pt.pro.R.string.px, 0)
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.removeToVir() {
        withBack {
            filterForRemovingVir(
                virtualFiles = virtualFiles,
                virtualFilesName = virtualFilesName
            ).letSusBack { (arrHolders, arrStr) ->
                arrStr.distinct().toMutableList().letSusBack { itA ->
                    withMainNormal {
                        if (childFragmentManager.findFragmentByTag(DIALOG_VIRTUAL) == null) {
                            newPopForVirtual {
                                itA.pushStrings()
                                arrHolders.pushList(false)
                                this@ParentFileFragment.pushListener()
                                this@newPopForVirtual
                            }.show(childFragmentManager, DIALOG_VIRTUAL)
                        }
                    }
                }
            }
        }
    }

    protected suspend fun MutableList<FileSack>.doRemoveFromVir() {
        withBack {
            ctx.newDBFile {
                this@doRemoveFromVir.onEachSusBack(context) {
                    if (context == null) return@onEachSusBack
                    this@onEachSusBack.virName.toStr.letSusBack { v ->
                        updateVirtual(v, -1)
                        deleteMedia(this@onEachSusBack.filePath, v)
                    }
                }
            }
        }
    }

    internal suspend fun android.content.Context.dbVirFiles() {
        withBack {
            favoritesFiles.clear()
            virtualFiles.clear()
            virtualFilesName.clear()
        }
        withBack {
            newDBFile {
                favoritesFiles.addAll(getAllFavPath())
                getAllVirName().runSusBack {
                    virtualFiles.addAll(two)
                    virtualFilesName.addAll(three)
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.actionDoFavorite() {
        withBack {
            toMutableList().asSequence().filter {
                !favoritesFiles.contains(it.filePath)
            }.letSusBack { fav ->
                context?.newDBFile {
                    fav.toMutableList().insertFavFile(null)
                }?.also {
                    withBack {
                        fav.toMutableList().onEachSusBack(context) {
                            favoritesFiles.add(this@onEachSusBack.filePath)
                        }
                    }
                }
            }
        }
    }

    protected suspend fun MutableList<FileSack>.pending() {
        doPending()
        displayPending(pendHash.size)
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<FileSack>.doPending() {
        withBack {
            this@doPending.onEachSusBack(context) {
                this@onEachSusBack.addPending()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun MutableList<FileSack>.actionUnDoFavorite() {
        withBackDef(mutableListOf()) {
            toMutableList().asSequence().filter {
                favoritesFiles.contains(it.filePath)
            }.toMutableList()
        }.letSusBack { unFav ->
            withBack {
                context?.newDBFile {
                    unFav.onEachSusBack(context) {
                        deleteMedia(this@onEachSusBack.filePath, null.toStr)
                    }
                }
            }
        }
    }

    @Suppress("LongLine")
    @com.pt.common.global.WorkerAnn
    override fun MutableList<FileSack>.applyHidden() {
        jobNativeFile = launchDef {
            withBack {
                this@applyHidden.onEachSusBack(context) {
                    FileLate(this@onEachSusBack.filePath).applySusBack {
                        FileLate(
                            this@applySusBack.parent?.toStr + "/." + this@applySusBack.name
                        ).letSusBack { newPat ->
                            if (isFile) {
                                this@applySusBack.copyFileTo(
                                    newPat
                                ).letSusBack {
                                    context?.fileDeleter(filePath, typeFile, fileUri?.toUri)
                                }
                            } else {
                                (this@onEachSusBack.fileUri
                                    ?: context?.uriProvider(
                                        this@onEachSusBack.filePath,
                                        com.pt.pro.BuildConfig.APPLICATION_ID
                                    ).toStr).let {
                                    context?.renameFile(
                                        this@onEachSusBack.filePath,
                                        newPat.absolutePath,
                                        this@onEachSusBack.copy(
                                            filePath = newPat.absolutePath,
                                            fileName = newPat.name,
                                            fileUri = it,
                                            dateModified = System.currentTimeMillis(),
                                        )
                                    )
                                }
                            }
                        }
                    }
                    context.nullChecker()
                    displayJustNotify()
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun MutableList<FileSack>.applyShow() {
        jobNativeFile = launchDef {
            withBack {
                this@applyShow.onEachSusBack(context) {
                    context.nullChecker()
                    FileLate(this@onEachSusBack.filePath).applySusBack {
                        this@applySusBack.letSusBack { itF ->
                            (itF.parent?.toStr + "/" + itF.name.removeRange(0, 1))
                        }.letSusBack { pat ->
                            if (isFile) {
                                this@applySusBack.copyFileTo(FileLate(pat)).letSusBack {
                                    cont.mediaStoreFile(this@onEachSusBack, pat, null)
                                    context?.mediaBroadCast(filePath, pat)
                                    context?.fileDeleter(filePath, typeFile, fileUri?.toUri)
                                }
                            } else {
                                (this@onEachSusBack.fileUri
                                    ?: context?.uriProvider(
                                        this@onEachSusBack.filePath,
                                        com.pt.pro.BuildConfig.APPLICATION_ID
                                    ).toStr).let {
                                    context?.renameFile(
                                        this@onEachSusBack.filePath,
                                        pat,
                                        this@onEachSusBack.copy(
                                            filePath = pat,
                                            fileName = FileLate(pat).name,
                                            fileUri = it,
                                            dateModified = System.currentTimeMillis(),
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                context.nullChecker()
                displayJustNotify()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun MutableList<FileSack>.applyStoreShow() {
        jobNativeFile = launchDef {
            this@applyStoreShow.onEachSusBack(context) {
                cont.insertStoreFile(this@onEachSusBack)
                context.nullChecker()
                displayJustNotify()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    internal suspend inline fun Sequence<FileSack>.doCheckVir(
        @com.pt.common.global.WorkerAnn crossinline b: suspend MutableList<FileSack>.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            return@withBackDef this@doCheckVir.filter {
                virtualFiles.contains(it.filePath)
            }.toMutableList()
        }.letSusBack(b)
    }

    @com.pt.common.global.WorkerAnn
    protected fun applyDelete() {
        jobNativeFile = launchDef {
            displayBeforeDelete()
            withBack {
                onlyMedia.asReversed().onEachSusBack(context) {
                    context.nullChecker()
                    context?.fileDeleter(this@onEachSusBack.filePath, this@onEachSusBack.typeFile)
                }

            }
            context.nullChecker()
            withBack {
                onlyFolder.asReversed().onEachSusBack(context) {
                    context.nullChecker()
                    context?.fileDeleter(this@onEachSusBack.filePath, this@onEachSusBack.typeFile)
                }
            }
            context.nullChecker()
            displayAfterDelete()
        }
    }

    protected suspend fun MutableList<FileSack>.addToClipboard(isCopy: Boolean) {
        doAddToClipBoard(isCopy)
        context.nullChecker()
        displayAddToClipboard()
    }

    abstract suspend fun android.view.ViewGroup.snakeBarAndroid(
        parentChildCount: Int,
    )

    abstract suspend fun displayBeforeDelete()
    abstract fun fraMangerHaveChanges()
    abstract suspend fun displayJustNotify()
    abstract suspend fun displayAfterDelete()
    abstract suspend fun FileUpdateSack.doReloadFile()
    abstract suspend fun FileUpdateSack.doRefresh()
    abstract suspend fun com.pt.pro.file.fasten.FragmentFileFasten.displayAfterClipboard()
    abstract suspend fun updateRecycler(newFile: String, anim: Boolean, pos: Int)
    abstract suspend fun MutableList<FileSack>.displayForVirtual(anim: Boolean)
    abstract suspend fun launchPending(relaunch: Boolean)

    abstract suspend fun MutableList<FileSack>.displayReloadFavorites()
    abstract fun onResort()
    abstract suspend fun displayPending(siz: Int)
    abstract suspend fun com.pt.pro.file.fasten.FragmentFileFasten.refresh()
    abstract suspend fun displayAddToClipboard()
    abstract suspend fun reloadFavorites()
    abstract suspend fun FileSack.forZipFiles()
    abstract suspend fun myShareTwo(
        fileSack: FileSack,
        actionType: String,
    )

    @com.pt.common.global.APIAnn(android.os.Build.VERSION_CODES.M)
    protected fun MutableList<MusicSack>.checkDrawOverlayPermission(pos: Int) {
        resultFloating?.launch(goToOverlay)
        musicHolder.addAll(this@checkDrawOverlayPermission)
        musicPos = pos
    }

    private var resultFloating: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (context?.canFloat == true && musicHolder.isNotEmpty()) {
                doLaunchMusicPlayer()
            } else {
                context?.makeToastRec(com.pt.pro.R.string.rl, 0)
            }
        }

    @com.pt.common.global.WorkerAnn
    internal suspend inline fun CharSequence.doSearchOnList(
        @com.pt.common.global.WorkerAnn crossinline f: suspend MutableList<FileSack>.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            return@withBackDef catchy(mutableListOf()) {
                return@catchy searchList.filter {
                    it.fileName.contains(this@doSearchOnList, true)
                }.distinct().toMutableList()
            }
        }.letSusBack(f)
    }

    private fun doLaunchMusicPlayer() {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                context?.newIntentSus(com.pt.pro.musical.music.MusicPlayer::class.java) {
                    flags = FLAGS
                    action = PLAY_MUSIC
                    putExtra(KEY_ORDER, "tillITHurts")
                    putExtra(ALL_SONGS, musicHolder as ArrayList)
                    putExtra(POPUP_POS, musicPos)
                    this@newIntentSus
                }?.alsoSusBack {
                    androidx.core.content.ContextCompat.startForegroundService(ctx, it)
                    homeLauncher {
                        startActivity(this)
                    }
                }
            }
        }
    }

    private var renameListener: com.pt.pro.gallery.dialogs.RenameDialog.DialogListenerTwo?
        get() {
            return com.pt.pro.gallery.dialogs.RenameDialog.DialogListenerTwo { fp, tp, nm ->
                pushJob { j ->
                    launchDef {
                        j?.checkIfDone()
                        withBack {
                            FileSack(
                                nm,
                                tp,
                                fileUri,
                                fileSize,
                                typeFile,
                                System.currentTimeMillis(),
                            ).letSusBack { pic ->
                                if (context?.renameFile(fp, tp, pic) == true) {
                                    context?.makeToastRecSus(com.pt.pro.R.string.rd, 0)
                                    fileCLick.lastOrNull()?.doReloadFile()
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


    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        observing?.let {
            cont.unregisterContentObserver(
                it
            )
        }
        resultFloating?.unregister()
        renameListener = null
        fileCLickNative = null
        pendHashNative = null
        allMediaNative = null
        searchListNative = null
        clipBoardFilesNative = null
        onlyFolderNative = null
        onlyMediaNative = null
        favoritesFilesNative = null
        virtualFilesNative = null
        virtualFilesNameNative = null
        musicHolderNative = null
        observing = null
        resultFloating = null

        jobNativeFile?.cancelJob()
        jobNativeFile = null

        includeShareNative = null
        includeOptionsNative = null
        super.onDestroyView()
    }
}