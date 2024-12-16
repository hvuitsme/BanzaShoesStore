package com.hvuitsme.shopshoes.activity

import android.graphics.Outline
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.hvuitsme.shopshoes.R
import com.hvuitsme.shopshoes.adapter.BrandAdapter
import com.hvuitsme.shopshoes.adapter.ImagePagerAdapter
import com.hvuitsme.shopshoes.adapter.SlideAdapter
import com.hvuitsme.shopshoes.databinding.ActivityHomeBinding
import com.hvuitsme.shopshoes.model.SlideModel
import com.hvuitsme.shopshoes.viewmodel.MainViewModel
import kotlinx.coroutines.Runnable

class HomeActivity : AppCompatActivity() {

    private lateinit var brandImageViews : List<ImageView>
    private var selectedImageView : ImageView ?= null
    private lateinit var binding: ActivityHomeBinding
    private val viewModel = MainViewModel()
    private val handler = Handler(Looper.getMainLooper())

//    private var currentPage = 0
//    private lateinit var viewPager: ViewPager2
//    private lateinit var mGoogleSignInClient: GoogleSignInClient
//    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
        initBrand()

        startAutoScroll()
        stopAutoScroll()

//        viewPager = findViewById(R.id.pagerView)
//
//        val imageList = listOf(
//            R.drawable.ad_image_1,
//            R.drawable.ad_image_2,
//            R.drawable.ad_image_3
//        )
//
//        val adapter = ImagePagerAdapter(imageList)
//        viewPager.adapter = adapter
//
//        val startPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % imageList.size)
//        viewPager.setCurrentItem(startPosition, true)

//        brandImageViews = listOf(
//            findViewById(R.id.iv_louisvuitton),
//            findViewById(R.id.iv_nike),
//            findViewById(R.id.iv_adidas),
//            findViewById(R.id.iv_balenciaga),
//            findViewById(R.id.iv_converse),
//            findViewById(R.id.iv_puma),
//            findViewById(R.id.iv_vans),
//            findViewById(R.id.iv_golden_goose)
//        )
//
//        val defaultBrandIv = findViewById<ImageView>(R.id.iv_louisvuitton)
//        setSolidColor(defaultBrandIv, R.color.grey)
//        selectedImageView = defaultBrandIv
//
//        // gán sự kiện click cho mỗi imageview
//        brandImageViews.forEach { imageView ->
//            imageView.setOnClickListener {
//                //Đặt nền của từng iv về trạng thái ban đầu(mặc định)
//                selectedImageView?.let { resetBgColor(it) }
//
//                //Đặt nền của iv sau khi được chọn
//                setSolidColor(imageView, R.color.grey)
//
//                //Cập nhật trạng thái
//                selectedImageView = imageView
//            }
//        }

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

    private fun initBrand() {
        binding.progressBarBrand.visibility = View.VISIBLE
        viewModel.brands.observe(this, Observer{
            binding.viewBrand.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewBrand.adapter = BrandAdapter(it)
            binding.progressBarBrand.visibility = View.GONE
        })
        viewModel.loadBrands()
    }

    //thiết lập slide cho banner
    private fun banners(image: List<SlideModel>){
        binding.pagerView.adapter = SlideAdapter(image, binding.pagerView)
        binding.pagerView.clipToPadding = false
        binding.pagerView.clipChildren = false
        binding.pagerView.offscreenPageLimit = 3
        binding.pagerView.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        binding.pagerView.outlineProvider = object : ViewOutlineProvider(){
            override fun getOutline(view: View, outline: Outline) {
                val cornerRadius = resources.getDimension(R.dimen.size_x5)
                outline.setRoundRect(0, 0, view.width, view.height, cornerRadius)
            }
        }
        binding.pagerView.clipToOutline = true

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40)) // căn lề khoảng cách 40dp
        }

        binding.pagerView.setPageTransformer(compositePageTransformer)

//        if (image.size > 1){
//          đặt điều kiện nếu image ở trang nào thì theo trang ấy,
//          và chấm dưới trang di chuyển theo(hiện tại bỏ qua vì ko có nó)
//            binding.dotIndicator.visibility = View.VISIBLE
//            binding.dotIndicator.attachTo(binding.pagerView)
//        }
    }

    private fun initBanner(){
        binding.progressBar.visibility = View.VISIBLE
        viewModel.banners.observe(this, Observer {
            banners(it)
            binding.progressBar.visibility = View.GONE
        })
        viewModel.loadBanners()
    }

    // Tự động cuộn và dừng cuộn
    private fun startAutoScroll() {
        val delay: Long = 6000

        val runnable = object : Runnable{
            override fun run() {
                if (binding.pagerView.adapter != null){
                    val itemCount = binding.pagerView.adapter?.itemCount ?: 0
                    val nextPage = (binding.pagerView.currentItem + 1) % itemCount
                    binding.pagerView.setCurrentItem(nextPage, true)
                }
                handler.postDelayed(this, delay)
            }
        }
        handler.postDelayed(runnable, delay)
    }

    private fun stopAutoScroll() {
        handler.removeCallbacksAndMessages(null)
    }

    override fun onResume() {
        super.onResume()
        startAutoScroll()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

//    private fun setSolidColor(imageView: ImageView, colorResId: Int) {
//        val drawable = imageView.background as GradientDrawable
//        drawable.setColor(ContextCompat.getColor(this, colorResId)) //đặt lại màu nền
//    }
//
//    private fun resetBgColor(imageView: ImageView) {
//        val drawable = imageView.background as GradientDrawable
//        drawable.setColor(ContextCompat.getColor(this, R.color.transparent))
//    }

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

//    private fun startAutoScroll() {
//        val delay: Long = 6000
//        handler.postDelayed(object : Runnable{
//            override fun run(){
//                currentPage = viewPager.currentItem + 1
//                viewPager.setCurrentItem(currentPage, true)
//                handler.postDelayed(this, delay)
//            }
//        },delay)
//    }
//
//    private fun stopAutoScroll(){
//        handler.removeCallbacksAndMessages(null)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        stopAutoScroll()
//    }
}