package com.tpa.questapp.homefragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.tpa.questapp.MainActivity
import com.tpa.questapp.R
import kotlinx.android.synthetic.main.fragment_profile.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
//    var context: Activity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        context = getActivity();
        val view =  inflater.inflate(R.layout.fragment_profile, container, false)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid
        val email = auth.currentUser!!.email.toString()
        database = FirebaseDatabase.getInstance().getReference("users").child(userId)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        auth = Firebase.auth
        googleSignInClient = GoogleSignIn.getClient(activity!!.application, gso)

        database.addValueEventListener( object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("fullname").value.toString().trim()
                val gender = snapshot.child("gender").value.toString().trim()
                val location = snapshot.child("location").value.toString().trim()
                val education = snapshot.child("major").value.toString().trim()
                val imgUrl = snapshot.child("pictProfile").value.toString().trim()
                val job = snapshot.child("job").value.toString().trim()
                Picasso.get().load(imgUrl).into(view.imgProfile)
                view.fullNameTxt.setText(name)
                view.emailTxt.setText(email)
                view.genderTxt.setText(gender)
                view.locationTxt.setText(location)
                view.educationTxt.setText(education)
                view.jobTxt.setText(job)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        view.logoutTxt.setOnClickListener{
            googleSignInClient.signOut()
            val i = Intent(context, MainActivity::class.java)
            this.startActivity(i)
            activity!!.finish()
        }
        return view
    }
}
