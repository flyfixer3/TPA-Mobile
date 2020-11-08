package com.tpa.questapp.roomdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.QuestionRoom
import kotlinx.android.synthetic.main.activity_question_room_form.*
import java.util.*

class QuestionRoomFormActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var questionId: String
    private lateinit var roomId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_room_form)

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
                    else -> {
                        val imgDef =
                            "https://firebasestorage.googleapis.com/v0/b/fir-authquestapp.appspot.com/o/post1.jpg?alt=media&token=5a221865-ba4c-40d7-9cb5-b1650ba58a2b"
                        writeQuestionRoom(auth.uid.toString(), imgDef, topic, question)
                        Toast.makeText(this, "Success Add Question", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }else{
            database.child("rooms").child(roomId).child("questionrooms").child(questionId).addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    topicRoomField.setText(snapshot.child("questionTopic").value.toString().trim())
                    questionRoomField.setText(snapshot.child("question").value.toString().trim())
                }

            })
            addQuestionRoomBtn.setText("Update Question")
            addQuestionRoomBtn.setOnClickListener {
                val topic = topicRoomField.text.toString().trim { it <= ' ' }
                val question = questionRoomField.text.toString().trim { it <= ' ' }
                when{
                    topic.isEmpty() -> topicRoomField.error = "The spesific topic field must be filled"
                    question.length > 250 -> questionRoomField.error = "The question field maks. 250 characters"
                    else -> {
                        val imgDef =
                            "https://firebasestorage.googleapis.com/v0/b/fir-authquestapp.appspot.com/o/post1.jpg?alt=media&token=5a221865-ba4c-40d7-9cb5-b1650ba58a2b"
                        writeQuestionRoom(auth.uid.toString(), imgDef, topic, question)
                        Toast.makeText(this, "Success Update Question", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun writeQuestionRoom(userId: String, imgQuestion: String, topicQuestion: String, question: String){
        if (questionId.equals("add")){
            questionId = UUID.randomUUID().toString()
        }
        val question = QuestionRoom(questionId,userId,topicQuestion, question, imgQuestion, roomId)
        database.child("rooms").child(roomId).child("questionrooms").child(questionId).setValue(question)
    }
}