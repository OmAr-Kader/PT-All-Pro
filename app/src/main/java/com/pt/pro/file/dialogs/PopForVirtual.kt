package com.pt.pro.file.dialogs

import com.pt.common.mutual.life.GlobalDia
import com.pt.common.stable.withMain

class PopForVirtual : GlobalDia<com.pt.pro.databinding.PopForVirtualBinding>(),
    com.pt.pro.file.interfaces.DialogListener {

    private var vir: MutableList<String> = mutableListOf()
    private var files: MutableList<com.pt.common.global.FileSack> = mutableListOf()
    private var isAdd: Boolean = true
    private var itemListener: com.pt.pro.file.interfaces.ItemFileListener? = null


    override fun MutableList<com.pt.common.global.FileSack>.pushList(bool: Boolean) {
        files = this
        isAdd = bool
    }

    override fun com.pt.pro.file.interfaces.ItemFileListener.pushListener() {
        itemListener = this
    }

    override fun MutableList<String>.pushStrings() {
        vir = this
    }

    override val android.view.LayoutInflater.creBinD: android.view.View
        @com.pt.common.global.UiAnn
        get() = com.pt.pro.databinding.PopForVirtualBinding.inflate(this).also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root


    @com.pt.common.global.UiAnn
    override suspend fun com.pt.pro.databinding.PopForVirtualBinding.intiViews() {
        withMain {
            virTitle.text = if (isAdd) {
                recD.getString(com.pt.pro.R.string.aq)
            } else {
                recD.getString(com.pt.pro.R.string.rx)
            }
            createFolder.setOnClickListener {
                itemListener?.apply {
                    files.toMutableList().openVirtual()
                }
                dia.dismiss()
            }
        }
        withMain {
            vir.forEach { itF ->
                with(
                    com.pt.pro.databinding.ItemClipBoardBinding.inflate(actD.layoutInflater)
                ) {
                    daysButton.apply {
                        text = itF
                        setOnClickListener {
                            if (isAdd) {
                                itemListener?.apply {
                                    files.toMutableList().addForVir(itF)
                                }
                            } else {
                                itemListener?.apply {
                                    files.toMutableList().removeFromVir(itF)
                                }
                            }
                            dia.dismiss()
                        }
                    }
                    virLinear.addView(this@with.root)
                }
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDetach() {
        isAdd = true
        itemListener = null
        vir.clear()
        files.clear()
        super.onDetach()
    }

}