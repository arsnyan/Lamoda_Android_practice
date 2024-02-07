package com.arsnyan.lamodacopy.ui.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arsnyan.lamodacopy.data.Color
import com.arsnyan.lamodacopy.data.ProductVariation
import com.arsnyan.lamodacopy.data.Size
import com.arsnyan.lamodacopy.databinding.ColorSelectorBinding
import com.arsnyan.lamodacopy.databinding.SizeSelectorCardViewBinding
import com.arsnyan.lamodacopy.ui.productview.ProductScreenViewModel

class ColorSelectorAdapter(private val viewModel: ProductScreenViewModel, private var colors: List<Color>)
    : RecyclerView.Adapter<ColorSelectorAdapter.ViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: ColorSelectorBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                notifyItemChanged(selectedPos)
                selectedPos = adapterPosition
                notifyItemChanged(selectedPos)
                viewModel.selectColor(colors[position])
                //Log.d("CHANGE COLOR", "New color is: ${colors[position]}, viewModel color: ${viewModel.selectedColor.value}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ColorSelectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.isSelected = selectedPos == holder.bindingAdapterPosition
        if (position == selectedPos)
            holder.binding.root.setCardBackgroundColor(android.graphics.Color.parseColor("#d8d8d8"))
        else
            holder.binding.root.setCardBackgroundColor(android.graphics.Color.parseColor("#ffffff"))
        println(colors)
        val variation = viewModel.product.value?.variations?.filter { it.color == colors[position] }?.get(0)
        println(variation?.urls?.get(0))
        holder.binding.preview.load(variation?.urls?.get(0))
        holder.binding.colorTxt.text = holder.itemView.context.getString(colors[position].stringId)
        holder.binding.price.text = variation?.currentPrice.toString()
    }
}