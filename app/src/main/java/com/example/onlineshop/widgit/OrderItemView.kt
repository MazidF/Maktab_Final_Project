package com.example.onlineshop.widgit

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.onlineshop.R
import com.example.onlineshop.databinding.OrderItemBinding
import com.example.onlineshop.ui.model.OrderItem
import com.example.onlineshop.utils.loadImageInto

class OrderItemView @JvmOverloads constructor(
    context: Context,
) : LinearLayout(context), Bindable<OrderItem> {
    private var lines: List<View>
    private var images: List<ImageView>
    private val binding: OrderItemBinding

    init {
        val view = inflate(context, R.layout.order_item, this)
        binding = OrderItemBinding.bind(view)
        images = with(binding) {
            listOf(
                orderItemImg1,
                orderItemImg2,
                orderItemImg3,
                orderItemImg4,
            )
        }
        lines = with(binding) {
            listOf(
                orderItemLine1,
                orderItemLine2,
                orderItemLine3,
            )
        }
    }

    override fun bind(t: OrderItem?): Unit = with(binding) {
        t?.let { item ->
            orderItemDate.text = item.date
            orderItemId.text = item.id.toString()
            orderItemCost.text = item.total
            orderItemProducts.apply {
                val list = item.lineItems
                val size = if (list.size < 4) list.size else 4
                repeat(size) {
                    loadImageInto(list[it].product.imageUrl, images[it].also { imageView ->
                        imageView.isVisible = true
                    })
                    lines[it].isVisible = true
                }
                repeat(4 - size) {
                    images[it].isVisible = false
                    lines[it].isVisible = false
                }
            }
        }
    }

    override fun getView(): View {
        return this
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.orderItemProductsRoot.setOnClickListener(l)
    }
}