package com.pt.pro.extra.fasten

data class SettingGalleryFasten(
    val root_: android.widget.FrameLayout,
    val frameSortByText: android.widget.FrameLayout,
    val sortByImage: androidx.appcompat.widget.AppCompatImageView,
    val mainFrameSortBy: android.widget.FrameLayout,
    val frameSortBy: android.widget.FrameLayout,
    val frameSortByFolderText: android.widget.FrameLayout,
    val sortByFolderImage: androidx.appcompat.widget.AppCompatImageView,
    val mainFrameSortByFolder: android.widget.FrameLayout,
    val frameSortByFolder: android.widget.FrameLayout,
    val frameColumnText: android.widget.FrameLayout,
    val columnImage: androidx.appcompat.widget.AppCompatImageView,
    val mainFrameColumn: android.widget.FrameLayout,
    val frameColumn: android.widget.FrameLayout,
    val switchResolution: androidx.appcompat.widget.AppCompatCheckBox,
    val galleryScreen: androidx.appcompat.widget.AppCompatCheckBox,
    val galleryStory: androidx.appcompat.widget.AppCompatCheckBox,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}