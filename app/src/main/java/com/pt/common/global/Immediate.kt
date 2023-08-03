package com.pt.common.global

inline val android.content.Context.findStringPrefNull: (String, String, String?) -> String?
    get() = { prefID, keyId, default ->
        getSharedPreferences(
            prefID,
            android.content.Context.MODE_PRIVATE
        ).getString(keyId, default)
    }

inline val android.content.Context.findStringPreference: suspend (String, String) -> String
    get() = { keyId, default ->
        findPrefString(keyId) {
            default
        }
    }

inline val android.content.Context.findStringPrefNullable: suspend (String) -> String?
    get() = { keyId ->
        findPrefStringNull(keyId)
    }

inline val android.content.Context.findIntegerPref: (prefID: String, String, Int) -> Int
    get() {
        return { prefID, keyId, default ->
            getSharedPreferences(
                prefID,
                android.content.Context.MODE_PRIVATE
            ).getInt(keyId, default)
        }
    }


inline val android.content.Context.findIntegerPrefDb: suspend (prefID: String, String, Int) -> Int
    get() = { prefID, keyId, default ->
        findPrefInt(keyId) {
            findIntegerPref(prefID, keyId, default)
        }
    }

inline val android.content.Context.findIntegerPreference: suspend (String, Int) -> Int
    get() = { keyId, default ->
        findPrefInt(keyId) {
            default
        }
    }

inline val android.content.Context.findLongPreference: suspend (String, Long) -> Long
    get() = { keyId, default ->
        findPrefLong(keyId) {
            default
        }
    }

inline val android.content.Context.findBooleanPref: (String, String, Boolean) -> Boolean
    get() = { prefID, keyId, default ->
        getSharedPreferences(
            prefID,
            android.content.Context.MODE_PRIVATE
        ).getBoolean(keyId, default)
    }

inline val android.content.Context.findBooleanPrefDb: suspend (String, String, Boolean) -> Boolean
    get() = { prefID, keyId, default ->
        findPrefBoolean(keyId) {
            findBooleanPref(prefID, keyId, default)
        }
    }

inline val android.content.Context.findBooleanPreference: suspend (String, Boolean) -> Boolean
    get() = { keyId, default ->
        findPrefBoolean(keyId) {
            default
        }
    }

inline val android.content.Context.findBooleanPreferenceNull: suspend (String) -> Boolean?
    get() = { keyId ->
        findPrefBooleanNull(keyId)
    }

inline val android.content.Context.nightRider: Boolean
    get() = com.pt.common.stable.catchy(false) {
        findBooleanPref(com.pt.common.stable.NIGHT, com.pt.common.stable.RIDERS, false)
    }

@get:androidx.annotation.StyleRes
inline val android.content.Context.fetchStyle: Int
    @com.pt.common.global.MainAnn
    get() = kotlin.run {
        findIntegerPref(
            com.pt.common.stable.NEW_STYLE,
            com.pt.common.stable.NEW_MY_THEM,
            -1
        ).run {
            if (this == -1) {
                fetchOldStyle
            } else {
                com.pt.pro.extra.utils.SettingHelper.styleId(this)
            }
        }
    }

@get:androidx.annotation.StyleRes
inline val android.content.Context.fetchOldStyle: Int
    @com.pt.common.global.MainAnn
    @android.annotation.SuppressLint("DiscouragedApi")
    get() {
        return findStringPrefNull(
            com.pt.common.stable.STYLE,
            com.pt.common.stable.MY_THEM,
            null
        ).run {
            if (this == null) {
                fetchFirstStyle
            } else {
                resources.getIdentifier(
                    this,
                    com.pt.common.BuildConfig.STYLE_ANDROID,
                    com.pt.pro.BuildConfig.APPLICATION_ID
                ).let {
                    if (it != 0) it else fetchFirstStyle
                }
            }
        }
    }

inline val android.content.Context.fetchFirstStyle: Int
    @androidx.annotation.StyleRes
    @com.pt.common.global.MainAnn
    get() {
        return if (resources.isDeviceDark)
            com.pt.pro.R.style.AIO_Night_And6
        else
            com.pt.pro.R.style.AIO_Light_And6
    }

inline val android.content.Context.isCarUiMode: Boolean
    get() {
        return com.pt.common.stable.catchy(false) {
            fetchSystemService(uiModeService)?.let {
                it.currentModeType.let { cm ->
                    cm == android.content.res.Configuration.UI_MODE_TYPE_CAR ||
                            cm == android.content.res.Configuration.UI_MODE_TYPE_TELEVISION ||
                            cm == android.content.res.Configuration.UI_MODE_TYPE_WATCH ||
                            cm == android.content.res.Configuration.UI_MODE_TYPE_APPLIANCE ||
                            cm == android.content.res.Configuration.UI_MODE_TYPE_DESK ||
                            (isV_O && cm == android.content.res.Configuration.UI_MODE_TYPE_VR_HEADSET)
                }
            } ?: false
        }
    }

inline val android.content.Context.fetchMediaContext: android.content.Context
    @com.pt.common.global.MainAnn
    get() {
        return if (isV_R) {
            return com.pt.common.stable.catchy(this) {
                return@catchy createDisplayContext(fetchDisplay).createWindowContext(
                    android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    null
                )
            }
        } else {
            this
        }
    }


@get:androidx.annotation.UiContext
inline val android.content.Context.fetchOverlayContext: android.content.Context
    @com.pt.common.global.MainAnn
    get() {
        return if (isV_R) {
            com.pt.common.stable.catchy(this@fetchOverlayContext) {
                createDisplayContext(fetchDisplay).createWindowContext(
                    android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    null
                )
            }.also {
                it.fetchStyle.apply(it::setTheme)
            }
        } else {
            this@fetchOverlayContext.also {
                it.fetchStyle.apply(it::setTheme)
            }
        }
    }

@com.pt.common.global.MainAnn
fun androidx.appcompat.widget.AppCompatImageView.statBarLine(stat: Int) {
    android.widget.FrameLayout.LayoutParams(
        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
        stat
    ).apply(::setLayoutParams)
}

@com.pt.common.global.MainAnn
fun android.view.View.cardAsView(act: Int) {
    android.widget.FrameLayout.LayoutParams(
        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
        act
    ).apply(::setLayoutParams)
}

@com.pt.common.global.MainAnn
fun android.widget.FrameLayout.myActMargin(act: Int) {
    android.widget.FrameLayout.LayoutParams(
        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
        android.widget.FrameLayout.LayoutParams.MATCH_PARENT
    ).apply {
        topMargin = act
        layoutParams = this
    }
}

@com.pt.common.global.MainAnn
fun androidx.constraintlayout.widget.ConstraintLayout.myActMarginCon(act: Int) {
    android.widget.FrameLayout.LayoutParams(
        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
        android.widget.FrameLayout.LayoutParams.MATCH_PARENT
    ).apply {
        topMargin = act
        layoutParams = this
    }
}

@com.pt.common.global.MainAnn
fun android.widget.FrameLayout.myStatMargin(stat: Int) {
    android.widget.FrameLayout.LayoutParams(
        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
        android.widget.FrameLayout.LayoutParams.MATCH_PARENT
    ).apply {
        topMargin = stat
        layoutParams = this
    }
}