@file:Suppress("SpellCheckingInspection", "HardCodedStringLiteral")

package com.pt.common.stable

import com.pt.common.global.FileLate
import com.pt.common.global.getOwnFile
import com.pt.common.media.toFileList

const val IS_ALL_SONGS: Int = 2
const val IS_YOUR_PLAYLIST: Int = 3
const val ARTIST_IS: Int = 4
const val ALBUM_IS: Int = 5
const val ALL_PLAYLIST: Int = 6

const val UNKNOWN: Int = -1
const val FOLDER: Int = 0
const val IMAGE: Int = 1
const val AUDIO: Int = 2
const val VIDEO: Int = 3

const val PDF: Int = 4
const val DOC: Int = 5
const val DOCX: Int = 6
const val XLS: Int = 7
const val XLSX: Int = 8
const val PPT: Int = 9
const val PPTX: Int = 10
const val TXT: Int = 11
const val HTML: Int = 12
const val RAR: Int = 13
const val ZIP: Int = 14
const val APK: Int = 15

const val CATO_IMAGE: Int = 0
const val CATO_VIDEO: Int = 1
const val CATO_AUDIO: Int = 2
const val CATO_DOCUMENT: Int = 3
const val CATO_ARCHIVES: Int = 4
const val CATO_MUSIC: Int = 5
const val CATO_SD: Int = 6

inline val FileLate.fetchFileName: String
    get() = nameWithoutExtension.ifEmpty { name }

inline val String.checkHiddenMedia: Boolean
    get() = kotlin.runCatching { substring(0, 1) == "." }.getOrDefault(false)

suspend fun fileCountNative(filePath: String): Int = justCoroutine {
    withBackDef(0) {
        runCatching {
            (FileLate(filePath).list()?.size ?: 0)
        }.getOrDefault(0)
    }
}


suspend fun fileCount(filePath: String, s: (Int) -> Unit) {
    justCoroutine {
        catchySus(0) {
            (FileLate(filePath).list()?.size ?: 0).let(s)
        }
    }
}


inline val Array<String>.containsArr: Boolean
    get() {
        for (i in 1 until size) {
            if (this[0].contains(this[i])) return true
        }
        return false
    }

inline val String.findType: Int
    @com.pt.common.global.WorkerAnn
    get() {
        return when {
            isDirHaveImages -> IMAGE
            isDirHaveVideos -> VIDEO
            isDirHaveAudios -> AUDIO
            contains("pdf".toRegex()) -> PDF
            contains("doc".toRegex()) -> DOC
            contains("docx".toRegex()) -> DOCX
            contains("xls".toRegex()) -> XLS
            contains("xlsx".toRegex()) || contains("xlsm".toRegex()) -> XLSX
            contains("ppt".toRegex()) -> PPT
            contains("pptx".toRegex()) -> PPTX
            contains("txt".toRegex()) -> TXT
            contains("html".toRegex()) -> HTML
            contains("rar".toRegex()) -> RAR
            contains("zip".toRegex()) -> ZIP
            contains("apk".toRegex()) -> APK
            else -> UNKNOWN
        }
    }

inline val String.isDirHaveAudios: Boolean
    get() {
        return arrayOf(
            this@isDirHaveAudios,
            "mp3",
            "m4a",
            "aac",
            "amr",
            "flac",
            "mid",
            "ogg",
            "wav",
            "3gp",
            "xmf",
            "mxmf"
        ).containsArr
    }

inline val String.isDirHaveImages: Boolean
    get() {
        return arrayOf(
            this@isDirHaveImages,
            "jpg",
            "jpeg",
            "png",
            "avif",
            "webp",
            "HEIC",
            "heic",
            "heif"
        ).containsArr
    }

inline val String.isDirHaveVideos: Boolean
    get() {
        return arrayOf(
            this@isDirHaveVideos,
            "mp4",
            "gif",
            "webm",
            "mkv",
        ).containsArr
    }

inline val String.isDirHaveGif: Boolean
    get() {
        return arrayOf(
            this@isDirHaveGif,
            "gif",
            "webp",
        ).containsArr
    }

inline val android.content.Context.storesPathes: suspend () -> MutableList<FileLate>
    get() = {
        withDefaultDef(mutableListOf()) {
            arrayOf(
                FileLate("/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/.Statuses"),
                FileLate("/storage/emulated/0/WhatsApp/Media/.Statuses"),
                getOwnFile(STORY_FOLDER)
            ).let { a ->
                mutableListOf<FileLate>().apply {
                    a.forEach { p ->
                        if (p.exists()) {
                            this@apply.add(p)
                        }
                    }
                }
            }
        }
    }


inline val isHaveStoresPathes: suspend () -> Boolean
    get() = {
        withDefaultDef(false) {
            arrayOf(
                FileLate("/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/.Statuses"),
                FileLate("/storage/emulated/0/WhatsApp/Media/.Statuses"),
            ).any {
                it.exists() && (it.listFiles()?.toFileList() ?: arrayListOf()).isNotEmpty()
            }
        }
    }