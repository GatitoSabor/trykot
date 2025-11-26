package com.example.lvlup.data

import android.content.Context
import androidx.room.Room
// ⚠️ IMPORTACIONES REQUERIDAS:
import com.example.lvlup.repository.ProductRepository
import com.example.lvlup.network.RetrofitClient

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    @Volatile
    private var PRODUCT_REPO_INSTANCE: ProductRepository? = null

    fun getInstance(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "appdb"
            ).build().also { INSTANCE = it }
        }

    /**
     * Provee el singleton del ProductRepository.
     * Asegura que AppDatabase esté inicializada y le pasa sus dependencias (DAO y API).
     */
    fun getProductRepository(context: Context): ProductRepository =
        PRODUCT_REPO_INSTANCE ?: synchronized(this) {
            val appDatabase = getInstance(context) // Aseguramos que la base de datos esté lista

            PRODUCT_REPO_INSTANCE ?: ProductRepository(
                productDao = appDatabase.productDao(), // Obtiene el DAO de Room
                apiService = RetrofitClient.productoApiService // Obtiene el servicio API de Retrofit
            ).also { PRODUCT_REPO_INSTANCE = it }
        }
}