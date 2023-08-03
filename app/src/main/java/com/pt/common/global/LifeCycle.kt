package com.pt.common.global

import androidx.core.content.edit
import com.pt.common.stable.alsoSus
import com.pt.common.stable.applySus
import com.pt.common.stable.listThrowable

internal fun android.app.Service.stopServiceNow() {
    kotlin.runCatching {
        if (isV_N) {
            stopForeground(android.app.Service.STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }.onFailure {
        stopSelf()
    }
}

inline val android.content.ContextWrapper.fetchHandler: android.os.Handler
    get() = androidx.core.os.HandlerCompat.createAsync(mainLooper)

inline val android.content.Context.fetchHand: android.os.Handler
    get() = androidx.core.os.HandlerCompat.createAsync(mainLooper)

internal suspend fun android.widget.PopupWindow.dismissSus() {
    com.pt.common.stable.withMain {
        dismiss()
    }
}

@android.annotation.SuppressLint("CommitTransaction")
@androidx.annotation.UiThread
internal suspend inline fun androidx.fragment.app.FragmentManager.fragmentLauncher(
    tag: String,
    @androidx.annotation.UiThread crossinline transaction: suspend androidx.fragment.app.FragmentTransaction.() -> Unit,
) {
    com.pt.common.stable.withMainDef(null) {
        findFragmentByTag(tag)
    }.alsoSus { fra ->
        if (fra != null) {
            com.pt.common.stable.justCoroutine {
                beginTransaction().remove(fra).commit()
            }.alsoSus {
                beginTransaction().applySus {
                    setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    transaction(this@applySus)
                }.commit()
            }
        } else {
            com.pt.common.stable.withMainNormal {
                beginTransaction().applySus {
                    setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    transaction(this)
                }.commit()
            }
        }
    }
}

@android.annotation.SuppressLint("CommitTransaction")
@androidx.annotation.UiThread
internal suspend inline fun androidx.fragment.app.FragmentManager.fragmentStackLauncher(
    tag: String,
    @androidx.annotation.UiThread crossinline transaction: suspend androidx.fragment.app.FragmentTransaction.() -> Unit,
) {
    if (findFragmentByTag(tag) != null) return
    com.pt.common.stable.withMainNormal {
        beginTransaction().applySus {
            setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_NONE)
            transaction(this)
        }.commit()
    }
}

suspend fun androidx.appcompat.app.AppCompatActivity.finishSus() {
    com.pt.common.stable.withMain {
        finish()
    }
}

fun androidx.appcompat.app.AppCompatActivity.toFinish() {
    finish()
    android.content.Intent(
        this@toFinish,
        com.pt.pro.main.former.MainActivity::class.java
    ).apply {
        flags = android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP or
                android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP or
                android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra(com.pt.common.stable.NOT_FOR_PRO, true)
    }.also(this@toFinish::startActivity)
}

fun androidx.appcompat.app.AppCompatActivity.toFinishSetAlarm() {
    finish()
    android.content.Intent(
        this@toFinishSetAlarm,
        com.pt.pro.alarm.views.MainActivityAlarm::class.java
    ).apply {
        flags = android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP or
                android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP or
                android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
        putExtra(com.pt.common.stable.NOT_FOR_PRO, true)
    }.also(this@toFinishSetAlarm::startActivity)
}

@com.pt.common.global.WorkerAnn
fun android.content.Context.isServiceNotRunning(serviceClassName: String?): Boolean {
    fetchSystemService(activityService).apply {
        @Suppress("DEPRECATION")
        this?.getRunningServices(Int.MAX_VALUE)?.forEach {
            if (it.service.className == serviceClassName) {
                return false
            }
        }
    }
    return true
}

suspend fun android.content.Context.updateBooleanPrefSus(
    prefID: String,
    keyId: String,
    b: Boolean
) {
    com.pt.common.stable.withMain {
        com.pt.common.stable.catchy(Unit) {
            getSharedPreferences(prefID, android.content.Context.MODE_PRIVATE).edit {
                putBoolean(keyId, b)
            }
        }
    }
}

suspend fun android.content.Context.updateIntPrefSus(prefID: String, keyId: String, b: Int) {
    com.pt.common.stable.withMain {
        com.pt.common.stable.catchy(Unit) {
            getSharedPreferences(prefID, android.content.Context.MODE_PRIVATE).edit {
                putInt(keyId, b)
            }
        }
    }
}

inline val android.content.Context.getDimensions: (Int, def: Int) -> Int
    get() = { rec, def ->
        com.pt.common.stable.catchy(def) {
            try {
                resources.getDimension(rec).toInt()
            } catch (e: android.content.res.Resources.NotFoundException) {
                e.listThrowable()
                def
            }
        }
    }

internal inline fun foundObserver(
    crossinline run: () -> Unit,
): android.database.ContentObserver {
    return object : android.database.ContentObserver(
        null
    ) {
        override fun onChange(selfChange: Boolean) {
            run.invoke()
        }
    }
}
