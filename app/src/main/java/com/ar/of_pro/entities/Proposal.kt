package com.ar.of_pro.entities

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable


//DUDA - idProvider  y idRequest nunca tienen que ser NULOS. Como se maneja eso?
class Proposal(
    idProvider: String?,
    idRequest: String?,
    bid: Float?,
    commentary: String?,
    disabled: Boolean?,
) : Parcelable {


    var idProvider: String = ""
    var idRequest: String = "" //no se pasa inicialmente
    var bid: Float = 0f
    var commentary: String = ""
    var disabled: Boolean = false


    @SuppressLint("NewApi")
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readBoolean()

    )


    init {
        this.idProvider = idProvider!!
        this.idRequest = idRequest!!
        this.bid = bid!!
        this.commentary = commentary!!
        this.disabled = disabled!!

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idProvider)
        parcel.writeString(idRequest)
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

}