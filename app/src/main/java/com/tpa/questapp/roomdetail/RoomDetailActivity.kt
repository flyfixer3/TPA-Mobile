package com.tpa.questapp.roomdetail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.Room
import com.tpa.questapp.room.RoomFormActivity
import kotlinx.android.synthetic.main.activity_room_detail.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.fragment_post_detail_room.*

class RoomDetailActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_detail)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        updateRoomBtn.isVisible = false
        deleteRoomBtn.isVisible = false

        val roomId:String = intent.getStringExtra("roomId").toString()
        var at: Room? = null
        database.child("rooms").child(roomId).addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                at = snapshot.getValue(Room::class.java)
                findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = at?.nameRoom
                Picasso.get().load(at?.imgRoom).into(imgRoomDetail)
                topicRoomDetail.text = at?.topic
                descRoomDetail.text = at?.descRoom
                if (at?.userId.equals(auth.uid.toString())){
                    findViewById<FloatingActionButton>(R.id.fab).isVisible = false
                    updateRoomBtn.isVisible = true
                    deleteRoomBtn.isVisible = true
//                    addPostRoomDetailBtn.isVisible = true
//                    addQuestionRoomDetailBtn.isVisible = true
                }
            }
        })

        database.child("users").child(auth.uid.toString()).child("followrooms").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (u in snapshot.children){
                    val room = u.getValue(Room::class.java)
                    if(room!!.roomId.equals(roomId)){
                        fab.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_baseline_remove_24))
                        fab.tooltipText = "unfollow"
//                        addPostRoomDetailBtn.isVisible = true
//                        addQuestionRoomDetailBtn.isVisible = true
                    }
                }
            }
        })
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            if(fab.getTooltipText()!!.equals("follow")){
                fab.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_baseline_remove_24))
                fab.tooltipText = "unfollow"
                Snackbar.make(view, "Successfully Follow this room", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                database.child("users").child(auth.uid.toString()).child("followrooms").child(roomId).setValue(at)
                database.child("rooms").child(roomId).child("followers").child(auth.uid.toString()).setValue(auth.uid.toString())
//                addPostRoomDetailBtn.isVisible = true
//                addQuestionRoomDetailBtn.isVisible = true
            }else{
                fab.setImageDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.ic_baseline_follow_24))
                fab.tooltipText = "follow"
                Snackbar.make(view, "Successfully Unfollow this room", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
                database.child("users").child(auth.uid.toString()).child("followrooms").child(roomId).removeValue()
                database.child("rooms").child(at?.roomId.toString()).child("followers").child(auth.uid.toString()).removeValue()
//                addPostRoomDetailBtn.isVisible = false
//                addQuestionRoomDetailBtn.isVisible = false
            }

        }
        viewpager_detailroom.adapter = RoomDetailPagerAdapter(supportFragmentManager, this)

        showMemberBtn.setOnClickListener {
            val intent = Intent(this@RoomDetailActivity, RoomMemberActivity::class.java)
            intent.putExtra("roomId", roomId)
            startActivity(intent)
        }

        updateRoomBtn.setOnClickListener {
            val intent = Intent(this@RoomDetailActivity, RoomFormActivity::class.java)
            intent.putExtra("roomId", roomId)
            startActivity(intent)
        }

        deleteRoomBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(this.resources.getString(R.string.RoomConfirm))
            builder.setMessage(this.resources.getString(R.string.RoomDelete))
            builder.setPositiveButton(this.resources.getString(R.string.cancel)){dialogInterface, which -> }
            builder.setNegativeButton(this.resources.getString(R.string.ok)){dialogInterface, which ->
                database.child("rooms").child(roomId).removeValue()
                database.child("users").child(auth.uid.toString()).child("rooms").child(roomId).removeValue()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
    }
}