package com.pt.pro.extra.screener

import com.pt.common.global.*
import com.pt.common.BuildConfig.*
import com.pt.common.media.createNewFolder
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.extra.background.OneService
import com.pt.pro.extra.interfaces.ScreenListener
import com.pt.pro.extra.utils.SettingHelper

abstract class BaseScreen : GlobalSimpleFragment<com.pt.pro.extra.fasten.ScreenCaptureFasten>(), ScreenListener,
    android.view.View.OnLongClickListener, android.widget.SeekBar.OnSeekBarChangeListener {

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    protected var lockScreen: Boolean = true
    protected var isWait: Boolean = false
    protected var gateOpen: Boolean = false
    protected var optionHead: Int = SettingHelper.FLOATING_HEAD

    protected var mOriginProgress: Int = 50

    @Volatile
    protected var screen: Boolean = false

    protected var firstProgress: Int = 50

    protected var swiperListener: com.pt.pro.extra.interfaces.SwiperListener? = null

    protected var swiperMyHead: com.pt.pro.extra.interfaces.SwiperListener? = null

    protected var resultScreen: Int = -1
    protected var dataScreen: android.content.Intent? = null

    @com.pt.common.global.WorkerAnn
    internal inline fun projectionManager(intent: android.content.Intent.() -> Unit) {
        ctx.fetchSystemService(mediaProjectionService)?.apply {
            android.content.Intent(this.createScreenCaptureIntent()).apply(intent)
        }
    }


    internal val checkRunnable: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            catchy(Unit) {
                optionHead.isNullService.let {
                    binder?.seekLaunch?.apply {
                        if (it && progress == 950) {
                            toCatchSackAfter(99, 100L) {
                                if (isV_N) {
                                    setProgress(50, true)
                                } else {
                                    progress = 50
                                }
                                mOriginProgress = 50
                                afterCheck(50)
                                turnCurrent(true)
                            }.postAfter()
                        } else if (!it && progress == 50) {
                            toCatchSackAfter(88, 100L) {
                                if (isV_N) {
                                    setProgress(950, true)
                                } else {
                                    progress = 950
                                }
                                mOriginProgress = 950
                                afterCheck(950)
                                turnCurrent(false)
                            }.postAfter()
                        }
                    }
                }
                checkRunnable.rKTSack(800L).postAfter()
            }
        }

    internal inline val Int.isNullService: Boolean
        get() {
            return when (this) {
                SettingHelper.SCREEN_SWIPER -> {
                    OneService.swipeManagerListener == null
                }
                SettingHelper.FLOATING_HEAD -> {
                    OneService.floatingManagerListener == null
                }
                SettingHelper.NAVIGATION_HEAD -> {
                    OneService.naviManagerListener == null
                }
                else -> {
                    OneService.noteManagerListener == null
                }
            }
        }


    @com.pt.common.global.UiAnn
    protected fun textSwiper(p: Int) {
        binding.slideText.apply {
            if (p >= 300 && isVis) {
                justGone()
            } else if (p < 300 && isGon) {
                justVisible()
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    protected suspend fun startNaviService() {
        withDefault {
            ctx.newIntentSus(OneService::class.java) {
                flags = BACKGROUND_FLAGS
                putExtra(ONE_SERVICE_TYPE, SERVICE_NAVI)
                this@newIntentSus
            }.alsoSusBack {
                if (ctx.isServiceNotRunning(OneService::class.java.canonicalName)) {
                    androidx.core.content.ContextCompat.startForegroundService(ctx, it)
                } else {
                    android.app.PendingIntent.getService(ctx, 0, it, PEND_FLAG).send()
                }
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    protected suspend fun startNoteService() {
        withDefault {
            ctx.newIntentSus(OneService::class.java) {
                flags = BACKGROUND_FLAGS
                putExtra(ONE_SERVICE_TYPE, SERVICE_NOTE)
                this@newIntentSus
            }.alsoSusBack {
                if (ctx.isServiceNotRunning(OneService::class.java.canonicalName)) {
                    androidx.core.content.ContextCompat.startForegroundService(ctx, it)
                } else {
                    android.app.PendingIntent.getService(ctx, 0, it, PEND_FLAG).send()
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun startRecordingService(resultCode: Int, data_: android.content.Intent?) {
        withDefault {
            ctx.newIntentSus(OneService::class.java) {
                flags = BACKGROUND_FLAGS
                putExtra(ONE_SERVICE_TYPE, SERVICE_SWIPER)
                putExtra(FLOATING_EXTRA_DATA, data_)
                putExtra(FLOATING_RESULT_CODE, resultCode)
                this@newIntentSus
            }.alsoSusBack {
                if (ctx.isServiceNotRunning(OneService::class.java.canonicalName)) {
                    context.nullChecker()
                    swiperListener?.doLaunch()
                    androidx.core.content.ContextCompat.startForegroundService(ctx, it)
                } else {
                    android.app.PendingIntent.getService(ctx, 0, it, PEND_FLAG).send()
                    if (OneService.swipeManagerListener == null) {
                        swiperListener?.doLaunch()
                    } else {
                        swiperListener?.doStop()
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    protected suspend fun startFloatingService(resultCode: Int, data: android.content.Intent?) {
        withDefault {
            ctx.newIntentSus(OneService::class.java) {
                flags = BACKGROUND_FLAGS
                putExtra(ONE_SERVICE_TYPE, SERVICE_FLOAT)
                putExtra(FLOATING_RESULT_CODE, resultCode)
                putExtra(FLOATING_EXTRA_DATA, data)
                this@newIntentSus
            }.alsoSusBack {
                if (ctx.isServiceNotRunning(OneService::class.java.canonicalName)) {
                    swiperMyHead?.doLaunch()
                    androidx.core.content.ContextCompat.startForegroundService(ctx, it)
                } else {
                    if (OneService.floatingManagerListener == null) {
                        swiperMyHead?.doLaunch()
                    } else {
                        swiperMyHead?.doStop()
                    }
                    android.app.PendingIntent.getService(ctx, 0, it, PEND_FLAG).send()
                }
            }
        }
    }

    protected suspend fun fileScreenCreator() {
        withBack {
            ctx.findStringPrefNullable(SCR_FOL_KEY).let { sF ->
                ("$ROOT/$FILE_SCREEN").let<String, Unit> { str ->
                    if (sF != null && !com.pt.common.global.FileLate(sF.toString()).isDirectory) {
                        cont.createNewFolder(
                            FILE_SCREEN.fetchMyRootFile.absolutePath,
                            FILE_SCREEN
                        )
                    } else if (sF == null && com.pt.common.global.FileLate(str).exists()) {
                        ctx.updatePrefString(
                            SCR_FOL_KEY,
                            str
                        )
                    } else if (sF == null && !com.pt.common.global.FileLate(str).exists()) {
                        cont.createNewFolder(
                            FILE_SCREEN.fetchMyRootFile.absolutePath,
                            FILE_SCREEN
                        )
                    }
                }
            }
            optionHead = ctx.findIntegerPreference(SCR_OPT, SettingHelper.FLOATING_HEAD)
        }
    }

    private fun afterCheck(p: Int) {
        gateOpen = true
        toCatchSackAfter(324, 300L) {
            gateOpen = false
        }.postAfter()
        textSwiper(p)
    }

    protected fun turnCurrent(enable: Boolean) {
        launchDef {
            withMain {
                when (optionHead) {
                    SettingHelper.SCREEN_SWIPER -> {
                        if (enable) swiperListener?.doStop() else swiperListener?.doLaunch()
                    }
                    SettingHelper.FLOATING_HEAD -> {
                        if (enable) swiperMyHead?.doStop() else swiperMyHead?.doLaunch()
                    }
                }
            }
        }
    }

    protected suspend fun stopSeekForService() {
        withMain {
            checkRunnable.rKTSack(500L).postAfter()
        }
    }

    protected fun stopRecordingService(optionHead: Int) {
        launchDef {
            stopSeekForService()
            when (optionHead) {
                SettingHelper.SCREEN_SWIPER -> startRecordingService(resultScreen, dataScreen)
                SettingHelper.FLOATING_HEAD -> startFloatingService(resultScreen, dataScreen)
                SettingHelper.NAVIGATION_HEAD -> startNaviService()
                SettingHelper.NOTE_HEAD -> startNoteService()
            }
        }
    }

    override fun onDestroyView() {
        swiperListener = null
        swiperMyHead = null
        super.onDestroyView()
    }

}