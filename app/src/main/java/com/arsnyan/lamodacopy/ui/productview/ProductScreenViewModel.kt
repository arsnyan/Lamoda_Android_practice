package com.arsnyan.lamodacopy.ui.productview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
class ProductScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: Flow<Product?> = _product

    init {
        savedStateHandle.get<Int>("product_id")?.let {
            getProduct(id = it)
        }
    }

    private fun getProduct(id: Int) {
        viewModelScope.launch {
            //val result = productRepository.getProduct(id).asDomainModel()
            //_product.emit(result)
        }
    }
}