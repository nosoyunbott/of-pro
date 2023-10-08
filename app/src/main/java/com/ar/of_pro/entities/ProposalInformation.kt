package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

class ProposalInformation(name: String?, bidAmount: Float?, calification: Float?, commentary: String?,
                          calificationQty: Int?, requestId: String?, providerId: String?): Parcelable {
    //TODO cambiar nombre de la clase porque nuestros usuarios estan todos bajo la entity User
    var name: String = ""
    var bidAmount: Float = 0f
    var calification: Float = 0f
    var commentary: String = ""
    var calificationQty: Int = 0
    var requestId: String = ""
    var providerId: String = ""


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    init {
        this.name = name!!
        this.bidAmount = bidAmount!!
        this.calification = calification!!
        this.commentary = commentary!!
        this.calificationQty = calificationQty!!
        this.requestId=requestId!!
        this.providerId=providerId!!
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeFloat(bidAmount)
        parcel.writeFloat(calification)
        parcel.writeString(commentary)
        parcel.writeInt(calificationQty)
        parcel.writeString(requestId)
        parcel.writeString(providerId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProposalInformation> {
        override fun createFromParcel(parcel: Parcel): ProposalInformation {
            return ProposalInformation(parcel)
        }

        override fun newArray(size: Int): Array<ProposalInformation?> {
            return arrayOfNulls(size)
        }
    }


}