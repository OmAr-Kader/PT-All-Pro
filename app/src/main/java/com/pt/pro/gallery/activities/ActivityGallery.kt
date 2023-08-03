package com.pt.pro.gallery.activities

import com.pt.common.global.*
import com.pt.common.stable.alsoSus
import com.pt.common.stable.alsoSusBack

class ActivityGallery : com.pt.common.mutual.life.GlobalActivity() {

    private var browser: FragmentGallery? = null

    override suspend fun initActivity(i: Int) {
        com.pt.common.stable.justCoroutine {
            checkPermission {
                com.pt.common.stable.newFragmentGallery {
                    browser = this@newFragmentGallery
                    return@newFragmentGallery this@newFragmentGallery
                }.alsoSus {
                    supportFragmentManager.fragmentLauncher(com.pt.common.stable.FRAGMENT_MAIN_GALLERY) {
                        add(com.pt.pro.R.id.globalFrame, it, com.pt.common.stable.FRAGMENT_MAIN_GALLERY)
                    }
                }
            }
        }
        intiOnBackPressed {
            browser?.also {
                if (it.onBackCheck) {
                    toFinish()
                }
            } ?: toFinish()
        }
        updatePrefInt(com.pt.common.stable.ID_APP, com.pt.common.stable.GALLERY)
    }


    private suspend fun checkPermission(a: suspend () -> Unit) {
        intent?.getBooleanExtra(com.pt.common.stable.SHORTCUT, false)?.alsoSusBack {
            if (it && !hasExternalReadWritePermission) {
                toFinish()
            } else {
                a.invoke()
            }
        } ?: a.invoke()
    }

    @Suppress("DEPRECATION")
    @com.pt.common.global.MainAnn
    override fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initViews(statusHeight: Int) {
        three.statBarLine(statusHeight + actionBarHeight)
    }

    override val Int.topFrameMargin: Int
        get() = 0

    @com.pt.common.global.MainAnn
    override fun onDestroy() {
        browser = null
        com.pt.pro.gallery.fasten.GalleryInflater.destroyFasten()
        super.onDestroy()
    }

}

