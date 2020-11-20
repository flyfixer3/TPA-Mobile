package com.tpa.questapp.question

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.Answer
import com.tpa.questapp.model.Ticket
import com.tpa.questapp.roomdetail.CommentRoomQuestionActivity
import com.tpa.questapp.roomdetail.QuestionRoomFormActivity

class AnswerQuestionListAdapter : RecyclerView.Adapter<AnswerQuestionListAdapter.Companion.Holder>{
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    companion object{
        class Holder : RecyclerView.ViewHolder{
            lateinit var userName: TextView
            lateinit var profileImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var answer: TextView
            lateinit var upvote: TextView
            lateinit var downvote: TextView
            lateinit var followBtn: Button
            lateinit var answerContainer: RelativeLayout
            lateinit var upvoteBtn: ImageButton
            lateinit var downvoteBtn: ImageButton
            lateinit var media: ImageView
            lateinit var commentCountAnswer: TextView
            lateinit var addComment: TextView

            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.txtUserName) as TextView
                profileImg = rv.findViewById(R.id.picture_path) as de.hdodenhof.circleimageview.CircleImageView
                answer = rv.findViewById(R.id.txt_answer) as TextView
                upvote = rv.findViewById(R.id.textUpvote) as TextView
                downvote = rv.findViewById(R.id.textDownvote) as TextView
                upvoteBtn = rv.findViewById(R.id.iv_upvote) as ImageButton
                downvoteBtn = rv.findViewById(R.id.iv_downvote) as ImageButton
                followBtn = rv.findViewById(R.id.followHomeBtn) as Button
                answerContainer = rv.findViewById(R.id.answerContainer) as RelativeLayout
                media = rv.findViewById(R.id.imageContainer) as ImageView
                commentCountAnswer = rv.findViewById(R.id.commentCount) as TextView
                addComment = rv.findViewById(R.id.addCommentAnswer) as TextView
            }
        }
    }

    var list: ArrayList<Answer> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<Answer>, con: Context) : super() {
        this.list = list
        this.con = con
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var rv: View
        var holder: AnswerQuestionListAdapter.Companion.Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.list_answer, parent,false)
        holder = AnswerQuestionListAdapter.Companion.Holder(rv)
        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        var at: Answer
        at = list.get(position)
        database.child("users").child(at.userId.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Picasso.get().load(snapshot.child("pictProfile").value.toString().trim()).into(holder!!.profileImg)
                holder.userName.setText(snapshot.child("fullname").value.toString().trim())
            }
        })
        database.child("answer").child(at.answerId.toString()).addValueEventListener( object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                holder.commentCountAnswer.setText(snapshot.child("comments").childrenCount.toString())
            }

        })
        holder.commentCountAnswer.setText("0")
        holder.addComment.setOnClickListener {
            val intent = Intent(con, CommentQuestionDetailActivity::class.java)
            intent.putExtra("answerId", at.answerId)
            con.startActivity(intent)
        }
        Picasso.get().load(at.media.toString()).into(holder!!.media)
        holder.answer.setText(at.answer)
        holder.downvote.setText(at.downvote.toString())
        holder.upvote.setText(at.upvote.toString())
        if (at.userId.equals(auth.uid.toString())){
            holder!!.followBtn.isVisible = false
        }

        var keyFollowRooms: String = ""
        holder!!.followBtn.setOnClickListener {
            if(holder.followBtn.text.equals("Follow")){
                holder.followBtn.setText("Unfollow")
                database.child("users").child(auth.uid.toString()).child("following").child(at.userId.toString()).setValue(at.userId.toString())
                database.child("users").child(at.userId.toString()).child("followers").child(auth.uid.toString()).setValue(auth.uid.toString())
            }else{
                holder.followBtn.setText("Follow")
                database.child("users").child(auth.uid.toString()).child("following").child(at.userId.toString()).removeValue()
                database.child("users").child(at.userId.toString()).child("followers").child(auth.uid.toString()).removeValue()
            }
        }
        holder.upvoteBtn.setOnClickListener{
            database.child("answer").child(at.answerId.toString()).child("upvote").setValue(at.upvote!!.plus(1))
            database.child("questions").child(at.questionId.toString()).child("answers").child(at.answerId.toString()).child("upvote").setValue(at.upvote!!.plus(1))
        }
        holder.downvoteBtn.setOnClickListener{
            database.child("answer").child(at.answerId.toString()).child("downvote").setValue(at.downvote!!.plus(1))
            database.child("questions").child(at.questionId.toString()).child("answers").child(at.answerId.toString()).child("downvote").setValue(at.downvote!!.plus(1))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}