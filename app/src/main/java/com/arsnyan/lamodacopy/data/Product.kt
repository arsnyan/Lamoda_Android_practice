package com.arsnyan.lamodacopy.data

import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date
import javax.inject.Inject

@Serializable
data class ProductDto(
    @SerialName("id") val id: Long, @SerialName("brand") val brand: Int, @SerialName("category") val category: Int,
    @SerialName("urls") val urls: List<String>, @SerialName("current_price") var currentPrice: Int,
    @SerialName("original_price") var originalPrice: Int, @SerialName("apply_club_discount") val applyClubDiscount: Boolean,
    @SerialName("sizes") var sizes: List<Int>, @SerialName("available_items") var availableItems: Int,
    @SerialName("created_at") var date: String, @SerialName("color") var color: Color,
    @SerialName("pattern") var pattern: Pattern, @SerialName("vendor_id") var vendorId: String?
)

data class Product(
    val id: Long, val brand: Brand?, val category: Category?,
    val urls: List<String>, var currentPrice: Int, var originalPrice: Int,
    val applyClubDiscount: Boolean, var sizes: List<Size>, var availableItems: Int, var date: Date,
    var color: Color, var pattern: Pattern, var vendorId: String?
)

interface ProductRepository {
    suspend fun getProducts(): List<ProductDto>?
    suspend fun getProduct(id: Int): ProductDto
}

class ProductRepositoryImpl @Inject constructor(private val postgrest: Postgrest) : ProductRepository {
    override suspend fun getProducts(): List<ProductDto> {
        return withContext(Dispatchers.IO) {
            postgrest["products"].select {
                limit(10)
            }.decodeList()
        }
    }

    override suspend fun getProduct(id: Int): ProductDto {
        return withContext(Dispatchers.IO) {
            postgrest["products"].select { eq("id", id) }.decodeSingle()
        }
    }
}
