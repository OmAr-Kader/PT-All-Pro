package com.pt.pro.file.dialogs

import android.os.SystemClock
import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.media.getMusicBit
import com.pt.common.stable.*
import com.pt.pro.R

class PopUpForMusic : com.pt.common.mutual.life.GlobalDia<com.pt.pro.databinding.PopForMusicBinding>(),
    com.pt.pro.file.interfaces.PopMusicListener {

    private var currentSong: MusicSack? = null
    private var currentMode = 0

    private var allMusic: MutableList<MusicSack> = mutableListOf()
    private var currentPos: Int = 0
    private var musicListener: com.pt.pro.file.interfaces.ItemFileListener? = null

    private var mediaPlay: androidx.media3.exoplayer.ExoPlayer? = null

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    private var error = false

    private var isRealPlay = true

    private var jobMusic: kotlinx.coroutines.Job? = null

    override fun MutableList<MusicSack>.pushMusic(position: Int) {
        allMusic = this
        currentPos = position
    }

    override fun com.pt.pro.file.interfaces.ItemFileListener.pushListener() {
        musicListener = this
    }

    override val android.view.LayoutInflater.creBinD: android.view.View
        @com.pt.common.global.UiAnn
        get() = com.pt.pro.databinding.PopForMusicBinding.inflate(this).also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root

    @com.pt.common.global.UiAnn
    override suspend fun com.pt.pro.databinding.PopForMusicBinding.intiViews() {
        qHand = ctxD.fetchHand
        lifecycle.addObserver(this@PopUpForMusic)
        launchMain {
            withMain {
                playMusic.setOnClickListener(this@PopUpForMusic)
                saveEdit.setOnClickListener(this@PopUpForMusic)
                musicCircle.setOnLongClickListener(this@PopUpForMusic)
                musicCircle.setOnClickListener(this@PopUpForMusic)
                previousMusic.setOnClickListener(this@PopUpForMusic)
                nextMusic.setOnClickListener(this@PopUpForMusic)
                editSong.setOnLongClickListener(this@PopUpForMusic)
                editSong.setOnClickListener(this@PopUpForMusic)
                cancelEdit.setOnClickListener(this@PopUpForMusic)
                albumFrame.setOnClickListener(this@PopUpForMusic)
                browserCover.setOnClickListener(this@PopUpForMusic)
                copyForCover.setOnClickListener(this@PopUpForMusic)
                searchCover.setOnClickListener(this@PopUpForMusic)
                backCover.setOnClickListener(this@PopUpForMusic)
                if (recD.isRightToLeft) {
                    ctxD.compactImage(R.drawable.ic_next_song) {
                        previousMusic.setImageDrawable(this@compactImage)
                    }
                    ctxD.compactImage(R.drawable.ic_previous_song) {
                        nextMusic.setImageDrawable(this@compactImage)
                    }
                } else {
                    ctxD.compactImage(R.drawable.ic_previous_song) {
                        previousMusic.setImageDrawable(this@compactImage)
                    }
                    ctxD.compactImage(R.drawable.ic_next_song) {
                        nextMusic.setImageDrawable(this@compactImage)
                    }
                }
                seekBarMusic.onSeekListener { it, mode ->
                    if (mode == 1) {
                        unPost(onEverySecond.two)
                    } else if (mode == 0) {
                        (SystemClock.elapsedRealtime() - it).let { l ->
                            currentDuration.base = l
                        }
                    } else {
                        if (!error) {
                            mediaPlay?.apply {
                                seekTo(it.toLong())
                                if (!isPlaying) play()
                            }
                            onEverySecond.rKTSack(100L).postAfter()
                        } else {
                            ctxD.makeToastRec(R.string.uy, 0)
                        }
                    }
                }
            }
            withMain {
                initViews()
                updateMusicViews(currentMusic)
                intiPlayer()
            }
        }
    }

    //@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private suspend fun intiPlayer() {
        withMain {
            androidx.media3.exoplayer.ExoPlayer.Builder(
                ctxD
            ).applySus {
                //setPauseAtEndOfMediaItems(false)
                //setUseLazyPreparation(true)
                //setUsePlatformDiagnostics(false)
                setAudioAttributes(musicAudioAttr, true)
                setHandleAudioBecomingNoisy(true)
            }.build().alsoSus {
                it.playWhenReady = true
                mediaPlay = it
                playerListener?.let { it1 -> it.addListener(it1) }
                it.musicPlayerPush()
            }
        }
    }

    private var playerListener: androidx.media3.common.Player.Listener? = object : androidx.media3.common.Player.Listener {
        override fun onEvents(
            player: androidx.media3.common.Player,
            events: androidx.media3.common.Player.Events
        ) {
            super.onEvents(player, events)
            if (events.isItemChanged) {
                launchDef {
                    mediaPlay?.apply {
                        afterPrepare()
                    }
                }
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == androidx.media3.common.Player.STATE_ENDED) {
                if (currentPos < allMusic.size - 1) {
                    currentPos += 1
                } else if (currentPos == allMusic.size - 1) {
                    currentPos = 0
                }
                binder?.updateMusicViews(currentMusic)
                musicPlayerInit()
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)

            if (isPlaying) {
                if (!isRealPlay) {
                    mediaPlay?.pause()
                } else {
                    binder?.apply {
                        if (!continueRun) {
                            onEverySecond.rKTSack(100L).postAfter()
                            continueRun = true
                        }
                        ctxD.compactImage(R.drawable.ic_pause_music) {
                            playMusic.setImageDrawable(this@compactImage)
                        }
                    }
                }
            } else {
                binder?.apply {
                    continueRun = false
                    unPost(onEverySecond.two)
                    ctxD.compactImage(R.drawable.ic_play_circle) {
                        playMusic.setImageDrawable(this@compactImage)
                    }
                }
            }
        }
    }


    private var audioListener: android.media.AudioManager.OnAudioFocusChangeListener? =
        android.media.AudioManager.OnAudioFocusChangeListener {

        }


    @com.pt.common.global.UiAnn
    private fun com.pt.pro.databinding.PopForMusicBinding.switchEditShow() {
        launchMain {
            withMain {
                if (editCon.isVis) {
                    currentMode = 0
                    detailsLin.visibleEndSus(250L)
                    detailsFrame.changeWeight(0.8F, 1.1F)
                    editCon.goneStartSus(250L)
                } else {
                    currentMode = 1
                    editCon.visibleStartSus(250L)
                    detailsFrame.changeWeight(1.1F, 0.8F)
                    detailsLin.goneEndSus(250L)
                    coverOptions.goneEndSus(250L)
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    private fun com.pt.pro.databinding.PopForMusicBinding.switchCoverEdit() {
        launchMain {
            withMain {
                if (currentMode == 1) {
                    detailsFrame.changeWeight(0.8F, 1.1F)
                }
                if (coverOptions.isVis) {
                    currentMode = 0
                    detailsLin.visibleEndSus(250L)
                    coverOptions.goneStartSus(250L)
                } else {
                    currentMode = 2
                    coverOptions.visibleStartSus(250L)
                    detailsLin.goneEndSus(250L)
                    editCon.goneEndSus(250L)
                }
            }
        }
    }

    private fun androidx.media3.exoplayer.ExoPlayer.playPause() {
        if (!error) {
            if (isPlaying) {
                isRealPlay = false
                pause()
            } else {
                playSong()
            }
        } else {
            ctxD.makeToastRec(R.string.uy, 0)
        }
    }

    @com.pt.common.global.UiAnn
    override fun onClick(p0: android.view.View?) {
        with<com.pt.pro.databinding.PopForMusicBinding, Unit>(binding) {
            when (p0) {
                playMusic -> mediaPlay?.playPause()
                saveEdit -> binding.doEditCurrentSong()
                cancelEdit -> switchEditShow()
                editSong -> switchEditShow()
                musicCircle -> {
                    mediaPlay?.pause()
                    launchMusicPlayer()
                }
                previousMusic -> {
                    if (currentPos == 0) currentPos = allMusic.size - 1 else currentPos -= 1
                    musicPlayerInit()
                    updateMusicViews(currentMusic)
                }
                nextMusic -> {
                    if (currentPos != allMusic.size - 1) currentPos += 1 else currentPos = 0
                    musicPlayerInit()
                    updateMusicViews(currentMusic)
                }
                albumFrame -> switchCoverEdit()
                backCover -> switchCoverEdit()
                searchCover -> {
                    (currentSong ?: allMusic[currentPos]).let {
                        it.artist.let { art ->
                            if (
                                art != android.provider.MediaStore.UNKNOWN_STRING
                                && !art.isNullOrEmpty()
                            ) {
                                art
                            } else {
                                ""
                            }
                        }.let { art ->
                            it.title?.let { name ->
                                if (name.isNotEmpty()) {
                                    searchOpen("$art $name")
                                }
                            }
                        }

                    }
                }
                copyForCover -> {
                    allMusic[currentPos].pathAudio?.let {
                        ctxD.sendToClipDate(it)
                        switchCoverEdit()
                        ctxD.makeToastRec(R.string.tp, 0)
                    }
                }
                browserCover -> {
                    ctxD.findPicker(com.pt.pro.BuildConfig.APPLICATION_ID) {
                        resultActivity?.launch(this)
                    }
                }
            }
        }
    }

    private fun searchOpen(txt: String) {
        launchDef {
            runCatching {
                searchProviderImgHd(txt, ctxD.findIntegerPrefDb(ID_SEARCH, SEARCH_ENGINE, 0)) {
                    action = com.pt.common.BuildConfig.BROWSER_PRIVATE
                    actD.startActivity(this)
                }
            }.onFailure {
                runCatching {
                    searchProviderImgHd(txt, ctxD.findIntegerPrefDb(ID_SEARCH, SEARCH_ENGINE, 0)) {
                        actD.startActivity(this)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun onLongClick(p0: android.view.View?): Boolean {
        when (p0 ?: return false) {
            binder?.editSong -> R.string.vs
            binder?.musicCircle -> R.string.va
            else -> R.string.va
        }.let {
            p0.popUpComment(it, R.attr.rmoBackground, 0)
        }
        return true
    }

    @com.pt.common.global.UiAnn
    private fun android.view.View.popUpComment(
        @androidx.annotation.StringRes des: Int,
        @androidx.annotation.AttrRes color: Int,
        iy: Int,
    ) {
        with(
            ctxD.commentView(color, resources.getString(des))
        ) {
            android.widget.PopupWindow(
                this@with,
                WRAP,
                WRAP,
                false
            ).run {
                this@run.isOutsideTouchable = true
                this@run.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                if (iy == 0) {
                    this@run.showAsDropDown(this@popUpComment)
                } else {
                    this@run.showAsDropDown(this@popUpComment, 0, iy)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.databinding.PopForMusicBinding.doEditCurrentSong() {
        launchMain {
            withMainDef(false) {
                !titleEdit.text.isNullOrEmpty() &&
                        !artistEdit.text.isNullOrEmpty() &&
                        !albumEdit.text.isNullOrEmpty()
            }.let {
                withMain {
                    if (it) {
                        allMusic[currentPos].applySus {
                            title = titleEdit.text.toStr
                            artist = artistEdit.text.toStr
                            album = albumEdit.text.toStr
                        }.doEditSong()
                    } else {
                        ctxD.makeToastRecSus(R.string.vd, 0)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.databinding.PopForMusicBinding.initViews() {
        detailsLin.orientation = recD.linDirection
        actD.windowManager?.fetchDimensionsMan {
            val widthNew: Int
            val heightNew: Int
            recD.getDimension(R.dimen.gop).toPixelD.let {
                if (recD.isLandTraditional) {
                    heightNew = this@fetchDimensionsMan.height - it
                    widthNew = heightNew + it
                } else {
                    widthNew = this@fetchDimensionsMan.width - it
                    heightNew = widthNew + it
                }
            }
            dia.window?.apply {
                android.view.WindowManager.LayoutParams().apply {
                    copyFrom(attributes)
                    width = widthNew
                    height = heightNew
                    gravity = android.view.Gravity.CENTER
                }.also {
                    attributes = it
                }
            }
            cardPopMusic.framePara(
                widthNew,
                heightNew
            ) {
                gravity = android.view.Gravity.CENTER
            }
        }
    }

    override fun com.pt.pro.databinding.PopForMusicBinding.updateMusicViews(path: String) {
        jobMusic?.cancelJob()
        jobMusic = launchDef {
            ctxD.getMusicBit(path, R.drawable.ic_album, 1) {
                context.nullChecker()
                this@getMusicBit?.displayUpdateMusicViews()
            }
            (ctxD.contentResolver.findOneSong(path) ?: ctxD.getMusicDetails(
                path,
                true
            )).letSusBack {
                currentSong = it
                context.nullChecker()
                displayTxt(it)
            }
        }
    }

    private suspend fun android.graphics.Bitmap.displayUpdateMusicViews() {
        displayImg()
        withBack {
            DSack(
                ctxD.theme.findAttr(R.attr.rmoText),
                ctxD.theme.findAttr(R.attr.rmoBackHint),
                ctxD.theme.findAttr(R.attr.rmoGrey),
            ).letSusBack { def ->
                if (this@displayUpdateMusicViews.width != 0) {
                    this@displayUpdateMusicViews.paletteSus(def, nightRider).letSusBack {
                        binder?.changeButtonColor(it)
                    }
                } else {
                    binder?.changeButtonColor(def)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun android.graphics.Bitmap?.displayImg() {
        withMain {
            binder?.albumImage?.setImageBitmap(this@displayImg)
        }
    }

    private suspend fun com.pt.pro.databinding.PopForMusicBinding.displayTxt(it: MusicSack) {
        withMain {
            musicArtist.text = it.artist
            musicTitle.text = it.title
            titleEdit.text = it.title?.toEditable
            artistEdit.text = it.artist?.toEditable
            albumEdit.text = it.album?.toEditable
        }
    }

    private suspend fun com.pt.pro.databinding.PopForMusicBinding.changeButtonColor(
        col: DSack<Int, Int, Int>,
    ) {
        withMain {
            playMusic.svgReColor(col.one)
            previousMusic.svgReColor(col.one)
            nextMusic.svgReColor(col.one)
            editSong.svgReColor(col.one)
            musicCircle.svgReColor(col.one)
            musicArtist.setTextColor(col.one)
            titleText.setTextColor(col.one)
            albumText.setTextColor(col.one)
            artistText.setTextColor(col.one)
            currentDuration.setTextColor(col.one)
            musicDuration.setTextColor(col.one)
            musicTitle.setTextColor(col.one)
            titleEdit.setTextColor(col.one)
            artistEdit.setTextColor(col.one)
            albumEdit.setTextColor(col.one)
            saveEdit.setTextColor(col.one)
            cancelEdit.setTextColor(col.one)
            seekBarMusic.apply {
                thumbTintList = col.one.toTintList
                progressTintList = col.one.toTintList
                androidx.core.graphics.ColorUtils.setAlphaComponent(
                    col.one,
                    200
                ).toTintList.let {
                    progressBackgroundTintList = it
                }
            }
            cardPopMusic.setBackgroundColor(col.two)
            seekBarMusic.progressBackgroundTintList = col.three.toTintList
            browserCoverFrame.backReColor(col.one)
            browserCover.svgReColor(col.two)
            copyForCoverFrame.backReColor(col.one)
            copyForCover.svgReColor(col.two)
            searchCoverFrame.backReColor(col.one)
            searchCover.svgReColor(col.two)
            backCoverFrame.backReColor(col.one)
            backCover.svgReColor(col.two)
        }
    }

    override val currentMusic: String
        get() {
            return allMusic[currentPos].pathAudio ?: dia.run {
                dismiss()
                ""
            }
        }

    private var continueRun = false

    override val onEverySecond: DSackT<() -> Unit, Int>
        get() = toCatchSack(11) {
            binder?.apply {
                mediaPlay?.let {
                    (SystemClock.elapsedRealtime() -
                            it.currentPosition).let { l ->
                        currentDuration.base = l
                    }
                    if (continueRun) {
                        onEverySecond.rKTSack(100L).postAfter()
                    }
                    seekBarMusic.apply {
                        progress = it.currentPosition.toInt()
                    }
                }
            }
            Unit
        }

    override fun musicPlayerInit() {
        launchMain {
            mediaPlay?.stopSus()
            mediaPlay?.musicPlayerPush()
        }
    }

    private inline val musicTypeAudio: androidx.media3.common.AudioAttributes
        get() {
            return androidx.media3.common.AudioAttributes.Builder().apply {
                setUsage(androidx.media3.common.C.USAGE_MEDIA)
                setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC)
            }.build()
        }

    private suspend fun androidx.media3.exoplayer.ExoPlayer.musicPlayerPush() {
        withMain {
            runCatching {
                setMediaItem(
                    androidx.media3.common.MediaItem.Builder().setUri(currentMusic).build()
                )
                setAudioAttributes(musicTypeAudio, true)
            }
        }
        prepareSus()
    }


    internal suspend fun androidx.media3.exoplayer.ExoPlayer.afterPrepare() {
        withMain {
            binder?.apply {
                seekBarMusic.max = durationSus().toInt()
                if (!continueRun) {
                    onEverySecond.rKTSack(100L).postAfter()
                    continueRun = true
                }
                musicDuration.text = durationSus().findMediaDuration
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onAttach(context: android.content.Context) {
        actD.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onAttach(context)
    }

    @com.pt.common.global.MainAnn
    override fun onDetach() {
        actD.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onDetach()
    }

    private fun androidx.media3.exoplayer.ExoPlayer.playSong() {
        isRealPlay = true
        play()
    }


    private var resultActivity: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == -1) {
                launchDef {
                    withBackDef(null) {
                        return@withBackDef runCatching {
                            return@runCatching if (it.data == null) {
                                ctxD.getCaptureImageOutputUri(com.pt.pro.BuildConfig.APPLICATION_ID)?.path
                            } else {
                                it?.data?.data?.let { itD ->
                                    ctxD.getImagePathFromURI(itD)
                                }
                            }
                        }.getOrNull()
                    }?.letSusBack { path ->
                        allMusic[currentPos].pathAudio?.letSusBack { it1 ->
                            saveArtwork(it1, FileLate(path))
                        } ?: ctxD.makeToastRecSus(R.string.xe, 0)
                    }
                }
            }
        }

    private suspend fun MusicSack.doEditSong() {
        launchDef {
            runCatching {
                editSongTags {
                    withBack {
                        android.media.MediaScannerConnection.scanFile(
                            ctxD,
                            arrayOf(pathAudio ?: ""),
                            null,
                            null
                        )
                    }
                }
                kotlinx.coroutines.delay(200L)
                withMain {
                    binder?.applySus {
                        updateMusicViews(currentMusic)
                        switchEditShow()
                    }
                }
            }.onFailure {
                withMain {
                    it.toStr.logProvCrash("doEditSong")
                    ctxD.makeToastRec(R.string.xe, 0)
                    binder?.switchEditShow()
                }
            }
        }
    }

    private suspend fun saveArtwork(path: String, artFile: FileLate) {
        FileLate(path).doSaveArtwork(artFile).letSus {
            displaySaveArtwork(isFailed = it)
        }
    }

    private suspend fun displaySaveArtwork(isFailed: Boolean) {
        kotlinx.coroutines.delay(100L)
        withMain {
            binder?.applySus {
                if (!isFailed) {
                    updateMusicViews(currentMusic)
                    mediaPlay?.apply {
                        if (!isPlaying) playSong()
                    }
                } else {
                    ctxD.makeToastRecSus(R.string.xe, 0)
                }
                switchCoverEdit()
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun FileLate.doSaveArtwork(artFile: FileLate) = justCoroutine {
        editSongImage(artFile) {
            android.media.MediaScannerConnection.scanFile(
                ctxD,
                arrayOf(path),
                null,
                null
            )
        }
    }

    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binder?.initViews()
    }

    private fun launchMusicPlayer() {
        musicListener?.run {
            allMusic.launchMusic(currentPos, 222)
        }
        dia.dismiss()
    }

    @com.pt.common.global.MainAnn
    override fun onPause() {
        super.onPause()
        isRealPlay = false
        mediaPlay?.pause()
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        resultActivity?.unregister()
        mediaPlay?.apply {
            synchronized(this) {
                catchy(Unit) {
                    playerListener?.let { removeListener(it) }
                }
                catchy(Unit) {
                    pause()
                }
                catchy(Unit) {
                    stop()
                }
                catchy(Unit) {
                    release()
                }
            }
        }
        binder?.apply {
            albumImage.setImageBitmap(null)
        }
        allMusic.clear()
        currentPos = 0
        error = false
        binder = null
        playerListener = null
        mediaPlay = null
        musicListener = null
        audioListener = null
        resultActivity = null
        jobMusic = null
        super.onDestroyView()
    }

}