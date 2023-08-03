package com.pt.pro.gallery.fragments

import android.view.MotionEvent
import android.view.View
import com.pt.common.global.*
import com.pt.common.media.getVideoRatio
import com.pt.common.moderator.touch.ContactListener
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.gallery.fasten.VideoFragmentFasten
import com.pt.pro.gallery.service.VideoFloating

class FragmentVideo : ParentFragmentVideo(), android.view.SurfaceHolder.Callback {

    private var invokeForVideo: (android.view.SurfaceHolder.() -> Unit)? = null
    private var surfaceView: android.view.SurfaceHolder? = null

    override fun VideoFragmentFasten.onViewCreated() {
        binder?.thumb?.loadForVideo(mediaHold.pathMedia.toStr)
        binder?.videoView?.holder?.apply {
            surface
            addCallback(this@FragmentVideo)
        }
        pushJob {
            launchImdMain {
                context.nullChecker()
                intiViews()
                currentSeekVideo = context?.getCurrentSeek(mediaHold.nameMedia)
            }
        }
    }

    private suspend fun VideoFragmentFasten.intiViews() {
        withMain {
            playPause.apply {
                setOnClickListener(this@FragmentVideo)
            }
            volume.setOnClickListener(this@FragmentVideo)
            screenBrightness.setOnClickListener(this@FragmentVideo)
            seekScale.setOnSeekBarChangeListener(seekListener)
            frameVideo.apply {
                touchListener?.setContactListener(true)
            }
            qHand = ctx.fetchHand
            lifecycle.addObserver(this@FragmentVideo)
            isRight = rec.isRightToLeft
            act.windowManager?.fetchDimensionsSus {
                leftHalf = this@fetchDimensionsSus.width / 3
                rightHalf = leftHalf * 2
                videoObserver(this)
            }
            volumeBoolean = false
            brightnessBoolean = false
            ctx.soundManager.also {
                vol = it?.getStreamVolume(
                    android.media.AudioManager.STREAM_MUSIC
                ) ?: 3
            }
            brt = android.provider.Settings.System.getInt(
                context?.contentResolver ?: return@withMain,
                android.provider.Settings.System.SCREEN_BRIGHTNESS
            ).toFloat()
            brightnessBoolean = false
            if (surfaceView == null) {
                surfaceView = binder?.videoView?.holder
            }
        }
    }

    private fun androidx.media3.exoplayer.ExoPlayer.intiPlayer() {
        synchronized(this) {
            clearVideoSurface()
            setMediaItem(
                androidx.media3.common.MediaItem.Builder().setUri(
                    mediaHold.pathMedia.toUri
                ).build()
            ).apply {
                prepare()
            }
        }
    }

    private suspend fun intiVideo() {
        withMain {
            iBindingSus {
                playPause.applySus {
                    isEnabled = true
                    svgReClear()
                }
                videoView.justVisible()
            }
        }
    }

    override fun onChangedPlaybackState(playbackState: Int) {
        if (playbackState == androidx.media3.common.Player.STATE_ENDED) {
            funComplete()
        }
    }

    override fun onChangedTracks() {
        pushJob {
            launchImdMain {
                withMain {
                    playerVideo.applySus {
                        surfaceView?.alsoSus { sv ->
                            setVideoSurfaceHolder(sv)
                            invokeForVideo = null
                        } ?: kotlin.run {
                            invokeForVideo = s@{
                                setVideoSurfaceHolder(this@s)
                                invokeForVideo = null
                            }
                        }
                    }
                }
                context.nullChecker()
                playerVideo.displayIfFoundSeek(currentSeekVideo?.seekTime ?: 1L)
                context.nullChecker()
                iBindingSus {
                    thumb.justGone()
                }
                intiVideo()
            }
        }
    }

