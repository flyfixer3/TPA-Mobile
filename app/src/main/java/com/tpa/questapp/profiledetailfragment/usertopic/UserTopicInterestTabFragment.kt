package com.tpa.questapp.profiledetailfragment.usertopic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tpa.questapp.R
import com.tpa.questapp.followfragment.ListFollowAdapter
import com.tpa.questapp.followfragment.ListFollowingAdapter
import com.tpa.questapp.model.User
import kotlinx.android.synthetic.main.fragment_user_follower_tab.view.*
import kotlinx.android.synthetic.main.fragment_user_following_tab.view.*
import kotlinx.android.synthetic.main.fragment_user_topic_interest_tab.view.*

class UserTopicInterestTabFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_topic_interest_tab, container, false)
        val topics = arrayListOf<String>()
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        database = FirebaseDatabase.getInstance().getReference("users").child(userId).child("listTopic")

        database.addValueEventListener(object  : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                topics.clear()
                if(p0.exists()){
                    for (h in p0.children){
                        val topic = h.getValue().toString()
                        topics.add(topic!!)
                    }
                    val adapter : ArrayAdapter<String> = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, topics)
                    view.topicInterestListView.adapter = adapter
                }
            }

        })

        return view
    }
}