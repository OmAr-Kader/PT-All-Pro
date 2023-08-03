package com.pt.pro.file.views.pdf

import androidx.core.widget.doAfterTextChanged
import com.pt.common.global.*
import com.pt.common.media.fetchFileFromUri
import com.pt.common.media.openInValid
import com.pt.common.media.writeInValid
import com.pt.common.mutual.life.GlobalFragment
import com.pt.common.stable.*
import com.pt.pro.file.fasten.FileInflater
import com.pt.pro.file.fasten.TextViewerFasten

class FragmentTextViewer : GlobalFragment<TextViewerFasten>(),
    android.view.View.OnTouchListener, com.pt.pro.file.interfaces.TextViewerListener {

    @Volatile
    private var isEditable: Boolean = true

    private var filePath: String? = null

    private var indexesNative: MutableList<Int>? = mutableListOf()
    private inline val allIndexes: MutableList<Int> get() = indexesNative!!

    private var currentIndex: DSackT<Int, Int> = DSackT(-1, 0)

    private inline val fetchColorAccent: Int
        get() = them.findAttr(android.R.attr.colorAccent)

    private inline val fetchPrimaryAccentColor: Int
        get() = them.findAttr(com.pt.pro.R.attr.primaryAccentColor)

    private var scaleGestureDetector: android.view.ScaleGestureDetector? = null
    private var isTouch = false
    private var ratioPlus: Float = 0.0F
    private var endScroll: Float = 0.0F
    private var intY: Float = 0.0F
    private var endHeight: Float = 0.0F
    private var endRatio: Float = 0.0F
    private var start: Float = 0.0F

    private var isDoInLand: Boolean = false
    private var margeWidth: Int = 0
    private var margeHeight: Int = 0
    private var marginNum: Int = 0

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null


    override fun DSack<Int, Int, Boolean>.setIntiMargin() {
        margeWidth = one
        margeHeight = two
        isDoInLand = three
    }

    override fun String.setFileUriAndPath() {
        filePath = this@setFileUriAndPath
    }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        get() = {
            FileInflater.run {
                this@creBin.context.inflaterTextViewer()
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root_
        }

    override fun TextViewerFasten.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentTextViewer)
        if (FileLate(filePath.toStr).exists()) {
            launchDef {
                doIntiTxt()
            }
        } else {
            launchDef {
                cont.openInValid(act.intent.data ?: return@launchDef) {
                    this?.fileDescriptor?.let {
                        it.readTextFileOpen {
                            binder?.intiViews(this@readTextFileOpen)
                        }
                    } ?: run {
                        ctx.fetchFileFromUri(act.intent.data ?: return@run, "txt.txt") {
                            isEditable = false
                            filePath = it
                            doIntiTxt()
                        }
                    }
                }
            }
        }
    }

    private fun doIntiTxt() {
        toCatchSack(652) {
            filePath?.let {
                binder?.picName?.text = com.pt.common.global.FileLate(
                    it
                ).let { a -> a.name ?: a.nameWithoutExtension }
            }
        }.postNow()
        launchDef {
            kotlinx.coroutines.delay(200L)
            withBack {
                readTextFile(filePath.toStr) {
                    binder?.intiViews(this@readTextFile)
                }
            }
        }
    }

    private fun TextViewerFasten.intiViews(str: String?) {
        launchMain {
            doIntiViews()
            withMain {
                if (str == null) {
                    ctx.makeToastRec(com.pt.pro.R.string.xe, 0)
                    return@withMain
                } else {
                    textViewer.apply {
                        setOnTouchListener(this@FragmentTextViewer)
                        text = str.toEditable
                    }
                }
                scaleGestureDetector = android.view.ScaleGestureDetector(
                    ctx,
                    SimpleOnScale(textViewer.textSize)
                )
                searchName.apply {
                    setOnEditTextImeBackListener()
                    doAfterTextChanged { ed ->
                        if (ed.isNullOrEmpty() or ed.isNullOrBlank()) {
                            textViewer.also { tv ->
                                tv.text?.let { ed ->
                                    tv.text = ed.toString().toEditable
                                }
                            }
                        } else {
                            textViewer.text?.toString()?.let { txt ->
                                forSearch(txt, ed.toString())
                            }
                        }
                    }
                }
                searchText.setOnClickListener(this@FragmentTextViewer)
                downText.setOnClickListener(this@FragmentTextViewer)
                upText.setOnClickListener(this@FragmentTextViewer)
                mainBack.setOnClickListener(this@FragmentTextViewer)
                zoomOut.setOnClickListener(this@FragmentTextViewer)
                zoomIn.setOnClickListener(this@FragmentTextViewer)
                rotateScreen.setOnClickListener(this@FragmentTextViewer)
                saveChanges.setOnClickListener(this@FragmentTextViewer)
                root_.fetchViewDim { _, rootHeight ->
                    updateDim(rootHeight.toFloat())
                    run.postAfter()
                    scrollProvider()
                }
            }
            withMain {
                textViewer.doAfterTextChanged {
                    if (textViewer.hasFocus() && saveChanges.isGon) {
                        saveChanges.justVisible()
                    }
                }
            }
        }
    }

    private inner class SimpleOnScale(
        size: Float
    ) : android.view.ScaleGestureDetector.SimpleOnScaleGestureListener() {
        private var newProduct: Float = size
        override fun onScale(detector: android.view.ScaleGestureDetector): Boolean {
            (newProduct * detector.scaleFactor).also { product ->
                newProduct = product
                toCatchSackAfter(318, 250L) {
                    binder?.textViewer?.apply {
                        if (textSize == product) return@toCatchSackAfter
                        setTextSize(
                            android.util.TypedValue.COMPLEX_UNIT_PX,
                            product
                        )
                    }
                }.postAfter()
            }
            return true
        }
    }

    private suspend fun TextViewerFasten.doIntiViews() {
        withMain {
            marginNum = getMarginNumber
            ctx.actionBarHeight.let { act ->
                frameForScroll.framePara(WRAP, MATCH) {
                    topMargin = act
                    gravity = android.view.Gravity.END
                }
                head.framePara(MATCH, act) {}
                frameRec.framePara(MATCH, MATCH) {
                    topMargin = act
                    bottomMargin = act
                }
                headInner.framePara(MATCH, MATCH) {
                    marginStart = marginNum
                    marginEnd = marginNum
                }
                headDown.framePara(MATCH, WRAP) {
                    gravity = android.view.Gravity.BOTTOM
                    marginStart = marginNum
                    marginEnd = marginNum
                }
                headOptions.framePara(MATCH, act) {
                    gravity = android.view.Gravity.BOTTOM
                }
                searchName.editAppearance()
                picName.editAppearance()
            }
        }
        withMain {
            them.findAttr(com.pt.pro.R.attr.rmoText).let {
                searchName.setTextColor(it)
                picName.setTextColor(it)
            }
        }
    }


    private inline val getMarginNumber: Int
        get() {
            return rec.isLandTraditional.let { isLand ->
                (isDoInLand == isLand).let { same ->
                    if (same && isLand) {
                        margeWidth
                    } else if (!same && isLand) {
                        if (rec.configuration.isTablet) {
                            margeWidth
                        } else {
                            margeHeight
                        }
                    } else if (same && !isLand) {
                        margeWidth
                    } else {
                        if (rec.configuration.isTablet) {
                            margeWidth
                        } else {
                            margeHeight
                        }
                    }
                }
            }
        }

    @com.pt.common.global.MainAnn
    private fun updateDim(heightPar: Float) {
        (ctx.actionBarHeight.toFloat() + rec.statusBarHeight).let { c ->
            start = c
            endRatio = heightPar - c - 70F.toPixel - margeHeight
            endHeight = endRatio + start
        }
    }


    private fun TextViewerFasten.scrollProvider() {
        pointerScroll.addSenseListener(false) { _, event, type ->
            when (type) {
                DOWN_SEN -> {
                    isTouch = true
                    intY = event.rawY - start
                    unPost(run.two)
                }
                UP_SEN -> {
                    isTouch = false
                    intY = event.rawY - start
                    run.postAfter()
                }
                MOVE_SEN -> {
                    if (textViewer.text.isNullOrEmpty()) return@addSenseListener
                    endScroll = when {
                        event.rawY < start -> 0.0F
                        event.rawY > endHeight -> endRatio
                        else -> event.rawY - start
                    }
                    binding.textViewer.lineCount.let { lc ->
                        ratioPlus = when (endScroll) {
                            0.0F -> 0F
                            endRatio -> (lc - 1).toFloat()
                            else -> lc * (kotlin.math.abs(endScroll) / endRatio)
                        }
                        binding.apply {
                            pointerScroll.y = endScroll
                            scroll.scrollTo(
                                0,
                                textViewer.layout.getLineTop(ratioPlus.toInt())
                            )
                        }
                    }
                }
            }
        }
    }

    private val run: DSack<() -> Unit, Int, Long>
        get() = toCatchSackAfter(11, 200L) {
            binder?.apply {
                textViewer.apply {
                    layout?.getLineForVertical(scroll.scrollY)?.let { d ->
                        ((d.toFloat() / lineCount.toFloat()) * endRatio).let { f ->
                            pointerScroll.also {
                                if (f != it.y) it.y = f else return@also
                            }
                        }
                    }
                }
                run.postAfter()
            }
            Unit
        }

    @android.annotation.SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: android.view.View?, event: android.view.MotionEvent): Boolean {
        return if (event.pointerCount == 2) {
            scaleGestureDetector?.onTouchEvent(event)
            true
        } else {
            false
        }
    }

    private var jobNativeSearch: kotlinx.coroutines.Job? = null


    private fun TextViewerFasten.forSearch(
        text: String,
        word: String
    ) {
        jobNativeSearch?.cancelJob()
        jobNativeSearch = launchDef {
            changeBackTextColor(text, word)
        }
    }

    private suspend fun TextViewerFasten.changeBackTextColor(
        text: String,
        word: String
    ) {
        fetchIndexesOfText(text, word) {
            doChangeBackTextColor(
                this@fetchIndexesOfText,
                text,
                fetchColorAccent,
                fetchPrimaryAccentColor
            )
        }
    }

    private suspend inline fun fetchIndexesOfText(
        text: String,
        word: String,
        crossinline b: suspend Sequence<DSackT<Int, Int>>.() -> Unit
    ) {
        withBackDef(null) {
            val indexes: MutableList<DSackT<Int, Int>> = mutableListOf()

            var index: Int = text.indexOf(word, ignoreCase = true)
            val matchLength: Int = word.length
            if (index != -1) {
                indexes.add(DSackT(index, index + matchLength))
            }
            while (index >= 0) {
                println(index)
                index = text.indexOf(word, index + matchLength, ignoreCase = true)
                if (index != -1) {
                    indexes.add(DSackT(index, index + matchLength))
                }
            }
            indexes.distinct().asSequence()
        }?.letSusBack(b)
    }


    private suspend fun TextViewerFasten.doChangeBackTextColor(
        b: Sequence<DSackT<Int, Int>>,
        text: String,
        c: Int,
        tx: Int
    ) {
        withBackDef(text) {
            if (b.count() == 0) {
                indexesNative = mutableListOf()
                return@withBackDef (textViewer.text?.toString() ?: "").toEditable
            }
            indexesNative = b.map { it.one }.toMutableList()
            android.text.SpannableStringBuilder(text).alsoSusBack { spannable ->
                b.onEachBack(ctx) {
                    spannable.setSpan(
                        android.text.style.BackgroundColorSpan(c),
                        one,
                        two,
                        33
                    )
                    spannable.setSpan(
                        android.text.style.ForegroundColorSpan(tx),
                        one,
                        two,
                        33
                    )
                    // android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                }
            }
        }.letSus {
            withMain {
                textViewer.setTextKeepState(it, android.widget.TextView.BufferType.SPANNABLE)
            }
        }
    }


    override fun TextViewerFasten.onClick(v: android.view.View) {
        when (v) {
            rotateScreen -> doRotateScreen()
            zoomOut -> textViewer.changeZoom(ifIn = false)
            zoomIn -> textViewer.changeZoom(ifIn = true)
            mainBack -> requireParentFragment().childFragmentManager.popBackStack()
            searchText -> changeSearchVis()
            upText -> textViewer.changePointer(isUp = false)
            downText -> textViewer.changePointer(isUp = true)
            saveChanges -> {
                textViewer.text?.saveChanged() ?: ctx.makeToastRec(com.pt.pro.R.string.vd, 0)
            }
        }
    }

    private fun androidx.appcompat.widget.AppCompatEditText.changePointer(isUp: Boolean) {
        if (allIndexes.isEmpty()) return
        hideSearchKeyAndShowAnother {
            if (isUp) {
                currentIndex = if (currentIndex.one >= allIndexes.size - 1) {
                    DSackT(0, allIndexes.elementAt(0))
                } else {
                    DSackT(currentIndex.one + 1, allIndexes.elementAt(currentIndex.one + 1))
                }
                setSelection(currentIndex.two)
            } else {
                currentIndex = if (currentIndex.one <= 0) {
                    DSackT(allIndexes.size - 1, allIndexes.last())
                } else {
                    DSackT(currentIndex.one - 1, allIndexes.elementAt(currentIndex.one - 1))
                }
                setSelection(currentIndex.two)
            }
        }
    }

    private fun TextViewerFasten.changeSearchVis() {
        if (downText.isVis) {
            textViewer.also { tv ->
                tv.text?.let { ed ->
                    tv.text = ed.toString().toEditable
                }
            }
            hideSearch()
        } else {
            showSearch()
        }
    }

    override fun invoke() {
        binder?.hideSearch()
    }

    private fun TextViewerFasten.showSearch() {
        picName.justGone()
        downText.justVisible()
        upText.justVisible()
        searchName.justVisible()
        ctx.launchKeyBoard {
            if (!isAcceptingText) {
                toCatchSack(48) {
                    searchName.apply {
                        this@apply.requestFocus()
                        showSoftInput(this@apply, 2)
                    }
                }.postNow()
            }
        }
    }

    private fun TextViewerFasten.hideSearch() {
        searchName.text?.clear()
        picName.justVisible()
        downText.justGone()
        upText.justGone()
        searchName.justGone()
        ctx.launchKeyBoard {
            if (isAcceptingText) {
                hideSoftInputFromWindow(searchName.windowToken, 0)
            }
        }
    }

    private fun androidx.appcompat.widget.AppCompatEditText.hideSearchKeyAndShowAnother(
        a: () -> Unit
    ) {
        if (!isFocused) {
            catchyUnit {
                ctx.launchKeyBoard {
                    this.hideSoftInputFromWindow(binder?.searchName?.windowToken, 0)
                }
            }
            catchyUnit {
                this@hideSearchKeyAndShowAnother.requestFocus()
            }
            a.invoke()
        } else a.invoke()
    }

    private fun androidx.appcompat.widget.AppCompatEditText.changeZoom(ifIn: Boolean) {
        act.runOnUiThread {
            textSize.let {
                setTextSize(
                    android.util.TypedValue.COMPLEX_UNIT_PX,
                    if (ifIn) it + 1.5F else if (it > 1.0F) it - 1.5F else it
                )
            }
        }
    }

    @android.annotation.SuppressLint("SourceLockedOrientationActivity")
    private fun doRotateScreen() {
        if (!rec.isLandTraditional) {
            act.requestedOrientation =
                android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            act.requestedOrientation =
                android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }


    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        launchMain {
            withMain {
                binder?.applySus {
                    doIntiViews()
                    root_.fetchViewDim { _, rootHeight ->
                        updateDim(rootHeight.toFloat())
                    }
                }
            }
        }
    }

    override fun TextViewerFasten.onLongClick(v: android.view.View): Boolean {
        return true
    }

    private fun android.text.Editable.saveChanged() {
        if (!isEditable) {
            ctx.makeToastRec(com.pt.pro.R.string.xe, 0)
            return
        }
        launchDef {
            if (FileLate(filePath.toStr).exists()) {
                writeTextFile(filePath.toStr, this@saveChanged.toString()) { b ->
                    launchDef {
                        withDefault {
                            android.media.MediaScannerConnection.scanFile(
                                ctx,
                                arrayOf(filePath.toStr),
                                null,
                                null
                            )
                        }
                    }
                    launchMain {
                        afterSave(b)
                    }
                }
            } else {
                cont.writeInValid(act.intent.data ?: return@launchDef) {
                    this?.fileDescriptor?.let {
                        it.writeTextFileOpen(this@saveChanged.toString()) { b ->
                            launchMain {
                                afterSave(b)
                            }
                        }
                    } ?: ctx.makeToastRecSus(com.pt.pro.R.string.xe, 0)
                }
            }
        }
    }

    private suspend fun afterSave(b: Boolean) {
        withMain {
            if (b) {
                binder?.saveChanges.justGone()
                ctx.makeToastRec(com.pt.pro.R.string.dn, 0)
            } else {
                ctx.makeToastRec(com.pt.pro.R.string.xe, 0)
            }
        }
    }

    override fun onPause() {
        context?.launchKeyBoard {
            if (isAcceptingText) {
                hideSoftInputFromWindow((binder ?: return@launchKeyBoard).searchName.windowToken, 0)
            }
        }
        super.onPause()
    }

    override fun onDestroyView() {
        binder?.searchName?.onViewDestroy()
        indexesNative = null
        scaleGestureDetector = null
        jobNativeSearch?.cancelJob()
        jobNativeSearch = null
        super.onDestroyView()
    }

}