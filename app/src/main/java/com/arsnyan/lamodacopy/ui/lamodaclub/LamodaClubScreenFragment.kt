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
import com.arsnyan.lamodacopy.ui.IndicatorSliderLabelFormatter
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider

class LamodaClubScreenFragment : Fragment() {

    companion object {
        fun newInstance() = LamodaClubScreenFragment()
    }

    private lateinit var viewModel: LamodaClubScreenViewModel
    private lateinit var btnExpandClubOptions: MaterialButton
    private lateinit var layoutClubCardView: ConstraintLayout
    private lateinit var linearLayoutExpandable: LinearLayout
    private lateinit var backButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lamoda_club_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnExpandClubOptions = view.findViewById(R.id.btn_expand_lamodaClubOptions)
        layoutClubCardView = view.findViewById(R.id.layout_clubCard)
        linearLayoutExpandable = view.findViewById(R.id.linearLayout_lamodaClub_expandable)
        backButton = view.findViewById(R.id.btn_back_lamodaClub)

        btnExpandClubOptions.setOnClickListener {
            linearLayoutExpandable.isVisible = !linearLayoutExpandable.isVisible
            TransitionManager.beginDelayedTransition(linearLayoutExpandable.parent as ViewGroup)
        }

        backButton.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LamodaClubScreenViewModel::class.java)
        // TODO: Use the ViewModel
    }

}