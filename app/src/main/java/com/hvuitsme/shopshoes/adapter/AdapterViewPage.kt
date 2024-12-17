package com.hvuitsme.shopshoes.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterViewPage(
    fragmentActivity: FragmentActivity,
    private val arr: ArrayList<Fragment>
): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = arr.size

    override fun createFragment(position: Int): Fragment {
        return arr[position]
    }

}