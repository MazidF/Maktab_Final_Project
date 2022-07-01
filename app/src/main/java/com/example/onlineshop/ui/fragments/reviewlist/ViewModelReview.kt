package com.example.onlineshop.ui.fragments.reviewlist

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.onlineshop.data.model.ProductReview
import com.example.onlineshop.data.repository.ShopRepository
import com.example.onlineshop.data.result.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewModelReview @Inject constructor(
    private val repository: ShopRepository,
) : ViewModel() {

    fun loadReviews(productId: Long): Flow<PagingData<ProductReview>> {
        return repository.getReviewOfProduct(productId)
    }

    fun createReview(
        productId: Long,
        review: String,
        rate: Int,
        customerName: Boolean = true,
    ): Flow<Resource<ProductReview>> {
        return repository.createReview(
            ProductReview(
                id = 0,
                date = "",
                rating = rate,
                review = review,
                verified = false,
                productId = productId,
                email = repository.customer.email,
                reviewer = if (customerName) repository.customer.username else "کاربر ناشناس",
            )
        )
    }
}
