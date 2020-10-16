package com.tpa.questapp

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.model.User
import kotlinx.android.synthetic.main.activity_register_detail.*


class RegisterDetailActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_detail)
        init()
    }

    private fun init() {
        auth = Firebase.auth
        val firebaseUser = auth.currentUser
        val extras = getIntent().getExtras()
        lateinit var profilePict:String
        var isUserRegister = false
        if (null != extras) {
            isUserRegister = extras.getBoolean("isUserRegister", false)
        }

        if (isUserRegister){
            profilePict = "https://firebasestorage.googleapis.com/v0/b/fir-authquestapp.appspot.com/o/quora-featured-image-2.png?alt=media&token=653a180b-9d87-4a4c-9d51-130599a10ff4"

        }else{
            profilePict = firebaseUser!!.photoUrl.toString()
            fullNameField.setText(firebaseUser!!.displayName.toString()).toString()
        }

        nextButton.setOnClickListener{
            val id: Int = genderRadioGroup!!.checkedRadioButtonId
            val radio: RadioButton = findViewById(id)
            val user = User(
                pictProfile = profilePict,
                fullname = fullNameField.text.toString(),
                gender = radio.text.toString(),
                job = jobField.text.toString(),
                location = locationField.text.toString()
            )
            val intent = Intent(this, RegisterMajor::class.java).putExtra("User", user)
            startActivity(intent)
        }
    }
}