package com.arsnyan.lamodacopy.ui.productview

import android.content.Context
import android.graphics.Paint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginStart
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

    private val descriptionList = mutableListOf<ListItem>()

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

        binding.discountSize.text = resources.getString(R.string.discount_size_placeholder, discountSize)

        descriptionList.add(ListItem("articule", "1"))
        descriptionList.add(ListItem("sostav", "2"))
        descriptionList.add(ListItem("size", "3"))
        descriptionList.add(ListItem("articule", "RTFLA1321351"))
        val descriptionAdapter = DescriptionArrayAdapter(view.context, , descriptionList)
        binding.additionalDesc.adapter = descriptionAdapter

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

    data class ListItem(val top: String, val bottom: String)

    class DescriptionArrayAdapter(val context: Context, resource: Int, objects: List<ListItem>) : ArrayAdapter<ListItem>(context, resource, objects) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val listItem = getItem(position)

            var view = convertView
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.description_list_item, parent, false)
            }

            val title = view?.findViewById<TextView>(R.id.title)
            val body = view?.findViewById<TextView>(R.id.body)
            title?.text = listItem?.top
            body?.text = listItem?.bottom

            return view!!
        }
    }
}