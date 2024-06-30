package com.arsnyan.lamodacopy.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.databinding.FragmentProductListBinding
import com.arsnyan.lamodacopy.ui.recyclerview.ProductAdapter
import com.arsnyan.lamodacopy.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    companion object {
        fun newInstance() = ProductListFragment()
    }

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: ProductListViewModel by viewModels()

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        val adapter = ProductAdapter(view.context, object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val action = ProductListFragmentDirections
                    .actionNavigationProductListToNavigationProductScreen(position)
                findNavController().navigate(action)
            }
        })
        binding.productList.adapter = adapter
        val manager = GridLayoutManager(context, 2)
        manager.orientation = VERTICAL
        binding.productList.layoutManager = manager

        lifecycleScope.launch {
            viewModel.productList.collectLatest { products ->
                adapter.submitData(products)

                binding.category.text = viewModel.category?.name
                //binding.leftItems.text = resources.getString(R.string.left_items_counter, products.)
            }
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                // TODO (Add loading animation like spinner or fake loading data)
            } else {
                // TODO (Then hide it back)
            }
        }
    }

    // TODO (Fix this shit already, it breaks whenever you interrupt the coroutine)
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}