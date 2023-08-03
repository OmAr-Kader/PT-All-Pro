package com.pt.pro.gallery.adapters

import androidx.recyclerview.widget.RecyclerView
import com.pt.common.global.*
import com.pt.pro.gallery.interfaces.GalleryListener
import com.pt.pro.gallery.interfaces.MediaListener
import com.pt.common.mutual.adapter.GlobalAdapterLong
import com.pt.common.stable.*
import com.pt.pro.gallery.fasten.TimeFasten

class MediaAdapter(
    override val mediaList: MutableList<MediaSack>,
    override var picListener: GalleryListener?,
    override val widthX: Int,
    override val increase: Int,
    override val numOfCol: Int,
    override var positionListener: ((forStart: Boolean, forEnd: Boolean) -> Unit)?,
) : RecyclerView.Adapter<MediaAdapter.HolderMedia>(), MediaListener, () -> Unit {

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): HolderMedia = HolderMedia(
        com.pt.pro.gallery.fasten.GalleryInflater.run {
            parent.context.inflaterTime(widthX)
        }
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

    override fun onViewRecycled(holder: HolderMedia) {
        super.onViewRecycled(holder)
        holder.finalClear()
    }


    override fun invoke() {
        mediaList.clear()
        picListener = null
        positionListener = null
    }

    @com.pt.common.global.UiAnn
    inner class HolderMedia(
        item: TimeFasten,
    ) : GlobalAdapterLong<TimeFasten, MediaSack>(item) {

        override val Int.item: MediaSack
            get() = mediaList.getI(this)

        override fun TimeFasten.bind() {
            picture.apply {
                setOnClickListener(this@HolderMedia)
                setOnLongClickListener(this@HolderMedia)
            }
        }

        override fun TimeFasten.attach(it: MediaSack, i: Int) {
            if (it.isImage) videoPlay.justGone()
            else videoPlay.justVisible()
            it.pathMedia.toStr.also { str ->
                picture.apply {
                    tag = str
                    loadPicture(image = it, increase = increase, str = str)
                }
            }
            positionListener?.apply {
                if (mediaList.size == numOfCol) {
                    invoke(true, true)
                } else {
                    when (i) {
                        mediaList.size - 1 -> invoke(false, true)
                        mediaList.size - numOfCol -> invoke(false, false)
                        numOfCol + 3 -> invoke(false, false)
                        0 -> invoke(true, false)
                    }
                }
            }
        }

        override fun TimeFasten.clear() {}

        override fun finalClear() {}

        override fun android.view.View.onClick(it: MediaSack) {
            picListener?.apply {
                mediaList.onPicClicked(posA) {
                    binder.root_.startAnimation(scaleDownAnimation)
                }
            }
        }

        override fun android.view.View.onLongClick(it: MediaSack): Boolean {
            picListener?.apply {
                it.addMediaPending()
            }
            return true
        }
    }

}
