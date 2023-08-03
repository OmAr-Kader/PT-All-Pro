package com.pt.pro.main.former

import com.pt.common.global.*
import com.pt.common.stable.alsoSus
import com.pt.common.stable.applySus
import com.pt.common.stable.launchImdMain

class MainActivity : com.pt.common.mutual.life.GlobalBaseActivity() {

    private var fasten: DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            launchImdMain {
                intiGlobalViews {
                    initActivity(it)
                }
            }
        } else {
            recreateAct()
        }
    }

    private suspend fun intiGlobalViews(a: suspend (Int) -> Unit) {
        com.pt.common.stable.justCoroutine {
            inflate(
                resources.statusBarHeight,
                actionBarHeight
            ).alsoSus {
                @com.pt.common.global.ViewAnn
                fasten = it
            }.applySus {
                one.applySus(::setContentView)
            }.also {
                a.invoke(it.two.id)
            }
        }
    }


    private fun android.content.Context.inflate(
        stat: Int,
        act: Int,
    ): DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView> {
        return android.widget.FrameLayout(
            this@inflate
        ).run root@{
            android.view.WindowManager.LayoutParams(
                com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
            ).also(this@root::setLayoutParams)
            val img = theme.findAttr(com.pt.pro.R.attr.rmoBackground).let {
                setBackgroundColor(it)
                androidx.appcompat.widget.AppCompatImageView(
                    context
                ).apply {
                    framePara(com.pt.common.stable.MATCH, act + stat) {}
                    svgReColor(it)
                    compactImage(com.pt.pro.R.drawable.card_back) {
                        setImageDrawable(this@compactImage)
                    }
                }.also(this@root::addView)
            }
            val frame = android.widget.FrameLayout(
                context
            ).apply {
                id = com.pt.pro.R.id.globalFrame
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    topMargin = stat
                }
            }.also(this@root::addView)
            com.pt.common.global.DSack(this@root, frame, img)
        }
    }


    private suspend fun initActivity(i: Int) {
        com.pt.common.stable.justCoroutine {
            FragmentMain().alsoSus {
                supportFragmentManager.fragmentLauncher(com.pt.common.stable.FRAGMENT_MAIN) {
                    add(i, it, com.pt.common.stable.FRAGMENT_MAIN)
                }
            }
        }
    }

    override fun onAttachedToWindow() {
        nightRider.let {
            window?.apply {
                myBaseActivity(!it)
                statusBarColor = theme.findAttr(com.pt.pro.R.attr.rmoBackground)
            }
        }
        super.onAttachedToWindow()
    }

    @com.pt.common.global.MainAnn
    private fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initViews(
        statusHeight: Int
    ) {
        two.myStatMargin(statusHeight)
        three.statBarLine(statusHeight + actionBarHeight)
    }


    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        fasten?.initViews(resources.statusBarHeight)
    }

    override fun onDestroy() {
        fasten = null
        super.onDestroy()
    }

}