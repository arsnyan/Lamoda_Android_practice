package com.arsnyan.lamodacopy.ui.productview

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.Color
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductRepository
import com.arsnyan.lamodacopy.data.Size
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    //

    private val _randomProducts = MutableStateFlow<List<Product>?>(listOf())
    val randomProducts: Flow<List<Product>?> = _randomProducts

    private val _similarByBrand = MutableStateFlow<List<Product>?>(listOf())
    val similarByBrand: Flow<List<Product>?> = _similarByBrand

    private val _similarVisually = MutableStateFlow<List<Product>?>(listOf())
    val similarVisually: Flow<List<Product>?> = _similarVisually

    private val _similarByColor = MutableStateFlow<List<Product>?>(listOf())
    val similarByColor: Flow<List<Product>?> = _similarByColor

    //

    val sizesByColor = MutableLiveData<List<Size>>(listOf())
    val selectedColor = MutableLiveData<Color>(Color.MULTICOLOR)
    val selectedSize = MutableLiveData<Size?>(null)
    val currentVariationId = MutableLiveData<Int>(0)

    init {
        savedStateHandle.get<Int>("product_id")?.let { id ->
            getProduct(id = id)
        }
    }

    fun selectSize(size: Size) {
        viewModelScope.launch {
            selectedSize.value = size
            selectVariation(selectedColor.value!!, size)
        }
    }

    fun selectColor(color: Color) {
        viewModelScope.launch {
            selectedColor.value = color
            updateSizeList(color)
            val currentSize = selectedSize.value
            val defaultSize = sizesByColor.value!![0]
            if (!sizesByColor.value!!.contains(currentSize)) {
                selectedSize.value = defaultSize
            } else {
                selectVariation(color, currentSize ?: defaultSize)
            }
        }
    }

    private suspend fun updateSizeList(color: Color? = null) {
        val filteredByColor = product.value?.variations?.filter { v -> v.color == (color ?: selectedColor.value) }
        sizesByColor.value = filteredByColor?.map { it.size } ?: listOf()
    }

    private suspend fun selectVariation(color: Color, size: Size) {
        Log.i("Current variation", currentVariationId.value.toString())
        val foundVariation = product.value?.variations?.find { it.color == color && it.size == size }
        Log.i("Found variation", foundVariation?.id.toString())
        currentVariationId.value = (foundVariation?.id?.minus(1)) ?: 0
    }

    private fun getProduct(id: Int) {
        viewModelScope.launch {
            val result = productRepository.getProduct(id)
            _product.emit(result)
            selectedSize.value = result.variations[0].size
            selectedColor.value = result.variations[0].color
            updateSizeList()

            //currentVariationId.observe(lifecycleOwner) {
                getRandomProducts(result, currentVariationId.value!!)
                getSimilarByBrand(result, currentVariationId.value!!)
                getSimilarVisually(result, currentVariationId.value!!)
                getSimilarByColor(result, currentVariationId.value!!)
            //}
        }
    }

    private suspend fun getRandomProducts(product: Product, varId: Int) {
        val result = productRepository.getProducts(
            filters = mapOf("category" to product.category.id),
            filtersVariations = mapOf("size_id" to product.variations[varId].size.id)
        ).shuffled()
        _randomProducts.emit(result)
        //Log.i("LAMODA_RANDOM", result.toString())
    }

    private suspend fun getSimilarByBrand(product: Product, varId: Int) {
        val result = productRepository.getProducts(
            filters = mapOf("brand" to product.brand.id, "category" to product.category.id),
            filtersVariations = mapOf("size_id" to product.variations[varId].size.id)
        ).shuffled()
        _similarByBrand.emit(result)
        //Log.i("LAMODA_BYBRAND", result.toString())
    }

    private suspend fun getSimilarVisually(product: Product, varId: Int) {
        val result = productRepository.getProducts(
            filters = mapOf("category" to product.category.id),
            filtersVariations = mapOf("pattern" to product.variations[varId].pattern, "size_id" to product.variations[varId].size.id)
        ).shuffled()
        _similarVisually.emit(result)
        //Log.i("LAMODA_VISUALLY", result.toString())
    }

    private suspend fun getSimilarByColor(product: Product, varId: Int) {
        val result = productRepository.getProducts(
            filters = mapOf("category" to product.category.id),
            filtersVariations = mapOf("color" to product.variations[varId].color, "size_id" to product.variations[varId].size.id)
        ).shuffled()
        _similarByColor.emit(result)
        //Log.i("LAMODA_BYCOLOR", result.toString())
    }
}