package com.tpa.questapp.roomdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tpa.questapp.R


/**
 * A simple [Fragment] subclass.
 * Use the [PostDetailRoomFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostDetailRoomFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_detail_room, container, false)
    }
}