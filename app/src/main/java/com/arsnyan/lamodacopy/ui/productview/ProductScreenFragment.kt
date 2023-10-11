package com.arsnyan.lamodacopy.ui.productview

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.transition.TransitionManager
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.databinding.FragmentProductScreenBinding
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

        lifecycleScope.launch {
            viewModel.product.collect {product ->
                binding.productBrand.text = product?.brand?.name
                binding.productCategory.text = product?.category?.name
                binding.preDiscountPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                val preDiscountPrice = product?.originalPrice
                val currentPrice = product?.currentPrice

                if (preDiscountPrice != null && currentPrice != null) {
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

                    val discountSize = (currentPrice * 100) / preDiscountPrice
                    binding.discountSize.text = resources.getString(R.string.discount_size_placeholder, discountSize)
                }

                if (product != null && product.availableItems > 0) {
                    binding.btnAddToCart.text =
                        resources.getString(R.string.lbl_add_to_cart, product.availableItems)
                } else {
                    binding.btnAddToCart.apply {
                        isFocusable = false
                        isClickable = false
                        setBackgroundColor(resources.getColor(R.color.disabled, resources.newTheme()))
                        text = resources.getString(R.string.lbl_out_of_stock)
                    }
                }
            }
        }


        val description = """
                id
                vendor_id...
                size
                48 UK
                color
                Black
                Shape
                Simple
            """.trimIndent()

        binding.additionalDesc.text = description

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