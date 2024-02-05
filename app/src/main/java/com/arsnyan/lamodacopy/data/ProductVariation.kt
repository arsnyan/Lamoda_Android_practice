package com.arsnyan.lamodacopy.data

import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import javax.inject.Inject

@Serializable
data class ProductVariationDto(
    @SerialName("id") val id: Int, @SerialName("product_id") val productId: Int,
    @SerialName("size_id") val sizeId: Int, @SerialName("color") val color: Color,
    @SerialName("pattern") val pattern: Pattern, @SerialName("original_price") val originalPrice: Int,
    @SerialName("current_price") var currentPrice: Int, @SerialName("stock") var stock: Int,
    @SerialName("urls") val urls: List<String>, @SerialName("use_club") var useClub: Boolean,
    @SerialName("created") val createdAt: String
)

data class ProductVariation(
    val id: Int, val productId: Int, val size: Size, val color: Color, val pattern: Pattern,
    val originalPrice: Int, var currentPrice: Int, var stock: Int, val urls: List<String>,
    var useClub: Boolean, val createdAt: ZonedDateTime
)

interface ProductVariationRepository {
    suspend fun getVariation(id: Int): ProductVariation

    suspend fun getVariationsByProduct(productId: Int, filters: Map<String, Any>? = null): List<ProductVariation>
}

class ProductVariationRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val sizeRepository: SizeRepository
) : ProductVariationRepository {
    override suspend fun getVariation(id: Int): ProductVariation {
        return withContext(Dispatchers.IO) {
            val dto: ProductVariationDto = postgrest["product_variations"].select { eq("id", id) }.decodeSingle()
            ProductVariation(
                id = dto.id,
                productId = dto.productId,
                size = sizeRepository.getSize(dto.sizeId),
                color = dto.color,
                pattern = dto.pattern,
                originalPrice = dto.originalPrice,
                currentPrice = dto.currentPrice,
                stock = dto.stock,
                urls = dto.urls,
                useClub = dto.useClub,
                createdAt = ZonedDateTime.parse(dto.createdAt)
            )
        }
    }

    override suspend fun getVariationsByProduct(productId: Int, filters: Map<String, Any>?): List<ProductVariation> {
        return withContext(Dispatchers.IO) {
            println("GET VARIATION BY PRODUCT")
            val dtos: List<ProductVariationDto> = postgrest["product_variations"].select {
                eq("product_id", productId)
                filters?.forEach { (key, value) ->
                    eq(key, value)
                }
            }.decodeList()
            dtos.map { dto ->
                ProductVariation(
                    id = dto.id,
                    productId = dto.productId,
                    size = sizeRepository.getSize(dto.sizeId),
                    color = dto.color,
                    pattern = dto.pattern,
                    originalPrice = dto.originalPrice,
                    currentPrice = dto.currentPrice,
                    stock = dto.stock,
                    urls = dto.urls,
                    useClub = dto.useClub,
                    createdAt = ZonedDateTime.parse(dto.createdAt)
                )
            }
        }
    }
}