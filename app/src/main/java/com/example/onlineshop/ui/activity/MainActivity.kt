package com.example.onlineshop.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.onlineshop.R
import com.example.onlineshop.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navController by lazy {
        Runnable {

        }
        findNavController(R.id.fragmentContainerView)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() = with(binding) {
        setSupportActionBar(toolbar)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentHome -> {
                    showAppbar()
                    bottomNavigation.isVisible = true
                }
                else -> {
                    hideAppbar()
                    bottomNavigation.isVisible = false
                }
            }
        }
        drawerBtn.setOnClickListener {
            openOrCloseDrawer()
        }
        searchBtn.setOnClickListener {

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
}
