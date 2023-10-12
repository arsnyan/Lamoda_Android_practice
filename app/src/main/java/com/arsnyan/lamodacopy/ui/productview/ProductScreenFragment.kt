package com.arsnyan.lamodacopy.ui.productview

import android.graphics.Paint
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.databinding.FragmentProductScreenBinding
import com.arsnyan.lamodacopy.ui.recyclerview.ImageCarouselMultipleShowAdapter
import com.arsnyan.lamodacopy.ui.recyclerview.MarginItemDecoration
import com.arsnyan.lamodacopy.ui.recyclerview.SizeSelectorAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductScreenFragment : Fragment() {
    companion object {
        fun newInstance() = ProductScreenFragment()
    }

    private var _binding: FragmentProductScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: ProductScreenViewModel by viewModels()

        binding.btnClose.setOnClickListener { findNavController().navigateUp() }

        lifecycleScope.launch {
            viewModel.product.collect {product ->
                if (product != null) {
                    binding.productBrand.text = product.brand?.name
                    binding.productCategory.text = product.category?.name
                    binding.preDiscountPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                    val preDiscountPrice = product.originalPrice
                    val currentPrice = product.currentPrice

                    binding.preDiscountPrice.text = preDiscountPrice.toString()
                    binding.currentPrice.text = currentPrice.toString()

                    if (preDiscountPrice > currentPrice) {
                        binding.preDiscountPrice.apply {
                            visibility = View.VISIBLE
                        }
                        binding.currentPrice.apply {
                            setTextColor(resources.getColor(R.color.branded_flame, resources.newTheme()))
                        }
                    } else {
                        binding.preDiscountPrice.visibility = View.GONE
                        binding.discountSize.visibility = View.GONE
                    }

                    val discountSize = 100 - ((currentPrice * 100) / preDiscountPrice)
                    binding.discountSize.text = resources.getString(R.string.discount_size_placeholder, discountSize)

                    if (product.availableItems > 0) {
                        binding.btnAddToCart.apply {
                            text =
                                resources.getString(R.string.lbl_add_to_cart, product.availableItems)
                            isFocusable = true
                            isClickable = true
                            setBackgroundColor(
                                resources.getColor(
                                    R.color.black,
                                    resources.newTheme()
                                )
                            )
                        }
                    } else {
                        binding.btnAddToCart.apply {
                            isFocusable = false
                            isClickable = false
                            setBackgroundColor(resources.getColor(R.color.disabled, resources.newTheme()))
                            text = resources.getString(R.string.lbl_out_of_stock)
                        }
                    }

                    binding.productCarousel.apply {
                        adapter = ImageCarouselMultipleShowAdapter(product.urls)
                        val manager = LinearLayoutManager(context)
                        manager.orientation = RecyclerView.HORIZONTAL
                        layoutManager = manager
                    }

                    val snapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(binding.productCarousel)

                    val sizesAdapter = SizeSelectorAdapter(product.sizes, viewModel)
                    binding.sizeSelector.apply {
                        adapter = sizesAdapter
                        val manager = LinearLayoutManager(context)
                        manager.orientation = RecyclerView.HORIZONTAL
                        addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.dimen_grid_margin_step)))
                        layoutManager = manager
                    }
                    viewModel.selectedSizeItem.observe(viewLifecycleOwner) { itemId ->
                        Log.d("ITEM", itemId.toString())
                        if (itemId != -1) {
                            val selectedSize = product.sizes[sizesAdapter.getSelectedItem()]
                            binding.sizeDescription.apply {
                                val attributesList = mutableListOf<String>()
                                with(selectedSize) {
                                    if (hips != null)
                                        attributesList += resources.getString(
                                            R.string.lbl_hips,
                                            hips
                                        )
                                    if (waist != null)
                                        attributesList += resources.getString(
                                            R.string.lbl_waist,
                                            waist
                                        )
                                    if (bust != null)
                                        attributesList += resources.getString(
                                            R.string.lbl_bust,
                                            bust
                                        )
                                    if (feetLength != null)
                                        attributesList += resources.getString(
                                            R.string.lbl_feet,
                                            feetLength.toString()
                                        )
                                }
                                text = attributesList.joinToString(", ")
                                    .replaceFirstChar { it.uppercase() }
                                isVisible = true
                                TransitionManager.beginDelayedTransition(binding.constraintLayout as ViewGroup)
                            }
                        } else {
                            binding.sizeDescription.isVisible = false
                        }
                    }

                    binding.additionalDesc.text = resources.getString(
                        R.string.additional_description,
                        product.vendorId,
                        resources.getString(product.color.stringId),
                        resources.getString(product.pattern.resId)
                    )
                }
            }
        }

        binding.btnExpandDesc.setOnClickListener {
            if (binding.additionalDesc.visibility == View.GONE) {
                binding.additionalDesc.visibility = View.VISIBLE
                binding.btnExpandDesc.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_expand_less_24, resources.newTheme())
                TransitionManager.beginDelayedTransition(binding.constraintLayout as ViewGroup)
            } else {
                binding.additionalDesc.visibility = View.GONE
                binding.btnExpandDesc.icon = ResourcesCompat.getDrawable(resources, R.drawable.baseline_expand_more_24, resources.newTheme())
                TransitionManager.beginDelayedTransition(binding.constraintLayout as ViewGroup)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}