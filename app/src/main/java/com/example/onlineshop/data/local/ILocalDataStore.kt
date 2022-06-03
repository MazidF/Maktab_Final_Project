package com.example.onlineshop.data.local

interface ILocalDataStore {
    suspend fun loadCartMap(): HashMap<Long, Int>

    suspend fun saveCartMap(map: HashMap<Long, Int>)
}
