package com.pt.pro.extra.views

import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import com.pt.common.global.*
import com.pt.common.moderator.recycler.NoAnimGridManager
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.databinding.FragmentSettingGeneralBinding
import com.pt.pro.extra.utils.ColorAdapter
import com.pt.pro.extra.utils.SettingHelper
import com.pt.pro.extra.utils.SettingHelper.getStylesListLight
import com.pt.pro.extra.utils.SettingHelper.getStylesListNight

class FragmentSettingGeneral : GlobalSimpleFragment<FragmentSettingGeneralBinding>() {

    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    private inline val arrayStyleNight
        get() = ctx.getStylesListNight()

    private inline val arrayStyleLight
        get() = ctx.getStylesListLight()

    private var colorAdapter: ColorAdapter? = null

    private var screenWidth: Int = 0

    private inline val searchValues: Array<Int>
        get() {
            return run {
                arrayOf(
                    SEARCH_GOOGLE,
                    SEARCH_YANDEX,
                    SEARCH_DUCK,
                    SEARCH_BAIDU,
                    SEARCH_BING,
                    SEARCH_AOL
                )
            }
        }


    private inline val displaysSearchValues: Array<String>
        get() {
            return arrayOf(
                com.pt.common.BuildConfig.GOOGLE_SEARCH,
                com.pt.common.BuildConfig.YANDEX_SEARCH,
                com.pt.common.BuildConfig.DUCK_SEARCH,
                com.pt.common.BuildConfig.BAIDU_SEARCH,
                com.pt.common.BuildConfig.BING_SEARCH,
                com.pt.common.BuildConfig.AOL_SEARCH
            )
        }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            FragmentSettingGeneralBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }

    @com.pt.common.global.UiAnn
    override fun FragmentSettingGeneralBinding.onViewCreated() {
        lifecycle.addObserver(this@FragmentSettingGeneral)
        launchImdMain {
            justCoroutineMain {
                act.windowManager?.fetchDimensionsMan {
                    screenWidth = this@fetchDimensionsMan.width
                }
                switchDarkMode.apply {
                    isChecked = nightRider
                    jumpDrawablesToCurrentState()
                    setOnClickListener(this@FragmentSettingGeneral)
                }
            }
            justCoroutineMain {
                intiRec()
            }
            justCoroutineMain {
                frameSearchText.setOnClickListener(this@FragmentSettingGeneral)
                ctx.fetchPicker.apply {
                    minValue = 0
                    displaysSearchValues.let {
                        maxValue = it.size - 1
                        displayedValues = it
                    }
                    value = ctx.findIntegerPrefDb(ID_SEARCH, SEARCH_ENGINE, SEARCH_GOOGLE).let {
                        searchValues.indexOf(it)
                    }
                    addSenseListener(false) { _, _, type ->
                        if (type == UP_SEN) {
                            searchValues.getOrNull(value)?.also { aa ->
                                toCatchSackAfter(382, SAVE_CONST) {
                                    launchDef {
                                        ctx.updatePrefInt(SEARCH_ENGINE, aa)
                                    }
                                }.postBackAfter()
                            }
                        }
                    }
                }.also {
                    frameSearch.addView(it)
                }
                depWall.setOnCheckedChangeListener(widCheckListener)
                darkWall.setOnCheckedChangeListener(widCheckListener)
                whiteWall.setOnCheckedChangeListener(widCheckListener)
                ctx.findIntegerPref(ID_WIDGET, KEY_WIDGET, WIDGET_DEP).let {
                    when (it) {
                        WIDGET_DEP -> {
                            depWall.apply {
                                isChecked = true
                                jumpDrawablesToCurrentState()
                            }
                        }
                        WIDGET_DARK -> {
                            darkWall.apply {
                                isChecked = true
                                jumpDrawablesToCurrentState()
                            }
                        }
                        else -> {
                            whiteWall.apply {
                                isChecked = true
                                jumpDrawablesToCurrentState()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun FragmentSettingGeneralBinding.intiRec() {
        act.windowManager.fetchDimensionsMan {
            recStyle.apply {
                isNestedScrollingEnabled = false
                ((this@fetchDimensionsMan.width - 30F.toPixel).toFloat() / 57F.toPixel.toFloat()).toInt().let {
                    layoutManager = NoAnimGridManager(
                        ctx,
                        it
                    )
                }
                (if (nightRider) arrayStyleNight else arrayStyleLight).also { stylesList ->
                    ctx.fetchStyle.let { st ->
                        stylesList.indexOfFirst {
                            st == it.two
                        }.let {
                            if (it == -1) 0 else it
                        }
                    }.let { stylePosition ->
                        ColorAdapter(
                            stylesList,
                            stylePosition,
                        ) { style, _, _ ->
                            style.let(::onStyleClick)
                        }.apply {
                            adapter = this
                            colorAdapter = this
                        }
                        scrollToPosition(stylePosition)
                    }
                }
            }
        }
    }

    private var widCheckListener: CompoundButton.OnCheckedChangeListener? =
        CompoundButton.OnCheckedChangeListener { p0, p1 ->
            if (p1) {
                binding.apply {
                    when (p0) {
                        depWall -> {
                            darkWall.isChecked = false
                            whiteWall.isChecked = false
                            WIDGET_DEP
                        }
                        darkWall -> {
                            depWall.isChecked = false
                            whiteWall.isChecked = false
                            WIDGET_DARK
                        }
                        else -> {
                            depWall.isChecked = false
                            darkWall.isChecked = false
                            WIDGET_WHITE
                        }
                    }.let {
                        launchDef {
                            ctx.updatePrefInt(KEY_WIDGET, it)
                            withDefault {
                                ctx.updateWidget(it)
                            }
                        }
                    }
                }
            }
        }


    override fun FragmentSettingGeneralBinding.onClick(v: android.view.View) {
        if (v == switchDarkMode) {
            colorAdapter?.colorPosition?.let { updateNight(it) }
        } else if (v == frameSearchText) {
            displaySearch()
        }
    }

    private fun FragmentSettingGeneralBinding.displaySearch() {
        pushJob {
            launchDef {
                withMain {
                    if (frameSearch.isVis) {
                        frameSearch.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            searchImage.startAnimation(this)
                        }
                        mainFrameSearch.goneHandlerSus()
                    } else {
                        mainFrameSearch.justVisibleSus()
                        frameSearch.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            searchImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private fun updateNight(stylePosition: Int) {
        launchDef {
            if (nightRider) {
                ctx.updateBooleanPrefSus(NIGHT, RIDERS, false)
                ctx.updateIntPrefSus(
                    NEW_STYLE,
                    NEW_MY_THEM,
                    SettingHelper.fromStyleToNumber(arrayStyleLight[stylePosition].two)
                )
            } else {
                ctx.updateBooleanPrefSus(NIGHT, RIDERS, true)
                ctx.updateIntPrefSus(
                    NEW_STYLE,
                    NEW_MY_THEM,
                    SettingHelper.fromStyleToNumber(arrayStyleNight[stylePosition].two)
                )
            }
            justCoroutine {
                com.pt.pro.extra.fasten.ExtraInflater.destroyFasten()
                com.pt.pro.gallery.fasten.GalleryInflater.destroyFasten()
                com.pt.pro.file.fasten.FileInflater.destroyFasten()
                com.pt.pro.musical.fasten.MusicInflater.destroyFasten()
                com.pt.pro.main.odd.MainFasten.destroyFasten()
                com.pt.pro.alarm.fasten.AlarmFasten.destroyFasten()
            }
            justCoroutine {
                act.recreateSetting()
            }
        }
    }

    private suspend fun AppCompatActivity.recreateSetting() {
        kotlinx.coroutines.delay(200L)
        withDefault {
            finish()
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    android.content.Intent.FLAG_ACTIVITY_NO_HISTORY or
                    android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(NEED_CLEAN, true)
            this@FragmentSettingGeneral.startActivity(intent)
        }
    }

    private fun onStyleClick(style: Int) {
        launchImdMain {
            ctx.updateIntPrefSus(NEW_STYLE, NEW_MY_THEM, SettingHelper.fromStyleToNumber(style))
            justCoroutine {
                com.pt.pro.extra.fasten.ExtraInflater.destroyFasten()
                com.pt.pro.gallery.fasten.GalleryInflater.destroyFasten()
                com.pt.pro.file.fasten.FileInflater.destroyFasten()
                com.pt.pro.musical.fasten.MusicInflater.destroyFasten()
                com.pt.pro.main.odd.MainFasten.destroyFasten()
            }
            act.recreateSetting()
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.intiRec()
    }

    override fun onDestroyView() {
        binder?.recStyle?.adapter = null
        colorAdapter = null
        super.onDestroyView()
    }

}