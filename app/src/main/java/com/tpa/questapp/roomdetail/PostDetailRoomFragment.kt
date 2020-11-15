package com.tpa.questapp.roomdetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.Post
import com.tpa.questapp.model.Room
import kotlinx.android.synthetic.main.fragment_post_detail_room.*
import kotlinx.android.synthetic.main.fragment_post_detail_room.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDetailRoomFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var postList: ArrayList<Post>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val roomId:String = activity!!.intent.getStringExtra("roomId").toString()
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.fragment_post_detail_room, container, false)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.postRoomList.layoutManager = layoutManager
        postList = arrayListOf()

        view.addPostRoomDetailBtn.isVisible = false
        view.noPostTxt.isVisible = false
        view.noPostImg.isVisible = false

        database.child("users").child(auth.uid.toString()).child("followrooms").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                view.addPostRoomDetailBtn.isVisible = false
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    val room = h.getValue(Room::class.java)
                    if(room!!.roomId.equals(roomId)){
                        view.addPostRoomDetailBtn.isVisible = true
                    }
                }
            }

        })

        database.child("rooms").child(roomId).addValueEventListener( object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("userId").value.toString().equals(auth.uid.toString())){
                    view.addPostRoomDetailBtn.isVisible = true
                }
            }

        })

        database.child("rooms").child(roomId).child("posts").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for (h in snapshot.children){
                    val post = h.getValue(Post::class.java)
                    postList.add(post!!)
                }
                    view.noPostTxt.isVisible = postList.isEmpty()
                    view.noPostImg.isVisible = postList.isEmpty()
                val adp = RoomPostListAdapter(postList,view.context)
                view.postRoomList.adapter = adp
            }

        })
        view.addPostRoomDetailBtn.setOnClickListener {
            val intent = Intent(view.context, PostRoomFormActivity::class.java)
            intent.putExtra("roomId", roomId)
            intent.putExtra("postId","add")
            startActivity(intent)
        }
        return view
    }
}