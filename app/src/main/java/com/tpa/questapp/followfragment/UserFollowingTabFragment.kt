package com.tpa.questapp.followfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tpa.questapp.R
import com.tpa.questapp.model.User
import kotlinx.android.synthetic.main.fragment_user_follower_tab.view.*
import kotlinx.android.synthetic.main.fragment_user_following_tab.view.*

class UserFollowingTabFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var userList: ArrayList<String>
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users").child(auth.uid.toString()).child("following")
        val allDbUser = FirebaseDatabase.getInstance().getReference("users")
        val view =  inflater.inflate(R.layout.fragment_user_following_tab, container, false)
        userList = arrayListOf()

        database.addValueEventListener(object  : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                userList.clear()
                if(p0.exists()){
                    for (h in p0.children){
                        val user = h.key.toString()
                        userList.add(user)
                    }
//
                }
            }

        })
        val allUserList = arrayListOf<User>()
        allDbUser.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                allUserList.clear()
                for (h in snapshot.children){
                    for (d in userList) {
                        if (h.key.equals(d)) {
                            val user = h.getValue(User::class.java)
                            allUserList.add(user!!)
                        }
                    }
                }
                view.followingListView.adapter = ListFollowingAdapter(view.context, R.layout.list_following, allUserList)
            }
        })

        return view
    }
}