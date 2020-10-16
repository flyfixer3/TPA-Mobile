package com.tpa.questapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tpa.questapp.followfragment.FollowPagerAdapter
import kotlinx.android.synthetic.main.activity_user_follow.*

class UserFollowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_follow)
        init()
    }

    private fun init() {
        viewpager_follow.adapter = FollowPagerAdapter(supportFragmentManager)
    }
}