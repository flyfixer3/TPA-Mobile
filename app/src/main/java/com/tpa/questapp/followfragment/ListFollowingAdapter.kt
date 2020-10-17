package com.tpa.questapp.followfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tpa.questapp.model.User
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.list_following.view.*

class ListFollowingAdapter(
    var ctx: Context,
    var resource: Int,
    private var items:List<User>
) : ArrayAdapter<User>(ctx, resource, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(ctx)
        val view:View = layoutInflater.inflate(resource, null)

        val imageUser: CircleImageView = view.userFollowingListImg
        val nameUser: TextView = view.nameUserFollowingListTxt
        val majorUser: TextView = view.majorUserFollowingListTxt

        var mItem: User = items[position]
        Picasso.get().load(mItem.pictProfile).into(imageUser)
        nameUser.setText(mItem.fullname)
        majorUser.setText(mItem.major)
        return view
    }
}