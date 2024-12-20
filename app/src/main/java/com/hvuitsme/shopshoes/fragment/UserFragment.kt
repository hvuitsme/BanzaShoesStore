package com.hvuitsme.shopshoes.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.hvuitsme.shopshoes.R
import com.hvuitsme.shopshoes.activity.WelcomeActivity
import com.hvuitsme.shopshoes.databinding.FragmentUserBinding
import de.hdodenhof.circleimageview.CircleImageView

class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(/* context = */ requireContext(), /* options = */
            gso)

        val auth = Firebase.auth
        val user = auth.currentUser

        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email)
        val ivAccount = view.findViewById<CircleImageView>(R.id.profile_image)

        if (user != null){
            val userName = user.displayName
            val userEmail = user.email
            val userPhotoUrl = user.photoUrl

            tvName.text = userName
            tvEmail.text = userEmail
        // Nếu thay thành tv chào có thể để trước đó 1 string sẵn
        // như "welcome" + userName

            if (userPhotoUrl != null){
                Glide.with(this)
                    .load(userPhotoUrl)
                    .placeholder(R.drawable.personal_img)
                    .into(ivAccount)
            }
        }

        val signout_btn = view.findViewById<FloatingActionButton>(R.id.btn_signout)
        signout_btn.setOnClickListener {
            signOutAndStartWelcomeActivity()
        }

    }

    private fun signOutAndStartWelcomeActivity() {
        mAuth.signOut()

        mGoogleSignInClient.signOut().addOnCompleteListener {
            handler.postDelayed({
                startActivity(Intent(requireContext(), WelcomeActivity::class.java ))
                activity?.finish()
            },2000)
        }
    }


}