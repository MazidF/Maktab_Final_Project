package com.example.onlineshop.ui.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentLoadableBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class FragmentLoadable(
    private val resource: Int,
    private val hasRefresh: Boolean = true,
) : Fragment(R.layout.fragment_loadable) {

    private var _binding: FragmentLoadableBinding? = null
    private val binding: FragmentLoadableBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoadableBinding.bind(view)

        initView()
    }

    private fun initView() = with(binding) {
        fragmentLoadableRefreshLayout.isEnabled = hasRefresh
        val child = View.inflate(requireContext(), resource, null).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1f)
        }
        fragmentLoadableRoot.addView(child)
    }

    private fun startRefreshing() = with(binding) {
        if (hasRefresh) {
            fragmentLoadableRefreshLayout.isRefreshing = true
            refresh()
        }
    }

    abstract fun refresh()

    private fun stopRefreshing() = with(binding) {
        if (hasRefresh) {
            fragmentLoadableRefreshLayout.isRefreshing = false
        }
    }

    protected fun startLoading() = with(binding) {
        fragmentLoadableLottie.isVisible = true
        fragmentLoadableRefreshLayout.isVisible = false
    }

    protected fun stopLoading() = with(binding) {
        fragmentLoadableLottie.isVisible = false
        fragmentLoadableRefreshLayout.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}