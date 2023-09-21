package com.arsnyan.lamodacopy.ui.productview

import android.graphics.Paint
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionManager
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.databinding.FragmentProductScreenBinding
import kotlin.math.floor

class ProductScreenFragment : Fragment() {

    companion object {
        fun newInstance() = ProductScreenFragment()
    }

    private lateinit var viewModel: ProductScreenViewModel
    private var _binding: FragmentProductScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductScreenBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductScreenViewModel::class.java]

        binding.preDiscountPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

        val preDiscountPrice = Integer.parseInt(binding.preDiscountPrice.text.toString())
        val currentPrice = Integer.parseInt(binding.currentPrice.text.toString())
        val lamodaDiscount = 799
        val lamodaClubDiscount = 0
        val discountSize = 100 - floor(((currentPrice * 100) / preDiscountPrice).toDouble()).toInt()

        if (preDiscountPrice > currentPrice) {
            binding.preDiscountPrice.visibility = View.VISIBLE
            binding.currentPrice.apply {
                setTextColor(resources.getColor(R.color.branded_flame, resources.newTheme()))
            }
        } else {
            binding.preDiscountPrice.visibility = View.GONE
            binding.discountSize.visibility = View.GONE
        }

        val description = """
                id
                RTL12312912JDAS
                size
                48 UK
                color
                Black
                Shape
                Simple
            """.trimIndent()

        binding.additionalDesc.text = description

        binding.discountSize.text = resources.getString(R.string.discount_size_placeholder, discountSize)

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

    fun color(color: Int, vararg content: CharSequence): CharSequence = apply(content, ForegroundColorSpan(color))

    private fun apply(content: Array<out CharSequence>, vararg tags: Any): CharSequence {
        return SpannableStringBuilder().apply {
            openTags(tags)
            content.forEach { charSequence -> append(charSequence) }
            closeTags(tags)
        }
    }

    private fun Spannable.openTags(tags: Array<out Any>) {
        tags.forEach {tag ->
            setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK)
        }
    }

    private fun Spannable.closeTags(tags: Array<out Any>) {
        tags.forEach { tag ->
            if (length > 0) {
                setSpan(tag, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            } else {
                removeSpan(tag)
            }
        }
    }
}