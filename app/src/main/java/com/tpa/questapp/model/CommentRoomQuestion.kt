package com.tpa.questapp.model

import android.os.Parcel
import android.os.Parcelable

class CommentRoomQuestion(
    var commentId: String? ="",
    var userId: String? = "",
    var comment: String? = "",
    var roomId: String? = "",
    var questionId: String? = "",
    var created: String? =""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeString(commentId)
        parcel?.writeString(userId)
        parcel?.writeString(comment)
        parcel?.writeString(roomId)
        parcel?.writeString(questionId)
        parcel?.writeString(created)
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<CommentRoomPost> {
        override fun createFromParcel(parcel: Parcel): CommentRoomPost {
            return CommentRoomPost(parcel)
        }

        override fun newArray(size: Int): Array<CommentRoomPost?> {
            return arrayOfNulls(size)
        }
    }

}