package com.pt.common.media

import android.provider.MediaStore.Files.FileColumns.*
import com.pt.common.BuildConfig.*
import com.pt.common.global.*
import com.pt.common.global.FileLate
import com.pt.common.stable.*


suspend fun android.content.ContentResolver?.getListFiles(
    it: FileLate, s: suspend (Long) -> Unit,
) {
    if (it.exists()) {
        if (isV_R) {
            this?.getListFiles30(it, s)
        } else {
            getListFilesNot30(it, s)
        }
    }
}

suspend fun android.content.ContentResolver?.getListFilesNot30(
    it: FileLate, s: suspend (Long) -> Unit,
) {
    withBackCurDef(-1) {
        runCatching {
            var fileS = 0L
            fileS += it.length()
            it.listFiles()?.toFileList()?.onEachSusBack(this@getListFilesNot30) {
                if (isDirectory) {
                    getListFiles(this) {
                        @Strictfp
                        fileS += it
                    }
                } else {
                    @Strictfp
                    fileS += length()
                }
            }
            s(fileS)
        }.getOrDefault(0)
    }
}

@APIAnn(android.os.Build.VERSION_CODES.R)
suspend fun android.content.ContentResolver.getListFiles30(
    it: FileLate, s: suspend (Long) -> Unit,
) {
    withBackCurDef(0L) {
        var size: Long = 0
        query(
            android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL), arrayOf(
                SIZE
            ),
            PATH.toStr + " " + DATA_LIKE,
            arrayOf("%${it.absolutePath}%"),
            null
        )?.useBack {
            while (moveToNext()) {
                size += curLong(SIZE)
            }
        }
        size
    }.letSusBack(s)
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.getMangerFiles(
    parentDir: FileLate,
): MutableList<FileSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        val dossiers: MutableList<FileSack> = mutableListOf()
        parentDir.listFiles()?.toFileList()?.onEachSusBack(this@getMangerFiles) {
            if (this@onEachSusBack.isDirectory) {
                this@onEachSusBack.fetchFileName.letSusBack { na ->
                    FileSack(
                        fileName = na,
                        filePath = this@onEachSusBack.absolutePath,
                        fileUri = null,
                        fileSize = this@onEachSusBack.length(),
                        typeFile = FOLDER,
                        dateModified = this@onEachSusBack.lastModified(),
                    ).alsoSusBack(dossiers::add)
                }
            } else {
                this@onEachSusBack.fetchFileName.letSusBack { na ->
                    FileSack(
                        fileName = na,
                        filePath = this@onEachSusBack.absolutePath,
                        fileUri = null,
                        fileSize = this@onEachSusBack.length(),
                        typeFile = this@onEachSusBack.extension.trim().findType,
                        dateModified = this@onEachSusBack.lastModified(),
                    ).alsoSusBack(dossiers::add)
                }
            }
        }
        return@withBackCurDef dossiers
    }
}

