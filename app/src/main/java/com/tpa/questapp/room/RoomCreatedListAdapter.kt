package com.tpa.questapp.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.Room

class RoomCreatedListAdapter: RecyclerView.Adapter<RoomCreatedListAdapter.Companion.Holder>{
    companion object{
        class Holder : RecyclerView.ViewHolder{

            lateinit var roomName: TextView
            lateinit var roomImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var roomMajor : TextView

            constructor(rv: View) : super(rv){
                roomName = rv.findViewById(R.id.nameRoomCreatedListTxt) as TextView
                roomImg = rv.findViewById(R.id.roomCreatedListImg) as de.hdodenhof.circleimageview.CircleImageView
                roomMajor = rv.findViewById(R.id.topicRoomCreatedListTxt) as TextView
            }
        }
    }

    var list: ArrayList<Room> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<Room>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomCreatedListAdapter.Companion.Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.room_created_list, parent,false)
        holder = Holder(rv)
        return holder
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var at: Room = list[position]
        holder.roomName.setText(at.nameRoom)
        holder.roomMajor.setText(at.topic)
        Picasso.get().load(at.imgRoom).into(holder.roomImg)
    }
}