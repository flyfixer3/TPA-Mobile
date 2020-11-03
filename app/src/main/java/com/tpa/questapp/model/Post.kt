package com.tpa.questapp.model

import android.os.Parcel
import android.os.Parcelable

data class Post(
    var postId: String? ="",
    var userId: String? = "",
    var postTitle: String? = "",
    var postDesc: String? = "",
    var postImg: String? = "",
    var roomId: String? = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ){

    }
    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeString(postId)
        parcel?.writeString(userId)
        parcel?.writeString(postTitle)
        parcel?.writeString(postDesc)
        parcel?.writeString(postImg)
        parcel?.writeString(roomId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }

}