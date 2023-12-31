package com.arsnyan.lamodacopy.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class CategoryDto(@SerialName("id") val id: Int, @SerialName("name") val name: String,
                       @SerialName("gender") val gender: String) {
    fun asDomainModel(): Category = Category(id = this.id, name = this.name, gender = this.gender)
}

data class Category(val id: Int, val name: String, val gender: String)

interface CategoryRepository {
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getCategory(id: Int): CategoryDto
}

class CategoryRepositoryImpl @Inject constructor(private val postgrest: Postgrest, @ApplicationContext private val context: Context) : CategoryRepository {
    override suspend fun getCategories(): List<CategoryDto> {
        return withContext(Dispatchers.IO) {
            if (context.resources.configuration.locales[0].language == "ru")
                postgrest["categories_ru"].select().decodeList()
            else
                postgrest["categories"].select().decodeList()
        }
    }

    override suspend fun getCategory(id: Int): CategoryDto {
        return withContext(Dispatchers.IO) {
            if (context.resources.configuration.locales[0].language == "ru")
                postgrest["categories_ru"].select { eq("id", id) }.decodeSingle()
            else
                postgrest["categories"].select { eq("id", id) }.decodeSingle()
        }
    }
}