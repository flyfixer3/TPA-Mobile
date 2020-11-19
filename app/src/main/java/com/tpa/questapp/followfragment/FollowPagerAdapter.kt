package com.tpa.questapp.followfragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tpa.questapp.R

class FollowPagerAdapter(fm: FragmentManager, context: Context): FragmentPagerAdapter(fm) {
    // sebuah list yang menampung objek Fragment
    private val context = context
    private val pages = listOf(
        UserFollowerTabFragment(),
        UserFollowingTabFragment()
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
            0 -> context.resources.getString(R.string.follower)
            else -> context.resources.getString(R.string.following)
        }
    }
}