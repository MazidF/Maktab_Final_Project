package com.example.onlineshop.ui.fragments.reviewlist

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.onlineshop.R
import com.example.onlineshop.data.model.ProductReview
import com.example.onlineshop.databinding.BigProductReviewBinding
import com.example.onlineshop.databinding.FragmentReviewListBinding
import com.example.onlineshop.ui.fragments.FragmentConnectionObserver
import com.example.onlineshop.ui.fragments.adapter.ItemPagingAdapter
import com.example.onlineshop.ui.fragments.adapter.diff_callback.ProductReviewDiffItemCallback
import com.example.onlineshop.ui.fragments.productinfo.review.ProductReviewAdapter
import com.example.onlineshop.ui.model.Rate
import com.example.onlineshop.utils.failedDialog
import com.example.onlineshop.utils.launchOnState
import com.example.onlineshop.utils.setHtmlText
import com.example.onlineshop.widgit.Bindable
import kotlinx.coroutines.flow.collect

class FragmentReviewList : FragmentConnectionObserver(R.layout.fragment_review_list) {
    private var _binding: FragmentReviewListBinding? = null
    private val binding: FragmentReviewListBinding
        get() = _binding!!

    private val viewModel: ViewModelReview by viewModels()
    private val args: FragmentReviewListArgs by navArgs()
    private lateinit var reviewAdapter: ItemPagingAdapter<ProductReview>

    private fun getAdapter(): ItemPagingAdapter<ProductReview> {
        return object : ItemPagingAdapter<ProductReview>(
            ProductReviewDiffItemCallback(),
            this::onItemClick,
        ) {
            override fun onCreateBindable(
                parent: ViewGroup,
                viewType: Int
            ): Bindable<ProductReview> {
                return object : Bindable<ProductReview> {
                    private val binding = BigProductReviewBinding.inflate(
                        layoutInflater, parent, false
                    )

                    override fun bind(t: ProductReview?): Unit = with(binding) {
                        t?.let { item ->
                            val rate = Rate.get(item.rating)
                            bigProductReviewName.text = item.reviewer
                            bigProductReviewText.setHtmlText(item.review)
                            bigProductReviewRateRoot.background?.setTint(rate.getColor(root.context))
                            bigProductReviewDate.text = item.date
                            bigProductReviewRate.text = item.rating.toString()
                            bigProductReviewQuality.text = rate.text
                        }
                    }

                    override fun getView(): View {
                        return binding.root
                    }
                }
            }
        }
    }

    private fun onItemClick(item: ProductReview) {
        if (item.productId == args.productId) {
            showPopupMenu(item)
        }
    }

    private fun showPopupMenu(item: ProductReview) {
//        PopupMenu(requireContext(), )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentReviewListBinding.bind(view)

        initView()
        observe()
    }

    private fun initView() = with(binding) {
        reviewAdapter = getAdapter()
        fragmentReviewsList.adapter = reviewAdapter
    }

    private fun observe() = with(binding) {
        launchOnState(Lifecycle.State.STARTED) {
            viewModel.loadReviews(args.productId).collect {
                reviewAdapter.submitData(it)
            }
        }
        launchOnState(Lifecycle.State.STARTED) {
            reviewAdapter.loadStateFlow.collect {
                when (it.refresh) {
                    is LoadState.NotLoading -> {
                        stopLoading()
                    }
                    LoadState.Loading -> {
                        startLoading()
                    }
                    is LoadState.Error -> {}
                }
            }
        }
    }

    private fun startLoading() {
        binding.fragmentReviewsLottie.apply {
            this.startLoading()
            isVisible = true
        }
    }

    private fun stopLoading() {
        binding.fragmentReviewsLottie.apply {
            this.stopLoading()
            isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.fragmentReviewsList.adapter = null
        _binding = null
    }

    override fun navigateToConnectionFailed() {

    }
}