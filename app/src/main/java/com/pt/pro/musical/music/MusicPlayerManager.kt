package com.pt.pro.musical.music

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*
import com.pt.pro.musical.fasten.MusicHeadFasten
import com.pt.pro.musical.fasten.MusicHeadPlayerFasten
import com.pt.pro.musical.fasten.MusicRecyclerHeadFasten

open class MusicPlayerManager(
    ctx: android.content.Context,
    ctxMe: android.content.Context,
) : GlobalMusicManger(ctx, ctxMe) {

    override val getViewRoot: android.view.View get() = headPlayer.root_

    override val getViewRoot2: android.view.View get() = headMusic.root_

    @com.pt.common.global.MainAnn
    override fun android.view.WindowManager.setWindowManager() {
        windManager = this
    }

    @com.pt.common.global.MainAnn
    override fun MusicHeadFasten.setViewRoot() {
        @com.pt.common.global.ViewAnn
        rootMain = this
    }

    @com.pt.common.global.MainAnn
    override fun MusicHeadPlayerFasten.setViewRoot2() {
        @com.pt.common.global.ViewAnn
        rootDetails = this
    }

    @com.pt.common.global.MainAnn
    override fun setRepeatMode(repeat: Int) {
        reMode = repeat
    }

    @com.pt.common.global.MainAnn
    override fun setInit() {
        isRight = recM.isRightToLeft
        setParams()
    }

    @com.pt.common.global.MainAnn
    private fun setParams() {
        ctxM.fetchDimensions {
            screenWidth = this@fetchDimensions.width
            screenHeight = this@fetchDimensions.height
            params = floatingWindowManger()
            fetchParams.x = -(this@fetchDimensions.width / 2)
            fetchParams.y = -100
        }
    }

    @com.pt.common.global.MainAnn
    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    override fun androidx.media3.session.MediaLibraryService.intiPlayer(): androidx.media3.session.MediaLibraryService.MediaLibrarySession {
        return androidx.media3.exoplayer.ExoPlayer.Builder(ctxM).apply {
            setAudioAttributes(com.pt.common.global.musicAudioAttr, true)
            setHandleAudioBecomingNoisy(true)
            setWakeMode(androidx.media3.common.C.WAKE_MODE_LOCAL)
        }.build().also {
            it.setForegroundMode(true)
            it.repeatMode = androidx.media3.common.Player.REPEAT_MODE_ALL
            it.playWhenReady = true
            it.addListener(this@MusicPlayerManager)
        }.let { exo ->
            mediaPlay = exo
            androidx.media3.session.MediaLibraryService.MediaLibrarySession.Builder(
                this@intiPlayer,
                exo,
                LibrarySessionCallback()
            ).setSessionActivity(sessionActivityPendingIntent).setId(CH_MUS)
                .build().apply {
                    mediaSessionNative = this
                }
        }
    }

    override fun updateViewRoot() {
        qHand = ctxM.fetchHand
        pushJob {
            launchImdMain {
                justCoroutine {
                    if (isFromCreate != true) {
                        context.catchyBadToken {
                            windowManagerMusic.addView(getViewRoot2, fetchParams)
                            windowManagerMusic.addView(getViewRoot, fetchParams)
                        }
                    }
                }
                displayForLoad()
                context.nullChecker()
                reMode = ctxM.findIntegerPreference(REPEAT_KEY, REPEAT_ALL)
                displayRepeater(reMode)
                context.nullChecker()
                musicAnimation = headPlayer.circleCard.spinForEver()
                context.nullChecker()
                initViews()
                context.nullChecker()
                withMain {
                    songPlayer.apply {
                        reMode.intiRepeater.let {
                            shuffleModeEnabled = it.two
                            repeatMode = it.one
                        }
                    }
                }
                justCoroutine {
                    if (allItemNative.isNullOrEmpty()) {
                        allMusic.intiPlayer(currPos, intiSeekSong)
                    } else return@justCoroutine
                }
                justCoroutine {
                    if (isCarMode) {
                        ctxM.makeToastRecSus(com.pt.pro.R.string.ot, 1)
                    }
                }
            }
        }
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                firstNotify()
                context.nullChecker()
                allMusic.loadAllMusic()
                context.nullChecker()
                doSoundEffect(songPlayer.audioSessionIdSus())
            }
        }
    }

    private suspend fun MutableList<MusicSack>.loadAllMusic() {
        justCoroutine {
            doLoadAllMusic()
        }
    }

    private suspend fun displayForLoad() {
        withMain {
            rootMain?.pro?.apply {
                isProgressLoading = true
                setColor(android.graphics.Color.RED)
            }
            runLoadDisplay.rKTSack(100L).postAfter()
        }
    }

    private suspend fun removeForLoad() {
        if (backProcessISDone && mainProcessISDone) {
            withMain {
                (rootMain ?: return@withMain).pro.applySus {
                    isProgressLoading = false
                    setColor(android.graphics.Color.DKGRAY)
                }
                onSecondEvery.rKTSack(100L).postAfter()
            }
        }
    }

    private val runLoadDisplay: DSackT<() -> Unit, Int>
        get() = toCatchSack(22) {
            if (context != null) {
                if (backProcessISDone && mainProcessISDone) {
                    unPost(runLoadDisplay.two)
                    unPost(runLoadRemove.two)
                } else {
                    rootMain?.pro?.apply {
                        setProgress(getMax().toFloat())
                    }
                    runLoadRemove.rKTSack(300L).postAfter()
                }
            }
        }

    private val runLoadRemove: DSackT<() -> Unit, Int>
        get() = DSackT({
            if (context != null) {
                if (backProcessISDone && mainProcessISDone) {
                    unPost(runLoadDisplay.two)
                    unPost(runLoadRemove.two)
                } else {
                    rootMain?.pro?.apply {
                        setProgress(getMin().toFloat())
                    }
                    runLoadDisplay.rKTSack(300L).postAfter()
                }
            }
        }, 33)

    private suspend fun firstNotify() {
        currentMusic?.pathAudio?.toStr?.letSusBack {
            updatePlayerViews(pos = currPos, path = it, isPlay = false)
        }
    }

    private fun MutableList<MusicSack>.intiPlayer(
        currPos: Int,
        intiSeekSong: Long
    ) {
        intiPlayerList(currPos, intiSeekSong)
    }

    private fun MutableList<MusicSack>.doLoadAllMusic() {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                context.nullChecker()
                this@doLoadAllMusic.margeArtistList(contM)
                context.nullChecker()
                this@doLoadAllMusic.insertPlaylist()
                justCoroutine {
                    mainProcessISDone = true
                }
                context.nullChecker()
                removeForLoad()
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun whichPlayList() {
        layoutRec.also {
            if (it == null) {
                withMain {
                    headPlayer.circleFrame.goneFadeSus(300L)
                    headMusic.frameDetails.applySus {
                        goneFadeSus(300L)
                        tag = false
                    }
                }
                withMainNormal {
                    intiRecView()
                }
            } else {
                withMain {
                    headPlayer.circleFrame.goneFadeSus(300L)
                    headMusic.frameDetails.applySus {
                        goneFadeSus(300L)
                        tag = false
                    }
                    it.searchMusic.text?.clear()
                    it.cardFrame.applySus {
                        tag = true
                        justVisibleSus()
                    }
                    songColors.reColor(isForce = true)
                    it.rePushAdapter()
                    musicAdapter.applySus {
                        currentPath = currentMusic?.pathAudio.toStr
                        allMusic.updateSongs()
                    }
                    scrollRec(currentMusic?.pathAudio)
                }
            }
        }
    }


    override fun invoke() {
        launchDef {
            imeBack()
        }
    }

    private suspend fun imeBack() {
        withMain {
            layoutRec?.applySus {
                displayAfterCato(whatSongMode)
                searchMusic.text?.clear()
            }
            windowManagerMusic.alsoSus {
                context.catchyBadToken {
                    it.updateViewLayout(headMusic.root_, fetchParams)
                }
            }
        }
    }

    override suspend fun doSwipeFun(path: String) {
        allMusic.indexOfFirst {
            it.pathAudio == path
        }.alsoSus { pos ->
            withMain {
                allMusic.removeAtIndex(pos)
                allItemMusic.removeAtIndex(pos)
                allMusicOriginal.removeAtIndex(pos)
            }
            songPlayer.removeItemFromPlayList(pos)
        }
        withMain {
            allMusic.indexOf(currentMusic).letSus {
                if (it == -1) 0 else it
            }.letSus { newCurrentPos ->
                currPos = newCurrentPos
                musicAdapter.apply {
                    currentMusic?.pathAudio?.let { onDismissItem(path, it) }
                }
            }
        }
    }

    override suspend fun doMoveFun(pos: Int, tarPos: Int) {
        musicAdapter.applySus {
            doMigration(pos, tarPos)
            songPlayer.moveItemFromPlayList(pos, tarPos)
        }
    }

    private suspend fun doMigration(pos: Int, tarPos: Int) {
        musicAdapter.applySus {
            allMusic.alsoSus { allM ->
                justCoroutine {
                    if (pos <= tarPos) {
                        java.util.Collections.rotate(allM.subList(pos, tarPos + 1), -1)
                    } else {
                        java.util.Collections.rotate(allM.subList(tarPos, pos + 1), 1)
                    }
                }
                justCoroutine {
                    haveMove = true
                    currPos = allMusic.indexOf(currentMusic).let {
                        if (it == -1) 0 else it
                    }
                }
                rotateOriginal(pos = pos, tarPos = tarPos)
            }
        }
    }

    private fun androidx.media3.common.Player.pushMusic(pos: Int) {
        pushJob {
            jobPushMusic?.cancelJob()
            launchDef {
                resetRepeater()
                justCoroutine {
                    isFromCreate = false
                }
                unPost(onSecondEvery.two)
                skipPos(pos)
            }.also { jobPushMusic = it }
        }
    }


    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
        super.onPlayerError(error)
        isError = true
    }

    override fun onPlayerErrorChanged(error: androidx.media3.common.PlaybackException?) {
        super.onPlayerErrorChanged(error)
        isError = true
    }

    override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
        super.onDeviceVolumeChanged(volume, muted)
        if (volume == 0 && !haveHand) {
            runnable.rKTSack(120000L).postAfter()
            haveHand = true
        } else if (volume != 0 && haveHand) {
            unPost(runnable.two)
            haveHand = false
        }
    }

    private inline val androidx.media3.exoplayer.ExoPlayer.fetchItemIndex: Int
        get() {
            return catchy(currentMediaItemIndex) {
                if (reMode == REPEAT_SHUFFLE) {
                    allMusic[currentMediaItemIndex].pathAudio?.let { u ->
                        allMusic.indexOfFirst { it.pathAudio == u }.let { ind ->
                            if (ind == -1) currentMediaItemIndex else ind
                        }
                    } ?: currentMediaItemIndex
                } else {
                    currentMediaItemIndex
                }
            }
        }


    override fun onTracksChanged(tracks: androidx.media3.common.Tracks) {
        if (allMusic.isEmpty()) {
            return
        }
        if (isFromCreate == true) {
            toCatchSack(4923) {
                songPlayer.pause()
            }.postNow()
        }
        super.onTracksChanged(tracks)
        songPlayer.fetchItemIndex.also { ind ->
            allMusic.getINull(ind)?.currentItemChanged(ind = ind)
        }
    }

    private fun MusicSack.currentItemChanged(ind: Int) {
        if (currPos != ind || changMusic != pathAudio) {
            //pushJob {
            jobItemChanged?.cancelJob()
            jobItemChanged = launchImdMain {
                //j?.checkIfDone()
                justCoroutine {
                    currPos = ind
                    currentMusic = this@currentItemChanged
                    changMusic = pathAudio
                    updateFromCreate()
                }
                afterPrepare()
                updatePlayerViews(ind, pathAudio.toStr, true)
                displayPlay()
                justCoroutine {
                    if (isFromCreate == true) {
                        songPlayer.pauseSus()
                    } else {
                        songPlayer.playSong()
                    }
                }
            }
            //}
            if (reMode == REPEAT_ONE_THREE || reMode == REPEAT_ONE_FIVE) {
                numberOfRepeats.runForCheckRepeater.rKTSack(400L).postAfter()
            }
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        playPauseChanged(isPlaying)
    }

    private suspend fun afterPrepare() {
        justCoroutine {
            backProcessISDone = true
        }
        removeForLoad()
    }

    private fun updateFromCreate() {
        if (isFromCreate == true) {
            toCatchSackAfter(936, 2000L) {
                isFromCreate = false
            }.postAfter()
        }
    }

    private inline val Int.runForCheckRepeater: DSackT<() -> Unit, Int>
        get() = toCatchSack(523) {
            launchDef {
                forCheckRepeater()
            }
            Unit
        }

    private suspend fun Int.forCheckRepeater() {
        withMain {
            isError = false
            @Suppress("KotlinConstantConditions")
            when (this@forCheckRepeater) {
                0 -> {
                    numberOfRepeats = if (reMode == REPEAT_ONE_THREE) 2 else 4
                    songPlayer.repeatMode = androidx.media3.common.Player.REPEAT_MODE_ONE
                }
                1 -> {
                    numberOfRepeats -= 1
                    songPlayer.apply {
                        repeatMode = androidx.media3.common.Player.REPEAT_MODE_ALL
                    }
                }
                else -> {
                    numberOfRepeats -= 1
                }
            }
        }
    }

    private fun playPauseChanged(isPlaying: Boolean) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                playPause(isPlaying)
            }
        }
    }

    private suspend fun playPause(isPlaying: Boolean) {
        withDefault {
            if (isPlaying) {
                playState()
            } else {
                pauseState()
            }
        }
    }

    private suspend fun pauseState() {
        handlePauseState()
        context.nullChecker()
        displayPause()
        isError = false
    }

    private suspend fun handlePauseState() {
        withMain {
            song?.showNotification(
                DSack(one = false, two = isInShow, three = isCarMode)
            )
            MusicObject.activityListener?.apply {
                updatePlayPause(false)
            }
        }
    }

    private suspend fun displayPause() {
        context.nullChecker()
        withMain {
            context?.compactImage(com.pt.pro.R.drawable.ic_play_song) {
                headMusic.playMusic.setImageDrawable(this)
            }
        }
        context.nullChecker()
        withMain {
            unPost(onSecondEvery.two)
            if (isInShow) {
                musicAnimation?.onPauseAnimation()
            }
        }
    }


    private suspend fun playState() {
        handlePlayState()
        context.nullChecker()
        displayPlay()
        isError = false
    }

    private suspend fun displayPlay() {
        withMain {
            onSecondEvery.rKTSack(100L).postAfter()
            context?.compactImage(com.pt.pro.R.drawable.ic_pause_song) {
                headMusic.playMusic.setImageDrawable(this)
            }
            if (needToWrap && isSmall) {
                (rootMain ?: return@withMain).circleCard.spinForLast()
            } else if (!isSmall) {
                musicAnimation?.requestContinue()
            }
        }
    }

    private suspend fun handlePlayState() {
        withMain {
            song?.showNotification(
                DSack(one = true, two = isInShow, three = isCarMode)
            )
            MusicObject.activityListener?.apply {
                updatePlayPause(true)
            }

        }
    }

    private suspend fun androidx.core.animation.ObjectAnimator.onPauseAnimation() {
        withMain {
            runCatching {
                if (!isSmall)
                    pause()
                else
                    end()
            }.onFailure {
                it.toStr.logProvCrash("AIO")
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun MusicSack.onPlayItemCLick() {
        allMusic.indexOfFirst {
            it.pathAudio == this@onPlayItemCLick.pathAudio
        }.let {
            if (it != -1) {
                songPlayer.pushMusic(it)
            } else {
                return
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun onDetailsClick(
        filterFromAlbum: String?,
        filterFromArtist: String?,
        artist: String?,
    ) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                catoFilter(
                    IS_ALL_SONGS,
                    filterFromAlbum = filterFromAlbum,
                    filterFromArtist = filterFromArtist,
                    artist = artist
                )
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun onPlaylist(playlistBelong: String?, pos: Int) {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                ctxM.newDBPlaylist {
                    getAllSongs(playlistBelong.toString())
                }.applySusBack {
                    onEachSusBack(context) {
                        songType = IS_YOUR_PLAYLIST
                    }.pushNewSongs(if (pos > this@applySusBack.lastIndex) 0 else pos, true)
                }
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    override fun MusicSack.onSongItemCLick(add: Boolean, run: () -> Unit) {
        if (allMusic.isEmpty()) {
            mutableListOf(
                this@onSongItemCLick.copy(songType = IS_YOUR_PLAYLIST)
            ).pushNewSongs(0, false)
            run.invoke()
            return
        }
        if (add) {
            pushJob { j ->
                launchDef {
                    j?.checkIfDone()
                    val s = justCoroutine { allMusic.size }
                    justCoroutine {
                        allMusic.add(this@onSongItemCLick.copy(songType = IS_YOUR_PLAYLIST))
                        allMusicOriginal.add(this@onSongItemCLick.copy(songType = IS_YOUR_PLAYLIST))
                    }
                    pathAudio.toStr.newItem(s, title) {
                        allItemMusic.add(this@newItem)
                        songPlayer.addNewItem(this@newItem)
                        withMain {
                            run.invoke()
                        }
                    }
                }
            }
        } else {
            pushJob { j ->
                launchDef {
                    j?.checkIfDone()
                    withBack {
                        allMusic.indexOfFirst {
                            it.pathAudio == this@onSongItemCLick.pathAudio
                        }.let { i ->
                            if (allMusic.size > 1) {
                                withBack {
                                    allItemMusic.removeAtIndex(i)
                                    allMusic.removeAtIndex(i)
                                    allMusicOriginal.removeAtIndex(i)
                                }
                                songPlayer.removeItemFromPlayList(i)
                                justCoroutine {
                                    if (i == currPos) {
                                        if (currPos == allMusic.size) {
                                            songPlayer.pushMusic(0)
                                        } else {
                                            songPlayer.pushMusic(currPos)
                                        }
                                    }
                                }
                                withMain {
                                    run.invoke()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override suspend fun androidx.media3.exoplayer.ExoPlayer.addNewItem(
        ite: androidx.media3.common.MediaItem
    ) {
        withMain {
            addMediaItem(ite)
        }
    }

    private suspend fun androidx.media3.exoplayer.ExoPlayer.removeItemFromPlayList(i: Int) {
        withMain {
            removeMediaItem(i)
        }
    }

    private suspend fun androidx.media3.exoplayer.ExoPlayer.moveItemFromPlayList(i: Int, i2: Int) {
        withMain {
            moveMediaItem(i, i2)
        }
    }

    override suspend fun applyPlayOrPause(isPlay: Boolean, changeMode: Boolean) {
        if (!isError) {
            applyPlayAndPause(isPlay)
        } else {
            ctxM.makeToastRecSus(com.pt.pro.R.string.uy, 0)
        }
    }

    private suspend fun applyPlayAndPause(isPlay: Boolean) {
        withMain {
            if (isPlay) {
                lastPauseTime = System.currentTimeMillis()
                songPlayer.pauseSus()
            } else {
                songPlayer.playSong()
            }
        }
    }

    override suspend fun applyPreviousSong(currentPos: Int) {
        withMain {
            isFromCreate = false
            if (songPlayer.currentPosition > 4000L) {
                songPlayer.applyPrevious()
            } else {
                applyPrev(currentPos)
            }
        }
    }

    private suspend fun applyPrev(currentPos: Int) {
        withMain {
            if (!isSmall) musicAnimation?.requestContinue() else return@withMain
        }
        justScope {
            allMusic.getINull(currentPos)?.let {
                currentMusic = it
                if (reMode == REPEAT_SHUFFLE) {
                    songPlayer.skipPosForShuffle(false)
                } else {
                    songPlayer.pushMusic(currentPos.skipPrevPos)
                }
            }
        }
    }

    private suspend fun androidx.media3.common.Player.applyPrevious() {
        withMain {
            seekTo(1)
        }
        playSong()
    }

    private fun resetRepeater() {
        if (reMode == REPEAT_ONE_THREE || reMode == REPEAT_ONE_FIVE) {
            numberOfRepeats = if (reMode == REPEAT_ONE_THREE) 3 else 5
            songPlayer.repeatMode = androidx.media3.common.Player.REPEAT_MODE_ONE
        }
    }

    override suspend fun applyNextMusic(currentPos: Int) {
        withMain {
            if (!isSmall) musicAnimation?.requestContinue() else return@withMain
        }
        withMain {
            isFromCreate = false
            allMusic.getINull(currentPos)?.let {
                currentMusic = it
                if (reMode == REPEAT_SHUFFLE) {
                    songPlayer.skipPosForShuffle(true)
                } else {
                    songPlayer.pushMusic(currentPos.skipNextPos)
                }
            }
        }
    }


    override fun MutableList<MusicSack>.pushNewSongs(pos: Int, displayAfter: Boolean) {
        if (this@pushNewSongs.isEmpty()) return
        pushJob { j ->
            launchImdMain {
                j?.checkIfDone()
                pushSongs(pos, displayAfter)
                justCoroutine {
                    allMusic.intiPlayer(pos, 1L)
                }
            }
        }
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                goIf(displayAfter) {
                    displayForLoad()
                }
                displayNewSongs(displayAfter)
                context.nullChecker()
                loadAllMusic()
            }
        }
    }

    private suspend fun MutableList<MusicSack>.pushSongs(pos: Int, displayAfter: Boolean) {
        withMain {
            songPlayer.stop()
        }
        withMain {
            songPlayer.clearMediaItems()
        }
        withMain {
            isFromCreate = false
            currPos = pos
            changMusic = null
            mainProcessISDone = false
            intiSeekSong = 1L
            backProcessISDone = false
            allMusicOriNative = this@pushSongs

            isTouchActive = true
            if (displayAfter) {
                whatSongMode = IS_YOUR_PLAYLIST
                songColors = songColors.copy(three = IS_YOUR_PLAYLIST)
            }
            allMusicNative = this@pushSongs.toMutableList()
            currentMusic = allMusic.getI(pos)
            if (reMode == REPEAT_ONE_THREE) {
                numberOfRepeats = 3
            } else if (reMode == REPEAT_ONE_FIVE) {
                numberOfRepeats = 5
            }
        }
    }

    private suspend fun MutableList<MusicSack>.displayNewSongs(displayAfter: Boolean) {
        withMain {
            if (needToWrap && !songPlayer.isPlaying && isSmall) {
                headPlayer.circleCard.spinForLast()
            } else if (!isSmall) {
                musicAnimation?.requestContinue()
            }
            headMusic.musicArtist.text = allMusic.getI(currPos).artist
        }
        context.nullChecker()
        inti(false)
        setOrgAllSongs(currPos)
        context.nullChecker()
        goIf(displayAfter) {
            withMain {
                if (layoutRec != null && layoutRec?.cardFrame?.tag == true) {
                    layoutRec?.applySus {
                        recyclerSongs.clearRecyclerPool()
                        savePlaylist.justVisibleSus()
                    }
                    musicAdapter.applySus {
                        currentPath = currentMusic?.pathAudio.toStr
                        allMusic.updateSongs()
                    }
                    scrollRec(currentMusic?.pathAudio)
                }
                catoFilter(IS_YOUR_PLAYLIST, null, null, null)
            }
        }
    }

    private suspend fun MusicSack.displayNewDuration(isFailed: Boolean) {
        context.nullChecker()
        withBackDef(this@displayNewDuration) {
            if (dur_NSongs != Long.MAX_VALUE || dur_NSongs != 0L) {
                this@displayNewDuration
            } else {
                (context ?: return@withBackDef this@displayNewDuration).getMusicDuration(
                    pathAudio.toStr
                ).letSusBack {
                    copy(dur_NSongs = it)
                }
            }
        }.letSus { newForDur ->
            context.nullChecker()
            withMain {
                song = newForDur
                newForDur.showNotification(
                    DSack(
                        one = mediaPlay?.isPlayingSus?.invoke() ?: return@withMain,
                        two = isInShowSus(),
                        three = isCarMode
                    )
                )
            }
            newForDur.newCor(isFailed)
        }
    }

    private suspend fun MusicSack.newCor(isFailed: Boolean) {
        showUpdateViews(isFailed)
        withMain {
            progressRatio = (dur_NSongs / 350).toInt()
            songDuration = dur_NSongs - progressRatio
            rootMain?.pro?.apply {
                setProgress(0F)
                setMax(dur_NSongs.toInt())
            }
        }
    }

    private suspend fun androidx.media3.common.Player.playSong() {
        withMain {
            if (!isPlaying) play()
        }
    }


    @com.pt.common.global.UiAnn
    override suspend fun MutableList<MusicSack>.doPlaylistSave() {
        withMain {
            stubPlaylist.alsoSus {
                it?.saveListView(this@doPlaylistSave)
                    ?: com.pt.pro.musical.fasten.MusicInflater.runSus {
                        ctxM.inflaterStubSavePlaylist().also { new ->
                            layoutRec?.stubPlaylistFrame?.addView(new.root_)
                            stubPlaylist = new
                        }
                    }.applySus {
                        saveListView(this@doPlaylistSave)
                    }
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MusicSack>.insertPlaylist() {
        justCoroutine {
            MusicObject.musicList = this@insertPlaylist.toMutableList()
        }
        justCoroutine {
            context?.sendWork(currPos, 0L, currentMusic?.pathAudio, com.pt.pro.musical.back.MusicIntiWorker::class.java)
        }
    }


    override suspend fun catoFilter(
        newWhatMode: Int,
        filterFromAlbum: String?,
        filterFromArtist: String?,
        artist: String?,
    ) {
        withMain {
            layoutRec?.searchMusic?.hideInputMethod()
        }
        whichAll(
            whatMode = newWhatMode,
            filterFromAlbum = filterFromAlbum,
            filterFromArtist = filterFromArtist,
            artist = artist,
        )
        context.nullChecker()
        layoutRec?.updateRecyclerOptions(newWhatMode)
    }


    @com.pt.common.global.WorkerAnn
    private suspend fun whichAll(
        whatMode: Int,
        filterFromArtist: String?,
        filterFromAlbum: String?,
        artist: String?,
    ) {
        withBackDef(mutableListOf()) {
            return@withBackDef when (whatMode) {
                IS_YOUR_PLAYLIST -> {
                    allMusic
                }
                ARTIST_IS -> {
                    contM.allArtistsLoader()
                }
                ALBUM_IS -> {
                    contM.allAlbumsLoader()
                }
                IS_ALL_SONGS -> {
                    ctxM.findBooleanPrefDb(
                        SHOW_VOICE,
                        VOICE_IN,
                        false
                    ).let {
                        return@let when {
                            filterFromArtist != null -> {
                                contM.filterAudioByArtist(filterFromArtist, it, artist)
                            }
                            filterFromAlbum != null -> {
                                contM.filterAudioByAlbum(filterFromAlbum, it, artist)
                            }
                            else -> {
                                contM.allAudioLoader(it)
                            }
                        }
                    }
                }
                ALL_PLAYLIST -> {
                    ctxM.newDBPlaylist {
                        getAllPlaylist()
                    }
                }
                else -> {
                    allMusic
                }
            }
        }.let { newPush ->
            whatSongMode = whatMode
            songColors = songColors.copy(three = whatMode)
            newPush.whichListInSearch()
            context.nullChecker()
            newPush.displayWhich(whatMode)
            context.nullChecker()
            layoutRec?.displayAfterCato(whatMode)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun MutableList<MusicSack>.displayWhich(whatMode: Int) {
        withMain {
            layoutRec?.applySus {
                searchMusic.text?.clear()
                recyclerSongs.clearRecyclerPool()
            }
            musicAda?.applySus {
                if (whatMode == IS_YOUR_PLAYLIST) {
                    currentPath = currentMusic?.pathAudio.toStr
                }
                updateSongs()
                refreshMusicPaths()
            }
            layoutRec?.recyclerSongs?.scheduleLayoutAnimation()
        }
    }

    override suspend fun updatePlayerViews(pos: Int, path: String, isPlay: Boolean) {
        context?.contentResolver?.findOneSong(path).letSusBack { ms ->
            if (ms == null) {
                context?.getMusicWithDuration(path, 500) {
                    updateViewsPlayer(isPlay, it)
                }
            } else {
                context?.getMusicBit(path, com.pt.pro.R.drawable.ic_failed_song, 1) {
                    ms.copy(bitmap = this@getMusicBit).updateViewsPlayer(isPlay, it)
                }
            }
        }
    }

    private suspend fun MusicSack.updateViewsPlayer(isPlay: Boolean, isFailed: Boolean) {
        context.nullChecker()
        displayNewDuration(isFailed)
        context.nullChecker()
        ifNotPlay(isPlay)
        context.nullChecker()
        displayUpdateViews()
    }


    private suspend fun ifNotPlay(play: Boolean) {
        withMain {
            if (!play) {
                rootMain?.circleCard?.clearAnimation()
                if (musicAnimation?.isRunning == true) musicAnimation?.end()
            }
        }
    }

    private suspend fun MusicSack.showUpdateViews(isFailed: Boolean) {
        withMain {
            mediaPlay?.applySus {
                createMetaMedia(currPos)?.build()?.letSus(::setPlaylistMetadata)
            }
        }
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                withBack {
                    context.nullChecker()
                    bitmap.letSusBack { bit ->
                        DSack(
                            ctxM.theme.findAttr(com.pt.pro.R.attr.rmoText),
                            ctxM.theme.findAttr(com.pt.pro.R.attr.rmoBackground),
                            ctxM.theme.findAttr(com.pt.pro.R.attr.rmoGrey),
                        ).letSusBack { def ->
                            if (0 != bit?.width) {
                                bit?.paletteSus(def, ctxM.nightRider).letSusBack {
                                    (it ?: def).changeButtonColor(this@showUpdateViews, isFailed)
                                }
                            } else {
                                def.changeButtonColor(this@showUpdateViews, isFailed)
                            }
                        }
                    }
                }
            }
        }
    }


    private suspend fun MusicSack.displayUpdateViews() {
        withMain {
            rootMain?.circleMusic?.apply {
                setImageBitmap(bitmap)
                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
            }
            rootDetails?.apply {
                musicArtist.text = artist
                musicTitle.text = title
            }
            if (layoutRec != null && whatSongMode == IS_YOUR_PLAYLIST) {
                musicAda?.apply {
                    currentMusic?.pathAudio?.let { updateCurrentView(it) }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun androidx.core.animation.ObjectAnimator.requestContinue() {
        withMain {
            if (isStarted) resume() else start()
        }
    }

    @com.pt.common.global.UiAnn
    override fun androidx.appcompat.widget.AppCompatEditText.searchProvide() {
        floatingWindowMangerSearch {
            x = fetchParams.x
            y = fetchParams.y
            context.catchyBadToken {
                windowManagerMusic.updateViewLayout(headMusic.root_, this)
            }
        }
        toCatchSackAfter(66, 100L) {
            runCatching {
                ctxM.launchKeyBoard {
                    requestFocus()
                    showSoftInput(this@searchProvide, 2)
                }
            }
            Unit
        }.postAfter()
    }

    @com.pt.common.global.UiAnn
    override fun androidx.appcompat.widget.AppCompatEditText.hideInputMethod() {
        runCatching {
            ctxM.launchKeyBoard {
                if (isAcceptingText) {
                    hideSoftInputFromWindow(windowToken, 0)
                    context.catchyBadToken {
                        windowManagerMusic.updateViewLayout(headMusic.root_, fetchParams)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun MusicRecyclerHeadFasten.updateRecyclerOptions(
        newWhatMode: Int,
    ) {
        withMain {
            if (newWhatMode == IS_YOUR_PLAYLIST) {
                isTouchActive = true
                scrollRec(currentMusic?.pathAudio)
            } else {
                isTouchActive = false
                scrollRec(null)
            }
            songColors = songColors.copy(three = newWhatMode)
            reColorItem(songColors)
        }
    }

    override suspend fun circleExtend() {
        pushJob {
            launchImdMain {
                withMain {
                    changePositionExtend()
                    rootDetails?.reColorDetails(songColors)
                    rootDetails?.frameChangeExtend()
                    context.nullChecker()
                    rootMain?.springExtend()
                }
            }
        }
    }

    private suspend fun changePositionExtend() {
        justScope {
            musicDim { _, sh ->
                headMusic.root_.fetchViewDimensions.height.let { one ->
                    val c = if (recM.isLandTraditional) 100F.toPixelM else 120F.toPixelM
                    if (one < c) {
                        ((sh / 2) - (recM.statusBarHeight + c)).let {
                            fetchParams.y = -it
                        }
                        context.catchyBadToken {
                            windowManagerMusic.updateViewLayout(headPlayer.root_, fetchParams)
                        }
                    } else if ((sh - one) < 125F.toPixelM) {
                        ((sh / 2) - (recM.navigationBarHeight + c)).let {
                            fetchParams.y = it
                        }
                        context.catchyBadToken {
                            windowManagerMusic.updateViewLayout(headPlayer.root_, fetchParams)
                        }
                    }
                }
            }
        }
    }

    private suspend fun MusicHeadFasten.springExtend() {
        justScope {
            isSmall = false
            circleFrame.framePara(160F.toPixelM, 160F.toPixelM) {}
        }
        circleCard.getSpringAnimation(0, 2F)
        circleCard.getSpringAnimation(1, 2F)
        pro.getSpringAnimation(0, 2F)
        pro.getSpringAnimation(1, 2F)
        justScope {
            if (songPlayer.isPlaying) {
                musicAnimation?.start()
            } else return@justScope
        }
    }

    private suspend fun MusicHeadPlayerFasten.frameChangeExtend() {
        justCoroutine {
            musicArtist.isSelected = true
            musicTitle.isSelected = true
        }
        justCoroutine {
            frameDetails.applySus {
                if (fetchParams.x > 0) {
                    android.view.Gravity.END
                } else {
                    android.view.Gravity.START
                }.let { g ->
                    framePara(
                        210F.toPixelM,
                        270F.toPixelM,
                    ) {
                        gravity = g
                    }
                }
                tag = true

            }
            frameDetails.visibleFadeSus(300L)
        }
    }

    override suspend fun circleShrink(force: Boolean) {
        pushJob {
            launchImdMain {
                withMain {
                    rootDetails?.frameChangeShrink()
                    rootMain?.springShrink(force = force)
                    clearAdapterForShrink()
                }
            }
        }
    }

    private suspend fun clearAdapterForShrink() {
        justScope {
            headMusic.apply {
                musicArtist.isSelected = false
                musicTitle.isSelected = false
            }
            musicAda?.justClear()
        }
    }

    private suspend fun MusicHeadPlayerFasten.frameChangeShrink() {
        justScope {
            frameDetails.applySus {
                this@applySus.framePara(-2, -2) {
                    gravity = if (fetchParams.x > 0) {
                        android.view.Gravity.END
                    } else {
                        android.view.Gravity.START
                    }
                }
                this@applySus.tag = false
            }
            frameDetails.goneFadeSus(200L)
        }
    }

    private suspend fun MusicHeadFasten.springShrink(force: Boolean) {
        justScope {
            isSmall = true
            musicAnimation?.pause()
        }
        circleCard.getSpringAnimation(0, 1F)
        circleCard.getSpringAnimation(1, 1F)
        pro.getSpringAnimation(0, 1F)
        pro.getSpringAnimation(1, 1F)
        justScope {
            circleFrame.framePara(82F.toPixelM, 82F.toPixelM) { }
            circleCard.clearAnimation()
            if (songPlayer.isPlaying || force) {
                circleCard.spinForLast()
            }
        }
    }


    @com.pt.common.global.UiAnn
    override suspend fun returnToCircle() {
        withMain {
            layoutRec?.applySus {
                if (searchMusic.isVis) {
                    hideSearch()
                    deletePlaylist.justVisibleSus()
                    savePlaylist.justVisibleSus()
                } else {
                    if (layoutDetails.isGon) {
                        stubPlaylist?.editCon?.goneFadeSus(250L)
                        stubPlaylist?.playlistEdit?.hideInputMethod()
                        layoutDetails.visibleFadeSus(250L)
                    } else {
                        doToCircle()
                    }
                }
            }
        }
    }


    @com.pt.common.global.UiAnn
    protected suspend fun doToCircle() {
        baseCato()
        context.nullChecker()
        withMain {
            if (songPlayer.isPlaying) {
                musicAnimation?.requestContinue()
            }
            headPlayer.applySus {
                circleFrame.visibleFadeSus(200L)
            }
            headMusic.applySus {
                frameDetails.alsoSus {
                    it.tag = true
                    it.visibleFadeSus(200L)
                }
                reColorDetails(songColors)
            }
        }
    }

    override suspend fun clearPlayList() {
        withMainNormal {
            justCoroutineMain {
                changMusic = null
                allMusic.clear()
                allMusicOriginal.clear()
                musicAdapter.removeSongs()
                rootMain?.circleMusic?.setImageDrawable(null)
                mainProcessISDone = true
                backProcessISDone = true
                isProgressLoading = false
            }
            justCoroutineMain {
                songPlayer.stop()
            }
            justCoroutineMain {
                songPlayer.clearMediaItems()
            }
            justCoroutineMain {
                layoutRec?.savePlaylist?.justInvisibleSus()
                (rootMain ?: return@justCoroutineMain).pro.applySus {
                    setColor(android.graphics.Color.DKGRAY)
                }
            }
            justCoroutineMain {
                isInShowSus().letSus {
                    song?.showNotification(
                        DSack(one = false, two = it, three = isCarMode),
                        ifForCLear = true
                    )
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun doUp(landScape: Boolean) {
        musicDim { wid, _ -> doUpWithDim(landScape, wid) }
    }

    private suspend fun doUpWithDim(landScape: Boolean, wid: Int) {
        if (fetchParams.x >= 0) {
            context.nullChecker()
            withMain {
                if (!landScape)
                    xBefore = wid / 2
                else
                    landXBefore = wid / 2

                animationOpen(fetchParams.x, wid / 2)
                headMusic.apply {
                    changeFramesForEnd(isRight)
                }
            }
        } else {
            context.nullChecker()
            withMain {
                if (!landScape)
                    xBefore = -(wid / 2)
                else
                    landXBefore = -(wid / 2)

                animationOpen(fetchParams.x, -(wid / 2))
                headMusic.apply {
                    changeFramesForStart(isRight)
                }
            }
        }
        context.nullChecker()
        withMain {
            if (!landScape) yBefore = fetchParams.y else landYBefore = fetchParams.y
        }
    }

    override suspend fun onServiceDestroySus(b: () -> Unit) {
        justCoroutine {
            stateDestroy()
            moveCall = null
            touchListener = null
            params = null
            allItemNative = null
        }
        context?.releaseMedia(b)
    }

    @android.annotation.SuppressLint("MissingPermission")
    private suspend fun android.content.Context?.releaseMedia(b: () -> Unit) {
        withMain {
            justScope {
                catchySus(Unit) {
                    mediaPlay?.sessionID?.let {
                        this@releaseMedia?.closeAudioEffectSession(
                            it,
                            com.pt.pro.BuildConfig.APPLICATION_ID
                        )
                    }
                }
            }
            justScope {
                try {
                    mediaSessionNative?.release()
                } catch (_: Exception) {
                }
            }
            mediaPlay?.applySus {
                justScope {
                    removeListener(this@MusicPlayerManager)
                }
                justScope {
                    stop()
                }
                justScope {
                    clearMediaItems()
                }
                justScope {
                    release()
                }
            }
        }
        this@releaseMedia?.onDestroyGlobalSus(isInShow, b)
    }

    override fun onServiceDestroy(b: () -> Unit) {
        context?.onDestroyService(b)
    }

    private fun android.content.Context?.onDestroyService(b: () -> Unit) {
        stateDestroy()
        moveCall = null
        touchListener = null
        params = null
        allItemNative = null
        catchy(Unit) {
            mediaPlay?.sessionID?.let {
                this@onDestroyService?.closeAudioEffectSession(
                    it,
                    com.pt.pro.BuildConfig.APPLICATION_ID
                )
            }
        }
        catchy(Unit) {
            try {
                mediaSessionNative?.release()
            } catch (_: Exception) {

            }
        }
        mediaPlay?.apply {
            synchronized(this) {
                catchy(Unit) {
                    removeListener(this@MusicPlayerManager)
                }
                catchy(Unit) {
                    stop()
                }
                catchy(Unit) {
                    clearMediaItems()
                }
                catchy(Unit) {
                    release()
                }
            }
        }
        this@onDestroyService?.onDestroyGlobal(isInShow, b)
    }

}

