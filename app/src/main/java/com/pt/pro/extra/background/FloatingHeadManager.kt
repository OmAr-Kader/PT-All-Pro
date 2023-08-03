package com.pt.pro.extra.background

import com.pt.common.global.*
import com.pt.common.media.callBroadCastInsert
import com.pt.common.media.mediaStoreImage
import com.pt.common.media.mediaStoreVideo
import com.pt.common.media.mediaVideoStore
import com.pt.common.stable.*
import kotlin.math.abs

open class FloatingHeadManager(ctx: android.content.Context) : GlobalServiceHead(ctx), com.pt.pro.extra.interfaces.FloatingListener {

    private var fetchHandlerNative: android.os.Handler? = null

    private inline val fetchHand: android.os.Handler
        get() = fetchHandlerNative ?: ctxS.fetchHand.also {
            fetchHandlerNative = it
        }

    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    private inline val screenFolder: suspend () -> String
        get() = {
            ctxS.findStringPreference(
                SCR_FOL_KEY,
                com.pt.common.BuildConfig.ROOT.toStr + "/" + com.pt.common.BuildConfig.FILE_SCREEN
            )
        }

    override val windowManage: android.view.WindowManager get() = winManager ?: ctxS.fetchSystemWindowManager.also { winManager = it }
    private var winManager: android.view.WindowManager? = null

    override val headRestBall: com.pt.pro.databinding.FloatingHeadWindowsBinding get() = viewRootNull!!
    private var viewRootNull: com.pt.pro.databinding.FloatingHeadWindowsBinding? = null

    override val headMine: com.pt.pro.databinding.FloatingHeadBinding get() = viewRootTwo!!
    private var viewRootTwo: com.pt.pro.databinding.FloatingHeadBinding? = null

    private var screen: Boolean = false
    private var resultCode: Int = -1
    private var dataIntent: android.content.Intent? = null

