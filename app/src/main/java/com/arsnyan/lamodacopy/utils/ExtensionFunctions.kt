package com.arsnyan.lamodacopy.utils

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import com.arsnyan.lamodacopy.R
import com.arsnyan.lamodacopy.data.Product
import java.time.ZonedDateTime

object ExtensionFunctions {
    /**
     * Determines, whether set [days] have passed from this moment.
     * For example, have 30 days passed since start date till now?
     */
    fun ZonedDateTime.haveDaysPassed(days: Int): Boolean {
        return ZonedDateTime.now().isAfter(this.plusDays(days.toLong()))
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
     * Used to clean repetition in discount visibility toggling code.
     * [context] is used to get a placeholder for the discount badge.
     * [discountClubBadgeView] is optional, since it's used widely.
     */
    fun Product.setDiscountVisibility(context: Context,
                                      currentVariationId: Int,
                                      originalPriceView: TextView, currentPriceView: TextView,
                                      discountBadgeView: TextView, discountClubBadgeView: TextView? = null) {
        val originalPrice = this.variations[currentVariationId].originalPrice
        val currentPrice = this.variations[currentVariationId].currentPrice

        with (context) {
            originalPriceView.text = originalPrice.toString()
            currentPriceView.text = getString(R.string.lbl_formatted_price, currentPrice)
            if(originalPrice > currentPrice) {
                originalPriceView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                currentPriceView.setTextColor(resources.getColor(R.color.branded_flame, resources.newTheme()))
                val discount = 100 - ((currentPrice * 100)/originalPrice)
                discountBadgeView.text = getString(R.string.discount_size_placeholder, discount)

                /**
                 * In order for new visibility rules set up in action, we have to set them manually
                 * on true condition
                 */
                originalPriceView.visibility = View.VISIBLE
                discountBadgeView.visibility = View.VISIBLE
                discountClubBadgeView?.visibility = View.VISIBLE
            } else {
                originalPriceView.visibility = View.GONE
                discountBadgeView.visibility = View.GONE
                discountClubBadgeView?.visibility = View.GONE
                currentPriceView.setTextColor(resources.getColor(R.color.black, resources.newTheme()))
            }
        }
    }
}