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
    @SerialName("id") val id: Long, @SerialName("brand") val brand: String, @SerialName("category") val category: String,
    @SerialName("urls") val urls: Array<String>, @SerialName("current_price") var currentPrice: Int, @SerialName("original_price") var originalPrice: Int,
    @SerialName("sizes") var sizes: String, @SerialName("available_items") var availableItems: Int?
    //@SerialName("attributes") var attributes: Map<String, String>? = null
) {
    fun asDomainModel(): Product {
        return Product(
            id = this.id,
            brand = this.brand,
            category = this.category,
            urls = this.urls,
            currentPrice = this.currentPrice,
            originalPrice = this.originalPrice,
            sizes = this.sizes,
            //attributes = this.attributes
        )
    }
}

data class Product(
    val id: Long, val brand: String, val category: String,
    val urls: Array<String>, var currentPrice: Int, var originalPrice: Int,
    var sizes: String//, var attributes: Map<String, String>? = null
)

interface ProductRepository {
    suspend fun getProducts(): List<ProductDto>?
    suspend fun getProduct(id: Int): ProductDto
}

class ProductRepositoryImpl @Inject constructor(private val postgrest: Postgrest, private val client: SupabaseClient) : ProductRepository {
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
