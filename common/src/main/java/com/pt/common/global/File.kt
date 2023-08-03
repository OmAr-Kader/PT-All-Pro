@file:Suppress("HardCodedStringLiteral")

package com.pt.common.global

import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.pt.common.stable.*

fun FileLate.listAllFiles(): MutableList<FileLate> = catchy(mutableListOf()) {
    try {
        if (exists()) {
            listFiles()?.toMutableList() ?: mutableListOf()
        } else mutableListOf()
    } catch (e: NoSuchElementException) {
        mutableListOf()
    } catch (e: SecurityException) {
        mutableListOf()
    }
}

@Suppress("DEPRECATION")
inline val String.fetchMyRootFile: FileLate
    @com.pt.common.global.WorkerAnn
    get() {
        return FileLate(Environment.getExternalStoragePublicDirectory("").toString(), this)
    }

@Suppress("DEPRECATION")
inline val String.fetchMyFile: FileLate
    @com.pt.common.global.WorkerAnn
    get() {
        return FileLate(Environment.getExternalStoragePublicDirectory("").toString() + this)
    }

fun android.content.Context.getOwnFile(
    name: String
): FileLate = FileLate(getExternalFilesDir(""), name)

fun FileLate.fileCreator(): FileLate {
    if (!exists()) {
        mkdirs()
    }
    return this
}

private inline val String.removePre: String
    get() = replace("/root", "").replace("/external_files", "")


fun android.content.Context.getPath(uri: Uri): String {
    return kotlin.runCatching {
        FileLate(uri.path.toStr).let { file ->
            if (file.exists()) {
                file.absolutePath
            } else {
                FileLate(uri.encodedPath.toStr).let { a ->
                    if (a.exists()) {
                        a.absolutePath
                    } else {
                        FileLate(java.net.URLEncoder.encode(uri.path, "UTF-8")).absolutePath.run {
                            java.net.URLDecoder.decode(this@run, "UTF-8").removePre
                        }.let {
                            if (FileLate(it).exists()) {
                                it
                            } else {
                                getPathTwo(uri)
                            }
                        }
                    }
                }
            }
        }
    }.getOrElse {
        it.listThrowable()
        getPathTwo(uri)
    }
}

private fun android.content.Context.getPathTwo(uri: Uri): String = run {
    if (isV_O && uri.path != null) {
        kotlin.runCatching {
            java.net.URLDecoder.decode(uri.toStr, "UTF-8").split("/root").getOrNull(1)?.removePre?.let { p ->
                if (FileLate(p).exists()) {
                    p
                } else {
                    getPathFromUri(uri)
                }
            } ?: getPathFromUri(uri)
        }.getOrElse {
            it.listThrowable()
            getPathFromUri(uri)
        }
    } else {
        getPathFromUri(uri)
    }
}

@Suppress("DEPRECATION")
private fun android.content.Context.getPathFromUri(uri: Uri): String = run {
    if (DocumentsContract.isDocumentUri(this@getPathFromUri, uri)) {
        if (uri.isExternalStorageDocument) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            if ("primary".equals(type, ignoreCase = true)) {
                return@run Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            }
        } else if (uri.isDownloadsDocument) {
            val id = DocumentsContract.getDocumentId(uri)
            val contentUri: Uri = android.content.ContentUris.withAppendedId(
                "content://downloads/public_downloads".toUri,
                java.lang.Long.valueOf(
                    id
                )
            )
            return@run getDataColumn(contentUri, null, null)
        } else if (uri.isMediaDocument) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }
                "video" -> {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }
                "ao" -> {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
            }
            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            return@run getDataColumn(contentUri, selection, selectionArgs)
        }
    } else if ("content".equals(uri.scheme, ignoreCase = true)) {
        return@run if (uri.isGooglePhotosUri) uri.lastPathSegment else getDataColumn(
            uri,
            null,
            null
        )
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return@run uri.path
    }
    return@run uri.path.toString()
}?.let {
    if (FileLate(it).exists()) {
        it
    } else {
        it.removePre
    }
} ?: uri.path.toString().let {
    if (FileLate(it).exists()) {
        it
    } else {
        it.removePre
    }
}

@Suppress("DEPRECATION")
private fun android.content.Context.getDataColumn(
    uri: Uri?,
    selection: String?,
    selectionArgs: Array<String>?,
): String? = runCatching {
    val column = MediaStore.Files.FileColumns.DATA
    val projection = arrayOf(column)
    return@runCatching contentResolver.query(
        uri!!,
        projection,
        selection,
        selectionArgs,
        null
    )?.useMy {
        return@useMy if (this@useMy.moveToFirst()) {
            val index: Int = this@useMy.getColumnIndexOrThrow(column)
            this@useMy.getString(index)
        } else {
            null
        }
    }
}.getOrNull()

