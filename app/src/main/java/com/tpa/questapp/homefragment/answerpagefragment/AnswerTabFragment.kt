package com.tpa.questapp.homefragment.answerpagefragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.R
import com.tpa.questapp.model.Answer
import com.tpa.questapp.model.CommentRoomPost
import kotlinx.android.synthetic.main.fragment_answer_tab.view.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [AnswerTabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnswerTabFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var AnswerList: ArrayList<Answer>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        val view = inflater.inflate(R.layout.fragment_answer_tab, container, false)
        AnswerList = arrayListOf()
        view.noAnswerUserImg.isVisible = false
        view.noAnswerUserTxt.isVisible = false

        view.userAnswerListView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        database.child("answer").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (j in snapshot.children){
                    val answer: Answer? = j.getValue(Answer::class.java)
                    Log.d("amnswer",answer!!.userId.toString())
                    if (answer.userId.equals(auth.uid.toString())){
                        AnswerList.add(answer)
                    }
                }
                view.userAnswerListView.adapter = UserAnswerListAdapter(AnswerList, view.context)
                if(AnswerList.isEmpty()){
                    view.noAnswerUserImg.isVisible = true
                    view.noAnswerUserTxt.isVisible = true
                }
            }

        })
        return view
    }

}