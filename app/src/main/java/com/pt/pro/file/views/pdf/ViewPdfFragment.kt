package com.pt.pro.file.views.pdf

import com.pt.common.global.*
import com.pt.common.media.fetchPdfForRender
import com.pt.common.media.renderPdf
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.file.interfaces.PDFViewerListener
import com.pt.pro.file.interfaces.SliderListener
import com.pt.pro.gallery.fasten.ZoomViewFasten

class ViewPdfFragment : GlobalSimpleFragment<ZoomViewFasten>(), PDFViewerListener,
    androidx.work.Configuration.Provider {

    override var pdfPath: String? = null

    override var pageNumber: Int = 0

    override var browserFileListener: SliderListener? = null

    override var hideSys: Boolean = false

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run { this@creBin.context.inflaterZoomView() }.also {
                binder = it
            }.root_
        }


    @com.pt.common.global.UiAnn
    override fun ZoomViewFasten.onViewCreated() {
        launchDef {
            intiImageView()
        }
    }

    private suspend fun intiImageView() {
        launchDef {
            cont.fetchPdfForRender(pdfPath, act.intent.data) {
                if (this@fetchPdfForRender == null) {
                    ctx.makeToastRecSus(com.pt.pro.R.string.xe, 0)
                    return@fetchPdfForRender
                }
                renderPdf(dis, pageNumber, {
                    ctx.makeToastRecSus(com.pt.pro.R.string.xe, 0)
                }) {
                    doIntiViews()
                }
            }
        }
    }

    private suspend fun android.graphics.Bitmap.doIntiViews() {
        withMain {
            binder?.zoomView?.apply {
                setExecutor(workManagerConfiguration.executor)
                com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.setPreferredBitmapConfig(android.graphics.Bitmap.Config.ARGB_8888)
                maxScale = 10F
                setEagerLoadingEnabled(false)
                runCatching {
                    setImage(com.davemorrissey.labs.subscaleview.ImageSource.bitmap(this@doIntiViews))
                }.onFailure {
                    ctx.makeToastRecSus(com.pt.pro.R.string.xe, 0)
                }
                setOnClickListener(this@ViewPdfFragment)
            }
        }
    }

    override fun doLockImg() {
        hideSystemUI()
        binding.zoomView.apply {
            isZoomEnabled = false
            setOnClickListener(null)
        }
    }

    override fun doUnLockImg() {
        showSystemUI()
        binding.zoomView.apply {
            isZoomEnabled = true
            setOnClickListener(this@ViewPdfFragment)
        }
    }

    @com.pt.common.global.UiAnn
    override fun hideOrShow() {
        if (hideSys) {
            showSystemUI()
        } else {
            hideSystemUI()
        }
        hideSys = !hideSys
    }

    @com.pt.common.global.UiAnn
    private fun hideSystemUI() {
        launchDef {
            withMain {
                act.window?.hideAllSystemUI()
                browserFileListener?.onHideCardView()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun showSystemUI() {
        launchDef {
            withMain {
                act.window?.showSystemUI()
                browserFileListener?.onShowCardView()
            }
        }
    }


    @com.pt.common.global.UiAnn
    override fun ZoomViewFasten.onClick(v: android.view.View) {
        hideOrShow()
    }

    @com.pt.common.global.MainAnn
    override fun onResume() {
        super.onResume()
        browserFileListener?.pdfFragment = this
    }

    override fun getWorkManagerConfiguration(): androidx.work.Configuration {
        return androidx.work.Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setDefaultProcessName(com.pt.pro.BuildConfig.APPLICATION_ID)
            .setExecutor(fetchExtractor)
            .build()
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        binder?.zoomView?.recycle()
        binder = null
        //touchListener = null
        super.onDestroyView()
    }

}
