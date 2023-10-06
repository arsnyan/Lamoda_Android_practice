package com.arsnyan.lamodacopy.ui.catalog

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.Brand
import com.arsnyan.lamodacopy.data.BrandRepository
import com.arsnyan.lamodacopy.data.Category
import com.arsnyan.lamodacopy.data.CategoryRepository
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductRepository
import com.arsnyan.lamodacopy.data.Size
import com.arsnyan.lamodacopy.data.SizeRepository
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
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository,
    private val sizeRepository: SizeRepository
) : ViewModel() {

    private val _brandList = MutableStateFlow<List<Brand>?>(listOf())
    val brandList: Flow<List<Brand>?> = _brandList
    private val _categoryList = MutableStateFlow<List<Category>?>(listOf())
    val categoryList: Flow<List<Category>?> = _categoryList
    private val _sizeList = MutableStateFlow<List<Size>?>(listOf())
    val sizeList: Flow<List<Size>?> = _sizeList
    private val _productList = MutableStateFlow<List<Product>?>(listOf())
    val productList: Flow<List<Product>?> = _productList

    init {
        getBrands()
        getCategories()
        getSizes()
        getProducts()
    }

    private fun getBrands() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val brands = brandRepository.getBrands()
            Log.d("BRANDS", brands.toString())
            _brandList.emit(brands.map { it.asDomainModel() })
        }
    }

    private fun getCategories() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val categories = categoryRepository.getCategories()
            Log.d("CATEGORIES", categories.toString())
            _categoryList.emit(categories.map { it.asDomainModel() })
        }
    }

    private fun getSizes() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val sizes = sizeRepository.getSizes()
            Log.d("SIZES", sizes.toString())
            _sizeList.emit(sizes.map { it.asDomainModel() })
        }
    }

    private fun getProducts() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val products = productRepository.getProducts()
            Log.d("SOMEEEE", products.toString())
            _productList.emit(products?.map { it.asDomainModel(_brandList.value!!, _categoryList.value!!, _sizeList.value!!) })
        }
    }
}