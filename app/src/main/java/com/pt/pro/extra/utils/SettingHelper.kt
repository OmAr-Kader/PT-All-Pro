package com.pt.pro.extra.utils

import android.content.Context
import com.pt.common.global.DSackT
import com.pt.common.global.fetchColor
import com.pt.pro.R.style
import com.pt.pro.R.color

object SettingHelper {

    const val FLOATING_HEAD: Int = 0

    const val NAVIGATION_HEAD: Int = 1

    const val SCREEN_SWIPER: Int = 2

    const val NOTE_HEAD: Int = 3

    const val MAX_HEAD: Int = 3

    inline val newScreenshot: String
        get() = "IMG" + System.currentTimeMillis() + "_screenShoot.jpeg"

    inline val String.newVideoRecord: String
        get() = this + "/" + "VID" + System.currentTimeMillis() + "_screenRecord.mp4"

    fun Context.getDataListColors(): MutableList<Int> = run {
        mutableListOf<Int>().apply {
            add(fetchColor(color.cpd))
            add(fetchColor(color.blc))
            add(fetchColor(color.skp))
            add(fetchColor(color.an6))
            add(fetchColor(color.an7))
            add(fetchColor(color.an8))
            add(fetchColor(color.an9))
            add(fetchColor(color.an10))
            add(fetchColor(color.an11d))
            add(fetchColor(color.an11))
            add(fetchColor(color.an12d))
            add(fetchColor(color.an12))
            add(fetchColor(color.an13d))
            add(fetchColor(color.an13))
            add(fetchColor(color.an14d))
            add(fetchColor(color.an14))
            add(fetchColor(color.an15d))
            add(fetchColor(color.an15))
            add(fetchColor(color.prd))
            add(fetchColor(color.pdr))
            add(fetchColor(color.an1))
            add(fetchColor(color.an2))
            add(fetchColor(color.an4))
            add(fetchColor(color.an5))
            add(fetchColor(color.ppu))
            add(fetchColor(color.pca))
            add(fetchColor(color.pnk))
            add(fetchColor(color.pik))
            add(fetchColor(color.ygp))
            add(fetchColor(color.yed))
            add(fetchColor(color.orp))
            add(fetchColor(color.ogd))
            add(fetchColor(color.rop))
            add(fetchColor(color.rod))
            add(fetchColor(color.bwp))
            add(fetchColor(color.pdb))
            add(fetchColor(color.ddo))
            add(fetchColor(color.pdd))
            add(fetchColor(color.wli))
        }
    }

    fun Context.getStylesListLight(): MutableList<DSackT<Int, Int>> = run {
        mutableListOf<DSackT<Int, Int>>().apply {
            add(DSackT(fetchColor(color.cpd), style.AIO_Light_Blue))
            add(DSackT(fetchColor(color.skp), style.AIO_Light_Sky))
            add(DSackT(fetchColor(color.an6), style.AIO_Light_And6))
            add(DSackT(fetchColor(color.an7), style.AIO_Light_And7))
            add(DSackT(fetchColor(color.an8), style.AIO_Light_And8))
            add(DSackT(fetchColor(color.an9), style.AIO_Light_And9))
            add(DSackT(fetchColor(color.an10d), style.AIO_Light_And10))
            add(DSackT(fetchColor(color.an11d), style.AIO_Light_And11))
            add(DSackT(fetchColor(color.an12d), style.AIO_Light_And12))
            add(DSackT(fetchColor(color.an13d), style.AIO_Light_And13))
            add(DSackT(fetchColor(color.an14d), style.AIO_Light_And14))
            add(DSackT(fetchColor(color.an15d), style.AIO_Light_And15))
            add(DSackT(fetchColor(color.prd), style.AIO_Light_Red))
            add(DSackT(fetchColor(color.an1), style.AIO_Light_And1))
            add(DSackT(fetchColor(color.an2), style.AIO_Light_And2))
            add(DSackT(fetchColor(color.an4), style.AIO_Light_And4))
            add(DSackT(fetchColor(color.an5), style.AIO_Light_And5))
            add(DSackT(fetchColor(color.ppu), style.AIO_Light_Purple))
            add(DSackT(fetchColor(color.pnk), style.AIO_Light_Pink))
            add(DSackT(fetchColor(color.ygp), style.AIO_Light_Yellow))
            add(DSackT(fetchColor(color.orp), style.AIO_Light_Orange))
            add(DSackT(fetchColor(color.rop), style.AIO_Light_Rose))
            add(DSackT(fetchColor(color.bwp), style.AIO_Light_Brown))
            add(DSackT(fetchColor(color.ddo), style.AIO_Light_Dodger))
            add(DSackT(fetchColor(color.wli), style.AIO_Light_Lime))
        }
    }