suspend fun android.content.Context.getStorageDirectories(): MutableList<String> = justCoroutine {
    withBackDef(mutableListOf()) {
        val rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE")
        val results: MutableList<String> = ArrayList()
        val externalDirs: Array<FileLate> = getExternalFilesDirs(null)
        externalDirs.onEach { file ->
            val path: String = file.path.split("/Android")[0]
            if (android.os.Environment.isExternalStorageRemovable(
                    file
                ) || rawSecondaryStoragesStr != null && rawSecondaryStoragesStr.contains(path)
            ) {
                results.add(path)
            }
        }
        results.toTypedArray().toMutableList()
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.fileDeleter(
    realPath: String, type: Int, uri: android.net.Uri? = null,
): Boolean = justCoroutineCur {
    withBackCurDef(false) {
        runCatching {
            (uri ?: DSackT(contentResolver.checkFileID(realPath), type).margeUri).letSusBack {
                if (it != null) {
                    contentResolver.delete(it, null, null)
                    callBroadCastInsert(realPath)
                    true
                } else {
                    false
                }
            }
        }
        return@withBackCurDef runCatching {
            return@runCatching FileLate(realPath).delete()
        }.getOrDefault(false)
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.allFilesLoader(
    path: String,
): MutableList<FileSack> = justCoroutineCur {
    if (isV_R) {
        allFilesLoader30(path)
    } else {
        allFilesLoaderNot31(path)
    }
}


@APIAnn(android.os.Build.VERSION_CODES.R)
@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.allFilesLoader30(
    path: String,
): MutableList<FileSack> = justCoroutineCur {
    val uri = android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL)
    val dossiers: MutableList<FileSack> = mutableListOf()
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        query(
            uri,
            arrayOf(PATH, _ID, DATE_MODIFIED, DATE_ADDED, DATE_TAKEN, MEDIA_TYPE, SIZE),
            PATH.toStr + " " + DATA_LIKE,
            arrayOf("%$path%"),
            null
        )?.useBack {
            while (moveToNext()) {
                FileLate(curStr(PATH).toStr).runSusBack {
                    fetchFileName.letSusBack { n ->
                        if (parent == path && !n.checkHiddenMedia) {
                            curInt(MEDIA_TYPE).letSusBack { type ->
                                if (type == 2 || type == 1 || type == 3) {
                                    FileSack(
                                        fileName = n,
                                        filePath = absolutePath,
                                        fileUri = ("$uri/" + curInt(_ID)),
                                        fileSize = curLong(SIZE),
                                        typeFile = type,
                                        dateModified = curLongTime(this@runSusBack),
                                    )
                                } else {
                                    if (isDirectory) {
                                        FileSack(
                                            fileName = n,
                                            filePath = absolutePath,
                                            fileUri = ("$uri/" + curInt(_ID)),
                                            fileSize = curLong(SIZE),
                                            typeFile = FOLDER,
                                            dateModified = curLongTime(this@runSusBack),
                                        )
                                    } else {
                                        FileSack(
                                            fileName = n,
                                            filePath = absolutePath,
                                            fileUri = ("$uri/" + curInt(_ID)),
                                            fileSize = curLong(SIZE),
                                            typeFile = extension.trim().findType,
                                            dateModified = curLongTime(this@runSusBack),
                                        )
                                    }
                                }.alsoSusBack(dossiers::add)
                            }
                        }
                    }
                }
            }
            return@useBack dossiers
        } ?: mutableListOf()
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.allFilesLoaderNot31(
    path: String,
): MutableList<FileSack> = justCoroutineCur {
    val uri = android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL)
    val dossiers: MutableList<FileSack> = mutableListOf()
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        query(
            uri,
            arrayOf(PATH, _ID, DATE_MODIFIED, DATE_ADDED, MEDIA_TYPE, SIZE),
            PATH.toStr + " " + DATA_LIKE,
            arrayOf("%$path%"),
            null
        )?.useBack {
            while (moveToNext()) {
                FileLate(curStr(PATH).toStr).runSusBack {
                    if (parent == path) {
                        curInt(MEDIA_TYPE).letSusBack { type ->
                            if (type == 2 || type == 1 || type == 3) {
                                FileSack(
                                    fileName = fetchFileName,
                                    filePath = absolutePath,
                                    fileUri = ("$uri/" + curInt(_ID)),
                                    fileSize = curLong(SIZE),
                                    typeFile = type,
                                    dateModified = curLongTime(this@runSusBack),
                                )
                            } else {
                                if (isDirectory) {
                                    FileSack(
                                        fileName = fetchFileName,
                                        filePath = absolutePath,
                                        fileUri = ("$uri/" + curInt(_ID)),
                                        fileSize = curLong(SIZE),
                                        typeFile = FOLDER,
                                        dateModified = curLongTime(this@runSusBack),
                                    )
                                } else {
                                    FileSack(
                                        fileName = fetchFileName,
                                        filePath = absolutePath,
                                        fileUri = ("$uri/" + curInt(_ID)),
                                        fileSize = curLong(SIZE),
                                        typeFile = extension.trim().findType,
                                        dateModified = curLongTime(this@runSusBack),
                                    )
                                }
                            }.alsoSusBack(dossiers::add)
                        }
                    }
                }
            }
            return@useBack dossiers
        } ?: mutableListOf()
    }
}

inline val FileLate.getFileType: String?
    @com.pt.common.global.CurAnn
    @com.pt.common.global.WorkerAnn
    get() = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.getFileUri(
    filePath: String,
    typeFile: Int,
    pac: String
): android.net.Uri = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(uriProviderNormal(filePath, pac)) {
        contentResolver.checkFileID(filePath).runSusBack {
            DSackT(this, typeFile).margeUri
        }.letSusBack {
            it ?: uriProviderNormal(filePath, pac)
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.checkFileID(
    filePath: String,
): Int? = justCoroutineCur {
    withBackCurDef(null) {
        runCatching {
            return@runCatching query(
                android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL),
                arrayOf(_ID),
                PATH.toStr + " " + DATA_EQUAL,
                arrayOf(filePath),
                null
            )?.useBack {
                if (moveToFirst()) {
                    return@useBack curInt(_ID)
                } else {
                    return@useBack null
                }
            }
        }.getOrNull()
    }
}

inline val DSackT<Int?, Int>.margeUri: android.net.Uri?
    @com.pt.common.global.CurAnn
    @com.pt.common.global.WorkerAnn
    get() {
        return if (one != null) {
            when (two) {
                IMAGE -> {
                    android.net.Uri.withAppendedPath(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "" + one
                    )
                }
                VIDEO -> {
                    android.net.Uri.withAppendedPath(
                        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        "" + one
                    )
                }
                AUDIO -> {
                    android.net.Uri.withAppendedPath(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        "" + one
                    )
                }
                else -> {
                    android.net.Uri.withAppendedPath(
                        android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL),
                        "" + one
                    )
                }
            }
        } else {
            null
        }
    }

@Suppress("HardCodedStringLiteral")
inline val Int.findArg: Array<String>
    @com.pt.common.global.CurAnn
    @com.pt.common.global.WorkerAnn
    get() {
        return android.webkit.MimeTypeMap.getSingleton().run {
            return@run when (this@findArg) {
                CATO_DOCUMENT -> {
                    arrayOf(
                        getMimeTypeFromExtension("pdf").toStr,
                        getMimeTypeFromExtension("doc").toStr,
                        getMimeTypeFromExtension("docx").toStr,
                        getMimeTypeFromExtension("xls").toStr,
                        getMimeTypeFromExtension("xlsx").toStr,
                        getMimeTypeFromExtension("ppt").toStr,
                        getMimeTypeFromExtension("pptx").toStr,
                        getMimeTypeFromExtension("txt").toStr
                    )
                }
                CATO_ARCHIVES -> {
                    arrayOf(
                        getMimeTypeFromExtension("rar").toStr,
                        getMimeTypeFromExtension("zip").toStr,
                        getMimeTypeFromExtension("jar").toStr
                    )
                }
                else -> {
                    arrayOf()
                }
            }
        }
    }

inline val Int.findTypes: String
    @com.pt.common.global.CurAnn
    @com.pt.common.global.WorkerAnn
    get() {
        return when (this) {
            CATO_DOCUMENT -> {
                (MIME_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                        MIME_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                        MIME_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                        MIME_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                        MIME_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                        MIME_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                        MIME_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                        MIME_TYPE + " " + DATA_EQUAL)
            }
            CATO_ARCHIVES -> {
                (MIME_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                        MIME_TYPE + " " + DATA_EQUAL + " " + DATA_OR + " " +
                        MIME_TYPE + " " + DATA_EQUAL)
            }
            else -> {
                ""
            }
        }
    }

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.findCategory(
    cato: Int,
): MutableList<FileSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        when (cato) {
            CATO_IMAGE -> {
                loadForCatoType(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE
                )
            }
            CATO_VIDEO -> {
                loadForCatoType(
                    android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    VIDEO
                )
            }
            CATO_AUDIO -> {
                loadForCatoType(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    AUDIO
                )
            }
            else -> {
                loadForCato(
                    cato.findTypes,
                    cato.findArg,
                    android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL)
                )
            }
        }
    }
}


