package com.tpa.questapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.model.Question
import kotlinx.android.synthetic.main.activity_question_form.*
import java.text.DateFormat
import java.util.*

class QuestionFormActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_form)
        database = Firebase.database.reference
        auth = Firebase.auth
        init()
    }

    private fun init() {
        loadTopic()
        createQuestionButton.setOnClickListener {
            val question = QuestionFormField!!.text.toString().trim { it <= ' ' }
            val topic = topicQuestionFormSpinner.selectedItem.toString()
            writeQuestion(auth.uid.toString(),question,topic)
            Toast.makeText(this, "Success Add Question", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadTopic() {
        ArrayAdapter.createFromResource(
            this,
            R.array.ComputerScience,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            topicQuestionFormSpinner.adapter = adapter
        }
    }
    private fun writeQuestion(userId: String, question: String, topic: String){
        val questionDate = DateFormat.getDateTimeInstance().format(Date())
        val questionId = UUID.randomUUID().toString()
        val question = Question(questionId,userId, question, topic,questionDate)


        database.child("questions").child(questionId.toString()).setValue(question)
        database.child("users").child(userId).child("questions").child(questionId.toString()).setValue(question)
    }
}