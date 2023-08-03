package com.pt.pro.notepad.dialogs

import com.pt.common.global.compactImage
import com.pt.common.mutual.life.GlobalDia
import com.pt.pro.file.fasten.FileInflater
import com.pt.pro.file.fasten.PopOptionFasten

class ChooseForImport @JvmOverloads constructor(
    private var importListener: ImportListener? = null
) : GlobalDia<PopOptionFasten>() {

    companion object {

        fun newInstance(
            importListener: ImportListener?
        ): ChooseForImport = ChooseForImport(importListener)
    }

    override val android.view.LayoutInflater.creBinD: android.view.View
        get() = FileInflater.run {
            this@creBinD.context.inflaterOption()
        }.also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root_


    @com.pt.common.global.UiAnn
    override suspend fun PopOptionFasten.intiViews() {
        cardOne.radius = 0F
        cardTwo.radius = 0F
        audioPic.apply {
            ctxD.compactImage(com.pt.pro.R.drawable.ic_create) {
                setImageDrawable(this@compactImage)
            }
            scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
        }
        txtOne.text = resources.getString(com.pt.pro.R.string.vh)
        musicPic.apply {
            ctxD.compactImage(com.pt.pro.R.drawable.ic_data_import) {
                setImageDrawable(this@compactImage)
            }
            scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
        }
        txtTwo.text = resources.getString(com.pt.pro.R.string.vl)
        audioFrame.setOnClickListener {
            importListener?.whichOption(111)
            dia.dismiss()
        }
        musicFrame.setOnClickListener {
            importListener?.whichOption(222)
            dia.dismiss()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        importListener = null
        super.onDestroyView()
    }

    fun interface ImportListener {
        fun whichOption(opt: Int)
    }
}