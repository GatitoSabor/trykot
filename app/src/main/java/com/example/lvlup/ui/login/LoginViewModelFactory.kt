package com.example.lvlup.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lvlup.data.AuthDataStore
import com.example.lvlup.network.ProductoApiService
import com.example.lvlup.repository.UserRepository

class LoginViewModelFactory(
    private val repo: UserRepository,
    private val apiService: ProductoApiService,
    private val authDataStore: AuthDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Pasar las tres dependencias
            return LoginViewModel(repo, apiService, authDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}