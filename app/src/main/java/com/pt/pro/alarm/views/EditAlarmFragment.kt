package com.pt.pro.alarm.views

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.pt.common.global.AlarmSack
import com.pt.common.global.findAttr
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.alarm.interfaces.AlarmDismiss
import com.pt.pro.alarm.interfaces.EditAlarmListener
import com.pt.pro.alarm.interfaces.EditFragmentListener
import com.pt.pro.alarm.objects.AlarmHelper
import com.pt.pro.databinding.FragmentEditAlarmBinding

class EditAlarmFragment : GlobalSimpleFragment<FragmentEditAlarmBinding>(), AlarmDismiss,
    EditFragmentListener {

    override var ala: AlarmSack? = null
    override var editAlarmListener: EditAlarmListener? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            FragmentEditAlarmBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }

    @com.pt.common.global.UiAnn
    override fun FragmentEditAlarmBinding.onViewCreated() {
        launchMain {
            justCoroutineMain {
                singleSwipe.setOnClickListener(this@EditAlarmFragment)
                singleClick.setOnClickListener(this@EditAlarmFragment)
                shakeDevice.setOnClickListener(this@EditAlarmFragment)
                doubleSwipe.setOnClickListener(this@EditAlarmFragment)
                when (ala?.dismissWay) {
                    AlarmHelper.SINGLE_SWIPE -> {
                        singleSwipe.setTextColor(them.findAttr(android.R.attr.colorAccent))
                    }
                    AlarmHelper.SINGLE_CLICK -> {
                        singleClick.setTextColor(them.findAttr(android.R.attr.colorAccent))
                    }
                    AlarmHelper.DOUBLE_SWIPE -> {
                        doubleSwipe.setTextColor(them.findAttr(android.R.attr.colorAccent))
                    }
                    AlarmHelper.SHAKE_DEVICE -> {
                        shakeDevice.setTextColor(them.findAttr(android.R.attr.colorAccent))
                    }
                }
                dismissPager.apply {
                    adapter = MyAdapter()
                    setCurrentItem(ala?.dismissWay ?: 0, false)
                }
            }
        }
    }

    private var scrollerChang: ViewPager2.OnPageChangeCallback? =
        object : ViewPager2.OnPageChangeCallback() {

            @com.pt.common.global.UiAnn
            override fun onPageSelected(position: Int) {
                swipeWay(position)
            }
        }

    override fun onResume() {
        scrollerChang?.let { binding.dismissPager.registerOnPageChangeCallback(it) }
        super.onResume()
    }

    override fun onPause() {
        scrollerChang?.let { binding.dismissPager.unregisterOnPageChangeCallback(it) }
        super.onPause()
    }

    @com.pt.common.global.UiAnn
    private inner class MyAdapter : FragmentStateAdapter(childFragmentManager, lifecycle) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment = run {
            return@run when (position) {
                AlarmHelper.SINGLE_SWIPE -> {
                    newFragmentSingleSwipe {
                        this@newFragmentSingleSwipe.alarm = this@EditAlarmFragment.ala
                        this@newFragmentSingleSwipe.alarmDismiss = this@EditAlarmFragment
                        this@newFragmentSingleSwipe.test = true
                        this@newFragmentSingleSwipe
                    }
                }
                AlarmHelper.SINGLE_CLICK -> {
                    newFragmentSingleClick {
                        this@newFragmentSingleClick.alarm = this@EditAlarmFragment.ala
                        this@newFragmentSingleClick.alarmDismiss = this@EditAlarmFragment
                        this@newFragmentSingleClick.test = true
                        this@newFragmentSingleClick
                    }
                }
                AlarmHelper.SHAKE_DEVICE -> {
                    newFragmentShake {
                        this@newFragmentShake.alarm = this@EditAlarmFragment.ala
                        this@newFragmentShake.alarmDismiss = this@EditAlarmFragment
                        this@newFragmentShake.test = true
                        this@newFragmentShake
                    }
                }
                AlarmHelper.DOUBLE_SWIPE -> {
                    newFragmentDoubleSwipe {
                        this@newFragmentDoubleSwipe.alarm = this@EditAlarmFragment.ala
                        this@newFragmentDoubleSwipe.alarmDismiss = this@EditAlarmFragment
                        this@newFragmentDoubleSwipe.test = true
                        this@newFragmentDoubleSwipe
                    }
                }
                else -> {
                    newFragmentShake {
                        this@newFragmentShake.alarm = this@EditAlarmFragment.ala
                        this@newFragmentShake.alarmDismiss = this@EditAlarmFragment
                        this@newFragmentShake.test = true
                        this@newFragmentShake
                    }
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    override fun swipeWay(way: Int) {
        with(binding) {
            when (way) {
                AlarmHelper.SINGLE_SWIPE -> {
                    editAlarmListener?.swipeWay(AlarmHelper.SINGLE_SWIPE)
                    singleSwipe.setTextColor(them.findAttr(android.R.attr.colorAccent))
                    them.findAttr(R.attr.rmoText).let {
                        singleClick.setTextColor(it)
                        shakeDevice.setTextColor(it)
                        doubleSwipe.setTextColor(it)
                    }
                    dismissPager.currentItem = way
                }
                AlarmHelper.SINGLE_CLICK -> {
                    editAlarmListener?.swipeWay(AlarmHelper.SINGLE_CLICK)
                    singleClick.setTextColor(them.findAttr(android.R.attr.colorAccent))
                    them.findAttr(R.attr.rmoText).let {
                        shakeDevice.setTextColor(it)
                        singleSwipe.setTextColor(it)
                        doubleSwipe.setTextColor(it)
                    }
                    dismissPager.currentItem = way
                }
                AlarmHelper.DOUBLE_SWIPE -> {
                    editAlarmListener?.swipeWay(AlarmHelper.DOUBLE_SWIPE)
                    them.findAttr(R.attr.rmoText).let {
                        singleClick.setTextColor(it)
                        shakeDevice.setTextColor(it)
                        singleSwipe.setTextColor(it)
                    }
                    doubleSwipe.setTextColor(them.findAttr(android.R.attr.colorAccent))
                    dismissPager.currentItem = way
                }
                AlarmHelper.SHAKE_DEVICE -> {
                    editAlarmListener?.swipeWay(AlarmHelper.SHAKE_DEVICE)
                    them.findAttr(R.attr.rmoText).let {
                        singleClick.setTextColor(it)
                        singleSwipe.setTextColor(it)
                        doubleSwipe.setTextColor(it)
                    }
                    shakeDevice.setTextColor(them.findAttr(android.R.attr.colorAccent))
                    dismissPager.currentItem = way
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun FragmentEditAlarmBinding.onClick(v: android.view.View) {
        when (v) {
            singleSwipe -> AlarmHelper.SINGLE_SWIPE
            singleClick -> AlarmHelper.SINGLE_CLICK
            doubleSwipe -> AlarmHelper.DOUBLE_SWIPE
            shakeDevice -> AlarmHelper.SHAKE_DEVICE
            else -> AlarmHelper.SINGLE_SWIPE
        }.let {
            swipeWay(it)
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        ala = null
        editAlarmListener = null
        binder = null
        scrollerChang = null
        super.onDestroyView()
    }

}