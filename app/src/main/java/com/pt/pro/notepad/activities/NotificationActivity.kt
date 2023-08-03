package com.pt.pro.notepad.activities

import com.pt.common.global.*
import com.pt.common.global.fragmentLauncher
import com.pt.common.stable.alsoSus

class NotificationActivity : com.pt.common.mutual.life.GlobalSubActivity() {

    private var fragmentNotify: com.pt.pro.notepad.fragments.FragmentNotification? = null

    override suspend fun initSubActivity(i: Int) {
        com.pt.common.stable.justCoroutine {
            com.pt.pro.notepad.fragments.FragmentNotification().alsoSus {
                fragmentNotify = it
                supportFragmentManager.fragmentLauncher(com.pt.common.stable.FRAGMENT_NOTIFICATION) {
                    add(i, it, com.pt.common.stable.FRAGMENT_NOTIFICATION)
                }
            }
        }
        intiOnBackPressed {
            if (fragmentNotify?.onMyOption == true) {
                finish()
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

    override fun onDestroy() {
        fragmentNotify = null
        super.onDestroy()
    }

}