package com.ar.of_pro.entities

import android.os.Parcel
import android.os.Parcelable
import java.util.UUID

class User(
    name: String?,
    lastName: String?,
    address: String?,
    location: String?,
    mail: String?,
    phone: Int?,
    rating: Double?,
    ratingQuantity: Int?,
    userType: String?,
    bio: String?,
    imageUrl: String?
) : Parcelable {

    var name: String = ""
    var lastName: String = ""
    var address: String = ""
    var location: String = ""
    var mail: String = ""
    var phone: Int = 0
    var rating: Double = 0.0
    var ratingQuantity: Int = 0
    var userType: String = ""
    var bio: String = ""
    var imageUrl: String = ""

    constructor() : this(null, null, null, null, null, null, null, null, null, null, null)

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    init {
        this.name = name!!
        this.lastName = lastName!!
        this.address = address!!
        this.location = location!!
        this.mail = mail!!
        this.phone = phone!!
        this.rating = rating!!
        this.ratingQuantity = ratingQuantity!!
        this.userType = userType!!
        this.bio = bio!!
        this.imageUrl = imageUrl!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(lastName)
        parcel.writeString(address)
        parcel.writeString(location)
        parcel.writeString(mail)
        parcel.writeInt(phone)
        parcel.writeDouble(rating)
        parcel.writeInt(ratingQuantity)
        parcel.writeString(userType)
        parcel.writeString(bio)
        parcel.writeString(imageUrl)
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