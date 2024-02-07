package com.arsnyan.lamodacopy.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arsnyan.lamodacopy.data.Color
import com.arsnyan.lamodacopy.databinding.ColorSelectorBinding
import com.arsnyan.lamodacopy.ui.productview.ProductScreenViewModel

class ColorSelectorAdapter(private val viewModel: ProductScreenViewModel, private var colors: List<Color>)
    : RecyclerView.Adapter<ColorSelectorAdapter.ViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: ColorSelectorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val previousSelectedPosition = selectedPos
            selectedPos = bindingAdapterPosition
            viewModel.selectVariation(colors[selectedPos], viewModel.variations[viewModel.currentId.value].size)
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ColorSelectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            holder.bind()
        }

        holder.itemView.isSelected = selectedPos == holder.bindingAdapterPosition
        if (position == selectedPos)
            holder.binding.root.setCardBackgroundColor(android.graphics.Color.parseColor("#d8d8d8"))
        else
            holder.binding.root.setCardBackgroundColor(android.graphics.Color.parseColor("#ffffff"))

        val variation = viewModel.variations.filter { it.color == colors[position] }
        holder.binding.preview.load(variation[0].urls[0])
        holder.binding.colorTxt.text = holder.itemView.context.getString(colors[position].stringId)
        holder.binding.price.text = variation[0].currentPrice.toString() + "₽"
    }
}