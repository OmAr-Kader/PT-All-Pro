package com.pt.common.global

import com.pt.common.stable.alsoSus
import com.pt.common.stable.applySus
import com.pt.common.stable.letSus
import com.pt.common.stable.useMy

inline val android.util.DisplayMetrics.findPixelDis: (Float) -> Int
    get() = {
        (it * density + 0.5f).toInt()
    }

inline val android.content.Context.findPixel: (Int) -> Int
    get() = {
        (it.toFloat() * resources.displayMetrics.density + 0.5f).toInt()
    }

inline val android.content.res.Resources.Theme.findAttr: (Int) -> Int
    get() = {
        android.util.TypedValue().apply {
            resolveAttribute(it, this, true)
        }.data
    }

inline val android.content.res.Configuration.isTablet: Boolean
    get() {
        val xL = screenLayout and android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK == 4
        val l = screenLayout and android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK ==
                android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
        return xL || l
    }

inline val android.content.res.Resources.linDirection: Int
    @com.pt.common.global.MainAnn
    get() {
        return if (isLandTraditional)
            androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
        else
            androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
    }
inline val android.content.res.Configuration.linConDirection: Int
    @com.pt.common.global.MainAnn
    get() {
        return if (orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE)
            androidx.appcompat.widget.LinearLayoutCompat.HORIZONTAL
        else
            androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
    }

inline val android.content.res.Resources.isLandTraditional: Boolean
    @com.pt.common.global.MainAnn
    get() {
        return configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    }

inline val android.content.res.Configuration.isLandTrad: Boolean
    @com.pt.common.global.MainAnn
    get() {
        return orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    }

inline val android.content.res.Resources.statusBarHeight: Int
    @com.pt.common.global.MainAnn
    @android.annotation.SuppressLint("DiscouragedApi", "InternalInsetResource")
    get() = com.pt.common.stable.catchy(48) {
        return@catchy getIdentifier(
            com.pt.common.BuildConfig.STAT_BAR_DIM,
            com.pt.common.BuildConfig.DIM_XM,
            com.pt.common.BuildConfig.ANDROID_XM
        ).run {
            getDimensionPixelSize(this)
        }
    }

inline val android.content.Context.actionBarHeight: Int
    @MainAnn
    get() = com.pt.common.stable.catchy(128) {
        return@catchy android.util.TypedValue().run {
            theme.resolveAttribute(android.R.attr.actionBarSize, this, true)
            android.util.TypedValue.complexToDimensionPixelSize(data, resources.displayMetrics)
        }
    }


inline val android.content.res.Resources.navigationBarHeight: Int
    @com.pt.common.global.MainAnn
    @android.annotation.SuppressLint("DiscouragedApi", "InternalInsetResource")
    get() = com.pt.common.stable.catchy(0) {
        getIdentifier(
            com.pt.common.BuildConfig.IS_SHOW_NAV,
            com.pt.common.BuildConfig.BOOL_REC,
            com.pt.common.BuildConfig.ANDROID_XM
        ).let { id ->
            @Suppress("KotlinConstantConditions")
            if (id > 0 && getBoolean(id)) {
                getIdentifier(
                    com.pt.common.BuildConfig.NAVIGATION_DIM,
                    com.pt.common.BuildConfig.DIM_XM,
                    com.pt.common.BuildConfig.ANDROID_XM
                ).let {
                    if (it > 0) {
                        return@catchy getDimensionPixelSize(it)
                    } else {
                        return@catchy 0
                    }
                }
            } else {
                0
            }
        }
    }

@com.pt.common.global.UiAnn
fun androidx.appcompat.widget.AppCompatImageView.svgReColorWhite() {
    androidx.core.widget.ImageViewCompat.setImageTintList(
        this@svgReColorWhite,
        android.content.res.ColorStateList.valueOf(android.graphics.Color.WHITE)
    )
}