    override fun onChangedIsPlaying(isPlaying: Boolean) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                if (isPlaying) {
                    playState()
                } else {
                    pauseState()
                }
            }
        }
    }

    override fun onError(error: androidx.media3.common.PlaybackException) {
        this@FragmentVideo.error = true
    }

    override var videoCall: androidx.media3.common.Player.Listener? =
        object : androidx.media3.common.Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                onChangedPlaybackState(playbackState)
            }

            override fun onTracksChanged(tracks: androidx.media3.common.Tracks) {
                super.onTracksChanged(tracks)
                onChangedTracks()
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                onChangedIsPlaying(isPlaying)
            }

            override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                super.onPlayerError(error)
                onError(error)
            }
        }

    private fun androidx.media3.exoplayer.ExoPlayer.playSong() {
        isRealPlay = true
        iBinding {
            thumb.justGone()
        }
        play()
    }

    private var seekListener: android.widget.SeekBar.OnSeekBarChangeListener?
        @com.pt.common.global.UiAnn
        get() {
            return object : android.widget.SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: android.widget.SeekBar?,
                    progress: Int,
                    fromUser: Boolean,
                ) {
                    if (binder == null) return
                    if (fromUser) {
                        if (seekBar == binding.seekScale) {
                            unPost(889)
                            unPost(scaleRemove.two)
                            if (volumeBoolean && !brightnessBoolean) {
                                vol = progress / VOLUME_CONSTANT
                                ctx.soundManager?.apply {
                                    setStreamVolume(android.media.AudioManager.STREAM_MUSIC, vol, 0)
                                }
                            } else if (brightnessBoolean && !volumeBoolean) {
                                brt = progress.toFloat()
                                act.window?.screenBrightness(brt)
                            } else {
                                binding.frameVideo.apply {
                                    if (progress < 400) {
                                        -(400 - progress)
                                    } else {
                                        (progress - 400)
                                    }.let {
                                        if (rec.isLandTraditional)
                                            scrollTo(scrollX, it)
                                        else
                                            scrollTo(it, scrollY)
                                    }
                                }
                            }
                        } else {
                            browserListener?.apply {
                                runSeek(progress.toLong())
                            }
                        }
                    }
                }


                override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {
                    if (binder == null) return
                    if (seekBar != binder?.seekScale) {
                        unPost(onEverySecond.two)
                    }
                }

                override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                    if (binder == null) return
                    if (seekBar == binder?.seekScale) {
                        if (scaled) {
                            toCatchSackAfter(889, 2000) {
                                removeSeek()
                            }.postAfter()
                        }
                    } else {
                        if (!error) {
                            seekBar?.progress?.toLong()?.let {
                                playerVideo.apply {
                                    seekTo(it)
                                    if (!isPlaying) play()
                                }
                            }
                        } else {
                            ctx.makeToastRec(R.string.uy, 0)
                        }
                        onEverySecond.rKTSack(100).postAfter()
                    }
                }
            }
        }
        set(value) {
            value.logProvLess()
        }

    private fun playAndPause() {
        if (!error) {
            playPause()
        } else {
            context?.makeToastRec(R.string.uy, 0)
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun playPause() {
        mediaPlay?.apply {
            if (isPlaying) {
                isRealPlay = false
                pause()
            } else {
                playSong()
            }
        }
    }

    override val runFloatingVideo: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            iBinding {
                videoView.apply {
                    videoPop(measuredWidth, measuredHeight)
                }
            }
        }

    @com.pt.common.global.WorkerAnn
    private fun videoPop(w: Int, h: Int) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                withBack {
                    if (!error) {
                        if (isV_N) {
                            if (ctx.checkDrawOverlayPermission()) {
                                isRealPlay = false
                                playerVideo.pauseSus()
                                launchPopUp(w, h)
                            }
                        } else {
                            playerVideo.pauseSus()
                            isRealPlay = false
                            launchPopUp(w, h)
                        }
                    } else {
                        ctx.makeToastRecSus(R.string.uy, 0)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun VideoFragmentFasten.volumeProvider() {
        if (seekScale.isVis) {
            ctx.compactImage(R.drawable.ic_screen_brightness) {
                screenBrightness.setImageDrawable(this@compactImage)
            }
            removeSeek()
        } else {
            unPost(889)
            unPost(scaleRemove.two)
            ctx.compactImage(R.drawable.ic_volume) {
                screenBrightness.setImageDrawable(this@compactImage)
            }
            volumeBoolean = true
            brightnessBoolean = false
            seekScale.apply {
                if (isV_O) min = 0
                max = 16 * VOLUME_CONSTANT
                progress = ctx.soundManager.currentVol * VOLUME_CONSTANT
                rotation = 1800F
                visibleFade(400)
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun VideoFragmentFasten.brightnessProvider() {
        if (seekScale.isVis) {
            ctx.compactImage(R.drawable.ic_volume_off) {
                volume.setImageDrawable(this@compactImage)
            }
            removeSeek()
        } else {
            ctx.compactImage(R.drawable.ic_brightness_off) {
                volume.setImageDrawable(this@compactImage)
            }
            unPost(scaleRemove.two)
            unPost(889)
            brightnessBoolean = true
            volumeBoolean = false
            seekScale.apply {
                if (isV_O) min = 0
                max = MAX_BRIGHTNESS
                progress = brt.toInt()
                rotation = 0F
                visibleFade(400)
            }
        }
    }

    override fun removeSeek() {
        if (context == null) return
        binder?.seekScale?.goneFade(300)
        unPost(889)
        scaleRemove.rKTSack(500L).postAfter()
    }

    override val scaleRemove: DSackT<() -> Unit, Int>
        get() = toCatchSack(22) {
            brightnessBoolean = false
            volumeBoolean = false
            iBinding {
                ctx.compactImage(R.drawable.ic_screen_brightness) {
                    screenBrightness.setImageDrawable(this@compactImage)
                }
                ctx.compactImage(R.drawable.ic_volume_off) {
                    volume.setImageDrawable(this@compactImage)
                }
                seekScale.apply {
                    rotation = 0f
                    if (isV_O) min = 0
                    max = 800
                    progress = max / 2
                }
            }
        }

    private fun com.pt.pro.gallery.objects.SeekVideo.updateSeek(time: Long) {
        seekTime = time
        ctx.newDBGallery {
            updateVideo()
        }
    }


    @com.pt.common.global.WorkerAnn
    override suspend fun launchPopUp(w: Int, h: Int) {
        withBack {
            popUpVideo = true
            if (!allImages.isNullOrEmpty()) {
                (allImages ?: return@withBack).toMutableList().asSequence().filter {
                    !it.isImage
                }.letSusBack {
                    (mediaHolder ?: return@letSusBack).copy(
                        mediaWidth = w,
                        mediaHigh = h,
                        mediaSize = playerVideo.currentPositionSus()
                    ).letSusBack { itC ->
                        if (ctx.isServiceNotRunning(VideoFloating::class.java.canonicalName)) {
                            ctx.newIntent(VideoFloating::class.java) {
                                action = ACTION_LOAD_FLOATING
                                putExtra(ALL_VIDEOS, it.toMutableList() as ArrayList)
                                putExtra(POPUP_VIDEO, itC)
                                this@newIntent
                            }.also {
                                androidx.core.content.ContextCompat.startForegroundService(
                                    ctx,
                                    it
                                )
                            }
                        } else {
                            ctx.newIntent(VideoFloating::class.java) {
                                putExtra(
                                    ALL_VIDEOS,
                                    it.toMutableList() as ArrayList
                                )
                                putExtra(POPUP_VIDEO, itC)
                                action = VIDEOS_UPDATE
                                this@newIntent
                            }.also {
                                android.app.PendingIntent.getService(
                                    ctx,
                                    0,
                                    it,
                                    PEND_FLAG
                                ).send()
                            }
                        }
                    }
                }
            }
            homeLauncher {
                if (context != null) {
                    this@FragmentVideo.startActivity(this)
                }
            }
        }
    }

    private val runForUpdate: DSackT<() -> Unit, Int>
        get() = toCatchSack(323) {
            if (seekSack != null && !seekSackUpdate) {
                browserListener?.apply {
                    seekSack?.let { (one, two) ->
                        seekSackUpdate = true
                        setMax(two)
                        runSeek(one)
                    }
                }
            } else {
                runForUpdate.rKTSack(100L).postAfter()
            }
            Unit
        }

    override val onEverySecond: DSackT<() -> Unit, Int>
        get() = toCatchSack(33) {
            if (mediaPlay?.isPlaying == true) {
                browserListener?.apply {
                    mediaPlay.mediaPosLong?.let {
                        runSeek(it)
                        du = it
                        onEverySecond.rKTSack(100L).postAfter()
                    }
                }
            }
        }


    @com.pt.common.global.MainAnn
    internal suspend fun videoObserver(siz: android.util.Size) {
        context.nullChecker()
        fetchVideoRatio(mediaHold).letSus { def ->
            getVideoRatio(mediaHold, def).letSus { ra ->
                context.nullChecker()
                siz.fetchVideoDim(ra)
            }.letSus {
                withMain {
                    binder?.videoView?.framePara(it.one, it.two) {
                        gravity = android.view.Gravity.CENTER
                    }
                    ratio = it.three
                    durationAnimation = if (ratio > 2) 500 else 200
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    override fun VideoFragmentFasten.onClick(v: View) {
        iBinding {
            when (v) {
                playPause -> playAndPause()
                volume -> volumeProvider()
                screenBrightness -> brightnessProvider()
            }
        }
    }

    override fun doLockVid() {
        iBinding {
            doHideDisplay()
            frameVideo.touchListener(remove = true)
        }
    }

    override fun doUnLockVid() {
        iBinding {
            doShowDisplay()
            frameVideo.touchListener(remove = false)
        }
    }

    private var touchListener: ContactListener? =
        object : ContactListener {

            private var numOfTaps = 0
            private var lastTapTimeMs: Long = 0
            private var touchDownMs: Long = 0
            override fun onUp(v: View) {}

            override fun onUp(v: View, event: MotionEvent) {
                if (event.action == MotionEvent.ACTION_UP &&
                    event.actionMasked == MotionEvent.ACTION_UP
                ) {
                    if ((System.currentTimeMillis() - touchDownMs) >
                        android.view.ViewConfiguration.getTapTimeout()
                    ) {
                        numOfTaps = 0
                        lastTapTimeMs = 0
                        return
                    }
                    if (numOfTaps > 0 &&
                        (System.currentTimeMillis() - lastTapTimeMs) <
                        android.view.ViewConfiguration.getDoubleTapTimeout()
                    ) {
                        numOfTaps += 1
                    } else {
                        numOfTaps = 1
                    }
                    lastTapTimeMs = System.currentTimeMillis()
                    if (numOfTaps == 1) {
                        toCatchSackAfter(
                            622,
                            android.view.ViewConfiguration.getDoubleTapTimeout() - 100L
                        ) {
                            if (hideSys) {
                                binder?.doShowDisplay()
                            } else {
                                binder?.doHideDisplay()
                            }
                            hideSys = !hideSys
                        }.postAfter()
                    } else if (numOfTaps == 2) {
                        if (event.x < rightHalf && event.x > leftHalf) {
                            if (ratio != 1.0F) {
                                if (scaled) {
                                    binder?.unScaleView()
                                } else {
                                    binder?.scaleView()
                                }
                                if (scaled) {
                                    toCatchSackAfter(889, 2000) {
                                        removeSeek()
                                    }.postAfter()
                                }
                                if (!toClear) toClear = scaled
                                scaled = !scaled
                            }
                        } else if (event.x > rightHalf) {
                            onEverySecond.rKTSack(100).postAfter()
                            toCatchSackAfter(682, 200L) {
                                if (isRight) {
                                    playerVideo.doRearward()
                                } else {
                                    playerVideo.doForward()
                                }
                            }.postAfter()
                        } else {
                            onEverySecond.rKTSack(100).postAfter()
                            toCatchSackAfter(522, 200L) {
                                if (isRight) {
                                    playerVideo.doForward()
                                } else {
                                    playerVideo.doRearward()
                                }
                            }.postAfter()
                        }
                    } else {
                        if (event.x > rightHalf) {
                            if (isRight) {
                                playerVideo.doTripleRearward()
                            } else {
                                playerVideo.doTripleForward()
                            }
                        } else {
                            if (isRight) {
                                playerVideo.doTripleForward()
                            } else {
                                playerVideo.doTripleRearward()
                            }
                        }
                    }
                }
            }

            override fun onDown(v: View, event: MotionEvent) {
                touchDownMs = System.currentTimeMillis()
                unPost(522)
                unPost(682)
                unPost(889)
                unPost(622)
            }

            override fun onMove(v: View, event: MotionEvent) {}

        }

    override val seekSacker: DSackT<Long, Int>?
        get() = seekSack

    override fun onResume() {
        super.onResume()
        mediaPlay?.apply {
            videoCall?.also {
                addListener(it)
            }
        }
        mediaPlay?.intiPlayer()
        //binder?.videoView?.onResume()
        browserListener?.vidFragment = this
        browserListener?.apply {
            seekListener?.seekListenerRun?.invoke()
            seekSack?.let {
                setMax(it.two)
                runSeek(du)
                seekSackUpdate = true
            } ?: runForUpdate.rKTSack(100L).postAfter()
        }
    }


    @com.pt.common.global.MainAnn
    override fun onPause() {
        if (du != currentSeekVideo?.seekTime) currentSeekVideo?.updateSeek(du)
        if (running) {
            isRealPlay = false
            mediaPlay?.pause()
        }
        if (scaled) {
            iBinding {
                frameVideo.clearAnimation()
                playPause.clearAnimation()
            }
            scaled = false
        }
        iBinding {
            invokeForVideo = null
            thumb.justVisible()
            playPause.isEnabled = false
        }
        super.onPause()
        videoCall?.let { mediaPlay?.removeListener(it) }
    }

    override fun onDestroyView() {
        iBinding {
            frameVideo.onViewDestroy()
            root_.removeAllViewsInLayout()
        }
        isRealPlay = false
        videoCall = null
        activityLauncher?.unregister()
        browserListener = null
        activityLauncher = null
        touchListener = null
        audioListener = null
        mediaHolder = null
        allImages = null
        seekListener = null
        seekSack = null
        super.onDestroyView()
    }

    override fun surfaceCreated(p0: android.view.SurfaceHolder) {
        surfaceView = p0
        invokeForVideo?.invoke(p0)
    }

    override fun surfaceChanged(p0: android.view.SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: android.view.SurfaceHolder) {
        surfaceView = null
    }

}
