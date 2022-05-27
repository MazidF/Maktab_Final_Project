package com.example.onlineshop.data.remote.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.onlineshop.data.model.Product
import com.example.onlineshop.utils.STARTING_PAGE_INDEX
import com.example.onlineshop.utils.result.SafeApiCall

abstract class RemoteProductPagingSource : PagingSource<Int, Product>() {

    abstract suspend fun getData(page: Int, loadSize: Int): SafeApiCall<List<Product>>

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPos ->
            state.closestPageToPosition(anchorPos)?.run {
                prevKey?.plus(1) ?: nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val key = params.key ?: STARTING_PAGE_INDEX
        return try {
            val safeApiCall = getData(key, params.loadSize)
            if (safeApiCall.isSuccessful) {
                val data = (safeApiCall as SafeApiCall.Success).body()
                LoadResult.Page(
                    data = data,
                    prevKey = if (key == STARTING_PAGE_INDEX) null else key.minus(1),
                    nextKey = if (data.isEmpty()) null else key.plus(1),
                    // TODO: check nextKey
                )
            } else {
                (safeApiCall as SafeApiCall.Fail).throwException()
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        fun getPager(
            config: PagingConfig,
            producer: suspend (Int, Int) -> SafeApiCall<List<Product>>
        ): Pager<Int, Product> {
            return Pager(
                config = config,
            ) {
                object : RemoteProductPagingSource() {
                    override suspend fun getData(page: Int, loadSize: Int): SafeApiCall<List<Product>> {
                        return producer(page, loadSize)
                    }
                }
            }
        }
    }
}