package com.pt.pro.extra.screener

import com.pt.common.global.*
import com.pt.common.stable.alsoSus
import com.pt.common.stable.alsoSusBack

class MainScreen : com.pt.common.mutual.life.GlobalActivity() {

    override suspend fun initActivity(i: Int) {
        com.pt.common.stable.justCoroutine {
            checkPermission {
                FragmentScreen().alsoSus { newFragment ->
                    supportFragmentManager.fragmentLauncher(com.pt.common.stable.FRAGMENT_SCREEN) {
                        add(i, newFragment, com.pt.common.stable.FRAGMENT_SCREEN)
                    }
                }
            }
        }
        intiOnBackPressed {
            toFinish()
        }
    }

    private suspend fun checkPermission(a: suspend () -> Unit) {
        intent?.getBooleanExtra(com.pt.common.stable.SHORTCUT, false)?.alsoSusBack {
            if (it && (!hasExternalReadWritePermission || !hasVoicePermission)) {
                toFinish()
            } else {
                a.invoke()
            }
        } ?: a.invoke()
    }

    override fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initViews(
        statusHeight: Int
    ) {
        two.myStatMargin(statusHeight)
        three.statBarLine(statusHeight + actionBarHeight)
    }

    override val Int.topFrameMargin: Int
        get() = this@topFrameMargin

}