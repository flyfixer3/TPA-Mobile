package com.tpa.questapp.search

import android.R.attr.data
import android.os.Bundle
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tpa.questapp.R
import com.tpa.questapp.model.User
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userList: ArrayList<User>
    private lateinit var userSearchList: ArrayList<User>
    private lateinit var userKey: ArrayList<String>
    private lateinit var userKeySearch: ArrayList<String>
    private lateinit var topicSearchList: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
    }

    private fun init() {
        search_box.setIconifiedByDefault(false)
//        list_user_search.isScrollContainer = false;
//        list_topic_search.isScrollContainer = false;
        database = FirebaseDatabase.getInstance().getReference("users")
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        userList = arrayListOf()
        userSearchList = arrayListOf()
        topicSearchList = arrayListOf()
        userKey = arrayListOf()
        userKeySearch = arrayListOf()
        val adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1, resources.getStringArray(
            R.array.ComputerScience
        ))
        list_topic_search.adapter = adapter
        setDynamicHeight(list_topic_search)

        database.addValueEventListener(object  : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                userList.clear()
                userKey.clear()
                if(p0.exists()){
                    for (h in p0.children){
                        val user = h.getValue(User::class.java)
                        userKey.add(h.key!!)
                        userList.add(user!!)
                    }
                    list_user_search.adapter = UserSearchListAdapter(applicationContext,
                        R.layout.search_user_list, userList)
                }
            }

        })
        setDynamicHeight(list_user_search)
        search_box.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(p0: String): Boolean {
                searchUser(p0)
                searchTopic(p0)
                return false
            }

            override fun onQueryTextChange(p0: String): Boolean {
                searchUser(p0)
                searchTopic(p0)
                return false
            }
        })
        list_user_search.setOnItemClickListener { parent, view, position, id ->
            val element = userKey[position] // The item that was clicked
            Toast.makeText(this, element,
                Toast.LENGTH_SHORT
            ).show()
//            val intent = Intent(this, BookDetailActivity::class.java)
//            startActivity(intent)
        }

    }

    private fun searchUser(p0: String) {
        userSearchList.clear()
        userKeySearch.clear()
        for(h in userList){
            if(h.fullname!!.toLowerCase().contains(p0.toLowerCase())){
                userSearchList.add(h)
                val pos = userList.indexOf(h)
                userKeySearch.add(userKey[pos])
            }
        }
        list_user_search.adapter = UserSearchListAdapter(applicationContext,
            R.layout.search_user_list, userSearchList)
        list_user_search.setOnItemClickListener { parent, view, position, id ->
            val element = userKeySearch[position] // The item that was clicked
            Toast.makeText(this, element,
                Toast.LENGTH_SHORT
            ).show()
//            val intent = Intent(this, BookDetailActivity::class.java)
//            startActivity(intent)
        }

    }

    private fun searchTopic(p0: String){
        topicSearchList.clear()
        for(i in resources.getStringArray(
            R.array.ComputerScience
        )){
            if(i.toLowerCase().contains(p0.toLowerCase())){
                topicSearchList.add(i)
            }
        }
        list_topic_search.adapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_list_item_1, topicSearchList)
    }

    fun setDynamicHeight(listView: ListView) {
        val adapter: ListAdapter = listView.getAdapter() ?: return
        //check adapter if null
        var height = 0
        val desiredWidth =
            MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED)
        for (i in 0 until adapter.getCount()) {
            val listItem: View = adapter.getView(i, null, listView)
            listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED)
            height += listItem.getMeasuredHeight()
        }
        val layoutParams: ViewGroup.LayoutParams = listView.getLayoutParams()
        layoutParams.height = height + listView.getDividerHeight() * (adapter.getCount() - 1)
        listView.setLayoutParams(layoutParams)
        listView.requestLayout()
    }
}