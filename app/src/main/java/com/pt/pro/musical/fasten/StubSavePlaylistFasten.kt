package com.pt.pro.musical.fasten

data class StubSavePlaylistFasten(
    val root_: android.widget.FrameLayout,
    val editCon: android.widget.FrameLayout,
    val playlistEdit: com.pt.common.moderator.over.EditTextBackEvent,
    val saveEdit: androidx.appcompat.widget.AppCompatButton,
    val cancelEdit: androidx.appcompat.widget.AppCompatButton,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}