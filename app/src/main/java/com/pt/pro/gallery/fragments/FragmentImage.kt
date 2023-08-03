package com.pt.pro.gallery.fragments

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.isDirHaveGif
import com.pt.common.stable.launchDef
import com.pt.common.stable.launchImdMain
import com.pt.common.stable.withMainNormal

class FragmentImage : com.pt.common.mutual.life.GlobalSimpleFragment<com.pt.pro.gallery.fasten.ZoomViewFasten>(),
    com.pt.pro.gallery.interfaces.ImageListener, androidx.work.Configuration.Provider {

    override var mediaHolder: MediaSack? = null
    override var browserListener: com.pt.pro.gallery.interfaces.BrowserListener? = null

    private var isFailed = false

    override var hideSys: Boolean = false

    private var uriNative: android.net.Uri? = null
    private inline val fetchUri: android.net.Uri
        get() = uriNative ?: mediaHolder?.uriMedia.toUriNull ?: ctx.uriProviderNormal(
            mediaHolder?.pathMedia,
            com.pt.pro.BuildConfig.APPLICATION_ID
        ).also {
            uriNative = it
        }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        get() = {
            com.pt.pro.gallery.fasten.GalleryInflater.run { this@creBin.context.inflaterZoomView() }.also {
                binder = it
            }.root_
        }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.ZoomViewFasten.onViewCreated() {
        launchImdMain {
            intiImageView()
        }
    }

    private suspend fun com.pt.pro.gallery.fasten.ZoomViewFasten.intiImageView() {
        cont.getExifOrientation(fetchUri.toStr).let {
            com.pt.common.stable.withMain {
                zoomView.apply {
                    setExecutor(workManagerConfiguration.executor)
                    com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.setPreferredBitmapConfig(android.graphics.Bitmap.Config.ARGB_8888)
                    maxScale = 10F
                    setEagerLoadingEnabled(false)
                    orientation = it
                    runCatching {
                        if (FileLate(mediaHolder?.pathMedia.toStr).extension.trim().isDirHaveGif) {
                            isFailed = true
                            this@FragmentImage.context?.loadAsGif(mediaHolder?.copy(uriMedia = fetchUri.toStr) ?: return@runCatching) {
                                launchImdMain {
                                    setImage(com.davemorrissey.labs.subscaleview.ImageSource.bitmap(this@loadAsGif ?: return@launchImdMain))
                                }
                            }
                        } else {
                            withMainNormal {
                                setImage(com.davemorrissey.labs.subscaleview.ImageSource.uri(fetchUri))
                            }
                        }
                    }.onFailure {
                        isFailed = true
                        this@FragmentImage.context.loadRealImage(fetchUri) {
                            launchImdMain {
                                setImage(com.davemorrissey.labs.subscaleview.ImageSource.bitmap(this@loadRealImage))
                            }
                        }
                    }
                    setOnClickListener(this@FragmentImage)
                }
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
            setOnClickListener(this@FragmentImage)
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
            com.pt.common.stable.withMain {
                act.window?.hideAllSystemUI()
                browserListener?.onHideCardView()
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun showSystemUI() {
        launchDef {
            com.pt.common.stable.withMain {
                act.window?.showSystemUI()
                browserListener?.onShowCardView()
            }
        }
    }


    @com.pt.common.global.UiAnn
    override fun com.pt.pro.gallery.fasten.ZoomViewFasten.onClick(v: android.view.View) {
        hideOrShow()
    }

    @com.pt.common.global.MainAnn
    override fun onResume() {
        super.onResume()
        browserListener?.imageFragment = this
    }

    override fun getWorkManagerConfiguration(): androidx.work.Configuration {
        return androidx.work.Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .setDefaultProcessName(com.pt.pro.BuildConfig.APPLICATION_ID)
            .setExecutor(com.pt.common.stable.fetchExtractor)
            .build()
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        binder?.zoomView?.apply {
            if (isFailed) {
                com.pt.common.stable.catchyUnit {
                    recycle()
                }
            }
        }
        mediaHolder = null
        browserListener = null
        uriNative = null
        super.onDestroyView()
    }

}
