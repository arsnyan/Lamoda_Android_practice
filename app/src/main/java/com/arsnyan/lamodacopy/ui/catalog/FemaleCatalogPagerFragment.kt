package com.arsnyan.lamodacopy.ui.catalog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsnyan.lamodacopy.SharedViewModel
import com.arsnyan.lamodacopy.data.Category
import com.arsnyan.lamodacopy.databinding.FragmentCatalogPagerBinding
import com.arsnyan.lamodacopy.databinding.SimpleListItemBinding
import kotlinx.coroutines.launch

class FemaleCatalogPagerFragment() : Fragment() {
    private var _binding: FragmentCatalogPagerBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogPagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.categories.collect { list ->
                    binding.list.adapter = CategoryAdapter(list.filter { it.gender == "f" })
                    val manager = LinearLayoutManager(context)
                    manager.orientation = RecyclerView.VERTICAL
                    binding.list.layoutManager = manager
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class MaleCatalogPagerFragment : Fragment() {
    private var _binding: FragmentCatalogPagerBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogPagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.categories.collect { list ->
                    binding.list.adapter = CategoryAdapter(list.filter { it.gender == "m" })
                    val manager = LinearLayoutManager(context)
                    manager.orientation = RecyclerView.VERTICAL
                    binding.list.layoutManager = manager
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class CategoryAdapter(private val categories: List<Category>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: SimpleListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        SimpleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.label.text = categories[position].name
        holder.itemView.setOnClickListener {
            Log.i("Category ID", categories[position].id.toString())
            val action = CatalogFragmentDirections
                .actionNavigationCatalogToNavigationProductList(categories[position].id)
            it.findNavController().navigate(action)
        }
    }
}