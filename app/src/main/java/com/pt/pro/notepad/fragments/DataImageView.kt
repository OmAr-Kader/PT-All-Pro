package com.pt.pro.notepad.fragments

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.setPreferredBitmapConfig
import com.pt.common.global.addSenseListener
import com.pt.common.stable.fetchExtractor
import com.sothree.slidinguppanel.SlidingUpPanelLayout as Slider

class DataImageView @JvmOverloads constructor(
    private var image: String? = null
) : androidx.fragment.app.Fragment(), androidx.work.Configuration.Provider {

    private inline val binding get() = binder!!

    private var binder: com.pt.pro.databinding.ShowDataImageBinding? = null

    private var slideOff = 0.0F
    private var slideState = Slider.PanelState.EXPANDED

    companion object {
        fun newInstance(image: String?): DataImageView = DataImageView(image)
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View = androidx.viewbinding.ViewBinding {
        com.pt.pro.databinding.ShowDataImageBinding.inflate(inflater, container, false).also {
            binder = it
        }.root
    }.root

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.zoomViewData.apply {
            setExecutor(workManagerConfiguration.executor)
            setPreferredBitmapConfig(android.graphics.Bitmap.Config.ARGB_8888)
            setEagerLoadingEnabled(false)
            android.net.Uri.fromFile(com.pt.common.global.FileLate(image.toString())).let {
                setImage(
                    com.davemorrissey.labs.subscaleview.ImageSource.uri(it)
                )
            }
            maxScale = 5.75f
        }
        binding.slidingPanelData.apply {
            panelState = Slider.PanelState.EXPANDED
            addSenseListener(false) { _, _, type ->
                if (type == com.pt.common.stable.UP_SEN) {
                    @Suppress("LongLine") if (slideOff < 0.3647541F && isAn) {
                        requireParentFragment().childFragmentManager.popBackStack()
                    }
                }
            }
            addPanelSlideListener(slideListener)
        }
    }

    private val isAn: Boolean
        get() = slideState == Slider.PanelState.ANCHORED

    private var slideListener: Slider.PanelSlideListener? = object : Slider.PanelSlideListener {
        override fun onPanelSlide(panel: android.view.View, slideOffset: Float) {
            slideOff = slideOffset
        }

        override fun onPanelStateChanged(
            panel: android.view.View,
            previousState: Slider.PanelState,
            newState: Slider.PanelState
        ) {
            if (newState == Slider.PanelState.COLLAPSED) {
                requireParentFragment().childFragmentManager.popBackStack()
            }
            slideState = newState
        }
    }


    override fun getWorkManagerConfiguration(): androidx.work.Configuration {
        return androidx.work.Configuration.Builder().setMinimumLoggingLevel(android.util.Log.INFO)
            .setDefaultProcessName(com.pt.pro.BuildConfig.APPLICATION_ID)
            .setExecutor(fetchExtractor).build()
    }

    override fun onDestroyView() {
        binder = null
        image = null
        slideListener = null
        super.onDestroyView()
    }
}