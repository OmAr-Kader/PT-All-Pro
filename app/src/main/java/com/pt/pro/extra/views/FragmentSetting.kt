package com.pt.pro.extra.views

import com.pt.common.global.*
import com.pt.common.stable.*
import com.pt.pro.extra.interfaces.ScreenListener
import com.pt.pro.extra.fasten.ActivitySettingFasten

class FragmentSetting : com.pt.common.mutual.life.GlobalSimpleFragment<ActivitySettingFasten>(),
    ScreenListener {

    private var currentSettingPage = 0

    override var lastJob: kotlinx.coroutines.Job? = null

    private val titleList: MutableList<String>
        get() {
            return mutableListOf<String>().apply {
                rec.run {
                    this@apply.add(getString(com.pt.pro.R.string.gl))
                    this@apply.add(getString(com.pt.pro.R.string.am))
                    this@apply.add(getString(com.pt.pro.R.string.ry))
                    this@apply.add(getString(com.pt.pro.R.string.fw))
                    this@apply.add(getString(com.pt.pro.R.string.mk))
                    this@apply.add(getString(com.pt.pro.R.string.zd))
                }
            }
        }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.extra.fasten.ExtraInflater.run {
                this@creBin.context.inflaterSetting()
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
                it.cardSetting.intiBack21(them.findAttr(android.R.attr.colorPrimary))
            }.root
        }

    override fun ActivitySettingFasten.onViewCreated() {
        lifecycle.addObserver(this@FragmentSetting)
        pushJob {
            launchImdMain {
                justCoroutine {
                    currentSettingPage = ctx.findIntegerPreference(SETTING_POS, 0)

                    clickPrevious.setOnClickListener(this@FragmentSetting)
                    clickNext.setOnClickListener(this@FragmentSetting)
                    mainBack.setOnClickListener(this@FragmentSetting)
                }
                justCoroutine {
                    swipePager.apply {
                        offscreenPageLimit = 1
                        scrollerChang?.let(::registerOnPageChangeCallback)
                        adapter = MyAdapter()
                        setCurrentItem(currentSettingPage, false)
                    }
                    tittlePager.apply {
                        offscreenPageLimit = 1
                        tittleChang?.let(::registerOnPageChangeCallback)
                        adapter = com.pt.pro.extra.utils.TitleAdapter(titleList)
                        setCurrentItem(currentSettingPage, false)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binder?.swipePager?.apply {
            scrollerChang?.let(::registerOnPageChangeCallback)
        }
        binder?.tittlePager?.apply {
            tittleChang?.let(::registerOnPageChangeCallback)
        }
    }

    override fun onPause() {
        binder?.swipePager?.apply {
            scrollerChang?.let(::unregisterOnPageChangeCallback)
        }
        binder?.tittlePager?.apply {
            tittleChang?.let(::unregisterOnPageChangeCallback)
        }
        super.onPause()
    }

    private suspend fun forPause(currentPage: Int) {
        ctx.updatePrefInt(SETTING_POS, currentPage)
    }


    @com.pt.common.global.UiAnn
    override fun ActivitySettingFasten.onClick(v: android.view.View) {
        pushJob {
            launchDef {
                withMain {
                    when (v) {
                        mainBack -> act.onBackPressedDispatcher.onBackPressed()
                        clickNext -> binding.tittlePager.setCurrentItem(
                            currentSettingPage + 1,
                            true
                        )
                        clickPrevious -> binding.tittlePager.setCurrentItem(
                            currentSettingPage - 1,
                            true
                        )
                    }
                }
            }
        }
    }

    override fun funChangeMain(position: Int) {
        launchImdMain {
            justCoroutine {
                forPause(position)
                currentSettingPage = position
            }
            justCoroutine {
                binding.apply {
                    tittlePager.setCurrentItem(position, true)
                    if (position == 0) {
                        clickPrevious.justInvisible()
                    } else {
                        clickPrevious.justVisible()
                    }
                    if (position == 5) {
                        clickNext.justInvisible()
                    } else {
                        clickNext.justVisible()
                    }
                }
            }
        }
    }

    private inner class MyAdapter : androidx.viewpager2.adapter.FragmentStateAdapter(
        childFragmentManager,
        lifecycle
    ) {

        override fun getItemCount(): Int = 6

        override fun createFragment(position: Int): androidx.fragment.app.Fragment = kotlin.run {
            when (position) {
                0 -> FragmentSettingGeneral()
                1 -> FragmentSettingAlarm()
                2 -> FragmentSettingGallery()
                3 -> FragmentSettingFile()
                4 -> FragmentSettingMusic()
                else -> FragmentSettingData()
            }
        }
    }

    private var tittleChang: androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback? =
        object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                funChange(position)
            }
        }

    @com.pt.common.global.UiAnn
    override fun funChange(position: Int) {
        binding.swipePager.setCurrentItem(position, true)
    }

    private var scrollerChang: androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback? =
        object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                funChangeMain(position)
            }
        }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.apply {
            frameSetting.myActMarginCon(ctx.actionBarHeight)
            cardSetting.cardAsView(ctx.actionBarHeight)
            textPt.apply {
                editAppearance()
                setTextColor(them.findAttr(android.R.attr.textColorPrimary))
            }
            tittlePager.apply {
                adapter = com.pt.pro.extra.utils.TitleAdapter(titleList)
                setCurrentItem(currentSettingPage, false)
            }
        }
    }

    override fun onDestroyView() {
        binder?.viewForAd?.removeAllViewsInLayout()
        binder?.swipePager?.removeAllViewsInLayout()
        binder?.root?.removeAllViewsInLayout()
        com.pt.pro.extra.fasten.ExtraInflater.destroyFasten()
        super.onDestroyView()
    }

}