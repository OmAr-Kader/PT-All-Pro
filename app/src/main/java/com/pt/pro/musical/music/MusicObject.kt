package com.pt.pro.musical.music

object MusicObject {

    @Volatile
    @JvmStatic
    var musicManagerListener: com.pt.pro.musical.interfaces.MusicOption? = null

    @Volatile
    @JvmStatic
    var musicList: MutableList<com.pt.common.global.MusicSack>? = null
        set(value) {
            value?.forEach {
                it.bitmap = null
            }
            field = value
        }


    @Volatile
    @JvmStatic
    var inDisplay: Boolean = false

    @Volatile
    @JvmStatic
    var activityListener: com.pt.pro.musical.interfaces.ActivityListener? = null

    @Volatile
    @JvmStatic
    var inProcess: Boolean = false

}