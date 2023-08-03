package com.pt.pro.file.interfaces

import com.pt.pro.file.fasten.ItemFileFavorFasten

interface FavoriteListener : kotlinx.coroutines.CoroutineScope {

    val favList: MutableList<com.pt.common.global.FileSack>
    var isCategory: Boolean
    var picListener: ItemFileListener?
    val allSvg: androidx.collection.ArrayMap<Int, android.graphics.drawable.Drawable>
    fun ItemFileFavorFasten.loadImgFilterAsync(
        it: com.pt.common.global.FileSack,
        str: String,
    )

    fun ItemFileFavorFasten.loadAsyncTxt(type: Int, str: String)
    suspend fun MutableList<com.pt.common.global.FileSack>.updateSearch(isCato: Boolean)
    suspend fun ItemFileFavorFasten.displayLoadAsyncTxt(
        str: String,
        it: String
    )

    fun onAdapterDestroy()

    fun ItemFileFavorFasten.loadImgNormalAsync(
        media: com.pt.common.global.FileSack,
        str: String
    )

    suspend fun ItemFileFavorFasten.doImgNormalAsync(
        media: com.pt.common.global.FileSack,
        str: String
    )

    suspend fun com.pt.common.moderator.over.GlideImageView.displayImgNormalAsync(
        media: com.pt.common.global.FileSack,
        str: String
    )

}