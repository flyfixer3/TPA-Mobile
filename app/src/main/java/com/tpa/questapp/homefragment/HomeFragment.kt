package com.tpa.questapp.homefragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.QuestionFormActivity
import com.tpa.questapp.R
import com.tpa.questapp.model.Ticket
import com.tpa.questapp.question.MainQuestionListAdapter
import com.tpa.questapp.room.RoomFormActivity
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        view.addQuestionButton.setOnClickListener {
            startActivity(
                Intent(
                    this.activity,
                    QuestionFormActivity::class.java
                )
            )
        }
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        var list: ArrayList<Ticket> = ArrayList()
        database.child("question").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var td = snapshot!!.value as HashMap<String,Any>
                    for(key in td.keys){
                        var post = td[key] as HashMap<String,Any>
                        list?.add(
                            Ticket(key,post["userId"] as String, post["question"] as String ,post["topic"] as String,post["createdDate"] as String)
                        )
                    }
                }
            }

        })

        view.rv_list_post.adapter = MainQuestionListAdapter(list, view.context)
        return view
    }
}