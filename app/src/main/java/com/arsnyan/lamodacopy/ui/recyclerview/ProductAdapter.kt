package com.arsnyan.lamodacopy.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.Size
import com.arsnyan.lamodacopy.databinding.ImageSliderPagerBinding
import com.arsnyan.lamodacopy.databinding.ProductCatalogCardViewBinding
import com.arsnyan.lamodacopy.utils.ExtensionFunctions.haveDaysPassed
import com.arsnyan.lamodacopy.utils.ExtensionFunctions.setDiscountVisibility
import com.arsnyan.lamodacopy.utils.OnItemClickListener

class ProductAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : PagingDataAdapter<Product, ProductAdapter.ViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            getItem(position)?.let { currentItem ->
                println(currentItem)
                if (currentItem.variations.maxOf { variations -> variations.stock } == 0)
                    binding.root.alpha = 0.75f
                binding.imageCarousel.adapter = ViewPagerAdapter(currentItem.variations[0].urls)
                currentItem.setDiscountVisibility(context, 0, binding.originalPrice, binding.currentPrice,
                    binding.badgeDiscount, binding.badgeClubDiscount)
                binding.brand.text = currentItem.brand.name
                binding.category.text = currentItem.category.name
                val allSizes = mutableSetOf<Size>()
                currentItem.variations.forEach { variations -> allSizes.add(variations.size) }
                binding.availableSizes.text = allSizes.map { originalSizeObj -> originalSizeObj.sizeRu }.joinToString(", ")

                binding.badgeNew.isVisible = !currentItem.variations
                    .maxBy { variation -> variation.createdAt }
                    .createdAt
                    .haveDaysPassed(30)

                holder.binding.root.setOnClickListener {
                    listener.onItemClick(it.id)
                }
            }
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