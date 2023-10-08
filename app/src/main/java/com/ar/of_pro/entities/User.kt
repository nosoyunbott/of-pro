package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable

class User(name: String?, rating: Float?) : Parcelable {

    var name: String = ""
    var rating: Float = 0f

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readFloat()
    )

    init {
        this.name = name!!
        this.rating = rating!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeFloat(rating)
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