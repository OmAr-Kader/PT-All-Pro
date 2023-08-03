package com.pt.pro.notepad.activities

import com.pt.common.global.*
import com.pt.common.global.fragmentLauncher
import com.pt.common.mutual.life.GlobalSubActivity
import com.pt.common.stable.alsoSus
import com.pt.pro.notepad.fragments.FragmentScanner

class ScannerActivity : GlobalSubActivity() {

    override suspend fun initSubActivity(i: Int) {
        com.pt.common.stable.withMainNormal {
            FragmentScanner().alsoSus {
                supportFragmentManager.fragmentLauncher(com.pt.common.stable.FRAGMENT_SCANNER) {
                    add(i, it, com.pt.common.stable.FRAGMENT_SCANNER)
                }
            }
        }
    }

    override fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initSubViews(
        statusHeight: Int
    ) {
        two.myStatMargin(statusHeight)
        three.statBarLine(statusHeight + actionBarHeight)
    }

    override val Int.topFrameMargin: Int
        get() = this@topFrameMargin

}
