package com.arsnyan.lamodacopy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.SharedViewModel
import com.arsnyan.lamodacopy.databinding.PagerSignInBinding
import com.arsnyan.lamodacopy.databinding.PagerSignUpBinding
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpPagerFragment(val viewModel: SharedViewModel) : Fragment() {
    private var _binding: PagerSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PagerSignUpBinding.inflate(inflater, container, false)
        with (binding) {
            makeAccountBtn.setOnClickListener {
                val email = emailField.editText!!.text.toString()
                val password = passwordField.editText!!.text.toString()
                viewModel.firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful)
                            parentFragment?.findNavController()?.navigateUp()
                }
            }
        }
        return binding.root
    }
}