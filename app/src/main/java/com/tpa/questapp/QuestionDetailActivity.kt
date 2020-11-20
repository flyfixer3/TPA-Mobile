package com.tpa.questapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.model.Answer
import com.tpa.questapp.model.QuestionRoom
import com.tpa.questapp.model.Room
import com.tpa.questapp.model.Ticket
import com.tpa.questapp.question.AnswerQuestionFormActivity
import com.tpa.questapp.question.AnswerQuestionListAdapter
import com.tpa.questapp.question.MainQuestionListAdapter
import com.tpa.questapp.roomdetail.PostRoomFormActivity
import com.tpa.questapp.roomdetail.QuestionRoomListAdapter
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.activity_question_detail.picture_path
import kotlinx.android.synthetic.main.activity_question_detail.txtUserName
import kotlinx.android.synthetic.main.activity_question_detail.txt_question


class   QuestionDetailActivity : AppCompatActivity(), RecycleViewClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        loadData()
    }
    fun loadData(){
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        var list: ArrayList<Answer> = ArrayList()
        val layoutManager = LinearLayoutManager(this@QuestionDetailActivity, LinearLayoutManager.VERTICAL, false )
        rv_list_answer.layoutManager = layoutManager
        rv_list_answer.setHasFixedSize(true)
        var ticket: Ticket? = intent.getParcelableExtra<Ticket>("ticket")
        database.child("users").child(ticket!!.userId.toString().trim()).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                txtUserName.text = snapshot.child("fullname").value.toString().trim()
                Picasso.get().load(snapshot.child("pictProfile").value.toString().trim()).into(picture_path)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        txt_question.text = ticket!!.question
        txt_question_date.text = ticket!!.createdDate


        database.child("questions").child(ticket.questionId.toString()).child("answers").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    list.clear()
                    for (h in snapshot.children){
                        list.add(Answer(h.key,
                            h.child("questionId").value.toString(),
                            h.child("userId").value.toString(),
                            h.child("answer").value.toString(),
                            h.child("media").value.toString(),
                            h.child("upvote").value.toString().toInt(),
                            h.child("downvote").value.toString().toInt()
                        ))
                    }
                    list.sortWith(object: Comparator<Answer>{
                        override fun compare(p1: Answer, p2: Answer): Int = when {
                            (p1.upvote!! - p1.downvote!!) < (p2.upvote!! - p2.downvote!!) -> 1
                            (p1.upvote!! - p1.downvote!!) == (p2.upvote!! - p2.downvote!!) -> 0
                            else -> -1
                        }
                    })
                    noAnsText.isVisible = list.isEmpty()
                    noAnswerImage.isVisible = list.isEmpty()
                    if(list.isNotEmpty()){
                        database.child("users").child(list.get(0).userId.toString().trim()).addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                bestUser.text = snapshot.child("fullname").value.toString().trim()
                                Picasso.get().load(snapshot.child("pictProfile").value.toString().trim()).into(picture_path)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
//                        Picasso.get().load(list.get(0).media).into(imageContainer)
                        best_answer.text = list.get(0).answer.toString()
                    }
                    rv_list_answer.adapter = AnswerQuestionListAdapter(list, this@QuestionDetailActivity)
                }
            }

        })
        addAnswerQuestion.setOnClickListener{
            val intent = Intent(it.context, AnswerQuestionFormActivity::class.java)
            intent.putExtra("questionId", ticket.questionId)
            startActivity(intent)
        }

    }

    override fun onItemClicked(view: View, ticket: Ticket) {

    }


}