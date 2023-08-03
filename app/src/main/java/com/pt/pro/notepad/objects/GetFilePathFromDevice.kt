@file:Suppress("HardCodedStringLiteral")

package com.pt.pro.notepad.objects

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.pt.common.global.FileLate
import com.pt.common.global.getOwnFile
import com.pt.common.global.logProvCrash
import com.pt.common.global.toStr
import com.pt.common.media.copyFileTo
import com.pt.common.stable.*


suspend fun Fragment.reload(db_path: String, fileName: String, isBackUp: Boolean): Boolean {
    return withBackDef(false) {
        try {
            val imgPathAlso = requireContext().getOwnFile(IMAGE_FILE)
            val recordPathAlso = requireContext().getOwnFile(RECORD_FILE)
            val array: MutableList<String> = mutableListOf()
            val tempArray = run {
                requireContext().newDBTables(fileName.noteTab) {
                    getAllDataTables(fileName)
                }
            }

            tempArray.forEach {
                array.add(it.mTableName ?: return@forEach)
            }
            lifecycleScope.launchDef {
                array.forEach {
                    val dbArray = run {
                        requireContext().newDBDataSus(it.noteDb) {
                            getAllChat(it)
                        }
                    }
                    dbArray.onEachSusBack(this@reload) {
                        if (imageUrl != null) {
                            FileLate(imageUrl).loadImage(
                                db_path,
                                imgPathAlso.absolutePath,
                                isBackUp
                            )
                        }
                        if (recordPath != null) {
                            FileLate(recordPath).loadRecord(
                                db_path,
                                recordPathAlso.absolutePath,
                                isBackUp
                            )
                        }
                    }
                }
            }
            return@withBackDef true
        } catch (e: Exception) {
            e.toStr.logProvCrash("failed")
            return@withBackDef false
        }
    }
}

private suspend fun FileLate.loadImage(
    db_path: String,
    imgPathAlso: String,
    isBackUp: Boolean
): Boolean = justCoroutine {
    withBackDef(false) {
        val fileNameFrom = if (isBackUp) "$imgPathAlso/$name" else db_path + name
        val fileNameTo = if (isBackUp) "$db_path/$name" else "$imgPathAlso/$name"
        FileLate(fileNameFrom).copyFileTo(FileLate(fileNameTo))
    }
}

private suspend fun FileLate.loadRecord(
    db_path: String,
    recordPathAlso: String,
    isBackUp: Boolean
): Boolean = justCoroutine {
    withBackDef(false) {
        val fileNameFrom = if (isBackUp) "$recordPathAlso/$name" else db_path + name
        val fileNameTo = if (isBackUp) "$db_path/$name" else "$recordPathAlso/$name"
        FileLate(fileNameFrom).copyFileTo(FileLate(fileNameTo))
    }
}
