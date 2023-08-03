package com.pt.common.mutual.life

import com.pt.common.global.*
import com.pt.common.stable.alsoSus
import com.pt.common.stable.applySus
import com.pt.common.stable.launchImdMain

abstract class GlobalActivity : GlobalBaseActivity() {

    internal var fasten: DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>? =
        null

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

    override fun onAttachedToWindow() {
        window?.apply {
            myBaseActivity(theme.findAttr(android.R.attr.textColorPrimary) == fetchColor(com.pt.pro.R.color.bla))
        }
        super.onAttachedToWindow()
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
            setBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackground))
            val img = androidx.appcompat.widget.AppCompatImageView(
                context
            ).apply {
                framePara(com.pt.common.stable.MATCH, act + stat) {}
                svgReColor(theme.findAttr(android.R.attr.colorPrimary))
                compactImage(com.pt.pro.R.drawable.card_back) {
                    setImageDrawable(this@compactImage)
                }
            }.also(this@root::addView)
            val frame = android.widget.FrameLayout(
                context
            ).apply {
                id = com.pt.pro.R.id.globalFrame
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                    topMargin = stat.topFrameMargin
                }
            }.also(this@root::addView)
            com.pt.common.global.DSack(this@root, frame, img)
        }
    }

    abstract val Int.topFrameMargin: Int

    abstract suspend fun initActivity(@androidx.annotation.IdRes i: Int)

    @com.pt.common.global.MainAnn
    abstract fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initViews(
        statusHeight: Int
    )

    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        fasten?.initViews(resources.statusBarHeight)
    }

    override fun onDestroy() {
        com.pt.common.mutual.pref.DbPrefInitializer.preference = null
        com.pt.common.stable.catchy(Unit) {
            fasten?.one?.removeAllViewsInLayout()
        }
        super.onDestroy()
        fasten = null
    }
}