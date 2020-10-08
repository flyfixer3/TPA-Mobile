package com.tpa.questapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RadioButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register_detail.*

class RegisterDetailActivity : AppCompatActivity() {
    private var arrayAdapter: ArrayAdapter<String> ? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_detail)
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
                if(majorSpinner.getSelectedItem().toString() == "Computer Science"){
                    loadTopicCS()
                }else if(majorSpinner.getSelectedItem().toString() == "Information Systems"){
                    loadTopicIS()
                }else if(majorSpinner.getSelectedItem().toString() == "Accounting"){
                    loadTopicAC()
                }else if(majorSpinner.getSelectedItem().toString() == "Management"){
                    loadTopicMN()
                }else{
                    topicListView.adapter = null
                }
            }
        }

        submitButton.setOnClickListener{
            val user = auth.currentUser
            val id: Int = genderRadioGroup.checkedRadioButtonId
            val radio: RadioButton = findViewById(id)
            writeNewUser(user?.uid.toString(), user!!.displayName.toString(), radio.text.toString() , jobField!!.text.toString(), locationField.text.toString(), majorSpinner.getSelectedItem().toString())
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
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
        arrayAdapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_multiple_choice, resources.getStringArray(R.array.ComputerScience))
        topicListView?.adapter = arrayAdapter
        topicListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun loadTopicIS(){
        arrayAdapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_multiple_choice, resources.getStringArray(R.array.InformationSystems))
        topicListView?.adapter = arrayAdapter
        topicListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun loadTopicAC(){
        arrayAdapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_multiple_choice, resources.getStringArray(R.array.Accounting))
        topicListView?.adapter = arrayAdapter
        topicListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun loadTopicMN(){
        arrayAdapter = ArrayAdapter(applicationContext,android.R.layout.simple_list_item_multiple_choice, resources.getStringArray(R.array.Management))
        topicListView?.adapter = arrayAdapter
        topicListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    private fun writeNewUser(userId: String, fullname: String, gender: String, job: String, location: String, major: String) {
        val user = User(fullname, gender, job, location, major)
        database.child("users").child(userId).setValue(user)
    }
}