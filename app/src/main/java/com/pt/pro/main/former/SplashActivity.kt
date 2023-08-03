package com.pt.pro.main.former

import com.pt.common.global.*
import com.pt.common.stable.*

@android.annotation.SuppressLint("CustomSplashScreen")
class SplashActivity : com.pt.common.mutual.life.GlobalBaseActivity() {

    @com.pt.common.global.MainAnn
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        if (isV_S) {
            androidx.core.splashscreen.SplashScreen.apply {
                installSplashScreen().apply {
                    setKeepOnScreenCondition {
                        true
                    }
                }
            }
        }
        super.onCreate(savedInstanceState)
        catchy(false) {
            findBooleanPref(F_THEME, TH_FIRST, true)
        }.let { fistCreate ->
            (if (fistCreate) resources.isDeviceDark else nightRider).let { night ->
                inflate(night).apply {
                    one.apply(::setContentView)
                    resources.displayMetrics.apply {
                        two.displaySplashActivity(findPixelDis(60F), findPixelDis(250F))
                    }
                    intiViews(fistCreate = fistCreate, night = night)
                }
            }
        }
    }

    private fun android.content.Context.inflate(night: Boolean): DSackT<android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView> {
        return android.widget.FrameLayout(
            this@inflate
        ).run root@{
            android.view.WindowManager.LayoutParams(
                MATCH, MATCH
            ).also(this@root::setLayoutParams)
            setBackgroundColor(fetchColor(if (night) com.pt.pro.R.color.bla else com.pt.pro.R.color.whi))
            val img = androidx.appcompat.widget.AppCompatImageView(
                context
            ).apply {
                findPixel(60).also {
                    framePara(it, it) {
                        gravity = android.view.Gravity.CENTER
                    }
                }
                compactImage(com.pt.pro.R.drawable.ic_logo_splash) {
                    setImageDrawable(this@compactImage)
                }
            }.also(this@root::addView)
            DSackT(this@root, img)
        }
    }

    private fun intiViews(
        fistCreate: Boolean, night: Boolean
    ) {
        intiFirst(fistCreate, night)
        doSplashActivity(fistCreate)
    }

    private inline val Boolean.fetchDefStyleNumber: Int
        @com.pt.common.global.MainAnn get() {
            if (this) {
                com.pt.pro.R.style.AIO_Night_And6
            } else {
                com.pt.pro.R.style.AIO_Light_And6
            }.also<@androidx.annotation.StyleRes Int> {
                return com.pt.pro.extra.utils.SettingHelper.fromStyleToNumber(it)
            }
        }

    private fun intiFirst(
        fistCreate: Boolean,
        NR: Boolean,
    ) {
        if (fistCreate) {
            launchDef {
                updateBooleanPrefSus(NIGHT, RIDERS, NR)
                updateIntPrefSus(NEW_STYLE, NEW_MY_THEM, NR.fetchDefStyleNumber)
            }
        } else return
    }

    @com.pt.common.global.UiAnn
    private fun androidx.appcompat.widget.AppCompatImageView?.displaySplashActivity(
        from: Int, to: Int
    ) {
        catchy(Unit) {
            androidx.core.animation.ValueAnimator.ofInt(
                from, to
            ).apply {
                addUpdateListener {
                    (animatedValue as Int).let { d ->
                        android.widget.FrameLayout.LayoutParams(d, d).apply {
                            gravity = android.view.Gravity.CENTER
                            this@displaySplashActivity?.layoutParams = this
                        }
                    }
                }
                duration = SPLASH_CONST
                start()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun doSplashActivity(fistCreate: Boolean) {
        java.util.concurrent.Executors.newSingleThreadScheduledExecutor().schedule({
            launchActivity(fistCreate)
        }, SPLASH_CONST, java.util.concurrent.TimeUnit.MILLISECONDS)
    }

    private fun launchActivity(fistCreate: Boolean) {
        launchDef {
            baseContext.nullChecker()
            justCoroutineBack {
                if (fistCreate) {
                    catchy(Unit) {
                        updateBooleanPrefSus(F_THEME, TH_FIRST, false)
                    }
                } else return@justCoroutineBack
            }
            baseContext.nullChecker()
            justCoroutine {
                this@SplashActivity.newIntentSus(MainActivity::class.java) {
                    addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    this@newIntentSus
                }
            }.alsoSusBack {
                baseContext.nullChecker()
                withBack {
                    it.applySusBack(this@SplashActivity::startActivity)
                    this@SplashActivity.overridePendingTransition(0, 0)
                    this@SplashActivity.finish()
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        findBooleanPref(NIGHT, RIDERS, resources.isDeviceDark).let {
            window?.apply {
                if (!it) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        insetsController?.setSystemBarsAppearance(
                            android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                            android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        )
                    } else {
                        @Suppress("DEPRECATION") if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            decorView.apply {
                                systemUiVisibility =
                                    android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            }
                        }
                    }
                }
                if (it) {
                    fetchColor(com.pt.pro.R.color.bla)
                } else {
                    fetchColor(com.pt.pro.R.color.whi)
                }.let { color ->
                    statusBarColor = color
                    navigationBarColor = color
                }
            }
        }
        super.onAttachedToWindow()
    }
}
