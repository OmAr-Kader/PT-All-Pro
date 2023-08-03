package com.pt.pro.gallery.objects

import android.os.Parcel
import android.os.Parcelable

data class SeekVideo(var videoName: String? = null, var seekTime: Long = 1) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoName)
        parcel.writeLong(seekTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SeekVideo> {
        override fun createFromParcel(parcel: Parcel): SeekVideo {
            return SeekVideo(parcel)
        }

        override fun newArray(size: Int): Array<SeekVideo?> {
            return arrayOfNulls(size)
        }
    }


}