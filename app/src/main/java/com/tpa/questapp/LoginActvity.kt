package com.tpa.questapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login_actvity.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_actvity)
        init()
    }

    private fun init() {
        loginButton.setOnClickListener { view ->
            var email = emailField.getText().toString()
            var password = passwordField.getText().toString()

            if (email.equals("")) {
                showMessageDialog("Email Field Required", "Email must be filled")
            } else if (password.equals("")) {
                showMessageDialog("Password Field Required", "Password must be filled")
            } else {
//                showMessageDialog("Unsuccessful Login", "Invalid Email or Password")
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{

                        if (!it.isSuccessful){ return@addOnCompleteListener
                            val intent = Intent (this, LoginActvity::class.java)
                            startActivity(intent)
                        }
                        else
                            Toast.makeText(this, "Succesfully Login", Toast.LENGTH_SHORT).show()
                        val intent = Intent (this, LoginActvity::class.java)
                        startActivity(intent)
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
}