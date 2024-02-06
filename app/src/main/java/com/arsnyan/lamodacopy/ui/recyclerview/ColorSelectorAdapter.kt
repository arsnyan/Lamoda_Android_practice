package com.arsnyan.lamodacopy.ui.recyclerview

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arsnyan.lamodacopy.data.ProductVariation
import com.arsnyan.lamodacopy.databinding.ColorSelectorBinding
import com.arsnyan.lamodacopy.databinding.SizeSelectorCardViewBinding
import com.arsnyan.lamodacopy.ui.productview.ProductScreenViewModel

class ColorSelectorAdapter(
    private val viewModel: ProductScreenViewModel
) : RecyclerView.Adapter<ColorSelectorAdapter.ViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION

    inner class ViewHolder(val binding: ColorSelectorBinding) : RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ColorSelectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    fun getSelectedItem(): Int = selectedPos
}