@file:Suppress("DEPRECATION")

package com.pt.common.global

import android.view.*
import android.view.View.*
import android.view.WindowManager.*
import android.view.WindowManager.LayoutParams.*
import com.pt.common.stable.catchy

@com.pt.common.global.MainAnn
fun Window.screenBrightness(newBright: Float) {
    attributes.apply {
        screenBrightness = newBright / 255F
    }.apply(::setAttributes)
}

inline val Boolean.flags: Int
    @com.pt.common.global.MainAnn
    get() {
        return if (isV_M && this) {
            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

@com.pt.common.global.MainAnn
fun Window.myBaseActivity(black: Boolean) {
    runCatching {
        decorView.systemUiVisibility = black.flags
        if (isV_R) {
            insetsController.apply {
                this?.setSystemBarsAppearance(
                    if (black) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }
        if (decorView.systemUiVisibility != black.flags)
            setFlags(FLAG_TRANSLUCENT_STATUS, FLAG_TRANSLUCENT_STATUS)
        else
            return
    }.onFailure {
        decorView.systemUiVisibility = if (isV_M && black) {
            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (isV_R) {
            insetsController.apply {
                this?.setSystemBarsAppearance(
                    if (black) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }
        setFlags(FLAG_TRANSLUCENT_STATUS, FLAG_TRANSLUCENT_STATUS)
    }
}

@com.pt.common.global.MainAnn
fun Window.sliderDestroy(
    @androidx.annotation.ColorInt nav: Int,
    @androidx.annotation.ColorInt txt: Int,
    @androidx.annotation.ColorInt pri: Int,
) {
    myBaseActivity(txt == android.graphics.Color.BLACK)
    if (isV_Q) {
        isNavigationBarContrastEnforced = true
    }
    if (isV_R) {
        clearWin(txt == android.graphics.Color.BLACK)
    }
    android.transition.Slide(Gravity.TOP).apply {
        duration = 100
        enterTransition = this
    }
    android.transition.Slide(Gravity.BOTTOM).apply {
        duration = 100
        exitTransition = this
    }
    clearFlags(FLAG_KEEP_SCREEN_ON)
    navigationBarColor = nav
    statusBarColor = pri
    attributes.apply {
        screenBrightness = BRIGHTNESS_OVERRIDE_NONE
    }.apply(::setAttributes)
}

@com.pt.common.global.MainAnn
fun Window.hideAllSystemUI() {
    if (isV_R) {
        catchy(Unit) {
            decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setDecorFitsSystemWindows(false)
        windowManager.currentWindowMetrics.apply {
            windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemGestures())
        }
        insetsController.apply {
            this?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            this?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_FULLSCREEN or
                SYSTEM_UI_FLAG_IMMERSIVE)

    }
}

@com.pt.common.global.MainAnn
fun Window.showSystemUI() {
    if (isV_R) {
        catchy(Unit) {
            decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        setDecorFitsSystemWindows(false)
        windowManager.currentWindowMetrics.apply {
            windowInsets.getInsets(WindowInsets.Type.systemGestures())
        }
        insetsController.run {
            this?.show(
                WindowInsets.Type.systemBars() or WindowInsets.Type.statusBars() or
                        WindowInsets.Type.navigationBars()
            )
            this?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}

@com.pt.common.global.APIAnn(android.os.Build.VERSION_CODES.R)
@com.pt.common.global.MainAnn
private fun Window.clearWin(black: Boolean) {
    setDecorFitsSystemWindows(true)
    windowManager.currentWindowMetrics.apply {
        windowInsets.getInsets(WindowInsets.Type.systemGestures())
    }
    insetsController.apply {
        this?.show(
            WindowInsets.Type.systemBars() or WindowInsets.Type.statusBars() or
                    WindowInsets.Type.navigationBars()
        )
        this?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )
        this?.setSystemBarsAppearance(
            if (black) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    }
}

@com.pt.common.global.MainAnn
fun Window.browser(@androidx.annotation.ColorInt nav: Int, ifTrue: Boolean) {
    if (ifTrue) {
        addFlags(FLAG_KEEP_SCREEN_ON)
    }

    statusBarColor = nav
    navigationBarColor = nav
    if (isV_R) {
        insetsController.apply {
            this?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }
    }
}

@com.pt.common.global.MainAnn
fun Window.browserTextViewer(
    @androidx.annotation.ColorInt nav: Int,
    ifTrue: Boolean,
    isBlack: Boolean,
) {
    if (ifTrue) {
        addFlags(FLAG_KEEP_SCREEN_ON)
    }
    statusBarColor = nav
    navigationBarColor = nav
    myBaseActivity(isBlack)
}

@com.pt.common.global.MainAnn
fun floatingWindowManger(): LayoutParams {
    return if (isV_O) {
        LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            TYPE_APPLICATION_OVERLAY,
            FLAG_HARDWARE_ACCELERATED
                    or FLAG_NOT_FOCUSABLE,
            android.graphics.PixelFormat.TRANSLUCENT
        )
    } else {
        (LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            TYPE_SYSTEM_ALERT,
            FLAG_HARDWARE_ACCELERATED
                    or FLAG_NOT_FOCUSABLE,
            android.graphics.PixelFormat.TRANSLUCENT
        ))
    }
}

@com.pt.common.global.MainAnn
inline fun floatingWindowMangerSearch(param: LayoutParams.() -> Unit) {
    if (isV_O) {
        LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            TYPE_APPLICATION_OVERLAY,
            SOFT_INPUT_ADJUST_NOTHING and
                    SOFT_INPUT_IS_FORWARD_NAVIGATION and
                    SOFT_INPUT_STATE_VISIBLE,
            android.graphics.PixelFormat.TRANSLUCENT
        ).also(param)
    } else {
        LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            TYPE_SYSTEM_ALERT,
            SOFT_INPUT_ADJUST_NOTHING and
                    SOFT_INPUT_IS_FORWARD_NAVIGATION and
                    SOFT_INPUT_STATE_VISIBLE,
            android.graphics.PixelFormat.TRANSLUCENT
        ).also(param)
    }
}

@com.pt.common.global.MainAnn
fun floatingVideoManger(): LayoutParams = run {
    return@run if (isV_O) {
        LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            TYPE_APPLICATION_OVERLAY,
            FLAG_HARDWARE_ACCELERATED
                    or FLAG_LAYOUT_NO_LIMITS
                    or FLAG_NOT_FOCUSABLE,
            android.graphics.PixelFormat.TRANSLUCENT
        )
    } else {
        @Suppress("DEPRECATION")
        LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            TYPE_SYSTEM_ALERT,
            FLAG_HARDWARE_ACCELERATED
                    or FLAG_LAYOUT_NO_LIMITS
                    or FLAG_NOT_FOCUSABLE,
            android.graphics.PixelFormat.TRANSLUCENT
        )
    }
}

@com.pt.common.global.MainAnn
fun floatingScreen(w: Int, h: Int): LayoutParams = run {
    return@run if (isV_O) {
        LayoutParams(
            w,
            h,
            TYPE_APPLICATION_OVERLAY,
            FLAGS_CHANGED or
                    FLAG_LOCAL_FOCUS_MODE or
                    FLAG_NOT_FOCUSABLE,
            android.graphics.PixelFormat.TRANSLUCENT
        )
    } else {
        LayoutParams(
            w,
            h,
            TYPE_SYSTEM_ALERT,
            FLAGS_CHANGED or
                    FLAG_LOCAL_FOCUS_MODE or
                    FLAG_NOT_FOCUSABLE,
            android.graphics.PixelFormat.TRANSLUCENT
        )
    }
}

@com.pt.common.global.MainAnn
fun Window.alarmFired() {
    if (isV_O_M) {
        setType(TYPE_KEYGUARD_DIALOG + TYPE_APPLICATION_OVERLAY)
    } else {
        addFlags(
            FLAGS_CHANGED + FLAG_SHOW_WHEN_LOCKED
                    + FLAG_TURN_SCREEN_ON
                    + FLAG_KEEP_SCREEN_ON
                    + FLAG_FULLSCREEN
        )
        setType(TYPE_KEYGUARD_DIALOG + TYPE_SYSTEM_ALERT)
    }
    if (!isV_O) {
        addFlags(FLAG_DISMISS_KEYGUARD)
    }
    if (isV_R) {
        setDecorFitsSystemWindows(false)
        windowManager.currentWindowMetrics.apply {
            windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemGestures())
        }
        insetsController.apply {
            this?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            this?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_FULLSCREEN or
                SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                SYSTEM_UI_FLAG_IMMERSIVE)
    }
}

@com.pt.common.global.MainAnn
fun Window.musicScreen() {
    if (isV_O_M) {
        setType(TYPE_KEYGUARD_DIALOG + TYPE_APPLICATION_OVERLAY)
    } else {
        addFlags(FLAG_SHOW_WHEN_LOCKED)
        setType(TYPE_KEYGUARD_DIALOG + TYPE_SYSTEM_ALERT)
    }
    if (isV_R) {
        setDecorFitsSystemWindows(false)
        windowManager.currentWindowMetrics.apply {
            windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemGestures())
        }
        insetsController.apply {
            this?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            this?.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LAYOUT_STABLE or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_FULLSCREEN or
                SYSTEM_UI_FLAG_IMMERSIVE)
    }
}
