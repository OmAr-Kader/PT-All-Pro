package com.pt.pro.extra.background

import com.pt.common.global.*
import com.pt.common.stable.*
import kotlin.math.abs

open class NaviHeadManager(ctx: android.content.Context) : GlobalServiceHead(ctx), android.hardware.SensorEventListener,
    com.pt.pro.extra.interfaces.NaviListener {

    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    private var lastVol = 0.0F
    private var lastBrt = 0.0F

    private var sensorManager: android.hardware.SensorManager? = null

    override val windowManage: android.view.WindowManager get() = winManager ?: ctxS.fetchSystemWindowManager.also { winManager = it }
    private var winManager: android.view.WindowManager? = null

    private var layout: com.pt.pro.databinding.FloatingHeadWindowsBinding? = null
    private var layout2: com.pt.pro.databinding.FloatingHeadAppsBinding? = null
    private var extend: Boolean = true

    override val headRestBall: com.pt.pro.databinding.FloatingHeadWindowsBinding get() = layout!!

    override val ballNavi: com.pt.pro.databinding.FloatingHeadAppsBinding get() = layout2!!

    private var myPara: android.view.WindowManager.LayoutParams? = null

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
    private var visible = false

    private var ifHaveLongRun = false

    internal var audioManagerNative: android.media.AudioManager? = null
    internal inline val audioManager: android.media.AudioManager?
        get() = audioManagerNative ?: ctxS.fetchSystemService(audioService).also {
            audioManagerNative = it
        }

    @Volatile
    private var volume: Int = 1

    @Volatile
    private var maxVol: Int = 16

    @Volatile
    private var correction = false

    @Volatile
    private var isLandscape = false

    @Volatile
    private var check = false

    @Volatile
    private var checkDone270 = false

    @Volatile
    private var checkDone90 = false

    private var fixedRotation: Boolean = false
    private var xBefore: Int? = null
    private var yBefore: Int? = null
    private var landXBefore: Int? = null
    private var landYBefore: Int? = null

    private var isRight = false
    private var screen: Boolean = true


    private inline val priAccent: Int
        get() = ctxS.theme.findAttr(android.R.attr.colorAccent)

    private inline val txtPri: Int
        get() = ctxS.theme.findAttr(android.R.attr.colorPrimary)

    @com.pt.common.global.MainAnn
    override fun android.view.WindowManager.setWindowManager() {
        winManager = this
    }

    @com.pt.common.global.MainAnn
    override fun com.pt.pro.databinding.FloatingHeadWindowsBinding.setViewRoot() {
        @com.pt.common.global.ViewAnn
        layout = this
    }

    @com.pt.common.global.MainAnn
    override fun com.pt.pro.databinding.FloatingHeadAppsBinding.setLayout2() {
        @com.pt.common.global.ViewAnn
        layout2 = this
    }

    @com.pt.common.global.MainAnn
    override fun setSerBoolean(serBool: Boolean) {
        this.extend = serBool
    }

    @com.pt.common.global.MainAnn
    override fun setInit() {
        setParams()
    }

    @com.pt.common.global.MainAnn
    private fun setParams() {
        ctxS.fetchDimensions {
            screenWidth = this@fetchDimensions.width
            screenHeight = this@fetchDimensions.height
            myPara = floatingWindowManger()
            fetchParams.x = this@fetchDimensions.width / 2
            fetchParams.y = 100
        }
    }

    override val fetchParams: android.view.WindowManager.LayoutParams
        get() = myPara ?: floatingWindowManger().also {
            myPara = it
        }

    override val getViewRoot: android.view.View get() = headRestBall.root

    override val getViewRoot2: android.view.View get() = ballNavi.root

    @com.pt.common.global.UiAnn
    override fun updateViewRoot() {
        ballNavi.apps.apply {
            touchListener?.setContactListener(true)
        }

        isRight = recS.isRightToLeft

        ctxS.fetchSystemService(audioService).also {
            audioManagerNative = it
            maxVol = it?.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC) ?: 16
            val curVol: Int = it?.getStreamVolume(android.media.AudioManager.STREAM_MUSIC) ?: 3
            lastVol = (curVol.toFloat() / maxVol.toFloat())
            lastBrt = android.provider.Settings.System.getInt(contS, android.provider.Settings.System.SCREEN_BRIGHTNESS).toFloat() / 255F
            volume = curVol
        }
        with<com.pt.pro.databinding.FloatingHeadWindowsBinding, Unit>(headRestBall) {
            alarmFrame.backReColor(android.graphics.Color.BLACK)
            fileFrame.backReColor(android.graphics.Color.BLACK)
            screenShootFrame.backReColor(android.graphics.Color.BLACK)
            screenRecordFrame.backReColor(android.graphics.Color.BLACK)
            alarmFrameNavi.svgReColor(beforeAlpha(priAccent, (lastVol * 255)))
            alarmHead.apply {
                ctxS.compactImage(com.pt.pro.R.drawable.ic_volume) {
                    setImageDrawable(this)
                }
                //svgReColor(priAccent.isColorDarkText)
                svgReColorWhite()
                volPlusListener?.setContactListener(true)
            }
            fileFrameNavi.svgReColor(beforeAlpha(priAccent, ((1 - lastVol) * 255)))
            fileHead.apply {
                ctxS.compactImage(com.pt.pro.R.drawable.ic_volume_off) {
                    setImageDrawable(this)
                }
                //svgReColor(priAccent.isColorDarkText)
                svgReColorWhite()
                volMinusListener?.setContactListener(true)
            }
            dataHead.apply {
                ctxS.compactImage(com.pt.pro.R.drawable.ic_rotation_screen) {
                    setImageDrawable(this)
                }
                setOnClickListener(this@NaviHeadManager)
            }
            galleryFloating.apply {
                ctxS.compactImage(com.pt.pro.R.drawable.ic_notification) {
                    setImageDrawable(this)
                }
                setOnClickListener(this@NaviHeadManager)
            }
            screenShootFrameNavi.svgReColor(beforeAlpha(txtPri, (lastBrt * 255)))
            screenShootHead.apply {
                ctxS.compactImage(com.pt.pro.R.drawable.ic_screen_brightness) {
                    setImageDrawable(this)
                }
                //svgReColor(txtPri.isColorDarkText)
                svgReColorWhite()
                brtPlusListener?.setContactListener(true)
            }
            screenRecordFrameNavi.svgReColor(beforeAlpha(txtPri, ((1 - lastBrt) * 255)))
            screenRecordHead.apply {
                ctxS.compactImage(com.pt.pro.R.drawable.ic_brightness_off) {
                    setImageDrawable(this)
                }
                //svgReColor(txtPri.isColorDarkText)
                svgReColorWhite()
                brtMinusListener?.setContactListener(true)
            }
        }
        /*if (!screen) {
            screenShootFrame.justGone()
            screenRecordFrame.justGone()
        }*/
        headRestBall.changeFramesForEnd(screen = screen, isRight = isRight)
//////////////////////////////////////////////////////////////////////////////////////////////////

        ctxS.fetchSystemService(sensorService)?.let {
            it.registerListener(
                this,
                it.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER),
                android.hardware.SensorManager.SENSOR_DELAY_NORMAL
            )
            sensorManager = it
        }
        oV?.let {
            contS.registerContentObserver(
                android.provider.Settings.System.CONTENT_URI,
                true,
                it
            )
        }
    }


    @com.pt.common.global.WorkerAnn
    override fun android.media.AudioManager.volume(inc: Boolean) {
        catchyUnit {
            if (inc) {
                adjustStreamVolume(
                    android.media.AudioManager.STREAM_MUSIC,
                    android.media.AudioManager.ADJUST_RAISE,
                    0
                )
            } else {
                adjustStreamVolume(
                    android.media.AudioManager.STREAM_MUSIC,
                    android.media.AudioManager.ADJUST_LOWER,
                    0
                )
            }
        }
    }

    override fun onClick(p0: android.view.View?) {
        when (p0) {
            headRestBall.galleryFloating -> {
                ctxS.expendStatusBar(true).let {
                    if (!it) ctxS.makeToastRec(com.pt.pro.R.string.xe, 0) else headRestBall.changeVisibilityNavi()
                }
            }
            headRestBall.dataHead -> {
                headRestBall.changeVisibilityNavi()
                ctxS.rotation()
            }
        }
    }

    override val volMinusRun: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            if (volume != 0 && context != null) {
                audioManager?.volume(false)
                volMinusRun.rKTSack(200L).postBackAfter()
            }
            Unit
        }


    private var volMinusListener: com.pt.common.moderator.touch.ContactListener? =
        object : com.pt.common.moderator.touch.ContactListener {
            override fun onUp(v: android.view.View) {
                unPost(volMinusRun.two)
                unPost(volPlusRun.two)
            }

            override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
                if (volume != 0) {
                    audioManager?.volume(false)
                    volMinusRun.rKTSack(100L).postBackAfter()
                }
            }

            override fun onMove(v: android.view.View, event: android.view.MotionEvent) {}

        }


    override val volPlusRun: DSackT<() -> Unit, Int>
        get() = toCatchSack(22) {
            if (volume < maxVol && context != null) {
                audioManager?.volume(true)
                volPlusRun.rKTSack(200L).postBackAfter()
            }
        }

    private var volPlusListener: com.pt.common.moderator.touch.ContactListener? =
        object : com.pt.common.moderator.touch.ContactListener {
            override fun onUp(v: android.view.View) {
                unPost(volMinusRun.two)
                unPost(volPlusRun.two)
            }

            override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
                if (volume < maxVol) {
                    audioManager?.volume(true)
                    volPlusRun.rKTSack(100L).postBackAfter()
                }
            }

            override fun onMove(v: android.view.View, event: android.view.MotionEvent) {

            }

        }

    private inline val android.content.Context.settingChecker: Boolean
        get() {
            return if (isCanWriteSetting) {
                true
            } else {
                ctxS.startActivity(goToSetting)
                false
            }
        }

    internal val brtMinusRun: DSackT<() -> Unit, Int>
        get() = toCatchSack(33) {
            if (context != null) {
                doBrightChange(false)
                brtMinusRun.rKTSack(200L).postBackAfter()
            }
            Unit
        }

    override fun doBrtMin() {
        if (ctxS.settingChecker) {
            doBrightChange(false)
            brtMinusRun.rKTSack(100L).postBackAfter()
        }
    }

    private var brtMinusListener: com.pt.common.moderator.touch.ContactListener? =
        object : com.pt.common.moderator.touch.ContactListener {
            override fun onUp(v: android.view.View) {
                unPost(brtMinusRun.two)
                unPost(brtPlusRun.two)
            }

            override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
                doBrtMin()
            }

            override fun onMove(v: android.view.View, event: android.view.MotionEvent) {}
        }

    internal val brtPlusRun: DSackT<() -> Unit, Int>
        get() = toCatchSack(44) {
            if (context != null) {
                doBrightChange(true)
                brtPlusRun.rKTSack(200L).postBackAfter()
            }
            Unit
        }

    override fun doBrtMax() {
        if (ctxS.settingChecker) {
            doBrightChange(true)
            brtPlusRun.rKTSack(100L).postBackAfter()
        }
    }

    private var brtPlusListener: com.pt.common.moderator.touch.ContactListener? =
        object : com.pt.common.moderator.touch.ContactListener {
            override fun onUp(v: android.view.View) {
                unPost(brtMinusRun.two)
                unPost(brtPlusRun.two)
            }

            override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
                doBrtMax()
            }

            override fun onMove(v: android.view.View, event: android.view.MotionEvent) {

            }

        }


    @com.pt.common.global.WorkerAnn
    private fun doBrightChange(inc: Boolean) {
        catchyUnit {
            android.provider.Settings.System.getInt(contS, android.provider.Settings.System.SCREEN_BRIGHTNESS).let { br ->
                if (inc) {
                    if (br < 255) (br + 15) else 255
                } else {
                    if (br > 0) (br - 15) else 0
                }.let { newBr ->
                    android.provider.Settings.System.putInt(contS, android.provider.Settings.System.SCREEN_BRIGHTNESS, newBr)
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun android.content.Context.rotation() {
        if (!fixedRotation && settingChecker) {
            makeRotation()
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun makeRotation() {
        isLandscape = recS.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
        val correctionDone = if (correction) {
            isLandscape = !isLandscape
            true
        } else {
            false
        }
        if (isLandscape) {
            android.provider.Settings.System.putInt(contS, android.provider.Settings.System.USER_ROTATION, android.view.Surface.ROTATION_0)
            check = false
            checkDone270 = false
            checkDone90 = false
        } else {
            android.provider.Settings.System.putInt(contS, android.provider.Settings.System.USER_ROTATION, android.view.Surface.ROTATION_90)
            check = true
        }
        toCatchSackAfter(864, 500) {
            val a = recS.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
            if (isLandscape == a && !correction) {
                correction = true
                makeRotation()
            }
        }.postBackAfter()
        if (correctionDone) correction = false
    }

    private var oV: android.database.ContentObserver? = object : android.database.ContentObserver(
        null
    ) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            launchDef {
                updateChanges()
            }
        }
    }

    override suspend fun updateChanges() {
        withMain {
            if (context == null) return@withMain
            with(headRestBall) {
                (audioManager?.getStreamVolume(android.media.AudioManager.STREAM_MUSIC) ?: 3).let {
                    volume = it
                    (it.toFloat() / maxVol.toFloat())
                }.let {
                    if (lastVol != it) {
                        lastVol = it
                        alarmFrameNavi.svgReColor(beforeAlpha(priAccent, (lastVol * 255)))
                        fileFrameNavi.svgReColor(beforeAlpha(priAccent, ((1 - lastVol) * 255)))
                    }
                }
                (android.provider.Settings.System.getInt(contS, android.provider.Settings.System.SCREEN_BRIGHTNESS).toFloat() /
                        255F).let {
                    if (lastBrt != it) {
                        screenShootFrameNavi.apply {
                            svgReColor(beforeAlpha(txtPri, (it * 255)))
                        }
                        screenRecordFrameNavi.apply {
                            svgReColor(beforeAlpha(txtPri, ((1 - it) * 255)))
                        }
                        lastBrt = it
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun beforeAlpha(@androidx.annotation.ColorInt c: Int, alpha: Float): Int = run {
        return@run when {
            alpha < 0.0F -> 0.0F
            alpha > 255F -> 255F
            else -> alpha
        }.let {
            return@let androidx.core.graphics.ColorUtils.setAlphaComponent(
                c,
                it.toInt()
            )
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.databinding.FloatingHeadWindowsBinding.changeVisibilityNavi() {
        if (!visible) {
            appsFrame.justVisible()
            constraintOne.justInvisible()
            constraintOne.visibleFade(200L)
        } else {
            constraintOne.invisibleFade(200L)
            toCatchSackAfter(862, 400L) {
                appsFrame.justGone()
                constraintOne.justGone()
            }.postAfter()
        }
        visible = !visible
    }


    private var touchListener: com.pt.common.moderator.touch.ContactListener? =
        object : com.pt.common.moderator.touch.ContactListener {

            @Volatile
            private var freeze = true
            private var xStart = 0.0F
            private var yStart = 0.0F

            private var touchD: Long = 0

            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            private var xDif = 0
            private var yDif = 0
            private var fakeMoveX = 0
            private var fakeMoveY = 0

            override fun onUp(v: android.view.View) {

            }

            override fun onUp(v: android.view.View, event: android.view.MotionEvent) {
                unPost(852)
                unPost(347)
                //if (freeze) {
                abs(xDif).let { tX ->
                    abs(yDif).let { tY ->
                        (tX < 30 && tY < 30).let { stop ->
                            if (stop) {
                                toCatchSackAfter(852, 200L) {
                                    headRestBall.changeVisibilityNavi()
                                }.postAfter()
                            }
                        }
                    }
                }
                //} else {
                val landScape = recS.configuration.run {
                    orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
                }
                musicDim { w, _ ->
                    if (fetchParams.x >= 0) {
                        animationOpenFloat(fetchParams.x, w / 2)
                        headRestBall.changeFramesForEnd(isRight = isRight, screen = screen)
                        if (!landScape)
                            xBefore = w / 2
                        else
                            landXBefore = w / 2
                    } else {
                        animationOpenFloat(fetchParams.x, -(w / 2))
                        headRestBall.changeFramesForStart(isRight = isRight, screen = screen)
                        if (!landScape)
                            xBefore = -(w / 2)
                        else
                            landXBefore = -(w / 2)
                    }
                    if (!landScape) yBefore = fetchParams.y else landYBefore = fetchParams.y
                    //beforeYouGo = fetchParams.y
                    //}
                    ballNavi.subBall.apply {
                        x = 0.0F
                        y = 0.0F
                    }
                    freeze = true
                }
            }

            override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
                ifHaveLongRun = true
                toCatchSackAfter(347, android.view.ViewConfiguration.getLongPressTimeout().toLong()) {
                    freeze = false
                }.postBackAfter()
                xDif = 0
                yDif = 0
                initialX = fetchParams.x
                initialY = fetchParams.y
                initialTouchX = event.rawX
                initialTouchY = event.rawY
                touchD = System.currentTimeMillis()
            }

            override fun onMove(v: android.view.View, event: android.view.MotionEvent) {
                if (ifHaveLongRun) {
                    if (abs(initialTouchX - event.rawX) > 5 || abs(initialTouchY - event.rawY) > 5) {
                        unPost(347)
                        ifHaveLongRun = false
                    }
                }
                fakeMoveX = initialX + (event.rawX - initialTouchX).toInt()
                fakeMoveY = initialY + (event.rawY - initialTouchY).toInt()
                musicDim { w, h ->
                    (w / 2 + initialX).let { itIX ->
                        (w / 2 + fakeMoveX).let { itPX ->
                            xDif = abs(itPX - itIX)
                        }
                    }
                    (h / 2 + initialY).let { itIY ->
                        (h / 2 + fakeMoveY).let { itPY ->
                            yDif = itPY - itIY
                        }
                    }
                    if (abs(xDif) > abs(yDif)) {
                        if (xDif == -abs(xDif)) {
                            xStart = -10.0F
                            yStart = 0.0F
                        } else {
                            xStart = 10.0F
                            yStart = 0.0F
                        }
                    } else {
                        if (yDif == -abs(yDif)) {
                            xStart = 0.0F
                            yStart = -10.0F
                        } else {
                            xStart = 0.0F
                            yStart = 10.0F
                        }
                    }
                    if (fetchParams.x < 0) {
                        (xDif.toFloat() / 100.0F) * 10.0F
                    } else {
                        (xDif.toFloat() / -100.0F) * 10.0F
                    }.let {
                        xStart = if (it == abs(it)) {
                            if (abs(it) < 10.0F) it else 10.0F
                        } else {
                            if (abs(it) < 10.0F) it else -10.0F
                        }
                        xStart = if (abs(it) < 10.0F) {
                            it
                        } else {
                            if (it == abs(it)) 10.0F else -10.0F
                        }
                        ballNavi.subBall.x = xStart
                    }
                    ((yDif.toFloat() / 100.0F) * 10.0F).let {
                        yStart = if (it == abs(it)) {
                            if (abs(it) < 10.0F) it else 10.0F
                        } else {
                            if (abs(it) < 10.0F) it else -10.0F
                        }
                        ballNavi.subBall.y = yStart
                    }
                    fetchParams.x = fakeMoveX
                    fetchParams.y = fakeMoveY
                    context.catchyBadToken {
                        windowManage.updateViewLayout(headRestBall.root, fetchParams)
                        windowManage.updateViewLayout(ballNavi.root, fetchParams)
                    }
                    if (fetchParams.x >= 0) {
                        headRestBall.changeFramesForEnd(isRight = isRight, screen = screen)
                    } else {
                        headRestBall.changeFramesForStart(isRight = isRight, screen = screen)
                    }
                }
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
                        layout?.root ?: return@catchyBadToken,
                        fetchParams
                    )
                    windowManage.updateViewLayout(
                        layout2?.root ?: return@catchyBadToken,
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
                        layout?.root ?: return@catchyBadToken,
                        fetchParams
                    )
                    windowManage.updateViewLayout(
                        layout2?.root ?: return@catchyBadToken,
                        fetchParams
                    )
                }
            }
            start()
        }
    }

    private fun animationUpDown(startX: Int, endX: Int, startY: Int, endY: Int) {
        returnAnimForChange(startX, endX, startY, endY) {
            addUpdateListener {
                fetchParams.x = getAnimatedValue(com.pt.common.BuildConfig.X_DIMENSION) as Int
                fetchParams.y = getAnimatedValue(com.pt.common.BuildConfig.Y_DIMENSION) as Int
                context?.catchyBadToken {
                    windowManage.updateViewLayout(
                        layout?.root ?: return@catchyBadToken,
                        fetchParams
                    )
                    windowManage.updateViewLayout(
                        layout2?.root ?: return@catchyBadToken,
                        fetchParams
                    )
                }
            }
            duration = 100
            start()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onChange(newConfig: Int) {
        doChange(newConfig)
    }


    private fun doChange(newConfig: Int) {
        fixedRotation = if (!fixedRotation) {
            (android.provider.Settings.System.getInt(contS, android.provider.Settings.System.USER_ROTATION) + 1).let {
                return@let (it != newConfig)
            }
        } else {
            false
        }
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
            (if (yBefore != null) yBefore else 100)?.let { yb ->
                yBefore = yb
                animationUpDown(fetchParams.x, xb, fetchParams.y, yb)
                if (xb == abs(xb))
                    headRestBall.changeFramesForEnd(screen = screen, isRight = isRight)
                else
                    headRestBall.changeFramesForStart(isRight = isRight, screen = screen)

                if (check) {
                    check = false
                    checkDone270 = false
                    checkDone90 = false
                }
            }
        }
    }


    private fun doChangeLand(wid: Int) {

        (if (landXBefore != null) landXBefore else -(wid / 2))?.let { lxb ->
            landXBefore = lxb
            (if (landYBefore != null) landYBefore else 100)?.let { lyb ->
                landYBefore = lyb
                animationUpDown(fetchParams.x, lxb, fetchParams.y, lyb)
                if (lxb == abs(lxb))
                    headRestBall.changeFramesForEnd(screen = screen, isRight = isRight)
                else
                    headRestBall.changeFramesForStart(isRight = isRight, screen = screen)
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun onSensorChanged(event: android.hardware.SensorEvent?) {
        val x = (event ?: return).values[0]
        if (event.sensor.type == android.hardware.Sensor.TYPE_ACCELEROMETER) {
            if (check) {
                if (x < -3) {
                    if (!checkDone270) {
                        android.provider.Settings.System.putInt(
                            contS,
                            android.provider.Settings.System.USER_ROTATION,
                            android.view.Surface.ROTATION_270
                        )
                        checkDone90 = false
                        checkDone270 = true
                    }
                } else if (x > 3) {
                    if (!checkDone90) {
                        android.provider.Settings.System.putInt(
                            contS,
                            android.provider.Settings.System.USER_ROTATION,
                            android.view.Surface.ROTATION_90
                        )
                        checkDone270 = false
                        checkDone90 = true
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun onAccuracyChanged(p0: android.hardware.Sensor?, p1: Int) {
    }

    @com.pt.common.global.UiAnn
    override fun onKeyBoardOpened() {
        beforeYouGo = fetchParams.y
        if (fetchParams.y > 50) {
            animationUp(fetchParams.y, -150)
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
        kotlin.runCatching {
            layout?.apply {
                alarmHead.onViewDestroy()
                screenShootHead.onViewDestroy()
                screenRecordHead.onViewDestroy()
                fileHead.onViewDestroy()
            }
            layout2?.apps?.onViewDestroy()
            context.catchyBadToken {
                windowManage.removeViewImmediate(headRestBall.root)
                windowManage.removeViewImmediate(ballNavi.root)
            }
            oV?.let {
                this?.contentResolver?.unregisterContentObserver(it)
            }
            touchListener = null
            sensorManager = null
            winManager = null
            layout = null
            layout2 = null
            myPara = null
            audioManagerNative = null
            beforeYouGo = 0
            visible = false
            volMinusListener = null
            volPlusListener = null
            brtMinusListener = null
            brtPlusListener = null
            oV = null
            stateDestroy()

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
