package com.tpa.questapp.homefragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.QuestionDetailActivity
import com.tpa.questapp.QuestionFormActivity
import com.tpa.questapp.R
import com.tpa.questapp.RecycleViewClickListener
import com.tpa.questapp.model.Room
import com.tpa.questapp.model.Ticket
import com.tpa.questapp.question.MainQuestionListAdapter
import com.tpa.questapp.room.RoomFormActivity
import com.tpa.questapp.room.RoomListAdapter
import com.tpa.questapp.roomdetail.RoomDetailActivity
import kotlinx.android.synthetic.main.activity_view_all_room.*
import kotlinx.android.synthetic.main.fragment_discover.view.*
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
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.rv_list_post.layoutManager = layoutManager
        view.rv_list_post.setHasFixedSize(true)
        database.child("questions").addValueEventListener(object: ValueEventListener, RecycleViewClickListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (h in snapshot.children){
                        list.add(Ticket(h.key,h.child("userId").value.toString(), h.child("question").value.toString(),h.child("topic").value.toString(), h.child("createdDate").value.toString())!!)
                    }
                    val adapter = MainQuestionListAdapter(list, view.context)
                    view.rv_list_post.adapter = adapter
                    adapter.listener = this
                }
            }

            override fun onItemClicked(view: View, ticket: Ticket) {
                Toast.makeText(view.context,
                    "Question ${ticket.questionId} berhasil di klik",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(view.context, QuestionDetailActivity::class.java)
                intent.putExtra("ticket", ticket)
                startActivity(intent)

            }

        })


        return view
    }
}