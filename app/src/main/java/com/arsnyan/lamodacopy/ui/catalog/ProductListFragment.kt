package com.arsnyan.lamodacopy.ui.catalog

import android.graphics.Paint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.marginStart
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.adapter.FragmentStateAdapter
import coil.load
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.databinding.FragmentProductListBinding
import com.arsnyan.lamodacopy.databinding.ImageSliderPagerBinding
import com.arsnyan.lamodacopy.databinding.ProductCatalogCardViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    companion object {
        fun newInstance() = ProductListFragment()
    }

    private lateinit var viewModel: ProductListViewModel
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
        viewModel = ViewModelProvider(this)[ProductListViewModel::class.java]
        var adapter: ProductAdapter
        lifecycleScope.launch {
            viewModel.productList.collect {products ->
                adapter = ProductAdapter(products!!)
                binding.productList.adapter = adapter
            }
        }
        val manager = GridLayoutManager(context, 2)
        manager.orientation = VERTICAL
        binding.productList.layoutManager = manager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class ProductAdapter(private val dataSet: List<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
        inner class ViewHolder(val binding: ProductCatalogCardViewBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
            ProductCatalogCardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        override fun getItemCount(): Int = dataSet.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder) {
                dataSet[position].let {
                    binding.imageCarousel.adapter = ViewPagerAdapter(it.urls)
                    binding.originalPrice.text = it.originalPrice.toString()
                    binding.currentPrice.text = "${it.currentPrice.toString()} ₽"
                    if(it.originalPrice > it.currentPrice) {
                        binding.originalPrice.apply {
                            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        }
                        binding.currentPrice.apply {
                            setTextColor(resources.getColor(R.color.branded_flame, resources.newTheme()))
                        }
                        val discount = (100 - ((it.currentPrice * 100)/it.originalPrice)).toString()
                        binding.badgeDiscount.text = "—$discount%"
                    } else {
                        binding.originalPrice.visibility = View.GONE
                        binding.badgeClubDiscount.visibility = View.GONE
                        binding.badgeDiscount.visibility = View.GONE
                    }
                    binding.brand.text = it.brand
                    binding.category.text = it.category
                    binding.availableSizes.text = it.sizes
                }
            }
        }

        private class ViewPagerAdapter(private val urls: Array<String>) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
            inner class ViewHolder(val binding: ImageSliderPagerBinding) : RecyclerView.ViewHolder(binding.root)

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
                ImageSliderPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            override fun getItemCount(): Int = urls.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.binding.image.load(urls[position])
            }
        }
    }
}