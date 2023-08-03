package com.pt.pro.main.former

import com.pt.common.global.*
import com.pt.common.media.*
import com.pt.common.stable.*

class FragmentMain : com.pt.common.mutual.life.GlobalSimpleFragment<com.pt.pro.main.odd.FragmentMainFasten>(), ((Int) -> Unit) {

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        @com.pt.common.global.UiAnn get() = {
            com.pt.pro.main.odd.MainFasten.run {
                this@creBin.context.inflaterMainFragment(
                    act = this@creBin.context.actionBarHeight,
                    rmoBack = this@creBin.context.theme.findAttr(com.pt.pro.R.attr.rmoBackground),
                    rmoText = this@creBin.context.theme.findAttr(com.pt.pro.R.attr.rmoText),
                )
            }.also {
                @com.pt.common.global.ViewAnn
                binder = it
            }.root
        }

    @Volatile
    private var lastIcon: Int? = null

    @Volatile
    internal var iconsName: MutableList<Int> = mutableListOf()
        get() = if (field.isEmpty()) iconList else field

    private var swiped = false
    private var mainAdapter: MainAdapter? = null
    private var isBothPer = true

    private var musicHolderNative: MutableList<MusicSack>? = mutableListOf()
    private inline val musicHolder: MutableList<MusicSack>
        get() = musicHolderNative ?: mutableListOf<MusicSack>().also {
            musicHolderNative = it
        }

    private var isFirst: Boolean = true
    private var musicPos: Int = 0

    override var lastJob: kotlinx.coroutines.Job? = null
    override var qHand: android.os.Handler? = null
    override var disposableMain: InvokingMutable = mutableListOf()
    override var invokerBagList: InvokingBackMutable = mutableListOf()
    override var exHand: java.util.concurrent.ScheduledExecutorService? = null


    @android.annotation.SuppressLint("Range")
    @com.pt.common.global.UiAnn
    override fun com.pt.pro.main.odd.FragmentMainFasten.onViewCreated() {
        qHand = (context ?: activity?.baseContext).let {
            if (it != null) {
                it.fetchHand
            } else {
                activity?.finish()
                null
            }
        }
        lifecycle.addObserver(this@FragmentMain)
        pushJob {
            launchDef {
                intiViews()
            }
        }
        act.intent.extras?.getString(android.app.SearchManager.QUERY).let {
            if (it != null) {
                pushJob { j ->
                    launchDef {
                        j?.checkIfDone()
                        fetchFromSearchQuery(it.lowercase())
                    }
                }
            } else return@let
        }
    }

    private fun ddoInti() {
        launchDef {
            context.nullChecker()
            withBack {
                activity?.setUpLaunchReview()
                intiWorks()
                context.nullChecker()
            }
        }
    }

    private suspend fun intiWorks() {
        justCoroutineBack {
            if (ctx.hasExternalReadWritePermission) {
                com.pt.pro.main.odd.WorkInitializer().create(
                    act.applicationContext
                ).alsoSusBack { wm ->
                    if (ctx.findBooleanPreferenceNull(KEY_STORY) == true) {
                        listOf(
                            androidx.work.OneTimeWorkRequest.Builder(
                                com.pt.pro.alarm.release.AlarmWorker::class.java
                            ).build(),
                            androidx.work.OneTimeWorkRequest.Builder(
                                com.pt.pro.gallery.objects.StatusWorker::class.java,
                            ).build(),
                            androidx.work.OneTimeWorkRequest.Builder(
                                com.pt.pro.gallery.objects.HiddenWorker::class.java
                            ).build(),
                        )
                    } else {
                        listOf(
                            androidx.work.OneTimeWorkRequest.Builder(
                                com.pt.pro.alarm.release.AlarmWorker::class.java
                            ).build(),
                            androidx.work.OneTimeWorkRequest.Builder(
                                com.pt.pro.gallery.objects.HiddenWorker::class.java,
                            ).build()
                        )
                    }.alsoSusBack { works ->
                        wm.enqueueUniqueWork("AllWorks", androidx.work.ExistingWorkPolicy.REPLACE, works)
                    }
                }
            } else return@justCoroutineBack
        }
    }

