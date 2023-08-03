package com.pt.pro.notepad.adapters

import android.content.Context
import android.media.AudioManager
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.HandlerCompat
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.pt.common.global.*
import com.pt.common.mutual.adapter.GlobalAdapter
import com.pt.common.mutual.adapter.GlobalAdapterLong
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.databinding.*
import com.pt.pro.notepad.interfaces.AdapterUpdate
import com.pt.pro.notepad.interfaces.DataClickListener
import com.pt.pro.notepad.models.DataKeeperItem
import com.pt.pro.notepad.objects.*

class DataKeeperAdapter(
    override val dataItems: MutableList<DataKeeperItem>,
    override val widthData: Int,
    override val colorUnselected: Int,
    override val colorSelect: Int,
    override var dataClickListener: DataClickListener?,
    listCol: MutableList<Int>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AdapterUpdate {

    override val dataItemsOri: MutableList<DataKeeperItem> = mutableListOf()

    @Volatile
    override var img: Boolean = false

    @Volatile
    override var rec: Boolean = false

    override val temp: MutableList<DataKeeperItem> = mutableListOf()
    override val images: MutableList<String> = mutableListOf()
    override val records: MutableList<String> = mutableListOf()

    @Volatile
    override var lastOpt: Int = HIDE_SEL

    @Volatile
    override var actionModeBoolean: Boolean = false

    @Volatile
    override var copyPos: Int = dataItems.size - 1

    @Volatile
    override var playRecord: Boolean = false

    private var mAudioPlayingHolder: RowAudioBinding? = null
    private var mPlayingPosition = -1

    private var lastLengthItem: Long = 0L

    private inline val mediaPlayer: ExoPlayer get() = mediaPlay!!

    private var mediaPlay: ExoPlayer? = null

    private val uiUpdateHandler = HandlerCompat.createAsync(android.os.Looper.getMainLooper())

    private var running = false

    @ColorInt
    private val dat = listCol.getI(0)//shardColor.getInt(DATA_COL, datCol)

    @ColorInt
    private val imp = listCol.getI(1)//shardColor.getInt(IMP_COL, impCol)

    @ColorInt
    private val lin = listCol.getI(2)//shardColor.getInt(LINK_COL, linCol)

    private val rem = listCol.getI(3)//shardColor.getInt(REM_COL, remCol)

    @ColorInt
    private val sca = listCol.getI(4)//shardColor.getInt(SCAN_COL, scaCol)

    @ColorInt
    private val ema = listCol.getI(5)//shardColor.getInt(MAIL_COL, emaCol)

    @Volatile
    private var haveNewImg: Boolean = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = when (viewType) {
        ADAPTER_IMAGE -> ViewHolderImage(
            DataImageRowBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
        ADAPTER_RECORD -> ViewHolderRecord(
            RowAudioBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
        ADAPTER_LINKER -> ViewHolderTextLinker(
            LinkRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        ADAPTER_EMAIL -> ViewHolderMail(
            EmailDataRowBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
        else -> ViewHolderText(
            TextDataRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ADAPTER_IMAGE -> (holder as ViewHolderImage).bind()
            ADAPTER_RECORD -> (holder as ViewHolderRecord).bind()
            ADAPTER_LINKER -> (holder as ViewHolderTextLinker).bind()
            ADAPTER_EMAIL -> (holder as ViewHolderMail).bind()
            else -> (holder as ViewHolderText).bind()
        }
    }

    @com.pt.common.global.MainAnn
    override suspend fun refreshAdapter() {
        withMain {
            actionModeBoolean = false
            if (temp.size != 0) {
                temp.forEach {
                    it.isSelectData = false
                }
                temp.clear()
                records.clear()
                images.clear()
            }
            lastOpt = HIDE_SEL
            rec = false
            img = false
        }
        justNotify()
    }

    override fun DataKeeperItem.updateWebItem() {
        haveNewImg = true
        dataItems.indexOfFirst {
            it.timeData == this@updateWebItem.timeData
        }.let {
            if (it != -1) {
                dataItems[it] = this@updateWebItem
                notifyItemChanged(it)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        when (holder.itemViewType) {
            ADAPTER_IMAGE -> (holder as ViewHolderImage).attach()
            ADAPTER_RECORD -> (holder as ViewHolderRecord).attach()
            ADAPTER_LINKER -> (holder as ViewHolderTextLinker).attach()
            ADAPTER_EMAIL -> (holder as ViewHolderMail).attach()
            else -> (holder as ViewHolderText).attach()
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        when (holder.itemViewType) {
            ADAPTER_IMAGE -> (holder as ViewHolderImage).clear()
            ADAPTER_RECORD -> (holder as ViewHolderRecord).clear()
            ADAPTER_LINKER -> (holder as ViewHolderTextLinker).clear()
            ADAPTER_EMAIL -> (holder as ViewHolderMail).clear()
            else -> (holder as ViewHolderText).clear()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataItems.getI(position).keeperType) {
            KEEP_IMG -> ADAPTER_IMAGE
            KEEP_RECORD -> ADAPTER_RECORD
            KEEP_LINK -> ADAPTER_LINKER
            KEEP_EMAIL -> ADAPTER_EMAIL
            else -> ADAPTER_TEXT
        }
    }

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemCount(): Int = if (dataItems.isEmpty()) 0 else dataItems.size

    override fun RowAudioBinding.updatePlayingView(isPlaying: Boolean) {
        if (mediaPlay == null) return
        if (isPlaying) {
            playRecord = true
            uiUpdateHandler.postDelayed(run, 200)
            root.context.compactImage(R.drawable.ic_pause_music) {
                imgPlayPause.setImageDrawable(this@compactImage)
            }
            playTime.base = SystemClock.elapsedRealtime() - mediaPlayer.currentPosition
            playTime.start()
            running = true
        } else {
            uiUpdateHandler.removeCallbacksAndMessages(null)
            root.context.compactImage(R.drawable.ic_play_circle) {
                imgPlayPause.setImageDrawable(this@compactImage)
            }
            playRecord = false
            if (running) {
                playTime.stop()
                playTime.base = SystemClock.elapsedRealtime() - mediaPlayer.currentPosition
                running = false
            }
        }
    }

    private fun RowAudioBinding.updateNonPlayingView() {
        if (this@updateNonPlayingView == mAudioPlayingHolder) {
            uiUpdateHandler.removeCallbacksAndMessages(null)
        }
        playTime.text = adapterTime(lastLengthItem)
        audioSeekBar.progress = 0
        playTime.stop()
        root.context.compactImage(R.drawable.ic_play_circle) {
            (mAudioPlayingHolder ?: return).imgPlayPause.setImageDrawable(this@compactImage)
        }
    }

    private val run: () -> Unit
        get() = {
            mediaPlay?.also {
                mAudioPlayingHolder?.audioSeekBar?.apply {
                    progress = it.currentPosition.toInt()
                    it.duration.let { du ->
                        if (du > 1 && max.toLong() != du) {
                            max = du.toInt()
                        }
                    }
                    uiUpdateHandler.postDelayed(run, 200)
                }
            }
        }

    override fun RowAudioBinding.performPlayButtonClick(dataKeeperItem: DataKeeperItem, posA: Int) {
        if (posA == mPlayingPosition) {
            if (mediaPlay == null) return
            mediaPlayer.apply {
                if (isPlaying) {
                    pause()
                } else {
                    play()
                }
            }
        } else {
            mPlayingPosition = posA
            if (mediaPlay != null) {
                if (null != mAudioPlayingHolder) {
                    if (mPlayingPosition != -1) {
                        mAudioPlayingHolder?.updateNonPlayingView()
                    }
                }
                mediaPlayer.stop()
                mediaPlayer.clearMediaItems()
            }
            mAudioPlayingHolder = this
            root.context.startMediaPlayer(dataKeeperItem)
        }
    }

    override fun Chronometer.onProgressChanged(progress: Int, position: Int) {
        if (position == mPlayingPosition) {
            mediaPlayer.apply {
                seekTo(progress.toLong())
            }
            base = SystemClock.elapsedRealtime() - mediaPlayer.currentPosition
            uiUpdateHandler.apply {
                removeCallbacksAndMessages(null)
                postDelayed(run, 200)
            }
        }
    }

    override fun stopPlayer() {
        if (null != mediaPlay) {
            mAudioPlayingHolder?.releaseMediaPlayer()
            mediaPlay?.apply {
                synchronized(this) {
                    catchy(Unit) {
                        recordCall?.let { removeListener(it) }
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
            mediaPlay = null
        }
    }

    override fun onAdapterDestroy() {
        temp.clear()
        images.clear()
        records.clear()
        dataItems.clear()
        dataItemsOri.clear()
        dataClickListener = null
        mediaPlay = null
        mAudioPlayingHolder = null
        audioListener = null
        recordCall = null
    }

    //@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    private fun Context.startMediaPlayer(dataKeeperItem: DataKeeperItem) {
        runCatching {
            ExoPlayer.Builder(
                this@startMediaPlayer
            ).apply {
                //setPauseAtEndOfMediaItems(true)
                //setUseLazyPreparation(true)
                //setUsePlatformDiagnostics(false)
                setAudioAttributes(musicAudioAttr, true)
                setHandleAudioBecomingNoisy(true)
            }.build().also { exoP ->
                exoP.playWhenReady = true
                mediaPlay = exoP
                recordCall?.let { exoP.addListener(it) }
                androidx.media3.common.MediaItem.Builder()
                    .setUri(dataKeeperItem.recordPath.toUri)
                    .build().let {
                        exoP.setMediaItem(it)
                    }
                exoP.shuffleModeEnabled = false
                exoP.repeatMode = Player.REPEAT_MODE_OFF
            }.also {
                it.prepare()
            }
        }.onFailure {
            it.toStr.logProvCrash("startMediaPlayer")
        }
    }

    private var recordCall: Player.Listener? = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == Player.STATE_ENDED) {
                mAudioPlayingHolder?.releaseMediaPlayer()
            }
        }


        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            mAudioPlayingHolder?.updatePlayingView(isPlaying)
        }

    }

    override fun RowAudioBinding.releaseMediaPlayer() {
        updateNonPlayingView()
        playTime.stop()
        audioSeekBar.progress = 0
        if (null != mediaPlay) {
            mediaPlay?.apply {
                synchronized(this) {
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
        }
        mPlayingPosition = -1
        playRecord = false
    }

    private var audioListener: AudioManager.OnAudioFocusChangeListener? =
        AudioManager.OnAudioFocusChangeListener {

        }

    override fun DataKeeperItem?.addItem() {
        if (this == null) return
        dataItems.add(this)
        dataItemsOri.add(this)
        notifyItemInserted(dataItems.size - 1)
    }

    override suspend fun MutableList<DataKeeperItem>.updateDataAdapter() {
        val siz = dataItems.size
        clearer()
        justRemove(siz)
        adder()
        justAdd(size)
    }

    override suspend fun MutableList<DataKeeperItem>.updateDataAdapterSearch() {
        val siz = dataItems.size
        clearerOnly()
        justRemove(siz)
        adderOnly()
        justAdd(size)
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun clearer() {
        withBack {
            dataItems.clear()
            dataItemsOri.clear()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun clearerOnly() {
        withBack {
            dataItems.clear()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<DataKeeperItem>.adder() {
        withBack {
            dataItems.addAll(this@adder)
            dataItemsOri.addAll(this@adder)
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<DataKeeperItem>.adderOnly() {
        withBack {
            dataItems.addAll(this@adderOnly)
        }
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

    @com.pt.common.global.UiAnn
    override suspend fun justNotify() {
        justRemove(dataItems.size)
        justAdd(dataItems.size)
    }

    inner class ViewHolderImage(
        item: DataImageRowBinding,
    ) : GlobalAdapterLong<DataImageRowBinding, DataKeeperItem>(item) {

        override val Int.item: DataKeeperItem
            get() = dataItems.getI(this)

        override fun DataImageRowBinding.bind() {
            linearImage.apply {
                setOnClickListener(this@ViewHolderImage)
                setOnLongClickListener(this@ViewHolderImage)
            }
        }

        override fun DataImageRowBinding.attach(it: DataKeeperItem, i: Int) {
            linearImage.apply {
                if (it.isSelectData && actionModeBoolean)
                    setBackgroundColor(colorSelect)
                else
                    setBackgroundColor(colorUnselected)
            }
            imageView.apply {
                loadForData(it.imageUrl.toStr)
            }
            msgTime2.text = DATA_TIME_FORM.toDataFormat(it.timeData)

        }

        override fun DataImageRowBinding.clear() {}

        override fun View.onClick(it: DataKeeperItem) {
            if (actionModeBoolean && it.isSelectData) {
                binder.linearImage.setBackgroundColor(colorUnselected)
                temp.remove(it)
                images.remove(it.imageUrl.toStr)
                if (images.isEmpty()) img = false
                it.isSelectData = false
            } else if (actionModeBoolean && !it.isSelectData) {
                img = true
                binder.linearImage.setBackgroundColor(colorSelect)
                temp.add(it)
                images.add(it.imageUrl.toStr)
                it.isSelectData = true
            } else {
                dataClickListener?.onImageClick(it.imageUrl.toStr)
            }
            temp.optionPre()
        }

        override fun View.onLongClick(it: DataKeeperItem): Boolean {
            return if (!actionModeBoolean) {
                img = true
                actionModeBoolean = true
                binder.linearImage.setBackgroundColor(colorSelect)
                temp.add(it)
                it.isSelectData = true
                images.add(it.imageUrl!!)
                temp.optionPre()
                true
            } else {
                false
            }
        }
    }

    inner class ViewHolderRecord(
        item: RowAudioBinding,
    ) : GlobalAdapterLong<RowAudioBinding, DataKeeperItem>(item) {

        override val Int.item: DataKeeperItem
            get() = dataItems.getI(this)

        override fun RowAudioBinding.bind() {
            revRecord.apply {
                setOnClickListener(this@ViewHolderRecord)
                setOnLongClickListener(this@ViewHolderRecord)
            }
            imgPlayPause.apply {
                tag = this@ViewHolderRecord
                setOnClickListener(this@ViewHolderRecord)
            }
        }

        override fun RowAudioBinding.attach(it: DataKeeperItem, i: Int) {
            revRecord.apply {
                if (it.isSelectData && actionModeBoolean)
                    setBackgroundColor(colorSelect)
                else
                    setBackgroundColor(colorUnselected)
            }
            playTime.text = adapterTime(it.recordLength)
            filePath.text = DATA_TIME_FORM.toDataFormat(it.timeData)
            audioSeekBar.onSeekListener { pro, stop ->
                if (stop == 1) {
                    uiUpdateHandler.removeCallbacksAndMessages(null)
                } else if (stop == -1 && mediaPlay != null) {
                    playTime.onProgressChanged(pro, i)
                }
            }
        }

        override fun RowAudioBinding.clear() {

        }

        override fun View.onClick(it: DataKeeperItem) {
            if (this == binder.imgPlayPause) {
                lastLengthItem = it.recordLength
                binder.performPlayButtonClick(it, posA)
            } else {
                if (actionModeBoolean && it.isSelectData) {
                    binder.revRecord.setBackgroundColor(colorUnselected)
                    temp.remove(it)
                    records.remove(it.recordPath.toStr)
                    if (records.isEmpty()) rec = false
                    it.isSelectData = false
                } else if (actionModeBoolean) {
                    rec = true
                    binder.revRecord.setBackgroundColor(colorSelect)
                    temp.add(it)
                    records.add(it.recordPath.toStr)
                    it.isSelectData = true
                }
                temp.optionPre()
            }
        }

        override fun View.onLongClick(it: DataKeeperItem): Boolean {
            return if (!actionModeBoolean) {
                rec = true
                actionModeBoolean = true
                binder.revRecord.setBackgroundColor(colorSelect)
                temp.add(it)
                it.isSelectData = true
                records.add(it.recordPath!!)
                temp.optionPre()
                true
            } else {
                false
            }
        }

    }

    override val Int.findKeeperColor: Int
        get() {
            return when (this@findKeeperColor) {
                KEEP_DATA -> dat
                KEEP_IMPORTANT -> imp
                KEEP_REMEMBER -> rem
                KEEP_EMAIL -> ema
                KEEP_SCAN -> sca
                else -> dat
            }
        }

    inner class ViewHolderText(
        item: TextDataRowBinding,
    ) : GlobalAdapterLong<TextDataRowBinding, DataKeeperItem>(item) {

        override val Int.item: DataKeeperItem
            get() = dataItems.getI(this)

        override fun TextDataRowBinding.bind() {
            revText.apply {
                setOnClickListener(this@ViewHolderText)
                setOnLongClickListener(this@ViewHolderText)
            }
        }

        override fun TextDataRowBinding.attach(it: DataKeeperItem, i: Int) {
            if (it.isSelectData && actionModeBoolean)
                revText.setBackgroundColor(colorSelect)
            else
                revText.setBackgroundColor(colorUnselected)

            if (it.keeperType == KEEP_SCAN)
                msgTime.isGon
            else
                msgTime.text = DATA_TIME_FORM.toDataFormat(it.timeData)

            it.keeperType.findKeeperColor.let { c ->
                cardMsg.setCardBackgroundColor(c)
                msgText.apply {
                    if (it.keeperType == KEEP_SCAN) {
                        maxWidth = widthData - 20F.toPixelA
                    }
                    if (it.keeperType == KEEP_SCAN || it.notTime != -1L) {
                        if (it.notTime != -1L && it.notTime > System.currentTimeMillis()) {
                            notIcon.also {
                                it.justVisible()
                                it.svgReColor(c.isColorDarkText)
                            }
                            this@apply.setPadding(7F.toPixelA, 0, 7F.toPixelA, 0)
                        }
                        scanner(it.notTime, it.dataText.toStr, c)
                    } else {
                        notIcon.justGone()
                        normalWhite(
                            it.dataText.toStr,
                            c,
                            it.keeperType == KEEP_IMPORTANT
                        )
                    }
                }
            }
        }

        override fun TextDataRowBinding.clear() {

        }

        override fun View.onClick(it: DataKeeperItem) {
            if (actionModeBoolean) {
                if (it.isSelectData) {
                    binder.revText.setBackgroundColor(colorUnselected)
                    temp.remove(it)
                    it.isSelectData = false
                } else {
                    binder.revText.setBackgroundColor(colorSelect)
                    temp.add(it)
                    it.isSelectData = true
                }
                temp.optionPre()
            }
        }

        override fun View.onLongClick(it: DataKeeperItem): Boolean {
            return if (!actionModeBoolean) {
                actionModeBoolean = true
                binder.revText.setBackgroundColor(colorSelect)
                temp.add(it)
                it.isSelectData = true
                temp.optionPre()
                copyPos = posA
                true
            } else {
                false
            }
        }

        private fun AppCompatTextView.scanner(timeData: Long, textEdit: String, c: Int) {
            setTextColor(c.isColorDarkText)
            (com.pt.common.BuildConfig.TEXT_START_COLOR + DATA_TIME_FORM.toDataFormat(
                timeData
            ) + com.pt.common.BuildConfig.TEXT_END_COLOR).let {
                "$textEdit<br>$it"
            }.replace("\n", "<br/>").toHtmlText.let {
                text = it
            }
        }

        private fun AppCompatTextView.normalWhite(textEdit: String, c: Int, t: Boolean) {
            maxWidth = (widthData * 0.7).toInt()
            setTextColor(c.isColorDarkText)
            if (t) {
                setTextSize(
                    android.util.TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.txt)
                )
                setTextKeepState(
                    ("<b>$textEdit</b>")
                        .replace("\r\n", "<br/>")
                        .replace("\n", "<br/>")
                        .replace(" ", "&nbsp;").toHtmlText, TextView.BufferType.SPANNABLE
                )
            } else {
                setTextSize(
                    android.util.TypedValue.COMPLEX_UNIT_PX,
                    resources.getDimension(R.dimen.tqd)
                )
                setTextKeepState(textEdit, TextView.BufferType.SPANNABLE)
            }
        }
    }

    inner class ViewHolderTextLinker(
        item: LinkRowBinding,
    ) : GlobalAdapterLong<LinkRowBinding, DataKeeperItem>(item) {

        override val Int.item: DataKeeperItem
            get() = dataItems.getI(this)

        override fun LinkRowBinding.bind() {
            posA.item.let {
                if (it.emailToSome != null) {
                    linkTitle.run {
                        setTextKeepState(it.emailToSome, TextView.BufferType.SPANNABLE)
                        maxWidth = ((widthData * 0.7).toInt() - 70F.toPixelA)
                    }
                }
                linkTitle.setTextColor(lin.isColorDarkText)
                if (it.emailToSome == null) {
                    linkInfo.justGone()
                } else {
                    linkInfo.justVisible()
                }
                if (haveNewImg) {
                    if (it.emailSubject == null) {
                        linkImage.justGone()
                    } else {
                        linkImage.justVisible()
                        linkImage.loadForWeb(it.emailSubject)
                    }
                }
            }
        }


        override fun LinkRowBinding.attach(it: DataKeeperItem, i: Int) {
            if (it.emailSubject == null) {
                linkImage.justGone()
            } else {
                linkImage.justVisible()
                linkImage.loadForWeb(it.emailSubject)
            }
            msgTime.text = DATA_TIME_FORM.toDataFormat(it.timeData)
            msgText.setTextKeepState(it.dataText, TextView.BufferType.SPANNABLE)
            if (it.isSelectData && actionModeBoolean)
                revText.setBackgroundColor(colorSelect)
            else
                revText.setBackgroundColor(colorUnselected)
            linkTitle.setTextColor(lin.isColorDarkText)
            cardMsg.setCardBackgroundColor(lin)
            msgText.apply {
                maxWidth = (widthData * 0.7).toInt()
                setTextColor(lin.isColorDarkText)
                paintFlags = paintFlags or android.graphics.Paint.UNDERLINE_TEXT_FLAG
                setOnLongClickListener(this@ViewHolderTextLinker)
                setOnClickListener(this@ViewHolderTextLinker)
            }
            revText.apply {
                setOnClickListener(this@ViewHolderTextLinker)
                setOnLongClickListener(this@ViewHolderTextLinker)
            }
        }

        override fun LinkRowBinding.clear() {}

        override fun View.onClick(it: DataKeeperItem) {
            if (this == binder.revText) {
                if (actionModeBoolean) {
                    if (it.isSelectData) {
                        binder.revText.setBackgroundColor(colorUnselected)
                        temp.remove(it)
                        it.isSelectData = false
                    } else {
                        binder.revText.setBackgroundColor(colorSelect)
                        temp.add(it)
                        it.isSelectData = true
                    }
                    temp.optionPre()
                }
            } else {
                if (!actionModeBoolean) {
                    dataClickListener?.apply {
                        it.pushLink(binder.msgText.text.toStr)
                    }
                } else {
                    if (actionModeBoolean && it.isSelectData) {
                        binder.revText.setBackgroundColor(colorUnselected)
                        temp.remove(it)
                        it.isSelectData = false
                    } else if (actionModeBoolean) {
                        binder.revText.setBackgroundColor(colorSelect)
                        temp.add(it)
                        it.isSelectData = true
                    }
                    temp.optionPre()
                }
            }
        }

        override fun View.onLongClick(it: DataKeeperItem): Boolean {
            if (this == binder.revText) {
                return if (!actionModeBoolean) {
                    actionModeBoolean = true
                    binder.revText.setBackgroundColor(colorSelect)
                    temp.add(it)
                    it.isSelectData = true
                    temp.optionPre()
                    copyPos = posA
                    true
                } else {
                    false
                }
            } else {
                return if (!actionModeBoolean) {
                    binder.msgText.apply {
                        dataClickListener?.onLink(true, text.toStr)
                    }
                    true
                } else {
                    false
                }
            }
        }
    }


    inner class ViewHolderMail(
        view: EmailDataRowBinding,
    ) : GlobalAdapter<EmailDataRowBinding, DataKeeperItem>(view) {

        override val Int.item: DataKeeperItem
            get() = dataItems.getI(this)

        override fun EmailDataRowBinding.bind() {
            root.setOnClickListener(this@ViewHolderMail)
        }

        override fun EmailDataRowBinding.attach(it: DataKeeperItem) {
            msgFrame.setCardBackgroundColor(ema)
            toEmail.text = it.emailToSome
            subjectEmail.text = it.emailSubject
            email.text = it.dataText
            emailTime.text = DATA_TIME_FORM.toDataFormat(it.timeData)
        }

        override fun EmailDataRowBinding.clear() {

        }

        override fun DataKeeperItem.onClick(i: Int) {
            dataClickListener?.apply {
                DSack(emailToSome, emailSubject, dataText).sendEmail()
            }
        }
    }

    override fun MutableList<DataKeeperItem>.optionPre() {
        if (isEmpty()) {
            dataClickListener?.onActionActions(HIDE_SEL)
            actionModeBoolean = false
        } else {
            when (last().keeperType) {
                KEEP_RECORD -> if (records.size > 1) MULTI_RECORD_SEL else RECORD_SEL
                KEEP_IMG -> if (images.size > 1) MULTI_IMG_SEL else IMG_SEL
                else -> TXT_SEL
            }.let {
                if (it != lastOpt) {
                    dataClickListener?.onActionActions(it)
                    lastOpt = it
                }
            }
        }
    }

}

