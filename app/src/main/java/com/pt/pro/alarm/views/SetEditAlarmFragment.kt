package com.pt.pro.alarm.views

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.pt.common.global.*
import com.pt.common.media.compressImage
import com.pt.common.media.deleteImage
import com.pt.common.media.getAudioPathFromURI
import com.pt.common.media.getImagePathFromURI
import com.pt.common.stable.*

class SetEditAlarmFragment : BaseSetAlarm() {

    private var fetchHandlerNative: android.os.Handler? = null

    private inline val fetchHand: android.os.Handler
        get() = fetchHandlerNative ?: ctx.fetchHand.also {
            fetchHandlerNative = it
        }

    override var ala: AlarmSack? = null
    private inline fun alarm(a: AlarmSack.() -> Unit) {
        ala.also {
            if (it != null) {
                a.invoke(it)
            } else {
                act.toFinishSetAlarm()
            }
        }
    }

    override val android.view.LayoutInflater.creBin: android.view.ViewGroup?.() -> android.view.View
        get() = {
            com.pt.pro.alarm.fasten.AlarmFasten.run {
                this@creBin.context.inflaterSetAlarm()
            }.also {
                binder = it
                it.subMainAlarm.orientation = rec.linDirection
                //it.setAlarmCard.intiBack21(them.findAttr(android.R.attr.colorPrimary))
                it.textLayout.justInvisible()
                it.sendButtonAlarm.justInvisible()
                when (act.intent.getIntExtra(MODE_EXTRA, ADD_ALARM)) {
                    EDIT_ALARM -> com.pt.pro.R.string.ed.dStr
                    else -> com.pt.pro.R.string.al.dStr
                }.let { tit ->
                    title = tit
                    binding.alarmMode.text = tit
                }
            }.root
        }

    override fun com.pt.pro.alarm.fasten.SetAlarmFasten.onViewCreated() {
        launchDef {
            intiAlarmDet {
                launchImdMain {
                    myViewCreated()
                }
            }
        }
    }

    private suspend fun myViewCreated() {
        isCircle = act.findBooleanPreference(CURRENT_PICKER, true)
        withMain {
            com.pt.pro.alarm.fasten.AlarmFasten.runSus {
                ctx.inflaterTime(isCircle)
            }.alsoSus { t ->
                stubPicker = t
                binding.pickTime.addViewSus { t.root_ }
                t.initTimePicker()
            }
        }
    }


    private suspend fun intiAlarmDet(a: () -> Unit) {
        withDefault {
            alarm {
                this@alarm.allDays.contains(true)
                setClicked = false
                record = this@alarm.recordAlarm
                image = this@alarm.imgBackground
                ringtoneUri = if (this@alarm.ringAlarm != null) {
                    this@alarm.ringAlarm
                } else {
                    ctx.fetchDefAlarm.toStr.letSusBack { def ->
                        ctx.findStringPreference(LAST_RINGTONE, def).letSusBack { pref ->
                            cont.getAudioPathFromURI(pref).letSusBack {
                                if (FileLate(it.toStr).exists()) {
                                    pref
                                } else {
                                    def
                                }
                            }
                        }
                    }
                }
            }
        }
        justCoroutine {
            a.invoke()
        }
    }

