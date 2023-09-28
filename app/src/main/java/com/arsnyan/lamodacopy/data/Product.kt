package com.arsnyan.lamodacopy.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class ProductDto(
    @SerialName("id") val id: Int, @SerialName("brand") val brand: String, @SerialName("category") val category: String,
    @SerialName("urls") val urls: List<String>?, @SerialName("current_price") var currentPrice: Int, @SerialName("original_price") var originalPrice: Int,
    @SerialName("available_items") var availableItems: Int, @SerialName("sizes") var sizes: Map<String, String>,
    @SerialName("attributes") var attributes: Map<String, String>? = null
)

data class Product(
    val id: Int, val brand: String, val category: String,
    val urls: List<String>?, var currentPrice: Int, var originalPrice: Int,
    var sizes: Map<String, String>, var attributes: Map<String, String>? = null
)

interface ProductRepository {
    suspend fun getProducts(): List<ProductDto>?
    suspend fun getProduct(id: Int): ProductDto
}

class ProductRepositoryImpl @Inject constructor(private val postgrest: Postgrest) : ProductRepository {
    override suspend fun getProducts(): List<ProductDto>? {
        return withContext(Dispatchers.IO) {
            postgrest["products"].select().decodeList()
        }
    }

    override suspend fun getProduct(id: Int): ProductDto {
        return withContext(Dispatchers.IO) {
            postgrest["products"].select { eq("id", id) }.decodeSingle()
        }
    }
}
