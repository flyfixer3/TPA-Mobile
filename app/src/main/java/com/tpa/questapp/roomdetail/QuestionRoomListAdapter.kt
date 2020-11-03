package com.tpa.questapp.roomdetail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.Post

class QuestionRoomListAdapter: RecyclerView.Adapter<QuestionRoomListAdapter.Companion.Holder> {

    companion object{
        class Holder : RecyclerView.ViewHolder{

            lateinit var userName: TextView
            lateinit var userImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var titlePostRoom: TextView
            lateinit var descPostRoom: TextView
            lateinit var imgPostRoom: ImageView
            lateinit var commentCountPost: TextView
            lateinit var updateBtn: TextView
            lateinit var deleteBtn: TextView

            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.userPostName) as TextView
                userImg = rv.findViewById(R.id.roomPostUserImg) as de.hdodenhof.circleimageview.CircleImageView
                titlePostRoom = rv.findViewById(R.id.titlePostRoom) as TextView
                imgPostRoom = rv.findViewById(R.id.imgPostRoom) as ImageView
                descPostRoom = rv.findViewById(R.id.descPostRoom) as TextView
                commentCountPost = rv.findViewById(R.id.commentCountPost) as TextView
                updateBtn = rv.findViewById(R.id.updatePostBtn) as TextView
                deleteBtn = rv.findViewById(R.id.deletePostBtn) as TextView
            }
        }
    }

    constructor(list: ArrayList<Post>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionRoomListAdapter.Companion.Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.question_room_list, parent,false)
        holder = Holder(rv)
        return holder
    }

    var list: ArrayList<Post> = arrayListOf()

    lateinit var con: Context

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        TODO("Not yet implemented")
    }
}