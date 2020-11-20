package com.tpa.questapp.question

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.Question
import com.tpa.questapp.room.RoomCreatedListAdapter
import kotlinx.android.synthetic.main.user_question_list.view.*

class UserQuestionListAdapter: RecyclerView.Adapter<UserQuestionListAdapter.Companion.Holder>{
    companion object{
        class Holder : RecyclerView.ViewHolder{
            lateinit var userName: TextView
            lateinit var profileImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var question: TextView
            lateinit var answerCount: TextView
            lateinit var question_date: TextView


            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.txtUserName) as TextView
                profileImg = rv.findViewById(R.id.picture_path) as de.hdodenhof.circleimageview.CircleImageView
                question = rv.findViewById(R.id.questionUserListTxt) as TextView
                answerCount =rv.findViewById(R.id.countQuestionUserListTxt) as TextView
                question_date =rv.findViewById(R.id.txt_question_date) as TextView
            }
        }
    }
    private lateinit var database: DatabaseReference

    var list: ArrayList<Question> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<Question>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserQuestionListAdapter.Companion.Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.user_question_list, parent,false)
        holder = Holder(rv)
        return holder
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        database = Firebase.database.reference
        var at: Question = list[position]
        holder.question.setText(at.question)
        holder.question_date.setText(at.createdDate)
        database.child("users").child(at.userId.toString()).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Picasso.get().load(snapshot.child("pictProfile").value.toString().trim()).into(holder!!.profileImg)
                holder.userName.setText(snapshot.child("fullname").value.toString().trim())
            }

        })
        database.child("questions").child(at.questionId.toString()).child("answers").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                holder.answerCount.setText(snapshot.childrenCount.toString())
            }

        })

    }
}