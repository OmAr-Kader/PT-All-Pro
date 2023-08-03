package com.pt.common.objects

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.pt.common.global.isV_T
import com.pt.common.stable.DEFAULT_PLAYLIST
import com.pt.common.stable.IS_YOUR_PLAYLIST

data class MusicHolder(
    var title: String? = null,
    var artist: String? = null,
    var pathAudio: String? = null,
    var idArtAlb: Int = 0,
    var dur_NSongs: Long = 0L,
    var bitmap: Bitmap? = null,
    var album: String? = null,
    var songType: Int = IS_YOUR_PLAYLIST,
    var playlistSong: String? = DEFAULT_PLAYLIST,
    var musicUri: String? = null,
) : Parcelable {

    @Suppress("DEPRECATION")
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readLong(),
        if (isV_T) parcel.readParcelable(
            Bitmap::class.java.classLoader,
            Bitmap::class.java
        ) else parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(artist)
        parcel.writeString(pathAudio)
        parcel.writeInt(idArtAlb)
        parcel.writeLong(dur_NSongs)
        parcel.writeParcelable(bitmap, flags)
        parcel.writeString(album)
        parcel.writeInt(songType)
        parcel.writeString(playlistSong)
        parcel.writeString(musicUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicHolder> {
        override fun createFromParcel(parcel: Parcel): MusicHolder {
            return MusicHolder(parcel)
        }

        override fun newArray(size: Int): Array<MusicHolder?> {
            return arrayOfNulls(size)
        }
    }
}