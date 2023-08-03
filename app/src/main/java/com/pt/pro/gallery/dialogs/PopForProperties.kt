package com.pt.pro.gallery.dialogs

import com.pt.common.BuildConfig.*
import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalDia
import com.pt.common.stable.*
import com.pt.pro.databinding.PopPropertyBinding

class PopForProperties : GlobalDia<PopPropertyBinding>(),
    com.pt.pro.gallery.interfaces.DialogsListener<Boolean> {

    override var folderPath: MutableList<MediaSack>? = mutableListOf()

    private inline val mediaHolder: MutableList<MediaSack> get() = folderPath!!

    override var listener: Boolean? = false
    private inline val pending: Boolean get() = listener!!

    override var folderMedia: MutableList<MediaFolderSack>? = null

    private var sizeTotal = 0L

    override val android.view.LayoutInflater.creBinD: android.view.View
        get() = PopPropertyBinding.inflate(this).also {
            @ViewAnn
            binder = it
        }.root

    @com.pt.common.global.UiAnn
    override suspend fun PopPropertyBinding.intiViews() {
        this@PopForProperties.context.nullChecker()
        withMain {
            pathTittle.text = DSackT(
                recD.getString(com.pt.pro.R.string.ph),
                "",
            ).reSizeText
            sizeTittle.text = DSackT(
                recD.getString(com.pt.pro.R.string.tz),
                "",
            ).reSizeText
            countTittle.text = DSackT(
                recD.getString(com.pt.pro.R.string.im),
                "",
            ).reSizeText
            itemSelected.text = mediaHolder.size.toStr
        }
        this@PopForProperties.context.nullChecker()
        withMain {
            if (!pending) {
                (FileLate(mediaHolder[0].pathMedia.toStr).parent?.toStr
                    ?: "").let {
                    path.text = it
                }
            } else {
                path.justGone()
                pathTittle.justGone()
            }
            okPro.setOnClickListener { dia.dismiss() }
        }
        this@PopForProperties.context.nullChecker()
        withMain {
            mediaHolder.onEachIndexedSus(context) { index, mediaHolder ->
                if (index > 4) kotlinx.coroutines.delay(10)
                proLinearLayout.loadAllProperties(mediaHolder)
            }
        }
        this@PopForProperties.context.nullChecker()
        withMain {
            tSize.text = sizeTotal.reformatSize(SIZE_MEGA, SIZE_GIGA)
        }
    }

    private var String?.longClickName: android.view.View.OnLongClickListener?
        @com.pt.common.global.UiAnn
        get() {
            return android.view.View.OnLongClickListener {
                this@longClickName?.let { it1 ->
                    ctxD.sendToClipDate(it1)
                    ctxD.makeToastRec(com.pt.pro.R.string.np, 0)
                }
                false
            }
        }
        set(value) {
            value.logProvLess()
        }

    private var String?.longClickPath: android.view.View.OnLongClickListener?
        @com.pt.common.global.UiAnn
        get() {
            return android.view.View.OnLongClickListener {
                this@longClickPath?.let { it1 ->
                    ctxD.sendToClipDate(it1)
                    ctxD.makeToastRec(com.pt.pro.R.string.tc, 0)
                }
                false
            }
        }
        set(value) {
            value.logProvLess()
        }

    @android.annotation.SuppressLint("SetTextI18n")
    @com.pt.common.global.UiAnn
    private suspend fun android.view.ViewGroup.loadAllProperties(media: MediaSack) {
        this@PopForProperties.context.nullChecker()
        justCoroutine {
            com.pt.pro.databinding.ItemPropertyBinding.inflate(
                actD.layoutInflater,
                this@loadAllProperties,
                false
            )
        }.applySus {
            this@PopForProperties.context.nullChecker()
            withMain {
                if (this@PopForProperties.context == null) return@withMain
                if (pending) {
                    pathPro.applySus {
                        justVisibleSus()
                        text = FileLate(media.pathMedia.toStr).parent?.toStr.run {
                            DSackT(
                                recD.getString(com.pt.pro.R.string.ph) + ":  ",
                                this@run.toStr,
                            ).reSizeText
                        }
                        setOnLongClickListener {
                            ctxD.sendToClipDate(media.pathMedia.toStr)
                            ctxD.makeToastRec(com.pt.pro.R.string.tp, 0)
                            false
                        }
                        setOnLongClickListener(media.pathMedia.longClickPath)
                    }
                }
                this@loadAllProperties.addView(this@applySus.root)
            }
            this@PopForProperties.context.nullChecker()
            withMain {
                namePro.applySus {
                    text = DSackT(
                        recD.getString(com.pt.pro.R.string.na) + ":  ",
                        media.nameMedia.toStr,
                    ).reSizeText
                    setOnLongClickListener(media.nameMedia.longClickName)
                }
                modifiedPro.setOnLongClickListener(media.pathMedia.longClickPath)
                sizePro.setOnLongClickListener(media.pathMedia.longClickPath)
                dimensionPro.setOnLongClickListener(media.pathMedia.longClickPath)
                dimensionPro.text = (media.mediaWidth.toStr + "*" + media.mediaHigh).run {
                    if (media.mediaWidth == 0) {
                        DSackT(
                            recD.getString(com.pt.pro.R.string.dm) + ":  ",
                            NOT_REAL_DIM.toStr + " " + this,
                        ).reSizeText
                    } else {
                        DSackT(
                            recD.getString(com.pt.pro.R.string.dm) + ":  ",
                            this,
                        ).reSizeText
                    }
                }
                modifiedPro.text = DSackT(
                    recD.getString(com.pt.pro.R.string.df) + ":  ",
                    media.dateModified.findFileDate,
                ).reSizeText
                sizePro.text = media.mediaSize.reformatSize(SIZE_MEGA, SIZE_GIGA).let {
                    if (it == "0.00") {
                        DSackT(
                            recD.getString(com.pt.pro.R.string.sz) + ":  ",
                            NOT_REAL_DIM.toStr + " " + it,
                        ).reSizeText
                    } else {
                        DSackT(
                            recD.getString(com.pt.pro.R.string.sz) + ":  ",
                            it,
                        ).reSizeText
                    }
                }
                sizeTotal += media.mediaSize
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        mediaHolder.clear()
        sizeTotal = 0L
        null.longClickName = null
        null.longClickPath = null
        super.onDestroyView()
    }

}