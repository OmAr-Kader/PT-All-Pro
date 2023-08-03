package com.pt.pro.alarm.views

import com.pt.common.global.*
import com.pt.common.stable.*

class SetEditAlarm : com.pt.common.mutual.life.GlobalSubActivity() {

    private var setAlarm: SetEditAlarmFragment? = null

    private var lastIdCreated: Int? = null

    private var resultActivity: Bring = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) {

    }

    override suspend fun initSubActivity(i: Int) {
        justCoroutine {
            intiOnBackPressed {
                (setAlarm ?: return@intiOnBackPressed).onBackL {
                    if (getMode == ADD_ALARM && !it && lastIdCreated != null) {
                        runCatching {
                            lastIdCreated?.let { aa ->
                                newDBAlarmNor {
                                    deleteAlarm1(aa)
                                }
                            }
                        }
                    }
                    toFinishSetAlarm()
                }
            }
        }
        justCoroutine {
            runCatching {
                if (isV_M && !isNotificationPolicyAccessGranted) {
                    updatePrefBoolean(FIRST_ALARM, false)
                    justCoroutine {
                        resultActivity?.launch(goToDisturb)
                    }
                } else {
                    return@runCatching
                }
            }
        }
        justCoroutine {
            launchDef {
                findAlarm {
                    intiFragment(i)
                }
            }
        }
    }

    private fun AlarmSack.intiFragment(i: Int) {
        launchImdMain {
            newSetEditAlarmFragment {
                setAlarm = this@newSetEditAlarmFragment
                ala = this@intiFragment
                this@newSetEditAlarmFragment
            }.alsoSus {
                supportFragmentManager.fragmentLauncher(FRAGMENT_SET_ALARM) {
                    add(i, it, FRAGMENT_SET_ALARM)
                }
            }
        }
    }

    override fun DSack<android.widget.FrameLayout, android.widget.FrameLayout, androidx.appcompat.widget.AppCompatImageView>.initSubViews(statusHeight: Int) {
        two.myStatMargin(statusHeight)
        three.statBarLine(statusHeight + actionBarHeight)
    }

    override val Int.topFrameMargin: Int
        get() = this@topFrameMargin

    private suspend fun findAlarm(
        b: AlarmSack.() -> Unit
    ) {
        if (getMode == EDIT_ALARM) {
            intent.getIntExtra(ALARM_EXTRA, -1).let {
                newDBAlarm {
                    getAlarmFire(it)
                } ?: createAlarm()
            }
        } else {
            createAlarm()
        }.letSusBack(b)
    }

    private suspend fun createAlarm(): AlarmSack = justCoroutine {
        return@justCoroutine com.pt.pro.alarm.objects.AlarmHelper.run {
            fetchValidId().let {
                lastIdCreated = it
                updatePrefInt(KEY_ALARM_CREATOR, it + 1)
                it
            }.let {
                return@let AlarmSack(
                    idAlarm = it,
                    timeAlarm = System.currentTimeMillis(),
                    labelAlarm = null,
                    ringAlarm = null,
                    dismissWay = 0,
                    recordAlarm = null,
                    switchAlarm = true,
                    imgBackground = null,
                    isAlarm = true,
                    isVibrate = false
                ).apply {
                    newDBAlarm {
                        addAlarm()
                    }
                }
            }
        }
    }

    private inline val getMode: Int
        get() = intent.getIntExtra(MODE_EXTRA, ADD_ALARM)

    override fun onDestroy() {
        resultActivity?.unregister()
        setAlarm = null
        lastIdCreated = null
        resultActivity = null
        super.onDestroy()
    }

}
