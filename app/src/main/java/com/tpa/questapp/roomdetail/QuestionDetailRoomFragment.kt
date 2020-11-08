package com.tpa.questapp.roomdetail

import android.content.Intent
import android.os.Bundle
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
import com.tpa.questapp.model.QuestionRoom
import com.tpa.questapp.model.Room
import kotlinx.android.synthetic.main.fragment_question_detail_room.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionDetailRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionDetailRoomFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var questionList: ArrayList<QuestionRoom>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val roomId:String = activity!!.intent.getStringExtra("roomId").toString()
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_question_detail_room, container, false)

        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        view.questionRoomList.layoutManager = layoutManager
        questionList = arrayListOf()

        view.addQuestionRoomDetailBtn.isVisible = false
        view.noQuestionImg.isVisible = false
        view.noQuestionTxt.isVisible = false

        view.addQuestionRoomDetailBtn.setOnClickListener {
            val intent = Intent(view.context, QuestionRoomFormActivity::class.java)
            intent.putExtra("roomId", roomId)
            intent.putExtra("questionId","add")
            startActivity(intent)
        }

        database.child("rooms").child(roomId).child("questionrooms").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                questionList.clear()
                for (h in snapshot.children){
                    val questionRoom = h.getValue(QuestionRoom::class.java)
                    questionList.add(questionRoom!!)
                }
                if (questionList.isEmpty()){
                    view.noQuestionImg.isVisible = true
                    view.noQuestionTxt.isVisible = true
                }
                val adp = QuestionRoomListAdapter(questionList,view.context)
                view.questionRoomList.adapter = adp
            }

        })

        database.child("users").child(auth.uid.toString()).child("followrooms").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                view.addQuestionRoomDetailBtn.isVisible = false
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    val room = h.getValue(Room::class.java)
                    if(room!!.roomId.equals(roomId)){
                        view.addQuestionRoomDetailBtn.isVisible = true
                    }
                }
            }

        })

        database.child("rooms").child(roomId).addValueEventListener( object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("userId").value.toString().equals(auth.uid.toString())){
                    view.addQuestionRoomDetailBtn.isVisible = true
                }
            }

        })

        return view
    }
}