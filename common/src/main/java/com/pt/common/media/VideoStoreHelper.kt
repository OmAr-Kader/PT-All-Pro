package com.pt.common.media

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore.Video.Media.*
import com.pt.common.BuildConfig.*
import com.pt.common.global.*
import com.pt.common.stable.*

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun ContentResolver.allVideosLoader(
    path: String,
    orderBy: String,
): MutableList<MediaSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val images: MutableList<MediaSack> = mutableListOf()
            val wh = (PATH + " " + DATA_LIKE + " " + DATA_AND + " " + SIZE +
                    " " + DATA_NOT_EQUAL)
            query(
                EXTERNAL_CONTENT_URI,
                arrayOf(PATH, _ID, DISPLAY_NAME, SIZE, WIDTH, HEIGHT, DATE_MODIFIED),
                wh,
                arrayOf("%$path%", "0"),
                orderBy
            )?.useBack {
                while (moveToNext()) {
                    if (path != FileLate(curStr(PATH).toStr).parent) continue
                    MediaSack(
                        nameMedia = curStr(DISPLAY_NAME),
                        pathMedia = curStr(PATH),
                        uriMedia = (EXTERNAL_CONTENT_URI.toStr + "/" + curInt(_ID)),
                        isImage = false,
                        mediaSize = curLong(SIZE),
                        mediaWidth = curInt(WIDTH),
                        mediaHigh = curInt(HEIGHT),
                        dateModified = curLong(DATE_MODIFIED).createTime
                    ).alsoSusBack(images::add)
                }
            }
            return@runCatching images
        }.getOrDefault(mutableListOf())
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun ContentResolver.loadAllVideo(
    orderBy: String
): MutableList<com.pt.common.objects.MediaDuo> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val mediaDuo: MutableList<com.pt.common.objects.MediaDuo> = mutableListOf()
            val picPaths: MutableList<String> = mutableListOf()

            query(
                EXTERNAL_CONTENT_URI,
                arrayOf(PATH, _ID, DISPLAY_NAME, TITLE, SIZE, WIDTH, HEIGHT, DATE_MODIFIED),
                null,
                null,
                orderBy
            )?.useBack {
                while (moveToNext()) {
                    MediaSack(
                        nameMedia = curStr(DISPLAY_NAME),
                        pathMedia = curStr(PATH),
                        uriMedia = (EXTERNAL_CONTENT_URI.toStr + "/" + curInt(_ID)),
                        isImage = false,
                        mediaSize = curLong(SIZE),
                        mediaWidth = curInt(WIDTH),
                        mediaHigh = curInt(HEIGHT),
                        dateModified = curLong(DATE_MODIFIED).createTime,
                    ).letSusBack { itM ->
                        FileLate(curStr(PATH).toStr).letSusBack { itF ->
                            itF.parent?.toStr.letSusBack { itP ->
                                if (!picPaths.contains(itP)) {
                                    if (itF.exists() && itF.canRead()) {
                                        picPaths.add(itP.toStr)
                                        itF.parentFile?.name.toStr.letSusBack { iN ->
                                            if (iN != "0") iN else ROOT_NAME
                                        }.letSusBack { name ->
                                            com.pt.common.objects.MediaDuo(
                                                itP,
                                                name,
                                                1,
                                                false,
                                                mutableListOf()
                                            ).letSusBack {
                                                it.mediaHolder.add(itM)
                                                mediaDuo.add(it)
                                            }
                                        }
                                    }
                                } else {
                                    mediaDuo.forEach {
                                        if (it.path == itP) {
                                            it.addPics()
                                            it.mediaHolder.add(itM)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return@runCatching mediaDuo
        }.getOrDefault(mutableListOf())
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun ContentResolver.loadAllVideoTime(
    orderBy: String
): MutableList<MediaSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val mediaHolder: MutableList<MediaSack> = mutableListOf()
            query(
                EXTERNAL_CONTENT_URI,
                arrayOf(PATH, _ID, DISPLAY_NAME, TITLE, SIZE, WIDTH, HEIGHT, DATE_MODIFIED),
                null,
                null,
                orderBy
            )?.useBack {
                while (moveToNext()) {
                    MediaSack(
                        nameMedia = curStr(DISPLAY_NAME),
                        pathMedia = curStr(PATH),
                        uriMedia = (EXTERNAL_CONTENT_URI.toStr + "/" + curInt(_ID)),
                        isImage = false,
                        mediaSize = curLong(SIZE),
                        mediaWidth = curInt(WIDTH),
                        mediaHigh = curInt(HEIGHT),
                        dateModified = curLong(DATE_MODIFIED).createTime,
                    ).letSusBack {
                        mediaHolder.add(it)
                    }
                }
            }
            return@runCatching mediaHolder
        }.getOrDefault(mutableListOf())
    }
}


@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun ContentResolver.loadAllDisplayVideoTime(
    orderBy: String,
    fromTime: String,
    toTime: String,
): MutableList<MediaSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val mediaHolder: MutableList<MediaSack> = mutableListOf()
            val wh = (DATE_MODIFIED + " " + DATA_MORE + " " + DATA_AND + " " +
                    DATE_MODIFIED + " " + DATA_LESS)
            query(
                EXTERNAL_CONTENT_URI,
                arrayOf(PATH, _ID, DISPLAY_NAME, TITLE, SIZE, WIDTH, HEIGHT, DATE_MODIFIED),
                wh,
                arrayOf(fromTime, toTime),
                orderBy
            )?.useBack {
                while (moveToNext()) {
                    MediaSack(
                        nameMedia = curStr(DISPLAY_NAME),
                        pathMedia = curStr(PATH),
                        uriMedia = (EXTERNAL_CONTENT_URI.toStr + "/" + curInt(_ID)),
                        isImage = false,
                        mediaSize = curLong(SIZE),
                        mediaWidth = curInt(WIDTH),
                        mediaHigh = curInt(HEIGHT),
                        dateModified = curLong(DATE_MODIFIED).createTime,
                    ).letSusBack {
                        mediaHolder.add(it)
                    }
                }
            }
            return@runCatching mediaHolder
        }.getOrDefault(mutableListOf())
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun ContentResolver.videoFoldersLoader(
    orderBy: String
): MutableList<MediaFolderSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val picFolders: MutableList<MediaFolderSack> = mutableListOf()
            val picPaths: MutableList<String> = mutableListOf()

            query(EXTERNAL_CONTENT_URI, arrayOf(PATH), null, null, orderBy)?.useBack {
                while (moveToNext()) {
                    FileLate(curStr(PATH).toStr).letSusBack { itF ->
                        itF.parent?.toStr.toStr.letSusBack { itP ->
                            if (!picPaths.contains(itP)) {
                                if (itF.exists() && itF.canRead()) {
                                    picPaths.add(itP)
                                    itF.parentFile?.name.toStr.run {
                                        if (this != "0") this else ROOT_NAME
                                    }.letSusBack { name ->
                                        MediaFolderSack(
                                            path = itP,
                                            folderName = name,
                                            numberOfPics = 1,
                                            firstImagePath = curStr(PATH)
                                        ).alsoSusBack(picFolders::add)
                                    }
                                }
                            } else {
                                picFolders.forEach {
                                    if (it.path == itP) it.addPics()
                                }
                            }
                        }
                    }
                }
            }
            return@runCatching picFolders
        }.getOrDefault(mutableListOf())
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.getVideoPathFromURI(contentUri: Uri): String? = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(null) {
        runCatching {
            contentResolver.query(contentUri, arrayOf(PATH), null, null, null)?.useBack {
                if (moveToFirst()) {
                    return@useBack curStr(PATH)
                } else {
                    return@useBack null
                }
            }
        }.getOrNull() ?: getPath(contentUri)
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun ContentResolver.checkVideoUri(videoPath: String): Uri? = justCoroutineCur {
    runCatching {
        return@runCatching withBackCurDef(null) {
            query(
                EXTERNAL_CONTENT_URI,
                arrayOf(_ID),
                PATH.toStr + " " + DATA_EQUAL,
                arrayOf(videoPath),
                null
            )?.useBack {
                if (moveToFirst()) {
                    return@useBack Uri.withAppendedPath(EXTERNAL_CONTENT_URI, "" + curInt(_ID))
                } else {
                    return@useBack null
                }
            }
        }
    }.getOrNull()
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun ContentResolver.mediaStoreVideo(pic: MediaSack?, fromPath: String?, delete: Uri?) {
    justCoroutineCur {
        runCatching {
            withBackCurDef(-1) {
                if (fromPath != null) {
                    ContentValues().applySusBack {
                        put(TITLE, (pic ?: return@applySusBack).nameMedia)
                        put(DESCRIPTION, pic.nameMedia)
                        put(DISPLAY_NAME, pic.nameMedia)
                        put(WIDTH, pic.mediaWidth)
                        put(HEIGHT, pic.mediaHigh)
                        put(SIZE, pic.mediaSize)
                        put(DATE_ADDED, System.currentTimeMillis())
                        put(DATE_MODIFIED, System.currentTimeMillis())
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            put(DATE_TAKEN, System.currentTimeMillis())
                            put(BUCKET_DISPLAY_NAME, pic.nameMedia)
                        }
                        put(PATH, fromPath)
                    }.alsoSusBack {
                        insert(EXTERNAL_CONTENT_URI, it)
                    }
                }
            }
            withBackCurDef(-1) {
                if (delete != null) {
                    delete(delete, null, null)
                }
            }
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
fun ContentResolver.mediaVideoStore(pic: MediaSack?, fromPath: String?) {
    runCatching {
        if (fromPath != null) {
            ContentValues().apply {
                put(TITLE, (pic ?: return@apply).nameMedia)
                put(DESCRIPTION, pic.nameMedia)
                put(DISPLAY_NAME, pic.nameMedia)
                put(WIDTH, pic.mediaWidth)
                put(HEIGHT, pic.mediaHigh)
                put(SIZE, pic.mediaSize)
                put(DATE_ADDED, System.currentTimeMillis())
                put(DATE_MODIFIED, System.currentTimeMillis())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(DATE_TAKEN, System.currentTimeMillis())
                    put(BUCKET_DISPLAY_NAME, pic.nameMedia)
                }
                put(PATH, fromPath)
            }.also {
                insert(EXTERNAL_CONTENT_URI, it)
            }
        }
    }
}
