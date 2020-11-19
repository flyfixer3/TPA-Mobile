package com.tpa.questapp.profiledetailfragment.usertopic

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.Question
import com.tpa.questapp.question.UserQuestionListAdapter

class UserTopicAdapter: RecyclerView.Adapter<UserTopicAdapter.Companion.Holder> {
    companion object{
        class Holder : RecyclerView.ViewHolder{

            lateinit var topic: TextView

            constructor(rv: View) : super(rv){
                topic = rv.findViewById(R.id.topicListTxt) as TextView
            }
        }
    }

    var list: ArrayList<String> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<String>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserTopicAdapter.Companion.Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.topic_list, parent,false)
        holder = Holder(rv)
        return holder
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var at = list[position]
        holder.topic.setText(at)
    }
}