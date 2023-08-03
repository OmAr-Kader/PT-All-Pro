package com.pt.pro.extra.background

import com.pt.common.global.*
import com.pt.common.media.callBroadCastInsert
import com.pt.common.media.mediaStoreImage
import com.pt.common.media.mediaStoreVideo
import com.pt.common.media.uriProviderNormal
import com.pt.common.stable.*
import kotlin.math.abs

class FloatingSwiperManager(ctx: android.content.Context) : com.pt.common.mutual.life.GlobalServiceManger(ctx),
    com.pt.pro.extra.interfaces.SwiperManagerListener {

    private var fetchHandlerNative: android.os.Handler? = null

    private inline val fetchHand: android.os.Handler
        get() = fetchHandlerNative ?: ctxS.fetchHand.also {
            fetchHandlerNative = it
        }

    private var smallCircle: com.pt.common.moderator.touch.TouchImageView? = null

    private val clickThreshold = 100

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

    @Volatile
    private var shootB = true

    private var mMediaProjection: android.media.projection.MediaProjection? = null
    private var mVirtualDisplay: android.hardware.display.VirtualDisplay? = null
    private var mMediaRecorder: android.media.MediaRecorder? = null
    private var saveVideoString: String? = null

    private var needToRecreate: Boolean = false

    @Volatile
    private var resultCode = -1

    @Volatile
    private var data: android.content.Intent? = null

    @Volatile
    private var statRecordingB = false

    @Volatile
    private var ifFailedToRecord = false

    private var mediumQ = false
    private var isAudio = true
    private var isEditorLaunch = false

    private var myPara: android.view.WindowManager.LayoutParams? = null

    private var windManager: android.view.WindowManager? = null
    override val windowSwiper: android.view.WindowManager get() = windManager ?: ctxS.fetchSystemWindowManager.also { windManager = it }

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

    private inline val screenFolder: suspend () -> String
        get() = {
            ctxS.findStringPreference(
                SCR_FOL_KEY,
                com.pt.common.BuildConfig.ROOT.toStr + "/" + com.pt.common.BuildConfig.FILE_SCREEN
            )
        }

    override val fetchParams: android.view.WindowManager.LayoutParams
        get() = myPara ?: run {
            if (recS.isLandTraditional) {
                floatingScreen(((screenWidth ?: 360F.toPixelS) * 0.7).toInt(), 12F.toPixelS)
            } else {
                floatingScreen(12F.toPixelS, ((screenHeight ?: 900F.toPixelS) * 0.7).toInt())
            }
        }.also {
            myPara = it
        }

    override val getViewRoot: android.view.View
        get() = smallCircle!!

    override val getViewRoot2: android.view.View
        get() = smallCircle!!


    override fun android.view.WindowManager.setWindowManager() {
        windManager = this
    }

    override fun android.content.Intent.setDataIntent(resultCode: Int) {
        this@FloatingSwiperManager.resultCode = resultCode
        this@FloatingSwiperManager.data = this@setDataIntent
    }

    override fun setInit() {
        getScreenSize()
        initializeView()
    }

    @com.pt.common.global.MainAnn
    private fun getScreenSize() {
        ctxS.fetchDimensions {
            screenWidth = this@fetchDimensions.width
            screenHeight = this@fetchDimensions.height
        }
    }


    @com.pt.common.global.UiAnn
    private fun initializeView() {
        com.pt.common.moderator.touch.TouchImageView(ctxS).apply {
            smallCircle = this@apply
            isClickable = true
            isFocusable = true
            adjustViewBounds = true
            scaleType = android.widget.ImageView.ScaleType.FIT_END
            isFocusableInTouchMode = true
        }
    }

    private fun android.content.Context.appearanceSwiper() {
        launchImdMain {
            justCoroutine {
                smallCircle?.alpha = 0.1F
                compactImage(com.pt.pro.R.drawable.ic_scanner_port) {
                    smallCircle?.setImageDrawable(this)
                }
            }
            justCoroutine {
                findBooleanPreference(IS_TRANS, true).letSusBack { isTransparent ->
                    if (isTransparent) {
                        android.graphics.Color.TRANSPARENT
                    } else {
                        if (shootB) {
                            ctxS.theme.findAttr(android.R.attr.colorPrimary)
                        } else {
                            ctxS.theme.findAttr(android.R.attr.colorAccent)
                        }
                    }
                }.let<@androidx.annotation.ColorInt Int, Unit> {
                    withMain {
                        smallCircle?.svgReColor(it)
                    }
                }
            }
        }
    }

    private fun addTintVew() {
        toCatchSack(99) {
            if (!ifFailedToRecord) {
                smallCircle?.apply {
                    alpha = 1.0F
                    svgReClear()
                }
                run.postNow()
            }
            Unit
        }.postNow()
    }

    override suspend fun removeTintSus(forceColor: Boolean) {
        withMain {
            removeTintVew(forceColor)
        }
    }

    override fun removeTintVew(forceColor: Boolean) {
        toCatchSack(88) {
            anim?.apply {
                if (isRunning) stop()
            }
            unPost(run.two)
            if (forceColor) {
                smallCircle?.svgReColor(android.graphics.Color.TRANSPARENT)
            } else {
                ctxS.appearanceSwiper()
            }
            Unit
        }.postNow()
    }

    private var anim: androidx.vectordrawable.graphics.drawable.Animatable2Compat? = null

    private val run: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            context?.apply {
                androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat.create(
                    this@apply,
                    com.pt.pro.R.drawable.avd_anim_right
                )?.also { dra ->
                    smallCircle?.apply {
                        setImageDrawable(dra)
                        (drawable as? androidx.vectordrawable.graphics.drawable.Animatable2Compat?)?.also {
                            anim = it
                            it.start()
                        }
                    }
                }
            }
            run.rKTSack(3000L).postAfter()
        }

    override fun updateViewRoot() {
        launchImdMain {
            justCoroutine {
                ctxS.applySus {
                    shootB = findBooleanPreference(SCR_SHOT, true)
                    mediumQ = findBooleanPreference(MED_QUAIL, true)
                    isAudio = findBooleanPreference(IS_AUD, true)
                    isEditorLaunch = findBooleanPreference(IS_EDITOR, false)
                }
            }
            justCoroutine {
                recS.configuration.let { newConfig ->
                    ctxS.appearanceSwiper()
                    if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                        portrait()
                        smallCircle?.apply {
                            onTouchListenerPort?.setContactListener(true)
                        }
                    } else if (newConfig.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
                        landscape()
                        smallCircle?.apply {
                            onTouchListenerLand?.setContactListener(true)
                        }
                    }
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    private fun portrait() {
        musicDim { wid, hei ->
            myPara = floatingScreen(12F.toPixelS, (hei * 0.7).toInt())
            fetchParams.apply {
                x = wid / 2
                y = 0
                softInputMode = android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
            }
            context.catchyBadToken {
                if (!(smallCircle ?: return@catchyBadToken).isShown) {
                    windManager?.addView(smallCircle, fetchParams)
                } else {
                    windManager?.updateViewLayout(smallCircle, fetchParams)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun landscape() {
        musicDim { wid, hei ->
            myPara = floatingScreen((wid * 0.7).toInt(), 12F.toPixelS)
            fetchParams.apply {
                x = 0
                y = hei / 2
                softInputMode = android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
            }
            context.catchyBadToken {
                if (!(smallCircle?.isShown ?: return@catchyBadToken)) {
                    windManager?.addView(smallCircle, fetchParams)
                } else {
                    windManager?.updateViewLayout(smallCircle, fetchParams)
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
        withBack {
            if (retry) {
                ctxS.fetchSystemService(mediaProjectionService).run {
                    this?.getMediaProjection(resultCode, data ?: return@withBack)
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
            }.getOrDefault(-1)
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
        withBack {
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
            removeTintVew(false)
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun touchScreenShot(resultCode: Int, data: android.content.Intent) {
        ctxS.fetchSystemService(mediaProjectionService).run {
            this?.getMediaProjection(resultCode, data)
        }?.apply {
            musicDim { wid, hei ->
                runCatching {
                    try {
                        launchDef {
                            startCapture(wid = wid, hei = hei)
                        }
                    } catch (_: IllegalStateException) {
                    }
                }
            }
        }
    }

    override fun onClick(p0: android.view.View?) {}


    override fun onKeyBoardOpened() {}

    override fun onKeyBoardClosed() {}

    override fun onChange(newConfig: Int) {
        launchMain {
            withMain {
                getScreenSize()
                context.catchyBadToken {
                    windowSwiper.removeViewImmediate(smallCircle)
                }
            }
            withMain {
                if (newConfig == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                    portrait()
                    smallCircle?.apply {
                        onTouchListenerPort?.setContactListener(true)
                    }
                } else {
                    landscape()
                    smallCircle?.apply {
                        onTouchListenerLand?.setContactListener(true)
                    }
                }
            }
        }
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
                fetchHand
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
                        toCatchSackAfter(77, 150L) {
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
        crossinline bit: suspend android.graphics.Bitmap.() -> Unit,
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
        writeBitmapSus(file).alsoSusBack {
            withBack {
                MediaSack(
                    file.name, file.absolutePath, null, true,
                    byteCount.toLong(), width, height, System.currentTimeMillis()
                ).letSusBack {
                    ctxS.contentResolver.mediaStoreImage(it, file.absolutePath, null)
                }.letSusBack {
                    justScope {
                        ctxS.callBroadCastInsert(file.absolutePath)
                        bool(true)
                        if (isEditorLaunch) {
                            ctxS.startActivity(
                                ctxS.uriProviderNormal(
                                    file.absolutePath,
                                    com.pt.pro.BuildConfig.APPLICATION_ID
                                ).goToPhotoView
                            )
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun stopRecording() {
        launchDef {
            doStopRecordSus(true)
            kotlinx.coroutines.delay(100L)
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
            }
            withBack {
                ctxS.callBroadCastInsert(saveVideoString ?: return@withBack)
                if (isEditorLaunch) {
                    saveVideoString?.letSusBack {
                        ctxS.startActivity(
                            ctxS.uriProviderNormal(
                                it,
                                com.pt.pro.BuildConfig.APPLICATION_ID
                            ).goToPhotoView
                        )
                    }
                }
            }
        }
    }

    private suspend fun doStopRecordSus(stop: Boolean) {
        removeTintSus(false)
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
            kotlin.runCatching {
                mMediaRecorder?.prepare()
            }.onFailure {
                it.toStr.logProvCrash("mMediaRecorder")
            }
            mVirtualDisplay?.applySusBack {
                release()
            }
            mMediaProjection?.applySusBack {
                stop()
            }
            unBackPostAll()
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
            mVirtualDisplay?.release()
            mMediaProjection?.stop()
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun broadcast() {
        runPower.postBackAfter()
        ctxS.fetchSystemService(mediaProjectionService).run {
            this?.getMediaProjection(resultCode, data ?: return)
        }?.apply {
            musicDim { wid, hei ->
                touchScreenRecord(isAudio && ctxS.hasVoicePermission, true, wid = wid, hei = hei)
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun recreateScannerPort() {
        toCatchSackAfter(486, 1800L) {
            portrait()
            needToRecreate = false
        }.postAfter()
    }

    private var onTouchListenerPort: com.pt.common.moderator.touch.ContactListener? =
        object : com.pt.common.moderator.touch.ContactListener {

            private var canGo = false
            private var initialX = 0
            private var initialTouchX = 0f
            private var parX = (screenWidth ?: 300F.toPixelS) / 2

            override fun onUp(v: android.view.View) {}

            override fun onUp(v: android.view.View, event: android.view.MotionEvent) {
                val duration = event.eventTime - event.downTime
                if (event.rawX <= ((screenWidth ?: 300F.toPixelS) * 0.25).toFloat() && canGo) {
                    if (shootB) {
                        runCatching {
                            smallCircle.justInvisible()
                        }.onFailure {
                            (smallCircle ?: return@onFailure).visibility = android.view.View.INVISIBLE
                        }
                        data?.let { touchScreenShot(resultCode, it) }
                        toCatchSackAfter(24, 500L) {
                            runCatching {
                                (smallCircle ?: return@runCatching).visibleFade(300)
                            }.onFailure {
                                (smallCircle ?: return@onFailure).visibility = android.view.View.VISIBLE
                            }
                            Unit
                        }.postAfter()
                    } else {
                        if (statRecordingB) {
                            stopRecording()
                            ctxS.makeToastRec(com.pt.pro.R.string.rf, 0)
                            statRecordingB = false
                            ctxS.vibrationProvider(vibe = 200)
                        } else {
                            broadcast()
                            statRecordingB = true
                            ctxS.vibrationProvider(vibe = 600)
                        }
                    }
                }
                if (duration < clickThreshold) {
                    toCatchSack(483) {
                        context.catchyBadToken {
                            windowSwiper.removeViewImmediate(smallCircle)
                        }
                    }.postNow()
                    recreateScannerPort()
                    needToRecreate = true
                }
                if (!needToRecreate) {
                    musicDim { wid, _ ->
                        v.animationOpen(parX, wid / 2, canGo && statRecordingB && !shootB)
                    }
                }
            }

            override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
                initialX = fetchParams.x
                initialTouchX = event.rawX
                if (!shootB && statRecordingB) {
                    removeTintVew(true)
                }
            }

            override fun onMove(v: android.view.View, event: android.view.MotionEvent) {
                val s = initialX + (event.rawX - initialTouchX).toInt()
                canGo = if (abs(s - fetchParams.x) < 100) {
                    fetchParams.x = initialX + (event.rawX - initialTouchX).toInt()
                    parX = fetchParams.x
                    context.catchyBadToken {
                        windowSwiper.updateViewLayout(smallCircle, fetchParams)
                    }
                    true
                } else {
                    false
                }
            }
        }


    @com.pt.common.global.UiAnn
    override fun android.view.View.animationOpen(beginValue: Int, endValue: Int, isNeedAnim: Boolean) {
        returnForSlip(beginValue, endValue) {
            duration = 300
            addUpdateListener {
                fetchParams.x = (animatedValue as Int)
                context.catchyBadToken {
                    windowSwiper.updateViewLayout(this@animationOpen, fetchParams)
                }
            }
            if (isNeedAnim) {
                endListener {
                    addTintVew()
                }
            }
            start()
        }
    }

    @com.pt.common.global.UiAnn
    override fun recreateScannerLand() {
        toCatchSackAfter(484, 1800L) {
            landscape()
            needToRecreate = false
        }.postAfter()
    }

    private var onTouchListenerLand: com.pt.common.moderator.touch.ContactListener? = object : com.pt.common.moderator.touch.ContactListener {
        private var initialY = 0
        private var canGo = false
        private var initialTouchY = 0f
        private var parY = ((screenHeight ?: 1000F.toPixelS) / 2.17).toInt()

        override fun onUp(v: android.view.View) {}

        override fun onUp(v: android.view.View, event: android.view.MotionEvent) {
            val duration = event.eventTime - event.downTime
            if (event.rawY <= ((screenHeight ?: 1000F.toPixelS) * 0.25).toFloat() && canGo) {
                if (shootB) {
                    runCatching {
                        smallCircle.justInvisible()
                    }.onFailure {
                        (smallCircle ?: return@onFailure).visibility = android.view.View.INVISIBLE
                    }
                    data?.let { touchScreenShot(resultCode, it) }
                    toCatchSackAfter(25, 500L) {
                        runCatching {
                            (smallCircle ?: return@runCatching).visibleFade(300)
                        }.onFailure {
                            (smallCircle ?: return@onFailure).visibility = android.view.View.VISIBLE
                        }
                        Unit
                    }.postAfter()
                } else {
                    if (statRecordingB) {
                        stopRecording()
                        ctxS.makeToastRec(com.pt.pro.R.string.rf, 0)
                        statRecordingB = false
                        ctxS.vibrationProvider(vibe = 200)
                    } else {
                        ctxS.makeToastRec(com.pt.pro.R.string.rs, 0)
                        /*handRecord.removeCallbacksAndMessages(null)
                        handRecord.postDelayed({
                        }, 1500)*/
                        broadcast()
                        statRecordingB = true
                        ctxS.vibrationProvider(vibe = 600)
                    }
                }
            }
            if (duration < clickThreshold) {
                toCatchSack(483) {
                    context.catchyBadToken {
                        windowSwiper.removeViewImmediate(smallCircle)
                    }
                }.postNow()
                recreateScannerLand()
                needToRecreate = true
            }
            if (!needToRecreate) {
                musicDim { _, hei ->
                    v.animationUp(parY, hei / 2, canGo && statRecordingB && !shootB)
                }
            }
        }

        override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
            initialY = fetchParams.y
            initialTouchY = event.rawY
            if (!shootB && statRecordingB) {
                removeTintVew(true)
            }
        }

        override fun onMove(v: android.view.View, event: android.view.MotionEvent) {
            val a = initialY + (event.rawY - initialTouchY).toInt()
            canGo = if (abs(a - fetchParams.y) < 100) {
                fetchParams.y = initialY + (event.rawY - initialTouchY).toInt()
                parY = fetchParams.y
                context.catchyBadToken {
                    windowSwiper.updateViewLayout(v, fetchParams)
                }
                true
            } else {
                false
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun android.view.View.animationUp(beginValue: Int, endValue: Int, isNeedAnim: Boolean) {
        returnForSlip(begin = beginValue, end = endValue) {
            duration = 200
            addUpdateListener {
                fetchParams.y = (animatedValue as Int)
                windowSwiper.updateViewLayout(this@animationUp, fetchParams)
            }
            if (isNeedAnim) {
                endListener {
                    addTintVew()
                }
            }
            start()
        }
    }


    override fun onServiceDestroy(b: () -> Unit) {
        context?.onDestroyService(b)
    }

    @com.pt.common.global.MainAnn
    private fun android.content.Context?.onDestroyService(b: () -> Unit) {
        kotlin.runCatching {
            smallCircle?.onViewDestroy()
            catchyBadToken {
                windManager?.removeViewImmediate(smallCircle)
            }
            removeTintVew(false)
            doStopRecord()
            stateDestroy()
            mMediaRecorder = null
            mMediaProjection = null
            mVirtualDisplay = null
            windManager = null
            smallCircle = null
            anim = null
            callBackRecord = null
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