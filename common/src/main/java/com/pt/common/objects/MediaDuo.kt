package com.pt.common.objects

import android.os.Parcel
import android.os.Parcelable
import com.pt.common.global.MediaSack
import com.pt.common.global.isV_T

data class MediaDuo(
    val path: String?,
    val folderName: String?,
    var numberOfPics: Int,
    val isFav: Boolean = false,
    var mediaHolder: MutableList<MediaSack>,
    var typeItem: Int = com.pt.common.stable.ITEM_NORMAL,
) : Parcelable {

    @Suppress("UNCHECKED_CAST")
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        (if (isV_T) {
            parcel.readArrayList(MediaSack::class.java.classLoader, MediaSack::class.java)
        } else {
            @Suppress("DEPRECATION")
            parcel.readArrayList(MediaSack::class.java.classLoader)
        } as? MutableList<MediaSack>?) ?: mutableListOf(),
        parcel.readInt()
    )

    fun addPics() {
        numberOfPics++
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(folderName)
        parcel.writeInt(numberOfPics)
        parcel.writeByte(if (isFav) 1 else 0)
        parcel.writeInt(typeItem)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaDuo> {
        override fun createFromParcel(parcel: Parcel): MediaDuo {
            return MediaDuo(parcel)
        }

        override fun newArray(size: Int): Array<MediaDuo?> {
            return arrayOfNulls(size)
        }
    }
}
