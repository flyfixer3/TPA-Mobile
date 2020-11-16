package com.tpa.questapp.homefragment.answerpagefragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
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
import kotlinx.android.synthetic.main.fragment_question_detail_room.view.*

class DiscoverFragment : Fragment() {
    lateinit var adapM: RoomListAdapter
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
        //filter room might like
        loadSpinner(view.filterRoomDiscover, view.context)
        loadSpinner(view.filterRoomCreatedDiscover, view.context)
        loadSpinner(view.filterRoomFollowDiscover, view.context)
        view.addRoomButton.setOnClickListener{
            val intent = Intent(view.context, RoomFormActivity::class.java)
            intent.putExtra("roomId", "")
            startActivity(intent)
        }

        view.roomMightLike.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomMightLike.setHasFixedSize(true)

        val lmc = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomCreatedDiscover.layoutManager = lmc
        view.roomCreatedDiscover.setHasFixedSize(true)

        val lmf = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        view.roomFollowDiscover.layoutManager = lmf
        view.roomFollowDiscover.setHasFixedSize(true)

        val lmt = GridLayoutManager(view.context, 3 )
        view.roomTopicDiscover.layoutManager = lmt
        view.roomTopicDiscover.setHasFixedSize(true)
        view.noRoomCategoryImg.isVisible = false
        view.noRoomCategoryTxt.isVisible = false


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
                view.filterRoomCreatedDiscover.isVisible = !roomCreatedUser.isEmpty()
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