@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.loadForCatoType(
    uriType: android.net.Uri,
    cato: Int,
): MutableList<FileSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            return@runCatching mutableListOf<FileSack>().applySusBack {
                query(
                    uriType,
                    arrayOf(PATH, _ID, DATE_MODIFIED, DATE_ADDED, TITLE, SIZE),
                    null,
                    null,
                    null
                )?.useBack {
                    while (moveToNext()) {
                        android.net.Uri.withAppendedPath(
                            uriType,
                            "" + curInt(_ID)
                        ).letSusBack { uri ->
                            curStr(TITLE)?.letSusBack { itT ->
                                curStr(PATH)?.letSusBack { itP ->
                                    FileSack(
                                        fileName = itT,
                                        filePath = itP,
                                        fileUri = uri.toStr,
                                        fileSize = curLong(SIZE),
                                        typeFile = cato,
                                        dateModified = curLong(DATE_MODIFIED).createTime,
                                    ).alsoSusBack(::add)
                                }
                            }
                        }
                    }
                }
            }
        }.getOrDefault(mutableListOf())
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.loadForCato(
    select: String,
    arg: Array<String>,
    uriType: android.net.Uri,
): MutableList<FileSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        return@withBackCurDef runCatching {
            val dossiers: MutableList<FileSack> = mutableListOf()
            query(
                uriType,
                arrayOf(PATH, _ID, TITLE, SIZE, DATE_MODIFIED),
                select,
                arg,
                null
            )?.useBack {
                while (moveToNext()) {
                    curStr(TITLE)?.letSusBack { itT ->
                        curStr(PATH)?.letSusBack { itP ->
                            FileSack(
                                fileName = itT,
                                filePath = itP,
                                fileUri = (uriType.toStr + "/" + curInt(_ID)),
                                fileSize = curLong(SIZE),
                                typeFile = FileLate(
                                    curStr(PATH)
                                        .toStr
                                ).extension.trim().findType,
                                dateModified = curLong(DATE_MODIFIED).createTime,
                            ).alsoSusBack(dossiers::add)
                        }
                    }
                }
            }
            return@runCatching dossiers
        }.getOrDefault(mutableListOf())
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.findCategoryCount(
    cato: Int,
): String = justCoroutineCur {
    return@justCoroutineCur withBackCurDef("0") {
        when (cato) {
            CATO_IMAGE -> {
                loadForCatoCountType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            }
            CATO_VIDEO -> {
                loadForCatoCountType(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            }
            CATO_AUDIO -> {
                loadForCatoCountType(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)
            }
            else -> {
                loadForCatoCount(cato.findTypes, cato.findArg)
            }
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.loadForCatoCountType(
    uriType: android.net.Uri,
): String = justCoroutineCur {
    return@justCoroutineCur runCatching {
        return@runCatching query(
            uriType,
            null,
            null,
            null,
            null
        )?.useBack {
            count.toStr
        } ?: "0"
    }.getOrDefault("0")
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.loadForCatoCount(
    selectionMimeType: String,
    arg: Array<String>,
): String = justCoroutineCur {
    return@justCoroutineCur runCatching {
        return@runCatching query(
            android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL),
            null,
            selectionMimeType,
            arg,
            null
        )?.useBack {
            count.toStr
        } ?: "0"
    }.getOrDefault("0")
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.renameFile(
    fromPath: String?,
    toPath: String?,
    pic: FileSack,
): Boolean =
    justCoroutineCur {
        if (pic.fileUri != null) {
            contentResolver.imageStoreUpdate(fromPath, pic, toPath)
            mediaBroadCast(fromPath, toPath)
        }
        return@justCoroutineCur FileLate(fromPath.toStr).renameTo(FileLate(toPath.toStr))
    }

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
private suspend fun android.content.ContentResolver.imageStoreUpdate(
    fromPath: String?,
    pic: FileSack,
    updatePath: String?,
) {
    justCoroutineCur {
        runCatching {
            android.content.ContentValues().applySusBack {
                put(TITLE, pic.fileName)
                put(DISPLAY_NAME, pic.fileName)
                put(DATE_MODIFIED, System.currentTimeMillis())
                if (isV_Q) {
                    put(DATE_TAKEN, System.currentTimeMillis() + 10000)
                }
                put(PATH, updatePath)
            }.alsoSusBack {
                update(
                    android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL),
                    it,
                    PATH.toStr + " " + DATA_EQUAL,
                    arrayOf(fromPath)
                )
            }

        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.insertStoreFile(pic: FileSack) {
    justCoroutineCur {
        runCatching {
            android.content.ContentValues().applySusBack {
                put(TITLE, pic.fileName)
                put(DISPLAY_NAME, pic.fileName)
                //put(SIZE, pic.fileSize)
                put(DATE_ADDED, System.currentTimeMillis())
                put(DATE_MODIFIED, System.currentTimeMillis())
                if (isV_Q) {
                    put(DATE_TAKEN, System.currentTimeMillis())
                }
                put(PATH, pic.filePath)
            }.alsoSusBack {
                insert(android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL), it)
            }
        }
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver.mediaStoreFile(
    pic: FileSack,
    toPath: String?,
    del: String?,
) {
    justCoroutineCur {
        withBackCurDef(Unit) {
            toPath?.letSusBack {
                insertStoreFile(pic.copy(filePath = it.toStr))
            }
        }
        withBackCurDef(Unit) {
            del?.letSusBack {
                deleteMoveFile(it, pic.typeFile)
            }
        }
    }
}

suspend fun android.content.ContentResolver.deleteMoveFile(del: String, typeFile: Int) {
    checkFileID(del).runSusBack {
        DSackT(this, typeFile).margeUri
    }.letSusBack { haveUri ->
        withBackCurDef(Unit) {
            if (haveUri != null) {
                runCatching {
                    delete(haveUri, null, null)
                }
            }
            runCatching {
                FileLate(del).letSusBack {
                    if (it.isFile) {
                        it.delete()
                    }
                }
            }
        }
    }

}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun FileLate.copyFileTo(toPath: FileLate): Boolean = justCoroutineCur {
    withBackCurDef(false) {
        if (!isFile) return@withBackCurDef false
        inputStream().useSusIT { input ->
            toPath.outputStream().useBack {
                input.copyTo(this)
            }
        }
        return@withBackCurDef true
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.moveFolderTo(
    from: FileLate,
    toPath: FileLate,
    fileHolder: FileSack,
): Boolean = justCoroutineCur {
    if (!toPath.exists()) {
        contentResolver.createNewFolder(toPath.absolutePath, toPath.name)
    }
    from.listFiles()?.toFileList()?.onEachSusBack(this@moveFolderTo) {
        runCatching {
            if (this@onEachSusBack.isDirectory) {
                FileSack(
                    this@onEachSusBack.name,
                    this@onEachSusBack.absolutePath,
                    null,
                    0,
                    FOLDER,
                    dateModified = 0
                ).letSusBack { itF ->
                    moveFolderTo(this@onEachSusBack, FileLate(toPath, this@onEachSusBack.name), itF)
                }
            } else {
                val copyDone = withBackCurDef(false) {
                    if (!this@onEachSusBack.isFile) return@withBackCurDef false
                    this@onEachSusBack.inputStream().useSusIT { input ->
                        FileLate(toPath, this@onEachSusBack.name).outputStream().useBack {
                            input.copyTo(this)
                        }
                    }
                    return@withBackCurDef true
                }
                withBackCurDef(false) {
                    if (copyDone) this@onEachSusBack.delete()
                    FileSack(
                        this@onEachSusBack.name,
                        this@onEachSusBack.absolutePath,
                        null,
                        0,
                        this@onEachSusBack.extension.trim().findType,
                        dateModified = 0
                    ).letSusBack { itF ->
                        contentResolver.mediaStoreFile(
                            itF,
                            FileLate(toPath, this@onEachSusBack.name).absolutePath,
                            FileLate(this@onEachSusBack, this@onEachSusBack.name).absolutePath
                        )
                    }
                }
            }
        }
    }
    runCatching {
        from.delete()
        contentResolver.mediaStoreFile(fileHolder, null, from.absolutePath)
    }
    true
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.Context.copyTryFolderTo(
    new: FileLate,
    toPath: FileLate,
): Boolean = justCoroutineCur {
    if (!toPath.exists()) {
        contentResolver.createNewFolder(toPath.absolutePath, toPath.name)
    }
    new.listFiles()?.toFileList()?.onEachSusBack(this@copyTryFolderTo) {
        runCatching {
            if (this@onEachSusBack.isDirectory) {
                copyTryFolderTo(this@onEachSusBack, FileLate(toPath, this@onEachSusBack.name))
            } else {
                withBackCurDef(false) {
                    if (!this@onEachSusBack.isFile) return@withBackCurDef false
                    this@onEachSusBack.inputStream().useSusIT { input ->
                        FileLate(toPath, this@onEachSusBack.name).outputStream().useBack {
                            input.copyTo(this)
                        }
                    }
                    return@withBackCurDef true
                }
                withBackCurDef(false) {
                    FileSack(
                        this@onEachSusBack.name,
                        this@onEachSusBack.absolutePath,
                        null,
                        0,
                        this@onEachSusBack.extension.trim().findType,
                        dateModified = 0
                    ).letSusBack { itF ->
                        contentResolver.mediaStoreFile(
                            itF,
                            FileLate(toPath, this@onEachSusBack.name).absolutePath,
                            null
                        )
                    }
                }
            }
        }
    }
    true
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
fun android.content.ContentResolver.createNewFolder(
    fullPath: String, name: String,
): Boolean = runCatching {
    FileLate(fullPath).let {
        if (!it.exists()) {
            it.mkdirs()
        }
    }
    android.content.ContentValues().apply {
        put(MIME_TYPE, MEDIA_TYPE_NONE)
        put(TITLE, name)
        put(DISPLAY_NAME, name)
        put(DATE_ADDED, System.currentTimeMillis())
        put(DATE_MODIFIED, System.currentTimeMillis())
        if (isV_Q) {
            put(DATE_TAKEN, System.currentTimeMillis())
        }
        put(PATH, fullPath)
    }.also {
        insert(android.provider.MediaStore.Files.getContentUri(DATA_EXTERNAL), it)
    }
    return@runCatching true
}.getOrDefault(false)


@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
fun android.content.Context.getFilePathFromURI(contentUri: android.net.Uri): String = run {
    return@run runCatching {
        return@runCatching contentResolver.query(contentUri, arrayOf(PATH), null, null, null)?.useMy {
            if (moveToFirst()) {
                return@useMy curStr(PATH)
            } else {
                return@useMy null
            }
        }
    }.getOrNull().let { if (FileLate(it ?: return@let null).exists()) it else null } ?: getPath(contentUri)
}

suspend fun android.content.Context.fetchFileFromUri(uri: android.net.Uri, file: String, a: (String) -> Unit) {
    withBackDef(Unit) {
        getOwnFile(file).also { ownFile ->
            justScope {
                if (ownFile.exists()) {
                    ownFile.delete()
                } else return@justScope
            }
            justScope {
                contentResolver.openInputStream(uri)?.useSusIT { input ->
                    ownFile.outputStream().useBack {
                        input.copyTo(this)
                    }
                    justScope {
                        a.invoke(ownFile.absolutePath)
                    }
                }
            }
        }
    }
}

suspend fun android.content.ContentResolver.openInValid(uri: android.net.Uri, s: suspend android.os.ParcelFileDescriptor?.() -> Unit) {
    withBackDef(null) {
        if (isV_Q) {
            openFile(uri, "r", null)
        } else {
            openFileDescriptor(uri, "r")
        }
    }.letSusBack(s)
}

suspend fun android.content.ContentResolver.writeInValid(uri: android.net.Uri, s: suspend android.os.ParcelFileDescriptor?.() -> Unit) {
    withBackDef(null) {
        if (isV_Q) {
            openFile(uri, "w", null)
        } else {
            openFileDescriptor(uri, "w")
        }
    }.letSusBack(s)
}

suspend fun android.content.ContentResolver.fetchPdfForRender(
    pdfPath: String?,
    uri: android.net.Uri?,
    s: suspend android.os.ParcelFileDescriptor?.() -> Unit
) {
    FileLate(pdfPath.toStr).let { fl ->
        if (fl.exists()) {
            android.os.ParcelFileDescriptor.open(
                fl,
                android.os.ParcelFileDescriptor.MODE_READ_ONLY
            ).alsoSusBack(s)
        } else {
            openInValid(uri ?: return) {
                this@openInValid.alsoSusBack(s)
            }
        }
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun android.os.ParcelFileDescriptor?.renderPdf(
    displayMetrics: android.util.DisplayMetrics,
    pageNum: Int,
    failed: (suspend () -> Unit)?,
    b: suspend android.graphics.Bitmap.() -> Unit,
) {
    withBack {
        if (this@renderPdf == null) {
            failed?.invoke()
            return@withBack
        }
        this@renderPdf.useSusIT { fp ->
            android.graphics.pdf.PdfRenderer(fp).useSusIT { pdfRenderer ->
                pdfRenderer.openPage(pageNum).useBack {
                    if (this == null) {
                        failed?.invoke()
                        return@useBack null
                    }
                    android.graphics.Bitmap.createBitmap(
                        (this.width / 72) * displayMetrics.densityDpi,
                        (this.height / 72) * displayMetrics.densityDpi,
                        android.graphics.Bitmap.Config.ARGB_8888
                    )?.alsoSusBack {
                        android.graphics.Canvas(it).alsoSusBack { c ->
                            c.drawColor(android.graphics.Color.WHITE)
                        }
                        justCoroutine {
                            render(
                                it,
                                null,
                                null,
                                android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                            )
                        }
                    } ?: runSusBack {
                        failed?.invoke()
                        return@runSusBack null
                    }
                }
            }?.runSusBack {
                this@runSusBack.allocationByteCount.toFloat().alsoSusBack { allocation ->
                    if (allocation > 50000000.0) {
                        (50000000.0 / allocation).letSusBack { ratio ->
                            android.graphics.Bitmap.createScaledBitmap(
                                this@runSusBack, (ratio * width.toFloat()).toInt(),
                                (ratio * height.toFloat()).toInt(),
                                true
                            ).alsoSusBack(b)
                        }
                    } else {
                        b.invoke(this@runSusBack)
                    }
                }
            }
        }
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
inline val android.os.ParcelFileDescriptor?.renderPdfSize: suspend () -> Int
    get() = {
        withBackDef(-1) {
            return@withBackDef android.graphics.pdf.PdfRenderer(this@renderPdfSize ?: return@withBackDef -1).useBack {
                return@useBack pageCount
            }
        }
    }

@com.pt.common.global.WorkerAnn
fun FileLate.deleteImage() {
    catchyUnit {
        if (isFile) {
            delete()
        } else return
    }
}