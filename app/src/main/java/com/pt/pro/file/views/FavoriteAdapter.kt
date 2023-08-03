package com.pt.pro.file.views

import androidx.recyclerview.widget.RecyclerView
import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.mutual.adapter.GlobalAdapterEX
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.file.fasten.FileInflater
import com.pt.pro.file.interfaces.FavoriteListener
import com.pt.pro.file.interfaces.ItemFileListener
import com.pt.pro.file.fasten.ItemFileFavorFasten
import kotlinx.coroutines.asCoroutineDispatcher

class FavoriteAdapter(
    @Volatile
    override var isCategory: Boolean,
    @Volatile
    override var picListener: ItemFileListener?,
    override val allSvg: androidx.collection.ArrayMap<Int, android.graphics.drawable.Drawable>,
) : RecyclerView.Adapter<FavoriteAdapter.PicHolder>(), FavoriteListener {

    override val favList: MutableList<FileSack> = mutableListOf()

    internal var jobNative: kotlinx.coroutines.Job? = null
    internal var extNative: java.util.concurrent.ExecutorService? = null
    internal var dispatcherNative: kotlinx.coroutines.ExecutorCoroutineDispatcher? = null

    internal inline val job: kotlinx.coroutines.Job
        get() = jobNative ?: kotlinx.coroutines.SupervisorJob().also { jobNative = it }

    internal inline val ext: java.util.concurrent.ExecutorService
        get() = extNative ?: fetchExtractor.also { extNative = it }

    internal inline val dispatch: kotlinx.coroutines.ExecutorCoroutineDispatcher
        get() = dispatcherNative ?: ext.asCoroutineDispatcher().also { dispatcherNative = it }

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = dispatch + job + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): PicHolder = android.view.LayoutInflater.from(parent.context).run {
        PicHolder(
            FileInflater.run { parent.context.inflaterFavor() }
        )
    }

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = favList.size

    override fun getItemId(position: Int): Long = position.toLong()

    @com.pt.common.global.UiAnn
    override fun onViewAttachedToWindow(holder: PicHolder) {
        super.onViewAttachedToWindow(holder)
        if (isCategory) {
            holder.attachFilter()
        } else {
            holder.attach()
        }
    }

    @com.pt.common.global.UiAnn
    override fun onViewDetachedFromWindow(holder: PicHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.clear()
    }

    override suspend fun MutableList<FileSack>.updateSearch(isCato: Boolean) {
        val siz = favList.size
        isCategory = isCato
        withMain {
            clearer()
            justRemove(siz)
            adder()
            justAdd(size)
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun clearer() {
        justCoroutineMain {
            favList.clear()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun MutableList<FileSack>.adder() {
        justCoroutineMain {
            favList.addAll(this@adder)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justRemove(siz: Int) {
        justCoroutineMain {
            notifyItemRangeRemoved(0, siz)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justAdd(siz: Int) {
        justCoroutineMain {
            notifyItemRangeInserted(0, siz)
        }
    }

    override fun onAdapterDestroy() {
        allSvg.clear()
        favList.clear()
        picListener = null

        job.cancelJob()
        catchy(Unit) {
            dispatch.close()
        }
        ext.shutdownNow()
        cancelScope()
        extNative = null
        jobNative = null
        dispatcherNative = null
    }

    @com.pt.common.global.UiAnn
    inner class PicHolder(
        item: ItemFileFavorFasten,
    ) : GlobalAdapterEX<ItemFileFavorFasten, FileSack>(item) {

        override val Int.item: FileSack
            get() = favList.getI(this)

        override fun ItemFileFavorFasten.bind() {
            root_.setOnClickListener(this@PicHolder)
        }

        override fun ItemFileFavorFasten.attachPlus(it: FileSack, i: Int) {
            val t = it.filePath.toStr
            folderImage.tag = t
            if (it.typeFile == CATO_MUSIC) {
                folderImageCard.radius = 30F.toPixelA.toFloat()
                folderImage.svgReClear()
            } else {
                folderImageCard.radius = 15F.toPixelA.toFloat()
                folderImage.svgReColor(themA.findAttr(R.attr.rmoText))
            }
            loadImgFilterAsync(str = t, it = it)

            fileName.apply {
                text = context.resources.findTextCato(it.typeFile)
                setTextColor(context.theme.findAttr(R.attr.rmoText))
            }
            if (it.typeFile == CATO_SD) {
                loadAsyncTxtSD(t, t)
            } else {
                loadAsyncTxt(type = it.typeFile, str = t)
            }
        }

        override fun ItemFileFavorFasten.attach(it: FileSack, i: Int) {
            it.filePath.toStr.also { t ->
                folderImage.tag = t
                loadImgNormalAsync(it, t)
            }
            folderImage.svgReClear()
            fileName.apply {
                text = it.fileName
                isSelected = true
            }
            fileType.text = if (it.typeFile == FOLDER) {
                "0"
            } else {
                FileLate(it.filePath).extension
            }
            if (!it.isReal) {
                fileType.text = it.fileSize.toStr
                themA.findAttr(android.R.attr.colorAccent)
            } else {
                themA.findAttr(R.attr.rmoText)
            }.let { itC ->
                fileName.setTextColor(itC)
                fileType.setTextColor(itC)
            }
        }

        override fun FileSack.onClick(i: Int) {
            if (isCategory) {
                when (typeFile) {
                    CATO_MUSIC -> {
                        picListener?.openMusicPlaylist()
                    }
                    CATO_SD -> {
                        picListener?.apply {
                            copy(typeFile = FOLDER).onRealItemCLick(i, true)
                        }
                    }
                    else -> {
                        picListener?.onCatoItemCLick(typeFile, setPath = true)
                    }
                }
            } else {
                if (isReal) {
                    picListener?.apply { onRealItemCLick(i, true) }
                } else {
                    picListener?.onVirtualItemCLick(fileName)
                }
            }
        }

        override fun ItemFileFavorFasten.clear() {
            folderImageCard.radius = 15F.toPixelA.toFloat()
            folderImage.svgReClear()
        }

        override fun ItemFileFavorFasten.finalClear() {}
    }

    override fun ItemFileFavorFasten.loadImgNormalAsync(media: FileSack, str: String) {
        launchDef {
            doImgNormalAsync(media, str)
            folderImage.displayImgNormalAsync(media, str)
        }
    }

    @com.pt.common.global.WorkerAnn
    override suspend fun ItemFileFavorFasten.doImgNormalAsync(media: FileSack, str: String) {
        withBack {
            if (media.typeFile == FOLDER && media.isReal) {
                fileCount(media.filePath) {
                    launchMain {
                        justCoroutineMain {
                            if (folderImage.tag == str) {
                                fileType.text = it.toStr
                            }
                        }
                    }
                }
            }
        }
    }

    private inline val String.failedSour: Int
        get() {
            return if (contains(".mp3".toRegex())) {
                R.drawable.ic_file_mp3
            } else {
                R.drawable.ic_m4a_file
            }
        }


    private inline val Int.fetchSvgByType: android.graphics.drawable.Drawable?
        get() = allSvg[this@fetchSvgByType]


    override suspend fun com.pt.common.moderator.over.GlideImageView.displayImgNormalAsync(media: FileSack, str: String) {
        withMain {
            when (media.typeFile) {
                IMAGE, VIDEO -> {
                    loadImageForManager(media, str = str)
                }
                UNKNOWN -> {
                    media.filePath.fetchIMGSizeSus().let {
                        if (it != null && it.outWidth != -1) {
                            loadImageForManager(media, str = str)
                        } else {
                            if (tag == str) {
                                setImageDrawable(context.compactImageReturn(R.drawable.ic_file_unknown))
                            }
                        }
                    }
                }
                AUDIO -> {
                    context.getMusicBit(media.filePath, media.filePath.failedSour, 3) {
                        displayAudioImage(DSackV(this@getMusicBit, str))
                    }
                }
                else -> {
                    media.typeFile.let {
                        setImageDrawable(
                            it.fetchSvgByType ?: context.compactImageReturn(it.checkFileImage)
                        )
                    }
                }
            }
        }
    }

    private suspend fun com.pt.common.moderator.over.GlideImageView.displayAudioImage(
        bit: DSackV<android.graphics.Bitmap?, String>,
    ) {
        withMain {
            if (tag == bit.two) {
                setImageBitmap(bit.one)
                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            } else {
                bit.one = null
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun ItemFileFavorFasten.loadImgFilterAsync(it: FileSack, str: String) {
        if (it.typeFile == CATO_MUSIC) {
            launchDef {
                root_.context.getMusicBit(it.filePath, R.drawable.ic_album, 3) {
                    root_.context.nullChecker()
                    folderImage.displayAudioImage(DSackV(this@getMusicBit, str))
                }
            }
        } else {
            folderImage.apply {
                when (it.typeFile) {
                    CATO_IMAGE -> R.drawable.ic_image_file_icon
                    CATO_VIDEO -> R.drawable.ic_video_filter
                    CATO_AUDIO -> R.drawable.ic_audio_filter
                    CATO_DOCUMENT -> R.drawable.ic_document_filter
                    CATO_SD -> R.drawable.ic_sd
                    else -> R.drawable.ic_archive_filter
                }.let {
                    root_.context.compactImage(it) {
                        setImageDrawable(this)
                    }
                }
            }
        }
    }

    internal fun ItemFileFavorFasten.loadAsyncTxtSD(sdPath: String, str: String) {
        launchDef {
            withBackDef("0") {
                root_.context.contentResolver.allFilesLoader(sdPath).size.toStr
            }.let {
                root_.context.nullChecker()
                displayLoadAsyncTxt(str, it)
            }
        }
    }

    override fun ItemFileFavorFasten.loadAsyncTxt(type: Int, str: String) {
        launchDef {
            withBackDef("0") {
                if (type == CATO_MUSIC) {
                    (root_.context ?: return@withBackDef "0").newDBPlaylist {
                        getAllSongsDefault().size.toStr
                    }
                } else {
                    root_.context.contentResolver.findCategoryCount(type)
                }
            }.let {
                root_.context.nullChecker()
                displayLoadAsyncTxt(str, it)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun ItemFileFavorFasten.displayLoadAsyncTxt(str: String, it: String) {
        withMain {
            if (folderImage.tag == str) {
                fileType.apply {
                    setTextColor(context.theme.findAttr(R.attr.rmoText))
                    text = it
                }
            }
        }
    }

}