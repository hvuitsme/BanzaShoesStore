package com.hvuitsme.shopshoes.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.hvuitsme.shopshoes.R
import jp.wasabeef.glide.transformations.BlurTransformation

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private var blurImage : ImageView?= null

//    companion object {
//        private const val RC_SIGN_IN = 9001
//    } // định nghĩa các thành viên tĩnh (static) hoặc hằng số(constant) trong lớp

    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null){
            navigateToHome()
        }

        val signInGG = findViewById<ImageView>(R.id.ic_google)

        signInGG.setOnClickListener {
            signIn()
        }

        blurImage = findViewById(R.id.imgBlur)

        Glide.with(this).load(R.drawable.bg_signin).apply(
            RequestOptions.bitmapTransform(
                BlurTransformation(25,27)
            )).into(blurImage!!)

        val rotateAnimator = ObjectAnimator.ofFloat(blurImage, "rotation", 0f, 360f)
        rotateAnimator.duration = 10000
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE
        rotateAnimator.interpolator = null
//        rotateAnimator.repeatMode = ObjectAnimator.RESTART //khởi động lại xoay vòng và bị delay một khoảng thời gian cụ thể là 1ms
        rotateAnimator.start()

        // thêm phần khởi tạo mới ActivityResultLaucher để xử lý đăng nhập thay thế cho phần onActivityResult cũ
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWthGG(account.idToken!!)
            } catch (e: ApiException){
                Toast.makeText(this, "Google signin failed ${e.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun signIn() {
        val ldAni = findViewById<LottieAnimationView>(R.id.loading)
        val dkOverlay = findViewById<View>(R.id.dark_overlay)

        ldAni.visibility = View.VISIBLE
        dkOverlay.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            val gso =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = googleSignInClient.signInIntent

            googleSignInLauncher.launch(signInIntent)

//          Kiểu cũ, đã lỗi thời
//          startActivityForResult(signInIntent, RC_SIGN_IN)
            ldAni.visibility = View.GONE
            dkOverlay.visibility = View.GONE
        }, 4000)
    }

    private fun firebaseAuthWthGG(idToken: String) {
        val credential =GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    Toast.makeText(this, "Sign in as ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
// phần code cũ khuyến khích không nên dùng
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN){
//            val task =GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWthGG(account.idToken!!)
//            }catch (e: ApiException){
//                Toast.makeText(this, "Google signin failed ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun navigateToHome(){
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}