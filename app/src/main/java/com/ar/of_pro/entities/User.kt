package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

class User(name: String?, rating: Double?) : Parcelable {

    var name: String = ""
    var rating: Double = 0.0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble()
    )

    init {
        this.name = name!!
        this.rating = rating!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(rating)
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