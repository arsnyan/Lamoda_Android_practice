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
    suspend fun getBrands(): List<BrandDto>
    suspend fun getBrand(id: Int): BrandDto
}

class BrandRepositoryImpl @Inject constructor(private val postgrest: Postgrest) : BrandRepository {
    override suspend fun getBrands(): List<BrandDto> {
        return withContext(Dispatchers.IO) {
            postgrest["brands"].select().decodeList()
        }
    }

    override suspend fun getBrand(id: Int): BrandDto {
        return withContext(Dispatchers.IO) {
            postgrest["brands"].select { eq("id", id) }.decodeSingle()
        }
    }
}
