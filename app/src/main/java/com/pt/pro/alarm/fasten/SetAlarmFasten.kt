package com.pt.pro.alarm.fasten

data class SetAlarmFasten(
    val root_: android.widget.FrameLayout,
    val setAlarmCard: androidx.constraintlayout.widget.ConstraintLayout,
    val mainBack: com.pt.common.moderator.over.ResizeImageView,
    val alarmMode: androidx.appcompat.widget.AppCompatTextView,
    val modeDone: androidx.appcompat.widget.AppCompatTextView,
    val alarmEdit: android.widget.FrameLayout,
    val subMainAlarm: androidx.appcompat.widget.LinearLayoutCompat,
    val pickTime: android.widget.FrameLayout,
    val ringtonePickerButton: androidx.appcompat.widget.AppCompatButton,
    val buttonBackgroundPicker: androidx.appcompat.widget.AppCompatButton,
    val dismissWay: androidx.appcompat.widget.AppCompatButton,
    val repeatingCheck: androidx.appcompat.widget.AppCompatCheckBox,
    val vibrateCheck: androidx.appcompat.widget.AppCompatCheckBox,
    val daysFrame: android.widget.FrameLayout,
    val textLayout: android.widget.FrameLayout,
    val textLabel: androidx.appcompat.widget.AppCompatEditText,
    val sendButtonAlarm: com.pt.pro.notepad.recorder.RecordButton,
    val playAlarmRecord: androidx.appcompat.widget.AppCompatImageView,
    val setFrame: android.widget.FrameLayout,
    val setAlarmButton: androidx.appcompat.widget.AppCompatButton,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}