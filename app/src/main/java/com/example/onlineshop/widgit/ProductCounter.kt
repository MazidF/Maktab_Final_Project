package com.example.onlineshop.widgit

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.onlineshop.R
import com.example.onlineshop.databinding.ProductCounterBinding

class ProductCounter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : ConstraintLayout(context, attrs) {
    private var onCountChangeListener: ((Int) -> Unit)? = null
    private val binding: ProductCounterBinding
    private var count = 0

    init {
        val view = inflate(context, R.layout.product_counter, this)
        binding = ProductCounterBinding.bind(view)

        initView()
    }

    private fun initView() = with(binding) {
        productCounterAdd.setOnClickListener {
            add()
        }
        productCounterRemove.setOnClickListener {
            remove()
        }
        productCounterBtn.setOnClickListener {
            add()
        }
    }

    private fun add() {
        setupCount(count + 1)
    }

    private fun remove() {
        setupCount(count - 1)
    }

    fun setupCount(count: Int) = with(binding) {
        this@ProductCounter.count = count
        when(count) {
            0 -> {
                productCounterRoot.isVisible = false
            }
            1 -> {
                productCounterRoot.isVisible = true
                productCounterRemove.setImageResource(R.drawable.ic_delete)
            }
            else -> {
                productCounterRoot.isVisible = true
                productCounterRemove.setImageResource(R.drawable.ic_remove)
            }
        }
        productCounterCount.text = count.toString()
        onCountChangeListener?.invoke(count)
    }

    fun setOnCountChangeListener(block: ((Int) -> Unit)?) {
        this.onCountChangeListener = block
    }

}