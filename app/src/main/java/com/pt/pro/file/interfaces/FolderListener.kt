package com.pt.pro.file.interfaces

import com.pt.pro.file.fasten.FolderFasten

interface FolderListener : kotlinx.coroutines.CoroutineScope {

    val fileList: MutableList<com.pt.common.global.FileSack>
    var picListener: ItemFileListener?
    var isActionMode: Boolean
    val deleteCopy: MutableList<com.pt.common.global.FileSack>
    var selectAll: Boolean
    val selectedViews: Sequence<android.widget.FrameLayout>
    var adPosition: Int
    var deleteActive: Boolean
    val allSvg: androidx.collection.ArrayMap<Int, android.graphics.drawable.Drawable>
    suspend fun MutableList<com.pt.common.global.FileSack>.updateSearch()
    suspend fun selectAll()
    suspend fun justNotify()
    suspend fun forDelete()
    suspend fun android.graphics.drawable.Drawable?.refreshAdapter()
    fun onAdapterDestroy()
    suspend fun FolderFasten.displayAsyncType(str: String, it: String)

    fun FolderFasten.loadAsyncType(
        media: com.pt.common.global.FileSack,
        str: String,
        imgJob: kotlinx.coroutines.Job?,
        typeJob: kotlinx.coroutines.Job?.() -> Unit
    )

    fun FolderFasten.loadAsyncTxtNumber(
        media: com.pt.common.global.FileSack,
        str: String,
        imgJob: kotlinx.coroutines.Job?,
        numberJob: kotlinx.coroutines.Job?.() -> Unit
    )

    fun com.pt.common.moderator.over.GlideImageView.loadImgAsync(
        media: com.pt.common.global.FileSack,
        str: String,
        imgJob: kotlinx.coroutines.Job?.() -> Unit
    )

    suspend fun FolderFasten.displayLoadAsyncType(
        str: String,
        it: String
    )

    suspend fun FolderFasten.displayLoadAsyncTxtNumber(
        str: String,
        it: String
    )

    suspend fun android.content.ContentResolver.getFolderSize(
        it: com.pt.common.global.FileSack,
        megaBText: String,
        gigaBText: String,
        s: (String) -> Unit
    )
}