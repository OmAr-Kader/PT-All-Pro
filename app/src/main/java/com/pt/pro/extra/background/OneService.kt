package com.pt.pro.extra.background

import androidx.lifecycle.lifecycleScope
import com.pt.common.global.*
import com.pt.common.mutual.base.OnConfigurationChanged
import com.pt.common.stable.*
import com.pt.pro.R

class OneService : androidx.lifecycle.LifecycleService() {

    @androidx.annotation.ColorInt
    private var txtColor = android.graphics.Color.WHITE
    private var result = -1
    private var data: android.content.Intent? = null

    private var viewContextNative: android.content.Context? = null

    private inline val viewContext: android.content.Context
        get() {
            return viewContextNative ?: baseContext.fetchOverlayContext.also { viewContextNative = it }
        }

    internal companion object {
        @JvmStatic
        @Volatile
        var floatingManagerListener: OnConfigurationChanged? = null

        @JvmStatic
        @Volatile
        var naviManagerListener: OnConfigurationChanged? = null

        @JvmStatic
        @Volatile
        var swipeManagerListener: OnConfigurationChanged? = null

        @JvmStatic
        @Volatile
        var noteManagerListener: OnConfigurationChanged? = null
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
        androidx.core.app.NotificationCompat.Builder(this, CH_APP).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(resources.getString(R.string.icon_name))
            setContentText(resources.getString(R.string.hp))
            newIntent(OneService::class.java) {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                action = DISMISS_ONE_SERVICE
                putExtra(ONE_SERVICE_TYPE, SERVICE_ALL)
                this@newIntent
            }.run {
                android.app.PendingIntent.getService(
                    this@OneService, SERVICE_ALL, this, PEND_FLAG
                )
            }.let {
                return@let androidx.core.app.NotificationCompat.Action.Builder(
                    R.drawable.ic_close, resources.getString(R.string.co), it
                )
            }.let { act ->
                addAction(act.build())
            }
            setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
            setAutoCancel(false)
            setOnlyAlertOnce(true)
            setOngoing(true)
            setCategory(androidx.core.app.NotificationCompat.CATEGORY_SERVICE)
            priority = androidx.core.app.NotificationCompat.PRIORITY_HIGH or
                    androidx.core.app.NotificationCompat.PRIORITY_MAX
            foregroundServiceBehavior =
                androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
        }.build().apply {
            flags = androidx.core.app.NotificationCompat.FLAG_FOREGROUND_SERVICE or
                    androidx.core.app.NotificationCompat.FLAG_ONGOING_EVENT
            if (isV_Q) {
                startForeground(
                    ONE_SERVICE,
                    this,
                    android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION
                )
            } else {
                startForeground(ONE_SERVICE, this)
            }
        }
        @androidx.annotation.ColorInt
        txtColor = if (resources.isDeviceDark)
            android.graphics.Color.WHITE
        else
            android.graphics.Color.BLACK
    }


    @android.annotation.SuppressLint("MissingPermission")
    private suspend fun android.content.Context.updateNot() {
        justCoroutine {
            androidx.core.app.NotificationCompat.Builder(this@updateNot, CH_APP).apply {
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setContentTitle(resources.getString(R.string.icon_name))
                setContentText(resources.getString(R.string.hp))
                android.widget.RemoteViews(
                    com.pt.pro.BuildConfig.APPLICATION_ID, R.layout.notification_icon
                ).apply {
                    updateType()
                }.apply {
                    also(::setCustomContentView)
                    also(::setCustomBigContentView)
                    also(::setCustomHeadsUpContentView)
                }
                setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                setAutoCancel(false)
                setOnlyAlertOnce(true)
                setOngoing(true)
                setCategory(androidx.core.app.NotificationCompat.CATEGORY_SERVICE)
                priority = androidx.core.app.NotificationCompat.PRIORITY_HIGH or
                        androidx.core.app.NotificationCompat.PRIORITY_MAX
                foregroundServiceBehavior =
                    androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
            }.build().also {
                it.flags = androidx.core.app.NotificationCompat.FLAG_FOREGROUND_SERVICE
                androidx.core.app.NotificationManagerCompat.from(this@updateNot).apply {
                    notify(ONE_SERVICE, it)
                }
            }
        }
    }

    private suspend fun android.widget.RemoteViews.updateType() {
        setOnClickPendingIntent(R.id.closeNav, getAction(SERVICE_NAVI))
        if (naviManagerListener != null) {
            setTextColor(R.id.titleNav, txtColor)
            setImageViewResource(R.id.closeNav, R.drawable.ic_close)
        } else {
            setTextColor(R.id.titleNav, android.graphics.Color.GRAY)
            setImageViewResource(R.id.closeNav, R.drawable.ic_floating)
        }
        setOnClickPendingIntent(R.id.closeNote, getAction(SERVICE_NOTE))
        if (noteManagerListener != null) {
            setTextColor(R.id.titleNote, txtColor)
            setImageViewResource(R.id.closeNote, R.drawable.ic_close)
        } else {
            setTextColor(R.id.titleNote, android.graphics.Color.GRAY)
            setImageViewResource(R.id.closeNote, R.drawable.ic_floating)
        }

        setOnClickPendingIntent(R.id.closeFloat, getAction(SERVICE_FLOAT))
        setTextColor(R.id.closeAll, txtColor)
        setOnClickPendingIntent(R.id.closeAll, getAction(SERVICE_ALL))
        if (data == null) {
            setViewVisibility(R.id.screenFrame, android.view.View.GONE)
            return
        }
        if (floatingManagerListener != null) {
            setTextColor(R.id.titleFloat, txtColor)
            setImageViewResource(R.id.closeFloat, R.drawable.ic_close)
        } else {
            setTextColor(R.id.titleFloat, android.graphics.Color.GRAY)
            setImageViewResource(R.id.closeFloat, R.drawable.ic_floating)
        }
        setOnClickPendingIntent(R.id.closeCircle, getAction(SERVICE_SWIPER))
        if (swipeManagerListener != null) {
            findBooleanPreference(SCR_SHOT, true).let {
                if (it) resources.getString(R.string.ss) else resources.getString(R.string.sr)
            }.let {
                setTextViewText(R.id.titleCircle, it)
            }
            setTextColor(R.id.titleCircle, txtColor)
            setImageViewResource(R.id.closeCircle, R.drawable.ic_close)
        } else {
            setTextColor(R.id.titleCircle, android.graphics.Color.GRAY)
            setImageViewResource(R.id.closeCircle, R.drawable.ic_floating)
        }
    }

    private inline val android.content.Context.getAction: (Int) -> android.app.PendingIntent
        get() = {
            newIntent(OneService::class.java) {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra(ONE_SERVICE_TYPE, it)
                this@newIntent
            }.run {
                android.app.PendingIntent.getService(this@getAction, it, this, PEND_FLAG)
            }
        }

    override fun onStartCommand(intent: android.content.Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        (intent ?: return START_REDELIVER_INTENT).getIntExtra(
            ONE_SERVICE_TYPE, SERVICE_ALL
        ).let { type ->
            data = if (isV_T) {
                intent.getParcelableExtra(FLOATING_EXTRA_DATA, android.content.Intent::class.java) ?: data
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(FLOATING_EXTRA_DATA) ?: data
            }
            if (type == SERVICE_ALL) {
                forDestroy {
                    stopServiceNow()
                }
            } else {
                lifecycleScope.launchImdMain {
                    when (type) {
                        SERVICE_NAVI -> naviManagerListener.createNaviManager()
                        SERVICE_FLOAT -> floatingManagerListener.createFloatingManager(intent)
                        SERVICE_SWIPER -> swipeManagerListener.createSwiperManager(intent)
                        SERVICE_NOTE -> noteManagerListener.createNoteManager()
                    }
                }
            }
            return@let
        }
        return START_STICKY
    }

    private fun forDestroy(a: () -> Unit) {
        naviManagerListener?.apply {
            onServiceDestroy {
                naviManagerListener = null
            }
        }
        noteManagerListener?.apply {
            onServiceDestroy {
                noteManagerListener = null
            }
        }
        floatingManagerListener?.apply {
            onServiceDestroy {
                floatingManagerListener = null
            }
        }
        swipeManagerListener?.apply {
            onServiceDestroy {
                swipeManagerListener = null
            }
        }
        a.invoke()
    }

    @com.pt.common.global.MainAnn
    private suspend fun OnConfigurationChanged?.createFloatingManager(intent: android.content.Intent) {
        if (this@createFloatingManager == null) {
            if (isV_T) {
                intent.getParcelableExtra(FLOATING_EXTRA_DATA, android.content.Intent::class.java) ?: data
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(FLOATING_EXTRA_DATA) ?: data
            }.let { dat ->
                intent.getIntExtra(FLOATING_RESULT_CODE, result).let { resultCode ->
                    data = dat
                    result = resultCode
                    viewContext.newFloatManager(dat, resultCode) {
                        updateViewRoot()
                        updateNot()
                    }
                }
            }
        } else {
            onServiceDestroy {
                floatingManagerListener = null
            }
            updateNot()
        }
    }

    private suspend fun OnConfigurationChanged?.createSwiperManager(intent: android.content.Intent) {
        if (this@createSwiperManager == null) {
            viewContext.apply {
                if (isV_T) {
                    intent.getParcelableExtra(FLOATING_EXTRA_DATA, android.content.Intent::class.java) ?: data
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(FLOATING_EXTRA_DATA) ?: data
                }.let { dat ->
                    intent.getIntExtra(FLOATING_RESULT_CODE, result).let { resultCode ->
                        data = dat
                        result = resultCode
                        this@apply.newSwiper(dat, resultCode) {
                            updateViewRoot()
                            updateNot()
                        }
                    }
                }
            }
        } else {
            onServiceDestroy {
                swipeManagerListener = null
            }
            updateNot()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun OnConfigurationChanged?.createNoteManager() {
        if (this@createNoteManager == null) {
            viewContext.applySus {
                this@applySus.newNoteManager {
                    updateViewRoot()
                    updateNot()
                }
            }
        } else {
            onServiceDestroy {
                noteManagerListener = null
            }
            updateNot()
        }
    }


    @com.pt.common.global.MainAnn
    private suspend fun OnConfigurationChanged?.createNaviManager() {
        if (this@createNaviManager == null) {
            viewContext.applySus {
                this@applySus.newNaviManager {
                    updateViewRoot()
                    updateNot()
                }
            }
        } else {
            onServiceDestroy {
                naviManagerListener = null
            }
            updateNot()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun android.content.Context.newNaviManager(navi: suspend NaviHeadManager.() -> Unit) {
        NaviHeadManager(this@newNaviManager).apply {
            (fetchSystemService(inflaterService) ?: return).also { inf ->
                com.pt.pro.databinding.FloatingHeadWindowsBinding.inflate(inf).also {
                    it.setViewRoot()
                }
                com.pt.pro.databinding.FloatingHeadAppsBinding.inflate(inf).also {
                    it.setLayout2()
                }
            }
            setInit()
            fetchSystemService(windowService).also {
                it?.setWindowManager()
                catchyBadToken {
                    it?.addView(getViewRoot, fetchParams)
                    it?.addView(getViewRoot2, fetchParams)
                }
            }
            naviManagerListener = this@apply
        }.alsoSus(navi)
    }

    @com.pt.common.global.MainAnn
    private suspend fun android.content.Context.newNoteManager(navi: suspend NoteHeadManager.() -> Unit) {
        NoteHeadManager(this@newNoteManager).apply {
            (fetchSystemService(inflaterService) ?: return).also { inf ->
                com.pt.pro.databinding.FloatingHeadBinding.inflate(inf).also {
                    it.setViewRoot()
                }
                com.pt.pro.databinding.NoteHeadBinding.inflate(inf).also {
                    it.setLayout2()
                }
            }
            setInit()
            fetchSystemService(windowService).also {
                it?.setWindowManager()
                catchyBadToken {
                    it?.addView(getViewRoot, fetchParams)
                    it?.addView(getViewRoot2, fetchParams)
                }
            }
            noteManagerListener = this@apply
        }.alsoSus(navi)
    }

    @com.pt.common.global.MainAnn
    private suspend fun android.content.Context.newFloatManager(
        dat: android.content.Intent?,
        resultCode: Int,
        navi: suspend FloatingHeadManager.() -> Unit,
    ) {
        FloatingHeadManager(this@newFloatManager).apply {
            (fetchSystemService(inflaterService) ?: return).also { inf ->
                com.pt.pro.databinding.FloatingHeadWindowsBinding.inflate(inf).also {
                    it.setViewRoot()
                }
                com.pt.pro.databinding.FloatingHeadBinding.inflate(inf).also {
                    it.setViewRoot2()
                }
            }
            findBooleanPreference(IS_SCREEN, true).let {
                if (it) dat != null else false
            }.let {
                setSerBoolean(it)
            }
            setResultCode(resultCode)
            dat?.setDataIntent()
            setInit()
            fetchSystemService(windowService).also {
                it?.setWindowManager()
                catchyBadToken {
                    it?.addView(getViewRoot, fetchParams)
                    it?.addView(getViewRoot2, fetchParams)
                }
            }
            floatingManagerListener = this@apply
        }.alsoSus(navi)
    }

    private suspend fun android.content.Context.newSwiper(
        dat: android.content.Intent?,
        resultCode: Int,
        swipe: suspend FloatingSwiperManager.() -> Unit,
    ) {
        if (dat == null) {
            newIntent(com.pt.pro.extra.screener.MainScreen::class.java) {
                flags = BACKGROUND_FLAGS
                return@newIntent this@newIntent
            }.let(::startActivity)
            makeToastRec(R.string.xe, 1)
            return
        }
        FloatingSwiperManager(ctx = this@newSwiper).apply {
            swipeManagerListener = this@apply
            setInit()
            dat.setDataIntent(resultCode)
            fetchSystemService(windowService).also {
                it?.setWindowManager()
            }
        }.alsoSus(swipe)
    }

    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        runCatching {
            naviManagerListener?.apply {
                onChange(newConfig.orientation)
            }
            noteManagerListener?.apply {
                onChange(newConfig.orientation)
            }
            floatingManagerListener?.apply {
                onChange(newConfig.orientation)
            }
            swipeManagerListener?.apply {
                onChange(newConfig.orientation)
            }
        }

    }

    @com.pt.common.global.MainAnn
    override fun onDestroy() {
        naviManagerListener?.apply {
            onServiceDestroy {
                naviManagerListener = null
            }
        }
        noteManagerListener?.apply {
            onServiceDestroy {
                noteManagerListener = null
            }
        }
        floatingManagerListener?.apply {
            onServiceDestroy {
                floatingManagerListener = null
            }
        }
        swipeManagerListener?.apply {
            onServiceDestroy {
                swipeManagerListener = null
            }
        }
        super.onDestroy()
        viewContextNative = null
    }

    /*override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(this)
            return
        }
        newBase.applicationContext?.resources.run {
            this?.configuration?.checkEnglishLang ?: true
        }.let { itE ->
            if (newBase.findBooleanPref(
                    ID_ENGLISH,
                    KEY_ENG,
                    LANG_DEF
                ) && !itE
            ) {
                newBase.runCatching {
                    wrap {
                        super.attachBaseContext(this)
                    }
                }.onFailure {
                    super.attachBaseContext(newBase)
                }
            } else {
                super.attachBaseContext(newBase)
            }
        }
    }*/

    private fun android.content.Context.createChannel() {
        androidx.core.app.NotificationChannelCompat.Builder(
            CH_APP, androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
        ).apply {
            setName(resources.getString(R.string.hp))
            setDescription(resources.getString(R.string.pc))
            setShowBadge(false)
            setSound(null, null)
        }.let {
            androidx.core.app.NotificationManagerCompat.from(this@createChannel).apply {
                createNotificationChannel(it.build())
            }
        }
    }
}