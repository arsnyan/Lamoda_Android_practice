package com.arsnyan.lamodacopy.ui.catalog

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arsnyan.lamodacopy.data.Category
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductPagingSource
import com.arsnyan.lamodacopy.data.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    lateinit var productList: Flow<PagingData<Product>>

    var category: Category? = null

    init {
        savedStateHandle.get<Int>("category_id")?.let { id ->
            productList = Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = {
                    ProductPagingSource(productRepository, mapOf("category" to id), emptyMap())
                }
            ).flow.cachedIn(viewModelScope)
        }
    }

    // TODO (Change a function to retrieve category name from id in savedStateHandle)
//    private fun getProducts(filters: Map<String, Any>) = viewModelScope.launch {
//        withContext(Dispatchers.IO) {
//            val products = productRepository.getProducts(productFilters = filters)
//            if (products.isNotEmpty())
//                category = products[0].category
//            _productList.emit(products)
//        }
//    }
}