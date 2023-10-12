package com.arsnyan.lamodacopy.ui.productview

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arsnyan.lamodacopy.data.BrandRepository
import com.arsnyan.lamodacopy.data.CategoryRepository
import com.arsnyan.lamodacopy.data.Product
import com.arsnyan.lamodacopy.data.ProductDto
import com.arsnyan.lamodacopy.data.ProductRepository
import com.arsnyan.lamodacopy.data.SizeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProductScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository,
    private val sizeRepository: SizeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _product = MutableStateFlow<Product?>(null)
    val product: Flow<Product?> = _product

    val selectedSizeItem = MutableLiveData<Int>(-1)

    init {
        savedStateHandle.get<Int>("product_id")?.let {id ->
            getProduct(id = id)
        }
    }

    fun selectItem(itemId: Int) {
        selectedSizeItem.value = itemId
    }

    private fun getProduct(id: Int) {
        viewModelScope.launch {
            val result = productRepository.getProduct(id).asDomainModel()
            _product.emit(result)
        }
    }

    private suspend fun ProductDto.asDomainModel(): Product {
        Log.d("BRANDS_DTO", this.brand.toString())
        Log.d("BRAND_DTO_TIMESTAMP", this.date)

        return Product(
            id = this.id,
            brand = brandRepository.getBrand(this.brand).asDomainModel(),
            category = categoryRepository.getCategory(this.category).asDomainModel(),
            urls = this.urls,
            currentPrice = this.currentPrice,
            originalPrice = this.originalPrice,
            applyClubDiscount = this.applyClubDiscount,
            sizes = sizeRepository.getSizesByIdList(this.sizes),
            availableItems = this.availableItems,
            date = SimpleDateFormat("yyyy-MM-dd", Locale.UK).parse(this.date),
            color = this.color,
            pattern = this.pattern,
            vendorId = this.vendorId
        )
    }
}