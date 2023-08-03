package com.pt.pro.notepad.fragments

import android.content.Intent
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.doAfterTextChanged
import com.pt.common.global.*
import com.pt.common.moderator.recycler.OverScrollBounceBehavior
import com.pt.common.moderator.recycler.OverScrollHorizontalBehavior
import com.pt.common.stable.*
import com.pt.pro.R
import com.pt.pro.notepad.models.*
import com.pt.pro.notepad.objects.*
import kotlin.random.Random

class FragmentCounter : ParentCounter() {

    companion object {
        @Volatile
        @JvmStatic
        var isKeys: Boolean = false
    }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn
        get() = {
            com.pt.pro.databinding.FragmentCounterBinding.inflate(this@creBin, this, false).also {
                @com.pt.common.global.ViewAnn
                binder = it
                it.initViews()
            }.root
        }

    override fun com.pt.pro.databinding.FragmentCounterBinding.onViewCreated() {
        qHand = ctx.fetchHand
        lifecycle.addObserver(this@FragmentCounter)

        launchDef {
            ctx.newDBDataUserCounter {
                getAllCounterUsers()
            }.letSusBack {
                TablesModelUser(userId = ADD_USER, userName = ADD_USER).run {
                    it.add(this)
                    it
                }
            }.letSusBack { tablesUser ->
                withBack {
                    TableDetailsHolder(
                        tableIndex,
                        nightRider = nightRider,
                        user = true,
                        detail = false,
                        firstTime = true
                    ).letSusBack { tdh ->
                        displayIntiViews(tablesUser, tdh)
                    }
                }
            }
        }
    }

    private suspend fun com.pt.pro.databinding.FragmentCounterBinding.displayIntiViews(
        tablesUser: MutableList<TablesModelUser>,
        tdh: TableDetailsHolder,
    ) {
        context.nullChecker()
        withMain {
            recyclerviewCounter.layoutAnimation = ctx.fallDownAnimationRec(
                dur = 500L,
                0.1F,
            )
            com.pt.pro.notepad.adapters.TableAdapterData(
                mutableListOf(),
                tablesUser,
                tdh,
                this@FragmentCounter,
                true,
                ctx.fetchColorList()
            ).also {
                tableAdapterCounter = it
            }
        }
        context.nullChecker()
        withMain {
            scrollCounter.isSmoothScrollingEnabled = false
            counterDaysRecycler.layoutManager = ctx.getManager
            recyclerviewCounter.apply {
                adapter = tableAdapterCounter
                scheduleLayoutAnimation()
            }
            add.setOnClickListener(this@FragmentCounter)
            save.setOnClickListener(this@FragmentCounter)
            spinnerLinear.setOnClickListener(this@FragmentCounter)
            android.widget.ArrayAdapter(
                ctx,
                R.layout.custom_list_item,
                R.id.suggestionsText,
                rec.getStringArray(R.array.sg)
            ).alsoSus {
                adapterText = it
            }
        }
    }

    private suspend fun MutableList<Int>.reorder(): MutableList<Int> = kotlin.run {
        withBackDef(this) {
            sortedWith(
                compareBy(String.CASE_INSENSITIVE_ORDER) { it.toString() }
            ).reversed().toMutableList()
        }
    }

    private suspend fun com.pt.pro.databinding.FragmentCounterBinding.clearCounter() {
        withMain {
            isSaved = false
            editableCounter.clear()
            rootView.removeAllViews()
            sumLinearInvisible.justInvisible()
            sumLinear.setBackgroundResource(R.color.fre)
            sumText.text?.clear()
            counterSaveSize = 0
        }
    }

    @com.pt.common.global.UiAnn
    override fun TablesModelMonth.onPicClickedData(tableIndexNumber: Int, reload: Boolean) {
        launchDef {
            withMain {
                binding.clearCounter()
            }
            withMain {
                if (!waitForDetails) {
                    tableIndex = tableIndexNumber
                    loadAgain(tableIndex)
                    numbersLoad(mTableName ?: return@withMain, mTableTime)
                } else {
                    if (reload) {
                        tableTimeCounter = mTableTime
                        counterBoolean = true
                        tableCounter = (mTableName ?: return@withMain)
                        binding.linearCounterDaysRecycler.justVisible()
                        waitForDetails = false
                        loadAgain(tableIndex)
                    } else {
                        if (isKeys) {
                            loadFilter(currentKey, tableIndexNumber == 1)
                        } else {
                            loadFilter(currentDay.toString(), tableIndexNumber == 1)
                        }
                    }
                }
            }
        }
    }

