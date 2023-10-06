package com.arsnyan.lamodacopy.data

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class ProductDto(
    @SerialName("id") val id: Long, @SerialName("brand") val brand: Int, @SerialName("category") val category: Int,
    @SerialName("urls") val urls: List<String>, @SerialName("current_price") var currentPrice: Int, @SerialName("original_price") var originalPrice: Int,
    @SerialName("sizes") var sizes: List<Int>, @SerialName("available_items") var availableItems: Int?,
    @SerialName("created_at") var timestamptz: String
) {
    fun asDomainModel(brandList: List<Brand>, categoryList: List<Category>, sizesList: List<Size>): Product {
        Log.d("BRANDS_DTO", this.brand.toString())
        Log.d("BRAND_DTO_TIMESTAMP", this.timestamptz)

        return Product(
            id = this.id,
            brand = brandList.find { brand -> brand.id == this.brand },
            category = categoryList.find { category -> category.id == this.category },
            urls = this.urls,
            currentPrice = this.currentPrice,
            originalPrice = this.originalPrice,
            sizes = this.sizes.mapNotNull { sizeId -> sizesList.find { size -> size.id == sizeId } },
            availableItems = this.availableItems
        )
    }
}

data class Product(
    val id: Long, val brand: Brand?, val category: Category?,
    val urls: List<String>, var currentPrice: Int, var originalPrice: Int,
    var sizes: List<Size>, var availableItems: Int?
)

interface ProductRepository {
    suspend fun getProducts(): List<ProductDto>?
    suspend fun getProduct(id: Int): ProductDto
}

class ProductRepositoryImpl @Inject constructor(private val postgrest: Postgrest, private val client: SupabaseClient) : ProductRepository {
    override suspend fun getProducts(): List<ProductDto> {
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
