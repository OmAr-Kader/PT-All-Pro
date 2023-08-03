package com.pt.pro.extra.fasten

data class SettingMusicFasten(
    val root_: android.widget.FrameLayout,
    val switchMusicScreen: androidx.appcompat.widget.AppCompatCheckBox,
    val showVoice: androidx.appcompat.widget.AppCompatCheckBox,
    val seekTitle: androidx.appcompat.widget.AppCompatTextView,
    val scaleNum: com.pt.common.moderator.over.ScalelessTextView,
    val pauseSeek: androidx.appcompat.widget.AppCompatSeekBar,
    val maxNum: com.pt.common.moderator.over.ScalelessTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}