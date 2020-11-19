package com.tpa.questapp.roomdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.CommentRoomPost
import com.tpa.questapp.model.Post
import kotlinx.android.synthetic.main.activity_comment_room_post.*
import kotlinx.android.synthetic.main.fragment_post_detail_room.view.*
import java.text.DateFormat
import java.util.*

class CommentRoomPostActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var CommentList: ArrayList<CommentRoomPost>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_room_post)

        database = Firebase.database.reference
        auth = Firebase.auth

        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        commentRoomPostList.layoutManager = lm
        CommentList = arrayListOf()

        val roomId:String = intent.getStringExtra("roomId").toString()
        val postId:String = intent.getStringExtra("postId").toString()
        val action:String = intent.getStringExtra("action").toString()
        val commentId:String = intent.getStringExtra("commentId").toString()
        val comment: String = intent.getStringExtra("comment").toString()

        if (action.equals("update")){
            commentField.setText(comment)
            addCommentForm.setText("Update Comment")
            addCommentForm.setOnClickListener {
                Toast.makeText(this, "Sucess Update Comment", Toast.LENGTH_LONG).show()
                val comment = CommentRoomPost(commentId,auth.uid.toString(), commentField.text.toString(),roomId,postId)
                database.child("rooms").child(roomId).child("posts").child(postId).child("comments").child(commentId).setValue(comment)
            }
        }else{
            addCommentForm.setOnClickListener {
                Toast.makeText(this, "Sucess Add Comment", Toast.LENGTH_LONG).show()
                val id = UUID.randomUUID().toString()
                val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
                val comment = CommentRoomPost(id,auth.uid.toString(), commentField.text.toString(),roomId,postId,currentDateTimeString)
                database.child("rooms").child(roomId).child("posts").child(postId).child("comments").child(id).setValue(comment)
            }
        }

        database.child("rooms").child(roomId).child("posts").child(postId).child("comments").orderByChild("created").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                CommentList.clear()
                for (h in snapshot.children){
                    val commentPost = h.getValue(CommentRoomPost::class.java)
                    CommentList.add(commentPost!!)
                }
                val adp = CommentRoomPostListAdapter(CommentList,this@CommentRoomPostActivity)
                commentRoomPostList.adapter = adp
            }

        })
    }
}