package com.pt.common.media

import com.pt.common.global.*
import com.pt.common.stable.alsoSusBack
import com.pt.common.stable.applySusBack

suspend fun android.content.Context.clearDiskCache() {
    com.pt.common.stable.withBack {
        try {
            com.pt.common.moderator.cross.GlideApp.get(
                applicationContext ?: return@withBack
            ).clearDiskCache()
        } catch (_: IllegalArgumentException) {
        }
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun android.content.Context?.loadRealImage(
    pathMedia: android.net.Uri,
    request: suspend android.graphics.Bitmap.() -> Unit
) {
    com.pt.common.stable.withBackDef(Unit) {
        com.pt.common.moderator.cross.GlideApp.with(this@loadRealImage ?: return@withBackDef).asBitmap().load(pathMedia)
            .dontAnimate()
            .dontTransform()
            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
            .skipMemoryCache(true).submit().get().alsoSusBack(request)
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend inline fun android.content.Context?.compressImage(
    path: String,
    crossinline bitmapPusher: android.graphics.Bitmap.() -> Unit,
) {
    com.pt.common.stable.withBack {
        com.pt.common.moderator.cross.GlideApp.with(this@compressImage ?: return@withBack).asBitmap().load(path)
            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
            .dontAnimate()
            .dontTransform()
            .priority(com.bumptech.glide.Priority.HIGH)
            .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL).submit().get().applySusBack {
                bitmapPusher(this ?: return@applySusBack)
            }
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun android.content.Context.loadAsGif(
    image: MediaSack,
    request: android.graphics.Bitmap?.() -> Unit
) {
    com.pt.common.stable.withBackDef(Unit) {
        com.pt.common.moderator.cross.GlideApp.with(this@loadAsGif).asBitmap().load(android.net.Uri.parse(image.uriMedia.toStr))
            .priority(com.bumptech.glide.Priority.IMMEDIATE)
            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
            .skipMemoryCache(true).submit().get().alsoSusBack(request)
    }
}

inline fun android.content.Context?.loadAlarm(
    img: String?,
    id: Int?,
    crossinline bitmapPusher: android.graphics.Bitmap.() -> Unit,
) {
    com.pt.common.moderator.cross.GlideApp.with(this ?: return).asBitmap().load(img)
        .signature(com.bumptech.glide.signature.ObjectKey(id ?: System.currentTimeMillis()))
        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.ALL)
        .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)
        .skipMemoryCache(true)
        .into(
            object : com.bumptech.glide.request.target.CustomTarget<android.graphics.Bitmap?>() {
                override fun onResourceReady(
                    resource: android.graphics.Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in android.graphics.Bitmap?>?
                ) {
                    bitmapPusher(resource)
                }

                override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {}
            }
        )
}


fun com.pt.common.moderator.cross.GlideRequests.loadForVideo(
    image: String,
): com.pt.common.moderator.cross.GlideRequest<android.graphics.drawable.Drawable> {
    return load(image)
        .dontAnimate()
        .dontTransform()
        .priority(com.bumptech.glide.Priority.IMMEDIATE)
        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
}

fun com.pt.common.moderator.cross.GlideRequests.loadForData(
    path: String
): com.pt.common.moderator.cross.GlideRequest<android.graphics.drawable.Drawable> {
    return load(path)
        .encodeFormat(android.graphics.Bitmap.CompressFormat.JPEG)
        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.RESOURCE)
        .override(1000, 1000)
        .skipMemoryCache(true)
}

fun com.pt.common.moderator.cross.GlideRequests.loadFolder(
    path: String
): com.pt.common.moderator.cross.GlideRequest<android.graphics.drawable.Drawable> {
    return load(path)
        .encodeFormat(android.graphics.Bitmap.CompressFormat.JPEG).transition(
            com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade(100)
        ).diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.RESOURCE)
        .override(1000, 1000)
        .skipMemoryCache(true)
}

fun com.pt.common.moderator.cross.GlideRequests.loadImage(
    image: MediaSack,
    incurs: Int,
): com.pt.common.moderator.cross.GlideRequest<android.graphics.drawable.Drawable> {
    return load(image.pathMedia)
        .signature(com.bumptech.glide.signature.ObjectKey(image.nameMedia + image.mediaWidth + incurs))
        .dontAnimate()
        .dontTransform()
        .priority(com.bumptech.glide.Priority.IMMEDIATE)
        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.RESOURCE)
        .override(incurs, incurs)
        .skipMemoryCache(true)
}

fun com.pt.common.moderator.cross.GlideRequests.loadAsWebImage(
    image: MediaSack,
): com.pt.common.moderator.cross.GlideRequest<android.graphics.drawable.Drawable> {
    return asDrawable()
        .load(image.pathMedia.toStr)
        .dontAnimate()
        .dontTransform()
        .skipMemoryCache(true)
}

fun com.pt.common.moderator.cross.GlideRequests.loadForWeb(
    link: String,
): com.pt.common.moderator.cross.GlideRequest<android.graphics.drawable.Drawable> {
    return load(link).override(
        com.bumptech.glide.request.target.Target.SIZE_ORIGINAL,
        com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
    ).signature(
        com.bumptech.glide.signature.ObjectKey(
            (link.toDefString(50) + kotlin.random.Random.nextInt(
                100
            ).toStr)
        )
    ).diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.RESOURCE)
        .skipMemoryCache(true)
}

fun com.pt.common.moderator.cross.GlideRequests.loadImageForManager(
    i: FileSack,
): com.pt.common.moderator.cross.GlideRequest<android.graphics.drawable.Drawable> {
    return load(i.filePath)
        .transition(
            com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade(
                100
            )
        ).signature(com.bumptech.glide.signature.ObjectKey(i.fileName + i.fileSize))
        .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.RESOURCE)
        .priority(com.bumptech.glide.Priority.HIGH)
        .override(300, 300)
        .skipMemoryCache(true)
}