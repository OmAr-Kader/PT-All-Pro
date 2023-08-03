package com.pt.pro.gallery.fasten

data class RowAllFasten(
    val root_: android.widget.FrameLayout,
    val extendFile: android.widget.FrameLayout,
    val fileName: androidx.appcompat.widget.AppCompatTextView,
    val extend: androidx.appcompat.widget.AppCompatTextView,
    val folderCardAll: androidx.cardview.widget.CardView,
    val folderRecyclerAll: com.pt.common.moderator.recycler.RecyclerForViews,
    val startGrd: androidx.appcompat.widget.AppCompatImageView,
    val endGrd: androidx.appcompat.widget.AppCompatImageView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}