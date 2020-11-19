package com.tpa.questapp.roomdetail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

import com.tpa.questapp.R
import com.tpa.questapp.model.QuestionRoom

class QuestionRoomListAdapter: RecyclerView.Adapter<QuestionRoomListAdapter.Companion.Holder> {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    companion object{
        class Holder : RecyclerView.ViewHolder{

            lateinit var userName: TextView
            lateinit var userImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var topicQuestion: TextView
            lateinit var question: TextView
            lateinit var imgQuestion: ImageView
            lateinit var commentCountQuestion: TextView
            lateinit var updateBtn: TextView
            lateinit var deleteBtn: TextView
            lateinit var addComment: TextView

            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.userQuestionRoomName) as TextView
                userImg = rv.findViewById(R.id.roomQuestionUserImg) as de.hdodenhof.circleimageview.CircleImageView
                topicQuestion = rv.findViewById(R.id.topicQuestionRoom) as TextView
                imgQuestion = rv.findViewById(R.id.imgPostRoom) as ImageView
                question = rv.findViewById(R.id.questionRoom) as TextView
                commentCountQuestion = rv.findViewById(R.id.commentCountQuestionRoom) as TextView
                updateBtn = rv.findViewById(R.id.updateQuestionRoomBtn) as TextView
                deleteBtn = rv.findViewById(R.id.deleteQuestionBtn) as TextView
                addComment = rv.findViewById(R.id.addCommentQPost) as TextView
            }
        }
    }

    constructor(list: ArrayList<QuestionRoom>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionRoomListAdapter.Companion.Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.question_room_list, parent,false)
        holder = Holder(rv)
        return holder
    }

    var list: ArrayList<QuestionRoom> = arrayListOf()

    lateinit var con: Context

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var at: QuestionRoom = list.get(position)
        database = Firebase.database.reference
        auth = Firebase.auth

        holder.deleteBtn.isVisible = false
        holder.updateBtn.isVisible = false

        database.child("users").child(at.userId.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                holder.userName.setText(snapshot.child("fullname").value.toString().trim())
                Picasso.get().load(snapshot.child("pictProfile").value.toString().trim()).into(holder!!.userImg)
            }

        })

        holder.topicQuestion.setText(at.questionTopic)
        holder.question.setText(at.question)
        database.child("rooms").child(at.roomId.toString()).child("questionrooms").child(at.questionRoomId.toString()).addValueEventListener( object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                holder.commentCountQuestion.setText(snapshot.child("comments").childrenCount.toString())
            }

        })
        holder.commentCountQuestion.setText("0")
        if (!(at.questionImg.equals("empty"))){
            Picasso.get().load(at.questionImg).into(holder.imgQuestion)
        }
        if(auth.uid.toString().equals(at.userId)){
            holder.updateBtn.isVisible = true
            holder.deleteBtn.isVisible = true
        }

        holder.updateBtn.setOnClickListener {
            Toast.makeText(con,"update", Toast.LENGTH_LONG).show()
            val intent = Intent(con, QuestionRoomFormActivity::class.java)
            intent.putExtra("roomId", at.roomId)
            intent.putExtra("questionId", at.questionRoomId)
            con.startActivity(intent)
        }

        holder.deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(con)
            builder.setTitle(con.resources.getString(R.string.QuestionConfirmation))
            builder.setMessage(con.resources.getString(R.string.QuestionDelete))
            builder.setPositiveButton(con.resources.getString(R.string.cancel)){dialogInterface, which -> }
            builder.setNegativeButton(con.resources.getString(R.string.ok)){dialogInterface, which ->
                database.child("rooms").child(at.roomId.toString()).child("questionrooms").child(at.questionRoomId.toString()).removeValue()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

        holder.addComment.setOnClickListener {
            val intent = Intent(con, CommentRoomQuestionActivity::class.java)
            intent.putExtra("roomId", at.roomId)
            intent.putExtra("questionId", at.questionRoomId)
            con.startActivity(intent)
        }
    }
}