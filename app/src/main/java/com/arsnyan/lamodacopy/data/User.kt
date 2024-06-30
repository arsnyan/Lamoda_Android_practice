package com.arsnyan.lamodacopy.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.ktor.util.Identity.decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class UserDto(@SerialName("id") val id: Int, @SerialName("name") val name: String, @SerialName("logo") val logo: String) {
    fun asDomainModel(): User {
        return User(
            id = this.id,
            name = this.name,
            logo = this.logo
        )
    }
}

data class User(val id: Int, val name: String, val logo: String)

interface UserRepository {
    suspend fun getBrands(): List<Brand>
    suspend fun getBrand(id: Int): Brand
}

class UserRepositoryImpl @Inject constructor(private val postgrest: Postgrest) : UserRepository {
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
            val dto: BrandDto = postgrest["brands"].select {
                filter { eq("id", id) }
            }.decodeSingle()
            Brand(id = dto.id, name = dto.name, logo = dto.logo)
        }
    }
}