package com.pt.common.global

import com.pt.common.stable.useBack

inline val Any?.toStr: String get() = toString()

inline val String.spaceLessCall: String
    get() = replace(" ", "").let { new ->
        if (new.contains("+")) new.replace("+", "").removeRange(0, 2) else new
    }

inline val android.content.Context.fetchClipboardText: android.text.Editable?
    get() {
        return com.pt.common.stable.catchy(null) {
            fetchSystemService(
                clipService
            )?.primaryClip?.getItemAt(0)?.text?.let { phone ->
                if (phone.trim() != "" && phone.length > 10) {
                    if (android.util.Patterns.PHONE.matcher(phone).matches()) {
                        phone.toString().toEditable
                    } else null
                } else null
            }
        }
    }

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun readTextFile(path: String, b: String?.() -> Unit) {
    com.pt.common.stable.withBackDef(null) {
        java.io.FileReader(path).useBack {
            readText()
        }
    }.let(b)
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun java.io.FileDescriptor.readTextFileOpen(b: String?.() -> Unit) {
    com.pt.common.stable.withBackDef(null) {
        java.io.FileReader(this@readTextFileOpen).useBack {
            readText()
        }
    }.let(b)
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun writeTextFile(path: String, str: String, b: (Boolean) -> Unit) {
    com.pt.common.stable.withBackDef(false) {
        java.io.FileWriter(path).useBack {
            write(str)
        }
        true
    }.let(b)
}

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun java.io.FileDescriptor.writeTextFileOpen(str: String, b: (Boolean) -> Unit) {
    com.pt.common.stable.withBackDef(false) {
        java.io.FileWriter(this@writeTextFileOpen).useBack {
            write(str)
        }
        true
    }.let(b)
}

inline val Long.fetchCalender: CalendarLate
    @com.pt.common.global.WorkerAnn
    get() {
        return CalendarLate.getInstance().apply {
            timeInMillis = this@fetchCalender
        }
    }

inline val Long.fetchCalenderDay: Int
    @com.pt.common.global.WorkerAnn
    get() {
        return CalendarLate.getInstance().apply {
            timeInMillis = this@fetchCalenderDay
        }[CalendarLate.DAY_OF_MONTH]
    }

inline val Long.timeInDay: DSackT<Long, Long>
    get() {
        return CalendarLate.getInstance().let { one ->
            one.timeInMillis = this@timeInDay
            one[CalendarLate.HOUR_OF_DAY] = 0
            one[CalendarLate.MINUTE] = 0
            one[CalendarLate.SECOND] = 0
            CalendarLate.getInstance().let { two ->
                two.timeInMillis = this@timeInDay
                two[CalendarLate.HOUR_OF_DAY] = 23
                two[CalendarLate.MINUTE] = 59
                two[CalendarLate.SECOND] = 59
                DSackT(one.timeInMillis, two.timeInMillis)
            }
        }
    }

inline val String?.toUri: android.net.Uri
    @com.pt.common.global.WorkerAnn
    get() = android.net.Uri.parse(this@toUri.toString())

inline val String?.toUriNull: android.net.Uri?
    @com.pt.common.global.WorkerAnn
    get() = this@toUriNull?.let { android.net.Uri.parse(it) }

inline val Long.fetchCalenderAlarm: Long
    @com.pt.common.global.WorkerAnn
    get() {
        return if (System.currentTimeMillis() > this@fetchCalenderAlarm) {
            CalendarLate.getInstance().run {
                timeInMillis = this@fetchCalenderAlarm
                CalendarLate.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                }.also {
                    it[CalendarLate.HOUR_OF_DAY] = this@run[CalendarLate.HOUR_OF_DAY]
                    it[CalendarLate.MINUTE] = this@run[CalendarLate.MINUTE]
                    it[CalendarLate.SECOND] = this@run[CalendarLate.SECOND]
                }.timeInMillis
            }
        } else {
            this@fetchCalenderAlarm
        }
    }


inline val android.content.Context.is24Hour: Boolean
    @com.pt.common.global.WorkerAnn
    get() = android.text.format.DateFormat.is24HourFormat(this)

inline val Long.findFileDate: String
    @com.pt.common.global.WorkerAnn
    get() {
        return java.util.Date(this).run {
            return@run android.text.format.DateFormat.format(
                com.pt.common.BuildConfig.TIME_FORMAT_ALL,
                this
            ).toString().let { one ->
                return@let android.text.format.DateFormat.format(
                    com.pt.common.BuildConfig.TIME_FORMAT_24,
                    this
                ).toString().let { two ->
                    (com.pt.common.BuildConfig.TEXT_JUST_BIG_BE + one +
                            com.pt.common.BuildConfig.TEXT_JUST_BIG_AF + " " +
                            com.pt.common.BuildConfig.TEXT_SMALL_BEFORE +
                            two + com.pt.common.BuildConfig.TEXT_SMALL_AFTER)
                }
            }
        }.let {
            androidx.core.text.HtmlCompat.fromHtml(
                it,
                androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
            ).toString()
        }
    }


inline val Long.findPicTitle: android.text.Spanned
    @com.pt.common.global.WorkerAnn
    get() {
        return java.util.Date(this@findPicTitle * 1000).run {
            return@run android.text.format.DateFormat.format(
                com.pt.common.BuildConfig.TIME_DAY,
                this
            ).toString().let { one ->
                return@let android.text.format.DateFormat.format(
                    com.pt.common.BuildConfig.TIME_FORMAT_24,
                    this
                ).toString().let { two ->
                    (com.pt.common.BuildConfig.TEXT_JUST_BIG_BE +
                            one + com.pt.common.BuildConfig.TEXT_JUST_BIG_AF +
                            com.pt.common.BuildConfig.TEXT_SMALL_BEFORE +
                            two + com.pt.common.BuildConfig.TEXT_SMALL_AFTER)
                }
            }
        }.let {
            androidx.core.text.HtmlCompat.fromHtml(
                it,
                androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

inline val Long.findMediaDuration: String
    @com.pt.common.global.WorkerAnn
    get() {
        return CalendarLate.getInstance(java.util.TimeZone.getTimeZone("UTC")).apply {
            timeInMillis = this@findMediaDuration
        }.run {
            if (this@findMediaDuration < 3599999) {
                return@run getVideoTime
            } else {
                return@run getMovieTime
            }
        }
    }

inline val CalendarLate.getVideoTime: String
    @com.pt.common.global.WorkerAnn
    get() {
        return if (this[CalendarLate.MINUTE] >= 10) {
            this[CalendarLate.MINUTE].toString()
        } else {
            "0" + this[CalendarLate.MINUTE]
        }.let { itM ->
            if (this[CalendarLate.SECOND] >= 10) {
                this[CalendarLate.SECOND].toString()
            } else {
                "0" + this[CalendarLate.SECOND]
            }.let { itS ->
                "$itM:$itS"
            }
        }
    }

inline val CalendarLate.getMovieTime: String
    @com.pt.common.global.WorkerAnn
    get() {
        return if (this[CalendarLate.HOUR] >= 10) {
            this[CalendarLate.HOUR].toString()
        } else {
            "0" + this[CalendarLate.HOUR]
        }.let { itY ->
            if (this[CalendarLate.MINUTE] >= 10) {
                this[CalendarLate.MINUTE].toString()
            } else {
                "0" + this[CalendarLate.MINUTE]
            }.let { itM ->
                if (this[CalendarLate.SECOND] >= 10) {
                    this[CalendarLate.SECOND].toString()
                } else {
                    "0" + this[CalendarLate.SECOND]
                }.let { itS ->
                    "$itY:$itM:$itS"
                }
            }
        }
    }

inline val String.toHtmlText: android.text.Spanned
    @com.pt.common.global.WorkerAnn
    get() {
        return androidx.core.text.HtmlCompat.fromHtml(
            this,
            androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

inline val String.toEditable: android.text.Editable?
    @com.pt.common.global.WorkerAnn
    get() = android.text.Editable.Factory.getInstance().newEditable(this)

inline val CharSequence.toEditableChar: android.text.Editable?
    @com.pt.common.global.WorkerAnn
    get() = android.text.Editable.Factory.getInstance().newEditable(this)

inline val android.text.Spannable.findLinkTitleUri: String?
    @com.pt.common.global.WorkerAnn
    get() {
        return when {
            androidx.core.text.util.LinkifyCompat.addLinks(
                this,
                android.text.util.Linkify.WEB_URLS
            ) -> {
                this.toString()
            }
            androidx.core.text.util.LinkifyCompat.addLinks(
                this,
                android.text.util.Linkify.EMAIL_ADDRESSES
            ) -> {
                null
            }
            androidx.core.text.util.LinkifyCompat.addLinks(
                this,
                android.text.util.Linkify.PHONE_NUMBERS
            ) -> {
                null
            }
            else -> {
                val ttt = this.toString()
                "https://www.google.com/search?q=$ttt&oq=$ttt&ie=UTF-8"
            }
        }
    }


inline val android.text.Spannable.checkLinkValidation: Boolean
    @com.pt.common.global.WorkerAnn
    get() {
        return androidx.core.text.util.LinkifyCompat.addLinks(
            this,
            android.text.util.Linkify.PHONE_NUMBERS
        ) or
                androidx.core.text.util.LinkifyCompat.addLinks(
                    this,
                    android.text.util.Linkify.EMAIL_ADDRESSES
                ) or
                androidx.core.text.util.LinkifyCompat.addLinks(
                    this,
                    android.text.util.Linkify.PHONE_NUMBERS
                )
    }

inline val Long.fetchRemainingTime: String
    @com.pt.common.global.WorkerAnn
    get() {
        return System.currentTimeMillis().let { ct ->
            (this - ct).let { newTime ->
                (newTime / 3600000).toInt().let { hours ->
                    ((newTime - hours * 60 * 60 * 1000 + 60000) / 60000).toInt()
                }
            }.let { mm ->
                (this - ct).let { new ->
                    (new / 3600000).toInt()
                }.let { hh ->
                    if (mm == 60) {
                        (hh + 1)
                    } else {
                        hh
                    }.let { rh ->
                        if (rh < 10) {
                            "0$rh"
                        } else {
                            rh.toString()
                        }.let { rH ->
                            when {
                                mm == 60 -> "00"
                                mm < 10 -> "0$mm"
                                else -> mm.toString()
                            }.let { rm ->
                                "$rH:$rm"
                            }
                        }
                    }
                }
            }
        }
    }

inline val DSackT<Long, Long>.isSameDay: Boolean
    @com.pt.common.global.WorkerAnn
    get() {
        return CalendarLate.getInstance().also {
            it.timeInMillis = one
        }.let { cal1 ->
            CalendarLate.getInstance().also {
                it.timeInMillis = two
            }.let { cal2 ->
                cal1.get(CalendarLate.DAY_OF_YEAR) == cal2.get(CalendarLate.DAY_OF_YEAR) &&
                        cal1.get(CalendarLate.YEAR) == cal2.get(CalendarLate.YEAR)
            }
        }
    }


inline val String.toDataFormat: (Long) -> String
    @com.pt.common.global.WorkerAnn
    get() = {
        android.text.format.DateFormat.format(this@toDataFormat, it).toString()
    }


inline val Long.toDurationFormat: String
    @com.pt.common.global.WorkerAnn
    get() {
        return (this@toDurationFormat * 1000).fetchCalender.run {
            run {
                if (this@toDurationFormat > 3600) this[CalendarLate.HOUR_OF_DAY].toString() + "h" else ""
            }.let { h ->
                if (this@toDurationFormat > 60) h + " " + this[CalendarLate.MINUTE].toString() + "min" else this@toDurationFormat.toString() + "s"
            }
        }
    }

inline val Long.toDurationFormatCall: String
    @com.pt.common.global.WorkerAnn
    get() {
        return (this@toDurationFormatCall * 1000).fetchCalender.run {
            run {
                if (this@toDurationFormatCall > 3600) this[CalendarLate.HOUR_OF_DAY].toString() + ":" else ""
            }.let { h ->
                if (this@toDurationFormatCall > 60) h + this[CalendarLate.MINUTE].toString() + ":" else "00:"
            }.let { hm ->
                this[CalendarLate.SECOND].let { s ->
                    if (s > 9) hm + s else hm + "0" + s
                }
            }
        }
    }

inline val String.toDefString: (Int) -> String
    @com.pt.common.global.WorkerAnn
    get() = {
        com.pt.common.stable.catchy(this) {
            substring(0, kotlin.math.min(it, length))
        }
    }

inline val Long.reformatSize: (String, String) -> String
    @com.pt.common.global.WorkerAnn
    get() = { megaBText, gigaBText ->
        if (toString().length >= 10) {
            this@reformatSize * 0.000000953674 * 0.00097656
        } else {
            this@reformatSize * 0.000000953674
        }.run {
            java.text.DecimalFormat("##.##").format(this).toString()
        }.let {
            if (this.toString().length >= 10) "$it $gigaBText" else "$it $megaBText"
        }
    }

inline val DSackT<String, String>.reSizeText: android.text.Spanned
    @com.pt.common.global.WorkerAnn
    get() {
        return (com.pt.common.BuildConfig.TEXT_BIG_BOLD_BE + one +
                com.pt.common.BuildConfig.TEXT_BIG_AF_BOLD + two).let {
            return@let androidx.core.text.HtmlCompat.fromHtml(
                it,
                androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }

inline val DSackT<CalendarLate, Long>.getRemainingDays: (String, String) -> String
    @com.pt.common.global.WorkerAnn
    get() = { d, ds ->
        CalendarLate.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }.run {
            return@run if (two > timeInMillis) {
                (one[CalendarLate.DAY_OF_WEEK] - this[CalendarLate.DAY_OF_WEEK]).let {
                    (it + 7) % 7
                }
            } else {
                (one[CalendarLate.DAY_OF_WEEK] - this[CalendarLate.DAY_OF_WEEK]).let {
                    ((it - 1) + 7) % 7
                }
            }.let {
                return@let when (it) {
                    1 -> "$it $d "
                    0 -> ""
                    else -> "$it $ds "
                }
            }
        }
    }
