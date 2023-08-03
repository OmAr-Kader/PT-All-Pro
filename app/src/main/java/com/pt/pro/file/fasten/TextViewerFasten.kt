package com.pt.pro.file.fasten

data class TextViewerFasten(
    val root_: com.pt.common.moderator.over.FrameSizer,
    val head: android.widget.FrameLayout,
    val headInner: androidx.constraintlayout.widget.ConstraintLayout,
    val mainBack: com.pt.common.moderator.over.ResizeImageView,
    val picName: androidx.appcompat.widget.AppCompatTextView,
    val searchName: com.pt.common.moderator.over.EditTextBackEvent,
    val downText: com.pt.common.moderator.over.ResizeImageView,
    val upText: com.pt.common.moderator.over.ResizeImageView,
    val searchText: com.pt.common.moderator.over.ResizeImageView,
    val frameRec: androidx.constraintlayout.widget.ConstraintLayout,
    val scroll: androidx.core.widget.NestedScrollView,
    val textViewer: androidx.appcompat.widget.AppCompatEditText,
    val headDown: android.widget.FrameLayout,
    val headOptions: androidx.constraintlayout.widget.ConstraintLayout,
    val saveChanges: com.pt.common.moderator.over.ResizeImageView,
    val zoomOut: com.pt.common.moderator.over.ResizeImageView,
    val zoomIn: com.pt.common.moderator.over.ResizeImageView,
    val rotateScreen: com.pt.common.moderator.over.ResizeImageView,
    val frameForScroll: android.widget.FrameLayout,
    val pointerScroll: androidx.cardview.widget.CardView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): com.pt.common.moderator.over.FrameSizer = root_
}