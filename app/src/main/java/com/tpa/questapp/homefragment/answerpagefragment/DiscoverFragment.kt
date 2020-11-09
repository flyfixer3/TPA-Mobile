package com.tpa.questapp.homefragment.answerpagefragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tpa.questapp.LoginActvity
import com.tpa.questapp.R
import com.tpa.questapp.model.Room
import com.tpa.questapp.room.RoomFormActivity
import com.tpa.questapp.room.RoomListAdapter
import com.tpa.questapp.room.RoomMightLikeClickListener
import com.tpa.questapp.room.ViewAllRoomActivity
import com.tpa.questapp.roomdetail.RoomDetailActivity
import kotlinx.android.synthetic.main.fragment_discover.view.*

class DiscoverFragment : Fragment() {
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var adap: RoomListAdapter
    private lateinit var database: DatabaseReference
    private lateinit var userDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var roomList: ArrayList<Room>
    private lateinit var roomCreatedUser: ArrayList<Room>
    private lateinit var roomFollowUser: ArrayList<Room>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("rooms")
        userDatabase = FirebaseDatabase.getInstance().getReference("users").child(auth.uid.toString())
        roomList = arrayListOf()
        roomCreatedUser = arrayListOf()
        roomFollowUser = arrayListOf()
        val view: View = inflater.inflate(
            R.layout.fragment_discover,
            container,
            false
        )
        // Inflate the layout for this fragment
        view.addRoomButton.setOnClickListener{
            val intent = Intent(view.context, RoomFormActivity::class.java)
            intent.putExtra("roomId", "")
            startActivity(intent)
        }
        layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomMightLike.layoutManager = layoutManager
        view.roomMightLike.setHasFixedSize(true)
        val lmc = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomCreatedDiscover.layoutManager = lmc
        view.roomCreatedDiscover.setHasFixedSize(true)
        val lmf = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomFollowDiscover.layoutManager = lmf
        view.roomFollowDiscover.setHasFixedSize(true)
        val lm1 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic1Discover.layoutManager = lm1
        view.roomTopic1Discover.setHasFixedSize(true)
        val lm2 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic2Discover.layoutManager = lm2
        view.roomTopic2Discover.setHasFixedSize(true)
        val lm3 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic3Discover.layoutManager = lm3
        view.roomTopic3Discover.setHasFixedSize(true)
        val lm4 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic4Discover.layoutManager = lm4
        view.roomTopic4Discover.setHasFixedSize(true)
        val lm5 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic5Discover.layoutManager = lm5
        view.roomTopic5Discover.setHasFixedSize(true)
        val lm6 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic6Discover.layoutManager = lm6
        view.roomTopic6Discover.setHasFixedSize(true)
        val lm7 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic7Discover.layoutManager = lm7
        view.roomTopic7Discover.setHasFixedSize(true)
        val lm8 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic8Discover.layoutManager = lm8
        view.roomTopic8Discover.setHasFixedSize(true)
        val lm9 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic9Discover.layoutManager = lm9
        view.roomTopic9Discover.setHasFixedSize(true)
        val lm10 = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomTopic10Discover.layoutManager = lm10
        view.roomTopic10Discover.setHasFixedSize(true)

