package com.tpa.questapp.roomdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.Comment
import kotlinx.android.synthetic.main.activity_comment.*
import java.util.*

class commentActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        database = Firebase.database.reference
        auth = Firebase.auth

        val roomId:String = intent.getStringExtra("roomId").toString()
        val postId:String = intent.getStringExtra("postId").toString()
        addCommentForm.setOnClickListener {
            Toast.makeText(this, "Sucess Add Comment", Toast.LENGTH_LONG).show()
            val id = UUID.randomUUID().toString()
            val comment = Comment(id,auth.uid.toString(), commentField.text.toString())
            database.child("rooms").child(roomId).child("posts").child(postId).child("comments").child(id).setValue(comment)
        }
    }
}