    private suspend fun com.pt.pro.alarm.fasten.SetAlarmFasten.intiViewDet() {
        justCoroutineMain {
            mainBack.setOnClickListener(this@SetEditAlarmFragment)
            modeDone.setOnClickListener(this@SetEditAlarmFragment)
            alarm {
                binding.textLabel.setText(this@alarm.labelAlarm)
                playAlarmRecord.setOnClickListener(this@SetEditAlarmFragment)
                if (this@alarm.recordAlarm != null) {
                    if (FileLate(this@alarm.recordAlarm.toStr).isFile) playAlarmRecord.justVisible()
                }
            }
            ctx.getOwnFile(com.pt.pro.notepad.objects.RECORD_FILE).let {
                it.fileCreator()
                binding.sendButtonAlarm.setDirectoryAudio(it)
            }

            binding.sendButtonAlarm.apply {
                setOnRecordClickListener {

                }
            }
            binding.sendButtonAlarm.setOnListenerRecord(recordListener)
            ringtonePickerButton.apply {
                ringtoneUri?.let {
                    ringtonePickerButton.text = android.media.RingtoneManager.getRingtone(
                        ctx,
                        it.toUri
                    ).getTitle(ctx)
                }
                setOnClickListener(this@SetEditAlarmFragment)
                setOnLongClickListener(this@SetEditAlarmFragment)
            }
            buttonBackgroundPicker.apply {
                if (image != null) {
                    alarm { text = this@alarm.imgBackground?.justName }
                }
                setOnClickListener(this@SetEditAlarmFragment)
                setOnLongClickListener(this@SetEditAlarmFragment)
            }
            dismissWay.apply {
                alarm {
                    declineWay = this@alarm.dismissWay
                    text = this@alarm.dismissWay.dismissText
                    vibrateCheck.isChecked = this@alarm.isVibrate
                }
                setOnClickListener(this@SetEditAlarmFragment)
                setOnLongClickListener(this@SetEditAlarmFragment)
                vibrateCheck.jumpDrawablesToCurrentState()
            }
            setAlarmButton.apply {
                setOnClickListener(this@SetEditAlarmFragment)
                setOnLongClickListener(this@SetEditAlarmFragment)
            }
        }
    }

    private inline val String.justName: String
        get() = FileLate(this).nameWithoutExtension

