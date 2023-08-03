package com.pt.pro.gallery.fragments

import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.gallery.interfaces.VideoListener
import com.pt.pro.gallery.fasten.VideoFragmentFasten
import com.pt.pro.gallery.service.VideoFloating

abstract class ParentFragmentVideo : GlobalSimpleFragment<VideoFragmentFasten>(), VideoListener {

    protected var seekSack: DSackT<Long, Int>? = null
    protected var seekSackUpdate: Boolean = false

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    @Volatile
    override var mediaHolder: MediaSack? = null

    protected inline val mediaHold: MediaSack get() = mediaHolder!!

    @Volatile
    override var allImages: MutableList<MediaSack>? = mutableListOf()

    @Volatile
    override var marginNum: Int = 0

    @Volatile
    override var marginHeight: Int = 0

    @Volatile
    override var browserListener: com.pt.pro.gallery.interfaces.BrowserListener? = null

    @Volatile
    override var hideSys: Boolean = false

    @Volatile
    protected var running: Boolean = false

    protected var brt: Float = 50.0F

    @Volatile
    protected var leftHalf: Int = 360

    @Volatile
    protected var ratio: Float = 1.0F

    @Volatile
    protected var durationAnimation: Long = 200L

    @Volatile
    protected var rightHalf: Int = 360

    @Volatile
    protected var du: Long = 1L

    @Volatile
    protected var brightnessBoolean: Boolean = false

    @Volatile
    protected var volumeBoolean: Boolean = false

    @Volatile
    protected var popUpVideo: Boolean = false

    @Volatile
    protected var currentSeekVideo: com.pt.pro.gallery.objects.SeekVideo? = null

    @Volatile
    protected var error: Boolean = false

    @Volatile
    protected var scaled: Boolean = false

    @Volatile
    protected var toClear: Boolean = false

    @Volatile
    protected var vol: Int = 1

    protected inline val android.media.AudioManager?.currentVol: Int
        get() = this?.getStreamVolume(android.media.AudioManager.STREAM_MUSIC) ?: 3

    @Volatile
    protected var isRight: Boolean = false

    @Volatile
    protected var isRealPlay: Boolean = false

    protected val playerVideo: androidx.media3.exoplayer.ExoPlayer
        @com.pt.common.global.MainAnn
        get() = mediaPlay!!

    @Volatile
    override var mediaPlay: androidx.media3.exoplayer.ExoPlayer? = null

    @Volatile
    protected var soundManagerNative: android.media.AudioManager? = null

