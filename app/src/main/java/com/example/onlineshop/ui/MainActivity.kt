package com.example.onlineshop.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.onlineshop.R
import com.example.onlineshop.databinding.ActivityMainBinding
import com.example.onlineshop.ui.fragments.cart.ViewModelCart
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navController by lazy {
        findNavController(R.id.fragmentContainerView)
    }
    private lateinit var binding: ActivityMainBinding

    private val cartViewModel: ViewModelCart by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
        navigationInit()
        drawerBtn.setOnClickListener {
            openOrCloseDrawer()
        }
        searchBtn.setOnClickListener {
            navController.navigate(R.id.fragmentSearch)
        }
    }

    private fun navigationInit() = with(binding) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentCart, R.id.fragmentProfile, R.id.fragmentHome, R.id.fragmentCategory, -> {
                    showAppbar()
                    bottomNavigation.isVisible = true
                }
                R.id.fragmentLogin -> {
                    hideAppbar()
                    bottomNavigation.isVisible = true
                }
                else -> {
                    hideAppbar()
                    bottomNavigation.isVisible = false
                }
            }
        }
    }

    private fun hideAppbar() {
        try {
            supportActionBar?.hide()
        } catch (e: Exception) {}
    }

    private fun showAppbar() {
        try {
            supportActionBar?.show()
        } catch (e: Exception) {}
    }

    private fun observe() = with(binding) {

    }

    private fun openOrCloseDrawer(): Boolean = with(binding) {
        if (root.isDrawerOpen(navigationView)) {
            root.closeDrawer(navigationView)
            true
        } else {
            root.openDrawer(navigationView)
            false
        }
    }

    override fun onBackPressed() {
        with(binding) {
            if (root.isDrawerOpen(navigationView).not()) {
                super.onBackPressed()
            } else {
                openOrCloseDrawer()
            }
        }
    }

    override fun onStop() {
        cartViewModel.save()
        super.onStop()
    }
}
