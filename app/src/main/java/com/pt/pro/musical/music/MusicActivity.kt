package com.pt.pro.musical.music

import android.graphics.RenderEffect.createBlurEffect
import android.graphics.Shader
import android.widget.ImageView
import com.pt.common.global.*
import com.pt.common.media.getMusicBit
import com.pt.common.stable.*
import com.pt.pro.musical.interfaces.ActivityListener
import com.pt.pro.musical.fasten.ActivityMusicFasten

class MusicActivity : com.pt.common.mutual.life.GlobalBaseActivity(), ActivityListener {

    private var binder: ActivityMusicFasten? = null

    private var jobMusic: kotlinx.coroutines.Job? = null

    @Suppress("LongLine")
    @com.pt.common.global.UiAnn
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        com.pt.pro.musical.fasten.MusicInflater.run { inflaterActivityMusic() }.also {
            @com.pt.common.global.ViewAnn
            binder = it
            it.circleSeekBar.progress = intent.getFloatExtra(MUSIC_PROGRESS, 0F)
        }.root_.also(::setContentView)
        window?.musicScreen() ?: finish()
        MusicObject.activityListener = this

        launchImdMain {
            intiViews()
        }
    }

    private suspend fun intiViews() {
        justCoroutineMain {
            binder?.linView?.orientation = resources.linDirection
            compactImage(
                if (!intent.getBooleanExtra(
                        MUSIC_IS_PLAY, false
                    )
                ) com.pt.pro.R.drawable.ic_play_circle else com.pt.pro.R.drawable.ic_pause_music
            ) {
                binder?.playPause?.setImageDrawable(this@compactImage)
            }

            val one = intent.getIntExtra(
                MUSIC_COLOR_ONE, theme.findAttr(com.pt.pro.R.attr.rmoText)
            )

            val two = intent.getIntExtra(
                MUSIC_COLOR_TWO, theme.findAttr(com.pt.pro.R.attr.rmoBackground)
            )
            val three = intent.getIntExtra(
                MUSIC_COLOR_THREE, theme.findAttr(com.pt.pro.R.attr.rmoGrey)
            )
            val musicSack = if (isV_T) {
                intent.getParcelableExtra(
                    MUSIC_OBJECT, MusicSack::class.java
                )
            } else {
                @Suppress("DEPRECATION") intent.getParcelableExtra(
                    MUSIC_OBJECT,
                )
            }
            binder?.applySus {
                if (isV_S) {
                    backImg.setRenderEffect(createBlurEffect(25F, 25F, Shader.TileMode.CLAMP))
                }
                circleSeekBar.setOnSeekBarChangeListener(this@MusicActivity)
                playPause.setOnClickListener {
                    pendAction(PAUSE_PLAY).send()
                }
                previousMusic.setOnClickListener {
                    pendAction(PREVIOUS_MUSIC).send()
                }
                nextMusic.setOnClickListener {
                    pendAction(NEXT_MUSIC).send()
                }
                unlockScreen.setOnClickListener {
                    finish()
                }
            }
            DSack(one, two, three).displayColors(
                musicSack = musicSack, intent.getFloatExtra(MUSIC_MAX, 300000F)
            )
            musicSack?.loadAlarmPic(false)
        }
        /*justScope {
            displayAd()
        }*/
    }

    /*private fun displayAd() {
        launchDef {
            com.pt.common.ads.AdRecycle.applySusBack {
                loadNativeAds(com.pt.common.BuildConfig.AD_BANNER_MUSIC)
            }
        }
    }*/

    internal inline val android.content.Context.pendAction: (String) -> android.app.PendingIntent
        @com.pt.common.global.WorkerAnn get() = {
            newIntent(MusicPlayer::class.java) {
                putExtra(KEY_ORDER, "tillITHurts")
                action = it
                this@newIntent
            }.let {
                android.app.PendingIntent.getService(
                    this@pendAction, 0, it, PEND_FLAG
                )
            }
        }


    override fun DSack<Int, Int, Int>.updateMusicActivity(
        musicSack: MusicSack, f: Float, isFailedLoadPic: Boolean
    ) {
        DSack(one, two, three).displayColors(musicSack, f)
        musicSack.loadAlarmPic(isFailedLoadPic)
    }

    override fun updatePlayPause(isPlayer: Boolean) {
        compactImage(
            if (!isPlayer) com.pt.pro.R.drawable.ic_play_circle else com.pt.pro.R.drawable.ic_pause_music
        ) {
            binder?.playPause?.setImageDrawable(this@compactImage)
        }
    }

    private fun MusicSack.loadAlarmPic(isFailedLoadPic: Boolean) {
        launchDef {
            if (isFailedLoadPic) {
                compactImageReturn(com.pt.pro.R.drawable.ic_failed_song)?.apply {
                    androidx.core.graphics.drawable.DrawableCompat.setTint(
                        this, android.graphics.Color.WHITE
                    )
                }?.toBitmapSus()?.letSusBack {
                    copy(bitmap = it).doLoadAlarmPic()
                }
            } else {
                doLoadAlarmPic()
            }
        }
    }

    private suspend fun MusicSack.doLoadAlarmPic() {
        if (this@doLoadAlarmPic.bitmap != null) {
            this@doLoadAlarmPic.bitmap.changeBrightness()?.letSusBack { darker ->
                if (isV_S) {
                    darker.loadBlurImage()
                    this@doLoadAlarmPic.bitmap.displayOriImage()
                } else {
                    DSackT(darker, this@doLoadAlarmPic.bitmap).loadBlurUnder31()
                }
            }
        } else {
            getMusicBit(pathAudio.toStr, com.pt.pro.R.drawable.ic_failed_song, 1) {
                this@getMusicBit.changeBrightness()?.letSusBack { darker ->
                    if (isV_S) {
                        darker.loadBlurImage()
                        this@getMusicBit?.displayOriImage()
                    } else {
                        DSackT(darker, this@getMusicBit).loadBlurUnder31()
                    }
                }
            }
        }
    }

    private suspend fun DSackT<android.graphics.Bitmap, android.graphics.Bitmap?>.loadBlurUnder31() {
        withMain {
            kotlin.runCatching {
                blur(one) {
                    withMain {
                        binder?.backImg?.setImageBitmap(this@blur)
                    }
                    two.displayOriImage()
                }
            }.onFailure {
                binder?.backImg?.setImageBitmap(null)
                two?.displayOriImage()
            }
        }
    }

    private suspend fun android.graphics.Bitmap?.loadBlurImage() {
        withMain {
            binder?.backImg?.applySus {
                setImageBitmap(this@loadBlurImage)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    }


    private suspend fun android.graphics.Bitmap?.displayOriImage() {
        withMain {
            binder?.coverImg?.applySus {
                setImageBitmap(this@displayOriImage)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
    }

    private fun DSack<Int, Int, Int>.displayColors(musicSack: MusicSack?, f: Float) {
        jobMusic?.cancelJob()
        jobMusic = launchImdMain {
            doDisplayColors(musicSack, f)
        }
    }

    private suspend fun DSack<Int, Int, Int>.doDisplayColors(musicSack: MusicSack?, f: Float) {
        justCoroutineMain {
            binder?.alsoSus {
                it.musicArtist.applySus {
                    text = musicSack?.artist ?: ""
                    isSelected = true
                }
                it.musicTitle.applySus {
                    text = musicSack?.title ?: ""
                    isSelected = true
                }
                it.circleSeekBar.max = f
            }
        }
        justCoroutineMain {
            binder?.applySus {
                if (findBooleanPref(NIGHT, RIDERS, resources.isDeviceDark)) {
                    playPause.svgReColorSus(one)
                    nextMusic.svgReColorSus(one)
                    previousMusic.svgReColorSus(one)

                    unlockScreen.setTextColor(one)
                    musicArtist.setTextColor(one)
                    musicTitle.setTextColor(one)
                    circleSeekBar.apply {
                        circleProgressColor = one
                        pointerColor = one
                    }
                    root_.setBackgroundColor(two)
                    unlockScreen.backReColor(one)
                } else {
                    playPause.svgReColorSus(two)
                    nextMusic.svgReColorSus(two)
                    previousMusic.svgReColorSus(two)

                    unlockScreen.setTextColor(two)
                    musicArtist.setTextColor(two)
                    musicTitle.setTextColor(two)
                    circleSeekBar.apply {
                        circleProgressColor = two
                        pointerColor = two
                    }
                    root_.setBackgroundColor(one)
                    unlockScreen.backReColor(two)
                }
            }
        }
    }

    override fun forFinish() {
        finish()
    }

    override fun updateProgress(f: Float) {
        if (!MusicObject.inProcess) binder?.circleSeekBar?.progress = f
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binder?.linView?.orientation = resources.linDirection
    }

    override fun onPause() {
        MusicObject.inDisplay = false
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        MusicObject.inDisplay = true
    }

    override fun onDestroy() {
        MusicObject.activityListener = null
        super.onDestroy()
        binder = null
        jobMusic = null
    }

    override fun onProgressChanged(
        circularSeekBar: com.pt.common.moderator.recycler.CircularSeekBar?, progress: Float, fromUser: Boolean
    ) {

    }

    override fun onStopTrackingTouch(seekBar: com.pt.common.moderator.recycler.CircularSeekBar?) {
        MusicObject.inProcess = false
        seekBar?.progress?.toLong()?.let { MusicObject.musicManagerListener?.updatePlayer(it) }
    }

    override fun onStartTrackingTouch(seekBar: com.pt.common.moderator.recycler.CircularSeekBar?) {
        MusicObject.inProcess = true
    }


}
