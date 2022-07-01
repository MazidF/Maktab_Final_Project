package com.example.onlineshop.ui.fragments.cart.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.onlineshop.R
import com.example.onlineshop.data.local.data_store.main.MainDataStore
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.databinding.FragmentCartBinding
import com.example.onlineshop.ui.fragments.cart.main.viewpager.SimpleViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentCart : FragmentConnectionObserver(R.layout.fragment_cart) {
    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding!!

    private val viewModel: ViewModelCart by activityViewModels()


    @Inject
    lateinit var mainDataStore: MainDataStore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCartBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        fragmentCurrentCartViewpager.apply {
            adapter = SimpleViewPagerAdapter(
                fragment = this@FragmentCart,
                list = listOf(
                    FragmentCurrentCart(),
                )
            )
            setupTab()
        }
    }

    private fun setupTab() = with(binding) {
        val tabNames = listOf(
            "سبد کنونی",
            "سفارشات قبلی"
        )
        TabLayoutMediator(
            fragmentCurrentCartTab, fragmentCurrentCartViewpager
        ) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }

    private fun observe() = with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {
        navController.navigate(FragmentCartDirections.actionFragmentCartToFragmentNetworkConnectionFailed())
    }
}