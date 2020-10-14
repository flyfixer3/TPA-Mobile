package com.tpa.questapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
    }

    private fun init() {
        val names = arrayOf("Android","Java","PHP","Python")
        val adapter:ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_1, names)

        list_item_search.adapter = adapter
        search_box.setIconifiedByDefault(false)

        search_box.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(names.contains(p0)){
                    adapter.filter.filter(p0)
                }else{
                    Toast.makeText(applicationContext, "Item notfound",Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                adapter.filter.filter(p0)
                return false
            }
        })
    }
}