@Suppress("SpellCheckingInspection")
inline val Uri.isExternalStorageDocument: Boolean
    get() {
        return "com.android.externalstorage.documents" == authority
    }

inline val Uri.isDownloadsDocument: Boolean
    get() {
        return "com.android.providers.downloads.documents" == authority
    }

inline val Uri.isMediaDocument: Boolean
    get() {
        return "com.android.providers.media.documents" == authority
    }

inline val Uri.isGooglePhotosUri: Boolean
    get() {
        return "com.gg.android.apps.photos.content" == authority
    }

fun FileLate.deleteRecursive() {
    if (isDirectory) {
        for (child in listFiles() ?: return) {
            child.deleteRecursive()
        }
    }
    delete()
}


suspend fun FileLate.deleteRecursiveSus() {
    withBackDef(Unit) {
        if (isDirectory) {
            for (child in listFiles() ?: return@withBackDef) {
                child.deleteRecursiveSus()
            }
        }
        delete()
    }
}

suspend fun FileLate.deleteRecursiveChildren() {
    withBackDef(Unit) {
        for (child in listFiles() ?: return@withBackDef) {
            child.deleteRecursiveSus()
        }
    }
}

@Suppress("DEPRECATION", "SpellCheckingInspection")
fun androidx.fragment.app.Fragment.openFolder(targerFile: String, name: String?) {
    runCatching {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val rootPath = "content://com.android.externalstorage.documents/document/primary"
            val samPath = "$rootPath%3A$name".toUri    //"%2f" represents "/"
//val uri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3ASCOMAR")
            android.content.Intent(android.content.Intent.ACTION_GET_CONTENT).run {
                type = "*/*"
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, samPath)
                startActivityForResult(this, 1)
            }
        } else {
            val uri = "/storage/emulated/0/$targerFile".toUri
            android.content.Intent(android.content.Intent.ACTION_VIEW).run {
                setDataAndType(uri, DocumentsContract.Document.MIME_TYPE_DIR)
                startActivity(
                    android.content.Intent.createChooser(
                        this, "Open Media folder"
                    )
                )
            }
        }
    }
}

inline val forAndroidData: android.content.Intent
    @com.pt.common.global.APIAnn(android.os.Build.VERSION_CODES.R)
    @Suppress("SpellCheckingInspection", "LongLine")
    get() {
        //"content://com.android.externalstorage.documents/tree/primary%3AAndroid"
        return "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata".let {
            it.toUri
        }.let {
            android.content.Intent(android.content.Intent.ACTION_GET_CONTENT).run {
                //type = "*/*"
                //addCategory(android.content.Intent.CATEGORY_OPENABLE)
                type = "*/*"
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                addFlags(android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                addFlags(android.content.Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
                putExtra("android.content.extra.SHOW_ADVANCED", true)
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, it)
            }
        }
    }

fun moveFile(inputPath: String, inputFile: String, outputPath: String) {
    runCatching {
        val inStream: java.io.Closeable?
        val out: java.io.OutputStream?
        val dir = FileLate(outputPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        inStream = FInStream(inputPath + inputFile)
        out = java.io.FileOutputStream(outputPath + inputFile)
        val buffer = ByteArray(1024)
        var read: Int
        while (inStream.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
        inStream.close()

        out.flush()
        out.close()

        FileLate(inputPath + inputFile).delete()
    }
}

inline val android.content.Context.getLocalFilePath: FileLate
    get() = FileLate(getExternalFilesDir(""), "Compressed").let {
        if (!it.exists()) {
            it.mkdirs()
        }
        ("IMG_" + System.currentTimeMillis() +
                kotlin.random.Random.nextInt(1000) +
                ".jpg").let<String, FileLate> { n ->
            return FileLate(it.absolutePath + "/" + n)
        }
    }

inline val android.content.Context.getCaptureImageOutputUri: (pac: String) -> Uri?
    get() = { pac ->
        runCatching {
            getExternalFilesDir("")?.let {
                FileProvider.getUriForFile(
                    this@getCaptureImageOutputUri,
                    pac + com.pt.common.BuildConfig.FILE_PROVIDER,
                    FileLate(it.path, "profile.jpg")
                )
            }
        }.getOrNull()
    }

suspend fun android.content.Context.deleteFileProvider(pac: String) {
    justCoroutine {
        withBack {
            val f = getCaptureImageOutputUri(pac)?.path ?: return@withBack
            FileLate(f).run {
                if (isFile) {
                    delete()
                }
            }
        }
    }
}