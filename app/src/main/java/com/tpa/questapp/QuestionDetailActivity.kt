package com.tpa.questapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.tpa.questapp.model.Ticket
import com.tpa.questapp.question.AnswerQuestionListAdapter
import com.tpa.questapp.question.MainQuestionListAdapter
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.activity_view_all_room.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class QuestionDetailActivity : AppCompatActivity(), RecycleViewClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
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

        txt_question.text = ticket.question
        txt_question_date.text = ticket.createdDate


        database.child("questions").child("answers").orderByChild("upvote").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){

//                    var td = snapshot!!.value as HashMap<String,Any>
//                    for(key in td.keys){
//                        var post = td[key] as HashMap<String,Any>
//                        list?.add(
//                            Ticket(key,post["userId"] as String, post["question"] as String ,post["topic"] as String,post["createdDate"] as String)
//                        )
//                    }

                    for (h in snapshot.children){

                        list.add(Answer(h.key,
                            h.child("userId").value.toString(),
                            h.child("answer").value.toString(),
                            h.child("media").value.toString(),
                            Integer.parseInt(h.child("upvote").value.toString()),
                            Integer.parseInt(h.child("downvote").value.toString())
                        ))
                    }
                    rv_list_answer.adapter = AnswerQuestionListAdapter(list, this@QuestionDetailActivity)
                }
            }

        })
    }

    override fun onItemClicked(view: View, ticket: Ticket) {

    }


}