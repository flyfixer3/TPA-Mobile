package com.tpa.questapp.followfragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.R
import com.tpa.questapp.model.User

class ListFollowingAdapter : RecyclerView.Adapter<ListFollowingAdapter.Companion.Holder>{
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    companion object{
        class Holder : RecyclerView.ViewHolder{
            lateinit var userName: TextView
            lateinit var userImg: de.hdodenhof.circleimageview.CircleImageView
            lateinit var userTopic: TextView
            lateinit var unfollowBtn: Button

            constructor(rv: View) : super(rv){
                userName = rv.findViewById(R.id.nameUserFollowingListTxt) as TextView
                userImg = rv.findViewById(R.id.userFollowingListImg) as de.hdodenhof.circleimageview.CircleImageView
                userTopic = rv.findViewById(R.id.majorUserFollowingListTxt) as TextView
                unfollowBtn = rv.findViewById(R.id.UnfollowFollowingButton) as Button
            }
        }
    }
    var list: ArrayList<User> = arrayListOf()

    lateinit var con: Context

    constructor(list: ArrayList<User>, con: Context) : super() {
        this.list = list
        this.con = con

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var rv: View
        var holder: Holder
        rv = LayoutInflater.from(parent!!.context).inflate(R.layout.list_following, parent,false)
        holder = Holder(rv)
        return holder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        var at: User
        at = list.get(position)
        Picasso.get().load(at.pictProfile).into(holder!!.userImg)
        holder!!.userName.setText(at.fullname)
        holder!!.userTopic.setText(at.major)
        holder!!.unfollowBtn.setOnClickListener{
            database.child("users").child(auth.uid.toString()).child("following").child(at.userId.toString()).removeValue()
            database.child("users").child(at.userId.toString()).child("followers").child(auth.uid.toString()).removeValue()
        }
    }
}