                loadSpinnerCategory(view.filterCategoryRoomDiscover, view.context, topic)
            }

            override fun onItemClicked(view: View, room: Room) {
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }

        })
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

        //            show room might like
        database.limitToFirst(10).addValueEventListener(object: ValueEventListener, RoomMightLikeClickListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                roomList.clear()
                for (h in snapshot.children){
                    val room = h.getValue(Room::class.java)
                    if(!(room!!.userId.equals(auth.uid.toString()))) {
                        roomList.add(room!!)
                    }
                }
                //show room might like
                adapM = RoomListAdapter(roomList,view.context)
                view.roomMightLike.adapter = adapM
                adapM.listener = this
            }

            override fun onItemClicked(view: View, room: Room) {
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }

        })
        database.addValueEventListener(object  : ValueEventListener, RoomMightLikeClickListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    roomTopic1.clear()
                    roomTopic2.clear()
                    roomTopic3.clear()
                    roomTopic4.clear()
                    roomTopic5.clear()
                    roomTopic6.clear()
                    roomTopic7.clear()
                    roomTopic8.clear()
                    roomTopic9.clear()
                    roomTopic10.clear()
                    for(h in snapshot.children){
                        val room = h.getValue(Room::class.java)
                        if(!(room!!.userId.equals(auth.uid.toString()))) {
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
                    }
                }
            }

            override fun onItemClicked(view: View, room: Room) {
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }
        })

        view.filterRoomCreatedDiscover.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener, RoomMightLikeClickListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (view.filterRoomCreatedDiscover.selectedItemPosition == 0){
                    roomCreatedUser.sortWith(object: Comparator<Room>{
                        override fun compare(p1: Room, p2: Room): Int = when {
                            p1.roomId!! > p2.roomId!! -> 1
                            p1.roomId == p2.roomId -> 0
                            else -> -1
                        }
                    })
                    val adp = RoomListAdapter(roomCreatedUser,view.context)
                    view.roomCreatedDiscover.adapter = adp
                    adp.listener = this
                }else if (view.filterRoomCreatedDiscover.selectedItemPosition == 1){
                    roomCreatedUser.sortWith(object: Comparator<Room>{
                        override fun compare(p1: Room, p2: Room): Int = when {
                            p1.createRoomDate!! < p2.createRoomDate!! -> 1
                            p1.createRoomDate == p2.createRoomDate -> 0
                            else -> -1
                        }
                    })
                    val adp = RoomListAdapter(roomCreatedUser,view.context)
                    view.roomCreatedDiscover.adapter = adp
                    adp.listener = this

                }else if (view.filterRoomCreatedDiscover.selectedItemPosition == 2){
                    roomCreatedUser.sortWith(object: Comparator<Room>{
                        override fun compare(p1: Room, p2: Room): Int = when {
                            p1.nameRoom!! > p2.nameRoom!! -> 1
                            p1.nameRoom == p2.nameRoom -> 0
                            else -> -1
                        }
                    })
                    val adp = RoomListAdapter(roomCreatedUser,view.context)
                    view.roomCreatedDiscover.adapter = adp
                    adp.listener = this
                }
            }

            override fun onItemClicked(view: View, room: Room) {
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }
        }

        view.filterRoomFollowDiscover.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener, RoomMightLikeClickListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (view.filterRoomFollowDiscover.selectedItemPosition == 0){
                    roomFollowUser.sortWith(object: Comparator<Room>{
                        override fun compare(p1: Room, p2: Room): Int = when {
                            p1.roomId!! > p2.roomId!! -> 1
                            p1.roomId == p2.roomId -> 0
                            else -> -1
                        }
                    })
                    val adapF = RoomListAdapter(roomFollowUser,view.context)
                    view.roomFollowDiscover.adapter = adapF
                    adapF.listener = this
                }else if (view.filterRoomFollowDiscover.selectedItemPosition == 1){
                    roomFollowUser.sortWith(object: Comparator<Room>{
                        override fun compare(p1: Room, p2: Room): Int = when {
                            p1.createRoomDate!! < p2.createRoomDate!! -> 1
                            p1.createRoomDate == p2.createRoomDate -> 0
                            else -> -1
                        }
                    })
                    val adapF = RoomListAdapter(roomFollowUser,view.context)
                    view.roomFollowDiscover.adapter = adapF
                    adapF.listener = this
                }else if (view.filterRoomFollowDiscover.selectedItemPosition == 2){
                    roomFollowUser.sortWith(object: Comparator<Room>{
                        override fun compare(p1: Room, p2: Room): Int = when {
                            p1.nameRoom!! > p2.nameRoom!! -> 1
                            p1.nameRoom == p2.nameRoom -> 0
                            else -> -1
                        }
                    })
                    val adapF = RoomListAdapter(roomFollowUser,view.context)
                    view.roomFollowDiscover.adapter = adapF
                    adapF.listener = this
                }
            }

            override fun onItemClicked(view: View, room: Room) {
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }
        }

        view.filterCategoryRoomDiscover.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener, RoomMightLikeClickListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                view.noRoomCategoryImg.isVisible = false
                view.noRoomCategoryTxt.isVisible = false
                if (view.filterCategoryRoomDiscover.selectedItemPosition == 0){
                    val adpT = RoomListAdapter(roomTopic1,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic1.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }else if (view.filterCategoryRoomDiscover.selectedItemPosition == 1){
                    view.noRoomCategoryImg.isVisible = false
                    view.noRoomCategoryTxt.isVisible = false
                    val adpT = RoomListAdapter(roomTopic2,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic2.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }else if (view.filterCategoryRoomDiscover.selectedItemPosition == 2){
                    view.noRoomCategoryImg.isVisible = false
                    view.noRoomCategoryTxt.isVisible = false
                    val adpT = RoomListAdapter(roomTopic3,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic3.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }else if (view.filterCategoryRoomDiscover.selectedItemPosition == 3){
                    view.noRoomCategoryImg.isVisible = false
                    view.noRoomCategoryTxt.isVisible = false
                    val adpT = RoomListAdapter(roomTopic4,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic4.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }else if (view.filterCategoryRoomDiscover.selectedItemPosition == 4){
                    view.noRoomCategoryImg.isVisible = false
                    view.noRoomCategoryTxt.isVisible = false
                    val adpT = RoomListAdapter(roomTopic5,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic5.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }else if (view.filterCategoryRoomDiscover.selectedItemPosition == 5){
                    view.noRoomCategoryImg.isVisible = false
                    view.noRoomCategoryTxt.isVisible = false
                    val adpT = RoomListAdapter(roomTopic6,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic6.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }else if (view.filterCategoryRoomDiscover.selectedItemPosition == 6){
                    view.noRoomCategoryImg.isVisible = false
                    view.noRoomCategoryTxt.isVisible = false
                    val adpT = RoomListAdapter(roomTopic7,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic7.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }else if (view.filterCategoryRoomDiscover.selectedItemPosition == 7){
                    view.noRoomCategoryImg.isVisible = false
                    view.noRoomCategoryTxt.isVisible = false
                    val adpT = RoomListAdapter(roomTopic8,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic8.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }else if (view.filterCategoryRoomDiscover.selectedItemPosition == 8){
                    view.noRoomCategoryImg.isVisible = false
                    view.noRoomCategoryTxt.isVisible = false
                    val adpT = RoomListAdapter(roomTopic9,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic9.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }else if (view.filterCategoryRoomDiscover.selectedItemPosition == 9){
                    view.noRoomCategoryImg.isVisible = false
                    view.noRoomCategoryTxt.isVisible = false
                    val adpT = RoomListAdapter(roomTopic10,view.context)
                    view.roomTopicDiscover.adapter = adpT
                    adpT.listener = this
                    if (roomTopic10.isEmpty()){
                        view.noRoomCategoryImg.isVisible = true
                        view.noRoomCategoryTxt.isVisible = true
                    }
                }
            }

            override fun onItemClicked(view: View, room: Room) {
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }
        }

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
                adapF.listener = this
                view.roomFollowDiscovertv.isVisible = !roomFollowUser.isEmpty()
                view.filterRoomFollowDiscover.isVisible = !roomFollowUser.isEmpty()

            }

            override fun onItemClicked(view: View, room: Room) {
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }

        })

        view.viewAllRoom.setOnClickListener {
            val intent = Intent (view.context, ViewAllRoomActivity::class.java)
            startActivity(intent)
        }

        view.filterRoomDiscover.onItemSelectedListener = object : AdapterView.OnItemSelectedListener, RoomMightLikeClickListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if( view.filterRoomDiscover.selectedItemPosition == 0){
                    roomList.sortWith(object: Comparator<Room>{
                        override fun compare(p1: Room, p2: Room): Int = when {
                            p1.roomId!! > p2.roomId!! -> 1
                            p1.roomId == p2.roomId -> 0
                            else -> -1
                        }
                    })
                    adapM = RoomListAdapter(roomList,view.context)
                    view.roomMightLike.adapter = adapM
                    adapM.listener = this
                }else if(view.filterRoomDiscover.selectedItemPosition == 1){
                    roomList.sortWith(object: Comparator<Room>{
                        override fun compare(p1: Room, p2: Room): Int = when {
                            p1.createRoomDate!! < p2.createRoomDate!! -> 1
                            p1.createRoomDate == p2.createRoomDate -> 0
                            else -> -1
                        }
                    })
                    adapM = RoomListAdapter(roomList,view.context)
                    view.roomMightLike.adapter = adapM
                    adapM.listener = this
                }else if(view.filterRoomDiscover.selectedItemPosition == 2){
                    roomList.sortWith(object: Comparator<Room>{
                        override fun compare(p1: Room, p2: Room): Int = when {
                            p1.nameRoom!! > p2.nameRoom!! -> 1
                            p1.nameRoom == p2.nameRoom -> 0
                            else -> -1
                        }
                    })
                    adapM = RoomListAdapter(roomList,view.context)
                    view.roomMightLike.adapter = adapM
                    adapM.listener = this
                }
            }

            override fun onItemClicked(view: View, room: Room) {
                val intent = Intent(view.context, RoomDetailActivity::class.java)
                intent.putExtra("roomId", room.roomId)
                startActivity(intent)
            }
        }
        return view
    }

    private fun loadSpinner(spi: Spinner, context: Context) {
        val arrayAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(
                R.array.roomDiscoverFilter
            )
        )
        spi.adapter = arrayAdapter
    }

    private fun loadSpinnerCategory(spi: Spinner, context: Context, topic: ArrayList<String>) {
        val arrayAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            topic
        )
        spi.adapter = arrayAdapter
    }
}