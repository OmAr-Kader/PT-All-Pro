package com.pt.pro.extra.receivers

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.RemoteViews
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.common.global.*

class BroadcastWidget : AppWidgetProvider() {

    private inline fun Context.updateAppWidget(remoteViews: RemoteViews.() -> Unit) {
        catchy(Unit) {
            try {
                RemoteViews(
                    com.pt.pro.BuildConfig.APPLICATION_ID, R.layout.layout_for_widget
                ).apply {
                    PendingIntent.getActivity(
                        this@updateAppWidget, 0, widgetDataKeeper, PEND_FLAG
                    ).apply {
                        setOnClickPendingIntent(R.id.tvWidget, this)
                    }
                    PendingIntent.getActivity(
                        this@updateAppWidget, 0, widgetAlarm, PEND_FLAG
                    ).apply {
                        setOnClickPendingIntent(R.id.textClockTime, this)
                        setOnClickPendingIntent(R.id.textClockAmPm, this)
                    }
                    PendingIntent.getActivity(
                        this@updateAppWidget, 3, widgetAllInOne, PEND_FLAG
                    ).apply {
                        setOnClickPendingIntent(R.id.linear_widget, this)
                    }
                    if (is24Hour) setViewVisibility(R.id.textClockAmPm, View.GONE)

                }.apply(remoteViews)
            } catch (_: android.content.res.Resources.NotFoundException) {
            }
        }
    }


    override fun onUpdate(
        context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray
    ) {
        context?.apply {
            findIntegerPref(ID_WIDGET, KEY_WIDGET, WIDGET_DEP).let { widMode ->
                when (widMode) {
                    WIDGET_DEP -> {
                        isNeedWhite
                    }
                    WIDGET_DARK -> {
                        false
                    }
                    else -> {
                        true
                    }
                }.let { isWhite ->
                    if (isWhite) {
                        fetchColor(R.color.gqe)
                    } else {
                        fetchColor(R.color.txt)
                    }.let<@androidx.annotation.ColorInt Int, Unit> { c ->
                        updateAppWidget {
                            setTextColor(R.id.tvWidget, c)
                            setTextColor(R.id.textClockTime, c)
                            setTextColor(R.id.textClockAmPm, c)
                            appWidgetManager?.updateAppWidget(
                                ComponentName(this@apply, BroadcastWidget::class.java), this
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == UPDATE_WIDGET_NOTE) {
            AppWidgetManager.getInstance(context).apply {
                catchy(Unit) {
                    try {
                        RemoteViews(
                            com.pt.pro.BuildConfig.APPLICATION_ID, R.layout.layout_for_widget
                        ).apply {
                            setTextViewText(
                                R.id.tvWidget, intent.getStringExtra(STRING_FOR_WIDGET)
                            )
                            updateAppWidget(
                                ComponentName(context, BroadcastWidget::class.java), this
                            )
                        }
                    } catch (_: android.content.res.Resources.NotFoundException) {
                    }
                }
            }
        } else if (intent.action == REFRESH_WID) {
            with<Context, Unit>(context) {
                intent.getIntExtra(EXTRA_WIDGET, WIDGET_DEP).let {
                    when (it) {
                        WIDGET_DEP -> {
                            isNeedWhite
                        }
                        WIDGET_DARK -> {
                            false
                        }
                        else -> {
                            true
                        }
                    }.let { isWhite ->
                        if (isWhite) {
                            fetchColor(R.color.gqe)
                        } else {
                            fetchColor(R.color.txt)
                        }.let {
                            AppWidgetManager.getInstance(this).apply {
                                updateAppWidget {
                                    setTextColor(R.id.tvWidget, it)
                                    setTextColor(R.id.textClockTime, it)
                                    setTextColor(R.id.textClockAmPm, it)
                                    updateAppWidget(
                                        ComponentName(this@with, BroadcastWidget::class.java), this
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}