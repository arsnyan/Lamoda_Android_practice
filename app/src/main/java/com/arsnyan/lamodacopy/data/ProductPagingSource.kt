package com.arsnyan.lamodacopy.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState

class ProductPagingSource(
    private val productRepository: ProductRepository,
    private val productFilters: Map<String, Any>,
    private val variationFilters: Map<String, Any>
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val nextPage = params.key ?: 1
            val response = productRepository.getProducts(productFilters, variationFilters, nextPage, params.loadSize)
            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (response.isEmpty()) null else nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}