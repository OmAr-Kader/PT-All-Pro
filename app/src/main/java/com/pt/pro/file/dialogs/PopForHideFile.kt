package com.pt.pro.file.dialogs

import com.pt.common.global.*
import com.pt.common.media.checkFileID
import com.pt.common.media.margeUri
import com.pt.common.mutual.life.GlobalDia
import com.pt.common.stable.*

class
PopForHideFile : GlobalDia<com.pt.pro.databinding.PopForHideBinding>(),
    com.pt.pro.file.interfaces.DialogListener {

    private var media: MutableList<FileSack> = mutableListOf()
    private var dialogListener: com.pt.pro.file.interfaces.ItemFileListener? = null

    override fun MutableList<FileSack>.pushList(bool: Boolean) {
        media = this
    }

    override fun com.pt.pro.file.interfaces.ItemFileListener.pushListener() {
        dialogListener = this
    }

    override val android.view.LayoutInflater.creBinD: android.view.View
        @com.pt.common.global.UiAnn
        get() = com.pt.pro.databinding.PopForHideBinding.inflate(this).also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root

    override suspend fun com.pt.pro.databinding.PopForHideBinding.intiViews() {
        beforeIntiViews()
        context.nullChecker()
        val forShow = media.doForShow()
        context.nullChecker()
        val forHide = media.doForHide()
        context.nullChecker()
        val forStoreShow = media.doForStoreShow()
        context.nullChecker()
        withMain {
            rotationBarProgressBar.hide()
            rotationBarCard.justGone()
            var foundHide = forShow.isNotEmpty()
            if (!foundHide) showLinear.justGone() else showLinear.justVisible()
            var foundUnHide = forHide.isNotEmpty()
            if (!foundUnHide) hideLinear.justGone() else hideLinear.justVisible()
            var foundUnHideStore = forStoreShow.isNotEmpty()
            if (!foundUnHideStore)
                showForStoreLinear.justGone()
            else
                showForStoreLinear.justVisible()

            ////////////////////////////////////////////////////////////////////////
            val pForH = if (forHide.isNotEmpty()) {
                recD.getString(com.pt.pro.R.string.fa) + " " + forHide.size.toStr + " " +
                        recD.getString(com.pt.pro.R.string.lo)
            } else {
                ""
            }

            hideText.text = pForH
            hideLinear.setOnClickListener {
                dialogListener?.run { forHide.applyHidden() }
                ctxD.makeToastRec(com.pt.pro.R.string.dn, 0)
                if (!foundHide) {
                    dia.dismiss()
                } else {
                    foundHide = false
                    hideLinear.justGone()
                }
            }

            val pForS = if (forShow.isNotEmpty() || forStoreShow.isNotEmpty()) {
                recD.getString(
                    com.pt.pro.R.string.hm
                ) + " " + forShow.doShowAndForStore(forStoreShow) +
                        " " + recD.getString(com.pt.pro.R.string.lo)
            } else {
                ""
            }
            showText.text = pForS
            showLinear.setOnClickListener {
                dialogListener?.run { forShow.applyShow() }
                ctxD.makeToastRec(com.pt.pro.R.string.dn, 0)
                if (!foundUnHide || foundUnHideStore) {
                    dia.dismiss()
                } else {
                    foundUnHide = false
                    showLinear.justGone()
                }
            }
            val mText =
                recD.getString(com.pt.pro.R.string.ow) + forStoreShow.size.toStr + " " +
                        recD.getString(com.pt.pro.R.string.lo)

            val pForHStore: String = if (forStoreShow.isNotEmpty()) {
                mText + " " + recD.getString(com.pt.pro.R.string.mi)
            } else {
                ""
            }
            showForStoreText.text = pForHStore
            showForStoreLinear.setOnClickListener {
                if (forStoreShow.isNotEmpty()) {
                    dialogListener?.run { forStoreShow.applyStoreShow() }
                }
                ctxD.makeToastRec(com.pt.pro.R.string.dn, 0)
                if (!foundHide || foundUnHideStore) {
                    dia.dismiss()
                } else {
                    foundUnHideStore = false
                    hideLinear.justGone()
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun com.pt.pro.databinding.PopForHideBinding.beforeIntiViews() {
        withMain {
            rotationBarCard.justVisible()
            rotationBarProgressBar.show()
        }
    }


    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<FileSack>.doForShow() = justCoroutine {
        withBackDef(mutableListOf()) {
            toMutableList().asSequence().filter {
                FileLate(it.filePath).name.checkHiddenMedia
            }.distinct().toMutableList()
        }
    }


    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<FileSack>.doForHide() = justCoroutine {
        withBackDef(mutableListOf()) {
            toMutableList().asSequence().filter {
                val f = FileLate(it.filePath)
                !f.name.checkHiddenMedia
            }.distinct().toMutableList()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<FileSack>.doForStoreShow() = justCoroutine {
        withBackDef(mutableListOf()) {
            filter {
                ctxD.contentResolver.checkFileID(it.filePath).letSusBack { p ->
                    DSackT(p, it.typeFile).margeUri
                }.letSusBack { u ->
                    u == null
                }
            }.distinct().toMutableList()
        }
    }

    private suspend fun MutableList<FileSack>.doShowAndForStore(
        m: MutableList<FileSack>
    ): Int = justCoroutine {
        withMainDef(this@doShowAndForStore.size) {
            mutableListOf<FileSack>().applySus {
                this@applySus.addAll(this@doShowAndForStore)
                this@applySus.addAll(m)
            }.distinctBy { it.filePath }.toMutableList().size
        }
    }


    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        dialogListener = null
        media.clear()
        super.onDetach()
        super.onDestroyView()
    }

}