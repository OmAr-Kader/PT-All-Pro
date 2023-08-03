package com.pt.pro

import com.pt.common.global.*

class App : androidx.multidex.MultiDexApplication(), androidx.work.Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        intiWeb()
    }

    private fun intiWeb() {
        com.pt.common.stable.catchy(Unit) {
            getProcessName { pro ->
                if (isV_P) {
                    android.webkit.WebView.setDataDirectorySuffix(pro)
                }
                com.pt.common.stable.catchy(Unit) {
                    android.webkit.WebView.setWebContentsDebuggingEnabled(false)
                }
                com.pt.common.stable.catchy(Unit) {
                    android.webkit.CookieManager.getInstance().also {
                        it.acceptCookie()
                        it.setAcceptCookie(true)
                    }
                }
            }
        }
    }

    private fun android.app.Application.getProcessName(str: (String) -> Unit) {
        if (isV_P) {
            com.pt.common.stable.catchy(null) {
                getProcessName()
            }
        } else {
            null
        }.let { pro ->
            if (pro == null) {
                fetchSystemService(activityService)?.apply {
                    runningAppProcesses.onEach {
                        if (it.pid == android.os.Process.myPid()) {
                            it.processName.let(str)
                        }
                    }
                }
            } else str(pro)
        }
    }

    override fun getWorkManagerConfiguration(): androidx.work.Configuration {
        return androidx.work.Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setExecutor(com.pt.common.stable.fetchExtractor)
            .setTaskExecutor(com.pt.common.stable.fetchExtractor)
            .setDefaultProcessName(BuildConfig.APPLICATION_ID).build()
    }

}