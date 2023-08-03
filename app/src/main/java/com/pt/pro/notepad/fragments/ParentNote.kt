package com.pt.pro.notepad.fragments

import androidx.core.text.toSpannable
import com.pt.common.global.*
import com.pt.common.media.deleteImage
import com.pt.common.mutual.life.GlobalFragment
import com.pt.common.stable.*
import com.pt.pro.notepad.models.DataKeeperItem
import com.pt.pro.notepad.models.TableDetailsHolder
import com.pt.pro.notepad.models.TablesModelMonth
import com.pt.pro.notepad.objects.*

abstract class ParentNote : GlobalFragment<com.pt.pro.databinding.FragmentDataBinding>(), com.pt.pro.notepad.interfaces.DataCounterListener,
    com.pt.pro.notepad.interfaces.OnActivityStateChanged, com.pt.pro.notepad.recorder.EditMessage.KeyboardListener,
    com.pt.pro.notepad.interfaces.DataClickListener, com.pt.pro.notepad.interfaces.OnAudioRecordListener {

    protected var tablesModels: MutableList<TablesModelMonth> = mutableListOf()
    protected var tableAdapter: com.pt.pro.notepad.adapters.TableAdapterData? = null
    protected var tableUserName: String? = null
    protected var s1: String? = null
    protected var ada: com.pt.pro.notepad.adapters.DataKeeperAdapter? = null
    protected var mDataKeeperItemsList: MutableList<DataKeeperItem> = mutableListOf()
    protected val lastLinkItems: MutableList<DataKeeperItem> = mutableListOf()
    protected var selectedCalendar: Long? = System.currentTimeMillis()
    protected var isPickCalender: Boolean = false

    private var handlerDelete: android.os.Handler? = null

    private var handlerDeletedItem: DataKeeperItem? = null

    private var preView: android.view.View? = null
    protected var date1: Long = 0
    protected var tableIndex: Int = -1

    protected var bottomRec: Boolean = true
    protected var waitForDetails: Boolean = false

    protected inline val mAdapter: com.pt.pro.notepad.adapters.DataKeeperAdapter get() = ada!!

    @Volatile
    protected var haveASearch: Boolean = false

    protected var counterBoolean: Boolean = true

    protected var haveShare: Boolean = true

    protected var parentChildCount: Int = 0
    private var doAnim: Boolean = true

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        get() = {
            com.pt.pro.databinding.FragmentDataBinding.inflate(this@creBin, this, false).also {
                binder = it
                it.initViews(rec.isLandTraditional)
            }.root
        }


    private fun com.pt.pro.databinding.FragmentDataBinding.initViews(isLandTraditional: Boolean) {
        if (isLandTraditional) {
            codMon.framePara(80F.toPixel, -1) {}
            mainDataCon.framePara(-1, -1) {
                marginStart = 80F.toPixel
            }
            tableRecyclerView.apply {
                androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(-1, -1).apply {
                    behavior = com.pt.common.moderator.recycler.OverScrollBounceBehavior(ctx)
                }.also {
                    layoutParams = it
                }
                layoutManager = ctx.getVerManager
                adapter = tableAdapter
                scheduleLayoutAnimation()
            }
        } else {
            codMon.framePara(-1, 80F.toPixel) {}
            mainDataCon.framePara(-1, -1) {
                topMargin = 80F.toPixel
            }
            tableRecyclerView.apply {
                androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(-1, -1).apply {
                    behavior = com.pt.common.moderator.recycler.OverScrollHorizontalBehavior(ctx)
                }.also {
                    layoutParams = it
                }
                layoutManager = ctx.getManager
                adapter = tableAdapter
                scheduleLayoutAnimation()
            }
        }
    }


    override fun onPause() {
        super.onPause()
        if (ada != null) {
            (ada ?: return).stopPlayer()
        }
    }


    protected suspend fun clickForData(ss: String) {
        justCoroutine {
            createTime().letSusBack {
                TablesModelMonth(
                    mTableName = returnTableName(ss, it.time),
                    tableDisplay = returnTableTitle(ss, it.time),
                    mTableTime = it.timeInMillis
                )
            }
        }.alsoSusBack { tablesModelMonth ->
            justCoroutine {
                ctx.newDBTables(ss.noteTab) {
                    getAllDataTables(ss)
                }.indexOfFirst {
                    it.mTableName == tablesModelMonth.mTableName
                }
            }.alsoSusBack { checkTables ->
                withBack {
                    if (checkTables == -1) {
                        kotlin.runCatching {
                            tablesModelMonth.createNewMonth(ss)
                        }
                    }
                }
                loadDirectly(ss)
            }
        }
    }


    private suspend fun loadDirectly(ss: String) {
        withBack {
            tablesModels = ctx.newDBTables(ss.noteTab) {
                getAllDataTables(ss)
            }
        }
        withMain {
            val tdh = TableDetailsHolder(
                0,
                nightRider = nightRider,
                user = false,
                detail = false,
                firstTime = true,
                isNotification = false
            )
            tableAdapter = com.pt.pro.notepad.adapters.TableAdapterData(
                tablesModels,
                mutableListOf(),
                tdh,
                dataListener = this@ParentNote,
                false,
                ctx.fetchColorList()
            )
            binding.tableRecyclerView.apply {
                adapter = tableAdapter
                scheduleLayoutAnimation()
            }
        }
    }

    protected inline val android.content.Context.fetchColorList: suspend () -> MutableList<Int>
        get() = {
            justCoroutine {
                mutableListOf<Int>().apply {
                    findIntegerPrefDb(COL_KEEP, DATA_COL, datCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, IMP_COL, impCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, LINK_COL, linCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, REM_COL, remCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, SCAN_COL, scaCol).also(::add)
                    findIntegerPrefDb(COL_KEEP, MAIL_COL, emaCol).also(::add)
                }
            }
        }

    protected suspend fun launchDateDialog() {
        withMain {
            date1.fetchMinAndMaxDate.let { (one, two) ->
                (selectedCalendar ?: one).fetchCalender.apply {
                    onDateSetListener?.let { dl ->
                        android.app.DatePickerDialog(
                            ctx,
                            if (nightRider) com.pt.pro.R.style.DeleteAlarmDialogTheme_Night else com.pt.pro.R.style.DeleteAlarmDialogTheme_Light,
                            dl,
                            this[CalendarLate.YEAR],
                            this[CalendarLate.MONTH],
                            this[CalendarLate.DAY_OF_MONTH]
                        ).also {
                            datePickerDialog = it
                            it.datePicker.apply {
                                minDate = one
                                maxDate = two
                            }
                            it.show()
                        }
                    }
                }
            }
        }
    }

    private var datePickerDialog: android.app.DatePickerDialog? = null

    private var onDateSetListener: android.app.DatePickerDialog.OnDateSetListener?
        get() = android.app.DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val calendarStart = CalendarLate.getInstance()
            calendarStart[year, month, dayOfMonth, 0, 0] = 0
            val calendarEnd = CalendarLate.getInstance()
            calendarEnd[year, month, dayOfMonth, 23, 59] = 59
            onCalendarClicked(calendarStart.timeInMillis, calendarEnd.timeInMillis, dayOfMonth)
            datePickerDialog?.dismiss()
        }
        set(@Suppress("UNUSED_PARAMETER") value) {}

    override fun checkIfHaveFra() {
        childFragmentManager.apply {
            if (findFragmentByTag(FRAGMENT_CALENDAR) != null) {
                popBackStack(FRAGMENT_CALENDAR, 0)
            }
            if (findFragmentByTag(FRAGMENT_IMAGE) != null) {
                popBackStack(FRAGMENT_IMAGE, 0)
            }
        }
    }

    private suspend fun TablesModelMonth.createNewMonth(ss: String) {
        adapterCreate(mTableName ?: return)
        ctx.newDBTables(ss.noteTab) {
            insertTable(ss)
        }
    }

    private suspend fun adapterCreate(tablesModel: String) {
        ctx.newDBDataSus(tablesModel.noteDb) {
            createMsgTable(tablesModel)
        }
    }

    override fun com.pt.pro.databinding.FragmentDataBinding.onClick(v: android.view.View) {

    }

    override fun com.pt.pro.databinding.FragmentDataBinding.onLongClick(v: android.view.View): Boolean {
        return true
    }


    protected suspend fun androidx.appcompat.widget.LinearLayoutCompat.refresh() {
        withMain {
            removeAllViews()
        }
    }

    protected fun com.pt.pro.notepad.adapters.DataKeeperAdapter.refresh() {
        launchImdMain {
            withMain {
                binder?.apply {
                    if (linearScroll.childCountSus() != 0) {
                        recyclerView.clearRecyclerPool()
                        refreshAdapter()
                        withMain {
                            linearScroll.applySus {
                                removeAllViews()
                            }
                        }
                    }
                }
            }
        }
    }

    protected fun androidx.appcompat.widget.AppCompatEditText.showInputMethod() {
        runCatching {
            ctx.launchKeyBoard {
                requestFocus()
                showSoftInput(this@showInputMethod, 2)
            }
        }
    }

    protected suspend fun androidx.appcompat.widget.AppCompatEditText.hideInputMethod() {
        withMain {
            runCatching {
                context?.launchKeyBoard {
                    if (isAcceptingText) {
                        hideSoftInputFromWindow(windowToken, 0)
                    }
                }
            }
        }
    }

    override fun DataKeeperItem.pushLink(txt: String) {
        when {
            androidx.core.text.util.LinkifyCompat.addLinks(
                (dataText ?: return).toSpannable(),
                android.text.util.Linkify.WEB_URLS
            ) -> {
                if (txt.isEmpty()) {
                    onLink(false, "")
                    return
                }
                launchDef {
                    ctx.runCatching {
                        webViewProvider(txt.txtCorrecter) {
                            this@ParentNote.startActivity(this)
                        }
                    }.onFailure {
                        ctx.runCatching {
                            webBrowserProvider(txt.txtCorrecter) {
                                this@ParentNote.startActivity(this)
                            }
                        }.onFailure {
                            withMain {
                                onLink(false, "")
                            }
                        }
                    }
                }
            }
            androidx.core.text.util.LinkifyCompat.addLinks(
                dataText.toSpannable(),
                android.text.util.Linkify.EMAIL_ADDRESSES
            ) -> {
                mailProvider(txt) {
                    this@ParentNote.startActivity(this)
                }
            }
            androidx.core.text.util.LinkifyCompat.addLinks(
                dataText.toSpannable(),
                android.text.util.Linkify.PHONE_NUMBERS
            ) -> {
                phoneProvider(txt) {
                    this@ParentNote.startActivity(this)
                }
            }
            else -> {
                launchDef {
                    ctx.findIntegerPrefDb(ID_SEARCH, SEARCH_ENGINE, 0).let {
                        kotlin.runCatching {
                            searchProvider(txt, it) {
                                this@ParentNote.startActivity(this)
                            }
                        }
                    }
                }
            }
        }
    }

    @Volatile
    protected var pointerImage: androidx.appcompat.widget.AppCompatImageView? = null

    protected suspend fun android.view.ViewGroup.addHandForFirstTime(isLandTraditional: Boolean) {
        withBack {
            ctx.updatePrefBoolean(KEY_FIRST_TIME, false)
        }
        withMain {
            androidx.appcompat.widget.AppCompatImageView(ctx).apply {
                pointerImage = this
                forImagePointerDet(isLandTraditional)
                ctx.compactImage(com.pt.pro.R.drawable.ic_hand_pointer) {
                    setImageDrawable(this)
                }
                this@addHandForFirstTime.addView(this@apply)
            }
        }
        withMain {
            forRunImagePointer(isLandTraditional)
        }
    }

    private fun androidx.appcompat.widget.AppCompatImageView.forImagePointerDet(
        isLandTraditional: Boolean
    ) {
        clearAnimation()
        animate().cancel()
        if (isLandTraditional) {
            rotation = -90F
            framePara(35F.toPixel, 35F.toPixel) {
                marginStart = 80F.toPixel
                topMargin = 25F.toPixel
            }
        } else {
            rotation = 0F
            framePara(35F.toPixel, 35F.toPixel) {
                marginStart = 25F.toPixel
                topMargin = 80F.toPixel
            }
        }
    }

    private fun forRunImagePointer(isLandTraditional: Boolean) {
        if (isLandTraditional) {
            130F.runSwipeLand.rKTSack(10L).postAfter()
        } else {
            130F.runSwipe.rKTSack(10L).postAfter()
        }
    }

    protected suspend fun androidx.appcompat.widget.AppCompatImageView.rotationHandForFirstTime() {
        withMain {
            unPostAll()
        }
        withMain {
            clearAnimation()
            animate().cancel()
            rotation = 180F
            framePara(35F.toPixel, 35F.toPixel) {
                gravity = android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL
                bottomMargin = rec.getDimension(com.pt.pro.R.dimen.rle).toInt()
            }
        }
        withMain {
            (-1F * 130F).runSwipe.rKTSack(100L).postAfter()
        }
    }

    private val Float.runSwipe: DSackT<() -> Unit, Int>
        @Suppress("LongLine")
        get() = toCatchSack(11) {
            if (context != null) {
                binder?.apply {
                    kotlin.runCatching {
                        if (context == null) return@runCatching
                        toCatchSackAfter(99, 100L) {
                            if (context == null) return@toCatchSackAfter
                            pointerImage?.apply {
                                animate()
                                    .translationY(this@runSwipe)
                                    .setInterpolator(android.view.animation.LinearInterpolator())
                                    .duration = 600L
                            }
                        }.postAfter()
                        toCatchSackAfter(88, 700L) {
                            if (context == null) return@toCatchSackAfter
                            pointerImage?.apply {
                                animate()
                                    .translationY(0F)
                                    .setInterpolator(android.view.animation.LinearInterpolator())
                                    .duration = 600L
                            }

                        }.postAfter()
                    }
                }
                runSwipe.rKTSack(1200L).postAfter()
            }
            Unit
        }


    private val Float.runSwipeLand: DSackT<() -> Unit, Int>
        @Suppress("LongLine")
        get() = toCatchSack(11) {
            if (context != null) {
                binder?.apply {
                    kotlin.runCatching {
                        if (context == null) return@runCatching
                        toCatchSackAfter(99, 100L) {
                            if (context == null) return@toCatchSackAfter
                            pointerImage?.apply {
                                animate()
                                    .translationX(this@runSwipeLand)
                                    .setInterpolator(android.view.animation.LinearInterpolator())
                                    .duration = 600L
                            }
                        }.postAfter()
                        toCatchSackAfter(88, 700L) {
                            if (context == null) return@toCatchSackAfter
                            pointerImage?.apply {
                                animate()
                                    .translationX(0F)
                                    .setInterpolator(android.view.animation.LinearInterpolator())
                                    .duration = 600L
                            }

                        }.postAfter()
                    }
                }
                runSwipeLand.rKTSack(1200L).postAfter()
            }
            Unit
        }

    private fun DataKeeperItem.deleteHandler(mRecentlyItemPosition: Int) {
        if (handlerDelete != null) {
            handlerDeletedItem.deleteBar()
        }
        handlerDeletedItem = this
        showSnackBar(mRecentlyItemPosition)

        handlerDelete = ctx.fetchHand
        handlerDelete?.postDelayed({
            deleteBar()
            handlerDelete = null
        }, 3500)
    }

    private fun DataKeeperItem?.deleteBar() {
        (this ?: return)
        activity?.baseContext?.apply {
            runCatching {
                if (notTime != -1L) {
                    newDBNotification {
                        deleteNot(notTime)
                    }
                }
                if (imageUrl != null) {
                    FileLate(imageUrl).deleteImage()
                } else if (recordPath != null) {
                    catchy(null) {
                        getPath(recordPath.toUri)
                    }?.let { path ->
                        FileLate(path).deleteImage()
                    }
                }
                newDBData((s1 ?: return@runCatching).noteDb) {
                    deleteDate(timeData, s1).let {
                        handlerDeletedItem = null
                    }
                }
            }
        }
    }

    private fun DataKeeperItem.showSnackBar(mRecentlyItemPosition: Int) = kotlin.run {
        binding.frameMainData.snakeBarDelete(this, mRecentlyItemPosition)
    }

    private fun DataKeeperItem.undoDelete(mRecentlyItemPosition: Int) {
        mAdapter.dataItems.add(mRecentlyItemPosition, this)
        mAdapter.notifyItemInserted(mRecentlyItemPosition)
        doneTask(mRecentlyItemPosition)
    }

    protected fun DataKeeperItem.afterSwipe(mRecentlyItemPosition: Int, delete: Boolean) {
        if (delete) {
            deleteHandler(mRecentlyItemPosition)
        } else {
            doneTask(mRecentlyItemPosition)
        }
    }

    private fun DataKeeperItem.doneTask(mRecentlyItemPosition: Int) {
        isDone = !isDone
        ctx.newDBData((s1 ?: return).noteDb) {
            upDateRecord(s1)
        }
        showUndoSnackBar(mRecentlyItemPosition)
    }

    private fun DataKeeperItem.showUndoSnackBar(mRecentlyItemPosition: Int) {
        binding.frameMainData.snakeBar(isDone, mRecentlyItemPosition, this)
    }

    private fun android.view.ViewGroup.snakeBar(
        isDone: Boolean,
        recItemPos: Int,
        dk: DataKeeperItem
    ) {
        if (childCount == parentChildCount) {
            with(com.pt.pro.gallery.fasten.GalleryInflater.run { layoutInflater.context.inflaterDelete() }) {
                snakeText.framePara(MATCH, MATCH) {
                    gravity = android.view.Gravity.CENTER_VERTICAL
                    marginEnd = 65F.toPixel
                }
                this@snakeBar.addView(this@with.root_)

                snakeText.text = if (isDone)
                    com.pt.pro.R.string.s4.dStr
                else
                    com.pt.pro.R.string.fk.dStr
                cancelDelete.justGone()
                confirmDelete.text = com.pt.pro.R.string.ud.dStr
                confirmDelete.setOnClickListener {
                    dk.undoDelete(recItemPos)
                    this@with.deleteFrame.goneBottom(200L)
                    toCatchSackAfter(99, 300L) {
                        if (context != null) {
                            this@snakeBar.removeView(this@with.root_)
                        }
                    }.postAfter()
                }
                deleteFrame.visibleBottom(300L)
                toCatchSackAfter(98, 3400) {
                    if (context != null) {
                        if (indexOfChild(this@with.root_) != -1) {
                            this@with.deleteFrame.goneBottom(200L)
                            toCatchSackAfter(97, 300L) {
                                if (context != null) {
                                    this@snakeBar.removeView(this@with.root_)
                                }
                            }.postAfter()
                        }
                    }
                }.postAfter()
            }
        }
    }

    private fun android.view.ViewGroup.snakeBarDelete(
        dk: DataKeeperItem,
        mRecentlyItemPosition: Int
    ) {
        if (preView != null) {
            doAnim = false
            this@snakeBarDelete.removeView(preView)
        }
        if (childCount == parentChildCount) {
            with(com.pt.pro.gallery.fasten.GalleryInflater.run { layoutInflater.context.inflaterDelete() }) {
                snakeText.framePara(MATCH, MATCH) {
                    gravity = android.view.Gravity.CENTER_VERTICAL
                    marginEnd = 65F.toPixel
                }
                this@ParentNote.preView = this@with.root_
                this@snakeBarDelete.addView(this@with.root_)
                snakeText.text = com.pt.pro.R.string.mt.dStr
                cancelDelete.justGone()
                confirmDelete.text = com.pt.pro.R.string.ud.dStr
                confirmDelete.setOnClickListener {
                    handlerDelete?.removeCallbacksAndMessages(null)
                    handlerDelete = null
                    mAdapter.dataItems.add(mRecentlyItemPosition, dk)
                    mAdapter.notifyItemInserted(mRecentlyItemPosition)
                    deleteFrame.goneBottom(200L)
                    toCatchSackAfter(77, 10L) {
                        binding.recyclerView.smoothScrollToPosition(mRecentlyItemPosition)
                    }.postAfter()
                    toCatchSackAfter(86, 300L) {
                        if (context != null) {
                            preView = null
                            this@snakeBarDelete.removeView(root_)
                        }
                    }.postAfter()
                }
                if (doAnim)
                    this@with.deleteFrame.visibleBottom(300L)
                else
                    this@with.deleteFrame.justVisible()

                toCatchSackAfter(88, 3400) {
                    if (context != null) {
                        if (indexOfChild(this@with.root_) != -1) {
                            this@with.deleteFrame.goneBottom(200L)
                            toCatchSackAfter(87, 300) {
                                if (context != null) {
                                    preView = null
                                    this@snakeBarDelete.removeView(this@with.root_)
                                }
                            }.postAfter()
                        }
                    }
                }.postAfter()
            }
        }
    }

    protected suspend fun adapterDetailsLoad(aa: String, color: String) {
        val isIn = ctx.findBooleanPrefDb(ID_INC, INC_DONE, false)
        withBackDef(mutableListOf()) {
            when (color) {
                com.pt.common.BuildConfig.DONE_OPT -> {
                    ctx.newDBDataSus(aa.noteDb) { getAllDone(aa) }
                }
                IMAGE_CHOOSE -> {
                    ctx.newDBDataSus(aa.noteDb) { getAllImages(aa, isIn) }
                }
                RECORD_CHOOSE -> {
                    ctx.newDBDataSus(aa.noteDb) { getAllRecord(aa, isIn) }
                }
                EMAIL_CHOOSE -> {
                    ctx.newDBDataSus(aa.noteDb) { getAllEmail(aa, isIn) }
                }
                else -> {
                    ctx.newDBDataSus(aa.noteDb) { getAllTypes(aa, color, isIn) }
                }
            }
        }.let {
            withMain {
                binding.recyclerView.recycledViewPool.clear()
                mAdapter.apply {
                    it.updateDataAdapter()
                }
            }
        }
    }

    override fun DSack<String?, String?, String?>.sendEmail() {
        doSendMail(this@sendEmail) {
            runCatching<Unit> {
                this@ParentNote.startActivity(this@doSendMail)
            }.onFailure {
                ctx.makeToastRec(com.pt.pro.R.string.nm, 0)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        (newConfig.isLandTrad).let { isLand ->
            pointerImage?.apply {
                if (s1 == null) {
                    ctx.fetchHand.post {
                        forImagePointerDet(isLand)
                        forRunImagePointer(isLand)
                    }
                } else {
                    ctx.fetchHand.post {
                        binding.tabData.removeView(this)
                        pointerImage = null
                    }
                }
            }
            binding.initViews(isLand)
        }
    }

    override fun onDestroyView() {
        ada?.onAdapterDestroy()
        ada?.refresh()
        tablesModels.clear()
        mDataKeeperItemsList.clear()
        lastLinkItems.clear()
        pointerImage = null
        binder?.recyclerView?.adapter = null
        binder?.tableRecyclerView?.adapter = null
        tableAdapter = null
        tableUserName = null
        s1 = null
        ada = null
        preView = null
        selectedCalendar = null
        onDateSetListener = null
        super.onDestroyView()
    }

}