package com.example.lvlup.ui.adminproductos

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lvlup.data.DatabaseProvider

class AdminProductosViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = DatabaseProvider.getInstance(context)
        return AdminProductosViewModel(db.productDao()) as T
    }
}
