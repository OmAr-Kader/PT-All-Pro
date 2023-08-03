package com.pt.pro.file.interfaces

interface SliderListener {

    var pdfFragment: com.pt.pro.file.views.pdf.ViewPdfFragment?

    fun com.pt.common.global.DSack<Int, Int, Boolean>.setIntiMargin()
    fun String.setFileUriAndPath(a: (String?, Int) -> Unit)

    suspend fun onShowCardView()
    suspend fun onHideCardView()
    fun funStateChanged()
    fun funPageSelected(position: Int)

}