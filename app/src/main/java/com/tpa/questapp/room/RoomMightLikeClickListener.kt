package com.tpa.questapp.room

import android.view.View
import com.tpa.questapp.model.Room

interface RoomMightLikeClickListener {
    fun onItemClicked(view: View, room: Room)
}