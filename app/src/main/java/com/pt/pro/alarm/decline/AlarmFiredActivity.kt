package com.pt.pro.alarm.decline

import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.setPadding
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.pt.common.global.*
import com.pt.common.media.loadAlarm
import com.pt.common.stable.*

class AlarmFiredActivity : com.pt.common.mutual.life.GlobalBaseActivity(),
    com.pt.pro.alarm.interfaces.AlarmDismiss, com.pt.common.mutual.base.JobHand {
    @Volatile
    private var powerManagerNative: android.os.PowerManager? = null
    private inline val android.content.Context.powerManager: android.os.PowerManager?
        get() = powerManagerNative ?: fetchSystemService(powerService).also {
            powerManagerNative = it
        }

    private val isScreenOn: Boolean?
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
                    toCatchSackAfter(594, 300L) {
                        if (ala?.dismissWay != com.pt.pro.alarm.objects.AlarmHelper.SHAKE_DEVICE &&
                            this@AlarmFiredActivity.baseContext != null
                        ) {
                            stopAlarm()
                            userFinish()
                        }
                        Unit
                    }.postAfter()
                }
                isOn = it
            }
            runPower.postBackAfter()
        }

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    @Volatile
    private var fasten: DSack<FrameLayout, AppCompatImageView, AppCompatImageView>? = null

    private var recordMediaPlayer: ExoPlayer? = null

    @Volatile
    private var ala: AlarmSack? = null

    private var originalBitmap: android.graphics.Bitmap? = null

    private var boolean: Boolean = false


    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        val f = inflate().also {
            @com.pt.common.global.ViewAnn
            fasten = it
        }.apply {
            setContentView(one)
        }
        window.alarmFired()
        qHand = fetchHandler
        lifecycle.addObserver(this@AlarmFiredActivity)
        launchImdMain {
            f.doIntiAlarm()
            intiAlarmSetting()
        }
    }

    private fun android.content.Context.inflate(): DSack<FrameLayout, AppCompatImageView, AppCompatImageView> {
        return FrameLayout(
            this@inflate
        ).run root@{
            android.view.WindowManager.LayoutParams(
                MATCH, MATCH
            ).also(this@root::setLayoutParams)
            setBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackground))
            val img = AppCompatImageView(
                context
            ).apply {
                framePara(MATCH, MATCH) {}
                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            }.also(this@root::addView)
            val playRecord = CardView(
                context
            ).run {
                this@run.framePara(findPixel(100)) {
                    gravity = android.view.Gravity.CENTER
                }
                this@run.setCardBackgroundColor(0)
                this@root.addView(this@run)
                AppCompatImageView(
                    context
                ).apply {
                    framePara(findPixel(100)) {
                        gravity = android.view.Gravity.CENTER
                    }
                    compactImage(com.pt.pro.R.drawable.circle) {
                        background = this
                    }
                    compactImage(com.pt.pro.R.drawable.ic_stop_record) {
                        setImageDrawable(this)
                    }
                    scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    backReColor(fetchColor(com.pt.pro.R.color.msc))
                    setPadding(15)
                    justInvisible()
                    isClickable = true
                    isFocusable = true
                }.also(this@run::addView)
            }
            FrameLayout(
                context
            ).apply {
                id = com.pt.pro.R.id.globalFrame
                framePara(MATCH, MATCH) {}
            }.also(this@root::addView)
            com.pt.common.global.DSack(this@root, playRecord, img)
        }
    }

    private suspend fun intiAlarmSetting() {
        justCoroutine {
            findIntegerPreference(REP_T, 1).let { itRep ->
                findIntegerPreference(
                    REMAINING_REPEAT_TIME,
                    1
                ).let { itDon ->
                    boolean = itRep != itDon
                    if (boolean) {
                        updatePrefInt(REMAINING_REPEAT_TIME, itDon + 1)
                    } else {
                        @Suppress("KotlinConstantConditions")
                        if (itRep != 1 && itDon != 1) {
                            updatePrefInt(REMAINING_REPEAT_TIME, 1)
                        }
                    }
                    findIntegerPreference(INTER_T, 10).let { itTrev ->
                        (itTrev * 60 * 1000L)
                    }.let { itDel ->
                        toCatchSackAfter(97, itDel) {
                            if (baseContext != null) {
                                toCatchSackAfter(397, 100L) {
                                    stopAlarm()
                                    if (baseContext != null) {
                                        if (boolean) {
                                            snoozeAlarm()
                                        } else {
                                            ala?.let {
                                                com.pt.pro.alarm.objects.AlarmHelper.apply {
                                                    missedNotification(
                                                        it.idAlarm,
                                                        it.labelAlarm,
                                                        itRep,
                                                        it.timeAlarm
                                                    )
                                                }
                                            }
                                        }
                                        unPostAll()
                                        unBackPostAll()
                                        finish()
                                    }
                                }.postAfter()
                            }
                        }.postBackAfter()
                    }
                }
            }
            runPower.postBackAfter()
            intiOnBackPressed {
                stopAlarm()
                userFinish()
            }
        }
    }

    @UiAnn
    private suspend fun DSack<FrameLayout, AppCompatImageView, AppCompatImageView>.doIntiAlarm() {
        intiAlarm { colImage ->
            withMain {
                if (colImage < 200) {
                    androidx.core.widget.ImageViewCompat.setImageTintList(
                        two,
                        android.graphics.Color.WHITE.toTintList
                    )
                }
                loadAlarmPic(true, ala?.imgBackground)
                when (ala?.dismissWay) {
                    com.pt.pro.alarm.objects.AlarmHelper.SINGLE_SWIPE -> {
                        newFragmentSingleSwipe {
                            this@newFragmentSingleSwipe.alarm = this@AlarmFiredActivity.ala
                            this@newFragmentSingleSwipe.colorImage = colImage
                            this@newFragmentSingleSwipe.alarmDismiss = this@AlarmFiredActivity
                            this@newFragmentSingleSwipe.test = false
                            this@newFragmentSingleSwipe
                        }
                    }
                    com.pt.pro.alarm.objects.AlarmHelper.SINGLE_CLICK -> {
                        newFragmentSingleClick {
                            this@newFragmentSingleClick.alarm = this@AlarmFiredActivity.ala
                            this@newFragmentSingleClick.colorImage = colImage
                            this@newFragmentSingleClick.alarmDismiss = this@AlarmFiredActivity
                            this@newFragmentSingleClick
                        }
                    }
                    com.pt.pro.alarm.objects.AlarmHelper.DOUBLE_SWIPE -> {
                        newFragmentDoubleSwipe {
                            this@newFragmentDoubleSwipe.alarm = this@AlarmFiredActivity.ala
                            this@newFragmentDoubleSwipe.colorImage = colImage
                            this@newFragmentDoubleSwipe.alarmDismiss = this@AlarmFiredActivity
                            this@newFragmentDoubleSwipe
                        }
                    }
                    com.pt.pro.alarm.objects.AlarmHelper.SHAKE_DEVICE -> {
                        newFragmentShake {
                            this@newFragmentShake.alarm = this@AlarmFiredActivity.ala
                            this@newFragmentShake.colorImage = colImage
                            this@newFragmentShake.alarmDismiss = this@AlarmFiredActivity
                            this@newFragmentShake
                        }
                    }
                    else -> {
                        newFragmentSingleSwipe {
                            this@newFragmentSingleSwipe.alarm = this@AlarmFiredActivity.ala
                            this@newFragmentSingleSwipe.colorImage = colImage
                            this@newFragmentSingleSwipe.alarmDismiss = this@AlarmFiredActivity
                            this@newFragmentSingleSwipe.test = false
                            this@newFragmentSingleSwipe
                        }
                    }
                }.also {
                    supportFragmentManager.fragmentLauncher(DIA) {
                        add(com.pt.pro.R.id.globalFrame, it, DIA)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    internal fun releaseMediaPlayer() {
        if (recordMediaPlayer != null) {
            (recordMediaPlayer ?: return).release()
            recordMediaPlayer = null
            userFinish()
        }
    }

    private suspend fun intiAlarm(a: suspend (Int) -> Unit) = justCoroutine {
        withBack {
            justCoroutine {
                intent.getIntExtra(TIME_FIRED, -1).let {
                    newDBAlarm {
                        getAlarmFire(it)
                    }
                }
            }.alsoSusBack {
                withBackDef(0) {
                    ala = it
                    if (it?.imgBackground == null) {
                        findWallpaper
                    } else {
                        runCatching {
                            android.graphics.drawable.BitmapDrawable(
                                resources,
                                android.graphics.BitmapFactory.decodeFile(it.imgBackground)
                            )
                        }.getOrNull()
                    }.run {
                        this?.toBitmap()?.colorDetection ?: 0
                    }
                }.alsoSusBack(a)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun dispatchKeyEvent(event: android.view.KeyEvent): Boolean {
        if (ala?.dismissWay == com.pt.pro.alarm.objects.AlarmHelper.SHAKE_DEVICE) return super.dispatchKeyEvent(event)
        return when (event.keyCode) {
            android.view.KeyEvent.KEYCODE_VOLUME_UP -> {
                if (recordMediaPlayer == null) {
                    snoozeAlarm()
                    stopAlarm()
                    unPost(97)
                    userFinish()
                    true
                } else {
                    false
                }
            }
            android.view.KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (recordMediaPlayer == null) {
                    snoozeAlarm()
                    stopAlarm()
                    unPost(97)
                    userFinish()
                    true
                } else {
                    false
                }
            }
            else -> super.dispatchKeyEvent(event)
        }
    }

    override fun doSnooze() {
        unPost(97)
        snoozeAlarm()
        stopAlarm()
        userFinish()
    }

    override fun doDismiss() {
        unPost(97)
        fasten?.loadAlarmPic(false, ala?.imgBackground)
        stopAlarm()
        ala?.recordAlarm.let {
            if (it != null && it != null.toString() && FileLate(it).isFile) {
                launchImdMain {
                    withMain {
                        fasten?.two?.apply {
                            justVisible()
                            isClickable = true
                            isFocusable = true
                            setOnClickListener {
                                releaseMediaPlayer()
                            }
                        }
                    }
                    playerRecord(it)
                }
            } else {
                toCatchSackAfter(99, 1500L) {
                    userFinish()
                }.postBackAfter()
            }
        }
    }

    private suspend fun playerRecord(record: String) {
        withMain {
            recordMediaPlayer = ExoPlayer.Builder(this@AlarmFiredActivity).applySus {
                setAudioAttributes(musicAudioAttr, true)
            }.build().also {
                it.playWhenReady = true
            }
        }
        withMain {
            with(recordMediaPlayer ?: return@withMain) {
                recordCall?.let { addListener(it) }
                setMediaItem(MediaItem.Builder().setUri(record.toUri).build())
                repeatMode = Player.REPEAT_MODE_OFF
                prepare()
            }
        }
        withMain {
            supportFragmentManager.popBackStack()
        }
    }

    private var recordCall: Player.Listener? = object : Player.Listener {

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == Player.STATE_ENDED) {
                launchDef {
                    withMain {
                        releaseMediaPlayer()
                    }
                }
            }
        }
    }


    private fun DSack<FrameLayout, AppCompatImageView, AppCompatImageView>.loadAlarmPic(isBlur: Boolean, img: String?) {
        if (!isBlur && originalBitmap != null) {
            launchDef {
                withMain {
                    three.applySus {
                        if (isV_S) {
                            setRenderEffect(null)
                        }
                        setImageBitmap(originalBitmap)
                    }
                }
            }
            return
        }
        if (img != null) {
            loadAlarm(img, ala?.idAlarm) {
                originalBitmap = this
                launchDef {
                    changeBrightness()?.alsoSusBack {
                        displayImage(it)
                    }
                }
            }
        } else {
            if (hasExternalReadWritePermission) {
                launchDef {
                    findWallpaper?.toBitmapSus()?.apply {
                        originalBitmap = this
                        changeBrightness()?.alsoSusBack {
                            displayImage(it)
                        }
                    }
                }
            }
        }
    }


    private suspend fun DSack<FrameLayout, AppCompatImageView, AppCompatImageView>.displayImage(b: android.graphics.Bitmap) {
        if (isV_S) {
            withMain {
                three.applySus {
                    setRenderEffect(
                        android.graphics.RenderEffect.createBlurEffect(
                            25F,
                            25F,
                            android.graphics.Shader.TileMode.CLAMP
                        )
                    )
                    setImageBitmap(b)
                }
            }
        } else {
            withBack {
                blur(b) {
                    withMain {
                        three.setImageBitmap(this@blur)
                    }
                }
            }
        }
    }


    private fun snoozeAlarm() {
        launchDef {
            withBackDef(null) {
                findIntegerPreference(SNO_T, 3).let {
                    System.currentTimeMillis() + (it * 60 * 1000L)
                }.let { st ->
                    com.pt.pro.alarm.objects.AlarmHelper.run {
                        ala.let { al ->
                            AlarmSack(
                                idAlarm = fetchValidId(),
                                timeAlarm = st,
                                labelAlarm = al?.labelAlarm,
                                ringAlarm = al?.ringAlarm,
                                recordAlarm = al?.recordAlarm,
                                dismissWay = al?.dismissWay ?: SINGLE_SWIPE,
                                isAlarm = false,
                                switchAlarm = false,
                                imgBackground = al?.imgBackground,
                                isVibrate = al?.isVibrate ?: false
                            )
                        }
                    }
                }
            }?.apply {
                justCoroutineBack {
                    com.pt.pro.alarm.release.AlarmReceiver.applySusBack {
                        setReminderAlarm(timeAlarm, idAlarm) {
                            newDBAlarm {
                                addAlarm()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun stopAlarm() {
        newIntent(com.pt.pro.alarm.release.AlarmServiceNotification::class.java) {
            action = ALARM_RECORD_FOR_DISMISS
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            this@newIntent
        }.let {
            android.app.PendingIntent.getService(baseContext, 37, it, PEND_FLAG).send()
        }
    }

    override fun userFinish() {
        unPostAll()
        unBackPostAll()
        finish()
    }

    override fun onDestroy() {
        stateDestroy()
        if (!isServiceNotRunning(
                com.pt.pro.alarm.release.AlarmServiceNotification::class.java.canonicalName
            )
        ) {
            stopAlarm()
        }
        recordCall?.let { recordMediaPlayer?.removeListener(it) }
        recordCall = null
        recordMediaPlayer = null
        fasten = null
        ala = null
        boolean = false
        originalBitmap = null
        super.onDestroy()
    }

}
