package com.pt.common.objects

import android.os.Parcel
import android.os.Parcelable

data class MediaHolder(
    val nameMedia: String?,
    @Volatile
    var pathMedia: String?,
    val uriMedia: String?,
    val isImage: Boolean,
    val mediaSize: Long,
    val mediaWidth: Int,
    val mediaHigh: Int,
    val dateModified: Long,
    @Volatile
    var isSelect: Boolean = false,
    @Volatile
    var typeItem: Int = com.pt.common.stable.ITEM_NORMAL,
    @Volatile
    var isFromFav: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        nameMedia = parcel.readString(),
        pathMedia = parcel.readString(),
        uriMedia = parcel.readString(),
        isImage = parcel.readByte().toInt() != 0,
        mediaSize = parcel.readLong(),
        mediaWidth = parcel.readInt(),
        mediaHigh = parcel.readInt(),
        dateModified = parcel.readLong(),
        isSelect = parcel.readByte().toInt() != 0,
        typeItem = parcel.readInt(),
        isFromFav = parcel.readByte().toInt() != 0,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nameMedia)
        parcel.writeString(pathMedia)
        parcel.writeString(uriMedia)
        parcel.writeByte((if (isImage) 1 else 0).toByte())
        parcel.writeLong(mediaSize)
        parcel.writeInt(mediaWidth)
        parcel.writeInt(mediaHigh)
        parcel.writeLong(dateModified)
        parcel.writeByte((if (isSelect) 1 else 0).toByte())
        parcel.writeInt(typeItem)
        parcel.writeByte((if (isFromFav) 1 else 0).toByte())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaHolder> {
        override fun createFromParcel(parcel: Parcel): MediaHolder {
            return MediaHolder(parcel)
        }

        override fun newArray(size: Int): Array<MediaHolder?> {
            return arrayOfNulls(size)
        }
    }
}