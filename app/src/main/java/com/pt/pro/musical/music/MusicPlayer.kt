package com.pt.pro.musical.music

import com.pt.common.global.*
import com.pt.common.media.fetchAudioLoader
import com.pt.common.stable.*
import kotlinx.coroutines.asCoroutineDispatcher

class MusicPlayer : androidx.media3.session.MediaLibraryService(), androidx.lifecycle.LifecycleOwner, kotlinx.coroutines.CoroutineScope {

    internal var jobNative: kotlinx.coroutines.Job? = null
    internal var extNative: java.util.concurrent.ExecutorService? = null
    internal var dispatcherNative: kotlinx.coroutines.ExecutorCoroutineDispatcher? = null

    internal inline val job: kotlinx.coroutines.Job
        get() = jobNative ?: kotlinx.coroutines.SupervisorJob().also { jobNative = it }

    internal inline val ext: java.util.concurrent.ExecutorService
        get() = extNative ?: fetchExtractor.also { extNative = it }

    internal inline val dispatch: kotlinx.coroutines.ExecutorCoroutineDispatcher
        get() = dispatcherNative ?: ext.asCoroutineDispatcher().also { dispatcherNative = it }

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = dispatch + job + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    private var lastStartId: Int = -1

    private val mDispatcher: androidx.lifecycle.ServiceLifecycleDispatcher = androidx.lifecycle.ServiceLifecycleDispatcher(this)

    private var fetchHandlerNative: java.util.concurrent.ScheduledExecutorService? = null

    private inline val fetchHand: java.util.concurrent.ScheduledExecutorService
        get() = fetchHandlerNative ?: java.util.concurrent.Executors.newSingleThreadScheduledExecutor().also {
            fetchHandlerNative = it
        }

    private inline val mediaLibraryService: () -> MusicPlayer
        get() = {
            this
        }

    private inline val android.content.Intent.fetchMusicList: MutableList<MusicSack>?
        get() = if (isV_T) {
            getParcelableArrayListExtra(ALL_SONGS, MusicSack::class.java)
        } else {
            @Suppress("DEPRECATION") getParcelableArrayListExtra(ALL_SONGS)
        }

    private var isInti: Int = 0 //0 destroyed // -1 createInti // 1 done

    override fun onBind(intent: android.content.Intent?): android.os.IBinder? {
        mDispatcher.onServicePreSuperOnBind()
        return super.onBind(intent)
    }

    @Suppress("DEPRECATION", "OverrideDeprecatedMigration", "OVERRIDE_DEPRECATION")
    override fun onStart(intent: android.content.Intent?, startId: Int) {
        mDispatcher.onServicePreSuperOnStart()
        super.onStart(intent, startId)
    }

    override fun onCreate() {
        fetchStyle.apply(baseContext::setTheme)
        mDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
        createNotificationChannel()
        fetchDefaultMusicNotify().apply {
            if (isV_T) {
                try {
                    startFor()
                } catch (e: android.app.ForegroundServiceStartNotAllowedException) {
                    stopSelf()
                }
            } else {
                startFor()
            }
        }
        fetchMusicForCreate()
    }

