package com.tpa.questapp.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Room(
    var roomId: String? ="",
    var userId: String? = "",
    var imgRoom: String? = "",
    var nameRoom: String? = "",
    var descRoom: String? = "",
    var topic: String? = "",
    var createRoomDate : String? = ""
): Parcelable{


    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ){

    }
    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(roomId)
        parcel?.writeString(userId)
        parcel?.writeString(imgRoom)
        parcel?.writeString(nameRoom)
        parcel?.writeString(descRoom)
        parcel?.writeString(topic)
        parcel?.writeString(createRoomDate)
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Room> {
        override fun createFromParcel(parcel: Parcel): Room {
            return Room(parcel)
        }

        override fun newArray(size: Int): Array<Room?> {
            return arrayOfNulls(size)
        }
    }
}

