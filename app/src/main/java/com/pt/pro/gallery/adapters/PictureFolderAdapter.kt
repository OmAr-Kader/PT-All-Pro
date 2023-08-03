package com.pt.pro.gallery.adapters

import com.pt.common.global.toStr
import com.pt.common.stable.getI
import com.pt.pro.gallery.fasten.PicFolderFasten

class PictureFolderAdapter(
    override val folderMedia: MutableList<com.pt.common.global.MediaFolderSack>,
    override var listenToClick: com.pt.pro.gallery.interfaces.GalleryListener?,
    override val folderWidth: Int,
    override val folderHeight: Int,
    override val color: Int,
) : androidx.recyclerview.widget.RecyclerView.Adapter<PictureFolderAdapter.FolderHolder>(),
    com.pt.pro.gallery.interfaces.PictureFolderListener {

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): FolderHolder = FolderHolder(
        com.pt.pro.gallery.fasten.GalleryInflater.run {
            parent.context.inflaterPicFolder(folderWidth, folderHeight)
        }
    )

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = folderMedia.size

    @com.pt.common.global.UiAnn
    override fun onViewAttachedToWindow(holder: FolderHolder) {
        super.onViewAttachedToWindow(holder)
        holder.attach()
    }

    override suspend fun MutableList<com.pt.common.global.MediaFolderSack>.updateFolder() {
        val siz = folderMedia.size
        clearer()
        justRemove(siz)
        adder()
        justAdd(size)
    }

    @com.pt.common.global.MainAnn
    private suspend fun clearer() {
        com.pt.common.stable.withMain {
            folderMedia.clear()
        }
    }

    @com.pt.common.global.MainAnn
    private suspend fun MutableList<com.pt.common.global.MediaFolderSack>.adder() {
        com.pt.common.stable.withMain {
            folderMedia.addAll(this@adder)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justRemove(siz: Int) {
        com.pt.common.stable.withMain {
            notifyItemRangeRemoved(0, siz)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justAdd(siz: Int) {
        com.pt.common.stable.withMain {
            notifyItemRangeInserted(0, siz)
        }
    }

    @com.pt.common.global.UiAnn
    override fun onViewDetachedFromWindow(holder: FolderHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.clear()
    }

    override fun onAdapterDestroy() {
        folderMedia.clear()
        listenToClick = null
    }

    override fun onViewRecycled(holder: FolderHolder) {
        super.onViewRecycled(holder)
        holder.finalClear()
    }

    @com.pt.common.global.UiAnn
    inner class FolderHolder(
        item: PicFolderFasten,
    ) : com.pt.common.mutual.adapter.GlobalAdapter<PicFolderFasten, com.pt.common.global.MediaFolderSack>(
        item
    ) {

        override val Int.item: com.pt.common.global.MediaFolderSack
            get() = folderMedia.getI(this)

        override fun PicFolderFasten.bind() {
            folderPic.setOnClickListener(this@FolderHolder)
        }

        @android.annotation.SuppressLint("SetTextI18n")
        override fun PicFolderFasten.attach(it: com.pt.common.global.MediaFolderSack) {
            if (it.folderName == com.pt.common.BuildConfig.FAVORITE) folderName.setTextColor(color)
            it.firstImagePath.toStr.let { str ->
                folderPic.tag = str
                folderPic.loadFolder(str)
            }
            folderName.text = (it.folderName.toStr + "\n" + it.numberOfPics)
        }

        override fun PicFolderFasten.clear() {}

        override fun finalClear() {}

        override fun com.pt.common.global.MediaFolderSack.onClick(i: Int) {
            listenToClick?.apply {
                this@onClick.folderName?.onFolderClicked(this@onClick.path.toStr, true)
            }
        }
    }

}