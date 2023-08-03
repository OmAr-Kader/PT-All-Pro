package com.pt.pro.extra.screener

import com.pt.common.global.*
import com.pt.common.stable.*
import com.pt.pro.extra.utils.SettingHelper

class FragmentScreen : BaseScreen() {

    private var lastItem: Int = SettingHelper.SCREEN_SWIPER

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.extra.fasten.ExtraInflater.run { this@creBin.context.inflaterScreenCapture() }.also {
                @com.pt.common.global.ViewAnn
                binder = it
                ctx.compactImage(com.pt.pro.R.drawable.circle) {
                    this?.let { dra ->
                        androidx.core.graphics.drawable.DrawableCompat.setTintList(
                            dra,
                            them.findAttr(android.R.attr.colorPrimary).toTintList,
                        )
                    }
                    it.seekLaunch.thumb = this
                }
            }.root
        }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.extra.fasten.ScreenCaptureFasten.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentScreen)
        launchMain {
            screen = ctx.findBooleanPreference(IS_SCREEN, true)
            fileScreenCreator()
            withMain {
                swiperMyHead?.setRunnable {
                    screen = it
                    checkIfEnableBe(optionHead)
                }
                mainBack.setOnClickListener(this@FragmentScreen)
                clickPrevious.setOnClickListener(this@FragmentScreen)
                clickNext.setOnClickListener(this@FragmentScreen)
                seekLaunch.apply {
                    progress = 50
                    max = 1000
                    addSenseListener(false) { _, _, _ ->
                        if (lockScreen && optionHead.isNullService) {
                            if (!isWait) {
                                isWait = true
                                checkDrawOverlay()
                            }
                        }
                    }
                }
                seekLaunch.setOnSeekBarChangeListener(this@FragmentScreen)
            }
            val lastTable = ctx.findStringPrefNullable(LAST_NOTE_TABLE)
            withMain {
                swipePager.apply {
                    offscreenPageLimit = 1
                    lastItem = if (lastTable == null) SettingHelper.SCREEN_SWIPER else SettingHelper.NOTE_HEAD
                    scrollerChang?.let(::registerOnPageChangeCallback)
                    adapter = MyAdapter(lastTable)
                    setCurrentItem(optionHead, false)
                }
                tittlePager.apply {
                    offscreenPageLimit = 1
                    tittleChang?.let(::registerOnPageChangeCallback)
                    adapter = com.pt.pro.extra.utils.TitleAdapter(titleList)
                    setCurrentItem(optionHead, false)
                }
            }
        }
    }

    private inline val titleList: MutableList<String>
        get() {
            return mutableListOf<String>().apply {
                rec.run {
                    this@apply.add(getString(com.pt.pro.R.string.fc))
                    this@apply.add(getString(com.pt.pro.R.string.nh))
                    this@apply.add(getString(com.pt.pro.R.string.le))
                    this@apply.add(getString(com.pt.pro.R.string.zd))
                }
            }
        }

    @com.pt.common.global.UiAnn
    override fun onProgressChanged(seekBar: android.widget.SeekBar?, p: Int, fromUser: Boolean) {
        if (gateOpen) {
            return
        }
        binding.seekLaunch.runCatching {
            val one = kotlin.math.abs(mOriginProgress - p) < 300
            val two = (!lockScreen || !optionHead.isNullService)

            if (one && two) {
                isActivated = true
                if (p > 950) {
                    progress = 950
                } else if (p < 50) {
                    progress = 50
                }
                textSwiper(p)
                mOriginProgress = progress
            } else {
                isActivated = false
                progress = mOriginProgress
            }
        }.onFailure {
            binding.seekLaunch.progress = mOriginProgress
        }
    }

    override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {
        firstProgress = binding.seekLaunch.progress
        unPost(checkRunnable.two)
    }

    @com.pt.common.global.UiAnn
    override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
        binding.seekLaunch.apply {
            if (firstProgress == progress) return@apply
            if (optionHead.isNullService) {
                if (progress > 800) {
                    progress = 950
                    lockScreen = true
                    startCaptureService()
                } else {
                    animateSeekBar(5)
                }
            } else {
                if (progress < 200) {
                    progress = 50
                    lockScreen = true
                    stopRecordingService(optionHead)
                } else {
                    animateSeekBar(950)
                }
            }
        }
        checkRunnable.rKTSack(1000L).postAfter()
    }

    override fun onResume() {
        super.onResume()
        checkRunnable.rKTSack(100L).postAfter()
        binding.swipePager.apply {
            scrollerChang?.let(::registerOnPageChangeCallback)
        }
        binding.tittlePager.apply {
            tittleChang?.let(::registerOnPageChangeCallback)
        }
    }

    override fun onPause() {
        binding.swipePager.apply {
            scrollerChang?.let(::unregisterOnPageChangeCallback)
        }
        binding.tittlePager.apply {
            tittleChang?.let(::unregisterOnPageChangeCallback)
        }
        unPostAll()
        super.onPause()
    }

    private suspend fun forPause(position: Int, prePos: Int) {
        if (position != prePos) {
            ctx.updatePrefInt(SCR_OPT, position)
        } else return
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

    @com.pt.common.global.UiAnn
    override fun funChangeMain(position: Int) {
        launchImdMain {
            forPause(position, optionHead)
            justCoroutine {
                optionHead = position
                binding.apply {
                    tittlePager.setCurrentItem(position, true)
                    gateOpen = true
                    toCatchSackAfter(324, 200L) {
                        gateOpen = false
                    }.postAfter()
                    if (position == 0) {
                        clickPrevious.justInvisible()
                    } else {
                        clickPrevious.justVisible()
                    }
                    if (position == lastItem) {
                        clickNext.justInvisible()
                    } else {
                        clickNext.justVisible()
                    }
                    optionHead.isNullService.let {
                        turnCurrent(it)
                        seekLaunch.progress = if (it) {
                            slideText.justVisible()
                            mOriginProgress = 50
                            50
                        } else {
                            slideText.justGone()
                            mOriginProgress = 950
                            950
                        }
                    }
                }
                checkIfEnableBe(optionHead)
            }
        }
    }

    private fun checkIfEnableBe(optionHead: Int) {
        optionHead.doCheck.let {
            lockScreen = !it
            binding.seekLaunch.displayCheck(it)
        }
    }

    private inline val Int.doCheck: Boolean
        @com.pt.common.global.WorkerAnn
        get() {
            return if (!this@doCheck.isNullService) {
                true
            } else {
                when (this@doCheck) {
                    SettingHelper.SCREEN_SWIPER -> ctx.canFloat && dataScreen != null
                    SettingHelper.FLOATING_HEAD -> {
                        if (screen) ctx.canFloat && dataScreen != null else ctx.canFloat
                    }
                    SettingHelper.NAVIGATION_HEAD -> ctx.canFloat
                    else -> ctx.canFloat
                }
            }
        }

    @com.pt.common.global.UiAnn
    private fun androidx.appcompat.widget.AppCompatSeekBar.displayCheck(status: Boolean) {
        isActivated = status
        if (status) {
            ctx.compactImage(com.pt.pro.R.drawable.circle) {
                this?.let {
                    androidx.core.graphics.drawable.DrawableCompat.setTintList(
                        it,
                        them.findAttr(android.R.attr.colorPrimary).toTintList,
                    )
                }
                thumb = this
            }
        } else {
            ctx.compactImage(com.pt.pro.R.drawable.lock_thumb) {
                thumb = this
            }
        }
    }

    private inner class MyAdapter(private val lastTable: String?) : androidx.viewpager2.adapter.FragmentStateAdapter(
        childFragmentManager,
        lifecycle
    ) {

        override fun getItemCount(): Int = if (lastTable == null) SettingHelper.MAX_HEAD else SettingHelper.MAX_HEAD + 1

        override fun createFragment(position: Int): androidx.fragment.app.Fragment = kotlin.run {
            when (position) {
                SettingHelper.SCREEN_SWIPER -> FragmentSwiper().apply {
                    swiperListener = this
                }
                SettingHelper.FLOATING_HEAD -> FragmentMyHead().apply {
                    swiperMyHead = this
                }
                SettingHelper.NAVIGATION_HEAD -> FragmentNaviHead()
                else -> FragmentNoteHead()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.apply {
            screenFrame.myActMargin(ctx.actionBarHeight)
            cardScreen.cardAsView(ctx.actionBarHeight)
            textPt.apply {
                editAppearance()
                setTextColor(them.findAttr(android.R.attr.textColorPrimary))
            }
            tittlePager.apply {
                adapter = com.pt.pro.extra.utils.TitleAdapter(titleList)
                setCurrentItem(optionHead, false)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.extra.fasten.ScreenCaptureFasten.onClick(v: android.view.View) {
        when (v) {
            mainBack -> act.onBackPressedDispatcher.onBackPressed()
            clickNext -> binding.tittlePager.setCurrentItem(optionHead + 1, true)
            clickPrevious -> binding.tittlePager.setCurrentItem(optionHead - 1, true)
        }
    }

    override fun onLongClick(v: android.view.View): Boolean {

        return true
    }

    @com.pt.common.global.WorkerAnn
    private fun nextCheck() {
        if (optionHead == SettingHelper.NAVIGATION_HEAD) {
            isWait = false
        } else {
            capturePer()
        }
        checkIfEnableBe(optionHead)
    }

    @com.pt.common.global.WorkerAnn
    private fun capturePer() {
        if (dataScreen == null) {
            projectionManager {
                resultProjection?.launch(this)
            }
        } else {
            isWait = false
        }
        checkIfEnableBe(optionHead)
    }

    private fun startCaptureService() {
        if (optionHead.lastCheck) {
            projectionManager {
                resultProjection?.launch(this)
            }
            binding.seekLaunch.progress = 50
            return
        }
        launchDef {
            stopSeekForService()
            when (optionHead) {
                SettingHelper.SCREEN_SWIPER -> startRecordingService(resultScreen, dataScreen)
                SettingHelper.FLOATING_HEAD -> startFloatingService(resultScreen, dataScreen)
                SettingHelper.NAVIGATION_HEAD -> startNaviService()
                else -> startNoteService()
            }
        }
    }

    private inline val Int.lastCheck: Boolean
        @com.pt.common.global.WorkerAnn
        get() {
            return (((this@lastCheck == SettingHelper.FLOATING_HEAD && screen)
                    || this@lastCheck == SettingHelper.SCREEN_SWIPER)
                    && dataScreen == null)
        }

    private fun checkDrawOverlay() {
        ctx.checkDrawOverlayPermission()
    }

    @com.pt.common.global.WorkerAnn
    private fun android.content.Context.checkDrawOverlayPermission() {
        if (isV_M && !android.provider.Settings.canDrawOverlays(this)) {
            resultFloating?.launch(goToOverlay)
        } else {
            nextCheck()
        }
    }

    private var resultFloating: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (ctx.canFloat) {
                nextCheck()
            } else {
                isWait = false
                checkIfEnableBe(optionHead)
                ctx.makeToastRec(com.pt.pro.R.string.rl, 0)
            }
        }

    private var resultProjection: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
                resultScreen = it.resultCode
                dataScreen = it.data
            } else {
                ctx.makeToastRec(com.pt.pro.R.string.d5, 0)
            }
            checkIfEnableBe(optionHead)
            checkIfEnableBe(optionHead)
            isWait = false
        }

    override fun onDestroyView() {
        resultProjection?.unregister()
        resultFloating?.unregister()
        tittleChang = null
        scrollerChang = null
        binder = null
        resultProjection = null
        resultFloating = null
        lockScreen = true
        isWait = false
        gateOpen = false
        optionHead = SettingHelper.FLOATING_HEAD
        mOriginProgress = 50
        super.onDestroyView()
    }

}