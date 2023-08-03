package com.pt.pro.musical.music

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.mutual.base.JobHand
import com.pt.common.stable.*
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.isActive

abstract class ParentMusicManger(
    @Volatile
    internal var context: android.content.Context?,
    @Volatile
    internal var contextMedia: android.content.Context?,
) : androidx.media3.common.Player.Listener, com.pt.pro.musical.interfaces.MusicOption,
    kotlinx.coroutines.CoroutineScope, JobHand {

    override var fetchMusicService: (() -> MusicPlayer)? = null

    internal inline val ctxM: android.content.Context get() = context ?: fetchMusicService?.invoke()?.baseContext!!

    private inline val ctxMedia: android.content.Context get() = contextMedia ?: fetchMusicService?.invoke()?.baseContext!!

    internal inline val recM: android.content.res.Resources get() = ctxM.resources
    internal inline val disM: android.util.DisplayMetrics
        get() = ctxM.resources.displayMetrics

    private inline val themM: android.content.res.Resources.Theme get() = ctxM.theme

    private var jobNative: kotlinx.coroutines.Job? = null
    private var extNative: java.util.concurrent.ExecutorService? = null
    private var dispatcherNative: kotlinx.coroutines.ExecutorCoroutineDispatcher? = null

    private inline val job: kotlinx.coroutines.Job
        get() = jobNative ?: kotlinx.coroutines.SupervisorJob().also { jobNative = it }

    private inline val ext: java.util.concurrent.ExecutorService
        get() = extNative ?: fetchExtractor.also { extNative = it }

    private inline val dispatch: kotlinx.coroutines.ExecutorCoroutineDispatcher
        get() = dispatcherNative ?: ext.asCoroutineDispatcher().also { dispatcherNative = it }

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = fetchMusicService?.invoke().let {
            return@let if (it != null && it.isActive) {
                it.coroutineContext
            } else {
                dispatch + job + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }
            }
        }

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    @Volatile
    internal var mediaPlay: androidx.media3.exoplayer.ExoPlayer? = null
    internal inline val songPlayer: androidx.media3.exoplayer.ExoPlayer
        @com.pt.common.global.MainAnn
        get() = mediaPlay ?: androidx.media3.exoplayer.ExoPlayer.Builder(ctxM).apply {
            setAudioAttributes(com.pt.common.global.musicAudioAttr, true)
            setHandleAudioBecomingNoisy(true)
        }.build().also {
            it.repeatMode = androidx.media3.common.Player.REPEAT_MODE_ALL
            it.playWhenReady = true
            it.addListener(this@ParentMusicManger)
        }.also {
            mediaPlay = it
        }

    @Volatile
    internal var musicAda: MusicAdapter? = null
    internal inline val musicAdapter: MusicAdapter get() = musicAda!!

    @Volatile
    internal var windManager: android.view.WindowManager? = null
    internal inline val windowManagerMusic: android.view.WindowManager
        get() = windManager ?: ctxM.fetchSystemWindowManager.also {
            windManager = it
        }

    @Volatile
    internal var params: android.view.WindowManager.LayoutParams? = null
    override val fetchParams: android.view.WindowManager.LayoutParams
        get() = params ?: floatingWindowManger().also {
            params = it
        }


    @Volatile
    internal var mediaSessionNative: androidx.media3.session.MediaLibraryService.MediaLibrarySession? = null

    internal inline val androidx.media3.exoplayer.ExoPlayer.mediaSession: androidx.media3.session.MediaLibraryService.MediaLibrarySession
        get() = mediaSessionNative ?: androidx.media3.session.MediaLibraryService.MediaLibrarySession.Builder(
            fetchMusicService!!.invoke(),
            this@mediaSession,
            LibrarySessionCallback()
        ).setSessionActivity(ctxM.sessionActivityPendingIntent).setId(CH_MUS)
            .build().apply {
                mediaSessionNative = this
            }

    @Volatile
    internal var mainProcessISDone: Boolean = false

    @Volatile
    internal var notify: androidx.media3.session.MediaNotification? = null

    @Volatile
    internal var backProcessISDone: Boolean = false

    @Volatile
    internal var isProgressLoading: Boolean = true

    @Volatile
    internal var musicAnimation: androidx.core.animation.ObjectAnimator? = null

    @Volatile
    internal var layoutRec: com.pt.pro.musical.fasten.MusicRecyclerHeadFasten? = null

    @Volatile
    internal var stubPlaylist: com.pt.pro.musical.fasten.StubSavePlaylistFasten? = null

    @Volatile
    internal var rootDetails: com.pt.pro.musical.fasten.MusicHeadPlayerFasten? = null
    internal inline val headMusic: com.pt.pro.musical.fasten.MusicHeadPlayerFasten get() = rootDetails!!

    @Volatile
    internal var rootMain: com.pt.pro.musical.fasten.MusicHeadFasten? = null
    internal inline val headPlayer: com.pt.pro.musical.fasten.MusicHeadFasten get() = rootMain!!

    internal var searchJob: kotlinx.coroutines.Job? = null

    internal var recyclerManager: com.pt.common.moderator.recycler.NoAnimLinearManager? = null


    internal var screenWidth: Int? = null
    internal var screenHeight: Int? = null
    internal inline fun musicDim(a: (wid: Int, hei: Int) -> Unit) {
        screenWidth.let { w ->
            screenHeight.let { h ->
                if (w != null && h != null) {
                    a.invoke(w, h)
                } else {
                    ctxM.fetchDimensions {
                        a.invoke(width, height)
                        screenWidth = this@fetchDimensions.width
                        screenHeight = this@fetchDimensions.height
                    }
                }
            }
        }
    }

    internal inline val android.content.Context.sessionActivityPendingIntent: android.app.PendingIntent
        get() = android.app.TaskStackBuilder.create(this@sessionActivityPendingIntent).run {
            addNextIntent(
                android.content.Intent(
                    this@sessionActivityPendingIntent,
                    com.pt.pro.main.former.MainActivity::class.java
                ).also { it.flags = BACKGROUND_FLAGS }
            )
            getPendingIntent(0, PEND_FLAG)
        }

    internal inline val devVol: Int?
        @com.pt.common.global.WorkerAnn
        get() = runCatching {
            songManager?.getStreamVolume(
                android.media.AudioManager.STREAM_MUSIC
            )
        }.getOrNull()

    protected val needToWrap: Boolean
        get() {
            return rootMain?.circleCard?.rotation?.toInt()?.let {
                it != 0 && it != 360
            } ?: false
        }

    internal inline val Float.toPixelM: Int
        @Strictfp
        get() = (this * disM.density + 0.5f).toInt()

    @Volatile
    internal var isCarMode: Boolean = false

    @Volatile
    internal var isRight: Boolean = false

    @Volatile
    internal var isTouchActive: Boolean = true

    @Volatile
    internal var haveMove: Boolean = false

    @Volatile
    internal var isError: Boolean = false

    @Volatile
    internal var isSmall: Boolean = true

    @Volatile
    internal var lastPauseTime: Long = 0L

    @Volatile
    internal var whatSongMode: Int = IS_YOUR_PLAYLIST

    @Volatile
    internal var recyclerMode: Int = 0

    @Volatile
    internal var reMode: Int = REPEAT_ALL

    @Volatile
    internal var numberOfRepeats: Int = 0

    @Volatile
    internal var progressRatio: Int = 0

    @Volatile
    internal var musicProgress: Long = 0L

    @Volatile
    private var lastProgress: Long = 0L

    @Volatile
    internal var songDuration: Long = 0L

    @Volatile
    internal var currPos: Int = 0

    @Volatile
    internal var song: MusicSack? = null

    @Volatile
    internal var currentMusic: MusicSack? = null

    @Volatile
    internal var changMusic: String? = null

    internal inline val Int.skipNextPos: Int
        get() {
            return when {
                this@skipNextPos < allMusic.size - 1 -> (this@skipNextPos + 1)
                else -> if (this@skipNextPos >= allMusic.lastIndex) {
                    0
                } else {
                    this@skipNextPos
                }
            }
        }

    internal inline val Int.skipPrevPos: Int
        get() {
            return when {
                this@skipPrevPos > 0 -> (this@skipPrevPos - 1)
                this@skipPrevPos <= 0 -> (allMusic.size - 1)
                else -> this@skipPrevPos
            }
        }

    internal inline val drags: Int
        get() {
            return (androidx.recyclerview.widget.ItemTouchHelper.UP or
                    androidx.recyclerview.widget.ItemTouchHelper.DOWN)
        }

    internal inline val swipes: Int
        get() {
            return androidx.recyclerview.widget.ItemTouchHelper.START or
                    androidx.recyclerview.widget.ItemTouchHelper.END
        }

    private var jobPosForShuffle: kotlinx.coroutines.Job? = null

    internal var jobPushMusic: kotlinx.coroutines.Job? = null

    internal var jobItemChanged: kotlinx.coroutines.Job? = null

    internal var jobPreviousSong: kotlinx.coroutines.Job? = null

    @Volatile
    internal var songColors: DSack<Int, Int, Int> = initColors
    private inline val initColors: DSack<Int, Int, Int>
        get() {
            return com.pt.common.global.DSack(
                themM.findAttr(com.pt.pro.R.attr.rmoText),
                themM.findAttr(com.pt.pro.R.attr.rmoBackHint),
                IS_YOUR_PLAYLIST,
            )
        }

    internal inline val android.content.res.Resources.isLand: Boolean
        get() = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE


    internal inline val contM: android.content.ContentResolver
        get() = kotlin.runCatching {
            if (isV_N) {
                (androidx.core.content.ContextCompat.isDeviceProtectedStorage(ctxM).run {
                    if (this) androidx.core.content.ContextCompat.createDeviceProtectedStorageContext(ctxM)?.contentResolver else ctxM.contentResolver
                } ?: ctxM.contentResolver)
            } else {
                ctxM.contentResolver
            }
        }.getOrDefault(ctxM.contentResolver)

    @Volatile
    internal var allMusicNative: MutableList<MusicSack>? = mutableListOf()
    internal inline val allMusic: MutableList<MusicSack>
        get() = allMusicNative ?: mutableListOf<MusicSack>().also {
            allMusicNative = it
        }

    @Volatile
    internal var allItemNative: MutableList<androidx.media3.common.MediaItem>? = mutableListOf()
    internal inline val allItemMusic: MutableList<androidx.media3.common.MediaItem>
        get() = allItemNative ?: mutableListOf<androidx.media3.common.MediaItem>().also {
            allItemNative = it
        }

    @Volatile
    internal var allMusicOriNative: MutableList<MusicSack>? = mutableListOf()
    internal inline val allMusicOriginal: MutableList<MusicSack>
        get() = allMusicOriNative ?: mutableListOf<MusicSack>().also {
            allMusicOriNative = it
        }

    @Volatile
    internal var searchListNative: MutableList<MusicSack>? = mutableListOf()
    internal inline val searchList: MutableList<MusicSack>
        get() = searchListNative ?: mutableListOf<MusicSack>().also {
            searchListNative = it
        }

    @Volatile
    internal var intiSeekSong: Long = 1L

    @Volatile
    internal var isFromCreate: Boolean? = null

    override fun inti(fromCreate: Boolean) {
        isCarMode = ctxM.isCarUiMode
        isFromCreate = fromCreate
    }

    override suspend fun MutableList<MusicSack>.setOrgAllSongs(pos: Int) {
        justCoroutine {
            if (this@setOrgAllSongs.isEmpty()) return@justCoroutine
            allMusicOriNative = this@setOrgAllSongs

            currPos = if (pos > this@setOrgAllSongs.lastIndex) 0 else pos
            currentMusic = this@setOrgAllSongs.getI(currPos)
            allMusicNative = this@setOrgAllSongs.toMutableList()
            ctxM.findStringPreference(
                MUSIC_SONG_KEY,
                ""
            ).let {
                if (it == currentMusic?.pathAudio) {
                    intiSeekSong = ctxM.findLongPreference(
                        MUSIC_PROGRESS_KEY,
                        1L
                    )
                }
            }
            if (reMode == REPEAT_ONE_THREE) {
                numberOfRepeats = 3
            } else if (reMode == REPEAT_ONE_FIVE) {
                numberOfRepeats = 5
            }
        }
    }

    protected suspend fun rotateOriginal(pos: Int, tarPos: Int) {
        withDefault {
            allMusicOriginal.applySusBack {
                if (pos <= tarPos) {
                    java.util.Collections.rotate(this@applySusBack.subList(pos, tarPos + 1), -1)
                } else {
                    java.util.Collections.rotate(this@applySusBack.subList(tarPos, pos + 1), 1)
                }
            }
            allItemMusic.applySusBack {
                if (pos <= tarPos) {
                    java.util.Collections.rotate(this@applySusBack.subList(pos, tarPos + 1), -1)
                } else {
                    java.util.Collections.rotate(this@applySusBack.subList(tarPos, pos + 1), 1)
                }
            }
        }
    }


    private var audioListener: android.media.AudioManager.OnAudioFocusChangeListener? =
        android.media.AudioManager.OnAudioFocusChangeListener {

        }

    @Volatile
    private var powerManagerNative: android.os.PowerManager? = null
    private inline val powerManager: android.os.PowerManager?
        get() = powerManagerNative ?: context?.fetchSystemService(powerService).also {
            powerManagerNative = it
        }

    internal var audioManager: android.media.AudioManager? = null
    internal inline val songManager: android.media.AudioManager?
        get() {
            return audioManager ?: context?.fetchSystemService(audioService).also {
                audioManager = it
            }
        }

    private inline val isScreenOn: Boolean?
        get() = kotlin.runCatching {
            try {
                powerManager?.isInteractive
            } catch (e: RuntimeException) {
                null
            }
        }.getOrNull()

    private var isOn: Boolean = true

    private val runPower: DSack<() -> Unit, Int, Long>
        get() = toCatchSackAfter(8448, 2000L) {
            isScreenOn?.also {
                if (it != isOn && !it) {
                    receiverForCircle(if (it) 1 else 0)
                }
                isOn = it
            }
            runPower.postBackAfter()
        }

    protected suspend fun doSoundEffect(sessionId: Int) {
        withDefault {
            ctxM.openAudioEffectSession(sessionId, com.pt.pro.BuildConfig.APPLICATION_ID)
            runPower.postBackAfter()
        }
    }


    private fun receiverForCircle(it: Int) {
        toCatchSackAfter(594, 300L) {
            if (it == 0 && this@ParentMusicManger.context != null) {
                currentMusic?.copy(bitmap = null)?.let { it1 ->
                    if (context != null && mediaPlay?.mediaItemCount != 0) {
                        songColors.launchMusicActivity(it1)
                    }
                }
                if (!isSmall) {
                    doScreenOff()
                }
            } else if (it == 1 && this@ParentMusicManger.context != null) {
                doScreenOn()
                unBackPost(pauseHand.two)
            }
            Unit
        }.postAfter()
    }

    internal var haveHand: Boolean = false

    internal inline val runnable: DSackT<() -> Unit, Int>
        get() = toCatchSack(55) {
            if (context != null) {
                if (0 == devVol && mediaPlay?.isPlaying == true) {
                    mediaPlay?.pause()
                }
            }
        }


    internal inline val android.content.Context.pendAction: (String) -> android.app.PendingIntent
        @com.pt.common.global.WorkerAnn
        get() = {
            newIntent(MusicPlayer::class.java) {
                putExtra(KEY_ORDER, "tillITHurts")
                action = it
                this@newIntent
            }.let {
                android.app.PendingIntent.getService(
                    this@pendAction,
                    0,
                    it,
                    PEND_FLAG
                )
            }
        }


    protected fun MutableList<MusicSack>.intiPlayerList(
        currPos: Int,
        intiSeekSong: Long
    ) {
        launchDef {
            withBack {
                ctxM.intiPlayerListInService(
                    this@intiPlayerList,
                ) {
                    afterIntiPlayerList(currPos, intiSeekSong)
                }
            }
        }
    }

    private fun MutableList<androidx.media3.common.MediaItem>.afterIntiPlayerList(
        currPos: Int,
        intiSeekSong: Long
    ) {
        launchDef {
            doAfterIntiPlayerList(currPos = currPos, intiSeekSong = intiSeekSong)
        }
    }

    private suspend fun MutableList<androidx.media3.common.MediaItem>.doAfterIntiPlayerList(
        currPos: Int,
        intiSeekSong: Long
    ) {
        withMain {
            allItemNative = this@doAfterIntiPlayerList
            songPlayer.applySus {
                setMediaItemsSus {
                    this@doAfterIntiPlayerList
                }
                justCoroutine {
                    seekToDefaultPosition(currPos)
                }
                prepareSus()
                justCoroutine {
                    if (isFromCreate == true) {
                        pauseSus()
                    } else {
                        intiMusicSeek(intiSeekSong)
                    }
                }
            }
        }
    }

    internal inline val Int.intiRepeater: DSackT<Int, Boolean>
        get() {
            return when (this) {
                REPEAT_ALL -> {
                    DSackT(
                        androidx.media3.common.Player.REPEAT_MODE_ALL,
                        false,
                    )
                }

                REPEAT_OFF -> {
                    DSackT(
                        androidx.media3.common.Player.REPEAT_MODE_OFF,
                        false,
                    )
                }

                REPEAT_ONE -> {
                    DSackT(
                        androidx.media3.common.Player.REPEAT_MODE_ONE,
                        false,
                    )
                }

                REPEAT_ONE_THREE -> {
                    numberOfRepeats = 3
                    DSackT(
                        androidx.media3.common.Player.REPEAT_MODE_ONE,
                        false,
                    )
                }

                REPEAT_ONE_FIVE -> {
                    numberOfRepeats = 5
                    DSackT(
                        androidx.media3.common.Player.REPEAT_MODE_ONE,
                        false,
                    )
                }

                REPEAT_SHUFFLE -> {
                    DSackT(
                        androidx.media3.common.Player.REPEAT_MODE_ALL,
                        true,
                    )
                }

                else -> {
                    DSackT(
                        androidx.media3.common.Player.REPEAT_MODE_ALL,
                        false,
                    )
                }
            }
        }

    protected fun MutableList<MusicSack>.intiAppendPlayerList() {
        launchDef {
            withBack {
                ctxM.intiPlayerListInService(
                    this@intiAppendPlayerList,
                ) {
                    afterAppendPlayList()
                }
            }
        }
    }

    private fun MutableList<androidx.media3.common.MediaItem>.afterAppendPlayList() {
        launchDef {
            doAfterAppend()
            catoFilter(IS_YOUR_PLAYLIST, null, null, null)
        }
    }

    private suspend fun MutableList<androidx.media3.common.MediaItem>.doAfterAppend() {
        withMain {
            allItemMusic.addAll(this@doAfterAppend)
        }
        withMain {
            songPlayer.applySus {
                addMediaItems(this@doAfterAppend)
            }
        }
    }

    internal suspend inline fun String.newItem(
        pos: Int,
        title: String?,
        crossinline newMediaItem: suspend androidx.media3.common.MediaItem.() -> Unit,
    ) {
        withBack {
            androidx.media3.common.MediaItem.Builder().applySusBack {
                this@newItem.toUri.letSusBack { uri ->
                    setUri(uri)
                    setMediaId(pos.toStr)
                    if (isCarMode) {
                        ctxM.getMusicAllDetails(this@newItem, pos = pos) {
                            setMediaMetadata(this@getMusicAllDetails.build())
                        }
                    } else {
                        updateMetaData(title = title) {
                            setMediaMetadata(this@updateMetaData.build())
                        }
                    }
                }
            }.build().applySusBack(newMediaItem)
        }
    }

    protected suspend fun MusicSack.createMetaMedia(
        pos: Int,
    ): androidx.media3.common.MediaMetadata.Builder? = justCoroutine {
        return@justCoroutine withBackDef(null) {
            return@withBackDef androidx.media3.common.MediaMetadata.Builder().applySusBack {
                bitmap?.let { bit ->
                    setArtworkData(
                        bit.toByteArray(),
                        androidx.media3.common.MediaMetadata.PICTURE_TYPE_MEDIA
                    )
                }
                setArtist(artist)
                setAlbumArtist(artist)
                setSubtitle(artist)
                setDiscNumber(pos)
                setIsPlayable(true)
                setDisplayTitle(title)
                setTitle(title)
            }
        }
    }

    protected suspend fun androidx.media3.common.Player.skipPos(pos: Int) {
        withMain {
            if (reMode == REPEAT_SHUFFLE) {
                seekToDefaultPosition(pos)
            } else {
                when {
                    currPos - pos == 1 -> seekToPreviousMediaItem()
                    pos - currPos == 1 -> seekToNextMediaItem()
                    else -> seekToDefaultPosition(pos)
                }
            }
        }
    }

    protected fun androidx.media3.common.Player.skipPosForShuffle(forward: Boolean) {
        jobPosForShuffle?.cancelJob()
        pushJob {
            launchDef {
                unPost(onSecondEvery.two)
                skipPosShuffle(forward)
            }.also { jobPosForShuffle = it }
        }
    }

    private suspend fun androidx.media3.common.Player.skipPosShuffle(forward: Boolean) {
        withMain {
            if (forward)
                seekToNextMediaItem()
            else
                seekToPreviousMediaItem()
        }
    }

    protected val musicAudioAttr: androidx.media3.common.AudioAttributes =
        androidx.media3.common.AudioAttributes.Builder().apply {
            setUsage(androidx.media3.common.C.USAGE_MEDIA)
            setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC)
        }.build()

    inner class LibrarySessionCallback : androidx.media3.session.MediaLibraryService.MediaLibrarySession.Callback {

        override fun onConnect(
            session: androidx.media3.session.MediaSession,
            controller: androidx.media3.session.MediaSession.ControllerInfo
        ): androidx.media3.session.MediaSession.ConnectionResult {
            val connectionResult = super.onConnect(session, controller)
            val availableSessionCommands = connectionResult.availableSessionCommands.buildUpon()
            return androidx.media3.session.MediaSession.ConnectionResult.accept(
                availableSessionCommands.build(),
                connectionResult.availablePlayerCommands
            )
        }

        override fun onGetLibraryRoot(
            session: androidx.media3.session.MediaLibraryService.MediaLibrarySession,
            browser: androidx.media3.session.MediaSession.ControllerInfo,
            params: androidx.media3.session.MediaLibraryService.LibraryParams?
        ): com.google.common.util.concurrent.ListenableFuture<androidx.media3.session.LibraryResult<androidx.media3.common.MediaItem>> {
            return (mediaPlay?.currentMediaItem ?: contM.fetchAudioLoaderNormal()?.run {
                pathAudio?.newItemNormal(-1, title)
            } ?: "Root".newItemNormal(-1, "Root")).run {
                com.google.common.util.concurrent.Futures.immediateFuture(
                    androidx.media3.session.LibraryResult.ofItem(
                        this,
                        params
                    )
                )
            }
        }

        override fun onGetItem(
            session: androidx.media3.session.MediaLibraryService.MediaLibrarySession,
            browser: androidx.media3.session.MediaSession.ControllerInfo,
            mediaId: String
        ): com.google.common.util.concurrent.ListenableFuture<androidx.media3.session.LibraryResult<androidx.media3.common.MediaItem>> {
            val item = allItemNative?.find {
                it.mediaId == mediaId
            } ?: contM.fetchAudioLoaderNormal()?.run {
                pathAudio?.newItemNormal(-1, title)
            } ?: "Root".newItemNormal(-1, "Root")
            return com.google.common.util.concurrent.Futures.immediateFuture(
                androidx.media3.session.LibraryResult.ofItem(
                    item,
                    null
                )
            )
        }

        override fun onSubscribe(
            session: androidx.media3.session.MediaLibraryService.MediaLibrarySession,
            browser: androidx.media3.session.MediaSession.ControllerInfo,
            parentId: String,
            params: androidx.media3.session.MediaLibraryService.LibraryParams?
        ): com.google.common.util.concurrent.ListenableFuture<androidx.media3.session.LibraryResult<Void>> {
            session.notifyChildrenChanged(browser, parentId, allMusic.size, params)
            return com.google.common.util.concurrent.Futures.immediateFuture(
                androidx.media3.session.LibraryResult.ofVoid()
            )
        }

        override fun onGetChildren(
            session: androidx.media3.session.MediaLibraryService.MediaLibrarySession,
            browser: androidx.media3.session.MediaSession.ControllerInfo,
            parentId: String,
            page: Int,
            pageSize: Int,
            params: androidx.media3.session.MediaLibraryService.LibraryParams?
        ): com.google.common.util.concurrent.ListenableFuture<androidx.media3.session.LibraryResult<com.google.common.collect.ImmutableList<androidx.media3.common.MediaItem>>> {
            val children = allItemNative ?: allMusicNative?.getOrNull(currPos)?.run {
                pathAudio.toStr.newItemNormal(currPos, title)
            }?.getList ?: (contM.fetchAudioLoaderNormal()?.run {
                pathAudio?.newItemNormal(-1, title)
            } ?: "Root".newItemNormal(-1, "Root")).getList

            return com.google.common.util.concurrent.Futures.immediateFuture(
                androidx.media3.session.LibraryResult.ofItemList(
                    children,
                    params
                )
            )
        }

        override fun onAddMediaItems(
            mediaSession: androidx.media3.session.MediaSession,
            controller: androidx.media3.session.MediaSession.ControllerInfo,
            mediaItems: List<androidx.media3.common.MediaItem>
        ): com.google.common.util.concurrent.ListenableFuture<List<androidx.media3.common.MediaItem>> {
            mediaItems.firstOrNull()?.apply {
                requestMetadata.fetchSearchStr?.let(::pushFromSearch) ?: mediaId.pushFroId()
            }
            return com.google.common.util.concurrent.Futures.immediateFuture(
                allItemNative ?: mutableListOf()
            )
        }

    }

    protected fun String.pushFroId() {
        if (this@pushFroId.isNotEmpty() && this@pushFroId.isNotBlank()) {
            catchy(-1) {
                this@pushFroId.toInt().also { i ->
                    allItemMusic.indexOfFirst {
                        catchy(-1) {
                            try {
                                it.mediaId.toInt()
                            } catch (_: NumberFormatException) {
                                -1
                            }
                        } == i
                    }.also { index ->
                        if (index > 0) {
                            launchDef {
                                songPlayer.skipPos(index)
                            }
                        }
                    }
                }
            }
        }
    }

    protected fun pushFromSearch(s: String) {
        launchDef {
            withBack {
                justCoroutine {
                    fetchListForQuery(s)
                }.letSusBack { art ->
                    if (art.isNullOrEmpty()) {
                        allMusicNative?.indexOfFirst {
                            it.title.toStr.lowercase().contains(s, true) ||
                                    it.pathAudio.toStr.lowercase().contains(s, true)
                        }.let { ms ->
                            if (ms == -1 || ms == null) {
                                fetchSearchSongFromAll(s) { i ->
                                    isFromCreate = false
                                    songPlayer.skipPos(i)
                                }
                            } else {
                                isFromCreate = false
                                songPlayer.skipPos(ms)
                            }
                        }
                    } else {
                        isFromCreate = false
                        art.pushNewSongs(0, true)
                    }
                }

            }
        }
    }

    private suspend fun fetchListForQuery(
        s: String
    ): MutableList<MusicSack>? {
        return contM.allArtistsLoader().run {
            this@run.firstOrNull {
                it.title.toStr.lowercase() == s
            } ?: this@run.firstOrNull {
                it.title.toStr.lowercase().contains(s, true)
            }
        }?.let { art ->
            contM.filterAudioByArtist(
                art.artist.toStr,
                false,
                art.artist
            ).onEachSusBack(true) {
                songType = IS_YOUR_PLAYLIST
            }
        }
    }

    private suspend inline fun fetchSearchSongFromAll(
        s: String,
        crossinline r: suspend (Int) -> Unit
    ) {
        contM.allAudioLoader(false).find {
            it.title.toStr.lowercase().contains(s, true) ||
                    it.pathAudio.toStr.lowercase().contains(s, true)
        }?.letSusBack {
            withDefault {
                allMusic.add(it.copy(songType = IS_YOUR_PLAYLIST))
                allMusicOriginal.add(it.copy(songType = IS_YOUR_PLAYLIST))
            }
            it.pathAudio.toStr.newItem(allMusic.lastIndex, it.title.toStr) {
                withMain {
                    allItemMusic.add(this@newItem)
                    songPlayer.addNewItem(this@newItem)
                    r.invoke(allMusic.lastIndex)
                }
            }
        } ?: songPlayer.playSus()
    }


    @com.pt.common.global.WorkerAnn
    protected fun MutableList<MusicSack>.doSavePlayList(playListName: String) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                withBack {
                    context?.newDBPlaylist<Unit> {
                        MusicSack(
                            title = playListName,
                            pathAudio = this@doSavePlayList.getI(0).pathAudio,
                            artist = size.toString(),
                            idArtAlb = currPos,
                        ).insertPlaylist()
                    }
                }
                context.nullChecker()
                withBack {
                    mutableListOf<MusicSack>().alsoSusBack { new ->
                        this@doSavePlayList.onEachSusBack(context) {
                            new.add(this@onEachSusBack.copy(playlistSong = playListName))
                        }
                    }.alsoSusBack {
                        context?.newDBPlaylist<Unit> {
                            runCatching {
                                getAllPlaylist().indexOfFirst {
                                    it.title == playListName
                                }.letSusBack { i ->
                                    if (i != -1) {
                                        deleteAllMusic(playListName)
                                        updatePlaylist(playListName, it.size)
                                    }
                                    it.insertMusic()
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private suspend fun updatePlayListFromSearch(str: String) {
        ctxM.getMusicAllDetails(
            str,
            pos = allMusic.lastIndex,
        ) {
            withMain {
                songPlayer.playlistMetadata = this@getMusicAllDetails.build()
            }
        }
    }

    protected fun String.newItemNormal(
        pos: Int,
        title: String?,
    ): androidx.media3.common.MediaItem {
        return androidx.media3.common.MediaItem.Builder().apply {
            this@newItemNormal.toUri.let { uri ->
                setUri(uri)
                setMediaId(pos.toStr)
                androidx.media3.common.MediaMetadata.Builder().apply {
                    setIsPlayable(true)
                    title?.let {
                        setDisplayTitle(it)
                        setTitle(it)
                    }
                }.run {
                    setMediaMetadata(this@run.build())
                }
            }
        }.build()
    }

    abstract suspend fun androidx.media3.exoplayer.ExoPlayer.addNewItem(
        ite: androidx.media3.common.MediaItem
    )

    override fun fetchSession(): androidx.media3.session.MediaLibraryService.MediaLibrarySession? = mediaSessionNative


    override fun fetchNotify(): androidx.media3.session.MediaNotification? = notify

    abstract fun setRepeatMode(repeat: Int)
    abstract fun com.pt.pro.musical.fasten.MusicHeadFasten.setViewRoot()
    abstract fun com.pt.pro.musical.fasten.MusicHeadPlayerFasten.setViewRoot2()
    abstract fun doScreenOn()
    abstract fun doScreenOff()
    abstract fun androidx.appcompat.widget.AppCompatEditText.searchProvide()
    abstract fun androidx.appcompat.widget.AppCompatEditText.hideInputMethod()

    abstract suspend fun applyPlayOrPause(isPlay: Boolean, changeMode: Boolean)
    abstract suspend fun applyPreviousSong(currentPos: Int)
    abstract suspend fun applyNextMusic(currentPos: Int)
    abstract suspend fun doMoveFun(pos: Int, tarPos: Int)
    abstract suspend fun doSwipeFun(path: String)
    abstract suspend fun circleExtend()
    abstract suspend fun clearPlayList()
    abstract suspend fun circleShrink(force: Boolean)
    abstract suspend fun doUp(landScape: Boolean)
    abstract suspend fun whichPlayList()
    abstract suspend fun hiddenProvider()
    abstract suspend fun MutableList<MusicSack>.doPlaylistSave()
    abstract suspend fun returnToCircle()
    abstract suspend fun updatePlayerViews(pos: Int, path: String, isPlay: Boolean)

    abstract suspend fun catoFilter(
        newWhatMode: Int,
        filterFromAlbum: String?,
        filterFromArtist: String?,
        artist: String?,
    )

    abstract suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.doRecolorItems(
        frontMyPlaylist: Int,
        backMyPlaylist: Int,
        frontShowArtists: Int,
        backShowArtists: Int,
        frontShowAlbums: Int,
        backShowAlbums: Int,
        frontShowAllMusic: Int,
        backShowAllMusic: Int,
        frontShowAllList: Int,
        backShowAllList: Int,
    )

    protected fun MusicSack.showNotification(
        ds: DSack<Boolean, Boolean, Boolean>,
        ifForCLear: Boolean = false
    ) {
        unBackPost(946)
        unBackPost(947)
        toCatchSackAfter(946, 100L) {
            launchDef {
                showNotificationImp(ds, ifForCLear)
            }
            toCatchSackAfter(947, 1000L) {
                launchDef {
                    song?.showNotificationImp(ds, ifForCLear)
                }
            }.postBackAfter()
        }.postBackAfter()
    }

    private inline val androidx.core.app.NotificationCompat.Builder.mediaStyle: (Boolean) -> androidx.media3.session.MediaStyleNotificationHelper.MediaStyle
        @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
        get() = {
            androidx.media3.session.MediaStyleNotificationHelper.MediaStyle(
                songPlayer.mediaSession
            ).also { ms ->
                if (!it) {
                    ms.setShowActionsInCompactView(0, 1, 2, 4)
                } else {
                    ms.setShowActionsInCompactView(0, 1, 2)
                }
                ms.setShowCancelButton(false)
                ms.setBuilder(this)
            }
        }

    @android.annotation.SuppressLint("MissingPermission")
    private suspend fun MusicSack.showNotificationImp(
        ds: DSack<Boolean, Boolean, Boolean>,
        ifForCLear: Boolean = false,
    ) {
        withDefault {
            withSusBack(ctxMedia) {
                androidx.core.app.NotificationCompat.Builder(this, CH_MUS).applySusBack {
                    setShowWhen(false)
                    setSmallIcon(
                        com.pt.pro.R.drawable.ic_launcher_foreground,
                        androidx.core.app.NotificationCompat.PRIORITY_MIN
                    )
                    if (ifForCLear) setContentTitle("") else setContentTitle(title)
                    if (ifForCLear) setContentText("") else setContentText(artist)
                    setContentIntent(pendAction(EXTEND_MUSIC))
                    setLargeIcon(bitmap)
                    //color = theme.findAttr(com.pt.music.R.attr.rmoText)
                    notificationAction(PREVIOUS_MUSIC, ds.one) {
                        addAction(build())
                    }
                    notificationAction(PAUSE_PLAY, ds.one) {
                        addAction(build())
                    }
                    notificationAction(NEXT_MUSIC, ds.one) {
                        addAction(build())
                    }
                    if (!ds.three && ds.two) {
                        notificationAction(CLOSE_MUSIC, ds.one) {
                            addAction(build())
                        }
                    } else if (!ds.two) {
                        notificationAction(
                            HIDDEN_MUSIC, ds.one
                        ) {
                            addAction(build())
                        }
                    }
                    setStyle(mediaStyle(ds.three))
                    setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                    setAutoCancel(false)
                    setOnlyAlertOnce(true)
                    setOngoing(true)
                    setDeleteIntent(ctxMedia.pendAction(CLOSE_MUSIC))
                    setWhen(System.currentTimeMillis())

                    setCategory(androidx.core.app.NotificationCompat.CATEGORY_SERVICE)
                    priority = androidx.core.app.NotificationCompat.PRIORITY_HIGH or
                            androidx.core.app.NotificationCompat.PRIORITY_MAX
                    foregroundServiceBehavior =
                        androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
                }.build().letSusBack {
                    notify = androidx.media3.session.MediaNotification(SERVICE_MUSIC, it)
                    it.flags = androidx.core.app.NotificationCompat.FLAG_FOREGROUND_SERVICE or
                            androidx.core.app.NotificationCompat.FLAG_ONGOING_EVENT

                    androidx.core.app.NotificationManagerCompat.from(ctxMedia).applySusBack {
                        notify(SERVICE_MUSIC, it)
                    }
                }
            }
        }
    }

    override fun updatePlayer(progress: Long) {
        mediaPlay?.apply {
            seekTo(progress)
            if (!isPlaying) play() else return
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend inline fun notificationAction(
        action: String, isPlay: Boolean,
        crossinline actNot: suspend androidx.core.app.NotificationCompat.Action.Builder.() -> Unit,
    ) {
        justCoroutine {
            when (action) {
                PREVIOUS_MUSIC -> com.pt.pro.R.drawable.ic_previous_song
                PAUSE_PLAY -> {
                    if (isPlay)
                        com.pt.pro.R.drawable.ic_pause_music
                    else
                        com.pt.pro.R.drawable.ic_play_circle
                }

                NEXT_MUSIC -> com.pt.pro.R.drawable.ic_next_song
                HIDDEN_MUSIC -> com.pt.pro.R.drawable.ic_hidden_not
                CLOSE_MUSIC -> com.pt.pro.R.drawable.ic_close
                else -> com.pt.pro.R.drawable.ic_next_song
            }
        }.letSusBack { icon ->
            androidx.core.app.NotificationCompat.Action.Builder(
                icon,
                action,
                ctxMedia.pendAction(action)
            ).letSusBack(actNot)
        }
    }

    protected suspend fun Sequence<MusicSack>.filterSongs(
        s: CharSequence,
    ): MutableList<MusicSack> = justCoroutine {
        return@justCoroutine withBackDef(mutableListOf()) {
            return@withBackDef runCatching {
                return@runCatching filter {
                    it.artist.toString().contains(s, true)
                }.distinctBy { it.title + it.artist }.toMutableList()
            }.getOrDefault(this@filterSongs.toMutableList())
        }.letSusBack { art ->
            return@letSusBack withBackDef(mutableListOf()) {
                return@withBackDef runCatching {
                    return@runCatching filter {
                        it.title.toString().contains(s, true)
                    }.distinctBy { it.title + it.artist }.toMutableList()
                }.getOrDefault(this@filterSongs.toMutableList())
            }.runSusBack {
                return@runSusBack withBackDef(mutableListOf()) {
                    return@withBackDef mutableListOf<MusicSack>().apply {
                        addAll(art)
                        addAll(this@runSusBack)
                    }.distinctBy { it.title + it.artist }.toMutableList()
                }
            }
        }
    }

    protected suspend fun MutableList<MusicSack>.whichListInSearch() {
        justScope {
            searchListNative = this@whichListInSearch.toMutableList()
        }
    }

    protected suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.reColorItem(
        it: DSack<Int, Int, Int>,
    ) {
        withDefault {
            val frontMyPlaylist: Int
            val backMyPlaylist: Int
            val frontShowArtists: Int
            val backShowArtists: Int
            val frontShowAlbums: Int
            val backShowAlbums: Int
            val frontShowAllMusic: Int
            val backShowAllMusic: Int
            val frontShowAllList: Int
            val backShowAllList: Int
            when (it.three) {
                ARTIST_IS -> {
                    frontMyPlaylist = it.one
                    backMyPlaylist = 0
                    frontShowArtists = it.two
                    backShowArtists = it.one
                    frontShowAlbums = it.one
                    backShowAlbums = 0
                    frontShowAllMusic = it.one
                    backShowAllMusic = 0
                    frontShowAllList = it.one
                    backShowAllList = 0
                }

                ALBUM_IS -> {
                    frontMyPlaylist = it.one
                    backMyPlaylist = 0
                    frontShowArtists = it.one
                    backShowArtists = 0
                    frontShowAlbums = it.two
                    backShowAlbums = it.one
                    frontShowAllMusic = it.one
                    backShowAllMusic = 0
                    frontShowAllList = it.one
                    backShowAllList = 0
                }

                IS_ALL_SONGS -> {
                    frontMyPlaylist = it.one
                    backMyPlaylist = 0
                    frontShowArtists = it.one
                    backShowArtists = 0
                    frontShowAlbums = it.one
                    backShowAlbums = 0
                    frontShowAllMusic = it.two
                    backShowAllMusic = it.one
                    frontShowAllList = it.one
                    backShowAllList = 0
                }

                ALL_PLAYLIST -> {
                    frontMyPlaylist = it.one
                    backMyPlaylist = 0
                    frontShowArtists = it.one
                    backShowArtists = 0
                    frontShowAlbums = it.one
                    backShowAlbums = 0
                    frontShowAllMusic = it.one
                    backShowAllMusic = 0
                    frontShowAllList = it.two
                    backShowAllList = it.one
                }

                else -> {
                    frontMyPlaylist = it.two
                    backMyPlaylist = it.one
                    frontShowArtists = it.one
                    backShowArtists = 0
                    frontShowAlbums = it.one
                    backShowAlbums = 0
                    frontShowAllMusic = it.one
                    backShowAllMusic = 0
                    frontShowAllList = it.one
                    backShowAllList = 0
                }
            }
            doRecolorItems(
                frontMyPlaylist = frontMyPlaylist,
                backMyPlaylist = backMyPlaylist,
                frontShowArtists = frontShowArtists,
                backShowArtists = backShowArtists,
                frontShowAlbums = frontShowAlbums,
                backShowAlbums = backShowAlbums,
                frontShowAllMusic = frontShowAllMusic,
                backShowAllMusic = backShowAllMusic,
                frontShowAllList = frontShowAllList,
                backShowAllList = backShowAllList,
            )
        }
    }

    protected val onSecondEvery: DSackT<() -> Unit, Int>
        get() = toCatchSack(44) {
            mediaPlay?.apply {
                if (context != null && !isProgressLoading && isPlaying) {
                    try {
                        mediaPosLongRun(lastProgress).let {
                            lastProgress = it
                            musicProgress = it
                            rootMain?.apply {
                                pro.setProgress(it.toFloat())
                            }
                            MusicObject.activityListener?.updateProgress(it.toFloat())

                            onSecondEvery.rKTSack(250L).postAfter()
                        }
                    } catch (_: java.lang.IllegalStateException) {

                    } catch (_: java.lang.RuntimeException) {

                    } catch (_: Throwable) {

                    }
                } else if (!isPlaying) {
                    return@apply
                }
            }
            Unit
        }

    internal inline val MutableList<MusicSack>.convertToSack: DSack<MutableList<String>, MutableList<String>, MutableList<String?>>
        get() {
            return mutableListOf<String>().let { oneTitle ->
                mutableListOf<String>().let { twoPath ->
                    mutableListOf<String?>().let { threeArtist ->
                        this@convertToSack.onEach {
                            it.title?.let { tit ->
                                it.pathAudio?.let { path ->
                                    it.artist.let { art ->
                                        oneTitle.add(tit)
                                        twoPath.add(path)
                                        threeArtist.add(art)
                                    }
                                }
                            }
                        }.let {
                            DSack(one = oneTitle, two = twoPath, three = threeArtist)
                        }
                    }
                }
            }
        }

    override var app: android.app.Application? = null

    protected fun <C : androidx.work.ListenableWorker> android.content.Context.sendWork(
        pos: Int,
        lastProgress: Long,
        pathAudio: String?,
        cla: Class<C>,
    ) {
        catchy(Unit) {
            androidx.work.Data.Builder()
                .putString(MUSIC_SONG_KEY, pathAudio)
                .putInt(MUSIC_POS_KEY, pos)
                .putLong(MUSIC_PROGRESS_KEY, lastProgress)
                .build().apply {
                    cla.simpleName.also { name ->
                        androidx.work.OneTimeWorkRequest.Builder(
                            cla
                        ).setInputData(this@apply).addTag(name).build().also {
                            com.pt.pro.main.odd.WorkInitializer().create(
                                app?.applicationContext ?: this@sendWork.applicationContext
                            ).enqueueUniqueWork(
                                "MusicWorker",
                                androidx.work.ExistingWorkPolicy.REPLACE,
                                it
                            )
                        }
                    }
                }
        }
    }

    private val pauseHand: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            toCatchSack(7923) {
                lastPauseTime = System.currentTimeMillis()
                mediaPlay?.pause()
            }.postNow()
        }

    private fun DSack<Int, Int, Int>.launchMusicActivity(musicSack: MusicSack) {
        //toCatchSack(158) {
        launchDef {
            justScope {
                (ctxM.findIntegerPreference(
                    INTERRUPT_MUSIC,
                    30
                ).toLong() * 60L * 1000L).let { interruptTime ->
                    pauseHand.rKTSack(interruptTime).postBackAfter()
                }
            }
            justScope {
                if (ctxM.findBooleanPreference(MUSIC_SCREEN, true) && !MusicObject.inDisplay
                ) {
                    ctxM.newIntentSus(MusicActivity::class.java) {
                        applySusBack {
                            flags = android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP or
                                    android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP or
                                    android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    android.content.Intent.FLAG_ACTIVITY_NEW_TASK

                            putExtra(MUSIC_OBJECT, musicSack)
                            putExtra(MUSIC_COLOR_ONE, one)
                            putExtra(MUSIC_COLOR_TWO, two)
                            putExtra(MUSIC_COLOR_THREE, three)
                        }
                        mediaPlay?.alsoSusBack {
                            putExtra(MUSIC_IS_PLAY, it.isPlayingSus())
                            putExtra(MUSIC_MAX, it.durationSus().toFloat())
                            putExtra(MUSIC_PROGRESS, it.currentPositionSus().toFloat())
                        }
                        return@newIntentSus this@newIntentSus
                    }.alsoSusBack {
                        ctxM.startActivity(it)
                    }
                }
            }
        }
        //    Unit
        //}.postNow()
    }


    @com.pt.common.global.MainAnn
    protected fun android.content.Context?.onDestroyGlobal(isInShow: Boolean, b: () -> Unit) {
        kotlin.runCatching {
            rootMain?.circleMusic?.onViewDestroy()
            currentMusic.let {
                allMusicOriginal.fetchIndex(it ?: return@let currPos, currPos)
            }.let {
                currPos = it
                MusicObject.musicList = allMusicOriginal.toMutableList()
            }
            rootDetails.apply {
                catchy(Unit) {
                    if (isInShow) {
                        context.catchyBadToken {
                            windManager?.removeViewImmediate(this@apply?.root_)
                        }
                    } else return@catchy
                }
            }
            rootMain.apply {
                catchy(Unit) {
                    if (isInShow) {
                        context.catchyBadToken {
                            windManager?.removeViewImmediate(this@apply?.root_)
                        }
                    } else return@catchy
                }
            }
            song?.bitmap = null
            musicAnimation?.end()
            musicAda?.onAdapterDestroy()
            layoutRec?.apply {
                recyclerSongs.apply {
                    adapter = null
                    removeAllViewsInLayout()
                }
                root_.apply {
                    removeAllViewsInLayout()
                }
            }
            rootMain?.apply {
                circleMusic.onViewDestroy()
                pro.onViewDestroy()
            }
            layoutRec?.apply {
                searchMusic.onViewDestroy()
                stubPlaylist?.playlistEdit?.onViewDestroy()
            }
            stubPlaylist?.root_?.removeAllViewsInLayout()
            searchJob?.cancelJob()

            this?.sendWork(currPos, lastProgress, currentMusic?.pathAudio, com.pt.pro.musical.back.MusicWorker::class.java)

            jobPreviousSong?.cancelJob()
            jobPosForShuffle?.cancelJob()
            jobItemChanged?.cancelJob()
            jobPushMusic?.cancelJob()
            jobNative?.cancelJob()
            catchy(Unit) {
                dispatcherNative?.close()
            }
            extNative?.shutdownNow()
            nullFunction(b)
        }.onFailure {
            b.invoke()
            it.toStr.logProvCrash("onDestroyService")
        }
    }


    @com.pt.common.global.MainAnn
    protected suspend fun android.content.Context?.onDestroyGlobalSus(
        isInShow: Boolean,
        b: () -> Unit
    ) {
        withMain {
            kotlin.runCatching {
                rootMain?.circleMusic?.onViewDestroy()
                currentMusic.let {
                    allMusicOriginal.fetchIndex(it ?: return@let currPos, currPos)
                }.let {
                    currPos = it
                    MusicObject.musicList = allMusicOriginal.toMutableList()
                }
                rootDetails.apply {
                    catchy(Unit) {
                        if (isInShow) {
                            context.catchyBadToken {
                                windManager?.removeViewImmediate(this@apply?.root_)
                            }
                        } else return@catchy
                    }
                }
                rootMain.apply {
                    catchy(Unit) {
                        if (isInShow) {
                            context.catchyBadToken {
                                windManager?.removeViewImmediate(this@apply?.root_)
                            }
                        } else return@catchy
                    }
                }
                song?.bitmap = null
                musicAnimation?.end()
                musicAda?.onAdapterDestroy()
                layoutRec?.apply {
                    recyclerSongs.apply {
                        adapter = null
                        removeAllViewsInLayout()
                    }
                    root_.apply {
                        removeAllViewsInLayout()
                    }
                }
                rootMain?.apply {
                    circleMusic.onViewDestroy()
                    pro.onViewDestroy()
                }
                layoutRec?.apply {
                    searchMusic.onViewDestroy()
                    stubPlaylist?.playlistEdit?.onViewDestroy()
                }
                searchJob?.cancelJob()
                this@onDestroyGlobalSus?.sendWork(
                    currPos,
                    lastProgress,
                    currentMusic?.pathAudio,
                    com.pt.pro.musical.back.MusicWorker::class.java
                )
                jobPreviousSong?.cancelJob()
                jobPosForShuffle?.cancelJob()
                jobItemChanged?.cancelJob()
                jobPushMusic?.cancelJob()
                jobNative?.cancelJob()
                catchy(Unit) {
                    dispatcherNative?.close()
                }
                extNative?.shutdownNow()

                nullFunction(b)
            }.onFailure {
                b.invoke()
                it.toStr.logProvCrash("onDestroyService")
            }
        }
    }

    private fun nullFunction(b: () -> Unit) {
        song = null
        stubPlaylist = null
        audioListener = null
        mediaSessionNative = null
        musicAnimation = null
        musicAda = null
        layoutRec = null
        extNative = null
        jobNative = null
        dispatcherNative = null
        powerManagerNative = null
        context = null
        contextMedia = null
        notify = null
        windManager = null
        rootDetails = null
        rootMain = null
        recyclerManager = null
        mediaPlay = null
        allMusicNative = null
        searchListNative = null
        isFromCreate = null
        audioManager = null
        changMusic = null
        searchJob = null
        currentMusic = null
        allMusicOriNative = null
        app = null
        jobPreviousSong = null
        jobPosForShuffle = null
        jobItemChanged = null
        jobPushMusic = null
        fetchMusicService = null
        b.invoke()
    }

}