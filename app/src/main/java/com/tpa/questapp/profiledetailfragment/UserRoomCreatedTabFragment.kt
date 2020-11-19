package com.tpa.questapp.profiledetailfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tpa.questapp.R
import com.tpa.questapp.model.Room
import com.tpa.questapp.room.RoomCreatedListAdapter
import kotlinx.android.synthetic.main.fragment_user_room_created_tab.view.*

class UserRoomCreatedTabFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var roomCreatedList: ArrayList<Room>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users").child(auth.uid.toString()).child("rooms")
        val view = inflater.inflate(R.layout.fragment_user_room_created_tab, container, false)

        view.roomCreatedListView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        roomCreatedList = arrayListOf()

        view.noRoomImg.isVisible = false
        view.noRoomTxt.isVisible = false

        database.addValueEventListener(object  : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                roomCreatedList.clear()
                for (h in snapshot.children){
                    val room = h.getValue(Room::class.java)
                    roomCreatedList.add(room!!)
                }
                view.roomCreatedListView.adapter = RoomCreatedListAdapter(roomCreatedList,view.context)
                if (roomCreatedList.isEmpty()){
                    view.noRoomImg.isVisible = true
                    view.noRoomTxt.isVisible = true
                }
            }

        })
        return view
    }
}