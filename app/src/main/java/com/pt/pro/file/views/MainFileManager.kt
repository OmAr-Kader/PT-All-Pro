package com.pt.pro.file.views

import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalActivity
import com.pt.common.stable.*

class MainFileManager : GlobalActivity() {

    private var fileBrowser: FragmentFile? = null

    override suspend fun initActivity(i: Int) {
        justCoroutine {
            checkPermission {
                newFragmentFile {
                    fileBrowser = this@newFragmentFile
                    return@newFragmentFile this@newFragmentFile
                }.alsoSus { newFragment ->
                    supportFragmentManager.fragmentLauncher(FRAGMENT_FILE) {
                        add(i, newFragment, FRAGMENT_FILE)
                    }
                }
            }
        }
        intiOnBackPressed {
            fileBrowser?.onBack()
        }
        updatePrefInt(ID_APP, FILE_MAN)
    }

    private suspend fun checkPermission(a: suspend () -> Unit) {
        intent?.getBooleanExtra(SHORTCUT, false)?.alsoSusBack {
            if (it && !hasExternalReadWritePermission) {
                toFinish()
            } else {
                a.invoke()
            }
        } ?: a.invoke()
    }

    @com.pt.common.global.MainAnn
    override fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initViews(statusHeight: Int) {
        three.statBarLine(statusHeight + actionBarHeight)
    }

    override val Int.topFrameMargin: Int
        get() = 0

    @com.pt.common.global.MainAnn
    override fun onDestroy() {
        fileBrowser = null
        com.pt.pro.file.fasten.FileInflater.destroyFasten()
        super.onDestroy()
    }

}