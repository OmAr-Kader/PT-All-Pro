package com.pt.pro.extra.views

import com.pt.common.BuildConfig.*
import android.widget.CompoundButton
import com.pt.common.global.*
import com.pt.common.media.clearDiskCache
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.extra.fasten.SettingGalleryFasten

class FragmentSettingGallery : GlobalSimpleFragment<SettingGalleryFasten>() {


    override var lastJob: kotlinx.coroutines.Job? = null

    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = java.util.concurrent.Executors.newSingleThreadScheduledExecutor()

    private var killed = false

    private val arrayOfSort: MutableList<String>
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

    private inline val sortByValues: Array<String>
        get() {
            return arrayOf(
                rec.getString(com.pt.pro.R.string.nf),
                rec.getString(com.pt.pro.R.string.bd),
                rec.getString(com.pt.pro.R.string.dh),
                rec.getString(com.pt.pro.R.string.eb),
                rec.getString(com.pt.pro.R.string.vf),
                rec.getString(com.pt.pro.R.string.ey)
            )
        }

    private val arrayOfSortFolder: MutableList<String>
        get() {
            return arrayListOf(
                BY_NAME_ASC,
                BY_NAME_DESC,
                BY_DATE_FOLDER_ASC,
                BY_DATE_FOLDER_DESC
            )
        }

    private inline val sortByFolders: Array<String>
        get() {
            return arrayOf(
                rec.getString(com.pt.pro.R.string.nf),
                rec.getString(com.pt.pro.R.string.bd),
                rec.getString(com.pt.pro.R.string.dh),
                rec.getString(com.pt.pro.R.string.eb)
            )
        }


    private inline val columnValues: Array<Int>
        get() {
            return arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        }

    private inline val DSackT<String, String>.columnValuesStr: Array<String>
        get() {
            return arrayOf(
                "1 $one",
                "2 $two",
                "3 $two",
                "4 $two",
                "5 $two",
                "6 $two",
                "7 $two",
                "8 $two",
                "9 $two",
                "10 $two",
                "11 $two",
                "12 $two"
            )
        }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.extra.fasten.ExtraInflater.run {
                this@creBin.context.inflaterGallerySetting()
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root_
        }


    @com.pt.common.global.UiAnn
    override fun SettingGalleryFasten.onViewCreated() {
        lifecycle.addObserver(this@FragmentSettingGallery)
        launchImdMain {
            frameSortByText.setOnClickListener(this@FragmentSettingGallery)
            frameSortByFolderText.setOnClickListener(this@FragmentSettingGallery)
            frameColumnText.setOnClickListener(this@FragmentSettingGallery)

            ctx.fetchPicker.apply {
                minValue = 0
                maxValue = sortByFolders.size - 1
                displayedValues = sortByFolders
                value = ctx.findStringPreference(FOL_SORT, BY_DATE_FOLDER_DESC).let {
                    arrayOfSortFolder.indexOf(it)
                }
                addSenseListener(false) { _, _, type ->
                    if (type == UP_SEN) {
                        arrayOfSortFolder.getOrNull(value)?.also { aa ->
                            toCatchSackAfter(1232, SAVE_CONST) {
                                setSortFolder(aa)
                            }.postBackAfter()
                        }
                    }
                }
            }.also {
                frameSortByFolder.addView(it)
            }
            ctx.fetchPicker.apply {
                minValue = 0
                maxValue = sortByValues.size - 1
                displayedValues = sortByValues
                value = ctx.findStringPreference(MED_SORT, BY_DATE_DESC).let {
                    arrayOfSort.indexOf(it)
                }
                addSenseListener(false) { _, _, type ->
                    if (type == UP_SEN) {
                        arrayOfSort.getOrNull(value)?.also { aa ->
                            toCatchSackAfter(162, SAVE_CONST) {
                                setSortMedia(aa)
                            }.postBackAfter()
                        }
                    }
                }
            }.also {
                frameSortBy.addView(it)
            }
            ctx.fetchPicker.apply {
                minValue = 0
                maxValue = columnValues.size - 1
                displayedValues = DSackT(
                    rec.getString(com.pt.pro.R.string.cz),
                    rec.getString(com.pt.pro.R.string.cv)
                ).columnValuesStr
                ctx.findIntegerPrefDb(ID_COL, COL_NUM, 3).letSusBack {
                    value = columnValues.indexOf(it)
                }
                addSenseListener(false) { _, _, type ->
                    if (type == UP_SEN) {
                        columnValues.getOrNull(value)?.also { aa ->
                            toCatchSackAfter(182, SAVE_CONST) {
                                setColumnNumbers(aa)
                            }.postBackAfter()
                        }
                    }
                }
            }.also {
                frameColumn.addView(it)
            }
            switchResolution.apply {
                isChecked = ctx.findBooleanPrefDb(ID_RESOLE, RES_NUM, false)
                jumpDrawablesToCurrentState()
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener(switchListener)
            }

            galleryScreen.applySus {
                isChecked = ctx.findBooleanPreference(GALLERY_SCREEN_ON, true)
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(GALLERY_SCREEN_ON, b)
                    }
                }
            }
            galleryStory.applySus {
                isChecked = ctx.findBooleanPreferenceNull(KEY_STORY) == true
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, b ->
                    launchDef {
                        ctx.updatePrefBoolean(KEY_STORY, b)
                    }
                }
            }
            if (!ctx.findBooleanPreference(B_RES_FIR, false)) {
                act.windowManager?.fetchDimensionsMan {
                    val resolution = kotlin.math.min(width, height) > 800
                    ctx.updatePrefBoolean(RES_NUM, resolution)
                    switchResolution.apply {
                        isChecked = resolution
                        jumpDrawablesToCurrentState()
                    }
                    ctx.updatePrefBoolean(B_RES_FIR, true)
                }
            }
        }
    }

    private fun setSortFolder(state: String) {
        launchDef {
            ctx.updatePrefString(FOL_SORT, state)
        }
    }

    private fun setSortMedia(state: String) {
        launchDef {
            ctx.updatePrefString(MED_SORT, state)
        }
    }

    private fun setColumnNumbers(aa: Int) {
        launchDef {
            ctx.updatePrefInt(COL_NUM, aa)
            justScope {
                if (!killed) {
                    killed = true
                    withBack {
                        catchySus(Unit) {
                            context?.clearDiskCache()
                        }
                    }
                } else return@justScope
            }
        }
    }

    private var switchListener: CompoundButton.OnCheckedChangeListener? =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            launchDef {
                ctx.updatePrefBoolean(RES_NUM, isChecked)
            }
            if (!killed) {
                killed = true
                clearGlideCache()
            }
        }

    @com.pt.common.global.WorkerAnn
    private fun clearGlideCache() {
        launchDef {
            withBack {
                catchySus(Unit) {
                    context?.clearDiskCache()
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun SettingGalleryFasten.onClick(v: android.view.View) {
        when (v) {
            frameSortByText -> displaySortBy()
            frameSortByFolderText -> displaySortByFolder()
            frameColumnText -> displayColumn()
        }
    }

    private fun SettingGalleryFasten.displaySortBy() {
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

    private fun SettingGalleryFasten.displaySortByFolder() {
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

    private fun SettingGalleryFasten.displayColumn() {
        pushJob {
            launchDef {
                withMain {
                    if (frameColumn.isVis) {
                        frameColumn.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            columnImage.startAnimation(this)
                        }
                        mainFrameColumn.goneHandlerSus()
                    } else {
                        mainFrameColumn.justVisibleSus()
                        frameColumn.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            columnImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        switchListener = null
        super.onDestroyView()
    }
}