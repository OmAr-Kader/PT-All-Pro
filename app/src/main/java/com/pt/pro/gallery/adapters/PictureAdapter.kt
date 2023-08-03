package com.pt.pro.gallery.adapters

import androidx.appcompat.widget.AppCompatImageView
import com.pt.common.global.*
import com.pt.common.mutual.adapter.GlobalAdapterLong
import com.pt.common.stable.*
import com.pt.pro.gallery.fasten.GalleryInflater
import com.pt.pro.gallery.fasten.PicItemFasten

class PictureAdapter(
    override val span: Int,
) : androidx.recyclerview.widget.RecyclerView.Adapter<PictureAdapter.HolderNew>(),
    com.pt.pro.gallery.interfaces.PicListener {

    override var picListener: com.pt.pro.gallery.interfaces.DisplayListen? = null
    override var widthX: Int = 120
    override var increase: Int = 3
    override val mediaList: MutableList<MediaSack> = mutableListOf()

    internal var selectedViewsNative: Sequence<AppCompatImageView>? = emptySequence()
    internal inline val selectedViews: Sequence<AppCompatImageView>
        get() = selectedViewsNative ?: emptySequence<AppCompatImageView>().also {
            selectedViewsNative = it
        }

    private var selectAll: Boolean = false
    private var actionEnable: Boolean = false
    override val isActionMode: Boolean get() = actionEnable

    private var deleteCopyNative: MutableList<MediaSack>? = mutableListOf()
    override val deleteCopy: MutableList<MediaSack>
        get() = deleteCopyNative ?: mutableListOf<MediaSack>().also {
            deleteCopyNative = it
        }

    override val pos: MutableList<Int> = mutableListOf()
    override var adPosition: Int = 0

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): PictureAdapter.HolderNew = HolderNew(parent.bindInflate)

    private inline val android.view.ViewGroup.bindInflate: PicItemFasten
        get() = GalleryInflater.run { context.inflaterPicItem(widthX) }

    override fun getItemViewType(position: Int): Int = mediaList.getI(position).typeItem

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: PictureAdapter.HolderNew, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = mediaList.size

    @com.pt.common.global.UiAnn
    override fun onViewAttachedToWindow(holder: PictureAdapter.HolderNew) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    @com.pt.common.global.UiAnn
    override fun onViewDetachedFromWindow(holder: PictureAdapter.HolderNew) {
        super.onViewDetachedFromWindow(holder)
        holder.clear()
    }

    override fun onViewRecycled(holder: PictureAdapter.HolderNew) {
        super.onViewRecycled(holder)
        holder.finalClear()
    }

    internal inline val String.excludeAds: Int
        get() {
            return mediaList.indexOfFirst {
                it.pathMedia == this@excludeAds
            }
        }


    override suspend fun MutableList<MediaSack>.updateMedia() {
        actionEnable = false
        val siz = mediaList.size
        withMain {
            clearer()
            justRemove(siz)
            toMutableList().adder()
            justAdd(size)
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun clearer() {
        justCoroutineMain {
            mediaList.clear()
        }
    }

    private suspend fun MutableList<MediaSack>.adder() {
        justCoroutine {
            this@adder.onEachSusBack(true) {
                isSelect = false
            }
        }.letSus { new ->
            justCoroutineMain {
                mediaList.addAll(new)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun justNotify() {
        justRemove(mediaList.size)
        justAdd(mediaList.size)
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

    override suspend fun selectAll() {
        doSelect()
        doAddToDelete()
        justRemove(mediaList.size)
        actionSelect()
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun doSelect() {
        withBack {
            selectAll = true
            mediaList.onEachSus(true) {
                this@onEachSus.isSelect = true
            }
            deleteCopy.clear()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun doAddToDelete() {
        withBack {
            deleteCopy.addAll(mediaList)
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun actionSelect() {
        withBack {
            picListener?.onActionDisplay(
                text = deleteCopy.size.toStr + "/" + mediaList.size, deleteCopy.size
            )
        }
    }

    override suspend fun refreshAdapter() {
        actionEnable = false
        val sameSize: Boolean = deleteCopy.size == selectedViews.count()
        withMain {
            justCoroutineMain {
                deleteCopy.onEachSus(true) {
                    this@onEachSus.isSelect = false
                }
            }
            justCoroutineMain {
                deleteCopy.clear()
                if (!selectAll && sameSize) {
                    runCatching {
                        if (selectedViews.count() != 0) {
                            selectedViews.forEach {
                                it.justGoneSus()
                            }
                        }
                    }.onFailure {
                        justNotify()
                    }
                } else {
                    selectAll = false
                    justNotify()
                }
            }
            justCoroutineMain {
                selectedViewsNative = emptySequence()
            }
        }
        //notifyDataSetChanged()
    }

    override fun onAdapterDestroy() {
        selectedViewsNative = null
        deleteCopyNative = null
        mediaList.clear()
        pos.clear()
        adPosition = 0
        picListener = null
    }

    @com.pt.common.global.UiAnn
    inner class HolderNew(
        item: PicItemFasten
    ) : GlobalAdapterLong<PicItemFasten, MediaSack>(item), android.view.View.OnLongClickListener {

        override val Int.item: MediaSack
            get() = mediaList.getI(this)

        override fun PicItemFasten.bind() {
            picture.apply {
                setOnClickListener(this@HolderNew)
                setOnLongClickListener(this@HolderNew)
            }
        }

        override fun PicItemFasten.attach(it: MediaSack, i: Int) {
            adPosition = i
            if (it.isImage) videoPlay.justGone()
            else videoPlay.justVisible()

            if (it.isSelect && isActionMode) selected.justVisible()
            else selected.justGone()
            it.pathMedia.toStr.also { str ->
                picture.apply {
                    tag = str
                    loadPicture(image = it, increase = increase, str = str)
                }
            }
        }

        override fun PicItemFasten.clear() {}

        override fun finalClear() {}

        override fun android.view.View.onClick(it: MediaSack) {
            if (isActionMode && !it.isSelect) {
                binder.selected.justVisible()
                it.isSelect = true
                selectedViewsNative = selectedViews.add(binder.selected)
                deleteCopy.add(it)
                pos.add(posA)
                picListener?.onActionDisplay(
                    deleteCopy.size.toStr + "/" + mediaList.size, deleteCopy.size
                )
            } else if (isActionMode && it.isSelect) {
                binder.selected.justGone()
                it.isSelect = false
                if (selectedViews.count() != 0) {
                    selectedViewsNative = selectedViews.remove(binder.selected)
                }
                if (deleteCopy.isNotEmpty()) deleteCopy.remove(it)
                if (pos.isNotEmpty()) pos.remove(posA)
                picListener?.onActionDisplay(
                    deleteCopy.size.toStr + "/" + mediaList.size, deleteCopy.size
                )
            } else {
                picListener?.apply {
                    mediaList.onPicClicked(it.pathMedia.toStr.excludeAds) {
                        kotlin.runCatching {
                            binder.root_.startAnimation(scaleDownAnimation)
                        }
                    }
                }
            }
        }

        override fun android.view.View.onLongClick(it: MediaSack): Boolean {
            return if (!isActionMode) {
                deleteCopy.add(it)
                actionEnable = true
                it.isSelect = true
                binder.selected.justVisible()
                pos.add(posA)
                val t = deleteCopy.size.toStr + "/" + mediaList.size
                picListener?.justDisplayAction(t)
                picListener?.onActionDisplay(
                    t, deleteCopy.size
                )
                selectedViewsNative = selectedViews.add(binder.selected)
                true
            } else false
        }
    }

}