    private var screenWidth: Int? = null
    private var screenHeight: Int? = null
    internal inline fun musicDim(a: (wid: Int, hei: Int) -> Unit) {
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

    private var beforeYouGo = 0

    private var para: android.view.WindowManager.LayoutParams? = null

    private var visible = false

    private var mMediaProjection: android.media.projection.MediaProjection? = null
    private var mVirtualDisplay: android.hardware.display.VirtualDisplay? = null
    private var mMediaRecorder: android.media.MediaRecorder? = null

    private var saveVideoString: String? = null

    @Volatile
    private var statRecordingB = false
    private var mediumQ = false
    private var isAudio = true
    private var xBefore: Int? = null
    private var yBefore: Int? = null
    private var landXBefore: Int? = null
    private var landYBefore: Int? = null

    private var isRight = false

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
                            stopRecording()
                            statRecordingB = false
                        }
                        Unit
                    }.postAfter()
                }
                isOn = it
            }
            runPower.postBackAfter()
        }

    @com.pt.common.global.MainAnn
    override fun android.view.WindowManager.setWindowManager() {
        winManager = this
    }

    @com.pt.common.global.MainAnn
    override fun com.pt.pro.databinding.FloatingHeadWindowsBinding.setViewRoot() {
        @com.pt.common.global.ViewAnn
        viewRootNull = this
    }

    @com.pt.common.global.MainAnn
    override fun com.pt.pro.databinding.FloatingHeadBinding.setViewRoot2() {
        @com.pt.common.global.ViewAnn
        viewRootTwo = this
    }

    @com.pt.common.global.MainAnn
    override fun setSerBoolean(serBool: Boolean) {
        this.screen = serBool
    }

    @com.pt.common.global.MainAnn
    override fun setResultCode(resultCode: Int) {
        this.resultCode = resultCode
    }

    @com.pt.common.global.MainAnn
    override fun android.content.Intent.setDataIntent() {
        dataIntent = this
    }

    override val getViewRoot: android.view.View get() = headRestBall.root

    override val getViewRoot2: android.view.View get() = headMine.root

    @com.pt.common.global.MainAnn
    override fun setInit() {
        setParams()
    }

    override val fetchParams: android.view.WindowManager.LayoutParams get() = para ?: floatingWindowManger().also { para = it }

    @com.pt.common.global.MainAnn
    private fun setParams() {
        ctxS.fetchDimensions {
            screenWidth = this@fetchDimensions.width
            screenHeight = this@fetchDimensions.height
            para = floatingWindowManger()
            fetchParams.x = this@fetchDimensions.width / 2
            fetchParams.y = -100
        }
    }


    @com.pt.common.global.UiAnn
    override fun updateViewRoot() {
        headMine.apps.apply {
            touchListener?.setContactListener(true)
        }
        isRight = recS.isRightToLeft

        with(headRestBall) {
            if (screen) {
                screenShootFrame.justVisible()
                screenRecordFrame.justVisible()
            } else {
                screenShootFrame.justGone()
                screenRecordFrame.justGone()
            }

            alarmHead.setOnClickListener(this@FloatingHeadManager)
            fileHead.setOnClickListener(this@FloatingHeadManager)
            dataHead.setOnClickListener(this@FloatingHeadManager)
            galleryFloating.setOnClickListener(this@FloatingHeadManager)
            screenShootHead.setOnClickListener(this@FloatingHeadManager)
            screenRecordHead.setOnClickListener(this@FloatingHeadManager)
        }
        headRestBall.changeFramesForEnd(screen = screen, isRight = isRight)

    }

    private var touchListener: com.pt.common.moderator.touch.ContactListener? =
        object : com.pt.common.moderator.touch.ContactListener {

            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onUp(v: android.view.View) {

            }

            override fun onUp(v: android.view.View, event: android.view.MotionEvent) {
                if (initialX == fetchParams.x && initialY == fetchParams.y) {
                    headRestBall.changeVisibility()
                } else {
                    val con = recS.configuration
                    val landScape = con.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
                    musicDim { wid, _ ->
                        if (fetchParams.x >= 0) {
                            if (!landScape) xBefore = wid / 2 else landXBefore = wid / 2
                            animationOpenFloat(fetchParams.x, wid / 2)
                            headRestBall.changeFramesForEnd(screen = screen, isRight = isRight)
                        } else {
                            if (!landScape)
                                xBefore = -(wid / 2)
                            else landXBefore =
                                -(wid / 2)

                            animationOpenFloat(fetchParams.x, -(wid / 2))
                            headRestBall.changeFramesForStart(isRight = isRight, screen = screen)
                        }

                        if (!landScape) yBefore = fetchParams.y else landYBefore = fetchParams.y
                        beforeYouGo = fetchParams.y
                    }
                }
            }

            override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
                initialX = fetchParams.x
                initialY = fetchParams.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
            }

            override fun onMove(v: android.view.View, event: android.view.MotionEvent) {
                fetchParams.x = initialX + (event.rawX - initialTouchX).toInt()
                fetchParams.y = initialY + (event.rawY - initialTouchY).toInt()
                context.catchyBadToken {
                    windowManage.updateViewLayout(headRestBall.root, fetchParams)
                    windowManage.updateViewLayout(headMine.root, fetchParams)
                }
                if (fetchParams.x >= 0)
                    headRestBall.changeFramesForEnd(screen = screen, isRight = isRight)
                else
                    headRestBall.changeFramesForStart(isRight = isRight, screen = screen)
            }

        }


    @com.pt.common.global.UiAnn
    override fun animationOpenFloat(beginValue: Int, endValue: Int) {
        returnForSlip(beginValue, endValue) {
            duration = 300
            addUpdateListener {
                fetchParams.x = (animatedValue as Int)
                context?.catchyBadToken {
                    windowManage.updateViewLayout(
                        viewRootTwo?.root ?: return@catchyBadToken, fetchParams
                    )
                    windowManage.updateViewLayout(
                        viewRootNull?.root ?: return@catchyBadToken,
                        fetchParams
                    )
                }
            }
            start()
        }
    }

    @com.pt.common.global.UiAnn
    private fun animationUp(beginValue: Int, endValue: Int) {
        returnForSlip(begin = beginValue, end = endValue) {
            duration = 200
            addUpdateListener {
                fetchParams.y = (animatedValue as Int)
                context?.catchyBadToken {
                    windowManage.updateViewLayout(
                        viewRootTwo?.root ?: return@catchyBadToken, fetchParams
                    )
                    windowManage.updateViewLayout(
                        viewRootNull?.root ?: return@catchyBadToken,
                        fetchParams
                    )
                }
            }
            start()
        }
    }

    @com.pt.common.global.UiAnn
    private fun animationUpDown(startX: Int, endX: Int, startY: Int, endY: Int) {
        returnAnimForChange(startX, endX, startY, endY) {
            duration = 100
            addUpdateListener {
                fetchParams.x = getAnimatedValue(com.pt.common.BuildConfig.X_DIMENSION) as Int
                fetchParams.y = getAnimatedValue(com.pt.common.BuildConfig.Y_DIMENSION) as Int
                context?.catchyBadToken {
                    windowManage.updateViewLayout(
                        viewRootTwo?.root ?: return@catchyBadToken, fetchParams
                    )
                    windowManage.updateViewLayout(
                        viewRootNull?.root ?: return@catchyBadToken,
                        fetchParams
                    )
                }
            }
            start()
        }
    }

    private var anim: androidx.core.animation.ValueAnimator? = null

    private fun addAnimAlpha(col: Int, from: Int, to: Int) {
        returnForSlip(
            from,
            to
        ) {
            anim = this
            addUpdateListener {
                (animatedValue as Int).let { f ->
                    viewRootTwo?.apply {
                        apps.backReColor(col.toColorAlpha(f))
                    }
                }
            }
            endListener {
                if (statRecordingB) {
                    addAnimAlpha(col, to, from)
                } else {
                    stopAnim()
                }
            }
            duration = 2000L
            start()
        }
    }

    private fun <T> launchActivity(cla: Class<T>) {
        android.content.Intent(ctxS, cla).apply {
            flags = BACKGROUND_FLAGS
            action = android.content.Intent.ACTION_RUN
            addCategory(android.content.Intent.CATEGORY_LAUNCHER)
        }.launcher()
    }

    override fun onClick(p0: android.view.View?) {
        headRestBall.apply {
            changeVisibility()
            when ((p0 ?: return)) {
                alarmHead -> {
                    launchActivity(com.pt.pro.alarm.views.MainActivityAlarm::class.java)
                }
                fileHead -> {
                    launchActivity(com.pt.pro.file.views.MainFileManager::class.java)
                }
                dataHead -> {
                    launchActivity(com.pt.pro.notepad.activities.NoteActivity::class.java)
                }
                galleryFloating -> {
                    launchActivity(com.pt.pro.gallery.activities.ActivityGallery::class.java)
                }
                screenShootHead -> runShout.postBackAfter()
                screenRecordHead -> startRecord()
            }
        }
    }

    private fun startRecord() {
        if (statRecordingB) {
            statRecordingB = false
            stopRecording()
            ctxS.vibrationProvider(vibe = 200)
        } else {
            ctxS.vibrationProvider(vibe = 400)
            statRecordingB = true
            broadcast()
        }
    }

    private inline val runShout: DSack<() -> Unit, Int, Long>
        get() = toCatchSackAfter(33, 500L) {
            if (dataIntent != null) {
                musicDim { wid, hei ->
                    launchDef {
                        ctxS.fetchSystemService(mediaProjectionService).run {
                            this?.getMediaProjection(resultCode, dataIntent ?: return@run null)
                        }.applySusBack {
                            runCatching {
                                try {
                                    this?.startCapture(wid = wid, hei = hei)
                                } catch (_: IllegalStateException) {
                                }
                            }
                        }
                    }
                }
            }
        }

    @com.pt.common.global.WorkerAnn
    private fun android.content.Intent.launcher() {
        android.app.PendingIntent.getActivity(ctxS, 0, this, PEND_FLAG).send()
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.databinding.FloatingHeadWindowsBinding.changeVisibility() {
        if (!visible) {
            appsFrame.justVisible()
            constraintOne.apply {
                justInvisible()
                visibleFade(200L)
            }
        } else {
            constraintOne.apply {
                invisibleFade(200L)
                toCatchSackAfter(18, 400L) {
                    appsFrame.justGone()
                    justGone()
                }.postAfter()
            }
        }
        visible = !visible
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun android.media.projection.MediaProjection.startCapture(wid: Int, hei: Int) {
        android.graphics.PixelFormat.RGBA_8888.let {
            if (isV_Q) {
                android.media.ImageReader.newInstance(
                    wid,
                    hei,
                    it,
                    5,
                    android.hardware.HardwareBuffer.USAGE_GPU_SAMPLED_IMAGE
                )
            } else {
                android.media.ImageReader.newInstance(wid, hei, it, 5)
            }
        }.alsoSusBack { oriIR ->
            createVirtualDisplay(
                FloatingHeadManager::class.java.simpleName,
                wid,
                hei,
                disS.densityDpi,
                android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY or
                        android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                oriIR.surface,
                null,
                null
            ).alsoSusBack { vd ->
                oriIR.setOnImageAvailableListener({ itR ->
                    val first = kotlin.runCatching {
                        try {
                            itR.acquireNextImage()
                        } catch (_: IllegalStateException) {
                            null
                        }
                    }.getOrNull()
                    if (first != null) {
                        toCatchSackAfter(99, 150L) {
                            launchDef {
                                first.doScreenshot(wid = wid, hei = hei) {
                                    withBack {
                                        FileLate(screenFolder(), com.pt.pro.extra.utils.SettingHelper.newScreenshot).letSusBack {
                                            saveImage(it) { itBool ->
                                                if (itBool) {
                                                    justScope {
                                                        if (isV_P) {
                                                            itR.discardFreeBuffers()
                                                        } else return@justScope
                                                    }
                                                    justScope {
                                                        itR.close()
                                                    }
                                                    justScope {
                                                        first.close()
                                                    }
                                                    justScope {
                                                        vd?.release()
                                                        this@startCapture.stop()
                                                    }
                                                    ctxS.makeToastRecSus(com.pt.pro.R.string.ss, 0)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }.postBackAfter()
                    }
                }, fetchHand)
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend inline fun android.media.Image.doScreenshot(
        wid: Int,
        hei: Int,
        crossinline bit: suspend android.graphics.Bitmap.() -> Unit
    ) {
        withBack {
            return@withBack withSusBack(planes) {
                (this[0].rowStride - this[0].pixelStride * wid).runSusBack {
                    android.graphics.Bitmap.createBitmap(
                        wid + this / this@withSusBack[0].pixelStride,
                        hei,
                        android.graphics.Bitmap.Config.ARGB_8888
                    ).alsoSusBack {
                        it.copyPixelsFromBuffer(this@withSusBack[0].buffer)
                    }.letSusBack {
                        android.graphics.Rect(0, 0, wid, hei).letSusBack { itR ->
                            android.graphics.Bitmap.createBitmap(
                                it,
                                itR.left,
                                itR.top,
                                itR.width(),
                                itR.height()
                            ).letSusBack(bit)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend inline fun android.graphics.Bitmap.saveImage(
        file: FileLate,
        crossinline bool: suspend (Boolean) -> Unit,
    ) {
        writeBitmapSus(file).applySusBack {
            withBack {
                MediaSack(
                    file.name, file.absolutePath, null, true,
                    byteCount.toLong(), width, height, System.currentTimeMillis()
                ).let {
                    contS.mediaStoreImage(it, file.absolutePath, null)
                }.letSusBack {
                    justScope {
                        ctxS.callBroadCastInsert(file.absolutePath)
                        bool(true)
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun broadcast() {
        if (dataIntent == null) return
        launchDef {
            withDefault {
                runPower.postBackAfter()
                ctxS.fetchSystemService(mediaProjectionService).run {
                    this?.getMediaProjection(resultCode, dataIntent ?: return@withDefault)
                }?.apply {
                    musicDim { wid, hei ->
                        touchScreenRecord(isAudio && ctxS.hasVoicePermission, true, wid = wid, hei = hei)
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun android.media.projection.MediaProjection.touchScreenRecord(isAud: Boolean, retry: Boolean, wid: Int, hei: Int) {
        launchDef {
            if (isV_S) {
                android.media.MediaRecorder(ctxS)
            } else {
                @Suppress("DEPRECATION")
                android.media.MediaRecorder()
            }.runSusBack {
                mMediaRecorder = this
                mMediaProjection = this@touchScreenRecord
                kotlin.runCatching {
                    recordCommon(isAud, wid = wid, hei = hei) {
                        prepareSus().letSusBack {
                            if (it == 1) {
                                screenRecordStart(retry, wid = wid, hei = hei)
                            } else {
                                failedRecord(retry, wid = wid, hei = hei)
                            }
                        }
                    }
                }.getOrElse {
                    failedRecord(retry, wid = wid, hei = hei)
                }
            }
        }
    }

    private suspend fun failedRecord(retry: Boolean, wid: Int, hei: Int) {
        doStopRecordSus(false)
        kotlinx.coroutines.delay(100)
        withDefault {
            if (retry) {
                ctxS.fetchSystemService(mediaProjectionService).run {
                    this?.getMediaProjection(resultCode, dataIntent ?: return@withDefault)
                }?.apply {
                    touchScreenRecord(isAud = false, retry = false, wid = wid, hei = hei)
                }
            } else {
                ctxS.makeToastRecSus(com.pt.pro.R.string.xe, 0)
            }
        }
    }

    private suspend fun android.media.MediaRecorder.prepareSus(): Int = justCoroutine {
        withBackDef(-1) {
            kotlin.runCatching {
                prepare()
                1
            }.getOrElse {
                it.toStr.logProvCrash("catchy")
                -1
            }
        }
    }

    private suspend fun android.media.projection.MediaProjection.screenRecordStart(retry: Boolean, wid: Int, hei: Int) {
        withBack {
            mVirtualDisplay = createVirtualDisplay(
                FloatingHeadManager::class.java.simpleName,
                wid,
                hei,
                disS.densityDpi,
                android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION,
                mMediaRecorder?.surface,
                callBackRecord,
                fetchHand
            )
            kotlin.runCatching {
                mMediaRecorder?.start()
                startAlpha()
            }.onFailure {
                failedRecord(retry, wid = wid, hei = hei)
            }
        }
    }

    private suspend inline fun android.media.MediaRecorder.recordCommon(
        isAud: Boolean,
        wid: Int,
        hei: Int,
        crossinline a: suspend () -> Unit
    ) {
        justCoroutine {
            if (isAud) {
                setAudioSource(soundSource)
            }
            setVideoSource(android.media.MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(android.media.MediaRecorder.OutputFormat.MPEG_4)
            setVideoSize(wid, hei)
            setVideoEncoder(android.media.MediaRecorder.VideoEncoder.H264)
            if (isAud) {
                setAudioEncoder(android.media.MediaRecorder.AudioEncoder.AMR_NB)
            }
            setVideoRate(mediumQ)
            setVideoFrameRate(if (mediumQ) 24 else 30)
            setRateAndEncoding(isAud)
            if (FileLate(screenFolder()).isDirectory) {
                saveVideoString = com.pt.pro.extra.utils.SettingHelper.run { screenFolder().newVideoRecord }
                setOutputFile(saveVideoString)
            } else {
                ctxS.makeToastRecSus(com.pt.pro.R.string.f7, 0)
            }
            setOnInfoListener { _, what, _ ->
                if (what == android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopRecording()
                }
            }
        }
        a.invoke()
    }


    private var callBackRecord: android.hardware.display.VirtualDisplay.Callback? = object : android.hardware.display.VirtualDisplay.Callback() {
        override fun onStopped() {
            super.onStopped()
            stopAnim()
        }
    }

    internal fun stopAnim() {
        toCatchSack(88) {
            anim?.apply {
                removeAllListeners()
                cancel()
            }
            viewRootTwo?.apply {
                apps.backReColor(
                    ctxS.theme.findAttr(android.R.attr.colorPrimary)
                )
            }
            Unit
        }.postNow()
    }


    private fun startAlpha() {
        launchDef {
            withMain {
                addAnimAlpha(ctxS.theme.findAttr(android.R.attr.colorPrimary), 255, 0)
            }
        }
    }

    private suspend fun stopAlpha() {
        withMain {
            anim?.applySus {
                removeAllListeners()
                cancel()
            }
        }
        kotlinx.coroutines.delay(100L)
        withMain {
            viewRootTwo?.applySus {
                apps.backReColor(ctxS.theme.findAttr(android.R.attr.colorPrimary))
            }
        }
    }

    private fun stopRecording() {
        launchDef {
            doStopRecordSus(true)
            withBack {
                FileLate(saveVideoString ?: return@withBack).letSusBack { itF ->
                    musicDim { wid, hei ->
                        MediaSack(
                            itF.name, itF.absolutePath, null, false,
                            itF.length(), wid, hei, System.currentTimeMillis()
                        ).letSusBack { itM ->
                            contS.mediaStoreVideo(itM, saveVideoString, null)
                        }
                    }
                }
                ctxS.callBroadCastInsert(saveVideoString ?: return@withBack)
            }
        }
    }


    private suspend fun doStopRecordSus(stop: Boolean) {
        stopAlpha()
        withBack {
            if (stop) {
                statRecordingB = false
            }
            mMediaRecorder?.applySusBack {
                kotlin.runCatching {
                    if (stop) {
                        stop()
                    }
                }
                /*kotlin.runCatching {
                    reset()
                }*/
                kotlin.runCatching {
                    release()
                }
            }
            /*kotlin.runCatching {
                mMediaRecorder?.prepare()
            }.onFailure {
                it.toStr.logProvCrash("mMediaRecorder")
            }*/
            mVirtualDisplay?.applySusBack {
                release()
            }
            mMediaProjection?.applySusBack {
                stop()
            }
            unBackPost(runPower.two)
            mMediaRecorder = null
            mVirtualDisplay = null
            mMediaProjection = null
        }
    }

    private fun doStopRecord() {
        kotlin.runCatching {
            mMediaRecorder?.apply {
                stop()
                //reset()
                release()
            }
            mMediaProjection?.stop()
            mVirtualDisplay?.release()
        }
        unBackPost(runPower.two)
    }

    @com.pt.common.global.MainAnn
    override fun onChange(newConfig: Int) {
        ctxS.fetchDimensions {
            screenWidth = this@fetchDimensions.width
            screenHeight = this@fetchDimensions.height
            if (newConfig == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                doChangePort(this@fetchDimensions.width)
            } else {
                doChangeLand(this@fetchDimensions.width)
            }
        }
    }

    private fun doChangePort(wid: Int) {
        (if (xBefore != null) xBefore else (wid / 2))?.let { xb ->
            xBefore = xb
            (if (yBefore != null) yBefore else -200)?.let { yb ->
                yBefore = yb
                animationUpDown(fetchParams.x, xb, fetchParams.y, yb)
                if (xb == abs(xb))
                    headRestBall.changeFramesForEnd(screen = screen, isRight = isRight)
                else
                    headRestBall.changeFramesForStart(isRight = isRight, screen = screen)
            }
        }
    }

    private fun doChangeLand(wid: Int) {
        (if (landXBefore != null) landXBefore else -(wid / 2))?.let { lxb ->
            landXBefore = lxb
            (if (landYBefore != null) landYBefore else -200)?.let { lyb ->
                landYBefore = lyb
                animationUpDown(fetchParams.x, lxb, fetchParams.y, lyb)
                if (lxb == abs(lxb))
                    headRestBall.changeFramesForEnd(screen = screen, isRight = isRight)
                else
                    headRestBall.changeFramesForStart(isRight = isRight, screen = screen)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun onKeyBoardOpened() {
        if (fetchParams.y > 50) {
            beforeYouGo = fetchParams.y
            animationUp(fetchParams.y, -200)
        }
    }

    @com.pt.common.global.UiAnn
    override fun onKeyBoardClosed() {
        if (beforeYouGo != fetchParams.y) {
            animationUp(fetchParams.y, beforeYouGo)
        }
    }

    override fun onServiceDestroy(b: () -> Unit) {
        context?.onDestroyService(b)
    }

    @com.pt.common.global.MainAnn
    private fun android.content.Context?.onDestroyService(b: () -> Unit) {
        runCatching {
            viewRootNull?.apply {
                alarmHead.onViewDestroy()
                screenShootHead.onViewDestroy()
                screenRecordHead.onViewDestroy()
                fileHead.onViewDestroy()
            }
            viewRootTwo?.apps?.onViewDestroy()
            catchyBadToken {
                windowManage.removeViewImmediate(headRestBall.root)
                windowManage.removeViewImmediate(headMine.root)
            }
            if (statRecordingB) {
                doStopRecord()
                //mServiceHandler?.removeCallbacksAndMessages(null)
                val file = FileLate(saveVideoString ?: return@runCatching)
                musicDim { wid, hei ->
                    val newMedia = MediaSack(
                        file.name, file.absolutePath, null, false,
                        file.length(), wid, hei, System.currentTimeMillis()
                    )
                    this?.contentResolver?.mediaVideoStore(newMedia, saveVideoString)
                    this?.callBroadCastInsert(saveVideoString ?: return@runCatching)
                }
            }
            anim?.apply {
                if (isRunning) {
                    removeAllListeners()
                    cancel()
                }
            }
            callBackRecord = null
            touchListener = null
            winManager = null
            viewRootNull = null
            viewRootTwo = null
            mMediaProjection = null
            mVirtualDisplay = null
            mMediaRecorder = null
            saveVideoString = null
            anim = null
            beforeYouGo = 0
            visible = false
            stateDestroy()
            fetchHandlerNative = null
            job.cancelJob()
            catchy(Unit) {
                dispatch.close()
            }
            ext.shutdownNow()
            cancelScope()
            extNative = null
            jobNative = null
            dispatcherNative = null

            b.invoke()
        }.onFailure {
            b.invoke()
        }
    }

}