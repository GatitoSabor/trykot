package com.example.lvlup.ui.home

import ProductListViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lvlup.data.DatabaseProvider
import com.example.lvlup.repository.ProductRepository

class ProductListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = DatabaseProvider.getInstance(context)
        val productRepo = ProductRepository(db.productDao())
        return ProductListViewModel(productRepo) as T
    }
}
