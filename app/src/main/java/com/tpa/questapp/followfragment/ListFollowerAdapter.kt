package com.tpa.questapp.followfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.User

class ListFollowerAdapter: RecyclerView.Adapter<ListFollowerAdapter.Companion.Holder>{
    companion object{
        class Holder : RecyclerView.ViewHolder{

            lateinit var userName: TextView
            lateinit var userImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var userMajor : TextView

            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.nameUserFollowerListTxt) as TextView
                userImg = rv.findViewById(R.id.userFollowerListImg) as de.hdodenhof.circleimageview.CircleImageView
                userMajor = rv.findViewById(R.id.majorUserFollowerListTxt) as TextView
            }
        }
    }

    var list: ArrayList<User> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<User>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFollowerAdapter.Companion.Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.list_follower, parent,false)
        holder = Holder(rv)
        return holder
    }
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var at: User = list[position]
        holder.userName.setText(at.fullname)
        holder.userMajor.setText(at.major)
        Picasso.get().load(at.pictProfile).into(holder.userImg)
    }

}