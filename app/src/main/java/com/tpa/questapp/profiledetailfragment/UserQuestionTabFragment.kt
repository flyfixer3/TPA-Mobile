package com.tpa.questapp.profiledetailfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase.*
import com.tpa.questapp.R
import com.tpa.questapp.model.Question
import com.tpa.questapp.question.UserQuestionListAdapter
import kotlinx.android.synthetic.main.fragment_user_question_tab.view.*

class UserQuestionTabFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var questionList: ArrayList<Question>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        database = getInstance().getReference("users").child(auth.uid.toString()).child("questions")
        questionList = arrayListOf()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_question_tab, container, false)
        view.userQuestionListView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)

        view.noQuestionUserImg.isVisible = false
        view.noQuestionUserTxt.isVisible = false

        database.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                questionList.clear()
                for (h in snapshot.children){
                    val question = h.getValue(Question::class.java)
                    questionList.add(question!!)
                }
                view.userQuestionListView.adapter = UserQuestionListAdapter(questionList, view.context)
                if (questionList.isEmpty()){
                    view.noQuestionUserImg.isVisible = true
                    view.noQuestionUserTxt.isVisible = true
                }
            }

        })
        return view
    }

}