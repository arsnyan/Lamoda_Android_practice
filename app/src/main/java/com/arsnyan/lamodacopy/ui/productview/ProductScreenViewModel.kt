package com.arsnyan.lamodacopy.ui.productview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    val product: Flow<Product?> = _product

    private val _randomProducts = MutableStateFlow<List<Product>?>(listOf())
    val randomProducts: Flow<List<Product>?> = _randomProducts

    private val _similarByBrand = MutableStateFlow<List<Product>?>(listOf())
    val similarByBrand: Flow<List<Product>?> = _similarByBrand

    private val _similarVisually = MutableStateFlow<List<Product>?>(listOf())
    val similarVisually: Flow<List<Product>?> = _similarVisually

    private val _similarByColor = MutableStateFlow<List<Product>?>(listOf())
    val similarByColor: Flow<List<Product>?> = _similarByColor

    private val _currentVariation = MutableStateFlow(0)
    val currentVariation: StateFlow<Int> = _currentVariation

    val selectedSizeItem = MutableLiveData(-1)

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
            currentVariation.collect {
                getRandomProducts(result, it)
                getSimilarByBrand(result, it)
                getSimilarVisually(result, it)
                getSimilarByColor(result, it)
            }
        }
    }

    private suspend fun getRandomProducts(product: Product, varId: Int) {
        val result = productRepository.getProducts(
            filters = mapOf("category" to product.category.id),
            filtersVariations = mapOf("size_id" to product.variations[varId].size.id)
        ).shuffled()
        _randomProducts.emit(result)
    }

    private suspend fun getSimilarByBrand(product: Product, varId: Int) {
        val result = productRepository.getProducts(
            filters = mapOf("brand" to product.brand.id, "category" to product.category.id),
            filtersVariations = mapOf("size_id" to product.variations[varId].size.id)
        ).shuffled()
        _similarByBrand.emit(result)
    }

    private suspend fun getSimilarVisually(product: Product, varId: Int) {
        val result = productRepository.getProducts(
            filters = mapOf("category" to product.category.id),
            filtersVariations = mapOf("pattern" to product.variations[varId].pattern, "size_id" to product.variations[varId].size.id)
        ).shuffled()
        _similarVisually.emit(result)
    }

    private suspend fun getSimilarByColor(product: Product, varId: Int) {
        val result = productRepository.getProducts(
            filters = mapOf("category" to product.category.id),
            filtersVariations = mapOf("color" to product.variations[varId].color, "size_id" to product.variations[varId].size.id)
        ).shuffled()
        _similarByColor.emit(result)
    }
}