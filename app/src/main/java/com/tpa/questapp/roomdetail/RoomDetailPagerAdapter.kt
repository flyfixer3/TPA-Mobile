package com.tpa.questapp.roomdetail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tpa.questapp.R

class RoomDetailPagerAdapter(fm: FragmentManager, context: Context): FragmentPagerAdapter(fm) {
    private val context = context
    private val pages = listOf(
        PostDetailRoomFragment(),
        QuestionDetailRoomFragment()
    )
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> context.resources.getString(R.string.post)
            else -> context.resources.getString(R.string.question)
        }
    }
}