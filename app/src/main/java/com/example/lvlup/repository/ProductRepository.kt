package com.example.lvlup.repository

import com.example.lvlup.data.ProductEntity
import com.example.lvlup.data.ProductDao
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    fun getProducts(): Flow<List<ProductEntity>> = productDao.getProducts()

    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> = productDao.getProductsByCategory(category)

    suspend fun insertAllDemo(productos: List<ProductEntity>) {
        productDao.insertAll(*productos.toTypedArray())
    }
}
