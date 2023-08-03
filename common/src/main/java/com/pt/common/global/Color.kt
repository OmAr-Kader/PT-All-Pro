package com.pt.common.global

import androidx.core.graphics.*
import androidx.core.graphics.drawable.toBitmap
import com.pt.common.stable.*
import kotlin.math.roundToInt

inline val String.toColor: Int
    @com.pt.common.global.WorkerAnn
    get() = runCatching { android.graphics.Color.parseColor(this) }.getOrDefault(0)

inline val Int.toColorAlpha: (Int) -> Int
    @com.pt.common.global.WorkerAnn
    get() = {
        runCatching {
            ColorUtils.setAlphaComponent(
                this@toColorAlpha,
                it
            )
        }.getOrDefault(this@toColorAlpha)
    }

inline val Int.toTintList: android.content.res.ColorStateList
    @com.pt.common.global.WorkerAnn
    get() = android.content.res.ColorStateList.valueOf(this)

inline val Int.isColorDarkText: Int
    @com.pt.common.global.WorkerAnn
    get() {
        return (1 - (0.299 * android.graphics.Color.red(this) +
                0.587 * android.graphics.Color.green(this) +
                0.114 * android.graphics.Color.blue(this))
                / 255).let {
            if (it <= 0.5) android.graphics.Color.BLACK else android.graphics.Color.WHITE
        }
    }

inline val @receiver:androidx.annotation.ColorInt Int.darken: Int
    @androidx.annotation.ColorInt
    get() = ColorUtils.blendARGB(this, android.graphics.Color.BLACK, 0.5F)

inline val Int.isColorDarkOrWhiter: (Double) -> Boolean
    @com.pt.common.global.WorkerAnn
    get() = { d ->
        (1 - (0.299 * android.graphics.Color.red(this) +
                0.587 * android.graphics.Color.green(this) +
                0.114 * android.graphics.Color.blue(this))
                / 255).let {
            it <= d
        }
    }

inline val Int.brightColor: Int
    @com.pt.common.global.WorkerAnn
    get() = intArrayOf(
        android.graphics.Color.red(this),
        android.graphics.Color.green(this),
        android.graphics.Color.blue(this)
    ).run {
        kotlin.math.sqrt(
            this[0] * this[0] * .241 + (this[1]
                    * this[1] * .691) + this[2] * this[2] * .068
        ).toInt()
    }

inline val String.fetchIMGSize: android.graphics.BitmapFactory.Options?
    @com.pt.common.global.WorkerAnn
    get() {
        return catchy(null) {
            android.graphics.BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                android.graphics.BitmapFactory.decodeFile(this@fetchIMGSize, this)
            }
        }
    }

inline val String.fetchIMGSizeSus: suspend () -> android.graphics.BitmapFactory.Options?
    @com.pt.common.global.WorkerAnn
    get() = {
        withBackDef(null) {
            android.graphics.BitmapFactory.Options().apply {
                inJustDecodeBounds = true
                android.graphics.BitmapFactory.decodeFile(this@fetchIMGSizeSus, this)
            }
        }
    }

inline val android.content.Context?.isNeedWhite: Boolean
    @com.pt.common.global.WorkerAnn
    get() {
        return catchy(true) {
            return@catchy findWallpaper?.toBitmap().run {
                colorDetection
            }.let {
                it < 200
            }
        }
    }

inline val android.content.Context?.findWallpaper: android.graphics.drawable.Drawable?
    @android.annotation.SuppressLint("MissingPermission") @com.pt.common.global.WorkerAnn
    get() = android.app.WallpaperManager.getInstance(this@findWallpaper)?.drawable

inline val android.graphics.Bitmap?.colorDetection: Int
    @com.pt.common.global.WorkerAnn
    get() {
        return if (this != null) {
            runCatching {
                return@runCatching getPixel((0.5 * width).toInt(), (0.6 * height).toInt()).run {
                    (0.21 * android.graphics.Color.red(this) +
                            0.71 * android.graphics.Color.blue(this) +
                            0.07 * android.graphics.Color.green(this))
                }.toInt()
            }.getOrDefault(0)
        } else {
            0
        }
    }


