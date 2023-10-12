package com.arsnyan.lamodacopy.ui.recyclerview

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsnyan.lamodacopy.data.Size
import com.arsnyan.lamodacopy.databinding.SizeSelectorCardViewBinding
import com.arsnyan.lamodacopy.ui.productview.ProductScreenViewModel

class SizeSelectorAdapter(private val sizes: List<Size>, private val viewModel: ProductScreenViewModel) : RecyclerView.Adapter<SizeSelectorAdapter.ViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: SizeSelectorCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                notifyItemChanged(selectedPos)
                selectedPos = adapterPosition
                notifyItemChanged(selectedPos)
                viewModel.selectItem(selectedPos)
                Log.d("ITEM", selectedPos.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        SizeSelectorCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = sizes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.isSelected = selectedPos == holder.bindingAdapterPosition
        if (position == selectedPos)
            holder.binding.root.setCardBackgroundColor(Color.parseColor("#d8d8d8"))
        else
            holder.binding.root.setCardBackgroundColor(Color.parseColor("#ffffff"))

        with(holder.binding) {
            ruSize.text = sizes[position].sizeRu.toString()
            intSize.text = sizes[position].sizeInt
        }
    }

    fun getSelectedItem(): Int = selectedPos
}