        var topic = arrayListOf<String>()
        userDatabase.addValueEventListener(object : ValueEventListener, RoomMightLikeClickListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
//                show created room user
                roomCreatedUser.clear()
                for(h in snapshot.child("rooms").children){
                    val room = h.getValue(Room::class.java)
                    roomCreatedUser.add(room!!)
                }
                view.roomCreatedDiscovertv.isVisible = !roomCreatedUser.isEmpty()
                val adp = RoomListAdapter(roomCreatedUser,view.context)
                view.roomCreatedDiscover.adapter = adp
                adp.listener = this

//                show room per topic
                val major = snapshot.child("major").value.toString().trim()
                if (major.equals("Accounting")){
                    topic = arrayListOf<String>("Financial","Tax","Audit","Accounting Information Systems","Fiduciary","Budgeting","Ledger","Sales","Report","Cash Flow")
                }else if(major.equals("Computer Science")){
                    topic = arrayListOf<String>("Algorithm","Calculus","Artificial Intelligence","Python","Mobile","Web Programming","Virtual Intelligence","Laravel","Network","Technology")
                }else if(major.equals("Information Systems")){
                    topic = arrayListOf<String>("Business Information","Statistic","Management System","Information","Technology","Web","Mobile Android","Object Oriented Programming","Design and Analysis","Java")
                }else{
                    topic = arrayListOf<String>("Stock Exchange","Marketing","Business","Analysis","Capital","Bull","Trade","Costing","Report","Strategy")
                }
                view.roomTopic1Discovertv.setText(topic[0])
                view.roomTopic2Discovertv.setText(topic[1])
                view.roomTopic3Discovertv.setText(topic[2])
                view.roomTopic4Discovertv.setText(topic[3])
                view.roomTopic5Discovertv.setText(topic[4])
                view.roomTopic6Discovertv.setText(topic[5])
                view.roomTopic7Discovertv.setText(topic[6])
                view.roomTopic8Discovertv.setText(topic[7])
                view.roomTopic9Discovertv.setText(topic[8])
                view.roomTopic10Discovertv.setText(topic[9])
            }

            override fun onItemClicked(view: View, room: Room) {
                Toast.makeText(view.context,
                    "Room ${room.roomId} berhasil di klik",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }

        })
        database.addValueEventListener(object  : ValueEventListener, RoomMightLikeClickListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

//            show room might like
            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                if(snapshot.exists()){
                    for (h in snapshot.children){
                        val room = h.getValue(Room::class.java)
                        roomList.add(room!!)
                    }
                    //show room might like
                    adap = RoomListAdapter(roomList,view.context)
                    view.roomMightLike.adapter = adap
                    adap.listener = this

                    val roomTopic1 = arrayListOf<Room>()
                    val roomTopic2 = arrayListOf<Room>()
                    val roomTopic3 = arrayListOf<Room>()
                    val roomTopic4 = arrayListOf<Room>()
                    val roomTopic5 = arrayListOf<Room>()
                    val roomTopic6 = arrayListOf<Room>()
                    val roomTopic7 = arrayListOf<Room>()
                    val roomTopic8 = arrayListOf<Room>()
                    val roomTopic9 = arrayListOf<Room>()
                    val roomTopic10 = arrayListOf<Room>()
                    for(h in snapshot.children){
                        val room = h.getValue(Room::class.java)
                        if(room!!.topic.equals(topic[0])){
                            roomTopic1.add(room)
                        }else if(room!!.topic.equals(topic[1])){
                            roomTopic2.add(room)
                        }else if(room!!.topic.equals(topic[2])){
                            roomTopic3.add(room)
                        }else if(room!!.topic.equals(topic[3])){
                            roomTopic4.add(room)
                        }else if(room!!.topic.equals(topic[4])){
                            roomTopic5.add(room)
                        }else if(room!!.topic.equals(topic[5])){
                            roomTopic6.add(room)
                        }else if(room!!.topic.equals(topic[6])){
                            roomTopic7.add(room)
                        }else if(room!!.topic.equals(topic[7])){
                            roomTopic8.add(room)
                        }else if(room!!.topic.equals(topic[8])){
                            roomTopic9.add(room)
                        }else if(room!!.topic.equals(topic[9])){
                            roomTopic10.add(room)
                        }

                    }
                    val adp1 = RoomListAdapter(roomTopic1,view.context)
                    view.roomTopic1Discover.adapter = adp1
                    view.roomTopic1Discovertv.isVisible = !roomTopic1.isEmpty()
                    val adp2 = RoomListAdapter(roomTopic2,view.context)
                    view.roomTopic2Discover.adapter = adp2
                    view.roomTopic2Discovertv.isVisible = !roomTopic2.isEmpty()
                    val adp3 = RoomListAdapter(roomTopic3,view.context)
                    view.roomTopic3Discover.adapter = adp3
                    view.roomTopic3Discovertv.isVisible = !roomTopic3.isEmpty()
                    val adp4 = RoomListAdapter(roomTopic4,view.context)
                    view.roomTopic4Discover.adapter = adp4
                    view.roomTopic4Discovertv.isVisible = !roomTopic4.isEmpty()
                    val adp5 = RoomListAdapter(roomTopic5,view.context)
                    view.roomTopic5Discover.adapter = adp5
                    view.roomTopic5Discovertv.isVisible = !roomTopic5.isEmpty()
                    val adp6 = RoomListAdapter(roomTopic6,view.context)
                    view.roomTopic6Discover.adapter = adp6
                    view.roomTopic6Discovertv.isVisible = !roomTopic6.isEmpty()
                    val adp7 = RoomListAdapter(roomTopic7,view.context)
                    view.roomTopic7Discover.adapter = adp7
                    view.roomTopic7Discovertv.isVisible = !roomTopic7.isEmpty()
                    val adp8 = RoomListAdapter(roomTopic8,view.context)
                    view.roomTopic8Discover.adapter = adp8
                    view.roomTopic8Discovertv.isVisible = !roomTopic8.isEmpty()
                    val adp9 = RoomListAdapter(roomTopic9,view.context)
                    view.roomTopic9Discover.adapter = adp9
                    view.roomTopic9Discovertv.isVisible = !roomTopic9.isEmpty()
                    val adp10 = RoomListAdapter(roomTopic10,view.context)
                    view.roomTopic10Discover.adapter = adp10
                    view.roomTopic10Discovertv.isVisible = !roomTopic10.isEmpty()

                    adp1.listener = this
                    adp2.listener = this
                    adp3.listener = this
                    adp4.listener = this
                    adp5.listener = this
                    adp6.listener = this
                    adp7.listener = this
                    adp8.listener = this
                    adp9.listener = this
                    adp10.listener = this
                }
            }

            override fun onItemClicked(view: View, room: Room) {
                Toast.makeText(view.context,
                    "Room ${room.roomId} berhasil di klik",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }
        })

        userDatabase.child("followrooms").addValueEventListener( object : ValueEventListener, RoomMightLikeClickListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

//            show followed room
            override fun onDataChange(snapshot: DataSnapshot) {
                roomFollowUser.clear()
                for (a in snapshot.children){
                    val room = a.getValue(Room::class.java)
                    roomFollowUser.add(room!!)
                }
                val adapF = RoomListAdapter(roomFollowUser,view.context)
                view.roomFollowDiscover.adapter = adapF
                view.roomFollowDiscovertv.isVisible = !roomFollowUser.isEmpty()
                adapF.listener = this
            }

            override fun onItemClicked(view: View, room: Room) {
                Toast.makeText(view.context,
                    "Room ${room.roomId} berhasil di klik",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }

        })

        view.viewAllRoom.setOnClickListener {
            val intent = Intent (view.context, ViewAllRoomActivity::class.java)
            startActivity(intent)
        }

