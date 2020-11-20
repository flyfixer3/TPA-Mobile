package com.tpa.questapp.question
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.CommentAnswer
import com.tpa.questapp.model.CommentRoomPost
import com.tpa.questapp.model.Post
import kotlinx.android.synthetic.main.activity_comment_question_detail.*
import kotlinx.android.synthetic.main.activity_comment_room_post.*
import kotlinx.android.synthetic.main.activity_comment_room_post.addCommentForm
import kotlinx.android.synthetic.main.activity_comment_room_post.commentField
import kotlinx.android.synthetic.main.fragment_post_detail_room.view.*
import java.text.DateFormat
import java.util.*

class CommentQuestionDetailActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var CommentList: ArrayList<CommentAnswer>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment_question_detail)

        database = Firebase.database.reference
        auth = Firebase.auth

        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        commentAnswerList.layoutManager = lm
        CommentList = arrayListOf()

        val answerId:String = intent.getStringExtra("answerId").toString()
        addCommentForm.setOnClickListener {
            Toast.makeText(this, "Sucess Add Comment", Toast.LENGTH_LONG).show()
            val id = UUID.randomUUID().toString()
            val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
            val comment = CommentAnswer(id,auth.uid.toString(), commentField.text.toString(),answerId,currentDateTimeString)
            database.child("answer").child(answerId).child("comments").child(id).setValue(comment)
        }

        database.child("answer").child(answerId).child("comments").orderByChild("created").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                CommentList.clear()
                for (h in snapshot.children){
                    val commentAnswer = h.getValue(CommentAnswer::class.java)
                    CommentList.add(commentAnswer!!)
                }
                val adp = CommentAnswerListAdapter(CommentList,this@CommentQuestionDetailActivity)
                commentAnswerList.adapter = adp
            }

        })
    }
}