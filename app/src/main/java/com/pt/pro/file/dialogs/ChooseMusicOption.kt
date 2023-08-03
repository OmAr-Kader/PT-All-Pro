package com.pt.pro.file.dialogs

import com.pt.common.global.toStr
import com.pt.common.media.getMusicBit
import com.pt.common.mutual.life.GlobalDia
import com.pt.common.stable.getI
import com.pt.common.stable.launchMain
import com.pt.pro.file.fasten.FileInflater
import com.pt.pro.file.fasten.PopOptionFasten

class ChooseMusicOption : GlobalDia<PopOptionFasten>(),
    com.pt.pro.file.interfaces.DialogListener {

    private var musicList: MutableList<com.pt.common.global.MusicSack> = mutableListOf()
    private var pos: Int = 0
    private var musicListener: com.pt.pro.file.interfaces.ItemFileListener? = null

    override fun MutableList<com.pt.common.global.MusicSack>.pushMusic(position: Int) {
        musicList = this
        pos = position
    }

    override fun com.pt.pro.file.interfaces.ItemFileListener.pushListener() {
        musicListener = this
    }

    override val android.view.LayoutInflater.creBinD: android.view.View
        @com.pt.common.global.UiAnn
        get() = FileInflater.run {
            this@creBinD.context.inflaterOption()
        }.also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root_


    @com.pt.common.global.WorkerAnn
    override suspend fun PopOptionFasten.intiViews() {
        ctxD.getMusicBit(musicList.getI(pos).pathAudio.toStr, com.pt.pro.R.drawable.ic_album, 2) {
            doIntiViews(this@getMusicBit)
        }
    }

    private fun PopOptionFasten.doIntiViews(bit: android.graphics.Bitmap?) {
        launchMain {
            com.pt.common.stable.withMain {
                audioPic.setImageBitmap(bit)
                musicPic.apply {
                    setImageBitmap(bit)
                    scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                }
                audioFrame.setOnClickListener {
                    musicListener?.apply {
                        musicList.toMutableList().launchMusic(pos, 111)
                    }
                    dia.dismiss()
                }
                musicFrame.setOnClickListener {
                    musicListener?.apply {
                        musicList.toMutableList().launchMusic(pos, 222)
                    }
                    dia.dismiss()
                }
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        musicListener = null
        musicList.clear()
        pos = 0
        super.onDestroyView()
    }

}