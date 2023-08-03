package com.pt.pro.gallery.adapters

import com.pt.common.global.*
import com.pt.common.mutual.adapter.GlobalAdapter
import com.pt.common.mutual.adapter.GlobalAdapterLong
import com.pt.common.objects.MediaDuoTime
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.gallery.interfaces.GalleryListener
import com.pt.pro.gallery.interfaces.TimeListener
import com.pt.pro.gallery.fasten.TimeFasten
import com.pt.pro.gallery.fasten.TittleFasten

class TimeAdapter(
    override val foldsDuo: MutableList<MediaDuoTime>,
    override var picListener: GalleryListener?,
    override val widthColumn: Int,
    override val increase: Int,
    override val span: Int,
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(), TimeListener {

    override var isHiddenActive: Boolean = false

    private val media: MutableList<MediaSack> = mutableListOf()

    override var adPosition: Int = 0

    override val runForLook: (Int) -> Int
        get() = {
            if (foldsDuo.getI(it).typeItem == ITEM_NORMAL) {
                1
            } else {
                span
            }
        }

    private var txtColorNative: Int? = null
    private var accentColorNative: Int? = null

    internal inline val android.content.Context.txtColor: Int
        get() = txtColorNative ?: theme.findAttr(R.attr.rmoText)
            .also { txtColorNative = it }

    internal inline val android.content.Context.accentColor: Int
        get() = accentColorNative ?: theme.findAttr(android.R.attr.colorAccent)
            .also { accentColorNative = it }


    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder = kotlin.run {
        if (viewType == ITEM_NORMAL) HolderMedia(parent.bindInflate)
        else TitleMedia(parent.bindInflateTitle)
    }

    private inline val android.view.ViewGroup.bindInflate: TimeFasten
        get() = com.pt.pro.gallery.fasten.GalleryInflater.run { context.inflaterTime(widthColumn) }

    private inline val android.view.ViewGroup.bindInflateTitle: TittleFasten
        get() = com.pt.pro.gallery.fasten.GalleryInflater.run { context.inflaterTitle() }

    override fun getItemViewType(position: Int): Int = foldsDuo.getI(position).typeItem

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        adPosition = position
        if (holder.itemViewType == ITEM_NORMAL) (holder as HolderMedia).bind()
        else (holder as TitleMedia).bind()
    }

    @com.pt.common.global.UiAnn
    override fun onViewAttachedToWindow(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.itemViewType == ITEM_NORMAL) (holder as HolderMedia).attach()
        else (holder as TitleMedia).attach()
    }

    @com.pt.common.global.UiAnn
    override fun onViewDetachedFromWindow(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.itemViewType == ITEM_NORMAL) (holder as HolderMedia).clear()
        else (holder as TitleMedia).clear()
    }

    override fun onViewRecycled(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder.itemViewType == ITEM_NORMAL) (holder as HolderMedia).finalClear()
        else (holder as TitleMedia).finalClear()
    }

    override suspend fun MutableList<MediaDuoTime>.updateDuoTime() {
        val siz = foldsDuo.size
        withMain {
            clearer()
            justRemove(siz)
            toMutableList().adder()
            justAdd(size)
            addMedia()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun clearer() {
        justCoroutineMain {
            foldsDuo.clear()
            media.clear()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun MutableList<MediaDuoTime>.adder() {
        justCoroutineMain {
            foldsDuo.addAll(this@adder)
        }
    }

    private suspend fun addMedia() {
        justCoroutineMain {
            foldsDuo.forEach {
                it.mediaHolder?.let { it1 -> media.add(it1) }
            }
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
        adPosition = 0
        media.clear()
        foldsDuo.clear()
        picListener = null
        accentColorNative = null
        txtColorNative = null
    }

    override fun getItemCount(): Int = foldsDuo.size

    inner class TitleMedia(
        item: TittleFasten,
    ) : GlobalAdapter<TittleFasten, MediaDuoTime>(item) {

        override val Int.item: MediaDuoTime
            get() = foldsDuo.getI(this)

        override fun TittleFasten.bind() {
            root_.setOnClickListener(this@TitleMedia)
        }

        override fun TittleFasten.attach(it: MediaDuoTime) {
            if (it.folderName == com.pt.common.BuildConfig.FAVORITE) {
                ctxA.accentColor
            } else {
                ctxA.txtColor
            }.let { itC ->
                fileName.setTextColor(itC)
                extend.setTextColor(itC)
            }
            fileName.text = it.folderName
            extend.text = it.numberOfPics.toString()
        }

        override fun TittleFasten.clear() {

        }

        override fun MediaDuoTime.onClick(i: Int) {
            if (folderName == com.pt.common.BuildConfig.FAVORITE) {
                picListener?.apply {
                    DSack(
                        -1L,
                        -1L,
                        com.pt.common.BuildConfig.FAVORITE
                    ).onTimeClicked(DISPLAY_FAVORITES)
                }
            } else {
                (foldsDuo[posA + 1].mediaHolder ?: (foldsDuo[posA + 2].mediaHolder))?.also {
                    it.dateModified.timeInDay.let { (one, two) ->
                        picListener?.apply {
                            DSack(
                                one / 1000,
                                two / 1000,
                                folderName.toStr
                            ).onTimeClicked(if (isHiddenActive) DISPLAY_HIDDEN_TIME else DISPLAY_TIME)
                        }
                    }
                }
            }

        }

    }

    @com.pt.common.global.UiAnn
    inner class HolderMedia(
        item: TimeFasten,
    ) : GlobalAdapterLong<TimeFasten, MediaDuoTime>(item) {

        override val Int.item: MediaDuoTime
            get() = foldsDuo.getI(this)

        override fun TimeFasten.bind() {
            picture.apply {
                setOnClickListener(this@HolderMedia)
                setOnLongClickListener(this@HolderMedia)
            }
        }

        override fun TimeFasten.attach(it: MediaDuoTime, i: Int) {
            it.mediaHolder?.let { ms ->
                if (ms.isImage) videoPlay.justGone() else videoPlay.justVisible()
                ms.pathMedia.toStr.also { str ->
                    picture.apply {
                        tag = str
                        loadPicture(image = ms, increase = increase, str = str)
                    }
                }
            }
        }

        override fun TimeFasten.clear() {}

        override fun finalClear() {}

        override fun android.view.View.onClick(it: MediaDuoTime) {
            picListener?.apply {
                it.mediaHolder?.let { current ->
                    media.indexOfFirst {
                        it.pathMedia.toStr == current.pathMedia.toStr && it.isFromFav == current.isFromFav
                    }
                }?.let { pos ->
                    if (pos == -1) {
                        return
                    } else {
                        media.onPicClicked(pos) {
                            catchy(Unit) {
                                binder.root_.startAnimation(scaleDownAnimation)
                            }
                        }
                    }
                }
            }
        }

        override fun android.view.View.onLongClick(it: MediaDuoTime): Boolean {
            picListener?.apply {
                it.mediaHolder?.addMediaPending()
            }
            return true
        }
    }

}