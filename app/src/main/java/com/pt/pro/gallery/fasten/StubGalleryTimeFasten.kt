package com.pt.pro.gallery.fasten

data class StubGalleryTimeFasten(
    val root_: com.pt.common.moderator.over.FrameSizer,
    val recGallery: com.pt.common.moderator.recycler.RecyclerForViews,
    val galleryBarCard: androidx.cardview.widget.CardView,
    val pointerScroll: androidx.cardview.widget.CardView
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): com.pt.common.moderator.over.FrameSizer = root_
}