    protected val android.content.Context.soundManager: android.media.AudioManager?
        get() = soundManagerNative ?: fetchSystemService(audioService).also {
            soundManagerNative = it
        }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run { this@creBin.context.inflaterVideo() }.also {
                it.editFrameOptions()
                @com.pt.common.global.ViewAnn
                binder = it
            }.root_
        }

    @com.pt.common.global.MainAnn
    private fun VideoFragmentFasten.editFrameOptions() {
        (marginNum + 5F.toPixel).let { mar ->
            upperOptionsVideo.framePara(-1, -2) {
                topMargin = ctx.actionBarHeight + rec.statusBarHeight
                marginStart = mar
                marginEnd = mar
            }
        }
    }

    protected suspend fun fetchVideoRatio(
        m: MediaSack,
    ): Float = justCoroutine {
        if (m.mediaWidth != 0 && m.mediaHigh != 0) {
            m.mediaWidth.toFloat() / m.mediaHigh.toFloat()
        } else {
            binder?.videoView?.run {
                width.toFloat() / height.toFloat()
            } ?: 1F
        }
    }

    @com.pt.common.global.APIAnn(android.os.Build.VERSION_CODES.M)
    protected fun android.content.Context.checkDrawOverlayPermission(): Boolean = run {
        if (!android.provider.Settings.canDrawOverlays(this)) {
            activityLauncher?.launch(goToOverlay)
            return@run false
        } else {
            return@run true
        }
    }

    protected var activityLauncher: Bring = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) {
        if (ctx.canFloat) {
            if (playerVideo.isPlaying) {
                isRealPlay = false
                playerVideo.pause()
            }

            pushJob { j ->
                launchMain {
                    j?.checkIfDone()
                    withMain {
                        binder?.videoView?.apply {
                            launchPopUp(measuredWidth, measuredHeight)
                        }
                    }
                }
            }
        } else {
            ctx.makeToastRec(R.string.rl, 0)
        }
    }

    @com.pt.common.global.UiAnn
    protected fun funComplete() {
        pushJob { j ->
            launchMain {
                j?.checkIfDone()
                withMain {
                    binding.apply {
                        ctx.compactImage(R.drawable.ic_play) {
                            playPause.setImageDrawable(this@compactImage)
                        }
                        playerVideo.apply {
                            seekTo(1L)
                            pause()
                        }
                        unPost(onEverySecond.two)
                        du = 1
                        browserListener?.apply {
                            runSeek(1)
                        }
                    }
                    running = false
                }
            }
        }
    }

    internal inline val android.util.Size.fetchVideoDim: (Float) -> DSack<Int, Int, Float>
        get() = { ra ->
            if (rec.isLandTraditional) {
                (height.toFloat() * (ra)).toInt().let {
                    if (it <= width) {
                        DSackT(it, MATCH)
                    } else {
                        DSackT(width, (width.toFloat() * (1 / ra)).toInt())
                    }
                }.run {
                    (width.toFloat() / (height.toFloat() * (ra))).let {
                        if (it < 1.0F) 1.0F else it
                    }.let { rat ->
                        DSack(one, two, rat)
                    }
                }
            } else {
                (width.toFloat() * (1 / ra)).toInt().let {
                    if (it <= height) {
                        DSackT(MATCH, it)
                    } else {
                        DSackT((height.toFloat() * (ra)).toInt(), height)
                    }
                }.run {
                    (height.toFloat() / (width.toFloat() * (1 / ra))).let {
                        if (it < 1.0F) 1.0F else it
                    }.let { rat ->
                        DSack(one, two, rat)
                    }
                }
            }
        }

    @com.pt.common.global.UiAnn
    protected fun VideoFragmentFasten.scaleView() {
        pushJob { j ->
            launchMain {
                j?.checkIfDone()
                withMain {
                    scaleRemove.rKTSack(50).postAfter()
                    if (seekScale.isGon) seekScale.visibleFade(400)
                    binding.doShowDisplay()
                    scaleView(ratio, durationAnimation) {
                        frameVideo.startAnimation(this)
                    }
                    if (playPause.isVis) {
                        scaleView(1F / ratio, durationAnimation) {
                            playPause.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected fun VideoFragmentFasten.unScaleView() {
        pushJob { j ->
            launchMain {
                j?.checkIfDone()
                withMain {
                    scaleViewOutPend(ratio, durationAnimation) {
                        frameVideo.startAnimation(this)
                    }
                    if (playPause.isVis) {
                        scaleViewOutPend(1 / ratio, durationAnimation) {
                            playPause.startAnimation(this)
                        }
                    }
                    removeSeek()
                }
                context.nullChecker()
                kotlinx.coroutines.delay(durationAnimation - 100L)
                frameVideo.scrollToTop()
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected fun VideoFragmentFasten.doShowDisplay() {
        pushJob { j ->
            launchMain {
                j?.checkIfDone()
                browserListener?.onShowCardView()
                withMain {
                    if (scaled && playPause.isGon) {
                        scaleView(1 / ratio, durationAnimation) {
                            playPause.startAnimation(this)
                        }
                    }
                    withMain {
                        act.window?.showSystemUI()
                        playPause.justVisible()
                        upperOptionsVideo.justVisible()
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected fun VideoFragmentFasten.doHideDisplay() {
        pushJob { j ->
            launchMain {
                j?.checkIfDone()
                browserListener?.onHideCardView()
                withMain {
                    act.window?.hideAllSystemUI()
                    ctx.compactImage(R.drawable.ic_volume_off) {
                        volume.setImageDrawable(this@compactImage)
                    }
                    ctx.compactImage(R.drawable.ic_screen_brightness) {
                        screenBrightness.setImageDrawable(this@compactImage)
                    }
                    removeSeek()
                    playPause.apply {
                        clearAnimation()
                        justGone()
                    }
                    upperOptionsVideo.justGone()
                }
            }
        }
    }

    protected fun androidx.media3.exoplayer.ExoPlayer.doForward() {
        currentPosition.let {
            if (it != duration) {
                seekTo(it + 10000)
                doFast(forward = true, triple = false)
            } else {
                seekTo(duration)
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun doFast(forward: Boolean, triple: Boolean) {
        browserListener?.apply {
            mediaPlay.mediaPosLong?.let {
                runSeek(it)
                du = it
            }
        }
        binding.forRearFrame.frameWard(forward = forward, triple = triple)
    }

    protected fun androidx.media3.exoplayer.ExoPlayer.doTripleForward() {
        currentPosition.let {
            if (it != duration) {
                seekTo(it + 30000)
                doFast(forward = true, triple = true)
            } else {
                seekTo(duration)
            }
        }
    }

    protected fun androidx.media3.exoplayer.ExoPlayer.doRearward() {
        currentPosition.let {
            if (it >= 200) {
                seekTo(it - 10000)
                doFast(forward = false, triple = false)
            } else {
                seekTo(1)
            }
        }
    }

    protected fun androidx.media3.exoplayer.ExoPlayer.doTripleRearward() {
        currentPosition.let {
            if (it >= 200L) {
                seekTo(it - 30000)
                doFast(forward = false, triple = true)
            } else {
                seekTo(1)
            }
        }

    }

    @com.pt.common.global.UiAnn
    private fun android.widget.FrameLayout.frameWard(forward: Boolean, triple: Boolean) {
        if (isRight) {
            doFrameWard(!forward, triple)
        } else {
            doFrameWard(forward, triple)
        }
    }

    @com.pt.common.global.UiAnn
    private fun android.widget.FrameLayout.doFrameWard(forward: Boolean, triple: Boolean) {
        if (forward) {
            if (triple) {
                if (rec.isLandTraditional) {
                    R.drawable.ic_forward_triple
                } else {
                    R.drawable.ic_forward_triple_land
                }
            } else {
                R.drawable.ic_forward
            }
        } else {
            if (triple) {
                if (rec.isLandTraditional) {
                    R.drawable.ic_rearward_triple
                } else {
                    R.drawable.ic_rearward_triple_land
                }
            } else {
                R.drawable.ic_rearward
            }
        }.let {
            ctx.compactImage(it) {
                background = this@compactImage
            }
        }
        visibleFade(200)
        toCatchSackAfter(972, 500) {
            goneFadeNull(150)
        }.postAfter()
    }


    protected var audioListener: android.media.AudioManager.OnAudioFocusChangeListener? =
        android.media.AudioManager.OnAudioFocusChangeListener {

        }

    abstract suspend fun launchPopUp(w: Int, h: Int)


    protected suspend fun android.content.Context.getCurrentSeek(
        currentVideoName: String?,
    ): com.pt.pro.gallery.objects.SeekVideo = justCoroutine {
        if (currentVideoName == null) return@justCoroutine com.pt.pro.gallery.objects.SeekVideo()
        return@justCoroutine withBackDef(com.pt.pro.gallery.objects.SeekVideo()) {
            newDBGallerySus {
                getOneSeek(currentVideoName)
            }
        }
    }

    protected suspend fun playState() {
        context.nullChecker()
        withMain {
            if (popUpVideo) {
                popUpVideo = false
                if (context != null) {
                    ctx.stopService(android.content.Intent(act, VideoFloating::class.java))
                    ctx.makeToastRec(R.string.cl, 0)
                }
            }
            running = true
            if (context != null) {
                ctx.compactImage(R.drawable.ic_pause) {
                    binder?.playPause?.setImageDrawable(this@compactImage)
                }
            }
            onEverySecond.rKTSack(100).postAfter()
        }
    }

    protected suspend fun pauseState() {
        context.nullChecker()
        withMain {
            if (running) {
                unPost(onEverySecond.two)
            }
            running = false
            if (context != null) {
                ctx.compactImage(R.drawable.ic_play) {
                    binder?.playPause?.setImageDrawable(this@compactImage)
                }
            }
        }
    }

    protected suspend fun androidx.media3.exoplayer.ExoPlayer.displayIfFoundSeek(d: Long) {
        context.nullChecker()
        withMain {
            justCoroutineMain {
                seekTo(d)
            }
            justCoroutineMain {
                browserListener?.apply {
                    runSeek.invoke(d)
                    setMax.invoke(durationSus().toInt())
                }
                du = d
                seekSack = DSackT(d, durationSus().toInt())
                toClear = true
            }
        }
    }

    override fun onDestroyView() {
        soundManagerNative = null
        super.onDestroyView()
    }

}