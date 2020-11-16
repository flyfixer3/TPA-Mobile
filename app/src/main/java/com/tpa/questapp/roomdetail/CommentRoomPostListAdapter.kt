package com.tpa.questapp.roomdetail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import com.tpa.questapp.model.CommentRoomPost

class CommentRoomPostListAdapter : RecyclerView.Adapter<CommentRoomPostListAdapter.Companion.Holder> {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    companion object{
        class Holder : RecyclerView.ViewHolder{

            lateinit var userName: TextView
            lateinit var userImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var comment: TextView
            lateinit var deleteBtn: TextView
            lateinit var updateBtn: TextView

            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.userCommentRoomName) as TextView
                userImg = rv.findViewById(R.id.roomCommentUserImg) as de.hdodenhof.circleimageview.CircleImageView
                comment = rv.findViewById(R.id.commentRoom) as TextView
                deleteBtn = rv.findViewById(R.id.deleteCommentBtn) as TextView
                updateBtn = rv.findViewById(R.id.updateCommentRoomBtn) as TextView
            }
        }
    }

    var list: ArrayList<CommentRoomPost> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<CommentRoomPost>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentRoomPostListAdapter.Companion.Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.comment_room_post_list, parent,false)
        holder = Holder(rv)
        return holder
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.deleteBtn.isVisible = false
        holder.updateBtn.isVisible = false

        var at: CommentRoomPost = list.get(position)
        database = Firebase.database.reference
        auth = Firebase.auth

        database.child("users").child(at.userId.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                holder.userName.setText(snapshot.child("fullname").value.toString().trim())
                Picasso.get().load(snapshot.child("pictProfile").value.toString().trim()).into(holder!!.userImg)
            }

        })
        if(auth.uid.toString().equals(at.userId)){
            holder.updateBtn.isVisible = true
            holder.deleteBtn.isVisible = true
        }
        holder.comment.setText(at.comment)
        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(con)
            builder.setTitle(con.resources.getString(R.string.CommentRoom))
            builder.setMessage(con.resources.getString(R.string.CommentDelete))
            builder.setPositiveButton(con.resources.getString(R.string.cancel)){dialogInterface, which -> }
            builder.setNegativeButton(con.resources.getString(R.string.ok)){dialogInterface, which ->
                database.child("rooms").child(at.roomId.toString()).child("posts").child(at.postId.toString()).child("comments").child(at.commentId.toString()).removeValue()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
        holder.updateBtn.setOnClickListener {
            val intent = Intent(con, CommentRoomPostActivity::class.java)
            intent.putExtra("roomId", at.roomId)
            intent.putExtra("postId", at.postId)
            intent.putExtra("action","update")
            intent.putExtra("commentId",at.commentId)
            intent.putExtra("comment", at.comment)
            con.startActivity(intent)
        }
    }

}