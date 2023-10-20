package com.arsnyan.lamodacopy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arsnyan.lamodacopy.BuildConfig
import com.arsnyan.lamodacopy.MainActivity
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.SharedViewModel
import com.arsnyan.lamodacopy.databinding.FragmentLoginBinding
import com.arsnyan.lamodacopy.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.utils.io.concurrent.shared

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentProfileBinding? = null
    private var _loginBinding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginBinding get() = _loginBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return if (sharedViewModel.isUserAnonymous()) {
            _loginBinding = FragmentLoginBinding.inflate(layoutInflater, container, false)

            val tabTitles = arrayOf(R.string.lbl_login, R.string.lbl_registration)
            loginBinding.pager.adapter = LoginPagerAdapter(this)
            TabLayoutMediator(loginBinding.tabs, loginBinding.pager) { tab, position ->
                tab.text = resources.getString(tabTitles[position])
            }.attach()

            loginBinding.root
        } else {
            _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

            val navController = Navigation.findNavController(requireView())
            binding.txtAppVersion.text = resources.getString(R.string.lbl_version, BuildConfig.VERSION_NAME)
            binding.btnOrders.setValue(viewModel.ordersCount)
            binding.cardviewUserDiscount.root.setOnClickListener {
                navController.navigate(R.id.action_navigation_profile_to_navigation_lamodaClubInfo)
            }

            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _loginBinding = null
    }

    inner class LoginPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return if (position == 0) {
                SignInPagerFragment()
            } else {
                SignUpPagerFragment(sharedViewModel)
            }
        }
    }
}