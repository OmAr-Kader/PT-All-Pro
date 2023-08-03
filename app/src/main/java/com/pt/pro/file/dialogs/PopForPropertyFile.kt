package com.pt.pro.file.dialogs

import androidx.core.text.toSpannable
import com.pt.common.BuildConfig.*
import com.pt.common.global.*
import com.pt.common.media.getListFiles
import com.pt.common.mutual.life.GlobalDia
import com.pt.common.stable.*
import com.pt.pro.databinding.PopPropertyBinding

class PopForPropertyFile : GlobalDia<PopPropertyBinding>(),
    com.pt.pro.file.interfaces.DialogListener {

    private var fileHolder: MutableList<FileSack> = mutableListOf()
    private var pending: Boolean = false

    private var sizeTotal = 0L

    override fun MutableList<FileSack>.pushList(bool: Boolean) {
        fileHolder = this
        pending = bool
    }


    override val android.view.LayoutInflater.creBinD: android.view.View
        @com.pt.common.global.UiAnn
        get() = PopPropertyBinding.inflate(this).also {
            @ViewAnn
            binder = it
        }.root

    @com.pt.common.global.UiAnn
    override suspend fun PopPropertyBinding.intiViews() {
        this@PopForPropertyFile.context.nullChecker()
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
            itemSelected.text = fileHolder.size.toStr
        }
        this@PopForPropertyFile.context.nullChecker()
        withMain {
            if (!pending) {
                path.text = FileLate(fileHolder[0].filePath).letSus {
                    it.parent.toStr
                }
            } else {
                path.justGoneSus()
                pathTittle.justGoneSus()
            }
            okPro.setOnClickListener { dia.dismiss() }
        }
        this@PopForPropertyFile.context.nullChecker()
        withMain {
            fileHolder.onEachIndexedSus(context) { index, fileHolder ->
                if (index > 4) kotlinx.coroutines.delay(10L)
                proLinearLayout.loadAllProperties(fileHolder)
            }
        }
        this@PopForPropertyFile.context.nullChecker()
        withMain {
            tSize.text = sizeTotal.reformatSize(SIZE_MEGA, SIZE_GIGA)
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun getImageDim(
        path: String,
    ): android.text.Spanned? = justCoroutine {
        path.fetchIMGSize.let {
            if (it != null) {
                val imageHeight = it.outHeight
                val imageWidth = it.outWidth
                val dimensionString = recD.getString(com.pt.pro.R.string.dm) + ":  "
                DSackT(
                    dimensionString,
                    "$imageHeight*$imageWidth",
                ).reSizeText
            } else {
                null
            }
        }

    }

    @com.pt.common.global.WorkerAnn
    private suspend fun getAudiDuration(
        path: String,
    ): android.text.Spanned? = justCoroutine {
        return@justCoroutine withBackDef(null) {
            catchy(null) {
                com.pt.common.moderator.over.MyMediaMetadataRetriever().useSusMeta {
                    setDataSource(path)
                    return@useSusMeta extractMetadata(
                        android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
                    )
                }
            }
        }.runSusBack {
            return@runSusBack withBackDef(null) {
                durationFormat(this@runSusBack?.toLong())
            }
        }
    }

    private suspend fun getFolderContText(
        filePath: String,
    ): android.text.Spanned = justCoroutine {
        return@justCoroutine fileCountNative(filePath).toStr.run {
            withMainDef("".toSpannable()) {
                DSackT(
                    (recD.getString(com.pt.pro.R.string.kd) + ":  "),
                    this@run,
                ).reSizeText
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun durationFormat(
        durationStr: Long?,
    ): android.text.Spanned? = withBackDef(null) {
        if (durationStr == null || context == null) return@withBackDef null
        return@withBackDef java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(
            durationStr
        ).letSusBack { min ->
            (java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(durationStr) % 60).run {
                DSackT(
                    recD.getString(com.pt.pro.R.string.zg) + ":  ",
                    min.toStr + ":" + this + recD.getString(com.pt.pro.R.string.nq),
                ).reSizeText
            }
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

    @com.pt.common.global.UiAnn
    private suspend fun android.view.ViewGroup.loadAllProperties(media: FileSack) {
        this@PopForPropertyFile.context.nullChecker()
        withMainDef(null) {
            com.pt.pro.databinding.ItemPropertyBinding.inflate(actD.layoutInflater)
        }?.applySus {
            this@PopForPropertyFile.context.nullChecker()
            withMain {
                if (pending) {
                    pathPro.applySus {
                        justVisibleSus()
                        text = DSackT(
                            recD.getString(com.pt.pro.R.string.ph) + ":  ",
                            FileLate(media.filePath).parent?.toStr ?: "",
                        ).reSizeText
                        setOnLongClickListener(media.filePath.longClickPath)
                    }
                    pathPro.setOnLongClickListener {
                        pathPro.text?.toString()?.let { it1 -> ctxD.sendToClipDate(it1) }
                        false
                    }
                }
            }
            this@PopForPropertyFile.context.nullChecker()
            withMain {
                namePro.applySus {
                    text = DSackT(
                        recD.getString(com.pt.pro.R.string.na) + ":  ",
                        media.fileName
                    ).reSizeText

                    setOnLongClickListener(media.fileName.longClickName)
                }
                modifiedPro.setOnLongClickListener(media.filePath.longClickPath)
                sizePro.setOnLongClickListener(media.filePath.longClickPath)
                dimensionPro.setOnLongClickListener(media.filePath.longClickPath)
            }
            this@PopForPropertyFile.context.nullChecker()
            media.doDimension().letSus {
                this@PopForPropertyFile.context.nullChecker()
                withMain {
                    dimensionPro.apply {
                        if (it != null)
                            text = it
                        else
                            justGone()
                    }
                }
            }
            this@PopForPropertyFile.context.nullChecker()
            withMain {
                modifiedPro.text = DSackT(
                    recD.getString(com.pt.pro.R.string.df) + ":  ",
                    media.dateModified.findFileDate,
                ).reSizeText
                this@loadAllProperties.addView(this@applySus.root)
            }
            this@PopForPropertyFile.context.nullChecker()
            withMain {
                if (media.typeFile == FOLDER) {
                    ctxD.contentResolver.getListFiles(FileLate(media.filePath)) {
                        if (context == null) return@getListFiles
                        this@PopForPropertyFile.context.nullChecker()
                        displaySize(it)
                    }
                } else {
                    displaySize(media.fileSize)
                }
            }
        }
    }

    private suspend fun com.pt.pro.databinding.ItemPropertyBinding.displaySize(siz: Long) {
        this@PopForPropertyFile.context.nullChecker()
        withMain {
            sizeTotal += siz
            siz.reformatSize(SIZE_MEGA, SIZE_GIGA).runSus {
                if (this == "0.00") {
                    DSackT(
                        recD.getString(com.pt.pro.R.string.sz) + ":  ",
                        "$NOT_REAL_DIM $this",
                    ).reSizeText
                } else {
                    DSackT(
                        recD.getString(com.pt.pro.R.string.sz) + ":  ",
                        this,
                    ).reSizeText
                }
            }.letSus {
                sizePro.text = it
            }
        }
    }

    private suspend fun FileSack.doDimension() = justCoroutine {
        return@justCoroutine when (typeFile) {
            IMAGE -> getImageDim(filePath)
            AUDIO -> getAudiDuration(filePath)
            FOLDER -> getFolderContText(filePath)
            else -> null
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        sizeTotal = 0L
        fileHolder.clear()
        null.longClickName = null
        null.longClickPath = null
        super.onDestroyView()
    }

}