@com.pt.common.global.WorkerAnn
suspend fun android.graphics.Bitmap.writeBitmapSus(file: FileLate) {
    withBack(shouldBack = true) {
        file.outputStream().useBack {
            compress(android.graphics.Bitmap.CompressFormat.JPEG, 95, this@useBack)
            this@useBack.flush()
        }
    }
}

@com.pt.common.global.WorkerAnn
suspend fun android.graphics.Bitmap.writeCompressedBitmap(file: FileLate) {
    justCoroutine {
        withBack {
            file.outputStream().useBack {
                compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, this)
                catchy(Unit) {
                    flush()
                }
            }
        }
    }
}

inline val android.content.Context.fetchColor: (Int) -> Int
    @com.pt.common.global.WorkerAnn
    get() = @androidx.annotation.ColorInt {
        androidx.core.content.ContextCompat.getColor(this@fetchColor, it)
    }

@com.pt.common.global.WorkerAnn
suspend fun changeColor(
    one: androidx.palette.graphics.Palette?,
    sack: DSack<Int, Int, Boolean>
): DSack<Int, Int, Int> = justCoroutine {
    withBackDef(mutableListOf()) {
        mutableListOf<Int>().runSusBack {
            one?.dominantSwatch?.rgb?.letSusBack(::add)
            one?.swatches?.onEachSusBack(true) {
                this@runSusBack.add(this@onEachSusBack.rgb)
            }
            arrayListOf<androidx.palette.graphics.Target>(
                androidx.palette.graphics.Target.LIGHT_MUTED,
                androidx.palette.graphics.Target.LIGHT_VIBRANT,
                androidx.palette.graphics.Target.MUTED,
                androidx.palette.graphics.Target.VIBRANT,
                androidx.palette.graphics.Target.DARK_VIBRANT,
                androidx.palette.graphics.Target.DARK_MUTED,
            ).letSusBack {
                mutableListOf<Int>().applySusBack {
                    it.onEachSusBack(true) {
                        one?.getSwatchForTarget(this@onEachSusBack)?.rgb?.letSusBack { it1 ->
                            this@applySusBack.add(it1)
                        }
                    }
                }
            }.letSusBack { it1 -> addAll(it1) }
            this@runSusBack
        }
    }.letSusBack { rbg ->
        withBackDef(DSack(sack.one, sack.two, sack.one.toColorAlpha(200))) {
            mutableListOf<Int>().runSusBack {
                rbg.onEachSusBack(true) {
                    this@runSusBack.add(this@onEachSusBack.brightColor)
                }
                this@runSusBack
            }.letSusBack { br ->
                if (sack.three) {
                    br.maxOrNull()
                } else {
                    br.minOrNull()
                }.letSusBack {
                    it?.letSusBack { itCN ->
                        rbg[br.indexOf(itCN)]
                    } ?: sack.one
                }.letSusBack { itFOne ->
                    if (sack.three) {
                        br.minOrNull()
                    } else {
                        br.maxOrNull()
                    }.letSusBack { iC ->
                        iC?.letSusBack { itCN ->
                            rbg[br.indexOf(itCN)]
                        } ?: sack.two
                    }.letSusBack { itFTwo ->
                        DSack(
                            itFOne.editMainMusicColor(sack.three),
                            itFTwo.editBackColor(sack.three),
                            itFOne.toColorAlpha(200)
                        )
                    }
                }
            }
        }
    }
}

private inline val Int.editBackColor: (Boolean) -> Int
    get() = {
        when {
            this@editBackColor.isColorDarkOrWhiter(0.15) && it -> {
                ColorUtils.blendARGB(this@editBackColor, android.graphics.Color.BLACK, 0.5F)
            }
            !this@editBackColor.isColorDarkOrWhiter(0.15) && it -> {
                this@editBackColor
            }
            this@editBackColor.isColorDarkOrWhiter(0.85) && !it -> {
                this@editBackColor
            }
            !this@editBackColor.isColorDarkOrWhiter(0.85) && !it -> {
                ColorUtils.blendARGB(this@editBackColor, android.graphics.Color.WHITE, 0.5F)
            }
            else -> {
                this@editBackColor
            }
        }
    }


