package com.tpa.questapp.roomdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.parser.IntegerParser
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
import kotlinx.android.synthetic.main.activity_register_major.*
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
        loadSpinner(view.filterQuestionRoom, view.context)
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

        view.filterQuestionRoom.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if( view.filterQuestionRoom.selectedItemPosition == 0){
                    questionList.sortWith(object: Comparator<QuestionRoom>{
                        override fun compare(p1: QuestionRoom, p2: QuestionRoom): Int = when {
                            p1.questionRoomId!! < p2.questionRoomId!! -> 1
                            p1.questionRoomId == p2.questionRoomId -> 0
                            else -> -1
                        }
                    })
                    val adp = QuestionRoomListAdapter(questionList,view.context)
                    view.questionRoomList.adapter = adp
                }else if( view.filterQuestionRoom.selectedItemPosition == 1){
                    questionList.sortWith(object: Comparator<QuestionRoom>{
                    override fun compare(p1: QuestionRoom, p2: QuestionRoom): Int = when {
                        p1.questionDate!! < p2.questionDate!! -> 1
                        p1.questionDate == p2.questionDate -> 0
                        else -> -1
                        }
                    })
                    val adp = QuestionRoomListAdapter(questionList,view.context)
                    view.questionRoomList.adapter = adp
                }else if( view.filterQuestionRoom.selectedItemPosition == 2){
                    database.child("rooms").child(roomId).child("questionrooms").addValueEventListener(object :
                        ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            questionList.clear()
                            for (h in snapshot.children){
                                val questionRoom = h.getValue(QuestionRoom::class.java)
                                var ca: Int = h.child("comments").childrenCount.toInt()
                                if (ca == 0){
                                    questionList.add(questionRoom!!)
                                }
                            }
                            if (questionList.isEmpty()){
                                view.noQuestionImg.isVisible = true
                                view.noQuestionTxt.isVisible = true
                            }
                            val adp = QuestionRoomListAdapter(questionList,view.context)
                            view.questionRoomList.adapter = adp
                        }

                    })
                }
            }
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
                    view.noQuestionImg.isVisible = questionList.isEmpty()
                    view.noQuestionTxt.isVisible = questionList.isEmpty()
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

    private fun loadSpinner(spi: Spinner, context: Context) {
        val arrayAdapter = ArrayAdapter(
            context,
            android.R.layout.simple_list_item_1,
            resources.getStringArray(
                R.array.questionRoomFilter
            )
        )
        spi.adapter = arrayAdapter
    }
}