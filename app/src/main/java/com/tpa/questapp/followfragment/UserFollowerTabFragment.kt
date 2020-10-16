package com.tpa.questapp.followfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*
import com.tpa.questapp.model.User
import com.tpa.questapp.R
import kotlinx.android.synthetic.main.fragment_user_follower_tab.view.*

class UserFollowerTabFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var userList: ArrayList<User>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        database = FirebaseDatabase.getInstance().getReference("users")
        val view =  inflater.inflate(R.layout.fragment_user_follower_tab, container, false)
        userList = arrayListOf()

        database.addValueEventListener(object  : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                userList.clear()
                if(p0.exists()){
                    for (h in p0.children){
                        val user = h.getValue(User::class.java)
                        userList.add(user!!)
                    }
                    view.followerListView.adapter = ListFollowAdapter(view.context, R.layout.list_follower, userList)
                }
            }

        })

        return view
    }
}