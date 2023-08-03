package com.pt.pro.file.objects

import androidx.work.ListenableWorker
import com.pt.common.global.deleteRecursiveChildren
import com.pt.common.global.getOwnFile

class FileWorker(
    private val context: android.content.Context,
    parameters: androidx.work.WorkerParameters,
) : androidx.work.CoroutineWorker(context, parameters) {

    override suspend fun doWork(): ListenableWorker.Result {
        context.getOwnFile(com.pt.common.BuildConfig.ZIP_FILE).deleteRecursiveChildren()
        return Result.success()
    }
}