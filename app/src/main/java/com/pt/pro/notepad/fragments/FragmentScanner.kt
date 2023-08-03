package com.pt.pro.notepad.fragments

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import com.pt.common.global.*
import com.pt.common.media.getImagePathFromURI
import com.pt.common.media.isImageViewIntent
import com.pt.common.media.uriProviderNormal
import com.pt.common.mutual.life.GlobalFragment
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.databinding.FragmentScannerBinding
import com.pt.pro.notepad.models.DataKeeperItem
import com.pt.pro.notepad.objects.KEEP_SCAN
import com.pt.pro.notepad.objects.noteDb
import com.pt.web.TextRecognizer

class FragmentScanner : GlobalFragment<FragmentScannerBinding>() {

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        get() = {
            FragmentScannerBinding.inflate(this@creBin, this, false).also {
                binder = it
            }.root
        }

    override fun FragmentScannerBinding.onViewCreated() {
        editText.applyEdit(them.findAttr(R.attr.rmoText))
        act.apply {
            if (intent.isImageViewIntent && intent.data != null) {
                intent.data?.let {
                    launchDef {
                        justCoroutineBack {
                            val path = ctx.getImagePathFromURI(it).toStr
                            doDetectedText(path)
                        }
                    }
                }
            } else {
                if (intent.getBooleanExtra(SCANNER_IS_GALLERY, true)) {
                    sendToMain.justGone()
                    runCatching {
                        intent.getStringExtra(SCANNER_GALLERY_PATH)?.let {
                            doDetectedText(it)
                        }
                    }
                } else {
                    getImagePicker()
                    actions.justGone()
                }
            }
        }

        root.doOnLayout {
            editText.setText("")
            backData.setOnClickListener(this@FragmentScanner)
            actions.apply {
                setOnClickListener(this@FragmentScanner)
                setOnLongClickListener(this@FragmentScanner)
            }
            google.apply {
                setOnClickListener(this@FragmentScanner)
                setOnLongClickListener(this@FragmentScanner)
            }
            translate.apply {
                setOnClickListener(this@FragmentScanner)
                setOnLongClickListener(this@FragmentScanner)
            }
            share.apply {
                setOnClickListener(this@FragmentScanner)
                setOnLongClickListener(this@FragmentScanner)
            }
            sendToMain.apply {
                setOnClickListener(this@FragmentScanner)
                setOnLongClickListener(this@FragmentScanner)
            }
            scannerAgain.apply {
                setOnClickListener(this@FragmentScanner)
                setOnLongClickListener(this@FragmentScanner)
            }
        }
    }

    override fun FragmentScannerBinding.onClick(v: android.view.View) {
        when (v) {
            scannerAgain -> getImagePicker()
            sendToMain -> doSaveAndFinish(editText.text.toString())
            share -> doShareText(editText.text.toString())
            translate -> doTranslateLauncher(editText.text.toString())
            google -> ctx.doSearchLauncher(editText.text.toString())
            actions -> doActionsDisplay()
            backData -> act.finish()
        }
    }

    private fun getImagePicker() {
        ctx.findPicker(com.pt.pro.BuildConfig.APPLICATION_ID) {
            resultActivity?.launch(this)
        }
    }

    override fun FragmentScannerBinding.onLongClick(v: android.view.View): Boolean {
        when (v) {
            scannerAgain -> R.string.fy
            sendToMain -> R.string.se
            share -> R.string.pk
            translate -> R.string.pz
            google -> R.string.wy
            actions -> R.string.ew
            else -> 0
        }.let {
            v.popUpComment(it, R.attr.rmoBackground, (-1 * 120F.toPixel))
        }
        return true
    }

