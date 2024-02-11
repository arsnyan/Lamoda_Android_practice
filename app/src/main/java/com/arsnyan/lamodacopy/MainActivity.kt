package com.arsnyan.lamodacopy

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arsnyan.lamodacopy.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: SharedViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        //lifecycleScope.launch {
            //viewModel.user.collect { user ->
                //navView.menu.clear()
                //navView.inflateMenu(if (user == null) R.menu.bottom_nav_login_menu else R.menu.bottom_nav_menu)
        navView.inflateMenu(R.menu.bottom_nav_menu)

                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.navigation_home, R.id.navigation_catalog, R.id.navigation_cart, R.id.navigation_favourites, R.id.navigation_profile//if (user == null) R.id.navigation_login else R.id.navigation_profile
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)
            //}
        //}
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        viewModel.setUser(auth.currentUser)
        viewModel.firebaseAuth = auth
    }
}