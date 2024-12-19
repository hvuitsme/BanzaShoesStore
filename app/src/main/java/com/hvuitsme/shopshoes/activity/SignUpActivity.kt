package com.hvuitsme.shopshoes.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hvuitsme.shopshoes.R
import jp.wasabeef.glide.transformations.BlurTransformation

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignUpLauncher: ActivityResultLauncher<Intent>
    private var blurImage: ImageView? = null

    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null){
            navigateToHome()
        }

        blurImage = findViewById(R.id.imgBlur)
        val signUpGG = findViewById<ImageView>(R.id.ic_google)

        Glide.with(this).load(R.drawable.bg_signin).apply(
            RequestOptions.bitmapTransform(
                BlurTransformation(25, 27)
            )).into(blurImage!!)

        val rotateAnimator = ObjectAnimator.ofFloat(blurImage, "rotation", 0f, 360f)
        rotateAnimator.duration = 10000
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE
        rotateAnimator.interpolator = null
        rotateAnimator.start()

        signUpGG.setOnClickListener {
            SignUp()
        }

        googleSignUpLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGG(account.idToken!!)
            } catch (e: ApiException){
                Toast.makeText(this, "Google sign up failed ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun SignUp() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignUpClient = GoogleSignIn.getClient(this, gso)
        val signUpIntent = googleSignUpClient.signInIntent

        googleSignUpLauncher.launch(signUpIntent)
    }

    private fun firebaseAuthWithGG(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    Toast.makeText(this, "Sign in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}