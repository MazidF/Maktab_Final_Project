package com.example.onlineshop.ui.fragments.productinfo.viewpager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.example.onlineshop.R
import com.example.onlineshop.databinding.FragmentImageViewerBinding
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.utils.loadImageInto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentImageViewer : Fragment(R.layout.fragment_image_viewer) {
    private var _binding: FragmentImageViewerBinding? = null
    private val binding: FragmentImageViewerBinding
        get() = _binding!!


    private lateinit var image: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.getString("image")?.let {
            image = it
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("image", image)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImageViewerBinding.bind(view)

        observe()
    }

    fun setImage(url: String) {
        image = url
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            loadImageInto(image, imageViewerImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}