package com.tpa.questapp.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.google.firebase.database.*
import com.tpa.questapp.R
import com.tpa.questapp.model.User
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var userList: ArrayList<User>
    private lateinit var userSearchList: ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
    }

    private fun init() {
        search_box.setIconifiedByDefault(false)

        database = FirebaseDatabase.getInstance().getReference("users")
        userList = arrayListOf()
        userSearchList = arrayListOf()

        database.addValueEventListener(object  : ValueEventListener {
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
                    list_item_search.adapter = UserSearchListAdapter(applicationContext,
                        R.layout.search_user_list, userList)
                }
            }

        })


        search_box.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(p0: String): Boolean {
                search(p0)
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean {
                search(p0)
                return false
            }
        })
    }

    private fun search(p0: String) {
        userSearchList.clear()
        for(h in userList){
            if(h.fullname!!.toLowerCase().contains(p0.toLowerCase())){
                userSearchList.add(h)
                Log.d("123",userSearchList.size.toString())
            }
        }
        list_item_search.adapter = UserSearchListAdapter(applicationContext,
            R.layout.search_user_list, userSearchList)
    }
}