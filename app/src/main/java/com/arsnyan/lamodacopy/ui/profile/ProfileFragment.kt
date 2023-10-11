package com.arsnyan.lamodacopy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.arsnyan.lamodacopy.BuildConfig
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
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
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        val navController = Navigation.findNavController(requireView())

        binding.txtAppVersion.text = resources.getString(R.string.lbl_version, BuildConfig.VERSION_NAME)
        binding.btnOrders.setValue(viewModel.ordersCount)
        binding.cardviewUserDiscount.root.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_lamodaClubInfo)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}