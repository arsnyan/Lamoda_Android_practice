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
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireView())
        binding.txtAppVersion.text = resources.getString(R.string.lbl_version, BuildConfig.VERSION_NAME)
        binding.btnOrders.setValue(viewModel.ordersCount)
        binding.cardviewUserDiscount.root.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_lamodaClubInfo)
        }
        binding.profileToolbar.notificationsButton.setOnClickListener {
            sharedViewModel.firebaseAuth.signOut()
            sharedViewModel.setUser(sharedViewModel.firebaseAuth.currentUser)
            navController.navigate(R.id.navigation_login)
        }

        lifecycleScope.launch {
            sharedViewModel.user.collect { user ->
                binding.profileToolbar.userEmailTextview.text = user?.email
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}