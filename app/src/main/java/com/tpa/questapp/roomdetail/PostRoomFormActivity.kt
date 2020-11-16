package com.tpa.questapp.roomdetail

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
import kotlinx.coroutines.android.awaitFrame


class PostRoomFormActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var postId: String
    private lateinit var roomId: String

    private var filepath : Uri = Uri.EMPTY
    companion object{
        val PICK_IMAGE_Code = 1000
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            filepath = data!!.data!!
            Picasso.get().load(filepath).into(roomPostFormImg)
        }
    }
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
                        val id = UUID.randomUUID().toString()
                        upload("postroom",id, filepath, auth.uid.toString(), titlePost, descPost)
                        Toast.makeText(this, "Success Add Post", Toast.LENGTH_LONG).show()
                        this.finish()
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
                        val id = UUID.randomUUID().toString()
                        upload("postroom",id, filepath, auth.uid.toString(), titlePost, descPost)
                        Toast.makeText(this, "Success Update Post", Toast.LENGTH_LONG).show()
                        this.finish()
                    }
                }
            }
        }

        imgPostUploadBtn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_Code)
        }
    }

    private fun writePost(userId: String, imgPost: String, titlePost: String, descPost: String){
        if (postId.equals("add")){
            postId = UUID.randomUUID().toString()
        }
        val post = Post(postId, userId, titlePost, descPost, imgPost, roomId)
        database.child("rooms").child(roomId).child("posts").child(postId).setValue(post)
    }

    private val TAG = "v"
    val storage = Firebase.storage.reference

    fun upload(folder: String, nameFile: String, ImageUri: Uri, userId: String, titlePost: String, descPost: String) {
        val uploadTask = storage.child(folder+"/"+nameFile+".jpg").putFile(ImageUri)
        uploadTask.addOnSuccessListener {
            val ref =  storage.child(folder+"/"+nameFile+".jpg")
            ref.downloadUrl.addOnCompleteListener {
                val downloadUri = it.result.toString()
                writePost(userId, downloadUri, titlePost, descPost)
            }
            Log.e(TAG, "Success")
        }.addOnFailureListener {
            Log.e(TAG,"failed")
            writePost(userId, "empty", titlePost, descPost)
        }
    }
}