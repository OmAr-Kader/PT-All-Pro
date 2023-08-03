package com.pt.pro.extra.screener

import com.pt.common.BuildConfig.FILE_SCREEN
import com.pt.common.BuildConfig.ROOT
import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.databinding.LayoutSwiperBinding
import com.pt.pro.extra.interfaces.SwiperListener

class FragmentSwiper : GlobalSimpleFragment<LayoutSwiperBinding>(), SwiperListener {

    private var isTrans = true
    private var isEditorLaunch = false
    private var isEnabled = true

    private var isAudioBoolean = true

    private var isShooter = false
    private var isLaunchBe = false

    private var mediumBoolean = false

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            LayoutSwiperBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
                it.subMainScreen.orientation = rec.linDirection
            }.root
        }

    @com.pt.common.global.UiAnn
    override fun LayoutSwiperBinding.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentSwiper)
        launchImdMain {
            justCoroutine {
                ctx.findStringPrefNullable(SCR_FOL_KEY).let { screenFolder ->
                    "$ROOT/$FILE_SCREEN".let { str ->
                        if (screenFolder == null && FileLate(str).exists()) {
                            ctx.updatePrefString(SCR_FOL_KEY, str)
                        }
                    }
                }
                isLaunchBe = ctx.findBooleanPreference(SCR_LAUNCH, false)
                isShooter = ctx.findBooleanPreference(SCR_SHOT, true)
                mediumBoolean = ctx.findBooleanPreference(MED_QUAIL, false)
                isAudioBoolean = ctx.findBooleanPreference(IS_AUD, true)
                isTrans = ctx.findBooleanPreference(IS_TRANS, true)
                isEditorLaunch = ctx.findBooleanPreference(IS_EDITOR, false)
            }
            intiViews()
            justCoroutine {
                switchStatus(isShooter)
                switchMode.apply {
                    isEnabled = !isLaunchBe
                    isChecked = isShooter
                    jumpDrawablesToCurrentState()
                }
                mediumQuality.apply {
                    isEnabled = !isLaunchBe
                    isChecked = mediumBoolean
                    jumpDrawablesToCurrentState()
                }
                highQuality.apply {
                    isEnabled = !isLaunchBe
                    isChecked = !mediumBoolean
                    jumpDrawablesToCurrentState()
                }
                isAudio.apply {
                    isEnabled = !isLaunchBe
                    isChecked = isAudioBoolean
                    jumpDrawablesToCurrentState()
                }
                isTransparent.apply {
                    isEnabled = !isLaunchBe
                    isChecked = isTrans
                    jumpDrawablesToCurrentState()
                }
                isEditLaunch.apply {
                    isEnabled = !isLaunchBe
                    isChecked = isEditorLaunch
                    jumpDrawablesToCurrentState()
                }
                isTransparent.setOnCheckedChangeListener(this@FragmentSwiper)
                isEditLaunch.setOnCheckedChangeListener(this@FragmentSwiper)
                mediumQuality.setOnCheckedChangeListener(this@FragmentSwiper)
                isAudio.setOnCheckedChangeListener(this@FragmentSwiper)
                highQuality.setOnCheckedChangeListener(this@FragmentSwiper)
                switchMode.setOnCheckedChangeListener(this@FragmentSwiper)
                runSwipe.rKTSack(200L).postAfter()
            }
        }
    }

    private suspend fun intiViews() {
        justCoroutine {
            if (com.pt.pro.extra.background.OneService.swipeManagerListener == null && isLaunchBe) {
                isLaunchBe = false
                ctx.updatePrefBoolean(SCR_LAUNCH, false)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun LayoutSwiperBinding.onClick(v: android.view.View) {

    }

    private suspend fun doRecordLauncher(isChecked: Boolean) {
        isShooter = if (isChecked) {
            switchStatus(true)
            ctx.updatePrefBoolean(SCR_SHOT, true)
            true
        } else {
            switchStatus(false)
            ctx.updatePrefBoolean(SCR_SHOT, false)
            false
        }
    }

    @com.pt.common.global.UiAnn
    private fun switchStatus(isShoot: Boolean) {
        launchDef {
            withMain {
                if (isShoot) {
                    binding.extraOptions.goneEndSus(300L)
                } else {
                    binding.extraOptions.visibleEndSus(250L)
                }
            }
        }
    }


    override fun doLaunch() {
        if (isEnabled) {
            launchDef {
                binding.unEnableOptions()
            }
            isEnabled = false
        }
    }

    override fun doStop() {
        if (!isEnabled) {
            launchDef {
                binding.doEnableOptions()
            }
            isEnabled = true
        }
    }

    private suspend fun LayoutSwiperBinding.doEnableOptions() {
        displayEnableOptions()
        ctx.updatePrefBoolean(SCR_LAUNCH, false)
    }

    @com.pt.common.global.UiAnn
    private suspend fun LayoutSwiperBinding.displayEnableOptions() {
        withMain {
            mediumQuality.isEnabled = true
            highQuality.isEnabled = true
            isAudio.isEnabled = true
            isTransparent.isEnabled = true
            isEditLaunch.isEnabled = true
            switchMode.isEnabled = true
            isLaunchBe = false
        }
    }

    private suspend fun LayoutSwiperBinding.unEnableOptions() {
        displayUnEnableOptions()
        ctx.updatePrefBoolean(SCR_LAUNCH, true)
    }

    @com.pt.common.global.UiAnn
    private suspend fun LayoutSwiperBinding.displayUnEnableOptions() {
        withMain {
            mediumQuality.isEnabled = false
            highQuality.isEnabled = false
            isAudio.isEnabled = false
            isTransparent.isEnabled = false
            isEditLaunch.isEnabled = false
            switchMode.isEnabled = false
            isLaunchBe = true
        }
    }

    private val runSwipe: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            if (context != null) {
                binder?.apply {
                    catchy(Unit) {
                        toCatchSackAfter(31, 10L, runVis).postAfter()
                        toCatchSackAfter(32, 300, runGone).postAfter()
                        toCatchSackAfter(33, 400L, runVis).postAfter()
                        toCatchSackAfter(34, 600L, runGone).postAfter()
                        toCatchSackAfter(99, 600L) {
                            if (context == null) return@toCatchSackAfter
                            pointerHand.animate().translationX(-100F).duration = 400L
                        }.postAfter()
                        toCatchSackAfter(88, 1000L) {
                            if (context == null) return@toCatchSackAfter
                            pointerHand.animate().translationX(0F).duration = 400L
                        }.postAfter()
                        toCatchSackAfter(77, 1400L) {
                            if (context == null) return@toCatchSackAfter
                            if (swipeCon.rotation == 90F) {
                                swipeCon.animate().rotation(0F).duration = 400L
                                pointerHand.animate().rotation(0F).duration = 400L
                            } else {
                                swipeCon.animate().rotation(90F).duration = 400L
                                pointerHand.animate().rotation(-90F).duration = 400L
                            }
                        }.postAfter()
                    }
                }
                runSwipe.rKTSack(1800L).postAfter()
            }
            Unit
        }

    private val runVis: () -> Unit
        get() = {
            binder?.viewGif?.apply {
                runCatching {
                    if (context != null) {
                        ctx.compactImage(R.drawable.ic_phone_pointer) {
                            this?.isAutoMirrored = true
                            setImageDrawable(this)
                        }
                    }
                }
            }
            Unit
        }

    private val runGone: () -> Unit
        get() = {
            binder?.viewGif?.apply {
                runCatching {
                    if (context != null) {
                        ctx.compactImage(R.drawable.ic_phone) {
                            this?.isAutoMirrored = true
                            setImageDrawable(this)
                        }
                    }
                }
            }
            Unit
        }

    override fun onResume() {
        super.onResume()
        unPostAll()
        runSwipe.rKTSack(100L).postAfter()
    }

    override fun onPause() {
        binding.apply {
            runCatching {
                pointerHand.apply {
                    translationX = 0F
                    rotation = 0F
                }
                swipeCon.apply {
                    rotation = 0F
                    translationX = 0F
                }
                unPostAll()
            }
        }
        super.onPause()
    }

    @com.pt.common.global.UiAnn
    override fun onCheckedChanged(buttonView: android.widget.CompoundButton?, isChecked: Boolean) {
        launchImdMain {
            binding.apply {
                when (buttonView) {
                    mediumQuality -> doQualityMedium(isChecked)
                    isTransparent -> {
                        isTrans = isChecked
                        ctx.updatePrefBoolean(IS_TRANS, isTrans)
                    }
                    isEditLaunch -> {
                        isEditorLaunch = isChecked
                        ctx.updatePrefBoolean(IS_EDITOR, isEditorLaunch)
                    }
                    highQuality -> doHighQuality(isChecked)
                    switchMode -> doRecordLauncher(isChecked)
                    isAudio -> {
                        isAudioBoolean = isChecked
                        ctx.updatePrefBoolean(IS_AUD, isAudioBoolean)
                    }
                }
            }
        }
    }


    private suspend fun LayoutSwiperBinding.doQualityMedium(isChecked: Boolean) {
        displayQualityMedium(isChecked)
        ctx.updatePrefBoolean(MED_QUAIL, mediumBoolean)
    }

    @com.pt.common.global.UiAnn
    private suspend fun LayoutSwiperBinding.displayQualityMedium(isChecked: Boolean) {
        justCoroutine {
            if (isChecked) {
                switchMode.isEnabled = true
                highQuality.isChecked = false
                mediumBoolean = true
            } else {
                if (!highQuality.isChecked) {
                    mediumBoolean = false
                    switchMode.isEnabled = false
                }
            }
        }
    }

    private suspend fun LayoutSwiperBinding.doHighQuality(isChecked: Boolean) {
        displayHighQuality(isChecked)
        ctx.updatePrefBoolean(MED_QUAIL, mediumBoolean)
    }

    @com.pt.common.global.UiAnn
    private suspend fun LayoutSwiperBinding.displayHighQuality(isChecked: Boolean) {
        justCoroutine {
            if (isChecked) {
                switchMode.isEnabled = true
                mediumQuality.isChecked = false
                mediumBoolean = false
            } else {
                if (!mediumQuality.isChecked) {
                    mediumBoolean = false
                    switchMode.isEnabled = false
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.subMainScreen.orientation = rec.linDirection
    }

}