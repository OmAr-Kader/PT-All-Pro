package com.pt.common.moderator.over

import com.pt.common.media.*

open class GlideImageView @JvmOverloads constructor(
    context: android.content.Context? = null,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = android.R.attr.previewImage,
) : androidx.appcompat.widget.AppCompatImageView(context!!, attrs, defStyle) {

    private var listTarget: com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable?>? = null

    private var requestNative: com.pt.common.moderator.cross.GlideRequests? = null

    private inline val android.content.Context.request: com.pt.common.moderator.cross.GlideRequests
        get() = requestNative ?: com.pt.common.moderator.cross.GlideApp.with(this@request).also {
            requestNative = it
        }

    fun loadForVideo(
        image: String
    ) {
        setImageDrawable(null)
        setImageBitmap(null)
        (context ?: return).request.loadForVideo(image).onDrawableTargetListener {
            setImageDrawable(this@onDrawableTargetListener)
        }
    }

    fun loadFolder(
        path: String,
    ) {
        setImageDrawable(null)
        setImageBitmap(null)
        (context ?: return).request.loadFolder(path).onDrawableTargetListener {
            setImageDrawable(this@onDrawableTargetListener)
        }
    }


    fun loadForData(
        path: String
    ) {
        setImageDrawable(null)
        setImageBitmap(null)
        (context ?: return).request.loadForData(path).onDrawableTargetListener {
            setImageDrawable(this@onDrawableTargetListener)
        }
    }

    fun loadForWeb(
        link: String,
    ) {
        setImageDrawable(null)
        setImageBitmap(null)
        (context ?: return).request.loadForWeb(link).onDrawableTargetListener {
            setImageDrawable(this@onDrawableTargetListener)
        }
    }

    fun loadImageForManager(
        i: com.pt.common.global.FileSack,
        str: String,
    ) {
        setImageDrawable(null)
        setImageBitmap(null)
        (context ?: return).request.loadImageForManager(i).onDrawableTargetListener {
            if (tag == str) {
                setImageDrawable(this@onDrawableTargetListener)
            }
        }
    }

    fun loadPicture(
        image: com.pt.common.global.MediaSack,
        increase: Int,
        str: String,
    ) {
        setImageDrawable(null)
        setImageBitmap(null)
        if (com.pt.common.stable.catchy(false) { com.pt.common.global.FileLate(str).extension.trim().contains("webp") }) {
            context?.loadAsWebImage(image = image, str = str)
        } else {
            context?.loadImage(image = image, incurs = increase, str = str)
        }
    }

    private fun android.content.Context.loadImage(
        image: com.pt.common.global.MediaSack,
        incurs: Int,
        str: String,
    ) {
        request.loadImage(image, incurs).onDrawableTargetListener {
            if (tag == str) {
                setImageDrawable(this@onDrawableTargetListener)
            }
        }
    }

    private fun android.content.Context.loadAsWebImage(
        image: com.pt.common.global.MediaSack,
        str: String,
    ) {
        request.loadAsWebImage(image).onDrawableTargetListener {
            if (tag == str) {
                setImageDrawable(this@onDrawableTargetListener)
            }
        }
    }

    private inline fun com.pt.common.moderator.cross.GlideRequest<android.graphics.drawable.Drawable>.onDrawableTargetListener(
        crossinline onBitmapLoaded: android.graphics.drawable.Drawable?.() -> Unit,
    ) {

        object : com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable?>() {
            override fun onResourceReady(
                resource: android.graphics.drawable.Drawable,
                transition: com.bumptech.glide.request.transition.Transition<in android.graphics.drawable.Drawable?>?
            ) {
                onBitmapLoaded.invoke(resource)
            }

            override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
        }.also<com.bumptech.glide.request.target.CustomTarget<android.graphics.drawable.Drawable?>> {
            listTarget = it
            into(it)
        }
    }

    override fun onDraw(canvas: android.graphics.Canvas?) {
        com.pt.common.stable.catchyUnit {
            super.onDraw(canvas)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        setImageDrawable(null)
        setImageBitmap(null)
        listTarget?.also {
            requestNative?.clear(it)
            listTarget = null
            requestNative = null
        }
    }
}