@com.pt.common.global.UiAnn
fun android.view.View.backReColor(@androidx.annotation.ColorInt color: Int) {
    if (isV_NOT_M) {
        background?.colorFilter = android.graphics.PorterDuffColorFilter(
            color,
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    } else {
        backgroundTintList = android.content.res.ColorStateList.valueOf(color)
    }
}

fun android.view.View.intiBack21(@androidx.annotation.ColorInt color: Int) {
    if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.LOLLIPOP) {
        background?.colorFilter = android.graphics.PorterDuffColorFilter(
            color,
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    } else return
}

@com.pt.common.global.UiAnn
fun androidx.appcompat.widget.AppCompatImageView.svgReColor(
    @androidx.annotation.ColorInt color: Int,
) {
    androidx.core.widget.ImageViewCompat.setImageTintList(
        this@svgReColor,
        android.content.res.ColorStateList.valueOf(color)
    )
}

@com.pt.common.global.UiAnn
suspend fun android.view.View.backReColorSus(@androidx.annotation.ColorInt color: Int) {
    com.pt.common.stable.withMain {
        if (isV_NOT_M) {
            background?.colorFilter = android.graphics.PorterDuffColorFilter(
                color,
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            backgroundTintList = android.content.res.ColorStateList.valueOf(color)
        }
    }
}

@com.pt.common.global.UiAnn
fun androidx.appcompat.widget.AppCompatSeekBar.seekReColor(@androidx.annotation.ColorInt color: Int) {
    if (isV_NOT_M) {
        progressDrawable?.colorFilter = android.graphics.PorterDuffColorFilter(
            color,
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    } else {
        progressTintList = android.content.res.ColorStateList.valueOf(color)
    }
}

@com.pt.common.global.UiAnn
suspend fun androidx.appcompat.widget.AppCompatImageView.svgReColorSus(
    @androidx.annotation.ColorInt color: Int,
) {
    com.pt.common.stable.withMain {
        //imageTintMode = android.graphics.PorterDuff.Mode
        //imageTintList = android.content.res.ColorStateList.valueOf(color)
        androidx.core.widget.ImageViewCompat.setImageTintList(
            this@svgReColorSus,
            android.content.res.ColorStateList.valueOf(color)
        )
    }
}

@com.pt.common.global.UiAnn
fun androidx.appcompat.widget.AppCompatImageView.svgReClear() {
    androidx.core.widget.ImageViewCompat.setImageTintList(
        this@svgReClear,
        null
    )
}

@androidx.annotation.Keep
fun Any?.logProvLess() {
    kotlin.runCatching {
        if (com.pt.common.BuildConfig.VERSION_DEBUG) {
            print(this.toStr)
        } else return@runCatching
    }
}
/*
@Suppress("unused")
fun Any?.logProv(log: String): String {
    kotlin.runCatching {
        if (com.pt.common.BuildConfig.VERSION_DEBUG) {
            android.util.Log.w(log, this@logProv.toStr)
        } else return@runCatching
        Unit
    }
    return this.toString() + log
}

@Suppress("unused")
suspend fun Any?.logProvSus(log: String): String = com.pt.common.stable.justCoroutine {
    kotlin.runCatching {
        if (com.pt.common.BuildConfig.VERSION_DEBUG) {
            android.util.Log.w(log, this@logProvSus.toStr)
        }
        Unit
    }
    return@justCoroutine this@logProvSus.toString() + log
}*/

fun String.logProvCrash(log: String): String {
    /*if (com.pt.common.BuildConfig.VERSION_DEBUG) {
        kotlin.runCatching {
            android.util.Log.e("logProvCrash$log", this@logProvCrash)
            if (this@logProvCrash.contains("JobCancellationException".toRegex())) {
                return@runCatching
            }
            @Suppress("HardCodedStringLiteral")
            java.io.FileWriter(com.pt.common.BuildConfig.FILE_LOGGER, true).useMy {
                ("\n" + "LAST_MODIFIED -> " + System.currentTimeMillis().findFileDate + "\n").let {
                    it + this@logProvCrash
                }.let(::write)
            }
        }.onFailure {
            android.util.Log.e("logProvCrash", it.toStr)
        }
    }*/
    return log
}

inline val android.content.res.Resources.isDeviceDark: Boolean
    @com.pt.common.global.MainAnn
    get() {
        return com.pt.common.stable.catchy(false) {
            (configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
                    android.content.res.Configuration.UI_MODE_NIGHT_YES)
        }
    }

inline val android.content.res.Resources.isRightToLeft: Boolean
    @com.pt.common.global.MainAnn
    get() = configuration.layoutDirection == 1

@com.pt.common.global.MainAnn
inline fun android.content.Context.fetchDimensions(siz: android.util.Size.() -> Unit) {
    with(
        fetchSystemService(windowService)
    ) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            this@with?.maximumWindowMetrics.run {
                this@run?.bounds?.let {
                    android.util.Size(it.width(), it.height()).apply(siz)
                }
            }
        } else {
            android.util.DisplayMetrics().apply {
                @Suppress("DEPRECATION")
                this@with?.defaultDisplay?.getRealMetrics(this@apply)
            }.run {
                android.util.Size(widthPixels, heightPixels).apply(siz)
            }
        }
    }
}

@com.pt.common.global.MainAnn
inline fun android.view.WindowManager.fetchDimensionsMan(siz: android.util.Size.() -> Unit) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
        maximumWindowMetrics.run {
            this@run.bounds.let {
                android.util.Size(it.width(), it.height()).apply(siz)
            }
        }
    } else {
        android.util.DisplayMetrics().apply {
            @Suppress("DEPRECATION")
            defaultDisplay.getRealMetrics(this@apply)
        }.run {
            android.util.Size(widthPixels, heightPixels).apply(siz)
        }
    }
}

@get:androidx.annotation.RequiresApi(android.os.Build.VERSION_CODES.R)
inline val android.content.Context.fetchDisplay: android.view.Display
    get() = fetchDisplayManager.getDisplay(android.view.Display.DEFAULT_DISPLAY)

inline val android.content.Context.fetchDisplayManager: android.hardware.display.DisplayManager
    get() {
        return androidx.core.content.ContextCompat.getSystemService(
            this,
            android.hardware.display.DisplayManager::class.java
        ) ?: getSystemService(
            android.content.Context.DISPLAY_SERVICE
        ) as android.hardware.display.DisplayManager
    }

suspend inline fun android.view.WindowManager.fetchDimensionsSus(
    crossinline siz: suspend android.util.Size.() -> Unit
) {
    com.pt.common.stable.withMain {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            this@fetchDimensionsSus.maximumWindowMetrics.bounds.letSus {
                android.util.Size(it.width(), it.height()).applySus(siz)
            }
        } else {
            android.util.DisplayMetrics().applySus {
                @Suppress("DEPRECATION")
                this@fetchDimensionsSus.defaultDisplay?.getRealMetrics(this@applySus)
            }.alsoSus {
                android.util.Size(it.widthPixels, it.heightPixels).applySus(siz)
            }
        }
    }
}

/*private fun isTablet(c: Context): Boolean {
    return ((c.getResources().getConfiguration().screenLayout
            and Configuration.SCREEN LAYOUT_SIZE_MASK)
            >= Configuration.SCREEN LAYOUT_SIZE_LARGE)
}*/
