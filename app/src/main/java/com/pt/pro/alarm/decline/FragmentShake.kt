package com.pt.pro.alarm.decline

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.SeekBar
import com.pt.pro.alarm.interfaces.AlarmDismiss
import com.pt.pro.databinding.FragmentShakeBinding
import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.launchImdMain

class FragmentShake : GlobalSimpleFragment<FragmentShakeBinding>(), SeekBar.OnSeekBarChangeListener,
    SensorEventListener, DismissListener {

    override var alarm: AlarmSack? = null
    override var colorImage: Int = 0
    override var alarmDismiss: AlarmDismiss? = null

    override var test: Boolean = false

    private var total = 10
    private var mSensorManager: SensorManager? = null
    private var mAcl = 0F
    private var mAclCurrent = 0F
    private var mAclLast = 0F
    private var count = 0

    private var mOriginProgress = 0

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            FragmentShakeBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
                it.sumMainShake.orientation = rec.linDirection
                ctx.compactImage(com.pt.pro.R.drawable.circle) {
                    this?.let { d ->
                        androidx.core.graphics.drawable.DrawableCompat.setTintList(
                            d,
                            them.findAttr(android.R.attr.colorPrimary).toTintList,
                        )
                    }
                    it.seekScale.thumb = this
                }
            }.root
        }

    @com.pt.common.global.UiAnn
    override fun FragmentShakeBinding.onViewCreated() {
        launchImdMain {
            com.pt.common.stable.justCoroutineMain {
                seekScale.apply {
                    progress = 50
                    max = 1000
                    setOnSeekBarChangeListener(this@FragmentShake)
                }
                if (ctx.is24Hour) {
                    clockAmPm.justGone()
                }
                content.startRippleAnimation()
                ctx.fetchSystemService(sensorService)?.apply {
                        registerListener(
                        this@FragmentShake,
                        getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL
                    )
                    mSensorManager = this
                }
                mAcl = 0.00F
                mAclCurrent = SensorManager.GRAVITY_EARTH
                mAclLast = SensorManager.GRAVITY_EARTH
            }
            com.pt.common.stable.justCoroutineMain {
                com.pt.common.stable.catchy(null) {
                    if (isV_O) {
                        try {
                            androidx.core.content.res.ResourcesCompat.getFont(
                                ctx,
                                com.pt.pro.R.font.widget_font_clock
                            )
                        } catch (_: android.content.res.Resources.NotFoundException) {
                            null
                        }
                    } else null
                }?.let {
                    clockTime.apply {
                        typeface = it
                    }
                    clockAmPm.apply {
                        typeface = it
                    }
                }
            }
            com.pt.common.stable.justCoroutineMain {
                if (test && !nightRider) {
                    ctx.fetchColor(com.pt.pro.R.color.bbo).let {
                        clockTime.setTextColor(it)
                        clockAmPm.setTextColor(it)
                        textLabel.setTextColor(it)
                        textRepeating.setTextColor(it)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager?.apply {
            registerListener(
                this@FragmentShake,
                getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onPause() {
        mSensorManager?.unregisterListener(this)
        super.onPause()
    }

    @com.pt.common.global.UiAnn
    override fun onProgressChanged(seekBar: SeekBar?, p: Int, fromUser: Boolean) {
        runCatching {
            if (kotlin.math.abs(mOriginProgress - p) < 300) {
                seekBar?.isEnabled = true
                when {
                    p >= 950 -> {
                        seekBar?.progress = 950
                    }
                    p <= 50 -> {
                        seekBar?.progress = 50
                    }
                    else -> {
                        if (p >= 300 && binding.slideText.isVis) {
                            binding.slideText.justGone()
                        } else if (p < 300 && binding.slideText.isGon) {
                            binding.slideText.justVisible()
                        }
                        seekBar?.progress = p
                    }
                }
                mOriginProgress = p
            } else {
                seekBar?.isEnabled = false
                seekBar?.progress = mOriginProgress
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        mOriginProgress = binding.seekScale.progress
    }

    @com.pt.common.global.UiAnn
    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        if (binding.seekScale.progress > 800) {
            alarmDismiss?.doSnooze()
        }
        binding.seekScale.animateSeekBar(5)
    }

    @com.pt.common.global.UiAnn
    override fun FragmentShakeBinding.onClick(v: android.view.View) {

    }

    override fun onSensorChanged(se: SensorEvent) {
        kotlin.runCatching {
            se.values[0].let { x ->
                se.values[1].let { y ->
                    se.values[2].let { z ->
                        mAclLast = mAclCurrent
                        mAclCurrent = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                        mAcl = (mAcl * 0.9f) + (mAclCurrent - mAclLast)
                        if (mAcl > 10) {
                            count++
                            if (count >= total) {
                                alarmDismiss?.doDismiss()
                                activity?.supportFragmentManager?.popBackStack()
                            } else {
                                binder?.dismissText?.text = (count - total).toString()
                            }
                        }
                    }
                }
            }
        }.onFailure {
            alarmDismiss?.doDismiss()
        }
    }

    @com.pt.common.global.UiAnn
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.sumMainShake.orientation = newConfig.linConDirection
    }

    override fun onDestroyView() {
        binder?.content?.stopRippleAnimation()
        mSensorManager = null
        alarmDismiss = null
        alarm = null
        binder = null
        super.onDestroyView()
    }

}