package com.arsnyan.lamodacopy.ui.catalog

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import coil.load
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.databinding.FragmentProductListBinding
import com.arsnyan.lamodacopy.databinding.ImageSliderPagerBinding
import com.arsnyan.lamodacopy.databinding.ProductCatalogCardViewBinding
import com.arsnyan.lamodacopy.utils.hasDaysPassed
import dagger.hilt.android.AndroidEntryPoint
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

        var adapter: ProductAdapter
        lifecycleScope.launch {
            viewModel.productList.collect { products ->
                adapter = ProductAdapter(products!!, view.context)
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

    class ProductAdapter(private val dataSet: List<Product>, private val context: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
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
                    binding.currentPrice.text = context.getString(R.string.lbl_formatted_price, it.currentPrice)
                    if(it.originalPrice > it.currentPrice) {
                        binding.originalPrice.apply {
                            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        }
                        binding.currentPrice.apply {
                            setTextColor(resources.getColor(R.color.branded_flame, resources.newTheme()))
                        }
                        val discount = 100 - ((it.currentPrice * 100)/it.originalPrice)
                        binding.badgeDiscount.text = context.getString(R.string.discount_size_placeholder, discount)
                    } else {
                        binding.originalPrice.visibility = View.GONE
                        binding.badgeClubDiscount.visibility = View.GONE
                        binding.badgeDiscount.visibility = View.GONE
                    }
                    binding.brand.text = it.brand?.name
                    binding.category.text = it.category?.name
                    binding.availableSizes.text = it.sizes.map { originalSizeObj -> originalSizeObj.sizeRu }.joinToString(", ")

                    binding.badgeNew.isVisible = !it.date.hasDaysPassed(30)
                }
            }
            holder.binding.root.setOnClickListener { view ->
                val action = ProductListFragmentDirections.actionNavigationProductListToNavigationProductScreen(dataSet[position].id.toInt())
                view.findNavController().navigate(action)
            }
        }

        private class ViewPagerAdapter(private val urls: List<String>) : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
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