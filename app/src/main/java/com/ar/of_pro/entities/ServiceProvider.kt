package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

class ServiceProvider(name: String?, bidAmount: Float?, calification: Float?): Parcelable {
    //TODO cambiar nombre de la clase porque nuestros usuarios estan todos bajo la entity User
    var name: String = ""
    var bidAmount: Float = 0f
    var calification: Float = 0f

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat()
    )

    init {
        this.name = name!!
        this.bidAmount = bidAmount!!
        this.calification = calification!!
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeFloat(bidAmount)
        parcel.writeFloat(calification)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ServiceProvider> {
        override fun createFromParcel(parcel: Parcel): ServiceProvider {
            return ServiceProvider(parcel)
        }

        override fun newArray(size: Int): Array<ServiceProvider?> {
            return arrayOfNulls(size)
        }
    }


}