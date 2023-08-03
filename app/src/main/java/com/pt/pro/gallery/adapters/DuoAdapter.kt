package com.pt.pro.gallery.adapters

import com.pt.common.global.*
import com.pt.common.stable.*

class DuoAdapter(
    override val foldsDuo: MutableList<com.pt.common.objects.MediaDuo>,
    override var picListener: com.pt.pro.gallery.interfaces.GalleryListener?,
    override val widthColumn: Int,
    override val increase: Int,
    override val isRightToLeft: Boolean,
    override val col: Int,
) : androidx.recyclerview.widget.RecyclerView.Adapter<DuoAdapter.HolderMedia>(), com.pt.pro.gallery.interfaces.DuoListener {

    override val observerDestroy: MutableList<() -> Unit> = mutableListOf()

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): DuoAdapter.HolderMedia = HolderMedia(parent.bindInflate)

    private inline val android.view.ViewGroup.bindInflate: com.pt.pro.gallery.fasten.RowAllFasten
        get() = com.pt.pro.gallery.fasten.GalleryInflater.run { context.inflaterRowAll(widthColumn = widthColumn, isRightToLeft = isRightToLeft) }


    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: DuoAdapter.HolderMedia, position: Int) {
        holder.bind()
    }

    override fun onViewRecycled(holder: DuoAdapter.HolderMedia) {
        super.onViewRecycled(holder)
        holder.finalClear()
    }

    override fun onViewDetachedFromWindow(holder: DuoAdapter.HolderMedia) {
        super.onViewDetachedFromWindow(holder)
        holder.clear()
    }

    @com.pt.common.global.UiAnn
    override fun onViewAttachedToWindow(holder: DuoAdapter.HolderMedia) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    override fun getItemCount(): Int = foldsDuo.size

    override fun getItemViewType(position: Int): Int = foldsDuo.getI(position).typeItem

    override suspend fun MutableList<com.pt.common.objects.MediaDuo>.updateDuo() {
        val siz = foldsDuo.size
        clearer()
        justRemove(siz)
        toMutableList().adder()
        justAdd(size)
    }

    @com.pt.common.global.MainAnn
    private suspend fun clearer() {
        withMain {
            foldsDuo.clear()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun MutableList<com.pt.common.objects.MediaDuo>.adder() {
        withMain {
            foldsDuo.addAll(this@adder)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justRemove(siz: Int) {
        withMain {
            notifyItemRangeRemoved(0, siz)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justAdd(siz: Int) {
        withMain {
            notifyItemRangeInserted(0, siz)
        }
    }

    override fun onAdapterDestroy() {
        observerDestroy.forEach {
            it.invoke()
        }
        foldsDuo.clear()
        picListener = null
        observerDestroy.clear()
    }

    @com.pt.common.global.UiAnn
    inner class HolderMedia(
        item: com.pt.pro.gallery.fasten.RowAllFasten,
    ) : com.pt.common.mutual.adapter.GlobalAdapter<com.pt.pro.gallery.fasten.RowAllFasten, com.pt.common.objects.MediaDuo>(item) {

        override val Int.item: com.pt.common.objects.MediaDuo
            get() = foldsDuo.getI(this)

        override fun com.pt.pro.gallery.fasten.RowAllFasten.bind() {
            extendFile.setOnClickListener(this@HolderMedia)
            folderRecyclerAll.apply {
                layoutAnimation = ctxA.scaleDownAnimationRec(dur = 200L, del = 0.1F)
                layoutManager = ctxA.getManager
            }
        }

        override fun com.pt.pro.gallery.fasten.RowAllFasten.attach(it: com.pt.common.objects.MediaDuo) {
            if (it.isFav) {
                themA.findAttr(android.R.attr.colorAccent)
            } else {
                themA.findAttr(com.pt.pro.R.attr.rmoText)
            }.let { itC ->
                fileName.setTextColor(itC)
                extend.setTextColor(itC)
            }
            fileName.text = it.folderName
            extend.text = it.mediaHolder.size.toStr
            folderRecyclerAll.apply {
                MediaAdapter(
                    it.mediaHolder,
                    picListener,
                    widthColumn,
                    increase,
                    col,
                    //this@DuoAdapter
                ) { hideStart, hideEnd ->
                    if (hideStart)
                        startGrd.justGone()
                    else
                        startGrd.justVisible()

                    if (hideEnd)
                        endGrd.justGone()
                    else
                        endGrd.justVisible()
                }.also {
                    adapter = it
                }
                scheduleLayoutAnimation()
            }
        }

        override fun com.pt.pro.gallery.fasten.RowAllFasten.clear() {
            folderRecyclerAll.adapter = null
        }

        override fun com.pt.common.objects.MediaDuo.onClick(i: Int) {
            picListener?.apply {
                this@onClick.folderName?.onFolderClicked(this@onClick.path.toStr, false)
            }
        }

    }

}
