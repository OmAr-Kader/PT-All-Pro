package com.pt.common.moderator.over

import android.media.MediaMetadataRetriever

open class MyMediaMetadataRetriever : MediaMetadataRetriever(), java.io.Closeable, AutoCloseable {

    override fun close() {
        release()
    }

}