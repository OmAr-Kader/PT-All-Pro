package com.pt.common.media

import com.pt.common.BuildConfig.*
import com.pt.common.global.FileLate
import com.pt.common.global.MediaFolderSack
import com.pt.common.global.MediaSack
import com.pt.common.stable.*

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver?.getListFolder(
    parentDir: FileLate,
): MutableList<MediaSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        val hiddenUnique: MutableList<MediaSack> = mutableListOf()
        parentDir.listFiles()?.toFileList()?.onEachSusBack(this@getListFolder) {
            when {
                extension.trim().isDirHaveImages -> {
                    MediaSack(
                        name,
                        absolutePath,
                        null,
                        true,
                        length(),
                        0,
                        0,
                        lastModified().createTime
                    ).letSusBack { itM ->
                        if (length() != 0L) hiddenUnique.add(itM)
                    }
                }
                extension.trim().isDirHaveVideos -> {
                    MediaSack(
                        name,
                        absolutePath,
                        null,
                        false,
                        length(),
                        0,
                        0,
                        lastModified().createTime
                    ).letSusBack { itM ->
                        if (length() != 0L) hiddenUnique.add(itM)
                    }
                }
                isDirectory -> hiddenUnique.addAll(getListFolder(this))
            }
        }
        return@withBackCurDef hiddenUnique
    }
}

@com.pt.common.global.CurAnn
@com.pt.common.global.WorkerAnn
suspend fun android.content.ContentResolver?.getListRoot(
    galleryM: Int,
): MutableList<MediaSack> = justCoroutineCur {
    return@justCoroutineCur withBackCurDef(mutableListOf()) {
        val imagesHiddenRoot: MutableList<MediaSack> = mutableListOf()
        val isImage = galleryM == GALLERY_ALL || galleryM == GALLERY_IMG
        val isVideo = galleryM == GALLERY_ALL || galleryM == GALLERY_VID
        FileLate(ROOT).listFiles()?.toFileList()?.onEachSusBack(this@getListRoot) {
            if (isImage && extension.trim().isDirHaveImages) {
                MediaSack(
                    name,
                    absolutePath,
                    null,
                    true,
                    length(),
                    0,
                    0,
                    lastModified().createTime
                ).letSusBack { itM ->
                    if (length() != 0L) imagesHiddenRoot.add(itM)
                }
            } else if (isVideo && extension.trim().isDirHaveVideos) {
                MediaSack(
                    name,
                    absolutePath,
                    null,
                    false,
                    length(),
                    0,
                    0,
                    lastModified().createTime
                ).letSusBack { itM ->
                    if (length() != 0L) imagesHiddenRoot.add(itM)
                }
            }
        }
        return@withBackCurDef imagesHiddenRoot
    }
}

@com.pt.common.global.WorkerAnn
suspend fun MutableList<String>.makeMediaFolders(): MutableList<MediaFolderSack> =
    justCoroutine {
        return@justCoroutine withBackDef(mutableListOf()) {
            val aa: MutableList<MediaFolderSack> = mutableListOf()
            onEachSusBack(true) {
                MediaFolderSack(
                    "${this@onEachSusBack}/",
                    FileLate(this@onEachSusBack).name,
                    0,
                    ""
                ).alsoSusBack(aa::add)
            }
            return@withBackDef aa
        }
    }
