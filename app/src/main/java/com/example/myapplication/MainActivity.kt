package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActvity::class.java)
            startActivity(intent)
        }

        registerBtn.setOnClickListener{
            val intent = Intent(this, RegisterActivtity::class.java)
            startActivity(intent)
        }
    }
}