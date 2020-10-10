package com.tpa.questapp.homefragment.answerpagefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tpa.questapp.R

/**
 * A simple [Fragment] subclass.
 * Use the [AnswerTabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnswerTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer_tab, container, false)
    }

}