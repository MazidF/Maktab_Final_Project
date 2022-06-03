package com.example.onlineshop.ui.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentSlideViewerBinding
import com.example.onlineshop.utils.loadImageInto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentSlideViewer : Fragment(R.layout.fragment_slide_viewer) {
    private var _binding: FragmentSlideViewerBinding? = null
    private val binding: FragmentSlideViewerBinding
        get() = _binding!!


    private val image = MutableStateFlow<String?>(null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSlideViewerBinding.bind(view)

        observe()
    }

    fun setImage(url: String) {
        lifecycleScope.launch {
            image.emit(url)
        }
    }

    private fun observe() = with(binding) {
        /*launchOnState(Lifecycle.State.CREATED)*/
        lifecycleScope.launch {
            image.collectLatest {
                it?.let {
                    loadImageInto(it, imageSliderImage)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}