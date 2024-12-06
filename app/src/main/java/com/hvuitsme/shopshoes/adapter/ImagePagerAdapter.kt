package com.hvuitsme.shopshoes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hvuitsme.shopshoes.R

class ImagePagerAdapter(private val images: List<Int>):
    RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
        class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val imageView: ImageView = itemView.findViewById(R.id.iv_carousel)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewpager_layout, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val realPosition = position % images.size//lặp lại từ đầu

//        holder.imageView.setImageResource(images[position])
        holder.imageView.setImageResource(images[realPosition])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}