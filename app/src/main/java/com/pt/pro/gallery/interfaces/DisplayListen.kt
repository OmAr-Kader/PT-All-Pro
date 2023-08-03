package com.pt.pro.gallery.interfaces

import com.pt.common.global.MediaSack
import com.pt.pro.gallery.adapters.PictureAdapter

interface DisplayListen {
    fun MutableList<MediaSack>.onPicClicked(position: Int, p: () -> Unit)
    fun justDisplayAction(text: String)
    fun onActionDisplay(text: String, siz: Int)
    suspend fun PictureAdapter.offMenuDisplay() {}
    suspend fun MutableList<MediaSack>.checkAfterEdit(posDesc: MutableList<Int>)
    fun toRefresh()
    suspend fun MutableList<MediaSack>.deleteMedia(posDesc: MutableList<Int>)

}