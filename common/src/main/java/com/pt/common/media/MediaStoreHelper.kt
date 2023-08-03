package com.pt.common.media

import android.provider.MediaStore.Files.FileColumns.*
import com.pt.common.BuildConfig.*
import com.pt.common.global.toStr
import com.pt.common.global.toUri
import com.pt.common.stable.*

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.uriProvider(
    filePath: String?,
    pac: String
): android.net.Uri = justCoroutineCur {
    withBackDef(filePath.toStr.toUri) {
        return@withBackDef androidx.core.content.FileProvider.getUriForFile(
            this@uriProvider,
            pac + FILE_PROVIDER,
            com.pt.common.global.FileLate(filePath.toStr)
        )
    }
}

inline val android.content.Context.uriProviderNormal: (String?, String) -> android.net.Uri
    get() = { it, pac ->
        androidx.core.content.FileProvider.getUriForFile(
            this@uriProviderNormal,
            pac + FILE_PROVIDER,
            com.pt.common.global.FileLate(it.toStr)
        )
    }

inline val android.content.Intent.isPickIntent: Boolean
    get() {
        return (action == android.content.Intent.ACTION_PICK || action == android.content.Intent.ACTION_GET_CONTENT) &&
                (data == android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI ||
                        data == android.provider.MediaStore.Video.Media.INTERNAL_CONTENT_URI ||
                        type == IMAGE_SENDER || type == android.provider.MediaStore.Images.Media.CONTENT_TYPE ||
                        type == VIDEO_SENDER || type == android.provider.MediaStore.Video.Media.CONTENT_TYPE ||
                        data.toStr.contains(IMAGE_PICKER.toRegex()) ||
                        type.toStr.contains(IMAGE_PICKER.toRegex()) ||
                        data.toStr.contains(VIDEO_PICKER.toRegex()) ||
                        type.toStr.contains(VIDEO_PICKER.toRegex()))
    }

inline val android.content.Intent.isImageViewIntent: Boolean
    get() {
        return action == android.content.Intent.ACTION_VIEW &&
                (data.toStr.contains(IMAGE_PICKER.toRegex())
                        || type.toStr.contains(IMAGE_PICKER.toRegex()))
    }

