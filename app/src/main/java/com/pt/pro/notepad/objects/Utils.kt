package com.pt.pro.notepad.objects

import com.pt.common.global.*
import com.pt.common.media.createNewFolder
import com.pt.common.stable.*
import com.pt.pro.R
import java.util.zip.ZipFile

suspend fun android.content.Context.prepareCounterBackUp(): String? = justCoroutine {
    return@justCoroutine (com.pt.common.BuildConfig.ROOT + FileLate.separator + MY_COUNTER_FILE).runSusBack {
        this@runSusBack.letSusBack(::FileLate).runSusBack {
            if (!isDirectory) exists() else true
        }.letSusBack {
            if (!it) {
                MY_COUNTER_FILE.fetchMyRootFile.absolutePath.applySusBack {
                    contentResolver.createNewFolder(this@runSusBack, MY_COUNTER_FILE)
                }
            } else {
                this@runSusBack
            }
        }
    }
}

suspend fun androidx.fragment.app.Fragment.backUpCounterProcess(
    userId: String,
    justMonth: String?,
) {
    withBack {
        val myF = FileLate.separator + MY_COUNTER_FILE + FileLate.separator + userId
        val myZipFile = "$myF.zip"
        val parent = requireContext().getOwnFile(FileLate.separator + MY_COUNTER_FILE)
        if (!parent.exists()) {
            parent.mkdirs()
        }
        val name = FileLate.separator + userId + ".zip"
        val outputPath = (FileLate.separator + MY_COUNTER_FILE).fetchMyFile

        val sourcePath = requireContext().getOwnFile(myF)
        val toLocation = requireContext().getOwnFile(myZipFile).absolutePath
        requireContext().exportDB(
            userId,
            sourcePath,
            TABLES_COUNTER,
            COUNTER_FILE,
            justMonth
        ).let {
            if (it) {
                if (zipFileAtPath(sourcePath.absolutePath, toLocation)) {
                    FileLate(sourcePath.absolutePath).deleteRecursive()
                    moveFile(parent.absolutePath, name, outputPath.absolutePath)
                    val filePlace = resources.getString(R.string.qm) + MY_COUNTER_FILE
                    openFolder(outputPath.absolutePath, MY_COUNTER_FILE)
                    context?.makeToastSus(filePlace, 0)
                }
            } else {
                context?.makeToastRecSus(R.string.xp, 0)
            }
        }
    }
}


suspend fun android.content.Context.prepareBackUpData(): String? = justCoroutine {
    return@justCoroutine (com.pt.common.BuildConfig.ROOT + FileLate.separator + MY_DATA_KEEPER_FILE).runSusBack {
        this@runSusBack.letSusBack(::FileLate).runSusBack {
            if (!isDirectory) exists() else true
        }.letSusBack {
            if (!it) {
                MY_DATA_KEEPER_FILE.fetchMyRootFile.absolutePath.applySusBack {
                    contentResolver.createNewFolder(this@runSusBack, MY_DATA_KEEPER_FILE)
                }
            } else {
                this@runSusBack
            }
        }
    }
}

suspend fun androidx.fragment.app.Fragment.backUpProcessData(
    userId: String,
    justMonth: String?,
) {
    val myF = FileLate.separator + MY_DATA_KEEPER_FILE + FileLate.separator + userId
    val myZipFile = "$myF.zip"
    val parent = requireContext().getOwnFile(FileLate.separator + MY_DATA_KEEPER_FILE)
    if (!parent.exists()) {
        parent.mkdirs()
    }

    val name = FileLate.separator + userId + ".zip"
    val outputPath = (FileLate.separator + MY_DATA_KEEPER_FILE).fetchMyFile

    val sourcePath = requireContext().getOwnFile(myF)
    val toLocation = requireContext().getOwnFile(myZipFile).absolutePath
    if (requireContext().exportDB(
            userId,
            sourcePath,
            TABLES_DATA_FILE,
            FILE_DATA,
            justMonth
        )
    ) {
        reload(sourcePath.absolutePath, userId, true)
        kotlinx.coroutines.delay(50L)
        zipFileAtPath(sourcePath.absolutePath, toLocation)
        kotlinx.coroutines.delay(50L)
        withBack {
            FileLate(sourcePath.absolutePath).deleteRecursive()
            moveFile(parent.absolutePath, name, outputPath.absolutePath)
            openFolder(outputPath.absolutePath, MY_DATA_KEEPER_FILE)
            val filePlace = resources.getString(R.string.qm) + MY_DATA_KEEPER_FILE
            requireContext().makeToastSus(filePlace, 0)
        }
    } else {
        requireContext().makeToastRecSus(R.string.xp, 0)
    }
}

