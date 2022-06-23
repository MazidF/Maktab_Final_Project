package com.example.onlineshop.data.local

interface ILocalDataSource {
    suspend fun loadCartMap(): HashMap<Long, Int>

    suspend fun saveCartMap(map: HashMap<Long, Int>)
}
