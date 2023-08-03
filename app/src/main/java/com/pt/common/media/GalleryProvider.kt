package com.pt.common.media

import com.pt.common.global.*
import com.pt.common.global.FileLate
import com.pt.common.global.MediaSack
import com.pt.common.stable.*

internal suspend fun android.content.Context.viewProvider(
    uri: android.net.Uri,
    isImage: Boolean,
    orderBy: String,
    orderByFolder: String,
    hiddenRunning: Boolean,
    isDoInLand: Boolean,
    margeWidth: Int,
    margeHeight: Int,
    b: com.pt.pro.gallery.objects.PagerHolder.() -> Unit
) {
    if (isImage) {
        val path = getImagePathFromURI(uri).toStr
        val folds = withBackDef(mutableListOf()) {
            contentResolver.imageFoldersLoader(orderByFolder)
        }
        //if (path != null) {
        val m = withBackDef(mutableListOf()) {
            val a = (FileLate(path).parentFile?.absolutePath ?: return@withBackDef mutableListOf()) + "/"
            contentResolver.allVideosLoader(a, orderBy).letSusBack { vi ->
                return@letSusBack contentResolver.allImageLoader(a, orderBy).runSusBack {
                    return@runSusBack mutableListOf<MediaSack>().applySusBack {
                        addAll(vi)
                        addAll(this@runSusBack)
                    }
                }
            }
        }
        withBack {
            if (m.isEmpty()) {
                mutableListOf<MediaSack>().apply {
                    val p = FileLate(path)
                    MediaSack(
                        p.name,
                        p.absolutePath,
                        uri.toStr,
                        true,
                        p.length(),
                        0,
                        0,
                        p.lastModified()
                    ).run(this@apply::add)
                }
            } else {
                m
            }.let { media ->
                media.indexOfFirst {
                    it.pathMedia == path
                }.let {
                    if (it == -1) 0 else it
                }.let {
                    com.pt.pro.gallery.objects.PagerHolder(
                        mediaHolder = media,
                        folds = folds,
                        imagePosition = it,
                        pending = hiddenRunning,
                        main = true,
                        isHiddenActive = hiddenRunning,
                        isFileManager = false,
                        isDoInLand = isDoInLand,
                        margeWidth = margeWidth,
                        margeHeight = margeHeight
                    ).letSusBack(b)
                }
            }
        }
    } else {
        val path = getVideoPathFromURI(uri).toStr
        val folds = withBackDef(mutableListOf()) {
            contentResolver.videoFoldersLoader(orderByFolder)
        }
        val m = withBackDef(mutableListOf()) {
            val a = (FileLate(path).parentFile?.absolutePath ?: return@withBackDef mutableListOf()) + "/"
            contentResolver.allVideosLoader(a, orderBy).letSusBack { vi ->
                return@letSusBack contentResolver.allImageLoader(a, orderBy).runSusBack {
                    return@runSusBack mutableListOf<MediaSack>().applySusBack {
                        addAll(vi)
                        addAll(this@runSusBack)
                    }
                }
            }
        }
        withBack {
            if (m.isEmpty()) {
                mutableListOf<MediaSack>().apply {
                    val p = FileLate(path)
                    MediaSack(
                        p.name,
                        p.absolutePath,
                        uri.toStr,
                        false,
                        p.length(),
                        0,
                        0,
                        p.lastModified()
                    ).run(this@apply::add)
                }
            } else {
                m
            }.let { media ->
                media.indexOfFirst {
                    it.pathMedia == path
                }.let {
                    if (it == -1) 0 else it
                }.let {
                    com.pt.pro.gallery.objects.PagerHolder(
                        mediaHolder = media,
                        folds = folds,
                        imagePosition = it,
                        pending = hiddenRunning,
                        main = true,
                        isHiddenActive = hiddenRunning,
                        isFileManager = false,
                        isDoInLand = isDoInLand,
                        margeWidth = margeWidth,
                        margeHeight = margeHeight
                    ).letSusBack(b)
                }
            }
        }
    }
}

internal suspend inline fun androidx.fragment.app.FragmentManager.doLaunchGalleryImage(
    tag: String,
    crossinline fragment: suspend androidx.fragment.app.FragmentTransaction.() -> Unit
) {
    justCoroutine {
        fragmentStackLauncher(tag) {
            setCustomAnimations(
                com.pt.pro.R.animator.slide_down,
                com.pt.pro.R.animator.slide_up,
                com.pt.pro.R.animator.slide_down_close,
                com.pt.pro.R.animator.slide_up_close
            )
            fragment(this)
            addToBackStack(tag)
        }
    }
}

internal inline val MediaSack.sackConverterTo: FileSack
    get() {
        return FileSack(
            fileName = nameMedia.toString(),
            filePath = pathMedia.toString(),
            fileUri = uriMedia,
            typeFile = if (isImage) IMAGE else VIDEO,
            fileSize = mediaSize,
            dateModified = System.currentTimeMillis()
        )
    }

internal inline fun android.content.Context.editLauncher(
    uriMedia: android.net.Uri?,
    pathMedia: String,
    intent: android.content.Intent.() -> Unit
) {
    (uriMedia ?: uriProviderNormal(pathMedia, com.pt.pro.BuildConfig.APPLICATION_ID)).let { ur ->
        android.content.Intent(
            android.content.Intent.ACTION_EDIT,
            ur
        ).also {
            it.flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            /*it.clipData = android.content.ClipData(
                "share",
                arrayOf(com.pt.common.BuildConfig.IMAGE_SENDER),
                android.content.ClipData.Item(ur)
            )*/
        }
    }.also {
        android.content.Intent.createChooser(it, "Edit").apply(intent)
    }
}

internal suspend fun getVideoRatio(
    m: MediaSack,
    def: Float,
): Float = justCoroutine {
    withBackDef(def) {
        withBackDef(def) {
            kotlin.runCatching {
                com.pt.common.moderator.over.MyMediaMetadataRetriever().useSusMeta {
                    setDataSource(m.pathMedia)
                    extractMetadata(
                        android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH
                    )?.toFloat()?.letSusBack { w ->
                        extractMetadata(
                            android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT
                        )?.toFloat()?.letSusBack { h ->
                            extractMetadata(
                                android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION
                            )?.toInt().letSusBack {
                                if (it == 90 || it == 270) {
                                    (h / w)
                                } else {
                                    (w / h)
                                }
                            }
                        }
                    }.letSusBack {
                        it ?: def
                    }
                }
            }.getOrDefault(def)
        }
    }
}