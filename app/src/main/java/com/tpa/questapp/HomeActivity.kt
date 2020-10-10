package com.tpa.questapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tpa.questapp.homefragment.AnswerFragment
import com.tpa.questapp.homefragment.DiscoverFragment
import com.tpa.questapp.homefragment.HomeFragment
import com.tpa.questapp.homefragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private val homeFragment = HomeFragment()
    private val answerFragment = AnswerFragment()
    private val discoverFragment = DiscoverFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init(){
        replaceFragment(homeFragment, "Home")
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment, "Home")
                R.id.answer -> replaceFragment(answerFragment, "Answer")
                R.id.discover -> replaceFragment(discoverFragment, "Discover")
                R.id.profile -> replaceFragment(profileFragment, "Profile")
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.homeContainer, fragment)
            transaction.commit()
            headerMenu.setText(title)
        }
    }
}