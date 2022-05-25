package com.example.onlineshop.ui.fragments

import androidx.recyclerview.widget.DiffUtil
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.ui.model.ProductListItem
import com.example.onlineshop.utils.clazz
import com.example.onlineshop.widgit.Refreshable

class ProductDiffItemCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}

class ProductItemDiffItemCallback : DiffUtil.ItemCallback<ProductListItem.Item>() {
    override fun areItemsTheSame(oldItem: ProductListItem.Item, newItem: ProductListItem.Item): Boolean {
        return oldItem.product.id == newItem.product.id
    }

    override fun areContentsTheSame(oldItem: ProductListItem.Item, newItem: ProductListItem.Item): Boolean {
        return oldItem.product == newItem.product
    }
}

class ProductListItemDiffItemCallback : DiffUtil.ItemCallback<ProductListItem>() {
    override fun areItemsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
        return oldItem.clazz() == newItem.clazz() && when (oldItem) {
            is ProductListItem.Footer, is ProductListItem.Header -> true
            is ProductListItem.Item -> oldItem.product.id == (newItem as ProductListItem.Item).product.id
        }
    }

    override fun areContentsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
        return when (oldItem) {
            is ProductListItem.Footer, is ProductListItem.Header -> true
            is ProductListItem.Item -> oldItem.product == (newItem as ProductListItem.Item).product
        }
    }
}