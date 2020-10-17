package com.tpa.questapp.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tpa.questapp.model.User

class TopicSearchListAdapter(
    var ctx: Context,
    var resource: Int,
    private var items:List<User>
) : ArrayAdapter<User>(ctx, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(ctx)
        val view:View = layoutInflater.inflate(resource, null)

//        val topic: TextView = view.
        return view
    }
}