suspend fun androidx.fragment.app.Fragment.getNewUserData(
    aa: String?,
): Boolean = justCoroutine {
    return@justCoroutine withBackDef(false) {
        val ada = FileLate(aa!!)
        val fileName = ada.name

        val pos: Int = fileName.lastIndexOf(".")
        val justName: String = if (pos > 0) fileName.toDefString(pos) else fileName

        val file1 = (ada.parentFile!!.toString() + FileLate.separator + justName
                + FileLate.separator + justName + FileLate.separator)

        val file = justName + TABLES_DATA_FILE
        if (ada.unzipFile()) {
            if (requireContext().importDb(file1, file)) {
                if (requireContext().importDbTables(
                        file1,
                        justName,
                        TABLES_DATA_FILE,
                        FILE_DATA
                    )
                ) {
                    if (reload(file1, justName, false)) {
                        val arrayTemp = requireContext().newDBDataUser {
                            getAllUsers()
                        }

                        val aaa1: MutableList<String> = mutableListOf()
                        arrayTemp.forEach {
                            it.userId?.let { it1 -> aaa1.add(it1) }
                        }
                        if (!aaa1.contains(justName)) {
                            requireContext().insertDataUserName(justName)
                        }
                        val f = FileLate(
                            ada.parentFile!!.toString() + FileLate.separator + justName
                        )
                        f.deleteRecursive()
                        requireContext().makeToastRecSus(R.string.mp, 0)
                        return@withBackDef true
                    }
                } else {
                    requireContext().makeToastRecSus(R.string.la, 0)
                    return@withBackDef true
                }
                return@withBackDef false
            } else {
                requireContext().makeToastRecSus(R.string.ui, 0)
                return@withBackDef false
            }
        } else {
            requireContext().makeToastRecSus(R.string.mr, 0)
            return@withBackDef false
        }
    }

}

