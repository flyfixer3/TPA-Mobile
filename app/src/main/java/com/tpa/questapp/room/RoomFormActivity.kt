package com.tpa.questapp.room

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.Room
import kotlinx.android.synthetic.main.activity_room_form.*
import java.text.DateFormat
import java.util.*


class RoomFormActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var roomId:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_form)
        database = Firebase.database.reference
        auth = Firebase.auth
        init()
    }

    private fun init() {
        loadTopic()
        roomId = intent.getStringExtra("roomId").toString()
        if(roomId != ""){
            database.child("rooms").child(roomId).addValueEventListener(object :ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val at = snapshot.getValue(Room::class.java)
                    roomNameFormField.setText(at?.nameRoom)
                    roomDescriptionFormField.setText(at?.descRoom)
                    topicRoomFormSpinner.setSelection(getIndex(topicRoomFormSpinner, at?.topic.toString()))
                    createRoomFormButton.setText("Update Room")
                }

            })

        }
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

    private fun getIndex(spinner: Spinner, topic: String): Int {
        for (i in 0 until topicRoomFormSpinner.getCount()) {
            if (topicRoomFormSpinner.getItemAtPosition(i).toString().equals(topic)) {
                return i
            }
        }
        return 0
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

        if (roomId != ""){

        }else{
            roomId = UUID.randomUUID().toString()
        }
        val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
        val room = Room(roomId, userId, imgRoom, nameRoom, descRoom, topic, currentDateTimeString)
        database.child("rooms").child(roomId).setValue(room)
        database.child("users").child(userId).child("rooms").child(roomId).setValue(room)
    }
}