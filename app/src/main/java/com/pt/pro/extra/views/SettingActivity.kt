package com.pt.pro.extra.views

import com.pt.common.global.*
import com.pt.common.stable.NEED_CLEAN
import com.pt.common.stable.alsoSus

class SettingActivity : com.pt.common.mutual.life.GlobalActivity() {

    override suspend fun initActivity(i: Int) {
        com.pt.common.stable.justCoroutineMain {
            if (intent.getBooleanExtra(com.pt.common.stable.IS_SETTING, true)) {
                FragmentSetting().alsoSus {
                    supportFragmentManager.fragmentLauncher(com.pt.common.stable.FRAGMENT_SETTING) {
                        add(i, it, com.pt.common.stable.FRAGMENT_SETTING)
                    }
                }
            } else {
                FragmentAbout().alsoSus {
                    supportFragmentManager.fragmentLauncher(com.pt.common.stable.FRAGMENT_ABOUT) {
                        add(i, it, com.pt.common.stable.FRAGMENT_ABOUT)
                    }
                }
            }
        }
        intiOnBackPressed {
            if (intent.getBooleanExtra(NEED_CLEAN, false)) {
                finish()
                android.content.Intent(
                    this@SettingActivity,
                    com.pt.pro.main.former.MainActivity::class.java
                ).apply {
                    flags = android.content.Intent.FLAG_ACTIVITY_NO_HISTORY or
                            android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra(com.pt.common.stable.NOT_FOR_PRO, true)
                }.also(this@SettingActivity::startActivity)
            } else {
                toFinish()
            }
        }
    }

    override fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initViews(statusHeight: Int) {
        two.myStatMargin(statusHeight)
        three.statBarLine(statusHeight + actionBarHeight)
    }

    override val Int.topFrameMargin: Int
        get() = this@topFrameMargin

}