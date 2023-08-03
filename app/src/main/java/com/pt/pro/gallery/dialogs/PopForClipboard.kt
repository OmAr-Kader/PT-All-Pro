package com.pt.pro.gallery.dialogs

import android.content.res.Configuration
import androidx.appcompat.widget.LinearLayoutCompat
import com.pt.common.global.FileLate
import com.pt.common.global.MediaFolderSack
import com.pt.common.global.MediaSack
import com.pt.common.global.makeToastRec
import com.pt.common.mutual.life.GlobalDia
import com.pt.common.stable.launchDef
import com.pt.common.stable.withMain
import com.pt.pro.R
import com.pt.pro.databinding.ItemClipBoardBinding
import com.pt.pro.databinding.PopForClipboardBinding
import com.pt.pro.gallery.interfaces.DialogsListener

class PopForClipboard : GlobalDia<PopForClipboardBinding>(),
    DialogsListener<(MutableList<MediaSack>.(currentPath: String?, mCopy: Boolean) -> Unit)?> {

    override var folderMedia: MutableList<MediaFolderSack>? = mutableListOf()
    override var folderPath: MutableList<MediaSack>? = mutableListOf()
    override var listener: (MutableList<MediaSack>.(currentPath: String?, mCopy: Boolean) -> Unit)? =
        null

    override val android.view.LayoutInflater.creBinD: android.view.View
        get() = PopForClipboardBinding.inflate(actD.layoutInflater).also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root

    @com.pt.common.global.UiAnn
    override suspend fun PopForClipboardBinding.intiViews() {
        withMain {
            radioCopy.apply {
                isChecked = true
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        radioCopy.isChecked = true
                        radioMove.isChecked = false
                    }
                }
            }
            radioMove.apply {
                isChecked = false
                jumpDrawablesToCurrentState()
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        radioMove.isChecked = true
                        radioCopy.isChecked = false
                    }
                }
            }
            linearClipboard.board()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        launchDef {
            withMain {
                binding.linearClipboard.removeAllViewsInLayout()
            }
            withMain {
                binding.linearClipboard.board()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun LinearLayoutCompat.board() {
        withMain {
            folderMedia?.forEach { (path, folderName, _, _) ->
                if (FileLate(path.toString()).isDirectory) {
                    with(
                        ItemClipBoardBinding.inflate(actD.layoutInflater)
                    ) {
                        daysButton.apply {
                            text = folderName
                            setOnClickListener {
                                folderPath?.toMutableList()?.let {
                                    listener?.invoke(
                                        it,
                                        path.toString(),
                                        binding.radioCopy.isChecked
                                    )
                                }
                                dia.dismiss()
                                ctxD.makeToastRec(R.string.dn, 0)
                            }
                        }
                        this@board.addView(this.root)
                    }
                }
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        folderMedia = null
        folderPath = null
        listener = null
        binder = null
        super.onDestroyView()
    }
}