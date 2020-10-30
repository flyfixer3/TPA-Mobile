package com.tpa.questapp.model

import android.os.Parcel
import android.os.Parcelable

data class Member (
    var userId : String? = "",
    var pictProfile: String? = "",
    var fullname: String? = "",
    val major: String?="",
    var location: String? = "",
    var gender: String? = "",
    var job: String? = ""
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
        parcel.writeString(userId)
        parcel.writeString(pictProfile)
        parcel.writeString(fullname)
        parcel.writeString(major)
        parcel.writeString(location)
        parcel.writeString(gender)
        parcel.writeString(job)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Member> {
        override fun createFromParcel(parcel: Parcel): Member {
            return Member(parcel)
        }

        override fun newArray(size: Int): Array<Member?> {
            return arrayOfNulls(size)
        }
    }

}