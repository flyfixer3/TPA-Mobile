package com.tpa.questapp.roomdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
import com.tpa.questapp.model.CommentRoomQuestion
import kotlinx.android.synthetic.main.activity_comment_room_question.*
import java.text.DateFormat
import java.util.*

class CommentRoomQuestionActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var CommentList: ArrayList<CommentRoomQuestion>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_room_question)

        database = Firebase.database.reference
        auth = Firebase.auth

        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        commentRoomQuestionList.layoutManager = lm
        CommentList = arrayListOf()

        val roomId:String = intent.getStringExtra("roomId").toString()
        val questionId:String = intent.getStringExtra("questionId").toString()
        val action:String = intent.getStringExtra("action").toString()
        val commentId:String = intent.getStringExtra("commentId").toString()
        val comment: String = intent.getStringExtra("comment").toString()

        if (action.equals("update")){
            commentQField.setText(comment)
            addCommentQForm.setText("Update Comment")
            addCommentQForm.setOnClickListener {
                Toast.makeText(this, "Sucess Update Comment", Toast.LENGTH_LONG).show()
                val comment = CommentRoomQuestion(commentId,auth.uid.toString(), commentQField.text.toString(),roomId,questionId)
                database.child("rooms").child(roomId).child("questionrooms").child(questionId).child("comments").child(commentId).setValue(comment)
            }
        }else{
            addCommentQForm.setOnClickListener {
                Toast.makeText(this, "Sucess Add Comment", Toast.LENGTH_LONG).show()
                val id = UUID.randomUUID().toString()
                val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
                val comment = CommentRoomQuestion(id,auth.uid.toString(), commentQField.text.toString(),roomId,questionId,currentDateTimeString)
                database.child("rooms").child(roomId).child("questionrooms").child(questionId).child("comments").child(id).setValue(comment)
            }
        }

        database.child("rooms").child(roomId).child("questionrooms").child(questionId).child("comments").orderByChild("created").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                CommentList.clear()
                for (h in snapshot.children){
                    val commentQuestion = h.getValue(CommentRoomQuestion::class.java)
                    CommentList.add(commentQuestion!!)
                }
                val adp = CommentRoomQuestionListAdapter(CommentList,this@CommentRoomQuestionActivity)
                commentRoomQuestionList.adapter = adp
            }

        })

    }
}