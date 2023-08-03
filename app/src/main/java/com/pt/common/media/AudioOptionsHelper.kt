package com.pt.common.media

import com.pt.common.global.*
import com.pt.common.stable.*

internal inline val MutableList<MusicSack>.musicIndex: suspend (String) -> Int
    get() = a@{ path ->
        for ((index, item) in this.withIndex()) {
            if (item.pathAudio == path)
                return@a index
        }
        return@a -1
    }

internal inline val androidx.media3.common.MediaItem.RequestMetadata.fetchSearchStr: String?
    get() {
        return if (isV_T) {
            extras?.getString(
                android.provider.MediaStore.EXTRA_MEDIA_TITLE
            )?.lowercase() ?: searchQuery?.lowercase()
        } else {
            @Suppress("DEPRECATION")
            extras?.get(
                android.provider.MediaStore.EXTRA_MEDIA_TITLE
            )?.toString()?.lowercase() ?: searchQuery?.lowercase()
        }.let { query ->
            if (query?.startsWith("play ", ignoreCase = true) == true) {
                query.drop(5)
            } else {
                query
            }
        }
    }

internal suspend inline fun android.content.Context.getMusicBit(
    path: String,
    @androidx.annotation.DrawableRes failed: Int,
    num: Int = 4,
    crossinline b: suspend android.graphics.Bitmap?.(Boolean) -> Unit,
) {
    justCoroutineCur {
        withBackCurDef(null) {
            runCatching {
                com.pt.common.moderator.over.MyMediaMetadataRetriever().useSusMeta {
                    setDataSource(path)
                    runCatching {
                        embeddedPicture
                    }.getOrNull()?.letSusBack {
                        android.graphics.BitmapFactory.Options().applySusBack {
                            //outWidth = num
                            //outHeight = num
                            if (num > 1) inSampleSize = num
                        }.runSusBack {
                            android.graphics.BitmapFactory.decodeByteArray(it, 0, it.size, this)
                        }
                    }
                }
            }.getOrNull()
        }.alsoSusBack { bit ->
            withBackCurDef(null) {
                if (bit == null) {
                    b.invoke(compactImageReturn(failed)?.toBitmapSus(), true)
                } else {
                    b.invoke(bit, false)
                }
            }
        }
    }
}


@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
internal suspend inline fun android.content.Context.getMusicWithDuration(
    path: String,
    num: Int,
    crossinline m: suspend MusicSack.(Boolean) -> Unit,
) {
    justCoroutineCur {
        var isFailed = false
        return@justCoroutineCur withBackCurDef(null) {
            runCatching {
                com.pt.common.moderator.over.MyMediaMetadataRetriever().useSusMeta {
                    setDataSource(path)
                    runCatching {
                        this@useSusMeta.embeddedPicture
                    }.getOrNull().letSusBack {
                        return@letSusBack if (it == null) {
                            withBackCurDef(null) {
                                isFailed = true
                                compactImageReturn(
                                    com.pt.pro.R.drawable.ic_failed_song
                                )?.toBitmapSus()
                            }
                        } else {
                            withBackCurDef(null) {
                                android.graphics.BitmapFactory.Options().applySusBack {
                                    outWidth = num
                                    outHeight = num
                                    //if (num > 1) inSampleSize = num
                                }.runSusBack {
                                    android.graphics.BitmapFactory.decodeByteArray(
                                        it,
                                        0,
                                        it.size,
                                        this
                                    )
                                }
                            }
                        }
                    }.letSusBack { bit ->
                        ((this@useSusMeta.extractMetadata(
                            android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
                        )?.toLong()) ?: 0L).letSusBack { d ->
                            MusicSack(
                                android.provider.MediaStore.UNKNOWN_STRING,
                                FileLate(path).extension,
                                bitmap = bit,
                                dur_NSongs = d
                            )
                        }
                        runCatching {
                            this@useSusMeta.extractMetadata(
                                android.media.MediaMetadataRetriever.METADATA_KEY_TITLE
                            ).runSusBack {
                                return@runSusBack if (isNullOrEmpty())
                                    FileLate(path).fetchFileName
                                else
                                    this
                            }.letSusBack { tit ->
                                ((this@useSusMeta.extractMetadata(
                                    android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
                                )?.toLong()) ?: 0L).letSusBack { d ->
                                    contentResolver.findIDForArtist(
                                        path,
                                        FileLate(path).extension
                                    ) {}.runSusBack {
                                        MusicSack(
                                            title = tit,
                                            artist = this,
                                            bitmap = bit,
                                            dur_NSongs = d
                                        )
                                    }
                                }
                            }
                        }.getOrElse {
                            MusicSack(
                                artist = android.provider.MediaStore.UNKNOWN_STRING,
                                bitmap = bit,
                                title = FileLate(path).extension
                            )
                        }
                    }
                }
            }.getOrElse {
                isFailed = true
                MusicSack(
                    artist = android.provider.MediaStore.UNKNOWN_STRING,
                    bitmap = compactImageReturn(
                        com.pt.pro.R.drawable.ic_failed_song
                    )?.toBitmapSus(),
                    title = FileLate(path).extension
                )
            }.applySusBack {
                m.invoke(this, isFailed)
            }
        }
    }
}

