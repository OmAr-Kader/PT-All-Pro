package com.pt.pro.file.fasten

data class FragmentFileFasten(
    val root_: com.pt.common.moderator.over.FrameSizer,
    val subFileFrame: android.widget.FrameLayout,
    val galleryCard: androidx.constraintlayout.widget.ConstraintLayout,
    val mainBack: com.pt.common.moderator.over.ResizeImageView,
    val picName: androidx.appcompat.widget.AppCompatTextView,
    val searchEdit: androidx.appcompat.widget.AppCompatEditText,
    val clipboardFrame: android.widget.FrameLayout,
    val clipboardImage: com.pt.common.moderator.over.ResizeImageView,
    val clipboardText: androidx.appcompat.widget.AppCompatTextView,
    val swipeMode: com.pt.common.moderator.over.ResizeImageView,
    val extendFile: com.pt.common.moderator.over.ResizeImageView,
    val fileFrame: android.widget.FrameLayout,
    val scrollFile: android.widget.HorizontalScrollView,
    val filesLinear: androidx.appcompat.widget.LinearLayoutCompat,
    val codFav: android.widget.FrameLayout,
    val recyclerFavorites: com.pt.common.moderator.recycler.RecyclerForViews,
    val subMangerFrame: android.widget.FrameLayout,
    val recyclerFiles: com.pt.common.moderator.recycler.RecyclerForViews,
    val forSnakes: android.widget.FrameLayout,
    val pointerScroll: androidx.cardview.widget.CardView,
    val includeShareSub: android.widget.FrameLayout,
    val includeOptionsStub: android.widget.FrameLayout,
    val hiddenTittleLinear: androidx.appcompat.widget.LinearLayoutCompat,
    val hiddenTittleClick: androidx.appcompat.widget.AppCompatTextView,
    val fileBarCard: androidx.cardview.widget.CardView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): com.pt.common.moderator.over.FrameSizer = root_
}