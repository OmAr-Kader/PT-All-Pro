package com.pt.pro.notepad.interfaces

interface DataCounterListener {
    fun com.pt.pro.notepad.models.TablesModelMonth.onPicClickedData(
        tableIndexNumber: Int,
        reload: Boolean,
    ) {
    }

    fun onPicFirstData(clickedUser: String?, ownerName: String?) {}
    fun longCLick(idUser: String, IsBachUp: Boolean, justMonth: String?) {}
}