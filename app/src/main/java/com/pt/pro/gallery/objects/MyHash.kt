package com.pt.pro.gallery.objects

internal object MyHash {

    @JvmStatic
    @Volatile
    var isLock: Boolean = false

    @JvmStatic
    @Volatile
    var favChange: Boolean = false

    @JvmStatic
    var REC_MEDIA_NATIVE: androidx.collection.ArrayMap<Int, MutableList<com.pt.common.global.MediaSack>>? = androidx.collection.arrayMapOf()

    @JvmStatic
    inline val REC_MEDIA: androidx.collection.ArrayMap<Int, MutableList<com.pt.common.global.MediaSack>>
        get() = REC_MEDIA_NATIVE ?: androidx.collection.arrayMapOf<Int, MutableList<com.pt.common.global.MediaSack>>().also {
            REC_MEDIA_NATIVE = it
        }

    @JvmStatic
    inline val HASH_MEDIA: MutableList<com.pt.common.global.MediaSack>
        get() = REC_MEDIA[PENDING_MEDIA_HASH] ?: mutableListOf<com.pt.common.global.MediaSack>().also {
            REC_MEDIA[PENDING_MEDIA_HASH] = it
        }

    const val PENDING_MEDIA_HASH: Int = 777

    fun com.pt.common.global.MediaSack.addPending(): Boolean {
        return kotlin.runCatching {
            if (!HASH_MEDIA.contains(this)) {
                HASH_MEDIA.add(this)
            }
            true
        }.getOrDefault(false)
    }

    fun MutableList<com.pt.common.global.MediaSack>.checkGalleryPending() {
        kotlin.runCatching {
            if (REC_MEDIA[PENDING_MEDIA_HASH] != null) {
                forEach {
                    HASH_MEDIA.apply {
                        if (this@apply.contains(it)) {
                            this@apply.remove(it)
                        }
                    }
                }
            }
        }.getOrDefault(false)
    }

}

