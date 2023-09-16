package com.arsnyan.lamodacopy.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arsnyan.lamodacopy.R

class DoubleSidedListAdapter(private val items: Array<Pair<String, Int>>) :
    RecyclerView.Adapter<DoubleSidedListAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView
        val valueView: TextView

        init {
            titleView = view.findViewById(R.id.double_sided_list_item_title)
            valueView = view.findViewById(R.id.double_sided_list_item_value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.double_sided_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleView.text = items[position].first
        if (items[position].second > 0)
            holder.valueView.text = items[position].second.toString()
    }

    override fun getItemCount(): Int = items.size

}