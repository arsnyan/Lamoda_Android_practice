package com.arsnyan.lamodacopy.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arsnyan.lamodacopy.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpPagerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pager_sign_up, container, false)
    }
}