package com.pt.pro.gallery.interfaces

interface GalleryCommonInterface {
    var itemListener: GalleryListener?
    fun setISHidden(boolean: Boolean, galleryM: Int)
    fun doInitAllLoad(galleryM: Int)
    fun scannerHidden(galleryM: Int)
    fun toRefresh()
    fun onSearch(searchTxt: CharSequence?) {}
}