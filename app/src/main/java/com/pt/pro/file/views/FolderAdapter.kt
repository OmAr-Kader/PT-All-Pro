package com.pt.pro.file.views

import androidx.collection.ArrayMap
import com.pt.common.BuildConfig.SIZE_GIGA
import com.pt.common.BuildConfig.SIZE_MEGA
import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.mutual.adapter.GlobalAdapterSus
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.file.interfaces.FolderListener
import com.pt.pro.file.interfaces.ItemFileListener
import com.pt.pro.file.fasten.FolderFasten
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancelChildren

class FolderAdapter(
    override val fileList: MutableList<FileSack>,
    override var picListener: ItemFileListener?,
    override val allSvg: ArrayMap<Int, android.graphics.drawable.Drawable>,
) : androidx.recyclerview.widget.RecyclerView.Adapter<FolderAdapter.HolderFolder>(), FolderListener {

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

    private var selectedViewsNative: Sequence<android.widget.FrameLayout>? = emptySequence()
    override val selectedViews: Sequence<android.widget.FrameLayout> get() = selectedViewsNative!!

    @Volatile
    override var selectAll: Boolean = false

    private var deleteCopyNative: MutableList<FileSack>? = mutableListOf()

    @Volatile
    override var isActionMode: Boolean = false
    override val deleteCopy: MutableList<FileSack> get() = deleteCopyNative!!

    @Volatile
    override var adPosition: Int = 0

    @Volatile
    override var deleteActive: Boolean = false

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): FolderAdapter.HolderFolder = HolderFolder(parent.bindInflate)

    private inline val android.view.ViewGroup.bindInflate: FolderFasten
        get() = com.pt.pro.file.fasten.FileInflater.run { context.inflaterFolderAdapter() }

    override fun getItemViewType(position: Int): Int = fileList.getI(position).typeItem

    override fun getItemCount(): Int = fileList.size

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: FolderAdapter.HolderFolder, position: Int) {
        if (!deleteActive) holder.bind() else holder.attachFilter()
    }


    @com.pt.common.global.UiAnn
    override fun onViewAttachedToWindow(holder: FolderAdapter.HolderFolder) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    override fun onViewDetachedFromWindow(holder: FolderAdapter.HolderFolder) {
        super.onViewDetachedFromWindow(holder)
        holder.clear()
    }

    override fun onViewRecycled(holder: FolderAdapter.HolderFolder) {
        super.onViewRecycled(holder)
        holder.finalClear()
    }

    internal inline val String.excludeAds: Int
        get() {
            return fileList.indexOfFirst {
                it.filePath == this@excludeAds
            }
        }

    private inline val realSize: Int
        get() = fileList.size

    override suspend fun MutableList<FileSack>.updateSearch() {
        val siz = fileList.size
        withMain {
            clearer()
            justCoroutineMain {
                job.cancelChildren()
            }
            justRemove(siz)
            toMutableList().adder()
            justAdd(size)
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun clearer() {
        justCoroutineMain {
            fileList.clear()
        }
    }

    private suspend fun MutableList<FileSack>.adder() {
        justCoroutine {
            this@adder.onEachSusBack(true) {
                isSelect = false
            }
        }.letSus { new ->
            justCoroutineMain {
                fileList.addAll(new)
            }
        }
    }


    override suspend fun forDelete() {
        deleteActive = true
        isActionMode = false
    }

    override suspend fun android.graphics.drawable.Drawable?.refreshAdapter() {
        val sameSize: Boolean = deleteCopy.size == selectedViews.count()
        withMain {
            justCoroutineMain {
                deleteCopy.clear()
            }
            justCoroutine {
                isActionMode = false
                deleteCopy.forEach {
                    it.isSelect = false
                }
                fileList.forEach {
                    it.isSelect = false
                }
                sameSize && selectedViews.count() != 0
            }.letSus {
                justCoroutineMain {
                    if (!selectAll && it) {
                        runCatching {
                            selectedViews.applySus {
                                forEach {
                                    it.background = this@refreshAdapter
                                }
                            }
                        }.onFailure {
                            selectAll = false
                            justRemove(fileList.size)
                            justAdd(fileList.size)
                        }
                    } else {
                        selectAll = false
                        justRemove(fileList.size)
                        justAdd(fileList.size)
                    }
                }
                justCoroutineMain {
                    selectedViewsNative = emptySequence()
                }
            }
        }
    }

    override suspend fun selectAll() {
        withMain {
            justCoroutineMain {
                selectAll = true
                fileList.forEach {
                    it.isSelect = true
                }
                deleteCopy.clear()
            }
            justRemove(fileList.size)
            justCoroutineMain {
                deleteCopy.addAll(fileList)
            }
            justAdd(fileList.size)
            justCoroutineMain {
                (deleteCopy.size.toString() + "/" + realSize).let {
                    picListener?.onActionDisplay(showCard = false, text = it)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun justNotify() {
        justRemove(fileList.size)
        justAdd(fileList.size)
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
        fileList.clear()
        allSvg.clear()
        picListener = null
        adPosition = 0
        selectedViewsNative = null
        deleteCopyNative = null
        isActionMode = false
        deleteActive = false

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
    inner class HolderFolder(
        item: FolderFasten,
    ) : GlobalAdapterSus<FolderFasten, FileSack>(item) {

        @Volatile
        private var imgJob: kotlinx.coroutines.Job? = null

        @Volatile
        private var typeJob: kotlinx.coroutines.Job? = null

        @Volatile
        private var numberJob: kotlinx.coroutines.Job? = null

        override val Int.item: FileSack
            get() = fileList.getI(this)


        private inline val isSamPos: Boolean get() = binder.folderImage.tag == posA.item.filePath

        override fun FolderFasten.bind() {
            fileType.text = ""
            fileNumber.text = ""
            fileName.text = ""
            root_.apply {
                isClickable = true
                isFocusable = true
                setOnClickListener(this@HolderFolder)
                setOnLongClickListener(this@HolderFolder)
            }
        }

        override fun FolderFasten.attachPlus() {
            fileType.text = ""
            fileNumber.text = ""
            fileName.text = ""
            root_.apply {
                isClickable = false
                isFocusable = false
                setOnClickListener(null)
                setOnLongClickListener(null)
            }
        }

        override fun FolderFasten.attach(it: FileSack, i: Int) {
            val t = it.filePath.toStr
            adPosition = i
            folderImage.tag = t
            rowFile.apply {
                if (isSamPos) {
                    if (it.isSelect && isActionMode) {
                        setBackgroundColor(themA.findAttr(R.attr.colorPrimaryAlpha))
                    } else {
                        ctxA.compactImage(R.drawable.ripple_curvy) {
                            background = this@compactImage
                        }
                    }
                }
            }
            if (isSamPos) fileName.text = it.fileName
            when (it.typeFile) {
                FOLDER -> {
                    if (isSamPos) fileNumber.text = "0"
                }
                AUDIO -> {
                    if (isSamPos) {
                        it.fileSize.reformatSize(SIZE_MEGA, SIZE_GIGA).let { itS ->
                            fileType.text = itS
                        }
                    }
                }
                else -> {
                    if (isSamPos) {
                        fileNumber.text = FileLate(t).extension.trim()
                        it.fileSize.reformatSize(SIZE_MEGA, SIZE_GIGA).let { itS ->
                            fileType.text = itS
                        }
                    }
                }
            }
            if (isSamPos) {
                folderImage.loadImgAsync(it, t) {
                    imgJob = this
                }
                if (it.typeFile == FOLDER) {
                    loadAsyncType(it, t, imgJob) {
                        typeJob = this
                    }
                } else if (it.typeFile == AUDIO) {
                    loadAsyncTxtNumber(it, t, imgJob) {
                        numberJob = this
                    }
                }
            }
        }

        override fun FileSack.onClick(i: Int) {
            if (isActionMode) {
                if (!this@onClick.isSelect) {
                    binder.rowFile.apply {
                        setBackgroundColor(themA.findAttr(R.attr.colorPrimaryAlpha))
                    }
                    this@onClick.isSelect = true
                    deleteCopy.add(this@onClick)
                    selectedViewsNative = selectedViews.add(binder.rowFile)
                    picListener?.apply {
                        onActionDisplay(showCard = false, text = txtTitleOption)
                    }
                } else {
                    binder.rowFile.apply {
                        ctxA.compactImage(R.drawable.ripple_curvy) {
                            background = this@compactImage
                        }
                    }
                    this@onClick.isSelect = false
                    deleteCopy.remove(this@onClick)
                    selectedViewsNative = selectedViews.remove(binder.rowFile)
                    if (deleteCopy.isNotEmpty()) {
                        picListener?.apply {
                            onActionDisplay(showCard = false, text = txtTitleOption)
                        }
                    } else {
                        picListener?.apply {
                            onActionDisplay(showCard = false, text = null)
                        }
                    }
                }
            } else {
                picListener?.apply {
                    this@onClick.onRealItemCLick(filePath.toStr.excludeAds, false)
                }
            }
        }

        override fun android.view.View.onLongClick(it: FileSack) {
            if (!isActionMode) {
                binder.rowFile.setBackgroundColor(themA.findAttr(R.attr.colorPrimaryAlpha))
                isActionMode = true
                selectedViewsNative = selectedViews.add(binder.rowFile)
                deleteCopy.add(it)
                it.isSelect = true
                picListener?.apply {
                    onActionDisplay(showCard = true, text = txtTitleOption)
                }
            }
        }

        override suspend fun FolderFasten.clear(i: Int) {}

        override fun clear() {
            imgJob?.cancel()
            typeJob?.cancel()
            numberJob?.cancel()
            imgJob = null
            typeJob = null
            numberJob = null
        }

        override fun FolderFasten.finalClear() {}
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


    @com.pt.common.global.UiAnn
    override fun com.pt.common.moderator.over.GlideImageView.loadImgAsync(
        media: FileSack, str: String,
        imgJob: kotlinx.coroutines.Job?.() -> Unit
    ) {
        if (!FileLate(media.filePath).exists()) return

        when (media.typeFile) {
            IMAGE, VIDEO -> {
                launchMain {
                    loadImageForManager(media, str = str)
                }.let(imgJob)
            }
            UNKNOWN -> {
                launchMain {
                    applySus {
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
                }.let(imgJob)
            }
            AUDIO -> {
                launchDef {
                    context.getMusicBit(
                        media.filePath, media.filePath.failedSour, 4
                    ) {
                        displayAudioImage(DSackV(this@getMusicBit, str), imgJob)
                    }
                }.let(imgJob)
            }
            else -> {
                launchMain {
                    context.applySus {
                        media.typeFile.let {
                            setImageDrawable(
                                it.fetchSvgByType ?: compactImageReturn(it.checkFileImage)
                            )
                        }
                    }
                }.let(imgJob)
            }
        }
    }

    private fun com.pt.common.moderator.over.GlideImageView.displayAudioImage(
        bit: DSackV<android.graphics.Bitmap?, String>,
        imgJob: kotlinx.coroutines.Job?.() -> Unit
    ) {
        launchMain {
            justCoroutineMain {
                if (tag == bit.two) {
                    setImageBitmap(bit.one)
                } else {
                    bit.one = null
                }
            }
        }.let(imgJob)
    }

    override fun FolderFasten.loadAsyncType(
        media: FileSack, str: String, imgJob: kotlinx.coroutines.Job?,
        typeJob: kotlinx.coroutines.Job?.() -> Unit
    ) {
        launchDef {
            imgJob?.checkIfDone()
            withBack {
                fileCount(media.filePath) {
                    launchMain {
                        displayLoadAsyncType(str, it.toStr)
                    }.let(typeJob)
                }
            }
            withBack {
                root_.context.applySusBack {
                    contentResolver.getFolderSize(
                        media, SIZE_MEGA, SIZE_GIGA
                    ) {
                        launchMain {
                            displayAsyncType(str, it)
                        }.let(typeJob)
                    }
                }
            }
        }.let(typeJob)
    }

    @com.pt.common.global.UiAnn
    override suspend fun FolderFasten.displayLoadAsyncType(str: String, it: String) {
        withMain {
            if (folderImage.tag == str) fileNumber.text = it
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun FolderFasten.displayAsyncType(str: String, it: String) {
        withMain {
            if (folderImage.tag == str) {
                fileType.text = it
            }
        }
    }

    internal val txtTitleOption: String
        get() = deleteCopy.size.toString() + "/" + realSize

    override fun FolderFasten.loadAsyncTxtNumber(
        media: FileSack, str: String, imgJob: kotlinx.coroutines.Job?,
        numberJob: kotlinx.coroutines.Job?.() -> Unit
    ) {
        launchDef {
            imgJob?.checkIfDone()
            withBack {
                root_.context.contentResolver.findIDForArtist(
                    media.filePath, FileLate(media.filePath).extension.trim()
                ) {
                    launchMain {
                        root_.context.nullChecker()
                        displayLoadAsyncTxtNumber(str, it)
                    }.let(numberJob)
                }
            }
        }.let(numberJob)
    }

    override suspend fun FolderFasten.displayLoadAsyncTxtNumber(str: String, it: String) {
        withMain {
            if (folderImage.tag == str) {
                fileNumber.text = it
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override suspend fun android.content.ContentResolver.getFolderSize(
        it: FileSack,
        megaBText: String,
        gigaBText: String,
        s: (String) -> Unit,
    ) {
        getListFiles(FileLate(it.filePath)) {
            s(it.reformatSize(megaBText, gigaBText))
        }
    }

}