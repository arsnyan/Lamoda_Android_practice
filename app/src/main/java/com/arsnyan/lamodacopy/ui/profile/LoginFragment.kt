package com.arsnyan.lamodacopy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.SharedViewModel
import com.arsnyan.lamodacopy.databinding.FragmentLoginBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        val tabTitles = arrayOf(R.string.lbl_login, R.string.lbl_registration)
        binding.pager.adapter = LoginPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            tab.text = resources.getString(tabTitles[position])
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class LoginPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return if (position == 0) {
                SignInPagerFragment()
            } else {
                SignUpPagerFragment()
            }
        }
    }
}