package com.tpa.questapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tpa.questapp.model.User
import kotlinx.android.synthetic.main.activity_register_major.*

class RegisterMajor : AppCompatActivity() {
    private var arrayAdapter: ArrayAdapter<String> ? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_major)
        init()
        auth = Firebase.auth
        database = Firebase.database.reference
    }
    private fun init() {
        loadMajor()
        majorSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(majorSpinner.selectedItem.toString() == "Computer Science"){
                    loadTopicCS()
                }else if(majorSpinner.selectedItem.toString() == "Information Systems"){
                    loadTopicIS()
                }else if(majorSpinner.selectedItem.toString() == "Accounting"){
                    loadTopicAC()
                }else if(majorSpinner.selectedItem.toString() == "Management"){
                    loadTopicMN()
                }else{
                    topicListView.adapter = null
                }
            }
        }
        submitButton.isClickable = false
        submitButton.isEnabled = false
        submitButton.setBackgroundColor(Color.parseColor("#FFFFFF"))
        topicListView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                var checked: Int = 0
                submitButton.isClickable = false
                submitButton.isEnabled = false
                submitButton.setBackgroundColor(Color.parseColor("red"))
                val sparseBooleanArray: SparseBooleanArray = topicListView.checkedItemPositions

                for (i in 0 until topicListView.count) {
                    if (sparseBooleanArray.get(i)) {
                        checked += 1
                    }
                }
                if (checked >= 3) {
                    submitButton.isClickable = true
                    submitButton.isEnabled = true
                    submitButton.setBackgroundColor(Color.parseColor("black"))
                }
            }
        submitButton.setOnClickListener{
            val user = auth.currentUser
            val sparseBooleanArray: SparseBooleanArray = topicListView.checkedItemPositions
            val listTopic = mutableListOf<String>()
            for (i in 0 until topicListView.count) {
                if(sparseBooleanArray.get(i)){
                    listTopic.add(topicListView.getItemAtPosition(i) as String)
                }
            }
            var registerUser = intent.getParcelableExtra<User>("User")
            writeNewUser(registerUser!!.pictProfile.toString(),
                user!!.uid.toString(),
            registerUser.fullname.toString(),
            registerUser.gender.toString(),
            registerUser.job.toString(),
            registerUser.location.toString(),
            majorSpinner.selectedItem.toString(),
            listTopic)

            startActivity(Intent(this, HomeActivity::class.java))


        }
    }


    private fun loadMajor() {
        ArrayAdapter.createFromResource(
            this,
            R.array.majors_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            majorSpinner.adapter = adapter
        }
    }

    private fun loadTopicCS(){
        arrayAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_multiple_choice,
            resources.getStringArray(
                R.array.ComputerScience
            )
        )
        topicListView?.adapter = arrayAdapter
        topicListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun loadTopicIS(){
        arrayAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_multiple_choice,
            resources.getStringArray(
                R.array.InformationSystems
            )
        )
        topicListView?.adapter = arrayAdapter
        topicListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun loadTopicAC(){
        arrayAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_multiple_choice,
            resources.getStringArray(
                R.array.Accounting
            )
        )
        topicListView?.adapter = arrayAdapter
        topicListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun loadTopicMN(){
        arrayAdapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_list_item_multiple_choice,
            resources.getStringArray(
                R.array.Management
            )
        )
        topicListView?.adapter = arrayAdapter
        topicListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun writeNewUser(pictProfile: String,
                             userId: String,
                             fullname: String,
                             gender: String,
                             job: String,
                             location: String,
                             major: String,
                             listTopic: MutableList<String>
    ) {

        val user = User(
            pictProfile,
            fullname,
            major,
            location,
            gender,
            job
        )
        database.child("users").child(userId).setValue(user)
        listTopic.forEach {
            database.child("users").child(userId).child("listTopic").push().setValue(it)
        }

    }
}