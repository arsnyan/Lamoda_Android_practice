package com.arsnyan.lamodacopy.data

import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class SizeDto(
    @SerialName("id") val id: Int, @SerialName("size_ru") val sizeRu: Int?, @SerialName("size_int") val sizeInt: String?,
    @SerialName("bust") val bust: String?, @SerialName("waist") val waist: String?, @SerialName("hips") val hips: String?,
    @SerialName("size_eu") val sizeEu: Int?, @SerialName("size_uk") val sizeUk: Int?, @SerialName("size_us") val sizeUs: Int?
) {
    fun asDomainModel(): Size {
        return Size(
            id = this.id,
            sizeRu = this.sizeRu,
            sizeInt = this.sizeInt,
            bust = this.bust,
            waist = this.waist,
            hips = this.hips,
            sizeEu = this.sizeEu,
            sizeUk = this.sizeUk,
            sizeUs = this.sizeUs
        )
    }
}

data class Size(
    val id: Int, val sizeRu: Int?, val sizeInt: String?, val bust: String?, val waist: String?, val hips: String?,
    val sizeEu: Int?, val sizeUk: Int?, val sizeUs: Int?
)

interface SizeRepository {
    suspend fun getSizes(): List<SizeDto>
    suspend fun getSize(id: Int): SizeDto
}

class SizeRepositoryImpl @Inject constructor(private val postgrest: Postgrest) : SizeRepository {
    override suspend fun getSizes(): List<SizeDto> {
        return withContext(Dispatchers.IO) {
            postgrest["sizes"].select().decodeList()
        }
    }

    override suspend fun getSize(id: Int): SizeDto {
        return withContext(Dispatchers.IO) {
            postgrest["sizes"].select { eq("id", id) }.decodeSingle()
        }
    }
}
