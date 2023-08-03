package com.pt.common.mutual.life

import android.content.res.Configuration
import android.os.Bundle
import com.pt.common.global.*
import com.pt.common.stable.*

abstract class GlobalSubActivity : GlobalBaseActivity() {

    internal var fasten: DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>? =
        null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            launchImdMain {
                intiSubBViews {
                    initSubActivity(it)
                }
            }
        } else {
            recreateAct()
        }
    }

    override fun onAttachedToWindow() {
        window?.apply {
            myBaseActivity(
                theme.findAttr(android.R.attr.textColorPrimary) == fetchColor(
                    com.pt.pro.R.color.bla
                )
            )
        }
        super.onAttachedToWindow()
    }

    private suspend fun intiSubBViews(a: suspend (Int) -> Unit) {
        justCoroutine {
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
                MATCH, MATCH
            ).also(this@root::setLayoutParams)
            setBackgroundColor(theme.findAttr(com.pt.pro.R.attr.rmoBackHint))
            val img = androidx.appcompat.widget.AppCompatImageView(
                context
            ).apply {
                framePara(MATCH, act + stat) {}
                svgReColor(theme.findAttr(android.R.attr.colorPrimary))
                compactImage(com.pt.pro.R.drawable.card_back) {
                    setImageDrawable(this@compactImage)
                }
            }.also(this@root::addView)
            val frame = android.widget.FrameLayout(
                context
            ).apply {
                id = com.pt.pro.R.id.globalFrame
                framePara(MATCH, MATCH) {
                    topMargin = stat.topFrameMargin
                }
            }.also(this@root::addView)
            com.pt.common.global.DSack(this@root, frame, img)
        }
    }

    abstract val Int.topFrameMargin: Int

    abstract suspend fun initSubActivity(i: Int)

    abstract fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initSubViews(
        statusHeight: Int
    )

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        fasten?.initSubViews(resources.statusBarHeight)
    }

    override fun onDestroy() {
        catchy(Unit) {
            fasten?.one?.removeAllViewsInLayout()
        }
        super.onDestroy()
        fasten = null
    }

}