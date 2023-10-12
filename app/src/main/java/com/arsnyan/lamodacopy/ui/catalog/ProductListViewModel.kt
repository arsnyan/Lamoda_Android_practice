package com.arsnyan.lamodacopy.ui.catalog

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.BrandRepository
import com.arsnyan.lamodacopy.data.CategoryRepository
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductDto
import com.arsnyan.lamodacopy.data.ProductRepository
import com.arsnyan.lamodacopy.data.SizeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository,
    private val sizeRepository: SizeRepository
) : ViewModel() {
    private val _productList = MutableStateFlow<List<Product>?>(listOf())
    val productList: Flow<List<Product>?> = _productList

    init {
        getProducts()
    }

    private fun getProducts() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val products = productRepository.getProducts()
            Log.d("SOMEEEE", products.toString())
            _productList.emit(products?.map { it.asDomainModel() })
        }
    }

    private suspend fun ProductDto.asDomainModel(): Product {
        Log.d("BRANDS_DTO", this.brand.toString())
        Log.d("BRAND_DTO_TIMESTAMP", this.date)

        return Product(
            id = this.id,
            brand = brandRepository.getBrand(this.brand).asDomainModel(),
            category = categoryRepository.getCategory(this.category).asDomainModel(),
            urls = this.urls,
            currentPrice = this.currentPrice,
            originalPrice = this.originalPrice,
            applyClubDiscount = this.applyClubDiscount,
            sizes = sizeRepository.getSizesByIdList(this.sizes),
            availableItems = this.availableItems,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(this.date),
            color = this.color,
            pattern = this.pattern,
            vendorId = this.vendorId
        )
    }
}