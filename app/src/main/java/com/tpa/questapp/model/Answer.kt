package com.tpa.questapp.model

import android.os.Parcel
import android.os.Parcelable

class Answer (
    var answerId: String? = "",
    var userId: String? = "",
    var answer: String? = "",
    var media: String? = "",
    var upvote: Int,
    var downvote: Int
):Parcelable{
    constructor(parcel: Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ){
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(answerId)
        parcel.writeString(userId)
        parcel.writeString(answer)
        parcel.writeString(media)
        parcel.writeInt(upvote)
        parcel.writeInt(downvote)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }

}