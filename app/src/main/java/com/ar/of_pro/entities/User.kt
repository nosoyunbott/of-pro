package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

class User(name: String?, rating: Double?, id: String?) : Parcelable {

    var name: String = ""
    var rating: Double = 0.0
    var id: String = ""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString()
    )

    init {
        this.name = name!!
        this.rating = rating!!
        this.id = id!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(rating)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}