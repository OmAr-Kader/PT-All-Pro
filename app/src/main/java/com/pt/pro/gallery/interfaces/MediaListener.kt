package com.pt.pro.gallery.interfaces

interface MediaListener {
    val mediaList: MutableList<com.pt.common.global.MediaSack>
    var picListener: GalleryListener?
    val widthX: Int
    val increase: Int
    val numOfCol: Int
    var positionListener: ((forStart: Boolean, forEnd: Boolean) -> Unit)?
}