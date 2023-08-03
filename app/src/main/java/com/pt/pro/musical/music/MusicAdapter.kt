package com.pt.pro.musical.music

import com.pt.common.global.*
import com.pt.common.media.filterAudioByAlbum
import com.pt.common.media.getMusicBit
import com.pt.common.media.musicIndex
import com.pt.common.mutual.adapter.GlobalAdapterEX
import com.pt.common.stable.*
import com.pt.pro.musical.fasten.ItemSongFasten
import kotlinx.coroutines.asCoroutineDispatcher

class MusicAdapter(
    override val songList: MutableList<MusicSack>,
    override val pathsList: MutableList<String>,
    @Volatile
    override var currentPath: String,
    override val isRTL: Boolean,
    override var picListener: com.pt.pro.musical.interfaces.MusicOption?,
) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(),
    com.pt.pro.musical.interfaces.MusicListenerAdapter {

    override var colorItem: Int = android.graphics.Color.RED.toColorAlpha(40)

    internal var jobNative: kotlinx.coroutines.Job? = null
    internal var extNative: java.util.concurrent.ExecutorService? = null
    internal var dispatcherNative: kotlinx.coroutines.ExecutorCoroutineDispatcher? = null

    internal inline val job: kotlinx.coroutines.Job
        get() = jobNative ?: kotlinx.coroutines.SupervisorJob().also { jobNative = it }

    internal inline val ext: java.util.concurrent.ExecutorService
        get() = extNative ?: fetchExtractor.also { extNative = it }

    internal inline val dispatch: kotlinx.coroutines.ExecutorCoroutineDispatcher
        get() = dispatcherNative ?: ext.asCoroutineDispatcher().also { dispatcherNative = it }

    override val coroutineContext: kotlin.coroutines.CoroutineContext
        get() = dispatch + job + kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> }

    @com.pt.common.global.UiAnn
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup,
        viewType: Int,
    ): androidx.recyclerview.widget.RecyclerView.ViewHolder = when (viewType) {
        IS_YOUR_PLAYLIST -> SongHolder(parent.fastenMusicHolder)
        IS_ALL_SONGS -> AllSongHolder(parent.fastenMusicHolder)
        ALBUM_IS -> DetailsHolder(parent.fastenMusicHolder)
        ARTIST_IS -> DetailsHolder(parent.fastenMusicHolder)
        else -> PlaylistHolder(parent.fastenMusicHolder)
    }

    private inline val android.view.ViewGroup.fastenMusicHolder: ItemSongFasten
        get() = com.pt.pro.musical.fasten.MusicInflater.run {
            context.inflaterItemSong()
        }

    override fun getItemViewType(position: Int): Int = songList.getI(position).songType

    @com.pt.common.global.UiAnn
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            IS_YOUR_PLAYLIST -> (holder as SongHolder).bind()
            IS_ALL_SONGS -> (holder as AllSongHolder).bind()
            ALBUM_IS -> (holder as DetailsHolder).bind()
            ARTIST_IS -> (holder as DetailsHolder).bind()
            else -> (holder as PlaylistHolder).bind()
        }
    }

    override fun onViewRecycled(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        when (holder.itemViewType) {
            IS_YOUR_PLAYLIST -> {
                (holder as SongHolder).finalClear()
            }
            IS_ALL_SONGS -> {
                (holder as AllSongHolder).finalClear()
            }
            ALBUM_IS -> {
                (holder as DetailsHolder).finalClear()
            }
            ARTIST_IS -> {
                (holder as DetailsHolder).finalClear()
            }
            else -> {
                (holder as PlaylistHolder).finalClear()
            }
        }
    }

    override fun getItemCount(): Int = songList.size

    @com.pt.common.global.UiAnn
    override fun onViewAttachedToWindow(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder.itemViewType) {
            IS_YOUR_PLAYLIST -> (holder as SongHolder).attach()
            IS_ALL_SONGS -> (holder as AllSongHolder).attach()
            ALBUM_IS -> (holder as DetailsHolder).attach()
            ARTIST_IS -> (holder as DetailsHolder).attach()
            else -> (holder as PlaylistHolder).attach()
        }
    }

    @com.pt.common.global.UiAnn
    override fun onViewDetachedFromWindow(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        /*when (holder.itemViewType) {
            IS_YOUR_PLAYLIST -> (holder as SongHolder).clear()
            IS_ALL_SONGS -> (holder as AllSongHolder).clear()
            ALBUM_IS -> (holder as DetailsHolder).clear()
            ARTIST_IS -> (holder as DetailsHolder).clear()
            ALL_PLAYLIST -> (holder as PlaylistHolder).clear()
            else -> (holder as SongHolder).clear()
        }*/
    }

    override suspend fun MutableList<MusicSack>.updateSongs() {
        val siz = songList.size
        clearer()
        justRemove(siz)
        toMutableList().adder()
        justAdd(size)
    }

    override val MutableList<MusicSack>.fetchTruePosition: (adaPos: Int, adaPosTarget: Int) -> Int
        get() = { _, pos ->
            catchy(pos) {
                songList.run {
                    this@fetchTruePosition.indexOf(this@run[pos])
                }
            }
        }

    @com.pt.common.global.UiAnn
    private suspend fun clearer() {
        withMain {
            songList.clear()
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun MutableList<MusicSack>.adder() {
        withMain {
            songList.addAll(this@adder)
        }
    }

    //@com.pt.common.global.UiAnn
    override suspend fun justNotify() {
        justRemove(songList.size)
        justAdd(songList.size)
    }

    @com.pt.common.global.UiAnn
    private suspend fun justRemove(siz: Int) {
        withMain {
            notifyItemRangeRemoved(0, siz)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun justAdd(siz: Int) {
        withMain {
            notifyItemRangeInserted(0, siz)
        }
    }

    override suspend fun removeSongs() {
        withMain {
            songList.size.let { ss ->
                songList.clear()
                notifyItemRangeRemoved(0, ss)
            }
        }
    }

    override suspend fun onDismissItem(removedPath: String, newCurrentPath: String) {
        withMain {
            songList.indexOfFirst { it.pathAudio.toStr == removedPath }.let { position ->
                songList.removeAtIndex(position)
                notifyItemRemoved(position)
            }

        }
        withMain {
            songList.indexOfFirst { it.pathAudio.toStr == currentPath }.let { upPos ->
                if (newCurrentPath == currentPath) {
                    notifyItemChanged(upPos)
                } else {
                    updateCurrentView(currentPath)
                }
            }
        }
    }

    override suspend fun onItemMove(adaPos: Int, adaPosTarget: Int, newCurrentPath: String) {
        songList.applySusBack {
            justScope {
                if (adaPos <= adaPosTarget) {
                    java.util.Collections.rotate(subList(adaPos, adaPosTarget + 1), -1)
                } else {
                    java.util.Collections.rotate(subList(adaPosTarget, adaPos + 1), 1)
                }
            }
            justScope {
                currentPath = newCurrentPath
                notifyItemMoved(adaPos, adaPosTarget)
            }
        }
    }

    @com.pt.common.global.UiAnn
    override suspend fun updateCurrentView(newCurrentPath: String) {
        withMain {
            songList.musicIndex(currentPath).also { pos ->
                songList.musicIndex(newCurrentPath).also { currentPos ->
                    currentPath = newCurrentPath
                    notifyItemChanged(pos)
                    notifyItemChanged(currentPos)
                }
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override suspend fun MutableList<String>.updatePaths() {
        withDefault {
            justScope {
                pathsList.clear()
            }
            justScope {
                pathsList.addAll(this@updatePaths)
            }
        }
    }

    override fun justClear() {
        val siz = songList.size
        songList.clear()
        notifyItemRangeRemoved(0, siz)
    }

    @com.pt.common.global.UiAnn
    override fun onAdapterDestroy() {
        val siz = songList.size
        songList.clear()
        pathsList.clear()
        notifyItemRangeRemoved(0, siz)
        picListener = null

        job.cancelJob()
        catchy(Unit) {
            dispatch.close()
        }
        ext.shutdownNow()
        cancelScope()
        extNative = null
        jobNative = null
        dispatcherNative = null
    }

    @com.pt.common.global.UiAnn
    inner class DetailsHolder(
        item: ItemSongFasten,
    ) : GlobalAdapterEX<ItemSongFasten, MusicSack>(item) {

        @Volatile
        private var imgJob: kotlinx.coroutines.Job? = null

        override val Int.item: MusicSack
            get() = songList.getI(this)

        override fun ItemSongFasten.bind() {
            root_.setOnClickListener(this@DetailsHolder)
        }

        override fun ItemSongFasten.attach(it: MusicSack, i: Int) {
            clear()
            root_.tag = it.pathAudio.toStr
            songArtist.apply {
                text = it.dur_NSongs.toStr
                isSelected = true
            }
            songTitle.apply {
                text = it.title.toStr
                isSelected = true
            }
            songImage.apply {
                if (it.songType == ALBUM_IS) {
                    loadAsyncAlbum(it.album.toStr, it.pathAudio.toStr) {
                        imgJob = this
                    }
                } else {
                    setImageDrawable(null)
                }
            }
        }

        override fun ItemSongFasten.clear() {
            songImage.apply {
                setImageBitmap(null)
                setImageDrawable(null)
            }
            root_.apply {
                ctxA.compactImage(com.pt.pro.R.drawable.ripple_rectangle) {
                    background = this@compactImage
                }
            }
        }

        override fun ItemSongFasten.finalClear() {
            imgJob?.cancel()
            imgJob = null
        }

        override fun MusicSack.onClick(i: Int) {
            if (songType == ALBUM_IS) {
                picListener?.apply {
                    onDetailsClick(
                        filterFromAlbum = album.toStr,
                        filterFromArtist = null,
                        artist = artist.toStr
                    )
                }
            } else {
                picListener?.apply {
                    onDetailsClick(
                        filterFromAlbum = null,
                        filterFromArtist = artist.toStr,
                        artist = artist.toStr
                    )
                }
            }
        }

        override fun ItemSongFasten.attachPlus(it: MusicSack, i: Int) {}
    }

    @com.pt.common.global.UiAnn
    inner class SongHolder(
        item: ItemSongFasten,
    ) : GlobalAdapterEX<ItemSongFasten, MusicSack>(item) {

        @Volatile
        private var imgJob: kotlinx.coroutines.Job? = null

        override val Int.item: MusicSack
            get() = songList.getI(this)

        override fun ItemSongFasten.bind() {
            root_.apply {
                setOnClickListener(this@SongHolder)
                updateFrameBack(songList.getOrNull(posA)?.pathAudio.toStr == currentPath)
            }
        }

        override fun ItemSongFasten.attach(it: MusicSack, i: Int) {
            clear()
            val mp = it.pathAudio.toStr
            root_.apply {
                tag = mp
                updateFrameBack(mp == currentPath)
            }
            songArtist.apply {
                text = it.artist
                isSelected = true
            }
            songTitle.apply {
                text = it.title ?: FileLate(mp).fetchFileName
                isSelected = true
            }
            loadAsyncImg(mp, mp) {
                imgJob = this
            }
        }

        private fun android.widget.FrameLayout.updateFrameBack(boolean: Boolean) {
            if (boolean) {
                setBackgroundColor(colorItem)
            } else {
                ctxA.compactImage(com.pt.pro.R.drawable.ripple_rectangle) {
                    background = this@compactImage
                }
            }
        }

        override fun ItemSongFasten.clear() {
            songImage.apply {
                setImageBitmap(null)
                setImageDrawable(null)
            }
            root_.apply {
                ctxA.compactImage(com.pt.pro.R.drawable.ripple_rectangle) {
                    background = this@compactImage
                }
            }
        }

        override fun ItemSongFasten.finalClear() {
            imgJob?.cancel()
            imgJob = null
        }

        override fun MusicSack.onClick(i: Int) {
            picListener?.apply {
                onPlayItemCLick()
            }
        }

        override fun ItemSongFasten.attachPlus(it: MusicSack, i: Int) {}
    }


    @com.pt.common.global.UiAnn
    inner class AllSongHolder(
        item: ItemSongFasten,
    ) : GlobalAdapterEX<ItemSongFasten, MusicSack>(item) {

        @Volatile
        private var imgJob: kotlinx.coroutines.Job? = null

        override val Int.item: MusicSack
            get() = songList.getI(this)

        override fun ItemSongFasten.bind() {
            root_.setOnClickListener(this@AllSongHolder)
            root_.updateFrameBack(posA.item.pathAudio.toStr)
        }

        override fun ItemSongFasten.attach(it: MusicSack, i: Int) {
            clear()
            val mp = it.pathAudio.toStr
            root_.tag = mp
            songArtist.apply {
                text = it.artist
                isSelected = true
            }
            songTitle.apply {
                text = it.title ?: FileLate(mp).fetchFileName
                isSelected = true
            }
            root_.updateFrameBack(mp)
            loadAsyncImg(mp, mp) {
                imgJob = this
            }
        }

        private fun android.widget.FrameLayout.updateFrameBack(pathAudio: String) {
            if (pathsList.contains(pathAudio)) {
                setBackgroundColor(colorItem)
            } else {
                ctxA.compactImage(com.pt.pro.R.drawable.ripple_rectangle) {
                    background = this@compactImage
                }
            }
        }

        override fun ItemSongFasten.clear() {
            songImage.apply {
                setImageBitmap(null)
                setImageDrawable(null)
            }
            root_.apply {
                ctxA.compactImage(com.pt.pro.R.drawable.ripple_rectangle) {
                    background = this@compactImage
                }
            }
        }

        override fun ItemSongFasten.finalClear() {
            imgJob?.cancel()
            imgJob = null
        }

        override fun MusicSack.onClick(i: Int) {
            if (pathsList.contains(pathAudio.toStr) && pathsList.size > 1) {
                picListener?.apply {
                    onSongItemCLick(false) {
                        pathsList.remove(pathAudio.toStr)
                        binder.root_.apply {
                            ctxA.compactImage(com.pt.pro.R.drawable.ripple_rectangle) {
                                background = this@compactImage
                            }
                            animMusic(false)
                        }

                    }
                }
            } else {
                picListener?.apply {
                    onSongItemCLick(true) {
                        pathsList.add(pathAudio.toStr)
                        binder.root_.apply {
                            setBackgroundColor(colorItem)
                            animMusic(true)
                        }
                    }
                }
            }
        }

        private fun android.view.View.animMusic(isPush: Boolean) {
            if (isPush) {
                if (isRTL) DSackT(0.0F, 60F) else DSackT(0.0F, -60F)
            } else {
                if (isRTL) DSackT(0.0F, -25F) else DSackT(0.0F, 25F)
            }.also { (one, two) ->
                catchy(Unit) {
                    androidx.core.animation.ValueAnimator.ofFloat(one, two).apply {
                        repeatMode = androidx.core.animation.ValueAnimator.REVERSE
                        repeatCount = 1
                        duration = 250L

                        addUpdateListener {
                            this@animMusic.x = (animatedValue as Float)
                        }
                        start()
                    }
                }
            }
        }

        override fun ItemSongFasten.attachPlus(it: MusicSack, i: Int) {}
    }

    @com.pt.common.global.UiAnn
    inner class PlaylistHolder(
        item: ItemSongFasten,
    ) : GlobalAdapterEX<ItemSongFasten, MusicSack>(item) {

        @Volatile
        private var imgJob: kotlinx.coroutines.Job? = null

        override val Int.item: MusicSack
            get() = songList.getI(this)

        override fun ItemSongFasten.bind() {
            root_.setOnClickListener(this@PlaylistHolder)
        }

        override fun ItemSongFasten.attach(it: MusicSack, i: Int) {
            clear()
            val mp = it.pathAudio.toStr
            root_.tag = mp
            songArtist.apply {
                text = it.artist
                isSelected = true
            }
            songTitle.apply {
                text = it.title ?: FileLate(mp).fetchFileName
                isSelected = true
            }
            loadAsyncImg(mp, mp) {
                imgJob = this
            }
        }

        override fun ItemSongFasten.clear() {
            songImage.apply {
                setImageBitmap(null)
                setImageDrawable(null)
            }
            root_.apply {
                ctxA.compactImage(com.pt.pro.R.drawable.ripple_rectangle) {
                    background = this@compactImage
                }
            }
        }

        override fun ItemSongFasten.finalClear() {
            imgJob?.cancel()
            imgJob = null
        }

        override fun MusicSack.onClick(i: Int) {
            picListener?.apply {
                onPlaylist(playlistBelong = this@onClick.title, pos = this@onClick.idArtAlb)
            }
        }

        override fun ItemSongFasten.attachPlus(it: MusicSack, i: Int) {}
    }

    @com.pt.common.global.WorkerAnn
    override fun ItemSongFasten.loadAsyncAlbum(
        album: String,
        str: String,
        job: kotlinx.coroutines.Job?.() -> Unit
    ) {
        launchDef {
            withBackDef(null) {
                root_.context.contentResolver.filterAudioByAlbum(art = album, isVo = false, artist = album)
            }?.firstOrNull()?.pathAudio?.letSusBack { fistSong ->
                root_.context.nullChecker()
                if (root_.tag == str) {
                    root_.context.getMusicBit(fistSong, com.pt.pro.R.drawable.ic_album, 4) {
                        root_.context.nullChecker()
                        displayLoadAsyncAlbum(DSackV(this@getMusicBit, str), job)
                    }
                }
            } ?: displayFailedAsyncAlbum(job)
        }.also(job)
    }

    private fun ItemSongFasten.displayFailedAsyncAlbum(
        job: kotlinx.coroutines.Job?.() -> Unit
    ) {
        launchMain {
            withMain {
                songImage.apply {
                    context.compactImage(com.pt.pro.R.drawable.ic_album) {
                        setImageDrawable(this@compactImage)
                        scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    }
                }
            }
        }.also(job)
    }

    @com.pt.common.global.UiAnn
    override fun ItemSongFasten.displayLoadAsyncAlbum(
        bit: DSackV<android.graphics.Bitmap?, String>,
        job: kotlinx.coroutines.Job?.() -> Unit
    ) {
        launchMain {
            withMain {
                if (root_.tag == bit.two) {
                    songImage.apply {
                        setImageBitmap(bit.one)
                        scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    }
                } else {
                    bit.one = null
                }
            }
        }.also(job)
    }

    override fun ItemSongFasten.loadAsyncImg(
        path: String,
        str: String,
        job: kotlinx.coroutines.Job?.() -> Unit
    ) {
        launchDef {
            root_.context.getMusicBit(path, com.pt.pro.R.drawable.ic_failed_song, 4) {
                displayLoadAsyncImg(DSackV(this@getMusicBit, str), job)
            }
        }.also(job)
    }

    override fun ItemSongFasten.displayLoadAsyncImg(
        bit: DSackV<android.graphics.Bitmap?, String>,
        job: kotlinx.coroutines.Job?.() -> Unit
    ) {
        launchMain {
            withMain {
                root_.context.nullChecker()
                if (root_.tag == bit.two) {
                    songImage.apply {
                        setImageBitmap(bit.one)
                        scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    }
                } else {
                    bit.one = null
                }
            }
        }.also(job)
    }
}