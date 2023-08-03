package com.pt.pro.gallery.dialogs

import androidx.core.widget.doAfterTextChanged
import com.pt.common.mutual.life.GlobalDia

class RenameDialog : GlobalDia<com.pt.pro.databinding.RenameDialogBinding>(),
    com.pt.pro.gallery.interfaces.RenameDialogListener {

    override var fileHolder: com.pt.common.global.FileSack? = null
    override var mediaHold: com.pt.common.global.MediaSack? = null
    override var oneListener: DialogListenerOne? = null
    override var twoListener: DialogListenerTwo? = null

    private var pathFile: com.pt.common.global.FileLate? = null

    override val android.view.LayoutInflater.creBinD: android.view.View
        get() = com.pt.pro.databinding.RenameDialogBinding.inflate(actD.layoutInflater).also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root

    @com.pt.common.global.UiAnn
    override suspend fun com.pt.pro.databinding.RenameDialogBinding.intiViews() {
        pathFile = if (fileHolder != null) {
            com.pt.common.global.FileLate(fileHolder?.filePath.toString())
        } else {
            com.pt.common.global.FileLate(mediaHold?.pathMedia.toString())
        }
        this@intiViews.newName.apply {
            setText(pathFile?.nameWithoutExtension)
            doAfterTextChanged {
                this@intiViews.okPro.isEnabled = !it.isNullOrEmpty()
            }
        }
        okPro.setOnClickListener {
            pathFile?.let { file ->
                newName.text.toString().let { newName ->
                    file.parent.let {
                        it ?: return@setOnClickListener
                        if (file.isDirectory) {
                            it.toString() + com.pt.common.global.FileLate.separator + newName
                        } else {
                            it.toString() + com.pt.common.global.FileLate.separator +
                                    newName + "." + file.extension.trim()
                        }
                    }.let { toPathText ->
                        if (fileHolder != null) {
                            twoListener?.apply {
                                fileHolder?.also {
                                    it.copy().onRename(file.path, toPathText, newName)
                                }
                            }
                        } else {
                            oneListener?.apply {
                                mediaHold?.also {
                                    it.copy().onRename(file.path, toPathText, newName)
                                }
                            }
                        }
                        dia.dismiss()
                    }
                }
            }
        }
    }


    @java.lang.FunctionalInterface
    fun interface DialogListenerOne {
        fun com.pt.common.global.MediaSack.onRename(
            fromPath: String?, toPath: String?, newName: String?
        )
    }


    @java.lang.FunctionalInterface
    fun interface DialogListenerTwo {
        fun com.pt.common.global.FileSack.onRename(
            fromPath: String,
            toPath: String,
            newName: String,
        )
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        oneListener = null
        twoListener = null
        fileHolder = null
        mediaHold = null
        pathFile = null
        super.onDestroyView()
    }

}
