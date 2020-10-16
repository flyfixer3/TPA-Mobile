package com.tpa.questapp.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tpa.questapp.model.User
import kotlinx.android.synthetic.main.search_user_list.view.*

class UserSearchListAdapter(
    var ctx: Context,
    var resource: Int,
    private var items:List<User>
) : ArrayAdapter<User>(ctx, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(ctx)
        val view:View = layoutInflater.inflate(resource, null)

        val imageUser: de.hdodenhof.circleimageview.CircleImageView = view.userSearchListImg
        val nameUser: TextView = view.nameUserSearchListxt
        val majorUser: TextView = view.majorUserSearchList

        var mItem: User = items[position]
        Picasso.get().load(mItem.pictProfile).into(imageUser)
        nameUser.setText(mItem.fullname)
        majorUser.setText(mItem.major)

        return view
    }
}
