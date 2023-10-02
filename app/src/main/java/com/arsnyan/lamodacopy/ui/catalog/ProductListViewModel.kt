package com.arsnyan.lamodacopy.ui.catalog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductDto
import com.arsnyan.lamodacopy.data.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val productRepository: ProductRepository) : ViewModel() {
    private val _productList = MutableStateFlow<List<Product>?>(listOf())
    val productList: Flow<List<Product>?> = _productList

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            val products = productRepository.getProducts()
            Log.d("SOMEEEE", products.toString())
            _productList.emit(products?.map { it.asDomainModel() })
        }
    }
}