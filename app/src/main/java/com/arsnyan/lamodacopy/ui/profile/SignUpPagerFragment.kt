package com.arsnyan.lamodacopy.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.SharedViewModel
import com.arsnyan.lamodacopy.databinding.PagerSignUpBinding
import com.arsnyan.lamodacopy.utils.ExtensionFunctions.interactive
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpPagerFragment : Fragment() {
    private val viewModel: SharedViewModel by activityViewModels()

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
                makeAccountBtn.interactive(false)
                viewModel.firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            parentFragment?.findNavController()?.navigate(R.id.navigation_profile)
                            viewModel.setUser(viewModel.firebaseAuth.currentUser)
                            viewModel.firebaseAuth.currentUser!!.uid
                        }
                        else {
                            makeAccountBtn.interactive(true)
                            Toast.makeText(context, "Error making an account", Toast.LENGTH_SHORT).show()
                            Log.e("FIREBASE_AUTH", task.exception.toString())
                        }
                }
            }
        }
        return binding.root
    }
}