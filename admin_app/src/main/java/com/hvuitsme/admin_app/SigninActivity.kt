package com.hvuitsme.admin_app

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jp.wasabeef.glide.transformations.BlurTransformation

class SigninActivity : AppCompatActivity() {
    private var blurImage: ImageView ?= null
    private lateinit var usernameEdt: EditText
    private lateinit var passwordEdt: EditText
    private lateinit var signInBtn: Button
    private lateinit var database: DatabaseReference

    @SuppressLint("ObjectAnimatorBinding")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        FirebaseApp.initializeApp(this)

        blurImage = findViewById(R.id.imgBlur)

        Glide.with(this).load(R.drawable.bg_signin).apply(RequestOptions
            .bitmapTransform(BlurTransformation(25, 27))).into(blurImage!!)
        val rotateAnimator = ObjectAnimator.ofFloat(blurImage, "rotation", 0f, 360f)
        rotateAnimator.duration = 10000
        rotateAnimator.repeatCount = ObjectAnimator.INFINITE
        rotateAnimator.interpolator = null
        rotateAnimator.start()

        usernameEdt = findViewById(R.id.username)
        passwordEdt = findViewById(R.id.password)
        signInBtn = findViewById(R.id.Signin)


        database = FirebaseDatabase.getInstance().getReference("users/admin")

        signInBtn.setOnClickListener {
            val username = usernameEdt.text.toString().trim()
            val password = passwordEdt.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //kiểm tra thông tin đăng nhập
            loginAdmin(username, password)
        }
    }

    private fun loginAdmin(username: String, password: String) {
        loadingSrc(true)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            database.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){
                        val adminUsername = snapshot.child("username").value.toString()
                        val adminPassword = snapshot.child("password").value.toString()

                        if(username == adminUsername && password == adminPassword){
                            Toast.makeText(this@SigninActivity, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SigninActivity, HomeActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this@SigninActivity, "Username hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@SigninActivity, "Không tìm thấy thông tin admin", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@SigninActivity, "Lỗi ${error.message}", Toast.LENGTH_SHORT).show()
                }

            })
        },4000)
    }

    private fun loadingSrc(show: Boolean) {
        val ldAni = findViewById<LottieAnimationView>(R.id.loading)
        val dkOverlay = findViewById<View>(R.id.dark_overlay)

        if (show){
            ldAni.visibility = View.VISIBLE
            dkOverlay.visibility = View.VISIBLE
        }else{
            ldAni.visibility = View.GONE
            dkOverlay.visibility = View.GONE
        }
    }
}