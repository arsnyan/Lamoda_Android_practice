package com.arsnyan.lamodacopy.ui.recyclerview

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsnyan.lamodacopy.data.ProductVariation
import com.arsnyan.lamodacopy.data.Size
import com.arsnyan.lamodacopy.databinding.SizeSelectorCardViewBinding
import com.arsnyan.lamodacopy.ui.productview.ProductScreenViewModel

class SizeSelectorAdapter(private val viewModel: ProductScreenViewModel, private var sizes: List<Size>)
    : RecyclerView.Adapter<SizeSelectorAdapter.ViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: SizeSelectorCardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                notifyItemChanged(selectedPos)
                selectedPos = adapterPosition
                notifyItemChanged(selectedPos)
                viewModel.selectSize(sizes[position])
                //Log.d("CHANGE SIZE", sizes[position].toString())
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
        println(sizes)
        holder.binding.ruSize.text = sizes[position].sizeRu.toString()
        holder.binding.intSize.text = sizes[position].sizeInt
    }
}