package com.arsnyan.lamodacopy.ui.catalog

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.Category
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _productList = MutableStateFlow<List<Product>?>(listOf())
    val productList: Flow<List<Product>?> = _productList

    var category: Category? = null

    init {
        savedStateHandle.get<Int>("category_id")?.let { id ->
            Log.i("Category ID", id.toString())
            getProducts(mapOf("category" to id))
        }
    }

    private fun getProducts(filters: Map<String, Any>) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val products = productRepository.getProducts(filters = filters)
            if (products.isNotEmpty())
                category = products[0].category
            _productList.emit(products)
        }
    }
}