private inline val Int.editMainMusicColor: (Boolean) -> Int
    get() = {
        when {
            this@editMainMusicColor.isColorDarkOrWhiter(0.85) && it -> {
                this@editMainMusicColor
            }
            !this@editMainMusicColor.isColorDarkOrWhiter(0.85) && it -> {
                ColorUtils.blendARGB(this@editMainMusicColor, android.graphics.Color.WHITE, 0.5F)
            }
            this@editMainMusicColor.isColorDarkOrWhiter(0.15) && !it -> {
                ColorUtils.blendARGB(this@editMainMusicColor, android.graphics.Color.BLACK, 0.5F)
            }
            !this@editMainMusicColor.isColorDarkOrWhiter(0.15) && !it -> {
                this@editMainMusicColor
            }
            else -> {
                this@editMainMusicColor
            }
        }
    }

suspend fun android.graphics.drawable.Drawable.toBitmapSus(
    @androidx.annotation.Px width: Int = intrinsicWidth,
    @androidx.annotation.Px height: Int = intrinsicHeight,
    config: android.graphics.Bitmap.Config? = null,
): android.graphics.Bitmap? = justCoroutine {
    withBackDef(null) {
        if (this is android.graphics.drawable.BitmapDrawable) {
            if (config == null || bitmap.config == config) {
                if (width == intrinsicWidth && height == intrinsicHeight) {
                    return@withBackDef bitmap
                }
                return@withBackDef android.graphics.Bitmap.createScaledBitmap(
                    bitmap,
                    width,
                    height,
                    true
                )
            }
        }
        val (oldLeft, oldTop, oldRight, oldBottom) = bounds
        return@withBackDef android.graphics.Bitmap.createBitmap(
            width,
            height,
            config ?: android.graphics.Bitmap.Config.ARGB_8888
        ).applySusBack {
            setBounds(0, 0, width, height)
            draw(android.graphics.Canvas(this@applySusBack))
            setBounds(oldLeft, oldTop, oldRight, oldBottom)
        }
    }
}

suspend fun android.graphics.Bitmap?.changeBrightness(
    contrast: Float = 1F,
    brightness: Float = -100F,
): android.graphics.Bitmap? = justCoroutine {
    if (this@changeBrightness == null) return@justCoroutine null
    withBackDef(this@changeBrightness) {
        floatArrayOf(
            contrast,
            0f,
            0f,
            0f,
            brightness,
            0f,
            contrast,
            0f,
            0f,
            brightness,
            0f,
            0f,
            contrast,
            0f,
            brightness,
            0f,
            0f,
            0f,
            1f,
            0f
        ).letSusBack {
            android.graphics.ColorMatrix(it)
        }.letSusBack { cm ->
            android.graphics.Bitmap.createBitmap(width, height, config).applySusBack {
                android.graphics.Canvas(this@applySusBack).alsoSusBack { c ->
                    android.graphics.Paint().alsoSusBack { p ->
                        p.colorFilter = android.graphics.ColorMatrixColorFilter(cm)
                        c.drawBitmap(this@changeBrightness, 0F, 0F, p)
                    }
                }
            }
        }
    }
}

@Suppress("DEPRECATION")
suspend inline fun android.content.Context.blur(
    image: android.graphics.Bitmap,
    crossinline blurBitmap: suspend android.graphics.Bitmap.() -> Unit,
) {
    withBack {
        val width = (image.width.toFloat() * 0.6F).roundToInt()
        val height = (image.height.toFloat() * 0.6F).roundToInt()
        android.graphics.Bitmap.createScaledBitmap(
            image,
            width,
            height,
            false
        ).alsoSusBack { inputBitmap ->
            android.graphics.Bitmap.createBitmap(inputBitmap).applySusBack {
                android.renderscript.RenderScript.create(this@blur).alsoSusBack {
                    android.renderscript.ScriptIntrinsicBlur.create(
                        it,
                        android.renderscript.Element.U8_4(it)
                    ).alsoSusBack { intrinsicBlur ->
                        android.renderscript.Allocation.createFromBitmap(
                            it,
                            inputBitmap
                        ).letSusBack { tmpIn ->
                            android.renderscript.Allocation.createFromBitmap(
                                it,
                                this@applySusBack
                            ).letSusBack { tmpOut ->
                                intrinsicBlur.setRadius(24F)
                                intrinsicBlur.setInput(tmpIn)
                                intrinsicBlur.forEach(tmpOut)
                                tmpOut.copyTo(this@applySusBack)
                            }
                        }
                    }
                }
            }.alsoSusBack(blurBitmap)
        }
    }
}

