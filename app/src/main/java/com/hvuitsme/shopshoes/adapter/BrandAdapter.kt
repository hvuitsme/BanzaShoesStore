package com.hvuitsme.shopshoes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hvuitsme.shopshoes.R
import com.hvuitsme.shopshoes.databinding.BrandLayoutBinding
import com.hvuitsme.shopshoes.model.BrandModel

class BrandAdapter(
    val brandItems: MutableList<BrandModel>,

    ) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    private var selectedPosition = 0
    private var lastSelectedPosition = -1

    inner class BrandViewHolder(val biding: BrandLayoutBinding) :
        RecyclerView.ViewHolder(biding.root) {
        init {
            biding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    lastSelectedPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(lastSelectedPosition)
                    notifyItemChanged(selectedPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BrandAdapter.BrandViewHolder {
        val biding = BrandLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrandViewHolder(biding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val item = brandItems[position]
        Glide.with(holder.itemView.context)
            .load(item.picUrl)
            .into(holder.biding.ivBrand)

        if (selectedPosition == position) {
            holder.biding.ivBrand.setBackgroundResource(0)
            holder.biding.brandLayout.setBackgroundResource(R.drawable.brand_bg_btn_2)
            //đổi màu chủ thể (hiện tại là vector) nếu được nhấn vào
//            ImageViewCompat.setImageTintList(
//                holder.biding.ivBrand,
//                ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.white))
//            )
            //đổi màu và hiện chủ thể (hiện tại là chữ) nếu được nhấn vào
//            holder.biding.titleText.visibility = View.VISIBLE
//            holder.biding.titleText.setTextColor(
//              ContextCompat.getColor(
//                  holder.itemView.context,
//                  R.color.white
//              )
//            )
        }else{
            holder.biding.ivBrand.setBackgroundResource(R.drawable.brand_bg_btn)
            holder.biding.brandLayout.setBackgroundResource(0)
            //đổi màu chủ thể (hiện tại là vector) nếu được nhấn vào
//            ImageViewCompat.setImageTintList(
//                holder.biding.ivBrand,
//                ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context, R.color.white))
//            )
            //đổi màu và hiện chủ thể (hiện tại là chữ) nếu được nhấn vào
//            holder.biding.titleText.visibility = View.GONE
//            holder.biding.titleText.setTextColor(
//              ContextCompat.getColor(
//                  holder.itemView.context,
//                  R.color.white
//              )
//            )
        }
    }

    override fun getItemCount(): Int = brandItems.size
}