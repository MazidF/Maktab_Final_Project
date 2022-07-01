package com.example.onlineshop.ui.fragments.reviewmaker

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.example.onlineshop.R
import com.example.onlineshop.data.model.ProductReview
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.databinding.FragmentReviewMakerBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.fragments.reviewlist.ViewModelReview
import com.example.onlineshop.ui.model.Rate
import com.example.onlineshop.utils.launchOnState
import kotlinx.coroutines.flow.Flow

class FragmentReviewMaker : FragmentConnectionObserver(R.layout.fragment_review_maker) {
    private val viewModel: ViewModelReview by viewModels()
    private val args: FragmentReviewMakerArgs by navArgs()

    private var _binding: FragmentReviewMakerBinding? = null
    private val binding: FragmentReviewMakerBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewMakerBinding.bind(view)

        initView()
    }

    private fun initView() = with(binding) {
        fragmentReviewMakerSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                fragmentReviewMakerTitle.text = Rate.get(progress + 1).text
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        fragmentReviewMakerSendBtn.setOnClickListener {
            if (check()) {
                val review = fragmentReviewMakerReview.text.toString().trim()
                val flow = viewModel.createReview(
                    productId = args.productId,
                    review = review,
                    rate = fragmentReviewMakerSeekbar.progress + 1,
                    customerName = fragmentReviewMakerCheckbox.isChecked.not(),
                )
                observeResult(flow)
            }
        }
    }

    private fun observeResult(flow: Flow<Resource<ProductReview>>) {
        launchOnState(Lifecycle.State.STARTED) {
            flow.collect {
                when(it) {
                    is Resource.Fail -> {
                        stopLoading()
                        Toast.makeText(
                            requireContext(),
                            "دوباره تلاش کنید.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Loading -> {
                        startLoading()
                    }
                    is Resource.Success -> {
                        back()
                    }
                }
            }
        }
    }

    private fun startLoading() = with(binding) {
        fragmentReviewMakerSendBtn.visibility = View.INVISIBLE
        fragmentReviewMakerSendBtn.isEnabled = false
    }

    private fun stopLoading() = with(binding) {
        fragmentReviewMakerSendBtn.visibility = View.VISIBLE
        fragmentReviewMakerSendBtn.isEnabled = true
    }

    private fun check(): Boolean = with(binding) {
        fragmentReviewMakerReview.text.isNotBlank()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun navigateToConnectionFailed() {

    }
}