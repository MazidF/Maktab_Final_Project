package com.example.onlineshop.ui.fragments.productinfo.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// TODO: change this
class ProductImageViewPagerAdapter(
    fragment: Fragment,
    private val fragments: List<Fragment>,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}