//        view.filterRecentBtn.setOnClickListener{
//            roomList.sortWith(object: Comparator<Room>{
//                override fun compare(p1: Room, p2: Room): Int = when {
//                    p1.createRoomDate!! > p2.createRoomDate!! -> 1
//                    p1.createRoomDate == p2.createRoomDate -> 0
//                    else -> -1
//                }
//            })
//            adap = RoomListAdapter(roomList,view.context)
//            view.roomMightLike.adapter = adap
//            view.filterRecentBtn.setBackgroundColor(resources.getColor(R.color.colorAccent))
//            view.filterAlphaBtn.setBackgroundColor(resources.getColor(R.color.grey))
//        }
//        view.filterAlphaBtn.setOnClickListener {
//            roomList.sortWith(object: Comparator<Room>{
//                override fun compare(p1: Room, p2: Room): Int = when {
//                    p1.nameRoom!! > p2.nameRoom!! -> 1
//                    p1.nameRoom == p2.nameRoom -> 0
//                    else -> -1
//                }
//            })
//            adap = RoomListAdapter(roomList,view.context)
//            view.roomMightLike.adapter = adap
//            view.filterRecentBtn.setBackgroundColor(resources.getColor(R.color.grey))
//            view.filterAlphaBtn.setBackgroundColor(resources.getColor(R.color.colorAccent))
//        }
        return view
    }
}