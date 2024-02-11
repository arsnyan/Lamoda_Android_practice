package com.arsnyan.lamodacopy.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.SharedViewModel
import com.arsnyan.lamodacopy.data.Category
import com.arsnyan.lamodacopy.databinding.FragmentCatalogBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CatalogFragment : Fragment() {
    companion object {
        fun newInstance() = CatalogFragment()
    }

    private var _binding: FragmentCatalogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatalogViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.categories.collect {
                    val tabTitles = arrayOf(R.string.women, R.string.men)
                    binding.pager.adapter = CatalogPagerAdapter(this@CatalogFragment, it)
                    TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
                        tab.text = resources.getString(tabTitles[position])
                    }.attach()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.pager.adapter = null
        _binding = null
    }

    class CatalogPagerAdapter(fragment: Fragment, private val categories: List<Category>) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return if (position == 0)
                FemaleCatalogPagerFragment()
            else
                MaleCatalogPagerFragment()
        }
    }
}