    private fun intiFirstWorks() {
        catchyUnit {
            if (ctx.hasExternalReadWritePermission) {
                com.pt.pro.main.odd.WorkInitializer().create(
                    act.applicationContext
                ).also { wm ->
                    wm.enqueueUniqueWork(
                        "FirstWorks", androidx.work.ExistingWorkPolicy.REPLACE,
                        androidx.work.OneTimeWorkRequest.Builder(
                            com.pt.pro.gallery.objects.HiddenWorker::class.java,
                        ).build()
                    )
                }
            } else return@catchyUnit
        }
    }

    private inline val MutableList<MusicSack>.filterMusic: suspend (String) -> MutableList<MusicSack>
        get() = { str ->
            toMutableList().asSequence().filter {
                it.title.toStr.lowercase().contains(
                    str, true
                ) || it.pathAudio.toStr.lowercase().contains(
                    str, true
                )
            }.toMutableList()
        }

    private suspend fun fetchFromSearchQuery(
        query: String,
    ) {
        when {
            query.startsWith("play ", ignoreCase = true) -> {
                query.drop(5)
            }
            else -> {
                query
            }
        }.let { qur ->
            cont.allArtistsLoader().filterMusic(qur).run {
                if (this@run.isEmpty()) {
                    cont.allAudioLoader(false).filterMusic(qur)
                } else {
                    this@run
                }
            }
        }.apply {
            if (this@apply.isEmpty()) {
                ctx.makeToastRecSus(com.pt.pro.R.string.hw, 0)
            } else {
                if (ctx.isServiceNotRunning(com.pt.pro.musical.music.MusicPlayer::class.java.canonicalName)) {
                    this@apply.launchMusic(0)
                } else {
                    this@apply.relaunchMusic(0)
                }
            }
        }
    }

    private suspend fun com.pt.pro.main.odd.FragmentMainFasten.intiViews() {
        withBack {
            iconsName = ctx.createIconsArray()
            lastIcon = ctx.fetchLastApp()
        }
        context.nullChecker()
        withMain {
            binder?.intiRec(rec.isLandTraditional)
            cardMain.intiBack21(them.findAttr(android.R.attr.colorPrimary))
            pagerMenu.setOnClickListener(this@FragmentMain)
            mainBack.setOnClickListener(this@FragmentMain)
        }
        justScope {
            ddoInti()
        }
    }

