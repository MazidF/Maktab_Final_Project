package com.example.onlineshop.ui.fragments.product_info.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentImageViewerBinding
import com.example.onlineshop.utils.loadImageInto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentImageViewer : Fragment(R.layout.fragment_image_viewer) {
    private var _binding: FragmentImageViewerBinding? = null
    private val binding: FragmentImageViewerBinding
        get() = _binding!!


    private val image = MutableStateFlow<String?>(null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImageViewerBinding.bind(view)

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
                    loadImageInto(it, imageViewerImage)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}