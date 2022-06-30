package com.example.onlineshop.ui.fragments.reviewlist

import android.os.Bundle
import android.view.View
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentReviewListBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver

class FragmentReviewList : FragmentConnectionObserver(R.layout.fragment_review_list) {
    private var _binding: FragmentReviewListBinding? = null
    private val binding: FragmentReviewListBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewListBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {

    }

    private fun observe() = with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {

    }
}