    private suspend fun com.pt.pro.main.odd.FragmentMainFasten.intiRec(isLandTraditional: Boolean) {
        justCoroutine {
            iconsRecyclerView.layoutManager = com.pt.common.moderator.recycler.NoAnimGridManager(
                ctx, if (isLandTraditional) 3 else 2
            )
            root_.fetchViewDim { rootWidth: Int, rootHeight: Int ->
                (rootHeight - ctx.actionBarHeight).let {
                    if (isLandTraditional) it - 10F.toPixel else it
                }.also { h ->
                    launchImdMain {
                        iconsRecyclerView.loadApps(rootWidth, h, isLandTraditional)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFirst) {
            activity?.intent?.apply {
                if (isV_NOT_M && action == WRITE_SETTING_ACCESS) {
                    requestSettingPermissions?.launch(arrayOf(android.Manifest.permission.WRITE_SETTINGS))
                }
                if (getBooleanExtra(PLAY_MUSIC, false) && (!ctx.canFloat || !ctx.hasExternalReadWritePermission)) {
                    invoke(MUSIC)
                }
            }
            isFirst = false
        } else {
            launchImdMain {
                ctx.fetchLastApp().alsoSus { a ->
                    withMain {
                        toCatchSack(42) {
                            mainAdapter?.refreshAdapter(a, -1)
                        }.postNow()
                    }
                }
            }
        }
    }

    private inline val android.content.Context.fetchLastApp: suspend () -> Int
        get() = {
            lastIcon ?: findIntegerPreference(ID_APP, MUSIC)
        }


    @com.pt.common.global.UiAnn
    private suspend fun androidx.recyclerview.widget.RecyclerView.loadApps(
        widthOriginal: Int,
        heightOriginal: Int,
        isLand: Boolean,
    ) {
        withMain {
            adapter = null
        }
        withMain {
            MainAdapter(
                iconsName,
                if (isLand) widthOriginal / 3 else widthOriginal / 2,
                if (isLand) heightOriginal / 2 else heightOriginal / 3,
                nightRider,
                this@FragmentMain
            ).alsoSus {
                it.lastApp = lastIcon ?: MUSIC
                mainAdapter = it
                adapter = it
            }
            simpleCallback?.letSus { androidx.recyclerview.widget.ItemTouchHelper(it).attachToRecyclerView(this@loadApps) }
        }
    }

    @com.pt.common.global.UiAnn
    override fun com.pt.pro.main.odd.FragmentMainFasten.onClick(v: android.view.View) {
        when (v) {
            mainBack -> homeIntent.also(::startActivity)
            pagerMenu -> doPopDisplay()
        }
    }

    private inline val drag: Int
        get() {
            return (androidx.recyclerview.widget.ItemTouchHelper.UP or androidx.recyclerview.widget.ItemTouchHelper.DOWN or androidx.recyclerview.widget.ItemTouchHelper.START or androidx.recyclerview.widget.ItemTouchHelper.END)
        }

    private var simpleCallback: androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback? =
        object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(drag, 0) {
            @com.pt.common.global.UiAnn
            override fun onSelectedChanged(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE && swiped) {
                    pushJob {
                        launchMain {
                            withMain {
                                mainAdapter?.apply {
                                    iconsName.updateIcons()
                                }
                            }
                            updateNewIcons()
                        }
                    }
                }
            }

            @com.pt.common.global.UiAnn
            override fun onMove(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                target: androidx.recyclerview.widget.RecyclerView.ViewHolder,
            ): Boolean {
                val fromPosition = viewHolder.absoluteAdapterPosition
                val toPosition = target.absoluteAdapterPosition
                swiped = true
                iconsName.apply {
                    if (fromPosition <= toPosition) {
                        java.util.Collections.rotate(
                            subList(
                                fromPosition, toPosition + 1
                            ), -1
                        )
                    } else {
                        java.util.Collections.rotate(
                            subList(
                                toPosition, fromPosition + 1
                            ), 1
                        )
                    }
                }
                recyclerView.adapter?.notifyItemMoved(fromPosition, toPosition)
                swiped = true
                return false
            }

            override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {}
        }


    internal suspend fun updateNewIcons() {
        withBack {
            ctx.apply {
                iconsName.forEachIndexed { i, s ->
                    updatePrefInt(ICON_KEY + i, s)
                }
            }
        }
    }

    private inline val android.content.Context.createIconsArray: suspend () -> MutableList<Int>
        get() = {
            mutableListOf<Int>().applySusBack {
                iconList.onEachIndexedSus(this@createIconsArray) { i, s ->
                    this@applySusBack.add(findIntegerPreference(ICON_KEY + i, s))
                }
            }.letSusBack {
                return@letSusBack if (it.size != 6) iconList else it
            }
        }

    private inline val iconList: MutableList<Int>
        get() {
            return mutableListOf<Int>().apply {
                add(ALARM)
                add(FILE_MAN)
                add(MUSIC)
                add(GALLERY)
                add(DATA_KEEP)
                add(OVERLAY)
            }
        }

    @com.pt.common.global.WorkerAnn
    override fun invoke(app: Int) {
        toCatchSack(43) {
            mainAdapter?.refreshAdapter(app, 1)
        }.postNow()
        pushJob {
            launchDef {
                justScope {
                    ctx.updatePrefInt(ID_APP, app)
                }
                withBack {
                    lastIcon = app
                    when (app) {
                        ALARM -> {
                            if (!ctx.hasExternalReadWritePermission || !ctx.hasVoicePermission) {
                                forAllCheck()
                                return@withBack
                            }
                            ctx.newIntentSus(com.pt.pro.alarm.views.MainActivityAlarm::class.java) {
                                flags = FLAGS
                                addCategory(LAUNCH_CATO)
                                this@newIntentSus
                            }.applySusBack {
                                this@FragmentMain.startActivity(this@applySusBack)
                            }
                        }
                        FILE_MAN -> {
                            if (!ctx.hasExternalReadWritePermission) {
                                forStorageCheck()
                                return@withBack
                            }
                            ctx.newIntentSus(com.pt.pro.file.views.MainFileManager::class.java) {
                                flags = FLAGS
                                addCategory(LAUNCH_CATO)
                                this@newIntentSus
                            }.applySusBack {
                                this@FragmentMain.startActivity(this@applySusBack)
                            }
                        }
                        DATA_KEEP -> {
                            if (!ctx.hasExternalReadWritePermission || !ctx.hasVoicePermission) {
                                forAllCheck()
                                return@withBack
                            }
                            ctx.newIntentSus(com.pt.pro.notepad.activities.NoteActivity::class.java) {
                                flags = FLAGS
                                addCategory(LAUNCH_CATO)
                                this@newIntentSus
                            }.applySusBack {
                                this@FragmentMain.startActivity(this@applySusBack)
                            }
                        }
                        GALLERY -> {
                            if (!ctx.hasExternalReadWritePermission) {
                                forStorageCheck()
                                return@withBack
                            }
                            ctx.newIntentSus(
                                com.pt.pro.gallery.activities.ActivityGallery::class.java
                            ) {
                                flags = FLAGS
                                addCategory(LAUNCH_CATO)
                                this@newIntentSus
                            }.applySusBack {
                                this@FragmentMain.startActivity(this@applySusBack)
                            }
                        }
                        OVERLAY -> {
                            if (!ctx.hasExternalReadWritePermission || !ctx.hasVoicePermission) {
                                forAllCheck()
                                return@withBack
                            }
                            ctx.newIntentSus(com.pt.pro.extra.screener.MainScreen::class.java) {
                                flags = FLAGS
                                addCategory(LAUNCH_CATO)
                                this@newIntentSus
                            }.applySusBack {
                                this@FragmentMain.startActivity(this@applySusBack)
                            }
                        }
                        MUSIC -> {
                            if (!ctx.hasExternalReadWritePermission) {
                                forStorageCheck()
                                return@withBack
                            }
                            openMusicPlaylist()
                        }
                    }
                }
            }
        }
    }

    private fun android.content.Context.popAbout(
        b: DSack<androidx.cardview.widget.CardView, androidx.appcompat.widget.AppCompatTextView, androidx.appcompat.widget.AppCompatTextView>.() -> Unit
    ) {
        return androidx.cardview.widget.CardView(this@popAbout).run root_@{
            android.view.WindowManager.LayoutParams(
                WRAP,
                WRAP
            ).apply {
                gravity = android.view.Gravity.END
            }
            setCardBackgroundColor(them.findAttr(com.pt.pro.R.attr.rmoBackHint))
            radius = dis.findPixelDis(10F).toFloat()
            val aboutPop: androidx.appcompat.widget.AppCompatTextView
            val settingPop: androidx.appcompat.widget.AppCompatTextView
            androidx.appcompat.widget.LinearLayoutCompat(context).apply lin@{
                framePara(WRAP, WRAP) {}
                orientation = androidx.appcompat.widget.LinearLayoutCompat.VERTICAL
                aboutPop = androidx.appcompat.widget.AppCompatTextView(context).apply {
                    editAppearanceLargePopupMenu()
                    linearPara(WRAP, WRAP, 1F) {
                        gravity = android.view.Gravity.CENTER_HORIZONTAL
                    }
                    compactImage(com.pt.pro.R.drawable.ripple_cur) {
                        background = this
                    }
                    gravity = android.view.Gravity.CENTER
                    minWidth = dis.findPixelDis(120F)
                    minHeight = dis.findPixelDis(48F)
                    dis.findPixelDis(35F).also { d35 ->
                        dis.findPixelDis(8F).also { d8 ->
                            setPadding(d35, d8, d35, d8)
                        }
                    }
                    textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                    text = resources.getString(com.pt.pro.R.string.qo)
                    setTextColor(them.findAttr(com.pt.pro.R.attr.rmoText))

                }.also(this@lin::addView)
                settingPop = androidx.appcompat.widget.AppCompatTextView(context).apply {
                    editAppearanceLargePopupMenu()
                    linearPara(WRAP, WRAP, 1F) {
                        gravity = android.view.Gravity.CENTER_HORIZONTAL
                    }
                    compactImage(com.pt.pro.R.drawable.ripple_cur) {
                        background = this
                    }
                    gravity = android.view.Gravity.CENTER
                    minWidth = dis.findPixelDis(120F)
                    minHeight = dis.findPixelDis(48F)
                    dis.findPixelDis(35F).also { d35 ->
                        dis.findPixelDis(8F).also { d8 ->
                            setPadding(d35, d8, d35, d8)
                        }
                    }
                    textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                    text = resources.getString(com.pt.pro.R.string.wx)
                    setTextColor(them.findAttr(com.pt.pro.R.attr.rmoText))
                }.also(this@lin::addView)
            }.also(this@root_::addView)
            DSack(this@root_, aboutPop, settingPop).also(b)
        }
    }

    @com.pt.common.global.UiAnn
    private fun doPopDisplay() {
        pushJob {
            launchMain {
                withMain {
                    ctx.popAbout {
                        android.widget.PopupWindow(
                            one, WRAP, WRAP, true
                        ).apply {
                            setBackgroundDrawable(android.graphics.drawable.ColorDrawable(0))
                            two.setOnClickListener {
                                pushJob {
                                    launchDef {
                                        forOpenSetting(isSetting = false)
                                        dismissSus()
                                    }
                                }
                            }
                            three.setOnClickListener {
                                pushJob {
                                    launchDef {
                                        forOpenSetting(isSetting = true)
                                        dismissSus()
                                    }
                                }
                            }
                            showAsDropDown(binding.pagerMenu)
                        }
                    }
                }
            }
        }
    }


    private suspend fun forOpenSetting(isSetting: Boolean) {
        withBack {
            ctx.newIntentSus(com.pt.pro.extra.views.SettingActivity::class.java) {
                flags = FLAGS
                addCategory(LAUNCH_CATO)
                putExtra(IS_SETTING, isSetting)
                this@newIntentSus
            }.applySusBack {
                this@FragmentMain.startActivity(this@applySusBack)
            }
        }
    }


    private fun openMusicPlaylist() {
        pushJob {
            launchDef {
                withBackDef(mutableListOf()) {
                    ctx.newDBPlaylist {
                        getAllSongsDefault()
                    }
                }.runSusBack {
                    context.nullChecker()
                    withBack {
                        if (isNotEmpty()) {
                            if (ctx.isServiceNotRunning(
                                    com.pt.pro.musical.music.MusicPlayer::class.java.canonicalName
                                )
                            ) {
                                launchMusic(-1)
                            } else {
                                relaunchMusic(-1)
                            }
                        } else {
                            cont.fetchAudioLoader()?.let {
                                mutableListOf(it).letSusBack { fe ->
                                    @Suppress("LongLine") if (ctx.isServiceNotRunning(
                                            com.pt.pro.musical.music.MusicPlayer::class.java.canonicalName
                                        )
                                    ) {
                                        fe.launchMusic(0)
                                    } else {
                                        fe.relaunchMusic(0)
                                    }
                                }
                            } ?: ctx.makeToastRecSus(com.pt.pro.R.string.hw, 0)
                        }
                    }
                }
            }
        }
    }

    private suspend fun MutableList<MusicSack>.launchMusic(pos: Int) {
        withMain {
            if (this@launchMusic.isEmpty()) return@withMain
            if (ctx.canFloat) {
                doLaunchMusic(pos)
            } else {
                if (isV_M) {
                    checkDrawOverlayPermission(pos)
                }
            }
        }
    }

    private fun MutableList<MusicSack>.doLaunchMusic(pos: Int) {
        launchDef {
            withBack {
                ctx.newIntentSus(com.pt.pro.musical.music.MusicPlayer::class.java) {
                    flags = FLAGS
                    action = PLAY_MUSIC
                    putExtra(KEY_ORDER, "tillITHurts")
                    putExtra(ALL_SONGS, this@doLaunchMusic as ArrayList)
                    putExtra(POPUP_POS, pos)
                    this@newIntentSus
                }.also {
                    androidx.core.content.ContextCompat.startForegroundService(
                        ctx, it
                    )
                    if (activity?.intent?.getBooleanExtra(SHORTCUT, false) == true) {
                        activity?.finish()
                    } else {
                        homeLauncher {
                            if (context != null) {
                                this@FragmentMain.startActivity(this)
                            }
                        }
                    }
                }

            }
        }
    }

    @com.pt.common.global.WorkerAnn
    private suspend fun MutableList<MusicSack>.relaunchMusic(pos: Int) {
        withBack {
            if (this@relaunchMusic.isEmpty()) return@withBack
            if (ctx.canFloat) {
                ctx.newIntentSus(com.pt.pro.musical.music.MusicPlayer::class.java) {
                    action = UPDATE_MUSIC
                    putExtra(KEY_ORDER, "tillITHurts")
                    putExtra(ALL_SONGS, this@relaunchMusic as ArrayList)
                    putExtra(POPUP_POS, pos)
                    this@newIntentSus
                }.also {
                    android.app.PendingIntent.getService(
                        ctx, 0, it, PEND_FLAG
                    ).send()
                }
            } else {
                if (isV_M) {
                    checkDrawOverlayPermission(pos)
                }
            }
        }
    }

    @APIAnn(android.os.Build.VERSION_CODES.M)
    private fun MutableList<MusicSack>.checkDrawOverlayPermission(pos: Int) {
        resultFloating?.launch(goToOverlay)
        musicHolder.addAll(this@checkDrawOverlayPermission)
        musicPos = pos
    }

    private var resultFloating: Bring = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) {
        if (ctx.canFloat && musicHolder.isNotEmpty()) {
            musicHolder.doLaunchMusic(musicPos)
        } else {
            ctx.makeToastRec(com.pt.pro.R.string.rl, 0)
        }
    }

    private fun forStorageCheck() {
        if (isV_R) {
            act.askForPermissionHighVersions(isBoth = false)
        } else {
            requestStoragePer?.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    private fun askForBothPermission() {
        if (isV_R) {
            act.askForPermissionHighVersions(isBoth = true)
        } else {
            requestBothPermissions?.launch(
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }


    private fun forAllCheck() {
        if (!ctx.applicationContext.hasExternalReadWritePermission) {
            askForBothPermission()
        } else {
            requestRecordPermissions?.launch(arrayOf(android.Manifest.permission.RECORD_AUDIO))
        }
    }


    @APIAnn(android.os.Build.VERSION_CODES.R)
    private fun androidx.appcompat.app.AppCompatActivity.askForPermissionHighVersions(
        isBoth: Boolean,
    ) {
        isBothPer = isBoth
        runCatching<androidx.appcompat.app.AppCompatActivity, Unit> {
            android.content.Intent(
                android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            ).apply {
                addCategory(android.content.Intent.CATEGORY_DEFAULT)
                data = (com.pt.common.BuildConfig.PACK_AGE +
                        com.pt.pro.BuildConfig.APPLICATION_ID).toUri
                activityLauncher?.launch(this)
            }
        }.onFailure {
            runCatching<androidx.appcompat.app.AppCompatActivity, Unit> {
                android.content.Intent(
                    android.provider.Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                ).apply {
                    addCategory(android.content.Intent.CATEGORY_DEFAULT)
                    data = (com.pt.common.BuildConfig.PACK_AGE +
                            com.pt.pro.BuildConfig.APPLICATION_ID).toUri
                    activityLauncher?.launch(this)
                }
            }.onFailure {
                this@FragmentMain.startActivity(storageSecondWayHigh(com.pt.pro.BuildConfig.APPLICATION_ID))
            }
            runOnUiThread {
                makeToastRec(com.pt.pro.R.string.mb, 1)
            }
        }
    }

    @APIAnn(android.os.Build.VERSION_CODES.R)
    private var activityLauncher: Bring = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) {
        if (android.os.Environment.isExternalStorageManager()) {
            if (isBothPer) {
                requestRecordPermissions?.launch(arrayOf(android.Manifest.permission.RECORD_AUDIO))
            } else {
                if (!ctx.hasNotificationPermission) {
                    forNotificationPer?.launch(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS))
                } else {
                    lastIcon?.let { it1 -> invoke(it1) }
                }
            }
            intiFirstWorks()
        } else {
            activity?.runOnUiThread {
                ctx.makeToastRec(com.pt.pro.R.string.pr, 0)
            }
        }
    }


    @APIAnn(android.os.Build.VERSION_CODES.TIRAMISU)
    private var forNotificationPer: BringPer = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { perm ->
        if (perm[android.Manifest.permission.POST_NOTIFICATIONS] == true) {
            lastIcon?.let { it1 -> invoke(it1) }
        } else {
            activity?.runOnUiThread {
                ctx.makeToastRec(com.pt.pro.R.string.pr, 0)
            }
        }
    }

    private var requestBothPermissions: BringPer = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { perm ->
        if (perm[android.Manifest.permission.READ_EXTERNAL_STORAGE] == true && perm[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
            if (!ctx.applicationContext.hasVoicePermission) {
                requestRecordPermissions?.launch(arrayOf(android.Manifest.permission.RECORD_AUDIO))
            }
            intiFirstWorks()
        } else {
            activity?.runOnUiThread {
                ctx.makeToastRec(com.pt.pro.R.string.pr, 0)
            }
        }
    }

    private var requestStoragePer: BringPer = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { perm ->
        if (perm[android.Manifest.permission.READ_EXTERNAL_STORAGE] == true && perm[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
            lastIcon?.let { it1 -> invoke(it1) }
            intiFirstWorks()
        } else {
            activity?.runOnUiThread {
                ctx.makeToastRec(com.pt.pro.R.string.pf, 0)
            }
        }
    }

    private var requestRecordPermissions: BringPer = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { perm ->
        if (perm[android.Manifest.permission.RECORD_AUDIO] == true) {
            if (!ctx.hasNotificationPermission) {
                forNotificationPer?.launch(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS))
            } else {
                lastIcon?.let { it1 -> invoke(it1) }
            }
        } else {
            activity?.runOnUiThread {
                ctx.makeToastRec(com.pt.pro.R.string.ix, 0)
            }
        }
    }


    private var requestSettingPermissions: BringPer = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { perm ->
        if (perm[android.Manifest.permission.WRITE_SETTINGS] == true) {
            homeLauncher {
                if (context != null) {
                    this@FragmentMain.startActivity(this)
                }
            }
        } else {
            activity?.runOnUiThread {
                ctx.makeToastRec(com.pt.pro.R.string.pr, 0)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        binder?.iconsRecyclerView?.adapter = null
        super.onConfigurationChanged(newConfig)
        launchImdMain {
            binder?.applySus {
                justCoroutineMain {
                    ctx.actionBarHeight.also { act ->
                        frameRec.myActMargin(act)
                        cardMain.cardAsView(act)
                    }
                    textPt.apply {
                        editAppearance()
                        setTextColor(them.findAttr(com.pt.pro.R.attr.rmoText))
                    }
                }
                intiRec(newConfig.isLandTrad)
            }
        }
    }

    private suspend fun android.app.Activity.setUpLaunchReview() {
        withDefault {
            findIntegerPreference(FIRST_FOR_RATE, -1).alsoSusBack { ffr ->
                catchyUnit {
                    if (ffr in 0..3) {
                        doLaunchReview(ffr)
                    } else if (ffr == -1) {
                        updatePrefInt(FIRST_FOR_RATE, 0)
                    }
                }
            }
        }
    }

    private suspend fun android.app.Activity.doLaunchReview(ffr: Int) {
        withDefault {
            com.google.android.play.core.review.ReviewManagerFactory.create(this@doLaunchReview).also { rm ->
                rm.requestReviewFlow().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.let {
                            rm.launchReviewFlow(this@doLaunchReview, it).addOnCompleteListener {
                                launchDef {
                                    updatePrefInt(FIRST_FOR_RATE, ffr + 1)
                                }
                            }
                        }
                    } else {
                        launchDef {
                            updatePrefInt(FIRST_FOR_RATE, ffr + 1)
                        }
                    }
                }
            }
        }
    }

    @com.pt.common.global.MainAnn
    override fun onDestroyView() {
        com.pt.pro.gallery.objects.MyHash.apply {
            REC_MEDIA_NATIVE?.clear()
            REC_MEDIA_NATIVE = null
        }
        mainAdapter?.onAdapterDestroy()
        binder = null
        lastIcon = null
        iconsName.clear()
        swiped = false
        mainAdapter = null
        isBothPer = true
        simpleCallback = null
        requestBothPermissions?.unregister()
        requestStoragePer?.unregister()
        requestRecordPermissions?.unregister()
        requestSettingPermissions?.unregister()
        resultFloating?.unregister()
        com.pt.pro.main.odd.MainFasten.destroyFasten()
        resultFloating = null
        requestBothPermissions = null
        requestStoragePer = null
        requestRecordPermissions = null
        requestSettingPermissions = null
        if (isV_R) {
            activityLauncher?.unregister()
            activityLauncher = null
        }
        if (isV_T) {
            forNotificationPer?.unregister()
            forNotificationPer = null
        }
        isFirst = true
        musicHolderNative = null
        super.onDestroyView()
    }

}

