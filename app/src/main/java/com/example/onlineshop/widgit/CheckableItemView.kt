package com.example.onlineshop.widgit

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.RadioButton
import com.example.onlineshop.R
import com.example.onlineshop.databinding.CheckableItemBinding
import com.example.onlineshop.utils.setup

class CheckableItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : FrameLayout(context, attrs) {
    private val binding: CheckableItemBinding

    init {
        val view = inflate(context, R.layout.checkable_item, this)
        binding = CheckableItemBinding.bind(view).apply {
            checkableItemBtn.setup(false)
        }

        val attribute = context.obtainStyledAttributes(attrs, R.styleable.CheckableItemView)

        val text = attribute.getString(R.styleable.CheckableItemView_itemText) ?: ""
        setText(text)

        attribute.recycle()
    }

    private fun setText(text: String) {
        binding.checkableItemText.text = text
    }

    override fun setOnClickListener(l: OnClickListener?) = with(binding.checkableItemBtn) {
        if (l != null) {
            setup(isChecked) {
                l.onClick(this)
            }
        } else {
            setup(isChecked) {}
        }
    }

    fun setIsChecked(value: Boolean) {
        binding.checkableItemBtn.apply {
            isSelected = value
            isChecked = value
        }
    }

    fun isChecked() = binding.checkableItemBtn.isChecked

    fun getRadioButton() = binding.checkableItemBtn
}
