package com.pt.pro.gallery.activities

import com.pt.common.global.*
import com.pt.common.stable.alsoSus
import com.pt.common.stable.newFragmentDisPlay

class DisplayMedia : com.pt.common.mutual.life.GlobalSubActivity() {

    private var browser: FragmentDisPlay? = null

    @com.pt.common.global.WorkerAnn
    override suspend fun initSubActivity(i: Int) {
        com.pt.common.stable.justCoroutine {
            newFragmentDisPlay {
                browser = this@newFragmentDisPlay
                return@newFragmentDisPlay this@newFragmentDisPlay
            }.alsoSus {
                supportFragmentManager.fragmentLauncher(com.pt.common.stable.FRAGMENT_DISPLAY) {
                    add(i, it, com.pt.common.stable.FRAGMENT_DISPLAY)
                }
            }
        }
        intiOnBackPressed {
            browser?.onBack() ?: finish()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onAttachedToWindow() {
        intent.getIntExtra(
            com.pt.common.stable.DISPLAY_TYPE,
            com.pt.common.stable.DISPLAY_NORMAL
        ).also {
            if (it == com.pt.common.stable.DISPLAY_PENDING || it == com.pt.common.stable.DISPLAY_HIDDEN ||
                it == com.pt.common.stable.DISPLAY_HIDDEN_TIME
            ) {
                window.apply {
                    statusBarColor = fetchColor(com.pt.pro.R.color.hbd)
                    myBaseActivity(false)
                }
            } else {
                super.onAttachedToWindow()
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initSubViews(statusHeight: Int) {
        three.statBarLine(statusHeight + actionBarHeight)
    }

    override val Int.topFrameMargin: Int
        get() = 0

    @com.pt.common.global.MainAnn
    override fun onDestroy() {
        browser = null
        super.onDestroy()
    }


}
