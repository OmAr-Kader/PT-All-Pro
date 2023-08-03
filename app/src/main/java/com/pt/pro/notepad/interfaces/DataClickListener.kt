package com.pt.pro.notepad.interfaces

interface DataClickListener {

    fun onLink(clip: Boolean, text: String?)
    fun removeItem(position: Int, delete: Boolean)
    suspend fun saveImg(filePath: String?)
    fun onCalendarClicked(start: Long, end: Long, dayOfMonth: Int)

    fun checkIfHaveFra()
    fun onImageClick(uri: String)
    fun onActionActions(opt: Int)
    fun com.pt.pro.notepad.models.DataKeeperItem.saveEmail()

    fun duringSwipe(option: String, show: Boolean)
    fun newUserLauncher()
    fun com.pt.pro.notepad.models.DataKeeperItem.pushLink(txt: String)

    fun com.pt.common.global.DSack<String?, String?, String?>.sendEmail()
}
