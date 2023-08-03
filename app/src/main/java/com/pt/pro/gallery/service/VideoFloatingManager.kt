package com.pt.pro.gallery.service

import com.pt.common.global.*
import com.pt.common.media.getVideoRatio
import com.pt.common.stable.*

open class VideoFloatingManager(
    ctx: android.content.Context, private var contextMedia: android.content.Context?
) : com.pt.common.mutual.life.GlobalServiceManger(ctx), android.view.SurfaceHolder.Callback, com.pt.pro.gallery.interfaces.VideoFloatingListener {

    override val floPlayer: androidx.media3.exoplayer.ExoPlayer
        @com.pt.common.global.MainAnn get() = mediaPlay!!

    override val viewVideo: VideoFloatFasten get() = viewRt!!

    private var mediaPlay: androidx.media3.exoplayer.ExoPlayer? = null
    private var viewRt: VideoFloatFasten? = null
    private var params: android.view.WindowManager.LayoutParams? = null
    private var previousSeek: com.pt.pro.gallery.objects.SeekVideo? = null
    private var position: Int = 0

    private var allVideosNative: MutableList<MediaSack>? = mutableListOf()
    private inline val allVideos: MutableList<MediaSack>
        get() = allVideosNative ?: mutableListOf<MediaSack>().also {
            allVideosNative = it
        }

    private var video: MediaSack? = null

    private var videoRatio = 0f
    private var hide = false
    private var half = 0
    private var fullScreen = false
    private var widthV = 0
    private var heightV = 0
    private var widthOriginal = 0
    private var heightOriginal = 0
    private var screenWidth: Int? = null
    private var screenHeight: Int? = null
    private inline fun musicDim(a: (wid: Int, hei: Int) -> Unit) {
        screenWidth.let { w ->
            screenHeight.let { h ->
                if (w != null && h != null) {
                    a.invoke(w, h)
                } else {
                    ctxS.fetchDimensions {
                        a.invoke(width, height)
                        screenWidth = this@fetchDimensions.width
                        screenHeight = this@fetchDimensions.height
                    }
                }
            }
        }
    }

    private var lastProgress = 0L

    private var showSeekBoolean = false
    private var isRealPlay = true
    private var isEmptySong = false
    private var isError = false
    private var windowManager: android.view.WindowManager? = null
    private var beforeYouGo = 0

    private var isInti: Boolean = false


    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    @Volatile
    private var powerManagerNative: android.os.PowerManager? = null
    private inline val android.content.Context.powerManager: android.os.PowerManager?
        get() = powerManagerNative ?: fetchSystemService(powerService).also {
            powerManagerNative = it
        }

    private val isScreenOn: Boolean?
        get() = kotlin.runCatching {
            try {
                context?.powerManager?.isInteractive
            } catch (e: RuntimeException) {
                null
            }
        }.getOrNull()

    private var isOn: Boolean = true

    private val runPower: DSack<() -> Unit, Int, Long>
        get() = toCatchSackAfter(8448, 2000L) {
            isScreenOn?.also {
                if (it != isOn && !it) {
                    toCatchSackAfter(594, 300L) {
                        if (context != null) {
                            onChange(0)
                        }
                        Unit
                    }.postAfter()
                }
                isOn = it
            }
            runPower.postBackAfter()
        }

    private inline val Int.heightVideo: Int get() = (this.toFloat() / videoRatio).toInt()

    private inline val previousVideo: MediaSack
        get() {
            return if (position == 0) {
                allVideos.lastIndex
            } else {
                (position - 1)
            }.let {
                position = it
                allVideos[it]
            }
        }

    private inline val getNextVideo: MediaSack
        get() {
            return if (position == allVideos.size - 1) {
                0
            } else {
                (position + 1)
            }.let {
                position = it
                allVideos[it]
            }
        }

    @com.pt.common.global.MainAnn
    override fun setInit() {
        setParams()
    }

    @com.pt.common.global.MainAnn
    override fun VideoFloatFasten.setViewRoot() {
        viewRt = this
    }

    override val getViewRoot: android.widget.FrameLayout get() = viewVideo.root

    override val getViewRoot2: android.view.View get() = viewVideo.root

    override val fetchParams: android.view.WindowManager.LayoutParams
        get() = params ?: floatingVideoManger().also {
            params = it
        }

    @com.pt.common.global.MainAnn
    override fun android.view.WindowManager.setWindowManager() {
        windowManager = this
    }

    @com.pt.common.global.MainAnn
    override fun MediaSack.setCurrentVideo() {
        video = this
    }

    @com.pt.common.global.MainAnn
    override fun MutableList<MediaSack>.setAllVideos() {
        allVideos.addAll(this)
    }


    @com.pt.common.global.MainAnn
    private fun setParams() {
        params = floatingVideoManger()
        ctxS.fetchDimensions {
            screenWidth = this@fetchDimensions.width
            screenHeight = this@fetchDimensions.height
            fetchParams.softInputMode = android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
            fetchParams.gravity = android.view.Gravity.CENTER
            video?.let {
                if (it.mediaWidth != 0 && it.mediaHigh != 0) {
                    videoRatio = (video ?: return).run {
                        (mediaWidth.toFloat() / mediaHigh.toFloat())
                    }
                    widthV = kotlin.run {
                        if (videoRatio > 1.0F) {
                            (this@fetchDimensions.width * 0.4).toInt()
                        } else {
                            (this@fetchDimensions.width * 0.3).toInt()
                        }
                    }
                    heightV = widthV.heightVideo
                    widthOriginal = widthV
                    heightOriginal = heightV
                    fetchParams.width = widthV
                    fetchParams.height = heightV

                    android.widget.FrameLayout.LayoutParams(widthV, heightV).apply {
                        viewVideo.floatingVideo.layoutParams = this
                    }
                    android.widget.FrameLayout.LayoutParams(
                        widthV, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = android.view.Gravity.CENTER_VERTICAL
                        viewVideo.constraintButtons.layoutParams = this
                    }
                }
            }
        }
    }

    override fun updateViewRoot() {
        launchImdMain {
            withMain {
                viewVideo.close.setOnClickListener(this@VideoFloatingManager)
                viewVideo.floatingVideo.apply {
                    if (holder != null) {
                        requestLayout()
                        intiVideoFloating(holder)
                        isInti = true
                    } else {
                        requestLayout()
                        viewVideo.floatingVideo.holder?.addCallback(this@VideoFloatingManager)
                    }
                }
                runPower.postBackAfter()
            }
        }
    }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private suspend fun intiVideoFloating(holder: android.view.SurfaceHolder) {
        withMain {
            half = 100
            mediaPlay = androidx.media3.exoplayer.ExoPlayer.Builder(contextMedia ?: ctxS).apply {
                setAudioAttributes(musicAudioAttr, true)
                setHandleAudioBecomingNoisy(true)
                setWakeMode(androidx.media3.common.C.WAKE_MODE_LOCAL)
            }.build().also {
                it.setForegroundMode(true)
                it.playWhenReady = true
                it.setVideoSurfaceHolder(holder)
                videoCall?.let { it1 -> it.addListener(it1) }
            }
            with(viewVideo) {
                root_.apply {
                    touchListener?.setContactListener(false)
                }
                seekBarFloating.onSeekListener { it, mode ->
                    when (mode) {
                        1 -> {
                            viewRt?.seekBarFloating?.handler?.removeCallbacksAndMessages(null)
                        }
                        0 -> {
                            lastProgress = it.toLong()
                            currentDurationFloating.run {
                                base = android.os.SystemClock.elapsedRealtime() - it
                            }
                        }
                        else -> {
                            viewRt?.seekBarFloating?.handler?.postDelayed(
                                onEverySecond, 100
                            )
                            floPlayer.apply {
                                seekTo(it.toLong())
                                if (!isPlaying) play()
                            }
                        }
                    }
                }
                position = allVideos.indexOfFirst {
                    it.pathMedia == video?.pathMedia
                }
                previous.setOnClickListener(this@VideoFloatingManager)
                next.setOnClickListener(this@VideoFloatingManager)
                playFloating.setOnClickListener(this@VideoFloatingManager)
                scaled.setOnClickListener(this@VideoFloatingManager)
                video?.let { videoInti(it) }
            }
        }
    }

    private fun MediaSack.pushVideo() {
        launchDef {
            doPushVideo()
        }
    }

    private suspend fun MediaSack.doPushVideo() {
        if (!isEmptySong) {
            isEmptySong = true
            withMain {
                floPlayer.stop()
            }
            withMain {
                floPlayer.clearMediaItems()
            }
            withMain {
                viewVideo.videoInti(this@doPushVideo)
            }
        }
        withMain {
            runSong.postAfter()
        }
    }

    private val runSong: DSack<() -> Unit, Int, Long>
        get() = toCatchSackAfter(68, 300L) {
            isEmptySong = false
        }

    private suspend fun VideoFloatFasten.videoInti(u: MediaSack) {
        seekBarFloating.handler?.removeCallbacksAndMessages(null)
        floPlayer.applySus {
            withMain {
                androidx.media3.common.MediaItem.Builder().setUri(
                    (u.uriMedia ?: u.pathMedia).toUri
                ).build().letSus(::setMediaItem)
                repeatMode = androidx.media3.common.Player.REPEAT_MODE_OFF
            }
            prepareSus()
        }
    }

    private suspend fun VideoFloatFasten.afterPrepare(u: MediaSack, duration: Long) {
        if (context != null) {
            videoRatio = fetchVideoRatio(u).letSusBack {
                getVideoRatio(u, it)
            }
            withMain {
                musicDim { wid, _ ->
                    mediaPlay?.applySus {
                        widthOriginal = kotlin.run {
                            if (videoRatio > 1.0F) {
                                (wid * 0.4).toInt()
                            } else {
                                (wid * 0.3).toInt()
                            }
                        }
                        heightOriginal = widthOriginal.heightVideo
                        heightV = widthV.heightVideo
                        viewVideo.updateFloatingVideo(widthV, heightV)
                        seekBarFloating.max = duration.toInt()
                        seekBarFloating.handler?.postDelayed(onEverySecond, 100)
                        if (previousSeek != null) updateSeek(currentPosition)
                        previousSeek = getCurrentSeek(u.nameMedia.toStr)
                        durationFloating.text = duration.findMediaDuration
                    }
                }
            }
        }
    }


    internal suspend fun doTrackChanged() {
        withMain {
            isError = false
            mediaPlay?.applySus {
                play()
                allVideos[position].letSus { u ->
                    video = u
                    viewVideo.afterPrepare(u, durationSus())
                }
            }
        }
    }

    private var videoCall: androidx.media3.common.Player.Listener? = object : androidx.media3.common.Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == androidx.media3.common.Player.STATE_ENDED) {
                playCompleted()
            }
        }

        override fun onTracksChanged(tracks: androidx.media3.common.Tracks) {
            super.onTracksChanged(tracks)
            launchDef {
                doTrackChanged()
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            launchDef {
                doPlayChanged(isPlaying)
            }
        }

        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
            super.onPlayerError(error)
            viewRt?.seekBarFloating?.handler?.removeCallbacksAndMessages(null)
            ctxS.makeToastRec(com.pt.pro.R.string.uy, 0)
            isError = true
        }

        override fun onPlayerErrorChanged(error: androidx.media3.common.PlaybackException?) {
            super.onPlayerErrorChanged(error)
            viewRt?.seekBarFloating?.handler?.removeCallbacksAndMessages(null)
            ctxS.makeToastRec(com.pt.pro.R.string.uy, 0)
            isError = true
        }

    }

    internal suspend fun doPlayChanged(isPlaying: Boolean) {
        withMain {
            if (isPlaying) {
                viewRt?.seekBarFloating?.handler?.postDelayed(onEverySecond, 100)
                ctxS.compactImage(com.pt.pro.R.drawable.ic_pause) {
                    viewRt?.playFloating?.setImageDrawable(this@compactImage)
                }
            } else {
                viewRt?.seekBarFloating?.handler?.removeCallbacksAndMessages(null)
                ctxS.compactImage(com.pt.pro.R.drawable.ic_play) {
                    viewRt?.playFloating?.setImageDrawable(this@compactImage)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun playCompleted() {
        floPlayer.apply {
            seekTo(1L)
            pause()
        }
        viewVideo.updatePosition()
    }

    private fun stopPlayer() {
        if (floPlayer.isPlaying) {
            isRealPlay = false
            floPlayer.pause()
        }
    }

    private fun androidx.media3.exoplayer.ExoPlayer.playSong() {
        isRealPlay = true
        play()
    }

    private suspend fun VideoFloatFasten.getCurrentSeek(
        currentVideo: String
    ): com.pt.pro.gallery.objects.SeekVideo {
        return if (context != null) {
            ctxS.newDBGallerySus {
                getOneSeek(currentVideo)
            }.let {
                withMain {
                    floPlayer.apply {
                        seekTo(it.seekTime)
                        currentPosition.let { p ->
                            seekBarFloating.progress = p.toInt()
                            currentDurationFloating.base = android.os.SystemClock.elapsedRealtime() - p
                        }
                    }
                }
                it
            }
        } else {
            com.pt.pro.gallery.objects.SeekVideo()
        }
    }

    private fun updateSeek(currentPosition: Long) {
        previousSeek?.seekTime = currentPosition
        ctxS.newDBGallery {
            previousSeek?.updateVideo()
        }
    }

    private val onEverySecond: () -> Unit by lazy {
        return@lazy {
            catchy(Unit) {
                viewRt?.apply {
                    updatePosition()
                    seekBarFloating.handler?.postDelayed(onEverySecond, 1000)
                }
            }
        }
    }

    private suspend fun VideoFloatFasten.fetchVideoRatio(
        m: MediaSack,
    ): Float = justCoroutine {
        if (m.mediaWidth != 0 && m.mediaHigh != 0) {
            m.mediaWidth.toFloat() / m.mediaHigh.toFloat()
        } else {
            floatingVideo.width.toFloat() / floatingVideo.height.toFloat()
        }
    }

    @com.pt.common.global.UiAnn
    private fun VideoFloatFasten.updatePosition() {
        runCatching {
            floPlayer.mediaPosLongRun(lastProgress).let {
                lastProgress = it
                seekBarFloating.progress = it.toInt()
                currentDurationFloating.run {
                    base = android.os.SystemClock.elapsedRealtime() - it
                }
            }

        }
    }

    @com.pt.common.global.UiAnn
    override fun VideoFloatFasten.extensionsVisible() {
        durationLinear.justVisible()
        previous.justVisible()
        next.justVisible()
        upperFrame.justVisible()
    }

    @com.pt.common.global.UiAnn
    override fun VideoFloatFasten.extensionsGone() {
        durationLinear.justGone()
        previous.justGone()
        next.justGone()
    }

    @com.pt.common.global.UiAnn
    private fun doScale() {
        widthV += 100
        heightV = (widthV / videoRatio).toInt()
        musicDim { wid, hei ->
            if (widthV < wid && heightV < hei) {
                if (widthV > (wid * 0.7).toInt()) {
                    showSeekBoolean = true
                    viewVideo.extensionsVisible()
                }
                fullScreen = false
            } else {
                if (!fullScreen) {
                    widthV = wid
                    heightV = (widthV / videoRatio).toInt()
                } else {
                    widthV = widthOriginal
                    heightV = heightOriginal
                    showSeekBoolean = false
                    viewVideo.extensionsGone()
                }
                fullScreen = !fullScreen
            }
            viewVideo.updateFloatingVideo(widthV, heightV)
        }
    }

    @com.pt.common.global.UiAnn
    private fun VideoFloatFasten.updateFloatingVideo(widthPlus: Int, heightPlus: Int) {
        if (widthPlus == 0) return
        if (heightPlus == 0) return

        fetchParams.width = widthPlus
        fetchParams.height = heightPlus
        if (fullScreen) {
            fetchParams.x = 0
            fetchParams.y = 0
        }
        context.catchyBadToken {
            windowManager?.updateViewLayout(viewVideo.root, fetchParams)
        }
        floatingVideo.framePara(widthPlus, heightPlus) { }
        constraintButtons.framePara(
            widthPlus, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT
        ) {
            gravity = android.view.Gravity.CENTER_VERTICAL
        }
        durationLinear.framePara(widthPlus, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT) {
            gravity = android.view.Gravity.BOTTOM
        }
        upperFrame.framePara(widthPlus, android.widget.FrameLayout.LayoutParams.WRAP_CONTENT) {}
    }

    @com.pt.common.global.UiAnn
    override fun onClick(v: android.view.View?) {
        viewVideo.apply {
            when (v) {
                previous -> previousVideo.pushVideo()
                next -> getNextVideo.pushVideo()
                scaled -> doScale()
                playFloating -> {
                    if (!isError) {
                        if (floPlayer.isPlaying) {
                            isRealPlay = false
                            floPlayer.pause()
                        } else {
                            floPlayer.playSong()
                        }
                    } else {
                        ctxS.makeToastRec(com.pt.pro.R.string.uy, 0)
                    }
                }
                close -> {
                    context?.makeToastRec(com.pt.pro.R.string.cl, 0)
                    closeVideo()
                }
            }
        }
    }

    private fun closeVideo() {
        launchDef {
            withDefault {
                justCoroutineBack {
                    previousSeek?.alsoSusBack {
                        it.seekTime = floPlayer.currentPositionSus()
                        ctxS.newDBGallery {
                            it.updateVideo()
                        }
                    }
                }
                justCoroutineBack {
                    context?.newIntent(VideoFloating::class.java) {
                        flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                        action = ACTION_VIDEO_FLOATING
                        putExtra("ext", ACTION_VIDEO_FLOATING)
                        this@newIntent
                    }?.apply {
                        android.app.PendingIntent.getService(
                            context ?: return@justCoroutineBack, SERVICE_VIDEO, this, PEND_FLAG
                        ).send()
                    }
                }
            }
        }
    }

    private var touchListener: com.pt.common.moderator.touch.ContactListener? = object : com.pt.common.moderator.touch.ContactListener {
        private var numOfTaps = 0
        private var lastTapTimeMs: Long = 0
        private var touchDownMs: Long = 0
        private var initialX = 0
        private var initialY = 0
        private var initialTouchX = 0F
        private var initialTouchY = 0F

        override fun onUp(v: android.view.View, event: android.view.MotionEvent) {}

        override fun onUp(v: android.view.View) {
            if ((System.currentTimeMillis() - touchDownMs) > android.view.ViewConfiguration.getTapTimeout()) {
                numOfTaps = 0
                lastTapTimeMs = 0
                return
            }
            if (numOfTaps > 0 && (System.currentTimeMillis() - lastTapTimeMs) < android.view.ViewConfiguration.getDoubleTapTimeout()) {
                numOfTaps += 1
            } else {
                numOfTaps = 1
            }
            lastTapTimeMs = System.currentTimeMillis()
            if (numOfTaps == 1) {
                toCatchSackAfter(
                    11, android.view.ViewConfiguration.getDoubleTapTimeout() - 100L
                ) {
                    viewVideo.apply {
                        if (hide) {
                            hide = false
                            playFloating.justVisible()
                            upperFrame.justVisible()
                            if (showSeekBoolean) {
                                extensionsVisible()
                            }
                        } else {
                            hide = true
                            playFloating.justGone()
                            upperFrame.justGone()
                            if (showSeekBoolean) {
                                extensionsGone()
                            }
                        }
                    }
                    Unit
                }.postAfter()
            }
        }

        override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
            touchDownMs = System.currentTimeMillis()
            initialX = fetchParams.x
            initialY = fetchParams.y
            initialTouchX = event.rawX
            initialTouchY = event.rawY
        }

        override fun onMove(v: android.view.View, event: android.view.MotionEvent) {
            fetchParams.x = initialX + (event.rawX - initialTouchX).toInt()
            fetchParams.y = initialY + (event.rawY - initialTouchY).toInt()
            context.catchyBadToken {
                windowManager?.updateViewLayout(viewVideo.root, fetchParams)
            }
        }
    }


    @com.pt.common.global.UiAnn
    override fun MutableList<MediaSack>.updateVideos(newVideo: MediaSack) {
        allVideosNative = this
        video = newVideo
        position = allVideos.indexOfFirst {
            it.pathMedia == video?.pathMedia
        }.let {
            if (it == -1) 0 else it
        }
        newVideo.pushVideo()
    }


    @com.pt.common.global.UiAnn
    private fun android.view.View.animationUp(beginValue: Int, endValue: Int) {
        returnForSlip(begin = beginValue, end = endValue) {
            duration = 200
            addUpdateListener {
                params?.apply {
                    y = (animatedValue as Int)
                    context.catchyBadToken {
                        windowManager?.updateViewLayout(this@animationUp, this)
                    }
                }
            }
            start()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onKeyBoardOpened() {
        beforeYouGo = fetchParams.y
        if (fetchParams.y > 50) {
            viewRt?.root?.animationUp(fetchParams.y, -50)
        }
    }

    @com.pt.common.global.MainAnn
    override fun onKeyBoardClosed() {
        if (beforeYouGo != fetchParams.y) {
            viewRt?.root?.animationUp(fetchParams.y, beforeYouGo)
        }
    }

    @com.pt.common.global.MainAnn
    override fun onChange(newConfig: Int) {
        stopPlayer()
    }

    override fun onServiceDestroy(b: () -> Unit) {
        context?.onDestroyService(b)
    }

    @com.pt.common.global.MainAnn
    private fun android.content.Context?.onDestroyService(b: () -> Unit) {
        kotlin.runCatching {
            viewRt?.apply {
                seekBarFloating.handler?.removeCallbacksAndMessages(null)
                root_.onViewDestroy()
                catchyBadToken {
                    windowManager?.removeViewImmediate(root)
                }
            }
            mediaPlay?.apply {
                synchronized(this) {
                    catchyUnit {
                        clearVideoSurfaceHolder(viewRt?.floatingVideo?.holder)
                    }
                    catchy(Unit) {
                        videoCall?.let { removeListener(it) }
                    }
                    catchy(Unit) {
                        pause()
                    }
                    catchy(Unit) {
                        stop()
                    }
                    catchy(Unit) {
                        release()
                    }
                }
            }
            contextMedia = null
            allVideosNative = null
            isRealPlay = true
            videoCall = null
            touchListener = null
            viewRt = null
            params = null
            previousSeek = null
            job.cancelJob()
            catchy(Unit) {
                dispatch.close()
            }
            ext.shutdownNow()
            cancelScope()
            extNative = null
            jobNative = null
            dispatcherNative = null
            stateDestroy()

            b.invoke()
        }.onFailure {
            b.invoke()
        }
    }

    override fun surfaceCreated(p0: android.view.SurfaceHolder) {
        if (!isInti) {
            launchImdMain {
                intiVideoFloating(p0)
            }
        }
    }

    override fun surfaceChanged(p0: android.view.SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: android.view.SurfaceHolder) {}

}
