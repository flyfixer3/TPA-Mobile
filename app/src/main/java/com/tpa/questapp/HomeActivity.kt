package com.tpa.questapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tpa.questapp.homefragment.AnswerFragment
import com.tpa.questapp.homefragment.HomeFragment
import com.tpa.questapp.homefragment.ProfileFragment
import com.tpa.questapp.homefragment.answerpagefragment.DiscoverFragment
import com.tpa.questapp.search.SearchActivity
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
        replaceFragment(homeFragment, resources.getString(R.string.home))
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment, resources.getString(R.string.home))
                R.id.answer -> replaceFragment(answerFragment, getString(R.string.answer))
                R.id.discover -> replaceFragment(discoverFragment, getString(R.string.discover))
                R.id.profile -> replaceFragment(profileFragment,getString( R.string.profile))
            }
            true
        }
        headerMenu.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    SearchActivity::class.java
                )
            )
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