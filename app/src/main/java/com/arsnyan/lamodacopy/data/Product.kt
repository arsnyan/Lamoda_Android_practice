package com.arsnyan.lamodacopy.data

import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class ProductDto(
    @SerialName("id") val id: Int, @SerialName("brand") val brand: Int,
    @SerialName("category") val category: Int, @SerialName("description") var description: String?
)

data class Product(
    val id: Int, val brand: Brand, val category: Category, var desc: String?,
    var variations: List<ProductVariation>
)

interface ProductRepository {
    suspend fun getProducts(filters: Map<String, Any>? = null, filtersVariations: Map<String, Any>? = null): List<Product>
    suspend fun getProduct(id: Int): Product
}

class ProductRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository,
    private val productVariationRepository: ProductVariationRepository
) : ProductRepository {
    override suspend fun getProducts(filters: Map<String, Any>?, filtersVariations: Map<String, Any>?): List<Product> {
        return withContext(Dispatchers.IO) {
            postgrest["products"].select { limit(10); filters?.forEach { (k, v) -> eq(k, v) } }.decodeList<ProductDto>().map { dto ->
                Product(
                    id = dto.id,
                    brand = brandRepository.getBrand(dto.brand),
                    category = categoryRepository.getCategory(dto.category),
                    desc = dto.description,
                    variations = productVariationRepository.getVariationsByProduct(dto.id, filtersVariations)
                )
            }
        }
    }

    override suspend fun getProduct(id: Int): Product {
        return withContext(Dispatchers.IO) {
            val dto: ProductDto = postgrest["products"].select {
                eq("id", id)
            }.decodeSingle()

            Product(
                id = dto.id,
                brand = brandRepository.getBrand(dto.brand),
                category = categoryRepository.getCategory(dto.category),
                desc = dto.description,
                variations = productVariationRepository.getVariationsByProduct(dto.id)
            )
        }
    }
}