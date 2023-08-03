package com.pt.pro.notepad.interfaces

import com.pt.pro.notepad.models.DataKeeperItem

interface AdapterUpdate {
    val dataItems: MutableList<DataKeeperItem>
    val dataItemsOri: MutableList<DataKeeperItem>
    val widthData: Int
    val colorUnselected: Int
    val colorSelect: Int
    var dataClickListener: DataClickListener?

    var img: Boolean
    var rec: Boolean

    val temp: MutableList<DataKeeperItem>
    val images: MutableList<String>
    val records: MutableList<String>
    var actionModeBoolean: Boolean
    var copyPos: Int
    var playRecord: Boolean

    var lastOpt: Int
    fun MutableList<DataKeeperItem>.optionPre()

    fun DataKeeperItem?.addItem() {}

    val Int.findKeeperColor: Int
    suspend fun MutableList<DataKeeperItem>.updateDataAdapter() {}
    suspend fun MutableList<DataKeeperItem>.updateDataAdapterSearch() {}
    fun stopPlayer() {}
    fun onAdapterDestroy()
    suspend fun justNotify()
    fun com.pt.pro.databinding.RowAudioBinding.updatePlayingView(isPlaying: Boolean)
    fun com.pt.pro.databinding.RowAudioBinding.performPlayButtonClick(
        dataKeeperItem: DataKeeperItem,
        posA: Int
    )

    fun android.widget.Chronometer.onProgressChanged(progress: Int, position: Int)
    fun com.pt.pro.databinding.RowAudioBinding.releaseMediaPlayer()
    suspend fun refreshAdapter()
    fun DataKeeperItem.updateWebItem()
}