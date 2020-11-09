package com.tpa.questapp.homefragment.answerpagefragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import com.tpa.questapp.model.Answer

class UserAnswerListAdapter: RecyclerView.Adapter<UserAnswerListAdapter.Companion.Holder> {
    private lateinit var database: DatabaseReference
    companion object{
        class Holder : RecyclerView.ViewHolder{

            lateinit var userName: TextView
            lateinit var userImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var userQuestion : TextView
            lateinit var answer : TextView
            lateinit var answerImg: ImageView

            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.userNameAnswerQuestion) as TextView
                userImg = rv.findViewById(R.id.userImgAnswerQuestion) as de.hdodenhof.circleimageview.CircleImageView
                userQuestion = rv.findViewById(R.id.userAnswerQuestion) as TextView
                answer = rv.findViewById(R.id.userAnswer) as TextView
                answerImg = rv.findViewById(R.id.userAnswerImage) as ImageView
            }
        }
    }

    var list: ArrayList<Answer> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<Answer>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAnswerListAdapter.Companion.Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.user_answer_list, parent,false)
        holder = Holder(rv)
        return holder
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        database = Firebase.database.reference
        var at: Answer = list[position]

        holder.answer.setText(at.answer)
        Picasso.get().load(at.media).into(holder.answerImg)

        database.child("questions").child(at.questionId.toString()).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userId = snapshot.child("userId").value.toString()
                database.child("users").child(userId).addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        holder.userName.setText(snapshot.child("fullname").value.toString())
                        Picasso.get().load(snapshot.child("pictProfile").value.toString()).into(holder.userImg)
                    }

                })
                holder.userQuestion.setText(snapshot.child("question").value.toString())
            }

        })


    }
}