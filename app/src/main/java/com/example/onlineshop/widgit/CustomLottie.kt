package com.example.onlineshop.widgit

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.example.onlineshop.R
import com.example.onlineshop.databinding.CustomLottieBinding

class CustomLottie @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : FrameLayout(context, attrs) {
    private val binding: CustomLottieBinding
    private var resource: Int

    init {
        val view = inflate(context, R.layout.custom_lottie, this)
        binding = CustomLottieBinding.bind(view)

        context.obtainStyledAttributes(attrs, R.styleable.CustomLottie).apply {
            resource = this.getResourceId(R.styleable.CustomLottie_lottie_rawRes, R.raw.three_dots_loading)
            recycle()
        }

        initView()
    }

    private fun initView() = with(binding) {
        lottie.apply {
            setAnimation(resource)
        }
    }

    fun startLoading(resource: Int = R.raw.three_dots_loading) {
        binding.lottie.apply {
            if (resource != this@CustomLottie.resource) {
                this@CustomLottie.resource = resource
                setAnimation(resource)
            }
            playAnimation()
        }
        this.isVisible = true
    }

    fun stopLoading() {
        this.isVisible = false
        binding.lottie.pauseAnimation()
    }

}