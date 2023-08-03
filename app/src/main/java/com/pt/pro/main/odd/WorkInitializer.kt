package com.pt.pro.main.odd

class WorkInitializer : androidx.startup.Initializer<androidx.work.WorkManager> {

    override fun create(
        context: android.content.Context,
    ): androidx.work.WorkManager = androidx.work.WorkManager.getInstance(context)

    override fun dependencies(): List<Class<out androidx.startup.Initializer<*>>> = emptyList()
}