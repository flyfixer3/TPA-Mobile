package com.tpa.questapp

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FirebaseStorageManager() {
    private val TAG = "v"
    val storage = Firebase.storage.reference
    private lateinit var mProgressDialog: ProgressDialog
    fun upload(folder: String, nameFile: String, ImageUri: Uri){
        val uploadTask = storage.child(folder+"/"+nameFile+".jpg").putFile(ImageUri)
        uploadTask.continueWithTask{task ->
            if(! task.isSuccessful){
                task.exception?.let{
                    throw it
                }
            }
            val ref =  storage.child(folder+"/"+nameFile+".jpg")
            ref.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
                val d = it.result
                Log.e(TAG, d.toString())
            }
        }.addOnSuccessListener {
            Log.e(TAG, "Success")
        }.addOnFailureListener {
            Log.e(TAG,"failed")
        }
    }

    fun getUrl(folder: String, nameFile: String, ImageUri: Uri)
    : String {
        val d: String
        val uploadTask = storage.child(folder+"/"+nameFile+".jpg").putFile(ImageUri)
        uploadTask.continueWithTask{task ->
            if(! task.isSuccessful){
                task.exception?.let{
                    throw it
                }
            }
            val ref =  storage.child(folder+"/"+nameFile+".jpg")
            ref.downloadUrl
        }.addOnCompleteListener {
            if (it.isSuccessful){
            }
        }
        return "j"
    }
}