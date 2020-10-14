package com.tpa.questapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import kotlinx.android.synthetic.main.activity_register_activtity.*

class RegisterActivtity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activtity)
        init()
        auth = Firebase.auth
        database = Firebase.database.reference
    }

    private fun init() {
        registerButton.setOnClickListener {
            val email = emailField!!.text.toString().trim { it <= ' ' }
            val password = passwordField!!.text.toString().trim { it <= ' ' }
            val repassword = re_passwordField!!.text.toString().trim { it <= ' ' }
            var idxAt = email.indexOf('@')
            var alpa = 0
            var num = 0
            for(i in password){
                var ps: Int = i.toInt()
                if (ps >= 65 && ps <= 90) alpa++
                if (ps >= 95 && ps <= 122) alpa++
                if (ps >= 48 && ps <= 57) num++
            }

            when {
                TextUtils.isEmpty(email) -> emailField!!.error = "Enter email address!"
                !email.contains("@") -> emailField!!.error = "Email must contain '@'"
                !email.endsWith(".com") && !email.endsWith(".co.id") -> emailField!!.error = "Email must contain '.com' or '.co.id'"
                email.get(idxAt+1) == '.' || email.get(idxAt-1) == '.' -> emailField!!.error = "Email cannot contain '.@' or '@.'"
                TextUtils.isEmpty(password) -> passwordField!!.error = "Enter password!"
                alpa == 0 || num == 0 -> passwordField!!.error = "password must be in alphanumeric format"
                TextUtils.isEmpty(repassword) -> re_passwordField!!.error = "Please retype the password"
                password != repassword -> re_passwordField!!.error = "Password and Retype Password must be match"

                else -> {
                    auth!!.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            this@RegisterActivtity
                        ) { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(
                                    this@RegisterActivtity,
                                    ""+task.exception!!.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@RegisterActivtity,
                                    "Account Created. Here you go to next activity." + task.isSuccessful,
                                    Toast.LENGTH_SHORT
                                ).show()
                                onAuthSuccess(task.result?.user!!)
                            }
                        }
                }
            }
        }
    }


    private fun onAuthSuccess(user: FirebaseUser) {
        var registerUser: Boolean = true
        startActivity(Intent(this@RegisterActivtity, LoginActvity::class.java))
        finish()
    }
}