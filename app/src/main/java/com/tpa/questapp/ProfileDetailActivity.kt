package com.tpa.questapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tpa.questapp.profiledetailfragment.ProfileDetailPagerAdapter
import kotlinx.android.synthetic.main.activity_profile_detail.*


class ProfileDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_detail)
        init()
    }

    private fun init() {
        val pos = intent.getIntExtra("tabDetailProfile",0)
        viewpager_profiledetail.adapter = ProfileDetailPagerAdapter(supportFragmentManager,this)
        viewpager_profiledetail.setCurrentItem(pos)
    }
}