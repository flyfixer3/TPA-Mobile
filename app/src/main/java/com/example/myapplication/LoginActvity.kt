package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login_actvity.*

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
                showMessageDialog("Unsuccessful Login", "Invalid Email or Password")
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