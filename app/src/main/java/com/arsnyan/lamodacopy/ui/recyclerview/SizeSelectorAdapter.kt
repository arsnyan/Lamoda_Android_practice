package com.arsnyan.lamodacopy.ui.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsnyan.lamodacopy.data.Size
import com.arsnyan.lamodacopy.databinding.SizeSelectorCardViewBinding
import com.arsnyan.lamodacopy.ui.productview.ProductScreenViewModel

class SizeSelectorAdapter(private val viewModel: ProductScreenViewModel, private var sizes: List<Size>)
    : RecyclerView.Adapter<SizeSelectorAdapter.ViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: SizeSelectorCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val previousSelectedPosition = selectedPos
            selectedPos = bindingAdapterPosition
            viewModel.selectVariation(viewModel.variations[viewModel.currentId.value].color, sizes[selectedPos])
            notifyItemChanged(previousSelectedPosition)
            notifyItemChanged(selectedPos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        SizeSelectorCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = sizes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            holder.bind()
        }

        holder.itemView.isSelected = selectedPos == holder.bindingAdapterPosition
        if (position == selectedPos)
            holder.binding.root.setCardBackgroundColor(Color.parseColor("#d8d8d8"))
        else
            holder.binding.root.setCardBackgroundColor(Color.parseColor("#ffffff"))

        holder.binding.ruSize.text = sizes[position].sizeRu.toString()
        holder.binding.intSize.text = sizes[position].sizeInt

    }
}