    private fun android.app.Notification.startFor() {
        if (isV_Q) {
            startForeground(
                SERVICE_MUSIC,
                this,
                android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            startForeground(SERVICE_MUSIC, this)
        }
    }

    override fun onStartCommand(intent: android.content.Intent?, flags: Int, startId: Int): Int {
        lastStartId = startId
        launchDef {
            justScope {
                if (null != intent?.getStringExtra(KEY_ORDER)) {
                    when (intent.action ?: return@justScope) {
                        CLOSE_MUSIC -> dismissMediaCircle()
                        PLAY_MUSIC, UPDATE_MUSIC -> {
                            justScope {
                                if (isInti == 0) {
                                    withMain {
                                        MusicObject.musicManagerListener?.onServiceDestroy { }
                                        MusicObject.musicManagerListener = null
                                    }
                                }
                                isInti = 1
                            }
                            intent.fetchMusicList.fetchMusicPlayerManager(intent.fetchMusicPos()) {
                                updatePrefInt(ID_APP, MUSIC)
                            }
                        }
                        else -> {
                            MusicObject.musicManagerListener?.onUpdateService(intent.action)
                        }
                    }
                }
            }
            justScope {
                super.onStartCommand(intent, flags, startId)
            }
        }
        return START_STICKY
    }

    private fun fetchMusicForCreate() {
        if (MusicObject.musicManagerListener?.fetchSession() != null) return
        checkPre {
            newMusicManager {
                intiPlayer().let {
                    setNotificationProvider()
                    if (!catchy(false) { sessions.any { s -> s.id == it.id } }) {
                        catchyUnit {
                            addSession(it)
                        }
                    }
                    isInti = -1
                    inti(true)
                    fetchHand.schedule({
                        if (isInti == -1 && baseContext != null) {
                            launchDef {
                                if (allMusicOriNative.isNullOrEmpty()) {
                                    openMusicPlaylist {
                                        this@openMusicPlaylist?.setOrgAllSongs(
                                            findIntegerPreference(
                                                MUSIC_POS_KEY,
                                                0
                                            )
                                        )
                                        baseContext.nullChecker()
                                        justCoroutine {
                                            updateViewRoot()
                                        }
                                    }
                                } else {
                                    updateViewRoot()
                                }
                            }
                        }
                        fetchHandlerNative?.shutdown()
                        fetchHandlerNative = null
                    }, 3000L, java.util.concurrent.TimeUnit.MILLISECONDS)
                }
                MusicObject.musicManagerListener = this
            }
        }
    }

    private fun checkPre(a: () -> Unit) {
        MusicObject.musicManagerListener?.onServiceDestroy {}
        MusicObject.musicManagerListener = null
        if (!applicationContext.hasExternalReadWritePermission || !applicationContext.canFloat) {
            android.content.Intent(
                baseContext,
                com.pt.pro.main.former.MainActivity::class.java
            ).apply {
                action = android.content.Intent.ACTION_VIEW
                flags = BACKGROUND_FLAGS
                putExtra(PLAY_MUSIC, true)
            }.run(::startActivity)
        } else a.invoke()
    }

    private suspend fun MutableList<MusicSack>?.fetchMusicPlayerManager(
        pos: Int, b: suspend com.pt.pro.musical.interfaces.MusicOption. () -> Unit
    ) {
        MusicObject.musicManagerListener.also { mo ->
            if (mo == null) {
                baseContext.newMusicManagerSus {
                    catchySus(Unit) {
                        justCoroutine {
                            intiPlayer().let {
                                setNotificationProvider()
                                if (!catchy(false) { sessions.any { s -> s.id == it.id } }) {
                                    catchyUnit {
                                        addSession(it)
                                    }
                                }
                            }
                        }
                        inti(false)
                        intiPlayerList(this@fetchMusicPlayerManager, pos)
                        justCoroutine {
                            MusicObject.musicManagerListener = this@newMusicManagerSus
                            b.invoke(this@newMusicManagerSus)
                        }
                        return@catchySus
                    }
                }
            } else {
                catchySus(Unit) {
                    mo.applySus {
                        if (qHand == null) {
                            inti(false)
                            intiPlayerList(this@fetchMusicPlayerManager, pos)
                            b.invoke(mo)
                        } else {
                            this@fetchMusicPlayerManager?.alsoSus {
                                it.pushNewSongs(if (pos > it.lastIndex) 0 else pos, true)
                            }
                        }
                    }
                    return@catchySus
                }
            }
        }
    }

    private suspend fun com.pt.pro.musical.interfaces.MusicOption.intiPlayerList(
        musicList: MutableList<MusicSack>?, pos: Int
    ) {
        if (musicList != null && musicList.isNotEmpty()) {
            musicList.setOrgAllSongs(pos)
            baseContext.nullChecker()
            justCoroutine {
                updateViewRoot()
            }
        } else {
            openMusicPlaylist {
                this@openMusicPlaylist?.setOrgAllSongs(pos)
                baseContext.nullChecker()
                justCoroutine {
                    updateViewRoot()
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    internal fun android.content.Context.newMusicManager(
        @com.pt.common.global.UiAnn mm: MusicPlayerManager.() -> Unit,
    ) {
        catchy(Unit) {
            fetchOverlayContext.apply<@androidx.annotation.UiContext android.content.Context> ctx@{
                MusicPlayerManager(
                    this@ctx,
                    fetchMediaContext,
                ).apply {
                    app = application
                    fetchMusicService = mediaLibraryService
                    com.pt.pro.musical.fasten.MusicInflater.apply {
                        this@ctx.inflaterMusicHead().setViewRoot()
                        this@ctx.inflaterMusicHeadPlayer().setViewRoot2()
                    }
                    setInit()
                    fetchSystemService(windowService)?.also {
                        it.setWindowManager()
                    }
                }.also(mm)
            }
        }
    }


    @com.pt.common.global.UiAnn
    internal suspend fun android.content.Context.newMusicManagerSus(
        @com.pt.common.global.UiAnn mm: suspend MusicPlayerManager.() -> Unit,
    ) {
        withMain {
            fetchOverlayContext.applySus<@androidx.annotation.UiContext android.content.Context> ctx@{
                MusicPlayerManager(
                    this@ctx,
                    fetchMediaContext,
                ).applySus {
                    app = application
                    fetchMusicService = mediaLibraryService
                    com.pt.pro.musical.fasten.MusicInflater.apply {
                        this@ctx.inflaterMusicHead().setViewRoot()
                        this@ctx.inflaterMusicHeadPlayer().setViewRoot2()
                    }
                    setInit()
                    fetchSystemService(windowService)?.also {
                        it.setWindowManager()
                    }
                }.alsoSus(mm)
            }
        }
    }

    private inline val android.content.Intent.fetchMusicPos: suspend () -> Int
        get() = {
            getIntExtra(POPUP_POS, -1).let {
                if (it == -1) {
                    findIntegerPreference(MUSIC_POS_KEY, 0)
                } else it
            }
        }

    private suspend inline fun openMusicPlaylist(
        crossinline f: suspend MutableList<MusicSack>?.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            newDBPlaylist {
                getAllSongsDefault()
            }
        }.runSusBack {
            withBack {
                if (this@runSusBack.isNotEmpty()) {
                    this@runSusBack.letSusBack(f)
                } else {
                    contentResolver.fetchAudioLoader()?.let {
                        mutableListOf(it).letSusBack(f)
                    } ?: deviceHaveNoMedia()
                }
            }
        }
    }

    override val lifecycle: androidx.lifecycle.Lifecycle get() = mDispatcher.lifecycle

    override fun onGetSession(
        controllerInfo: androidx.media3.session.MediaSession.ControllerInfo
    ): MediaLibrarySession? = MusicObject.musicManagerListener?.fetchSession()


    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun setNotificationProvider() {
        setMediaNotificationProvider(
            object : androidx.media3.session.MediaNotification.Provider {
                override fun createNotification(
                    session: androidx.media3.session.MediaSession,
                    customLayout: com.google.common.collect.ImmutableList<androidx.media3.session.CommandButton>,
                    actionFactory: androidx.media3.session.MediaNotification.ActionFactory,
                    onNotificationChangedCallback: androidx.media3.session.MediaNotification.Provider.Callback
                ): androidx.media3.session.MediaNotification = kotlin.run {
                    if (isInti == 0) {
                        fetchDefaultMusicNotify().fetchMediaNotify
                    } else {
                        MusicObject.musicManagerListener?.fetchNotify()
                            ?: fetchDefaultMusicNotify().fetchMediaNotify
                    }
                }

                override fun handleCustomCommand(
                    session: androidx.media3.session.MediaSession,
                    action: String,
                    extras: android.os.Bundle
                ): Boolean = true
            }
        )
    }

    internal inline val android.app.Notification.fetchMediaNotify: androidx.media3.session.MediaNotification
        get() = androidx.media3.session.MediaNotification(SERVICE_MUSIC, this@fetchMediaNotify)

    internal fun fetchDefaultMusicNotify(): android.app.Notification {
        return androidx.core.app.NotificationCompat.Builder(
            this,
            CH_MUS
        ).apply {
            setSmallIcon(com.pt.pro.R.drawable.ic_launcher_foreground)
            color = fetchColor(com.pt.pro.R.color.cac)
            closeAction.let {
                androidx.core.app.NotificationCompat.Action.Builder(
                    com.pt.pro.R.drawable.ic_close,
                    getString(com.pt.pro.R.string.co),
                    it
                ).build().let(::addAction)
            }
            doneAction.let {
                androidx.core.app.NotificationCompat.Action.Builder(
                    com.pt.pro.R.drawable.ic_play_circle,
                    getString(com.pt.pro.R.string.av),
                    it
                ).build().let(::addAction)
            }
            setContentTitle(resources.getString(com.pt.pro.R.string.zh))
            setCategory(androidx.core.app.NotificationCompat.CATEGORY_SERVICE)
            priority = androidx.core.app.NotificationCompat.PRIORITY_HIGH or
                    androidx.core.app.NotificationCompat.PRIORITY_MAX
            foregroundServiceBehavior = androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
        }.build().also {
            it.flags = androidx.core.app.NotificationCompat.FLAG_FOREGROUND_SERVICE or
                    androidx.core.app.NotificationCompat.FLAG_NO_CLEAR
        }
    }

    private inline val doneAction: android.app.PendingIntent
        get() {
            return newIntent(MusicPlayer::class.java) {
                action = PLAY_MUSIC
                putExtra(KEY_ORDER, "tillITHurts")
                this@newIntent
            }.let {
                android.app.PendingIntent.getService(
                    this@MusicPlayer,
                    0,
                    it,
                    PEND_FLAG
                )
            }
        }

    private inline val closeAction: android.app.PendingIntent
        get() {
            return newIntent(MusicPlayer::class.java) {
                putExtra(KEY_ORDER, "tillITHurts")
                action = CLOSE_MUSIC
                this@newIntent
            }.let {
                android.app.PendingIntent.getService(
                    this@MusicPlayer,
                    0,
                    it,
                    PEND_FLAG
                )
            }
        }

    private fun android.content.Context.createNotificationChannel() {
        androidx.core.app.NotificationChannelCompat.Builder(
            CH_MUS,
            androidx.core.app.NotificationManagerCompat.IMPORTANCE_MAX
        ).apply {
            setName(resources.getString(com.pt.pro.R.string.mk))
            setDescription(resources.getString(com.pt.pro.R.string.kp))
            setShowBadge(false)
            setSound(null, null)
            setLightsEnabled(false)
            setImportance(
                androidx.core.app.NotificationManagerCompat.IMPORTANCE_LOW or
                        androidx.core.app.NotificationManagerCompat.IMPORTANCE_MIN
            )
            setVibrationEnabled(false)
        }.build().let {
            androidx.core.app.NotificationManagerCompat.from(this).apply {
                createNotificationChannel(it)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        MusicObject.musicManagerListener?.onChange(newConfig.orientation)
    }

    private suspend fun deviceHaveNoMedia() {
        withMain {
            baseContext.makeToastRecSus(com.pt.pro.R.string.hw, 1)
            dismissMediaCircle()
        }
    }

    private fun dismissMediaCircle() {
        isInti = 0
        fetchHandlerNative?.shutdown()
        fetchHandlerNative = null
        if (isV_N) {
            stopForeground(android.app.Service.STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        if (MusicObject.musicManagerListener != null) {
            launchImdMain {
                MusicObject.musicManagerListener?.applySus {
                    onServiceDestroySus {
                        stopSelfResult(lastStartId)
                        com.pt.pro.musical.fasten.MusicInflater.destroyFasten()
                        MusicObject.musicManagerListener = null
                        lastStartId = -1
                    }
                }
                MusicObject.activityListener?.forFinish()
            }
        } else {
            MusicObject.activityListener?.forFinish()
            stopSelfResult(lastStartId)
            fetchSystemService(notificationService).also { nm ->
                nm?.cancel(SERVICE_MUSIC)
            }
            lastStartId = -1
        }
    }

    override fun onDestroy() {
        isInti = 0
        lastStartId = -1
        fetchHandlerNative?.shutdown()
        fetchHandlerNative = null
        catchyUnit {
            MusicObject.musicManagerListener?.apply {
                onServiceDestroy {
                    com.pt.pro.musical.fasten.MusicInflater.destroyFasten()
                    MusicObject.musicManagerListener = null
                }
            }
            MusicObject.activityListener?.forFinish()
        }
        mDispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()

        jobNative?.cancelJob()
        catchy(Unit) {
            dispatcherNative?.close()
        }
        extNative?.shutdownNow()
        cancelScope()
        extNative = null
        jobNative = null
        dispatcherNative = null
    }

}