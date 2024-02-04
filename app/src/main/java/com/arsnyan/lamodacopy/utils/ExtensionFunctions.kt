package com.arsnyan.lamodacopy.utils

import android.content.Context
import android.graphics.Paint
import android.icu.util.Calendar
import android.view.View
import android.widget.TextView
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductVariation
import java.time.ZonedDateTime
import java.util.Date

object ExtensionFunctions {
    fun ZonedDateTime.haveDaysPassed(days: Int): Boolean {
        return ZonedDateTime.now() == this.plusDays(30)
    }

    /**
     * Changes view's interactivity based on [allowInteraction]
     */
    fun View.interactive(allowInteraction: Boolean) {
        isFocusable = allowInteraction
        isClickable = allowInteraction
        isEnabled = allowInteraction
    }

    /**
     * Used to clean repetition in discount visibility toggling code
     * [context] is used to get a placeholder for the discount badge
     * [discountClubBadgeView] is optional, since it's used widely
     */
    fun Product.setDiscountVisibility(context: Context,
                                               originalPriceView: TextView, currentPriceView: TextView,
                                               discountBadgeView: TextView, discountClubBadgeView: TextView? = null) {
        val originalPrice = this.variations.maxBy { v -> v.originalPrice }.originalPrice
        val currentPrice = this.variations.minBy { v -> v.currentPrice }.currentPrice
        originalPriceView.text = originalPrice.toString()
        currentPriceView.text = context.getString(R.string.lbl_formatted_price, currentPrice)
        if(originalPrice > currentPrice) {
            originalPriceView.apply {
                paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
            currentPriceView.apply {
                setTextColor(resources.getColor(R.color.branded_flame, resources.newTheme()))
            }
            val discount = 100 - ((currentPrice * 100)/originalPrice)
            discountBadgeView.text = context.getString(R.string.discount_size_placeholder, discount)
        } else {
            originalPriceView.visibility = View.GONE
            discountBadgeView.visibility = View.GONE
            discountClubBadgeView?.visibility = View.GONE
        }
    }
}