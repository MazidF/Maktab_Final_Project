package com.example.onlineshop.ui.fragments.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.onlineshop.databinding.LoadingItemBinding

class LoadingAdapter : LoadStateAdapter<LoadingAdapter.LoadingHolder>() {

    class LoadingHolder(
        private val binding: LoadingItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(state: LoadState) = with(binding) {
            when(state) {
                LoadState.Loading -> {
                    loadingItemProgressBar.isVisible = false
                }
                else -> {
                    loadingItemProgressBar.isVisible = false
                }
            }
        }
    }

    override fun onBindViewHolder(holder: LoadingHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingHolder {
        val binding = LoadingItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return LoadingHolder(binding)
    }
}
