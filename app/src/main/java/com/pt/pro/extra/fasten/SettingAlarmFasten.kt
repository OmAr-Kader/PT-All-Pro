package com.pt.pro.extra.fasten

data class SettingAlarmFasten(
    val root_: android.widget.FrameLayout,
    val frameIntervalText: android.widget.FrameLayout,
    val intervalImage: androidx.appcompat.widget.AppCompatImageView,
    val mainFrameInterval: android.widget.FrameLayout,
    val frameInterval: android.widget.FrameLayout,
    val frameSnoozeText: android.widget.FrameLayout,
    val snoozeImage: androidx.appcompat.widget.AppCompatImageView,
    val mainFrameSnooze: android.widget.FrameLayout,
    val frameSnooze: android.widget.FrameLayout,
    val frameRepeatText: android.widget.FrameLayout,
    val repeatImage: androidx.appcompat.widget.AppCompatImageView,
    val mainFrameRepeat: android.widget.FrameLayout,
    val frameRepeat: android.widget.FrameLayout,
    val frameVolumeText: android.widget.FrameLayout,
    val volumeImage: androidx.appcompat.widget.AppCompatImageView,
    val mainFrameVolume: android.widget.FrameLayout,
    val frameVolume: android.widget.FrameLayout,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}