    private fun FragmentScannerBinding.doActionsDisplay() {
        launchDef {
            withMain {
                if (google.isGon) {
                    animateRotation(0F, -180F, 300L) {
                        actions.startAnimation(this)
                    }
                    translate.visibleFade(200)
                    google.visibleFade(250)
                    share.visibleFade(300)
                    translateText.justVisible()
                    googleText.justVisible()
                    shareText.justVisible()
                } else {
                    animateRotation(-180F, 0F, 300L) {
                        actions.startAnimation(this)
                    }
                    translate.goneFade(300)
                    google.goneFade(250)
                    share.goneFade(200)
                    translateText.justGone()
                    googleText.justGone()
                    shareText.justGone()
                }
            }
        }
    }

    private fun android.content.Context.doSearchLauncher(txt: String) {
        if (txt.isEmpty()) {
            ctx.makeToastRec(R.string.vd, 0)
            return
        }
        findIntegerPref(
            ID_SEARCH,
            SEARCH_ENGINE,
            0
        ).let {
            catchy(Unit) {
                searchProvider(txt, it) {
                    this@FragmentScanner.startActivity(this)
                }
            }
        }
    }

    private fun doTranslateLauncher(txt: String) {
        if (txt.isEmpty()) {
            ctx.makeToastRec(R.string.vd, 0)
            return
        }
        ctx.findIntegerPref(
            ID_SEARCH,
            SEARCH_ENGINE,
            0
        ).let {
            translateProvider(txt, it) {
                ctx.apply {
                    sendToClipDate(txt)
                    this@FragmentScanner.startActivity(this@translateProvider)
                }
            }
        }
    }

    private fun doShareText(txt: String) {
        if (txt.isEmpty()) {
            ctx.makeToastRec(R.string.vd, 0)
            return
        }
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, txt)
            startActivity(this)
        }
    }

    private fun doSaveAndFinish(txt: String) {
        if (txt.isEmpty()) {
            ctx.makeToastRec(R.string.vd, 0)
            return
        }
        launchDef {
            withBack {
                act.intent.getStringExtra(TABLE_NAME)?.letSusBack { ss ->
                    val tim = System.currentTimeMillis()
                    ctx.newDBDataSus(ss.noteDb) {
                        DataKeeperItem(
                            dataText = txt,
                            timeData = tim,
                            imageUrl = null,
                            keeperType = KEEP_SCAN,
                            emailSubject = null,
                            emailToSome = null,
                            recordPath = null,
                            recordLength = 0L,
                            dayNum = tim.fetchCalenderDay,
                            isDone = false
                        ).runSusBack {
                            insertMsg(ss)
                        }
                    }
                }
            }
            act.finishSus()
        }
    }

    private fun doDetectedText(path: String) {
        launchDef {
            TextRecognizer(ctx.applicationContext) {
                launchMain {
                    if (it.isNullOrEmpty() || it.isBlank()) {
                        ctx.makeToastRec(R.string.xd, 1)
                    } else {
                        binding.editText.append(it)
                    }
                }
            }.apply {
                detectedText(ctx.uriProviderNormal(path, com.pt.pro.BuildConfig.APPLICATION_ID))
            }
        }
    }

    private var resultActivity: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                launchDef {
                    withBackDef(null) {
                        return@withBackDef runCatching {
                            return@runCatching if (it.data == null) {
                                ctx.getCaptureImageOutputUri(com.pt.pro.BuildConfig.APPLICATION_ID)?.path
                            } else {
                                ctx.getImagePathFromURI(it?.data?.data ?: return@withBackDef null)
                            }
                        }.getOrNull()
                    }.let { pathPick ->
                        withBack {
                            if (pathPick == null) {
                                ctx.makeToastRecSus(R.string.fi, 0)
                                return@withBack
                            }
                            doDetectedText(pathPick)
                        }
                    }
                }
            }
        }

    override fun onDestroyView() {
        resultActivity?.unregister()
        resultActivity = null
        super.onDestroyView()
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.frameScanner.myActMargin(ctx.actionBarHeight)
        binding.cardScanner.cardAsView(ctx.actionBarHeight)
    }

}