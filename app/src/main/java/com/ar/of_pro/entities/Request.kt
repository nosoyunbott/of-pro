package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

class Request(requestTitle: String?, requestBidsAmount: Int?) : Parcelable {


     var requestTitle: String = ""
     var requestBidsAmount: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
    )

    init {
        this.requestTitle = requestTitle!!
        this.requestBidsAmount = requestBidsAmount!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Request> {
        override fun createFromParcel(parcel: Parcel): Request {
            return Request(parcel)
        }

        override fun newArray(size: Int): Array<Request?> {
            return arrayOfNulls(size)
        }
    }

}