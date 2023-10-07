package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

class Request(requestTitle: String?, requestBidsAmount: Int?, category: String?) : Parcelable {


     var requestTitle: String = ""
     var requestDate:String="02-10-2023"
     var  workerName:String="Hector Medina"
     var requestBidsAmount: Int = 0
     var category: String =""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    )

    init {
        this.requestTitle = requestTitle!!
        this.requestBidsAmount = requestBidsAmount!!
        this.category=category!!
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