    private var resultBack: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == androidx.appcompat.app.AppCompatActivity.RESULT_OK) {
                launchDef {
                    withBack {
                        ctx.getLocalFilePath.let { localImage ->
                            if (it.data == null) {
                                ctx.getCaptureImageOutputUri(
                                    com.pt.pro.BuildConfig.APPLICATION_ID
                                )?.path
                            } else {
                                it?.data?.data?.let { itD ->
                                    ctx.getImagePathFromURI(itD)
                                }
                            }.let { pathPick ->
                                if (pathPick != null) {
                                    ctx.compressImage(pathPick) {
                                        pushImage(localImage)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    private fun android.graphics.Bitmap.pushImage(new: FileLate) {
        launchDef {
            withBack {
                if (context == null) return@withBack
                writeCompressedBitmap(new)
            }
            withBack {
                if (context == null) return@withBack
                ctx.deleteFileProvider(com.pt.pro.BuildConfig.APPLICATION_ID)
                image = new.absolutePath
            }
            context.nullChecker()
            binding.updateImageTitle()
        }
    }

    private suspend fun com.pt.pro.alarm.fasten.SetAlarmFasten.updateImageTitle() {
        withMain {
            buttonBackgroundPicker.text = (image ?: return@withMain).justName
        }
    }

    private var resultRing: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            val uri: android.net.Uri? = if (isV_T) {
                it.data?.getParcelableExtra(
                    android.media.RingtoneManager.EXTRA_RINGTONE_PICKED_URI,
                    android.net.Uri::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                it.data?.getParcelableExtra(android.media.RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            }
            if (uri != null) {
                ringtoneUri = uri.toStr
                binding.ringtonePickerButton.text = android.media.RingtoneManager.getRingtone(
                    ctx,
                    uri
                ).getTitle(ctx)

            }
        }

    private fun resetTittle() {
        binding.alarmMode.apply {
            fetchHand.postDelayed({
                if (context != null) {
                    text = title
                }
            }, 500L)
        }
    }


    private suspend fun com.pt.pro.alarm.fasten.StubTimePickFasten.initTimePicker() {
        justCoroutineMain {
            if (isCircle)
                initCirclePicker()
            else
                initSpinnerPicker()
            alarm {
                this@initTimePicker.pickerCircle.apply {
                    setCirclePickerTime(this@alarm.timeAlarm)
                }
                this@initTimePicker.timeAlarm.run {
                    setTimePickerTime(this@alarm.timeAlarm)
                    setIs24HourView(isHour24)
                }
            }
            pickerCircle.timeChangedListener = com.sztorm.timepicker.TimeChangedListener {
                binding.alarmMode.text = com.pt.pro.alarm.objects.AlarmHelper.remainingTime(
                    currentDays,
                    it.currentTimeInMil,
                    true
                ).let { aa ->
                    "+ $aa"
                }
            }

            pickerCircle.addSenseListener(false) { _, _, type ->
                if (type == UP_SEN) {
                    resetTittle()
                }
            }

            timeAlarm.setOnTimeChangedListener { _, i, i2 ->
                binding.alarmMode.apply {
                    CalendarLate.getInstance().apply {
                        this[CalendarLate.HOUR_OF_DAY] = i
                        this[CalendarLate.MINUTE] = i2
                        this[CalendarLate.SECOND] = 0
                    }.also { calendar1 ->
                        com.pt.pro.alarm.objects.AlarmHelper.remainingTime(
                            currentDays,
                            calendar1.timeInMillis,
                            true
                        ).let {
                            return@let "+ $it"
                        }.let { txt ->
                            text = txt
                            runCatching {
                                handler?.postDelayed({
                                    if (context != null && txt == text) {
                                        resetTittle()
                                    }
                                }, 500L)
                            }
                        }
                    }
                }
            }
            switchAlarm.setOnLongClickListener(this@SetEditAlarmFragment)
            switchAlarm.setOnCheckedChangeListener { _, isChecked ->
                launchImdMain {
                    justCoroutine {
                        if (isChecked) {
                            isCircle = true
                            stubPicker?.initCirclePicker()
                        } else {
                            isCircle = false
                            stubPicker?.initSpinnerPicker()
                        }
                        resetTittle()
                    }
                    act.updatePrefBoolean(CURRENT_PICKER, isChecked)
                }
            }
            root_.apply {
                layoutAnimation = ctx.scaleDownAnimationRec(dur = 250, del = 0.1F)
            }.scheduleLayoutAnimation()
        }
        binder?.intiViewDet()
        justCoroutineMain {
            intiDaysViews()
        }
    }

    private fun intiDaysViews() {
        launchImdMain {
            withMain {
                com.pt.pro.alarm.fasten.AlarmFasten.runSus {
                    ctx.inflaterDay()
                }.alsoSus { d ->
                    binding.daysFrame.addViewSus { d.root_ }
                    d.initAlarmRepeater()
                }
            }
        }
    }

    private inline val com.sztorm.timepicker.PickedTime.currentTimeInMil: Long
        get() {
            return CalendarLate.getInstance().also {
                it[CalendarLate.HOUR_OF_DAY] = hour
                it[CalendarLate.MINUTE] = minute
                it[CalendarLate.SECOND] = 0
            }.timeInMillis
        }

    private fun com.pt.pro.alarm.fasten.StubTimePickFasten.initCirclePicker() {
        pickerCircle.setCirclePickerDate(timeAlarm.pickHour, timeAlarm.pickMinute)
        pickerCircle.justVisible()
        timeAlarm.justInvisible()
    }

    private fun com.pt.pro.alarm.fasten.StubTimePickFasten.initSpinnerPicker() {
        timeAlarm.apply {
            pickHour = pickerCircle.hour
            pickMinute = pickerCircle.minute
        }
        pickerCircle.justInvisible()
        timeAlarm.justVisible()
    }


    private suspend fun com.pt.pro.alarm.fasten.StubAlarmDayFasten.initAlarmRepeater() {
        withMain {
            if (rec.isRightToLeft) days.layoutDirection = android.view.View.LAYOUT_DIRECTION_RTL
            alarm { setDayCheckboxes(this@alarm) }
            binding.repeatingCheck.apply {
                if (anyDayRepeating) {
                    isChecked = true
                    jumpDrawablesToCurrentState()
                    days.justVisible()
                } else {
                    isChecked = false
                    days.justInvisible()
                }
            }
            myOnClickListener.also {
                mMon.setOnClickListener(it)
                mTues.setOnClickListener(it)
                mWed.setOnClickListener(it)
                mThurs.setOnClickListener(it)
                mFri.setOnClickListener(it)
                mSat.setOnClickListener(it)
                mSun.setOnClickListener(it)
            }
            binding.repeatingCheck.apply {
                setOnCheckedChangeListener { _, isChecked ->
                    if (!anyDayRepeating) {
                        if (isChecked) {
                            days.visibleFade(200)
                        } else {
                            days.invisibleFade(200)
                        }
                    } else {
                        days.invisibleFade(200)
                        notAnyDayRepeating()
                    }
                }
            }
            binding.apply {
                textLayout.visibleFade(300L)
                sendButtonAlarm.visibleFade(300L)
            }
        }
    }

    private var com.pt.pro.alarm.fasten.SetAlarmFasten?.recordListener: com.pt.pro.notepad.interfaces.OnAudioRecordListener?
        get() = object : com.pt.pro.notepad.interfaces.OnAudioRecordListener {
            override fun onRecordFinished(dataKeeperItem: com.pt.pro.notepad.models.DataKeeperItem?) {
                act.runOnUiThread {
                    if (dataKeeperItem != null) {
                        StringBuilder().append(dataKeeperItem.recordPath).let {
                            record = it.toStr
                        }
                        ctx.compactImage(com.pt.pro.R.drawable.ic_play_circle) {
                            this@recordListener?.playAlarmRecord?.apply {
                                setImageDrawable(this@compactImage)
                                justVisible()
                            }
                        }
                    } else {
                        ctx.compactImage(com.pt.pro.R.drawable.ic_play_circle) {
                            this@recordListener?.playAlarmRecord?.apply {
                                setImageDrawable(this@compactImage)
                                justGone()
                            }
                        }
                    }
                }
            }

            override fun onError(errorCode: Int) {
                act.runOnUiThread {
                    ctx.compactImage(com.pt.pro.R.drawable.ic_play_circle) {
                        this@recordListener?.playAlarmRecord?.apply {
                            setImageDrawable(this@compactImage)
                            justGone()
                        }
                    }
                }
            }

            override fun onRecordingStarted() {
                forStartRecord()
            }
        }
        set(value) {
            value.logProvLess()
        }


    internal fun forStartRecord() {
        activity?.runOnUiThread {
            ctx.compactImage(com.pt.pro.R.drawable.ic_mic) {
                binder?.playAlarmRecord?.apply {
                    setImageDrawable(this@compactImage)
                    justVisible()
                }
            }
            if (record != null && FileLate(record.toString()).isFile) {
                FileLate(record.toStr).deleteImage()
            }
            if (mediaPlayer != null) {
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }
    }

    override fun onPause() {
        mediaPlayer?.apply {
            catchy(Unit) {
                if (isPlaying) {
                    pause()
                }
            }
        }
        super.onPause()
    }

    private var recordCall: Player.Listener? = object : Player.Listener {

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            ctx.compactImage(
                if (isPlaying) com.pt.pro.R.drawable.ic_pause_music else com.pt.pro.R.drawable.ic_play_circle
            ) {
                binder?.playAlarmRecord?.setImageDrawable(this)
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            if (playbackState == Player.STATE_ENDED) {
                act.runOnUiThread {
                    binder?.apply {
                        ctx.compactImage(com.pt.pro.R.drawable.ic_play_circle) {
                            playAlarmRecord.setImageDrawable(this)
                        }
                    }
                    mediaPlayer?.stop()
                    mediaPlayer?.release()
                    mediaPlayer = null
                }
            }
        }
    }

    private var resultFloating: Bring =
        registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
        ) {
            if (ctx.canFloat) {
                doSaveAlarm()
            } else {
                ctx.makeToastRec(com.pt.pro.R.string.pr, 0)
            }
        }

    private fun doSaveAlarm() {
        launchImdMain {
            saveAlarm((stubPicker ?: return@launchImdMain).getTimeFromPicker.timeInMillis)
        }
    }

    private suspend fun saveAlarm(time: Long) {
        withMain {
            context.nullChecker()
            fetchAlarmTime(
                time
            ) { finalTime ->
                context.nullChecker()
                alarm {
                    fetchAlarmAll(finalTime, this@alarm.idAlarm) {
                        this@fetchAlarmAll.setAlarmLaunch()
                    }
                }
            }
        }
    }

    override fun com.pt.pro.alarm.fasten.SetAlarmFasten.onClick(v: android.view.View) {
        when (v) {
            setAlarmButton -> {
                if (ctx.canFloatForQ) {
                    doSaveAlarm()
                } else {
                    resultFloating?.launch(goToOverlay)
                }
            }
            ringtonePickerButton -> {
                ringtonePicker(com.pt.pro.R.string.so.dStr) {
                    resultRing?.launch(this)
                }
            }
            playAlarmRecord -> {
                if (mediaPlayer == null) {
                    val aa = record ?: return
                    if (FileLate(aa).isFile) {
                        soundNOT(aa)
                    }
                } else {
                    if ((mediaPlayer ?: return).isPlaying) {
                        mediaPlayer?.pause()
                    } else {
                        mediaPlayer?.play()
                    }
                }
            }
            mainBack -> act.onBackPressedDispatcher.onBackPressed()
            modeDone -> act.onBackPressedDispatcher.onBackPressed()
            buttonBackgroundPicker -> {
                ctx.findPicker(com.pt.pro.BuildConfig.APPLICATION_ID) {
                    resultBack?.launch(this)
                }
            }
            dismissWay -> {
                alarm {
                    launchMain {
                        modeDone.visibleFadeSus(250L)
                        launchDismissShow(this@alarm)
                    }
                }
            }
        }
    }


    private fun soundNOT(record: String) {
        runCatching {
            launchDef {
                doSoundNot(record)
            }
        }
    }

    private suspend fun doSoundNot(record: String) {
        withMain {
            androidx.media3.exoplayer.ExoPlayer.Builder(ctx)
                .setAudioAttributes(musicAudioAttr, true)
                .build().also {
                    it.playWhenReady = true
                }.applySus {
                    mediaPlayer = this
                    setMediaItem(MediaItem.Builder().setUri(record.toUri).build())
                    repeatMode = Player.REPEAT_MODE_OFF
                    recordCall?.let {
                        addListener(it)
                    }
                    prepare()
                }
        }
    }

    private suspend inline fun fetchAlarmTime(
        pickerTime: Long,
        crossinline b: suspend (Long) -> Unit
    ) {
        justCoroutine a@{
            return@a if (pickerTime < System.currentTimeMillis()) {
                pickerTime + 86400000L
            } else {
                pickerTime
            }
        }.let { time ->
            context.nullChecker()
            justCoroutine b@{
                return@b com.pt.pro.alarm.objects.AlarmHelper.run {
                    return@run getTimeForNextAlarm(pickerTime, currentDays)
                }
            }.let { cTime ->
                context.nullChecker()
                withMain {
                    time.fetchRemainingTime.let { reTime ->
                        if (anyDayRepeating) {
                            DSackT(cTime, pickerTime).getRemainingDays(
                                getString(com.pt.pro.R.string.y1),
                                getString(com.pt.pro.R.string.y5)
                            )
                        } else {
                            ""
                        }.let { reDayText ->
                            (com.pt.pro.R.string.aw.dStr + " " + "<b>" +
                                    reDayText + reTime + "</b>").let { sb2 ->
                                ctx.makeToast(sb2.toHtmlText, 0)
                            }
                        }
                    }
                }
                context.nullChecker()
                justCoroutine {
                    if (anyDayRepeating) {
                        cTime.timeInMillis
                    } else {
                        time
                    }.letSusBack(b)
                }
            }
        }
    }

    private suspend inline fun fetchAlarmAll(
        finalTime: Long,
        oldOneId: Int,
        crossinline b: AlarmSack.() -> Unit
    ) {
        withMain {
            setClicked = true
            binding.textLabel.text.letSus { labelText ->
                if (labelText.isNullOrEmpty()) {
                    (com.pt.pro.R.string.gt.dStr + String(Character.toChars(128513)) +
                            String(Character.toChars(128521)) +
                            com.pt.pro.R.string.up.dStr)
                } else {
                    labelText.toStr
                }
            }.letSus { textLabel ->
                if (ringtoneUri == null) {
                    ctx.fetchDefAlarm.toStr
                } else {
                    ringtoneUri.toStr
                }.runSus {
                    AlarmSack(
                        idAlarm = oldOneId,
                        timeAlarm = finalTime,
                        labelAlarm = textLabel,
                        ringAlarm = this@runSus,
                        dismissWay = declineWay,
                        recordAlarm = record,
                        switchAlarm = true,
                        imgBackground = image,
                        isAlarm = true,
                        allDays = currentDays,
                        isVibrate = binding.vibrateCheck.isChecked
                    ).letSus(b)
                }
            }
        }
    }

    private fun AlarmSack.setAlarmLaunch() {
        launchDef {
            justCoroutine {
                ctx.newDBAlarm {
                    updateAlarmSus()
                }
            }
            justCoroutine {
                ctx.updatePrefString(LAST_RINGTONE, ringtoneUri.toStr)
            }
            justCoroutine {
                com.pt.pro.alarm.release.AlarmReceiver.applySusBack {
                    ctx.setReminderAlarm(timeAlarm, idAlarm) {
                        act.finishSus()
                    }
                }
            }
        }
    }

    private suspend fun launchDismissShow(al: AlarmSack) {
        withMainNormal {
            childFragmentManager.fragmentStackLauncher(FRA_EDIT) {
                setCustomAnimations(
                    com.pt.pro.R.animator.slide_down, com.pt.pro.R.animator.slide_up,
                    com.pt.pro.R.animator.slide_down_close, com.pt.pro.R.animator.slide_up_close
                )
                newEditAlarmFragment {
                    ala = al.copy(dismissWay = declineWay)
                    editAlarmListener = this@SetEditAlarmFragment
                    this@newEditAlarmFragment
                }.also {
                    add(binding.alarmEdit.id, it, FRA_EDIT)
                }
                addToBackStack(FRA_EDIT)
            }
        }
    }

    override fun swipeWay(way: Int) {
        declineWay = way
        binding.dismissWay.text = way.dismissText
    }


    override fun onBackL(onBackCheck: (Boolean) -> Unit) {
        if (childFragmentManager.backStackEntryCount != 0) {
            binding.modeDone.goneFade(250L)
            childFragmentManager.popBackStack()
        } else {
            onBackCheck(setClicked)
        }
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        binding.apply {
            alarmEdit.myActMargin(ctx.actionBarHeight)
            setAlarmCard.cardAsView(ctx.actionBarHeight)
            subMainAlarm.orientation = newConfig.linConDirection
            alarmMode.apply {
                editAppearance()
                setTextColor(them.findAttr(android.R.attr.textColorPrimary))
            }
            modeDone.apply {
                editAppearance()
                setTextColor(them.findAttr(android.R.attr.textColorPrimary))
            }
        }
    }

    override fun onDestroyView() {
        if (mediaPlayer != null) {
            catchy(Unit) {
                recordCall?.let { mediaPlayer?.removeListener(it) }
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                }
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }
        recordCall = null
        resultFloating?.unregister()
        resultRing?.unregister()
        resultBack?.unregister()
        resultFloating = null
        resultRing = null
        resultBack = null
        stubPicker = null
        ala = null
        fetchHandlerNative = null
        title = null
        null.recordListener = null
        null.myOnClickListener = null
        super.onDestroyView()
    }

}