    private suspend fun numbersLoad(pictureFolderPath: String, tableTime: Long) {
        val days = justCoroutine {
            getDaysNumber()
        }
        withMain {
            tableTimeCounter = tableTime
            counterBoolean = true
            tableCounter = pictureFolderPath
            binding.linearCounterDaysRecycler.justVisible()
            days.loadDaysNumber()
        }
        withMain {
            binding.counterDaysRecycler.apply {
                System.currentTimeMillis().fetchCalender.also { cal ->
                    tableTime.fetchCalender.also { calTwo ->
                        toCatchSack(998) {
                            counterDaysAdapter?.resetFromOut()
                            if (cal[java.util.Calendar.MONTH] == calTwo[java.util.Calendar.MONTH]) {
                                smoothScrollToPosition(cal[java.util.Calendar.DAY_OF_MONTH])
                            } else {
                                scrollToPosition(days.size - 1)
                            }
                        }.postNow()
                    }
                }
            }
        }
    }

    private fun getDaysNumber(): MutableList<String> = kotlin.run {
        return@run mutableListOf<String>().apply {
            for (i in 1..counterDaysMax) {
                add(i.toString())
            }
            add(allStringResource)
        }
    }

    private fun getNumberOfDays(): MutableList<Int> = kotlin.run {
        return@run mutableListOf<Int>().apply {
            for (i in 1..counterDaysMax) {
                add(i)
            }
        }
    }

    private suspend fun getKeys(): MutableList<String> = justCoroutine {
        withBackDef(mutableListOf()) {
            tableCounter?.let {
                ctx.newDBCounter(it.countDb) {
                    getAllKeys(it)
                }
            } ?: mutableListOf()
        }.run {
            add(allStringResource)
            this
        }
    }

