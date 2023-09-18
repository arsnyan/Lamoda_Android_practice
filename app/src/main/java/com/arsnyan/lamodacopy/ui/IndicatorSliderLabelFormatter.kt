package com.arsnyan.lamodacopy.ui

import com.google.android.material.slider.LabelFormatter

class IndicatorSliderLabelFormatter : LabelFormatter {
    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }
}