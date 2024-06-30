package com.arsnyan.lamodacopy.ui.productview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.databinding.FragmentProductScreenBinding
import com.arsnyan.lamodacopy.ui.recyclerview.ColorSelectorAdapter
import com.arsnyan.lamodacopy.ui.recyclerview.ImageCarouselMultipleShowAdapter
import com.arsnyan.lamodacopy.ui.recyclerview.SizeSelectorAdapter
import com.arsnyan.lamodacopy.utils.ExtensionFunctions.interactive
import com.arsnyan.lamodacopy.utils.ExtensionFunctions.setDiscountVisibility
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
            viewModel.product.collect { product ->
                if (product != null) {
                    val variations = viewModel.variations

                    binding.productBrand.text = product.brand.name
                    binding.productCategory.text = product.category.name

                    val colors = variations.map { it.color }.toSet().toList()
                    val colorsAdapter = ColorSelectorAdapter(viewModel, colors)
                    binding.colorOptionsList.apply {
                        adapter = colorsAdapter
                        val manager = LinearLayoutManager(context)
                        manager.orientation = RecyclerView.HORIZONTAL
                        layoutManager = manager
                    }

                    val sizesByColor = variations.map { it.size }.toSet().toList()
                    val sizesAdapter = SizeSelectorAdapter(viewModel, sizesByColor)
                    binding.sizeSelector.apply {
                        adapter = sizesAdapter
                        val manager = LinearLayoutManager(context)
                        manager.orientation = RecyclerView.HORIZONTAL
                        layoutManager = manager
                    }

                    binding.productDescription.text = product.description

                    viewModel.currentId.collect { id ->
                        product.setDiscountVisibility(
                            requireActivity(), id, binding.originalPrice, binding.currentPrice,
                            binding.badgeDiscount
                        )

                        if (product.variations[id].stock > 0) {
                            binding.btnAddToCart.apply {
                                text =
                                    resources.getString(
                                        R.string.lbl_add_to_cart,
                                        product.variations[id].stock
                                    )
                                interactive(true)
                                setBackgroundColor(
                                    resources.getColor(
                                        R.color.black,
                                        resources.newTheme()
                                    )
                                )
                            }
                        } else {
                            binding.btnAddToCart.apply {
                                interactive(false)
                                setBackgroundColor(
                                    resources.getColor(
                                        R.color.disabled,
                                        resources.newTheme()
                                    )
                                )
                                text = resources.getString(R.string.lbl_out_of_stock)
                            }
                        }

                        binding.productCarousel.apply {
                            adapter =
                                ImageCarouselMultipleShowAdapter(product.variations[id].urls)
                            val manager = LinearLayoutManager(context)
                            manager.orientation = RecyclerView.HORIZONTAL
                            layoutManager = manager
                            onFlingListener = null
                        }

                        val snapHelper = PagerSnapHelper()
                        snapHelper.attachToRecyclerView(binding.productCarousel)

                        binding.additionalDesc.text = resources.getString(
                            R.string.additional_description,
                            product.id.toString(),
                            resources.getString(product.variations[id].color.stringId),
                            resources.getString(product.variations[id].pattern.resId)
                        )

                        val selectedSize = variations[id].size
                        binding.sizeDescription.apply {
                            val attributesList = mutableListOf<String>()
                            with(selectedSize) {
                                attributesList += resources.getString(
                                    R.string.lbl_hips,
                                    hips
                                )
                                attributesList += resources.getString(
                                    R.string.lbl_waist,
                                    waist
                                )
                                attributesList += resources.getString(
                                    R.string.lbl_bust,
                                    bust
                                )
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
                    }
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