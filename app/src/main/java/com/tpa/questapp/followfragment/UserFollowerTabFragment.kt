package com.tpa.questapp.followfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.model.User
import com.tpa.questapp.R
import kotlinx.android.synthetic.main.fragment_user_follower_tab.view.*

class UserFollowerTabFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var followerList: ArrayList<String>
    private lateinit var userList: ArrayList<User>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        val view =  inflater.inflate(R.layout.fragment_user_follower_tab, container, false)

        view.noFollowerImg.isVisible = false
        view.noFollowerTxt.isVisible = false

        view.followerListView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        followerList = arrayListOf()
        userList = arrayListOf()

        database.child("users").child(auth.uid.toString()).child("followers").addValueEventListener(object  : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                followerList.clear()
                if(p0.exists()){
                    for (h in p0.children){
                        val user = h.key.toString()
                        followerList.add(user)
                    }
                }
                if(followerList.isEmpty()){
                    view.noFollowerImg.isVisible = true
                    view.noFollowerTxt.isVisible = true
                }
            }

        })
        database.child("users").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (h in snapshot.children){
                    for (d in followerList) {
                        if (h.key.equals(d)) {
                            val user = h.getValue(User::class.java)
                            userList.add(user!!)
                        }
                    }
                }
                view.followerListView.adapter = ListFollowerAdapter(userList,view.context)
            }
        })

        return view
    }
}