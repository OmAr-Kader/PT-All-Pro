package com.pt.pro.alarm.fasten

data class StubTimePickFasten(
    val root_: android.widget.FrameLayout,
    val timeAlarm: android.widget.TimePicker,
    val pickerCircle: com.sztorm.timepicker.TimePicker,
    val switchAlarm: androidx.appcompat.widget.SwitchCompat,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}
