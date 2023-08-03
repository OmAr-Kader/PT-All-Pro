package com.pt.pro.extra.views

import com.pt.common.BuildConfig.*
import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.extra.fasten.SettingFileFasten

class FragmentSettingFile : GlobalSimpleFragment<SettingFileFasten>() {

    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    private inline val arrayOfSort: MutableList<String>
        get() {
            return arrayListOf(
                BY_NAME_ASC,
                BY_NAME_DESC,
                BY_DATE_ASC,
                BY_DATE_DESC,
                BY_SIZE_ASC,
                BY_SIZE_DESC
            )
        }

    private val arrayOfSortFolderManger: MutableList<String>
        get() {
            return arrayListOf(
                BY_NAME_ASC,
                BY_NAME_DESC,
                BY_DATE_ASC,
                BY_DATE_DESC
            )
        }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.extra.fasten.ExtraInflater.run {
                this@creBin.context.inflaterFileSetting()
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root_
        }

    @com.pt.common.global.UiAnn
    override fun SettingFileFasten.onViewCreated() {
        lifecycle.addObserver(this@FragmentSettingFile)
        launchImdMain {
            frameSortByFolderText.setOnClickListener(this@FragmentSettingFile)
            frameSortByText.setOnClickListener(this@FragmentSettingFile)

            val sortFolderBy = arrayOf(
                rec.getString(com.pt.pro.R.string.nf),
                rec.getString(com.pt.pro.R.string.bd),
                rec.getString(com.pt.pro.R.string.dh),
                rec.getString(com.pt.pro.R.string.eb),
            )
            ctx.fetchPicker.applySus {
                minValue = 0
                maxValue = sortFolderBy.size - 1
                displayedValues = sortFolderBy
                value = ctx.findStringPreference(FO_B_SORT, BY_NAME_ASC).let {
                    arrayOfSortFolderManger.indexOf(it)
                }
                addSenseListener(false) { _, _, type ->
                    if (type == UP_SEN) {
                        arrayOfSortFolderManger.getOrNull(value)?.also { aa ->
                            toCatchSackAfter(11, SAVE_CONST) {
                                launchDef {
                                    ctx.updatePrefString(FO_B_SORT, aa)
                                }
                            }.postBackAfter()
                        }
                    }
                }
            }.alsoSus {
                frameSortByFolder.addView(it)
            }

            //////////////////////////////////////////////////////////////////////////////////////////
            val sV = arrayOf(
                rec.getString(com.pt.pro.R.string.nf),
                rec.getString(com.pt.pro.R.string.bd),
                rec.getString(com.pt.pro.R.string.dh),
                rec.getString(com.pt.pro.R.string.eb),
                rec.getString(com.pt.pro.R.string.vf),
                rec.getString(com.pt.pro.R.string.ey)
            )
            ctx.fetchPicker.applySus {
                minValue = 0
                maxValue = sV.size - 1
                displayedValues = sV
                value = ctx.findStringPreference(FI_SORT_B, BY_DATE_DESC).let {
                    arrayOfSort.indexOf(it)
                }
                addSenseListener(false) { _, _, type ->
                    if (type == UP_SEN) {
                        arrayOfSort.getOrNull(value)?.also { aa ->
                            toCatchSackAfter(22, SAVE_CONST) {
                                launchDef {
                                    ctx.updatePrefString(FI_SORT_B, aa)
                                }
                            }.postBackAfter()
                        }
                    }
                }
            }.alsoSus {
                frameSortBy.addView(it)
            }
            usePicture.applySus {
                isChecked = ctx.findBooleanPreference(IMG_APP, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(IMG_APP, b)
                    }
                }
            }
            useVideo.applySus {
                isChecked = ctx.findBooleanPreference(VID_APP, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(VID_APP, b)
                    }
                }
            }
            useMusic.applySus {
                isChecked = ctx.findBooleanPreference(MUS_APP, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(MUS_APP, b)
                    }
                }
            }
            usePdf.applySus {
                isChecked = ctx.findBooleanPreference(PDF_APP, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(PDF_APP, b)
                    }
                }
            }
            useTxt.applySus {
                isChecked = ctx.findBooleanPreference(TEXT_APP, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(TEXT_APP, b)
                    }
                }
            }
            useZip.applySus {
                isChecked = ctx.findBooleanPreference(ZIP_APP, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(ZIP_APP, b)
                    }
                }
            }
            screenTxt.applySus {
                isChecked = ctx.findBooleanPreference(TEXT_SCREEN_ON, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(TEXT_SCREEN_ON, b)
                    }
                }
            }
            screenPdf.applySus {
                isChecked = ctx.findBooleanPreference(PDF_SCREEN_ON, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(PDF_SCREEN_ON, b)
                    }
                }
            }
        }
    }

    override fun SettingFileFasten.onClick(v: android.view.View) {
        when (v) {
            frameSortByText -> displaySortBy()
            frameSortByFolderText -> displaySortByFolder()
        }
    }

    private fun SettingFileFasten.displaySortBy() {
        pushJob {
            launchDef {
                withMain {
                    if (frameSortBy.isVis) {
                        frameSortBy.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            sortByImage.startAnimation(this)
                        }
                        mainFrameSortBy.goneHandlerSus()
                    } else {
                        mainFrameSortBy.justVisibleSus()
                        frameSortBy.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            sortByImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    private fun SettingFileFasten.displaySortByFolder() {
        pushJob {
            launchDef {
                withMain {
                    if (frameSortByFolder.isVis) {
                        frameSortByFolder.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            sortByFolderImage.startAnimation(this)
                        }
                        mainFrameSortByFolder.goneHandlerSus()
                    } else {
                        mainFrameSortByFolder.justVisibleSus()
                        frameSortByFolder.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            sortByFolderImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }


}