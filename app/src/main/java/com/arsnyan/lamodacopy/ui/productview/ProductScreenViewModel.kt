package com.arsnyan.lamodacopy.ui.productview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.Color
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductRepository
import com.arsnyan.lamodacopy.data.ProductVariation
import com.arsnyan.lamodacopy.data.Size
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    val variations: List<ProductVariation>
        get() = product.value!!.variations

    private val _currentId = MutableStateFlow(0)
    val currentId: StateFlow<Int> = _currentId

    init {
        savedStateHandle.get<Int>("product_id")?.let { id ->
            getProduct(id = id)
        }
    }

    fun selectVariation(color: Color, size: Size) {
        viewModelScope.launch {
            val foundVariation = variations.indexOf(variations.find { it.color == color && it.size == size }!!)
            _currentId.emit(foundVariation)
        }
    }

    private fun getProduct(id: Int) {
        viewModelScope.launch {
            val result = productRepository.getProduct(id)
            _product.emit(result)
        }
    }
}