inline val android.content.Intent.isVideoViewIntent: Boolean
    get() {
        return action == android.content.Intent.ACTION_VIEW &&
                (data.toStr.contains(VIDEO_PICKER.toRegex()) ||
                        type.toStr.contains(VIDEO_PICKER.toRegex()))
    }

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.allFoldersLoader(
    orderBy: String,
): MutableList<com.pt.common.global.MediaFolderSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val picFolders: MutableList<com.pt.common.global.MediaFolderSack> = mutableListOf()
            val picPaths: MutableList<String> = mutableListOf()
            val wh = (MEDIA_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                    MEDIA_TYPE + " " + DATA_EQUAL)
            query(
                android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL),
                arrayOf(PATH),
                wh,
                arrayOf(MEDIA_TYPE_IMAGE.toStr, MEDIA_TYPE_VIDEO.toStr),
                orderBy
            )?.useBack {
                while (moveToNext()) {
                    com.pt.common.global.FileLate(curStr(PATH).toStr).letSusBack { itF ->
                        itF.parent?.toStr.toStr.letSusBack { itP ->
                            if (!picPaths.contains(itP)) {
                                if (itF.exists() && itF.canRead()) {
                                    picPaths.add(itP)
                                    itF.parentFile?.name.toStr.run {
                                        return@run if (this != "0") this else "Internal"
                                    }.letSusBack { name ->
                                        com.pt.common.global.MediaFolderSack(
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
suspend fun android.content.ContentResolver.loadAllMedia(
    orderBy: String,
): MutableList<com.pt.common.objects.MediaDuo> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val mediaDuo: MutableList<com.pt.common.objects.MediaDuo> = mutableListOf()
            val picPaths: MutableList<String> = mutableListOf()

            val projection = arrayOf(
                PATH,
                _ID,
                DISPLAY_NAME,
                TITLE,
                SIZE,
                WIDTH,
                HEIGHT,
                DATE_MODIFIED,
                MEDIA_TYPE
            )
            val wh = (MEDIA_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                    MEDIA_TYPE + " " + DATA_EQUAL)
            query(
                android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL),
                projection,
                wh,
                arrayOf(MEDIA_TYPE_IMAGE.toStr, MEDIA_TYPE_VIDEO.toStr),
                orderBy
            )?.useBack {
                while (moveToNext()) {
                    (curInt(MEDIA_TYPE) == MEDIA_TYPE_IMAGE).letSusBack { itIm ->
                        if (itIm) {
                            android.net.Uri.withAppendedPath(
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "" + curInt(_ID)
                            )
                        } else {
                            android.net.Uri.withAppendedPath(
                                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                "" + curInt(_ID)
                            )
                        }.letSusBack { itU ->
                            com.pt.common.global.MediaSack(
                                nameMedia = curStr(DISPLAY_NAME),
                                pathMedia = curStr(PATH),
                                uriMedia = itU.toStr,
                                isImage = itIm,
                                mediaSize = curLong(SIZE),
                                mediaWidth = curInt(WIDTH),
                                mediaHigh = curInt(HEIGHT),
                                dateModified = curLong(DATE_MODIFIED).createTime,
                            ).letSusBack { itM ->
                                com.pt.common.global.FileLate(
                                    curStr(PATH).toStr
                                ).letSusBack { itF ->
                                    itF.parent?.toStr.toStr.letSusBack { itP ->
                                        if (!picPaths.contains(itP)) {
                                            if (itF.exists() && itF.canRead()) {
                                                picPaths.add(itP)
                                                itF.parentFile?.name.run {
                                                    if (this != "0") this else "Internal"
                                                }.letSusBack { name ->
                                                    com.pt.common.objects.MediaDuo(
                                                        path = itP,
                                                        folderName = name,
                                                        numberOfPics = 1,
                                                        isFav = false,
                                                        mediaHolder = mutableListOf()
                                                    ).letSusBack { itMD ->
                                                        itMD.mediaHolder.add(itM)
                                                        mediaDuo.add(itMD)
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
                }
            }
            return@runCatching mediaDuo
        }.getOrDefault(mutableListOf())
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.loadAllMediaSacks(
    orderBy: String,
): MutableList<com.pt.common.global.MediaSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val mediaHolder: MutableList<com.pt.common.global.MediaSack> = mutableListOf()
            val projection = arrayOf(
                PATH,
                _ID,
                DISPLAY_NAME,
                TITLE,
                SIZE,
                WIDTH,
                HEIGHT,
                DATE_MODIFIED,
                MEDIA_TYPE
            )
            val wh = (MEDIA_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                    MEDIA_TYPE + " " + DATA_EQUAL)
            query(
                android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL),
                projection,
                wh,
                arrayOf(MEDIA_TYPE_IMAGE.toStr, MEDIA_TYPE_VIDEO.toStr),
                orderBy
            )?.useBack {
                while (moveToNext()) {
                    (curInt(MEDIA_TYPE) == MEDIA_TYPE_IMAGE).letSusBack { itIm ->
                        if (itIm) {
                            android.net.Uri.withAppendedPath(
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "" + curLong(_ID)
                            )
                        } else {
                            android.net.Uri.withAppendedPath(
                                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                "" + curLong(_ID)
                            )
                        }.letSusBack { itU ->
                            com.pt.common.global.MediaSack(
                                nameMedia = curStr(DISPLAY_NAME),
                                pathMedia = curStr(PATH),
                                uriMedia = itU.toStr,
                                isImage = itIm,
                                mediaSize = curLong(SIZE),
                                mediaWidth = curInt(WIDTH),
                                mediaHigh = curInt(HEIGHT),
                                dateModified = curLong(DATE_MODIFIED).createTime,
                            ).alsoSusBack(mediaHolder::add)
                        }
                    }
                }
            }
            return@runCatching mediaHolder
        }.getOrDefault(mutableListOf())
    }
}


@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.loadAllMediaSacksTime(
    orderBy: String,
    fromTime: String,
    toTime: String,
): MutableList<com.pt.common.global.MediaSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val mediaHolder: MutableList<com.pt.common.global.MediaSack> = mutableListOf()
            val projection = arrayOf(
                PATH,
                _ID,
                DISPLAY_NAME,
                TITLE,
                SIZE,
                WIDTH,
                HEIGHT,
                DATE_MODIFIED,
                MEDIA_TYPE,
            )
            val wh = (DATE_MODIFIED + " " + DATA_MORE + " " + DATA_AND + " " +
                    DATE_MODIFIED + " " + DATA_LESS)
            query(
                android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL),
                projection,
                wh,
                arrayOf(fromTime, toTime),
                orderBy
            )?.useBack {
                while (moveToNext()) {
                    curInt(MEDIA_TYPE).letSusBack<Int, Unit> { itIm ->
                        if (itIm == MEDIA_TYPE_IMAGE) {
                            android.net.Uri.withAppendedPath(
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "" + curLong(_ID)
                            ).letSusBack { itU ->
                                com.pt.common.global.MediaSack(
                                    nameMedia = curStr(DISPLAY_NAME),
                                    pathMedia = curStr(PATH),
                                    uriMedia = itU.toStr,
                                    isImage = true,
                                    mediaSize = curLong(SIZE),
                                    mediaWidth = curInt(WIDTH),
                                    mediaHigh = curInt(HEIGHT),
                                    dateModified = curLong(DATE_MODIFIED).createTime,
                                ).alsoSusBack(mediaHolder::add)
                            }
                        } else if (itIm == MEDIA_TYPE_VIDEO) {
                            android.net.Uri.withAppendedPath(
                                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                "" + curLong(_ID)
                            ).letSusBack { itU ->
                                com.pt.common.global.MediaSack(
                                    nameMedia = curStr(DISPLAY_NAME),
                                    pathMedia = curStr(PATH),
                                    uriMedia = itU.toStr,
                                    isImage = false,
                                    mediaSize = curLong(SIZE),
                                    mediaWidth = curInt(WIDTH),
                                    mediaHigh = curInt(HEIGHT),
                                    dateModified = curLong(DATE_MODIFIED).createTime,
                                ).alsoSusBack(mediaHolder::add)
                            }
                        }
                    }
                }
            }

            return@runCatching mediaHolder
        }.getOrDefault(mutableListOf())
    }
}

suspend fun android.content.Context.setAsUseServices(
    deleteCopy: com.pt.common.global.MediaSack,
    s: String,
    pac: String,
    intent: suspend android.content.Intent.() -> Unit
) {
    withDefault {
        android.content.Intent(android.content.Intent.ACTION_ATTACH_DATA).apply {
            addCategory(android.content.Intent.CATEGORY_DEFAULT)
            deleteCopy.uriMedia.letSusBack { uriStr ->
                uriStr?.toUri ?: uriProviderNormal(deleteCopy.pathMedia, pac)
            }.letSusBack { itU ->
                if (deleteCopy.isImage) {
                    setDataAndType(itU, IMAGE_SENDER)
                    putExtra("mimeType", IMAGE_SENDER)
                } else {
                    setDataAndType(itU, VIDEO_SENDER)
                    putExtra("mimeType", VIDEO_SENDER)
                }
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                //putExtra("save_path", itU)
                putExtra(android.content.Intent.EXTRA_STREAM, itU)
            }
        }.alsoSusBack {
            android.content.Intent.createChooser(
                it,
                s
            ).alsoSusBack(intent)
        }
    }
}

@Suppress("DEPRECATION")
@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.mediaBroadCast(fromUri: String?, toPath: String?) {
    withBack {
        runCatching {
            android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                data = android.net.Uri.fromFile(com.pt.common.global.FileLate(toPath.toStr))
                sendBroadcast(this)
            }
            android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                data = android.net.Uri.fromFile(com.pt.common.global.FileLate(fromUri.toStr))
                sendBroadcast(this)
            }
            android.media.MediaScannerConnection.scanFile(
                this@mediaBroadCast,
                arrayOf(fromUri.toStr),
                null,
                null
            )
            android.media.MediaScannerConnection.scanFile(
                this@mediaBroadCast,
                arrayOf(toPath.toStr),
                null,
                null
            )
        }
    }
}

@Suppress("DEPRECATION")
@com.pt.common.global.WorkerAnn
fun android.content.Context.callBroadCastInsert(fromPath: String?) {
    runCatching {
        android.content.Intent(android.content.Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
            data = android.net.Uri.fromFile(com.pt.common.global.FileLate(fromPath.toStr))
            sendBroadcast(this)
        }
        android.media.MediaScannerConnection.scanFile(
            this@callBroadCastInsert,
            arrayOf(fromPath.toStr),
            null,
            null
        )
        return@runCatching
    }
}

@com.pt.common.global.WorkerAnn
suspend fun MutableList<com.pt.common.global.MediaSack>.orderAllLoaders(
    orderBy: String,
): MutableList<com.pt.common.global.MediaSack> = justCoroutine {
    return@justCoroutine withBackDef(mutableListOf()) {
        return@withBackDef when (orderBy) {
            BY_DATE_DESC, BY_DATE_FOLDER_DESC -> {
                sortedByDescending {
                    it.dateModified
                }.toMutableList()
            }
            BY_DATE_ASC, BY_DATE_FOLDER_ASC -> {
                sortedBy {
                    it.dateModified
                }.toMutableList()
            }
            BY_NAME_ASC -> {
                sortedWith(
                    compareBy(String.CASE_INSENSITIVE_ORDER) { it.nameMedia.toStr }
                ).toMutableList()
            }
            BY_NAME_DESC -> {
                sortedWith(
                    compareBy(String.CASE_INSENSITIVE_ORDER) { it.nameMedia.toStr }
                ).reversed().toMutableList()
            }
            BY_SIZE_ASC -> {
                sortedBy {
                    it.mediaSize
                }.toMutableList()
            }
            BY_SIZE_DESC -> {
                sortedByDescending {
                    it.mediaSize
                }.toMutableList()
            }
            else -> {
                sortedBy {
                    it.dateModified
                }.toMutableList()
            }
        }
    }
}


inline val Int.findResole: Int
    get() {
        return when (this) {
            1 -> 700
            2 -> 400
            3 -> 300
            4 -> 300
            5 -> 300
            6 -> 250
            7 -> 250
            8 -> 200
            9 -> 200
            10 -> 150
            11 -> 150
            12 -> 100
            else -> 400
        }
    }

inline val Int.findHighResole: Int
    get() {
        return when (this) {
            1 -> 1500
            2 -> 1200
            3 -> 1000
            4 -> 800
            5 -> 700
            6 -> 700
            7 -> 600
            8 -> 600
            9 -> 500
            10 -> 500
            11 -> 400
            12 -> 300
            else -> 500
        }
    }

suspend fun android.content.Context.shareMedia(
    deleteCopy: MutableList<com.pt.common.global.MediaSack>,
    pac: String,
    s: String,
    intent: suspend android.content.Intent.() -> Unit,
) {
    withDefault {
        mutableListOf<android.net.Uri>().applySusBack {
            deleteCopy.forEach {
                if (it.uriMedia != null) {
                    this@applySusBack.add(it.uriMedia.toUri)
                } else {
                    this@applySusBack.add(uriProvider(it.pathMedia.toStr, pac))
                }
            }
        }.letSusBack { files ->
            android.content.ClipData(
                "Share",
                arrayOf(if (deleteCopy.firstOrNull()?.isImage == true) IMAGE_SENDER else VIDEO_SENDER),
                android.content.ClipData.Item(files.firstOrNull())
            ).apply { files.forEach { addItem(android.content.ClipData.Item(it)) } }.let { cl ->
                android.content.Intent(
                    if (deleteCopy.size == 1)
                        android.content.Intent.ACTION_SEND
                    else
                        android.content.Intent.ACTION_SEND_MULTIPLE
                ).applySusBack {
                    flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                            android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    type = if (deleteCopy.firstOrNull()?.isImage == true) IMAGE_SENDER else VIDEO_SENDER
                    clipData = cl
                    if (deleteCopy.size == 1)
                        putExtra(android.content.Intent.EXTRA_STREAM, files[0])
                    else
                        putParcelableArrayListExtra(
                            android.content.Intent.EXTRA_STREAM,
                            ArrayList(files)
                        )
                }.alsoSusBack {
                    android.content.Intent.createChooser(
                        it,
                        s
                    ).alsoSusBack(intent)
                }
            }
        }
    }
}

suspend fun Array<out @JvmSuppressWildcards java.io.File>.toFileList(
): MutableList<java.io.File> = justCoroutineCur {
    return@justCoroutineCur ArrayList(this@toFileList.asCollection())
}

internal suspend fun Array<out java.io.File>.asCollection(
): Collection<java.io.File> = justCoroutineCur { ArrayAsCollection(this@asCollection) }

internal class ArrayAsCollection(
    private val values: Array<out @JvmSuppressWildcards java.io.File>,
) : Collection<java.io.File> {
    override val size: Int get() = values.size
    override fun isEmpty(): Boolean = values.isEmpty()
    override fun contains(element: java.io.File): Boolean = values.contains(element)
    override fun containsAll(elements: Collection<java.io.File>): Boolean = elements.all {
        contains(it)
    }

    override fun iterator(): Iterator<java.io.File> = values.iterator()
}
