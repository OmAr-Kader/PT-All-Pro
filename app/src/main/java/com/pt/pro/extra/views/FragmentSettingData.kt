package com.pt.pro.extra.views

import androidx.annotation.ColorInt
import com.pt.common.global.*
import com.pt.common.moderator.recycler.NoAnimGridManager
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.databinding.FragmentSettingDataBinding
import com.pt.pro.extra.utils.ColorDataAdapter
import com.pt.pro.extra.utils.SettingHelper.getDataListColors
import com.pt.pro.notepad.objects.*

class FragmentSettingData : GlobalSimpleFragment<FragmentSettingDataBinding>() {

    private var currentOpt = DATA_COL
    private var colorAdapter: ColorDataAdapter? = null
    private val colorsList: MutableList<Int> = mutableListOf()

    @ColorInt
    @Volatile
    private var dat = 0

    @ColorInt
    @Volatile
    private var imp = 0

    @ColorInt
    @Volatile
    private var lin = 0

    @Suppress("SpellCheckingInspection")
    @ColorInt
    @Volatile
    private var rem = 0

    @ColorInt
    @Volatile
    private var sca = 0

    @ColorInt
    @Volatile
    private var ema = 0

    private inline val whiteColor get() = android.graphics.Color.WHITE

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            FragmentSettingDataBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }

    @com.pt.common.global.UiAnn
    override fun FragmentSettingDataBinding.onViewCreated() {
        launchDef {
            withBack {
                ctx.applySusBack {
                    dat = findIntegerPrefDb(COL_KEEP, DATA_COL, ctx.fetchColor(R.color.dcl))
                    imp = findIntegerPrefDb(COL_KEEP, IMP_COL, ctx.fetchColor(R.color.iml))
                    lin = findIntegerPrefDb(COL_KEEP, LINK_COL, ctx.fetchColor(R.color.glm))
                    rem = findIntegerPrefDb(COL_KEEP, REM_COL, ctx.fetchColor(R.color.sml))
                    sca = findIntegerPrefDb(COL_KEEP, SCAN_COL, ctx.fetchColor(R.color.dcl))
                    ema = findIntegerPrefDb(COL_KEEP, MAIL_COL, ctx.fetchColor(R.color.dcl))
                }.letSusBack {
                    mutableListOf<Int>().applySusBack {
                        add(dat)
                        add(imp)
                        add(lin)
                        add(rem)
                        add(sca)
                        add(ema)
                        addAll(it.getDataListColors())
                    }.letSusBack {
                        colorsList.addAll(it.distinct())
                    }
                }.applySusBack {
                    intiViews()
                }
            }
        }
    }

    private fun FragmentSettingDataBinding.intiViews() {
        launchImdMain {
            withMain {
                intiRec()
                dataColorSet.setTextColor(them.findAttr(android.R.attr.colorAccent))
                dataColorSet.setOnClickListener(this@FragmentSettingData)
                importantColorSet.setOnClickListener(this@FragmentSettingData)
                linkColorSet.setOnClickListener(this@FragmentSettingData)
                rememberColorSet.setOnClickListener(this@FragmentSettingData)
                mailColorSet.setOnClickListener(this@FragmentSettingData)

                photoCompress.apply {
                    isChecked = ctx.findBooleanPreference(IMG_COMP, true)
                    jumpDrawablesToCurrentState()
                    setOnCheckedChangeListener { _, b ->
                        launchDef {
                            ctx.updatePrefBoolean(IMG_COMP, b)
                        }
                    }
                }
                includeDone.apply {
                    isChecked = ctx.findBooleanPreference(INC_DONE, false)
                    jumpDrawablesToCurrentState()
                    setOnCheckedChangeListener { _, b ->
                        launchDef {
                            ctx.updatePrefBoolean(INC_DONE, b)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun FragmentSettingDataBinding.onClick(v: android.view.View) {
        val a = when (v) {
            dataColorSet -> {
                dataColorSet.setTextColor(them.findAttr(android.R.attr.colorAccent))
                importantColorSet.setTextColor(whiteColor)
                linkColorSet.setTextColor(whiteColor)
                rememberColorSet.setTextColor(whiteColor)
                mailColorSet.setTextColor(whiteColor)
                currentOpt = DATA_COL
                colorsList.indexOf(dat)
            }
            importantColorSet -> {
                dataColorSet.setTextColor(whiteColor)
                importantColorSet.setTextColor(them.findAttr(android.R.attr.colorAccent))
                linkColorSet.setTextColor(whiteColor)
                rememberColorSet.setTextColor(whiteColor)
                mailColorSet.setTextColor(whiteColor)
                currentOpt = IMP_COL
                colorsList.indexOf(imp)
            }
            linkColorSet -> {
                dataColorSet.setTextColor(whiteColor)
                importantColorSet.setTextColor(whiteColor)
                linkColorSet.setTextColor(them.findAttr(android.R.attr.colorAccent))
                rememberColorSet.setTextColor(whiteColor)
                mailColorSet.setTextColor(whiteColor)
                currentOpt = LINK_COL
                colorsList.indexOf(lin)
            }
            rememberColorSet -> {
                dataColorSet.setTextColor(whiteColor)
                importantColorSet.setTextColor(whiteColor)
                linkColorSet.setTextColor(whiteColor)
                rememberColorSet.setTextColor(them.findAttr(android.R.attr.colorAccent))
                mailColorSet.setTextColor(whiteColor)
                currentOpt = REM_COL
                colorsList.indexOf(rem)
            }
            mailColorSet -> {
                dataColorSet.setTextColor(whiteColor)
                importantColorSet.setTextColor(whiteColor)
                linkColorSet.setTextColor(whiteColor)
                rememberColorSet.setTextColor(whiteColor)
                mailColorSet.setTextColor(them.findAttr(android.R.attr.colorAccent))
                currentOpt = MAIL_COL
                colorsList.indexOf(ema)
            }
            else -> {
                dataColorSet.setTextColor(them.findAttr(android.R.attr.colorAccent))
                importantColorSet.setTextColor(whiteColor)
                linkColorSet.setTextColor(whiteColor)
                rememberColorSet.setTextColor(whiteColor)
                mailColorSet.setTextColor(whiteColor)
                currentOpt = DATA_COL
                colorsList.indexOf(dat)
            }
        }
        colorAdapter?.apply {
            notifyItemChanged(a)
            notifyItemChanged(colorPosition)
            colorPosition = a
        }
    }

    @androidx.annotation.UiThread
    private fun FragmentSettingDataBinding.intiRec() {
        act.windowManager.fetchDimensionsMan {
            recyclerDataColor.apply {
                ((this@fetchDimensionsMan.width - 30F.toPixel).toFloat() / 57F.toPixel).let {
                    layoutManager = NoAnimGridManager(
                        ctx,
                        it.toInt()
                    )
                    ColorDataAdapter(colorsList, 0) { _, color, _ ->
                        color.let(::onColorClick)
                    }.apply {
                        colorAdapter = this
                        adapter = this
                    }
                }
            }
        }
    }

    private fun onColorClick(color: Int) {
        launchDef {
            ctx.updatePrefInt(currentOpt, color)
            when (currentOpt) {
                DATA_COL -> {
                    dat = color
                }
                IMP_COL -> {
                    imp = color
                }
                LINK_COL -> {
                    lin = color
                }
                REM_COL -> {
                    rem = color
                }
                SCAN_COL -> {
                    sca = color
                }
                MAIL_COL -> {
                    ema = color
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.intiRec()
    }

    override fun onDestroyView() {
        binder?.recyclerDataColor?.adapter = null
        currentOpt = DATA_COL
        colorAdapter?.onAdapterDestroy()
        colorAdapter = null
        colorsList.clear()
        super.onDestroyView()
    }

}