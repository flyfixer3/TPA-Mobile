package com.tpa.questapp.profiledetailfragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tpa.questapp.profiledetailfragment.usertopic.UserTopicInterestTabFragment

class ProfileDetailPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm)  {
    // sebuah list yang menampung objek Fragment
    private val pages = listOf(
        UserQuestionTabFragment(),
        UserTopicInterestTabFragment(),
        UserRoomCreatedTabFragment()
    )

    // menentukan fragment yang akan dibuka pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    // judul untuk tabs
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Question"
            1 -> "Topic Interest"
            else -> "Room Created"
        }
    }
}