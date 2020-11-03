package com.tpa.questapp.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.Room
import kotlinx.android.synthetic.main.activity_view_all_room.*
import kotlinx.android.synthetic.main.fragment_discover.view.*

class ViewAllRoomActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var roomList: ArrayList<Room>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_room)

        roomList = arrayListOf()
        database = Firebase.database.reference
        val layoutManager = GridLayoutManager(this@ViewAllRoomActivity, 3 )
        AllRoomList.layoutManager = layoutManager
//        AllRoomList.setHasFixedSize(true)

        database.child("rooms").addValueEventListener( object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    val room = h.getValue(Room::class.java)
                    roomList.add(room!!)
                }
                val adp = RoomListAdapter(roomList,this@ViewAllRoomActivity)
                AllRoomList.adapter = adp
            }

        })
    }
}