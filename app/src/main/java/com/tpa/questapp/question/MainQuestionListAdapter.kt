package com.tpa.questapp.question

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import com.tpa.questapp.RecycleViewClickListener
import com.tpa.questapp.model.Room
import com.tpa.questapp.model.Ticket
import com.tpa.questapp.model.User
import com.tpa.questapp.room.RoomListAdapter
import com.tpa.questapp.room.RoomMightLikeClickListener
import kotlinx.android.synthetic.main.list_post.view.*
import org.w3c.dom.Text

class MainQuestionListAdapter : RecyclerView.Adapter<MainQuestionListAdapter.Companion.Holder>{
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    var listener: RecycleViewClickListener? = null
    companion object{
        class Holder : RecyclerView.ViewHolder{
            lateinit var userName: TextView
            lateinit var profileImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var answer: TextView
            lateinit var question: TextView
            lateinit var answerCount: TextView
            lateinit var question_date: TextView
            lateinit var answer_date: TextView
            lateinit var upvote: TextView
            lateinit var downvote: TextView
            lateinit var followBtn: Button
            lateinit var answerContainer: LinearLayout

            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.txtUserName) as TextView
                profileImg = rv.findViewById(R.id.picture_path) as de.hdodenhof.circleimageview.CircleImageView
                question = rv.findViewById(R.id.txt_question) as TextView
                answer = rv.findViewById(R.id.txt_answer) as TextView
                question_date = rv.findViewById(R.id.txt_post_date) as TextView
                answer_date = rv.findViewById(R.id.txt_ans_date) as TextView
                answerCount = rv.findViewById(R.id.answerCount) as TextView
                upvote = rv.findViewById(R.id.textUpvote) as TextView
                downvote = rv.findViewById(R.id.textDownvote) as TextView
                followBtn = rv.findViewById(R.id.followHomeBtn) as Button
                answerContainer = rv.findViewById(R.id.answerContainer) as LinearLayout
            }
        }
    }

    var list: ArrayList<Ticket> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<Ticket>, con: Context) : super() {
        this.list = list
        this.con = con
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.list_post, parent,false)
        holder = Holder(rv)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        var at: Ticket
        at = list.get(position)
        holder.itemView.setOnClickListener{
            listener?.onItemClicked(it,at)
        }
        database.child("users").child(at.userId.toString()).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Picasso.get().load(snapshot.child("pictProfile").value.toString().trim()).into(holder!!.profileImg)
                holder.userName.setText(snapshot.child("fullname").value.toString().trim())
            }

        })
        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.child("users").child(auth.uid.toString()).child("following").children){
                    if(h.value.toString().equals(at.userId.toString())){
                        holder.followBtn.setText("Unfollow")
                    }
                }
            }
        })
        database.child("questions").child(at.questionId.toString()).child("answers").orderByChild("upvote").limitToFirst(1).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    holder.answer_date.setText(snapshot.child("answerDate").value.toString().trim())
                    holder.answer.setText(snapshot.child("answer").value.toString().trim())
                    holder.downvote.setText(snapshot.child("downvote").value.toString().trim())
                    holder.upvote.setText(snapshot.child("upvote").value.toString().trim())
                }else{
                    holder.answerContainer.isVisible = false
                }
            }
        })
        database.child("questions").child(at.questionId.toString()).child("answers").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    holder.answerCount.setText(snapshot.childrenCount.toString() + " Answers")
                }else{
                    holder.answerCount.setText("0 Answers")
                }
            }
        })

        holder!!.question_date.setText(at.createdDate)
        holder!!.question.setText(at.question)

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
    }


}