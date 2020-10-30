package com.tpa.questapp.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.Room

class RoomListAdapter : RecyclerView.Adapter<RoomListAdapter.Companion.Holder>{
    var listener: RoomMightLikeClickListener? = null
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    companion object{
        class Holder : RecyclerView.ViewHolder{
            lateinit var roomName: TextView
            lateinit var roomImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var roomTopic: TextView
            lateinit var unfollowBtn: Button
            lateinit var followBtn: Button

            constructor(rv: View) : super(rv){
                roomName = rv.findViewById(R.id.roomNameList) as TextView
                roomImg = rv.findViewById(R.id.roomListImg) as de.hdodenhof.circleimageview.CircleImageView
                roomTopic = rv.findViewById(R.id.roomTopicList) as TextView
                unfollowBtn = rv.findViewById(R.id.unfollowDiscoverBtn) as Button
                followBtn = rv.findViewById(R.id.followDiscoverBtn) as Button
            }
        }
    }


    var list: ArrayList<Room> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<Room>, con: Context) : super() {
        this.list = list
        this.con = con

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.room_list, parent,false)
        holder = Holder(rv)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        var at: Room
        at = list.get(position)
        Picasso.get().load(at.imgRoom).into(holder!!.roomImg)
        holder!!.roomName.setText(at.nameRoom)
        holder!!.roomTopic.setText(at.topic)
        holder.itemView.setOnClickListener{
            listener?.onItemClicked(it,at)
        }
        if (at.userId.equals(auth.uid.toString())){
            holder!!.followBtn.isVisible = false
        }

        var keyFollowRooms: String = ""
        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.child("users").child(auth.uid.toString()).child("followrooms").children){
                    val room = h.getValue(Room::class.java)
                    keyFollowRooms = h.key.toString()
                    if(room!!.roomId.equals(at.roomId)){
                        holder.followBtn.setText("unfollow")
                    }
                }
            }

        })
        holder!!.followBtn.setOnClickListener {
            if(holder.followBtn.text.equals("follow")){
                holder.followBtn.setText("unfollow")
                database.child("users").child(auth.uid.toString()).child("followrooms").push().setValue(at)
                database.child("rooms").child(at.roomId.toString()).child("followers").child(auth.uid.toString()).setValue(auth.uid.toString())
            }else{
                holder.followBtn.setText("follow")
                database.child("users").child(auth.uid.toString()).child("followrooms").child(keyFollowRooms).removeValue()
                database.child("rooms").child(at.roomId.toString()).child("followers").child(auth.uid.toString()).removeValue()
            }
        }
    }

}