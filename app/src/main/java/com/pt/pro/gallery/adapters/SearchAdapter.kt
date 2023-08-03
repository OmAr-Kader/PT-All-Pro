package com.pt.pro.gallery.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pt.common.global.*
import com.pt.common.mutual.adapter.GlobalAdapterLong
import com.pt.common.stable.getI
import com.pt.common.stable.withMain
import com.pt.pro.gallery.fasten.GalleryInflater
import com.pt.pro.gallery.fasten.PicItemFasten
import com.pt.pro.gallery.interfaces.GalleryListener
import com.pt.pro.gallery.interfaces.SearchAdapterListener

class SearchAdapter(
    override val mediaList: MutableList<MediaSack>,
    override var picListener: GalleryListener?,
    override val widthX: Int,
    override val increase: Int,
) : RecyclerView.Adapter<SearchAdapter.HolderMedia>(), SearchAdapterListener {

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HolderMedia = HolderMedia(
        GalleryInflater.run { parent.context.inflaterPicItem(widthX) }
    )

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holderMedia: HolderMedia, position: Int) {
        holderMedia.bind()
    }

    override fun getItemCount(): Int = if (mediaList.isEmpty()) 0 else mediaList.size

    @com.pt.common.global.UiAnn
    override fun onViewAttachedToWindow(holderMedia: HolderMedia) {
        super.onViewAttachedToWindow(holderMedia)
        holderMedia.attach()
    }

    @com.pt.common.global.UiAnn
    override fun onViewDetachedFromWindow(holderMedia: HolderMedia) {
        super.onViewDetachedFromWindow(holderMedia)
        holderMedia.clear()
    }

    override suspend fun MutableList<MediaSack>.updateMedia() {
        val siz = mediaList.size
        clearer()
        justRemove(siz)
        adder()
        justAdd(size)
    }

    @com.pt.common.global.MainAnn
    private suspend fun clearer() {
        withMain {
            mediaList.clear()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun MutableList<MediaSack>.adder() {
        withMain {
            mediaList.addAll(this@adder)
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
        mediaList.clear()
        picListener = null
    }

    @com.pt.common.global.UiAnn
    inner class HolderMedia(
        item: PicItemFasten,
    ) : GlobalAdapterLong<PicItemFasten, MediaSack>(item) {

        override val Int.item: MediaSack
            get() = mediaList.getI(this)

        override fun PicItemFasten.bind() {
            root_.layoutParams = RecyclerView.LayoutParams(widthX, widthX)
            picture.apply {
                setOnClickListener(this@HolderMedia)
                setOnLongClickListener(this@HolderMedia)
            }
        }

        override fun PicItemFasten.attach(it: MediaSack, i: Int) {
            if (it.isImage)
                videoPlay.justGone()
            else
                videoPlay.justVisible()

            it.pathMedia.toStr.also { str ->
                picture.apply {
                    tag = str
                    loadPicture(image = it, increase = increase, str = str)
                }
            }
        }

        override fun PicItemFasten.clear() {}

        override fun View.onClick(it: MediaSack) {
            picListener?.apply {
                mediaList.onPicClicked(posA) {
                    kotlin.runCatching {
                        binder.root_.startAnimation(scaleDownAnimation)
                    }
                }
            }
        }

        override fun View.onLongClick(it: MediaSack): Boolean {
            picListener?.apply {
                it.addMediaPending()
            }
            return true
        }
    }

}
