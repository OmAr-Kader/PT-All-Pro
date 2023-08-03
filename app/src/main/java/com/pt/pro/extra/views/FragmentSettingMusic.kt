package com.pt.pro.extra.views

import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.extra.fasten.SettingMusicFasten

class FragmentSettingMusic : GlobalSimpleFragment<SettingMusicFasten>() {

    private var screenWidth: Int = 0

    private var isSeekChanged = false

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.extra.fasten.ExtraInflater.run {
                this@creBin.context.inflaterMusicSetting()
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root_
        }

    @android.annotation.SuppressLint("SetTextI18n")
    @com.pt.common.global.UiAnn
    override fun SettingMusicFasten.onViewCreated() {
        launchImdMain {
            act.windowManager?.fetchDimensionsMan {
                screenWidth = this@fetchDimensionsMan.width
            }
            showVoice.apply {
                isChecked = ctx.findBooleanPrefDb(SHOW_VOICE, VOICE_IN, false)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(VOICE_IN, b)
                    }
                }
            }
            switchMusicScreen.apply {
                isChecked = ctx.findBooleanPreference(MUSIC_SCREEN, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(MUSIC_SCREEN, b)
                    }
                }
            }
            pauseSeek.max = 105
            rec.getString(com.pt.pro.R.string.us).let { min ->
                scaleNum.text = "15 ".toStr + min
                maxNum.text = "120 ".toStr + min
                val seekText = rec.getString(com.pt.pro.R.string.iz)
                ctx.findIntegerPreference(INTERRUPT_MUSIC, 30).let { inter ->
                    seekTitle.text = (seekText + "<br>\"" + (inter).toStr.convertToColor + " $min\"").toHtmlText
                    pauseSeek.progress = inter - 15
                }
                pauseSeek.max = 105
                saveInterrupt()
                pauseSeek.onSeekListener { it, mode ->
                    seekTitle.text = (seekText + "<br>\"" + (it + 15).toStr.convertToColor + " $min\"").toHtmlText
                    isSeekChanged = true
                    if (mode == -1) {
                        launchDef {
                            saveInterrupt()
                        }
                    }
                }
            }
        }
    }

    private inline val String.convertToColor: String
        get() {
            return (com.pt.common.BuildConfig.TEXT_COLOR_RED + this@convertToColor + com.pt.common.BuildConfig.TEXT_END_COLOR)
        }

    private suspend fun saveInterrupt() {
        if (isSeekChanged) {
            ctx.updatePrefInt(INTERRUPT_MUSIC, (binding.pauseSeek.progress + 15))
            isSeekChanged = false
        }
    }

    override fun SettingMusicFasten.onClick(v: android.view.View) {}
}