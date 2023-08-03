package com.pt.pro.alarm.views

import android.content.ComponentName
import android.content.pm.PackageManager
import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalActivity
import com.pt.common.stable.alsoSus
import com.pt.common.stable.alsoSusBack
import com.pt.pro.alarm.release.AlarmReceiver

class MainActivityAlarm : GlobalActivity() {

    override suspend fun initActivity(i: Int) {
        com.pt.common.stable.justCoroutine {
            checkPermission {
                FragmentAlarm().alsoSus {
                    supportFragmentManager.fragmentLauncher(com.pt.common.stable.FRAGMENT_ALARM) {
                        add(i, it, com.pt.common.stable.FRAGMENT_ALARM)
                    }
                }
            }
        }
        intiOnBackPressed {
            toFinish()
        }
        updatePrefInt(com.pt.common.stable.ID_APP, com.pt.common.stable.ALARM)
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

    @com.pt.common.global.MainAnn
    override fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initViews(
        statusHeight: Int
    ) {
        two.myStatMargin(statusHeight)
        three.statBarLine(statusHeight + actionBarHeight)
    }

    override val Int.topFrameMargin: Int
        get() = this@topFrameMargin

    override fun onDestroy() {
        ComponentName(this, AlarmReceiver::class.java).apply {
            this@MainActivityAlarm.packageManager.setComponentEnabledSetting(
                this, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
            )
        }
        super.onDestroy()
    }
}
