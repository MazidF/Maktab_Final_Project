package com.example.onlineshop.ui.fragments.cart.main.viewpager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SimpleViewPagerAdapter(
    fragment: Fragment,
    val list: List<Fragment>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = list.size

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }



}
