package com.hvuitsme.shopshoes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.hvuitsme.shopshoes.databinding.ViewpagerLayout2Binding
import com.hvuitsme.shopshoes.model.SlideModel

class SlideAdapter(
    private var slideItems: List<SlideModel>,
    private val viewPager2: ViewPager2
) :
    RecyclerView.Adapter<SlideAdapter.SlideViewHolder>() {
    private lateinit var context: Context
//    private val runnable = Runnable {
//        slideItems = slideItems
//        notifyDataSetChanged()
//    }

    class SlideViewHolder(private val binding: ViewpagerLayout2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setImage(slideItems: SlideModel, context: Context) {
            Glide.with(context)
                .load(slideItems.url)
                .apply { RequestOptions().transform(CenterInside()) }
                .into(binding.imageSlide)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlideAdapter.SlideViewHolder {
        context = parent.context
        val binding = ViewpagerLayout2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideAdapter.SlideViewHolder, position: Int) {
        holder.setImage(slideItems[position], context)
//        if(position == slideItems.lastIndex -1){
//            viewPager2.post(runnable)
//        }
    }

    override fun getItemCount(): Int = slideItems.size
}