package com.tpa.questapp.roomdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.followfragment.ListFollowAdapter
import com.tpa.questapp.model.Room
import com.tpa.questapp.model.User
import kotlinx.android.synthetic.main.activity_room_member.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_user_follower_tab.view.*

class RoomMemberActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var memberList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_member)

        database = Firebase.database.reference
        memberList = arrayListOf()

        val roomId:String = intent.getStringExtra("roomId").toString()

        var at: Room? = null
        database.child("rooms").child(roomId).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                at = snapshot.getValue(Room::class.java)
                database.child("users").child(at?.userId.toString()).addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        nameOwnerRoomMember.setText(snapshot.child("fullname").value.toString().trim())
                        majorOwnerRoomMember.setText(snapshot.child("major").value.toString().trim())
                        Picasso.get().load(snapshot.child("pictProfile").value.toString().trim()).into(imgOwnerRoomMember)
                    }
                })
            }
        })

        database.child("rooms").child(roomId).child("followers").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                memberList.clear()
                if(snapshot.exists()){
                    for (h in snapshot.children){
                        val user = h.key.toString()
                        memberList.add(user)
                    }
//
                }
            }
        })

        val allUserList = arrayListOf<User>()
        database.child("users").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                allUserList.clear()
                for (h in snapshot.children){
                    for (d in memberList) {
                        if (h.key.equals(d)) {
                            val user = h.getValue(User::class.java)
                            allUserList.add(user!!)
                        }
                    }
                }
                MemberRoomListView.adapter = ListFollowAdapter(applicationContext, R.layout.list_follower, allUserList)
            }
        })


    }
}