package com.tpa.questapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register_activtity.*

class RegisterActivtity : AppCompatActivity() {
    private var auth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activtity)
        init()
        auth = Firebase.auth
    }

    private fun init() {
        loadMajor()
        loadLocation()
        registerButton.setOnClickListener {
            val email = emailField!!.text.toString().trim { it <= ' ' }
            val password = passwordField!!.text.toString().trim { it <= ' ' }
            val repassword = re_passwordField!!.text.toString().trim { it <= ' ' }

            when {
                TextUtils.isEmpty(email) -> emailField!!.error = "Enter email address!"
                TextUtils.isEmpty(password) -> passwordField!!.error = "Enter password!"
                !password.matches("^[a-zA-Z0-9]*$".toRegex()) -> passwordField!!.error = "password must be in alphanumeric format"
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
                                startActivity(Intent(this@RegisterActivtity, MainActivity::class.java))
                                finish()
                            }
                        }
                }
            }
        }
    }

    private fun loadMajor() {
        val spinnerMajor: Spinner = findViewById(R.id.majors_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.majors_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerMajor.adapter = adapter
        }
    }

    private fun loadLocation(){
        val spinnerLocation: Spinner = findViewById(R.id.locations_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.locations_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerLocation.adapter = adapter
        }
    }
}