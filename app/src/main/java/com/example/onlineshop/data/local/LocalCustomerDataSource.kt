package com.example.onlineshop.data.local

import android.content.Context
import com.example.onlineshop.utils.readObjectFromFile
import com.example.onlineshop.utils.writeObjectOnFile
import kotlinx.coroutines.CoroutineDispatcher

class LocalCustomerDataSource(
    private val dispatcher: CoroutineDispatcher,
    private val context: Context,
) : ILocalDataSource {

    companion object {
        const val CART_FILE_NAME = "ProductCart.json"
    }

    override suspend fun loadCartMap(): HashMap<Long, Int> {
        return readObjectFromFile(
            fileName = CART_FILE_NAME,
            root = context.filesDir
        ) ?: hashMapOf()
    }

    override suspend fun saveCartMap(map: HashMap<Long, Int>) {
        writeObjectOnFile(
            any = map,
            fileName = CART_FILE_NAME,
            root = context.filesDir
        )
    }
}