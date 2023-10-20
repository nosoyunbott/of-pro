package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

data class RequestHistory(
    val request: Request?,
    val clientName: String,
    val providerName: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Request::class.java.classLoader),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(request, flags)
        parcel.writeString(clientName)
        parcel.writeString(providerName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RequestHistory> {
        override fun createFromParcel(parcel: Parcel): RequestHistory {
            return RequestHistory(parcel)
        }

        override fun newArray(size: Int): Array<RequestHistory?> {
            return arrayOfNulls(size)
        }
    }
}
