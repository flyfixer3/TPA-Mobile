package com.tpa.questapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var pictProfile: String? = "",
    var fullname: String? = "",
    val major: String?="",
    var location: String? = "",
    var gender: String? = "",
    var job: String? = "",
    var userId: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pictProfile)
        parcel.writeString(fullname)
        parcel.writeString(major)
        parcel.writeString(location)
        parcel.writeString(gender)
        parcel.writeString(job)
        parcel.writeString(userId)
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




