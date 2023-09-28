package com.arsnyan.lamodacopy.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.databinding.ProfileSectionCardViewBinding
import com.google.android.material.card.MaterialCardView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSectionCardView constructor(
    context: Context,
    attrs: AttributeSet
): MaterialCardView(context, attrs) {
    private var binding: ProfileSectionCardViewBinding

    init {
        binding = ProfileSectionCardViewBinding.inflate(LayoutInflater.from(context), this, true)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProfileSectionCardView)
        binding.profileCardviewIcon.setImageResource(typedArray.getResourceId(R.styleable.ProfileSectionCardView_icon, 0))
        binding.profileCardviewLabel.text = typedArray.getString(R.styleable.ProfileSectionCardView_label)
        binding.profileCardviewValue.text = typedArray.getInt(R.styleable.ProfileSectionCardView_value, 0).toString()
        typedArray.recycle()
    }

    fun setValue(value: Int) {
        binding.profileCardviewValue.text = value.toString()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (Integer.parseInt(binding.profileCardviewValue.text as String) <= 0)
            binding.profileCardviewValue.visibility = INVISIBLE
    }
}