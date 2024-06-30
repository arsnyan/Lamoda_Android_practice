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
    @SerialName("feet_length") val feetLength: Float?, @SerialName("size_eu") val sizeEu: Int?,
    @SerialName("size_uk") val sizeUk: Int?, @SerialName("size_us") val sizeUs: Int?
) {
    fun asDomainModel() = Size(
        id = this.id,
        sizeRu = this.sizeRu,
        sizeInt = this.sizeInt,
        bust = this.bust,
        waist = this.waist,
        hips = this.hips,
        feetLength = this.feetLength,
        sizeEu = this.sizeEu,
        sizeUk = this.sizeUk,
        sizeUs = this.sizeUs
    )
}

data class Size(
    val id: Int, val sizeRu: Int?, val sizeInt: String?, val bust: String?, val waist: String?, val hips: String?,
    val feetLength: Float?, val sizeEu: Int?, val sizeUk: Int?, val sizeUs: Int?
)

interface SizeRepository {
    suspend fun getSizes(): List<Size>
    suspend fun getSize(id: Int): Size
    suspend fun getSizesByIdList(ids: List<Int>): List<Size>
}

class SizeRepositoryImpl @Inject constructor(private val postgrest: Postgrest) : SizeRepository {
    override suspend fun getSizes(): List<Size> {
        return withContext(Dispatchers.IO) {
            val dtos: List<SizeDto> = postgrest["sizes"].select().decodeList()
            val finalList = mutableListOf<Size>()
            dtos.forEach { dto -> finalList += dto.asDomainModel() }
            finalList
        }
    }

    override suspend fun getSize(id: Int): Size {
        return withContext(Dispatchers.IO) {
            val dto: SizeDto = postgrest["sizes"].select {
                filter {
                    eq("id", id)
                }
            }.decodeSingle()
            dto.asDomainModel()
        }
    }

    override suspend fun getSizesByIdList(ids: List<Int>): List<Size> {
        val list = mutableListOf<Size>()
        return withContext(Dispatchers.IO) {
            for (id in ids) {
                list.add(postgrest["sizes"].select {
                    filter {
                        eq("id", id)
                    }
                }.decodeSingle<SizeDto>().asDomainModel())
            }
            list
        }
    }
}
