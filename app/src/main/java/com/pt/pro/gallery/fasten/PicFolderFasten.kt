package com.pt.pro.gallery.fasten

data class PicFolderFasten(
    val root_: android.widget.FrameLayout,
    val folderFrame: androidx.cardview.widget.CardView,
    val folderPic: com.pt.common.moderator.over.GlideImageView,
    val folderName: com.pt.common.moderator.over.ScalelessTextView
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}