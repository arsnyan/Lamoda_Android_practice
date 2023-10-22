package com.arsnyan.lamodacopy.utils

import android.view.View

object ExtensionFunctions {
    /**
     * Changes view's interactivity based on [allowInteraction]
     */
    fun View.interactive(allowInteraction: Boolean) {
        isFocusable = allowInteraction
        isClickable = allowInteraction
        isEnabled = allowInteraction
    }
}