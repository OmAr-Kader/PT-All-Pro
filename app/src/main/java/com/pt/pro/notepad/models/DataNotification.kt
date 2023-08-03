package com.pt.pro.notepad.models

import android.os.Parcel
import android.os.Parcelable

data class DataNotification(
    val idData: Int,
    val dataText: String?,
    val timeNotify: Long,
    val recordPath: String?,
    val imageUrl: String?,
    var isDone: Boolean = false,
    var tableName: String? = null,
    var tableUserName: String? = null,
    var tableIndex: Int = 0,
    var textIndex: Int = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
        idData = parcel.readInt(),
        dataText = parcel.readString(),
        timeNotify = parcel.readLong(),
        recordPath = parcel.readString(),
        imageUrl = parcel.readString(),
        isDone = parcel.readByte() != 0.toByte(),
        tableName = parcel.readString(),
        tableUserName = parcel.readString(),
        tableIndex = parcel.readInt(),
        textIndex = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idData)
        parcel.writeString(dataText)
        parcel.writeLong(timeNotify)
        parcel.writeString(recordPath)
        parcel.writeString(imageUrl)
        parcel.writeByte(if (isDone) 1 else 0)
        parcel.writeString(tableName)
        parcel.writeString(tableUserName)
        parcel.writeInt(tableIndex)
        parcel.writeInt(textIndex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataNotification> {
        override fun createFromParcel(parcel: Parcel): DataNotification {
            return DataNotification(parcel)
        }

        override fun newArray(size: Int): Array<DataNotification?> {
            return arrayOfNulls(size)
        }
    }
}