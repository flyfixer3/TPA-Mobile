package com.tpa.questapp.roomdetail

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
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
import com.tpa.questapp.model.QuestionRoom
import kotlinx.android.synthetic.main.activity_post_room_form.*
import kotlinx.android.synthetic.main.activity_question_room_form.*
import java.text.DateFormat
import java.util.*

class QuestionRoomFormActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var questionId: String
    private lateinit var roomId: String
    lateinit var filepath : Uri

    companion object{
        private val PICK_IMAGE_Code = 1000
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            filepath = data!!.data!!
            Log.d("123", filepath.toString())
            var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
//            roomPostFormImg.setImageBitmap(bitmap)
            Picasso.get().load(filepath).into(roomQuestionFormImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_room_form)

        database = Firebase.database.reference
        auth = Firebase.auth

        questionId = intent.getStringExtra("questionId").toString()
        roomId = intent.getStringExtra("roomId").toString()
        imgQuestionUploadBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PostRoomFormActivity.PICK_IMAGE_Code)
        }
        if(questionId.equals("add")){
            addQuestionRoomBtn.setOnClickListener {
                val topic = topicRoomField.text.toString().trim { it <= ' ' }
                val question = questionRoomField.text.toString().trim { it <= ' ' }
                when{
                    topic.isEmpty() -> topicRoomField.error = "The spesific topic field must be filled"
                    question.length > 250 -> questionRoomField.error = "The question field maks. 250 characters"
                    else -> {
                        val imgDef =
                            "https://firebasestorage.googleapis.com/v0/b/fir-authquestapp.appspot.com/o/messageImage_1604993851101.jpg?alt=media&token=3609c023-18e0-44d6-9382-1b9ad43451f4"
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
        val questionDate = DateFormat.getDateTimeInstance().format(Date())
        if (questionId.equals("add")){
            questionId = UUID.randomUUID().toString()
        }
        val question = QuestionRoom(questionId,userId,topicQuestion, question, imgQuestion, roomId, questionDate)
        database.child("rooms").child(roomId).child("questionrooms").child(questionId).setValue(question)
    }
}