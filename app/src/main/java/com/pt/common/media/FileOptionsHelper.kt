package com.pt.common.media

import com.pt.common.BuildConfig.*
import com.pt.common.global.*
import com.pt.common.stable.*

inline val android.content.Intent.isPDFIntent: Boolean
    get() {
        return action == android.content.Intent.ACTION_VIEW &&
                (data.toStr.contains("application/pdf".toRegex())
                        || type.toStr.contains("application/pdf".toRegex()))
    }

inline val android.content.Intent.isTxtIntent: Boolean
    get() {
        return action == android.content.Intent.ACTION_VIEW &&
                (data.toStr.contains("text/plain".toRegex()) ||
                        type.toStr.contains("text/plain".toRegex()))
    }

inline val android.content.Intent.isZipIntent: Boolean
    get() {
        return action == android.content.Intent.ACTION_VIEW &&
                (data.toStr.contains("application/zip".toRegex()) ||
                        type.toStr.contains("application/java-archive".toRegex()))
    }

inline fun android.content.Context.findRecAnim(anim: (Int) -> Unit) {
    fetchDimensions {
        (this@fetchDimensions.height - findPixel(140)).let {
            (it / resources.displayMetrics.findPixelDis(70F))
        }.also(anim)
    }
}

suspend fun Sequence<FileSack>.resortForFavorite(): MutableList<FileSack> =
    justCoroutine {
        withBackDef(mutableListOf()) {
            asSequence().filter {
                it.typeFile == FOLDER
            }.toMutableList()
        }.letSusBack { newOne ->
            withBackDef(mutableListOf()) {
                asSequence().filter {
                    it.typeFile != FOLDER
                }.toMutableList()
            }.letSusBack { newTwo ->
                mutableListOf<FileSack>().applySusBack {
                    this@applySusBack.addAll(newOne)
                    this@applySusBack.addAll(newTwo)
                }
            }
        }
    }

suspend fun MutableList<String>.getMusicIcon(
    num: Int, last: String?
): MutableList<FileSack> = justCoroutine {
    mutableListOf<FileSack>().applySusBack {
        last?.letSusBack {
            FileSack("", it, null, num.toLong(), CATO_MUSIC, 0, virName = "").alsoSusBack(::add)
        }
        this@getMusicIcon.onEachSusBack(true) {
            FileSack("", this@onEachSusBack, null, 0, CATO_SD, 0, virName = "").alsoSusBack(::add)
        }
        FileSack("", "", null, 0, CATO_IMAGE, 0, virName = "").alsoSusBack(::add)
        FileSack("", "", null, 0, CATO_VIDEO, 0, virName = "").alsoSusBack(::add)
        FileSack("", "", null, 0, CATO_AUDIO, 0, virName = "").alsoSusBack(::add)
        FileSack("", "", null, 0, CATO_DOCUMENT, 0, virName = "").alsoSusBack(::add)
        FileSack("", "", null, 0, CATO_ARCHIVES, 0, virName = "").alsoSusBack(::add)
    }
}

suspend fun MutableList<FileSack>.recreateForGallery(
): MutableList<MediaSack> = justCoroutine {
    return@justCoroutine withBackDef(mutableListOf()) {
        return@withBackDef mutableListOf<MediaSack>().applySusBack {
            this@recreateForGallery.onEachSusBack(true) {
                MediaSack(
                    this@onEachSusBack.fileName,
                    this@onEachSusBack.filePath,
                    null,
                    this@onEachSusBack.typeFile == IMAGE,
                    this@onEachSusBack.fileSize,
                    0,
                    0,
                    this@onEachSusBack.dateModified
                ).letSusBack { itM ->
                    this@applySusBack.add(itM)
                }
            }
        }
    }
}

suspend fun Sequence<FileSack>.filterOnlyFav(s: MutableList<String>): MutableList<FileSack> =
    justCoroutine {
        filterIndexed { _, mediaHolder ->
            s.contains(mediaHolder.filePath)
        }.toMutableList()
    }

suspend fun Sequence<FileSack>.filterNotFav(s: MutableList<String>): MutableList<FileSack> =
    justCoroutine {
        filterIndexed { _, mediaHolder ->
            !s.contains(mediaHolder.filePath)
        }.toMutableList()
    }


fun MutableList<FileSack>.filterForRemovingVir(
    virtualFiles: MutableList<String>,
    virtualFilesName: MutableList<String>,
): DSack<MutableList<FileSack>, MutableList<String>, MutableList<String>> {
    val vTemp: MutableList<String> = mutableListOf()
    val del = filterIndexed { _, fileHolder ->
        virtualFiles.contains(fileHolder.filePath).let {
            if (it) {
                if (fileHolder.virName == null) {
                    virtualFiles.indexOf(fileHolder.filePath).let { ii ->
                        fileHolder.virName = virtualFilesName[ii]
                        vTemp.add(virtualFilesName[ii])
                    }
                } else {
                    vTemp.add(fileHolder.virName.toStr)
                }
            }
            it
        }
    }
    return com.pt.common.global.DSack(
        del.toMutableList(),
        vTemp,
        mutableListOf()
    )
}

