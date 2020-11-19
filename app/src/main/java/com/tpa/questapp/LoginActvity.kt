package com.tpa.questapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login_actvity.*


class LoginActvity : AppCompatActivity() {

    val PREFS_NAME = "CurrentUser"
    val KEY_UID = "key.uid"
    lateinit var sp: SharedPreferences

    lateinit var auth:FirebaseAuth

    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_actvity)
        sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        init()
    }

    private fun init() {
        auth = Firebase.auth
        database = Firebase.database.reference
        moveRegis.setOnClickListener{
            startActivity(
                Intent(
                    this@LoginActvity,
                    RegisterActivtity::class.java
                ))
        }
        loginButton.setOnClickListener { view ->
            var email = emailField.getText().toString()
            var password = passwordField.getText().toString()
            var idxAt = email.indexOf('@')

            var alpa = 0
            var num = 0
            for(i in password){
               var ps: Int = i.toInt()
                if (ps >= 65 && ps <= 90) alpa++
                if (ps >= 95 && ps <= 122) alpa++
                if (ps >= 48 && ps <= 57) num++
            }

            if (email.equals("")) {
                showMessageDialog("Email Field Required", "Email must be filled")
            } else if(!email.contains("@")){
                showMessageDialog("Invalid Email","Email must contain '@'")
            } else if(!email.endsWith(".com") && !email.endsWith(".co.id")){
                showMessageDialog("Invalid Email","Email must contain '.com' or '.co.id'")
            } else if(email.get(idxAt+1) == '.' || email.get(idxAt-1) == '.' ){
                showMessageDialog("Invalid Email","Email cannot contain '.@' or '@.")
            } else if (password.equals("")) {
                showMessageDialog("Password Field Required", "Password must be filled")
            } else if (alpa == 0 || num == 0){
                showMessageDialog("Invalid Password", "Pasword must be alphanumeric")
            } else {
//                showMessageDialog("Unsuccessful Login", "Invalid Email or Password")
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{

                        if (!it.isSuccessful){ return@addOnCompleteListener
                            val intent = Intent (this, LoginActvity::class.java)
                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(this, "Succesfully Login", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser

                            val userListener = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    // Get Post object and use the values to update the UI
                                    if (dataSnapshot.exists()) {
                                        saveUid(auth.uid.toString())
                                        startActivity(
                                            Intent(
                                                this@LoginActvity,
                                                HomeActivity::class.java
                                            )
                                        )
                                    } else {
                                        startActivity(
                                            Intent(
                                                this@LoginActvity,
                                                RegisterDetailActivity::class.java
                                            ).putExtra("isUserRegister",true)
                                        )
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Getting Post failed, log a message
                                    // ...
                                }
                            }
                            database.child("users").child(user!!.uid).addListenerForSingleValueEvent(userListener)
                        }
                    }
                    .addOnFailureListener{
                        Log.d("Main", "Failed Login: ${it.message}")
                        Toast.makeText(this, "Email/Password incorrect", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun showMessageDialog(title: String, message: String){
        val alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        alert.setMessage(message)
        alert.setPositiveButton("Ok") { dialogInterface, i -> }
        alert.show()
    }

    private fun saveUid(uid: String){
        val editor:SharedPreferences.Editor = sp.edit()
        editor.putString(KEY_UID, uid)
        editor.apply();
    }
}
