package com.example.onlineshop.ui.fragments.productinfo.review

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.data.model.ProductReview
import com.example.onlineshop.databinding.BigProductReviewBinding
import com.example.onlineshop.databinding.SmallProductReviewBinding
import com.example.onlineshop.ui.fragments.adapter.diff_callback.ProductReviewDiffItemCallback
import com.example.onlineshop.ui.model.Rate
import com.example.onlineshop.utils.setHtmlText

class ProductReviewAdapter(
    private val onItemClick: (ProductReview) -> Unit,
) : ListAdapter<ProductReview, ProductReviewAdapter.ProductReviewHolder>(
    ProductReviewDiffItemCallback()
) {
    inner class ProductReviewHolder(
        private val binding: SmallProductReviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var productReview: ProductReview? = null

        init {
            binding.root.setOnClickListener {
                productReview?.let {
                    if (it.productId != -1L) {
                        it.let(onItemClick)
                    }
                }
            }
        }

        fun bind(item: ProductReview) = with(binding) {
            productReview = item
            smallProductReviewName.text = item.reviewer
            smallProductReviewText.setHtmlText(item.review)
            smallProductReviewDate.text = item.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewHolder {
        val binding = SmallProductReviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ProductReviewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductReviewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
