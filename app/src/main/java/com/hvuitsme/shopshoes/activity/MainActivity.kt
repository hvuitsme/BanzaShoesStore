package com.hvuitsme.shopshoes.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.hvuitsme.shopshoes.R
import com.hvuitsme.shopshoes.adapter.AdapterViewPage
import com.hvuitsme.shopshoes.fragment.FavoriteFragment
import com.hvuitsme.shopshoes.fragment.HomeFragment
import com.hvuitsme.shopshoes.fragment.UserFragment

class MainActivity : AppCompatActivity() {
    private lateinit var pagerMain: ViewPager2
    private val fragmentArrayList = ArrayList<Fragment>()
    private lateinit var bottNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottNav = findViewById(R.id.bottNav)
        pagerMain = findViewById(R.id.pagerMain)
        fragmentArrayList.add(HomeFragment())
        fragmentArrayList.add(FavoriteFragment())
        fragmentArrayList.add(UserFragment())

        val adapterViewPage = AdapterViewPage(this, fragmentArrayList)

        pagerMain.isUserInputEnabled = false
        pagerMain.adapter = adapterViewPage
        pagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> bottNav.selectedItemId = R.id.v_home
                    1 -> bottNav.selectedItemId = R.id.v_favorite
                    2 -> bottNav.selectedItemId = R.id.v_user
                    else -> bottNav.selectedItemId = R.id.v_home //default(mặc định)
                }
                super.onPageSelected(position)
            }
        })
        bottNav.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.v_home -> pagerMain.setCurrentItem(0)
                    R.id.v_favorite -> pagerMain.setCurrentItem(1)
                    R.id.v_user -> pagerMain.setCurrentItem(2)
                }
                return true
            }
        })
    }
}