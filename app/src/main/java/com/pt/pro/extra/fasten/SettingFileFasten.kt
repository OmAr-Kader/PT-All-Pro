package com.pt.pro.extra.fasten

data class SettingFileFasten(
    val root_: android.widget.FrameLayout,
    val frameSortByText: android.widget.FrameLayout,
    val sortByImage: androidx.appcompat.widget.AppCompatImageView,
    val mainFrameSortBy: android.widget.FrameLayout,
    val frameSortBy: android.widget.FrameLayout,
    val frameSortByFolderText: android.widget.FrameLayout,
    val sortByFolderImage: androidx.appcompat.widget.AppCompatImageView,
    val mainFrameSortByFolder: android.widget.FrameLayout,
    val frameSortByFolder: android.widget.FrameLayout,
    val usePicture: androidx.appcompat.widget.AppCompatCheckBox,
    val useVideo: androidx.appcompat.widget.AppCompatCheckBox,
    val useMusic: androidx.appcompat.widget.AppCompatCheckBox,
    val usePdf: androidx.appcompat.widget.AppCompatCheckBox,
    val useTxt: androidx.appcompat.widget.AppCompatCheckBox,
    val useZip: androidx.appcompat.widget.AppCompatCheckBox,
    val screenTxt: androidx.appcompat.widget.AppCompatCheckBox,
    val screenPdf: androidx.appcompat.widget.AppCompatCheckBox,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}