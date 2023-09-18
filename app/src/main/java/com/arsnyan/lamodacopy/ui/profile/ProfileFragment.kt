package com.arsnyan.lamodacopy.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.ui.customview.ProfileSectionCardView
import com.arsnyan.lamodacopy.ui.lamodaclub.LamodaClubScreenFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import com.google.android.material.shape.MaterialShapeDrawable

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var btnOrders: ProfileSectionCardView
    private lateinit var cardUserDiscount: MaterialCardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        btnOrders = view.findViewById(R.id.btn_orders)
        cardUserDiscount = view.findViewById(R.id.cardview_user_discount)
        val navController = Navigation.findNavController(requireView())

        btnOrders.setValue(viewModel.ordersCount)
        cardUserDiscount.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_lamodaClubInfo)
        }
    }
}