package com.pt.pro.alarm.views

import android.content.res.Configuration
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.pt.common.global.*
import com.pt.common.media.deleteImage
import com.pt.common.moderator.recycler.NoAnimGridManager
import com.pt.common.mutual.life.GlobalFragment
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.alarm.interfaces.AlarmMainListener
import com.pt.pro.alarm.objects.AlarmHelper
import com.pt.pro.alarm.objects.AlarmHelper.timeFromNow
import com.pt.pro.alarm.fasten.FragmentAlarmFasten
import com.pt.pro.alarm.release.AlarmReceiver.Companion.cancelReminderAlarm
import com.pt.pro.alarm.release.AlarmReceiver.Companion.cancelReminderAlarmSus
import com.pt.pro.alarm.release.AlarmReceiver.Companion.setReminderAlarm

class FragmentAlarm : GlobalFragment<FragmentAlarmFasten>(), AlarmMainListener {

    private var alarmAdapter: AlarmAdapter? = null

    @Volatile
    private var isFirstOpen = true

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.alarm.fasten.AlarmFasten.run { this@creBin.context.inflaterAlarm() }.also {
                @com.pt.common.global.ViewAnn
                binder = it
                it.alarmCard.intiBack21(them.findAttr(android.R.attr.colorPrimary))
            }.root
        }

    override var lastJob: kotlinx.coroutines.Job? = null

    @com.pt.common.global.UiAnn
    override fun FragmentAlarmFasten.onViewCreated() {
        launchDef {
            intiViews()
            forLoad()
        }
    }

    private suspend fun FragmentAlarmFasten.intiViews() {
        withMain {
            alarmAdapter = AlarmAdapter(
                mutableListOf(),
                nightRider = nightRider,
                is24 = ctx.is24Hour,
                refreshListener = this@FragmentAlarm
            )
            recyclerAlarms.apply {
                layoutManager = NoAnimGridManager(
                    ctx, if (rec.isLandTraditional) 2 else 1
                )
                adapter = alarmAdapter
            }
            mainBack.setOnClickListener(this@FragmentAlarm)
            addAlarmButton.apply {
                setOnClickListener(this@FragmentAlarm)
                setOnLongClickListener(this@FragmentAlarm)
            }
        }
    }

    private suspend fun forLoad() {
        ctx.newDBAlarm {
            getValidAlarms()
        }.letSusBack {
            context.nullChecker()
            it.displayRefresh()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onResume() {
        super.onResume()
        if (!isFirstOpen) {
            launchDef {
                forRefresh(null)
            }
        }
    }


    override fun onPause() {
        isFirstOpen = false
        super.onPause()
    }

    private suspend fun forRefresh(@androidx.annotation.StringRes s: Int?) {
        ctx.newDBAlarm {
            getValidAlarms()
        }.letSusBack {
            context.nullChecker()
            it.displayRefresh()
            s?.letSusBack { it1 -> ctx.makeToastRecSus(it1, 0) } ?: return@letSusBack
        }
    }

    private suspend fun MutableList<AlarmSack>.displayRefresh() {
        withMain {
            binder?.recyclerAlarms?.clearRecyclerPool()
            alarmAdapter?.apply {
                setAlarms()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun AlarmSack.forLaunch() {
        ctx.newIntent(SetEditAlarm::class.java) {
            putExtra(MODE_EXTRA, EDIT_ALARM)
            putExtra(ALARM_EXTRA, idAlarm)
            flags = FLAGS
            addCategory(LAUNCH_CATO)
            this@newIntent
        }.also(::startActivity)
    }

    override fun AlarmSack.forDelete() {
        launchDef {
            ctx.cancelReminderAlarmSus(this@forDelete)
            ctx.newDBAlarm {
                deleteAlarm(this@forDelete)
            }.letSusBack {
                justCoroutine {
                    recordAlarm?.let { del -> FileLate(del).deleteImage() }
                }
                justCoroutine {
                    if (it == 1) {
                        forRefresh(R.string.d4)
                    } else return@justCoroutine
                }
            }
        }
    }

    override fun AlarmSack.forSwitchOff(a: suspend () -> Unit) {
        launchDef {
            withBack {
                ctx.newDBAlarm {
                    this@forSwitchOff.copy(switchAlarm = false).updateAlarmSus()
                }
                ctx.cancelReminderAlarm(this@forSwitchOff)
            }
            a.invoke()
            kotlinx.coroutines.delay(100L)
            forRefresh(R.string.ac)
        }
    }

    override fun AlarmSack.forSwitchOn(a: suspend () -> Unit) {
        launchDef {
            withBack {
                fetchAlarmTime(allDays.contains(true)) { finalTime ->
                    ctx.setReminderAlarm(finalTime, idAlarm) {
                        ctx.newDBAlarm {
                            copy(switchAlarm = true, timeAlarm = finalTime).updateAlarmSus()
                        }
                    }
                }
            }
            a.invoke()
            kotlinx.coroutines.delay(200L)
            justCoroutine {
                forRefresh(null)
            }
        }
    }

    private suspend inline fun AlarmSack.fetchAlarmTime(
        anyDayRepeating: Boolean,
        crossinline b: suspend (Long) -> Unit
    ) {
        justCoroutine {
            timeAlarm.timeFromNow
        }.letSusBack { pickerTime ->
            context.nullChecker()
            justCoroutine a@{
                return@a if (pickerTime < System.currentTimeMillis()) {
                    pickerTime + 86400000L
                } else {
                    pickerTime
                }
            }.letSusBack { time ->
                context.nullChecker()
                justCoroutine {
                    AlarmHelper.getTimeForNextAlarm(pickerTime, allDays)
                }.let { cTime ->
                    context.nullChecker()
                    withMain {
                        time.fetchRemainingTime.let { reTime ->
                            if (anyDayRepeating) {
                                DSackT(
                                    cTime,
                                    pickerTime
                                ).getRemainingDays(
                                    getString(R.string.y1),
                                    getString(R.string.y5)
                                )
                            } else {
                                ""
                            }.let { reDayText ->
                                (R.string.aw.dStr + " " + "<b>" +
                                        reDayText + reTime + "</b>").let { sb2 ->
                                    ctx.makeToast(sb2.toHtmlText, 0)
                                }
                            }
                        }
                    }
                    context.nullChecker()
                    justCoroutine {
                        if (anyDayRepeating) {
                            cTime.timeInMillis
                        } else {
                            time
                        }
                    }.letSusBack(b)
                }
            }
        }
    }

    override fun FragmentAlarmFasten.onClick(v: View) {
        if (v == binding.mainBack) act.onBackPressedDispatcher.onBackPressed() else createNewAlarm()
    }

    @com.pt.common.global.WorkerAnn
    private fun createNewAlarm() {
        launchDef {
            ctx.newIntent(SetEditAlarm::class.java) {
                putExtra(MODE_EXTRA, ADD_ALARM)
                flags = FLAGS
                addCategory(LAUNCH_CATO)
                this@newIntent
            }.also {
                this@FragmentAlarm.startActivity(it)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun FragmentAlarmFasten.onLongClick(v: View): Boolean {
        v.popUpComment(R.string.lh, R.attr.rmoBackHint, (-1 * 120F.toPixel))
        return true
    }

    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        with(binder ?: return) {
            alarmCard.cardAsView(ctx.actionBarHeight)
            alarmRecFrame.myActMargin(ctx.actionBarHeight)
            titleAlarm.editAppearance()
            recyclerAlarms.apply {
                layoutManager = GridLayoutManager(ctx, if (rec.isLandTraditional) 2 else 1)
                adapter = alarmAdapter
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        binder?.root?.removeAllViewsInLayout()
        binder?.recyclerAlarms?.adapter = null
        alarmAdapter?.onAdapterDestroy()
        alarmAdapter = null
        isFirstOpen = false
        com.pt.pro.alarm.fasten.AlarmFasten.destroyFasten()
        super.onDestroyView()
    }

}