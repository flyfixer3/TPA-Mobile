package com.tpa.questapp.roomdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.Post
import com.tpa.questapp.model.Room
import com.tpa.questapp.room.RoomListAdapter

class RoomPostListAdapter : RecyclerView.Adapter<RoomPostListAdapter.Companion.Holder> {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    companion object{
        class Holder : RecyclerView.ViewHolder{

            lateinit var userName: TextView
            lateinit var userImg: ImageView
            lateinit var titlePostRoom: TextView
            lateinit var descPostRoom: TextView
            lateinit var imgPostRoom: ImageView
            lateinit var commentCountPost: TextView
            lateinit var updateBtn: TextView
            lateinit var deleteBtn: TextView
            lateinit var addComment: TextView

            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.userPostName) as TextView
                userImg = rv.findViewById(R.id.roomPostUserImg) as ImageView
                titlePostRoom = rv.findViewById(R.id.titlePostRoom) as TextView
                imgPostRoom = rv.findViewById(R.id.imgPostRoom) as ImageView
                descPostRoom = rv.findViewById(R.id.descPostRoom) as TextView
                commentCountPost = rv.findViewById(R.id.commentCountPost) as TextView
                updateBtn = rv.findViewById(R.id.updatePostBtn) as TextView
                deleteBtn = rv.findViewById(R.id.deletePostBtn) as TextView
                addComment = rv.findViewById(R.id.addCommentPost) as TextView
            }
        }
    }

    var list: ArrayList<Post> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<Post>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.room_post_list, parent,false)
        holder = Holder(rv)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.deleteBtn.isVisible = false
        holder.updateBtn.isVisible = false
        var at: Post
        at = list.get(position)
        database = Firebase.database.reference
        auth = Firebase.auth
        database.child("users").child(at.userId.toString()).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                holder.userName.setText(snapshot.child("fullname").value.toString().trim())
                Picasso.get().load(snapshot.child("pictProfile").value.toString().trim()).into(holder!!.userImg)
            }

        })
        Picasso.get().load(at.postImg).into(holder.imgPostRoom)
        holder.titlePostRoom.setText(at.postTitle)
        holder.descPostRoom.setText(at.postDesc)
        holder.commentCountPost.setText("0")
        if(auth.uid.toString().equals(at.userId)){
            holder.updateBtn.isVisible = true
            holder.deleteBtn.isVisible = true
        }
        holder.updateBtn.setOnClickListener {
            Toast.makeText(con,"update",Toast.LENGTH_LONG).show()
            val intent = Intent(con, PostRoomFormActivity::class.java)
            intent.putExtra("roomId", at.roomId)
            intent.putExtra("postId", at.postId)
            con.startActivity(intent)
        }

        holder.deleteBtn.setOnClickListener {
            database.child("rooms").child(at.roomId.toString()).child("posts").child(at.postId.toString()).removeValue()
        }
        holder.addComment.setOnClickListener {
            val intent = Intent(con, commentActivity::class.java)
            intent.putExtra("roomId", at.roomId)
            intent.putExtra("postId", at.postId)
            con.startActivity(intent)
        }
    }
}