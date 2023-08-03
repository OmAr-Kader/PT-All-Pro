package com.pt.pro.notepad.fragments

import androidx.core.text.toSpannable
import androidx.core.widget.doAfterTextChanged
import com.pt.common.global.*
import com.pt.common.media.compressImage
import com.pt.common.media.copyFileTo
import com.pt.common.media.getImagePathFromURI
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.notepad.models.*
import com.pt.pro.notepad.objects.*

class FragmentNote : ParentNote() {

    override fun com.pt.pro.databinding.FragmentDataBinding.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentNote)
        launchImdMain {
            context.nullChecker()
            justCoroutineMain {
                act.windowManager?.fetchDimensionsMan {
                    ada = com.pt.pro.notepad.adapters.DataKeeperAdapter(
                        mDataKeeperItemsList,
                        this@fetchDimensionsMan.width,
                        them.findAttr(R.attr.rmoBackground),
                        them.findAttr(R.attr.colorPrimaryAlpha),
                        this@FragmentNote,
                        ctx.fetchColorList()
                    )
                }
                rev.intiBack21(them.findAttr(R.attr.rmoBackHint))
                revInside.intiBack21(them.findAttr(R.attr.rmoBackground))
            }
            context.nullChecker()
            justCoroutineMain {
                tableRecyclerView.layoutAnimation = ctx.fallDownAnimationRec(
                    dur = 500L,
                    0.1F,
                )
                recyclerView.apply {
                    justInvisible()
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(ctx).apply {
                        reverseLayout = false
                        stackFromEnd = true
                    }
                    adapter = mAdapter
                    itemAnimator = null
                    onScrollListener {
                        bottomRec = !it.canScrollVertically(1)
                    }
                    adapterDate()
                }
                if (act.intent.getStringExtra(NOT_ALARM_KEY) != null) {
                    act.intent.fromNotification()
                } else {
                    doNormalLoad()
                }
                imageOption.setOnClickListener {
                    launchDef {
                        it?.popImgFor()
                    }
                }
            }
            context.nullChecker()
            justCoroutineMain {
                ctx.getOwnFile(IMAGE_FILE).fileCreator()
                ctx.getOwnFile(RECORD_FILE).let {
                    it.fileCreator()
                    sendButton.setDirectoryAudio(it)
                }
                sendButton.apply {
                    setOnRecordClickListener {
                        if (binding.editMessage.text.toString() != "") {
                            launchImdMain {
                                it?.popDataWindow()
                            }
                        }
                    }
                    setOnListenerRecord(this@FragmentNote)
                }
                parentChildCount = tabData.childCount
                editMessage.setKeyboardListener(this@FragmentNote)

                if (ctx.findBooleanPrefDb(FIRST_TIME_ANIM, KEY_FIRST_TIME, true)) {
                    binding.tabData.addHandForFirstTime(rec.isLandTraditional)
                }
            }
        }
    }

    private suspend fun doNormalLoad() {
        ctx.newDBDataUser {
            getAllUsers()
        }.normalLoad()
    }

    private suspend fun MutableList<TablesModelUser>.normalLoad() {
        fetchAdapterData {
            tableAdapter = this
            binding.tableRecyclerView.apply {
                adapter = this@fetchAdapterData
                scheduleLayoutAnimation()
            }
        }
    }

    private suspend fun MutableList<TablesModelUser>.fetchAdapterData(
        b: suspend com.pt.pro.notepad.adapters.TableAdapterData.() -> Unit
    ) {
        withMain {
            TablesModelUser(
                userId = ADD_USER,
                userName = ADD_USER
            ).runSus {
                this@fetchAdapterData.add(this)
            }
            TableDetailsHolder(
                0,
                nightRider = nightRider,
                user = true,
                detail = false,
                firstTime = true,
                isNotification = false
            ).letSus { tdh ->
                com.pt.pro.notepad.adapters.TableAdapterData(
                    mutableListOf(),
                    this@fetchAdapterData,
                    tdh,
                    dataListener = this@FragmentNote,
                    false,
                    ctx.fetchColorList()
                ).letSus(b)
            }
        }
    }

    private fun android.content.Intent.fromNotification() {
        launchDef {
            withBackDef(null) {
                if (isV_T) {
                    getBundleExtra(NOT_BUNDLE_EXTRA)?.getParcelable(
                        NOT_ALARM_KEY,
                        DataNotification::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    getBundleExtra(NOT_BUNDLE_EXTRA)?.getParcelable(NOT_ALARM_KEY) as? DataNotification?
                }
            }?.displayFromNotification()
        }
    }

    private suspend fun DataNotification.displayFromNotification() {
        s1 = tableName
        withMain {
            act.intent.getIntExtra(INTENT_ID, idData).let {
                androidx.core.app.NotificationManagerCompat.from(ctx).apply {
                    cancel(it)
                }
            }
        }
        withBack {
            tableUserName?.let {
                tablesModels = ctx.newDBTables(it.noteTab) {
                    getAllDataTables(it)
                }
            }
        }
        withMain {
            tableAdapter = TableDetailsHolder(
                tableIndex,
                nightRider = nightRider,
                user = false,
                detail = false,
                firstTime = false,
                isNotification = true
            ).let { tdh ->
                com.pt.pro.notepad.adapters.TableAdapterData(
                    tablesModels,
                    mutableListOf(),
                    tdh,
                    dataListener = this@FragmentNote,
                    false,
                    ctx.fetchColorList()
                )
            }
            binding.tableRecyclerView.apply {
                adapter = tableAdapter
                scheduleLayoutAnimation()
                toCatchSack(998) {
                    smoothScrollToPosition(tableIndex)
                }.postNow()
            }
        }
        kotlinx.coroutines.delay(100L)
        withMain {
            adapterLoad(s1 ?: return@withMain)
        }
        kotlinx.coroutines.delay(400L)
        withMain {
            binding.recyclerView.smoothScrollToPosition(textIndex)
        }
        s1?.letSus {
            ctx.updatePrefString(LAST_NOTE_TABLE, it)
        }
    }

    private suspend fun android.content.Context.fetchImageForNote() {
        withBack {
            findPicker(com.pt.pro.BuildConfig.APPLICATION_ID) { resultImg?.launch(this) }
        }
    }

    private suspend fun android.view.View.popImgFor() {
        withMain {
            with<com.pt.pro.databinding.PopForImageBinding, Unit>(
                com.pt.pro.databinding.PopForImageBinding.inflate(
                    act.layoutInflater,
                    requireView() as? android.view.ViewGroup,
                    false
                )
            ) {
                android.widget.PopupWindow(
                    root,
                    WRAP,
                    WRAP,
                    true
                ).apply {
                    setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                    withMain {
                        searchPic.setOnClickListener {
                            popImage(444)
                            dismiss()
                        }
                        calPick.setOnClickListener {
                            popImage(333)
                            dismiss()
                        }
                        scanner.setOnClickListener {
                            popImage(222)
                            dismiss()
                        }
                        scanner.setOnLongClickListener {
                            it?.popUpComment(
                                R.string.t8,
                                R.attr.rmoBackHint,
                                0,
                            )
                            true
                        }
                        calPick.setOnLongClickListener {
                            it?.popUpComment(
                                R.string.gz,
                                R.attr.rmoBackHint,
                                0,
                            )
                            true
                        }
                        imagePic.setOnClickListener {
                            popImage(111)
                            dismiss()
                        }
                        imagePic.setOnLongClickListener {
                            it?.popUpComment(
                                R.string.zs,
                                R.attr.rmoBackHint,
                                0,
                            )
                            true
                        }
                    }
                    animationStyle = R.style.Animation
                    showAsDropDown(
                        this@popImgFor,
                        5,
                        -1 * ((rec.getDimension(R.dimen.rle).toInt() * 5) + (60F.toPixel))
                    )
                }
            }
        }
    }

    private suspend fun android.view.ViewGroup.popSearchFor() {
        justCoroutineMain {
            with(
                com.pt.pro.databinding.PopForSearchBinding.inflate(
                    act.layoutInflater,
                    this@popSearchFor,
                    false
                )
            ) {
                justCoroutineMain {
                    searchEdit.applySus {
                        showInputMethod()
                        setOnClickListener {
                            showInputMethod()
                        }
                    }
                }
                justCoroutineMain {
                    searchEdit.doAfterTextChanged {
                        launchDef {
                            searchAll(it)
                        }
                    }
                }
                searchBtn.setOnClickListener {
                    launchMain {
                        justCoroutineMain {
                            haveASearch = false
                            searchEdit.hideInputMethod()
                            this@popSearchFor.removeView(this@with.root)
                        }
                        justCoroutineMain {
                            mAdapter.applySus {
                                dataItemsOri.toMutableList().updateDataAdapter()
                            }
                        }
                    }
                }
                haveASearch = true
                this@popSearchFor.addView(this@with.root)
            }
        }
    }

    private fun popImage(opt: Int) {
        when (opt) {
            111 -> {
                launchImdMain {
                    binding.editMessage.hideInputMethod()
                    kotlinx.coroutines.delay(100L)
                    ctx.fetchImageForNote()
                }
            }
            222 -> {
                launchImdMain {
                    binding.editMessage.hideInputMethod()
                    kotlinx.coroutines.delay(100L)
                    launchScanActivity()
                }
            }
            333 -> {
                launchImdMain {
                    binding.editMessage.hideInputMethod()
                    kotlinx.coroutines.delay(100L)
                    launchDateDialog()
                }
            }
            444 -> {
                doDisplaySearch()
            }
        }
    }

    private suspend fun launchScanActivity() {
        withBack {
            android.content.Intent(ctx, com.pt.pro.notepad.activities.ScannerActivity::class.java)
                .applySusBack {
                    putExtra(TABLE_NAME, s1)
                    putExtra(SCANNER_IS_GALLERY, false)
                    putExtra(SCANNER_GALLERY_PATH, "")
                    resultScan?.launch(this)
                }
        }
    }

    private fun doDisplaySearch() {
        if (!haveASearch) {
            launchImdMain {
                binding.editMessage.hideInputMethod()
                kotlinx.coroutines.delay(100L)
                binding.frameMainData.popSearchFor()
            }
        }
    }

    private suspend fun searchAll(text: CharSequence?) {
        if (!text.isNullOrEmpty()) {
            withBackDef(mAdapter.dataItems) {
                kotlin.runCatching {
                    mAdapter.dataItems.toMutableList().toMutableList().asSequence().filter {
                        it.dataText?.contains(text, true) == true
                    }.distinct().toMutableList()
                }.getOrDefault(mAdapter.dataItems)
            }.letSus {
                context.nullChecker()
                withMain {
                    mAdapter.applySus {
                        it.toMutableList().updateDataAdapterSearch()
                    }
                }
            }
        } else {
            mAdapter.applySus {
                dataItemsOri.toMutableList().updateDataAdapter()
            }
        }
    }

    private inline val fetchDataColor: suspend (String, Int) -> Int
        get() = { str, defCol ->
            ctx.findIntegerPrefDb(COL_KEEP, str, defCol)
        }

    private suspend fun android.view.View.popDataWindow() {
        justCoroutineMain {
            with<com.pt.pro.databinding.CustomLayoutBinding, Unit>(
                com.pt.pro.databinding.CustomLayoutBinding.inflate(
                    act.layoutInflater,
                    requireView() as? android.view.ViewGroup,
                    false
                )
            ) {
                android.widget.PopupWindow(
                    root,
                    WRAP,
                    WRAP,
                    false
                ).applySus {
                    datImg.svgReColorSus(fetchDataColor(DATA_COL, ctx.fetchColor(R.color.dcl)))
                    impImg.svgReColorSus(fetchDataColor(IMP_COL, ctx.fetchColor(R.color.iml)))
                    linImg.svgReColorSus(fetchDataColor(LINK_COL, ctx.fetchColor(R.color.glm)))
                    remImg.svgReColorSus(fetchDataColor(REM_COL, ctx.fetchColor(R.color.sml)))
                    emaImg.svgReColorSus(fetchDataColor(MAIL_COL, ctx.fetchColor(R.color.dcl)))
                    justCoroutine {
                        setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                        datLinear.setOnClickListener {
                            onSaveClicked(KEEP_DATA)
                            dismiss()
                        }
                        impLinear.setOnClickListener {
                            onSaveClicked(KEEP_IMPORTANT)
                            dismiss()
                        }
                        linLinear.setOnClickListener {
                            launchDef {
                                onSaveLink()
                            }
                            dismiss()
                        }
                        remLinear.setOnClickListener {
                            onSaveClicked(KEEP_REMEMBER)
                            dismiss()
                        }
                        emailLinear.setOnClickListener {
                            onEmail()
                            dismiss()
                        }
                    }
                    justCoroutine {
                        this@applySus.isOutsideTouchable = true
                        this@applySus.showAsDropDown(
                            this@popDataWindow,
                            0,
                            -1 * (rec.getDimension(R.dimen.rle).toInt() + (5F * 65F).toPixel)
                        )
                    }
                }
            }
        }
    }

    private fun myShare(haveText: Boolean) {
        launchImdMain {
            context.nullChecker()
            justCoroutineMain {
                binding.linearScroll.refresh()
            }
            context.nullChecker()
            justCoroutineMain {
                com.pt.pro.file.fasten.FileInflater.run { ctx.inflaterNoteOption(haveText) }.apply {
                    backOpt.setOnClickListener {
                        mAdapter.refresh()
                    }
                    copy?.setOnClickListener {
                        ctx.sendToClipDate(buildMessage())
                        ctx.makeToastRec(R.string.tp, 0)
                        mAdapter.refresh()
                    }
                    not?.setOnClickListener {
                        myNotification()
                    }
                    share.setOnClickListener {
                        if (haveShare) {
                            if (mAdapter.rec)
                                shareRecord30()
                            else if (mAdapter.img)
                                shareImage30()
                        } else {
                            launchSender(
                                buildMessage(),
                                null,
                                ""
                            ) {
                                launchDef {
                                    act.startActivity(this@launchSender)
                                }
                            }
                        }
                    }
                }.root_.also(binding.linearScroll::addView)
                binding.linearScroll.visibleFade(300)
            }
        }
    }

    override fun onRecordFinished(dataKeeperItem: DataKeeperItem?) {
        launchMain {
            justCoroutineMain {
                ctx.compactImage(R.drawable.ic_menu_list) {
                    binding.imageOption.apply {
                        setImageDrawable(this@compactImage)
                        svgReColor(them.findAttr(R.attr.rmoGrey))
                    }
                }
            }
            dataKeeperItem?.save()
        }
    }

    override fun onError(errorCode: Int) {
        if (errorCode == 3) {
            ctx.compactImage(R.drawable.ic_menu_list) {
                binding.imageOption.apply {
                    setImageDrawable(this@compactImage)
                    svgReColor(them.findAttr(R.attr.rmoGrey))
                }
            }
        }
    }

    override fun onRecordingStarted() {
        activity?.runOnUiThread {
            ctx.compactImage(R.drawable.ic_mic) {
                binding.imageOption.apply {
                    setImageDrawable(this@compactImage)
                    svgReColor(ctx.fetchColor(R.color.ord))
                }
            }
            mAdapter.stopPlayer()
        }
    }

    private fun myNotification() {
        launchMain {
            mAdapter.dataItems[mAdapter.copyPos].letSus { curr ->
                fetchNotifyData(mAdapter.dataItems[mAdapter.copyPos]) { new ->
                    withBack {
                        android.content.Intent(
                            act,
                            com.pt.pro.notepad.activities.NotificationActivity::class.java
                        ).apply {
                            putExtra(NOTIFICATION, new)
                            putExtra(KEEPER_TIME, curr.timeData)
                            resultNot?.launch(this)
                        }
                    }
                    withMain {
                        mAdapter.refresh()
                    }
                }
            }
        }
    }

    private suspend fun fetchNotifyData(
        curr: DataKeeperItem,
        b: suspend (DataNotification) -> Unit
    ) {
        withMainDef(null) {
            val txt = buildMessage()
            val dataNotification = DataNotification(
                idData = kotlin.random.Random.nextInt(10000),
                dataText = txt,
                timeNotify = curr.notTime,
                recordPath = curr.recordPath,
                imageUrl = curr.imageUrl,
                isDone = curr.isDone,
                tableName = s1,
                tableUserName = tableUserName,
                tableIndex = tableIndex,
                textIndex = 0
            )
            if (curr.notTime == -1L) {
                dataNotification
            } else {
                ctx.newDBNotification {
                    getOneData(curr.notTime)
                }?.run {
                    com.pt.pro.notepad.receivers.NoteReceiver.apply {
                        ctx.cancelReminderNote(idData)
                    }
                    this
                } ?: dataNotification
            }.letSus(b)
        }
    }

    private suspend fun DataKeeperItem.remNotification() {
        withBack {
            DataNotification(
                idData = kotlin.random.Random.nextInt(10000),
                dataText = dataText,
                timeNotify = notTime,
                recordPath = recordPath,
                imageUrl = imageUrl,
                isDone = isDone,
                tableName = s1,
                tableUserName = tableUserName,
                tableIndex = tableIndex,
                textIndex = 0
            ).let {
                android.content.Intent(
                    act,
                    com.pt.pro.notepad.activities.NotificationActivity::class.java
                ).apply {
                    putExtra(NOTIFICATION, it)
                    putExtra(KEEPER_TIME, timeData)
                    resultNot?.launch(this)
                }
            }
        }
    }

    private var resultNot: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            launchDef {
                adapterLoad(s1 ?: return@launchDef)
            }
        }

    private fun buildMessage(): String {
        val aa: StringBuilder = StringBuilder()
        mAdapter.temp[mAdapter.temp.size - 1].dataText
        mAdapter.temp.forEach {
            if (it.dataText != null) {
                aa.append(it.dataText)
                aa.append(System.getProperty("line.separator"))
            }
        }
        return aa.toString()
    }


    override fun onTypingStarted() {
        pointerImage?.apply {
            binding.tabData.removeView(this)
            pointerImage = null
        }
        binding.sendButton.changeToMessage(true)
    }

    private suspend fun onSaveLink() {
        val msg = withMainDef(null) {
            binding.editMessage.text
        }
        withBack {
            if (msg.isNullOrEmpty()) return@withBack
            val t = System.currentTimeMillis()
            DataKeeperItem(
                dataText = msg.toString(),
                keeperType = KEEP_LINK,
                emailToSome = null,
                emailSubject = null,
                timeData = t,
                recordPath = null,
                recordLength = 0L,
                imageUrl = null,
                dayNum = t.fetchCalenderDay,
                isDone = false,
            ).applySusBack {
                save()
                lastLinkItems.add(this)
            }
            msg.toSpannable().findLinkTitleUri.letSusBack { itLin ->
                if (itLin != null) {
                    activity?.applicationContext?.also { ct ->
                        com.pt.web.WebTextProvider(ct) { table, time, title, img ->
                            if (this == null) return@WebTextProvider
                            lastLinkItems.findLast { it.timeData == time }?.let { dataKeeperItem ->
                                activity?.baseContext?.apply {
                                    dataKeeperItem.copy(
                                        emailToSome = title?.toDefString?.let { it(200) },
                                        emailSubject = img
                                    ).also { d ->
                                        this@apply.newDBData(table.noteDb) {
                                            d.upDateRecord(table)
                                        }
                                        ada?.apply {
                                            d.updateWebItem()
                                        }
                                    }
                                }
                            }
                        }.run {
                            webTitleProvider(link = itLin, time = t, table = s1 ?: return@run)
                        }
                    }
                }
            }
        }
    }

    private fun onSaveClicked(t: Int) {
        launchMain {
            withMainDef(null) {
                binding.editMessage.text
            }.alsoSus {
                if (it.isNullOrEmpty()) return@alsoSus
                doSaveNote(it, t)
            }
        }
    }

    private suspend fun doSaveNote(msg: android.text.Editable, t: Int) {
        withBack {
            ctx.stringForWidget(msg.toString())
            val tim = System.currentTimeMillis()
            DataKeeperItem(
                dataText = msg.toString(),
                keeperType = t,
                emailToSome = null,
                emailSubject = null,
                timeData = tim,
                recordPath = null,
                recordLength = 0L,
                imageUrl = null,
                dayNum = tim.fetchCalenderDay,
                isDone = false,
            ).run {
                save()
                if (t == KEEP_REMEMBER) {
                    remNotification()
                }
            }
        }
    }

    private suspend fun DataKeeperItem.save() {
        withBack {
            ctx.newDBDataSus((s1 ?: return@withBack).noteDb) {
                insertMsg(s1)
            }
        }
        withMain {
            binding.editMessage.setText("")
            mAdapter.run { addItem() }
            binding.recyclerView.smoothScrollToPosition(mAdapter.dataItems.size - 1)
        }
    }

    private fun onEmail() {
        val msg = binding.editMessage.text.toString().trim { it <= ' ' }
        if (childFragmentManager.findFragmentByTag(DIALOG_EMAIL) == null) {
            com.pt.pro.notepad.dialogs.DialogEmail.newInstance(msg, this).also {
                it.show(childFragmentManager, DIALOG_EMAIL)
            }
        }
    }

    override suspend fun saveImg(filePath: String?) {
        val tim = System.currentTimeMillis()
        DataKeeperItem(
            dataText = null,
            keeperType = KEEP_IMG,
            emailToSome = null,
            emailSubject = null,
            timeData = tim,
            recordPath = null,
            recordLength = 0L,
            imageUrl = filePath,
            dayNum = tim.fetchCalenderDay,
            isDone = false,
        ).save()
    }

    override fun onTextDeleted() {
        binding.sendButton.changeToMessage(false)
    }

    override fun TablesModelMonth.onPicClickedData(tableIndexNumber: Int, reload: Boolean) {
        launchImdMain {
            withMain {
                pointerImage?.rotationHandForFirstTime()
            }
            withMain {
                if (!waitForDetails) {
                    s1 = mTableName
                    date1 = mTableTime
                    counterBoolean = false
                    tableIndex = tableIndexNumber
                    adapterLoad(mTableName ?: return@withMain)
                    loadDetails()
                    waitForDetails = true
                } else {
                    if (reload) {
                        loadAgain(tableIndex)
                        s1?.let { adapterLoad(it) }
                        waitForDetails = false
                    } else {
                        s1?.let { adapterDetailsLoad(it, mTableName ?: return@let) }
                    }
                    mAdapter.refresh()
                }
                if (isPickCalender) {
                    binding.imageOption.svgReColor(them.findAttr(R.attr.rmoGrey))
                    isPickCalender = false
                }
            }
            s1?.letSus {
                ctx.updatePrefString(LAST_NOTE_TABLE, it)
            }
        }
    }

    override fun onPicFirstData(clickedUser: String?, ownerName: String?) {
        launchImdMain {
            withMain {
                tableUserName = clickedUser
                binding.apply {
                    tableRecyclerView.removeAllViewsInLayout()
                }
            }
            withMain {
                clickForData(clickedUser ?: return@withMain)
            }
        }
    }

    private suspend fun loadAgain(tableIndex: Int) {
        withBack {
            val tdh = TableDetailsHolder(
                tableIndex,
                nightRider = nightRider,
                user = false,
                detail = false,
                firstTime = false,
                isNotification = false
            )
            tableAdapter = com.pt.pro.notepad.adapters.TableAdapterData(
                tablesModels,
                mutableListOf(),
                tdh,
                dataListener = this@FragmentNote,
                false,
                ctx.fetchColorList()
            )
        }
        withMain {
            binding.tableRecyclerView.apply {
                adapter = tableAdapter
                scheduleLayoutAnimation()
            }
        }
    }

    private suspend fun TablesModelMonth.loadDetails() {
        withBack {
            val detailsArray = loadWithCategory()
            val tdh = TableDetailsHolder(
                0,
                nightRider = nightRider,
                user = false,
                detail = true,
                firstTime = true,
                isNotification = false
            )
            com.pt.pro.notepad.adapters.TableAdapterData(
                detailsArray,
                mutableListOf(),
                tdh,
                this@FragmentNote,
                false,
                ctx.fetchColorList()
            ).also {
                tableAdapter = it
            }
        }
        withMain {
            binding.tableRecyclerView.apply {
                layoutAnimation = ctx.translateRec(
                    dur = 300L,
                    del = 0.1F
                )
                adapter = tableAdapter
                scheduleLayoutAnimation()
            }
        }
    }

    private var resultScan: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            launchMain {
                withMain {
                    adapterLoad(s1 ?: return@withMain)
                }
            }
        }

    private var resultImg: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
                it?.data.bringImg(ctx.getLocalFilePath)
            }
        }

    private fun android.content.Intent?.bringImg(localImage: FileLate) {
        launchDef {
            val pathPick = withBackDef(null) {
                if (this@bringImg == null) {
                    ctx.getCaptureImageOutputUri(com.pt.pro.BuildConfig.APPLICATION_ID)?.path
                } else {
                    ctx.getImagePathFromURI(this@bringImg.data ?: return@withBackDef null)
                }
            }
            withBack {
                if (pathPick == null) return@withBack
                if (ctx.findBooleanPreference(IMG_COMP, true)) {
                    ctx.compressImage(pathPick) {
                        pushImage(localImage)
                    }
                } else {
                    FileLate(pathPick).copyFileTo(localImage)
                    ctx.deleteFileProvider(com.pt.pro.BuildConfig.APPLICATION_ID)
                    saveImg(localImage.absolutePath)
                }
            }
        }
    }

    override fun longCLick(idUser: String, IsBachUp: Boolean, justMonth: String?) {
        if (IsBachUp) {
            launchDef {
                val outputPath = ctx.prepareBackUpData()
                if (outputPath != null) {
                    if (justMonth == null) {
                        backUpProcessData(idUser, null)
                    } else {
                        backUpProcessData(tableUserName ?: return@launchDef, justMonth)
                    }
                }
            }
        } else {
            if (childFragmentManager.findFragmentByTag(DIALOG_CHOOSE_IMPORT) == null) {
                com.pt.pro.notepad.dialogs.ChooseForImport.newInstance(importListener).also {
                    it.show(childFragmentManager, DIALOG_CHOOSE_IMPORT)
                }
            }
        }
    }

    private var importListener: com.pt.pro.notepad.dialogs.ChooseForImport.ImportListener?
        get() {
            return com.pt.pro.notepad.dialogs.ChooseForImport.ImportListener { opt ->
                if (opt == 111) {
                    newUserLauncher()
                } else {
                    android.content.Intent(android.content.Intent.ACTION_GET_CONTENT).apply {
                        addCategory(android.content.Intent.CATEGORY_OPENABLE)
                        type = "*/*"
                    }.runCatching {
                        resultBack?.launch(this)
                    }.onFailure {
                        ctx.makeToastRec(R.string.px, 0)
                    }
                }
            }
        }
        set(value) {
            value.logProvLess()
        }

    override fun newUserLauncher() {
        launchMain {
            if (childFragmentManager.findFragmentByTag(DIALOG_NEW_USER) == null) {
                com.pt.pro.notepad.dialogs.DialogNewUser.newInstance(
                    userListener, false,
                    nightRider
                ).also {
                    it.show(childFragmentManager, DIALOG_NEW_USER)
                }
            }
        }
    }


    private var userListener: com.pt.pro.notepad.dialogs.DialogNewUser.CreateNewListener?
        get() {
            return com.pt.pro.notepad.dialogs.DialogNewUser.CreateNewListener { realName, dataName ->
                launchDef {
                    ctx.newDBDataUser {
                        TablesModelUser(userName = realName, userId = dataName).insertUser()
                    }
                    ctx.newDBTables(dataName.noteTab) {
                        createTable(dataName)
                    }
                }

            }
        }
        set(value) {
            value.logProvLess()
        }

    private var resultBack: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            launchDef {
                ctx.getPath(
                    (it.data ?: return@launchDef).data ?: return@launchDef
                ).let { aa ->
                    if (aa != com.pt.common.BuildConfig.DATA_NULL) {
                        if (getNewUserData(aa)) {
                            val tablesUser = ctx.newDBDataUser {
                                getAllUsers()
                            }
                            TablesModelUser(
                                userId = ADD_USER,
                                userName = ADD_USER
                            ).runSusBack {
                                tablesUser.add(this)
                            }
                            val tdh = TableDetailsHolder(
                                0,
                                nightRider = nightRider,
                                user = true,
                                detail = false,
                                firstTime = true,
                                isNotification = false
                            )
                            com.pt.pro.notepad.adapters.TableAdapterData(
                                mutableListOf(),
                                tablesUser,
                                tdh,
                                dataListener = this@FragmentNote,
                                false,
                                ctx.fetchColorList()
                            ).alsoSus {
                                tableAdapter = it
                                binding.tableRecyclerView.apply {
                                    adapter = it
                                    scheduleLayoutAnimation()
                                }
                            }
                        }
                    } else {
                        ctx.makeToastRecSus(R.string.mr, 0)
                    }
                }
            }
        }

    override fun onImageClick(uri: String) {
        if (childFragmentManager.findFragmentByTag(FRAGMENT_IMAGE) == null) {
            launchMain {
                withMainNormal {
                    childFragmentManager.fragmentStackLauncher(FRAGMENT_IMAGE) {
                        setCustomAnimations(
                            R.animator.slide_down, R.animator.slide_up,
                            R.animator.slide_down_close, R.animator.slide_up_close
                        )
                        add(R.id.tabData, DataImageView.newInstance(uri), FRAGMENT_IMAGE)
                        addToBackStack(FRAGMENT_IMAGE)
                    }
                }
            }
        }
    }

    override fun onCalendarClicked(start: Long, end: Long, dayOfMonth: Int) {
        launchDef {
            withBackDef(mutableListOf()) {
                ctx.newDBDataSus(s1!!.noteDb) {
                    getInTime(s1, dayOfMonth, ctx.findBooleanPref(ID_INC, INC_DONE, false))
                }
            }.displayFromCalendar(start, dayOfMonth)
        }
    }

    private suspend fun MutableList<DataKeeperItem>.displayFromCalendar(
        start: Long,
        dayOfMonth: Int,
    ) {
        withMain {
            mDataKeeperItemsList = this@displayFromCalendar
            selectedCalendar = start
            isPickCalender = true
            binding.imageOption.svgReColor(them.findAttr(android.R.attr.colorAccent))
            childFragmentManager.popBackStack()
            binding.recyclerView.recycledViewPool.clear()
            mAdapter.apply { mDataKeeperItemsList.updateDataAdapter() }
            ctx.makeToast(dayOfMonth, 0)
        }
    }

    override fun onActionActions(opt: Int) {
        launchDef {
            when (opt) {
                HIDE_SEL -> {
                    mAdapter.refresh()
                }
                TXT_SEL -> {
                    doForTxtSender()
                }
                IMG_SEL -> {
                    doForImgSender()
                }
                MULTI_IMG_SEL -> {
                    doForImgSender()
                }
                RECORD_SEL -> {
                    doForRecordSender()
                }
                MULTI_RECORD_SEL -> {
                    doForRecordSender()
                }
            }
        }
    }

    private fun doForTxtSender() {
        haveShare = false
        myShare(true)
    }

    private fun doForImgSender() {
        haveShare = true
        myShare(false)
    }

    private fun doForRecordSender() {
        haveShare = true
        myShare(false)
    }

    override fun removeItem(position: Int, delete: Boolean) {
        mAdapter.apply {
            dataItems[position].copy().let {
                dataItems.removeAtIndex(position)
                notifyItemRemoved(position)
                it.afterSwipe(position, delete)
            }
        }
    }

    override fun duringSwipe(option: String, show: Boolean) {
        /*binding.tempHint.apply {
            if (show) {
                justVisible()
                setTextColor(them.findAttr(android.R.attr.colorAccent))
                text = option
            } else {
                justGone()
            }
        }*/
    }

    override fun onLink(clip: Boolean, text: String?) {
        if (clip && text != null) {
            launchDef {
                ctx.sendToClipDate(text)
                ctx.makeToastRecSus(R.string.z6, 0)
            }
        } else {
            ctx.makeToastRec(R.string.vk, 0)
        }
    }

    private suspend fun adapterLoad(aa: String) {
        withBack {
            mDataKeeperItemsList = ctx.newDBDataSus((s1 ?: return@withBack).noteDb) {
                getAllChat(aa)
            }
        }
        withMain {
            justCoroutineMain {
                mAdapter.applySus { mDataKeeperItemsList.updateDataAdapter() }
                binding.applySus {
                    recyclerView.recycledViewPool.clear()
                    recyclerView.visibleFade(200L)
                }
            }
            context.nullChecker()
            justCoroutineMain {
                binding.mainDataCon.justVisible()
                act.intent.apply {
                    if (getStringExtra(android.content.Intent.EXTRA_TEXT) != null) {
                        getStringExtra(android.content.Intent.EXTRA_TEXT).let {
                            binding.editMessage.setText(it)
                        }
                    }
                }
            }
        }
    }

    private fun androidx.recyclerview.widget.RecyclerView.adapterDate() {
        com.pt.pro.notepad.adapters.SwipeToDeleteHelper(
            mAdapter,
            ctx.compactImageReturn(R.drawable.ic_done),
            ctx.compactImageReturn(R.drawable.ic_reload),
            ctx.compactImageReturn(R.drawable.ic_delete_swipe),
            them.findAttr(android.R.attr.colorAccent),
            R.string.d1.dStr,
            R.string.dn.dStr,
            R.string.dr.dStr,
            this@FragmentNote
        ).run {
            androidx.recyclerview.widget.ItemTouchHelper(this)
        }.apply {
            attachToRecyclerView(this@adapterDate)
        }
    }

    private fun shareImage30() {
        launchDef {
            mAdapter.temp.toMutableList().toMutableList().asSequence().filter {
                it.imageUrl != null
            }.toMutableList().also {
                if (it.size == 1) {
                    ctx.shareSingleImage(it.firstOrNull() ?: return@also) {
                        act.startActivity(this)
                    }
                } else {
                    ctx.shareSetImage(it) {
                        act.startActivity(this)
                    }
                }
            }
        }
    }

    private fun shareRecord30() {
        launchDef {
            mAdapter.temp.toMutableList().toMutableList().asSequence().filter {
                it.recordPath != null
            }.toMutableList().also {
                if (it.size == 1) {
                    ctx.shareSingleRecord(it.firstOrNull() ?: return@also) {
                        act.startActivity(this)
                    }
                } else {
                    ctx.shareSetRecord(it) {
                        act.startActivity(this)
                    }
                }
            }
        }
    }

    private fun android.graphics.Bitmap.pushImage(new: FileLate) {
        launchDef {
            withBack {
                context.nullChecker()
                writeCompressedBitmap(new)
            }
            withBack {
                if (context == null) return@withBack
                ctx.deleteFileProvider(com.pt.pro.BuildConfig.APPLICATION_ID)
            }
            saveImg(new.absolutePath)
        }
    }


    override val onMyOption: Boolean
        get() {
            return when {
                binding.linearScroll.childCount != 0 -> {
                    mAdapter.refresh()
                    false
                }
                childFragmentManager.backStackEntryCount != 0 -> {
                    childFragmentManager.popBackStack()
                    false
                }
                else -> {
                    true
                }
            }
        }


    override fun DataKeeperItem.saveEmail() {
        launchDef {
            save()
        }
    }

    override fun onDestroyView() {
        resultNot?.unregister()
        resultScan?.unregister()
        resultImg?.unregister()
        resultBack?.unregister()
        pointerImage = null
        resultBack = null
        resultScan = null
        resultImg = null
        resultNot = null
        userListener = null
        importListener = null
        super.onDestroyView()
    }
}
