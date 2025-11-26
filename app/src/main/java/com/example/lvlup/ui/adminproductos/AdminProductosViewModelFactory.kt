package com.example.lvlup.ui.adminproductos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lvlup.data.DatabaseProvider

class AdminProductosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminProductosViewModel::class.java)) {
            // Obtener el ProductRepository desde el DatabaseProvider (que ya tiene el API Service)
            val repository = DatabaseProvider.getProductRepository(context)

            @Suppress("UNCHECKED_CAST")
            return AdminProductosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}