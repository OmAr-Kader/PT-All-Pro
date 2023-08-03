package com.pt.pro.extra.views

import com.pt.common.global.*
import com.pt.common.mutual.life.GlobalSimpleFragment
import com.pt.common.stable.*
import com.pt.pro.R.string
import com.pt.pro.databinding.FragmentAboutSettingBinding

class FragmentAbout : GlobalSimpleFragment<FragmentAboutSettingBinding>() {

    override var lastJob: kotlinx.coroutines.Job? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        get() = {
            FragmentAboutSettingBinding.inflate(this@creBin, this, false).also {
                @ViewAnn
                binder = it
            }.root
        }

    @android.annotation.SuppressLint("SetTextI18n")
    override fun FragmentAboutSettingBinding.onViewCreated() {
        lifecycle.addObserver(this@FragmentAbout)
        pushJob {
            launchImdMain {
                withMain {
                    ctx.fetchLicenseText(com.pt.pro.R.dimen.tsg, android.R.attr.colorAccent) {
                        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                        textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                        setTextSus(string.qv)
                        setPadding(0, 10, 0, 10)
                        isAllCaps = true
                        setOnClickListener {
                            shareTextProvider(com.pt.common.BuildConfig.APP_LINK) {
                                this@FragmentAbout.startActivity(this)
                            }
                        }
                        frameAbout.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText(com.pt.pro.R.dimen.tsg, com.pt.pro.R.attr.rmoText) {
                        text = rec.getString(string.qx) + " " + com.pt.pro.BuildConfig.VERSION_NAME
                        setPadding(0, 12, 0, 12)
                        isAllCaps = true
                        frameAbout.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText(com.pt.pro.R.dimen.txt, com.pt.pro.R.attr.rmoText) {
                        text =
                            (rec.getString(string.iq) + "\n\n" + rec.getString(string.app_description))
                        setPadding(0, 12, 0, 12)
                        frameAbout.addViewSus {
                            this@fetchLicenseText
                        }
                    }
////////////////////////////////////////////////////////////////////////////////////////////////////
                    ctx.fetchLicenseText(com.pt.pro.R.dimen.tsg, android.R.attr.colorAccent) {
                        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                        textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                        setTextSus(string.qc)
                        setPadding(0, 10, 0, 10)
                        isAllCaps = true
                        setOnClickListener {
                            android.content.Intent(
                                android.content.Intent.ACTION_VIEW,
                                com.pt.common.BuildConfig.PRIVACY_POLICY.toUri,
                            ).also(::startActivity)
                        }
                        framePrivacy.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText(com.pt.pro.R.dimen.tsg, android.R.attr.colorAccent) {
                        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                        textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                        setTextSus(string.qn)
                        setPadding(0, 12, 0, 12)
                        isAllCaps = true
                        setOnClickListener {
                            doSendMail(DSack(com.pt.common.BuildConfig.EMAIL, "", "")) {
                                runCatching<Unit> {
                                    this@FragmentAbout.startActivity(this@doSendMail)
                                }.onFailure {
                                    ctx.makeToastRec(string.nm, 0)
                                }
                            }
                        }
                        framePrivacy.addViewSus {
                            this@fetchLicenseText
                        }
                    }
////////////////////////////////////////////////////////////////////////////////////////////////////
                    ctx.fetchLicenseText {
                        setTextSus(string.lis_kotlin)
                        frameLicense.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText {
                        setTextSus(string.lis_glide)
                        frameLicense.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText {
                        setTextSus(string.lis_sub)
                        frameLicense.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText {
                        setTextSus(string.lis_panel)
                        frameLicense.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText {
                        setTextSus(string.lis_pick)
                        frameLicense.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText {
                        setTextSus(string.lis_rip)
                        frameLicense.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText {
                        setTextSus(string.lis_ala)
                        frameLicense.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText {
                        setTextSus(string.lis_soup)
                        frameLicense.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                    ctx.fetchLicenseText {
                        setTextSus(string.lis_tagger)
                        frameLicense.addViewSus {
                            this@fetchLicenseText
                        }
                    }
                }
                withMain {
                    frameLicenseText.setOnClickListener(this@FragmentAbout)
                    framePrivacyText.setOnClickListener(this@FragmentAbout)
                    frameAboutText.setOnClickListener(this@FragmentAbout)
                    mainBack.setOnClickListener(this@FragmentAbout)
                }

            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun FragmentAboutSettingBinding.onClick(v: android.view.View) {
        when (v) {
            mainBack -> act.onBackPressedDispatcher.onBackPressed()
            frameLicenseText -> displayLicAndLib()
            framePrivacyText -> displayPrivacy()
            frameAboutText -> displayAbout()
        }
    }

    private fun FragmentAboutSettingBinding.displayPrivacy() {
        pushJob {
            launchDef {
                withMain {
                    if (framePrivacy.isVis) {
                        framePrivacy.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            privacyImage.startAnimation(this)
                        }
                        mainFramePrivacy.goneHandlerSus()
                    } else {
                        mainFramePrivacy.justVisibleSus()
                        framePrivacy.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            privacyImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    private fun FragmentAboutSettingBinding.displayAbout() {
        pushJob {
            launchDef {
                withMain {
                    if (frameAbout.isVis) {
                        frameAbout.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            aboutImage.startAnimation(this)
                        }
                        mainFrameAbout.goneHandlerSus()
                    } else {
                        mainFrameAbout.justVisibleSus()
                        frameAbout.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            aboutImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    private fun FragmentAboutSettingBinding.displayLicAndLib() {
        pushJob {
            launchDef {
                withMain {
                    if (frameLicense.isVis) {
                        frameLicense.invisibleTopSus()
                        animateRotation(90F, 0F, 300L) {
                            licenseImage.startAnimation(this)
                        }
                        mainFrameLicense.goneHandlerSus()
                    } else {
                        mainFrameLicense.justVisibleSus()
                        frameLicense.visibleTopSus(300)
                        animateRotation(0F, 90F, 300L) {
                            licenseImage.startAnimation(this)
                        }
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.apply {
            frameSetting.myActMarginCon(ctx.actionBarHeight)
            cardSetting.cardAsView(ctx.actionBarHeight)
        }
    }

    override fun onDestroyView() {
        binder?.frameAbout?.removeAllViewsInLayout()
        binder?.root?.removeAllViewsInLayout()
        com.pt.pro.extra.fasten.ExtraInflater.destroyFasten()
        super.onDestroyView()
    }

}