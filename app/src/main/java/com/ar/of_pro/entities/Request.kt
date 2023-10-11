package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

class Request(
    requestTitle: String?,
    requestBidAmount: Int?,
    categoryOcupation: String?,
    categoryService: String?,
    description: String?,
    state: String?,
    date: String?,
    maxCost: Int?,
    clientId: String?,
) : Parcelable {


    var requestTitle: String = ""
    var requestBidAmount: Int = 0 //no se pasa inicialmente
    var categoryOcupation: String = ""
    var categoryService: String = ""
    var description: String = ""
    var state: String = ""
    var date: String = ""
    var maxCost: Int = 0
    var clientId: String = ""
    var providerId: String = "" //no se pasa inicialmente

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
    )

    init {
        this.requestTitle = requestTitle!!
        this.requestBidAmount = requestBidAmount!!
        this.categoryOcupation = categoryOcupation!!
        this.categoryService = categoryService!!
        this.description = description!!
        this.state = state!!
        this.date = date!!
        this.maxCost = maxCost!!
        this.clientId = clientId!!

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

        const val IN_COURSE = "EN CURSO"
        const val PENDING = "PENDIENTE"
        const val FINISHED = "FINALIZADA"
    }

}