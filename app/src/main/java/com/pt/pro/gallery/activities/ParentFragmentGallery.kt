package com.pt.pro.gallery.activities

import com.pt.common.global.*
import com.pt.common.media.isPickIntent
import com.pt.common.mutual.life.GlobalFragment
import com.pt.common.stable.*

abstract class ParentFragmentGallery<VB : androidx.viewbinding.ViewBinding> : GlobalFragment<VB>() {

    protected var allMediaNative: MutableList<MediaSack>? = mutableListOf()
    protected inline val allMedia: MutableList<MediaSack>
        get() = allMediaNative ?: mutableListOf<MediaSack>().also {
            allMediaNative = it
        }

    protected var allFavoriteNative: MutableList<MediaSack>? = mutableListOf()
    protected inline val allFavorite: MutableList<MediaSack>
        get() = allFavoriteNative ?: mutableListOf<MediaSack>().also {
            allFavoriteNative = it
        }


    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    protected inline val androidx.cardview.widget.CardView?.loadRun: DSackT<() -> Unit, Int>
        get() = toCatchSack(55) {
            this@loadRun?.justVisible()
            unPost(11)
        }

    internal inline val orderByFolder: suspend () -> String
        get() = {
            ctx.findStringPreference(FOL_SORT, com.pt.common.BuildConfig.BY_DATE_FOLDER_DESC)
        }

    internal inline val loadAll: suspend () -> Int
        get() = {
            ctx.findIntegerPrefDb(GAL_OPT, GAL_MODE, 1)
        }

    internal inline val orderBy: suspend () -> String
        get() = {
            ctx.findStringPreference(MED_SORT, com.pt.common.BuildConfig.BY_DATE_DESC)
        }

    internal inline val mIsPickIntent: Boolean
        get() = act.intent.isPickIntent

    internal inline val multiplyPick: Boolean
        get() = act.intent.getBooleanExtra(android.content.Intent.EXTRA_ALLOW_MULTIPLE, false)

    internal inline val medPend: MutableList<MediaSack>
        get() = com.pt.pro.gallery.objects.MyHash.REC_MEDIA.run {
            this@run[com.pt.pro.gallery.objects.MyHash.PENDING_MEDIA_HASH] ?: mutableListOf()
        }

    @com.pt.common.global.UiAnn
    protected suspend fun android.view.View.popWindow(
        inv: () -> Unit,
        isFromDisplay: Boolean,
    ) {
        withMain {
            with<com.pt.pro.databinding.PopWindowResortBinding, Unit>(
                com.pt.pro.databinding.PopWindowResortBinding.inflate(
                    act.layoutInflater,
                    requireView() as? android.view.ViewGroup?,
                    false
                )
            ) {
                android.widget.PopupWindow(
                    root,
                    WRAP,
                    WRAP,
                    true
                ).applySus {
                    withMain {
                        resortPop = this@applySus
                        setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                        if (isFromDisplay) {
                            root.setCardBackgroundColor(them.findAttr(com.pt.pro.R.attr.rmoBackground))
                        }
                    }
                    intiPopView(
                        inv,
                        isFromDisplay,
                        tarColor = them.findAttr(android.R.attr.colorAccent),
                        wholeColor = them.findAttr(com.pt.pro.R.attr.rmoText),
                        isFolder = if (isFromDisplay) false else ctx.findBooleanPreference(GAL_WHICH, true)
                    )
                    withMain {
                        showAsDropDown(this@popWindow)
                    }
                }
            }
        }
    }

    private var resortPop: android.widget.PopupWindow? = null

