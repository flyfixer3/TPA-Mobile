package com.tpa.questapp.homefragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tpa.questapp.QuestionFormActivity
import com.tpa.questapp.R
import com.tpa.questapp.room.RoomFormActivity
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

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
        return view
    }
}