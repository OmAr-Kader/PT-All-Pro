package com.pt.pro.gallery.objects

import com.pt.common.media.getListFolder
import com.pt.common.stable.*

class HiddenWorker(
    private val context: android.content.Context,
    parameters: androidx.work.WorkerParameters,
) : androidx.work.CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        context.contentResolver.getListFolder(
            com.pt.common.global.FileLate(com.pt.common.BuildConfig.ROOT)
        ).applySusBack {
            mutableListOf<com.pt.common.global.MediaSack>().alsoSusBack { new ->
                context.newDBGallerySus {
                    getAllHiddenMedia()
                }.alsoSusBack {
                    this@applySusBack.onEachSusBack(context) {
                        if (!it.contains(this@onEachSusBack)) new.add(this@onEachSusBack)
                    }
                }
            }.letSusBack {
                context.newDBGallerySus {
                    it.insertFavMedia(1)
                }
            }
        }
        return Result.success()
    }

}