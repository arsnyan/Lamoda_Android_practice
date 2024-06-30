package com.arsnyan.lamodacopy.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
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
    suspend fun getCategories(filters: Map<String, Any>? = null): List<Category>
    suspend fun getCategory(id: Int): Category
}

class CategoryRepositoryImpl @Inject constructor(private val postgrest: Postgrest, @ApplicationContext private val context: Context) : CategoryRepository {
    override suspend fun getCategories(filters: Map<String, Any>?): List<Category> {
        return withContext(Dispatchers.IO) {
            val dtos: List<CategoryDto> = when (context.resources.configuration.locales[0].language) {
                "ru" -> postgrest["categories_ru"].select {
                    filter {
                        filters?.forEach { (k, v) -> eq(k, v) }
                    }
                }.decodeList()
                else -> postgrest["categories"].select {
                    filter{
                        filters?.forEach { (k, v) -> eq(k, v) }
                    }
                }.decodeList()
            }

            dtos.map {
                it.asDomainModel()
            }
        }
    }

    override suspend fun getCategory(id: Int): Category {
        return withContext(Dispatchers.IO) {
            val dto: CategoryDto = when (context.resources.configuration.locales[0].language) {
                "ru" -> postgrest["categories_ru"].select {
                    filter{
                        eq("id", id)
                    }
                }.decodeSingle()
                else -> postgrest["categories"].select {
                    filter{
                        eq("id", id)
                    }
                }.decodeSingle()
            }
            dto.asDomainModel()
        }
    }
}