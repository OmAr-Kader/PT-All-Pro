package com.pt.pro.alarm.fasten

import com.pt.common.global.*

object AlarmFasten : com.pt.common.mutual.life.GlobalInflater() {
    @JvmStatic
    fun android.content.Context.inflaterAlarm(): FragmentAlarmFasten {
        val act = actionBarHeight
        val fasten: FragmentAlarmFasten
        frameLayoutParent root_@{
            framePara(com.pt.common.stable.MATCH) {}
            this@root_.linear alarmCard@{
                framePara(com.pt.common.stable.MATCH, act) {}
                val mainBack = this@alarmCard.resizeImage mainBack@{
                    linearPara(
                        com.pt.common.stable.WRAP, com.pt.common.stable.MATCH
                    ) {
                        findPixel(4).also {
                            topMargin = it
                            bottomMargin = it
                        }
                        findPixel(10).also {
                            setPadding(0, it, 0, it)
                        }
                    }
                    background = compactImageReturn(com.pt.pro.R.drawable.ripple_oval_card)
                    setImageDrawable(compactImageReturn(com.pt.pro.R.drawable.ic_back_home))
                }
                val titleAlarm = this@alarmCard.textViewer titleAlarm@{
                    androidx.appcompat.widget.LinearLayoutCompat.LayoutParams(
                        com.pt.common.stable.MATCH, com.pt.common.stable.WRAP
                    ).apply {
                        gravity = android.view.Gravity.START or android.view.Gravity.CENTER
                        marginStart = findPixel(3)
                    }.also(this@titleAlarm::setLayoutParams)
                    editAppearance()
                    maxLines = 1
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                    setText(com.pt.pro.R.string.tg)
                    setTextColor(textColorPrimary)
                }
                this@root_.frame alarmRecFrame@{
                    framePara(com.pt.common.stable.MATCH, com.pt.common.stable.MATCH) {
                        topMargin = act
                    }
                    this@alarmRecFrame.constraint alarmRoot@{
                        framePara(com.pt.common.stable.MATCH) {}
                        val recyclerAlarms = this@alarmRoot.recycler recyclerAlarms@{
                            constraintPara(com.pt.common.stable.MATCH, 0) {
                                topToTop = 0
                                startToStart = 0
                                endToEnd = 0
                                bottomToBottom = 0
                            }
                        }
                        this@alarmRoot.card addAlarmButton@{
                            constraintPara(findPixel(55)) {
                                endToEnd = 0
                                bottomToBottom = 0
                                marginEnd = findPixel(20)
                                bottomMargin = findPixel(15)
                            }
                            elevation = findPixel(4).toFloat()
                            foreground = compactImageReturn(com.pt.pro.R.drawable.ripple_rectangle)
                            radius = findPixel(20).toFloat()
                            setCardBackgroundColor(colorPrimary)
                            cardElevation = findPixel(4).toFloat()
                            maxCardElevation = findPixel(4).toFloat()
                            this@addAlarmButton.image {
                                framePara(
                                    com.pt.common.stable.MATCH, com.pt.common.stable.MATCH
                                ) {
                                    findPixel(15).also { setMargins(it, it, it, it) }

                                    setImageDrawable(compactImageReturn(com.pt.pro.R.drawable.ic_add))
                                    svgReColor(textColorPrimary)
                                }
                            }
                            fasten = FragmentAlarmFasten(
                                root_ = this@root_,
                                alarmCard = this@alarmCard,
                                mainBack = mainBack,
                                titleAlarm = titleAlarm,
                                alarmRecFrame = this@alarmRecFrame,
                                recyclerAlarms = recyclerAlarms,
                                addAlarmButton = this@addAlarmButton
                            )
                        }
                    }
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterSetAlarm(): SetAlarmFasten {
        val act: Int = actionBarHeight
        val txtSize: Float = getDimensions(com.pt.pro.R.dimen.txt, 15).toFloat()
        val txtColor: Int = rmoText
        val accent: Int = colorAccent
        val di: Int = getDimensions(com.pt.pro.R.dimen.dt, 12)
        val bim: Int = getDimensions(com.pt.pro.R.dimen.bim, 4)
        val fasten: SetAlarmFasten
        frameLayoutParent root_@{
            framePara(com.pt.common.stable.MATCH) {}
            val mainBack: com.pt.common.moderator.over.ResizeImageView
            val alarmMode: androidx.appcompat.widget.AppCompatTextView
            val modeDone: androidx.appcompat.widget.AppCompatTextView
            val setAlarmCard = this@root_.constraint setAlarmCard@{
                framePara(com.pt.common.stable.MATCH, act) {}
                compactImage(com.pt.pro.R.drawable.card_back) {
                    background = this@compactImage
                }
                backReColor(colorPrimary)
                mainBack = this@setAlarmCard.resizeImage {
                    id = com.pt.pro.R.id.lockBrowser
                    constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                        marginStart = dpToPx(5)
                        dpToPx(4).also { d4 ->
                            topMargin = d4
                            bottomMargin = d4
                        }
                        topToTop = 0
                        bottomToBottom = 0
                        startToStart = 0
                    }
                    dpToPx(10).also { setPadding(0, it, 0, it) }
                    compactImage(com.pt.pro.R.drawable.ripple_oval_card) {
                        background = this
                    }
                    compactImage(com.pt.pro.R.drawable.ic_back_arrow) {
                        setImageDrawable(this)
                    }
                }
                alarmMode = this@setAlarmCard.textViewer {
                    constraintPara(0, com.pt.common.stable.WRAP) {
                        bottomToBottom = 0
                        startToEnd = com.pt.pro.R.id.lockBrowser//mainBack
                        endToStart = com.pt.pro.R.id.showAlbums//modeDone
                        topToTop = 0
                        horizontalBias = 0F
                    }
                    singleNonScroll()
                    editAppearance()
                    text = resources.getString(com.pt.pro.R.string.rg)
                    setTextColor(textColorPrimary)
                }
                modeDone = this@setAlarmCard.textViewer {
                    id = com.pt.pro.R.id.showAlbums
                    constraintPara(com.pt.common.stable.WRAP, com.pt.common.stable.MATCH) {
                        marginStart = dpToPx(5)
                        bottomToBottom = 0
                        endToEnd = 0
                        topToTop = 0
                        horizontalBias = 0F
                    }
                    editAppearance()
                    compactImage(com.pt.pro.R.drawable.ripple_cur) {
                        background = this
                    }
                    gravity = android.view.Gravity.CENTER
                    dpToPx(8).also { setPadding(it, 0, it, 0) }
                    text = resources.getString(com.pt.pro.R.string.dn)
                    textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
                    setTextColor(textColorPrimary)
                    justGone()
                }
            }
            this@root_.frame alarmEdit@{
                id = com.pt.pro.R.id.globalFrame
                framePara(com.pt.common.stable.MATCH) {
                    topMargin = act
                }
                this@alarmEdit.linear subMainAlarm@{
                    framePara(com.pt.common.stable.MATCH) {}
                    isBaselineAligned = false
                    verticalLin()
                    val pickTime = this@subMainAlarm.frame {
                        linearPara(
                            com.pt.common.stable.MATCH,
                            com.pt.common.stable.MATCH,
                            1.1F
                        ) {}
                        isClickable = false
                        isFocusable = false
                    }
                    this@subMainAlarm.constraint constraint@{
                        linearPara(
                            com.pt.common.stable.MATCH,
                            com.pt.common.stable.MATCH,
                            0.9F
                        ) {
                            topMargin = dpToPx(3)
                        }
                        isBaselineAligned = false
                        val ringtonePickerButton = this@constraint.button {
                            constraintPara(0, com.pt.common.stable.WRAP) {
                                endToStart = com.pt.pro.R.id.listAllFrame//frameLayout3
                                startToStart = 0
                                topToTop = 0
                                horizontalBias = 0.05F
                                marginStart = di
                                marginEnd = di
                            }
                            compactImage(com.pt.pro.R.drawable.item_inside_bcl) {
                                background = this@compactImage
                            }
                            backReColor(accent)
                            maxLines = 1
                            dpToPx(2).also {
                                setPadding(it, 0, it, 0)
                            }
                            text = resources.getString(com.pt.pro.R.string.r9)
                            isAllCaps = false
                            setTextColor(txtColor)
                            setTextSize(
                                android.util.TypedValue.COMPLEX_UNIT_PX,
                                txtSize
                            )
                        }
                        this@constraint.frame {
                            id = com.pt.pro.R.id.listAllFrame
                            constraintPara(1, 1) {
                                endToEnd = 0
                                startToStart = 0
                                topToTop = 0
                                horizontalBias = 0.33F
                            }
                        }
                        this@constraint.frame {
                            id = com.pt.pro.R.id.listArtistFrame
                            constraintPara(1, 1) {
                                endToEnd = 0
                                startToStart = 0
                                topToTop = 0
                                horizontalBias = 0.66F
                            }
                        }
                        val buttonBackgroundPicker = this@constraint.button {
                            id = com.pt.pro.R.id.savePlaylist
                            constraintPara(0, com.pt.common.stable.WRAP) {
                                endToStart = com.pt.pro.R.id.listArtistFrame//frameLayout8
                                startToEnd = com.pt.pro.R.id.listAllFrame//frameLayout3
                                topToTop = 0
                                marginStart = di
                                marginEnd = di
                            }
                            compactImage(com.pt.pro.R.drawable.item_inside_bcl) {
                                background = this@compactImage
                            }
                            backReColor(accent)
                            maxLines = 1
                            dpToPx(2).also {
                                setPadding(it, 0, it, 0)
                            }
                            text = resources.getString(com.pt.pro.R.string.bg)
                            isAllCaps = false
                            setTextColor(txtColor)
                            setTextSize(
                                android.util.TypedValue.COMPLEX_UNIT_PX,
                                txtSize
                            )
                        }
                        val dismissWay = this@constraint.button {
                            constraintPara(0, com.pt.common.stable.WRAP) {
                                endToEnd = 0
                                startToEnd = com.pt.pro.R.id.listArtistFrame//frameLayout8
                                topToTop = 0
                                horizontalBias = 0.95F
                                marginStart = di
                                marginEnd = di
                            }
                            compactImage(com.pt.pro.R.drawable.item_inside_bcl) {
                                background = this@compactImage
                            }
                            backReColor(accent)
                            maxLines = 1
                            dpToPx(2).also {
                                setPadding(it, 0, it, 0)
                            }
                            text = resources.getString(com.pt.pro.R.string.wa)
                            isAllCaps = false
                            setTextColor(txtColor)
                            setTextSize(
                                android.util.TypedValue.COMPLEX_UNIT_PX,
                                txtSize
                            )
                        }
                        val repeatingCheck: androidx.appcompat.widget.AppCompatCheckBox
                        val vibrateCheck: androidx.appcompat.widget.AppCompatCheckBox
                        this@constraint.frame frame@{
                            id = com.pt.pro.R.id.galleryModeFrame
                            constraintPara(
                                com.pt.common.stable.MATCH,
                                com.pt.common.stable.WRAP
                            ) {
                                topMargin = dpToPx(15)
                                startToStart = 0
                                endToEnd = 0
                                topToBottom = com.pt.pro.R.id.savePlaylist
                            }
                            repeatingCheck = androidx.appcompat.widget.AppCompatCheckBox(
                                context
                            ).apply {
                                framePara(
                                    com.pt.common.stable.WRAP,
                                    com.pt.common.stable.WRAP
                                ) {
                                    marginStart = dpToPx(15)
                                }
                                buttonTintList = txtColor.toTintList
                                setTextColor(txtColor)
                                text = resources.getString(com.pt.pro.R.string.rp)
                                setTextSize(
                                    android.util.TypedValue.COMPLEX_UNIT_PX,
                                    txtSize
                                )
                                minHeight = dpToPx(48)
                            }.also(this@frame::addView)
                            vibrateCheck = androidx.appcompat.widget.AppCompatCheckBox(
                                context
                            ).apply {
                                framePara(
                                    com.pt.common.stable.WRAP,
                                    com.pt.common.stable.WRAP
                                ) {
                                    gravity = android.view.Gravity.END
                                    marginEnd = dpToPx(15)
                                }
                                buttonTintList = txtColor.toTintList
                                setTextColor(txtColor)
                                text = resources.getString(com.pt.pro.R.string.vb)
                                setTextSize(
                                    android.util.TypedValue.COMPLEX_UNIT_PX,
                                    txtSize
                                )
                                minHeight = dpToPx(48)
                            }.also(this@frame::addView)
                        }
                        val daysFrame = this@constraint.frame {
                            id = com.pt.pro.R.id.deletePlaylist
                            constraintPara(
                                com.pt.common.stable.MATCH,
                                com.pt.common.stable.WRAP
                            ) {
                                topMargin = bim
                                startToStart = 0
                                endToEnd = 0
                                topToBottom = com.pt.pro.R.id.galleryModeFrame
                            }
                            minHeight = resources.getDimension(
                                com.pt.pro.R.dimen.gt3
                            ).toInt()
                        }
                        val textLabel: androidx.appcompat.widget.AppCompatEditText
                        val playAlarmRecord: androidx.appcompat.widget.AppCompatImageView
                        val rle = resources.getDimensions(com.pt.pro.R.dimen.rle, 47)
                        val sendButtonAlarm = com.pt.pro.notepad.recorder.RecordButton(context).apply {
                            id = com.pt.pro.R.id.searchButton
                            constraintPara(
                                rle,
                            ) {
                                //topMargin = bim
                                startToStart = 0
                                endToEnd = 0
                                topToBottom = com.pt.pro.R.id.deletePlaylist
                                bottomToTop = com.pt.pro.R.id.returnMusic//setFrame
                                verticalBias = 0.65F
                                horizontalBias = 0.99F
                            }
                            clickableView()
                            scaleType = android.widget.ImageView.ScaleType.CENTER_INSIDE
                            background = compactImageReturn(com.pt.pro.R.drawable.record_data)
                            backReColor(rmoBackground)
                            compactImage(com.pt.pro.R.drawable.ic_mic_black) {
                                setImageDrawable(this@compactImage)
                            }
                            svgReColor(rmoGrey)
                        }.also(this@constraint::addView)
                        val textLayout = this@constraint.frame textLayout@{
                            constraintPara(
                                0,
                                0
                            ) {
                                //topMargin = bim
                                startToStart = 0
                                endToStart = com.pt.pro.R.id.searchButton
                                topToBottom = com.pt.pro.R.id.deletePlaylist
                                bottomToTop = com.pt.pro.R.id.returnMusic//setFrame
                                verticalBias = 0.32F
                                minHeight = rle
                                minimumHeight = rle
                                dpToPx(15).also {
                                    marginStart = it
                                    marginEnd = it
                                }
                            }
                            fitsSystemWindows = true
                            playAlarmRecord = this@textLayout.image {
                                framePara(rle) {
                                    gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.START
                                }
                                justGone()
                                compactImage(com.pt.pro.R.drawable.ic_play_circle) {
                                    setImageDrawable(this@compactImage)
                                }
                                svgReColor(rmoText)
                            }
                            textLabel = this@textLayout.editText {
                                framePara(
                                    com.pt.common.stable.MATCH,
                                    com.pt.common.stable.WRAP
                                ) {
                                    gravity = android.view.Gravity.CENTER_VERTICAL or android.view.Gravity.START
                                    marginStart = rle
                                }
                                hint = resources.getString(com.pt.pro.R.string.am)
                                setTextColor(txtColor)
                                setHintTextColor(rmoGrey)
                                setTextSize(
                                    android.util.TypedValue.COMPLEX_UNIT_PX,
                                    txtSize
                                )
                                minimumHeight = dpToPx(48)
                                inputType = android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                                        android.text.InputType.TYPE_CLASS_TEXT
                                if (isV_O) {
                                    importantForAutofill =
                                        android.view.View.IMPORTANT_FOR_AUTOFILL_NO
                                }
                            }
                        }
                        val setAlarmButton: androidx.appcompat.widget.AppCompatButton
                        val setFrame = this@constraint.frame setFrame@{
                            id = com.pt.pro.R.id.returnMusic
                            constraintPara(
                                com.pt.common.stable.MATCH,
                                com.pt.common.stable.WRAP,
                            ) {
                                topMargin = bim
                                startToStart = 0
                                endToEnd = 0
                                bottomToBottom = 0
                                bottomMargin = resources.getDimension(
                                    com.pt.pro.R.dimen.cmm
                                ).toInt()
                            }
                            setAlarmButton = this@setFrame.button {
                                framePara(
                                    resources.getDimension(com.pt.pro.R.dimen.srh).toInt(),
                                    com.pt.common.stable.WRAP,
                                ) {
                                    gravity = android.view.Gravity.CENTER
                                }
                                compactImage(com.pt.pro.R.drawable.item_empty_large) {
                                    background = this@compactImage
                                }
                                backReColor(colorPrimary)
                                elevation = dpToPx(6).toFloat()
                                text = resources.getString(com.pt.pro.R.string.sm)
                                isAllCaps = false
                                setTextColor(txtColor)
                                setTextSize(
                                    android.util.TypedValue.COMPLEX_UNIT_PX,
                                    txtSize
                                )
                            }
                        }
                        fasten = SetAlarmFasten(
                            root_ = this@root_,
                            setAlarmCard = setAlarmCard,
                            mainBack = mainBack,
                            alarmMode = alarmMode,
                            modeDone = modeDone,
                            alarmEdit = this@alarmEdit,
                            subMainAlarm = this@subMainAlarm,
                            pickTime = pickTime,
                            ringtonePickerButton = ringtonePickerButton,
                            buttonBackgroundPicker = buttonBackgroundPicker,
                            dismissWay = dismissWay,
                            repeatingCheck = repeatingCheck,
                            vibrateCheck = vibrateCheck,
                            daysFrame = daysFrame,
                            textLayout = textLayout,
                            textLabel = textLabel,
                            sendButtonAlarm = sendButtonAlarm,
                            playAlarmRecord = playAlarmRecord,
                            setFrame = setFrame,
                            setAlarmButton = setAlarmButton,
                        )
                    }
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterTime(isCircle: Boolean): StubTimePickFasten {
        val fasten: StubTimePickFasten
        frameLayoutParent root_@{
            framePara(com.pt.common.stable.MATCH) {}
            val timeAlarm = android.widget.TimePicker(
                com.pt.common.stable.catchy(context) {
                    androidx.appcompat.view.ContextThemeWrapper(
                        context, com.pt.pro.R.style.Theme_TimePicker
                    )
                }, null, com.pt.pro.R.style.Theme_TimePicker
            ).apply {
                framePara(com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.CENTER
                }
                justInvisible()
            }.also(this@root_::addView)
            val pickerCircle = com.sztorm.timepicker.TimePicker(
                context
            ).apply {
                framePara(com.pt.common.stable.MATCH) {
                    gravity = android.view.Gravity.CENTER
                }
                justGone()
                clockFaceColor = rmoBackHint
                isTrackTouchable = true
                pointerColor = colorPrimary
                textColor = rmoText
                trackColor = rmoGrey
                pointerRadius = dpToPx(25).toFloat()
                trackSize = dpToPx(15).toFloat()
            }.also(this@root_::addView)
            this@root_.linear lin@{
                framePara(com.pt.common.stable.WRAP) {
                    gravity = android.view.Gravity.BOTTOM or android.view.Gravity.END
                    bottomMargin = dpToPx(2)
                }
                isBaselineAligned = false
                horizontalLin()
                this@lin.textViewer {
                    linearPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F) {
                        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                    }
                    setTextColor(rmoText)
                    textSize(resources.getDimension(com.pt.pro.R.dimen.txt))
                    text = resources.getString(com.pt.pro.R.string.gis)
                }
                val switchAlarm = androidx.appcompat.widget.SwitchCompat(
                    com.pt.common.stable.catchy(context) {
                        androidx.appcompat.view.ContextThemeWrapper(
                            context, com.pt.pro.R.style.switchCompatStyle
                        )
                    },
                ).apply {
                    linearPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F) {
                        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                        resources.getDimension(com.pt.pro.R.dimen.cms).toInt().also {
                            marginStart = it
                            marginEnd = it
                        }
                    }
                    isChecked = isCircle
                    minimumHeight = dpToPx(48)
                    switchMinWidth = (resources.getDimension(
                        com.pt.pro.R.dimen.ted
                    ) * 3).toInt()
                    compactImage(com.pt.pro.R.drawable.double_toggle) {
                        trackDrawable = this@compactImage
                    }
                }.also(this@lin::addView)
                this@lin.textViewer {
                    linearPara(com.pt.common.stable.WRAP, com.pt.common.stable.WRAP, 0F) {
                        gravity = android.view.Gravity.START or android.view.Gravity.CENTER_VERTICAL
                    }
                    setTextColor(rmoText)
                    textSize(resources.getDimension(com.pt.pro.R.dimen.txt))
                    text = resources.getString(com.pt.pro.R.string.c)
                }
                fasten = StubTimePickFasten(
                    root_ = this@root_,
                    timeAlarm = timeAlarm,
                    pickerCircle = pickerCircle,
                    switchAlarm = switchAlarm,
                )
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.inflaterDay(): StubAlarmDayFasten {
        val fasten: StubAlarmDayFasten
        frameLayoutParent root_@{
            framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {
                topMargin = resources.getDimension(com.pt.pro.R.dimen.bim).toInt()
            }
            this@root_.constraint days@{
                framePara(com.pt.common.stable.MATCH, com.pt.common.stable.WRAP) {}
                justInvisible()
                resources.getDimension(com.pt.pro.R.dimen.gt3).toInt().also { dim ->
                    resources.getDimension(com.pt.pro.R.dimen.tle).also { tle ->
                        fetchColor(com.pt.pro.R.color.gry).also { gry ->
                            fetchColor(com.pt.pro.R.color.rdd).also { rdd ->
                                val mMon = dayTextView(
                                    dim,
                                    tle,
                                    gry,
                                    rdd,
                                    resources.getString(com.pt.pro.R.string.md),
                                    0.05F,
                                ).also(this@days::addView)
                                val mTues = dayTextView(
                                    dim,
                                    tle,
                                    gry,
                                    rdd,
                                    resources.getString(com.pt.pro.R.string.td),
                                    0.2F,
                                ).also(this@days::addView)
                                val mWed = dayTextView(
                                    dim,
                                    tle,
                                    gry,
                                    rdd,
                                    resources.getString(com.pt.pro.R.string.wd),
                                    0.35F,
                                ).also(this@days::addView)
                                val mThurs = dayTextView(
                                    dim,
                                    tle,
                                    gry,
                                    rdd,
                                    resources.getString(com.pt.pro.R.string.ts),
                                    0.5F,
                                ).also(this@days::addView)
                                val mFri = dayTextView(
                                    dim,
                                    tle,
                                    gry,
                                    rdd,
                                    resources.getString(com.pt.pro.R.string.fd),
                                    0.65F,
                                ).also(this@days::addView)
                                val mSat = dayTextView(
                                    dim,
                                    tle,
                                    gry,
                                    rdd,
                                    resources.getString(com.pt.pro.R.string.sy),
                                    0.8F,
                                ).also(this@days::addView)
                                val mSun = dayTextView(
                                    dim,
                                    tle,
                                    gry,
                                    rdd,
                                    resources.getString(com.pt.pro.R.string.su),
                                    0.95F,
                                ).also(this@days::addView)

                                fasten = StubAlarmDayFasten(
                                    root_ = this@root_, days = this@days,
                                    mMon = mMon,
                                    mTues = mTues,
                                    mWed = mWed,
                                    mThurs = mThurs,
                                    mFri = mFri,
                                    mSat = mSat,
                                    mSun = mSun,
                                )
                            }
                        }

                    }
                }
            }
        }
        return fasten
    }

    @JvmStatic
    fun android.content.Context.dayTextView(
        dim: Int,
        tle: Float,
        gry: Int,
        rdd: Int,
        str: String,
        horBase: Float
    ): com.pt.common.moderator.over.ScalelessTextView {
        return com.pt.common.moderator.over.ScalelessTextView(
            this@dayTextView
        ).apply {
            constraintPara(dim, dim) {
                bottomToBottom = 0
                topToTop = 0
                endToEnd = 0
                startToStart = 0
                horizontalBias = horBase
            }
            isAllCaps = true
            appearanceLikeButton()
            compactImage(com.pt.pro.R.drawable.circle) {
                background = this@compactImage
            }
            backReColor(rdd)
            gravity = android.view.Gravity.CENTER
            setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, tle)
            textAlignment = android.view.View.TEXT_ALIGNMENT_GRAVITY
            text = str
            setTextColor(gry)
            if (isV_M) {
                compactImage(com.pt.pro.R.drawable.ripple_oval) {
                    foreground = this@compactImage
                }
            } else return@apply
        }
    }

    @JvmStatic
    fun destroyFasten() {
        destroyGlobalFasten()
    }
}