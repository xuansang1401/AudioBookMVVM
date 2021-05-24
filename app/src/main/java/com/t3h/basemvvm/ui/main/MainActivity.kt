package com.t3h.basemvvm.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.t3h.basemvvm.R
import com.t3h.basemvvm.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
//
//        val navHostFragment = supportFragmentManager
//                .findFragmentById(R.id.nav_host) as NavHostFragment?
//        NavigationUI.setupWithNavController(
//                binding.btNavHome,
//                navHostFragment!!.navController
//        )
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            navHostFragment.navController
        )
//        slide_fragment_host



    }
}