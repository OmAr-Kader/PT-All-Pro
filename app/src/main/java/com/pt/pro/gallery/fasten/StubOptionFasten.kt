package com.pt.pro.gallery.fasten

data class StubOptionFasten(
    val root_: com.pt.common.moderator.over.FrameTint,
    val backDisplayMenu: com.pt.common.moderator.over.ResizeImageView,
    val menuNumber: androidx.appcompat.widget.AppCompatTextView,
    val share: com.pt.common.moderator.over.ResizeImageView,
    val deleteDate: com.pt.common.moderator.over.ResizeImageView,
    val pagerMenu: com.pt.common.moderator.over.ResizeImageView,
    val clipDate: androidx.appcompat.widget.AppCompatTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}