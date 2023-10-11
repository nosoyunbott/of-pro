package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable


//DUDA - idProvider  y idRequest nunca tienen que ser NULOS. Como se maneja eso?
class Proposal(
    providerId: String?,
    requestId: String?,
    bid: Float?,
    commentary: String?,
) : Parcelable {


    var providerId: String = ""
    var requestId: String = "" //no se pasa inicialmente
    var bid: Float = 0f
    var commentary: String = ""
    var disabled: Boolean = false


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),


    )


    init {
        this.providerId = providerId!!
        this.requestId = requestId!!
        this.bid = bid!!
        this.commentary = commentary!!
        this.disabled = false

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(providerId)
        parcel.writeString(requestId)
        parcel.writeFloat(bid)
        parcel.writeString(commentary)
        parcel.writeByte(if (disabled) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Proposal> {
        override fun createFromParcel(parcel: Parcel): Proposal {
            return Proposal(parcel)
        }

        override fun newArray(size: Int): Array<Proposal?> {
            return arrayOfNulls(size)
        }
    }

    fun setDisabled(){
        //TODO Para desactivar proposals que son rechazadas
    };


}