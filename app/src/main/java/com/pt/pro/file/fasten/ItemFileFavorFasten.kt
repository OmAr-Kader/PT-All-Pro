package com.pt.pro.file.fasten

data class ItemFileFavorFasten(
    val root_: androidx.appcompat.widget.LinearLayoutCompat,
    val folderImageCard: androidx.cardview.widget.CardView,
    val folderImage: com.pt.common.moderator.over.GlideImageView,
    val fileName: com.pt.common.moderator.over.ScalelessTextView,
    val fileType: com.pt.common.moderator.over.ScalelessTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.appcompat.widget.LinearLayoutCompat = root_
}