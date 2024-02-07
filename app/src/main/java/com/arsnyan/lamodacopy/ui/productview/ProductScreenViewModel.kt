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
import com.arsnyan.lamodacopy.data.ProductVariation
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

    val variations: List<ProductVariation>
        get() = product.value!!.variations

    //

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

    /**
     * Starting from now it's really not efficient to get 4 instances of products, even with different queries
     * This means that I might request 4 product sets at the same time, which increases number of queries, which
     * isn't efficient. I think.
     */
//    private suspend fun getRandomProducts(product: Product, varId: Int) {
//        val result = productRepository.getProducts(
//            filters = mapOf("category" to product.category.id),
//            filtersVariations = mapOf("size_id" to product.variations[varId].size.id)
//        ).shuffled()
//        _randomProducts.emit(result)
//        //Log.i("LAMODA_RANDOM", result.toString())
//    }
//
//    private suspend fun getSimilarByBrand(product: Product, varId: Int) {
//        val result = productRepository.getProducts(
//            filters = mapOf("brand" to product.brand.id, "category" to product.category.id),
//            filtersVariations = mapOf("size_id" to product.variations[varId].size.id)
//        ).shuffled()
//        _similarByBrand.emit(result)
//        //Log.i("LAMODA_BYBRAND", result.toString())
//    }
//
//    private suspend fun getSimilarVisually(product: Product, varId: Int) {
//        val result = productRepository.getProducts(
//            filters = mapOf("category" to product.category.id),
//            filtersVariations = mapOf("pattern" to product.variations[varId].pattern, "size_id" to product.variations[varId].size.id)
//        ).shuffled()
//        _similarVisually.emit(result)
//        //Log.i("LAMODA_VISUALLY", result.toString())
//    }
//
//    private suspend fun getSimilarByColor(product: Product, varId: Int) {
//        val result = productRepository.getProducts(
//            filters = mapOf("category" to product.category.id),
//            filtersVariations = mapOf("color" to product.variations[varId].color, "size_id" to product.variations[varId].size.id)
//        ).shuffled()
//        _similarByColor.emit(result)
//        //Log.i("LAMODA_BYCOLOR", result.toString())
//    }
}