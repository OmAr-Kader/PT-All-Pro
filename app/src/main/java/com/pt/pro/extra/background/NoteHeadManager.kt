package com.pt.pro.extra.background

import com.pt.common.global.*
import com.pt.common.stable.*
import com.pt.pro.notepad.objects.noteDb

class NoteHeadManager(ctx: android.content.Context?) : com.pt.common.mutual.life.GlobalServiceManger(ctx), com.pt.pro.extra.interfaces.NoteListener,
        () -> Unit {

    private var isRight = false
    private var beforeYouGo = 0

    private var visible = false

    override val headRestBall: com.pt.pro.databinding.NoteHeadBinding get() = viewRootNull!!
    private var viewRootNull: com.pt.pro.databinding.NoteHeadBinding? = null

    override val headMine: com.pt.pro.databinding.FloatingHeadBinding get() = viewRootTwo!!
    private var viewRootTwo: com.pt.pro.databinding.FloatingHeadBinding? = null

    private var para: android.view.WindowManager.LayoutParams? = null

    override val windowManage: android.view.WindowManager get() = winManager ?: ctxS.fetchSystemWindowManager.also { winManager = it }
    private var winManager: android.view.WindowManager? = null

    private var screenWidth: Int? = null
    private var screenHeight: Int? = null
    internal inline fun musicDim(a: (wid: Int, hei: Int) -> Unit) {
        screenWidth.let { w ->
            screenHeight.let { h ->
                if (w != null && h != null) {
                    a.invoke(w, h)
                } else {
                    ctxS.fetchDimensions {
                        a.invoke(width, height)
                        screenWidth = this@fetchDimensions.width
                        screenHeight = this@fetchDimensions.height
                    }
                }
            }
        }
    }

    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    private var xBefore: Int? = null
    private var yBefore: Int? = null
    private var landXBefore: Int? = null
    private var landYBefore: Int? = null

    override val fetchParams: android.view.WindowManager.LayoutParams
        get() = para ?: floatingWindowManger().also { para = it }

    override val getViewRoot: android.view.View
        get() = headMine.root

    override val getViewRoot2: android.view.View
        get() = headRestBall.root


    @com.pt.common.global.MainAnn
    override fun com.pt.pro.databinding.FloatingHeadBinding.setViewRoot() {
        @com.pt.common.global.ViewAnn
        viewRootTwo = this
    }

    @com.pt.common.global.MainAnn
    override fun com.pt.pro.databinding.NoteHeadBinding.setLayout2() {
        @com.pt.common.global.ViewAnn
        viewRootNull = this
    }

    override fun setInit() {
        ctxS.fetchDimensions {
            screenWidth = this@fetchDimensions.width
            screenHeight = this@fetchDimensions.height
            para = floatingWindowManger()
            fetchParams.x = this@fetchDimensions.width / 2
            fetchParams.y = -100
        }
        isRight = recS.isRightToLeft
    }

    override fun android.view.WindowManager.setWindowManager() {
        winManager = this
    }

    override fun updateViewRoot() {
        viewRootTwo?.apps?.apply {
            8F.toPixelS.also { setPadding(it, it, it, it) }
            ctxS.compactImage(com.pt.pro.R.drawable.ic_data) {
                setImageDrawable(this@compactImage)
            }
            svgReColor(ctxS.theme.findAttr(android.R.attr.textColorPrimary))
            touchListener?.setContactListener(true)
        }
        viewRootNull?.apply {
            if (isV_M) {
                ctxS.compactImage(com.pt.pro.R.drawable.ripple_oval) {
                    backButton.foreground = this@compactImage
                }
                ctxS.compactImage(com.pt.pro.R.drawable.ripple_oval) {
                    sendButton.foreground = this@compactImage
                }
            }
            backButton.setOnClickListener {
                changeVisibility()
            }
            sendButton.setOnClickListener {
                editMessage.text.let { ed ->
                    if (ed != null && ed.isNotBlank() && ed.isNotEmpty()) {
                        if (androidx.core.text.util.LinkifyCompat.addLinks(ed, android.text.util.Linkify.WEB_URLS)) {
                            saveNote(ed.toStr, com.pt.pro.notepad.objects.KEEP_LINK)
                        } else {
                            saveNote(ed.toStr, com.pt.pro.notepad.objects.KEEP_DATA)
                        }
                    } else {
                        ctxS.makeToastRec(com.pt.pro.R.string.vd, 0)
                    }
                }
            }
            editMessage.apply editMessage@{
                this@editMessage.addSenseListener(false) { _, _, i ->
                    if (i == DOWN_SEN) {
                        searchProvide()
                    }
                }
                setOnEditTextImeBackListener()
            }
        }
    }

    private suspend fun com.pt.pro.notepad.models.DataKeeperItem.searchForLinkImage(msg: String, s1: String, t: Long) {
        withDefault {
            com.pt.web.WebTextProvider(ctxS) { table, _, title, img ->
                context?.apply {
                    copy(
                        emailToSome = title?.toDefString?.let { it(200) },
                        emailSubject = img
                    ).also { d ->
                        newDBData(table.noteDb) {
                            d.upDateRecord(table)
                        }
                    }
                }
            }.apply {
                webTitleProvider(link = msg, time = t, table = s1)
            }
        }
    }

    private fun saveNote(msg: String, type: Int) {
        launchDef {
            withBack {
                val tim = System.currentTimeMillis()
                com.pt.pro.notepad.models.DataKeeperItem(
                    dataText = msg,
                    keeperType = type,
                    emailToSome = null,
                    emailSubject = null,
                    timeData = tim,
                    recordPath = null,
                    recordLength = 0L,
                    imageUrl = null,
                    dayNum = tim.fetchCalenderDay,
                    isDone = false,
                ).applySusBack {
                    ctxS.findStringPrefNullable(LAST_NOTE_TABLE)?.letSusBack { last ->
                        ctxS.newDBDataSus((last).noteDb) {
                            insertMsg(last)
                        }
                        justScope {
                            if (type == com.pt.pro.notepad.objects.KEEP_LINK) {
                                searchForLinkImage(msg = msg, s1 = last, t = tim)
                            } else return@justScope
                        }
                        withMain {
                            viewRootNull?.editMessage?.setText("")
                        }
                    }
                }
            }
        }
    }

    private var touchListener: com.pt.common.moderator.touch.ContactListener? = object : com.pt.common.moderator.touch.ContactListener {

        private var initialX = 0
        private var initialY = 0
        private var initialTouchX = 0f
        private var initialTouchY = 0f

        override fun onUp(v: android.view.View) {

        }

        override fun onUp(v: android.view.View, event: android.view.MotionEvent) {
            if (initialX == fetchParams.x && initialY == fetchParams.y) {
                viewRootNull?.changeVisibility()
            } else {
                val con = recS.configuration
                val landScape = con.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
                musicDim { wid, _ ->
                    if (fetchParams.x >= 0) {
                        if (!landScape) xBefore = wid / 2 else landXBefore = wid / 2
                        animationOpenFloat(fetchParams.x, wid / 2)
                    } else {
                        if (!landScape) xBefore = -(wid / 2)
                        else landXBefore = -(wid / 2)

                        animationOpenFloat(fetchParams.x, -(wid / 2))
                    }
                    if (!landScape) yBefore = fetchParams.y else landYBefore = fetchParams.y
                    beforeYouGo = fetchParams.y
                }
            }
        }

        override fun onDown(v: android.view.View, event: android.view.MotionEvent) {
            initialX = fetchParams.x
            initialY = fetchParams.y
            initialTouchX = event.rawX
            initialTouchY = event.rawY
        }

        override fun onMove(v: android.view.View, event: android.view.MotionEvent) {
            fetchParams.x = initialX + (event.rawX - initialTouchX).toInt()
            fetchParams.y = initialY + (event.rawY - initialTouchY).toInt()
            context.catchyBadToken {
                windowManage.updateViewLayout(headRestBall.root, fetchParams)
                windowManage.updateViewLayout(headMine.root, fetchParams)
            }
        }

    }


    @com.pt.common.global.UiAnn
    override fun animationOpenFloat(beginValue: Int, endValue: Int) {
        returnForSlip(beginValue, endValue) {
            duration = 300
            addUpdateListener {
                fetchParams.x = (animatedValue as Int)
                context?.catchyBadToken {
                    windowManage.updateViewLayout(
                        viewRootTwo?.root ?: return@catchyBadToken, fetchParams
                    )
                    windowManage.updateViewLayout(
                        viewRootNull?.root ?: return@catchyBadToken, fetchParams
                    )
                }
            }
            start()
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.databinding.NoteHeadBinding.changeVisibility() {
        if (!visible) {
            noteFrame.visibleEnd(300L)
            //editMessage.searchProvide()
        } else {
            noteFrame.apply {
                inVisibleEnd(300L)
                toCatchSackAfter(18, 400L) {
                    justGone()
                }.postAfter()
            }
            editMessage.hideInputMethod()
        }
        visible = !visible
    }

    @com.pt.common.global.UiAnn
    private fun androidx.appcompat.widget.AppCompatEditText.searchProvide() {
        floatingWindowMangerSearch {
            x = fetchParams.x
            y = fetchParams.y
            context.catchyBadToken {
                windowManage.updateViewLayout(headRestBall.root, this)
            }
        }
        toCatchSackAfter(66, 100L) {
            runCatching {
                ctxS.launchKeyBoard {
                    requestFocus()
                    showSoftInput(this@searchProvide, 2)
                }
            }
            Unit
        }.postAfter()
    }

    @com.pt.common.global.UiAnn
    private fun androidx.appcompat.widget.AppCompatEditText.hideInputMethod() {
        runCatching {
            ctxS.launchKeyBoard {
                if (isAcceptingText) {
                    hideSoftInputFromWindow(windowToken, 0)
                    context.catchyBadToken {
                        windowManage.updateViewLayout(headRestBall.root, fetchParams)
                    }
                }
            }
        }
    }

    override fun invoke() {
        launchDef {
            imeBack()
        }
    }

    private suspend fun imeBack() {
        withMain {
            winManager?.alsoSus {
                context.catchyBadToken {
                    it.updateViewLayout(viewRootNull?.root, fetchParams)
                }
            }
        }
    }

    override fun onClick(v: android.view.View?) {

    }

    override fun onKeyBoardOpened() {

    }

    override fun onKeyBoardClosed() {

    }

    override fun onChange(newConfig: Int) {
        doChange(newConfig)
    }

    private fun doChange(newConfig: Int) {
        ctxS.fetchDimensions {
            screenWidth = this@fetchDimensions.width
            screenHeight = this@fetchDimensions.height
            if (newConfig == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
                doChangePort(this@fetchDimensions.width)
            } else {
                doChangeLand(this@fetchDimensions.width)
            }
        }
    }

    private fun animationUpDown(startX: Int, endX: Int, startY: Int, endY: Int) {
        returnAnimForChange(startX, endX, startY, endY) {
            addUpdateListener {
                fetchParams.x = getAnimatedValue(com.pt.common.BuildConfig.X_DIMENSION) as Int
                fetchParams.y = getAnimatedValue(com.pt.common.BuildConfig.Y_DIMENSION) as Int
                context?.catchyBadToken {
                    windowManage.updateViewLayout(
                        viewRootNull?.root ?: return@catchyBadToken, fetchParams
                    )
                    windowManage.updateViewLayout(
                        viewRootTwo?.root ?: return@catchyBadToken, fetchParams
                    )
                }
            }
            duration = 100
            start()
        }
    }


    private fun doChangePort(wid: Int) {
        (if (xBefore != null) xBefore else (wid / 2))?.let { xb ->
            xBefore = xb
            (if (yBefore != null) yBefore else 100)?.let { yb ->
                yBefore = yb
                animationUpDown(fetchParams.x, xb, fetchParams.y, yb)
            }
        }
    }


    private fun doChangeLand(wid: Int) {
        (if (landXBefore != null) landXBefore else -(wid / 2))?.let { lxb ->
            landXBefore = lxb
            (if (landYBefore != null) landYBefore else 100)?.let { lyb ->
                landYBefore = lyb
                animationUpDown(fetchParams.x, lxb, fetchParams.y, lyb)
            }
        }
    }

    override fun onServiceDestroy(b: () -> Unit) {
        viewRootNull?.editMessage?.apply {
            hideInputMethod()
            onViewDestroy()
        }
        kotlin.runCatching {
            viewRootTwo?.apps?.onViewDestroy()
            context.catchyBadToken {
                windowManage.removeViewImmediate(headRestBall.root)
                windowManage.removeViewImmediate(headMine.root)
            }
            touchListener = null
            winManager = null
            viewRootNull = null
            viewRootTwo = null
            para = null
            beforeYouGo = 0
            visible = false
            job.cancelJob()
            catchy(Unit) {
                dispatch.close()
            }
            ext.shutdownNow()
            cancelScope()
            extNative = null
            jobNative = null
            dispatcherNative = null
            stateDestroy()
            b.invoke()
        }.onFailure {
            b.invoke()
        }
    }


}