suspend fun Sequence<FileSack>.onlyFolders(): MutableList<FileSack> =
    justCoroutine {
        return@justCoroutine withBackDef(mutableListOf()) {
            return@withBackDef filter {
                it.typeFile == FOLDER
            }.toMutableList()
        }
    }

suspend fun Sequence<FileSack>.onlyFiles(): MutableList<FileSack> =
    justCoroutine {
        return@justCoroutine withBackDef(mutableListOf()) {
            return@withBackDef filter {
                it.typeFile != FOLDER
            }.toMutableList()
        }
    }

suspend fun MutableList<FileSack>.orderFileAllLoaders(
    orderBy: String,
): MutableList<FileSack> = justCoroutine {
    return@justCoroutine withBackDef(mutableListOf()) {
        distinctBy { it.filePath }.toMutableList().doOrderFileAllLoaders(orderBy)
    }
}

suspend fun MutableList<FileSack>.doOrderFileAllLoaders(
    orderBy: String,
): MutableList<FileSack> = justCoroutine {
    when (orderBy) {
        BY_DATE_DESC -> {
            sortedByDescending {
                it.dateModified
            }.toMutableList()
        }
        BY_DATE_ASC -> {
            sortedBy {
                it.dateModified
            }.toMutableList()
        }
        BY_NAME_ASC -> {
            sortedWith(
                compareBy(String.CASE_INSENSITIVE_ORDER) { it.fileName }
            ).toMutableList()
        }
        BY_NAME_DESC -> {
            sortedWith(
                compareBy(String.CASE_INSENSITIVE_ORDER) { it.fileName }
            ).reversed().toMutableList()
        }
        BY_SIZE_ASC -> {
            sortedBy {
                it.fileSize
            }.toMutableList()
        }
        BY_SIZE_DESC -> {
            sortedByDescending {
                it.fileSize
            }.toMutableList()
        }
        else -> {
            sortedBy {
                it.dateModified
            }.toMutableList()
        }
    }
}

suspend fun installApk(
    uri: android.net.Uri,
    intent: suspend android.content.Intent.() -> Unit,
) {
    withDefault {
        android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
            flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            action = android.content.Intent.ACTION_VIEW
            setDataAndType(uri, "resource/folder")
        }.alsoSusBack {
            android.content.Intent.createChooser(
                it,
                "Choose App"
            ).alsoSusBack(intent)
        }
    }
}

suspend fun viewFiles30(
    uri: android.net.Uri,
    typ: String,
    intent: suspend android.content.Intent.() -> Unit,
) {
    withDefault {
        android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
            flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            setDataAndType(uri, typ)
            clipData = android.content.ClipData(
                "View",
                arrayOf(typ),
                android.content.ClipData.Item(uri)
            )
        }.alsoSusBack {
            android.content.Intent.createChooser(
                it,
                "Choose App"
            ).alsoSusBack(intent)
        }
    }
}

suspend fun android.content.Context.shareFiles30(
    deleteCopy: MutableList<FileSack>,
    typ: String,
    intent: suspend android.content.Intent.() -> Unit,
) {
    withDefault {
        mutableListOf<android.net.Uri>().applySusBack {
            deleteCopy.forEach {
                if (it.fileUri != null) {
                    this@applySusBack.add(it.fileUri.toUri)
                } else {
                    this@applySusBack.add(uriProvider(it.filePath.toStr, com.pt.pro.BuildConfig.APPLICATION_ID))
                }
            }
        }.letSusBack { files ->
            android.content.ClipData(
                "Share",
                arrayOf(typ),
                android.content.ClipData.Item(files.firstOrNull())
            ).apply { files.forEach { addItem(android.content.ClipData.Item(it)) } }.let { cl ->
                android.content.Intent(
                    if (deleteCopy.size == 1)
                        android.content.Intent.ACTION_SEND
                    else
                        android.content.Intent.ACTION_SEND_MULTIPLE
                ).apply {
                    flags = android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    type = typ
                    clipData = cl
                    if (deleteCopy.size == 1)
                        putExtra(android.content.Intent.EXTRA_STREAM, files[0])
                    else
                        putParcelableArrayListExtra(
                            android.content.Intent.EXTRA_STREAM,
                            files as ArrayList
                        )
                }.alsoSusBack {
                    android.content.Intent.createChooser(
                        it,
                        "Choose App"
                    ).alsoSusBack(intent)
                }
            }
        }
    }
}

inline val android.content.Context.androidDataFile: FileSack
    get() {
        return FileSack(
            fileName = ROOT_ANDROID_DATA_NAME,
            filePath = ROOT_ANDROID_DATA,
            fileUri = uriProviderNormal(
                ROOT_ANDROID_DATA,
                com.pt.pro.BuildConfig.APPLICATION_ID
            ).toStr,
            fileSize = 0,
            typeFile = FOLDER,
            dateModified = FileLate(ROOT_ANDROID_DATA).lastModified(),
        )
    }