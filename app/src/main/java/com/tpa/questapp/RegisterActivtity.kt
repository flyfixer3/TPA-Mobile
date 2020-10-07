package com.tpa.questapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.ArrayAdapter
import android.widget.Spinner
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
        loadMajor()
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
                                onAuthSuccess(task.result?.user!!)
                            }
                        }
                }
            }
        }
    }


    private fun onAuthSuccess(user: FirebaseUser) {
        var id: Int = genderRadioGroup.checkedRadioButtonId
        val radio: RadioButton = findViewById(id)
        writeNewUser(user.uid, fullNameField!!.text.toString(), radio.text.toString() , jobField!!.text.toString(), locationField.text.toString(), majorSpinner.getSelectedItem().toString())

        // Go to MainActivity
        startActivity(Intent(this@RegisterActivtity, LoginActvity::class.java))
        finish()
    }

    private fun writeNewUser(userId: String, fullname: String, gender: String, job: String, location: String, major: String) {
        val user = User(fullname, gender, job, location, major)
        database.child("users").child(userId).setValue(user)
    }
    private fun loadMajor() {
        ArrayAdapter.createFromResource(
            this,
            R.array.majors_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            majorSpinner.adapter = adapter
        }
    }
}