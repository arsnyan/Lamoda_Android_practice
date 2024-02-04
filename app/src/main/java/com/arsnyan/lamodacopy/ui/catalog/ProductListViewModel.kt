package com.arsnyan.lamodacopy.ui.catalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.BrandRepository
import com.arsnyan.lamodacopy.data.CategoryRepository
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductDto
import com.arsnyan.lamodacopy.data.ProductRepository
import com.arsnyan.lamodacopy.data.ProductVariation
import com.arsnyan.lamodacopy.data.ProductVariationDto
import com.arsnyan.lamodacopy.data.ProductVariationRepository
import com.arsnyan.lamodacopy.data.SizeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _productList = MutableStateFlow<List<Product>?>(listOf())
    val productList: Flow<List<Product>?> = _productList

    init {
        getProducts()
    }

    private fun getProducts() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val products = productRepository.getProducts()
            _productList.emit(products)
        }
    }
}