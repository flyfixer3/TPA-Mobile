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
import com.tpa.questapp.model.Post
import com.tpa.questapp.model.Room
import kotlinx.android.synthetic.main.activity_post_room_form.*
import kotlinx.android.synthetic.main.activity_room_form.*
import java.text.DateFormat
import java.util.*

class PostRoomFormActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var postId: String
    private lateinit var roomId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_room_form)

        database = Firebase.database.reference
        auth = Firebase.auth

        postId = intent.getStringExtra("postId").toString()
        roomId = intent.getStringExtra("roomId").toString()

        if(postId.equals("add")){
            createPostFormButton.setOnClickListener {
                val titlePost = roomPostTitleField!!.text.toString().trim { it <= ' ' }
                val descPost = roomPostDescriptionField!!.text.toString().trim { it <= ' ' }
                when {
                    titlePost.length < 5 -> roomPostTitleField!!.error = "The title post field must equals or more 5 characters"
                    descPost.length > 250 -> roomPostDescriptionField!!.error = "The description field maks. 250 characters"
                    else -> {
                        val imgDef="https://firebasestorage.googleapis.com/v0/b/fir-authquestapp.appspot.com/o/post1.jpg?alt=media&token=5a221865-ba4c-40d7-9cb5-b1650ba58a2b"
                        writePost(auth.uid.toString(), imgDef, titlePost, descPost)
                        Toast.makeText(this, "Success Add Post", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }else{
            database.child("rooms").child(roomId).child("posts").child(postId).addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    roomPostTitleField.setText(snapshot.child("postTitle").value.toString().trim())
                    roomPostDescriptionField.setText(snapshot.child("postDesc").value.toString().trim())
                }

            })
            createPostFormButton.setText("Update Post")
            createPostFormButton.setOnClickListener {
                val titlePost = roomPostTitleField!!.text.toString().trim { it <= ' ' }
                val descPost = roomPostDescriptionField!!.text.toString().trim { it <= ' ' }
                when {
                    titlePost.length < 5 -> roomPostTitleField!!.error = "The title post must equals or more 3 characters"
                    descPost.length > 250 -> roomPostDescriptionField!!.error = "The description maks. 250 characters"
                    else -> {
                        val imgDef="https://firebasestorage.googleapis.com/v0/b/fir-authquestapp.appspot.com/o/post1.jpg?alt=media&token=5a221865-ba4c-40d7-9cb5-b1650ba58a2b"
                        writePost(auth.uid.toString(), imgDef, titlePost, descPost)
                        Toast.makeText(this, "Success Update Post", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        createPostFormButton.setOnClickListener {
            val titlePost = roomPostTitleField!!.text.toString().trim { it <= ' ' }
            val descPost = roomPostDescriptionField!!.text.toString().trim { it <= ' ' }
            when {
                titlePost.length < 5 -> roomPostTitleField!!.error = "The title post must equals or more 3 characters"
                descPost.length > 250 -> roomPostDescriptionField!!.error = "The description maks. 250 characters"
                else -> {
                    val imgDef="https://firebasestorage.googleapis.com/v0/b/fir-authquestapp.appspot.com/o/post1.jpg?alt=media&token=5a221865-ba4c-40d7-9cb5-b1650ba58a2b"
                    writePost(auth.uid.toString(), imgDef, titlePost, descPost)
                    Toast.makeText(this, "Success Add Post", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun writePost(userId: String, imgPost: String, titlePost: String, descPost: String){

        if (postId.equals("add")){
            postId = UUID.randomUUID().toString()
        }
        val post = Post(postId, userId, titlePost, descPost, imgPost, roomId)
        database.child("rooms").child(roomId).child("posts").child(postId).setValue(post)
    }
}