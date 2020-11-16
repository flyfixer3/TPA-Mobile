package com.tpa.questapp.homefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tpa.questapp.R
import com.tpa.questapp.homefragment.answerpagefragment.MyPagerAdapter
import kotlinx.android.synthetic.main.fragment_answer.*
import kotlinx.android.synthetic.main.fragment_answer.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [AnswerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnswerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_answer, container, false)
        view.viewpager_main.adapter = MyPagerAdapter(childFragmentManager, view.context)
        view.tabs_main.setupWithViewPager(view.viewpager_main)
        // Inflate the layout for this fragment
        return view
    }
}