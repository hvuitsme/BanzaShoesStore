package com.hvuitsme.admin_app

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.wasabeef.glide.transformations.BlurTransformation

class WelcomeActivity : AppCompatActivity() {
    private var blurImage: ImageView ?= null

    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        setContentView(R.layout.activity_welcome)
        blurImage = findViewById(R.id.imgBlur)

        Glide.with(this).load(R.drawable.bg_signin).apply(RequestOptions
            .bitmapTransform(BlurTransformation(25, 27))).into(blurImage!!)

        val rotateAnimator = ObjectAnimator.ofFloat(blurImage, "rotation", 0f, 360f)
        rotateAnimator.duration = 10000
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE
        rotateAnimator.interpolator = null
        rotateAnimator.start()

        val signInBtn = findViewById<Button>(R.id.Signin)
        val signUpBtn = findViewById<Button>(R.id.Signup)

    }
}