    private suspend fun MutableList<String>.loadDaysNumber() {
        withMain {
            if (counterDaysAdapter == null) {
                counterDaysAdapter = com.pt.pro.notepad.adapters.CounterDaysAdapter(
                    this@loadDaysNumber, tableTimeCounter,
                    allStringResource, this@FragmentCounter
                )
                binding.counterDaysRecycler.adapter = counterDaysAdapter
            } else {
                counterDaysAdapter?.apply {
                    clickedDay = -1
                    tableTimeCounter = this@FragmentCounter.tableTimeCounter
                    update(them.findAttr(android.R.attr.colorPrimary))
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun loadAgain(tableIndex: Int) {
        withMain {
            TableDetailsHolder(
                tableIndex,
                nightRider = nightRider,
                user = false,
                detail = false,
                firstTime = false
            ).let { tdh ->
                com.pt.pro.notepad.adapters.TableAdapterData(
                    tablesModelCounters ?: return@withMain,
                    mutableListOf(),
                    tdh,
                    this@FragmentCounter,
                    true,
                    ctx.fetchColorList()
                ).also {
                    tableAdapterCounter = it
                    binding.recyclerviewCounter.apply {
                        adapter = it
                        scheduleLayoutAnimation()
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    override fun onPicFirstData(clickedUser: String?, ownerName: String?) {
        launchDef {
            withMain {
                tableUserName = clickedUser
                toCatchSack(998) {
                    binding.recyclerviewCounter.removeAllViewsInLayout()
                }.postNow()
            }
            withMain {
                clickForCounter(clickedUser ?: return@withMain)
            }
        }

    }

    @com.pt.common.global.UiAnn
    override fun onCounterDays(CounterDaysNumber: String?, schedule: Boolean) {
        launchDef {
            withMain {
                loadDetails(schedule)
                waitForDetails = true
                binding.counterLayout.justVisible()
                binding.clearCounter()
                if (CounterDaysNumber == allStringResource) {
                    binding.apply {
                        add.justGone()
                        save.justGone()
                        sumLinearInvisible.justVisible()
                        sumLinear.setBackgroundColor(them.findAttr(android.R.attr.colorAccent))
                    }
                    loadAllPrices()
                } else {
                    binding.apply {
                        sumLinearInvisible.justGone()
                        add.justVisible()
                        save.justVisible()
                    }
                    if (!isKeys) {
                        currentDay = (CounterDaysNumber ?: return@withMain).toInt()
                        getCountersItems(currentDay)
                    } else {
                        currentKey = CounterDaysNumber.toString()
                        getKeyItems(CounterDaysNumber ?: return@withMain)
                    }.loadCounter()
                }
                binding.linearButton.justVisible()
            }
        }
    }

    private suspend fun loadAllPrices() = justCoroutine {
        var allPrices = 0.0
        withBack {
            ctx.newDBCounter(tableCount.countDb) {
                getNumberOfDays().forEach {
                    getAllOnDay(tableCounter, it).let { a ->
                        if (a.prices != 0.0) {
                            loadUiDayPrices(it, a.prices.toString())
                            allPrices += a.prices
                        }
                    }
                }
                getAllKeys(tableCounter).onEachIndexedSusBack(context) { i, it ->
                    getAllOnKey(tableCounter, it).let { a ->
                        if (a.prices != 0.0) {
                            loadUiKeyPrices(it, i, a.prices.toString())
                            allPrices += a.prices
                        }
                    }
                }
            }
        }
        displaySum(allPrices)
    }

    @com.pt.common.global.UiAnn
    private suspend fun displaySum(allPrices: Double) {
        withMain {
            binding.sumText.text = allPrices.toString().toEditable
        }
    }

    override fun com.pt.pro.databinding.FragmentCounterBinding.onClick(v: android.view.View) {
        when (v) {
            spinnerLinear -> doKeyDisplay()
            save -> doSaveKey()
            add -> doAddCounter()
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.databinding.FragmentCounterBinding.onLongClick(
        v: android.view.View,
    ): Boolean {
        return true
    }

    @com.pt.common.global.UiAnn
    private fun doAddCounter() {
        launchDef {
            withMain {
                (System.currentTimeMillis() + Random.nextInt(1000)).let {
                    CounterItem(
                        idCounter = it,
                        time = System.currentTimeMillis(),
                        counterText = null,
                        price = null,
                        day = currentDay,
                        key = null
                    ).run {
                        counterView(true, them.findAttr(R.attr.rmoText))
                    }
                }
                binding.scrollCounter.scrollCounter()
            }
        }
    }

    @Suppress("KotlinConstantConditions")
    @com.pt.common.global.WorkerAnn
    private fun doSaveKey() {
        launchDef {
            val newInt: MutableList<Int> = mutableListOf()
            var allPricesSaved = 0.0
            withBack {
                ctx.newDBCounter(tableCount.countDb) {
                    editableCounter.onEachIndexedSusBack(context) { index, it ->
                        if (it.target.counterText.isNullOrEmpty()) {
                            deleteCounter(it.target.time, tableCounter)
                            newInt.add(index)
                        } else {
                            if (it.statCreate) {
                                it.target.insertCounter(tableCounter)
                            } else if (it.statUpdate && !it.statCreate) {
                                it.target.updateCounter(tableCounter).let { c ->
                                    if (c == -1) it.target.insertCounter(tableCounter)
                                }
                            }
                            allPricesSaved += it.target.price ?: 0.0
                        }
                        editableCounter[index] = it.copy(statCreate = false, statUpdate = false)
                    }
                }
            }
            newInt.displayKey(allPricesSaved)
        }
    }

    private suspend fun MutableList<Int>.displayKey(allPricesSaved: Double) {
        withMain {
            this@displayKey.reorder().forEach {
                binding.rootView.removeViewAt(it)
                editableCounter.removeAtIndex(it)
            }
            binding.apply {
                sumLinearInvisible.justVisible()
                sumLinear.setBackgroundColor(them.findAttr(android.R.attr.colorAccent))
                sumText.setText(allPricesSaved.toString())
            }
            isSaved = true
            ctx.makeToastRec(R.string.dn, 0)
            //onCounterDays(currentDay.toString(), false)
        }
    }

    @com.pt.common.global.UiAnn
    private fun doKeyDisplay() {
        launchDef {
            withMain {
                with(
                    com.pt.pro.databinding.PopWindowKeyBinding.inflate(
                        layoutInflater,
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
                        dayOption.setOnClickListener {
                            launchDef {
                                withMain {
                                    binding.counterState.text = R.string.dj.dStr
                                    isKeys = false
                                    dismiss()
                                }
                                binding.clearCounter()
                                getDaysNumber().loadDaysNumber()
                            }
                        }
                        keyOption.setOnClickListener {
                            launchDef {
                                withMain {
                                    binding.counterState.text = R.string.ks.dStr
                                    binding.clearCounter()
                                    getKeys().loadDaysNumber()
                                    isKeys = true
                                    dismiss()
                                }
                            }
                        }
                        showAsDropDown(binding.spinnerLinear)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun loadUiDayPrices(newDays: Int, newPrices: String) {
        withMain {
            with(com.pt.pro.databinding.ItemAllPricesBinding.inflate(layoutInflater)) {
                daysPrices.text = newDays.toString()
                allPrices.text = newPrices
                daysPrices.setOnClickListener {
                    doClickDaysPri(newDays)
                }
                binding.rootView.addView(root)
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun doClickDaysPri(newDays: Int) {
        launchDef {
            binding.clearCounter()
            withMain {
                if (isKeys) {
                    isKeys = false
                    getDaysNumber().loadDaysNumber()
                }
                currentDay = newDays
                binding.apply {
                    sumLinearInvisible.justGone()
                    add.justVisible()
                    save.justVisible()
                }
            }
            getCountersItems(currentDay).let {
                withMain {
                    it.loadCounter()
                    binding.counterDaysRecycler.scrollToPosition(newDays - 1)
                    kotlinx.coroutines.delay(150L)
                    counterDaysAdapter?.run {
                        changeColor(newDays - 1, false)
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun loadUiKeyPrices(newDays: String, i: Int, newPrices: String) {
        withMain {
            with(com.pt.pro.databinding.ItemAllPricesBinding.inflate(layoutInflater)) {
                daysPrices.text = newDays
                allPrices.text = newPrices
                daysPrices.setOnClickListener {
                    clickUiKeyPrices(newDays, i)
                }
                binding.rootView.addView(root)
            }
        }
    }


    @com.pt.common.global.UiAnn
    private fun clickUiKeyPrices(newDays: String, i: Int) {
        launchDef {
            binding.clearCounter()
            withMain {
                if (!isKeys) {
                    isKeys = true
                    getKeys().loadDaysNumber()
                }
                binding.apply {
                    sumLinearInvisible.justGone()
                    add.justVisible()
                    save.justVisible()
                }
            }
            getKeyItems(newDays).let {
                withMain {
                    it.loadCounter()
                    binding.counterDaysRecycler.scrollToPosition(i)
                    kotlinx.coroutines.delay(150L)
                    counterDaysAdapter?.run {
                        changeColor(i, false)
                    }
                }
            }
        }
    }

    private suspend fun getCountersItems(CounterDaysNumber: Int): ItemForCounter = justCoroutine {
        return@justCoroutine ctx.newDBCounter(tableCount.countDb) {
            getAllOnDay(tableCounter, CounterDaysNumber)
        }
    }

    private suspend fun getKeyItems(key: String): ItemForCounter = justCoroutine {
        return@justCoroutine ctx.newDBCounter(tableCount.countDb) {
            getAllOnKey(tableCounter, key)
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun loadDetails(schedule: Boolean) {
        withMain {
            tablesModelCounters?.getI?.let {
                it(tableIndex)
            }?.loadWithCategoryCounter()?.let { dA ->
                TableDetailsHolder(
                    tableIndex,
                    nightRider = nightRider,
                    user = false,
                    detail = true,
                    firstTime = true
                ).let { tdh ->
                    com.pt.pro.notepad.adapters.TableAdapterData(
                        dA.toMutableList(),
                        mutableListOf(),
                        tdh,
                        this@FragmentCounter,
                        true,
                        ctx.fetchColorList()
                    ).also {
                        tableAdapterCounter = it
                        binding.recyclerviewCounter.apply {
                            adapter = it
                            scheduleLayoutAnimation()
                            if (schedule) scheduleLayoutAnimation()
                        }
                    }
                }
            }
        }
    }

    private inline val counterDaysMax: Int
        get() {
            return tableTimeCounter.fetchCalender.getActualMaximum(java.util.Calendar.DATE)
        }

    @com.pt.common.global.UiAnn
    private fun androidx.core.widget.NestedScrollView.scrollCounter() {
        post {
            getChildAt(childCount - 1).run {
                bottom + paddingBottom
            }.let { bottom ->
                smoothScrollBy(0, (bottom - (scrollY + height) + 1))
            }
        }
    }

    private suspend fun loadFilter(CounterDaysNumber: String, positive: Boolean) = justCoroutine {
        withBack {
            ctx.newDBCounter(tableCount.countDb) {
                if (positive) {
                    getAllOnDayPositive(tableCounter, CounterDaysNumber, isKeys)
                } else {
                    getAllOnDayNegative(tableCounter, CounterDaysNumber, isKeys)
                }
            }.displayFilter()
        }
    }

    private suspend fun ItemForCounter.displayFilter() {
        withMain {
            counterItem.forEach {
                kotlinx.coroutines.delay(10)
                it.counterView(false, them.findAttr(R.attr.rmoText))
            }
            binding.apply {
                sumLinearInvisible.justVisible()
                sumLinear.setBackgroundColor(them.findAttr(android.R.attr.colorAccent))
                sumText.setText(prices.toString())
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun ItemForCounter.loadCounter() {
        withMain {
            tableCounter?.let {
                ctx.newDBCounter(tableCount.countDb) {
                    getAllKeys(it)
                }.also {
                    withMain {
                        strings.clear()
                    }
                    withMain {
                        strings.addAll(it)
                    }
                }
            }
            counterItem.forEach {
                kotlinx.coroutines.delay(10)
                it.counterView(false, them.findAttr(R.attr.rmoText))
            }
            binding.apply {
                sumLinearInvisible.justVisible()
                sumLinear.setBackgroundColor(them.findAttr(android.R.attr.colorAccent))
                sumText.setText(prices.toString())
            }
        }
    }

    @com.pt.common.global.UiAnn
    private suspend fun CounterItem.counterView(create: Boolean, color: Int) {
        withMain {
            editableCounter.add(CounterUpdate(this@counterView, create, false))
            val i = editableCounter.lastIndex
            with(com.pt.pro.databinding.ItemCounterBinding.inflate(layoutInflater)) {
                keyText.apply {
                    applyEdit(color)
                    if (key != null && key != com.pt.common.BuildConfig.DATA_NULL) {
                        text = key?.toEditable
                    }
                    setAdapter(adapterText)
                    doAfterTextChanged { s ->
                        if (s.isNullOrEmpty()) {
                            null
                        } else {
                            s.toString()
                        }.let { ss ->
                            this@counterView.let { new ->
                                new.key = ss
                                if (isSaved) {
                                    editableCounter.first {
                                        it.target.time == time
                                    }.let {
                                        it.target = new
                                        it.statCreate = false
                                        it.statUpdate = true
                                    }
                                } else {
                                    editableCounter[i].let {
                                        it.target = new
                                        it.statCreate = create
                                        it.statUpdate = true
                                    }
                                }
                            }
                        }
                    }
                }
                textCounter.apply {
                    applyEdit(color)
                    setAdapter(adapterText)
                    text = counterText?.toEditable
                    doAfterTextChanged { s ->
                        if (s.isNullOrEmpty()) {
                            null
                        } else {
                            s.toString()
                        }.let { ss ->
                            this@counterView.let { new ->
                                new.counterText = ss
                                if (isSaved) {
                                    editableCounter.first {
                                        it.target.time == time
                                    }.let {
                                        it.target = new
                                        it.statCreate = false
                                        it.statUpdate = true
                                    }
                                } else {
                                    editableCounter[i].let {
                                        it.target = new
                                        it.statCreate = create
                                        it.statUpdate = true
                                    }
                                }
                            }
                        }
                    }
                }
                priceCounter.apply {
                    applyEdit(color)
                    text = price?.toString()?.toEditable
                    doAfterTextChanged { s ->
                        if (s.isNullOrEmpty()) {
                            null
                        } else {
                            s.toString()
                        }.let { ss ->
                            this@counterView.let { new ->
                                new.price = if (ss.isNullOrEmpty()) {
                                    0.0
                                } else {
                                    runCatching {
                                        ss.toDouble()
                                    }.getOrDefault(-0.0)
                                }
                                if (isSaved) {
                                    editableCounter.first {
                                        it.target.time == time
                                    }.let {
                                        it.target = new
                                        it.statCreate = false
                                        it.statUpdate = true
                                    }
                                } else {
                                    editableCounter[i].let {
                                        it.target = new
                                        it.statCreate = create
                                        it.statUpdate = true
                                    }
                                }
                            }
                        }
                    }
                }
                root.animate().alpha(1F).duration = 200L
                binding.rootView.addView(root)
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    override fun longCLick(idUser: String, IsBachUp: Boolean, justMonth: String?) {
        launchDef {
            if (IsBachUp) {
                ctx.alertBackUp(idUser, justMonth)
            } else {
                askForImport()
            }
        }
    }

    private suspend fun android.content.Context.alertBackUp(userId: String, justMonth: String?) {
        withMain {
            if (nightRider) {
                androidx.appcompat.app.AlertDialog.Builder(
                    this@alertBackUp,
                    R.style.DeleteAlarmDialogTheme_Night
                )
            } else {
                androidx.appcompat.app.AlertDialog.Builder(
                    this@alertBackUp,
                    R.style.DeleteAlarmDialogTheme_Light
                )
            }.applySus {
                setTitle(context.resources.getString(R.string.pv))
                setMessage(context.resources.getString(R.string.bw))
                setPositiveButton(context.resources.getString(R.string.er)) { _, _ ->
                    doBackUp(userId, justMonth)
                }
                setNegativeButton(context.resources.getString(R.string.co)) { _, _ -> }
                create().runSus {
                    context.theme.findAttr(R.attr.rmoBackHint).toDrawable().letSus {
                        window?.setBackgroundDrawable(it)
                    }
                    show()
                }
            }
        }
    }

    private suspend fun askForImport() {
        withMainNormal {
            if (childFragmentManager.findFragmentByTag(DIALOG_IMPORT) == null) {
                com.pt.pro.notepad.dialogs.ChooseForImport.newInstance(
                    importListener
                ).alsoSus {
                    it.show(childFragmentManager, DIALOG_IMPORT)
                }
            }
        }
    }

    private fun doBackUp(idUser: String, justMonth: String?) {
        launchDef {
            withBack {
                if (ctx.prepareCounterBackUp() != null) {
                    if (justMonth == null) {
                        backUpCounterProcess(idUser, null)
                    } else {
                        backUpCounterProcess(tableUserName ?: return@withBack, justMonth)
                    }
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
                    runCatching {
                        Intent(Intent.ACTION_GET_CONTENT).apply {
                            addCategory(Intent.CATEGORY_OPENABLE)
                            type = "*/*"
                            resultGal?.launch(this)
                        }
                    }.onFailure {
                        ctx.makeToastRec(R.string.px, 0)
                    }
                }
            }
        }
        set(value) {
            value.logProvLess()
        }

    @com.pt.common.global.WorkerAnn
    override fun newUserLauncher() {
        launchMain {
            if (childFragmentManager.findFragmentByTag(DIALOG_CREATE) == null) {
                com.pt.pro.notepad.dialogs.DialogNewUser.newInstance(
                    userListener ?: return@launchMain,
                    false,
                    nightRider
                ).also {
                    it.show(childFragmentManager, DIALOG_CREATE)
                }
            }
        }
    }

    private var userListener: com.pt.pro.notepad.dialogs.DialogNewUser.CreateNewListener?
        get() {
            return com.pt.pro.notepad.dialogs.DialogNewUser.CreateNewListener { real, dataName ->
                launchDef {
                    ctx.newDBDataUserCounter {
                        TablesModelUser(userName = real, userId = dataName).apply {
                            insertCounterUser()
                        }
                    }
                    ctx.newDBTablesCounter(dataName.countTab) {
                        createTable(dataName)
                    }
                }
            }
        }
        set(value) {
            value.logProvLess()
        }


    private var resultGal: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
                launchDef {
                    it.data?.data?.let { uri ->
                        resultForImport(uri)
                    }
                }
            }
        }

    private suspend fun resultForImport(uri: android.net.Uri) {
        ctx.getPath(uri).let { aa ->
            if (aa != com.pt.common.BuildConfig.DATA_NULL) {
                if (ctx.getNewCounterUser(aa)) {
                    ctx.newDBDataUserCounter {
                        getAllCounterUsers()
                    }.let {
                        TablesModelUser(
                            userId = ADD_USER,
                            userName = ADD_USER
                        ).run {
                            it.add(this)
                            it
                        }
                    }.let { tablesUser ->
                        TableDetailsHolder(
                            tableIndex,
                            nightRider = nightRider,
                            user = true,
                            detail = false,
                            firstTime = true
                        ).let { tdh ->
                            withMain {
                                com.pt.pro.notepad.adapters.TableAdapterData(
                                    mutableListOf(),
                                    tablesUser,
                                    tdh,
                                    this@FragmentCounter,
                                    true,
                                    ctx.fetchColorList()
                                ).alsoSus {
                                    tableAdapterCounter = it
                                    binding.recyclerviewCounter.apply {
                                        adapter = it
                                        scheduleLayoutAnimation()
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                withMain {
                    ctx.makeToastRec(R.string.mr, 0)
                }
            }
        }
    }

    private suspend fun clickForCounter(ss: String) {
        justCoroutine {
            createTime().let {
                TablesModelMonth(
                    mTableName = returnTableName(ss, it.time),
                    tableDisplay = returnTableTitle(ss, it.time),
                    mTableTime = it.timeInMillis
                )
            }
        }.alsoSusBack { tablesModelMonth ->
            justCoroutine {
                ctx.newDBTablesCounter(ss.countTab) {
                    getAllCounterTables(ss)
                }.indexOfFirst {
                    it.mTableName == tablesModelMonth.mTableName
                }
            }.alsoSusBack { checkTables ->
                withBack {
                    if (checkTables == -1) {
                        kotlin.runCatching {
                            tablesModelMonth.createNewMonth(ss)
                        }.onFailure {
                            it.toStr.logProvCrash("checkTables")
                        }
                    }
                }
                loadDirectly(ss)
            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun TablesModelMonth.createNewMonth(ss: String) {
        adapterCreateCounter(mTableName ?: return)
        ctx.newDBTablesCounter(ss.countTab) {
            insertTable(ss)
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun adapterCreateCounter(tableCounter: String) {
        ctx.newDBCounter(tableCounter.countDb) {
            createMsgTable(tableCounter)
        }
    }


    private suspend fun loadDirectly(ss: String) {
        ctx.newDBTablesCounter(ss.countTab) {
            getAllCounterTables(ss)
        }.letSus { tablesModel ->
            tablesModelCounters = tablesModel
            TableDetailsHolder(
                tableIndex,
                nightRider = nightRider,
                user = false,
                detail = false,
                firstTime = true
            ).let { tdh ->
                com.pt.pro.notepad.adapters.TableAdapterData(
                    tablesModel,
                    mutableListOf(),
                    tdh,
                    this@FragmentCounter,
                    true,
                    ctx.fetchColorList()
                ).also {
                    tableAdapterCounter = it
                }
            }
        }
        withMain {
            binding.recyclerviewCounter.apply {
                adapter = tableAdapterCounter
                scheduleLayoutAnimation()
                runCatching {
                    if ((tablesModelCounters ?: return@apply).size != 0) {
                        toCatchSackAfter(99, 200L) {
                            tablesModelCounters?.let {
                                smoothScrollToPosition(it.size - 1)
                            }
                        }.postAfter()
                    }
                }
            }
        }
    }

    @com.pt.common.global.UiAnn
    private fun com.pt.pro.databinding.FragmentCounterBinding.initViews() {
        if (rec.isLandTraditional) {
            codMon.framePara(80F.toPixel, -1) {}
            linearCounterDaysRecycler.framePara(-1, -2) {
                marginStart = 80F.toPixel
            }
            counterLayout.framePara(-1, -1) {
                marginStart = 80F.toPixel
                topMargin = 70F.toPixel
                counterLayout.layoutParams = this
            }
            recyclerviewCounter.apply {
                androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(-1, -1).apply {
                    behavior = OverScrollBounceBehavior(ctx)
                }.also {
                    layoutParams = it
                }
                layoutManager = ctx.getVerManager
                adapter = tableAdapterCounter
                scheduleLayoutAnimation()
            }
        } else {
            codMon.framePara(-1, 80F.toPixel) {}
            linearCounterDaysRecycler.framePara(-1, -2) {
                topMargin = 80F.toPixel
            }
            counterLayout.framePara(-1, -1) {
                topMargin = 150F.toPixel
            }
            recyclerviewCounter.apply {
                androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams(-1, -1).apply {
                    behavior = OverScrollHorizontalBehavior(ctx)
                }.also {
                    layoutParams = it
                }
                layoutManager = ctx.getManager
                adapter = tableAdapterCounter
                scheduleLayoutAnimation()
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        launchDef {
            withMain {
                binding.initViews()
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        resultGal?.unregister()
        counterDaysAdapter?.onAdapterDestroy()
        importListener = null
        resultGal = null
        userListener = null
        binder?.recyclerviewCounter?.adapter = null
        binder?.counterDaysRecycler?.adapter = null
        super.onDestroyView()
    }

}