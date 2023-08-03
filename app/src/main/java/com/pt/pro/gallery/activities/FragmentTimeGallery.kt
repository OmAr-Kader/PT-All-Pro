package com.pt.pro.gallery.activities

import android.view.View
import android.view.ViewGroup
import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.moderator.recycler.NoAnimGridManager
import com.pt.common.stable.*
import com.pt.pro.gallery.adapters.TimeAdapter
import com.pt.pro.gallery.interfaces.GalleryCommonInterface
import com.pt.pro.gallery.interfaces.GalleryListener
import com.pt.common.objects.MediaDuoTime
import com.pt.pro.gallery.fasten.StubGalleryTimeFasten
import kotlin.math.abs

class FragmentTimeGallery : ParentFragmentGallery<StubGalleryTimeFasten>(),
    GalleryCommonInterface, View.OnTouchListener {

    override var itemListener: GalleryListener? = null

    private var columnNumber = 4
    private var duoAdapter: TimeAdapter? = null

    @Volatile
    private var hiddenRunning = false

    @Volatile
    private var galleryMode: Int = GALLERY_ALL

    private var increase: Int = 400

    private var isTouch = false
    private var ratioPlus: Float = 0.0F
    private var endScroll: Float = 0.0F
    private var endHeight: Float = 0.0F
    private var endRatio: Float = 0.0F
    private var start: Float = 0.0F

    @Volatile
    private var widthColumn: Int = 360
    private var scaleGestureDetector: android.view.ScaleGestureDetector? = null

    private var currentDuo: MutableList<MediaDuoTime>? = null

    override val android.view.LayoutInflater.creBin: ViewGroup?.() -> View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run { this@creBin.context.inflaterGalleryTime() }.also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }

    private suspend fun android.util.Size.intiViewDim(itC: Int) {
        columnNumber = itC
        increase = ctx.findBooleanPrefDb(ID_RESOLE, RES_NUM, false).let { itR ->
            if (!itR) itC.findResole else itC.findHighResole
        }
        widthColumn = if (!rec.isLandTraditional) {
            this@intiViewDim.width / itC
        } else {
            this@intiViewDim.width / (itC * 2)
        }
    }

    @com.pt.common.global.UiAnn
    @android.annotation.SuppressLint("ClickableViewAccessibility")
    override fun StubGalleryTimeFasten.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentTimeGallery)
        recGallery.setOnTouchListener(this@FragmentTimeGallery)
        pointerScroll.addSenseListener(false) { _, event, type ->
            when (type) {
                DOWN_SEN -> {
                    isTouch = true
                }
                UP_SEN -> {
                    isTouch = false
                }
                MOVE_SEN -> {
                    if (allMedia.isEmpty()) return@addSenseListener
                    endScroll = when {
                        event.rawY < start -> 0.0F
                        event.rawY > endHeight -> endRatio
                        else -> event.rawY - start
                    }
                    ratioPlus = when (endScroll) {
                        0.0F -> 0F
                        endRatio -> (allMedia.size - 1).toFloat()
                        else -> allMedia.size * (abs(endScroll) / endRatio)
                    }
                    binding.apply {
                        pointerScroll.y = endScroll
                        recGallery.scrollToPosition(ratioPlus.toInt())
                    }
                }
            }

        }
        recGallery.scrollListener {
            funScrollChangeDis()
        }
        launchImdMain {
            context.nullChecker()
            intiViews()
        }
    }

    private suspend fun StubGalleryTimeFasten.intiViews() {
        withMain {
            context?.findIntegerPrefDb?.invoke(ID_COL, COL_NUM, 3)?.let {
                scaleGestureDetector = android.view.ScaleGestureDetector(
                    ctx,
                    SimpleOnScale(it.toFloat())
                )
                intiRec(it)
            }
        }
    }

    internal suspend fun StubGalleryTimeFasten.intiRec(span: Int) {
        act.windowManager?.fetchDimensionsSus {
            this@fetchDimensionsSus.intiViewDim(span)
            withMain {
                justCoroutineMain {
                    recGallery.layoutManager = NoAnimGridManager(
                        ctx,
                        if (rec.isLandTraditional) span * 2 else span
                    )
                }
                doIntiRec(span = span, isLand = rec.isLandTraditional)
                justCoroutineMain {
                    root_.fetchViewDim { _, rootHeight ->
                        doIntiDim(
                            dimHeight = this@fetchDimensionsSus.height,
                            rootHeight = rootHeight,
                            act = ctx.actionBarHeight,
                            status = resources.statusBarHeight,
                        )
                    }
                }
            }
        }
    }

    private suspend fun StubGalleryTimeFasten.doIntiRec(span: Int, isLand: Boolean) {
        TimeAdapter(
            mutableListOf(),
            picListener = itemListener,
            widthColumn = widthColumn,
            increase = increase,
            span = if (isLand) span * 2 else span,
        ).alsoSus {
            it.isHiddenActive = this@FragmentTimeGallery.hiddenRunning
            recGallery.applySus {
                (layoutManager as NoAnimGridManager).setLook(it.runForLook)
                duoAdapter = it
                adapter = it
            }
        }
        justCoroutine {
            currentDuo?.loadAll() ?: runSus {
                if (hiddenRunning) {
                    scannerHidden(galleryMode)
                } else {
                    doInitAllLoad(galleryMode)
                }
            }
        }
    }

    private fun doIntiDim(
        dimHeight: Int,
        rootHeight: Int,
        act: Int,
        status: Int,
    ) {
        (rootHeight + act + status).also { realHeight ->
            updateDim(
                (realHeight).toFloat(),
                (dimHeight - realHeight),
                act,
                status
            )
        }
    }

    private inner class SimpleOnScale(
        siz: Float
    ) : android.view.ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var newProduct: Float = siz
        override fun onScale(detector: android.view.ScaleGestureDetector): Boolean {
            catchy(Unit) {
                ((newProduct / detector.scaleFactor)).also { product ->
                    newProduct = product
                    toCatchSackAfter(234, 250L) {
                        catchy(Unit) {
                            binder?.recGallery?.apply {
                                if ((layoutManager as NoAnimGridManager).spanCount == product.toInt() ||
                                    product.toInt() < 1
                                ) return@toCatchSackAfter
                                launchMain {
                                    justCoroutineMain {
                                        binder?.intiRec(product.toInt())
                                    }
                                }
                            }
                        }
                    }.postAfter()
                }
            }
            return true
        }
    }

    @com.pt.common.global.UiAnn
    private fun funScrollChangeDis() {
        binding.runCatching {
            duoAdapter?.adPosition?.let { position ->
                if (position != 0 && !isTouch) {
                    (allMedia.size - position).let {
                        when {
                            it > (columnNumber * 3) -> (position - (columnNumber * 3))
                            position > allMedia.size -> allMedia.size
                            else -> position
                        }
                    }.let {
                        (it.toFloat() / allMedia.size.toFloat())
                    }.let {
                        if (it > 0) it * endRatio else 0F
                    }.let {
                        binding.apply {
                            pointerScroll.y = it
                        }
                    }
                }
            }

        }
    }


    private fun updateDim(heightPar: Float, nav: Int, act: Int, status: Int) {
        (status.toFloat() + act).let { c ->
            start = c
            endRatio = (heightPar - c - 70F.toPixel - nav)
            endHeight = endRatio + start
        }
    }

    @com.pt.common.global.UiAnn
    override fun StubGalleryTimeFasten.onClick(v: View) {
    }

    @com.pt.common.global.UiAnn
    override fun StubGalleryTimeFasten.onLongClick(v: View): Boolean {

        return false
    }

    override fun setISHidden(boolean: Boolean, galleryM: Int) {
        hiddenRunning = boolean
        galleryMode = galleryM
    }

    override fun doInitAllLoad(galleryM: Int) {
        hiddenRunning = false
        galleryMode = galleryM
        duoAdapter?.isHiddenActive = false
        launchDef {
            displayCardInti()
            loadAllForFolder(galleryM) {
                loadFavoritePic {
                    context.nullChecker()
                    withBack {
                        this@loadAllForFolder.loadByTime(this@loadFavoritePic)
                    }
                }
            }
        }
    }

    @android.annotation.SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: android.view.MotionEvent): Boolean {
        return if (event.pointerCount == 2) {
            scaleGestureDetector?.onTouchEvent(event)
            true
        } else {
            false
        }
    }

    private suspend fun displayCardInti() {
        withMain {
            if (binding.galleryBarCard.isGon) {
                binding.galleryBarCard.loadRun.rKTSack(LOAD_CONST).postAfterIfNot()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.loadByTime(
        af: MutableList<MediaDuoTime>,
    ) {
        justCoroutine {
            val currentTime = System.currentTimeMillis()
            var con = 1
            var lastTime = System.currentTimeMillis() + 86400000

            withBackDef(mutableListOf()) {
                val duo: MutableList<MediaDuoTime> = mutableListOf()

                this@loadByTime.onEachSusBack(context) {
                    if (!DSackT(this@onEachSusBack.dateModified, lastTime).isSameDay) {
                        duo.indexOfLast { itD ->
                            itD.typeItem == ITEM_NORMAL_TWO
                        }.let { index ->
                            if (index != -1) duo[index] = duo[index].copy(numberOfPics = con)
                        }

                        MediaDuoTime(
                            null,
                            DSackT(this@onEachSusBack.dateModified, currentTime).titleTime,
                            0,
                            null,
                            ITEM_NORMAL_TWO
                        ).let { itT ->
                            duo.add(itT)
                        }
                        MediaDuoTime(
                            null,
                            null,
                            0,
                            this@onEachSusBack,
                            ITEM_NORMAL
                        ).let { itNewM ->
                            duo.add(itNewM)
                        }
                        con = 1
                    } else {
                        MediaDuoTime(
                            null,
                            null,
                            0,
                            this@onEachSusBack,
                            ITEM_NORMAL
                        ).let { itNewM ->
                            duo.add(itNewM)
                        }
                        con += 1
                    }
                    lastTime = this@onEachSusBack.dateModified
                }
                duo
            }.letSusBack { duo ->
                context.nullChecker()
                withBackDef(mutableListOf()) {
                    mutableListOf<MediaDuoTime>().apply {
                        this@apply.addAll(af)
                        this@apply.addAll(duo)
                    }
                }.displayLoadByTime()
            }
        }
    }

    private suspend fun MutableList<MediaDuoTime>.displayLoadByTime() {
        justCoroutine {
            currentDuo = this@displayLoadByTime
        }
        withMain {
            loadAll()
        }
    }

    private inline val DSackT<Long, Long>.titleTime: String
        get() {
            return if (isSameDay) {
                rec.getString(com.pt.pro.R.string.dw)
            } else {
                com.pt.common.BuildConfig.TIME_DAY.toDataFormat(one)
            }
        }

    override fun toRefresh() {
        if (binding.galleryBarCard.isGon) {
            launchDef {
                swipeToRefreshAll()
            }
        }
    }

    private suspend fun swipeToRefreshAll() {
        withBack {
            if (hiddenRunning) {
                scannerHidden(galleryMode)
            } else {
                displayCardInti()
                ctx.newDBGallerySus {
                    getAllVFavMedia()
                }.let { favRefresh ->
                    loadAllForFolder(galleryMode) {
                        withBack {
                            if (allMedia.size != (favRefresh.size + this@loadAllForFolder.size)) {
                                loadFavoritePic {
                                    context.nullChecker()
                                    withBack {
                                        this@loadAllForFolder.loadByTime(this@loadFavoritePic)
                                    }
                                }
                            } else {
                                binding.displayAfterHidden()
                            }
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend inline fun loadAllForFolder(
        galleryM: Int,
        crossinline list: suspend MutableList<MediaSack>.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            when (galleryM) {
                GALLERY_VID -> cont.loadAllVideoTime(com.pt.common.BuildConfig.BY_DATE_FOLDER_DESC)
                GALLERY_IMG -> cont.loadAllImageTime(com.pt.common.BuildConfig.BY_DATE_FOLDER_DESC)
                else -> cont.loadAllMediaSacks(com.pt.common.BuildConfig.BY_DATE_FOLDER_DESC)
            }
        }.applySusBack(list)
    }


    override fun scannerHidden(galleryM: Int) {
        hiddenRunning = true
        galleryMode = galleryM
        duoAdapter?.isHiddenActive = true
        launchDef {
            binding.beforeScannerHidden()
            context.nullChecker()
            loadAllHidden(galleryM)
            context.nullChecker()
            binding.displayAfterHidden()
        }
    }

    private suspend fun StubGalleryTimeFasten.beforeScannerHidden() {
        withMain {
            if (galleryBarCard.isGon) {
                binding.galleryBarCard.loadRun.rKTSack(LOAD_CONST).postAfterIfNot()
            }
            recGallery.recycledViewPool.clear()
            duoAdapter?.apply {
                mutableListOf<MediaDuoTime>().updateDuoTime()
            }
        }
    }

    private suspend fun MutableList<MediaDuoTime>.loadAll() {
        displayLoadAll()
        context.nullChecker()
        doLoadAll()
    }

    @com.pt.common.global.UiAnn
    private suspend fun MutableList<MediaDuoTime>.displayLoadAll() {
        withMain {
            binding.recGallery.recycledViewPool.clear()
            duoAdapter?.apply {
                updateDuoTime()
            }
            binding.galleryBarCard.apply {
                unPost(loadRun.two)
                justGoneSus()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaDuoTime>.doLoadAll() {
        withBack {
            allMedia.clear()
            forEach {
                it.mediaHolder?.let { it1 -> allMedia.add(it1) }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun loadAllHidden(galleryM: Int) {
        allMedia.clear()
        val newList: MutableList<MediaSack> = mutableListOf()
        withBackDef(mutableListOf()) {
            ctx.newDBGallerySus {
                getAllHiddenMedia()
            }
        }.toMutableList().asSequence().runSusBack {
            when (galleryM) {
                GALLERY_VID -> this@runSusBack.filter { !it.isImage }
                GALLERY_IMG -> this@runSusBack.filter { it.isImage }
                else -> this@runSusBack
            }.toMutableList()
        }.letSusBack { fhH ->
            withBackDef(mutableListOf()) {
                if (context == null || !hiddenRunning)
                    mutableListOf()
                else
                    fhH
            }.let {
                withBack {
                    newList.addAll(it)
                }
            }
        }
        context.nullChecker()
        withBack {
            loadFavoritePic {
                newList.orderAllLoaders(com.pt.common.BuildConfig.BY_DATE_DESC).let { aOrder ->
                    withBack {
                        aOrder.toMutableList().loadByTime(this@loadFavoritePic)
                    }
                }
            }
        }
        context.nullChecker()
        allMedia.addAll(newList)
    }

    private suspend fun StubGalleryTimeFasten.displayAfterHidden() {
        context.nullChecker()
        withMain {
            galleryBarCard.apply {
                unPost(loadRun.two)
                justGoneSus()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend inline fun loadFavoritePic(
        crossinline list: suspend MutableList<MediaDuoTime>.() -> Unit,
    ) {
        withBackDef(mutableListOf()) {
            ctx.newDBGallerySus {
                getAllVFavMedia()
            }
        }.letSusBack { a ->
            context.nullChecker()
            withBackDef(mutableListOf()) {
                if (a.isNotEmpty()) {
                    val newArTime: MutableList<MediaDuoTime> = mutableListOf()
                    MediaDuoTime(
                        null,
                        com.pt.common.BuildConfig.FAVORITE,
                        a.size,
                        null,
                        ITEM_NORMAL_TWO
                    ).let {
                        newArTime.add(it)
                    }
                    a.forEach { itNew ->
                        MediaDuoTime(
                            null,
                            null,
                            0,
                            itNew,
                            ITEM_NORMAL
                        ).let {
                            newArTime.add(it)
                        }
                    }
                    newArTime
                } else {
                    mutableListOf()
                }
            }
        }.applySusBack(list)
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        launchImdMain {
            binder?.intiViews()
        }
    }

    override fun onDestroyView() {
        scaleGestureDetector = null
        duoAdapter?.isHiddenActive = false
        duoAdapter?.onAdapterDestroy()
        allMediaNative = null
        allFavoriteNative = null
        itemListener = null
        columnNumber = 4
        duoAdapter = null
        hiddenRunning = false
        increase = 400
        catchy(Unit) {
            binder?.recGallery?.adapter = null
            binder?.recGallery?.removeAllViewsInLayout()
            binder?.root?.removeAllViewsInLayout()
        }
        currentDuo = null
        super.onDestroyView()
    }

}