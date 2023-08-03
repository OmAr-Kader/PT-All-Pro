package com.pt.pro.gallery.interfaces

interface ImageListener {

    var mediaHolder: com.pt.common.global.MediaSack?
    var browserListener: BrowserListener?
    var hideSys: Boolean

    fun doLockImg()
    fun doUnLockImg()
    fun hideOrShow()
}