package com.tpa.questapp.model

import android.os.Parcel
import android.os.Parcelable

class Ticket(
    var questionId: String? ="",
    var userId: String? = "",
    var question: String? = "",
    var topic: String? = "",
    var createdDate: String? = ""
    ):Parcelable{
        constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(questionId)
            parcel.writeString(userId)
            parcel.writeString(question)
            parcel.writeString(topic)
            parcel.writeString(createdDate)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Ticket> {
            override fun createFromParcel(parcel: Parcel): Ticket {
                return Ticket(parcel)
            }

            override fun newArray(size: Int): Array<Ticket?> {
                return arrayOfNulls(size)
            }
        }

    }