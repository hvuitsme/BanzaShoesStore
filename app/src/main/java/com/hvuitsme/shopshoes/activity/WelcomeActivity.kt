package com.hvuitsme.shopshoes.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.hvuitsme.shopshoes.R
import jp.wasabeef.glide.transformations.BlurTransformation

class WelcomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var blurImage : ImageView ?= null

    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        //Kiểm tra xem đã đăng nhập tài khoản gg nào chưa
        if (currentUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        //Nếu chưa đăng nhập thì đặt mặc định là WelcomeActivity
        setContentView(R.layout.activity_welcome)
        blurImage = findViewById(R.id.imgBlur)
        val signInBtn = findViewById<Button>(R.id.Signin)
        val signUpBtn = findViewById<Button>(R.id.Signup)

        Glide.with(this).load(R.drawable.bg_signin).apply(RequestOptions
            .bitmapTransform(BlurTransformation(25,27))).into(blurImage!!)

        val rotateAnimator = ObjectAnimator.ofFloat(blurImage, "rotation", 0f, 360f)
        rotateAnimator.duration = 10000
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE
        rotateAnimator.interpolator = null
//        rotateAnimator.repeatMode = ObjectAnimator.RESTART //khởi động lại xoay vòng và bị delay một khoảng thời gian cụ thể là 1ms
        rotateAnimator.start()

        signInBtn.setOnClickListener {
//            startActivity(Intent(this, SignInActivity::class.java))
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) // Tắt animation chuyển Activity
            startActivity(intent)
            overridePendingTransition(0, 0) // Tắt hẳn hiệu ứng chuyển Activity
        }

        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            overridePendingTransition(0,0)
        }
    }
}