package com.example.onlineshop.ui.fragments

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.utils.loadImageInto
import com.example.onlineshop.databinding.SimpleVerticalProductItemBinding

class ProductPagingAdapter : PagingDataAdapter<Product, ProductPagingAdapter.ProductHolder>(ProductDiffItemCallback()) {

    inner class ProductHolder(
        // TODO: replace with bindable
        private val binding: SimpleVerticalProductItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product?) {
            with(binding) {
                if (item != null) {
                    loadImageInto(item.imageUrl, simpleVerticalProductImage)
                    simpleVerticalProductName.text = item.name
                    simpleVerticalProductCost.text = item.price
                    with(item.isOnSale()) {
                        if (this) {
                            simpleVerticalProductRealCost.text = item.regularPrice
                        }
                        simpleVerticalProductRealCost.isVisible = this
                        simpleVerticalProductDiscountPercent.isVisible = this
                        simpleVerticalProductDiscountPercentHolder.isVisible = this
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val binding = SimpleVerticalProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ProductHolder(binding)
    }
}

