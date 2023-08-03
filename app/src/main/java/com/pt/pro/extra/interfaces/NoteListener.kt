package com.pt.pro.extra.interfaces

interface NoteListener {

    val windowManage: android.view.WindowManager
    val headRestBall: com.pt.pro.databinding.NoteHeadBinding
    val headMine: com.pt.pro.databinding.FloatingHeadBinding

    fun com.pt.pro.databinding.FloatingHeadBinding.setViewRoot()
    fun com.pt.pro.databinding.NoteHeadBinding.setLayout2()

    fun com.pt.pro.databinding.NoteHeadBinding.changeVisibility()
    fun animationOpenFloat(beginValue: Int, endValue: Int)
}