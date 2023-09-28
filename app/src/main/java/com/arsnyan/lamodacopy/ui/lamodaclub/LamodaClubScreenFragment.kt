package com.arsnyan.lamodacopy.ui.lamodaclub

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.databinding.FragmentLamodaClubScreenBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LamodaClubScreenFragment : Fragment() {

    companion object {
        fun newInstance() = LamodaClubScreenFragment()
    }

    private lateinit var viewModel: LamodaClubScreenViewModel
    private var _binding: FragmentLamodaClubScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLamodaClubScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LamodaClubScreenViewModel::class.java]

        binding.btnExpandLamodaClubOptions.setOnClickListener {
            binding.linearLayoutLamodaClubExpandable.isVisible = !binding.linearLayoutLamodaClubExpandable.isVisible
            TransitionManager.beginDelayedTransition(binding.linearLayoutLamodaClubExpandable.parent as ViewGroup)
        }

        binding.btnBackLamodaClub.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}