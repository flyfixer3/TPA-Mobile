package com.tpa.questapp.homefragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tpa.questapp.R
import com.tpa.questapp.RegisterActivtity
import com.tpa.questapp.room.RoomFormActivity
import kotlinx.android.synthetic.main.fragment_discover.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_discover, container, false)
        // Inflate the layout for this fragment
        view.addRoomButton.setOnClickListener{
            startActivity(
                Intent(
                    this.activity,
                    RoomFormActivity::class.java
                )
            )
        }
        return view
    }
}