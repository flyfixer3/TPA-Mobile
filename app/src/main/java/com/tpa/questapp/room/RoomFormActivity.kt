package com.tpa.questapp.room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.RegisterActivtity
import com.tpa.questapp.model.Room
import kotlinx.android.synthetic.main.activity_register_major.*
import kotlinx.android.synthetic.main.activity_room_form.*
import java.util.*

class RoomFormActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_form)
        database = Firebase.database.reference
        auth = Firebase.auth
        init()
    }

    private fun init() {
        loadTopic()
        createRoomFormButton.setOnClickListener{
            val nameRoom = roomNameFormField!!.text.toString().trim { it <= ' ' }
            val descRoom = roomDescriptionFormField!!.text.toString().trim { it <= ' ' }

            when {
                nameRoom.length < 3 -> roomNameFormField!!.error = "The name room must equals or more 3 characters"
                descRoom.length < 100 -> roomDescriptionFormField!!.error = "The description must equals or more 100 characters"
                else -> {
                    val imgDef="https://firebasestorage.googleapis.com/v0/b/fir-authquestapp.appspot.com/o/quora-featured-image-2.png?alt=media&token=653a180b-9d87-4a4c-9d51-130599a10ff4"
                    val topic = topicRoomFormSpinner.selectedItem.toString()
                    writeRoom(auth.uid.toString(), imgDef, nameRoom, descRoom, topic)
                    Toast.makeText(this, "Success Add Room", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun loadTopic() {
        ArrayAdapter.createFromResource(
            this,
            R.array.ComputerScience,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            topicRoomFormSpinner.adapter = adapter
        }
    }

    private fun writeRoom(userId: String, imgRoom: String, nameRoom: String, descRoom: String, topic: String){
        val room = Room(userId, imgRoom, nameRoom, descRoom, topic)
        val roomId = UUID.randomUUID()
        database.child("rooms").child(roomId.toString()).setValue(room)
        database.child("users").child(userId).child("rooms").child(roomId.toString()).setValue(room)
    }
}