package com.tpa.questapp.question

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.Post
import kotlinx.android.synthetic.main.activity_post_room_form.*
import java.util.*
import com.tpa.questapp.FirebaseStorageManager
import com.tpa.questapp.model.Answer
import kotlinx.android.synthetic.main.activity_answer_question_form.*
import kotlinx.coroutines.android.awaitFrame
import java.text.DateFormat


class AnswerQuestionFormActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var questionId: String
    private lateinit var answerId: String

    private var filepath : Uri = Uri.EMPTY
    companion object{
        val PICK_IMAGE_Code = 1000
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            filepath = data!!.data!!
            Picasso.get().load(filepath).into(answerFormImage)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_question_form)

        database = Firebase.database.reference
        auth = Firebase.auth

        questionId = intent.getStringExtra("questionId").toString()

        createAnswerFormButton.setOnClickListener {
            val descAnswer = answerFormDescription!!.text.toString().trim { it <= ' ' }
            when {
                descAnswer.length > 250 -> answerFormDescription!!.error = "The answer field maks. 250 characters"
                else -> {
                    val id = UUID.randomUUID().toString()
                    upload("answer",id, filepath, auth.uid.toString(), descAnswer)
                    Toast.makeText(this, "Success Add Answer", Toast.LENGTH_LONG).show()
                    this.finish()
                }
            }
        }


        imgAnswerUploadBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_Code)
        }
    }

    private fun writeAnswer(userId: String, imgAnswer: String, descAnswer: String){
        answerId = UUID.randomUUID().toString()
        val answerDate = DateFormat.getDateTimeInstance().format(Date())
        val answer = Answer(answerId, questionId, userId, descAnswer, imgAnswer, 0, 0,answerDate)
        database.child("answer").child(answerId).setValue(answer)
        database.child("questions").child(questionId).child("answers").child(answerId).setValue(answer)
    }

    private val TAG = "v"
    val storage = Firebase.storage.reference

    fun upload(folder: String, nameFile: String, ImageUri: Uri, userId: String, descPost: String) {
        val uploadTask = storage.child(folder+"/"+nameFile+".jpg").putFile(ImageUri)
        uploadTask.addOnSuccessListener {
            val ref =  storage.child(folder+"/"+nameFile+".jpg")
            ref.downloadUrl.addOnCompleteListener {
                val downloadUri = it.result.toString()
                writeAnswer(userId, downloadUri, descPost)
            }
            Log.e(TAG, "Success")
        }.addOnFailureListener {
            Log.e(TAG,"failed")
            writeAnswer(userId, "empty", descPost)
        }
    }
}