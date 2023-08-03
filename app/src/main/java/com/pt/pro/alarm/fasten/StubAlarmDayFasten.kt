package com.pt.pro.alarm.fasten

data class StubAlarmDayFasten(
    val root_: android.widget.FrameLayout,
    val days: androidx.constraintlayout.widget.ConstraintLayout,
    val mMon: com.pt.common.moderator.over.ScalelessTextView,
    val mTues: com.pt.common.moderator.over.ScalelessTextView,
    val mWed: com.pt.common.moderator.over.ScalelessTextView,
    val mThurs: com.pt.common.moderator.over.ScalelessTextView,
    val mFri: com.pt.common.moderator.over.ScalelessTextView,
    val mSat: com.pt.common.moderator.over.ScalelessTextView,
    val mSun: com.pt.common.moderator.over.ScalelessTextView,
) : androidx.viewbinding.ViewBinding {

    override fun getRoot(): android.widget.FrameLayout = root_
}