internal suspend fun android.content.Context.getMusicAllTags(
    path: String,
    tryFirst: Boolean,
    bitmapFailed: ByteArray?,
): DSackT<MusicSack, ByteArray?> = justCoroutine {
    withBackCurDef(null) {
        runCatching {
            com.pt.common.moderator.over.MyMediaMetadataRetriever().useSusMeta {
                setDataSource(path)
                runCatching {
                    this@useSusMeta.embeddedPicture
                }.getOrNull().letSusBack {
                    return@letSusBack if (it == null) {
                        bitmapFailed
                    } else {
                        this@useSusMeta.embeddedPicture
                    }
                }.letSusBack { bit ->
                    ((this@useSusMeta.extractMetadata(
                        android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
                    )?.toLong()) ?: 0L).letSusBack { d ->
                        DSackT(
                            MusicSack(
                                android.provider.MediaStore.UNKNOWN_STRING,
                                FileLate(path).extension,
                                dur_NSongs = d
                            ), bit
                        )
                    }
                    runCatching {
                        this@useSusMeta.extractMetadata(
                            android.media.MediaMetadataRetriever.METADATA_KEY_TITLE
                        ).runSusBack {
                            return@runSusBack if (isNullOrEmpty())
                                FileLate(path).fetchFileName
                            else
                                this
                        }.letSusBack { tit ->
                            ((this@useSusMeta.extractMetadata(
                                android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
                            )?.toLong()) ?: 0L).letSusBack { d ->
                                if (tryFirst) {
                                    this@useSusMeta.extractMetadata(
                                        android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST
                                    ) ?: contentResolver.findIDForArtist(
                                        path,
                                        FileLate(path).extension
                                    ) {}
                                } else {
                                    contentResolver.findIDForArtist(
                                        path,
                                        FileLate(path).extension
                                    ) {}
                                }.letSusBack { art ->
                                    DSackT(
                                        MusicSack(
                                            title = tit,
                                            artist = art,
                                            dur_NSongs = d
                                        ), bit
                                    )
                                }
                            }
                        }
                    }.getOrElse {
                        DSackT(
                            MusicSack(
                                artist = android.provider.MediaStore.UNKNOWN_STRING,
                                title = FileLate(path).extension
                            ), bit
                        )
                    }
                }
            }
        }.getOrElse {
            DSackT(
                MusicSack(
                    artist = android.provider.MediaStore.UNKNOWN_STRING,
                    title = FileLate(path).extension
                ),
                bitmapFailed
            )
        }
    } ?: justCoroutine {
        DSackT(
            MusicSack(
                artist = android.provider.MediaStore.UNKNOWN_STRING,
                title = FileLate(path).extension
            ), bitmapFailed
        )
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun updateMetaData(
    title: String?,
    crossinline m: suspend androidx.media3.common.MediaMetadata.Builder.() -> Unit,
) {
    withBack {
        androidx.media3.common.MediaMetadata.Builder().applySusBack {
            setIsPlayable(true)
            title?.let {
                setDisplayTitle(it)
                setTitle(it)
            }
        }.applySusBack(m)
    }
}

internal suspend inline fun android.content.Context.intiPlayerListInService(
    allMusic: MutableList<MusicSack>,
    crossinline b: suspend MutableList<androidx.media3.common.MediaItem>.() -> Unit,
) {
    withBackDef(mutableListOf()) {
        mutableListOf<@JvmSuppressWildcards androidx.media3.common.MediaItem>().applySusBack {
            if (isCarUiMode) {
                allMusic.onEachIndexedSusBack(this@intiPlayerListInService) { pos, it ->
                    androidx.media3.common.MediaItem.Builder().applySusBack {
                        it.pathAudio.toStr.letSusBack {
                            setUri(it.toUri)
                            setMediaId(pos.toStr)
                            getMusicAllDetails(it, pos = pos) {
                                setMediaMetadata(this@getMusicAllDetails.build())
                            }
                        }
                    }.build().letSusBack(::add)
                }
            } else {
                allMusic.onEachIndexedSusBack(this@intiPlayerListInService) { pos, (title, _, pathAudio, _, _, _, _, _, _, _) ->
                    androidx.media3.common.MediaItem.Builder().applySusBack {
                        pathAudio.toStr.toUri.let { u ->
                            setUri(u)
                            setMediaId(pos.toStr)
                            updateMetaData(title) {
                                setMediaMetadata(build())
                            }
                        }
                    }.build().letSusBack(::add)
                }
            }
        }
    }.letSusBack {
        this@intiPlayerListInService.nullChecker()
        b.invoke(it)
    }
}

@com.pt.common.global.WorkerAnn
internal suspend inline fun android.content.Context.getMusicAllDetails(
    path: String,
    pos: Int,
    crossinline m: suspend androidx.media3.common.MediaMetadata.Builder.() -> Unit,
) {
    withBack {
        withBackCurDef(null) {
            catchySus(null) {
                compactImageReturn(
                    com.pt.pro.R.drawable.ic_failed_song
                )?.toBitmapSus()?.toByteArray()
            }
        }.alsoSusBack { f ->
            kotlin.runCatching {
                getMusicAllTags(path = path, tryFirst = true, f)
            }.getOrElse {
                getMusicAllTags(path = path, tryFirst = false, f)
            }.applySusBack {
                androidx.media3.common.MediaMetadata.Builder().applySusBack {
                    two?.let { bit ->
                        setArtworkData(
                            bit,
                            androidx.media3.common.MediaMetadata.PICTURE_TYPE_MEDIA
                        )
                    }
                    setArtist(one.artist)
                    setAlbumArtist(one.artist)
                    setSubtitle(one.artist)
                    setDiscNumber(pos)
                    //setTheme()
                    setIsPlayable(true)
                    setDisplayTitle(one.title)
                    setTitle(one.title)
                }.applySusBack(m)
            }
        }
    }
}

internal suspend fun android.graphics.Bitmap.toByteArray(): ByteArray? = justCoroutine {
    withBackDef(null) {
        java.io.ByteArrayOutputStream().applySusBack {
            compress(android.graphics.Bitmap.CompressFormat.PNG, 100, this)
        }.toByteArray()
    }
}

