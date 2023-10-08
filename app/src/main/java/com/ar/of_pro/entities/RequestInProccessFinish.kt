package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

class RequestInProccessFinish() : Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RequestInProccessFinish> {
        override fun createFromParcel(parcel: Parcel): RequestInProccessFinish {
            return RequestInProccessFinish(parcel)
        }

        override fun newArray(size: Int): Array<RequestInProccessFinish?> {
            return arrayOfNulls(size)
        }
    }
}