    fun Context.getStylesListNight(): MutableList<DSackT<Int, Int>> = run {
        mutableListOf<DSackT<Int, Int>>().apply {
            add(DSackT(fetchColor(color.blc), style.AIO_Night_Blue))
            add(DSackT(fetchColor(color.csa), style.AIO_Night_Sky))
            add(DSackT(fetchColor(color.an6), style.AIO_Night_And6))
            add(DSackT(fetchColor(color.an7), style.AIO_Night_And7))
            add(DSackT(fetchColor(color.an8), style.AIO_Night_And8))
            add(DSackT(fetchColor(color.an9), style.AIO_Night_And9))
            add(DSackT(fetchColor(color.an10), style.AIO_Night_And10))
            add(DSackT(fetchColor(color.an11), style.AIO_Night_And11))
            add(DSackT(fetchColor(color.an12), style.AIO_Night_And12))
            add(DSackT(fetchColor(color.an13), style.AIO_Night_And13))
            add(DSackT(fetchColor(color.an14), style.AIO_Night_And14))
            add(DSackT(fetchColor(color.an15), style.AIO_Night_And15))
            add(DSackT(fetchColor(color.pdr), style.AIO_Night_Red))
            add(DSackT(fetchColor(color.an1), style.AIO_Night_And1))
            add(DSackT(fetchColor(color.an2), style.AIO_Night_And2))
            add(DSackT(fetchColor(color.an4), style.AIO_Night_And4))
            add(DSackT(fetchColor(color.an5), style.AIO_Night_And5))
            add(DSackT(fetchColor(color.pca), style.AIO_Night_Purple))
            add(DSackT(fetchColor(color.pik), style.AIO_Night_Pink))
            add(DSackT(fetchColor(color.yed), style.AIO_Night_Yellow))
            add(DSackT(fetchColor(color.ogd), style.AIO_Night_Orange))
            add(DSackT(fetchColor(color.rod), style.AIO_Night_Rose))
            add(DSackT(fetchColor(color.pdb), style.AIO_Night_Brown))
            add(DSackT(fetchColor(color.pdd), style.AIO_Night_Dodger))
            add(DSackT(fetchColor(color.lid), style.AIO_Night_Lime))
        }
    }

    @get:androidx.annotation.StyleRes
    inline val styleId: (Int) -> Int
        get() = {
            when (it) {
                0 -> style.AIO_Light_Blue
                1 -> style.AIO_Light_Sky
                2 -> style.AIO_Light_And6
                3 -> style.AIO_Light_And7
                4 -> style.AIO_Light_And8
                5 -> style.AIO_Light_And9
                6 -> style.AIO_Light_And10
                7 -> style.AIO_Light_And11
                8 -> style.AIO_Light_And12
                9 -> style.AIO_Light_And13
                10 -> style.AIO_Light_And14
                11 -> style.AIO_Light_And15
                12 -> style.AIO_Light_Red
                13 -> style.AIO_Light_And1
                14 -> style.AIO_Light_And2
                15 -> style.AIO_Light_And4
                16 -> style.AIO_Light_And5
                17 -> style.AIO_Light_Purple
                18 -> style.AIO_Light_Pink
                19 -> style.AIO_Light_Yellow
                20 -> style.AIO_Light_Orange
                21 -> style.AIO_Light_Rose
                22 -> style.AIO_Light_Brown
                23 -> style.AIO_Light_Dodger
                24 -> style.AIO_Light_Lime
                25 -> style.AIO_Night_Blue
                26 -> style.AIO_Night_Red
                27 -> style.AIO_Night_Purple
                28 -> style.AIO_Night_Pink
                29 -> style.AIO_Night_Sky
                30 -> style.AIO_Night_Yellow
                31 -> style.AIO_Night_Orange
                32 -> style.AIO_Night_Rose
                33 -> style.AIO_Night_Brown
                34 -> style.AIO_Night_Dodger
                35 -> style.AIO_Night_Lime
                36 -> style.AIO_Night_And1
                37 -> style.AIO_Night_And2
                38 -> style.AIO_Night_And4
                39 -> style.AIO_Night_And5
                40 -> style.AIO_Night_And6
                41 -> style.AIO_Night_And7
                42 -> style.AIO_Night_And8
                43 -> style.AIO_Night_And9
                44 -> style.AIO_Night_And10
                45 -> style.AIO_Night_And11
                46 -> style.AIO_Night_And12
                47 -> style.AIO_Night_And13
                48 -> style.AIO_Night_And14
                49 -> style.AIO_Night_And15
                else -> style.AIO_Light_Blue
            }
        }