    private suspend fun com.pt.pro.databinding.PopWindowResortBinding.intiPopView(
        inv: () -> Unit,
        isFromDisplay: Boolean,
        tarColor: Int,
        wholeColor: Int,
        isFolder: Boolean,
    ) {
        withMain {
            if (isFromDisplay) {
                radioFolder.justInvisibleSus()
                radioFile.justInvisibleSus()
            } else {
                radioFolder.justVisibleSus()
                radioFile.justVisibleSus()
            }
        }
        withMain {
            recolorViewWindow(
                up = if (isFolder) orderByFolder() else orderBy(),
                tarColor = tarColor,
                wholeColor = wholeColor
            )
            popClickListGallery(resortPop, inv, isFromDisplay = isFromDisplay, tarColor = tarColor, wholeColor = wholeColor).also { itL ->
                nameAsc.setOnClickListener(itL)
                nameDesc.setOnClickListener(itL)
                dateAsc.setOnClickListener(itL)
                dateDesc.setOnClickListener(itL)
                sizeAsc.setOnClickListener(itL)
                sizeDesc.setOnClickListener(itL)
            }
            radioFolder.isChecked = isFolder
            radioFolder.jumpDrawablesToCurrentState()
            radioFile.isChecked = !isFolder
            radioFile.jumpDrawablesToCurrentState()

            sizeAsc.also {
                if (isFolder) it.justGone() else it.justVisible()
            }
            sizeDesc.also {
                if (isFolder) it.justGone() else it.justVisible()
            }
        }
        withMain {
            radioFolder.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    launchMain {
                        radioFolder.isChecked = true
                        radioFile.isChecked = false
                        sizeAsc.justGone()
                        sizeDesc.justGone()
                        recolorViewWindow(
                            up = orderByFolder(),
                            tarColor = tarColor,
                            wholeColor = wholeColor,
                        )
                        ctx.updatePrefBoolean(
                            GAL_WHICH, true
                        )
                    }
                }
            }
            radioFile.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    launchMain {
                        radioFile.isChecked = true
                        radioFolder.isChecked = false
                        sizeAsc.justVisible()
                        sizeDesc.justVisible()
                        recolorViewWindow(
                            up = orderBy(),
                            tarColor = tarColor,
                            wholeColor = wholeColor
                        )
                        ctx.updatePrefBoolean(GAL_WHICH, false)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.databinding.PopWindowResortBinding.popClickListGallery(
        pop: android.widget.PopupWindow?,
        inv: () -> Unit,
        isFromDisplay: Boolean,
        tarColor: Int,
        wholeColor: Int,
    ) = android.view.View.OnClickListener {
        launchImdMain {
            val isFolder = if (isFromDisplay) false else ctx.findBooleanPreference(GAL_WHICH, true)
            when (it.id) {
                com.pt.pro.R.id.nameAsc -> com.pt.common.BuildConfig.BY_NAME_ASC
                com.pt.pro.R.id.nameDesc -> com.pt.common.BuildConfig.BY_NAME_DESC
                com.pt.pro.R.id.dateAsc -> {
                    if (isFolder) com.pt.common.BuildConfig.BY_DATE_FOLDER_ASC else com.pt.common.BuildConfig.BY_DATE_ASC
                }
                com.pt.pro.R.id.dateDesc -> {
                    if (isFolder) com.pt.common.BuildConfig.BY_DATE_FOLDER_DESC else com.pt.common.BuildConfig.BY_DATE_DESC
                }
                com.pt.pro.R.id.sizeAsc -> com.pt.common.BuildConfig.BY_SIZE_ASC
                com.pt.pro.R.id.sizeDesc -> com.pt.common.BuildConfig.BY_SIZE_DESC
                else -> com.pt.common.BuildConfig.BY_NAME_ASC
            }.let { up ->
                withMain {
                    recolorViewWindow(up = up, tarColor = tarColor, wholeColor = wholeColor)
                }
                justScope {
                    if (isFolder) {
                        if (!up.contains(orderByFolder().toRegex())) {
                            ctx.updatePrefString(FOL_SORT, up)
                            inv.invoke()
                        }
                    } else {
                        if (!up.contains(orderBy().toRegex())) {
                            ctx.updatePrefString(MED_SORT, up)
                            inv.invoke()
                        }
                    }
                }
            }
        }
        if (pop?.isShowing == true) pop.dismiss() else return@OnClickListener
    }

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.databinding.PopWindowResortBinding.recolorViewWindow(
        up: String,
        tarColor: Int,
        wholeColor: Int,
    ) {
        when (up) {
            com.pt.common.BuildConfig.BY_NAME_ASC -> {
                nameAsc.setTextColor(tarColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_NAME_DESC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(tarColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_DATE_ASC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(tarColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_DATE_DESC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(tarColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_DATE_FOLDER_ASC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(tarColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_DATE_FOLDER_DESC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(tarColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_SIZE_ASC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(tarColor)
                sizeDesc.setTextColor(wholeColor)
            }
            com.pt.common.BuildConfig.BY_SIZE_DESC -> {
                nameAsc.setTextColor(wholeColor)
                nameDesc.setTextColor(wholeColor)
                dateAsc.setTextColor(wholeColor)
                dateDesc.setTextColor(wholeColor)
                sizeAsc.setTextColor(wholeColor)
                sizeDesc.setTextColor(tarColor)
            }
        }
    }

    override fun onDestroyView() {
        resortPop = null
        super.onDestroyView()
    }

}