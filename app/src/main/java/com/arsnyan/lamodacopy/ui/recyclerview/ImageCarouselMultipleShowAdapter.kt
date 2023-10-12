package com.arsnyan.lamodacopy.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arsnyan.lamodacopy.databinding.ImageCarouselMultipleShowBinding
import com.arsnyan.lamodacopy.databinding.ImageSliderPagerBinding

class ImageCarouselMultipleShowAdapter(private val urls: List<String>)
    : RecyclerView.Adapter<ImageCarouselMultipleShowAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ImageCarouselMultipleShowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ImageCarouselMultipleShowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = urls.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.image.load(urls[position])
    }
}