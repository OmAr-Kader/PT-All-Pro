package com.pt.pro.gallery.dialogs

import com.pt.common.global.*
import com.pt.common.media.checkImageUri
import com.pt.common.media.checkVideoUri
import com.pt.common.mutual.life.GlobalDia
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.databinding.PopForHideBinding
import com.pt.pro.gallery.interfaces.DialogsListener

class PopForHide : GlobalDia<PopForHideBinding>(),
    DialogsListener<PopForHide.PopHiddenListener> {

    override var folderPath: MutableList<MediaSack>? = mutableListOf()

    private inline val foldPath: MutableList<MediaSack> get() = folderPath!!

    override var listener: PopHiddenListener? = null

    override var folderMedia: MutableList<MediaFolderSack>? = null

    override val android.view.LayoutInflater.creBinD: android.view.View
        get() = PopForHideBinding.inflate(this).also {
            @com.pt.common.global.ViewAnn
            binder = it
        }.root


    override suspend fun PopForHideBinding.intiViews() {
        val forHidePic = foldPath.doForHidePic()
        context.nullChecker()
        val forHideVideo = foldPath.doForHideVideo()
        context.nullChecker()
        val forShowPic = foldPath.doForShowPic()
        context.nullChecker()
        val forShowVideo = foldPath.doForShowVideo()
        context.nullChecker()
        val forStoreShowPic = foldPath.doStoreShowPic()
        context.nullChecker()
        val forStoreShowVideo = foldPath.doStoreShowVideo()
        context.nullChecker()
        withMain {
            var foundHide = forShowPic.isNotEmpty() || forShowVideo.isNotEmpty()
            if (!foundHide) showLinear.justInvisible()

            var foundHideStore = forStoreShowPic.isNotEmpty() || forStoreShowVideo.isNotEmpty()
            if (!foundHideStore) showForStoreLinear.justInvisible()

            showForStoreLinear.justGone()

            var foundUnHide = (forHidePic.isNotEmpty() || forHideVideo.isNotEmpty()) &&
                    (forShowPic.isEmpty() || forShowVideo.isEmpty())

            if (!foundUnHide) hideLinear.justInvisible()
            ///////////////////////////////////////////////////////////////////////////////////
            val pForH: String = if (forHidePic.isNotEmpty()) {
                recD.getString(R.string.fa) + " " + forHidePic.size.toStr +
                        " " + recD.getString(R.string.gp)
            } else {
                ""
            }
            val and: String = if (forHidePic.isNotEmpty() && forHideVideo.isNotEmpty())
                " " + recD.getString(R.string.an) + " "
            else
                ""
            val vFHide: String = if (forHideVideo.isNotEmpty()) {
                recD.getString(R.string.fa) + " " + forHideVideo.size.toStr + " " +
                        recD.getString(R.string.vo)
            } else {
                ""
            }
            val a = pForH + and + vFHide
            hideText.text = a
            hideLinear.setOnClickListener {
                listener?.forHiddenShow(forHidePic, forHideVideo, 111)
                ctxD.makeToastRec(R.string.dn, 0)
                if (!foundHide) {
                    dia.dismiss()
                } else {
                    foundHide = false
                    hideLinear.justInvisible()
                }
            }

            val pForS = if (forShowPic.isNotEmpty() || forStoreShowPic.isNotEmpty()) {
                recD.getString(R.string.hm) + " " + forShowPic.size.toStr + " " +
                        recD.getString(R.string.gp)
            } else {
                ""
            }

            val and2 = if (forShowPic.isNotEmpty() && forShowVideo.isNotEmpty())
                " " + recD.getString(R.string.an) + " "
            else
                ""

            val vFShow = if (forShowVideo.isNotEmpty() || forStoreShowVideo.isNotEmpty())
                recD.getString(R.string.hm) + " " + forShowVideo.size.toStr + " " +
                        recD.getString(R.string.vo)
            else
                ""
            val aa = pForS + and2 + vFShow
            showText.text = aa
            showLinear.setOnClickListener {
                listener?.forHiddenShow(forShowPic, forShowVideo, 222)
                /*if (forStoreShowPic.isNotEmpty() || forStoreShowVideo.isNotEmpty()) {
                    listener?.forHiddenShow(forShowPic, forShowVideo, 333)
                }*/
                ctxD.makeToastRec(R.string.dn, 0)
                if (!foundUnHide) {
                    dia.dismiss()
                } else {
                    foundUnHide = false
                    showLinear.justInvisible()
                }
            }
            val mText = recD.getString(R.string.ow) +
                    (forStoreShowPic.size + forStoreShowVideo.size).toStr

            val pForHStore: String = if (forStoreShowPic.isNotEmpty() ||
                forStoreShowVideo.isNotEmpty()
            ) {
                mText + " " + recD.getString(R.string.lo) + recD.getString(R.string.mi)
            } else {
                ""
            }

            showForStoreText.text = pForHStore
            showForStoreLinear.setOnClickListener {
                if (forStoreShowPic.isNotEmpty() || forStoreShowVideo.isNotEmpty()) {
                    listener?.forHiddenShow(forShowPic, forShowVideo, 333)
                }
                ctxD.makeToastRec(R.string.dn, 0)
                if (!foundHide) {
                    dia.dismiss()
                } else {
                    foundHideStore = false
                    hideLinear.justInvisible()
                }
            }
        }
    }


    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.doForHidePic() = justCoroutine {
        withBackDef(mutableListOf()) {
            toMutableList().asSequence().filter {
                val f = FileLate(it.pathMedia!!)
                it.isImage && !f.name.checkHiddenMedia
            }.distinct().toMutableList()
        }
    }


    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.doForHideVideo() = justCoroutine {
        withBackDef(mutableListOf()) {
            toMutableList().asSequence().filter {
                val f = FileLate(it.pathMedia!!)
                !it.isImage && !f.name.checkHiddenMedia
            }.distinct().toMutableList()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.doForShowPic() = justCoroutine {
        withBackDef(mutableListOf()) {
            toMutableList().asSequence().filter {
                val f = FileLate(it.pathMedia!!)
                it.isImage && f.name.checkHiddenMedia
            }.distinct().toMutableList()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.doForShowVideo() = justCoroutine {
        withBackDef(mutableListOf()) {
            toMutableList().asSequence().filter {
                val f = FileLate(it.pathMedia!!)
                !it.isImage && f.name.checkHiddenMedia
            }.distinct().toMutableList()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.doStoreShowPic() = justCoroutine {
        withBackDef(mutableListOf()) {
            filter {
                it.isImage && (it.uriMedia != null ||
                        ctxD.contentResolver.checkImageUri(it.pathMedia.toStr) != null)
            }.distinct().toMutableList()
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MediaSack>.doStoreShowVideo() = justCoroutine {
        withBackDef(mutableListOf()) {
            filter {
                !it.isImage && (it.uriMedia != null ||
                        ctxD.contentResolver.checkVideoUri(it.pathMedia.toStr) != null)
            }.distinct().toMutableList()
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        listener = null
        folderPath = null
        super.onDestroyView()
    }

    @FunctionalInterface
    fun interface PopHiddenListener {
        fun forHiddenShow(
            picsForHide: MutableList<MediaSack>,
            videosForHide: MutableList<MediaSack>,
            opt: Int,
        )
    }

}