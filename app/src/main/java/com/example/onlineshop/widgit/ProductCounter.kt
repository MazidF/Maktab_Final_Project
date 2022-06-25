package com.example.onlineshop.widgit

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.getBooleanOrThrow
import androidx.core.view.isVisible
import com.example.onlineshop.R
import com.example.onlineshop.data.result.Resource
import com.example.onlineshop.databinding.ProductCounterBinding
import kotlinx.coroutines.flow.Flow
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class ProductCounter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : ConstraintLayout(context, attrs) {
    private var onCountChangeListener: ((Int) -> Unit)? = null
    private val binding: ProductCounterBinding
    private var count = 0

    private var counterInput = CounterInput.NONE
        set(value) {
            when (value) {
                CounterInput.ADD,
                CounterInput.SUBTRACT -> {
                    onClick?.invoke(count + value.value)
                }
            }
            field = value
        }
    private val withLoading: Boolean
    private var isLoading = false
        set(value) {
            binding.productCounterLottie.isVisible = value
            binding.productCounterCount.isVisible = value.not()
            field = value
        }

    private var onClick: ((Int) -> Unit)? = null

    inline fun TypedArray.use(block: TypedArray.() -> Unit) {
        this.block()
        this.recycle()
    }

    init {
        val view = inflate(context, R.layout.product_counter, this)
        binding = ProductCounterBinding.bind(view)

        context.obtainStyledAttributes(attrs, R.styleable.ProductCounter).run {
            withLoading = this.getBoolean(R.styleable.ProductCounter_withLoading, false)
            this.recycle()
        }

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
        if (withLoading) {
            if (isLoading.not()) {
                counterInput = CounterInput.ADD
                startLoading()
            }
        } else {
            setupCount(count + 1)
        }
    }

    private fun remove() {
        if (withLoading) {
            if (isLoading.not()) {
                counterInput = CounterInput.ADD
                startLoading()
            }
        } else {
            setupCount(count - 1)
        }
    }

    fun setupCount(count: Int) = with(binding) {
        this@ProductCounter.count = count
        when (count) {
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

    fun <T> setLoadingResult(result: Resource<T>) {
        if (withLoading.not()) return
        if (result is Resource.Fail<T>) {
            stopLoading()
        } else if (result is Resource.Success<T>) {
            setupCount(count + counterInput.value)
            stopLoading()
        }
    }

    fun setLoadingResult(newCount: Int) {
        if (withLoading.not()) return
        setupCount(newCount)
        stopLoading()
    }

    fun setOnCountChangeListener(block: ((Int) -> Unit)?) {
        this.onCountChangeListener = block
    }

    private fun startLoading() = with(binding) {
        isLoading = true
    }

    private fun stopLoading() = with(binding) {
        counterInput = CounterInput.NONE
        isLoading = false
    }

    fun setOnClick(onClick: ((Int) -> Unit)?) {
        this.onClick = onClick
    }

    private enum class CounterInput(
        val value: Int
    ) {
        ADD(1),
        SUBTRACT(-1),
        NONE(0);
    }

}