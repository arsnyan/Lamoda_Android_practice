package com.arsnyan.lamodacopy.ui.productview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ProductScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: Flow<Product?> = _product
    private val _currentVariation = MutableStateFlow<Int>(0)
    val currentVariation: StateFlow<Int> = _currentVariation

    val selectedSizeItem = MutableLiveData<Int>(-1)

    init {
        savedStateHandle.get<Int>("product_id")?.let { id ->
            getProduct(id = id)
        }
    }

    fun selectItem(itemId: Int) {
        selectedSizeItem.value = itemId
    }

    suspend fun selectVariation(variationListId: Int) {
        _currentVariation.emit(variationListId)
    }

    private fun getProduct(id: Int) {
        viewModelScope.launch {
            val result = productRepository.getProduct(id)
            _product.emit(result)
        }
    }
}