package com.arsnyan.lamodacopy.data

import java.io.Serializable

class Product(
    val id: String, val brand: String, val category: String, val urls: List<String>?, var currentPrice: Int, var originalPrice: Int,
    var availableItems: Int, var sizes: Map<String, String>?, var attributes: Map<String, Any>? = null
) : Serializable {
    constructor():this("", "", "", null, 0, 0, 0, null)

    override fun toString(): String {
        return "$id, $brand, $category, ${urls.toString()}, $currentPrice, $originalPrice, $availableItems, ${sizes.toString()}, ${attributes.toString()}"
    }
}
