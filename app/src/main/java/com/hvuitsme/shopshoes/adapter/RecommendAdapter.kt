package com.hvuitsme.shopshoes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hvuitsme.shopshoes.databinding.PdLayoutBinding
import com.hvuitsme.shopshoes.model.ProductModel

class RecommendAdapter(val items: MutableList<ProductModel>): RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder>() {

    class RecommendViewHolder(val binding: PdLayoutBinding ): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendAdapter.RecommendViewHolder {
        val binding = PdLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendAdapter.RecommendViewHolder, position: Int) {
        val items = items[position]
        with(holder.binding){
            pdTitle.text = items.title
            pdPrice.text = "${items.price} VNĐ"
            rating.text = items.rating.toString()

            Glide.with(holder.itemView.context)
                .load(items.picUrl[0])
                .into(ivProduct)

            root.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int = items.size
}