suspend fun android.content.Context.getNewCounterUser(
    aa: String?,
): Boolean = justCoroutine {
    return@justCoroutine withBackDef(false) {
        val ada = FileLate(aa!!)
        val fileName = ada.name

        val pos: Int = fileName.lastIndexOf(".")
        val justName: String = if (pos > 0) fileName.toDefString(pos) else fileName
        val file1 = (ada.parentFile!!.toString() + FileLate.separator + justName +
                FileLate.separator + justName + FileLate.separator)

        val file = justName + TABLES_COUNTER
        if (ada.unzipFile()) {
            if (importDb(file1, file)) {
                if (importDbTables(
                        file1,
                        justName,
                        TABLES_COUNTER,
                        COUNTER_FILE
                    )
                ) {
                    val arrayTemp = newDBDataUserCounter {
                        getAllCounterUsers()
                    }
                    val aaa1: MutableList<String> = mutableListOf()
                    arrayTemp.forEach {
                        it.userId?.let { it1 -> aaa1.add(it1) }
                    }
                    if (!aaa1.contains(justName)) {
                        insertCounterUserName(justName)
                    }
                    try {
                        val f = FileLate(
                            ada.parentFile!!.toString() + FileLate.separator + justName
                        )
                        f.deleteRecursive()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    makeToastRecSus(R.string.mp, 0)
                    return@withBackDef true
                } else {
                    makeToastRecSus(R.string.ui, 0)
                }
                return@withBackDef false
            } else {
                makeToastRecSus(R.string.ui, 0)
            }
            return@withBackDef false
        } else {
            makeToastRecSus(R.string.mr, 0)
            return@withBackDef false
        }
    }
}

private suspend fun android.content.Context.insertCounterUserName(aaa: String) {
    val newAaa = aaa.replace("[0-9]".toRegex(), "")
    newDBDataUserCounter {
        com.pt.pro.notepad.models.TablesModelUser(userName = newAaa, userId = aaa).run {
            insertCounterUser()
        }
    }
}

private suspend fun android.content.Context.insertDataUserName(aaa: String) {
    val newAaa = aaa.replace("[0-9]".toRegex(), "")
    newDBDataUser {
        com.pt.pro.notepad.models.TablesModelUser(userName = newAaa, userId = aaa).run {
            insertUser()
        }
    }
}

private suspend fun android.content.Context.exportDB(
    userId: String,
    sd: FileLate,
    keyTable: String,
    keyData: String,
    justMonth: String?,
): Boolean {
    val aa: MutableList<String> = mutableListOf()
    val dd = userId + keyTable
    aa.add(dd)
    val array = newDBTables(dd) {
        getAllDataTables(userId)
    }
    array.forEach {
        if (justMonth != null) {
            if (justMonth == it.mTableName) aa.add(it.mTableName + keyData)
        } else {
            aa.add(it.mTableName + keyData)
        }
    }
    return exportOneDB(aa, sd)
}

@android.annotation.SuppressLint("SetWorldReadable", "SetWorldWritable")
private suspend fun android.content.Context.exportOneDB(
    aa: MutableList<String>,
    sd: FileLate,
): Boolean = justCoroutine {
    return@justCoroutine withBackDef(false) {
        runCatching {
            sd.setReadable(true, false)
            sd.setWritable(true, false)
        }
        if (!sd.exists()) {
            sd.mkdirs()
        }
        if (sd.canWrite()) {
            aa.forEach {
                val dpPath = (applicationInfo.dataDir +
                        com.pt.common.BuildConfig.DATABASE_PATH + it)
                val currentDB = FileLate(dpPath)
                val backupDB = FileLate(sd, it)
                if (backupDB.exists()) {
                    backupDB.delete()
                }
                val src = java.io.FileInputStream(currentDB).channel
                val dst = java.io.FileOutputStream(backupDB).channel
                dst.transferFrom(src, 0, src.size())
                src.close()
                dst.close()
            }
        }
        return@withBackDef true
    }
}

private suspend fun android.content.Context.importDb(db_path: String, fileName: String): Boolean {
    return withBackDef(false) {
        kotlin.runCatching {
            val dbFile = FileLate(db_path + fileName)
            val mInputStream = java.io.DataInputStream(java.io.FileInputStream(dbFile))
            val dpPath = (applicationInfo.dataDir +
                    com.pt.common.BuildConfig.DATABASE_PATH + fileName)
            val outFileName: String = getDatabasePath(dpPath).absolutePath

            val mOutputStream = java.io.FileOutputStream(outFileName)
            val buffer = ByteArray(1024)
            var length: Int
            while (mInputStream.read(buffer).also { length = it } > 0) {
                mOutputStream.write(buffer, 0, length)
            }
            mOutputStream.flush()
            mOutputStream.close()
            mInputStream.close()

            return@withBackDef true
        }.getOrDefault(false)
    }
}


private suspend fun android.content.Context.importDbTables(
    db_path: String,
    fileName: String,
    keyTable: String,
    keyData: String,
): Boolean = justCoroutine {
    val array: MutableList<String> = mutableListOf()
    withBack {
        val dataName = fileName + keyTable
        newDBTables(dataName) {
            getAllDataTables(fileName)
        }.forEach {
            array.add(it.mTableName + keyData)
        }
    }
    return@justCoroutine withBackDef(false) {
        return@withBackDef importDbTablesFile(db_path, array)
    }
}

private suspend fun android.content.Context.importDbTablesFile(
    db_path: String,
    array: MutableList<String>,
): Boolean = justCoroutine {
    withBackDef(false) {
        array.onEachSusBack(this@importDbTablesFile) {
            kotlin.runCatching {
                val dbFile = FileLate(db_path + this@onEachSusBack)
                val mInputStream = java.io.DataInputStream(java.io.FileInputStream(dbFile))

                val dpPath = (applicationInfo.dataDir +
                        com.pt.common.BuildConfig.DATABASE_PATH + this@onEachSusBack)
                val outFileName: String = getDatabasePath(dpPath).absolutePath
                val mOutputStream = java.io.FileOutputStream(outFileName)
                val buffer = ByteArray(1024)
                var length: Int
                while (mInputStream.read(buffer).also { length = it } > 0) {
                    mOutputStream.write(buffer, 0, length)
                }
                mOutputStream.flush()
                mOutputStream.close()
                mInputStream.close()
            }
        }
        return@withBackDef true
    }
}

private suspend fun zipFileAtPath(sourcePath: String, toLocation: String): Boolean = justCoroutine {
    withBackDef(false) {
        val buffer = 2048
        val sourceFile = FileLate(sourcePath)
        if (!sourceFile.exists()) {
            sourceFile.mkdirs()
        }
        kotlin.runCatching {
            val origin: java.io.BufferedInputStream?
            val dest = java.io.FileOutputStream(toLocation)
            val out = java.util.zip.ZipOutputStream(
                java.io.BufferedOutputStream(dest)
            )
            if (sourceFile.isDirectory) {
                out.zipSubFolder(sourceFile, sourceFile.parent!!.length)
            } else {
                val data = ByteArray(buffer)
                val fi = java.io.FileInputStream(sourcePath)
                origin = java.io.BufferedInputStream(fi, buffer)
                val entry = java.util.zip.ZipEntry(sourcePath.getLastPathComponent)
                entry.time = sourceFile.lastModified() // to keep modification i1 after unzipping
                out.putNextEntry(entry)
                var count: Int
                while (origin.read(data, 0, buffer).also { count = it } != -1) {
                    out.write(data, 0, count)
                }
            }
            out.close()
            return@withBackDef true
        }.getOrElse {
            it.printStackTrace()
            return@withBackDef false
        }
    }
}

@Throws(java.io.IOException::class)
private suspend fun java.util.zip.ZipOutputStream.zipSubFolder(
    folder: FileLate,
    basePathLength: Int
) {
    withBack {
        val buffer = 2048
        val fileList = folder.listFiles()
        fileList?.toMutableList()?.onEachSusBack(this@zipSubFolder) {
            if (this@onEachSusBack.isDirectory) {
                kotlin.runCatching {
                    zipSubFolder(this@onEachSusBack, basePathLength)
                }
            } else {
                kotlin.runCatching {
                    val data = ByteArray(buffer)
                    val unmodifiedFilePath = this@onEachSusBack.path
                    val relativePath = unmodifiedFilePath.substring(basePathLength)
                    val fi = java.io.FileInputStream(unmodifiedFilePath)
                    val origin = java.io.BufferedInputStream(fi, buffer)
                    val entry = java.util.zip.ZipEntry(relativePath)
                    entry.time =
                        this@onEachSusBack.lastModified() // to keep modification i1 after unzipping
                    putNextEntry(entry)
                    var count: Int
                    while (origin.read(data, 0, buffer).also { count = it } != -1) {
                        write(data, 0, count)
                    }
                    origin.close()
                }
            }
        }
    }
}

internal inline val String.getLastPathComponent: String
    get() {
        val segments = this@getLastPathComponent.split(FileLate.separator.toRegex()).toTypedArray()
        return if (segments.isEmpty()) "" else segments[segments.size - 1]
    }

suspend fun FileLate.unzipFile(): Boolean {
    return kotlin.runCatching {
        unzip()
        return true
    }.getOrElse {
        false
    }
}

suspend fun FileLate.unzip(unzipLocationRoot: FileLate? = null) {
    val rootFolder = unzipLocationRoot
        ?: FileLate(parentFile?.absolutePath + FileLate.separator + nameWithoutExtension)
    if (!rootFolder.exists()) {
        rootFolder.mkdirs()
    }
    withBack {
        kotlin.run {
            ZipFile(this@unzip).useSusIT { zip ->
                zip.entries()
                    .asSequence()
                    .map {
                        val outFile =
                            FileLate(rootFolder.absolutePath + FileLate.separator + it.name)
                        ZipIO(it, outFile)
                    }
                    .map {
                        it.output.parentFile?.run {
                            if (!exists()) mkdirs()
                        }
                        it
                    }
                    .filter { !it.entry.isDirectory }
                    .toMutableList().onEachSusBack(true) {
                        kotlin.runCatching {
                            zip.getInputStream(entry).useSusIT { input ->
                                output.outputStream().useSusIT { output ->
                                    input.copyTo(output)
                                }
                            }
                        }
                    }
            }
        }
    }
}

data class ZipIO(val entry: java.util.zip.ZipEntry, val output: FileLate)