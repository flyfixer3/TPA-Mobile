package com.tpa.questapp.model

import android.os.Parcel
import android.os.Parcelable

class QuestionRoom(
    var questionRoomId: String? ="",
    var userId: String? = "",
    var questionTopic: String? = "",
    var question: String? = "",
    var questionImg: String? = "",
    var roomId: String? ="",
    var questionDate: String? = ""
): Parcelable {
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

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeString(questionRoomId)
        parcel?.writeString(userId)
        parcel?.writeString(questionTopic)
        parcel?.writeString(question)
        parcel?.writeString(questionImg)
        parcel?.writeString(roomId)
        parcel?.writeString(questionDate)
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<QuestionRoom> {
        override fun createFromParcel(parcel: Parcel): QuestionRoom {
            return QuestionRoom(parcel)
        }

        override fun newArray(size: Int): Array<QuestionRoom?> {
            return arrayOfNulls(size)
        }
    }
}