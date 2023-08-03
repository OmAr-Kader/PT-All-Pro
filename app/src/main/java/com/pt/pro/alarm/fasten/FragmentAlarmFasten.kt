package com.pt.pro.alarm.fasten

data class FragmentAlarmFasten(
    val root_: android.widget.FrameLayout,
    val alarmCard: androidx.appcompat.widget.LinearLayoutCompat,
    val mainBack: com.pt.common.moderator.over.ResizeImageView,
    val titleAlarm: androidx.appcompat.widget.AppCompatTextView,
    val alarmRecFrame: android.widget.FrameLayout,
    val recyclerAlarms: com.pt.common.moderator.recycler.RecyclerForViews,
    val addAlarmButton: androidx.cardview.widget.CardView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}