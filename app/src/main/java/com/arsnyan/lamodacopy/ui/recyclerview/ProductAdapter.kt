package com.arsnyan.lamodacopy.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.Size
import com.arsnyan.lamodacopy.databinding.ImageSliderPagerBinding
import com.arsnyan.lamodacopy.databinding.ProductCatalogCardViewBinding
import com.arsnyan.lamodacopy.utils.ExtensionFunctions.haveDaysPassed
import com.arsnyan.lamodacopy.utils.ExtensionFunctions.setDiscountVisibility
import com.arsnyan.lamodacopy.utils.OnItemClickListener

class ProductAdapter(private val dataSet: List<Product>, private val context: Context,
                     private val listener: OnItemClickListener) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ProductCatalogCardViewBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION)
                listener.onItemClick(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ProductCatalogCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            dataSet[position].let {
                println(it)
                if (it.variations.maxOf { variations -> variations.stock } == 0)
                    binding.root.alpha = 0.75f
                binding.imageCarousel.adapter = ViewPagerAdapter(it.variations[0].urls)
                it.setDiscountVisibility(context, 0, binding.originalPrice, binding.currentPrice,
                    binding.badgeDiscount, binding.badgeClubDiscount)
                binding.brand.text = it.brand.name
                binding.category.text = it.category.name
                val allSizes = mutableSetOf<Size>()
                it.variations.forEach { variations -> allSizes.add(variations.size) }
                binding.availableSizes.text = allSizes.map { originalSizeObj -> originalSizeObj.sizeRu }.joinToString(", ")

                binding.badgeNew.isVisible = !it.variations
                    .maxBy { variation -> variation.createdAt }
                    .createdAt
                    .haveDaysPassed(30)
            }
        }
        holder.binding.root.setOnClickListener {
            listener.onItemClick(dataSet[position].id)
        }
    }

    private class ViewPagerAdapter(private val urls: List<String>) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
        inner class ViewHolder(val binding: ImageSliderPagerBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            ImageSliderPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        override fun getItemCount(): Int = urls.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.image.load(urls[position])
        }
    }
}