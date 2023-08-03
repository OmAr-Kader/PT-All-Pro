package com.pt.pro.gallery.objects

import com.pt.common.global.fileCreator
import com.pt.common.global.getOwnFile
import com.pt.common.media.copyFileTo
import com.pt.common.media.getListFolder
import com.pt.common.stable.alsoSusBack
import com.pt.common.stable.letSusBack
import com.pt.common.stable.onEachSusBack
import com.pt.common.stable.storesPathes

class StatusWorker(
    context: android.content.Context,
    parameters: androidx.work.WorkerParameters,
) : androidx.work.CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        com.pt.common.stable.catchyUnit {
            val myStoryPath = applicationContext.getOwnFile(com.pt.common.stable.STORY_FOLDER).fileCreator().absolutePath
            applicationContext.storesPathes().onEach { p ->
                applicationContext.contentResolver.getListFolder(
                    p
                ).onEachSusBack(applicationContext) {
                    com.pt.common.global.FileLate(pathMedia.toString()).alsoSusBack { newStoryFile ->
                        com.pt.common.global.FileLate(myStoryPath + "/" + newStoryFile.name).letSusBack { storyFile ->
                            if (!storyFile.exists()) {
                                newStoryFile.copyFileTo(storyFile)
                            } else return@letSusBack
                        }
                    }
                }
            }
            com.pt.common.stable.catchyUnit {
                com.pt.pro.main.odd.WorkInitializer().create(
                    applicationContext
                ).enqueueUniquePeriodicWork(
                    "StatusWorker",
                    androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
                    androidx.work.PeriodicWorkRequest.Builder(
                        StatusWorker::class.java,
                        androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS * 4,
                        java.util.concurrent.TimeUnit.MILLISECONDS
                    ).build()
                )
            }
        }
        return Result.success()
    }

}