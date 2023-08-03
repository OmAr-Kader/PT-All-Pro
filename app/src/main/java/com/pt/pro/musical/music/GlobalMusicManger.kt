package com.pt.pro.musical.music

import androidx.core.widget.doAfterTextChanged
import com.pt.common.global.*
import com.pt.common.media.musicIndex
import com.pt.common.stable.*
import kotlin.math.abs
import kotlin.math.atan2

abstract class GlobalMusicManger(
    context: android.content.Context?,
    ctxMe: android.content.Context?,
) : ParentMusicManger(context, ctxMe), android.view.View.OnLongClickListener,
    android.view.View.OnClickListener {

    internal inline val isInShow: Boolean get() = rootMain?.root_?.parent != null

    internal inline val isInShowSus: suspend () -> Boolean
        get() = {
            withMainDef(false) {
                rootMain?.root_?.parent != null
            }
        }

    private var beforeYouGo: Int = 0
    internal var xBefore: Int? = null
    internal var yBefore: Int? = null
    internal var landXBefore: Int? = null
    internal var landYBefore: Int? = null


    protected suspend fun initViews() {
        justCoroutine {
            rootDetails?.frameDetails?.applySus {
                tag = false
                justGoneSus()
            }
            headPlayer.pro.setMin(0)
            headPlayer.circleMusic.apply {
                touchListener?.setContactListener(true)
            }
            headMusic.applySus {
                ctxM.compactImage(
                    if (isRight)
                        com.pt.pro.R.drawable.ic_next_song
                    else
                        com.pt.pro.R.drawable.ic_previous_song
                ) {
                    previousMusic.setImageDrawable(this)
                }
                ctxM.compactImage(
                    if (isRight)
                        com.pt.pro.R.drawable.ic_previous_song
                    else
                        com.pt.pro.R.drawable.ic_next_song
                ) {
                    nextMusic.setImageDrawable(this)
                }
                nextMusic.setOnClickListener(this@GlobalMusicManger)
                playMusic.setOnClickListener(this@GlobalMusicManger)
                previousMusic.setOnClickListener(this@GlobalMusicManger)
                playList.setOnClickListener(this@GlobalMusicManger)
                refiner.setOnClickListener(this@GlobalMusicManger)
                hideCircle.setOnClickListener(this@GlobalMusicManger)
                changeFramesForStart(isRight)
            }
        }
        context.nullChecker()
        withMain {
            if (musicAda != null) {
                musicAda?.apply {
                    justNotify()
                }
            }
        }
    }


    protected fun intiRecView() {
        pushJob { j ->
            launchImdMain {
                j?.checkIfDone()
                addRecView()
            }
        }
    }

    private suspend fun addRecView() {
        withMain {
            com.pt.pro.musical.fasten.MusicInflater.run {
                ctxM.inflaterMusicRecycler()
            }.also {
                @ViewAnn
                layoutRec = it
            }.applySus {
                addRecyclerView(ctxM.findIntegerPreference(KEY_EXTEND, 0))
            }.alsoSus {
                headMusic.stubRecycler.addView(it.root_)
            }
        }
    }

    private suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.addRecyclerView(
        recyclerMode: Int,
    ) {
        withMain {
            justScope {
                musicDim { wid, hei ->
                    displayExtendOrShrink(
                        mode = recyclerMode,
                        isLand = recM.isLandTraditional,
                        act = ctxM.actionBarHeight,
                        wid = wid,
                        hei = hei,
                    )
                }
            }
            recyclerSongs.applySus {
                layoutAnimation = ctxM.fallDownAnimationRec(
                    dur = 200L,
                    del = 0.15F,
                )
                ctxM.getVerManager.also {
                    recyclerManager = it
                    layoutManager = it
                }
                moveCall?.also {
                    androidx.recyclerview.widget.ItemTouchHelper(it).attachToRecyclerView(this)
                }
                MusicAdapter(
                    mutableListOf(),
                    mutableListOf(),
                    currentPath = currentMusic?.pathAudio.toStr,
                    isRTL = recM.isRightToLeft,
                    picListener = this@GlobalMusicManger,
                ).also { ma ->
                    musicAda = ma
                    adapter = ma
                }
            }
            searchMusic.applySus {
                this@GlobalMusicManger.setOnEditTextImeBackListener()
            }
            cardFrame.applySus {
                tag = true
                visibleFadeSus(200L)
            }
            justScope {
                returnMusic.setOnClickListener(this@GlobalMusicManger)
                myPlaylist.setOnClickListener(this@GlobalMusicManger)
                showArtists.setOnClickListener(this@GlobalMusicManger)
                showAlbums.setOnClickListener(this@GlobalMusicManger)
                showAllMusic.setOnClickListener(this@GlobalMusicManger)
                savePlaylist.setOnClickListener(this@GlobalMusicManger)
                extendButton.setOnClickListener(this@GlobalMusicManger)
                searchButton.setOnClickListener(this@GlobalMusicManger)
                deletePlaylist.setOnClickListener(this@GlobalMusicManger)
                showAllList.setOnClickListener(this@GlobalMusicManger)
                myPlaylist.setOnLongClickListener(this@GlobalMusicManger)
                showArtists.setOnLongClickListener(this@GlobalMusicManger)
                showAlbums.setOnLongClickListener(this@GlobalMusicManger)
                showAllMusic.setOnLongClickListener(this@GlobalMusicManger)
                showAllList.setOnLongClickListener(this@GlobalMusicManger)
                savePlaylist.setOnLongClickListener(this@GlobalMusicManger)
                extendButton.setOnLongClickListener(this@GlobalMusicManger)
                deletePlaylist.setOnLongClickListener(this@GlobalMusicManger)
                searchButton.setOnLongClickListener(this@GlobalMusicManger)
            }
            searchMusic.applySus {
                doAfterTextChanged { s ->
                    layoutRec?.apply {
                        forSearch(s)
                    }
                }
                setOnClickListener(this@GlobalMusicManger)
            }
            justScope {
                layoutRec?.applySus {
                    recyclerSongs.clearRecyclerPool()
                }
                musicAdapter.applySus {
                    refreshMusicPaths()
                    allMusic.updateSongs()
                }
                scrollRec(currentMusic?.pathAudio)
            }
            allMusic.whichListInSearch()
            songColors.reColor(isForce = true)
        }
    }

    @com.pt.common.global.UiAnn
    protected suspend fun scrollRec(path: String?) {
        runCatching {
            layoutRec?.recyclerSongs?.scrollToPosition(path?.let { allMusic.musicIndex(it) } ?: 0)
        }
    }

    protected suspend fun MusicAdapter.refreshMusicPaths() {
        withDefault {
            mutableListOf<String>().apply {
                allMusic.onEachSusBack(context) {
                    add(pathAudio.toStr)
                }
            }.updatePaths()
        }
    }

    private fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.forSearch(
        s: android.text.Editable?,
    ) {
        searchJob?.cancelJob()
        searchJob = if (!s.isNullOrEmpty()) {
            launchDef {
                searchNotEmpty(s)
            }
        } else {
            launchMain {
                searchEmpty(searchMusic.isVis)
            }
        }
    }

    private suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.searchEmpty(boolean: Boolean) {
        withMain {
            if (boolean) {
                recyclerSongs.clearRecyclerPool()
                musicAdapter.applySus {
                    searchList.updateSongs()
                }
            } else return@withMain
        }
    }

    private suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.searchNotEmpty(
        s: android.text.Editable,
    ) {
        searchList.toMutableList().asSequence().filterSongs(s).letSus {
            context.nullChecker()
            withMain {
                recyclerSongs.clearRecyclerPool()
                musicAdapter.applySus {
                    if (songList != it) it.updateSongs() else return@applySus
                }
            }
        }
    }


    internal var moveCall: androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback?
        @com.pt.common.global.UiAnn
        get() {
            return object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
                0, 0
            ) {

                override fun isLongPressDragEnabled(): Boolean = isTouchActive

                override fun isItemViewSwipeEnabled(): Boolean = isTouchActive

                override fun getMovementFlags(
                    recyclerView: androidx.recyclerview.widget.RecyclerView,
                    viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                ): Int = if (viewHolder.itemViewType == IS_YOUR_PLAYLIST) makeMovementFlags(drags, swipes)
                else makeMovementFlags(0, 0)

                override fun onMove(
                    recyclerView: androidx.recyclerview.widget.RecyclerView,
                    viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                    target: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                ): Boolean {
                    pushJob { j ->
                        launchMain {
                            j?.checkIfDone()
                            musicAdapter.applySus {
                                val adaPos = viewHolder.absoluteAdapterPosition
                                val adaPosTarget = target.absoluteAdapterPosition
                                allMusic.fetchTruePosition(adaPos, adaPos).alsoSus { pos ->
                                    allMusic.fetchTruePosition(adaPos, adaPosTarget).alsoSus { tarPos ->
                                        doMoveFun(pos, tarPos)
                                        displayMigration(adaPos, adaPosTarget)
                                    }
                                }
                            }
                        }
                    }
                    return true
                }

                private suspend fun displayMigration(adaPos: Int, adaPosTarget: Int) {
                    withMain {
                        runCatching {
                            musicAdapter.apply {
                                currentMusic?.pathAudio?.let {
                                    onItemMove(adaPos = adaPos, adaPosTarget = adaPosTarget, newCurrentPath = it)
                                }
                            }
                        }
                    }
                }

                override fun onSwiped(
                    viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                    direction: Int,
                ) {
                    pushJob {
                        launchMain {
                            musicAdapter.letSus {
                                it.songList.getOrNull(viewHolder.absoluteAdapterPosition)?.pathAudio
                            }?.alsoSus { it1 -> doSwipeDelete(it1) }
                        }
                    }
                }

                private suspend fun doSwipeDelete(path: String) {
                    withMain {
                        if (allMusic.size == 1) {
                            withMain {
                                musicAdapter.notifyItemInserted(0)
                            }
                            return@withMain
                        }
                        doSwipeFun(path)
                    }
                }
            }
        }
        set(value) {
            value.logProvLess()
        }


    protected suspend fun DSack<Int, Int, Int>.changeButtonColor(
        musicSack: MusicSack,
        isFailed: Boolean
    ) {
        withMain {
            songColors = this@changeButtonColor.copy(three = songColors.three)
        }
        context.nullChecker()
        withMain {
            MusicObject.activityListener?.apply {
                songColors.updateMusicActivity(
                    musicSack,
                    mediaPlay?.durationSus?.invoke()?.toFloat() ?: 300000F,
                    isFailed
                )
            }
        }
        context.nullChecker()
        songColors.reColor()
    }

    @com.pt.common.global.MainAnn
    protected suspend fun DSack<Int, Int, Int>.reColor(isForce: Boolean = false) {
        withMain {
            layoutRec?.applySus {
                if (cardFrame.tag == true || isForce) {
                    reColorRec(this@reColor)
                }
            }
            rootDetails?.applySus {
                if (frameDetails.tag == true || isForce) {
                    reColorDetails(this@reColor)
                }
            }
        }
    }

    private suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.reColorRec(
        it: DSack<Int, Int, Int>,
    ) {
        cardFrame.setCardBackgroundColor(it.two)
        reColorItem(it)
        reColorImg(it.one)
        it.updateColorRecyclerItem()
    }

    private suspend fun DSack<Int, Int, Int>.updateColorRecyclerItem() {
        androidx.core.graphics.ColorUtils.blendARGB(one, two, 0.7F).letSus {
            withMain {
                musicAda?.colorItem = it
            }
            withMain {
                if (isSmall) return@withMain
                recyclerManager?.findFirstVisibleItemPosition()?.letSus { first ->
                    recyclerManager?.findLastVisibleItemPosition()?.letSus { last ->
                        musicAda?.applySus {
                            for (i in first..last) {
                                notifyItemChanged(i)
                            }
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected suspend fun com.pt.pro.musical.fasten.MusicHeadPlayerFasten.reColorDetails(
        it: DSack<Int, Int, Int>,
    ) {
        textColor(it)
        reColorBack(it)
        reColor(it)
    }

    @com.pt.common.global.UiAnn
    protected suspend fun com.pt.pro.musical.fasten.MusicHeadPlayerFasten.textColor(
        it: DSack<Int, Int, Int>,
    ) {
        withMain {
            musicArtist.applySus {
                setTextColor(it.one)
            }
            musicTitle.applySus {
                setTextColor(it.one)
            }
            /*layoutRec?.applySus {
                searchMusic.alsoSus { sm ->
                    sm.setTextColor(it.one)
                }
            }*/
        }
    }

    @com.pt.common.global.UiAnn
    protected suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.reColorImg(
        @androidx.annotation.ColorInt one: Int,
    ) {
        returnMusic.svgReColorSus(one)
        savePlaylist.svgReColorSus(one)
        deletePlaylist.svgReColorSus(one)
        searchButton.svgReColorSus(one)
        extendButton.svgReColorSus(one)
    }

    @com.pt.common.global.UiAnn
    protected suspend fun com.pt.pro.musical.fasten.MusicHeadPlayerFasten.reColorBack(
        it: DSack<Int, Int, Int>,
    ) {
        playList.backReColorSus(it.one)
        playMusic.backReColorSus(it.one)
        nextMusic.backReColorSus(it.two)
        refiner.backReColorSus(it.two)
        hideCircle.backReColorSus(it.two)
        previousMusic.backReColorSus(it.two)

        musicDetails.backReColorSus(it.two)
    }

    @com.pt.common.global.UiAnn
    protected suspend fun com.pt.pro.musical.fasten.MusicHeadPlayerFasten.reColor(
        it: DSack<Int, Int, Int>,
    ) {
        playList.svgReColorSus(it.two)
        playMusic.svgReColorSus(it.two)
        nextMusic.svgReColorSus(it.one)
        refiner.svgReColorSus(it.one)
        hideCircle.svgReColorSus(it.one)
        previousMusic.svgReColorSus(it.one)

        musicTitle.setTextColor(it.one)
        musicArtist.setTextColor(it.one)
    }

    protected suspend fun com.pt.pro.musical.fasten.StubSavePlaylistFasten.saveListView(
        list: MutableList<MusicSack>
    ) {
        justScope {
            playlistEdit.searchProvide()
            playlistEdit.setOnClickListener {
                playlistEdit.searchProvide()
            }
            cancelEdit.setOnClickListener {
                editCon.goneFade(250L)
                layoutRec?.apply {
                    stubPlaylistFrame.goneFade(250L)
                    layoutDetails.visibleFade(250L)
                }
                playlistEdit.hideInputMethod()
            }
            saveEdit.setOnClickListener {
                playlistEdit.text.let {
                    if (it.isNullOrEmpty()) {
                        ctxM.makeToastRec(com.pt.pro.R.string.vd, 0)
                    } else {
                        list.doSavePlayList(it.toString())
                        editCon.goneFade(250L)
                        layoutRec?.apply {
                            stubPlaylistFrame.goneFade(250L)
                            layoutDetails.visibleFade(250L)
                        }
                        playlistEdit.hideInputMethod()
                    }
                }
            }
        }
        changeColors(songColors)
        layoutRec?.applySus {
            layoutDetails.goneFadeSus(250L)
            stubPlaylistFrame.visibleFadeSus(250L)
        }
        editCon.visibleFadeSus(250L)
    }


    private suspend fun com.pt.pro.musical.fasten.StubSavePlaylistFasten.changeColors(
        it: DSack<Int, Int, Int>
    ) {
        /*playlistEdit.applySus {
            setTextColor(it.one)
            this@GlobalMusicManger.setOnEditTextImeBackListener()
        }*/
        cancelEdit.alsoSus { pe ->
            pe.setTextColor(it.one)
        }
        saveEdit.alsoSus { pe ->
            pe.setTextColor(it.one)
        }
        editCon.alsoSus { pe ->
            pe.setBackgroundColor(it.two)
        }
    }


    @com.pt.common.global.UiAnn
    protected fun com.pt.pro.musical.fasten.MusicHeadPlayerFasten.changeFramesForEnd(
        isRight: Boolean,
    ) {
        if (isRight) {
            changeFramesForBegin()
        } else {
            changeFramesForFinish()
        }
    }

    @com.pt.common.global.UiAnn
    protected fun com.pt.pro.musical.fasten.MusicHeadPlayerFasten.changeFramesForStart(
        isRight: Boolean,
    ) {
        if (isRight) {
            changeFramesForFinish()
        } else {
            changeFramesForBegin()
        }
    }

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.musical.fasten.MusicHeadPlayerFasten.changeFramesForBegin() {
        playList.framePara(40F.toPixelM, 40F.toPixelM) {
            gravity = android.view.Gravity.START
            marginStart = 10F.toPixelM
        }
        playMusic.framePara(40F.toPixelM, 40F.toPixelM) {
            gravity = android.view.Gravity.START
            marginStart = 80F.toPixelM
        }
        previousMusic.framePara(35F.toPixelM, 35F.toPixelM) {
            gravity = android.view.Gravity.END
            topMargin = 30F.toPixelM
            marginEnd = 30F.toPixelM
        }
        refiner.framePara(35F.toPixelM, 35F.toPixelM) {
            gravity = android.view.Gravity.END
            topMargin = 80F.toPixelM
        }
        hideCircle.framePara(35F.toPixelM, 35F.toPixelM) {
            gravity = android.view.Gravity.END or android.view.Gravity.BOTTOM
            bottomMargin = 80F.toPixelM
        }
        nextMusic.framePara(35F.toPixelM, 35F.toPixelM) {
            gravity = android.view.Gravity.END or android.view.Gravity.BOTTOM
            marginEnd = 30F.toPixelM
            bottomMargin = 30F.toPixelM
        }
        musicDetails.framePara(140F.toPixelM, 45F.toPixelM) {
            gravity = android.view.Gravity.START or android.view.Gravity.BOTTOM
            topMargin = 45F.toPixelM
            marginStart = 5F.toPixelM
        }
    }


    @com.pt.common.global.UiAnn
    override fun onKeyBoardOpened() {
        beforeYouGo = fetchParams.y
        if (fetchParams.y > 50) {
            animationUp(fetchParams.y, -50)
        }
    }

    @com.pt.common.global.UiAnn
    override fun onKeyBoardClosed() {
        if (beforeYouGo != fetchParams.y) {
            animationUp(fetchParams.y, beforeYouGo)
        }
    }

    @com.pt.common.global.UiAnn
    private fun animationUp(beginValue: Int, endValue: Int) {
        returnForSlip(begin = beginValue, end = endValue) {
            duration = 200
            addUpdateListener {
                fetchParams.apply {
                    y = (animatedValue as Int)
                    context?.catchyBadToken {
                        windowManagerMusic.updateViewLayout(
                            rootDetails?.root_ ?: return@catchyBadToken, this
                        )
                        windowManagerMusic.updateViewLayout(
                            rootMain?.root_ ?: return@catchyBadToken,
                            this
                        )
                    }
                }
            }
            start()
        }
    }

    @com.pt.common.global.UiAnn
    private fun animationUpDown(startX: Int, endX: Int, startY: Int, endY: Int) {
        returnAnimForChange(startX, endX, startY, endY) {
            addUpdateListener {
                fetchParams.apply {
                    x = getAnimatedValue(com.pt.common.BuildConfig.X_DIMENSION) as Int
                    y = getAnimatedValue(com.pt.common.BuildConfig.Y_DIMENSION) as Int
                    context?.catchyBadToken {
                        windowManagerMusic.updateViewLayout(
                            rootDetails?.root_ ?: return@catchyBadToken, this
                        )
                        windowManagerMusic.updateViewLayout(
                            rootMain?.root_ ?: return@catchyBadToken,
                            this
                        )
                    }
                }
            }
            duration = 100
            start()
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.doRecolorItems(
        frontMyPlaylist: Int,
        backMyPlaylist: Int,
        frontShowArtists: Int,
        backShowArtists: Int,
        frontShowAlbums: Int,
        backShowAlbums: Int,
        frontShowAllMusic: Int,
        backShowAllMusic: Int,
        frontShowAllList: Int,
        backShowAllList: Int,
    ) {
        myPlaylist.apply {
            svgReColorSus(frontMyPlaylist)
            backReColorSus(backMyPlaylist)
        }
        showArtists.apply {
            svgReColorSus(frontShowArtists)
            backReColorSus(backShowArtists)
        }
        showAlbums.apply {
            svgReColorSus(frontShowAlbums)
            backReColorSus(backShowAlbums)
        }
        showAllMusic.apply {
            svgReColorSus(frontShowAllMusic)
            backReColorSus(backShowAllMusic)
        }
        showAllList.apply {
            svgReColorSus(frontShowAllList)
            backReColorSus(backShowAllList)
        }
    }

    private fun MusicSack.notifyForCar(isCar: Boolean) {
        launchDef {
            showNotification(DSack(songPlayer.isPlayingSus(), isInShowSus(), isCar))
        }
    }

    @com.pt.common.global.MainAnn
    override fun onChange(newConfig: Int) {
        isRight = ctxM.applicationContext.resources.isRightToLeft
        doChange(
            newConfig == android.content.res.Configuration.ORIENTATION_PORTRAIT
        )
        ctxM.isCarUiMode.let {
            isCarMode = it
            song?.notifyForCar(it)
            if (it) {
                allMusic.pushNewSongs(if (currPos > allMusic.lastIndex) 0 else currPos, true)
            }
        }
    }

    private fun doChange(isPort: Boolean) {
        launchDef {
            withMain {
                ctxM.fetchDimensions {
                    screenWidth = this@fetchDimensions.width
                    screenHeight = this@fetchDimensions.height
                    layoutRec?.displayExtendOrShrink(
                        mode = recyclerMode,
                        isLand = !isPort,
                        act = ctxM.actionBarHeight,
                        wid = this@fetchDimensions.width,
                        hei = this@fetchDimensions.height
                    )
                    if (isPort) {
                        doChangePort(this@fetchDimensions.width)
                    } else {
                        doChangeLand(this@fetchDimensions.width)
                    }
                }
            }
        }
    }

    private fun doChangePort(wid: Int) {
        (if (xBefore != null) xBefore else -(wid / 2))?.let { xb ->
            xBefore = xb
            (if (yBefore != null) yBefore else -200)?.let { yb ->
                yBefore = yb
                animationUpDown(fetchParams.x, xb, fetchParams.y, yb)
                headMusic.apply {
                    if (xb == abs(xb))
                        changeFramesForEnd(isRight)
                    else
                        changeFramesForStart(isRight)
                }
            }
        }
    }

    private fun doChangeLand(wid: Int) {
        (if (landXBefore != null) landXBefore else -(wid / 2))?.let { lxb ->
            landXBefore = lxb
            (if (landYBefore != null) landYBefore else -200)?.let { lyb ->
                landYBefore = lyb
                animationUpDown(fetchParams.x, lxb, fetchParams.y, lyb)
                headMusic.apply {
                    if (lxb == abs(lxb))
                        changeFramesForEnd(isRight)
                    else
                        changeFramesForStart(isRight)
                }
            }
        }
    }


    override fun onUpdateService(action: String?) {
        if (mediaPlay != null) {
            pushJob {
                jobPreviousSong?.cancelJob()
                launchDef {
                    when (action) {
                        PAUSE_PLAY -> applyPlayOrPause(
                            isPlay = songPlayer.isPlayingSus(),
                            changeMode = true
                        )
                        EXTEND_MUSIC -> doExtend()
                        HIDDEN_MUSIC -> hiddenProvider()
                        NEXT_MUSIC -> applyNextMusic(currPos)
                        PREVIOUS_MUSIC -> applyPreviousSong(currPos)
                    }
                }.also { jobPreviousSong = it }
            }
        }
    }

    private suspend fun doExtend() {
        isInShowSus().letSus {
            if (it) {
                if (isSmall) {
                    circleExtend()
                    whichPlayList()
                }
            } else {
                showViews()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.musical.fasten.MusicHeadPlayerFasten.changeFramesForFinish() {
        playMusic.framePara(40F.toPixelM, 40F.toPixelM) {
            gravity = android.view.Gravity.END
            marginEnd = 80F.toPixelM
        }
        playList.framePara(40F.toPixelM, 40F.toPixelM) {
            gravity = android.view.Gravity.END
            marginEnd = 10F.toPixelM
        }
        previousMusic.framePara(35F.toPixelM, 35F.toPixelM) {
            gravity = android.view.Gravity.START
            topMargin = 30F.toPixelM
            marginStart = 30F.toPixelM
        }
        refiner.framePara(35F.toPixelM, 35F.toPixelM) {
            gravity = android.view.Gravity.START
            topMargin = 80F.toPixelM
        }
        hideCircle.framePara(35F.toPixelM, 35F.toPixelM) {
            gravity = android.view.Gravity.START or android.view.Gravity.BOTTOM
            bottomMargin = 80F.toPixelM
        }
        nextMusic.framePara(35F.toPixelM, 35F.toPixelM) {
            gravity = android.view.Gravity.START or android.view.Gravity.BOTTOM
            marginStart = 30F.toPixelM
            bottomMargin = 30F.toPixelM
        }
        musicDetails.framePara(140F.toPixelM, 45F.toPixelM) {
            gravity = android.view.Gravity.END or android.view.Gravity.BOTTOM
            topMargin = 45F.toPixelM
            marginStart = 5F.toPixelM
        }
    }

    private suspend fun android.view.View.applyPlay() {
        withMain {
            if (tag == null) tag = "firstClick"
            applyPlayOrPause(isPlay = songPlayer.isPlayingSus(), changeMode = true)
        }
    }

    @com.pt.common.global.UiAnn
    override fun onClick(v: android.view.View?) {
        pushJob {
            if (v == layoutRec?.returnMusic) {
                launchImdMain {
                    returnToCircle()
                }
            } else {
                launchImdMain {
                    v.doOnClick()
                }
            }
        }
    }

    private suspend fun android.view.View?.doOnClick() {
        withMain {
            headMusic.applySus {
                when (this@doOnClick) {
                    playMusic -> this@doOnClick.applyPlay()
                    previousMusic -> applyPreviousSong(currPos)
                    nextMusic -> applyNextMusic(currPos)
                    refiner -> songPlayer.repeater(reMode)
                    hideCircle -> hiddenProvider()
                    playList -> whichPlayList()
                }
            }
            layoutRec?.applySus {
                when (this@doOnClick) {
                    searchButton -> searchDisplay()
                    searchMusic -> searchMusic.searchProvide()
                    myPlaylist -> catoFilter(IS_YOUR_PLAYLIST, null, null, null)
                    showArtists -> catoFilter(ARTIST_IS, null, null, null)
                    showAlbums -> catoFilter(ALBUM_IS, null, null, null)
                    showAllMusic -> catoFilter(IS_ALL_SONGS, null, null, null)
                    showAllList -> catoFilter(ALL_PLAYLIST, null, null, null)
                    savePlaylist -> whichOrderSave(savePlaylist.tag == false)
                    extendButton -> musicDim { wid, hei ->
                        extendOrShrink(recM.isLandTraditional, act = ctxM.actionBarHeight, wid = wid, hei = hei)
                    }
                    deletePlaylist -> whichOrderDelete(deletePlaylist.tag == false)
                }
            }
        }
    }

    private suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.extendOrShrink(
        isLand: Boolean,
        act: Int,
        wid: Int,
        hei: Int,
    ) {
        withMain {
            extendButton.applySus {
                when (recyclerMode) {
                    0 -> {
                        if (isLand) {
                            (kotlin.math.min(hei, 600F.toPixelM) - act).let { h ->
                                cardFrame.framePara(
                                    300F.toPixelM,
                                    h
                                ) {}
                            }
                        } else {
                            cardFrame.framePara(
                                300F.toPixelM,
                                600F.toPixelM
                            ) {}
                        }
                        ctxM.compactImage(com.pt.pro.R.drawable.ic_extend) {
                            setImageDrawable(this@compactImage)
                        }
                        1
                    }
                    1 -> {
                        wid.also { w ->
                            hei.also { dimH ->
                                recM.navigationBarHeight.also { nav ->
                                    if (isLand) {
                                        (dimH - act).also { h ->
                                            cardFrame.framePara(
                                                w - nav - act,
                                                h
                                            ) {}
                                        }
                                    } else {
                                        (dimH - nav - act).also { h ->
                                            cardFrame.framePara(
                                                w,
                                                h
                                            ) {}
                                        }
                                    }
                                }
                            }
                        }
                        ctxM.compactImage(com.pt.pro.R.drawable.ic_shrink) {
                            setImageDrawable(this@compactImage)
                        }
                        2
                    }
                    else -> {
                        if (isLand) {
                            (kotlin.math.min(hei, 400F.toPixelM) - act).let { h ->
                                cardFrame.framePara(
                                    220F.toPixelM,
                                    h
                                ) {}
                            }
                        } else {
                            cardFrame.framePara(
                                220F.toPixelM,
                                400F.toPixelM
                            ) {}
                        }
                        ctxM.compactImage(com.pt.pro.R.drawable.ic_extend) {
                            setImageDrawable(this@compactImage)
                        }
                        0
                    }
                }.let {
                    recyclerMode = it
                    ctxM.updatePrefInt(KEY_EXTEND, it)
                }
            }
        }
    }

    private fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.displayExtendOrShrink(
        mode: Int,
        isLand: Boolean,
        act: Int,
        wid: Int,
        hei: Int
    ) {
        extendButton.apply {
            recyclerMode = mode
            when (mode) {
                0 -> {
                    if (isLand) {
                        (kotlin.math.min(hei, 400F.toPixelM) - act).let { h ->
                            cardFrame.framePara(
                                220F.toPixelM,
                                h
                            ) {}
                        }
                    } else {
                        cardFrame.framePara(
                            220F.toPixelM,
                            400F.toPixelM
                        ) {}
                    }
                    ctxM.compactImage(com.pt.pro.R.drawable.ic_extend) {
                        setImageDrawable(this@compactImage)
                    }
                }
                1 -> {
                    if (isLand) {
                        (kotlin.math.min(hei, 600F.toPixelM) - act).let { h ->
                            cardFrame.framePara(
                                300F.toPixelM,
                                h
                            ) {}
                        }
                    } else {
                        cardFrame.framePara(
                            300F.toPixelM,
                            600F.toPixelM
                        ) {}
                    }
                    ctxM.compactImage(com.pt.pro.R.drawable.ic_extend) {
                        setImageDrawable(this@compactImage)
                    }
                }
                else -> {
                    wid.also { w ->
                        hei.also { dimH ->
                            recM.navigationBarHeight.also { nav ->
                                if (recM.configuration.isTablet) {
                                    (dimH - nav - act).also { h ->
                                        cardFrame.framePara(
                                            w,
                                            h
                                        ) {}
                                    }
                                } else {
                                    if (isLand) {
                                        (dimH - act).also { h ->
                                            cardFrame.framePara(
                                                w - nav - act,
                                                h
                                            ) {}
                                        }
                                    } else {
                                        (dimH - nav - act).also { h ->
                                            cardFrame.framePara(
                                                w,
                                                h
                                            ) {}
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ctxM.compactImage(com.pt.pro.R.drawable.ic_shrink) {
                        setImageDrawable(this@compactImage)
                    }
                }
            }
        }
    }


    private suspend fun whichOrderSave(isFalse: Boolean) {
        if (isFalse) {
            searchList.appendNewSongs()
        } else {
            allMusic.doPlaylistSave()
        }
    }

    private suspend fun MutableList<MusicSack>.appendNewSongs() {
        withDefaultDef(mutableListOf()) {
            onEachSusBack(context) {
                songType = IS_YOUR_PLAYLIST
            }
        }.letSusBack { list ->
            withDefaultDef(mutableListOf()) {
                musicAdapter.pathsList.runSusBack {
                    list.toMutableList().asSequence().filter {
                        !contains(it.pathAudio)
                    }.toMutableList()
                }
            }
        }.toMutableList().alsoSusBack {
            withDefault {
                allMusic.addAll(it.toMutableList())
                allMusicOriginal.addAll(it.toMutableList())
                it.intiAppendPlayerList()
            }
        }
    }

    private suspend fun whichOrderDelete(isFalse: Boolean) {
        if (isFalse) {
            searchList.pushFromCato()
        } else {
            clearPlayList()
        }
    }

    private suspend fun MutableList<MusicSack>.pushFromCato() {
        withDefault {
            onEachSusBack(context) {
                songType = IS_YOUR_PLAYLIST
            }.pushNewSongs(0, true)
        }
    }

    override suspend fun hiddenProvider() {
        isInShowSus().letSus {
            if (it) {
                song?.showNotification(
                    DSack(
                        one = songPlayer.isPlayingSus(),
                        two = false,
                        isCarMode
                    )
                )
                hiddenViews()
            } else {
                showViews()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun showViews() {
        context.nullChecker()
        withMain {
            if (!isInShow) {
                context.catchyBadToken {
                    windowManagerMusic.addView(headMusic.root_, fetchParams)
                    windowManagerMusic.addView(headPlayer.root_, fetchParams)
                }
            }
        }
        context.nullChecker()
        updatePlayerViews(currPos, allMusic.getI(currPos).pathAudio.toStr, false)
    }

    @com.pt.common.global.UiAnn
    private suspend fun hiddenViews() {
        circleShrink(true)
        context.nullChecker()
        withMain {
            context.catchyBadToken {
                windowManagerMusic.removeViewImmediate(headPlayer.root_)
                windowManagerMusic.removeViewImmediate(headMusic.root_)
            }
        }
    }


    protected suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.hideSearch() {
        withMain {
            searchMusic.applySus {
                if (isVis) {
                    text?.clear()
                    justGoneSus()
                    hideInputMethod()
                    searchButton.justVisibleSus()
                }
            }
        }
    }

    private suspend inline fun androidx.media3.common.Player.repeater(repeatMode: Int) {
        withBackDef(
            DSack(
                androidx.media3.common.Player.REPEAT_MODE_ALL,
                false,
                REPEAT_ALL
            )
        ) {
            unPost(523)
            when (repeatMode) {
                REPEAT_ALL -> {
                    DSack(
                        androidx.media3.common.Player.REPEAT_MODE_OFF,
                        false,
                        REPEAT_OFF
                    )
                }
                REPEAT_OFF -> {
                    DSack(
                        androidx.media3.common.Player.REPEAT_MODE_ONE,
                        false,
                        REPEAT_ONE
                    )
                }
                REPEAT_ONE -> {
                    numberOfRepeats = 3
                    DSack(
                        androidx.media3.common.Player.REPEAT_MODE_ONE,
                        false,
                        REPEAT_ONE_THREE
                    )
                }
                REPEAT_ONE_THREE -> {
                    numberOfRepeats = 5
                    DSack(
                        androidx.media3.common.Player.REPEAT_MODE_ONE,
                        false,
                        REPEAT_ONE_FIVE
                    )
                }
                REPEAT_ONE_FIVE -> {
                    DSack(
                        androidx.media3.common.Player.REPEAT_MODE_ALL,
                        true,
                        REPEAT_SHUFFLE
                    )
                }
                REPEAT_SHUFFLE -> {
                    DSack(
                        androidx.media3.common.Player.REPEAT_MODE_ALL,
                        false,
                        REPEAT_ALL
                    )
                }
                else -> {
                    DSack(
                        androidx.media3.common.Player.REPEAT_MODE_ALL,
                        false,
                        REPEAT_ALL
                    )
                }
            }
        }.letSus {
            withMain {
                shuffleModeEnabled = it.two
                setRepeatMode(it.one)
                updateRepeatMode(it.three)
                displayRepeater(it.three)
            }
        }
    }

    private suspend fun updateRepeatMode(three: Int) {
        withDefault {
            reMode = three
            ctxM.updatePrefInt(REPEAT_KEY, three)
        }
    }

    protected suspend fun displayRepeater(
        reMode: Int,
    ) {
        justCoroutine {
            when (reMode) {
                REPEAT_OFF -> com.pt.pro.R.drawable.ic_repeat_off
                REPEAT_ONE -> com.pt.pro.R.drawable.ic_repeat_one
                REPEAT_ONE_THREE -> com.pt.pro.R.drawable.ic_repeat_three
                REPEAT_ONE_FIVE -> com.pt.pro.R.drawable.ic_repeat_five
                REPEAT_SHUFFLE -> com.pt.pro.R.drawable.ic_shuffle
                REPEAT_ALL -> com.pt.pro.R.drawable.ic_repeat_on
                else -> com.pt.pro.R.drawable.ic_repeat_on
            }.letSus {
                ctxM.compactImage(it) {
                    headMusic.refiner.setImageDrawable(this)
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    protected fun animationOpen(beginValue: Int, endValue: Int) {
        returnForSlip(begin = beginValue, end = endValue) {
            duration = 300
            addUpdateListener {
                fetchParams.apply {
                    x = (animatedValue as Int)
                    context?.catchyBadToken {
                        windowManagerMusic.updateViewLayout(
                            rootDetails?.root_ ?: return@catchyBadToken, this
                        )
                        windowManagerMusic.updateViewLayout(
                            rootMain?.root_ ?: return@catchyBadToken,
                            this
                        )
                    }
                }
            }
            start()
        }
    }

    private suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.searchDisplay() {
        withMain {
            if (searchMusic.isVis) {
                deletePlaylist.visibleEndSus(250L)
                savePlaylist.visibleEndSus(250L)
                searchButton.visibleEndSus(250L)
                searchMusic.apply {
                    goneStartSus(250L)
                    hideInputMethod()
                }
            } else {
                deletePlaylist.goneEndSus(250L)
                savePlaylist.goneEndSus(250L)
                searchButton.goneEndSus(250L)
                searchMusic.apply {
                    visibleStartSus(250L)
                    searchProvide()
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun onLongClick(v: android.view.View?): Boolean {
        layoutRec?.apply {
            when (v ?: return true) {
                showAlbums -> com.pt.pro.R.string.ab
                showArtists -> com.pt.pro.R.string.ae
                myPlaylist -> com.pt.pro.R.string.ww
                showAllMusic -> com.pt.pro.R.string.ak
                showAllList -> com.pt.pro.R.string.pq
                deletePlaylist -> {
                    if (v.tag == false) com.pt.pro.R.string.wz else com.pt.pro.R.string.pw
                }
                savePlaylist -> {
                    if (v.tag == false) com.pt.pro.R.string.kl else com.pt.pro.R.string.aj
                }
                searchButton -> com.pt.pro.R.string.rw
                extendButton -> com.pt.pro.R.string.zf
                else -> com.pt.pro.R.string.ak
            }.let {
                v.popUpComment(it, com.pt.pro.R.attr.rmoBackHint, 0)
            }
        }
        return true
    }

    @com.pt.common.global.UiAnn
    protected fun android.view.View.popUpComment(
        @androidx.annotation.StringRes des: Int,
        @androidx.annotation.AttrRes color: Int,
        iy: Int,
    ) {
        with(
            ctxM.commentView(color, resources.getString(des))
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

    override fun doScreenOn() {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                if (System.currentTimeMillis() - lastPauseTime > 120000 && !songPlayer.isPlayingSus()) {
                    isInShowSus().letSusBack {
                        song?.showNotification(DSack(one = false, two = it, three = isCarMode))
                    }
                }
            }
        }
    }

    override fun doScreenOff() {
        pushJob { j ->
            launchDef {
                j?.checkIfDone()
                circleShrink(false)
                screenOff()
            }
        }
    }

    private suspend fun screenOff() {
        withMain {
            headPlayer.circleFrame.justVisibleSus()
            headMusic.frameDetails.applySus {
                tag = false
                justGoneSus()
            }
            if (layoutRec != null && isInShow) {
                context.catchyBadToken {
                    windowManagerMusic.updateViewLayout(headMusic.root_, fetchParams)
                }
                baseCato()
            }
        }
    }


    protected suspend fun baseCato() {
        justScope {
            isTouchActive = true
            whatSongMode = IS_YOUR_PLAYLIST
            songColors = songColors.copy(three = IS_YOUR_PLAYLIST)
        }
        layoutRec?.closeRecycler()
        allMusic.whichListInSearch()
        context.nullChecker()
    }

    private suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.closeRecycler() {
        withMain {
            cardFrame.applySus {
                tag = false
                justGoneSus()
            }
            context.catchyBadToken {
                windowManagerMusic.updateViewLayout(headMusic.root_, fetchParams)
            }
            displayAfterCato(IS_YOUR_PLAYLIST)
            searchMusic.hideInputMethod()
            recyclerSongs.adapter = null
            musicAda?.applySus {
                isTouchActive = true
                recyclerManager?.removeAllViews()
                removeSongs()
            }
        }
    }


    protected suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.displayAfterCato(
        whatMode: Int,
    ) {
        withMain {
            when (whatMode) {
                IS_YOUR_PLAYLIST -> {
                    hideSearch()
                    deletePlaylist.justVisibleSus()
                    savePlaylist.justVisibleSus()
                    ctxM.compactImage(com.pt.pro.R.drawable.ic_save_playlist) {
                        savePlaylist.apply {
                            setImageDrawable(this@compactImage)
                            tag = true
                        }
                    }
                    ctxM.compactImage(com.pt.pro.R.drawable.ic_delete_icon) {
                        deletePlaylist.apply {
                            setImageDrawable(this@compactImage)
                            tag = true
                        }
                    }
                }
                ARTIST_IS -> {
                    hideSearch()
                    deletePlaylist.justInvisibleSus()
                    savePlaylist.justInvisibleSus()
                }
                ALBUM_IS -> {
                    hideSearch()
                    deletePlaylist.justInvisibleSus()
                    savePlaylist.justInvisibleSus()
                }
                ALL_PLAYLIST -> {
                    hideSearch()
                    deletePlaylist.justInvisibleSus()
                    savePlaylist.justInvisibleSus()
                }
                IS_ALL_SONGS -> {
                    hideSearch()
                    savePlaylist.applySus {
                        if (allMusic.isEmpty()) {
                            justInvisibleSus()
                        } else {
                            justVisibleSus()
                        }
                        ctxM.compactImage(com.pt.pro.R.drawable.ic_append_playlist) {
                            setImageDrawable(this@compactImage)
                            tag = false
                        }
                    }
                    deletePlaylist.justVisibleSus()
                    ctxM.compactImage(com.pt.pro.R.drawable.ic_play_circle) {
                        deletePlaylist.apply {
                            setImageDrawable(this@compactImage)
                            tag = false
                        }
                    }
                }
            }
        }
    }

    protected suspend fun com.pt.pro.musical.fasten.MusicRecyclerHeadFasten.rePushAdapter() {
        withMain {
            recyclerSongs.adapter = musicAda
        }
    }

    internal inline var touchListener: com.pt.common.moderator.touch.ContactListener?
        @com.pt.common.global.UiAnn
        get() {
            return object : com.pt.common.moderator.touch.ContactListener {
                private var canGo = false
                private var haveRotate = false
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX: Float = 0F
                private var initialTouchY: Float = 0F
                private var mCurrAngle: Float = 0.0F
                private var mPrevAngle: Float = 0.0F
                private var firstAngle: Float = 0.0F


                override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
                    val xc = (v.width / 2f).toDouble()
                    val yc = (v.height / 2f).toDouble()
                    val x = event.x
                    val y = event.y
                    @Strictfp
                    initialX = fetchParams.x
                    @Strictfp
                    initialY = fetchParams.y
                    @Strictfp
                    initialTouchX = event.rawX
                    @Strictfp
                    initialTouchY = event.rawY

                    val aa = Math.toDegrees(atan2(yc - y, xc - x))
                    @Strictfp
                    firstAngle = ((aa + 360) % 360).toFloat()
                    musicAnimation?.pause()
                }

                override fun onUp(v: android.view.View) {
                    pushJob {

                        launchDef {
                            doUpTouch()
                        }
                    }
                }

                private suspend fun doUpTouch() {
                    withMain {
                        if (canGo) {
                            when {
                                musicProgress in 1 until songDuration -> {
                                    catchy(Unit) {
                                        songPlayer.apply {
                                            seekTo(musicProgress)
                                            if (!isPlaying) play()
                                        }
                                    }
                                    headPlayer.pro.setProgress(musicProgress.toFloat())
                                }
                                musicProgress < 0 -> {
                                    musicProgress = 0
                                    songPlayer.runCatching {
                                        songPlayer.apply {
                                            seekTo(0)
                                            if (!isPlaying) play()
                                        }
                                    }
                                }
                                musicProgress > songDuration -> {
                                    musicProgress = songDuration
                                    catchy(Unit) {
                                        songPlayer.seekTo(songDuration + progressRatio - 100)
                                    }
                                }
                            }
                            canGo = false
                        }
                        if (!isSmall) {
                            if (songPlayer.isPlayingSus()) {
                                withMain {
                                    musicAnimation?.resume()
                                }
                                onSecondEvery.rKTSack(250L).postAfter()
                            }
                            if (!haveRotate) {
                                circleShrink(false)
                            }
                        } else {
                            withMainDef(false) {
                                initialX != fetchParams.x || fetchParams.y != initialY
                            }.let { boolean ->
                                if (boolean) {
                                    doUp(recM.isLand)
                                } else {
                                    circleExtend()
                                }
                            }
                        }
                        haveRotate = false

                        @Strictfp
                        mPrevAngle = mCurrAngle
                        @Strictfp
                        initialX = fetchParams.x
                        @Strictfp
                        initialY = fetchParams.y
                    }
                }


                override fun onMove(v: android.view.View, event: android.view.MotionEvent) {
                    val xc = (v.width / 2f).toDouble()
                    val yc = (v.height / 2f).toDouble()
                    val x = event.x
                    val y = event.y
                    @Strictfp
                    mPrevAngle = mCurrAngle
                    if (!isSmall) {
                        canGo = event.run {
                            abs(initialTouchX - rawX).let<@Strictfp Float, Boolean> { difX ->
                                abs(initialTouchY - rawY).let<@Strictfp Float, Boolean> { difY ->
                                    difX > 10.0F || difY > 10.0F
                                }
                            }
                        }
                        if (canGo) {
                            kotlin.run {
                                Math.toDegrees(
                                    atan2(yc - y, xc - x)
                                ).toFloat().let<@Strictfp Float, @Strictfp Float> { bb ->
                                    ((bb + 360) % 360).let<@Strictfp Float, @Strictfp Float> { a ->
                                        if (a < firstAngle) 360F + a else a
                                    }
                                }
                            }.let<@Strictfp Float, Unit> {
                                mCurrAngle = it - firstAngle
                            }
                            unPost(onSecondEvery.two)
                            musicProgress = if (mCurrAngle > mPrevAngle)
                                musicProgress + progressRatio
                            else
                                musicProgress - progressRatio

                            if (musicProgress in 1 until songDuration) {
                                headPlayer.pro.setProgress(musicProgress.toFloat())
                            }
                            animateRotate(mPrevAngle, mCurrAngle) {
                                headPlayer.circleCard.startAnimation(this)
                            }
                            haveRotate = true
                        }
                    } else {
                        fetchParams.x = initialX + (event.rawX - initialTouchX).toInt()
                        fetchParams.y = initialY + (event.rawY - initialTouchY).toInt()
                        context.catchyBadToken {
                            windowManagerMusic.updateViewLayout(headPlayer.root_, fetchParams)
                        }
                    }
                }
            }
        }
        set(value) {
            value.logProvLess()
        }

}