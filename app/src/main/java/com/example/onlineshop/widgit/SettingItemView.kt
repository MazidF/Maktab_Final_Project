package com.example.onlineshop.widgit

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.onlineshop.R
import com.example.onlineshop.databinding.SettingItemBinding

class SettingItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : LinearLayout(context, attrs) {
    private var hasSwitch: Boolean
    private val binding: SettingItemBinding

    init {
        val view = inflate(context, R.layout.setting_item, this)
        binding = SettingItemBinding.bind(view)

        val attribute = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView)

        val text = attribute.getString(R.styleable.SettingItemView_text) ?: ""
        setText(text)

        val src = attribute.getResourceId(R.styleable.SettingItemView_iconSrc, 0)
        setSrc(src)

        hasSwitch = attribute.getBoolean(R.styleable.SettingItemView_hasSwitch, false)
        setupSwitch(hasSwitch)

        val hide = attribute.getBoolean(R.styleable.SettingItemView_hide, false)
        binding.settingItemBtn.isVisible = hide.not()

        val color = attribute.getColor(R.styleable.SettingItemView_tint, Color.BLACK)
        setTint(color)

        attribute.recycle()
    }

    private fun setupSwitch(switch: Boolean) = with(binding) {
        settingItemBtn.isVisible = switch.not()
        settingItemSwitch.isVisible = switch
    }

    private fun setTint(color: Int) = with(binding) {
        settingItemIcon.setColorFilter(color)
        settingItemText.setTextColor(color)
    }

    private fun setSrc(src: Int) {
        binding.settingItemIcon.setImageResource(src)
    }

    private fun setText(text: String) {
        binding.settingItemText.text = text
    }

    override fun setOnClickListener(l: OnClickListener?) {
        if (hasSwitch) {
            binding.settingItemSwitch.setOnClickListener(l)
            return
        }
        binding.settingItemBtn.setOnClickListener(l)
        binding.settingItemText.setOnClickListener(l)
        binding.settingItemIcon.setOnClickListener(l)
    }

    fun setIsChecked(value: Boolean) {
        binding.settingItemSwitch.apply {
            isChecked = value
        }
    }

    fun isChecked(): Boolean? {
        return if (hasSwitch) {
            binding.settingItemSwitch.isChecked
        } else {
            null
        }
    }

}