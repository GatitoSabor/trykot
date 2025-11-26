package com.example.lvlup.ui.home

import ProductListViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lvlup.data.DatabaseProvider
import com.example.lvlup.repository.ProductRepository

class ProductListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            // ⚠️ CORRECCIÓN CLAVE: Obtener el ProductRepository completamente configurado
            // desde el DatabaseProvider, que ya inyecta el ProductDao y el ProductoApiService.
            val repo = DatabaseProvider.getProductRepository(context)

            @Suppress("UNCHECKED_CAST")
            return ProductListViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}