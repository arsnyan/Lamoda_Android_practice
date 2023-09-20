package com.arsnyan.lamodacopy.ui.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.arsnyan.lamodacopy.BuildConfig
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.databinding.FragmentProfileBinding
import com.arsnyan.lamodacopy.ui.customview.ProfileSectionCardView
import com.arsnyan.lamodacopy.ui.lamodaclub.LamodaClubScreenFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.snackbar.Snackbar

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
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
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
        binding.cardviewReviewsQuestions.root.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_product_screen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}