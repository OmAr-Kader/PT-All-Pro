package com.pt.pro.gallery.service

import androidx.lifecycle.lifecycleScope
import com.pt.common.global.*
import com.pt.common.stable.*

class VideoFloating : androidx.lifecycle.LifecycleService() {

    companion object {
        @JvmStatic
        var videoManagerListener: com.pt.pro.gallery.interfaces.VideoFloatingListener? = null
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
        androidx.core.app.NotificationCompat.Builder(this, CH_VID).apply {
            setSmallIcon(com.pt.pro.R.drawable.ic_launcher_foreground)
            setContentTitle(resources.getString(com.pt.pro.R.string.icon_name))
            setContentText(resources.getString(com.pt.pro.R.string.tv))
            android.widget.RemoteViews(
                com.pt.pro.BuildConfig.APPLICATION_ID,
                com.pt.pro.R.layout.notify_icon
            ).apply {
                newIntent(VideoFloating::class.java) {
                    flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                    action = ACTION_VIDEO_FLOATING
                    putExtra("ext", ACTION_VIDEO_FLOATING)
                    this@newIntent
                }.run {
                    android.app.PendingIntent.getService(
                        this@VideoFloating,
                        SERVICE_VIDEO,
                        this,
                        PEND_FLAG
                    )
                }.let {
                    setOnClickPendingIntent(com.pt.pro.R.id.closeNot, it)
                    return@let androidx.core.app.NotificationCompat.Action.Builder(
                        com.pt.pro.R.drawable.ic_close,
                        resources.getString(com.pt.pro.R.string.co),
                        it
                    )
                }.let { act ->
                    addAction(act.build())
                }
                setTextViewText(
                    com.pt.pro.R.id.titleNot,
                    resources.getString(com.pt.pro.R.string.rg)
                )
                if (resources.isDeviceDark) {
                    android.graphics.Color.WHITE
                } else {
                    android.graphics.Color.BLACK
                }.let { c ->
                    setTextColor(com.pt.pro.R.id.titleNot, c)
                }
            }.apply {
                also(::setCustomContentView)
                //also(::setCustomBigContentView)
                also(::setCustomHeadsUpContentView)
            }

            android.content.Intent(
                this@VideoFloating,
                com.pt.pro.gallery.activities.ActivityGallery::class.java
            ).run {
                android.app.PendingIntent.getActivity(this@VideoFloating, 0, this, PEND_FLAG)
            }.let(::setContentIntent)
            setAutoCancel(false)
            setOnlyAlertOnce(true)
            setOngoing(true)
            foregroundServiceBehavior =
                androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
            setCategory(androidx.core.app.NotificationCompat.CATEGORY_SERVICE)
            priority = androidx.core.app.NotificationCompat.PRIORITY_HIGH
        }.build().apply {
            flags = androidx.core.app.NotificationCompat.FLAG_FOREGROUND_SERVICE or
                    androidx.core.app.NotificationCompat.FLAG_ONGOING_EVENT
            if (isV_Q) {
                startForeground(
                    SERVICE_VIDEO,
                    this,
                    android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
            } else {
                startForeground(SERVICE_VIDEO, this)
            }
        }
    }

    override fun onStartCommand(intent: android.content.Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.action?.let { act ->
            if (act == ACTION_VIDEO_FLOATING) {
                videoManagerListener?.apply {
                    onServiceDestroy {
                        videoManagerListener = null
                        stopServiceNow()
                    }
                }
            } else if (act == ACTION_LOAD_FLOATING || act == VIDEOS_UPDATE) {
                lifecycleScope.launchImdMain {
                    fetchOverlayContext.apply {
                        intent.fetchVideos?.let { vidS ->
                            intent.fetchCurrentVideo?.let { vid ->
                                kotlinx.coroutines.delay(100)
                                justCoroutine {
                                    if (VIDEOS_UPDATE == intent.action && videoManagerListener != null) {
                                        videoManagerListener?.apply {
                                            vidS.updateVideos(vid)
                                        }
                                    } else {
                                        this@apply.newVideoManager(vidS, vid).apply {
                                            updateViewRoot()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Unit
        }

        return START_REDELIVER_INTENT
    }


    private inline val android.content.Intent.fetchVideos: MutableList<MediaSack>?
        get() = if (isV_T) {
            getParcelableArrayListExtra(ALL_VIDEOS, MediaSack::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableArrayListExtra(ALL_VIDEOS)
        }

    private inline val android.content.Intent.fetchCurrentVideo: MediaSack?
        get() = if (isV_T) {
            getParcelableExtra(POPUP_VIDEO, MediaSack::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(POPUP_VIDEO)
        }

    @com.pt.common.global.MainAnn
    private fun android.content.Context.newVideoManager(
        allVideos: MutableList<MediaSack>,
        video: MediaSack,
    ): VideoFloatingManager {
        return VideoFloatingManager(this@newVideoManager, fetchMediaContext).apply {
            videoManagerListener = this
            VideoFloatFasten.run {
                this@newVideoManager.inflater(
                    d50 = findPixel(50),
                )
            }.setViewRoot()
            video.setCurrentVideo()
            allVideos.setAllVideos()
            setInit()
            fetchSystemService(windowService).also {
                it?.setWindowManager()
                it?.addView(getViewRoot, fetchParams)
            }
        }
    }

    private fun android.content.Context.createChannel() {
        androidx.core.app.NotificationChannelCompat.Builder(
            CH_VID,
            androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
        ).apply {
            setName(resources.getString(com.pt.pro.R.string.fv))
            setDescription(resources.getString(com.pt.pro.R.string.a6))
            setShowBadge(false)
            setSound(null, null)
        }.build().let {
            androidx.core.app.NotificationManagerCompat.from(this@createChannel).apply {
                createNotificationChannel(it)
            }
        }
    }


    @com.pt.common.global.MainAnn
    override fun onDestroy() {
        videoManagerListener?.apply {
            onServiceDestroy {
                videoManagerListener = null
            }
        }
        super.onDestroy()
    }

}