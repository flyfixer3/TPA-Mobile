package com.tpa.questapp.model

import android.os.Parcel
import android.os.Parcelable

class Answer (
    var answerId: String? = "",
    var questionId: String? = "",
    var userId: String? = "",
    var answer: String? = "",
    var media: String? = "",
    var upvote: String? = "",
    var downvote: String? = ""
):Parcelable{
    constructor(parcel: Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ){
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(answerId)
        parcel.writeString(questionId)
        parcel.writeString(userId)
        parcel.writeString(answer)
        parcel.writeString(media)
        parcel.writeString(upvote)
        parcel.writeString(downvote)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Answer> {
        override fun createFromParcel(parcel: Parcel): Answer {
            return Answer(parcel)
        }

        override fun newArray(size: Int): Array<Answer?> {
            return arrayOfNulls(size)
        }
    }

}