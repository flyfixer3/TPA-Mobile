package com.tpa.questapp

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage

class FirebaseStorageManager {
    private val TAG = "FirebaseStorageManager"

    private val mStorageRef = FirebaseStorage.getInstance().reference
    private lateinit var mProgressDialog: ProgressDialog
    fun upload(con: Context, ImageUri: Uri){
        mProgressDialog = ProgressDialog(con)
        mProgressDialog.setMessage("Please Wait")
        val uploadTask = mStorageRef.child("users/profilepic.png").putFile(ImageUri)
        uploadTask.addOnSuccessListener {
            Log.e(TAG, "Success")
        }.addOnFailureListener {
            Log.e(TAG,"failed")
        }
    }
}