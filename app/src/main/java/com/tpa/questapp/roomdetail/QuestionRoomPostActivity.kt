package com.tpa.questapp.roomdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.Post
import kotlinx.android.synthetic.main.activity_question_room_post.*
import java.util.*

class QuestionRoomPostActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var questionId: String
    private lateinit var roomId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_room_post)

        database = Firebase.database.reference
        auth = Firebase.auth

        questionId = intent.getStringExtra("questionId").toString()
        roomId = intent.getStringExtra("roomId").toString()

        if(questionId.equals("add")){
            addQuestionRoomBtn.setOnClickListener {
                val topic = topicRoomField.text.toString().trim { it <= ' ' }
                val question = questionRoomField.text.toString().trim { it <= ' ' }
                when{
                    topic.isEmpty() -> topicRoomField.error = "The spesific topic field must be filled"
                    question.length > 250 -> questionRoomField.error = "The question field maks. 250 characters"
                    else -> "sini"
                }
            }

        }else{
            addQuestionRoomBtn.setOnClickListener {

            }
        }
    }

//    private fun writeQuestion(userId: String, imgPost: String, titlePost: String, descPost: String){
//
//        if (postId.equals("add")){
//            postId = UUID.randomUUID().toString()
//        }
//        val post = Post(postId, userId, titlePost, descPost, imgPost, roomId)
//        database.child("rooms").child(roomId).child("posts").child(postId).setValue(post)
//    }
}