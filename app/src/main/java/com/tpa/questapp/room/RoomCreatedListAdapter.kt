package com.tpa.questapp.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tpa.questapp.model.Room
import kotlinx.android.synthetic.main.room_created_list.view.*

class RoomCreatedListAdapter(
    var ctx: Context,
    var resource: Int,
    private var items:List<Room>
) : ArrayAdapter<Room>(ctx, resource, items)  {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(ctx)
        val view:View = layoutInflater.inflate(resource, null)

        val imageRoom: de.hdodenhof.circleimageview.CircleImageView = view.roomCreatedListImg
        val nameRoom: TextView = view.nameRoomCreatedListTxt
        val topic: TextView = view.topicRoomCreatedListTxt

        var mItem: Room = items[position]
        Picasso.get().load(mItem.imgRoom).into(imageRoom)
        nameRoom.setText(mItem.nameRoom)
        topic.setText(mItem.topic)
        return view
    }
}