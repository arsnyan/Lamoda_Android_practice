package com.arsnyan.lamodacopy.ui.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.arsnyan.lamodacopy.R
import com.google.android.material.card.MaterialCardView

class ProfileSectionCardView constructor(
    context: Context,
    attrs: AttributeSet
): MaterialCardView(context, attrs) {
    private val iconView: ImageView
    private val labelView:TextView
    private val valueView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_cardview_profile_section, this, true)

        iconView = findViewById(R.id.profile_cardview_icon)
        labelView = findViewById(R.id.profile_cardview_label)
        valueView = findViewById(R.id.profile_cardview_value)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProfileSectionCardView)
        iconView.setImageResource(typedArray.getResourceId(R.styleable.ProfileSectionCardView_icon, 0))
        labelView.text = typedArray.getString(R.styleable.ProfileSectionCardView_label)
        valueView.text = typedArray.getInt(R.styleable.ProfileSectionCardView_value, 0).toString()
        typedArray.recycle()
    }

    fun setValue(value: Int) {
        valueView.text = value.toString()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (Integer.parseInt(valueView.text as String) <= 0)
            valueView.visibility = INVISIBLE
    }
}