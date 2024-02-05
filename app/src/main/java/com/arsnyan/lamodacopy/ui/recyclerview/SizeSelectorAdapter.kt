package com.arsnyan.lamodacopy.ui.recyclerview

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsnyan.lamodacopy.data.ProductVariation
import com.arsnyan.lamodacopy.databinding.SizeSelectorCardViewBinding
import com.arsnyan.lamodacopy.ui.productview.ProductScreenViewModel

class SizeSelectorAdapter(
    private val variation: List<ProductVariation>,
    private val viewModel: ProductScreenViewModel
) : RecyclerView.Adapter<SizeSelectorAdapter.ViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: SizeSelectorCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                notifyItemChanged(selectedPos)
                selectedPos = adapterPosition
                notifyItemChanged(selectedPos)
                viewModel.selectSize(selectedPos, variation[selectedPos].id)
                Log.d("ITEM", selectedPos.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        SizeSelectorCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = variation.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.isSelected = selectedPos == holder.bindingAdapterPosition
        if (position == selectedPos)
            holder.binding.root.setCardBackgroundColor(Color.parseColor("#d8d8d8"))
        else
            holder.binding.root.setCardBackgroundColor(Color.parseColor("#ffffff"))

        with(holder.binding) {
            ruSize.text = variation[position].size.sizeRu.toString()
            intSize.text = variation[position].size.sizeInt
        }
    }

    fun getSelectedItem(): Int = selectedPos
}