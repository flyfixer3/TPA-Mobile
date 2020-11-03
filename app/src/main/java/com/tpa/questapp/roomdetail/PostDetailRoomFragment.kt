package com.tpa.questapp.roomdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.Post
import com.tpa.questapp.model.Room
import kotlinx.android.synthetic.main.fragment_post_detail_room.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDetailRoomFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var postList: ArrayList<Post>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val roomId:String = activity!!.intent.getStringExtra("roomId").toString()
        Log.d("postRoomId", roomId)
        database = Firebase.database.reference

        val view = inflater.inflate(R.layout.fragment_post_detail_room, container, false)
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.postRoomList.layoutManager = layoutManager
        postList = arrayListOf()
        database.child("rooms").child(roomId).child("posts").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for (h in snapshot.children){
                    val post = h.getValue(Post::class.java)
                    Log.d("post",post.toString())
                    postList.add(post!!)
                }
                val adp = RoomPostListAdapter(postList,view.context)
                view.postRoomList.adapter = adp
            }

        })
        return view
    }
}