suspend fun android.graphics.Bitmap.paletteSus(
    def: DSack<Int, Int, Int>,
    nightRider: Boolean,
): DSack<Int, Int, Int> =
    justCoroutine {
        withBackDef(null) {
            androidx.palette.graphics.Palette.from(this@paletteSus).generate()
        }.letSusBack { pla ->
            if (pla == null) {
                def
            } else {
                changeColor(
                    pla,
                    DSack(def.one, def.two, nightRider)
                )
            }
        }
    }

suspend fun android.content.ContentResolver.getExifOrientation(sourceUri: String): Int = justCoroutine {
    withDefaultDef(0) {
        var exifOrientation = 0
        if (sourceUri.startsWith(android.content.ContentResolver.SCHEME_CONTENT)) {
            var cursor: android.database.Cursor? = null
            try {
                val columns = arrayOf(android.provider.MediaStore.Images.Media.ORIENTATION)
                cursor = query(android.net.Uri.parse(sourceUri), columns, null, null, null)
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        val orientation = cursor.getInt(0)
                        exifOrientation = if (listOf(
                                0,
                                90,
                                180,
                                270,
                                -1
                            ).contains(orientation) && orientation != -1
                        ) {
                            orientation
                        } else {
                            0
                        }
                    }
                }
            } catch (e: Exception) {
                exifOrientation = 0
            } finally {
                cursor?.close()
            }
        } else if (sourceUri.startsWith("file:///") && !sourceUri.startsWith("file:///android_asset/")) {
            try {
                val exifInterface = androidx.exifinterface.media.ExifInterface(sourceUri.substring("file:///".length - 1))
                val orientationAttr = exifInterface.getAttributeInt(
                    androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
                )
                exifOrientation = when (orientationAttr) {
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL, androidx.exifinterface.media.ExifInterface.ORIENTATION_UNDEFINED -> {
                        0
                    }
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> {
                        90
                    }
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> {
                        180
                    }
                    androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> {
                        270
                    }
                    else -> {
                        0
                    }
                }
            } catch (e: Exception) {
                exifOrientation = 0
            }
        }
        return@withDefaultDef exifOrientation
    }
}

/*suspend fun drawTextToBitmap(
    s: DSack<Float, String, Int>,
): android.graphics.Bitmap? = justCoroutine {
    return@justCoroutine withBackDef(null) {
        android.graphics.Bitmap.createBitmap(
            1000,
            1000,
            android.graphics.Bitmap.Config.ARGB_8888
        ).applySusBack {
            android.graphics.Canvas(
                this@applySusBack
            ).letSusBack<android.graphics.Canvas, Unit> { canvas ->
                canvas.drawColor(s.three)
                android.graphics.Paint(
                    android.graphics.Paint.ANTI_ALIAS_FLAG
                ).alsoSusBack { paint ->
                    paint.color = android.graphics.Color.rgb(110, 110, 110)
                    paint.textSize = (15F * s.one)
                    //paint.setShadowLayer(1f, 0f, 1f, android.graphics.Color.DK GRAY)
                    android.graphics.Rect().alsoSusBack { bounds ->
                        paint.getTextBounds(s.two, 0, s.two.length, bounds)
                        val x: Int = (width - bounds.width()) / 6
                        val y: Int = (height + bounds.height()) / 5
                        canvas.drawText(s.two, x * s.one, y * s.one, paint)
                    }
                }
            }
        }
    }
}*/
