package com.pt.pro.file.interfaces

interface PDFViewerListener {

    var pdfPath: String?
    var pageNumber: Int
    var browserFileListener: SliderListener?
    var hideSys: Boolean

    fun doLockImg()
    fun doUnLockImg()
    fun hideOrShow()
}