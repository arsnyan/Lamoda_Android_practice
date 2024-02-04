package com.arsnyan.lamodacopy.data

import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class BrandDto(@SerialName("id") val id: Int, @SerialName("name") val name: String, @SerialName("logo") val logo: String) {
    fun asDomainModel(): Brand {
        return Brand(
            id = this.id,
            name = this.name,
            logo = this.logo
        )
    }
}

data class Brand(val id: Int, val name: String, val logo: String)

interface BrandRepository {
    suspend fun getBrands(): List<Brand>
    suspend fun getBrand(id: Int): Brand
}

class BrandRepositoryImpl @Inject constructor(private val postgrest: Postgrest) : BrandRepository {
    override suspend fun getBrands(): List<Brand> {
        return withContext(Dispatchers.IO) {
            val dtos: List<BrandDto> = postgrest["brands"].select().decodeList()
            val finalList = mutableListOf<Brand>()
            dtos.forEach { dto ->
                finalList += Brand(id = dto.id, name = dto.name, logo = dto.logo)
            }
            finalList
        }
    }

    override suspend fun getBrand(id: Int): Brand {
        return withContext(Dispatchers.IO) {
            val dto: BrandDto = postgrest["brands"].select { eq("id", id) }.decodeSingle()
            Brand(id = dto.id, name = dto.name, logo = dto.logo)
        }
    }
}
