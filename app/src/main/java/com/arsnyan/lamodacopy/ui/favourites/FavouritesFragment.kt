package com.arsnyan.lamodacopy.ui.favourites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.arsnyan.lamodacopy.SharedViewModel
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.Size
import com.arsnyan.lamodacopy.databinding.FragmentFavouritesBinding
import com.arsnyan.lamodacopy.databinding.ImageSliderPagerBinding
import com.arsnyan.lamodacopy.databinding.ProductCatalogCardViewBinding
import com.arsnyan.lamodacopy.ui.catalog.ProductListFragmentDirections
import com.arsnyan.lamodacopy.ui.recyclerview.ProductAdapter
import com.arsnyan.lamodacopy.utils.ExtensionFunctions.haveDaysPassed
import com.arsnyan.lamodacopy.utils.ExtensionFunctions.setDiscountVisibility
import com.arsnyan.lamodacopy.utils.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavouritesFragment()
    }

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    if (user != null) {
                        with(binding) {
                            productList.adapter = ProductAdapter(listOf(), view.context, object : OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    val action = ProductListFragmentDirections
                                        .actionNavigationProductListToNavigationProductScreen(position)
                                    findNavController().navigate(action)
                                }
                            })
                        }
                    }
                }
            }
        }
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
                    println(it)
                    if (it.variations.maxOf { variations -> variations.stock } == 0)
                        binding.root.alpha = 0.75f
                    binding.imageCarousel.adapter = ViewPagerAdapter(it.variations[0].urls)
                    it.setDiscountVisibility(context, 0, binding.originalPrice, binding.currentPrice,
                        binding.badgeDiscount, binding.badgeClubDiscount)
                    binding.brand.text = it.brand.name
                    binding.category.text = it.category.name
                    val allSizes = mutableSetOf<Size>()
                    it.variations.forEach { variations -> allSizes.add(variations.size) }
                    binding.availableSizes.text = allSizes.map { originalSizeObj -> originalSizeObj.sizeRu }.joinToString(", ")

                    binding.badgeNew.isVisible = !it.variations
                        .maxBy { variation -> variation.createdAt }
                        .createdAt
                        .haveDaysPassed(30)
                }
            }
            holder.binding.root.setOnClickListener { view ->
                val action = ProductListFragmentDirections
                    .actionNavigationProductListToNavigationProductScreen(4)
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