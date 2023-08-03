package com.pt.pro.gallery.interfaces

interface BrowserListener {
    val allImages: MutableList<com.pt.common.global.MediaSack>
    var imageFragment: com.pt.pro.gallery.fragments.FragmentImage?
    var vidFragment: com.pt.pro.gallery.fragments.FragmentVideo?
    val runSeek: (Long) -> Unit
    val setMax: (Int) -> Unit
    val android.widget.SeekBar.OnSeekBarChangeListener.seekListenerRun: () -> Unit
    fun com.pt.pro.gallery.objects.PagerHolder.init()
    suspend fun onShowCardView() {}
    suspend fun onHideCardView()
    fun funStateChanged()
    fun funPageSelected(position: Int)
    suspend fun viewPagerSkip()
    fun com.pt.common.global.MediaSack.doRename(
        fromPath: String?,
        toPath: String?,
        newName: String?,
    )
}