    inline val fromStyleToNumber: (Int) -> Int
        get() = {
            when (it) {
                style.AIO_Light_Blue -> 0
                style.AIO_Light_Sky -> 1
                style.AIO_Light_And6 -> 2
                style.AIO_Light_And7 -> 3
                style.AIO_Light_And8 -> 4
                style.AIO_Light_And9 -> 5
                style.AIO_Light_And10 -> 6
                style.AIO_Light_And11 -> 7
                style.AIO_Light_And12 -> 8
                style.AIO_Light_And13 -> 9
                style.AIO_Light_And14 -> 10
                style.AIO_Light_And15 -> 11
                style.AIO_Light_Red -> 12
                style.AIO_Light_And1 -> 13
                style.AIO_Light_And2 -> 14
                style.AIO_Light_And4 -> 15
                style.AIO_Light_And5 -> 16
                style.AIO_Light_Purple -> 17
                style.AIO_Light_Pink -> 18
                style.AIO_Light_Yellow -> 19
                style.AIO_Light_Orange -> 20
                style.AIO_Light_Rose -> 21
                style.AIO_Light_Brown -> 22
                style.AIO_Light_Dodger -> 23
                style.AIO_Light_Lime -> 24
                style.AIO_Night_Blue -> 25
                style.AIO_Night_Red -> 26
                style.AIO_Night_Purple -> 27
                style.AIO_Night_Pink -> 28
                style.AIO_Night_Sky -> 29
                style.AIO_Night_Yellow -> 30
                style.AIO_Night_Orange -> 31
                style.AIO_Night_Rose -> 32
                style.AIO_Night_Brown -> 33
                style.AIO_Night_Dodger -> 34
                style.AIO_Night_Lime -> 35
                style.AIO_Night_And1 -> 36
                style.AIO_Night_And2 -> 37
                style.AIO_Night_And4 -> 38
                style.AIO_Night_And5 -> 39
                style.AIO_Night_And6 -> 40
                style.AIO_Night_And7 -> 41
                style.AIO_Night_And8 -> 42
                style.AIO_Night_And9 -> 43
                style.AIO_Night_And10 -> 44
                style.AIO_Night_And11 -> 45
                style.AIO_Night_And12 -> 46
                style.AIO_Night_And13 -> 47
                style.AIO_Night_And14 -> 48
                style.AIO_Night_And15 -> 49
                else -> 0
            }
        }


    @Suppress("unused", "HardCodedStringLiteral")
    fun tempTemp() {
        color.ecw
        style.AppTheme_video_Light
        com.pt.pro.R.dimen.cds
        com.pt.pro.R.dimen.background_fixed_large
        com.pt.pro.R.string.hu
        com.pt.pro.R.string.app_name
        /*
        lintOptions {
            ignore "QueryPermissionsNeeded"
            checkDependencies true
            checkReleaseBuilds false
            ignoreTestSources true
            disable 'TypographyFractions','TypographyQuotes'
            enable 'RtlHardcoded', 'RtlCompat', 'RtlEnabled'
            quiet true
            abortOnError false
            ignoreWarnings true
            checkOnly 'NewApi', 'InlinedApi', 'HandlerLeak'
        }
        signingConfig playStoreConfig //Add your own signing config
        //merge
        */

    }

}