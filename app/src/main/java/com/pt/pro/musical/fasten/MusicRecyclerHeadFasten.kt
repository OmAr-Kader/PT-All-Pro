package com.pt.pro.musical.fasten

data class MusicRecyclerHeadFasten(
    val root_: androidx.cardview.widget.CardView,
    val cardFrame: androidx.cardview.widget.CardView,
    val layoutDetails: android.widget.FrameLayout,
    val returnMusic: com.pt.common.moderator.over.ResizeImageView,
    val searchMusic: com.pt.common.moderator.over.EditTextBackEvent,
    val deletePlaylist: com.pt.common.moderator.over.ResizeImageView,
    val savePlaylist: com.pt.common.moderator.over.ResizeImageView,
    val searchButton: com.pt.common.moderator.over.ResizeImageView,
    val extendButton: com.pt.common.moderator.over.ResizeImageView,
    val myPlaylist: com.pt.common.moderator.over.ResizeImageView,
    val showArtists: com.pt.common.moderator.over.ResizeImageView,
    val showAlbums: com.pt.common.moderator.over.ResizeImageView,
    val showAllMusic: com.pt.common.moderator.over.ResizeImageView,
    val showAllList: com.pt.common.moderator.over.ResizeImageView,
    val recyclerSongs: com.pt.common.moderator.recycler.RecyclerForViews,
    val stubPlaylistFrame: android.widget.FrameLayout,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): androidx.cardview.widget.CardView = root_
}