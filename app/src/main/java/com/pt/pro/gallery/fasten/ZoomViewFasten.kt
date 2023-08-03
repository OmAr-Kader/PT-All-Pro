package com.pt.pro.gallery.fasten

data class ZoomViewFasten(
    val root_: android.widget.FrameLayout,
    val zoomView: com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}