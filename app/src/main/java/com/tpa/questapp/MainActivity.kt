package com.tpa.questapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        googleProvider()
    }

    private fun googleProvider() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        auth = Firebase.auth
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //register with google
        database = Firebase.database.reference
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN)
    }

//    override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        val currentUser = auth.currentUser
//        if(currentUser != null){
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//        }else{
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
//    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser

                    val userListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            // Get Post object and use the values to update the UI
                            if(dataSnapshot.exists()){
                                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                            }else{
                                startActivity(Intent(this@MainActivity, RegisterDetailActivity::class.java))
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            // ...
                        }
                    }
                    //insert user data to database
                    database.child("users").child(user!!.uid).addListenerForSingleValueEvent(userListener)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(getApplicationContext(),"Sign In with Google failed",Toast.LENGTH_SHORT).show();
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == MainActivity.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...

            }
        }
    }

    private fun init() {
        googleBtn.setOnClickListener{
            signIn()
        }
        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActvity::class.java)
            startActivity(intent)
        }

        registerBtn.setOnClickListener{
            val intent = Intent(this, RegisterActivtity::class.java)
            startActivity(intent)
        }
    }


    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 9001
    }
}