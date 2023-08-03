package com.pt.pro.file.fasten

data class FolderFasten(
    val root_: android.widget.FrameLayout,
    val rowFile: android.widget.FrameLayout,
    val folderImage: com.pt.common.moderator.over.GlideImageView,
    val fileName: androidx.appcompat.widget.AppCompatTextView,
    val fileNumber: androidx.appcompat.widget.AppCompatTextView,
    val fileType: androidx.appcompat.widget.AppCompatTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}