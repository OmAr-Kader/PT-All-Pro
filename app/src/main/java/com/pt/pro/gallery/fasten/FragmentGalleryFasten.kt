package com.pt.pro.gallery.fasten

data class FragmentGalleryFasten(
    val root_: com.pt.common.moderator.over.FrameSizer,
    val frameGalleryMain: android.widget.FrameLayout,
    val galleryCard: androidx.constraintlayout.widget.ConstraintLayout,
    val mainBack: com.pt.common.moderator.over.ResizeImageView,
    val picName: androidx.appcompat.widget.AppCompatTextView,
    val searchEdit: androidx.appcompat.widget.AppCompatEditText,
    val reloadVideos: com.pt.common.moderator.over.ResizeImageView,
    val reloadImages: com.pt.common.moderator.over.ResizeImageView,
    val reloadMain: com.pt.common.moderator.over.ResizeImageView,
    val extendGallery: com.pt.common.moderator.over.ResizeImageView,
    val frameAllGallery: androidx.constraintlayout.widget.ConstraintLayout,
    val galleryModeFrame: android.widget.FrameLayout,
    val frameForSearch: android.widget.FrameLayout,
    val forSnakes: android.widget.FrameLayout,
    val storyButton: androidx.cardview.widget.CardView,
    val extendOption: androidx.cardview.widget.CardView,
    val camera: androidx.appcompat.widget.AppCompatImageView,
    val hidden: androidx.appcompat.widget.AppCompatImageView,
    val pendingFrame: android.widget.FrameLayout,
    val pendingText: androidx.appcompat.widget.AppCompatTextView,
    val reSort: androidx.appcompat.widget.AppCompatImageView,
    val searchIcon: androidx.appcompat.widget.AppCompatImageView,
    val empty: androidx.appcompat.widget.AppCompatTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): com.pt.common.moderator.over.FrameSizer = root_
}