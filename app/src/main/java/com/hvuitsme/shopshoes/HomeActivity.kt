package com.hvuitsme.shopshoes

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.hvuitsme.shopshoes.adapter.ImagePagerAdapter
import kotlinx.coroutines.Runnable

class HomeActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    private lateinit var viewPager: ViewPager2
    private val handler = Handler(Looper.getMainLooper())
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewPager = findViewById(R.id.pagerView)

        val imageList = listOf(
            R.drawable.ad_image_1,
            R.drawable.ad_image_2,
            R.drawable.ad_image_3
        )

        val adapter = ImagePagerAdapter(imageList)
        viewPager.adapter = adapter

        val startPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % imageList.size)
        viewPager.setCurrentItem(startPosition, true)

        startAutoScroll()

//        mAuth = FirebaseAuth.getInstance()
//
//
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//
//
//
//        val textView = findViewById<TextView>(R.id.name)
//
//        val auth = Firebase.auth
//        val user = auth.currentUser
//
//        if (user != null) {
//            val userName = user.displayName
//            textView.text = "Welcome, " + userName
//        } else {
//            // Handle the case where the user is not signed in
//        }
//
//
//
//// Inside onCreate() method
//        val sign_out_button = findViewById<Button>(R.id.logout_button)
//        sign_out_button.setOnClickListener {
//            signOutAndStartSignInActivity()
//        }
    }

    private fun startAutoScroll() {
        val delay: Long = 6000
        handler.postDelayed(object : Runnable{
            override fun run(){
                currentPage = viewPager.currentItem + 1
                viewPager.setCurrentItem(currentPage, true)
                handler.postDelayed(this, delay)
            }
        },delay)
    }

    private fun stopAutoScroll(){
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoScroll()
    }

//    private fun signOutAndStartSignInActivity() {
//        mAuth.signOut()
//
//        mGoogleSignInClient.signOut().addOnCompleteListener(this) {
//            // Optional: Update UI or show a message to the user
//            val intent = Intent(this@HomeActivity, WelcomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
}