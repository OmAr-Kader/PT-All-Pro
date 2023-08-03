package com.pt.pro.file.dialogs

import com.pt.common.global.justInvisible
import com.pt.common.mutual.life.GlobalDia

class PopForCreate : GlobalDia<com.pt.pro.databinding.PopForCreateBinding>(),
    com.pt.pro.file.interfaces.DialogListener {

    private var songs: MutableList<com.pt.common.global.FileSack> = mutableListOf()
    private var isFromVir: Boolean = false
    private var dialogListener: com.pt.pro.file.interfaces.ItemFileListener? = null
    private var isReal = true

    override fun MutableList<com.pt.common.global.FileSack>.pushList(bool: Boolean) {
        songs = this
        isFromVir = bool
    }

    override fun com.pt.pro.file.interfaces.ItemFileListener.pushListener() {
        dialogListener = this
    }

    override val android.view.LayoutInflater.creBinD: android.view.View
        @com.pt.common.global.UiAnn
        get() = com.pt.pro.databinding.PopForCreateBinding.inflate(this).also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root

    @com.pt.common.global.UiAnn
    override suspend fun com.pt.pro.databinding.PopForCreateBinding.intiViews() {
        com.pt.common.stable.withMain {
            if (isFromVir) {
                isReal = false
                virRealLinear.justInvisible()
            }
            real.isChecked = true
            virtual.isChecked = false
            real.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    real.isChecked = true
                    virtual.isChecked = false
                    isReal = true
                }
            }
        }
        com.pt.common.stable.withMain {
            virtual.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    virtual.isChecked = true
                    real.isChecked = false
                    isReal = false
                }
            }
            okPro.setOnClickListener {
                if (inputCreate.text.isNullOrEmpty()) {
                    inputCreate.hint = recD.getString(com.pt.pro.R.string.za)
                } else {
                    dialogListener?.apply {
                        songs.toMutableList().createFolder(
                            inputCreate.text.toString(),
                            isReal,
                            isFromVir
                        )
                    }
                }
                dia.dismiss()
            }
            noPro.setOnClickListener {
                dia.dismiss()
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        isReal = true
        songs.clear()
        isFromVir = false
        dialogListener = null
        super.onDestroyView()
    }

}
